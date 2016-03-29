package io.github.jhcpokemon.expressassist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.jhcpokemon.expressassist.R;

public class WebSignInActivity extends AppCompatActivity {
    @Bind(R.id.web_view)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_sign_in);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent.getStringExtra("uri") != null) {
            webView.loadUrl(intent.getStringExtra("uri"));
        } else {
            webView.loadUrl("http://jhcpokemon.github.io/others/sign-in.html");
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
