package io.github.jhcpokemon.expressassist.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.jhcpokemon.expressassist.R;
import io.github.jhcpokemon.expressassist.fragment.adapter.RecyclerViewAdapter;
import io.github.jhcpokemon.expressassist.model.ExpressLog;
import io.github.jhcpokemon.expressassist.model.ExpressLogProvider;

public class ExpressItemFragment extends Fragment {
    private OnListItemClickListener mListener;

    public ExpressItemFragment() {
    }

    public static ExpressItemFragment newInstance() {
        return new ExpressItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expressitem_list, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new RecyclerViewAdapter(ExpressLogProvider.getLOGS(), mListener));
        }
        return view;
    }

    public interface OnListItemClickListener {
        void onListItemClicked(ExpressLog log);
    }

    public void setOnClickListener(OnListItemClickListener listener) {
        mListener = listener;
    }
}
