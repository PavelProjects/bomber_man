
package com.example.bomberman.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.bomberman.R;
import com.example.bomberman.objects.CharacterObject;
import com.example.bomberman.objects.MapBlock;
import com.example.bomberman.surfaces.GameSurface;

public class LevelMap {
    private MapBlock[][] blocks;
    private CharacterObject character;
    private int width, height, rows, columns;
    private Paint linePaint = new Paint();
    private final int cellSize = 64;
    private GameSurface surface;

    public LevelMap(GameSurface surface, int width, int height, int map){
        Log.d("Map", "Start loading");
        rows = width/cellSize;
        columns = height/cellSize;
        loadFromJson();
        this.surface = surface;
        this.height = height;
        this.width = width;
        Bitmap character_img = BitmapFactory.decodeResource(surface.getResources(), R.raw.bomberman);
        this.character = new CharacterObject(this, surface, character_img, 5, 3, 128, 182, cellSize, 1);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(2);
        Log.d("Map", "Loaded, rows|columns");
    }

    private void loadFromJson(){
        blocks = new MapBlock[rows][columns];
        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                blocks[i][j] = new MapBlock(null, i * 64, j * 64, cellSize, false, true);
            }
        }
        for(int i = 0; i < rows*columns){
            
        }
    }

    public void update(){
        if((character.getY_dir() != 0 || character.getX_dir() != 0) && characterCanMove(character.getX_dir(), character.getY_dir())) {
            character.update();
        }else{
            character.updateImage();
        }
    }

    public void draw(Canvas canvas){
        canvas.drawColor(Color.WHITE);
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                blocks[i][j].draw(canvas);
        for(int i = 0; i <= height/cellSize; i++){
            canvas.drawLine(0, i*cellSize, width, i*cellSize, linePaint);
        }
        for(int i = 0; i <= width/cellSize; i++){
            canvas.drawLine(i*cellSize, 0, i*cellSize, height, linePaint);
        }
        character.draw(canvas);
    }

    public void setCharacterDirection(int x, int y){
        character.setX_dir(x);
        character.setY_dir(y);
    }

    public boolean characterCanMove(int dirx, int diry){
        int col = (character.getX()+character.getWidth()/2)/cellSize-1;
        int row = (character.getY()+character.getHeight()/2)/cellSize-1;
        try {
            MapBlock block = blocks[row + diry][col + dirx];
            if(!block.isPassable()){
                if(dirx < 0) {
                    if (block.getX() + cellSize >= character.getX())
                        return false;
                }else if(dirx > 0){
                    if(block.getX() <= character.getX() + character.getWidth())
                        return false;
                }
                if(diry < 0){
                    if(block.getY() + cellSize/2 >= character.getY())
                        return false;
                }else if(diry > 0){
                    if(block.getY() <= character.getY() + character.getHeight())
                        return false;
                }
            }
        }catch (Exception e){}
        Log.d("Map", row + " :: " + col + "||" + (row + diry) + " " + (col + dirx));
        return true;
    }


    public void characterStay(){
        character.stay();
    }

    public void plantBomb(){
        character.addBomb();
    }

    public int getCellSize(){
        return cellSize;
    }

    public CharacterObject getCharacter(){
        return character;
    }
}
