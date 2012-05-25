package com.listapp;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.listapp.model.ListItem;

import java.util.List;

public class ViewListActivity extends ListActivity implements OnItemClickListener {

	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		adapter.add("test item 1");
		adapter.add("test item 2");
		setListAdapter(adapter);
		getListView().setAdapter(adapter);
		getListView().setOnItemClickListener(this);
		new FetchItemsTask(this).execute();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	public void setItems(List<ListItem> result) {
		adapter.clear();
		for (ListItem listItemProxy : result) {
			adapter.add(listItemProxy.getItemText());
		}
	}
	
}