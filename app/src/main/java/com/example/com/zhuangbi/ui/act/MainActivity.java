package com.example.com.zhuangbi.ui.act;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.example.com.zhuangbi.R;
import com.example.com.zhuangbi.imp.Constant;
import com.example.com.zhuangbi.ui.fragment.MainFragment;


/**
 * 主页面
 */
public class MainActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout = null;//侧拉布局
    private Toolbar mToolbar = null;//toolBar
    private SwipeRefreshLayout mSwipeRefreshLayout = null;//滑动的

    private FrameLayout mainContainer = null;//帧布局


    /**
     * 初始化方法
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawerlayout);
        mToolbar = (Toolbar) this.findViewById(R.id.toolBar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.sr);
        mainContainer = (FrameLayout) this.findViewById(R.id.mainContainer);
        //mToolbar.setTitle("陈装逼");//这个方法要在setSupportActionBar之前调用 或者在onResume方法里面调用
        setSupportActionBar(mToolbar); //设置toolBar
        //设置进度条的背景颜色
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(this, R.color.progress_color));
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);//设置进度条很大
        mSwipeRefreshLayout.setOnRefreshListener(new MsrOnRefreshListener());
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, android.R.color.holo_blue_light),
                ContextCompat.getColor(this, android.R.color.holo_green_dark),
                ContextCompat.getColor(this, android.R.color.holo_red_light)

        );
        /**
         * 设置Drawerlayout的侧拉菜单
         */
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(
                this,//上下文参数
                mDrawerLayout,//侧拉布局
                mToolbar,//设置toolBar
                R.string.toggle_open,
                R.string.toggle_close
        );

        //侧拉菜单
        actionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        //执行
        //动态加载最新的
        loadLatestFragment();
    }

    /**
     * 加载最新的Fragment
     */
    private void loadLatestFragment() {

        FragmentManager fragmentManager=getSupportFragmentManager();

        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.mainContainer,new MainFragment(), Constant.MAIN_TAG);

        fragmentTransaction.commit();//提交事物
    }


    /**
     * 设置标题
     */
    @Override
    protected void onResume() {
        super.onResume();
        mToolbar.setTitle("装逼陈");
    }


    public void closeDrawer(){
        mDrawerLayout.closeDrawers();
    }
    //handler对象
    Handler handler=new Handler();

    /**
     * 下拉接口
     */
    class MsrOnRefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            //在主线程里面给个3秒钟的延迟
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            },3000);
        }
    }




}
