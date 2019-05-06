package com.eugenestudio.flappybird.actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Entity {
    public abstract void update(float deltaTime);
    public abstract void draw(SpriteBatch batch);
}
