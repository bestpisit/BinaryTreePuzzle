package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import org.mini2Dx.core.geom.LineSegment;
import org.mini2Dx.core.geom.Point;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;

import static com.mystudio.BSTPuzzle.BSTPuzzle.*;

public class Player {
    private PlayerTexture playerTexture;
    public float x = 0;
    public float y = 0;
    public float vsp = 0;
    public float hsp = 0;
    public float wsp = 4;
    private float destX = GAME_WIDTH;
    private float destY = 300;
    private float checkW = 5;
    private float checkH = 32;
    public boolean xScale = true;
    PlayerTexture playerRun = new PlayerTexture("Main Characters/Ninja Frog/Run (32x32).png");
    PlayerTexture playerIdle = new PlayerTexture("Main Characters/Ninja Frog/Idle (32x32).png");
    PlayerTexture playerJump = new PlayerTexture("Main Characters/Ninja Frog/Jump (32x32).png");
    PlayerTexture playerFall = new PlayerTexture("Main Characters/Ninja Frog/Fall (32x32).png");
    public Player(PlayerTexture playerTexture,float x,float y) {
        this.playerTexture = playerTexture;
        playerTexture.playerAnimation.setLooping(true);
        playerRun.playerAnimation.setLooping(true);
        playerIdle.playerAnimation.setLooping(true);
        this.x = x;
        this.y = y;
    }
    public void update(float delta) {
        this.playerTexture.playerAnimation.update(delta);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean jump = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
        float move = (right? 1:0) - (left? 1: 0);
        //destination calculation
        boolean atDestinationX = (Math.abs(this.x-this.destX)<this.checkW);
        //boolean atDestinationX = new Rectangle(this.destX-16,this.y+32+1,5,32).intersects(this.x-12,this.y+32+1,24,32);
        if(!atDestinationX){
            float f = this.destX-this.x;
            move = Math.signum(f);
        }
        else{
            move = 0;
        }
        hsp = move * this.wsp;
        if(collideWall(this.hsp,0)){
            while(!collideWall(Math.abs(hsp)/hsp,0)){
                this.x += + Math.abs(hsp)/hsp;
            }
            hsp = 0;
            //check dest
            if(!atDestinationX){
                jump = true;
            }
        }
        x += hsp;
        if(hsp>0){
            xScale = false;
            this.playerTexture = playerRun;
        }
        else if(hsp<0){
            xScale = true;
            this.playerTexture = playerRun;
        }
        else{
            this.playerTexture = playerIdle;
        }
        if(atDestinationX && (Math.abs(this.y+32+1-this.destY)>this.checkH)){
            jump = true;
        }
        if(jump && collideWall(0,1)){
            this.vsp = -10;
        }
        vsp += GRV;
        if(collideWall(0,this.vsp)){
            while(!collideWall(0,Math.abs(vsp)/vsp)){
                this.y += + Math.abs(vsp)/vsp;
            }
            vsp = 0;
        }
        if(vsp>0){
            this.playerTexture = playerJump;
        }
        else if(vsp<0){
            this.playerTexture = playerFall;
        }
        this.y += vsp;
    }
    boolean collideWall(float hsp,float vsp){
        Rectangle sRect = new Rectangle(this.x-12+hsp,this.y+32+1+vsp,24,32);
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
        g.setColor(Color.BROWN);
        g.drawCircle(this.destX,this.destY,10);
        //g.fillRect(this.destX-16,this.y+32+1,checkW,32);
        playerTexture.playerAnimation.setFlipX(xScale);
        playerTexture.playerAnimation.draw(g,this.x-16,this.y+32);
    }
}
