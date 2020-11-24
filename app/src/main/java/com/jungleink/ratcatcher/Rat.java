package com.jungleink.ratcatcher;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Rat {
    private int rat_x;
    private int rat_y;
    private int max_x;
    private int max_y;
    private Bitmap rat_model;
    private int rat_speed = 30;
    private Random random;
    private GameView gameView;
    private boolean is_drawable;

    public Rat(Bitmap rat_model, GameView gameView, int max_x, int max_y, int rat_x) {
        this.rat_model = rat_model;
        this.gameView = gameView;
        this.max_x = max_x;
        this.max_y = max_y;
        this.rat_x = rat_x;
        this.rat_y = -rat_model.getHeight();
        this.is_drawable = false;
        random = new Random();
    }


    public static Rat clone(Rat rat) {
        return new Rat(rat.rat_model, rat.getGameView(), rat.max_x, rat.max_y,
                new Random().nextInt(rat.max_x) + rat.rat_model.getWidth());
    }

    private void updateObstacle() {
        rat_y += rat_speed;
    }

    public void obstacleIsOut() {
        if (rat_y >= max_y) {
            rat_y = -60 - getRat_model().getHeight();
            rat_x = random.nextInt(max_x - getRat_model().getWidth()) + 1;
            is_drawable = false;
        }
    }

    public void drawRat(Canvas canvas) {
        obstacleIsOut();
        updateObstacle();
        canvas.drawBitmap(rat_model, rat_x, rat_y, null);
    }

    public int getMax_x() {
        return max_x;
    }

    public void setMax_x(int max_x) {
        this.max_x = max_x;
    }

    public int getMax_y() {
        return max_y;
    }

    public void setMax_y(int max_y) {
        this.max_y = max_y;
    }

    public int getRat_x() {
        return rat_x;
    }

    public void setRat_x(int rat_x) {
        this.rat_x = rat_x;
    }

    public int getRat_y() {
        return rat_y;
    }

    public void setRat_y(int rat_y) {
        this.rat_y = rat_y;
    }

    public Bitmap getRat_model() {
        return rat_model;
    }

    public void setRat_model(Bitmap rat_model) {
        this.rat_model = rat_model;
    }

    public GameView getGameView() {
        return gameView;
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }

    public boolean getIs_drawable() {
        return is_drawable;
    }

    public void setIs_drawable(boolean is_drawable) {
        this.is_drawable = is_drawable;
    }
}
