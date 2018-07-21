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
import com.javaweb.browser.bookmark.Bookmark;
import com.javaweb.browser.bookmark.BookmarkModel;

import java.util.List;

public class BookmarkAdapter extends ArrayAdapter<Bookmark>{

	TextView item_bookmark_title;
	TextView item_bookmark_url;
	ImageView item_bookmark_delete;
	ListView bookmark_list;

	public BookmarkAdapter(@NonNull Context context, int resource, @NonNull List<Bookmark> objects) {
		super(context, resource, objects);
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		Bookmark bookmark = getItem(position);
		final String id = bookmark.getId();
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.item_bookmark, parent,false);
		} else {
			view = convertView;
		}

		item_bookmark_title = (TextView)view.findViewById(R.id.item_bookmark_title);
		item_bookmark_url = (TextView) view.findViewById(R.id.item_bookmark_url);
		item_bookmark_delete = (ImageView) view.findViewById(R.id.item_bookmark_delete);
//		bookmark_list = (ListView) .findViewById(R.id.bookmark_list);


		item_bookmark_title.setText(bookmark.getTitle());
		item_bookmark_url.setText(bookmark.getUrl());

		item_bookmark_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BookmarkModel bookmarkModel = new BookmarkModel(getContext(), "bookmark", null, 1);
				SQLiteDatabase db = bookmarkModel.getWritableDatabase();
				boolean result = bookmarkModel.deleteBookmark(db, id);
				if (result) {
					((MainActivity)getContext()).getAllBookmark();
					Toast.makeText(getContext(), "删除成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getContext(), "删除失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
		return view;
	}


}
