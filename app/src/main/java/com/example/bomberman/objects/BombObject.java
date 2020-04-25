package com.example.bomberman.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.example.bomberman.map.LevelMap;


public class BombObject extends GameObject {
    private int inst_id, power, countdown, instRow, instCol;
    private long installTime, lastDrawTime = -1;
    private boolean detonated = false, burned = false, remove = false;
    private int imageIndex = 0, changeTime = 200; //ms
    private int spaceUp = 0, spaceDown = 0, spaceLeft = 0, spaceRight = 0;

    private final int explosionTime = 1000;

    private LevelMap lvl;

    private Bitmap[] bombImage, explosion_center, explosion_left, explosion_right, explosion_down, explosion_up, explosion_line_vertical, explosion_line_horizontal, after_explosion;
    private Bitmap currentImage;

    public BombObject(LevelMap lvl, Bitmap image, int imageRows, int imageColumns, int inst_id, int col, int row, int cellSize, int power, int countdown) {
        super(image, imageRows, imageColumns, cellSize * col, cellSize * row, cellSize);
        this.lvl = lvl;
        this.countdown = countdown;
        this.installTime = System.nanoTime();
        this.power = power;
        this.instRow = row;
        this.instCol = col;
        this.inst_id = inst_id;

        bombImage = new Bitmap[2];
        explosion_center = new Bitmap[4];
        explosion_right = new Bitmap[4];
        explosion_left = new Bitmap[4];
        explosion_down = new Bitmap[4];
        explosion_up = new Bitmap[4];
        explosion_line_vertical = new Bitmap[4];
        explosion_line_horizontal = new Bitmap[4];
        after_explosion = new Bitmap[6];

        for (int i = 0; i < 2; i++) {
            bombImage[i] = Bitmap.createScaledBitmap(createSubImageAt(0, i), this.cellSize, this.cellSize, false);
        }
        for (int i = 0; i < 4; i++) {
            explosion_center[i] = Bitmap.createScaledBitmap(createSubImageAt(i + 1, 0), this.cellSize, this.cellSize, false);
            explosion_up[i] = Bitmap.createScaledBitmap(createSubImageAt(i + 1, 1), this.cellSize, this.cellSize, false);
            explosion_line_vertical[i] = Bitmap.createScaledBitmap(createSubImageAt(i + 1, 2), this.cellSize, this.cellSize, false);
            explosion_down[i] = Bitmap.createScaledBitmap(createSubImageAt(i + 1, 3), this.cellSize, this.cellSize, false);
            explosion_left[i] = Bitmap.createScaledBitmap(createSubImageAt(i + 1, 4), this.cellSize, this.cellSize, false);
            explosion_line_horizontal[i] = Bitmap.createScaledBitmap(createSubImageAt(i + 1, 5), this.cellSize, this.cellSize, false);
            explosion_right[i] = Bitmap.createScaledBitmap(createSubImageAt(i + 1, 6), this.cellSize, this.cellSize, false);
        }
        for (int i = 0; i < 6; i++) {
            after_explosion[i] = Bitmap.createScaledBitmap(createSubImageAt(5, i), this.cellSize, this.cellSize, false);
        }
        for(int i = power; i > 0; i--){
            if(!lvl.solidBlockInCell(row + (-1)*i, col)){
                spaceUp++;
            }else if(spaceUp != 0){
                spaceUp = 0;
            }
            if(!lvl.solidBlockInCell(row + i, col)){
                spaceDown++;
            }else if(spaceDown != 0){
                spaceDown = 0;
            }
            if(!lvl.solidBlockInCell(row, col + (-1)*i)){
                spaceLeft++;
            }else if(spaceLeft != 0){
                spaceLeft = 0;
            }
            if(!lvl.solidBlockInCell(row, col + i)){
                spaceRight++;
            }else if(spaceRight != 0){
                spaceRight = 0;
            }
        }
        currentImage = bombImage[imageIndex];
    }

    @Override
    public void update(LevelMap lvl) {
        long now = System.nanoTime();
        int passed = (int) ((now - installTime) / 1000000);
        if(passed >= explosionTime/2 && detonated && burned){
            remove = true;
        }else if(passed > explosionTime/2 && detonated && !burned){
            burn();
        }else if (passed > countdown && !detonated) {
            detonate();
        }
    }

    public void detonate(){
        if(!detonated) {
            detonated = true;
            changeTime = explosionTime / 8;
            imageIndex = 0;
            installTime = System.nanoTime();
        }
    }
    public void burn(){
        burned = true;
        changeTime = explosionTime / 12;
        imageIndex = 0;
        installTime = System.nanoTime();
    }
    @Override
    public void draw(Canvas canvas) {
        if(lastDrawTime == -1)
            lastDrawTime = System.nanoTime();
        long deltaTime = (int) ((System.nanoTime() - lastDrawTime) / 1000000);
        if(deltaTime > changeTime) {
            if (!detonated && !burned) {
                currentImage = bombImage[imageIndex];
                imageIndex++;
                if(imageIndex >= 2)
                    imageIndex = 0;
            } else if (detonated && !burned) {
                if(imageIndex >= 4)
                    imageIndex = 0;
                currentImage = explosion_center[imageIndex];
                imageIndex++;
            } else if (detonated && burned) {
                if(imageIndex >= 6)
                    imageIndex = 0;
                currentImage= after_explosion[imageIndex];
                imageIndex++;
            }
            lastDrawTime = System.nanoTime();
        }
        if(detonated && !burned && imageIndex < 4){
            for(int i = 0; i < spaceLeft; i++)
                canvas.drawBitmap(explosion_line_horizontal[imageIndex], this.x - i*cellSize, this.y, null);
            for(int i = 0; i < spaceRight; i++)
                canvas.drawBitmap(explosion_line_horizontal[imageIndex], this.x + i*cellSize, this.y, null);
            for(int i = 0; i < spaceUp; i++)
                canvas.drawBitmap(explosion_line_vertical[imageIndex], this.x, this.y - i*cellSize, null);
            for(int i = 0; i < spaceDown; i++)
                canvas.drawBitmap(explosion_line_vertical[imageIndex], this.x, this.y + i*cellSize, null);

            canvas.drawBitmap(explosion_up[imageIndex], this.x, this.y - spaceUp*cellSize, null);
            canvas.drawBitmap(explosion_down[imageIndex], this.x, this.y + spaceDown*cellSize, null);
            canvas.drawBitmap(explosion_left[imageIndex], this.x - spaceLeft*cellSize, this.y, null);
            canvas.drawBitmap(explosion_right[imageIndex], this.x + spaceRight*cellSize, this.y, null);
        }
        canvas.drawBitmap(currentImage, this.x, this.y, null);

    }

    public boolean inExplosionRange(int row, int column){
        if(instRow <= row && instRow + spaceDown >= row && instCol == column)
            return true;
        if(instRow >= row && instRow - spaceUp >= row && instCol == column)
            return true;
        if(instRow == row && instCol >= column && instCol - spaceLeft <= column)
            return true;
        if(instRow == row && instCol <= column && instCol + spaceRight >= column)
            return true;
        return false;
    }

    public int getRow(){
        return instRow;
    }

    public int getColumn(){
        return instCol;
    }

    public int getInstId(){
        return inst_id;
    }

    public int[] getSpaces(){
        return new int[]{spaceUp, spaceDown, spaceRight, spaceLeft};
    }

    public boolean isDetonated() {
        return detonated;
    }

    public boolean isBurnedOut() {
        return burned;
    }

    public int getPower() {
        return this.power;
    }
    public boolean canRemove(){
        return remove;
    }
}
