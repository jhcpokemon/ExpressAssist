package io.github.jhcpokemon.expressassist.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.jhcpokemon.expressassist.R;
import io.github.jhcpokemon.expressassist.fragment.SearchFragment;
import io.github.jhcpokemon.expressassist.fragment.adapter.ListAdapter;
import io.github.jhcpokemon.expressassist.model.Data;
import io.github.jhcpokemon.expressassist.model.ExpressCompanyProvider;
import io.github.jhcpokemon.expressassist.model.ExpressDetail;
import io.github.jhcpokemon.expressassist.model.ExpressLog;
import io.github.jhcpokemon.expressassist.util.UtilPack;

public class DetailActivity extends AppCompatActivity {
    @Bind(R.id.web_linear_layout)
    LinearLayout linearLayout;
    @Bind(R.id.web_search)
    WebView webView;
    @Bind(R.id.detail_list)
    ListView listView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.web_progress_bar)
    ProgressBar webProgressBar;
    private List<Data> data = new ArrayList<>();
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        adapter = new ListAdapter(this, data);
        listView.setAdapter(adapter);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    webProgressBar.setVisibility(View.GONE);
                } else {
                    webProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);

        Intent intent = getIntent();
        ExpressCompanyProvider expressCompanyProvider = new ExpressCompanyProvider(getApplicationContext());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String date = dateFormat.format(Calendar.getInstance().getTime());


        String uri;
        if (intent.getStringExtra("uri") != null) {
            uri = intent.getStringExtra("uri");
            Log.i(UtilPack.TAG, uri);
            List<String> list = UtilPack.parseUri(uri);
            ExpressLog log;
            if (list.size() == 3)//带用户UID的请求，返回Json数据
            {
                log = new ExpressLog(list.get(2), date, expressCompanyProvider.
                        getCompanyName(list.get(1)), list.get(1));
                Log.i("Save log", log.toString());
                log.save();
                showExpressMessage(uri);
            } else //加载网页
            {
                linearLayout.setVisibility(View.VISIBLE);
                webView.loadUrl(uri);
                log = new ExpressLog(list.get(1), date, expressCompanyProvider.
                        getCompanyName(list.get(0)), list.get(0));
                log.save();
            }
        } else if (intent.getSerializableExtra("log") != null) {
            ExpressLog log = (ExpressLog) intent.getSerializableExtra("log");
            if (SearchFragment.FOR_WEB_SEARCH.contains(log.company_name)) //加载网页
            {
                linearLayout.setVisibility(View.VISIBLE);
                uri = SearchFragment.WEB_URI + "type=" + log.company_code + "&postid=" + log.express_order;
                webView.loadUrl(uri);
            } else {
                //带用户UID的请求，返回Json数据
                uri = SearchFragment.URI + SearchFragment.UID + "&com=" + log.company_code + "&nu=" + log.express_order;
                showExpressMessage(uri);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void showExpressMessage(String uri) {
        new MyAsyncTask().execute(uri);
    }

    class MyAsyncTask extends AsyncTask<String, Integer, String> {

        /**
         * UI线程
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        /**
         * 非UI线程
         *
         * @param url API请求
         * @return json数据
         */
        @Override
        protected String doInBackground(String... url) {
            Log.i(UtilPack.TAG, url[0]);
            StringBuilder jsonSB = new StringBuilder();
            BufferedReader bufferedReader;
            int totalLine = 0;
            int lineNow = 0;
            String line;
            URL httpURL;
            try {
                httpURL = new URL(url[0]);
                HttpURLConnection connection = (HttpURLConnection) httpURL.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while (bufferedReader.readLine() != null) totalLine++;

                bufferedReader = new BufferedReader(new InputStreamReader(httpURL.openConnection().getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    lineNow++;
                    publishProgress((lineNow / totalLine) * 100);
                    jsonSB.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonSB.toString();
        }

        /**
         * UI线程
         *
         * @param result json数据
         */
        @Override
        protected void onPostExecute(String result) {
            ExpressDetail expressDetail = JSON.parseObject(result, ExpressDetail.class);
            progressBar.setVisibility(View.GONE);
            switch (expressDetail.getStatus()) {
                case "0":
                    Toast.makeText(getApplicationContext(), R.string.status_0, Toast.LENGTH_SHORT).show();
                    break;
                case "1":
                    listView.setVisibility(View.VISIBLE);
                    data.addAll(expressDetail.getData());
                    adapter.notifyDataSetChanged();
                    break;
                case "2":
                    Toast.makeText(getApplicationContext(), R.string.status_2, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }
    }
}
