package com.example.bomberman.surfaces;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread{
    private boolean running;

    private GameSurface surface;
    private SurfaceHolder holder;

    public GameThread(GameSurface surface, SurfaceHolder holder){
        this.surface = surface;
        this.holder = holder;
    }

    @Override
    public void run(){
        long lastUpdate = System.nanoTime();

        while(running){
            Canvas canvas = null;
            try{
                canvas = this.holder.lockCanvas();
                synchronized (canvas){
                    this.surface.update();
                    this.surface.draw(canvas);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(canvas != null){
                    this.holder.unlockCanvasAndPost(canvas);
                }
            }
            long now = System.nanoTime();
            long waitTime = (now - lastUpdate)/1000000;
            if(waitTime < 10){
                waitTime = 10;
            }
            try{
                this.sleep(waitTime);
            }catch(Exception e){
                e.printStackTrace();
            }
            lastUpdate = System.nanoTime();
        }
    }

    public void setRunning(boolean state){
        this.running = state;
    }
}
