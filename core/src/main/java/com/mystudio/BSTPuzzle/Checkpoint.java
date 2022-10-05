package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.graphics.Animation;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.SpriteSheet;

public class Checkpoint {
    public float x;
    public float y;
    public float width = 32;
    public float height = 32;
    private float frameDuration = 0.025f;

    Texture spriteTexture = new Texture("Items/Checkpoints/Start/Start (Moving) (64x64).png");

    SpriteSheet SpriteSheet = new SpriteSheet(spriteTexture,64,64);;
    Animation animation = new Animation();
    Checkpoint(float x,float y){
        this.x = x;
        this.y = y;
        animation.setLooping(true);
        for(int i=0;i<SpriteSheet.getTotalFrames();i++){
            animation.addFrame(SpriteSheet.getSprite(i),frameDuration);
        }
    }
    public void update(float delta){
        this.animation.update(delta/2);
    }
    public void render(Graphics g){
        g.setColor(Color.BLACK);
        g.setLineHeight(1);
        //g.drawRect(this.x,this.y,this.width,this.height);
        this.animation.draw(g,this.x,this.y-64);
    }
}
