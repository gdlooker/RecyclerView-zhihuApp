package com.example.com.zhuangbi.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.com.zhuangbi.R;
import com.example.com.zhuangbi.adapter.NewsItemChentHeaderFooterAdapter;
import com.example.com.zhuangbi.base.BaseActivity;
import com.example.com.zhuangbi.base.BaseFragment;
import com.example.com.zhuangbi.bean.Content;
import com.example.com.zhuangbi.bean.News;
import com.example.com.zhuangbi.bean.StoriesEntity;
import com.example.com.zhuangbi.imp.Constant;
import com.example.com.zhuangbi.utils.HttpUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;


/**
 * Created by Administrator on 2017/2/22.
 */

public class NewsFragment extends BaseFragment {

    public static final String NEWS_TAG = "newsfragment_tag";
    private View view;

    private ImageLoader mImageLoader;
    private String urlId;
    private String title;

    private RecyclerView mRecyclerView = null;
    private View headView;
    private News news;

    private TextView tv_title;
    private ImageView iv_title;
    private NewsItemChentHeaderFooterAdapter newsItemChentHeaderFooterAdapter;


    /**
     *
     * @param id 从左侧菜单传入过来的Id
     * @param title 标题
     * @return
     */
    public static NewsFragment newInstance(String id, String title) {

        Bundle args = new Bundle();
        args.putString("id",id);
        args.putString("title",title);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 拿到
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            urlId =getArguments().getString("id");
            title = getArguments().getString("title");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.news_layout, container, false);
        mImageLoader = ImageLoader.getInstance();//
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);//listview拿到了

        //加载新闻fragment的一个头布局 这个头布局不是图片的轮播了
        headView = inflater.inflate(R.layout.news_header, container, false);
        iv_title = (ImageView) headView.findViewById(R.id.iv_title);
        tv_title = (TextView) headView.findViewById(R.id.tv_title);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //根据网络
        if(HttpUtils.isNetworkConnected(context)) {
            //加载网络数据
                loadNetData(Constant.BASEURL+Constant.THEMENEWS+urlId);
        }else{
            //
            Toast.makeText(context,"没有网络数据",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 加载网路数据
     * @param url
     */
    private void loadNetData(String url) {

        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                    @Override
                    public void onResponse(String response, int id) {
                        /**
                         * 加载网络数据
                         *
                         */
                        Log.i(TAG,"newResponse="+response);
                        parseJson(response);
                    }
                });
    }

    /**
     * 解析网络数据
     * @param response
     */
    private void parseJson(String response) {
        Log.i(TAG,"response="+response);
        Gson gson=new Gson();
        News news=gson.fromJson(response,News.class);
        //进行数据解析
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        //RecyclerView设置头布局
        tv_title.setText(news.getDescription());//设置项的布局
        mImageLoader.displayImage(news.getImage(), iv_title, options);//加载图片

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //最外面的那个大对象
        List<StoriesEntity> entityList=news.getStories();
        Log.i(TAG,"chenzb=66666"+entityList.toString());
        newsItemChentHeaderFooterAdapter = new NewsItemChentHeaderFooterAdapter(context);
        mRecyclerView.setAdapter(newsItemChentHeaderFooterAdapter);

        //先添加头部
        newsItemChentHeaderFooterAdapter.addHeadView(headView);
        newsItemChentHeaderFooterAdapter.addList(entityList);
        newsItemChentHeaderFooterAdapter.setOnItemClickListener(new NewsItemChentHeaderFooterAdapter.OnItemClickListener() {
            @Override
            public void onItemSelected(List<StoriesEntity> storiesEntities, int position) {

                //点击事件
                StoriesEntity storiesEntity=storiesEntities.get(position);
                Intent intent=new Intent(context, LatestContentActivity.class);
                intent.putExtra("entity",storiesEntity);
                startActivity(intent);
                ((Activity)context).overridePendingTransition(0, 0);
            }
        });
    }

    /**
     * 最新的内容Act
     */
    public static class LatestContentActivity extends BaseActivity {


        /**
         *
         */
        private AppBarLayout mAppBarLayout=null;
        private ImageView iv=null;
        private Toolbar mToolbar=null;
        private WebView mWebView=null;
        private Content content;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_latest_content);
            mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
            iv= (ImageView) this.findViewById(R.id.iv);
            mToolbar=(Toolbar) this.findViewById(R.id.toolBar);
            mWebView= (WebView) this.findViewById(R.id.webview);
            /**
             * 接收RecyclerView点击事件的传递过来的对象
             */
            //获取点击列表传入过来的对象
            StoriesEntity storiesEntity= (StoriesEntity) getIntent().getSerializableExtra("entity");
            Log.i(TAG,"singObj="+storiesEntity.toString());
            //设置toolBar
            setSupportActionBar(mToolbar);
            //
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                    onBackPressed();
                }
            });

            //判断网络是否连接 的
            if (HttpUtils.isNetworkConnected(context)){
                //有网络
                sendOkHttp(Constant.BASEURL+Constant.CONTENT+storiesEntity.getId());
            }else{
                //没有网络

            }
        }

        /**
         * 发送OkHttp请求
         * @param url
         */
        private void sendOkHttp(String url) {
            OkHttpUtils.get()
                    .url(url)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {

                            Log.i(TAG,"responseLatest="+response);
                            parseJson(response);
                        }
                    });
        }

        /**
         * json解析
         */
        private void parseJson(String response) {
            Gson gson=new Gson();
            content = gson.fromJson(response, Content.class);
            Log.i(TAG,"contentUrl="+content.getImage());
            Toast.makeText(this,"头部图片轮播路径："+content.getImage(),Toast.LENGTH_SHORT).show();
            Glide.with(LatestContentActivity.this).load(content.getImage()).asBitmap().into(iv);
            String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
            String html = "<html><head>" + css + "</head><body>" + content.getBody() + "</body></html>";
            html = html.replace("<div class=\"img-place-holder\">", "");
            mWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);

        }


        /**
         * 返回键调用的方法
         */
        @Override
        public void onBackPressed() {
            super.onBackPressed();
            Log.i(TAG,"onBackPressed");
            //调用finis方法
            this.finish();
            //来个动画
            overridePendingTransition(0, R.anim.slide_out_to_left_from_right);
        }
    }
}
