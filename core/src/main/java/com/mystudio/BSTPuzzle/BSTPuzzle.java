package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.graphics.viewport.Viewport;

import java.util.ArrayList;
import java.util.Random;

public class BSTPuzzle extends BasicGame {
	public static final String GAME_IDENTIFIER = "com.mystudio.BSTPuzzle";
    public static final float GAME_WIDTH = 640;
    public static final float GAME_HEIGHT = 480;
    Viewport fitViewport;

	private Texture texture;
    private Texture Background;

    public static BST bst = null;

    float xPos = 0f;
    float yPos = 0f;
    public static Player player;
    PlayerTexture playerTexture;
    public static ArrayList<Wall> walls = new ArrayList<Wall>();
    public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    public static ArrayList<Confetti> confettis = new ArrayList<Confetti>();
    public static ArrayList<BST> bstS = new ArrayList<BST>();
    public static final float GRV = 0.93f;
    Checkpoint startPoint;
    public static Goal goal;
    public static int Score = 0;
    public static int GameState = 0;
	@Override
    public void initialise() {
        texture = new Texture("mini2Dx.png");
        Background = new Texture("Background/Green.png");
        fitViewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
        playerTexture = new PlayerTexture("Main Characters/Ninja Frog/Idle (32x32).png",32,32);
        player = new Player(playerTexture,GAME_WIDTH/2,GAME_HEIGHT/2);
        goal = new Goal(GAME_WIDTH/2-32,GAME_HEIGHT-32-15);
        //create wall
//        for(int i=0;i<GAME_WIDTH/64;i++){
//            Wall w = new Wall(i*32,GAME_HEIGHT-16*(i+1));
//            walls.add(w);
//        }
        for(int i=0;i<Math.round(GAME_WIDTH/32);i++){
            Wall w = new Wall(i*32,GAME_HEIGHT-32);
            walls.add(w);
        }
        //create border
        for(int i=0;i<GAME_WIDTH/32;i++){
            //Wall w = new Wall(i*32,-33);
            Wall w1 = new Wall(i*32,GAME_HEIGHT);
            //walls.add(w);
            walls.add(w1);
        }
        for(int j=0;j<GAME_HEIGHT/32;j++){
            Wall w = new Wall(-33,j*32);
            Wall w1 = new Wall(GAME_WIDTH,j*32);
            walls.add(w);
            walls.add(w1);
        }
        walls.add(new Wall(GAME_WIDTH/2,GAME_HEIGHT-32-16));
        walls.add(new Wall(GAME_WIDTH/2-32,GAME_HEIGHT-32-16));
        walls.add(new Wall(GAME_WIDTH/2-32-32,GAME_HEIGHT-32-16));
//        Enemy ee = new Enemy(new Random().nextInt((int) GAME_WIDTH-128)+32,0,"RockSM");
//        enemies.add(ee);
//        BST nB = new BST(5,ee,"MINHEAP");
//        bstS.add(nB);
    }
    String[] TreeType = {"BT","BST","MINHEAP","MAXHEAP"};
    String[] EnemyType = {"Bunny","RockSM"};
    @Override
    public void update(float delta) {
        if(GameState == 0){
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                GameState = 1;
            }
        }
        else if(GameState == 1){
            if(player.life>0){
                Score++;
            }
            else{
                GameState = -1;
            }
            if(bst != null){
                bst.update();
            }
            else{
                if(bstS.size()>0){
                    BST getB = bstS.get(new Random().nextInt(bstS.size()));
                    bst = getB;
                }
                else{
                    //create awa
                    int number = 1 + new Random().nextInt(Math.round(Score/(20*50))+1);
                    for(int i=0;i<number;i++){
                        String tree = TreeType[new Random().nextInt(TreeType.length)];
                        String eType = EnemyType[new Random().nextInt(EnemyType.length)];
                        boolean location = new Random().nextBoolean();
                        float posL = 32;
                        if(location){
                            posL = GAME_WIDTH-64;
                        }
                        if(player.x < 100){
                            posL = GAME_WIDTH-64;
                        }
                        if(player.x > GAME_WIDTH-100){
                            posL = 32;
                        }
                        Random rand = new Random();
                        Enemy ee = new Enemy(posL+ rand.nextInt(64+32*new Random().nextInt(Math.round(Score/(20*50))+1)),-1*rand.nextInt(200),eType);
                        enemies.add(ee);
                        BST nB = new BST(3+rand.nextInt(player.killCount+1),ee,tree);
                        bstS.add(nB);
                    }
                }
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)){
                if(bstS.size()>1){
                    int ind = bstS.indexOf(bst);
                    if(ind!=-1){
                        if(ind>=bstS.size()-1){
                            bst = bstS.get(0);
                        }
                        else{
                            bst = bstS.get(ind+1);
                        }
                    }
                }
            }
        }
        else if(GameState == -1){
            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                Score = 0;
                GameState = 1;
                player = new Player(playerTexture,GAME_WIDTH/2,GAME_HEIGHT/2);
            }
        }
        if(xPos<64){
            xPos+=0.5f;
            yPos+=0.5f;
        }
        else{
            xPos=0;
            yPos=0;
        }
        player.update(delta);
        goal.update(delta);
        for(int i=0;i<confettis.size();i++){
            confettis.get(i).update(delta);
        }
        for(int i=0;i<enemies.size();i++){
            enemies.get(i).update(delta);
        }
    }
    
    @Override
    public void interpolate(float alpha) {
    
    }
    @Override
    public void render(Graphics g) {
        fitViewport.apply(g);
        for(int j=-1;j<15;j++){
            for(int i=-1;i<15;i++) {
                g.drawTexture(Background, 64*i+xPos, 64*j+yPos);
            }
        }
        if(GameState==0){
            g.drawString("Press Space To Play",GAME_WIDTH/2,GAME_HEIGHT/2);
        }
        else if(GameState==1){
            g.drawString(String.valueOf((int)Score/20),0,64);
            if(bst != null){
                bst.render(g);
            }
        }
        else if(GameState==-1){
            g.drawString("Press Space To Play Again",GAME_WIDTH/2,GAME_HEIGHT/2);
            g.drawString("Your Score "+String.valueOf((int)Score/20),GAME_WIDTH/2,GAME_HEIGHT/2-32);
        }
        for(int i=0;i<walls.size();i++){
            walls.get(i).render(g);
        }
        goal.render(g);
        for(int i=0;i<enemies.size();i++){
            enemies.get(i).render(g);
        }
        player.render(g);
        for(int i=0;i<confettis.size();i++){
            confettis.get(i).render(g);
        }
    }
    public static void SpriteCollection(Enemy e){
        if(e.type == "Bunny"){
            e.eRun = new PlayerTexture("Enemies/Bunny/Run (34x44).png",34,44);
            e.eIdle = new PlayerTexture("Enemies/Bunny/Idle (34x44).png",34,44);
            e.eJump = new PlayerTexture("Enemies/Bunny/Jump.png",34,44);
            e.eFall = new PlayerTexture("Enemies/Bunny/Fall.png",34,44);
            e.eHit = new PlayerTexture("Enemies/Bunny/Hit (34x44).png",34,44);
            e.width = 34;
            e.height = 44;
        }
        else if(e.type == "RockSM"){
            e.eRun = new PlayerTexture("Enemies/Rocks/Rock2_Run (32x28).png",32,28);
            e.eIdle = new PlayerTexture("Enemies/Rocks/Rock2_Idle (32x28).png",32,28);
            e.eJump = new PlayerTexture("Enemies/Rocks/Rock2_Idle (32x28).png",32,28);
            e.eFall = new PlayerTexture("Enemies/Rocks/Rock2_Idle (32x28).png",32,28);
            e.eHit = new PlayerTexture("Enemies/Rocks/Rock2_Hit (32x28).png",32,28);
            e.width = 32;
            e.height = 28;
        }
    }
}
