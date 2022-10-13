package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Animation;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.SpriteSheet;

import java.awt.*;

import static com.mystudio.BSTPuzzle.BSTPuzzle.*;

public class Enemy {
    public float x;
    public float y;
    private float frameDuration = 0.025f;
    private float hsp = 0;
    private float vsp = 0;
    private float wsp = 1.6f;
    private float ksp = 0;
    public String type = "";

    Texture spriteTexture;
    org.mini2Dx.core.graphics.SpriteSheet SpriteSheet;
    public int width = 34;
    public int height = 44;
    public boolean isDead = false;
    PlayerTexture eRun = null;
    PlayerTexture eIdle = null;
    PlayerTexture eJump = null;
    PlayerTexture eFall = null;
    PlayerTexture eHit = null;
    PlayerTexture eDisappear = new PlayerTexture("Main Characters/Desappearing (96x96).png",96,96);
    private PlayerTexture playerTexture;
    boolean xScale = false;
    BST myBST = null;
    int life = 1;
    float strength = 1;
    Enemy(float x,float y,String t){
        this.type = t;
        SpriteCollection(this);
        this.x = x;
        this.y = y;
        playerTexture = eIdle;
        playerTexture.playerAnimation.setLooping(true);
        eRun.playerAnimation.setLooping(true);
        eIdle.playerAnimation.setLooping(true);
        eHit.playerAnimation.setLooping(true);
        eDisappear.playerAnimation.setLooping(true);
        if(t == "Chicken"){
            wsp = 1.8f;
            strength = 1f;
        }
        else if(t == "Bunny"){
            wsp = 1.7f;
            strength = 1.25f;
        }
        else if(t == "Ghost"){
            wsp = 1.9f;
            strength = 0.2f;
        }
        else if(t == "RockLG"){
            life = 2;
            strength = 2f;
        }
        else if(t == "RockSM"){
            life = 1;
            strength = 1.75f;
        }
    }
    public void update(float delta){
        if(isDead && deadC>=1){
            this.playerTexture.playerAnimation.update(delta/3);
        }
        else{
            this.playerTexture.playerAnimation.update(delta/2);
        }
        Rectangle rthis = new Rectangle(this.x-this.width/2,this.y-this.height,this.width,this.height);
        Rectangle rPlayer = new Rectangle(player.getX()-12,player.getY()+32,24,32);
        boolean atPlayer = rthis.intersects(rPlayer);
        boolean jump = false;
        float move = 0f;
        if(!atPlayer){
            move = -1*Math.abs(this.x- player.x)/(this.x- player.x);
        }
        else{
            if(!isDead){
                if(--life <= 0){
                    isDead = true;
                    myBST.isDead = true;
                }
                if(myBST != bst){
                    bstS.remove(myBST);
                }
                player.vsp -= 10*strength;
                if(this.x < player.x){
                    player.xScale = true;
                    player.ksp = 6*strength;
                }
                else{
                    player.xScale = false;
                    player.ksp = -6*strength;
                }
                player.life--;
                player.isDead = true;
            }
        }
        hsp = move * this.wsp;
        hsp += ksp;
        if(collideWall(this.hsp,0)){
            while(!collideWall(Math.abs(hsp)/hsp,0)){
                this.x += Math.abs(hsp)/hsp;
            }
            hsp = 0;
            if(Math.abs(ksp) > 5f){
                ksp *= -1;
            }
        }
        if(Math.abs(ksp) > 3f){
            ksp -= 0.05f*ksp;
        }
        else{
            ksp = 0;
        }
        if(isDead){
            hsp = 0;
            jump = false;
        }
        x += hsp;
        if(collideWall(0,1) && this.type=="Bunny"){
            this.vsp = -20;
            this.ksp = 1;
        }
        vsp += GRV;
        if(this.vsp < 0){
            this.playerTexture = eJump;
        }
        else if(this.vsp > 0){
            this.playerTexture = eFall;
        }
        if(hsp > 0){
            this.xScale = false;
            this.playerTexture = eRun;
        }
        else if(hsp<0){
            this.xScale = true;
            this.playerTexture = eRun;
        }
        else{
            this.playerTexture = eIdle;
        }
        if(collideWall(0,this.vsp)){
            while(!collideWall(0,Math.abs(vsp)/vsp)){
                this.y += Math.abs(vsp)/vsp;
            }
            vsp = 0;
        }
        this.y += vsp;
    }
    boolean collideWall(float hsp,float vsp){
        Rectangle sRect = new Rectangle(this.x-width/2+hsp,this.y-height+vsp,width,height);
        for(int i=0;i<walls.size();i++){
            Wall w = walls.get(i);
            Rectangle wRect = new Rectangle(w.x,w.y,w.width,w.height);
            if(sRect.intersects(wRect)){
                return true;
            }
        }
        return false;
    }
    int deadTick = 0;
    int deadC = 0;
    public void render(Graphics g){
        //g.drawCircle(this.x,this.y,5);
        //g.drawRect(this.x-this.width/2,this.y-this.height,this.width,this.height);
        if(isDead){
            if(deadTick<20){
                deadTick++;
                this.playerTexture = eHit;
            }
            else if(deadTick<40){
                deadTick++;
                this.playerTexture = eIdle;
            }
            else{
                if(deadC<1){
                    deadC++;
                    deadTick = 0;
                }
                else{
                    this.width=96;
                    this.height=96-32;
                    this.playerTexture = eDisappear;
                    if(this.playerTexture.playerAnimation.getCurrentFrameIndex()==this.playerTexture.playerAnimation.getNumberOfFrames()-1){
                        deadC = 0;
                        enemies.remove(this);
                    }
                }
            }
            this.playerTexture.playerAnimation.setFlipX(!xScale);
            this.playerTexture.playerAnimation.draw(g,this.x-width/2,this.y-height+1);
        }
        else{
            this.playerTexture.playerAnimation.setFlipX(!xScale);
            this.playerTexture.playerAnimation.draw(g,this.x-width/2,this.y-height+1);
        }
    }
}
