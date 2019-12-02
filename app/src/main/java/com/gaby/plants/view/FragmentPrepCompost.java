package com.gaby.plants.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.gaby.plants.R;
import com.gaby.plants.utils.FragmentUtils;
import com.gaby.plants.viewmodel.GardenViewModel;

import pl.droidsonroids.gif.GifImageView;

public class FragmentPrepCompost extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preparando_compost, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GardenViewModel vm = ViewModelProviders.of(this.getActivity()).get(GardenViewModel.class);
        long newPlantId = vm.getNewPlantId();
        vm.changePrepCompost(newPlantId);

        GifImageView imageView = this.getView().findViewById(R.id.preparando_compost);
        TextView titleView = this.getView().findViewById(R.id.preparando_compost_title);
        TextView descView = this.getView().findViewById(R.id.preparando_compost_desc);
        descView.setAlpha(0f);
        Button btnView = this.getView().findViewById(R.id.btnContinuarPrepCompost);
        btnView.setAlpha(0f);
        btnView.setOnClickListener(v -> FragmentUtils.showArcore(this.getActivity()));

        ObjectAnimator animImgScaleX = ObjectAnimator.ofFloat(imageView, View.SCALE_X, .5f).setDuration(1500);
        animImgScaleX.setInterpolator(new OvershootInterpolator());
        ObjectAnimator animImgScaleY = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, .5f).setDuration(1500);
        animImgScaleY.setInterpolator(new OvershootInterpolator());
        ObjectAnimator animImgTranslationY = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_Y, -650).setDuration(1500);
        animImgTranslationY.setInterpolator(new OvershootInterpolator());
        ObjectAnimator animTitleTranslationY = ObjectAnimator.ofFloat(titleView, View.TRANSLATION_Y, -900).setDuration(1500);
        animTitleTranslationY.setInterpolator(new OvershootInterpolator());

        ObjectAnimator animPrepDesc = ObjectAnimator.ofFloat(descView, View.ALPHA, 1f).setDuration(1000);
        ObjectAnimator animPrepDescTranslationY = ObjectAnimator.ofFloat(descView, View.TRANSLATION_Y, -850).setDuration(1000);
        ObjectAnimator animPrepBtnTranslationY = ObjectAnimator.ofFloat(btnView, View.TRANSLATION_Y, -400).setDuration(100);
        ObjectAnimator animPrepBtn = ObjectAnimator.ofFloat(btnView, View.ALPHA, 1f).setDuration(400);

        AnimatorSet animator = new AnimatorSet();
        animator.play(animImgScaleX).after(2000);
        animator.play(animImgScaleX).with(animImgScaleY);
        animator.play(animImgScaleX).with(animImgTranslationY);
        animator.play(animImgScaleX).with(animTitleTranslationY);
        animator.play(animImgScaleX).before(animPrepDesc);
        animator.play(animPrepDesc).with(animPrepDescTranslationY);
        animator.play(animPrepDescTranslationY).before(animPrepBtnTranslationY);
        animator.play(animPrepBtnTranslationY).before(animPrepBtn);
        animator.start();
    }
}
