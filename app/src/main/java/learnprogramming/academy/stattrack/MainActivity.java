package learnprogramming.academy.stattrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    public EditText summonerNameInput;
    public Spinner serverSpinner;
    public AppCompatButton loginButton, logoutButton, getFavoritesButton;
    private LoginInstanceDao loginInstanceDao;
    private LoginInstance loginInstance;
    private String username;
    private String password;
    private String userEmail;
    private static final int NOTIFICATION_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sets background programmatically to prevent picture squishing when scrolling is enabled
        this.getWindow().setBackgroundDrawableResource(R.drawable.background);

        // prevents landscape orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        hideSystemUI();

        //2 lines below are for purging the DB entries
        //UserDatabase.getInstance(this).userDao().nukeTable();
        //UserDatabase.getInstance(this).favoritePlayerDao().nukeTable();

        loginButton = findViewById(R.id.loginButton);
        logoutButton = findViewById(R.id.logoutButton);
        getFavoritesButton = findViewById(R.id.favorites_button);
        summonerNameInput = findViewById(R.id.summoner_name_input);
        serverSpinner = findViewById(R.id.server_spinner);

        loginInstanceDao = UserDatabase.getInstance(this).loginInstanceDao();
        loginInstance = loginInstanceDao.checkIfLoggedIn();

        // change button visibilities based on login status (logged in / out)
        if(loginInstance != null) {
            username = loginInstance.getUsername();
            password = loginInstance.getPassword();
            userEmail = loginInstance.getEmail();

            logoutButton.setVisibility(AppCompatButton.VISIBLE);
            loginButton.setVisibility(AppCompatButton.GONE);
            getFavoritesButton.setVisibility(AppCompatButton.VISIBLE);
        } else {
            logoutButton.setVisibility(AppCompatButton.GONE);
            getFavoritesButton.setVisibility(AppCompatButton.GONE);
            username = ""; // set to empty string to prevent crashed later
        }

        // used for daily reminder (random facts about the game)
        //https://docs.oracle.com/javase/7/docs/api/java/util/Calendar.html
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // if notification has passed, schedule it for tomorrow
        // notification will not show up again if app is not opened after the previous notification appeared
        if(Calendar.getInstance().after(calendar)){
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            //calendar.add(Calendar.SECOND, 10);
        }

        // intent for daily notification broadcast
        Intent intent = new Intent(MainActivity.this, NotificationBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), NOTIFICATION_ID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

        //setup spinner / dropdown menu
        ArrayAdapter<CharSequence> spinnerAdapter=ArrayAdapter.createFromResource(this, R.array.servers,
                android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        serverSpinner.setAdapter(spinnerAdapter);
    }

    public void showFavorites(View view){
        // if user is logged in
        if(username != null){
            Intent intent = new Intent(MainActivity.this, ShowFavoritePlayers.class);
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            startActivity(intent);
        }
        else{
            Toast.makeText(MainActivity.this, "Log in to see favorite accounts", Toast.LENGTH_LONG).show();
        }
    }

    public void getMatchHistory(View view){
        // if user is logged in will call match history with option to add to favorites
        if(!username.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, ShowMatches.class);
            intent.putExtra("summonerName", summonerNameInput.getText().toString());
            intent.putExtra("server", serverSpinner.getSelectedItem().toString());
            intent.putExtra("userEmail", userEmail);
            startActivity(intent);
        }
        // prevents user from adding player account to favorites if not logged in
        else{
            Intent intent = new Intent(MainActivity.this, ShowMatches.class);
            intent.putExtra("summonerName", summonerNameInput.getText().toString());
            intent.putExtra("server", serverSpinner.getSelectedItem().toString());
            intent.putExtra("userEmail", "");
            startActivity(intent);
        }
    }

    // moves to login / register fragments
    public void goToLogin(View view){
        Intent intent = new Intent(MainActivity.this, RegisterLoginFragActivity.class);
        startActivity(intent);
    }

    public void logout(View view){
        loginInstanceDao.logout();

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
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