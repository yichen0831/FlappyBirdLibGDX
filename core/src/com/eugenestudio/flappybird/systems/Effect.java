package com.eugenestudio.flappybird.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Effect {
    public abstract boolean isDone();
    public abstract void update(float deltaTime);
    public abstract void draw(SpriteBatch batch);
}
