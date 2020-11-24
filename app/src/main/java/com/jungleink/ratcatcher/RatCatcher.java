package com.jungleink.ratcatcher;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class RatCatcher {
    private int rat_catcher_x;
    private int rat_catcher_y;
    private int max_x;
    private int max_y;

    private int rat_catcher_speed;
    private Bitmap rat_catcher_model;

    private GameView gameView;
    private boolean catcher_right = false;
    private boolean catcher_left = false;

    public RatCatcher(Bitmap rat_catcher_model, GameView gameView, int max_x, int max_y) {
        this.rat_catcher_model = rat_catcher_model;
        this.gameView = gameView;
        this.max_x = max_x;
        this.max_y = max_y;

        this.rat_catcher_x = max_x / 2;
        this.rat_catcher_y = max_y - rat_catcher_model.getHeight() - 10;
    }

    public void draw_rat_catcher(Canvas canvas) {
        if (catcher_right==true){
            rat_catcher_x+=10;
        }else if (catcher_left==true){
            rat_catcher_x-=10;
        }
        canvas.drawBitmap(rat_catcher_model, rat_catcher_x, rat_catcher_y, null);
    }

    public void setRat_catcher_x(int rat_catcher_x) {
        this.rat_catcher_x = rat_catcher_x;
    }

    public int getRat_catcher_x() {
        return rat_catcher_x;
    }

    public Bitmap getRat_catcher_model() {
        return rat_catcher_model;
    }

    public void setRat_catcher_model(Bitmap rat_catcher_model) {
        this.rat_catcher_model = rat_catcher_model;
    }

    public int getRat_catcher_y() {
        return rat_catcher_y;
    }

    public void setRat_catcher_y(int rat_catcher_y) {
        this.rat_catcher_y = rat_catcher_y;
    }

    public boolean isCatcher_right() {
        return catcher_right;
    }

    public void setCatcher_right(boolean catcher_right) {
        this.catcher_right = catcher_right;
    }

    public boolean isCatcher_left() {
        return catcher_left;
    }

    public void setCatcher_left(boolean catcher_left) {
        this.catcher_left = catcher_left;
    }
}
