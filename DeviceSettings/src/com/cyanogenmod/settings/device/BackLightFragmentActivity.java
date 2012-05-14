/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanogenmod.settings.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.cyanogenmod.settings.device.R;

public class BackLightFragmentActivity extends PreferenceFragment {

    private static final String PREF_ENABLED = "1";
    private static final String TAG = "GalaxyS2Parts_General";

    private static final String FILE_BLN_TOGGLE = "/sys/class/misc/backlightnotification/enabled";
	private static final String FILE_BREATHING_TOGGLE = "/sys/class/misc/backlightnotification/breathing_enabled";
    private static final String FILE_BLINKING_TOGGLE = "/sys/class/misc/backlightnotification/blinking_enabled";
	private static final String FILE_LEDFADE_TOGGLE = "/sys/class/misc/backlightnotification/led_fadeout";
    
    private TouchKeyBacklightTimeout mTouchKeyBacklightTimeout;
    private BLNTimeout mBLNTimeout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.backlight_preferences);

 		mTouchKeyBacklightTimeout = (TouchKeyBacklightTimeout) findPreference(DeviceSettings.KEY_BACKLIGHT_TIMEOUT);
        mTouchKeyBacklightTimeout.setEnabled(mTouchKeyBacklightTimeout.isSupported());

		mBLNTimeout = (BLNTimeout) findPreference(DeviceSettings.KEY_BLN_TIMEOUT);
        mBLNTimeout.setEnabled(mBLNTimeout.isSupported());
     }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        String boxValue;
        String key = preference.getKey();

        Log.w(TAG, "key: " + key);

        if (key.compareTo(DeviceSettings.KEY_BLN_LIGHT) == 0) {
            boxValue = (((CheckBoxPreference)preference).isChecked() ? "1" : "0");
            Utils.writeValue(FILE_BLN_TOGGLE, boxValue);
        } else if (key.compareTo(DeviceSettings.KEY_BLN_BREATHE) == 0) {
            // when calibration data utilization is disablen and enabled back,
            // calibration is done at the same time by driver
            boxValue = (((CheckBoxPreference)preference).isChecked() ? "1" : "0");
            Utils.writeValue(FILE_BREATHING_TOGGLE, boxValue);
        } else if (key.compareTo(DeviceSettings.KEY_BLN_BLINK) == 0) {
            boxValue = (((CheckBoxPreference)preference).isChecked() ? "1" : "0");
            Utils.writeValue(FILE_BLINKING_TOGGLE, boxValue);
        }
			else if (key.compareTo(DeviceSettings.KEY_BLN_FADE) == 0) {
            boxValue = (((CheckBoxPreference)preference).isChecked() ? "1" : "0");
            Utils.writeValue(FILE_LEDFADE_TOGGLE, boxValue);
        }

        return true;
    }

    public static boolean isSupported(String FILE) {
        return Utils.fileExists(FILE);
    }

    public static void restore(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Utils.writeValue(FILE_BLN_TOGGLE, sharedPrefs.getString(DeviceSettings.KEY_BLN_LIGHT, "1"));
		Utils.writeValue(FILE_BREATHING_TOGGLE, sharedPrefs.getString(DeviceSettings.KEY_BLN_BREATHE, "1"));
		Utils.writeValue(FILE_BLINKING_TOGGLE, sharedPrefs.getString(DeviceSettings.KEY_BLN_BLINK, "1"));
		Utils.writeValue(FILE_LEDFADE_TOGGLE, sharedPrefs.getString(DeviceSettings.KEY_BLN_FADE, "1"));
    }
}

