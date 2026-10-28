package com.example.gujaraticlock;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.*;
import java.util.Calendar;

public class MainActivity extends Activity {
    SharedPreferences p;
    TextView[] t = new TextView[4];
    LinearLayout prev;
    Handler h = new Handler();
    Runnable r = new Runnable() { public void run() { refresh(); h.postDelayed(this, 1000); } };

    int dp(int v) { return (int) (v * getResources().getDisplayMetrics().density); }

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        p = getSharedPreferences("p", MODE_PRIVATE);
        ScrollView sv = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(16), dp(16), dp(16));
        sv.addView(root);

        prev = new LinearLayout(this);
        prev.setOrientation(LinearLayout.VERTICAL);
        prev.setGravity(Gravity.CENTER);
        prev.setPadding(dp(12), dp(20), dp(12), dp(20));
        for (int k = 0; k < 4; k++) {
            t[k] = new TextView(this);
            t[k].setGravity(Gravity.CENTER);
            prev.addView(t[k]);
        }
        t[0].setTypeface(null, 1); t[2].setTypeface(null, 1);
        root.addView(prev);

        root.addView(label("થીમ / Themes"));
        int[][] P = {{0xFFFFF7E6,0xFF7A1F1F},{0xFF101820,0xFFFFD166},{0xFFE8F5E9,0xFF1B5E20},{0xFFE3F2FD,0xFF0D47A1},{0xFF000000,0xFFFFFFFF},{0xFFFFF176,0xFF222222}};
        LinearLayout row = new LinearLayout(this);
        for (final int[] c : P) {
            Button bt = new Button(this);
            bt.setText("અ"); bt.setBackgroundColor(c[0]); bt.setTextColor(c[1]);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(52), 1);
            lp.setMargins(dp(3), 0, dp(3), 0);
            bt.setLayoutParams(lp);
            bt.setOnClickListener(v -> { p.edit().putInt("bg", c[0]).putInt("fg", c[1]).apply(); changed(); });
            row.addView(bt);
        }
        root.addView(row);

        root.addView(label("અક્ષર સાઇઝ / Text size"));
        root.addView(seek("size", 100, 50));
        root.addView(label("પારદર્શકતા / Background opacity"));
        root.addView(seek("alpha", 255, 255));

        CheckBox cb = new CheckBox(this);
        cb.setText("તારીખ બતાવો / Show date");
        cb.setChecked(p.getBoolean("date", true));
        cb.setOnCheckedChangeListener((v, on) -> { p.edit().putBoolean("date", on).apply(); changed(); });
        root.addView(cb);

        Button pin = new Button(this);
        pin.setText("વિજેટ હોમ સ્ક્રીન પર ઉમેરો / Add widget");
        pin.setOnClickListener(v -> {
            AppWidgetManager m = AppWidgetManager.getInstance(this);
            if (Build.VERSION.SDK_INT >= 26 && m.isRequestPinAppWidgetSupported()) {
                m.requestPinAppWidget(new ComponentName(this, ClockWidget.class), null, null);
            } else {
                Toast.makeText(this, "Home screen પર લાંબો સમય દબાવો → Widgets → ગુજરાતી ઘડિયાળ", Toast.LENGTH_LONG).show();
            }
        });
        root.addView(pin);
        setContentView(sv);
    }

    TextView label(String s) {
        TextView v = new TextView(this);
        v.setText(s); v.setPadding(0, dp(16), 0, dp(4));
        return v;
    }

    SeekBar seek(final String key, int max, int def) {
        SeekBar s = new SeekBar(this);
        s.setMax(max); s.setProgress(p.getInt(key, def));
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar sb, int v, boolean u) { p.edit().putInt(key, v).apply(); changed(); }
            public void onStartTrackingTouch(SeekBar sb) {}
            public void onStopTrackingTouch(SeekBar sb) {}
        });
        return s;
    }

    void changed() { refresh(); ClockWidget.updateAll(this); }

    void refresh() {
        int bg = p.getInt("bg", 0xFFFFF7E6), fg = p.getInt("fg", 0xFF7A1F1F);
        prev.setBackgroundColor(Color.argb(p.getInt("alpha", 255), Color.red(bg), Color.green(bg), Color.blue(bg)));
        float s = 0.6f + p.getInt("size", 50) / 100f;
        Calendar c = Calendar.getInstance();
        String[] tx = {Gu.day(c), Gu.period(c), Gu.time(c), Gu.date(c)};
        float[] sz = {26, 18, 56, 20};
        for (int k = 0; k < 4; k++) {
            t[k].setText(tx[k]); t[k].setTextColor(fg);
            t[k].setTextSize(TypedValue.COMPLEX_UNIT_SP, sz[k] * s);
        }
        t[3].setVisibility(p.getBoolean("date", true) ? TextView.VISIBLE : TextView.GONE);
    }

    @Override protected void onResume() { super.onResume(); h.post(r); ClockWidget.updateAll(this); }
    @Override protected void onPause() { super.onPause(); h.removeCallbacks(r); }
}
