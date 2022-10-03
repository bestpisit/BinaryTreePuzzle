package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import org.mini2Dx.core.geom.Rectangle;
import org.mini2Dx.core.graphics.Graphics;

import static com.mystudio.BSTPuzzle.BSTPuzzle.GRV;
import static com.mystudio.BSTPuzzle.BSTPuzzle.walls;

public class Player {
    private PlayerTexture playerTexture;
    public float x = 0;
    public float y = 0;
    public float vsp = 0;
    public float hsp = 0;
    public float wsp = 4;
    public boolean xScale = true;
    public Player(PlayerTexture playerTexture,float x,float y) {
        this.playerTexture = playerTexture;
        playerTexture.playerAnimation.setLooping(true);
        this.x = x;
        this.y = y;
    }
    public void update(float delta) {
        playerTexture.playerAnimation.update(delta);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean jump = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
        if(jump && collideWall(0,1)){
            this.vsp = -10;
        }
        int move = (right? 1:0) - (left? 1: 0);
        hsp = move * this.wsp;
        if(hsp>0){
            xScale = false;
        }
        else if(hsp<0){
            xScale = true;
        }
        if(collideWall(this.hsp,0)){
            while(!collideWall(Math.abs(hsp)/hsp,0)){
                this.x += + Math.abs(hsp)/hsp;
            }
            hsp = 0;
        }
        x += hsp;
        vsp += GRV;
        if(collideWall(0,this.vsp)){
            while(!collideWall(0,Math.abs(vsp)/vsp)){
                this.y += + Math.abs(vsp)/vsp;
            }
            vsp = 0;
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
        g.setColor(Color.BROWN);
        //g.fillRect(this.x-12,this.y+33,24,32);
        playerTexture.playerAnimation.setFlipX(xScale);
        playerTexture.playerAnimation.draw(g,this.x-16,this.y+32);
    }
}
