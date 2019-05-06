package com.eugenestudio.flappybird.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.kotcrab.vis.ui.widget.VisLabel;

public class Hud implements Disposable {

    private Stage stage;
    private VisLabel distanceLabel;
    private VisLabel speedLabel;
    private VisLabel playTimeLabel;
    private Image pauseImage;

    public Hud(AssetManager assetManager) {
        stage = new Stage();
        pauseImage = new Image(assetManager.get("textures/Pause.png", Texture.class));
        pauseImage.setSize(40f, 40f);
        pauseImage.setPosition((1280f - 40f) / 2f, (720f - 40f) / 2f);
        pauseImage.setVisible(false);
        stage.addActor(pauseImage);

        distanceLabel = new VisLabel("Distance: 0");
        distanceLabel.setPosition(320f, 15f);
        distanceLabel.setColor(0.3f, 0.3f, 0.3f, 1f);
        stage.addActor(distanceLabel);

        speedLabel = new VisLabel("Speed: 0");
        speedLabel.setPosition(20f, 15f);
        speedLabel.setColor(0.3f, 0.3f, 0.3f, 1f);
        stage.addActor(speedLabel);

        playTimeLabel = new VisLabel("Time: 0");
        playTimeLabel.setPosition(720f, 15f);
        playTimeLabel.setColor(0.3f, 0.3f, 0.3f, 1f);
        stage.addActor(playTimeLabel);
    }

    public void act(float deltaTime) {
        stage.act(deltaTime);
    }

    public void draw() {
        stage.draw();
    }

    public void setDistance(float distance) {
        distanceLabel.setText(String.format("Distance: %.2f", distance));
    }

    public void setSpeed(float speed) {
        speedLabel.setText(String.format("Speed: %.0f", speed));
    }

    public void setPlayTime(float playTime) {
        playTimeLabel.setText(String.format("Time: %.2f", playTime));
    }

    public void setPause(boolean paused) {
        pauseImage.setVisible(paused);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
