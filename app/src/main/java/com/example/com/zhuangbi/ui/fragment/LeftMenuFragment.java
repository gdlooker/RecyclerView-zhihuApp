package com.example.com.zhuangbi.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.zhuangbi.R;
import com.example.com.zhuangbi.adapter.MenuAdapter;
import com.example.com.zhuangbi.base.BaseFragment;
import com.example.com.zhuangbi.bean.NewsMenuItem;
import com.example.com.zhuangbi.divider.RecycleViewDivider;
import com.example.com.zhuangbi.imp.Constant;
import com.example.com.zhuangbi.ui.act.MainActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by Administrator on 2017/2/21.
 * 左侧菜单的Fragment
 */

public class LeftMenuFragment extends BaseFragment implements View.OnClickListener {

    private View view = null;

    private TextView tvMain = null;
    private RecyclerView mRecyclerView = null;

    public MenuAdapter menuAdapter = null;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private List<NewsMenuItem> newsMenuItemList;
    private MenuAdapterOnClicker menuAdapterOnClicker;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);
        tvMain = (TextView) view.findViewById(R.id.tvMain);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.mRecyclerView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        menuAdapter = new MenuAdapter(context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(menuAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画
        mRecyclerView.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL));
        menuAdapterOnClicker = new MenuAdapterOnClicker();
        menuAdapter.setOnItemClickListener(menuAdapterOnClicker);//recyclerView的监听
        tvMain.setOnClickListener(this);//首页的点击事件
        //发送okHttp进行json解析
        sendOkHttp(Constant.BASEURL + Constant.THEMES);//发送OkHttp请求

    }


    /**
     * 发送OkHttp
     *
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

                        //处理json数据
                        Log.i(TAG, "response=" + response);

                        //首先得到最外面的jsonObject对象
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            //获取里面的json数组 根据key获取
                            JSONArray jsonArray = jsonObject.getJSONArray("others");

                            newsMenuItemList = new ArrayList<NewsMenuItem>();
                            //jsonArray之后  应该进行json遍历
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //获取单个的jsonObect
                                JSONObject singJsonObject = (JSONObject) jsonArray.get(i);
                                //赋值给单个json对象
                                NewsMenuItem newsMenuItem = new NewsMenuItem();
                                newsMenuItem.setDescription(singJsonObject.getString("description"));
                                newsMenuItem.setId(singJsonObject.getString("id"));
                                newsMenuItem.setName(singJsonObject.getString("name"));
                                newsMenuItem.setColor(singJsonObject.getString("color"));
                                newsMenuItem.setThumbnail(singJsonObject.getString("thumbnail"));
                                //丢到集合里面去====这里只用到了2个字段====要传入给NewsFragment 作为它的列表的头布局
                                newsMenuItemList.add(newsMenuItem);
                            }
                            //拿完数据之后
                            //刷新数据
                            menuAdapter.addList(newsMenuItemList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvMain:

                if (context instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) context;
                    mainActivity.closeDrawer();
                    mainActivity.getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainContainer, new MainFragment())
                            .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
                            .commit();
                }

                break;
        }
    }

    /**
     * RecyclerView的点击事件
     */
    class MenuAdapterOnClicker implements MenuAdapter.OnItemClickListener {
        @Override
        public void onItemSelected(List<NewsMenuItem> newsMenuItemList, int position) {

            Toast.makeText(context, "position=" + position, Toast.LENGTH_SHORT).show();
            /**
             * 左侧菜单的点击的是逻辑代码
             * 当我点击的时候 =====让主页面的Fragment做切换
             */
            if (context instanceof MainActivity){
                //step 拿到宿主Act
                MainActivity mainActivity= (MainActivity) context;
                //step2 通过宿主Act来关闭左侧菜单 同时让右边的Fragment做切换
                mainActivity.closeDrawer();
                //
                NewsMenuItem newsMenuItem = newsMenuItemList.get(position);
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainContainer,NewsFragment.newInstance(newsMenuItem.getId(), newsMenuItem.getName()));
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                fragmentTransaction.commit();//提交
            }

        }
    }
}
