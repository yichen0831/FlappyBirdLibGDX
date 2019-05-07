package com.eugenestudio.flappybird.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;
import com.kotcrab.vis.ui.widget.VisLabel;

public class Hud implements Disposable {

    private Stage stage;
    private VisLabel distanceLabel;
    private VisLabel speedLabel;
    private VisLabel playTimeLabel;
    private VisLabel tapsLabel;
    private Image pauseImage;
    private Image getReadyImage;
    private Image gameOverImage;

    public Hud(AssetManager assetManager) {
        stage = new Stage();
        pauseImage = new Image(assetManager.get("textures/Pause.png", Texture.class));
        pauseImage.setSize(40f, 40f);
        pauseImage.setPosition((1280f - 40f) / 2f, (720f - 40f) / 2f);
        pauseImage.setVisible(false);
        stage.addActor(pauseImage);

        getReadyImage = new Image(assetManager.get("textures/GetReady.png", Texture.class));
        getReadyImage.setSize(480f, 140f);
        getReadyImage.setPosition((1280f - 480f) / 2f, (720f - 140f) / 2f);
        getReadyImage.setVisible(false);
        stage.addActor(getReadyImage);

        gameOverImage = new Image(assetManager.get("textures/GameOver.png", Texture.class));
        gameOverImage.setSize(500f, 140f);
        gameOverImage.setPosition((1280f - 500f) / 2f, (720f - 140f) / 2f);
        gameOverImage.setVisible(false);
        stage.addActor(gameOverImage);

        Color fontColor = new Color(0.4f, 0.5f, 0.36f, 1f);

        distanceLabel = new VisLabel("Distance: 0");
        distanceLabel.setPosition(320f, 15f);
        distanceLabel.setColor(fontColor);
        stage.addActor(distanceLabel);

        speedLabel = new VisLabel("Speed: 0");
        speedLabel.setPosition(20f, 15f);
        speedLabel.setColor(fontColor);
        stage.addActor(speedLabel);

        playTimeLabel = new VisLabel("Time: 0");
        playTimeLabel.setPosition(720f, 15f);
        playTimeLabel.setColor(fontColor);
        stage.addActor(playTimeLabel);

        tapsLabel = new VisLabel("Taps: 0");
        tapsLabel.setPosition(1020f, 15f);
        tapsLabel.setColor(fontColor);
        stage.addActor(tapsLabel);
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

    public void setTaps(int taps) {
        tapsLabel.setText(String.format("Taps: %d", taps));
    }

    public void setPause(boolean paused) {
        pauseImage.setVisible(paused);
    }

    public void setGetReadyImageVisible(boolean visible) {
        getReadyImage.setVisible(visible);
    }

    public void setGameOverImageVisible(boolean visible) {
        gameOverImage.setVisible(visible);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
