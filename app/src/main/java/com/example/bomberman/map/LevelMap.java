
package com.example.bomberman.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.bomberman.R;
import com.example.bomberman.objects.BombObject;
import com.example.bomberman.objects.CharacterObject;
import com.example.bomberman.objects.MapBlock;
import com.example.bomberman.surfaces.GameSurface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LevelMap {
    private MapBlock[][] blocks;
    private CharacterObject character;
    private int width, height, rows, columns;
    private Paint linePaint = new Paint();
    private int cellSize;
    private GameSurface surface;
    private ArrayList<BombObject> bombs = new ArrayList<>();

    public LevelMap(GameSurface surface, int width, int height, int map){
        Log.d("Map", "Start loading");
        this.surface = surface;
        this.height = height;
        this.width = width;
        loadFromJson(map);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(2);
        Log.d("Map", "Loaded, rows|columns");
    }

    private void loadFromJson(int map){
        String jsonString = null;
        try{
            InputStream inputStream = surface.getResources().openRawResource(map);
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputReader);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null){
                builder.append(line);
                builder.append("\n");
            }
            jsonString = builder.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        Log.d("Map", jsonString);
        try {
            JSONObject jsonMap = new JSONObject(jsonString);
            this.cellSize = jsonMap.getInt("cellSize");
            Bitmap character_img = BitmapFactory.decodeResource(surface.getResources(), R.raw.bomberman);
            this.character = new CharacterObject(surface, character_img, 5, 3, jsonMap.getInt("chr_col"), jsonMap.getInt("chr_row"), cellSize, 1);
            JSONArray jsonBlocks = jsonMap.getJSONArray("blocks");
            rows = width/cellSize;
            columns = height/cellSize;
            blocks = new MapBlock[rows][columns];
            for(int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    blocks[i][j] = new MapBlock(null, i * 64, j * 64, cellSize, false, true);
                }
            }
            JSONObject bl;
            for(int i = 0; i < jsonBlocks.length(); i++){
                bl = jsonBlocks.getJSONObject(i);
                blocks[bl.getInt("row")][bl.getInt("col")] = new MapBlock(null, bl.getInt("row"), bl.getInt("col"), cellSize, bl.getBoolean("bonus"), bl.getBoolean("passable"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void update(){
        try {
            for (int i = 0; i < bombs.size(); i++) {
                bombs.get(i).update();
                if (bombs.get(i).canRemove()) {
                    bombs.remove(i);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if((character.getY_dir() != 0 || character.getX_dir() != 0) && !solidBlockNear(character.getRow(), character.getColumn(),
                character.getX(), character.getY(), character.getWidth(), character.getHeight(), character.getX_dir(), character.getY_dir())) {
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
        for (BombObject bomb : bombs)
            bomb.draw(canvas);
        character.draw(canvas);
    }

    public void setCharacterDirection(int x, int y){
        character.setX_dir(x);
        character.setY_dir(y);
    }

    public boolean solidBlockNear(int row, int col, int x, int y, int width, int height, int dirx, int diry){
        try {
            //Log.d("Map", row + "|" + col + "  " + width + "|" + height + " " + dirx + "|" + diry);
            if(!blocks[row + diry][col + dirx].isPassable()){
               if(dirx < 0) {
                    if (blocks[row + diry][col + dirx].getX() + cellSize >= x) {
                        return true;
                    }
                }else if(dirx > 0){
                    if(blocks[row + diry][col + dirx].getX() <= x + width) {
                        return true;
                    }
                }
                if(diry < 0){
                    if(blocks[row + diry][col + dirx].getY() + cellSize/2 >= y) {
                        return true;
                    }
                }else if(diry > 0){
                    if(blocks[row + diry][col + dirx].getY() <= y + height) {
                        return true;
                    }
                }
            }
        }catch (Exception e){}
        //Log.d("Map", row + " :: " + col + "||" + (row + diry) + " " + (col + dirx));
        return false;
    }

    public boolean solidBlockInCell(int row, int column){
        if(row >= 0 && column >= 0){
            return !blocks[row][column].isPassable();
        }
        return true;
    }

    public void characterStay(){
        character.stay();
    }

    public void plantBomb(){
        if (bombs.size() <= character.getBomb_count()) {
            bombs.add(new BombObject(this, BitmapFactory.decodeResource(surface.getResources(), R.raw.bomb), 6, 7,
                    character.getColumn(), character.getRow(), getCellSize(), character.getBomb_power(), 3000));
        }
    }

    public int getCellSize(){
        return cellSize;
    }

    public CharacterObject getCharacter(){
        return character;
    }
}
