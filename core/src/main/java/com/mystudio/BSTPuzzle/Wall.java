package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.graphics.Color;
import org.mini2Dx.core.graphics.Graphics;

public class Wall {
    public float x;
    public float y;
    public float width = 32;
    public float height = 32;
    Wall(float x,float y){
        this.x = x;
        this.y = y;
    }
    public void render(Graphics g){
        g.setColor(Color.BLACK);
        g.drawRect(this.x,this.y,this.width,this.height);
    }
}
