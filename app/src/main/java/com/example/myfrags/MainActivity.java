package com.example.myfrags;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends FragmentActivity implements  Fragment1.OnButtonClickListener{
    private int [] frames;
    private boolean hidden;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            frames = new int [] {R.id.frame1, R.id.frame2, R.id.frame3, R.id.frame4};
            hidden = false;
            Fragment [] fragments = new Fragment [] {new Fragment1(), new Fragment2(), new Fragment3(), new Fragment4()};
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            for (int i=0; i < 4; i++) {
                fragmentTransaction.add(frames[i], fragments[i]);
            }
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            frames = savedInstanceState.getIntArray("FRAMES");
            hidden = savedInstanceState.getBoolean("HIDDEN");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("FRAMES", frames);
        outState.putBoolean("HIDDEN", hidden);
    }

    @Override
    public void onButtonClickClockwise() {
        int t = frames[0];
        frames[0] = frames[1];
        frames[1] = frames[2];
        frames[2] = frames[3];
        frames[3] = t;

        newFragments();
    }

    @Override
    public void onButtonClickHide() {
        if (hidden) return;
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (Fragment f: fragmentManager.getFragments()) {
            if (f instanceof Fragment1) continue;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(f);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        hidden = true;
    }

    @Override
    public void onButtonClickRestore() {
        if (!hidden) return;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (Fragment f: fragmentManager.getFragments()) {
            if (f instanceof Fragment1) continue;
            fragmentTransaction.show(f);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        hidden = false;
    }

    @Override
    public void onButtonClickShuffle() {
        List<Integer> list = new ArrayList<>(Arrays.asList(frames[0], frames[1], frames[2], frames[3]));
        Collections.shuffle(list);
        for (int i=0; i < 4; i++) {
            frames[i] = list.get(i).intValue();
        }
        newFragments();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof Fragment1) {
            ((Fragment1) fragment).setButtonCLickListener(this);
        }
    }
    private void newFragments() {
        Fragment [] newFragments = new Fragment [] {new Fragment1(), new Fragment2(), new Fragment3(), new Fragment4()};
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < 4; i++) {
            fragmentTransaction.replace(frames[i], newFragments[i]);
            if (hidden && !(newFragments[i] instanceof Fragment1)) fragmentTransaction.hide(newFragments[i]);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}