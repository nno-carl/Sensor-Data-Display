package com.example.sensordatadisplay;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import java.io.File;
import java.util.ArrayList;

public class OfflineActivity extends Activity {
    private GLSurfaceView offView;

    private OfflineRender offrender;

    public static final String DATE = "";
    private String date;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_layout);


        offView = (GLSurfaceView) findViewById(R.id.offGLView);
        offView.setEGLContextClientVersion(2);

        offrender = new OfflineRender();
        offView.setRenderer(offrender);
        offView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        getActionBar().setTitle("Offline");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        date = intent.getStringExtra(DATE);
        Simulation(date);

    }

    protected void onResume() {
        super.onResume();
        offView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        offView.onPause();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //read saved data and perform offline simulation
    public void Simulation(String date) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sensor Data/" + date + ".txt");
        final ArrayList dataList = Read_Write.getFileContent(file);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < dataList.size(); i++){

                        String[] data = dataList.get(i).toString().split("  ");
                        String[] quaternion = data[1].split(",");
                        float w = Float.valueOf(quaternion[1]);
                        float x = Float.valueOf(quaternion[2]);
                        float y = Float.valueOf(quaternion[3]);
                        float z = Float.valueOf(quaternion[4]);

                        offrender.setX(x);
                        offrender.setY(y);
                        offrender.setZ(z);
                        offrender.setW(w);

                        offView.requestRender();
                        Thread.sleep(500);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
