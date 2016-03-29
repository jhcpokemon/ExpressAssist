package io.github.jhcpokemon.expressassist.model;


import android.content.Context;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ExpressCompanyProvider {
    private List<Company> companies = new ArrayList<>();

    public ExpressCompanyProvider(Context context) {
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            InputStream jsonInStr = context.getAssets().open("company.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(jsonInStr));
            while ((line = br.readLine()) != null){
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        companies = JSON.parseArray(sb.toString(),Company.class);
    }

    public String getCompanyCode(String name) {
        String code = "";
        for (Company company : companies){
            if (company.name.equals(name)){
                code =  company.code;
                break;
            }
        }
        return code;
    }

    public List<Company> getCompanies() {
        return companies;
    }
}
