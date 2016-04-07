package io.github.jhcpokemon.expressassist.fragment.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
        Log.i("Log", holder.mLog.toString());
        holder.mOrderView.append(mLogs.get(position).express_order);
        holder.mDateView.setText(mLogs.get(position).express_date);
        holder.mNameView.append(mLogs.get(position).company_name);

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
        public final TextView mDateView;
        public final TextView mNameView;
        public ExpressLog mLog;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mOrderView = (TextView) view.findViewById(R.id.order);
            mDateView = (TextView) view.findViewById(R.id.date);
            mNameView = (TextView) view.findViewById(R.id.name);
        }

    }
}
