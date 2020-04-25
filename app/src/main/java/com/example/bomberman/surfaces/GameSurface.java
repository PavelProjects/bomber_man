package com.example.bomberman.surfaces;

import android.content.Context;
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
    private int cellSize;

    public GameSurface(Context context) {
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.levelMap = new LevelMap(this, getWidth(), getHeight(), R.raw.map);
        cellSize = levelMap.getCellSize();
        this.control = new ControlsObject(this, 3*cellSize, getHeight() - 3*cellSize, Color.BLUE,  cellSize, cellSize);

        this.thread = new GameThread(this, holder);
        this.thread.setRunning(true);
        this.thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        control.pressEvent(event, this.levelMap.getCharacter(), this);
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

    public LevelMap getLevelMap(){
        return levelMap;
    }
}
