package com.svalero.todolist.preferences;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.svalero.todolist.R;

public class PreferencesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_screen, rootKey);
    }
}
