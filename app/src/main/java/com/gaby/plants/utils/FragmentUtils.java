package com.gaby.plants.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.gaby.plants.R;
import com.gaby.plants.view.FragmentArcore;

import java.util.List;

public class FragmentUtils {
    public static void replaceFragment(FragmentActivity myActivity, Fragment fragment) {
        FragmentTransaction ft = myActivity.getSupportFragmentManager().beginTransaction();
        List<Fragment> fragmentList = myActivity.getSupportFragmentManager().getFragments();

        for (Fragment f : fragmentList) {
            if (f instanceof FragmentArcore) {
                ft.hide(f);
            } else {
                ft.remove(f);
            }
        }
        ft.add(R.id.frameFragment, fragment);
        ft.commit();
    }

    public static void showArcore(FragmentActivity myActivity) {
        FragmentTransaction ft = myActivity.getSupportFragmentManager().beginTransaction();
        List<Fragment> fragmentList = myActivity.getSupportFragmentManager().getFragments();

        boolean arFragmentExist = false;
        for (Fragment f : fragmentList) {
            if (f instanceof FragmentArcore) {
                ft.show(f);
                arFragmentExist = true;
            } else {
                ft.hide(f);
            }
        }
        if (!arFragmentExist) {
            FragmentArcore arFragment = new FragmentArcore();
            ft.add(R.id.frameFragment, arFragment);
        }
        ft.commit();
    }
}
