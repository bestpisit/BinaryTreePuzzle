package com.mystudio.BSTPuzzle;

import org.mini2Dx.core.graphics.Graphics;

public class Player {
    private float playerTextureHeight, playerTextureWidth;
    private PlayerTexture playerTexture;
    public Player(PlayerTexture playerTexture) {
        this.playerTexture = playerTexture;
        playerTextureHeight = playerTexture.playerSpriteSheet.getSprite(0).getHeight()*3;
        playerTextureWidth = playerTexture.playerSpriteSheet.getSprite(0).getWidth()*3;
        playerTexture.playerAnimation.setLooping(true);
    }
    public void update(float delta) {
        playerTexture.playerAnimation.update(delta);
    }
    public void render(Graphics g){
        playerTexture.playerAnimation.setFlipX(true);
        playerTexture.playerAnimation.draw(g,100,100);
    }
}
