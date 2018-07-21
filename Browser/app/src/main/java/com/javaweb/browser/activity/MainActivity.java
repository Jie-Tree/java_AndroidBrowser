package com.javaweb.browser.activity;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;


import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.*;
import android.widget.*;
import com.javaweb.browser.R;
import com.javaweb.browser.adapter.BookmarkAdapter;
import com.javaweb.browser.adapter.DownloadAdapter;
import com.javaweb.browser.adapter.HistoryAdapter;
import com.javaweb.browser.bookmark.Bookmark;
import com.javaweb.browser.bookmark.BookmarkModel;
import com.javaweb.browser.downloadutils.db.DBManager;
import com.javaweb.browser.downloadutils.db.DownloadModel;
import com.javaweb.browser.history.History;
import com.javaweb.browser.history.HistoryModel;
import com.javaweb.browser.utils.NetUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener {

	RelativeLayout index_view;
    EditText index_title_edit;
    WebView  index_webView;
    LinearLayout index_bottom_menu_goback;
    LinearLayout index_bottom_menu_nogoback;
    LinearLayout index_bottom_menu_goforward;
    LinearLayout index_bottom_menu_nogoforward;
    LinearLayout index_bottom_menu_gohome;
    LinearLayout index_bottom_menu_nogohome;
    LinearLayout index_bottom_menu_morepaper;
    FrameLayout index_bottom_menu_more;
    ProgressBar index_title_progress;
    TextView input_url_go;

    LinearLayout bottom_dialog;
    View index_background;

    RelativeLayout popup_exit;

    ImageView title_top_earth;
    LinearLayout search_view;
    EditText search_title_edit;
    Button search_title_cancel;
    ImageView search_title_url_clear;
    Button search_title_go;
    ImageView index_title_refresh;

    WebViewClient homeWebViewClient;
    WebChromeClient homeWebChromeClient;

    String web_title;

    RelativeLayout popup_add_bookmark;
    LinearLayout add_bookmark_view;
    ImageView add_bookmark_goback;
    EditText add_bookmark_name;
    EditText add_bookmark_url;
    TextView add_bookmark_ok;

    RelativeLayout popup_bookmark;
    LinearLayout bookmark_view;
    ImageView bookmark_goback;
    ListView bookmark_list;

    RelativeLayout popup_history;
    LinearLayout history_view;
	ImageView history_goback;
    ListView history_list;
    Button history_clear;

    LinearLayout morepaper_view;
    TextView morepaper_num;
    ListView morepaper_list;
    ImageView morepaper_add;

    RelativeLayout popup_download;
    LinearLayout download_view;
    ImageView download_goback;
    RecyclerView download_list;

    LinearLayout add_download_view;
    ImageView add_download_goback;
    EditText add_download_name;
    EditText add_download_url;
    TextView add_download_ok;

    RelativeLayout popup_night;
    ImageView night;
    ImageView night_open;
    boolean isnight = false;

    RelativeLayout popup_computer;
    ImageView computer;
    ImageView computer_open;
    boolean iscomputer =false;

    RelativeLayout popup_nocache;
    ImageView nocache;
    ImageView nocache_open;
    boolean isnocache =false;

    RelativeLayout popup_share;

    RelativeLayout popup_setting;
    LinearLayout setting_view;
    ImageView setting_goback;
    TextView setting_title;
    EditText setting_url;
    Button setting_button;

    ArrayList<String[]> paper;
    //主页地址
    WebSettings webSettings;
    private String home_url = "https://www.hao123.com/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        index_view = (RelativeLayout)findViewById(R.id.index_view);
        index_title_edit = (EditText) index_view.findViewById(R.id.index_title_edit);
        index_webView = (WebView)index_view.findViewById(R.id.index_webView);
        index_bottom_menu_goback = (LinearLayout)findViewById(R.id.index_bottom_menu_goback);
        index_bottom_menu_nogoback = (LinearLayout)findViewById(R.id.index_bottom_menu_nogoback);
        index_bottom_menu_goforward = (LinearLayout)findViewById(R.id.index_bottom_menu_goforward);
        index_bottom_menu_nogoforward = (LinearLayout)findViewById(R.id.index_bottom_menu_nogoforward);
        index_bottom_menu_gohome = (LinearLayout)findViewById(R.id.index_bottom_menu_gohome);
        index_bottom_menu_nogohome = (LinearLayout)findViewById(R.id.index_bottom_menu_nogohome);
        index_bottom_menu_more = (FrameLayout)findViewById(R.id.index_bottom_menu_more);//菜单
        index_title_refresh = (ImageView)index_view.findViewById(R.id.index_title_refresh);
        index_title_progress = (ProgressBar)index_view.findViewById(R.id.index_title_progress);
        index_background = (View)findViewById(R.id.index_background);
        input_url_go = (TextView) findViewById(R.id.input_url_go);

        title_top_earth = (ImageView) findViewById(R.id.title_top_earth);
        search_view = (LinearLayout)findViewById(R.id.search_view);
        search_title_edit = (EditText)search_view.findViewById(R.id.search_title_edit);
        search_title_cancel = (Button)search_view.findViewById(R.id.search_title_cancel);
        search_title_go = (Button)search_view.findViewById(R.id.search_title_go);
        search_title_url_clear = (ImageView)search_view.findViewById(R.id.search_title_url_clear);
        popup_add_bookmark = (RelativeLayout)findViewById(R.id.popup_add_bookmark);
        add_bookmark_view = (LinearLayout)findViewById(R.id.add_bookmark_view);
        add_bookmark_goback = (ImageView)findViewById(R.id.add_bookmark_goback);
        add_bookmark_name = (EditText) add_bookmark_view.findViewById(R.id.add_bookmark_name);
        add_bookmark_url = (EditText) add_bookmark_view.findViewById(R.id.add_bookmark_url);
        add_bookmark_ok = (TextView) add_bookmark_view.findViewById(R.id.add_bookmark_ok);

        popup_bookmark = (RelativeLayout)findViewById(R.id.popup_bookmark);
        bookmark_view = (LinearLayout)findViewById(R.id.bookmark_view);
        bookmark_goback = (ImageView)findViewById(R.id.bookmark_goback);
        bookmark_list = (ListView) findViewById(R.id.bookmark_list);


        popup_history = (RelativeLayout)findViewById(R.id.popup_history);
	    history_view = (LinearLayout) findViewById(R.id.history_view);
	    history_goback = (ImageView)findViewById(R.id.history_goback);
        history_list = (ListView) findViewById(R.id.history_list);
        history_clear = (Button) findViewById(R.id.history_clear);

        index_bottom_menu_morepaper = (LinearLayout)findViewById(R.id.index_bottom_menu_new_window);
        morepaper_view = (LinearLayout) findViewById(R.id.morepaper_view);
        morepaper_list = (ListView) findViewById(R.id.morepaper_list);
        morepaper_add = (ImageView) findViewById(R.id.morepaper_add);
        morepaper_num = (TextView)findViewById(R.id.morepapre_num);

        popup_download = (RelativeLayout)findViewById(R.id.popup_download);
        download_view = (LinearLayout) findViewById(R.id.download_view);
        download_goback = (ImageView)findViewById(R.id.download_goback);
        download_list = (RecyclerView) findViewById(R.id.download_list);

        add_download_view = (LinearLayout)findViewById(R.id.add_download_view);
        add_download_goback = (ImageView)findViewById(R.id.add_download_goback);
        add_download_name = (EditText) add_download_view.findViewById(R.id.add_download_name);
        add_download_url = (EditText) add_download_view.findViewById(R.id.add_download_url);
        add_download_ok = (TextView) add_download_view.findViewById(R.id.add_download_ok);


        bottom_dialog = (LinearLayout)findViewById(R.id.bottom_dialog);
        popup_exit = (RelativeLayout)findViewById(R.id.popup_exit);

        popup_night = (RelativeLayout)findViewById(R.id.popup_night);
        night = (ImageView) popup_night.findViewById(R.id.img_night);
        night_open = (ImageView) popup_night.findViewById(R.id.img_night_open);

        popup_computer = (RelativeLayout)findViewById(R.id.popup_computer);
        computer = (ImageView) popup_computer.findViewById(R.id.img_computer);
        computer_open = (ImageView) popup_computer.findViewById(R.id.img_computer_open);

        popup_nocache = (RelativeLayout)findViewById(R.id.popup_no_cache);
        nocache = (ImageView) popup_nocache.findViewById(R.id.img_no_chahe);
        nocache_open = (ImageView) popup_nocache.findViewById(R.id.img_no_chahe_open);
        
        popup_share = (RelativeLayout)findViewById(R.id.popup_share);

        popup_setting = (RelativeLayout)findViewById(R.id.popup_setting);
        setting_view = (LinearLayout) findViewById(R.id.setting_view);
        setting_goback = (ImageView)findViewById(R.id.setting_goback);
        setting_title = (TextView)findViewById(R.id.setting_title);
        setting_url = (EditText)findViewById(R.id.setting_url);
        setting_button = (Button) findViewById(R.id.setting_button);

        paper = new ArrayList<String[]>();
        paper.add(new String[]{"hao123导航-上网从这里开始" ,home_url});
        morepaper_list.setAdapter(new MorepaperAdapter(this, R.layout.item_morepaper, paper));
//--------------------以下是监听器--------------------
        index_bottom_menu_more.setOnClickListener(this);
        index_bottom_menu_gohome.setOnClickListener(this);
        index_title_edit.setOnClickListener(this);
        index_bottom_menu_goback.setOnClickListener(this);
        index_bottom_menu_goforward.setOnClickListener(this);
        index_title_refresh.setOnClickListener(this);
        index_background.setOnClickListener(this);

        popup_exit.setOnClickListener(this);


        search_title_edit.addTextChangedListener(search_title_edit_changed);
        search_title_cancel.setOnClickListener(this);
        search_title_url_clear.setOnClickListener(this);
        search_title_go.setOnClickListener(this);

        popup_add_bookmark.setOnClickListener(this);
        add_bookmark_goback.setOnClickListener(this);
        add_bookmark_ok.setOnClickListener(this);

        popup_bookmark.setOnClickListener(this);
        bookmark_goback.setOnClickListener(this);
        bookmark_list.setOnItemClickListener(this);

        popup_history.setOnClickListener(this);
	    history_goback.setOnClickListener(this);
        history_list.setOnItemClickListener(this);
        history_clear.setOnClickListener(this);

        index_bottom_menu_morepaper.setOnClickListener(this);
        morepaper_list.setOnItemClickListener(this);
        morepaper_add.setOnClickListener(this);

//      popup_add_download.setOnClickListener(this);
        add_download_goback.setOnClickListener(this);
        add_download_ok.setOnClickListener(this);

        popup_download.setOnClickListener(this);
        download_goback.setOnClickListener(this);

        popup_night.setOnClickListener(this);
        popup_computer.setOnClickListener(this);
        popup_nocache.setOnClickListener(this);
        popup_share.setOnClickListener(this);

        popup_setting.setOnClickListener(this);
        setting_goback.setOnClickListener(this);
        setting_button.setOnClickListener(this);
        setting_url.setOnClickListener(this);
        setting_title.setOnClickListener(this);

        initHome();
        getAllDownload();//获取下载列表的数据，打开下载列表时不再刷新
    }


    @Override
    public void onClick(View v) {
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);//设置软键盘
            Animation animation;
        switch (v.getId()){
            case R.id.setting_button:
                home_url = setting_url.getText().toString();
                System.out.println(home_url);
                setting_goback.callOnClick();
                break;
            case R.id.popup_setting:
                setting_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_show));
                setting_view.setVisibility(View.VISIBLE);
                index_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_wait));
                index_view.setVisibility(View.GONE);
                index_background.setVisibility(View.GONE);
                bottom_dialog.setVisibility(View.GONE);
                setting_title.setText(index_webView.getTitle());
                setting_url.setText(index_webView.getUrl());
                break;
            case R.id.setting_goback:
                setting_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_hide));
                setting_view.setVisibility(View.GONE);
                index_view.setVisibility(View.VISIBLE);
                break;
            case R.id.popup_share:
                Toast.makeText(this, "该功能未实现", Toast.LENGTH_SHORT).show();
                break;
            case R.id.popup_no_cache:
                if(!isnocache){
                    nocache.setVisibility(View.INVISIBLE);
                    nocache_open.setVisibility(View.VISIBLE);
                    isnocache = true;
                }else{
                    nocache_open.setVisibility(View.GONE);
                    nocache.setVisibility(View.VISIBLE);
                    isnocache = false;
                }
                bottom_dialog.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_hide));
                index_background.setVisibility(View.GONE);
                bottom_dialog.setVisibility(View.GONE);
                break;

            case R.id.popup_night:
                Toast.makeText(this, "只实现了图标状态切换", Toast.LENGTH_SHORT).show();
                if(!isnight){
                    night.setVisibility(View.INVISIBLE);
                    night_open.setVisibility(View.VISIBLE);
                    isnight = true;
                }else{
                    night_open.setVisibility(View.GONE);
                    night.setVisibility(View.VISIBLE);
                    isnight = false;
                }
                bottom_dialog.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_hide));
                index_background.setVisibility(View.GONE);
                bottom_dialog.setVisibility(View.GONE);
                break;

            case R.id.popup_computer:
                if(!iscomputer){
                    computer.setVisibility(View.INVISIBLE);
                    computer_open.setVisibility(View.VISIBLE);
                    iscomputer = true;
                    webSettings.setBuiltInZoomControls(true);//支持缩放
                    CookieSyncManager.createInstance(this);
                    CookieManager.getInstance().removeAllCookie();
                    index_webView.clearCache(true);
                    webSettings.setUserAgentString("Mozilla/5.0 (Windows NT 10.0; WOW64) " +
                            "AppleWebKit/537.36 (KHTML, like Gecko)" +
                            " Chrome/63.0.3239.132 Safari/537.36");
                    index_webView.loadUrl(index_webView.getOriginalUrl());
                    System.out.println("computeropen");
                }else{
                    computer_open.setVisibility(View.GONE);
                    computer.setVisibility(View.VISIBLE);
                    iscomputer = false;
                    CookieSyncManager.createInstance(this);
                    CookieManager.getInstance().removeAllCookie();
                    index_webView.clearCache(true);
                    webSettings.setBuiltInZoomControls(false);//支持缩放
                    webSettings.setUserAgentString("Mozilla/5.0 (Linux; Android 7.0; EVA-AL10 Build/HUAWEIEVA-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/62.0.3202.84 Mobile Safari/537.36");
                    index_webView.loadUrl(index_webView.getOriginalUrl());
                    System.out.println("computerclose");
                }
                bottom_dialog.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_hide));
                index_background.setVisibility(View.GONE);
                bottom_dialog.setVisibility(View.GONE);
                break;

            case R.id.index_title_edit:
                //点击地址栏时发生
                //index_view.setVisibility(View.GONE);
                search_view.setVisibility(View.VISIBLE);
                index_title_refresh.setVisibility(View.GONE);
                search_title_edit.setText(web_title);
                search_title_edit .selectAll();
                search_title_edit.requestFocus();
                inputMethodManager.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);//显示软键盘
                break;
            case R.id.search_title_cancel:
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);//隐藏软键盘
                index_view.setVisibility(View.VISIBLE);
                search_view.setVisibility(View.GONE);
                index_title_refresh.setVisibility(View.VISIBLE);
                search_title_edit.clearFocus();
                break;
            case R.id.search_title_go:
                String goUrl = search_title_edit.getText().toString();
	            if (NetUtil.isHttpUrl(goUrl)) {
		            if (!goUrl.startsWith("http://") && !goUrl.startsWith("https://")) {
			            goUrl = "http://" + goUrl;
		            }
	            } else {
		            goUrl = "http://www.baidu.com/s?wd=" + goUrl;
	            }
	            search_title_edit.setText(goUrl);
	            search_title_cancel.callOnClick();
                index_webView.loadUrl(goUrl);
                break;
            case R.id.index_bottom_menu_goback:
                index_webView.goBack();
                break;
            case R.id.index_bottom_menu_goforward:
                index_webView.goForward();
                break;
            case R.id.index_bottom_menu_gohome:
                index_webView.loadUrl(home_url);
                break;
            case R.id.index_bottom_menu_new_window:
                //多页面
                if(bottom_dialog.getVisibility()==View.VISIBLE){
                    bottom_dialog.setVisibility(View.GONE);
                }
                if(morepaper_view.getVisibility()==View.GONE){
                    animation = AnimationUtils.loadAnimation(this,R.anim.menu_show);
                    index_background.setVisibility(View.VISIBLE);
                    morepaper_view.startAnimation(animation);
                    morepaper_view.setVisibility(View.VISIBLE);

                }else{
                    morepaper_view.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_hide));
                    index_background.setVisibility(View.GONE);
                    morepaper_view.setVisibility(View.GONE);
                }
                break;
            case R.id.morepaper_add:
                paper.add(new String[]{index_webView.getTitle(),index_webView.getUrl()});
                morepaper_list.setAdapter(new MorepaperAdapter(this, R.layout.item_morepaper, paper));
                morepaper_view.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_hide));
                index_background.setVisibility(View.GONE);
                morepaper_view.setVisibility(View.GONE);
                morepaper_num.setText(""+morepaper_list.getAdapter().getCount());
                index_bottom_menu_gohome.callOnClick();
                break;
            case R.id.index_bottom_menu_more:
                //菜单栏
                if(morepaper_view.getVisibility()==View.VISIBLE){
                    morepaper_view.setVisibility(View.GONE);
                }
                if(bottom_dialog.getVisibility()==View.GONE){
                    animation = AnimationUtils.loadAnimation(this,R.anim.menu_show);
                    index_background.setVisibility(View.VISIBLE);
                    bottom_dialog.startAnimation(animation);
                    bottom_dialog.setVisibility(View.VISIBLE);

                }else{
                    bottom_dialog.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_hide));
                    index_background.setVisibility(View.GONE);
                    bottom_dialog.setVisibility(View.GONE);
                }
                break;
            case R.id.search_title_url_clear:
                //清空地址栏
                search_title_edit.setText("");
                break;
            case R.id.index_title_refresh:
                //刷新
                index_webView.reload();
                break;
            case R.id.index_background:
                if(bottom_dialog.getVisibility()==View.VISIBLE){
                    bottom_dialog.setVisibility(View.GONE);
                    index_background.setVisibility(View.GONE);
                }else {
                    morepaper_view.setVisibility(View.GONE);
                    index_background.setVisibility(View.GONE);
                }
                break;
            case R.id.popup_exit:
                finish();
                onDestroy();
                System.exit(0);//整个进程
                break;

            case R.id.popup_add_bookmark:
                //add_bookmark_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_show));
                //add_bookmark_view.setVisibility(View.VISIBLE);
                //index_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_wait));
                //index_view.setVisibility(View.GONE);
                addBookmark();
                bottom_dialog.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_hide));
                index_background.setVisibility(View.GONE);
                bottom_dialog.setVisibility(View.GONE);
                break;
            case R.id.add_bookmark_goback:
                add_bookmark_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_hide));
                add_bookmark_view.setVisibility(View.GONE);
                index_view.setVisibility(View.VISIBLE);
                break;
            case R.id.add_bookmark_ok:
                //添加收藏
                //addBookmark();
               // if (result) {
                    //add_bookmark_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_hide));
                    //add_bookmark_view.setVisibility(View.GONE);
                    //index_view.setVisibility(View.VISIBLE);
               // }
               break;

            case R.id.popup_bookmark:
                bookmark_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_show));
                bookmark_view.setVisibility(View.VISIBLE);
                index_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_wait));
                index_view.setVisibility(View.GONE);
                index_background.setVisibility(View.GONE);
                bottom_dialog.setVisibility(View.GONE);
                getAllBookmark();
                break;
            case R.id.bookmark_goback:
                bookmark_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_hide));
                bookmark_view.setVisibility(View.GONE);
                index_view.setVisibility(View.VISIBLE);
                break;

            case R.id.history_clear:

                HistoryModel historyModel = new HistoryModel(history_view.getContext(), "history", null, 1);
                SQLiteDatabase db = historyModel.getWritableDatabase();
                db.execSQL("delete from history");
                getAllHistory();
                break;

	        case R.id.popup_history:
                history_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_show));
                history_view.setVisibility(View.VISIBLE);
                index_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_wait));
		        index_view.setVisibility(View.GONE);
		        index_background.setVisibility(View.GONE);
		        bottom_dialog.setVisibility(View.GONE);
		        getAllHistory();
		        break;
	        case R.id.history_goback:
                history_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_hide));
		        history_view.setVisibility(View.GONE);
		        index_view.setVisibility(View.VISIBLE);
		        break;
            case R.id.add_download_goback:
                add_download_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_hide));
                add_download_view.setVisibility(View.GONE);
                index_view.setVisibility(View.VISIBLE);
                break;
            case R.id.add_download_ok:
                //添加下载
                boolean result = addDownload();
                if (result) {
                    add_download_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_hide));
                    add_download_view.setVisibility(View.GONE);
                    index_view.setVisibility(View.VISIBLE);
                }
                break;


            case R.id.popup_download:
                download_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_show));
                download_view.setVisibility(View.VISIBLE);
                index_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_wait));
		        index_view.setVisibility(View.GONE);
		        index_background.setVisibility(View.GONE);
		        bottom_dialog.setVisibility(View.GONE);
		        //初始化时加载列表，此处不刷新
//		        getAllDownload();
		        break;
	        case R.id.download_goback:
                download_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_hide));
                download_view.setVisibility(View.GONE);
		        index_view.setVisibility(View.VISIBLE);
		        break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String url;
        switch (view.getId()) {
            case R.id.item_bookmark_view:
                url = (String) ((TextView) view.findViewById(R.id.item_bookmark_url)).getText();
                search_title_edit.setText(url);
                search_title_go.callOnClick();
                bookmark_goback.callOnClick();
                break;
            case R.id.item_history_view:
                url = (String) ((TextView) view.findViewById(R.id.item_history_url)).getText();
                search_title_edit.setText(url);
                search_title_go.callOnClick();
                history_goback.callOnClick();
                break;
            case R.id.item_morepaper:
                url = paper.get(position)[1];
                index_background.callOnClick();
                search_title_edit.setText(url);
                search_title_go.callOnClick();
                break;

        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && index_webView.canGoBack() && index_view.getVisibility()==View.VISIBLE) {
            index_webView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        } else if ((keyCode == KeyEvent.KEYCODE_BACK) && bottom_dialog.getVisibility()==View.VISIBLE) {
            bottom_dialog.setAnimation(AnimationUtils.loadAnimation(this,R.anim.menu_hide));
            index_background.setVisibility(View.GONE);
            bottom_dialog.setVisibility(View.GONE);
            return true;
        } else if ((keyCode == KeyEvent.KEYCODE_BACK) && search_view.getVisibility()==View.VISIBLE) {
            index_view.setVisibility(View.VISIBLE);
            search_view.setVisibility(View.GONE);
            search_title_edit.clearFocus();
            return true;
        } else if ((keyCode == KeyEvent.KEYCODE_BACK) && add_bookmark_view.getVisibility()==View.VISIBLE) {
            add_bookmark_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_hide));
            add_bookmark_view.setVisibility(View.GONE);
            index_view.setVisibility(View.VISIBLE);
            return true;
        } else if ((keyCode == KeyEvent.KEYCODE_BACK) && bookmark_view.getVisibility()==View.VISIBLE) {
            bookmark_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_hide));
            bookmark_view.setVisibility(View.GONE);
            index_view.setVisibility(View.VISIBLE);
            return true;
        }else if ((keyCode == KeyEvent.KEYCODE_BACK) && history_view.getVisibility()==View.VISIBLE) {
            history_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_hide));
            history_view.setVisibility(View.GONE);
            index_view.setVisibility(View.VISIBLE);
            return true;
        }else if ((keyCode == KeyEvent.KEYCODE_BACK) && add_download_view.getVisibility()==View.VISIBLE) {
            add_download_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_hide));
            add_download_view.setVisibility(View.GONE);
            index_view.setVisibility(View.VISIBLE);
            return true;
        }else if ((keyCode == KeyEvent.KEYCODE_BACK) && download_view.getVisibility()==View.VISIBLE) {
            download_view.startAnimation(AnimationUtils.loadAnimation(this,R.anim.view_hide));
            download_view.setVisibility(View.GONE);
            index_view.setVisibility(View.VISIBLE);
            return true;
        }
        else if((keyCode == KeyEvent.KEYCODE_BACK)&&(!index_webView.canGoBack())){
            AlertDialog ad = new AlertDialog.Builder(this).setTitle("确认退出吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //退出APP
                            popup_exit.callOnClick();
                        }
                    })
                    .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //nothing to do
                        }
                    })
                    .show();
        }
        return false;
    }

    private void initHome(){
        //初始化浏览器
        webSettings = index_webView.getSettings();
        webSettings.setBuiltInZoomControls(false); // 设置显示缩放按钮
        webSettings.setJavaScriptEnabled(true);//支持JavaScript
        webSettings.setAllowFileAccess(true);//设置可以访问文件
        webSettings.setSupportZoom(true);
        webSettings.setBlockNetworkImage(false);//显示图片

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){//5.0以上https图片显示问题修复
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        homeWebViewClient = new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                web_title = url;
                index_title_edit.setText(url);
                search_title_edit.setText(url);
                index_title_progress.setVisibility(View.VISIBLE);//设置进度条可见
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(url.equals(home_url+"/")){
                    index_bottom_menu_gohome.setVisibility(View.GONE);
                    index_bottom_menu_nogohome.setVisibility(View.VISIBLE);
                }else{
                    index_bottom_menu_gohome.setVisibility(View.VISIBLE);
                    index_bottom_menu_nogohome.setVisibility(View.GONE);
                }

                if(index_webView.canGoForward()){
                    index_bottom_menu_goforward.setVisibility(View.VISIBLE);
                    index_bottom_menu_nogoforward.setVisibility(View.GONE);
                }else{
                    index_bottom_menu_goforward.setVisibility(View.GONE);
                    index_bottom_menu_nogoforward.setVisibility(View.VISIBLE);
                }

                if(index_webView.canGoBack()){
                    index_bottom_menu_goback.setVisibility(View.VISIBLE);
                    index_bottom_menu_nogoback.setVisibility(View.GONE);
                }else{
                    index_bottom_menu_goback.setVisibility(View.GONE);
                    index_bottom_menu_nogoback.setVisibility(View.VISIBLE);
                }

                //设置地址栏内容为标题
                String title = view.getTitle().trim();
                if (!title.equals("")) {
                    index_title_edit.setText(title);
                }

                if (!title.equals("")) {
                    add_bookmark_name.setText(title);
                    add_bookmark_url.setText(url);
                } else {
                    add_bookmark_name.setText("");
                    add_bookmark_url.setText("");
                }
                //添加历史
                //是否无痕浏览
                if(!isnocache){
                    addHistory(view);
                }
           }

        };


        homeWebChromeClient = new WebChromeClient(){
            //设置进度条
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress==100){
                    index_title_progress.setVisibility(View.GONE);
                }else{
                    index_title_progress.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        };


        index_webView.setWebChromeClient(homeWebChromeClient);
        index_webView.setWebViewClient(homeWebViewClient);

        //有没有下载发生
        index_webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                String filename = NetUtil.getFileName(url);
                add_download_name.setText(filename);
                add_download_url.setText(url);
                add_download_view.setVisibility(View.VISIBLE);
                add_download_view.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.view_show));
                index_view.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.view_wait));
                index_view.setVisibility(View.GONE);
            }
        });

        index_webView.loadUrl(home_url);
    }

    TextWatcher search_title_edit_changed = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //是否显示清除
            //是否显示前往
            //是否显示取消
            String edit = search_title_edit.getText().toString();
            if(edit.length()>0){
                search_title_url_clear.setVisibility(View.VISIBLE);
                search_title_go.setVisibility(View.VISIBLE);
                search_title_cancel.setVisibility(View.GONE);
            }else{
                search_title_url_clear.setVisibility(View.GONE);
                search_title_go.setVisibility(View.GONE);
                search_title_cancel.setVisibility(View.VISIBLE);
            }

            if (NetUtil.isHttpUrl(edit)) {
                search_title_go.setText("前往");
                title_top_earth.setImageResource(R.mipmap.wangluo);

            } else {
                search_title_go.setText("搜索");
                title_top_earth.setImageResource(R.mipmap.app_web_browser_sm);
            }

        }
    };


    private boolean addBookmark() {
        BookmarkModel bookmarkModel = new BookmarkModel(getApplicationContext(), "bookmark", null, 1);
        SQLiteDatabase db = bookmarkModel.getWritableDatabase();
        String title = add_bookmark_name.getText().toString();
        String url = add_bookmark_url.getText().toString();
        if ("".equals(title) || "".equals(url)) {
            Toast.makeText(this, "标题或地址不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        //bookmarkModel.addBookmark(db, title, url);
        int result = bookmarkModel.addBookmark(db, title, url);
        if (result==1) {
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            return true;
        } else if(result==0){
            Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            Toast.makeText(this, "不要重复收藏", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    public void getAllBookmark() {
        BookmarkModel bookmarkModel = new BookmarkModel(this, "bookmark", null, 1);
        SQLiteDatabase db = bookmarkModel.getReadableDatabase();
        ArrayList<Bookmark> allBookmark = bookmarkModel.getAllBookmark(db);
        bookmark_list.setAdapter(new BookmarkAdapter(this, R.layout.item_bookmark, allBookmark));
    }

    private boolean addHistory(WebView view) {
        HistoryModel historyModel = new HistoryModel(this, "history", null, 1);
        SQLiteDatabase db = historyModel.getWritableDatabase();
        String title = view.getTitle();
        String url = view.getUrl();
        if ("".equals(title) || "".equals(url)) {
            Toast.makeText(this, "标题或地址不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean result = historyModel.addHistory(db, title, url);
//        if (result) {
//            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
//            return true;
//        } else {
//            Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return result;
    }

    public void getAllHistory() {
        HistoryModel historyModel = new HistoryModel(this, "history", null, 1);
        SQLiteDatabase db = historyModel.getReadableDatabase();
        ArrayList<History> allHistory = historyModel.getAllHistory(db);
        history_list.setAdapter(new HistoryAdapter(this, R.layout.item_history, allHistory));
    }

    private boolean addDownload() {
        DownloadModel downloadModel = new DownloadModel(this, "download", null, 1);
        SQLiteDatabase db = downloadModel.getWritableDatabase();
        String title = add_download_name.getText().toString();
        String url = add_download_url.getText().toString();
        if ("".equals(title) || "".equals(url)) {
            Toast.makeText(this, "标题或地址不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean result = downloadModel.addDownload(db, title, url);//开始下载
        if (result) {
            ((DownloadAdapter)download_list.getAdapter()).mDownloadInfo = downloadModel.getAllDownload(db);
            download_list.getAdapter().notifyItemInserted(0);
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void getAllDownload() {
        DownloadModel downloadModel = new DownloadModel(this, "download", null, 1);
        SQLiteDatabase db = downloadModel.getWritableDatabase();
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        //设置RecyclerView 布局
        download_list.setLayoutManager(layoutmanager);
        download_list.setAdapter(new DownloadAdapter(this, downloadModel.getAllDownload(db)));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBManager.getInstance(this).closeDb();
    }

    public class MorepaperAdapter extends ArrayAdapter{
        TextView item_morepaper_title;
        ImageView item_morepaper_delete;
        ArrayList<String[]> title;
        public MorepaperAdapter(@NonNull Context context, int resource , ArrayList<String[]> s) {
            super(context, resource);
            title = s;
            System.out.println("shili "+title);
        }

        @Override
        public int getCount() {
            return title.size();
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_morepaper, parent,false);
                System.out.println("creat view");
            } else {
                view = convertView;
                System.out.println("No creat view");
            }

            item_morepaper_title = (TextView)view.findViewById(R.id.item_morepaper_title);
            item_morepaper_delete = (ImageView) view.findViewById(R.id.item_morepaper_delete);

            item_morepaper_title.setText(title.get(position)[0]);
            System.out.println(title);
            item_morepaper_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getCount()==1){
                        AlertDialog ad = new AlertDialog.Builder(getContext()).setTitle("确认退出吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //退出APP
                                        finish();
                                        onDestroy();
                                        System.exit(0);//整个进程
                                    }
                                })
                                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //nothing to do
                                    }
                                })
                                .show();

                    }
                    else{
                        title.remove(position);
                        morepaper_num.setText(""+getCount());
                        notifyDataSetChanged();
                    }

                }
            });

            return view;
        }


    }

}
