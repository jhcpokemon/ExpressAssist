package io.github.jhcpokemon.expressassist.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.jhcpokemon.expressassist.R;
import io.github.jhcpokemon.expressassist.model.ExpressLog;

public class SettingFragment extends Fragment implements View.OnClickListener {
    private static final String policy = "Dependencies:\nButter Knife:\nField and method binding for Android views which "
            + "uses annotation processing to generate boilerplate code for you.\n  \u02D9Eliminate findViewById"
            + " calls by using @Bind on fields.\n  \u02D9Group multiple views in a list or array. Operate on all"
            + "of them at once with actions, setters, or properties.\n  \u02D9Eliminate anonymous inner-classes"
            + "for listeners by annotating methods with @OnClick and others.\n  \u02D9Eliminate resource lookups"
            + "by using resource annotations on fields.\nSugar Orm:\nIt is intended to simplify the interaction"
            + "with SQLite database in Android.\n  \u02D9It eliminates writing SQL queries to interact with SQLite db."
            + "\n  \u02D9It takes care of creating your database.\n  \u02D9It manages object relationships too.\n  \u02D9"
            + "It provides you with clear and simple APIs for db operations\nFast Json:\nFastjson is a JSON processor"
            + "(JSON parser + JSON generator) written in Java:\n  \u02D9FAST (measured to be faster than any other Java"
            + "parser and databinder, includes jackson. )\n  \u02D9Powerful (full data binding for common JDK classes as"
            + "well as any Java Bean class, Collection, Map, Date or enum)\n  \u02D9Zero-dependency (does not rely on "
            + "other packages beyond JDK)\n  \u02D9Open Source (Apache License 2.0)\nCopyright [jhcpokemon]\n\nLicensed"
            + "under the Apache License, Version 2.0 (the \"License\");\nyou may not use this file except in compliance"
            + " with the License.\nYou may obtain a copy of the License at\n\n    http://www.apache.org/licenses/LICENSE"
            + "-2.0\n\nUnless required by applicable law or agreed to in writing, software\ndistributed under the License "
            + "is distributed on an \"AS IS\" BASIS,\nWITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied"
            + ".\nSee the License for the specific language governing permissions and\nlimitations under the License.";
    @Bind(R.id.version)
    TextView versionTextView;
    @Bind(R.id.policy)
    TextView policyTextView;
    @Bind(R.id.clear)
    TextView clearTextView;
    private long firstClick;
    private int count = 5;

    public SettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        versionTextView.setOnClickListener(this);
        policyTextView.setOnClickListener(this);
        clearTextView.setOnClickListener(this);
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
            case R.id.clear:
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_msg", Context.MODE_APPEND);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", "");
                editor.putString("password", "");
                editor.putBoolean("save", false);
                editor.putBoolean("auto", false);
                editor.apply();
                ExpressLog.deleteAll(ExpressLog.class);
                getActivity().finish();
                break;
            case R.id.version:
                if (count == 5) firstClick = SystemClock.uptimeMillis();
                if (SystemClock.uptimeMillis() - firstClick > 1500 && count == 0) {
                    Dialog imageDialog = new Dialog(getContext());
                    ViewGroup.LayoutParams params = imageDialog.getWindow().getAttributes();
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    params.height = WindowManager.LayoutParams.MATCH_PARENT;
                    imageDialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
                    imageDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    imageDialog.setContentView(R.layout.image_dialog);
                    imageDialog.setCancelable(true);
                    imageDialog.show();
                    versionTextView.setClickable(false);
                }
                count--;
                break;
            case R.id.policy:
                Dialog policyDialog = new Dialog(getContext()) {
                    @Override
                    protected void onCreate(Bundle savedInstanceState) {
                        super.onCreate(savedInstanceState);
                        setTitle(R.string.policy);
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_policy, null, false);
                        policyTextView = (TextView) view.findViewById(R.id.policy);
                        policyTextView.setText(policy);
                        setContentView(view);
                        setCancelable(true);
                        ViewGroup.LayoutParams params = getWindow().getAttributes();
                        params.width = WindowManager.LayoutParams.MATCH_PARENT;
                        getWindow().setAttributes((WindowManager.LayoutParams) params);
                    }
                };

                policyDialog.show();
                break;
            default:
                break;
        }
    }
}
