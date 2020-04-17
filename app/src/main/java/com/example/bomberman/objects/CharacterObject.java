package com.example.bomberman.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.bomberman.R;
import com.example.bomberman.surfaces.GameSurface;

import java.util.ArrayList;

public class CharacterObject extends GameObject{
    private int  bomb_count, bomb_power, kills, id, installed;
    private float speed;
    private GameSurface surface;
    private ArrayList<BombObject> bombs = new ArrayList<>();
    private long lastDrawTime = -1;
    private int x_dir = 0, y_dir = 0;

    public CharacterObject(GameSurface surface, Bitmap image, int x, int y, int id) {
        super(image, 1, 1, x, y);
        speed = 0.2f;
        bomb_count = 2;
        bomb_power = 1;
        kills = 0;
        this.surface = surface;
        this.id = id;
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

    public void addBomb(){
        if(bombs.size() < bomb_count){
            bombs.add(new BombObject(BitmapFactory.decodeResource(surface.getResources(), R.raw.bomb), 1, 1, this.x, this.y, this.bomb_power, 5));
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
        this.lastDrawTime = System.nanoTime();
        for(BombObject bomb : bombs)
            bomb.draw(canvas);
    }

    public void update(){
        this.move();
        for(BombObject bomb : bombs){
            bomb.update();
            if(bomb.isBurnedOut()) {
                bombs.remove(bomb);
            }
        }
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

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setInstalled(int inst){
        this.installed = inst;
    }
    public int getInstalled(){
        return installed;
    }
}
