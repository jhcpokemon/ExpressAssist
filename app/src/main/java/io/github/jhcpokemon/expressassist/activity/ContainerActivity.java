package io.github.jhcpokemon.expressassist.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.concurrent.ExecutionException;

import io.github.jhcpokemon.expressassist.R;
import io.github.jhcpokemon.expressassist.fragment.ExpressItemFragment;
import io.github.jhcpokemon.expressassist.fragment.SearchFragment;
import io.github.jhcpokemon.expressassist.fragment.SettingFragment;
import io.github.jhcpokemon.expressassist.model.ExpressLog;

public class ContainerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ExpressItemFragment.OnListItemClickListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private FragmentManager manager;
    private ExpressItemFragment historyFragment;
    private SearchFragment searchFragment;
    private SettingFragment settingFragment;
    private ImageLoader imageLoader;
    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager = getSupportFragmentManager();
        historyFragment = ExpressItemFragment.newInstance();
        searchFragment = new SearchFragment();
        settingFragment = new SettingFragment();
        historyFragment.setOnItemClickListener(this);
        imageLoader = LoginActivity.imageLoader;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);
        if (!isOnline()) {
            Toast.makeText(getApplicationContext(), R.string.error_network, Toast.LENGTH_LONG).show();
        }

        View header = navigationView.getHeaderView(0);
        RoundedImageView avatarImageView = (RoundedImageView) header.findViewById(R.id.avatar_img);
        TextView mailTextView = (TextView) header.findViewById(R.id.gms_mail_text);
        TextView nameTextView = (TextView) header.findViewById(R.id.gms_name_text);
        Button logoutBtn = (Button) header.findViewById(R.id.gms_log_out_btn);
        logoutBtn.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.getParcelableExtra("account") != null) {
            GoogleSignInAccount account = intent.getParcelableExtra("account");
            try {
                avatarImageView.setImageBitmap(new ImageLoadTask().execute(account.getPhotoUrl().toString()).get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            mailTextView.setText(account.getEmail());
            nameTextView.setText(account.getDisplayName());
        } else {
            avatarImageView.setImageResource(R.drawable.default_avatar);
            mailTextView.setText(intent.getStringExtra("mail"));
            logoutBtn.setVisibility(View.GONE);
            nameTextView.setVisibility(View.GONE);
        }
        if (!intent.getBooleanExtra("empty", false)) {
            manager.beginTransaction().add(R.id.container, historyFragment).commit();
        } else {
            manager.beginTransaction().add(R.id.container, searchFragment).commit();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        client = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        client.connect();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            manager.beginTransaction().replace(R.id.container, searchFragment).commit();
        } else if (id == R.id.nav_history) {
            manager.beginTransaction().replace(R.id.container, historyFragment).commit();
        } else if (id == R.id.nav_setting) {
            manager.beginTransaction().replace(R.id.container, settingFragment).commit();
        } else if (id == R.id.nav_share) {
            Intent mIntent = new Intent();
            mIntent.setAction(Intent.ACTION_SEND);
            mIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_message));
            mIntent.setType("text/plain");
            startActivity(Intent.createChooser(mIntent, getResources().getString(R.string.send_to)));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListItemClicked(ExpressLog mLog) {
        Intent intent = new Intent(ContainerActivity.this, DetailActivity.class);
        intent.putExtra("log", mLog);
        startActivity(intent);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gms_log_out_btn:
                signOut();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        client.disconnect();

    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(client).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                finish();
            }
        });
        ExpressLog.deleteAll(ExpressLog.class);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            return imageLoader.loadImageSync(params[0]);
        }
    }
}
