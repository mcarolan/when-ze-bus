package net.mcarolan.whenzebus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.mcarolan.whenzebus.ColorGenerator;
import net.mcarolan.whenzebus.R;

import java.util.List;

public class GenericListAdapter<T extends GenericListItem> extends ArrayAdapter<T> {

    public GenericListAdapter(Context context, List<T> items) {
        super(context, R.layout.listitem_generic, items);
    }

    static class ViewHolder {
        TextView legend;
        TextView title;
        TextView description;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem_generic, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.listitem_generic_title);
            viewHolder.legend = (TextView) convertView.findViewById(R.id.listitem_generic_legend);
            viewHolder.description = (TextView) convertView.findViewById(R.id.listitem_generic_description);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final T item = getItem(position);

        viewHolder.legend.setText(item.getLegend());
        viewHolder.legend.setBackgroundColor(ColorGenerator.getBackgroundColorIntFor(item.getLegend()));
        viewHolder.legend.setTextColor(ColorGenerator.getForegroundColorIntFor(item.getLegend()));

        viewHolder.title.setText(item.getTitle());

        viewHolder.description.setText(item.getDescription());

        return convertView;
    }

}
