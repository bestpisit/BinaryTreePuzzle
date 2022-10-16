package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;

import java.util.Random;

import static com.mystudio.BSTPuzzle.BSTPuzzle.*;
import static com.mystudio.BSTPuzzle.BSTPuzzle.enemies;

public class Player {
    public PlayerTexture playerTexture;
    public float x = 0;
    public float y = 0;
    public float vsp = 0;
    public float hsp = 0;
    public float wsp = 1.5f;
    private float destX = GAME_WIDTH;
    private float destY = 300;
    private float checkW = 5;
    private float checkH = 32;
    public boolean xScale = true;
    public boolean isDead = false;
    boolean isAppear = true;
    public int life = 3;
    public float ksp = 0;
    PlayerTexture playerRun = new PlayerTexture("Main Characters/Ninja Frog/Run (32x32).png",32,32);
    PlayerTexture playerIdle = new PlayerTexture("Main Characters/Ninja Frog/Idle (32x32).png",32,32);
    PlayerTexture playerJump = new PlayerTexture("Main Characters/Ninja Frog/Jump (32x32).png",32,32);
    PlayerTexture playerFall = new PlayerTexture("Main Characters/Ninja Frog/Fall (32x32).png",32,32);
    PlayerTexture playerHit = new PlayerTexture("Main Characters/Ninja Frog/Hit (32x32).png",32,32);
    PlayerTexture playerAppear = new PlayerTexture("Main Characters/Appearing (96x96).png",96,96);
    Texture heart;
    public int killCount = 0;
    public Player(PlayerTexture playerTexture,float x,float y) {
        this.playerTexture = playerTexture;
        playerTexture.playerAnimation.setLooping(true);
        playerRun.playerAnimation.setLooping(true);
        playerIdle.playerAnimation.setLooping(true);
        playerHit.playerAnimation.setLooping(true);
        heart = new Texture("UserInterface/heart.png");
        this.x = x;
        this.y = y;
    }
    Random rand = new Random();
    Enemy nearestEnemy(){
        float nearest = -1;
        Enemy ee = null;
        for(int i=0;i<enemies.size();i++){
            Enemy e = enemies.get(i);
            if(!e.isDead){
                float des = (float) Math.sqrt(Math.pow(this.x-e.x,2)+Math.pow(this.y-e.y,2));
//                if(des+hsp>100 && des+hsp < 120){
                    if(nearest==-1){
                        nearest = des;
                        ee = e;
                    }
                    else if(nearest > des){
                        nearest = des;
                        ee = e;
                    }
                //}
            }
        }
        return ee;
    }
    public void update(float delta) {
        this.playerTexture.playerAnimation.update(delta*wsp/3);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean jump = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
        float move;
        move = (right? 1:0) - (left? 1: 0);
        if(move == 0 && GameState==1){
            move = 1;
        }
        //destination calculation

        boolean atDestinationX = (Math.abs(this.x-this.destX)<this.checkW);
        Enemy nearest = nearestEnemy();
        boolean getReckt = false;
//        if(move == 0){
//            if(nearest != null){
//                getReckt = (Math.abs(this.x-nearest.x)<10);
//                if(!getReckt){
//                    float f = this.nearestEnemy().x-this.x;
//                    move = -1*Math.signum(f);
//                }
//            }
//            else{
////                if(!(Math.abs(this.x-goal.x-hsp-21)<5)){
////                    move = -1*Math.signum((float) this.x-goal.x-hsp-21);
////                }
//            }
//        }
        hsp = move * this.wsp;
        if(life<=0 || isDead){
            this.hsp = 0;
        }
        hsp += ksp;
            if(hsp > 0){
                for(int i=0;i<walls.size();i++){
                    if(walls.get(i).type == "floor"){
                        walls.get(i).xPos -= hsp;
                    }
                    else{
                        walls.get(i).x -= hsp;
                    }
                }
                for(int i=0;i<enemies.size();i++){
                    enemies.get(i).x-= hsp;
                }
                goal.x -= hsp;
                player.x -= hsp;
            }
            else{
                for(int i=0;i<walls.size();i++){
                    if(walls.get(i).type == "floor"){
                        walls.get(i).xPos -= hsp;
                    }
                    else{
                        walls.get(i).x -= hsp;
                    }
                }
                for(int i=0;i<enemies.size();i++){
                    enemies.get(i).x-= hsp;
                }
                goal.x -= hsp;
                player.x -= hsp;
            }
        float preHsp = hsp;
        boolean collide = false;
        if(collideWall(this.hsp,0)){
            collide = true;
            while(!collideWall(Math.abs(hsp)/hsp,0)){
                this.x += Math.abs(hsp)/hsp;
            }
            hsp = 0;
            if(Math.abs(ksp) > 5f){
                ksp *= -1;
            }
            //check dest
            if(!getReckt){
                jump = true;
            }
        }
        if(Math.abs(ksp) > 3f){
            ksp -= 0.05f*ksp;
        }
        else{
            ksp = 0;
        }
//        if(atDestinationX && (Math.abs(this.y+32+1-this.destY)>this.checkH)){
//            jump = true;
//        }
        if(jump && collideWall(0,1) && (life>0) && !isDead){
            this.vsp -= 10;
        }
        vsp += GRV;
        if(collideWall(0,this.vsp)){
            while(!collideWall(0,Math.abs(vsp)/vsp)){
                this.y += + Math.abs(vsp)/vsp;
            }
            vsp = 0;
        }
        if(hsp>0){
            xScale = false;
            this.playerTexture = playerRun;
        }
        else if(hsp<0){
            xScale = true;
            this.playerTexture = playerRun;
        }
        else{
            if(!isDead){
                this.playerTexture = playerIdle;
            }
        }
        if(vsp>0){
            this.playerTexture = playerJump;
        }
        else if(vsp<0){
            this.playerTexture = playerFall;
        }
        if(vsp != 0){
            if(ksp != 0){
                if(xScale == true){
                    pRotation -= Math.signum(hsp)*hsp*2;
                }
                else{
                    pRotation += Math.signum(hsp)*hsp*2;
                }
            }
            else{
                pRotation = 0;
            }
        }
        else{
            pRotation = 0;
        }
        if(isAppear){
            hsp=0;
            vsp=0;
        }
        x += hsp;
        this.y += vsp;
    }
    float pRotation = 0;
    boolean collideWall(float hsp,float vsp){
        Rectangle sRect = new Rectangle(this.x-12+hsp,this.y+32+vsp,24,32);
        for(int i=0;i<walls.size();i++){
            Wall w = walls.get(i);
            Rectangle wRect = new Rectangle(w.x,w.y,w.width,w.height);
            if(sRect.intersects(wRect)){
                return true;
            }
        }
        return false;
    }
    float getX(){
        return this.x;
    }
    float getY(){
        return this.y;
    }
    public void render(Graphics g){
        float rX = GAME_WIDTH/g.getWindowWidth();
        float rY = GAME_HEIGHT/g.getWindowHeight();
        float tX = g.getWindowWidth()-g.getWindowHeight()*GAME_WIDTH/GAME_HEIGHT;
        if(tX >= 0){
            rX = GAME_WIDTH/(g.getWindowWidth()-tX);
            tX = tX/(4);
        }
        else{
            tX = 0;
        }
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            this.destX = Gdx.input.getX()*rX-tX;
            this.destY = Gdx.input.getY()*rY;
        }
        //g.setColor(Color.BROWN);
        //g.drawCircle(this.destX,this.destY,10);
        float w = 0;
        for(int h=0;h<this.life;h++){
            g.drawTexture(heart,10+w,GAME_HEIGHT-42);
            w+= heart.getWidth();
        }
        //g.drawRect(this.x-12,this.y+32,24,32);
        playerTexture.playerAnimation.setFlipX(xScale);
        if(life<=0){
            playerTexture.playerAnimation.setRotation(90);
            playerTexture.playerAnimation.setLooping(false);
            playerTexture = playerIdle;
            playerTexture.playerAnimation.draw(g,this.x-16,this.y+32+6);
        }
        else{
            if(isDead){
                playerTexture = playerHit;
                if(playerTexture.playerAnimation.getCurrentFrameIndex()>=playerTexture.playerAnimation.getNumberOfFrames()-1){
                    isDead = false;
                }
                playerTexture.playerAnimation.setRotation(pRotation);
                playerTexture.playerAnimation.draw(g,this.x-16,this.y+32+1);
            }
            else if(isAppear){
                playerTexture = playerAppear;
                if(playerTexture.playerAnimation.getCurrentFrameIndex()==playerTexture.playerAnimation.getNumberOfFrames()-1){
                    isAppear = false;
                    playerTexture = playerIdle;
                }
                else{
                    playerTexture.playerAnimation.setRotation(pRotation);
                    playerTexture.playerAnimation.draw(g,this.x-48,this.y-32);
                }
            }
            else{
                playerTexture.playerAnimation.setRotation(pRotation);
                playerTexture.playerAnimation.setLooping(true);
                playerTexture.playerAnimation.draw(g,this.x-16,this.y+32+1);
            }
        }
    }
}
