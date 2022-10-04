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

import java.util.ArrayList;

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
    public static ArrayList<Wall> walls = new ArrayList<Wall>();
    public static final float GRV = 0.93f;
	@Override
    public void initialise() {
        texture = new Texture("mini2Dx.png");
        Background = new Texture("Background/Green.png");
        fitViewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
        playerTexture = new PlayerTexture("Main Characters/Ninja Frog/Idle (32x32).png");
        player = new Player(playerTexture,100,0);
        //create wall
//        for(int i=0;i<GAME_WIDTH/64;i++){
//            Wall w = new Wall(i*32,GAME_HEIGHT-16*(i+1));
//            walls.add(w);
//        }
        for(int i=0;i<Math.round(GAME_WIDTH/64);i++){
            Wall w = new Wall(GAME_WIDTH/2+i*32,GAME_HEIGHT-16*(i+1));
            walls.add(w);
        }
        //create border
        for(int i=0;i<GAME_WIDTH/32;i++){
            Wall w = new Wall(i*32,-33);
            Wall w1 = new Wall(i*32,GAME_HEIGHT);
            walls.add(w);
            walls.add(w1);
        }
        for(int j=0;j<GAME_HEIGHT/32;j++){
            Wall w = new Wall(-33,j*32);
            Wall w1 = new Wall(GAME_WIDTH,j*32);
            walls.add(w);
            walls.add(w1);
        }
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
        for(int i=0;i<walls.size();i++){
            walls.get(i).render(g);
        }
        //g.drawTexture(texture, 0f, 0f);
    }
}
