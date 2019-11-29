package com.gaby.plants;

import android.app.Activity;
import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gaby.plants.model.Plant;
import com.gaby.plants.view.FragmentAddCompost;
import com.gaby.plants.view.FragmentBotonesPrincipales;
import com.gaby.plants.view.FragmentPrepGround;
import com.gaby.plants.view.FragmentSelectPlant;
import com.gaby.plants.view.FragmentStartApp;
import com.gaby.plants.viewmodel.GardenViewModel;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Collection;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final double MIN_OPENGL_VERSION = 3.0;
    private ModelRenderable andyRenderable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }
//
//        // Pone un layout en la pantalla
        setContentView(R.layout.main_activity);
//
//        // VM Observer
//        final GardenViewModel vm = ViewModelProviders.of(this).get(GardenViewModel.class);
//
//        vm.listPlants().observe(this, new Observer<Collection<Plant>>() {
//            @Override
//            public void onChanged(@Nullable Collection<Plant> plants) {
//                System.out.println("Notificacion recibida Plants has changed");
//                for (Plant p : plants) {
//                    System.out.println("Plant:" + p.getPlantType() + " " + p.getDateOfBirth());
//                }
//                Button buttonChangeFragment = findViewById(R.id.buttonChangeFragment);
//            }
//        });
//
//        // Button events
//        Button buttonChangeFragment = findViewById(R.id.buttonChangeFragment);
//
//        buttonChangeFragment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeFragment(v);
//            }
//        });
        ArFragment arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        Log.i(TAG, "Cargando modelo.");
        // Cargar el Modelo en memoria
        ModelRenderable.builder()
                .setSource(this, R.raw.strawberry_two)
                .build()
                .thenAccept(renderable -> {
                    Log.i(TAG, "Archivo cargado.");
                    andyRenderable = renderable;
                })
                .exceptionally(
                        throwable -> {
                            Log.e(TAG, "Unable to load Renderable.", throwable);
                            return null;
                        });
        Log.i(TAG, "Empezar app.");

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (andyRenderable == null) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(andyRenderable);
                    andy.select();
                });
    }

//    public void changeFragment(View view) {
//        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frameFragment);
//
//        if (currentFragment == null || currentFragment instanceof FragmentBotonesPrincipales) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            FragmentStartApp fragmentStartApp = new FragmentStartApp();
//            ft.replace(R.id.frameFragment, fragmentStartApp);
//            ft.commit();
//        }
//
//
//        if (currentFragment instanceof FragmentStartApp) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            FragmentSelectPlant fragmentSelectPlant = new FragmentSelectPlant();
//            ft.replace(R.id.frameFragment, fragmentSelectPlant);
//            ft.commit();
//        }
//
//        if (currentFragment instanceof FragmentSelectPlant) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            FragmentPrepGround fragmentPrepGround = new FragmentPrepGround();
//            ft.replace(R.id.frameFragment, fragmentPrepGround);
//            ft.commit();
//        }
//
//        if (currentFragment instanceof FragmentPrepGround) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            FragmentAddCompost fragmentAddCompost = new FragmentAddCompost();
//            ft.replace(R.id.frameFragment, fragmentAddCompost);
//            ft.commit();
//        }
//
//        if (currentFragment instanceof FragmentAddCompost) {
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            FragmentBotonesPrincipales fragmentBotonesPrincipales = new FragmentBotonesPrincipales();
//            ft.replace(R.id.frameFragment, fragmentBotonesPrincipales);
//            ft.commit();
//        }
//    }
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
