package com.mystudio.BSTPuzzle;

import java.awt.*;
import java.util.*;
import java.util.List;

import com.badlogic.gdx.Gdx;
import org.mini2Dx.core.Mdx;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import org.mini2Dx.core.Mdx;
import org.mini2Dx.core.graphics.Graphics;

import static com.mystudio.BSTPuzzle.BSTPuzzle.*;

public class BST {
    private Node root = null;
    public static final int nWidth=64;
    public static final int nHeight=64;
    public static final int nRadius=20;
    public static ArrayList<Color> colorList = new ArrayList<Color>();
    private ArrayList<Node> nodes = new ArrayList<Node>();
    Random rand = new Random(new Date().getTime());
    private int numberNodes = 0;
    private Node selectN1 = null;
    private Node selectN2 = null;
    boolean isDead = false;
    PlayerTexture ePointer = new PlayerTexture("point.png",64,64);
    String type = "BT";
    public BST(int n,Enemy e,String type){
        this.target = e;
        this.target.myBST = this;
        this.type = type;
        ePointer.playerAnimation.setLooping(false);
        colorList.add(Color.BLUE);
        colorList.add(Color.BLACK);
        colorList.add(Color.GREEN);
        colorList.add(Color.GRAY);
        this.numberNodes = n;
        ArrayList<Integer> randomNumber = new ArrayList<Integer>();
        while(randomNumber.size()<n){
            boolean has = true;
            int r_number=0;
            while(has==true){
                r_number = rand.nextInt(n)+1;
                has = false;
                for(int i=0;i<randomNumber.size();i++){
                    if(randomNumber.get(i)==r_number){
                        has=true;
                    }
                }
            }
            randomNumber.add(r_number);
        }
        for(int i=0;i<n;i++){
            Node node = new Node(randomNumber.get(i),nRadius*2*(i)+64,32+300,colorList.get(rand.nextInt(colorList.size())),nRadius,this.type);
            nodes.add(node);
        }
        boolean all = true;
        while(all == true){
            all = true;
            randomNode();
            for(int i=0;i<nodes.size();i++) {
                Node now = nodes.get(i);
                now.checkNode();
                if (now.isPass == false) {
                    all = false;
                }
            }
            if(all == true){
                System.out.println("WEWER");
            }
        }
        generateTree();
    }
    private void generateTree(){
        //System.out.println(this.root.getValue());
        xPos = 64;
        inorder(this.root,0);
        float del_x = GAME_WIDTH/2 - (xPos-64)/2 - 64;
        for(int i=0;i<this.nodes.size();i++){
            this.nodes.get(i).setX(this.nodes.get(i).getX()+del_x);
        }
    }
    public int xPos = 64;
    private void inorder(Node now,int height){
        if(now != null){
            inorder(now.left,height+1);
            //System.out.println(now.getValue());
            now.setX(xPos);
            now.setY(64+height*32);
            this.xPos += 32;
            //System.out.println(xPos);
            inorder(now.right,height+1);
        }
    }
    private void randomNode(){
        ArrayList<Node> temp = new ArrayList<Node>();
        for(int i=0;i<this.nodes.size();i++){
            temp.add(this.nodes.get(i));
            this.nodes.get(i).left = null;
            this.nodes.get(i).right = null;
            this.nodes.get(i).isSelected = false;
            this.nodes.get(i).isHover = false;
            this.nodes.get(i).isDrag = false;
        }
        Random rand = new Random();
        float rootX = GAME_WIDTH/2;
        float rootY = 64+64;
        Node current = temp.get(rand.nextInt(temp.size()));
        current.setX(rootX);
        current.setY(rootY);
        this.root = current;
        while(temp.size()>0){
            temp.remove(current);
            if(temp.size()>0) {
                int childI = rand.nextInt(5);
                if(childI == 0){
                    Node tmp = temp.get(rand.nextInt(temp.size()));
                    current.left = tmp;
                    tmp.setX(current.getX()-64);
                    tmp.setY(current.getY()+64);
                    current = tmp;
                }
                else if(childI == 1){
                    Node tmp = temp.get(rand.nextInt(temp.size()));
                    current.right = tmp;
                    tmp.setX(current.getX()+64);
                    tmp.setY(current.getY()+64);
                    current = tmp;
                }
                else if(childI == 2 || childI == 3){
                    Node tmp = temp.get(rand.nextInt(temp.size()));
                    current.left = tmp;
                    tmp.setX(current.getX()-64);
                    tmp.setY(current.getY()+64);
                    if(temp.size()-1>=1){
                        temp.remove(tmp);
                        Node tmp2 = temp.get(rand.nextInt(temp.size()));
                        current.right = tmp2;
                        tmp2.setX(current.getX()+64);
                        tmp2.setY(current.getY()+64);
                        current = tmp2;
                    }
                    else{
                        current = tmp;
                    }
                }
                else{
                    Node tmp = temp.get(rand.nextInt(temp.size()));
                    current.right = tmp;
                    tmp.setX(current.getX()+64);
                    tmp.setY(current.getY()+64);
                    if(temp.size()-1>=1){
                        temp.remove(tmp);
                        Node tmp2 = temp.get(rand.nextInt(temp.size()));
                        current.left = tmp2;
                        tmp2.setX(current.getX()-64);
                        tmp2.setY(current.getY()+64);
                        current = tmp2;
                    }
                    else{
                        current = tmp;
                    }
                }
            }
        }
    }
    private int isClick = 0;
    private int passTimer = 0;
    private boolean pass = false;
    private  Node nowNode = null;
    public Enemy target = null;
    public void update(){
        if(selectN1 != null && selectN2 != null && pass == false && !isDead){
            if(selectN1.left == selectN2 || selectN1.right == selectN2 || selectN2.left == selectN1 || selectN2.right == selectN1){
                int holdv = selectN1.getValue();
                Color holdc = selectN1.getColor();
                selectN1.setValue(selectN2.getValue());
                selectN1.setColor(selectN2.getColor());
                selectN2.setValue(holdv);
                selectN2.setColor(holdc);
                selectN1.isSelected = false;
                selectN2.isSelected = false;
                selectN1 = null;
                selectN2 = null;
            }
            else{
                selectN1.isSelected = false;
                selectN2.isSelected = false;
                selectN1 = null;
                selectN2 = null;
            }
        }
        if(nowNode == null && pass == false && !isDead){
            for(int i=0;i<nodes.size();i++) {
                Node now = nodes.get(i);
                if(now.isHover && Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
                    nowNode = now;
                    now.isDrag = true;
                }
                else{
                    now.isDrag = false;
                }
            }
        }
        else if(!Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            if(nowNode != null){
                nowNode.isDrag = false;
                nowNode = null;
            }
        }
        if(isClick == 0 && Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            isClick = 1;
        }
        else if(isClick == 1 && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            isClick = 2;
        }
        boolean allPass = true;
        for(int i=0;i<nodes.size();i++){
            Node now = nodes.get(i);
            if(now.isPass == false){
                allPass = false;
            }
//            if((now.left != null && now.left.getValue() > now.getValue()) || (now.right != null && now.right.getValue() < now.getValue())){
//                allPass = false;
//            }
            now.update();
            if(selectN1 == null || selectN2 == null){
                if(now.isHover && isClick == 2){
                    now.isSelected = true;
                    if(selectN1==null){
                        selectN1 = now;
                    }
                    else if(selectN1!=null && selectN2==null && now != selectN1){
                        selectN2 = now;
                    }
                }
                else{
                    //now.isSelected = false;
                }
            }
        }
        if(allPass == true){
            if(pass == false){
                pass = true;
            }
        }
        if(pass == true || isDead){
            //dead boom!
            if(this.target!=null){
                this.target.isDead = true;
            }
            if(isDead){
                for(int i=0;i<nodes.size();i++){
                    nodes.get(i).setColor(Color.RED);
                }
            }
            if(passTimer < 20*1){
                passTimer++;
            }
            else{
                confetti(root);
                confetti(root);
                if(pass){
                    player.life++;
                    player.killCount++;
                    Score+=numberNodes*100;
                }
                pass = false;
                passTimer = 0;
                selectN1 = null;
                selectN2 = null;
                if(nowNode != null){
                    nowNode.isDrag = false;
                    nowNode = null;
                }
                nodes.clear();
                numberNodes++;
                ArrayList<Integer> randomNumber = new ArrayList<Integer>();
                while(randomNumber.size()<numberNodes){
                    boolean has = true;
                    int r_number=0;
                    while(has==true){
                        r_number = rand.nextInt(numberNodes)+1;
                        has = false;
                        for(int i=0;i<randomNumber.size();i++){
                            if(randomNumber.get(i)==r_number){
                                has=true;
                            }
                        }
                    }
                    randomNumber.add(r_number);
                }
                for(int i=0;i<numberNodes;i++){
                    Node node = new Node(randomNumber.get(i),nRadius*2*(i)+64,32+300,colorList.get(rand.nextInt(colorList.size())),nRadius,this.type);
                    nodes.add(node);
                }
                //create confetti
                bstS.remove(bst);
                bst = null;
                target = null;
                //randomNode();
                //generateTree();
            }
        }
//        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
//            randomNode();
//            generateTree();
//        }
        if(isClick == 2){
            isClick = 0;
        }
    }
    void confetti(Node now){
        if(now != null){
            if(now.left != null){
                float rotation = (float) ((float) Math.atan((now.getX()-now.left.getX())/(now.getY()-now.left.getY()))*180/Math.PI);
                confettis.add(new Confetti(Math.min(now.getX(),now.left.getX())-10,Math.min(now.getY(),now.left.getY()),rotation));
                confetti(now.left);
            }
            if(now.right != null){
                float rotation = (float) ((float) Math.atan((now.getX()-now.right.getX())/(now.getY()-now.right.getY()))*180/Math.PI);
                confettis.add(new Confetti(Math.min(now.getX(),now.right.getX())-10,Math.min(now.getY(),now.right.getY()),rotation));
                confetti(now.right);
            }
        }
    }
    public void render(Graphics g){
        g.drawString(this.type,0,32);
        for(int i=0;i<nodes.size();i++){
            Node now = nodes.get(i);
            now.renderLine(g);
        }
        for(int i=0;i<nodes.size();i++){
            Node now = nodes.get(i);
            now.render(g);
        }
//        if(isClick == 0){
//            g.drawString("0",100,100);
//        }
//        else if(isClick == 1){
//            g.drawString("1",100,100);
//        }
//        else if(isClick == 2){
//            g.drawString("2",100,100);
//        }
        if(target!=null){
            ePointer.playerAnimation.setRotation(180);
            ePointer.playerAnimation.draw(g, target.x-32, target.y- target.height-32-24);
        }
    }
}
