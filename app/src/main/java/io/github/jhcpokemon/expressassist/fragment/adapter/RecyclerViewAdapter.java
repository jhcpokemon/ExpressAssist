package io.github.jhcpokemon.expressassist.fragment.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.github.jhcpokemon.expressassist.R;
import io.github.jhcpokemon.expressassist.fragment.ExpressItemFragment;
import io.github.jhcpokemon.expressassist.model.ExpressLog;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private final List<ExpressLog> mLogs;
    private final ExpressItemFragment.OnListItemClickListener mListener;

    public RecyclerViewAdapter(List<ExpressLog> logs, ExpressItemFragment.OnListItemClickListener listener) {
        mLogs = logs;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_expressitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mLog = mLogs.get(position);
        holder.mOrderView.setText(mLogs.get(position).order);
        holder.mDataView.setText(mLogs.get(position).date);
        holder.mNameView.setText(mLogs.get(position).name);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListItemClicked(holder.mLog);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLogs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mOrderView;
        public final TextView mDataView;
        public final TextView mNameView;
        public ExpressLog mLog;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mOrderView = (TextView) view.findViewById(R.id.order);
            mDataView = (TextView) view.findViewById(R.id.date);
            mNameView = (TextView) view.findViewById(R.id.name);
        }

    }
}
