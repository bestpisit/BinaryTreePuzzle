package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.graphics.Animation;
import org.mini2Dx.core.graphics.SpriteSheet;

public class PlayerTexture {
    private static final String PLAYER_SPRITE_SHEET_LOCATION = "Main Characters/Ninja Frog/Idle (32x32).png";
    private float frameDuration = 0.025f;

    Texture spriteTexture;

    SpriteSheet playerSpriteSheet;
    Animation playerAnimation = new Animation();
    public PlayerTexture(String path,int w,int h){
        spriteTexture = new Texture(path);
        playerSpriteSheet = new SpriteSheet(spriteTexture,w,h);
        for(int i=0;i<playerSpriteSheet.getTotalFrames();i++){
            playerAnimation.addFrame(playerSpriteSheet.getSprite(i),frameDuration);
        }
    }
}
