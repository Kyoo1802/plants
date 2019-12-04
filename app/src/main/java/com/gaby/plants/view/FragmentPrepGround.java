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
import com.gaby.plants.model.Plant;
import com.gaby.plants.utils.FragmentUtils;
import com.gaby.plants.viewmodel.GardenViewModel;

import pl.droidsonroids.gif.GifImageView;

public class FragmentPrepGround extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preparando_tierra, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GardenViewModel vm = ViewModelProviders.of(this.getActivity()).get(GardenViewModel.class);
        Plant newPlant = vm.getNewPlant();
        vm.changePrepGround(newPlant.getPlantId());

        GifImageView prepGround = this.getView().findViewById(R.id.preparando_tierra);
        TextView prepGroundTitle = this.getView().findViewById(R.id.preparando_tierra_title);
        TextView prepGroundDesc = this.getView().findViewById(R.id.preparando_tierra_desc);
        prepGroundDesc.setAlpha(0f);
        Button btnContinuarPrepGround = this.getView().findViewById(R.id.btnContinuarPrepGround);
        btnContinuarPrepGround.setAlpha(0f);
        btnContinuarPrepGround.setOnClickListener(v -> FragmentUtils.replaceFragment(this.getActivity(), new FragmentPrepCompost()));

        ObjectAnimator animImgScaleX = ObjectAnimator.ofFloat(prepGround, View.SCALE_X, .4f).setDuration(1500);
        animImgScaleX.setInterpolator(new OvershootInterpolator());
        ObjectAnimator animImgScaleY = ObjectAnimator.ofFloat(prepGround, View.SCALE_Y, .4f).setDuration(1500);
        animImgScaleY.setInterpolator(new OvershootInterpolator());
        ObjectAnimator animImgTranslationY = ObjectAnimator.ofFloat(prepGround, View.TRANSLATION_Y, -570).setDuration(1500);
        animImgTranslationY.setInterpolator(new OvershootInterpolator());
        ObjectAnimator animTitleTranslationY = ObjectAnimator.ofFloat(prepGroundTitle, View.TRANSLATION_Y, -900).setDuration(1500);
        animTitleTranslationY.setInterpolator(new OvershootInterpolator());

        ObjectAnimator animPrepGroundDesc = ObjectAnimator.ofFloat(prepGroundDesc, View.ALPHA, 1f).setDuration(1000);
        ObjectAnimator animPrepGroundDescTranslationY = ObjectAnimator.ofFloat(prepGroundDesc, View.TRANSLATION_Y, -850).setDuration(1000);
        ObjectAnimator animPrepGroundBtnTranslationY = ObjectAnimator.ofFloat(btnContinuarPrepGround, View.TRANSLATION_Y, -600).setDuration(100);
        ObjectAnimator animPrepGroundBtn = ObjectAnimator.ofFloat(btnContinuarPrepGround, View.ALPHA, 1f).setDuration(400);

        AnimatorSet animator = new AnimatorSet();
        animator.play(animImgScaleX).after(1500);
        animator.play(animImgScaleX).with(animImgScaleY);
        animator.play(animImgScaleX).with(animImgTranslationY);
        animator.play(animImgScaleX).with(animTitleTranslationY);
        animator.play(animImgScaleX).before(animPrepGroundDesc);
        animator.play(animPrepGroundDesc).with(animPrepGroundDescTranslationY);
        animator.play(animPrepGroundDescTranslationY).before(animPrepGroundBtnTranslationY);
        animator.play(animPrepGroundBtnTranslationY).before(animPrepGroundBtn);
        animator.start();
    }
}
