package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.graphics.Animation;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.SpriteSheet;

import java.util.Random;

import static com.mystudio.BSTPuzzle.BSTPuzzle.GRV;
import static com.mystudio.BSTPuzzle.BSTPuzzle.confettis;
import static com.mystudio.BSTPuzzle.BSTPuzzle.GAME_HEIGHT;

public class Confetti {
    public float x;
    public float y;
    public float width = 64;
    public float height = 64;
    private float frameDuration = 0.025f;
    float rotation = 0;
    Random rand = new Random();
    Texture spriteTexture = new Texture("Other/Confetti (16x16).png");
    org.mini2Dx.core.graphics.SpriteSheet SpriteSheet = new SpriteSheet(spriteTexture,96,16);
    float hsp = 0;
    float vsp = 0;
    Animation animation = new Animation();
    Confetti(float x,float y){
        this(x,y,-1);
    }
    Confetti(float x,float y, float rotation){
        if(rotation==-1){
            this.rotation = rand.nextInt(360);
        }
        else{
            this.rotation = rotation;
        }
        this.hsp = 10-rand.nextInt(20);
        this.vsp = -1*rand.nextInt(10);
        this.x = x;
        this.y = y;
        animation.setLooping(false);
        for(int i=0;i<SpriteSheet.getTotalFrames();i++){
            animation.addFrame(SpriteSheet.getSprite(i),frameDuration);
        }
    }
    public void update(float delta){
        this.animation.update(delta/2);
        if(hsp>0){
            hsp -= 0.05f*hsp;
        }
        this.rotation += Math.signum(hsp)*3;
        vsp += GRV/10;
        x+=hsp;
        y+=vsp;
        if(this.y > GAME_HEIGHT+100){
            confettis.remove(this);
        }
    }
    public void render(Graphics g){
        this.animation.setRotation(this.rotation%360);
        this.animation.draw(g,this.x,this.y);
    }
}
