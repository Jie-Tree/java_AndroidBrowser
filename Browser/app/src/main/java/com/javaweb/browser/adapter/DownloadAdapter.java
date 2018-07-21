package com.javaweb.browser.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.javaweb.browser.R;
import com.javaweb.browser.activity.MainActivity;
import com.javaweb.browser.downloadutils.bean.DownloadInfo;
import com.javaweb.browser.downloadutils.constant.Constant;
import com.javaweb.browser.downloadutils.db.DBManager;
import com.javaweb.browser.downloadutils.db.DownloadModel;
import com.javaweb.browser.downloadutils.download.FileDownloader;
import com.javaweb.browser.downloadutils.state.DownloadState;
import com.javaweb.browser.downloadutils.state.impl.CancleDownloadState;
import com.javaweb.browser.downloadutils.state.impl.PrepareDownloadState;
import com.javaweb.browser.downloadutils.state.impl.StartDownloadState;
import com.javaweb.browser.downloadutils.state.impl.StopDownloadState;
import com.javaweb.browser.downloadutils.utils.DownLoaderController;
import com.javaweb.browser.downloadutils.utils.NetWorkUtil;

import java.io.File;
import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder>{

	private Context context;
	public ListView download_list;

	public List<DownloadInfo> mDownloadInfo;

	public DownloadAdapter(Context context, List<DownloadInfo> mDownloadInfo) {
		this.mDownloadInfo = mDownloadInfo;
		this.context = context;
	}

	static class ViewHolder extends RecyclerView.ViewHolder{
		TextView item_download_name;
		ImageView item_download_delete;
		ImageView item_download_start;
		ImageView item_download_pause;
		ProgressBar item_download_bar;

		public ViewHolder(View view) {
			super(view);
			item_download_name=(TextView) view.findViewById(R.id.item_download_name);
			item_download_pause=(ImageView) view.findViewById(R.id.item_download_pause);
			item_download_start=(ImageView) view.findViewById(R.id.item_download_start);
			item_download_delete = (ImageView) view.findViewById(R.id.item_download_delete);
			item_download_bar=(ProgressBar) view.findViewById(R.id.item_download_bar);

		}

	}


	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download,parent,false);
		ViewHolder holder = new ViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {//主要接受子线程发送的数据， 并用此数据配合主线程更新UI
		final Handler mHandler=new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				String url;
				switch (msg.what){
					case Constant.DOWNLOAD_START:
						holder.item_download_bar.setMax(msg.arg1);
						break;
					case Constant.DOWNLOAD_KEEP:
						holder.item_download_bar.setProgress(msg.arg1);
						break;
					case Constant.DOWNLOAD_COMPLETE:
						Toast.makeText(context,"下载完成",Toast.LENGTH_SHORT).show();
						url= (String) msg.obj;
						DBManager.getInstance(context).deleteInfo(url) ;
						break;
					case Constant.DOWNLOAD_FAIL:
						Toast.makeText(context,"下载失败",Toast.LENGTH_SHORT).show();
						String urlstr= (String) msg.obj;
						FileDownloader.getInstance().pauseDownload(urlstr);
						break;
					case Constant.DOWNLOAD_ClLEAN:
//                    do something
						holder.item_download_bar.setProgress(0);
						url= (String) msg.obj;
						DBManager.getInstance(context).deleteInfo(url) ;
						break;
					case Constant.DOWNLOAD_PREPARE:
						holder.item_download_bar.setMax(msg.arg2);
						holder.item_download_bar.setProgress(msg.arg1);
						break;
					default:
						break;
				}
				return true;

			}
		});
		final DownloadInfo downloadInfo = mDownloadInfo.get(position);
		final String id = downloadInfo.getId();
		final String filename = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/WebDownload/" + downloadInfo.getTitle();
		final DownloadState preparetate=new PrepareDownloadState();
		final DownloadState startstate=new StartDownloadState();
		final DownloadState pausestate=new StopDownloadState();
		final DownloadState canclestate=new CancleDownloadState();
		final DownLoaderController controller=new DownLoaderController();

		holder.item_download_pause.setVisibility(View.GONE);
		holder.item_download_start.setVisibility(View.VISIBLE);
		controller.setDownloadState(preparetate);
		controller.prepareDownload(context,mHandler,downloadInfo.getUrl(),filename,8);
		holder.item_download_pause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!NetWorkUtil.isNetworkAvailable(context)){
					Toast.makeText(context,"请检查网络",Toast.LENGTH_SHORT).show();
					return;
				}
				holder.item_download_pause.setVisibility(View.GONE);
				holder.item_download_start.setVisibility(View.VISIBLE);
				controller.setDownloadState(pausestate);
				controller.stopDownload(context,mHandler,downloadInfo.getUrl(), filename,8);
			}
		});
		holder.item_download_start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!NetWorkUtil.isNetworkAvailable(context)){
					Toast.makeText(context,"请检查网络",Toast.LENGTH_SHORT).show();
					return;
				}
				holder.item_download_pause.setVisibility(View.VISIBLE);
				holder.item_download_start.setVisibility(View.GONE);
				controller.setDownloadState(startstate);
				controller.startDownload(context,mHandler,downloadInfo.getUrl(),filename,8);
			}
		});
//		download_list = (ListView) .findViewById(R.id.download_list);


		holder.item_download_name.setText(downloadInfo.getTitle());
//		item_download_url.setText(downloadInfo.getUrl());

		holder.item_download_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				controller.setDownloadState(pausestate);
				controller.stopDownload(context,mHandler,downloadInfo.getUrl(), filename,8);
				DownloadModel downloadModel = new DownloadModel(context, "download", null, 1);
				SQLiteDatabase db = downloadModel.getWritableDatabase();
				boolean result = downloadModel.deleteDownload(db, id);
				controller.setDownloadState(canclestate);
				controller.cancleDownload(context,mHandler,downloadInfo.getUrl(),filename,8);
				if (result) {
					mDownloadInfo.remove(holder.getAdapterPosition());
					DownloadAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
//					((MainActivity)context).getAllDownload();
					Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	@Override
	public int getItemCount() {
		return mDownloadInfo.size();
	}


}
