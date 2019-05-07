package com.eugenestudio.flappybird.systems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.eugenestudio.flappybird.GdxGame;

public class FlapEffect extends Effect {
    private GdxGame game;
    private Vector2 position;
    private Sprite sprite;
    private float life = 1f;
    private float scale = 1f;

    public FlapEffect(GdxGame game, Texture texture, Vector2 position) {
        this.game = game;
        this.position = position;
        sprite = new Sprite(texture);
        sprite.setSize(4.2f, 4.2f);
        sprite.setOriginCenter();
        sprite.setPosition(position.x, position.y);
    }

    public boolean isDone() {
        return life <= 0f;
    }

    public void update(float deltaTime) {
        life -= deltaTime;
        scale += deltaTime;
        position.x -= game.getBirdSpeed() * deltaTime;
        sprite.setX(position.x);
        sprite.setScale(scale);
        sprite.setAlpha(life / 1f);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
