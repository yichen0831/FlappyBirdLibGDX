package com.eugenestudio.flappybird.systems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;
import com.eugenestudio.flappybird.GdxGame;

import java.util.ArrayList;
import java.util.List;

public class EffectSystem {
    private GdxGame game;
    private Texture flapEffectTexture;
    private Texture deathEffectTexture;
    private List<Effect> effectList;
    private Queue<Effect> effectRemovalQueue;

    public EffectSystem(GdxGame game) {
        this.game = game;
        flapEffectTexture = game.getAssetManager().get("textures/effect_circle.png");
        deathEffectTexture = game.getAssetManager().get("textures/grumpy_bubble.png");

        effectList = new ArrayList<>();
        effectRemovalQueue = new Queue<>();
    }

    public void update(float deltaTime) {
        for (Effect effect : effectList) {
            effect.update(deltaTime);
            if (effect.isDone()) {
                effectRemovalQueue.addLast(effect);
            }
        }

        while (!effectRemovalQueue.isEmpty()) {
            Effect effect = effectRemovalQueue.removeFirst();
            effectList.remove(effect);
        }
    }

    public void draw(SpriteBatch batch) {
        for (Effect effect : effectList) {
            effect.draw(batch);
        }
    }

    public void addFlapEffect(Vector2 position) {
        FlapEffect effect = new FlapEffect(game, flapEffectTexture, position);
        effectList.add(effect);
    }

    public void addFlapEffect(float x, float y) {
        addFlapEffect(new Vector2(x, y));
    }

    public void addDeathEffect(Vector2 position) {
        DeathEffect effect = new DeathEffect(game, deathEffectTexture, position);
        effectList.add(effect);
    }

    public void addDeathEffect(float x, float y) {
        addDeathEffect(new Vector2(x, y));
    }
}
