package com.example.bomberman.surfaces;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.bomberman.map.MapEditor;

public class EditorSurface extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread thread;
    private MapEditor mapEditor;

    public EditorSurface(Context context) {
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.mapEditor = new MapEditor(this, getWidth(), getHeight());

        this.thread = new GameThread(this, holder);
        this.thread.setRunning(true);
        this.thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            mapEditor.pressEvent((int) event.getX(), (int) event.getY());
        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.mapEditor.draw(canvas);
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

    public void exit(){
        mapEditor.parsToJSON();
    }
}
