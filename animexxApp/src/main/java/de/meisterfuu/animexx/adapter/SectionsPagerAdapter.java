package de.meisterfuu.animexx.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private List<FragmentHolder> mFragments = new ArrayList<FragmentHolder>();

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new ArrayList<FragmentHolder>();
    }

    @Override
    public int getItemPosition(Object object) {
        return FragmentPagerAdapter.POSITION_NONE;
        //return mFragments.indexOf(mFragments);
    }

    @Override
    public long getItemId(int position) {
        return mFragments.get(position).getId();
    }

    public void addFragment(FragmentHolder pHolder) {
        mFragments.add(pHolder);
    }

    public void addFragmentBeginning(FragmentHolder pHolder) {
        mFragments.add(0, pHolder);
    }

    public void addFragmentAt(int pPos, FragmentHolder pHolder) {
        mFragments.add(pPos, pHolder);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position).getFragment();
    }

    public FragmentHolder getHolder(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragments.get(position).getTitle();
    }

    public static class FragmentHolder {

        private String title;
        private String tag;
        private Fragment fragment;
        private long id;

        public FragmentHolder(String title, String tag, Fragment fragment, long id) {
            this.title = title;
            this.tag = tag;
            this.fragment = fragment;
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }

}