package com.example.com.zhuangbi.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.com.zhuangbi.R;
import com.example.com.zhuangbi.bean.StoriesEntity;
import com.example.com.zhuangbi.imp.Constant;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Administrator on 2016/12/31.
 * 添加头部跟尾部适配器
 */

public class NewsItemChentHeaderFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int ITEM_HEAD = 0;
    private int ITEM_COUNT = 1;
    private int ITEM_FOOT = 2;
    private List<StoriesEntity> stories;
    private Context context;
    private final LayoutInflater mInflater;
    private View itemView;
//    private int headSize = 0;
//    private int footSize;
//    private boolean isAddHead;
//    private boolean isAddFoot;


    private List<View> headViews;
    private List<View> footViews;
    //这个是父类那个RecyCleViw.ViewHoloder
    RecyclerView.ViewHolder viewHolder = null;
    private int headSize;
    private int currentPosition;

    /**
     * @param context
     */
    public NewsItemChentHeaderFooterAdapter(Context context) {
        this.context = context;
        stories = new ArrayList<>();
        headViews = new ArrayList<>();
        footViews = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 实例化ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_HEAD) {
            itemView = headViews.get(currentPosition);//
            viewHolder = new HeadViewHolder(itemView);
        } else if (viewType == ITEM_COUNT) {
            itemView = mInflater.inflate(R.layout.main_news_item, parent, false);
            viewHolder = new ViewHolder(itemView);
        } else if (viewType == ITEM_FOOT) {
            itemView = footViews.get(currentPosition - headViews.size() - stories.size());
            viewHolder = new FootViewHolder(itemView);
        }
        return viewHolder;
    }

    /**
     * 绑定ViewHolder
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int position) {


        if (holder instanceof HeadViewHolder){
            Log.i("chenzb","res=head");
        }
        if (holder instanceof ViewHolder) {
            Log.i("position", "itemposition=" + position);
            Log.i("chent", "position=" + position);
            final int itemPosition= position - headViews.size();

            //视图绑定
            //img
            if (stories.get(itemPosition).getType() ==Constant.TYPE) {//是要添加的标题

                ((ViewHolder) holder).itemContent.setVisibility(View.GONE);
                ((ViewHolder) holder).tv_topic.setVisibility(View.VISIBLE);
                //设置热点新闻
                ((ViewHolder) holder).tv_topic.setText(stories.get(itemPosition).getTitle());
                Log.i("chenzb", "bbGe=if");
            } else {
                Log.i("chenzb", "bbGe=else");
                //获取到最好的的
                final StoriesEntity storiesEntity =  stories.get(itemPosition);
                List<String> images = storiesEntity.getImages();
                if (images!=null&&!images.isEmpty()){
                    String imgUrl = stories.get(itemPosition).getImages().get(0);//只有一张图片
                    ((ViewHolder) holder).tv_topic.setVisibility(View.GONE);
                    ((ViewHolder) holder).itemContent.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(imgUrl)) {
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setUri(Uri.parse(imgUrl))
                                .build();

                        ((ViewHolder) holder).simpleDraweeView.setController(controller);

                    }
                }
                ((ViewHolder) holder).tv_title.setText(stories.get(itemPosition).getTitle());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (mOnItemClickListener!=null){
                         mOnItemClickListener.onItemSelected(stories,itemPosition);
                        }
                    }
                });
            }
        }
    }


    /**
     * 返回长度
     *
     * @return
     */
    @Override
    public int getItemCount() {

        if (stories == null) {
            return 0;
        }
        return stories.size() + headViews.size() + footViews.size();
    }

    /**
     * 获取某一项的视图类型
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        currentPosition = position;
        int headSize = headViews.size();
        int footSize = footViews.size();
        if (headSize > 0 && position < headSize) {
            return ITEM_HEAD;
        } else if (footSize > 0 && position >= headSize + stories.size()) {// 3  5     ===== 0   ---7
            return ITEM_FOOT;
        } else {
            return ITEM_COUNT;
        }

    }

    /**
     * 加载数据
     */

    public void addList(List<StoriesEntity> storiesList) {
        if (storiesList == null) {
            return;
        }
        if (storiesList.size() == 0) {
            return;
        }
        Log.i("chent","NewsItemData="+storiesList.toString());
        stories.addAll(storiesList);//集合添加集合
        //刷新数据
        this.notifyDataSetChanged();
    }

    public void addHeadView(View headView) {
        headViews.add(headView);
        //让它的size加1
        //headSize++;
        //booolean
        //isAddHead=true;
        this.notifyDataSetChanged();
    }

    public void addFootView(View footView) {
        footViews.add(footView);
        //isAddFoot=true;
        this.notifyDataSetChanged();
    }

    /**
     * ViewHoloder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {


        TextView tv_topic;
        TextView tv_title;
        SimpleDraweeView simpleDraweeView = null;
        RelativeLayout itemContent = null;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_topic = (TextView) itemView.findViewById(R.id.tv_topic);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.iv_title);
            itemContent = (RelativeLayout) itemView.findViewById(R.id.itemContent);
        }
    }

    /**
     * 头部
     */
    static class HeadViewHolder extends RecyclerView.ViewHolder {


        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 底部
     */
    static class FootViewHolder extends RecyclerView.ViewHolder {


        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }


    private OnItemClickListener mOnItemClickListener=null;



    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener=mOnItemClickListener;
    }

    /**
     *
     */
    public interface OnItemClickListener{

        void onItemSelected(List<StoriesEntity> storiesEntities, int position);
    }
}
