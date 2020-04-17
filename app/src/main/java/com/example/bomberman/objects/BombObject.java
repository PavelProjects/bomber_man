package com.example.bomberman.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.example.bomberman.surfaces.GameSurface;

public class BombObject extends GameObject{
    private int power, countdown;
    private long installTime;
    private boolean detonated = false, burned;
    private int col, rows;

    private Bitmap bombState[];
    private int currentImg = 0;

    public BombObject(Bitmap image, int rows, int cols, int x, int y, int power, int countdown) {
        super(image, rows, cols, x, y);
        this.countdown = countdown; //sec!!!!!!
        this.rows = rows;
        this.col = cols;
        this.installTime = System.nanoTime();
        this.power = power;

        bombState = new Bitmap[col];
        bombState[0] = image;
//
//        for(int i = 0; i < col ; i++) {
//            bombState[i] = createSubImageAt(1, i);
//        }
        //Log.d("bombs", "created");
    }

    public void update(){
        long now = System.nanoTime();
        int passed = (int) ((now - installTime) /1000000000); //sec
        //Log.d("bombs", "passed "+String.valueOf(passed));
        if(passed >= countdown){
            currentImg = col-1;
            detonated = true;
        }
        if(detonated && passed >= countdown + 1){
            burned = true;
           // Log.d("bombs", "ded");
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bombState[currentImg], x, y, null);
    }

    public boolean isDetonated(){
        return detonated;
    }
    public boolean isBurnedOut(){return burned;}
}
