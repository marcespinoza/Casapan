package com.casapan.pedidos.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.casapan.pedidos.Vista.FragmentArticulo;
import com.casapan.pedidos.Vista.FragmentConfig;
import com.casapan.pedidos.Vista.FragmentPedidos;

import java.util.ArrayList;
import java.util.List;
public class TabAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public TabAdapter(FragmentManager fm, int tabs) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentPedidos();
            case 1:
                return new FragmentArticulo();
            case 2:
                return new FragmentConfig();
            default:
                return null;
        }
    }
    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
