package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.graphics.viewport.Viewport;

public class BSTPuzzle extends BasicGame {
	public static final String GAME_IDENTIFIER = "com.mystudio.BSTPuzzle";
    public static final float GAME_WIDTH = 640;
    public static final float GAME_HEIGHT = 480;
    Viewport fitViewport;

	private Texture texture;
    private Texture Background;

    public BST bst = new BST(5);

    float xPos = 0f;
    float yPos = 0f;
    Player player;
    PlayerTexture playerTexture;
	@Override
    public void initialise() {
        texture = new Texture("mini2Dx.png");
        Background = new Texture("Background/Green.png");
        fitViewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
        playerTexture = new PlayerTexture();
        player = new Player(playerTexture);
    }
    
    @Override
    public void update(float delta) {
        bst.update();
        if(xPos<64){
            xPos+=0.5f;
            yPos+=0.5f;
        }
        else{
            xPos=0;
            yPos=0;
        }
        player.update(delta/2);
    }
    
    @Override
    public void interpolate(float alpha) {
    
    }
    
    @Override
    public void render(Graphics g) {
        fitViewport.apply(g);
        g.setColor(Color.BLUE);
        g.fillRect(0,0,GAME_WIDTH,GAME_HEIGHT);
        for(int j=-1;j<15;j++){
            for(int i=-1;i<15;i++) {
                g.drawTexture(Background, 64*i+xPos, 64*j+yPos);
            }
        }
        bst.render(g);
        player.render(g);
        //g.drawTexture(texture, 0f, 0f);
    }
}
