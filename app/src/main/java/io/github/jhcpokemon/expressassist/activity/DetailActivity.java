package io.github.jhcpokemon.expressassist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import io.github.jhcpokemon.expressassist.R;
import io.github.jhcpokemon.expressassist.fragment.SearchFragment;
import io.github.jhcpokemon.expressassist.model.ExpressCompanyProvider;
import io.github.jhcpokemon.expressassist.model.ExpressLog;
import io.github.jhcpokemon.expressassist.util.UtilPack;

public class DetailActivity extends AppCompatActivity {
    @Bind(R.id.web_search)
    WebView webView;
    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        ExpressCompanyProvider expressCompanyProvider = new ExpressCompanyProvider(getApplicationContext());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.CHINA);
        String date = dateFormat.format(Calendar.getInstance().getTime());

        if (intent.getStringExtra("uri") != null) {
            uri = intent.getStringExtra("uri");
            List<String> list = UtilPack.parseUri(uri);
            ExpressLog log;
            if (list.size() == 3) {
                log = new ExpressLog(list.get(1), date, expressCompanyProvider.
                        getCompanyName(list.get(2)), list.get(2));
                log.save();
            } else {
                webView.loadUrl(uri);
                log = new ExpressLog(list.get(0), date, expressCompanyProvider.
                        getCompanyName(list.get(1)), list.get(1));
                log.save();
            }
        } else if (intent.getSerializableExtra("log") != null) {
            ExpressLog log = (ExpressLog) intent.getSerializableExtra("log");
            if (SearchFragment.FOR_WEB_SEARCH.contains(log.name)) {
                uri = SearchFragment.URI + "&com=" + log.code + "&nu=" + log.order;
                webView.loadUrl(uri);
            } else {
                uri = SearchFragment.URI + SearchFragment.UID + "&com=" + log.code + "&nu=" + log.order;
            }
        }


    }
}
