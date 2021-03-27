package com.wang17.myphone.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang17.myphone.R;
import com.wang17.myphone.database.Setting;
import com.wang17.myphone.service.NianfoMusicService;
import com.wang17.myphone.database.DataContext;
import com.wang17.myphone.util._Session;
import com.wang17.myphone.util._Utils;

import java.util.Calendar;

public class AlarmWindowActivity extends AppCompatActivity {


    private MediaPlayer mp = new MediaPlayer();
    private Vibrator vibrator;
    private PowerManager.WakeLock mWakelock;
    private DataContext mDataContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); // hide title
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        winParams.flags |= (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        mDataContext= new DataContext(AlarmWindowActivity.this);
//        startMedia();
//        startVibrator();

        createDialog();

//        _Utils.speaker(this,"阿 弥 陀 佛");
        _Utils.speaker(this,mDataContext.getSetting(Setting.KEYS.alarm_window_msg,"阿 弥 陀 佛").getString());
//        Intent i = new Intent(this, SpeakerService.class);
//        i.putExtra("msg","愿以此功德，庄严佛净土，上报四重恩，下济三途苦，若有见闻者，西发菩提心，尽此一报身，同生 极 乐 国。");
//        startService(i);


        new Thread(new Runnable() {
            @Override
            public void run() {
//                SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
//                soundPool.load(AlarmWindowActivity.this, R.raw.ling3, 1);
//                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
//                    @Override
//                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                        int streamId = soundPool.play(1, ALERT_VOLUMN, ALERT_VOLUMN, 0, 0, 1);
//                        try {
//                            float add = (1 - ALERT_VOLUMN) / 2;
//                            float vol = ALERT_VOLUMN;
//                            for (int i = 0; i < 2; i++) {
//                                Thread.sleep(1000);
//                                vol += add;
//                                soundPool.setVolume(streamId, vol, vol);
//                            }
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });


                /**
                 * 停止念佛音乐服务
                 */
                if(mDataContext.getSetting(Setting.KEYS.tally_music_is_playing,false).getBoolean()){
                    stopService(new Intent(AlarmWindowActivity.this, NianfoMusicService.class));
                }
            }
        }).start();
    }

    /**
     *
     */
    private void createDialog() {
        try {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_alarm_finish, null);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(view).setCancelable(false).create();
            LinearLayout layoutMsg = (LinearLayout) view.findViewById(R.id.layout_msg);
            layoutMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            TextView textViewTitle = (TextView) view.findViewById(R.id.textView_msg);
            // 用findViewById这种方式来查找视图中的元素，必须先要将此视图“填充”到该容器中。因为AlertDialog的setView方法不具有这种“填充”功能，
            // 所以先要将dialog_alarm_finish视图文件填充到一个View中，讲此View对象setView给AlertDialog，然后通过该View来查找视图文件中对应的元素。
//            textViewTitle.setText("打开窗户，透透气。");
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 推迟10分钟提醒
     */
    private void tenMRemind() {
        //设置时间
        Calendar calendar_now = Calendar.getInstance();

        calendar_now.setTimeInMillis(System.currentTimeMillis());
        calendar_now.set(Calendar.HOUR_OF_DAY, calendar_now.get(Calendar.HOUR_OF_DAY));
        calendar_now.set(Calendar.MINUTE, calendar_now.get(Calendar.MINUTE) + 10);
        calendar_now.set(Calendar.SECOND, 0);
        calendar_now.set(Calendar.MILLISECOND, 0);

        //时间选择好了
        Intent intent = new Intent(_Session.ACTION_ALARM_NIANFO_OVER);
        //注册闹钟广播
        PendingIntent sender = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am;
        am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, calendar_now.getTimeInMillis(), sender);
    }

    /**
     * 开始播放铃声
     */
    private void startMedia() {
        try {
            mp.setDataSource(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
