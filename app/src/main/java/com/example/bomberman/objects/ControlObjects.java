package com.example.bomberman.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.bomberman.surfaces.GameSurface;

public class ControlObjects extends GameObject{
    private GameSurface surface;

    public ControlObjects(GameSurface surface, Bitmap image) {
        super(image, 0, surface.getHeight()-image.getHeight());
        this.surface = surface;
    }

    public void draw(Canvas canvas) {

    }
}
