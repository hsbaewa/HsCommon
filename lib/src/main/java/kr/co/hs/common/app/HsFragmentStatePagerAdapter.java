package kr.co.hs.common.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.HashMap;

/**
 * Created by Bae on 2016-12-27.
 */
public abstract class HsFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private HashMap<Integer, Boolean> mReloadState;

    public HsFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        mReloadState = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {
        HsFragment hsFragment = getHsFragmentItem(position);
        hsFragment.setPagerAdapterPosition(position);
        mReloadState.put(position, false);
        return hsFragment;
    }

    @Override
    public int getItemPosition(Object object) {
        if(object instanceof HsFragment){
            HsFragment hsFragment = (HsFragment) object;
            if(isReloadState(hsFragment)){
                setReloadState(hsFragment.getPagerAdapterPosition(), false);
                return POSITION_NONE;
            }
        }
        return super.getItemPosition(object);
    }

    private boolean isReloadState(HsFragment fragment){
        try{
            int position = fragment.getPagerAdapterPosition();
            if(position < 0)
                return false;
            return mReloadState.get(position);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    private void setReloadState(int position, boolean isReload){
        mReloadState.put(position, true);
    }

    public void notifyDataSetChanged(int position) {
        setReloadState(position, true);
        super.notifyDataSetChanged();
    }
    @Override
    public void notifyDataSetChanged() {
        for(int i=0;i<getCount();i++){
            setReloadState(i, true);
        }
        super.notifyDataSetChanged();
    }

    public abstract HsFragment getHsFragmentItem(int position);
}
