package com.example.sensordatadisplay;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class ModelFragment extends Fragment {
    public static GLSurfaceView gLView;

    public static GLRender render;

    private View view;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.model_layout, container, false);
        //draw 3D model
        gLView = (GLSurfaceView) view.findViewById(R.id.mGLView);
        gLView.setEGLContextClientVersion(2);
        render = new GLRender();
        gLView.setRenderer(render);
        gLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        // click to select cube
        gLView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null){
                    final float normalizedX = (event.getX() / (float) v.getWidth() * 2 - 1);
                    final float normalizedY = (event.getY() / (float) v.getHeight() * 2 - 1);
                    final int x = (int)event.getX();
                    final int y = (int)event.getY();
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        gLView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                GLRender.handleTouchPress(normalizedX, normalizedY);
                            }
                        });

                    }
                    return true;
                }
                return false;
            }
        });
        return view;
    }

    public void onResume(){
        super.onResume();
        gLView.onResume();
    }

    public void onPause(){
        super.onPause();
        gLView.onPause();
    }

}
