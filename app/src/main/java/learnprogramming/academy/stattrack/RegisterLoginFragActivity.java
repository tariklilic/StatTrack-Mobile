package learnprogramming.academy.stattrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class RegisterLoginFragActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login_frag);

        // prevents landscape orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // sets background programmatically to prevent picture squishing
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        hideSystemUI();

        bottomNavigationView = findViewById(R.id.bottom_navigation_bar);
        viewPager = findViewById(R.id.fragment_container);

        bottomNavigationView.setOnItemSelectedListener(onItemSelectedListener);
        setUpAdapter(viewPager);
    }

    // adds fragments to ViewPager2
    public void setUpAdapter(ViewPager2 viewPager){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPagerAdapter.addFragment(new LoginFragment());
        viewPagerAdapter.addFragment(new RegisterFragment());

        // prevents the user from changing which fragment is selected by scrolling left / right
        viewPager.setUserInputEnabled(false);

        viewPager.setAdapter(viewPagerAdapter);
    }

    // changes fragments based on navigation item which is clicked
    //
    public  BottomNavigationView.OnItemSelectedListener onItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()) {
                case (R.id.nav_login):
                    viewPager.setCurrentItem(0);
                    return true;
                case (R.id.nav_register):
                    viewPager.setCurrentItem(1);
                    return true;
            }
            return false;
        }
    };
    public void hideSystemUI() {
        View decorView = this.getWindow().getDecorView();
        this.getSupportActionBar().hide();
        int uiOptions = decorView.getSystemUiVisibility();
        int newUiOptions = uiOptions;
        newUiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE;
        newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(newUiOptions);
    }
}