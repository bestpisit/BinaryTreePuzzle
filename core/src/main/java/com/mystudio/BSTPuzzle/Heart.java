package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;

import static com.mystudio.BSTPuzzle.BSTPuzzle.*;

public class Heart {
    public float hsp = 0;
    public float vsp = 0;
    public float x = 0;
    public float y = 0;
    PlayerTexture heart = new PlayerTexture("UserInterface/heart.png",32,32);
    public Heart(float x,float y,float hsp,float vsp){
        this.x = x;
        this.y = y;
        this.hsp = hsp;
        this.vsp = vsp;
    }
    public void update(){
        Rectangle rthis = new Rectangle(this.x-8,this.y,16,32);
        Rectangle rPlayer = new Rectangle(player.getX()-12,player.getY(),24,32);
        boolean atPlayer = rthis.intersects(rPlayer);
        if(atPlayer){
            hearts.remove(this);
            player.life++;
        }
        if(Math.abs(hsp) >= 0.5f){
            hsp -= hsp*0.05f;
        }
        else{
            hsp = 0f;
        }
        x += hsp;
        vsp += GRV;
        if(collideWall(0,this.vsp)){
            while(!collideWall(0,Math.abs(vsp)/vsp)){
                this.y += Math.abs(vsp)/vsp;
            }
            if(Math.abs(vsp)>5f){
                vsp *= -0.5;
            }
            else{
                vsp = 0;
            }
        }
        y += vsp;
    }
    boolean collideWall(float hsp,float vsp){
        Rectangle sRect = new Rectangle(this.x-16+hsp,this.y+32+vsp,32,32);
        for(int i=0;i<walls.size();i++){
            Wall w = walls.get(i);
            Rectangle wRect = new Rectangle(w.x,w.y,w.width,w.height);
            if(sRect.intersects(wRect)){
                return true;
            }
        }
        return false;
    }
    public void render(Graphics g){
        heart.playerAnimation.draw(g,this.x,this.y+32);
    }
}
