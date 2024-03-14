package com.example.proyectointegrador.view.utils;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.proyectointegrador.R;
import com.example.proyectointegrador.view.fragments.GastosFragment;
import com.example.proyectointegrador.view.fragments.SaldosFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_gastos, R.string.tab_saldos};
    private final Context mContext;
    private GastosFragment gastosFragment;
    private SaldosFragment saldosFragment;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            gastosFragment = GastosFragment.newInstance();
            return gastosFragment;
        }else{
            saldosFragment = SaldosFragment.newInstance();
            return saldosFragment;
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }

    public GastosFragment getGastosFragment() {
        return gastosFragment;
    }

    public SaldosFragment getSaldosFragment() {
        return saldosFragment;
    }
}