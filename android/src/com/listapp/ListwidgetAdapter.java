package com.listapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.listapp.dao.NamedListDao;
import com.listapp.model.NamedList;

import java.util.ArrayList;
import java.util.List;

public class ListwidgetAdapter extends BaseAdapter implements ListAdapter {

	public class ViewHolder {
		TextView listNameView;
	}

	private LayoutInflater inflater;
    private Context mContext;

	public ListwidgetAdapter(Context context) {
	    this.mContext = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        namedListDao = new NamedListDao(mContext);
	}
	
	private List<NamedList> lists = new ArrayList<NamedList>();
    private NamedListDao namedListDao;
	
	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public NamedList getItem(int position) {
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

        NamedList list = lists.get(position);
        holder.listNameView.setText(list.name);
        return convertView;
	}

	protected void setLists(List<NamedList> argLists) {
		lists.clear();
		if (argLists != null) {
			lists.addAll(argLists);
			notifyDataSetChanged();
		}
	}

    public void reQuery() {
        lists.clear();
        List<NamedList> listAll = namedListDao.listAll();
        lists.addAll(listAll);
        notifyDataSetChanged();
    }
	
	
	
}
