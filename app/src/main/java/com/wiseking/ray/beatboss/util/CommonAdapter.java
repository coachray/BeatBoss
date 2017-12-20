package com.wiseking.ray.beatboss.util;

/**
 * Created by guo on 2017/12/10.
 */

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.BaseAdapter;

import com.wiseking.ray.beatboss.R;


public abstract class CommonAdapter<T> extends BaseAdapter
{
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected final int mItemLayoutId;
//    protected final int mBoderId;
    private int defItem=-1;//声明默认选中的项

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId, int myPosition)
    {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
        this.defItem=myPosition;
//        this.mBoderId=boderId;
    }

    @Override
    public int getCount()
    {
        return mDatas.size();
    }

    @Override
    public T getItem(int position)
    {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    /*public void setDefSelect(int position) {
        this.defItem = position;
        notifyDataSetChanged();
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        convert(viewHolder, getItem(position));
        if (defItem!=-1) {     //结束游戏来看成绩
            if (defItem==position) {    //把默认选中的高亮
                View view=viewHolder.getConvertView();

                /*//创建淡入淡出动画
                //创建一个AnimationSet对象，参数为Boolean型，
                //true表示使用Animation的interpolator，false则是使用自己的
                AnimationSet animationSet = new AnimationSet(true);
                //创建一个AlphaAnimation对象，参数从完全的透明度，到完全的不透明
                AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                //设置动画执行的时间
                alphaAnimation.setDuration(2000);
                //将alphaAnimation对象添加到AnimationSet当中
                animationSet.addAnimation(alphaAnimation);
                //使用ImageView的startAnimation方法执行动画
                view.startAnimation(animationSet);*/

                //创建幻灯片动画
                view.setBackgroundResource(R.drawable.anim);
                AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
                animationDrawable.start();

                //没有动画直接设置背景色
//                view.setBackgroundColor(Color.LTGRAY);
//                view.setBackgroundResource(mBoderId);
                defItem=-1;             //重置为-1
            }
        }
        return viewHolder.getConvertView();

    }

    public abstract void convert(ViewHolder helper, T item);

    private ViewHolder getViewHolder(int position, View convertView,
                                     ViewGroup parent)
    {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
                position);
    }

}
