package io.github.jhcpokemon.expressassist.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.jhcpokemon.expressassist.R;
import io.github.jhcpokemon.expressassist.model.Data;

public class ListAdapter extends BaseAdapter {
    private List<Data> list;
    private Context context;
    public ListAdapter(Context context, List<Data> objects) {
        this.context = context;
        list = objects;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.indexOf(list.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Data data = list.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_view, null, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.timeView.setText(data.getTime());
        viewHolder.contextView.setText(data.getContext());
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    static class ViewHolder {
        @Bind(R.id.time_view)
        TextView timeView;
        @Bind(R.id.context_view)
        TextView contextView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
