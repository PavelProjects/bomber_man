package com.example.bomberman.actvities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bomberman.R;

public class MainActivity extends Activity {
    Intent intent;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent(MainActivity.this, SurfaceActivity.class);
    }

    public void openGame(View view){
        intent.putExtra("surface", 0);
        startActivity(intent);
    }
    public void openEditor(View v){
        intent.putExtra("surface", 1);
        startActivity(intent);
    }

}
