package com.angryaim.pro;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(28, 40, 28, 24);
        TextView title = new TextView(this);
        title.setText("🎯 ANGRY AIM PRO\nFloating practice panel");
        title.setTextSize(24);
        box.addView(title);
        TextView note = new TextView(this);
        note.setText("\nThis is a simple movable practice panel. It does not read Carrom Pool, inspect its screen, or control the game.\n");
        note.setTextSize(16);
        box.addView(note);
        Button start = new Button(this);
        start.setText("Enable overlay & start");
        box.addView(start);
        Button stop = new Button(this);
        stop.setText("Stop overlay");
        box.addView(stop);
        setContentView(box);

        start.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivity(i);
            } else {
                startService(new Intent(this, OverlayService.class));
                finish();
            }
        });
        stop.setOnClickListener(v -> stopService(new Intent(this, OverlayService.class)));
    }
}
