package kr.co.hs.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by Bae on 2016-12-27.
 */
public class HsViewPager extends ViewPager {

    private PageWatcher mPageWatcher = null;


    public HsViewPager(Context context) {
        super(context);
    }

    public HsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setOnPageChangedListener(OnPageChangedListener listener){
        mPageWatcher = new PageWatcher(this, listener);
        super.addOnPageChangeListener(mPageWatcher);
    }


    class PageWatcher implements OnPageChangeListener {

        private HsViewPager mViewPager;
        private OnPageChangedListener mListener;
        private int currentScrollState = 0;
        private int currentPageSelected = 0;

        public PageWatcher(HsViewPager mJiranViewPager, OnPageChangedListener mListener) {
            this.mViewPager = mJiranViewPager;
            this.mListener = mListener;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentPageSelected = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            currentScrollState = state;
            if(currentScrollState == SCROLL_STATE_IDLE && this.mListener != null){
                this.mListener.onPageChanged(mViewPager, currentPageSelected);
            }
        }
    }

    public interface OnPageChangedListener{
        void onPageChanged(HsViewPager viewPager, int position);
    }
}
