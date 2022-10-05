package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.graphics.Animation;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.SpriteSheet;

public class Wall {
    public float x;
    public float y;
    public float width = 32;
    public float height = 32;
    private float frameDuration = 0.025f;

    Texture spriteTexture = new Texture("Terrain/Terrain (16x16).png");

    SpriteSheet wallSpriteSheet = new SpriteSheet(spriteTexture,16,32);;
    Animation wallAnimation = new Animation();
    Wall(float x,float y){
        this.x = x;
        this.y = y;
        wallAnimation.addFrame(wallSpriteSheet.getSprite(7),frameDuration);
    }
    public void render(Graphics g){
        g.setColor(Color.BLACK);
        g.setLineHeight(1);
        //g.drawRect(this.x,this.y,this.width,this.height);
        this.wallAnimation.draw(g,this.x,this.y);
        this.wallAnimation.draw(g,this.x+16,this.y);
    }
}
