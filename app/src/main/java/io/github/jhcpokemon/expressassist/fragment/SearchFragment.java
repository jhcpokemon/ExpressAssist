package io.github.jhcpokemon.expressassist.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.jhcpokemon.expressassist.R;
import io.github.jhcpokemon.expressassist.activity.DetailActivity;
import io.github.jhcpokemon.expressassist.activity.WebSignInActivity;
import io.github.jhcpokemon.expressassist.model.Company;
import io.github.jhcpokemon.expressassist.model.ExpressCompanyProvider;

public class SearchFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final String UID = "id=72ea3bfc8a4465dc";
    public static final String FOR_WEB_SEARCH = "包裹/平邮/挂号信EMSE邮宝EMS国际件国内邮件国际邮件申通顺丰圆通速递韵达快运中通速递";
    public static String URI = "http://api.kuaidi100.com/api?";
    @Bind(R.id.order)
    TextView orderTextView;
    @Bind(R.id.express_co_spinner)
    Spinner expressCompanySpinner;
    @Bind(R.id.search)
    Button searchBtn;
    @Bind(R.id.web_search)
    Button webSearchBtn;
    private ExpressCompanyProvider expressCompanyProvider;
    private String companyCode;
    private List<String> companiesName;

    public SearchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        searchBtn.setOnClickListener(this);
        webSearchBtn.setOnClickListener(this);
        companiesName = new ArrayList<>();
        expressCompanyProvider = new ExpressCompanyProvider(getContext());
        List<Company> companies = expressCompanyProvider.getCompanies();
        for (Company company : companies) {
            companiesName.add(company.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, companiesName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expressCompanySpinner.setAdapter(adapter);
        expressCompanySpinner.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.search:
                URI = URI + UID + "&com=" + companyCode + "&nu=" + orderTextView.getText().toString();
                intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("uri", URI);
                startActivity(intent);
                break;
            case R.id.web_search:
                URI = URI + "&com=" + companyCode + "&nu=" + orderTextView.getText().toString();
                intent = new Intent(getContext(), WebSignInActivity.class);
                intent.putExtra("uri", URI);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String companyName = companiesName.get(position);
        companyCode = expressCompanyProvider.getCompanyCode(companyName);
        if (FOR_WEB_SEARCH.contains(companyName)) {
            Toast.makeText(getContext(), getResources().getString(R.string.web_notify),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
