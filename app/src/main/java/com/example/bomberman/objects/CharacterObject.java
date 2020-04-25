package com.example.bomberman.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.example.bomberman.map.LevelMap;


public class CharacterObject extends GameObject {
    private int bomb_count, bomb_power, kills, id, installed = 0, height, width;
    private float speed;
    private LevelMap lvl;
    private long lastDrawTime = -1, lastImageChange = -1, lastPlantTime = -1;
    private int x_dir = 0, y_dir = 0, image_index = 0, deltaTime;
    private Bitmap currentImage;
    private Bitmap topToBottom[], leftToRight[], rightToLeft[], bottomToTop[], deadImage[];
    private boolean burned = false, dead = false;
    private final int padding = 2;

    Paint dot = new Paint();

    public CharacterObject(LevelMap lvl, Bitmap image, int rows, int columns, int col, int row, int cellSize, int id) {
        super(image, rows, columns, col * cellSize, row * cellSize, cellSize);
        speed = 0.3f;
        bomb_count = 3;
        bomb_power = 5;
        kills = 0;
        this.lvl = lvl;
        this.id = id;
        this.width = cellSize - 10;
        this.height = 4 * cellSize / 3;

        topToBottom = new Bitmap[this.columns];
        bottomToTop = new Bitmap[this.columns];
        rightToLeft = new Bitmap[this.columns];
        leftToRight = new Bitmap[this.columns];
        deadImage = new Bitmap[this.columns];

        for (int i = 0; i < this.columns; i++) {
            topToBottom[i] = Bitmap.createScaledBitmap(createSubImageAt(0, i), width, height, false);
            rightToLeft[i] = Bitmap.createScaledBitmap(createSubImageAt(1, i), width, height, false);
            leftToRight[i] = Bitmap.createScaledBitmap(createSubImageAt(2, i), width, height, false);
            bottomToTop[i] = Bitmap.createScaledBitmap(createSubImageAt(3, i), width, height, false);
            deadImage[i] = Bitmap.createScaledBitmap(createSubImageAt(4, i), width, height, false);
        }
        currentImage = topToBottom[image_index];

        dot.setColor(Color.RED);
    }

    public void setDirection(int x_dir, int y_dir) {
        this.x_dir = x_dir;
        this.y_dir = y_dir;
    }

    public void move() {
        if (!burned && canMove(lvl.getBlocks())) {
            long now = System.nanoTime();
            if (lastDrawTime == -1)
                lastDrawTime = now;
            deltaTime = (int) (now - lastDrawTime) / 1000000;
            float distance = speed * deltaTime;

            this.x = x + (int) (x_dir * distance);
            this.y = y + (int) (y_dir * distance);
            if(!canMove(lvl.getBlocks())){
                this.x = x - (int) (x_dir * distance);
                this.y = y - (int) (y_dir * distance);
            }
            if (x < 0) {
                x = 0;
            } else if (x > lvl.getWidth() - cellSize) {
                x = lvl.getWidth() - cellSize;
            }

            if (y + cellSize / 2 < 0) {
                y = -cellSize / 2;
            } else if (y > lvl.getHeight() - cellSize) {
                y = lvl.getHeight() - cellSize;
            }
        }
    }

    private void updateImage() {
        if (lastImageChange == -1)
            lastImageChange = System.nanoTime();
        deltaTime = (int) (System.nanoTime() - lastImageChange) / 1000000;
        if (deltaTime > 200) {
            if (!burned) {
                if (image_index >= columns) {
                    image_index = 1;
                }
                if (y_dir > 0) {
                    currentImage = topToBottom[image_index];
                } else if (y_dir < 0) {
                    currentImage = bottomToTop[image_index];
                } else if (x_dir < 0) {
                    currentImage = rightToLeft[image_index];
                } else if (x_dir > 0) {
                    currentImage = leftToRight[image_index];
                }
            } else {
                if (image_index >= columns) {
                    dead = true;
                } else {
                    currentImage = deadImage[image_index];
                }
            }
            image_index++;
            lastImageChange = System.nanoTime();
        }
    }

    public void stay() {
        if (!burned) {
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
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(currentImage, x, y, null);

        dot.setColor(Color.RED);
        canvas.drawCircle(x + width / 2, y + height / 2, 10, dot);
        dot.setColor(Color.BLUE);
        canvas.drawCircle(x, y + cellSize / 2, 5, dot);
        canvas.drawCircle(x - 5 + width, y + cellSize / 2, 5, dot);
        canvas.drawCircle(x - 5 + width, y + height, 5, dot);
        canvas.drawCircle(x, y + height, 5, dot);
        this.lastDrawTime = System.nanoTime();
    }

    @Override
    public void update(LevelMap lvl) {
//        for (int i = 0; i < lvl.getBombs().size(); i++) {
//            if (lvl.getBombs().get(i).isDetonated()) {
//                if (lvl.getBombs().get(i).inExplosionRange(getRow(), getColumn())) {
//                    burned = true;
//                    image_index = 0;
//                }
//            }
//        }
        if (y_dir != 0 || x_dir != 0)
            this.move();
        this.updateImage();
        if (lvl.getBlocks()[getRow()][getColumn()].getType() == 1 && lvl.getBlocks()[getRow()][getColumn()].isDestroyed()) {
            Log.d("Character", "Bonus");
            lvl.getBlocks()[getRow()][getColumn()].setType(2);
        }
        if (dead) {
            dead = burned = false;
            this.x = this.y = cellSize;
            bomb_count = 3;
            bomb_power = 2;
            kills = 0;
            image_index = 0;
            currentImage = topToBottom[image_index];
            lvl.spawnCharacter(1, 2);
        }

    }

    public boolean canMove(MapBlock[][] blocks) {
        MapBlock block;
        if(x_dir != 0) {
            block = nextCellBlock(x + width / 2, y + cellSize / 2, blocks);
            if (x_dir > 0) {
                if (block != null && !block.isPassable() && block.getX() < x + width + padding) {
//                Log.d("Map", "stop1");
                    return false;
                }
            }
            if (x_dir < 0) {
                if (block != null && !block.isPassable() && block.getX() + cellSize > x - padding) {
//                Log.d("Map", "stop2");
                    return false;
                }
            }
            block = nextCellBlock(x + width / 2, y + height, blocks);
            if (x_dir > 0) {
                if (block != null && !block.isPassable() && block.getX() < x + width + padding) {
//                Log.d("Map", "stop3");
                    return false;
                }
            }
            if (x_dir < 0) {
                if (block != null && !block.isPassable() && block.getX() + cellSize > x - padding) {
//                Log.d("Map", "stop4");
                    return false;
                }
            }
        }
        if(y_dir != 0) {
            block = nextCellBlock(x, y + height / 2, blocks);
            if (y_dir > 0) {
                if (block != null && !block.isPassable() && block.getY() < y + height + padding) {
//                Log.d("Map", "stop5");
                    return false;
                }
            }
            if (y_dir < 0) {
                if (block != null && !block.isPassable() && block.getY() + cellSize > y + cellSize / 2 - padding) {
//                Log.d("Map", "stop6");
                    return false;
                }
            }
            block = nextCellBlock(x + width, y + height / 2, blocks);
            if (y_dir > 0) {
                if (block != null && !block.isPassable() && block.getY() < y + height + padding) {
//                Log.d("Map", "stop7");
                    return false;
                }
            }
            if (y_dir < 0) {
                if (block != null && !block.isPassable() && block.getY() + cellSize > y + cellSize / 2 - padding) {
//                Log.d("Map", "stop8"  );
                    return false;
                }
            }
        }
        //Log.d("Map", row + " :: " + col + "||" + (row + diry) + " " + (col + dirx));
        return true;
    }

    private MapBlock nextCellBlock(int x, int y, MapBlock[][] blocks) {
        if ((x_dir < 0 && x / cellSize + x_dir >= 0) || (x_dir > 0 && x / cellSize + x_dir < lvl.columns)) {
            //Log.d("Character", blocks[y / cellSize][x / cellSize + x_dir].getType() + " hor");
            return blocks[y / cellSize][x / cellSize + x_dir];
        }
        if ((y_dir < 0 && y / cellSize + y_dir >= 0) || (y_dir > 0 && y / cellSize + y_dir < lvl.rows)) {
            //Log.d("Character", blocks[y / cellSize + y_dir][x / cellSize].getType() + "ver");
            return blocks[y / cellSize + y_dir][x / cellSize];
        }
        return null;
    }


    public void plantBomb(Bitmap image) {
        if (lastPlantTime == -1)
            lastPlantTime = System.nanoTime();
        int delta = (int) (System.nanoTime() - lastPlantTime) / 1000000;
        if (delta > 200 && installed <= bomb_count) {
            installed++;
            lastPlantTime = System.nanoTime();
            lvl.getBombs().add(new BombObject(lvl, image, 6, 7, id,
                    getColumn(), getRow(), cellSize, bomb_power, 5000));
        }
    }

    public void removeBomb(BombObject bomb) {
        if (installed > 0) {
            installed--;
        }
    }

    public void setX_dir(int x) {
        this.x_dir = x;
    }

    public void setY_dir(int y) {
        this.y_dir = y;
    }

    public int getX_dir() {
        return x_dir;
    }

    public int getY_dir() {
        return y_dir;
    }

    public void setSpeed(float s) {
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

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getRow() {
        //Log.d("Character", ((y + height / 2) / cellSize) + " " + (y + height / 2));
        return (y + height / 2) / cellSize;
    }

    public int getColumn() {
        //lLog.d("Character", ((x + width / 2) / cellSize) + " " + (x + width / 2));
        return (x + width / 2) / cellSize;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isDead() {
        return dead;
    }

}
