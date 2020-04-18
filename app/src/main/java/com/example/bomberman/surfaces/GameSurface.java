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
import com.example.bomberman.objects.BackGround;
import com.example.bomberman.objects.CharacterObject;
import com.example.bomberman.objects.ControlsObject;


public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread thread;
    private CharacterObject character;
    private ControlsObject control;
    private BackGround back;
    private final int gameCubeHeight = 64;

    public GameSurface(Context context) {
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap character_img = BitmapFactory.decodeResource(this.getResources(), R.raw.bomberman);
        this.character = new CharacterObject(this, character_img, 5, 3, getWidth()/2, getHeight()/2, 1);
        this.control = new ControlsObject(this, getHeight(), getWidth(), Color.BLUE);
        this.back = new BackGround(getWidth(), getHeight(), gameCubeHeight);

        this.thread = new GameThread(this, holder);
        this.thread.setRunning(true);
        this.thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN :
            case MotionEvent.ACTION_MOVE:
                control.pressEvent(this.character, (int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_UP :
                character.stay();
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
        this.back.draw(canvas);
        this.character.draw(canvas);
        this.control.draw(canvas);
    }

    public void update(){
        this.character.update();
    }

    public int getGameCubeHeight() {
        return gameCubeHeight;
    }
}
