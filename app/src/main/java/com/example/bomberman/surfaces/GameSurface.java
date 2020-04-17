package com.example.bomberman.surfaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.bomberman.R;
import com.example.bomberman.objects.CharacterObject;
import com.example.bomberman.objects.ControlObjects;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread thread;
    private CharacterObject character;
    private ControlObjects control;
    private int x_dir, y_dir;
    private int view_high, view_width;

    public GameSurface(Context context) {
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap character_img = BitmapFactory.decodeResource(this.getResources(), R.raw.model);
        Bitmap control_img = BitmapFactory.decodeResource(this.getResources(), R.raw.button);
        this.character = new CharacterObject(this, character_img, 100, 100);
        this.control = new ControlObjects(this, control_img);
        this.thread = new GameThread(this, holder);
        this.thread.setRunning(true);
        this.thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN : {
                int x = (int) event.getX();
                if (x > view_width / 2) {
                    character.setX_dir(1);
                } else {
                    character.setX_dir(-1);
                }
                break;
            }
            case MotionEvent.ACTION_UP :
                character.setX_dir(0);
                break;
        }
        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        view_high = canvas.getHeight();
        view_width = canvas.getWidth();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry){
            try{
                this.thread.setRunning(false);
                this.thread.join();
                retry = false;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(Color.BLUE);
        this.character.draw(canvas);
        this.control.draw(canvas);
    }

    public void update(){
        character.move();
    }
}
