package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.graphics.Animation;
import org.mini2Dx.core.graphics.SpriteSheet;

public class PlayerTexture {
    private static final String PLAYER_SPRITE_SHEET_LOCATION = "Main Characters/Ninja Frog/Idle (32x32).png";
    Pixmap pixmap200 = new Pixmap(Gdx.files.internal(PLAYER_SPRITE_SHEET_LOCATION));
    Pixmap pixmap100 = new Pixmap(32*11, 32, pixmap200.getFormat());

    private float frameDuration = 0.025f;

    Texture spriteTexture = new Texture(PLAYER_SPRITE_SHEET_LOCATION);

    SpriteSheet playerSpriteSheet = new SpriteSheet(spriteTexture,32,32);
    Animation playerAnimation = new Animation();
    public PlayerTexture(){
        for(int i=0;i<playerSpriteSheet.getTotalFrames();i++){
            playerAnimation.addFrame(playerSpriteSheet.getSprite(i),frameDuration);
        }
    }
}
