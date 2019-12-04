package com.gaby.plants.view;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gaby.plants.R;
import com.gaby.plants.model.Plant;
import com.gaby.plants.model.PlantState;
import com.gaby.plants.model.PlantType;
import com.gaby.plants.viewmodel.GardenViewModel;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

public class FragmentArcore extends Fragment {
    private static final String TAG = "MainActivity";
    private static final double MIN_OPENGL_VERSION = 3.0;
    Table<PlantType, PlantState, ModelRenderable> renderables = HashBasedTable.create();
    private ViewRenderable selectedPlantControl;
    private ViewRenderable addAbonoControl;
    private ViewRenderable addWaterControl;
    private ViewRenderable progressBarView;
    private boolean renderablesCompleted;
    private List<Node> plantControlNodes = new LinkedList<>();

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    private static void animateInfo(Activity myActivity, int newHeight) {
        View container = myActivity.findViewById(R.id.gardenInfoScroll);
        ValueAnimator animator = ValueAnimator.ofInt(container.getHeight(), newHeight);
        animator.setTarget(container);
        animator.setDuration(1000).start();
        animator.addUpdateListener(animation -> {
            ViewGroup.LayoutParams layoutparams = container.getLayoutParams();
            layoutparams.height = (Integer) animation.getAnimatedValue();
            container.setLayoutParams(layoutparams);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_arcore, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this.getActivity())) {
            return;
        }
        CompletableFuture.allOf(
                // Cargar Modelos en memoria
                createModelRenderable(R.raw.maceta, PlantType.SUNFLOWER, PlantState.SEED),
                createModelRenderable(R.raw.girasol2, PlantType.SUNFLOWER, PlantState.SPROUD),
                createModelRenderable(R.raw.girasol3, PlantType.SUNFLOWER, PlantState.PLANT),
                createModelRenderable(R.raw.girasol4, PlantType.SUNFLOWER, PlantState.FRUIT_PLANT),
                createModelRenderable(R.raw.maceta, PlantType.CORN, PlantState.SEED),
                createModelRenderable(R.raw.elote2, PlantType.CORN, PlantState.SPROUD),
                createModelRenderable(R.raw.elote3, PlantType.CORN, PlantState.PLANT),
                createModelRenderable(R.raw.elote4, PlantType.CORN, PlantState.FRUIT_PLANT),
                createModelRenderable(R.raw.maceta, PlantType.CALADIO, PlantState.SEED),
                createModelRenderable(R.raw.caladio2, PlantType.CALADIO, PlantState.SPROUD),
                createModelRenderable(R.raw.caladio3, PlantType.CALADIO, PlantState.PLANT),
                createModelRenderable(R.raw.caladio4, PlantType.CALADIO, PlantState.FRUIT_PLANT),
                createModelRenderable(R.raw.maceta, PlantType.HELECHOS, PlantState.SEED),
                createModelRenderable(R.raw.hojasalargadas2, PlantType.HELECHOS, PlantState.SPROUD),
                createModelRenderable(R.raw.hojasalargadas3, PlantType.HELECHOS, PlantState.PLANT),
                createModelRenderable(R.raw.hojasalargadas3, PlantType.HELECHOS, PlantState.FRUIT_PLANT),
                createModelRenderable(R.raw.maceta, PlantType.MARGARITA, PlantState.SEED),
                createModelRenderable(R.raw.purpleflower2, PlantType.MARGARITA, PlantState.SPROUD),
                createModelRenderable(R.raw.purpleflower3, PlantType.MARGARITA, PlantState.PLANT),
                createModelRenderable(R.raw.purpleflower4, PlantType.MARGARITA, PlantState.FRUIT_PLANT),
                createModelRenderable(R.raw.maceta, PlantType.ALOEVERA, PlantState.SEED),
                createModelRenderable(R.raw.aloevera2, PlantType.ALOEVERA, PlantState.SPROUD),
                createModelRenderable(R.raw.aloevera3, PlantType.ALOEVERA, PlantState.PLANT),
                createModelRenderable(R.raw.aloevera4, PlantType.ALOEVERA, PlantState.FRUIT_PLANT),
                // Renders the selected plant view
                ViewRenderable.builder()
                        .setView(this.getActivity(), R.layout.arview_selected_plant)
                        .build()
                        .thenAccept(renderable -> {
                            Log.i(TAG, "Archivo cargado.");
                            selectedPlantControl = renderable;
                        })
                        .exceptionally(
                                throwable -> {
                                    Log.e(TAG, "Unable to load Renderable.", throwable);
                                    return null;
                                }),
                ViewRenderable.builder()
                        .setView(this.getActivity(), R.layout.arview_add_abono)
                        .build()
                        .thenAccept(renderable -> {
                            Log.i(TAG, "Archivo cargado.");
                            addAbonoControl = renderable;
                        })
                        .exceptionally(
                                throwable -> {
                                    Log.e(TAG, "Unable to load Renderable.", throwable);
                                    return null;
                                }),
                ViewRenderable.builder()
                        .setView(this.getActivity(), R.layout.arview_add_water)
                        .build()
                        .thenAccept(renderable -> {
                            Log.i(TAG, "Archivo cargado.");
                            addWaterControl = renderable;
                        })
                        .exceptionally(
                                throwable -> {
                                    Log.e(TAG, "Unable to load Renderable.", throwable);
                                    return null;
                                }),
                ViewRenderable.builder()
                        .setView(this.getActivity(), R.layout.arview_progress_bar)
                        .build()
                        .thenAccept(renderable -> {
                            Log.i(TAG, "Archivo cargado.");
                            progressBarView = renderable;
                        })
                        .exceptionally(
                                throwable -> {
                                    Log.e(TAG, "Unable to load Renderable.", throwable);
                                    return null;
                                }))
                .handle(
                        (notUsed, throwable) -> {
                            renderablesCompleted = true;
                            return null;
                        });

        ArFragment arFragment = (ArFragment) this.getChildFragmentManager().findFragmentById(R.id.arFragment);
        FragmentActivity myActivity = this.getActivity();
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    GardenViewModel vm = ViewModelProviders.of(myActivity).get(GardenViewModel.class);
                    Plant newPlant = vm.getNewPlant();
                    if (!renderablesCompleted || newPlant == null || newPlant.getPlantState() != PlantState.PREP_COMPOST) {
                        return;
                    }
                    vm.changeToSeed(vm.getNewPlantId());

                    // Create the Anchor.
                    AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    Node plantNode = new Node();
                    plantNode.setParent(anchorNode);

                    Node plantSeed = new Node();
                    plantSeed.setParent(plantNode);
                    plantSeed.setRenderable(getRenderableFromPlant(newPlant.getPlantType(), PlantState.SEED));
                    Node plantSproud = new Node();
                    plantSproud.setParent(plantNode);
                    plantSproud.setRenderable(getRenderableFromPlant(newPlant.getPlantType(), PlantState.SPROUD));
                    plantSproud.setEnabled(false);
                    Node plantPlant = new Node();
                    plantPlant.setParent(plantNode);
                    plantPlant.setRenderable(getRenderableFromPlant(newPlant.getPlantType(), PlantState.PLANT));
                    plantPlant.setEnabled(false);
                    Node plantFruit = new Node();
                    plantFruit.setParent(plantNode);
                    plantFruit.setRenderable(getRenderableFromPlant(newPlant.getPlantType(), PlantState.FRUIT_PLANT));
                    plantFruit.setEnabled(false);

                    Node actionsViewNode = new Node();
                    actionsViewNode.setParent(plantNode);
                    actionsViewNode.setLocalPosition(new Vector3(0.0f, 0.65f, 0.2f));
                    actionsViewNode.setRenderable(selectedPlantControl);
                    actionsViewNode.setEnabled(false);
                    plantControlNodes.add(actionsViewNode);

                    Node abonoViewNode = new Node();
                    abonoViewNode.setParent(plantNode);
                    abonoViewNode.setLocalPosition(new Vector3(0.2f, 0.30f, 0.1f));
                    abonoViewNode.setRenderable(addAbonoControl);
                    abonoViewNode.setEnabled(false);
                    plantControlNodes.add(abonoViewNode);

                    Node waterViewNode = new Node();
                    waterViewNode.setParent(plantNode);
                    waterViewNode.setLocalPosition(new Vector3(0.20f, 0.20f, 0.05f));
                    waterViewNode.setRenderable(addWaterControl);
                    waterViewNode.setEnabled(false);
                    plantControlNodes.add(waterViewNode);

                    Node infoViewNode = new Node();
                    infoViewNode.setParent(plantNode);
                    infoViewNode.setLocalPosition(new Vector3(-0.45f, 0.00f, 0.05f));
                    infoViewNode.setRenderable(addAbonoControl);
                    infoViewNode.setEnabled(false);
                    plantControlNodes.add(infoViewNode);

                    Node progressViewNode = new Node();
                    progressViewNode.setParent(plantNode);
                    progressViewNode.setLocalPosition(new Vector3(-0.3f, 0.2f, 0.1f));
                    progressViewNode.setRenderable(progressBarView);
                    progressViewNode.setEnabled(false);
                    plantControlNodes.add(progressViewNode);

                    long newPlantId = newPlant.getPlantId();
                    vm.updatedPlant().observe(myActivity, plantUpdated -> {
                        if (plantUpdated.getPlantId() == newPlantId) {
                            ProgressBar pBarWater = progressBarView.getView().findViewById(R.id.progressBarWater);
                            if (plantUpdated.getWaterPercentage() != pBarWater.getProgress()) {
                                pBarWater.setProgress(plantUpdated.getWaterPercentage());
                            }

                            ProgressBar pBarAbono = progressBarView.getView().findViewById(R.id.progressBarAbono);
                            if (plantUpdated.getAbonoPercentage() != pBarAbono.getProgress()) {
                                pBarAbono.setProgress(plantUpdated.getAbonoPercentage());
                            }
                        }
                    });
                    vm.updatedState().observe(this.getActivity(), plantUpdated -> {
                        String test = "state change:" + plantUpdated.getPlantState();
                        System.out.println(test);
                        if (plantUpdated.getPlantId() == newPlantId
                                && (plantUpdated.getPlantState() == PlantState.SPROUD
                                || plantUpdated.getPlantState() == PlantState.PLANT
                                || plantUpdated.getPlantState() == PlantState.FRUIT_PLANT)) {
                            plantSeed.setEnabled(false);
                            plantSproud.setEnabled(false);
                            plantPlant.setEnabled(false);
                            plantFruit.setEnabled(false);
                            switch (plantUpdated.getPlantState()) {
                                case SEED:
                                    plantSeed.setEnabled(true);
                                    break;
                                case SPROUD:
                                    plantSproud.setEnabled(true);
                                    break;
                                case PLANT:
                                    plantPlant.setEnabled(true);
                                    break;
                                case FRUIT_PLANT:
                                    plantFruit.setEnabled(true);
                                    break;
                            }
                        }
                    });

                    plantSeed.setOnTapListener((hitTestResult, motionEvent1) -> {
                        boolean newEnabledState = !actionsViewNode.isEnabled();
                        long selectedPlantId = newPlantId;
                        Plant selectedPlant = vm.getPlant(selectedPlantId);
                        hideAllControls();
                        actionsViewNode.setEnabled(newEnabledState);
                        progressViewNode.setEnabled(newEnabledState);
                        if (newEnabledState) {
                            ProgressBar pBarWater = progressBarView.getView().findViewById(R.id.progressBarWater);
                            ProgressBar pBarAbono = progressBarView.getView().findViewById(R.id.progressBarAbono);
                            pBarWater.setProgress(selectedPlant.getWaterPercentage());
                            pBarAbono.setProgress(selectedPlant.getAbonoPercentage());
                            Button btnSun = selectedPlantControl.getView().findViewById(R.id.btnSun);
                            Button btnAddAbono = selectedPlantControl.getView().findViewById(R.id.btnAddAbono);
                            Button btnAddWater = selectedPlantControl.getView().findViewById(R.id.btnAddWater);
                            Button btnInfo = selectedPlantControl.getView().findViewById(R.id.btnInfo);
                            Button btnDelete = selectedPlantControl.getView().findViewById(R.id.btnDelete);
                            SeekBar seekBarSunAdjust = selectedPlantControl.getView().findViewById(R.id.seekBarSun);
                            seekBarSunAdjust.setProgress(0);
                            TextView seekBarSunValue = selectedPlantControl.getView().findViewById(R.id.txtSunDegrees);
                            TextView txtSunDegrees = selectedPlantControl.getView().findViewById(R.id.txtSunDegrees);
                            TextView txtsun_adjust = selectedPlantControl.getView().findViewById(R.id.sun_adjust);
                            TextView txtbien = selectedPlantControl.getView().findViewById(R.id.bien);
                            String sf = String.format(
                                    "%s: %d C y %d C.",
                                    getContext().getResources().getText(R.string.add_sun_desc),
                                    selectedPlant.getCorrectSunAmount()[0],
                                    selectedPlant.getCorrectSunAmount()[1]);
                            txtsun_adjust.setText(sf);
                            TextView txtwater_adjust = selectedPlantControl.getView().findViewById(R.id.water_adjust);
                            TextView txtabono_adjust = selectedPlantControl.getView().findViewById(R.id.abono_adjust);
                            seekBarSunAdjust.setVisibility(View.GONE);
                            txtSunDegrees.setVisibility(View.GONE);
                            txtwater_adjust.setVisibility(View.GONE);
                            txtabono_adjust.setVisibility(View.GONE);
                            txtbien.setVisibility(View.GONE);

                            if (selectedPlant.isHasSunLight()) {
                                txtsun_adjust.setVisibility(View.GONE);
                            } else {
                                txtsun_adjust.setVisibility(View.VISIBLE);
                            }

                            // Button Add Abono configuration
                            btnAddAbono.setOnClickListener(view -> {
                                if (!abonoViewNode.isEnabled()) {
                                    txtwater_adjust.setVisibility(View.GONE);
                                    txtabono_adjust.setVisibility(View.VISIBLE);
                                    vm.onTapBtnAddAbono(selectedPlantId);
                                    abonoViewNode.setEnabled(true);
                                    txtbien.setVisibility(View.GONE);
                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            // Sends a request to the UI Thread to trigger a method.
                                            new Handler(Looper.getMainLooper()).post(() -> abonoViewNode.setEnabled(false));
                                        }
                                    }, 1500);
                                }
                            });

                            // Button Add Water configuration
                            btnAddWater.setOnClickListener(view -> {
                                if (!waterViewNode.isEnabled()) {
                                    txtwater_adjust.setVisibility(View.VISIBLE);
                                    txtabono_adjust.setVisibility(View.GONE);
                                    vm.onTapBtnAddWater(selectedPlantId);
                                    waterViewNode.setEnabled(true);
                                    txtbien.setVisibility(View.GONE);
                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            // Sends a request to the UI Thread to trigger a method.
                                            new Handler(Looper.getMainLooper()).post(() -> waterViewNode.setEnabled(false));
                                        }
                                    }, 1500);
                                }
                            });

                            // Button View Info configuration
                            btnInfo.setOnClickListener(view -> {
                                FragmentGardenInfo fragment = (FragmentGardenInfo) this.getChildFragmentManager().findFragmentById(R.id.gardenInfo);
                                if (fragment.isShown()) {
                                    fragment.setShown(false);
                                    animateInfo(myActivity, 0);

                                } else {
                                    fragment.setPlant(selectedPlant);
                                    fragment.setShown(true);
                                    animateInfo(myActivity, (int) (myActivity.getResources().getDisplayMetrics().heightPixels * .8));
                                }
                                Button cerrar = fragment.getView().findViewById(R.id.cerrarGardenInfo);
                                cerrar.setOnClickListener(v -> {
                                    fragment.setShown(false);
                                    animateInfo(myActivity, 0);
                                });
                            });


                            // Button View Info configuration
                            btnDelete.setOnClickListener(view -> {
                                vm.onDeletePlant(selectedPlantId);
                                hideAllControls();
                                anchorNode.removeChild(plantNode);
                            });

                            btnSun.setOnClickListener(view -> {
                                seekBarSunAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                        seekBarSunValue.setText(progress + " C");

                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {
                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {
                                        if (selectedPlant.getCorrectSunAmount()[0] <= seekBar.getProgress() && seekBar.getProgress() <= selectedPlant.getCorrectSunAmount()[1]) {
                                            vm.onCompleteAdjustLight(selectedPlantId);
                                            seekBarSunAdjust.setVisibility(View.GONE);
                                            txtSunDegrees.setVisibility(View.GONE);
                                            txtsun_adjust.setVisibility(View.GONE);
                                            txtbien.setVisibility(View.VISIBLE);
                                            showPlantActionBtns(true, true);

                                        }
                                    }
                                });
                                seekBarSunAdjust.setVisibility(View.VISIBLE);
                                txtSunDegrees.setVisibility(View.VISIBLE);
                                seekBarSunAdjust.setAlpha(0);
                                seekBarSunAdjust.animate()
                                        .alpha(1)
                                        .setDuration(400);
                            });

                            showPlantActionBtns(selectedPlant.isHasSunLight(), false);
                        }
                    });
                    plantSproud.setOnTapListener((hitTestResult, motionEvent1) -> {
                        boolean newEnabledState = !actionsViewNode.isEnabled();
                        long selectedPlantId = newPlantId;
                        Plant selectedPlant = vm.getPlant(selectedPlantId);
                        hideAllControls();
                        actionsViewNode.setEnabled(newEnabledState);
                        progressViewNode.setEnabled(newEnabledState);
                        if (newEnabledState) {
                            ProgressBar pBarWater = progressBarView.getView().findViewById(R.id.progressBarWater);
                            ProgressBar pBarAbono = progressBarView.getView().findViewById(R.id.progressBarAbono);
                            pBarWater.setProgress(selectedPlant.getWaterPercentage());
                            pBarAbono.setProgress(selectedPlant.getAbonoPercentage());
                            Button btnSun = selectedPlantControl.getView().findViewById(R.id.btnSun);
                            Button btnAddAbono = selectedPlantControl.getView().findViewById(R.id.btnAddAbono);
                            Button btnAddWater = selectedPlantControl.getView().findViewById(R.id.btnAddWater);
                            Button btnInfo = selectedPlantControl.getView().findViewById(R.id.btnInfo);
                            Button btnDelete = selectedPlantControl.getView().findViewById(R.id.btnDelete);
                            SeekBar seekBarSunAdjust = selectedPlantControl.getView().findViewById(R.id.seekBarSun);
                            seekBarSunAdjust.setProgress(0);
                            TextView seekBarSunValue = selectedPlantControl.getView().findViewById(R.id.txtSunDegrees);
                            TextView txtSunDegrees = selectedPlantControl.getView().findViewById(R.id.txtSunDegrees);
                            TextView txtsun_adjust = selectedPlantControl.getView().findViewById(R.id.sun_adjust);
                            TextView txtbien = selectedPlantControl.getView().findViewById(R.id.bien);
                            String sf = String.format(
                                    "%s: %d C y %d C.",
                                    getContext().getResources().getText(R.string.add_sun_desc),
                                    selectedPlant.getCorrectSunAmount()[0],
                                    selectedPlant.getCorrectSunAmount()[1]);
                            txtsun_adjust.setText(sf);
                            TextView txtwater_adjust = selectedPlantControl.getView().findViewById(R.id.water_adjust);
                            TextView txtabono_adjust = selectedPlantControl.getView().findViewById(R.id.abono_adjust);
                            seekBarSunAdjust.setVisibility(View.GONE);
                            txtSunDegrees.setVisibility(View.GONE);
                            txtwater_adjust.setVisibility(View.GONE);
                            txtabono_adjust.setVisibility(View.GONE);
                            txtbien.setVisibility(View.GONE);

                            if (selectedPlant.isHasSunLight()) {
                                txtsun_adjust.setVisibility(View.GONE);
                            } else {
                                txtsun_adjust.setVisibility(View.VISIBLE);
                            }

                            // Button Add Abono configuration
                            btnAddAbono.setOnClickListener(view -> {
                                if (!abonoViewNode.isEnabled()) {
                                    txtwater_adjust.setVisibility(View.GONE);
                                    txtabono_adjust.setVisibility(View.VISIBLE);
                                    vm.onTapBtnAddAbono(selectedPlantId);
                                    abonoViewNode.setEnabled(true);
                                    txtbien.setVisibility(View.GONE);
                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            // Sends a request to the UI Thread to trigger a method.
                                            new Handler(Looper.getMainLooper()).post(() -> abonoViewNode.setEnabled(false));
                                        }
                                    }, 1500);
                                }
                            });

                            // Button Add Water configuration
                            btnAddWater.setOnClickListener(view -> {
                                if (!waterViewNode.isEnabled()) {
                                    txtwater_adjust.setVisibility(View.VISIBLE);
                                    txtabono_adjust.setVisibility(View.GONE);
                                    vm.onTapBtnAddWater(selectedPlantId);
                                    waterViewNode.setEnabled(true);
                                    txtbien.setVisibility(View.GONE);
                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            // Sends a request to the UI Thread to trigger a method.
                                            new Handler(Looper.getMainLooper()).post(() -> waterViewNode.setEnabled(false));
                                        }
                                    }, 1500);
                                }
                            });

                            // Button View Info configuration
                            btnInfo.setOnClickListener(view -> {
                                FragmentGardenInfo fragment = (FragmentGardenInfo) this.getChildFragmentManager().findFragmentById(R.id.gardenInfo);
                                if (fragment.isShown()) {
                                    fragment.setShown(false);
                                    animateInfo(myActivity, 0);

                                } else {
                                    fragment.setPlant(selectedPlant);
                                    fragment.setShown(true);
                                    animateInfo(myActivity, (int) (myActivity.getResources().getDisplayMetrics().heightPixels * .8));
                                }
                                Button cerrar = fragment.getView().findViewById(R.id.cerrarGardenInfo);
                                cerrar.setOnClickListener(v -> {
                                    fragment.setShown(false);
                                    animateInfo(myActivity, 0);
                                });
                            });


                            // Button View Info configuration
                            btnDelete.setOnClickListener(view -> {
                                vm.onDeletePlant(selectedPlantId);
                                hideAllControls();
                                anchorNode.removeChild(plantNode);
                            });

                            btnSun.setOnClickListener(view -> {
                                seekBarSunAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                        seekBarSunValue.setText(progress + " C");

                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {
                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {
                                        if (selectedPlant.getCorrectSunAmount()[0] <= seekBar.getProgress() && seekBar.getProgress() <= selectedPlant.getCorrectSunAmount()[1]) {
                                            vm.onCompleteAdjustLight(selectedPlantId);
                                            seekBarSunAdjust.setVisibility(View.GONE);
                                            txtSunDegrees.setVisibility(View.GONE);
                                            txtsun_adjust.setVisibility(View.GONE);
                                            txtbien.setVisibility(View.VISIBLE);
                                            showPlantActionBtns(true, true);

                                        }
                                    }
                                });
                                seekBarSunAdjust.setVisibility(View.VISIBLE);
                                txtSunDegrees.setVisibility(View.VISIBLE);
                                seekBarSunAdjust.setAlpha(0);
                                seekBarSunAdjust.animate()
                                        .alpha(1)
                                        .setDuration(400);
                            });

                            showPlantActionBtns(selectedPlant.isHasSunLight(), false);
                        }
                    });
                    plantPlant.setOnTapListener((hitTestResult, motionEvent1) -> {
                        boolean newEnabledState = !actionsViewNode.isEnabled();
                        long selectedPlantId = newPlantId;
                        Plant selectedPlant = vm.getPlant(selectedPlantId);
                        hideAllControls();
                        actionsViewNode.setEnabled(newEnabledState);
                        progressViewNode.setEnabled(newEnabledState);
                        if (newEnabledState) {
                            ProgressBar pBarWater = progressBarView.getView().findViewById(R.id.progressBarWater);
                            ProgressBar pBarAbono = progressBarView.getView().findViewById(R.id.progressBarAbono);
                            pBarWater.setProgress(selectedPlant.getWaterPercentage());
                            pBarAbono.setProgress(selectedPlant.getAbonoPercentage());
                            Button btnSun = selectedPlantControl.getView().findViewById(R.id.btnSun);
                            Button btnAddAbono = selectedPlantControl.getView().findViewById(R.id.btnAddAbono);
                            Button btnAddWater = selectedPlantControl.getView().findViewById(R.id.btnAddWater);
                            Button btnInfo = selectedPlantControl.getView().findViewById(R.id.btnInfo);
                            Button btnDelete = selectedPlantControl.getView().findViewById(R.id.btnDelete);
                            SeekBar seekBarSunAdjust = selectedPlantControl.getView().findViewById(R.id.seekBarSun);
                            seekBarSunAdjust.setProgress(0);
                            TextView seekBarSunValue = selectedPlantControl.getView().findViewById(R.id.txtSunDegrees);
                            TextView txtSunDegrees = selectedPlantControl.getView().findViewById(R.id.txtSunDegrees);
                            TextView txtsun_adjust = selectedPlantControl.getView().findViewById(R.id.sun_adjust);
                            TextView txtbien = selectedPlantControl.getView().findViewById(R.id.bien);
                            String sf = String.format(
                                    "%s: %d C y %d C.",
                                    getContext().getResources().getText(R.string.add_sun_desc),
                                    selectedPlant.getCorrectSunAmount()[0],
                                    selectedPlant.getCorrectSunAmount()[1]);
                            txtsun_adjust.setText(sf);
                            TextView txtwater_adjust = selectedPlantControl.getView().findViewById(R.id.water_adjust);
                            TextView txtabono_adjust = selectedPlantControl.getView().findViewById(R.id.abono_adjust);
                            seekBarSunAdjust.setVisibility(View.GONE);
                            txtSunDegrees.setVisibility(View.GONE);
                            txtwater_adjust.setVisibility(View.GONE);
                            txtabono_adjust.setVisibility(View.GONE);
                            txtbien.setVisibility(View.GONE);

                            if (selectedPlant.isHasSunLight()) {
                                txtsun_adjust.setVisibility(View.GONE);
                            } else {
                                txtsun_adjust.setVisibility(View.VISIBLE);
                            }

                            // Button Add Abono configuration
                            btnAddAbono.setOnClickListener(view -> {
                                if (!abonoViewNode.isEnabled()) {
                                    txtwater_adjust.setVisibility(View.GONE);
                                    txtabono_adjust.setVisibility(View.VISIBLE);
                                    vm.onTapBtnAddAbono(selectedPlantId);
                                    abonoViewNode.setEnabled(true);
                                    txtbien.setVisibility(View.GONE);
                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            // Sends a request to the UI Thread to trigger a method.
                                            new Handler(Looper.getMainLooper()).post(() -> abonoViewNode.setEnabled(false));
                                        }
                                    }, 1500);
                                }
                            });

                            // Button Add Water configuration
                            btnAddWater.setOnClickListener(view -> {
                                if (!waterViewNode.isEnabled()) {
                                    txtwater_adjust.setVisibility(View.VISIBLE);
                                    txtabono_adjust.setVisibility(View.GONE);
                                    vm.onTapBtnAddWater(selectedPlantId);
                                    waterViewNode.setEnabled(true);
                                    txtbien.setVisibility(View.GONE);
                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            // Sends a request to the UI Thread to trigger a method.
                                            new Handler(Looper.getMainLooper()).post(() -> waterViewNode.setEnabled(false));
                                        }
                                    }, 1500);
                                }
                            });

                            // Button View Info configuration
                            btnInfo.setOnClickListener(view -> {
                                FragmentGardenInfo fragment = (FragmentGardenInfo) this.getChildFragmentManager().findFragmentById(R.id.gardenInfo);
                                if (fragment.isShown()) {
                                    fragment.setShown(false);
                                    animateInfo(myActivity, 0);

                                } else {
                                    fragment.setPlant(selectedPlant);
                                    fragment.setShown(true);
                                    animateInfo(myActivity, (int) (myActivity.getResources().getDisplayMetrics().heightPixels * .8));
                                }
                                Button cerrar = fragment.getView().findViewById(R.id.cerrarGardenInfo);
                                cerrar.setOnClickListener(v -> {
                                    fragment.setShown(false);
                                    animateInfo(myActivity, 0);
                                });
                            });


                            // Button View Info configuration
                            btnDelete.setOnClickListener(view -> {
                                vm.onDeletePlant(selectedPlantId);
                                hideAllControls();
                                anchorNode.removeChild(plantNode);
                            });

                            btnSun.setOnClickListener(view -> {
                                seekBarSunAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                        seekBarSunValue.setText(progress + " C");

                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {
                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {
                                        if (selectedPlant.getCorrectSunAmount()[0] <= seekBar.getProgress() && seekBar.getProgress() <= selectedPlant.getCorrectSunAmount()[1]) {
                                            vm.onCompleteAdjustLight(selectedPlantId);
                                            seekBarSunAdjust.setVisibility(View.GONE);
                                            txtSunDegrees.setVisibility(View.GONE);
                                            txtsun_adjust.setVisibility(View.GONE);
                                            txtbien.setVisibility(View.VISIBLE);
                                            showPlantActionBtns(true, true);

                                        }
                                    }
                                });
                                seekBarSunAdjust.setVisibility(View.VISIBLE);
                                txtSunDegrees.setVisibility(View.VISIBLE);
                                seekBarSunAdjust.setAlpha(0);
                                seekBarSunAdjust.animate()
                                        .alpha(1)
                                        .setDuration(400);
                            });

                            showPlantActionBtns(selectedPlant.isHasSunLight(), false);
                        }
                    });
                    plantFruit.setOnTapListener((hitTestResult, motionEvent1) -> {
                        boolean newEnabledState = !actionsViewNode.isEnabled();
                        long selectedPlantId = newPlantId;
                        Plant selectedPlant = vm.getPlant(selectedPlantId);
                        hideAllControls();
                        actionsViewNode.setEnabled(newEnabledState);
                        progressViewNode.setEnabled(newEnabledState);
                        if (newEnabledState) {
                            ProgressBar pBarWater = progressBarView.getView().findViewById(R.id.progressBarWater);
                            ProgressBar pBarAbono = progressBarView.getView().findViewById(R.id.progressBarAbono);
                            pBarWater.setProgress(selectedPlant.getWaterPercentage());
                            pBarAbono.setProgress(selectedPlant.getAbonoPercentage());
                            Button btnSun = selectedPlantControl.getView().findViewById(R.id.btnSun);
                            Button btnAddAbono = selectedPlantControl.getView().findViewById(R.id.btnAddAbono);
                            Button btnAddWater = selectedPlantControl.getView().findViewById(R.id.btnAddWater);
                            Button btnInfo = selectedPlantControl.getView().findViewById(R.id.btnInfo);
                            Button btnDelete = selectedPlantControl.getView().findViewById(R.id.btnDelete);
                            SeekBar seekBarSunAdjust = selectedPlantControl.getView().findViewById(R.id.seekBarSun);
                            seekBarSunAdjust.setProgress(0);
                            TextView seekBarSunValue = selectedPlantControl.getView().findViewById(R.id.txtSunDegrees);
                            TextView txtSunDegrees = selectedPlantControl.getView().findViewById(R.id.txtSunDegrees);
                            TextView txtsun_adjust = selectedPlantControl.getView().findViewById(R.id.sun_adjust);
                            TextView txtbien = selectedPlantControl.getView().findViewById(R.id.bien);
                            String sf = String.format(
                                    "%s: %d C y %d C.",
                                    getContext().getResources().getText(R.string.add_sun_desc),
                                    selectedPlant.getCorrectSunAmount()[0],
                                    selectedPlant.getCorrectSunAmount()[1]);
                            txtsun_adjust.setText(sf);
                            TextView txtwater_adjust = selectedPlantControl.getView().findViewById(R.id.water_adjust);
                            TextView txtabono_adjust = selectedPlantControl.getView().findViewById(R.id.abono_adjust);
                            seekBarSunAdjust.setVisibility(View.GONE);
                            txtSunDegrees.setVisibility(View.GONE);
                            txtwater_adjust.setVisibility(View.GONE);
                            txtabono_adjust.setVisibility(View.GONE);
                            txtbien.setVisibility(View.GONE);

                            if (selectedPlant.isHasSunLight()) {
                                txtsun_adjust.setVisibility(View.GONE);
                            } else {
                                txtsun_adjust.setVisibility(View.VISIBLE);
                            }

                            // Button Add Abono configuration
                            btnAddAbono.setOnClickListener(view -> {
                                if (!abonoViewNode.isEnabled()) {
                                    txtwater_adjust.setVisibility(View.GONE);
                                    txtabono_adjust.setVisibility(View.VISIBLE);
                                    vm.onTapBtnAddAbono(selectedPlantId);
                                    abonoViewNode.setEnabled(true);
                                    txtbien.setVisibility(View.GONE);
                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            // Sends a request to the UI Thread to trigger a method.
                                            new Handler(Looper.getMainLooper()).post(() -> abonoViewNode.setEnabled(false));
                                        }
                                    }, 1500);
                                }
                            });

                            // Button Add Water configuration
                            btnAddWater.setOnClickListener(view -> {
                                if (!waterViewNode.isEnabled()) {
                                    txtwater_adjust.setVisibility(View.VISIBLE);
                                    txtabono_adjust.setVisibility(View.GONE);
                                    vm.onTapBtnAddWater(selectedPlantId);
                                    waterViewNode.setEnabled(true);
                                    txtbien.setVisibility(View.GONE);
                                    Timer timer = new Timer();
                                    timer.schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            // Sends a request to the UI Thread to trigger a method.
                                            new Handler(Looper.getMainLooper()).post(() -> waterViewNode.setEnabled(false));
                                        }
                                    }, 1500);
                                }
                            });

                            // Button View Info configuration
                            btnInfo.setOnClickListener(view -> {
                                FragmentGardenInfo fragment = (FragmentGardenInfo) this.getChildFragmentManager().findFragmentById(R.id.gardenInfo);
                                if (fragment.isShown()) {
                                    fragment.setShown(false);
                                    animateInfo(myActivity, 0);

                                } else {
                                    fragment.setPlant(selectedPlant);
                                    fragment.setShown(true);
                                    animateInfo(myActivity, (int) (myActivity.getResources().getDisplayMetrics().heightPixels * .8));
                                }
                                Button cerrar = fragment.getView().findViewById(R.id.cerrarGardenInfo);
                                cerrar.setOnClickListener(v -> {
                                    fragment.setShown(false);
                                    animateInfo(myActivity, 0);
                                });
                            });


                            // Button View Info configuration
                            btnDelete.setOnClickListener(view -> {
                                vm.onDeletePlant(selectedPlantId);
                                hideAllControls();
                                anchorNode.removeChild(plantNode);
                            });

                            btnSun.setOnClickListener(view -> {
                                seekBarSunAdjust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                        seekBarSunValue.setText(progress + " C");

                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {
                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {
                                        if (selectedPlant.getCorrectSunAmount()[0] <= seekBar.getProgress() && seekBar.getProgress() <= selectedPlant.getCorrectSunAmount()[1]) {
                                            vm.onCompleteAdjustLight(selectedPlantId);
                                            seekBarSunAdjust.setVisibility(View.GONE);
                                            txtSunDegrees.setVisibility(View.GONE);
                                            txtsun_adjust.setVisibility(View.GONE);
                                            txtbien.setVisibility(View.VISIBLE);
                                            showPlantActionBtns(true, true);

                                        }
                                    }
                                });
                                seekBarSunAdjust.setVisibility(View.VISIBLE);
                                txtSunDegrees.setVisibility(View.VISIBLE);
                                seekBarSunAdjust.setAlpha(0);
                                seekBarSunAdjust.animate()
                                        .alpha(1)
                                        .setDuration(400);
                            });

                            showPlantActionBtns(selectedPlant.isHasSunLight(), false);
                        }
                    });
                });

    }

    private CompletableFuture<Void> createModelRenderable(int id, PlantType type, PlantState state) {
        return ModelRenderable.builder()
                .setSource(this.getActivity(), id)
                .build()
                .thenAccept(renderable -> {
                    Log.i(TAG, "Archivo cargado.");
                    renderables.put(type, state, renderable);
                })
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Unable to load Renderable.", throwable);
                            return null;
                        });
    }

    private Renderable getRenderableFromPlant(PlantType plantType, PlantState plantState) {
        return renderables.get(plantType, plantState);
    }

    private void showPlantActionBtns(boolean show, boolean animate) {
        Button btnAddAbono = selectedPlantControl.getView().findViewById(R.id.btnAddAbono);
        Button btnAddWater = selectedPlantControl.getView().findViewById(R.id.btnAddWater);
        Button btnSun = selectedPlantControl.getView().findViewById(R.id.btnSun);
        if (show) {
            btnSun.setVisibility(View.GONE);
            btnAddAbono.setVisibility(View.VISIBLE);
            btnAddAbono.setAlpha(1f);
            btnAddWater.setVisibility(View.VISIBLE);
            btnAddWater.setAlpha(1f);
            if (animate) {
                btnAddAbono.setAlpha(0f);
                btnAddAbono.animate()
                        .alpha(1)
                        .setDuration(1000);
                btnAddWater.setAlpha(0f);
                btnAddWater.animate()
                        .alpha(1)
                        .setDuration(1000);
            }
        } else {
            btnSun.setVisibility(View.VISIBLE);
            btnAddAbono.setVisibility(View.GONE);
            btnAddWater.setVisibility(View.GONE);
        }
    }

    private void hideAllControls() {
        for (Node n : plantControlNodes) {
            n.setEnabled(false);
        }
    }
}