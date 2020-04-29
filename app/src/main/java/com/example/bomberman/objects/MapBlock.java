package com.example.bomberman.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MapBlock extends GameObject {
    private boolean destroyed = false;
    public static final int TYPE_SOLID = 0, TYPE_BONUS = 1, TYPE_BACKGROUND = 2, TYPE_BOMB = 3;
    public static final int BONUS_BOMB = 0, BONUS_FLAME = 1, BONUS_SPEED = 2;
    private int type, bonusType;
    Paint blockColor = new Paint();

    public MapBlock(Bitmap image, int x, int y, int cellSize, int type) {
        super(image, 1, 1, x, y, cellSize);
        if (type > 2) {
            this.type = 2;
        } else {
            this.type = type;
        }

        switch (this.type) {
            case TYPE_SOLID:
                blockColor.setColor(Color.BLACK);
                break;
            case TYPE_BONUS:
                blockColor.setColor(Color.BLUE);
                break;
            case TYPE_BACKGROUND:
                blockColor.setColor(Color.WHITE);
                break;
            default:
                blockColor.setColor(Color.parseColor("#FF00FF"));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(x, y, x + cellSize, y + cellSize, blockColor);
    }

    public void destroy() {
        destroyed = true;
        switch (bonusType) {
            case BONUS_BOMB:
                blockColor.setColor(Color.GREEN);
                break;
            case BONUS_FLAME:
                blockColor.setColor(Color.RED);
                break;
            case BONUS_SPEED:
                blockColor.setColor(Color.YELLOW);
                break;
            default:
                blockColor.setColor(Color.WHITE);
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public int getType() {
        return type;
    }
    public int getTypeBonus(){
        return bonusType;
    }

    public boolean isPassable() {
        switch (type) {
            case TYPE_BONUS:
                if (destroyed)
                    return true;
                else
                    return false;
            case TYPE_BACKGROUND:
                return true;
            default:
                return false;
        }
    }

    public void setType(int type, int bonusType) {
        this.type = type;
        this.bonusType = bonusType;
        switch (this.type) {
            case TYPE_SOLID:
                blockColor.setColor(Color.BLACK);
                break;
            case TYPE_BONUS:
                blockColor.setColor(Color.BLUE);
                break;
            case TYPE_BACKGROUND:
            case TYPE_BOMB:
                blockColor.setColor(Color.WHITE);
                break;
            default:
                blockColor.setColor(Color.parseColor("#FF00FF"));
        }
    }

    public int takeBonus() {
        type = TYPE_BACKGROUND;
        blockColor.setColor(Color.WHITE);
        return bonusType;
    }
}
