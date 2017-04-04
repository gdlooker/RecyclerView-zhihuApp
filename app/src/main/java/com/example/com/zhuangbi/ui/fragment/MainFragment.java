package com.example.com.zhuangbi.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.com.zhuangbi.R;
import com.example.com.zhuangbi.adapter.ChentHeaderFooterAdapter;
import com.example.com.zhuangbi.base.BaseFragment;
import com.example.com.zhuangbi.bean.Before;
import com.example.com.zhuangbi.bean.Latest;
import com.example.com.zhuangbi.bean.StoriesEntity;
import com.example.com.zhuangbi.customview.Kanner;
import com.example.com.zhuangbi.imp.Constant;
import com.example.com.zhuangbi.ui.act.MainActivity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

import static android.R.attr.x;
import static android.R.attr.y;

/**
 * Created by Administrator on 2017/2/21.
 * 主界面
 */

public class MainFragment extends BaseFragment {


    private View view = null;
    private RecyclerView mRecyclerView = null;

    private View headView = null;
    private Latest latest;
    private String date;
    private ChentHeaderFooterAdapter mChentHeaderFooterAdapter;


    private Kanner kanner = null;
    private SimpleDraweeView simpleDraweeView = null;
    private MainActivity mainActivity;
    private LinearLayoutManager linearLayoutManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //上下文
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
        view = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        headView = inflater.inflate(R.layout.head_layout, container, false);
        // simpleDraweeView = (SimpleDraweeView) headView.findViewById(R.id.simpleDraweeView);
        kanner = (Kanner) headView.findViewById(R.id.kanner);
       /* DraweeController draweeController = (DraweeController) Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(Constant.START_IMG))
                .build();
        simpleDraweeView.setController(draweeController);*/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

         //首次加载数据
        loadFirstData(Constant.BASEURL + Constant.LATESTNEWS);//首次默认加载数据

        setListener();//设置监听


    }

    /**
     * 设置监听
     */
    private void setListener() {
        //添加头布局
        mChentHeaderFooterAdapter.setOnRecyclerViewClickListener(new ChentHeaderFooterAdapter.OnRecyclerViewClickListener() {
            @Override
            public void onItemselected(List<StoriesEntity> storiesEntities, int position) {

                /**
                 * 主页面的列表的点击事件
                 */

                Intent intent=new Intent(context,NewsFragment.LatestContentActivity.class);

                //传递一个对象过来
                StoriesEntity storiesEntity=storiesEntities.get(position);
                intent.putExtra("entity",storiesEntity);
                startActivity(intent);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i(TAG,"onScolled=="+",x="+x+",y="+y);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                int visibleItemCount = linearLayoutManager.getChildCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = linearLayoutManager.getItemCount();
                Log.i(TAG, "first=" + firstVisibleItem + "\nlastItem=" + lastVisibleItem + ",total=" + linearLayoutManager.getItemCount());

                Log.i(TAG, "count1=" + visibleItemCount + "\ncount2=" + totalItemCount);
                //做判断执行刷新操作
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    //加载数据
                    Log.i("chent", "上拉加载");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //加载更多的数据
                            loadMore(Constant.BASEURL + Constant.BEFORE + date);//加载前面的数据
                        }
                    });
                }
            }
        });
    }

    /**
     * 加载更多的数据
     *
     * @param s
     */
    private void loadMore(String s) {
        OkHttpUtils.get()
                .url(s)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //json解析
                        Log.i(TAG, "chenzb666=" + response);
                        parseBeforeJson(response);
                    }
                });
    }

    /**
     * 解析先前的数据
     *
     * @param response
     */
    private void parseBeforeJson(String response) {

        Gson gson = new Gson();
        final Before before = gson.fromJson(response, Before.class);
        //
        if (before == null) {
            return;
        }
        date = before.getDate();
        //
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<StoriesEntity> stories = before.getStories();
                if (stories == null) {
                    return;
                }
                StoriesEntity top = new StoriesEntity();
                top.setType(Constant.TYPE);
                top.setTitle(convertDate(date));
                stories.add(0, top);
                mChentHeaderFooterAdapter.addList(stories);
            }
        });
    }

    /**
     * 首先加载的默认的数据
     *
     * @param latestnews
     */
    private void loadFirstData(String latestnews) {
        /**
         * 主页面的逻辑思想
         * 1：我要在上面的RecyclerView的头部加载一个headView布局
         */
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mChentHeaderFooterAdapter = new ChentHeaderFooterAdapter(context);
        mRecyclerView.setAdapter(mChentHeaderFooterAdapter);//实例化适配器

        OkHttpUtils.get()
                .url(latestnews)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        Log.i(TAG, "response的最新数据=" + response);
                        //进行gson解析
                        parseLatestJson(response);
                    }
                });
    }

    /**
     * gson解析最新的数据
     *
     * @param response
     */
    private void parseLatestJson(String response) {
        Gson gson = new Gson();
        latest = gson.fromJson(response, Latest.class);
        //获取最新的日期
        date = latest.getDate();
        Log.i(TAG, "latestBean=" + latest.toString());
        //设置图片轮播的数据
        kanner.setTopEntities(latest.getTop_stories());//这个就是图片的路径啊====
        mChentHeaderFooterAdapter.addHeadView(headView);//我是说这一步放入到最后面就可以显示
        handler.post(new Runnable() {
            @Override
            public void run() {
                //我现在这里是要获取什么数据啊
                //我是要获取里面的RecyclerView的一个集合
                List<StoriesEntity> stories = latest.getStories();
                //添加一个列表头对象 做标题
                StoriesEntity top = new StoriesEntity();
                top.setType(Constant.TYPE);
                top.setTitle(getResources().getString(R.string.itemNewsTitle));
                //传递给适配器
                stories.add(0, top);
                mChentHeaderFooterAdapter.addList(stories);
            }
        });
    }

    private String convertDate(String date) {
        String result = date.substring(0, 4);
        result += "年";
        result += date.substring(4, 6);
        result += "月";
        result += date.substring(6, 8);
        result += "日";
        return result;
    }

    Handler handler = new Handler();
}
