package com.javaweb.browser.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.javaweb.browser.R;
import com.javaweb.browser.activity.MainActivity;
import com.javaweb.browser.history.History;
import com.javaweb.browser.history.HistoryModel;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<History>{

	TextView item_history_title;
	TextView item_history_url;
	ImageView item_history_delete;
	ListView history_list;

	public HistoryAdapter(@NonNull Context context, int resource, @NonNull List<History> objects) {
		super(context, resource, objects);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		History history = getItem(position);
		final String id = history.getId();
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.item_history, parent,false);
		} else {
			view = convertView;
		}

		item_history_title = (TextView)view.findViewById(R.id.item_history_title);
		item_history_url = (TextView) view.findViewById(R.id.item_history_url);
		item_history_delete = (ImageView) view.findViewById(R.id.item_history_delete);
//		history_list = (ListView) .findViewById(R.id.history_list);


		item_history_title.setText(history.getTitle());
		item_history_url.setText(history.getUrl());

		item_history_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				HistoryModel historyModel = new HistoryModel(getContext(), "history", null, 1);
				SQLiteDatabase db = historyModel.getWritableDatabase();
				boolean result = historyModel.deleteHistory(db, id);
				if (result) {
					((MainActivity)getContext()).getAllHistory();
//					Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
				}
			}
		});



		return view;
	}


}
