package com.eugenestudio.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.eugenestudio.flappybird.actors.*;
import com.eugenestudio.flappybird.systems.PipeSystem;
import com.eugenestudio.flappybird.ui.Hud;
import com.kotcrab.vis.ui.VisUI;

import java.util.ArrayList;
import java.util.List;

public class GdxGame extends ApplicationAdapter {
    private final float viewportWidth = 128f;
    private final float viewportHeight = 72f;

    private enum State {
        Ready,
        Gaming,
        GameOver
    }

    private State state;

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private FitViewport viewport;

    private Bird bird;
    private Hud hud;

    public AssetManager getAssetManager() {
        return assetManager;
    }

    private AssetManager assetManager;
    private Sound pauseSound;
    private Sound readySound;
    private Sound goSound;
    private Sound gameOverSound;

    private float distance;
    private float playTime;
    private int taps;

    private boolean drawDebug;
    private boolean isGamePlaying;
    private boolean gameOver;
    private float canRestartCountDown = 2f;
    private float readyDelay = 1.5f;
    private boolean isReadySoundPlayed;
    private boolean isGoSoundPlayed;
    private boolean isGameOverSoundPlayed;

    public float getBirdSpeed() {
        return birdSpeed;
    }

    private float birdSpeed;
    private final float maxBirdSpeed = 50f;
    private final float minBirdSpeed = 5f;
    private final float birdSpeedStep = 5f;

    private PipeSystem pipeSystem;
    private List<Background> backgroundList;
    private List<Ground> groundList;

    @Override
    public void create() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.translate(viewportWidth / 2f, viewportHeight / 2f);
        viewport = new FitViewport(viewportWidth, viewportHeight, camera);

        shapeRenderer = new ShapeRenderer();

        assetManager = new AssetManager();
        loadAssets(assetManager);
        createSounds();
        createEntities();

        state = State.Ready;
        pipeSystem = new PipeSystem(this);

        VisUI.load(VisUI.SkinScale.X2);
        hud = new Hud(assetManager);

        setBirdSpeed(20f);
    }

    private void loadAssets(AssetManager assetManager) {
        TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter();
        textureParameter.minFilter = Texture.TextureFilter.Nearest;
        textureParameter.magFilter = Texture.TextureFilter.Nearest;
        assetManager.load("textures/BirdY.png", Texture.class, textureParameter);
        assetManager.load("textures/BackgroundLight.png", Texture.class, textureParameter);
        assetManager.load("textures/Ground.png", Texture.class, textureParameter);
        assetManager.load("textures/PipeTop.png", Texture.class, textureParameter);
        assetManager.load("textures/PipeBottom.png", Texture.class, textureParameter);
        assetManager.load("textures/Pause.png", Texture.class, textureParameter);
        assetManager.load("textures/GetReady.png", Texture.class, textureParameter);
        assetManager.load("textures/GameOver.png", Texture.class, textureParameter);

        assetManager.load("sounds/sfx_wing.ogg", Sound.class);
        assetManager.load("sounds/sfx_die.ogg", Sound.class);
        assetManager.load("sounds/sfx_hit.ogg", Sound.class);
        assetManager.load("sounds/Pause.ogg", Sound.class);
        assetManager.load("sounds/ready.ogg", Sound.class);
        assetManager.load("sounds/go.ogg", Sound.class);
        assetManager.load("sounds/game_over.ogg", Sound.class);
        assetManager.finishLoading();
    }

    private void createSounds() {
        pauseSound = assetManager.get("sounds/Pause.ogg");
        readySound = assetManager.get("sounds/ready.ogg");
        goSound = assetManager.get("sounds/go.ogg");
        gameOverSound = assetManager.get("sounds/game_over.ogg");
    }

    private void createEntities() {
        bird = new Bird(this);
        bird.setPosition(20f, 48f);

        backgroundList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Background background = new Background(this, assetManager.get("textures/BackgroundLight.png"));
            background.setPosition(background.getWidth() * i, 0f);
            backgroundList.add(background);
        }

        groundList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Ground ground = new Ground(this, assetManager.get("textures/Ground.png"));
            ground.setPosition(ground.getWidth() * i, 0f);
            groundList.add(ground);
        }
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (Background background : backgroundList) {
            background.draw(batch);
        }

        pipeSystem.draw(batch);

        for (Ground ground : groundList) {
            ground.draw(batch);
        }

        bird.draw(batch);
        batch.end();
        hud.draw();

        if (drawDebug) {
            Circle circle = bird.getCircleCollision();
            Rectangle rectangle = bird.getRectangleCollision();
            shapeRenderer.setColor(0f, 1f, 0f, 1f);
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.circle(circle.x, circle.y, circle.radius, 20);
            shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);

            for (Pipe pipe : pipeSystem.getPipeList()) {

                for (Rectangle rect : pipe.getBoundingRectangles()) {
                    shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
                }
            }

            shapeRenderer.end();
        }
    }

    private void update(float deltaTime) {
        camera.update();
        hud.act(deltaTime);
        handleInput();

        switch (state) {
            case Ready:
                readyDelay -= deltaTime;
                if (readyDelay <= 0f) {
                    state = State.Gaming;
                    isGamePlaying = true;
                    hud.setGetReadyImageVisible(false);
                } else if (readyDelay < 0.5f) {
                    if (!isGoSoundPlayed) {
                        isGoSoundPlayed = true;
                        goSound.play();
                    }
                } else if (!isReadySoundPlayed) {
                    isReadySoundPlayed = true;
                    readySound.play();
                    hud.setGetReadyImageVisible(true);
                }
                break;
            case Gaming:
                break;
            case GameOver:
                if (canRestart()) {
                    if (!isGameOverSoundPlayed) {
                        isGameOverSoundPlayed = true;
                        gameOverSound.play();
                    }
                    hud.setGameOverImageVisible(true);
                }
                break;
        }

        if (!isGamePlaying) {
            return;
        }

        for (Background background : backgroundList) {
            background.update(deltaTime);
        }

        pipeSystem.update(deltaTime);

        for (Ground ground : groundList) {
            ground.update(deltaTime);
        }

        bird.update(deltaTime);

        if (!gameOver) {
            distance += birdSpeed * deltaTime;
            playTime += deltaTime;

            if (pipeSystem.checkCollision(bird)) {
                bird.hit();
            }
        } else {
            canRestartCountDown -= deltaTime;
        }

        hud.setDistance(distance);
        hud.setPlayTime(playTime);
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            drawDebug = !drawDebug;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (state) {
                case Ready:
                    break;
                case Gaming:
                    isGamePlaying = !isGamePlaying;
                    hud.setPause(!isGamePlaying);
                    pauseSound.play();
                    break;
                case GameOver:
                    if (canRestart()) {
                        restart();
                    }
                    break;
            }
        }

        if (isGamePlaying) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                decreaseBirdSpeed();
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                increaseBirdSpeed();
            }
        }
    }

    private boolean canRestart() {
        return canRestartCountDown <= 0f;
    }

    private void increaseBirdSpeed() {
        birdSpeed = Math.min(birdSpeed + birdSpeedStep, maxBirdSpeed);
        hud.setSpeed(birdSpeed);
    }

    private void decreaseBirdSpeed() {
        birdSpeed = Math.max(birdSpeed - birdSpeedStep, minBirdSpeed);
        hud.setSpeed(birdSpeed);
    }

    private void setBirdSpeed(float speed) {
        birdSpeed = speed;
        hud.setSpeed(birdSpeed);
    }

    public void setGameOver() {
        gameOver = true;
        state = State.GameOver;
        setBirdSpeed(0f);
    }

    private void restart() {
        gameOver = false;
        isGamePlaying = false;
        canRestartCountDown = 2f;
        readyDelay = 1.5f;
        isReadySoundPlayed = false;
        isGoSoundPlayed = false;
        isGameOverSoundPlayed = false;

        taps = 0;
        distance = 0f;
        playTime = 0f;
        pipeSystem.reset();
        bird.reset();
        bird.setPosition(20f, 48f);
        setBirdSpeed(20f);
        state = State.Ready;
        hud.setGameOverImageVisible(false);
        hud.setPause(false);
        hud.setDistance(distance);
        hud.setPlayTime(playTime);
        hud.setTaps(taps);
    }

    public void addTap() {
        taps++;
        hud.setTaps(taps);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        assetManager.dispose();
        shapeRenderer.dispose();
        hud.dispose();
        VisUI.dispose();
    }
}
