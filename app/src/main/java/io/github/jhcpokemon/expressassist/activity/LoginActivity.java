package io.github.jhcpokemon.expressassist.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.jhcpokemon.expressassist.R;
import io.github.jhcpokemon.expressassist.model.ExpressLog;
import io.github.jhcpokemon.expressassist.model.ExpressLogProvider;
import io.github.jhcpokemon.expressassist.util.UtilPack;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnLongClickListener, GoogleApiClient.OnConnectionFailedListener {

    /**
     * UI
     */
    @Bind(R.id.email)
    AutoCompleteTextView mEmailView;
    @Bind(R.id.password)
    EditText mPasswordView;
    @Bind(R.id.save_email_pw)
    CheckBox saveEmailAndPwCB;
    @Bind(R.id.auto_login)
    CheckBox autoLoginCB;
    @Bind(R.id.email_register_button)
    Button mEmailRegisterButton;
    @Bind(R.id.email_log_in_button)
    Button mEmailLogInButton;
    @Bind(R.id.gms_btn)
    SignInButton gmsButton;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private GoogleApiClient client;
    private static final int RC_GMS_SIGNIN = 28;
    public static ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ExpressLog log = new ExpressLog("", "", "", "");
        log.save();
        log.delete();
        mSharedPreferences = getSharedPreferences("user_msg", MODE_APPEND);
        mEditor = mSharedPreferences.edit();
        if (mSharedPreferences.getAll().isEmpty()) {
            mEditor.putString("email", "");
            mEditor.putString("password", "");
            mEditor.putBoolean("save", false);
            mEditor.putBoolean("auto", false);
            mEditor.apply();
        }
        mEmailRegisterButton.setOnClickListener(this);
        mEmailRegisterButton.setOnLongClickListener(this);
        mEmailLogInButton.setOnClickListener(this);
        saveEmailAndPwCB.setOnCheckedChangeListener(this);
        autoLoginCB.setOnCheckedChangeListener(this);
        saveEmailAndPwCB.setChecked(mSharedPreferences.getBoolean("save", false));
        autoLoginCB.setChecked(mSharedPreferences.getBoolean("auto", false));
        gmsButton.setOnClickListener(this);

        if (saveEmailAndPwCB.isChecked()) {
            mEmailView.setText(mSharedPreferences.getString("email", ""));
            mPasswordView.setText(mSharedPreferences.getString("password", ""));
            if (autoLoginCB.isChecked() && UtilPack.valid(mEmailView.getText().toString(), mPasswordView.getText().toString())) {
                startActivity(offLineLoginIntent());
            }
        }

        googleSignInPrepare();
        imageLoaderInit();
    }


    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_register_button:
                String email;
                String password;
                if (mSharedPreferences.getString("email", "").equals("")) {
                    email = mEmailView.getText().toString();
                    password = mPasswordView.getText().toString();
                    switch (UtilPack.statusCode(email)) {
                        case 0:
                            if (!password.equals("")) {
                                mEditor.putString("email", email);
                                mEditor.putString("password", password);
                                mEditor.putBoolean("save", saveEmailAndPwCB.isChecked());
                                mEditor.putBoolean("auto", autoLoginCB.isChecked());
                                mEditor.apply();
                                startActivity(offLineLoginIntent());
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.input_pw, Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 1:
                            Toast.makeText(getApplicationContext(), R.string.error_invalid_email, Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                    mEditor.apply();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_retry, Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.email_log_in_button:
                if (UtilPack.valid(mEmailView.getText().toString(), mPasswordView.getText().toString())
                        && mPasswordView.getText().toString().equals(mSharedPreferences.getString("password", ""))) {
                    startActivity(offLineLoginIntent());
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_retry, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.gms_btn:
                googleSignIn();
                break;
            default:
                break;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(client);
        if (opr.isDone()) {
            Log.d(UtilPack.TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GMS_SIGNIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    private Intent offLineLoginIntent() {
        Intent mIntent = new Intent(LoginActivity.this, ContainerActivity.class);
        if (ExpressLogProvider.getLOGS().size() == 0) {
            mIntent.putExtra("empty", true);
        } else {
            mIntent.putExtra("empty", false);
        }
        mIntent.putExtra("mail", mEmailView.getText().toString());
        mIntent.putExtra("local", true);
        return mIntent;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.save_email_pw:
                mEditor.putBoolean("save", saveEmailAndPwCB.isChecked());
                mEditor.apply();
                break;
            case R.id.auto_login:
                mEditor.putBoolean("auto", autoLoginCB.isChecked());
                mEditor.apply();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        startActivity(new Intent(LoginActivity.this, WebRegisterActivity.class));
        return true;
    }

    private void googleSignInPrepare() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        client = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        gmsButton.setScopes(gso.getScopeArray());
        gmsButton.setSize(SignInButton.SIZE_STANDARD);
    }

    private void googleSignIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(client);
        startActivityForResult(intent, RC_GMS_SIGNIN);
    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            Intent intent = offLineLoginIntent();
            intent.putExtra("account", account);
            startActivity(intent);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void imageLoaderInit() {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(displayImageOptions)
                .build();
        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();
    }
}

