package com.example.bomberman.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class BombObject extends GameObject{
    private int power, countdown;
    private long installTime;
    private boolean detonated = false, burned;
    private int col, rows;

    private Bitmap bombState[];
    private int currentImg = 0;

    public BombObject(Bitmap image, int rows, int cols, int x, int y, int size, int power, int countdown) {
        super(image, rows, cols, x, y, size);
        this.x = ((x+size/2)/size)*size;
        this.y = ((y+size)/size)*size;
        this.countdown = countdown; //sec!!!!!!
        this.rows = rows;
        this.col = cols;
        this.installTime = System.nanoTime();
        this.power = power;

        bombState = new Bitmap[col];
        bombState[0] = Bitmap.createScaledBitmap(image, size, size, false);

        Log.d("Bombs", x/cellSize+" :: "+y/cellSize);
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
        if(detonated){
            int z = 1;
            for(int i = 0; i < 2; i++, z*=-1) {
                for (int j = 1; j <= power; j++)
                    canvas.drawBitmap(bombState[currentImg], x + z*j * cellSize, y, null);

                for (int j = 1; j <= power; j++)
                    canvas.drawBitmap(bombState[currentImg], x, y + z*j * cellSize, null);
            }
        }
    }

    public boolean isDetonated(){
        return detonated;
    }
    public boolean isBurnedOut(){return burned;}
    public int getPower(){
        return this.power;
    }
}
