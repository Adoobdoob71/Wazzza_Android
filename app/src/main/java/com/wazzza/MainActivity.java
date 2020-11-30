package com.wazzza;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.checkbox.MaterialCheckBox;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getSharedPreferences("theme", 0).getInt("theme", R.style.AppThemeLight));
        setContentView(R.layout.activity_main);
        InitializeVariables();
        HandleBottomNavigaitonView();
    }

    private void HandleBottomNavigaitonView() {
        final NavOptions.Builder navBuilder =  new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.fragment_fade_enter).setExitAnim(R.anim.fragment_fade_exit);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        final NavController navController = navHostFragment.getNavController();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_screen:
                        navController.navigate(R.id.homeFragment, null, navBuilder.build());
                        break;
                    case R.id.search_screen:
                        navController.navigate(R.id.searchFragment, null, navBuilder.build());
                        break;
                    case R.id.contacts_screen:
                        navController.navigate(R.id.contactsFragment, null, navBuilder.build());
                        break;
                    case R.id.profile_screen:
                        navController.navigate(R.id.profileFragment, null, navBuilder.build());
                        break;
                }
                return true;
            }
        });
    }

    private void InitializeVariables() {
        bottomNavigationView = findViewById(R.id.main_activity_bottom_navigation_view);
    }


    @Override
    public void onBackPressed() {
    }

    public void animateBounce(View view){
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "scaleX", 1.2f, 1f);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "scaleY", 1.2f, 1);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animX, animY);
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new BounceInterpolator());
        animatorSet.start();
    }

    public void GoToSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

}