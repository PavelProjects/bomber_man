package com.example.bomberman.surfaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.bomberman.R;
import com.example.bomberman.map.LevelMap;
import com.example.bomberman.objects.ControlsObject;


public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread thread;
    private ControlsObject control;
    private LevelMap levelMap;

    public GameSurface(Context context) {
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.levelMap = new LevelMap(this, getWidth(), getHeight(), 1);
        this.control = new ControlsObject(this, 3*levelMap.getCellSize(), getHeight() - 3*levelMap.getCellSize(), Color.BLUE, levelMap.getCellSize());

        this.thread = new GameThread(this, holder);
        this.thread.setRunning(true);
        this.thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN :
            case MotionEvent.ACTION_MOVE:
                control.pressEvent(this.levelMap, (int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_UP :
                this.levelMap.getCharacter().stay();
                break;
        }
        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

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
        this.levelMap.draw(canvas);
        this.control.draw(canvas);
    }

    public void update(){
        this.levelMap.update();
    }
}
