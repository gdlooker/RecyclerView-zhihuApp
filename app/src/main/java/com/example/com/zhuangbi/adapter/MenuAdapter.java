package com.example.com.zhuangbi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.com.zhuangbi.bean.NewsMenuItem;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Administrator on 2017/2/21.
 * 左侧菜单
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    public Context context=null;
    public List<NewsMenuItem> newsMenuItems=null;

    View itemView=null;
    public MenuAdapter(Context context){
        this.context=context;
        newsMenuItems=new ArrayList<>();
    }
    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemView=LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,parent,false);
        ViewHolder viewHolder=new ViewHolder(itemView);
        return viewHolder;
    }

    /**
     * 绑定ViewHolder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(MenuAdapter.ViewHolder holder, final int position) {




        //设置左侧标题数据做显示
        holder.itemTitle.setText(newsMenuItems.get(position).getName());

        //项的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //监听  判断 你set方法传入的这个接口是不是
                if (mOnItemClickListener!=null){
                    //
                    mOnItemClickListener.onItemSelected(newsMenuItems,position);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        if (newsMenuItems==null){
            return 0;
        }
        return newsMenuItems.size();
    }

    //刷新数据
    public void addList(List<NewsMenuItem> newsMenuItemList){
        if (newsMenuItemList==null){
            return;
        }
        if (newsMenuItemList.size()==0){
            return;
        }
        //集合添加集合
        newsMenuItems.addAll(newsMenuItemList);
        //刷新适配器
        this.notifyDataSetChanged();

    }


    /**
     *
     */
    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView itemTitle=null;
        public ViewHolder(View itemView) {
            super(itemView);
            itemTitle= (TextView) itemView.findViewById(android.R.id.text1);
        }
    }

    //定义接口变量

    public OnItemClickListener mOnItemClickListener=null;


    /***
     * 这个方法提供给外部进行使用
     */

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){


        this.mOnItemClickListener=mOnItemClickListener;
    }


    /**
     * 给RecyclerView做点击事件
     */

   public interface OnItemClickListener{

        void onItemSelected(List<NewsMenuItem> newsMenuItemList, int position);
    }
}
