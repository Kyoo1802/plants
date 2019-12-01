package com.gaby.plants.view;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gaby.plants.R;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.LinkedList;
import java.util.List;

public class FragmentArcore extends Fragment {
    private static final String TAG = "MainActivity";
    private static final double MIN_OPENGL_VERSION = 3.0;

    private ModelRenderable plantRenderable;
    private ViewRenderable selectedPlantControl;
    private ViewRenderable addAbonoControl;
    private List<Node> plantControlNodes =  new LinkedList<>();

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

        // Cargar el Modelo en memoria
        ModelRenderable.builder()
                .setSource(this.getActivity(), R.raw.strawberry_two)
                .build()
                .thenAccept(renderable -> {
                    Log.i(TAG, "Archivo cargado.");
                    plantRenderable = renderable;
                })
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Unable to load Renderable.", throwable);
                            return null;
                        });

        // Renders the selected plant view
        ViewRenderable.builder()
                .setView(this.getActivity(), R.layout.fragment_selected_plant)
                .build()
                .thenAccept(renderable -> {
                    Log.i(TAG, "Archivo cargado.");
                    selectedPlantControl = renderable;
                })
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Unable to load Renderable.", throwable);
                            return null;
                        });

        ViewRenderable.builder()
                .setView(this.getActivity(), R.layout.fragment_add_abono)
                .build()
                .thenAccept(renderable -> {
                    Log.i(TAG, "Archivo cargado.");
                    addAbonoControl = renderable;
                })
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Unable to load Renderable.", throwable);
                            return null;
                        });

        ArFragment arFragment = (ArFragment) this.getChildFragmentManager().findFragmentById(R.id.arFragment);
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (plantRenderable == null || selectedPlantControl == null || addAbonoControl == null) {
                        return;
                    }

                    // Create the Anchor.
                    AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    Node plant = new Node();
                    plant.setParent(anchorNode);
                    plant.setRenderable(plantRenderable);

                    Node controls = new Node();
                    controls.setParent(plant);
                    controls.setLocalPosition(new Vector3(0.0f, 0.45f, 0.0f));
                    controls.setRenderable(selectedPlantControl);
                    controls.setEnabled(false);
                    plantControlNodes.add(controls);

                    Node abono = new Node();
                    abono.setParent(plant);
                    abono.setLocalPosition(new Vector3(0.45f, 0.00f, 0.00f));
                    abono.setRenderable(addAbonoControl);
                    abono.setEnabled(false);

                    plant.setOnTapListener((hitTestResult, motionEvent1) -> {
                        boolean controlEnabled = !controls.isEnabled();
                        hideAllControsls();
                        controls.setEnabled(controlEnabled);
                    });
                    ImageButton btn = selectedPlantControl.getView().findViewById(R.id.addAbono);
                    btn.setOnClickListener( view -> {
                        abono.setEnabled(!abono.isEnabled());
                    });
                });
    }

    private void hideAllControsls() {
        for(Node n: plantControlNodes){
            n.setEnabled(false);
        }
    }


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
}