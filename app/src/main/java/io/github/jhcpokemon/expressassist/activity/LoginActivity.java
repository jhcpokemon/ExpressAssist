package io.github.jhcpokemon.expressassist.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.jhcpokemon.expressassist.R;
import io.github.jhcpokemon.expressassist.model.ExpressLog;
import io.github.jhcpokemon.expressassist.model.ExpressLogProvider;
import io.github.jhcpokemon.expressassist.util.UtilPack;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnLongClickListener {

    /**
     * UI
     */
    @Bind(R.id.email)
    AutoCompleteTextView mEmailView;
    @Bind(R.id.password)
    EditText mPasswordView;
    //    @Bind(R.id.login_progress)
    //    ProgressBar mProgressView;
    @Bind(R.id.save_email_pw)
    CheckBox saveEmailAndPwCB;
    @Bind(R.id.auto_login)
    CheckBox autoLoginCB;
    @Bind(R.id.email_sign_in_button)
    Button mEmailSignInButton;
    @Bind(R.id.email_log_in_button)
    Button mEmailLogInButton;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

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
        mEmailSignInButton.setOnClickListener(this);
        mEmailSignInButton.setOnLongClickListener(this);
        mEmailLogInButton.setOnClickListener(this);
        saveEmailAndPwCB.setOnCheckedChangeListener(this);
        autoLoginCB.setOnCheckedChangeListener(this);
        saveEmailAndPwCB.setChecked(mSharedPreferences.getBoolean("save", false));
        autoLoginCB.setChecked(mSharedPreferences.getBoolean("auto", false));

        if (saveEmailAndPwCB.isChecked()) {
            mEmailView.setText(mSharedPreferences.getString("email", ""));
            mPasswordView.setText(mSharedPreferences.getString("password", ""));
            if (autoLoginCB.isChecked() && UtilPack.valid(mEmailView.getText().toString(), mPasswordView.getText().toString())) {
                login();
            }
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button:
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
                                login();
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
                    login();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.error_retry, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void login() {
        Intent mIntent = new Intent(LoginActivity.this, ContainerActivity.class);
        if (ExpressLogProvider.getLOGS().size() == 0) {
            mIntent.putExtra("empty", true);
        } else {
            mIntent.putExtra("empty", false);
        }
        startActivity(mIntent);
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
        startActivity(new Intent(LoginActivity.this, WebSignInActivity.class));
        return true;
    }
}

