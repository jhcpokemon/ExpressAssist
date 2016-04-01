package io.github.jhcpokemon.expressassist.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.jhcpokemon.expressassist.R;
import io.github.jhcpokemon.expressassist.model.Data;

public class ListAdapter extends ArrayAdapter<Data> {
    public ListAdapter(Context context, List<Data> objects) {
        super(context, R.layout.list_item_view, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Data data = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_view, parent, false);
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

    private static class ViewHolder {
        @Bind(R.id.time_view)
        TextView timeView;
        @Bind(R.id.context_view)
        TextView contextView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


}
