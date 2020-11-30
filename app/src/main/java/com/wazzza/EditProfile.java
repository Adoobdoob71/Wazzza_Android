package com.wazzza;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EditProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSharedPreferences("theme", 0).getInt("theme", R.style.AppThemeLight));
        setContentView(R.layout.activity_edit_profile);
    }
}