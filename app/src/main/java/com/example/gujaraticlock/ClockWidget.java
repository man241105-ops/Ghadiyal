package com.example.gujaraticlock;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;
import java.util.Calendar;

public class ClockWidget extends AppWidgetProvider {
    static final String TICK = "com.example.gujaraticlock.TICK";

    @Override public void onUpdate(Context c, AppWidgetManager m, int[] ids) { updateAll(c); }

    @Override public void onReceive(Context c, Intent i) {
        super.onReceive(c, i);
        String a = i.getAction();
        if (a != null && !a.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) updateAll(c);
    }

    @Override public void onDisabled(Context c) {
        ((AlarmManager) c.getSystemService(Context.ALARM_SERVICE)).cancel(tickIntent(c));
    }

    static PendingIntent tickIntent(Context c) {
        Intent i = new Intent(c, ClockWidget.class).setAction(TICK);
        return PendingIntent.getBroadcast(c, 0, i, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
    }

    static void updateAll(Context c) {
        AppWidgetManager m = AppWidgetManager.getInstance(c);
        int[] ids = m.getAppWidgetIds(new ComponentName(c, ClockWidget.class));
        if (ids.length == 0) return;
        SharedPreferences p = c.getSharedPreferences("p", Context.MODE_PRIVATE);
        int bg = p.getInt("bg", 0xFFFFF7E6), fg = p.getInt("fg", 0xFF7A1F1F);
        int alpha = p.getInt("alpha", 255);
        float s = 0.6f + p.getInt("size", 50) / 100f;
        boolean showDate = p.getBoolean("date", true);
        Calendar cal = Calendar.getInstance();
        RemoteViews v = new RemoteViews(c.getPackageName(), R.layout.widget);
        v.setInt(R.id.bg, "setColorFilter", bg);
        v.setInt(R.id.bg, "setImageAlpha", alpha);
        int[] tv = {R.id.day, R.id.per, R.id.time, R.id.date};
        float[] sz = {22, 16, 44, 18};
        String[] tx = {Gu.day(cal), Gu.period(cal), Gu.time(cal), Gu.date(cal)};
        for (int k = 0; k < 4; k++) {
            v.setTextViewText(tv[k], tx[k]);
            v.setTextColor(tv[k], fg);
            v.setTextViewTextSize(tv[k], TypedValue.COMPLEX_UNIT_SP, sz[k] * s);
        }
        v.setViewVisibility(R.id.date, showDate ? View.VISIBLE : View.GONE);
        PendingIntent open = PendingIntent.getActivity(c, 1, new Intent(c, MainActivity.class), PendingIntent.FLAG_IMMUTABLE);
        v.setOnClickPendingIntent(R.id.root, open);
        m.updateAppWidget(ids, v);
        long next = (System.currentTimeMillis() / 60000 + 1) * 60000;
        ((AlarmManager) c.getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.RTC, next, tickIntent(c));
    }
}
