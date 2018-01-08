package com.example.bironu.irremocon.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import com.example.bironu.irremocon.R;


/**
 * Sipのアカウント設定
 */
public class IrRemoconPreferences {

    private final SharedPreferences mPref;
    private final Resources mRes;
    private static final Object mLock = new Object();

    public IrRemoconPreferences(Context context)
    {
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        mRes = context.getResources();
    }

    public void setRtpPort(int port)
    {
        synchronized (mLock) {
//            SharedPreferences.Editor editor = mPref.edit();
//            editor.putString(mRes.getString(R.string.pref_key_rtp_port), String.valueOf(port));
//            editor.commit();
        }
    }

    public int getRtpPort()
    {
        synchronized (mLock) {
//          return Integer.parseInt(mPref.getString(mRes.getString(R.string.pref_key_rtp_port), mRes.getString(R.string.pref_default_value_rtp_port)));
            return 0;
        }
    }
}
