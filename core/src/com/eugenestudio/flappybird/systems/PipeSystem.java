package com.eugenestudio.flappybird.systems;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Queue;
import com.eugenestudio.flappybird.GdxGame;
import com.eugenestudio.flappybird.actors.Bird;
import com.eugenestudio.flappybird.actors.Pipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PipeSystem {
    private GdxGame game;
    private Queue<Pipe> pipeQueue;
    private Queue<Pipe> pipeRemovalQueue;
    private List<Pipe> pipeList;

    private float nextDistance;
    private Random random;

    public PipeSystem(GdxGame game) {
        this.game = game;
        random = new Random(System.nanoTime());
        nextDistance = 20f;
        pipeList = new ArrayList<>();
        pipeRemovalQueue = new Queue<>();
        pipeQueue = new Queue<>();
        for (int i = 0; i < 10; i++) {
            Pipe pipe = new Pipe(game);
            pipeQueue.addLast(pipe);
        }
    }

    public void update(float deltaTime) {
        nextDistance -= game.getBirdSpeed() * deltaTime;

        if (nextDistance <= 0f) {
            float variance = -nextDistance;
            nextDistance += 40f;

            // Place the pipes.
            if (pipeQueue.size > 0) {
                Pipe pipe = pipeQueue.removeFirst();
                // Y Range: -26 ~ -56 with gap 10, -36 ~ -56 with gap 20
                float gap = 20f;
                int min = 16 + (int) gap;
                int max = 56;
                int y = -(min + random.nextInt(max - min));
                pipe.setPosition(128 + variance, y, gap);
                pipeList.add(pipe);

            }
        }

        for (Pipe pipe : pipeList) {
            pipe.update(deltaTime);

            if (pipe.isDone()) {
                pipeRemovalQueue.addLast(pipe);
            }
        }

        while (!pipeRemovalQueue.isEmpty()) {
            Pipe pipe = pipeRemovalQueue.removeFirst();
            pipe.reset();
            pipeList.remove(pipe);
            pipeQueue.addLast(pipe);
        }
    }

    public void draw(SpriteBatch batch) {
        for (Pipe pipe : pipeList) {
            pipe.draw(batch);
        }
    }

    public boolean checkCollision(Bird bird) {
        Rectangle rectangle = bird.getRectangleCollision();
        return pipeList.stream().anyMatch((pipe -> pipe.checkCollision(rectangle)));
    }

    public List<Pipe> getPipeList() {
        return pipeList;
    }

    public void reset() {
        while (!pipeRemovalQueue.isEmpty()) {
            Pipe pipe = pipeRemovalQueue.removeFirst();
            pipe.reset();
            pipeQueue.addLast(pipe);
        }

        for (Pipe pipe : pipeList) {
            pipe.reset();
            pipeQueue.addFirst(pipe);
        }

        pipeList.clear();
    }
}
