package com.mystudio.BSTPuzzle;

public interface Physics {
    float hsp = 0;
    float vsp = 0;
    void updatePhysics();
    boolean collideWall(float hsp,float vsp);
}
