package com.eugenestudio.flappybird.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.eugenestudio.flappybird.GdxGame;

public class Bird extends Entity {
    private GdxGame game;
    private Sound flapSound;
    private Sound dieSound;
    private Sound hitSound;

    private TextureRegion[] textureRegions;
    private Sprite sprite;
    private float rotation;
    private Circle collision;

    private int frameIndex;
    private boolean flapping;
    private float frameElapsedTime;

    private float yVelocity;

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    private boolean alive;
    private float deadElapsedTime;
    private boolean isDeadSoundPlayed;

    private final float size = 5f;
    private final float gravity = -60f;
    private final float flapForce = 36f;
    private final float framesPerSeconds = 0.06f;

    public Bird(GdxGame game) {
        this.game = game;
        AssetManager assetManager = game.getAssetManager();
        Texture texture = assetManager.get("textures/BirdY.png");
        textureRegions = new TextureRegion[3];
        textureRegions[0] = new TextureRegion(texture, 0, 0, 18, 18);
        textureRegions[1] = new TextureRegion(texture, 18, 0, 18, 18);
        textureRegions[2] = new TextureRegion(texture, 36, 0, 18, 18);
        sprite = new Sprite(textureRegions[0]);
        sprite.setSize(size, size);
        sprite.setOriginCenter();
        collision = new Circle(sprite.getX() + sprite.getOriginX(), sprite.getY() + sprite.getOriginY(), (size * 0.6f) / 2f);

        flapSound = assetManager.get("sounds/sfx_wing.ogg");
        dieSound = assetManager.get("sounds/sfx_die.ogg");
        hitSound = assetManager.get("sounds/sfx_hit.ogg");

        alive = true;
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
        updateCollision();
    }

    public Circle getCircleCollision() {
        return collision;
    }

    public Rectangle getRectangleCollision() {
        return new Rectangle(collision.x - collision.radius, collision.y - collision.radius,
                collision.radius * 2f, collision.radius * 2f);
    }

    @Override
    public void update(float deltaTime) {
        if (alive) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                flap();
            }

            if (flapping) {
                frameElapsedTime += deltaTime;
                if (frameElapsedTime >= framesPerSeconds) {
                    frameElapsedTime -= framesPerSeconds;
                    frameIndex = (frameIndex + 1);
                }

                if (frameIndex >= textureRegions.length) {
                    frameIndex = 0;
                    flapping = false;
                }
                sprite.setRegion(textureRegions[frameIndex]);
            }

            rotation = yVelocity / 2f;

            // If the bird gets too high or too low, it dies.
            if (collision.y - collision.radius < 15f || collision.y + collision.radius >= 72f) {
                hit();
            }

        } else {
            rotation += 120f * deltaTime;
            deadElapsedTime += deltaTime;

            if (deadElapsedTime >= 0.3f && !isDeadSoundPlayed) {
                isDeadSoundPlayed = true;
                dieSound.play();
            }
        }

        yVelocity += gravity * deltaTime;
        sprite.translateY(yVelocity * deltaTime);
        sprite.setRotation(rotation);
        updateCollision();
    }

    private void updateCollision() {
        collision.setPosition(sprite.getX() + sprite.getOriginY(), sprite.getY() + sprite.getOriginY());
    }

    private void flap() {
        flapping = true;
        frameIndex = 1;
        frameElapsedTime = 0;

        yVelocity = flapForce;
        flapSound.play();
        game.addTap();
    }

    public void hit() {
        hitSound.play();
        yVelocity = 0f;
        alive = false;
        game.setGameOver();
    }

    public void reset() {
        yVelocity = 0f;
        rotation = 0f;
        sprite.setRotation(rotation);
        alive = true;
        isDeadSoundPlayed = false;
        deadElapsedTime = 0f;
    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }
}
