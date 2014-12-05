package org.moviemastery.moviemastery.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import org.moviemastery.moviemastery.R;

import hugo.weaving.DebugLog;

/**
 * Created by Oskar on 2014-11-29.
 */
public class SoundPlayerService extends Service {

    public static final String SOUND = "SOUND";

    public static final int CHEERING = 0;
    public static final int AW = 1;
    public static final int GAME_OVER = 2;

    MediaPlayer cheeringPlayer;
    MediaPlayer awPlayer;
    MediaPlayer gameOverPlayer;

    public void onCreate(){
        super.onCreate();
        cheeringPlayer = MediaPlayer.create(this, R.raw.cheering);
        cheeringPlayer.setLooping(false);

        awPlayer = MediaPlayer.create(this, R.raw.aww);
        awPlayer.setLooping(false);

        gameOverPlayer = MediaPlayer.create(this, R.raw.game_over);
        gameOverPlayer.setLooping(false);
    }

    @DebugLog
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            switch (intent.getExtras().getInt(SOUND)) {
                case CHEERING:
                    cheeringPlayer.start();
                    break;
                case AW:
                    awPlayer.start();
                    break;
                case GAME_OVER:
                    gameOverPlayer.start();
                    break;
            }
        }
        return START_STICKY;
    }

    public void onStop(){
        cheeringPlayer.stop();
        cheeringPlayer.release();
        awPlayer.stop();
        awPlayer.release();
    }

    public void onPause(){
        cheeringPlayer.stop();
        cheeringPlayer.release();
        awPlayer.stop();
        awPlayer.release();
    }

    public void onDestroy(){
        cheeringPlayer.stop();
        cheeringPlayer.release();
        awPlayer.stop();
        awPlayer.release();
    }

    @Override
    public IBinder onBind(Intent objIndent) {
        return null;
    }
}
