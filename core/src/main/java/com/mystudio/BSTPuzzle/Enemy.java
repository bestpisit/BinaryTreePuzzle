package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Animation;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.SpriteSheet;

import static com.mystudio.BSTPuzzle.BSTPuzzle.*;

public class Enemy {
    public float x;
    public float y;
    private float frameDuration = 0.025f;
    private float hsp = 0;
    private float vsp = 0;
    private float wsp = 0.5f;
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
    }
    public void update(float delta){
        if(isDead && deadC>=3){
            this.playerTexture.playerAnimation.update(delta/5);
        }
        else{
            this.playerTexture.playerAnimation.update(delta/2);
        }
        boolean atPlayer = (Math.abs(this.x- player.x)<16 && Math.abs(this.y- player.y)<16);
        boolean jump = false;
        float move = 0f;
        if(!atPlayer){
            move = -1*Math.abs(this.x- player.x)/(this.x- player.x);
        }
        else{
            if(!isDead){
                isDead = true;
                myBST.isDead = true;
                if(myBST != bst){
                    bstS.remove(myBST);
                }
                if(this.x < player.x){
                    player.xScale = true;
                }
                else{
                    player.xScale = false;
                }
                player.life--;
                player.isDead = true;
            }
        }
        hsp = move * this.wsp;
        if(collideWall(this.hsp,0)){
            while(!collideWall(Math.abs(hsp)/hsp,0)){
                this.x += + Math.abs(hsp)/hsp;
            }
            hsp = 0;
            if(!atPlayer){
                jump = true;
            }
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
        if(isDead){
            hsp = 0;
            jump = false;
        }
        hsp=0;
        x += hsp;
        if(jump && collideWall(0,1)){
            this.vsp = -12;
        }
        vsp += GRV;
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
                if(deadC<3){
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
            this.playerTexture.playerAnimation.draw(g,this.x-width/2,this.y-height);
        }
        else{
            this.playerTexture.playerAnimation.setFlipX(!xScale);
            this.playerTexture.playerAnimation.draw(g,this.x-width/2,this.y-height);
        }
    }
}
