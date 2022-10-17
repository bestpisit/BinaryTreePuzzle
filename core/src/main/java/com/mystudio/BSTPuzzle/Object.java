package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.graphics.Graphics;

public class Object implements Physics {
    public float x;
    public float y;
    Texture nullTexture = new Texture("Main Characters/Appearing (96x96).png");
    void update() {
        updatePhysics();
    }
    void render(Graphics g){
        g.drawTexture(nullTexture,this.x,this.y);
    }

    @Override
    public void updatePhysics() {

    }

    @Override
    public boolean collideWall(float hsp, float vsp) {
        return false;
    }
}
