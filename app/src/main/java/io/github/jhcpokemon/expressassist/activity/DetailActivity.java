package io.github.jhcpokemon.expressassist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import io.github.jhcpokemon.expressassist.R;
import io.github.jhcpokemon.expressassist.fragment.SearchFragment;
import io.github.jhcpokemon.expressassist.fragment.adapter.ListAdapter;
import io.github.jhcpokemon.expressassist.model.Data;
import io.github.jhcpokemon.expressassist.model.ExpressCompanyProvider;
import io.github.jhcpokemon.expressassist.model.ExpressDetail;
import io.github.jhcpokemon.expressassist.model.ExpressLog;
import io.github.jhcpokemon.expressassist.util.UtilPack;

public class DetailActivity extends AppCompatActivity {
    @Bind(R.id.web_search)
    WebView webView;
    @Bind(R.id.detail_list)
    ListView listView;
    private List<Data> data = new ArrayList<>();
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        adapter = new ListAdapter(getApplicationContext(), data);
        listView.setAdapter(adapter);
        Intent intent = getIntent();
        ExpressCompanyProvider expressCompanyProvider = new ExpressCompanyProvider(getApplicationContext());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.CHINA);
        String date = dateFormat.format(Calendar.getInstance().getTime());

        String uri;
        if (intent.getStringExtra("uri") != null) {
            uri = intent.getStringExtra("uri");
            List<String> list = UtilPack.parseUri(uri);
            ExpressLog log;
            if (list.size() == 3)//带用户UID的请求，返回Json数据
            {
                log = new ExpressLog(list.get(1), date, expressCompanyProvider.
                        getCompanyName(list.get(2)), list.get(2));
                log.save();
                showExpressMessage(uri);
            } else //加载网页
            {
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(uri);
                log = new ExpressLog(list.get(0), date, expressCompanyProvider.
                        getCompanyName(list.get(1)), list.get(1));
                log.save();
            }
        } else if (intent.getSerializableExtra("log") != null) {
            ExpressLog log = (ExpressLog) intent.getSerializableExtra("log");
            if (SearchFragment.FOR_WEB_SEARCH.contains(log.name)) //加载网页
            {
                webView.setVisibility(View.VISIBLE);
                uri = SearchFragment.URI + "&com=" + log.code + "&nu=" + log.order;
                webView.loadUrl(uri);
            } else {
                //带用户UID的请求，返回Json数据
                uri = SearchFragment.URI + SearchFragment.UID + "&com=" + log.code + "&nu=" + log.order;
                showExpressMessage(uri);
            }
        }
    }

    private void showExpressMessage(String uri) {
        ExpressDetail expressDetail = JSON.parseObject(UtilPack.getJsonData(uri), ExpressDetail.class);
        switch (expressDetail.getStatus()) {
            case "0":
                Toast.makeText(getApplicationContext(), R.string.status_0, Toast.LENGTH_SHORT).show();
                break;
            case "1":
                listView.setVisibility(View.VISIBLE);
                data = expressDetail.getData();
                adapter.notifyDataSetChanged();
                break;
            case "2":
                Toast.makeText(getApplicationContext(), R.string.status_2, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
