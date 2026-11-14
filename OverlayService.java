package com.angryaim.pro;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class OverlayService extends Service {
    private WindowManager wm;
    private LinearLayout panel;
    private WindowManager.LayoutParams params;

    @Override public void onCreate() {
        super.onCreate();
        wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        int type = android.os.Build.VERSION.SDK_INT >= 26 ?
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                WindowManager.LayoutParams.TYPE_PHONE;

        panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(16, 12, 16, 12);
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.argb(235, 22, 31, 48));
        bg.setCornerRadius(24);
        bg.setStroke(2, Color.rgb(245,196,81));
        panel.setBackground(bg);

        TextView head = new TextView(this);
        head.setText("ANGRY AIM PRO  ✕");
        head.setTextColor(Color.rgb(245,196,81));
        head.setTextSize(16);
        head.setTypeface(null, Typeface.BOLD);
        panel.addView(head);

        TextView label = new TextView(this);
        label.setText("Practice angle: 0°");
        label.setTextColor(Color.WHITE);
        label.setTextSize(14);
        panel.addView(label);

        SeekBar angle = new SeekBar(this);
        angle.setMax(160);
        angle.setProgress(80);
        panel.addView(angle);
        angle.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar s, int p, boolean u) {
                label.setText("Practice angle: " + (p - 80) + "°");
            }
            public void onStartTrackingTouch(SeekBar s) {}
            public void onStopTrackingTouch(SeekBar s) {}
        });

        TextView tip = new TextView(this);
        tip.setText("Drag this panel to move • Practice aid only");
        tip.setTextColor(Color.LTGRAY);
        tip.setTextSize(11);
        panel.addView(tip);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                android.graphics.PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 30; params.y = 160;

        head.setOnTouchListener(new android.view.View.OnTouchListener() {
            int sx, sy; float tx, ty;
            public boolean onTouch(android.view.View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    sx = params.x; sy = params.y; tx = e.getRawX(); ty = e.getRawY(); return true;
                }
                if (e.getAction() == MotionEvent.ACTION_MOVE) {
                    params.x = sx + (int)(e.getRawX()-tx);
                    params.y = sy + (int)(e.getRawY()-ty);
                    wm.updateViewLayout(panel, params); return true;
                }
                if (e.getAction() == MotionEvent.ACTION_UP &&
                    e.getRawX()-tx < 8 && e.getRawX()-tx > -8 &&
                    e.getRawY()-ty < 8 && e.getRawY()-ty > -8) {
                    stopSelf(); return true;
                }
                return true;
            }
        });
        wm.addView(panel, params);
    }
    @Override public void onDestroy() {
        if (panel != null && panel.isAttachedToWindow()) wm.removeView(panel);
        super.onDestroy();
    }
    @Override public IBinder onBind(Intent i) { return null; }
}
