package com.bigggfish.slidelistview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * Created by bigggfish on 2016/12/1.
 * description:
 */
public class SlideDeleteListView extends ListView {

    private SlideView mFocusedItemView;


    public SlideDeleteListView(Context context) {
        super(context);
    }

    public SlideDeleteListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideDeleteListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {

                int position = pointToPosition(x, y);
                if (position != INVALID_POSITION) {
                    mFocusedItemView = (SlideView) getChildAt(position - getFirstVisiblePosition());
                } else {
                    mFocusedItemView = null;
                }
            }
            default:
                break;
        }
        if (mFocusedItemView != null) {
            boolean isScroll = mFocusedItemView.onRequireTouchEvent(event);
            if (isScroll) {
                //return true;
                event.setAction(MotionEvent.ACTION_CANCEL);
            }
        }
        return super.onTouchEvent(event);
    }



    ///////////////////////////////////////////////////////////////////////////////////////////////

    /***
     * 因为需要在adapter中操作item和各个item之间的关系。
     * 所以给SlideDeleteListView设置的adapter，必须继承自SlideBaseAdapter
     */
    static abstract class SlideBaseAdapter<T extends ViewHolder> extends BaseAdapter{

        private int mItemLayoutRes;

        private Context mContext;
        private LayoutInflater mInflater;
        private OnDeleteClickListener listener;
        private SlideView mLastSlideViewWithStatusOn;

        public SlideBaseAdapter(Context context, @LayoutRes int itemLayoutRes) {
            this.mItemLayoutRes = itemLayoutRes;
            this.mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            T viewHolder;
            SlideView slideView = (SlideView) convertView;
            if(slideView == null){
                View itemView = mInflater.inflate(mItemLayoutRes, null);
                slideView = new SlideView(mContext);
                slideView.setContentView(itemView);
                slideView.setOnSlideListener(new SlideListener());
                viewHolder = getViewHolder(slideView);
                slideView.setTag(viewHolder);
            }else{
                slideView.scrollToStart();
                viewHolder = (T)convertView.getTag();
            }
            viewHolder.deleteHolder.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onClick(position);
                }
            });
            convert(viewHolder, position);
            return slideView;
        }

        public abstract T getViewHolder(View itemView);

        public abstract void convert(T viewHolder, int position);

        private class SlideListener implements SlideView.OnSlideListener {

            @Override
            public void onSlide(View view, int status) {
                if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
                    mLastSlideViewWithStatusOn.shrink();
                    mLastSlideViewWithStatusOn = null;
                }

                if (status == SLIDE_STATUS_ON) {
                    mLastSlideViewWithStatusOn = (SlideView) view;
                }
            }
        }

        //点击删除按钮监听接口
        public interface OnDeleteClickListener {
            void onClick(int position);
        }

        public void setOnDeleteClickListener(OnDeleteClickListener listener) {
            this.listener = listener;
        }

    }

    /**
     * ViewHolder也必须继承本ViewHolder
     */
    static abstract class ViewHolder {
        protected View  slideView;
        protected ViewGroup deleteHolder;

        public ViewHolder(View itemView) {
            if(itemView == null){
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.slideView = itemView;
            deleteHolder = (ViewGroup) slideView.findViewById(R.id.holder);
        }
    }


}