package com.jungleink.ratcatcher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {
    private MainActivity mainActivity;
    private GameThread gameThread;
    private Timer timer;
    private int score;
    private int max_x;
    private int max_y;
    private boolean isTouch = false;
    private Timer fruitTimer;
    private Timer snakeTimer;
    private Bitmap[] background;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastAccelerometerUpdate;

    private Rat rat;
    private List<Rat> rats;
    private Iterator<Rat> ratIterator;

    private RatCatcher ratCatcher;

    private Bullet bullet;

    private Bitmap life[];
    private int life_count;

    public GameView(Context context, int max_x, int max_y) {
        super(context);
        getHolder().addCallback(this);

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        lastAccelerometerUpdate = 0;

        mainActivity = (MainActivity) context;
        this.max_x = max_x;
        this.max_y = max_y;
        score = 0;
        timer = new Timer();
        fruitTimer = new Timer();
        snakeTimer = new Timer();
        gameThread = new GameThread(this);
        //rat catcher
        ratCatcher = new RatCatcher(scale_bitmap(R.drawable.rat_catcher, 180, 180), this, max_x, max_y);
        //background
        background = new Bitmap[2];
        background[0] = scale_bitmap(R.drawable.background, max_x, max_y);
        //life
        life = new Bitmap[2];
        life[0] = scale_bitmap(R.drawable.heart, 65, 65);
        life[1] = scale_bitmap(R.drawable.heart_g, 65, 65);
        life_count = 3;
        //rat
        rat = new Rat(scale_bitmap(R.drawable.mad_rat, 120, 120), this, max_x, max_y, max_x / 2);
        rats = initRatList(rat, rats);
        ratIterator = rats.iterator();
        //bullet
        bullet = new Bullet(scale_bitmap(R.drawable.music_notes, 120, 120), this, max_x / 2, max_y - 10, max_x + 10, -20, false);

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameThread.setRun(true);
                gameThread.start();
            }
        }, 100);
        scheduleRatTask();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void space_draw(Canvas canvas) {
        canvas.drawBitmap(background[0], 0, 0, null);
        drawRats(canvas);

        //Life
        for (int i = 0; i < life_count; i++) {
            int x = (int) (canvas.getWidth() - 350 + life[0].getWidth() * 1.5 * i);
            int y = 30;
            canvas.drawBitmap(life[0], x, y, null);
        }

        //rat collision
        for (Rat r : rats) {
            if (r.getIs_drawable() == true) {
                if (Rect.intersects(getCollisionObject(ratCatcher.getRat_catcher_x(), ratCatcher.getRat_catcher_y(), ratCatcher.getRat_catcher_model())
                        , getCollisionObject(r.getRat_x(), r.getRat_y(), r.getRat_model()))) {
                    life_count--;
                    r.setRat_y(max_y + 30);
                    r.setRat_x(0);
                    if (life_count == 0) {
                        mainActivity.startEndActivity(score);
                        gameThread.setRun(false);
                    }
                }
            }
        }


        if (isTouch) {
            runBullet(ratCatcher.getRat_catcher_x(), ratCatcher.getRat_catcher_y(), true);
            isTouch = false;
        }
        bullet.drawBullet(canvas);

        // bullet/rat collision
        for (Rat r : rats) {
            if (r.getIs_drawable() == true) {
                if (Rect.intersects(getCollisionObject(bullet.getBullet_x(), bullet.getBullet_y(), bullet.getBullet_model())
                        , getCollisionObject(r.getRat_x(), r.getRat_y(), r.getRat_model()))) {
                    r.setRat_y(max_y + 30);
                    r.setRat_x(0);
                    score += 10;
                }
            }
        }
        if (ratCatcher.getRat_catcher_x() >= max_x - ratCatcher.getRat_catcher_model().getWidth()) {
            ratCatcher.setRat_catcher_x(max_x - ratCatcher.getRat_catcher_model().getWidth());
        } else if (ratCatcher.getRat_catcher_x() <= 0) {
            ratCatcher.setRat_catcher_x(0);
        }

        ratCatcher.draw_rat_catcher(canvas);
    }

    private void runBullet( int rat_catcher_x, int rat_catcher_y, boolean running) {
        bullet = Bullet.cloneBullet(bullet, rat_catcher_x, rat_catcher_y, running);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isTouch = true;
        return super.onTouchEvent(event);
    }

    private void drawRats(Canvas canvas) {
        for (Rat r : rats) {
            if (r.getIs_drawable() == true) {
                r.drawRat(canvas);
            }
        }
    }

    private void scheduleRatTask() {
        snakeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!ratIterator.hasNext()) {
                    ratIterator = rats.iterator();
                } else {
                    ratIterator.next().setIs_drawable(true);
                }
            }
        }, 200, 700);
    }

    private Bitmap scale_bitmap(Integer img_id, int size_x, int size_y) {
        Bitmap buff = BitmapFactory.decodeResource(getResources(), img_id);
        Bitmap finalBitmap = Bitmap.createScaledBitmap(buff, size_x, size_y, false);
        return finalBitmap;
    }

    public Rect getCollisionObject(int left, int top, Bitmap object_to_colission) {
        return new Rect(left, top, left + object_to_colission.getWidth(), top + object_to_colission.getHeight());
    }

    private List<Rat> initRatList(Rat initRat, List<Rat> ratList) {
        ratList = new ArrayList<>();
        ratList.add(Rat.clone(initRat));
        ratList.add(Rat.clone(initRat));
        ratList.add(Rat.clone(initRat));
        ratList.add(Rat.clone(initRat));
        ratList.add(Rat.clone(initRat));
        ratList.add(Rat.clone(initRat));
        ratList.add(Rat.clone(initRat));
        return ratList;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (System.currentTimeMillis() - lastAccelerometerUpdate > 50) {
                if (event.values[0] > 0) {
                    ratCatcher.setCatcher_left(true);
                    ratCatcher.setCatcher_right(false);
                } else {
                    ratCatcher.setCatcher_right(true);
                    ratCatcher.setCatcher_left(false);
                }
                lastAccelerometerUpdate = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
