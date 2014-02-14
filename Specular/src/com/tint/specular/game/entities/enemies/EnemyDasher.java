package com.tint.specular.game.entities.enemies;
 
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tint.specular.game.GameState;
import com.tint.specular.game.entities.Particle.Type;
import com.tint.specular.utils.Util;
 
/**
 *
 * @author Hugo Holmqvist
 *
 */		

 
public class EnemyDasher extends Enemy {
 
        private static Texture tex;
 
        private float speed;
        private double direction;
        private int boostingDelay = -1;
        
		
       
        public EnemyDasher(float x, float y, GameState gs) {
                super(x, y, gs, 3);
        }

        @Override
        public void render(SpriteBatch batch) {
                Util.drawCentered(batch, tex, x, y, (float) Math.toDegrees(direction) - 90);
        }
        
        @Override
        public boolean update() {
               
                //Boosting and changing direction
                if(boostingDelay > 50) {
                        speed += 0.5;
                       
                        dx = (float) (Math.cos(direction) * speed);
                        dy = (float) (Math.sin(direction) * speed);
                        x += dx * slowdown;
                        y += dy * slowdown;
                       
                        boostingDelay++;
                }              
               
                if(boostingDelay == 0) {
                       
                        if(gs.getPlayer() != null) {
                                int dx = (int) (gs.getPlayer().getX() - x);
                                int dy = (int) (gs.getPlayer().getY() - y);
                               
                                if(Math.abs(dx) > Math.abs(dy)) {
                                        direction = (dx > 0 ? 0 : Math.PI);
                                       
                                } else {
                                        direction = (dy > 0 ? Math.PI / 2 : Math.PI / 2 * 3 );
 
                                }
                        }
                       
                        speed = 0;
                        boostingDelay += 1;
                } else {
                        boostingDelay++;
                }
 
                //If going right, check if the player is still on the right side
                if (direction == 0) {
                        if(gs.getPlayer().getX() < x) {
                                boostingDelay = 0;
                        }
                } else if (direction == Math.PI) {
                        if(gs.getPlayer().getX() > x) {
                                boostingDelay = 0;  
                        }
                } else if (direction == Math.PI / 2) {
                        if(gs.getPlayer().getY() < y) {
                                boostingDelay = 0;
                        }
                } else if (direction == Math.PI / 2 * 3) {
                        if(gs.getPlayer().getY() > y) {
                                boostingDelay = 0;
                        }
                }
               
                return super.update();
        }
 
       
        @Override
        public float getInnerRadius() { return 16; }
        @Override
        public float getOuterRadius() { return 30; }
       
        public static void init() {
                tex = new Texture(Gdx.files.internal("graphics/game/dasher.png"));
                tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        }
 
        @Override
        public int getValue() {
                return 5;
        }
       
        @Override
        public void dispose() {
                tex.dispose();
        }
 
        @Override
        public Type getParticleType() {
                return Type.ENEMY_WANDERER;
        }
}