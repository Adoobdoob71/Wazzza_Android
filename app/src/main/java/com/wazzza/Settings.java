package com.wazzza;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.material.appbar.MaterialToolbar;

public class Settings extends AppCompatActivity {

    MaterialToolbar toolbar;
    Spinner themeSpinner;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean initialClickThemeSpinner = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSharedPreferences("theme", 0).getInt("theme", R.style.AppThemeLight));
        setContentView(R.layout.activity_settings);

        themeSpinner = findViewById(R.id.settings_activity_theme_spinner);
        toolbar = findViewById(R.id.settings_toolbar);
        sharedPreferences = this.getSharedPreferences("theme", 0);
        editor = sharedPreferences.edit();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (initialClickThemeSpinner) {
                    initialClickThemeSpinner = false;
                    return;
                }
                onSpinnerItemClick(adapterView, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.themes, R.layout.support_simple_spinner_dropdown_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        themeSpinner.setAdapter(arrayAdapter);
        defaultSelectThemeSpinner();

    }

    public void defaultSelectThemeSpinner(){
        switch (getSharedPreferences("theme", 0).getInt("theme", R.style.AppThemeLight)) {
            case R.style.AppThemeLight:
                themeSpinner.setSelection(0);
                break;
            case R.style.AppThemeDim:
                themeSpinner.setSelection(1);
                break;
            case R.style.AppThemeDark:
                themeSpinner.setSelection(2);
                break;
        }
    }

    public void onSpinnerItemClick(AdapterView<?> adapterView, int i) {
        String themeString = adapterView.getAdapter().getItem(i).toString();
        switch (themeString) {
            case "Light":
                editor.putInt("theme", R.style.AppThemeLight);
                break;
            case "Dim":
                editor.putInt("theme", R.style.AppThemeDim);
                break;
            case "Dark":
                editor.putInt("theme", R.style.AppThemeDark);
                break;
            default:
                return;
        }
        editor.apply();
        TaskStackBuilder.create(this)
                .addNextIntent(new Intent(this, MainActivity.class))
                .addNextIntent(getIntent())
                .startActivities();
    }

    public void GoToEditProfile(View view) {
        Intent intent = new Intent(this, EditProfile.class);
        startActivity(intent);
    }
}