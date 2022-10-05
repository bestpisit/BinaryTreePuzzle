package com.mystudio.BSTPuzzle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.mini2Dx.core.graphics.Graphics;
import com.badlogic.gdx.graphics.Color;

import static com.mystudio.BSTPuzzle.BSTPuzzle.GAME_WIDTH;
import static com.mystudio.BSTPuzzle.BSTPuzzle.GAME_HEIGHT;

public class Node {
    private int value = 8;
    public Node left = null;
    public Node right = null;
    private float radius = 0;
    private Color color = null;
    private float x = -1;
    private float y = -1;
    private float fX = -1;
    private float fY = -1;
    public boolean isHover = false;
    public boolean isSelected = false;
    public boolean isDrag = false;
    public boolean isPass = false;
    public String type = "";
    public Node(int v,float x,float y,Color color,float radius,String type){
        this.value = v;
        this.x = x;
        this.y = y;
        this.color = color;
        this.radius = radius;
        this.fX = x;
        this.fY = y;
        this.type = type;
    }
    public void update(){

    }
    public void renderLine(Graphics g){
        g.setColor(Color.BLACK);
        this.isPass = true;
        if(this.left!=null){
            g.setLineHeight(5);
            if(this.type=="BST"){
                int leftMax = checkNodeL(this.left);
                g.drawString(String.valueOf(leftMax),this.x-32,this.y);
                if(leftMax>this.value){
                    g.setColor(Color.RED);
                    this.isPass = false;
                }
                else{
                    g.setColor(Color.GREEN);
                }
            }
            else if(this.type=="BT"){
                if(this.left.getValue()>this.getValue()){
                    g.setColor(Color.RED);
                    this.isPass = false;
                }
                else{
                    g.setColor(Color.GREEN);
                }
            }
            else if(this.type=="MAXHEAP"){
                if(this.left.getValue()>this.value){
                    g.setColor(Color.RED);
                    this.isPass = false;
                }
                else{
                    g.setColor(Color.GREEN);
                }
                if(this.right != null){
                    if(this.left.getValue()>this.right.getValue()){
                        g.setColor(Color.RED);
                        this.isPass = false;
                    }
                }
            }
            else if(this.type=="MINHEAP"){
                if(this.left.getValue()<this.value){
                    g.setColor(Color.RED);
                    this.isPass = false;
                }
                else{
                    g.setColor(Color.GREEN);
                }
                if(this.right != null){
                    if(this.left.getValue()>this.right.getValue()){
                        g.setColor(Color.RED);
                        this.isPass = false;
                    }
                }
            }
            g.drawLineSegment(this.x,this.y,this.left.getX(),this.left.getY());
        }
        if(this.right!=null){
            g.setLineHeight(5);
            if(this.type=="BST") {
                int rightMax = checkNodeR(this.right);
                g.drawString(String.valueOf(rightMax), this.x + 32, this.y);
                if (rightMax < this.value) {
                    g.setColor(Color.RED);
                    this.isPass = false;
                } else {
                    g.setColor(Color.GREEN);
                }
            }
            else if(this.type=="BT"){
                if(this.right.getValue()<this.getValue()){
                    g.setColor(Color.RED);
                    this.isPass = false;
                }
                else{
                    g.setColor(Color.GREEN);
                }
            }
            else if(this.type=="MAXHEAP"){
                if(this.right.getValue()>this.value){
                    g.setColor(Color.RED);
                    this.isPass = false;
                }
                else{
                    g.setColor(Color.GREEN);
                }
                if(this.left != null){
                    if(this.left.getValue()>this.right.getValue()){
                        g.setColor(Color.RED);
                        this.isPass = false;
                    }
                }
            }
            else if(this.type=="MINHEAP"){
                if(this.right.getValue()<this.value){
                    g.setColor(Color.RED);
                    this.isPass = false;
                }
                else{
                    g.setColor(Color.GREEN);
                }
                if(this.left != null){
                    if(this.left.getValue()>this.right.getValue()){
                        g.setColor(Color.RED);
                        this.isPass = false;
                    }
                }
            }
            g.drawLineSegment(this.x,this.y,this.right.getX(),this.right.getY());
        }
    }
    public int checkNodeL(Node now){
        if(now!=null){
            int max = Math.max(now.value, checkNodeL(now.left));
            return Math.max(max,checkNodeL(now.right));
        }
        else{
            return 0;
        }
    }
    public int checkNodeR(Node now){
        if(now!=null){
            int min = Math.min(now.value, checkNodeR(now.left));
            return Math.min(min,checkNodeR(now.right));
        }
        else{
            return this.getValue();
        }
    }
    public void render(Graphics g){
        g.setColor(Color.RED);
        float rX = GAME_WIDTH/g.getWindowWidth();
        float rY = GAME_HEIGHT/g.getWindowHeight();
        float tX = g.getWindowWidth()-g.getWindowHeight()*GAME_WIDTH/GAME_HEIGHT;
        if(tX >= 0){
            rX = GAME_WIDTH/(g.getWindowWidth()-tX);
            tX = tX/(4);
        }
        else{
            tX = 0;
        }
        //g.drawString(String.valueOf(g.getWindowHeight()/GAME_HEIGHT),100,400);
        //g.drawLineSegment(Gdx.input.getX()*rX-tX,Gdx.input.getY()*rY,this.x,this.y);
        g.setColor(this.color);
        if(collisionCheck(Gdx.input.getX()*rX-tX,Gdx.input.getY()*rY)){
            this.isHover = true;
        }
        else{
            this.isHover = false;
        }
        if(this.isDrag == true){
            this.fX = Gdx.input.getX()*rX-tX;
            this.fY = Gdx.input.getY()*rY;
        }
        else{
            this.fX = this.x;
            this.fY = this.y;
        }
        g.fillCircle(this.fX,this.fY,this.radius);
        g.setColor(Color.WHITE);
        g.drawString(Integer.toString(this.value),this.fX,this.fY);
    }
    public float getX(){
        return this.x;
    }
    public float getY(){
        return this.y;
    }
    public void setX(float x){
        this.x = x;
    }
    public void setY(float y){
        this.y = y;
    }
    public int getValue(){
        return this.value;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setValue(int value){this.value = value;}
    boolean collisionCheck(float x,float y){
        double r_check = Math.sqrt(Math.pow(this.fX-x,2)+Math.pow(this.fY-y,2));
        if(r_check <= this.radius){
            return true;
        }
        else{
            return false;
        }
    }
}
