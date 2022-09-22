package learnprogramming.academy.stattrack;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ShowFavoritePlayers extends AppCompatActivity {

    // contains FavoritePlayers + info about user to which they belong
    private List<FavoritePlayerAndUser> favoritePlayerList;
    public ListView listView;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_favorite_players);

        // sets background programmatically to prevent picture squishing when scrolling is enabled
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        listView = findViewById(R.id.favorite_players_listview);
        hideSystemUI();

        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        password = extras.getString("password");

        FavoritePlayerDao favoritePlayerDao=UserDatabase.getInstance(this).favoritePlayerDao();
        favoritePlayerList = favoritePlayerDao.getFavoritesOfUser(extras.getString("username"), extras.getString("password"));
        if(favoritePlayerList.size() > 0) {
            setUpAdapter(favoritePlayerList);
        }
        else {
            Toast.makeText(this, "No favorites found", Toast.LENGTH_SHORT).show();
            finish();
        }

        // when favorite user is clicked (not the delete button), match history of said user is shown in ShowMatches activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                FavoritePlayerAndUser favoritePlayerParent = (FavoritePlayerAndUser) parent.getItemAtPosition(position);
                FavoritePlayer favoritePlayer = favoritePlayerParent.favoritePlayer;

                Intent intent = new Intent(ShowFavoritePlayers.this, ShowMatches.class);
                intent.putExtra("summonerName", favoritePlayer.getSummonerName());
                intent.putExtra("server", favoritePlayer.getServer());
                startActivity(intent);

            }
        });

    }

    // onClick function for delete button which removes selected FavoritePlayer from the database
    public void deleteFavoritePlayer(View view){

        // gets the user to which the favorite player belongs
        LoginInstanceDao loginInstanceDao = UserDatabase.getInstance(this).loginInstanceDao();
        LoginInstance loginInstance = loginInstanceDao.checkIfLoggedIn();

        if(loginInstance != null) {
            String userEmail = loginInstance.getEmail();

            FavoritePlayerDao favoritePlayerDao = UserDatabase.getInstance(view.getContext()).favoritePlayerDao();
            View parentView = (View) view.getParent();
            TextView summonerNameText = parentView.findViewById(R.id.summonerNameText);
            TextView serverText = parentView.findViewById(R.id.serverText);

            String summonerName = summonerNameText.getText().toString();
            String server = serverText.getText().toString();

            favoritePlayerDao.deleteFavoritePlayer(summonerName, server, userEmail);
            Intent intent = new Intent(this, ShowFavoritePlayers.class);
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            startActivity(intent);
            finish();
        }

        // if something fails when deleting favorite player (didn't occur yet and probably never will), return to MainActivity
        else{
            Toast.makeText(this, "Unable to delete favorite player", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            //prevents coming back to this activity using back button (backstack)
            finish();
        }
    }

    public void setUpAdapter(List<FavoritePlayerAndUser> favoritePlayerList){
        FavoritePlayerAdapter favoritePlayerAdapter = new FavoritePlayerAdapter(favoritePlayerList, this);
        listView.setAdapter(favoritePlayerAdapter);
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