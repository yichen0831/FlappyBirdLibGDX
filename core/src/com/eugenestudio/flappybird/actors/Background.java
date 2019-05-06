package com.eugenestudio.flappybird.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.eugenestudio.flappybird.GdxGame;

public class Background extends Entity {
    private Sprite sprite;
    private GdxGame game;

    public Background(GdxGame game, Texture texture) {
        this.game = game;
        sprite = new Sprite(texture);
        sprite.setSize(40.5f,72f);
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    @Override
    public void update(float deltaTime) {
        sprite.translateX(-game.getBirdSpeed() * deltaTime);

        if (sprite.getX() < -sprite.getWidth()) {
            sprite.setX(sprite.getX() + sprite.getWidth() * 5);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
