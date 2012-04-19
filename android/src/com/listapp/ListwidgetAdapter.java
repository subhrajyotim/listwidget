package com.listapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.turbomanage.listwidget.shared.proxy.NamedListProxy;

public class ListwidgetAdapter extends BaseAdapter implements ListAdapter {

	public class ViewHolder {
		TextView listNameView;
	}

	private LayoutInflater inflater;

	public ListwidgetAdapter(Context context) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	private List<NamedListProxy> lists = new ArrayList<NamedListProxy>();
	
	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public NamedListProxy getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem, parent, false);
            holder = new ViewHolder();
            holder.listNameView = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NamedListProxy list = lists.get(position);
        holder.listNameView.setText(list.getName());
        return convertView;
	}

	protected void setLists(List<NamedListProxy> argLists) {
		lists.clear();
		if (argLists != null) {
			lists.addAll(argLists);
			notifyDataSetChanged();
		}
	}

}
