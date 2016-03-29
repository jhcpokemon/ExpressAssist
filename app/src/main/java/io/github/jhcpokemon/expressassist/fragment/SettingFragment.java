package io.github.jhcpokemon.expressassist.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.jhcpokemon.expressassist.R;

public class SettingFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.version)
    TextView versionTextView;
    @Bind(R.id.policy)
    TextView policyTextView;
    private long hits[] = new long[5];

    public SettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        versionTextView.setOnClickListener(this);
        policyTextView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.version:
                System.arraycopy(hits, 1, hits, 0, hits.length);
                hits[hits.length - 1] = SystemClock.uptimeMillis();
                if (hits[0] - SystemClock.uptimeMillis() > 1500) {
                    Dialog imageDialog = new Dialog(getContext());
                    imageDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    imageDialog.setContentView(R.layout.image_dialog);
                    imageDialog.setCancelable(true);
                    imageDialog.show();
                }
                break;
            case R.id.policy:
                // TODO: 03/29/16 依赖的库以及开源协议
                Dialog policyDialog = new Dialog(getContext());
                policyDialog.setContentView(R.layout.policy_dialog);
                policyDialog.setCancelable(true);
                policyDialog.show();
                break;
            default:
                break;
        }
    }
}
