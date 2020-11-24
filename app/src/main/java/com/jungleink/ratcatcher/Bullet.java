package com.jungleink.ratcatcher;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

public class Bullet {
    private int bullet_x;
    private int bullet_y;
    private int max_x;
    private int max_y;
    private int min_y = 0;
    private Bitmap bullet_model;
    private int bullet_speed = 30;
    private boolean running;
    private Random random;
    private GameView gameView;

    public Bullet(Bitmap bullet_model, GameView gameView, int max_x, int max_y, int bullet_x, int bullet_y, boolean running) {
        this.bullet_model = bullet_model;
        this.gameView = gameView;
        this.max_x = max_x;
        this.max_y = max_y;
        this.min_y = -bullet_model.getHeight();
        this.bullet_x = bullet_x;
        this.bullet_y = bullet_y;
        this.running = true;
        random = new Random();
    }

    public static Bullet cloneBullet(Bullet bullet, int from_x, int from_y, boolean running) {
        return new Bullet(bullet.bullet_model, bullet.gameView, bullet.max_x, bullet.max_y, from_x, from_y, running);
    }

    private void updateBullet() {
        bullet_y -= bullet_speed;
    }

    public void bulletIsOut() {
        if (bullet_y <= 0) {
            bullet_y = max_y+1000;
            bullet_x = random.nextInt(max_x - max_x / 3) + 1;
            running = false;
        }
    }

    public void drawBullet(Canvas canvas) {
        bulletIsOut();
        if (running) {
            updateBullet();
            canvas.drawBitmap(bullet_model, bullet_x, bullet_y, null);
        }
    }

    public int getBullet_x() {
        return bullet_x;
    }

    public void setBullet_x(int bullet_x) {
        this.bullet_x = bullet_x;
    }

    public int getBullet_y() {
        return bullet_y;
    }

    public void setBullet_y(int bullet_y) {
        this.bullet_y = bullet_y;
    }

    public Bitmap getBullet_model() {
        return bullet_model;
    }

    public void setBullet_model(Bitmap bullet_model) {
        this.bullet_model = bullet_model;
    }
}
