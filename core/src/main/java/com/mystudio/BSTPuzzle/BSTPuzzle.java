package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Gdx.*;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.viewport.FitViewport;
import org.mini2Dx.core.graphics.viewport.Viewport;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
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
    public static int GameState = 9;
    PlayerTexture gameIntro = null;
    PlayerTexture logoMain;

    Music introSound;
    Music mainSound;
    Music endSound;
    ArrayList<Music> playSounds;
    Music playSound;
    ArrayList<Music> alreadySounds;
    public void chooseSound(){
        if(playSounds.size()>0){
            playSound = playSounds.get(new Random(new Date().getTime()).nextInt(playSounds.size()));
            playSounds.remove(playSound);
            alreadySounds.add(playSound);
            System.out.println(alreadySounds);
            System.out.println(playSounds);
        }
        else{
            for(int i=0;i<alreadySounds.size();i++){
                playSounds.add(alreadySounds.get(i));
            }
            alreadySounds.clear();
            chooseSound();
        }
    }
	@Override
    public void initialise() {
        texture = new Texture("mini2Dx.png");
        Background = new Texture("Background/Green.png");
        fitViewport = new FitViewport(GAME_WIDTH, GAME_HEIGHT);
        playerTexture = new PlayerTexture("Main Characters/Ninja Frog/Idle (32x32).png",32,32);
        player = new Player(playerTexture,GAME_WIDTH/2,GAME_HEIGHT/2);
        goal = new Goal(GAME_WIDTH/2-32,GAME_HEIGHT-32-15);
        gameIntro = new PlayerTexture("Intro/BST.png",500,500);
        logoMain = new PlayerTexture("Intro/LogoPNG.png",500,500);
        introSound = Gdx.audio.newMusic(Gdx.files.internal("Intro/IntroMusic.mp3"));
        mainSound = Gdx.audio.newMusic(Gdx.files.internal("Intro/main.mp3"));
        endSound = Gdx.audio.newMusic(Gdx.files.internal("Intro/end.mp3"));
        alreadySounds = new ArrayList<Music>();
        playSounds = new ArrayList<Music>();
        playSounds.add(Gdx.audio.newMusic(Gdx.files.internal("Intro/play1.mp3")));
        playSounds.add(Gdx.audio.newMusic(Gdx.files.internal("Intro/play2.mp3")));
        playSounds.add(Gdx.audio.newMusic(Gdx.files.internal("Intro/play3.mp3")));
        playSounds.add(Gdx.audio.newMusic(Gdx.files.internal("Intro/play4.mp3")));
        chooseSound();
        introSound.setVolume(2f);
        introSound.play();
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
    int introTick = -50;
    int clickTarget = 0;
    @Override
    public void update(float delta) {
        if(GameState == 9){
            gameIntro.playerAnimation.update(delta*0.8f);
            if(gameIntro.playerAnimation.getCurrentFrameIndex()>=gameIntro.playerAnimation.getNumberOfFrames()-1){
                if(introTick<0){
                    introTick++;
                }
                else {
                    introTick = 0;
                    GameState = 8;
                }
            }
        }
        else {
            if(GameState == 8){
                if(introTick < GAME_HEIGHT/10){
                    introTick++;
                    introY-=10;
                }
                else{
                    introTick = 0;
                    GameState = 0;
                    mainSound.play();
                }
            }
            if (GameState == 0) {
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    if(clickTarget == 0){
                        clickTarget = 1;
                    }
                }
                else{
                    if(clickTarget == 1){
                        GameState = 1;
                        mainSound.stop();
                        clickTarget = 0;
                    }
                    else{
                        clickTarget = 0;
                    }
                }
            } else if (GameState == 1) {
                if(mainSound.isPlaying()){
                    mainSound.stop();
                }
                if(endSound.isPlaying()){
                    endSound.stop();
                }
                if(!playSound.isPlaying()){
                    playSound.play();
                }
                if (player.life > 0) {
                    Score++;
                } else {
                    GameState = -1;
                }
                if (bst != null) {
                    bst.update();
                } else {
                    if (bstS.size() > 0) {
                        BST getB = bstS.get(new Random().nextInt(bstS.size()));
                        bst = getB;
                    } else {
                        //create awa
                        int number = 1 + new Random().nextInt(Math.round(Score / (20 * 50)) + 1);
                        for (int i = 0; i < number; i++) {
                            String tree = TreeType[new Random().nextInt(TreeType.length)];
                            String eType = EnemyType[new Random().nextInt(EnemyType.length)];
                            boolean location = new Random().nextBoolean();
                            float posL = 32;
                            if (location) {
                                posL = GAME_WIDTH - 64;
                            }
                            if (player.x < 100) {
                                posL = GAME_WIDTH - 64;
                            }
                            if (player.x > GAME_WIDTH - 100) {
                                posL = 32;
                            }
                            Random rand = new Random();
                            Enemy ee = new Enemy(posL + rand.nextInt(64 + 32 * new Random().nextInt(Math.round(Score / (20 * 50)) + 1)), -1 * rand.nextInt(200), eType);
                            enemies.add(ee);
                            BST nB = new BST(3 + rand.nextInt(player.killCount + 1), ee, tree);
                            bstS.add(nB);
                        }
                    }
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                    if (bstS.size() > 1) {
                        int ind = bstS.indexOf(bst);
                        if (ind != -1) {
                            if (ind >= bstS.size() - 1) {
                                bst = bstS.get(0);
                            } else {
                                bst = bstS.get(ind + 1);
                            }
                        }
                    }
                }
            } else if (GameState == -1) {
                if(!endSound.isPlaying()){
                    endSound.play();
                }
                if(playSound.isPlaying()){
                    playSound.stop();
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    Score = 0;
                    GameState = 1;
                    bst = null;
                    bstS.clear();
                    chooseSound();
                    player = new Player(playerTexture, GAME_WIDTH / 2, GAME_HEIGHT / 2);
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
    }
    
    @Override
    public void interpolate(float alpha) {

    }
    float introX = 0;
    float introY = 0;
    @Override
    public void render(Graphics g) {
        fitViewport.apply(g);
        if(GameState == 9){
            g.fillRect(introX,introY,GAME_WIDTH,GAME_HEIGHT);
            if(gameIntro!=null){
                gameIntro.playerAnimation.draw(g,GAME_WIDTH/2-250,introY);
            }
        }
        else{
            for(int j=-1;j<15;j++){
                for(int i=-1;i<15;i++) {
                    g.drawTexture(Background, 64*i+xPos, 64*j+yPos);
                }
            }
            if(GameState==0){
                //g.drawString("CLICK TO PLAY",GAME_WIDTH/2,GAME_HEIGHT/2);
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
            if(GameState==8 || GameState == 0){
                logoMain.playerAnimation.draw(g,GAME_WIDTH/2-250,-20);
            }
            if(GameState == 8){
                g.setColor(Color.WHITE);
                g.fillRect(introX,introY,GAME_WIDTH,GAME_HEIGHT);
                if(gameIntro!=null){
                    gameIntro.playerAnimation.draw(g,GAME_WIDTH/2-250,introY-20);
                }
            }
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
