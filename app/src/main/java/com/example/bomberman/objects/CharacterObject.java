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
    private long lastDrawTime = -1, lastImageChange = -1;
    private int x_dir = 0, y_dir = 0, image_index = 0, deltaTime;
    private Bitmap currentImage;
    private Bitmap topToBottom[], leftToRight[], rightToLeft[], bottomToTop[], dead[];

    public CharacterObject(GameSurface surface, Bitmap image,int rows, int columns, int x, int y, int id) {
        super(image, rows, columns, x, y, surface.getGameCubeHeight());
        speed = 0.2f;
        bomb_count = 3;
        bomb_power = 2;
        kills = 0;
        this.surface = surface;
        this.id = id;

        topToBottom = new Bitmap[this.columns];
        bottomToTop = new Bitmap[this.columns];
        rightToLeft = new Bitmap[this.columns];
        leftToRight = new Bitmap[this.columns];
        dead = new Bitmap[this.columns];

        for(int i = 0; i < this.columns; i++){
            topToBottom[i] = Bitmap.createScaledBitmap(createSubImageAt(0, i), getCellSize()-5, 4*getCellSize()/3, false);
            rightToLeft[i] = Bitmap.createScaledBitmap(createSubImageAt(1, i), getCellSize()-5, 4*getCellSize()/3, false);
            leftToRight[i] = Bitmap.createScaledBitmap(createSubImageAt(2, i), getCellSize()-5, 4*getCellSize()/3, false);
            bottomToTop[i] = Bitmap.createScaledBitmap(createSubImageAt(3, i), getCellSize()-5, 4*getCellSize()/3, false);
            dead[i] = createSubImageAt(4, i);
        }
        currentImage = topToBottom[image_index];
    }

    public void move(){
        long now = System.nanoTime();
        if(lastDrawTime == -1)
            lastDrawTime = now;
        deltaTime = (int) (now - lastDrawTime)/1000000;
        float distance = speed * deltaTime;

        this.x = x + (int)(x_dir * distance);
        this.y = y + (int)(y_dir * distance);

        if(x < 0){
            x = 0;
        }else if(x > surface.getWidth() - cellSize){
            x = surface.getWidth() - cellSize;
        }
        if(y < 0){
            y = 0;
        }else if(y > surface.getHeight() - cellSize){
            y = surface.getHeight() - cellSize;
        }

        image_index++;
        if(image_index >= columns){
            image_index = 1;
        }

        if(lastImageChange == -1)
            lastDrawTime = System.nanoTime();
        deltaTime = (int) (now - lastImageChange)/1000000;
        if(deltaTime > 100) {
            if (y_dir > 0) {
                currentImage = topToBottom[image_index];
            } else if (y_dir < 0) {
                currentImage = bottomToTop[image_index];
            } else if (x_dir < 0) {
                currentImage = rightToLeft[image_index];
            } else if (x_dir > 0) {
                currentImage = leftToRight[image_index];
            }
            lastImageChange = System.nanoTime();
        }
    }

    public void stay(){
        image_index = 0;
        if (y_dir > 0) {
            currentImage = topToBottom[image_index];
        } else if (y_dir < 0) {
            currentImage = bottomToTop[image_index];
        } else if (x_dir < 0) {
            currentImage = rightToLeft[image_index];
        } else if (x_dir > 0) {
            currentImage = leftToRight[image_index];
        }
        x_dir = 0;
        y_dir = 0;

    }

    public void addBomb(){
        if(bombs.size() <= bomb_count){
            bombs.add(new BombObject(BitmapFactory.decodeResource(surface.getResources(), R.raw.bomb), 1, 1, this.x, this.y, getCellSize(), this.bomb_power, 3));
        }
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(currentImage, x, y, null);
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
