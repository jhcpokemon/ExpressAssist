package io.github.jhcpokemon.expressassist.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        webView.loadUrl("http://jhcpokemon.github.io/others/sign-in.html");
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
