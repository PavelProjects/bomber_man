package com.example.bomberman.actvities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.bomberman.surfaces.EditorSurface;
import com.example.bomberman.surfaces.GameSurface;

public class SurfaceActivity extends Activity {
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Intent intent = getIntent();
        switch (intent.getIntExtra("surface", 0)) {
            case 0:
                setContentView(new GameSurface(this));
                break;
            case 1:
                setContentView(new EditorSurface(this));
                break;
        }
    }
}
