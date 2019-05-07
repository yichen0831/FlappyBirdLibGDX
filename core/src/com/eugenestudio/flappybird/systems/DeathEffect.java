package com.eugenestudio.flappybird.systems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.eugenestudio.flappybird.GdxGame;

public class DeathEffect extends Effect {
    private Animation<TextureRegion> animation;
    private float life = 1.2f;
    private Vector2 position;
    private float elapsed;

    public DeathEffect(GdxGame game, Texture texture, Vector2 position) {
        TextureRegion[] textureRegions = new TextureRegion[7];
        for (int i = 0; i < 7; i++) {
            TextureRegion textureRegion = new TextureRegion(texture, i * 32, 0, 32, 32);
            textureRegions[i] = textureRegion;
        }

        animation = new Animation<>(0.2f, textureRegions);

        this.position = position;
    }

    @Override
    public boolean isDone() {
        return life <= elapsed;
    }

    @Override
    public void update(float deltaTime) {
        elapsed += deltaTime;
    }

    @Override
    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(elapsed, true);
        batch.draw(currentFrame, position.x, position.y, 5f, 5f);
    }
}
