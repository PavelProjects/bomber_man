package com.example.bomberman.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.bomberman.surfaces.GameSurface;

public class CharacterObject extends GameObject{
    private int  bomb_count, bomb_power, kills;
    private float speed;
    private GameSurface surface;
    private long lastDrawTime = -1;
    private int x_dir = 0, y_dir = 0;

    public CharacterObject(GameSurface surface, Bitmap image, int x, int y) {
        super(image, x, y);
        speed = 0.1f;
        bomb_count = 1;
        bomb_power = 1;
        kills = 0;
        this.surface = surface;
    }

    public void move(){
        long now = System.nanoTime();
        if(lastDrawTime == -1)
            lastDrawTime = now;
        int deltaTime = (int) (now - lastDrawTime)/1000000;
        float distance = speed * deltaTime;

        this.x = x + (int)(x_dir * distance);
        this.y = y + (int)(y_dir * distance);

        if(x < 0){
            x = 0;
        }else if(x > surface.getWidth() - width){
            x = surface.getWidth() - width;
        }
        if(y < 0){
            y = 0;
        }else if(y > surface.getHeight() - height){
            y = surface.getHeight() - height;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
        this.lastDrawTime = System.nanoTime();
    }

    public void setX_dir(int x){
        this.x_dir = x;
    }
    public void setY_dir(int y){
        this.y_dir = y;
    }

    public void setSpeed(float s){
        this.speed = s;
    }

    public float getSpeed() {
        return speed;
    }

    public int getKills() {
        return kills;
    }

    public int getBomb_count() {
        return bomb_count;
    }

    public int getBomb_power() {
        return bomb_power;
    }

    public void setBomb_count(int bomb_count) {
        this.bomb_count = bomb_count;
    }

    public void setBomb_power(int bomb_power) {
        this.bomb_power = bomb_power;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }
}
