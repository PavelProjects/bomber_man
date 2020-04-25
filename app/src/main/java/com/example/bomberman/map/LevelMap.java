
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
import java.util.List;

public class LevelMap {
    private MapBlock[][] blocks;
    private CharacterObject character;
    private int width, height;
    public static int rows, columns;
    private Paint linePaint = new Paint();
    private int cellSize;
    private GameSurface surface;
    private ArrayList<BombObject> bombs = new ArrayList<>();

    public LevelMap(GameSurface surface, int width, int height, int map) {
        Log.d("Map", "Start loading");
        this.surface = surface;
        this.height = height;
        this.width = width;
        loadFromJson(map);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(2);
        Log.d("Map", "Loaded");
    }

    private void loadFromJson(int map) {
        String jsonString = null;
        try {
            InputStream inputStream = surface.getResources().openRawResource(map);
            InputStreamReader inputReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputReader);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
            jsonString = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.d("Map", jsonString);
        try {
            JSONObject jsonMap = new JSONObject(jsonString);
            this.cellSize = jsonMap.getInt("cellSize");
            spawnCharacter(jsonMap.getInt("chr_row"), jsonMap.getInt("chr_col"));
            JSONArray jsonBlocks = jsonMap.getJSONArray("blocks");
            rows = height / cellSize;
            columns = width / cellSize;
            blocks = new MapBlock[rows+1][columns+1];
            for (int i = 0; i <= rows; i++) {
                for (int j = 0; j <= columns; j++) {
                    blocks[i][j] = new MapBlock(null, i * cellSize, j * cellSize, cellSize, 2);
                }
            }
            JSONObject bl;
            for (int i = 0; i < jsonBlocks.length(); i++) {
                bl = jsonBlocks.getJSONObject(i);
                blocks[bl.getInt("row")][bl.getInt("col")] = new MapBlock(null,
                        bl.getInt("row"), bl.getInt("col"), cellSize, bl.getInt("type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void spawnCharacter(int row, int col) {
        Bitmap character_img = BitmapFactory.decodeResource(surface.getResources(), R.raw.bomberman);
        this.character = new CharacterObject(this, character_img, 5, 3, row, col, cellSize, 1);
    }

    public void update() {
        try {
            character.update(this);
            for (int i = 0; i < bombs.size(); i++) {
                bombs.get(i).update(this);
                if (bombs.get(i).isDetonated()) {
                    BombObject bomb = bombs.get(i);
                    if (bomb.getRow() - bomb.getSpaces()[0] - 1 >= 0) {
                        if (blocks[bomb.getRow() - bomb.getSpaces()[0] - 1][bomb.getColumn()].getType() == 1) {
                            blocks[bomb.getRow() - bomb.getSpaces()[0] - 1][bomb.getColumn()].destroy();
                        }
                    }
                    if (bomb.getRow() + bomb.getSpaces()[1] + 1 < this.rows) {
                        if (blocks[bomb.getRow() + bomb.getSpaces()[1] + 1][bomb.getColumn()].getType() == 1) {
                            blocks[bomb.getRow() + bomb.getSpaces()[1] + 1][bomb.getColumn()].destroy();
                        }
                    }
                    if (bomb.getColumn() + bomb.getSpaces()[2] + 1 < this.columns) {
                        if (blocks[bomb.getRow()][bomb.getColumn() + bomb.getSpaces()[2] + 1].getType() == 1) {
                            blocks[bomb.getRow()][bomb.getColumn() + bomb.getSpaces()[2] + 1].destroy();
                        }
                    }
                    if (bomb.getColumn() - bomb.getSpaces()[3] - 1 >= 0) {
                        if (blocks[bomb.getRow()][bomb.getColumn() - bomb.getSpaces()[3] - 1].getType() == 1) {
                            blocks[bomb.getRow()][bomb.getColumn() - bomb.getSpaces()[3] - 1].destroy();
                        }
                    }
                    for (BombObject bombObject : bombs) {
                        if (bombs.get(i).inExplosionRange(bombObject.getRow(), bombObject.getColumn()) && !bombObject.isDetonated()) {
                            bombObject.detonate();
                        }
                    }
                }
                if (bombs.get(i).canRemove()) {
                    character.removeBomb(bombs.get(i));
                    bombs.remove(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                blocks[i][j].draw(canvas);
        for (int i = 0; i <= height / cellSize; i++) {
            canvas.drawLine(0, i * cellSize, width, i * cellSize, linePaint);
        }
        for (int i = 0; i <= width / cellSize; i++) {
            canvas.drawLine(i * cellSize, 0, i * cellSize, height, linePaint);
        }
        for (int i = 0; i < bombs.size(); i++) //сделать бомбы наследников объекта блока карты
            bombs.get(i).draw(canvas);
        character.draw(canvas);
    }

    public boolean solidBlockInCell(int row, int column) {
        //Log.d("Map", row + " " + column + "||" + rows + " " + columns);
       if (row >= 0 && column >= 0 && row < rows && column < columns) {
            return !blocks[row][column].isPassable();
        }
        return true;
    }

    public int getCellSize() {
        return cellSize;
    }
    public CharacterObject getCharacter() {
        return character;
    }
    public MapBlock[][] getBlocks(){
        return blocks;
    }
    public List<BombObject> getBombs(){
        return bombs;
    }

    public int getWidth(){
        return surface.getWidth();
    }

    public int getHeight(){
        return surface.getHeight();
    }
}
