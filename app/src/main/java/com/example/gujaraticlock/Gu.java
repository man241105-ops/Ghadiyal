package com.example.gujaraticlock;
import java.util.Calendar;
import java.util.Locale;
final class Gu {
    static final String[] D = {"રવિવાર","સોમવાર","મંગળવાર","બુધવાર","ગુરુવાર","શુક્રવાર","શનિવાર"};
    static final String[] M = {"જાન્યુઆરી","ફેબ્રુઆરી","માર્ચ","એપ્રિલ","મે","જૂન","જુલાઈ","ઓગસ્ટ","સપ્ટેમ્બર","ઓક્ટોબર","નવેમ્બર","ડિસેમ્બર"};
    static String gd(String s) {
        StringBuilder b = new StringBuilder();
        for (char ch : s.toCharArray()) b.append(ch >= '0' && ch <= '9' ? (char) ('\u0AE6' + (ch - '0')) : ch);
        return b.toString();
    }
    static String day(Calendar c) { return D[c.get(Calendar.DAY_OF_WEEK) - 1]; }
    static String period(Calendar c) {
        int h = c.get(Calendar.HOUR_OF_DAY);
        return h >= 4 && h < 12 ? "સવારે" : h < 16 && h >= 12 ? "બપોરે" : h >= 16 && h < 20 ? "સાંજે" : "રાત્રે";
    }
    static String time(Calendar c) {
        int h = c.get(Calendar.HOUR); if (h == 0) h = 12;
        return gd(h + ":" + String.format(Locale.US, "%02d", c.get(Calendar.MINUTE)));
    }
    static String date(Calendar c) {
        return gd("" + c.get(Calendar.DAY_OF_MONTH)) + " " + M[c.get(Calendar.MONTH)] + " " + gd("" + c.get(Calendar.YEAR));
    }
}
