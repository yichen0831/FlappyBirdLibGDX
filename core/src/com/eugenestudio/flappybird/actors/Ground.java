package com.eugenestudio.flappybird.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.eugenestudio.flappybird.GdxGame;

public class Ground extends Entity {
    private Sprite sprite;
    private GdxGame game;

    public Ground(GdxGame game, Texture texture) {
        this.game = game;
        sprite = new Sprite(texture);
        sprite.setSize(45f, 15);
    }

    @Override
    public void update(float deltaTime) {
        sprite.translateX(-game.getBirdSpeed() * deltaTime);
        if (sprite.getX() < -sprite.getWidth()) {
            sprite.setX(sprite.getX() + sprite.getWidth() * 4);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public float getWidth() {
        return sprite.getWidth();
    }
}
