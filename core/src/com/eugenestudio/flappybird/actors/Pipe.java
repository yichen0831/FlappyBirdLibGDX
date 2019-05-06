package com.eugenestudio.flappybird.actors;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.eugenestudio.flappybird.GdxGame;

import java.util.ArrayList;
import java.util.List;

public class Pipe extends Entity {
    private GdxGame game;
    private Sprite topPipeSprite;
    private Sprite bottomPipeSprite;

    private Rectangle[] rectangles;

    private final float width = 13f;
    private final float height = 80f;

    public boolean isDone() {
        return isDone;
    }

    private boolean isDone;

    public Pipe(GdxGame game) {
        this.game = game;
        AssetManager assetManager = game.getAssetManager();
        topPipeSprite = new Sprite(assetManager.get("textures/PipeTop.png", Texture.class));
        bottomPipeSprite = new Sprite(assetManager.get("textures/PipeBottom.png", Texture.class));
        topPipeSprite.setSize(width, height);
        bottomPipeSprite.setSize(width, height);
        rectangles = new Rectangle[]{new Rectangle(), new Rectangle()};
    }

    public List<Rectangle> getBoundingRectangles() {
        List<Rectangle> rectangleList = new ArrayList<>(2);
        rectangleList.add(topPipeSprite.getBoundingRectangle());
        rectangleList.add(bottomPipeSprite.getBoundingRectangle());
        return rectangleList;
    }

    @Override
    public void update(float deltaTime) {
        float movementX = -game.getBirdSpeed() * deltaTime;
        topPipeSprite.translateX(movementX);
        bottomPipeSprite.translateX(movementX);

        rectangles[0].set(topPipeSprite.getBoundingRectangle());
        rectangles[1].set(bottomPipeSprite.getBoundingRectangle());

        if (topPipeSprite.getX() <= -20f) {
            isDone = true;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        topPipeSprite.draw(batch);
        bottomPipeSprite.draw(batch);
    }

    public boolean checkCollision(Rectangle rectangle) {
        for (Rectangle rect :
                rectangles) {
            if (rect.overlaps(rectangle)) {
                return true;
            }
        }

        return false;
    }

    public void setPosition(float x, float y, float gap) {
        topPipeSprite.setPosition(x, y + height + gap);
        bottomPipeSprite.setPosition(x, y);
    }

    public void reset() {
        isDone = false;
    }
}
