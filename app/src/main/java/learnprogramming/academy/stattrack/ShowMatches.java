package learnprogramming.academy.stattrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ShowMatches extends AppCompatActivity {
    private RequestQueue apiRequestQueue;
    private List<Match> matchList;
    public ListView listView;
    public static String isPlayingAudio;
    public AppCompatButton visitWebsiteButton;
    public TextView visitWebsiteText;
    public AppCompatButton addFavoriteButton;


    private String summonerName;
    private String server;
    private String userEmail;
    private String profileIconId;
    private static final String CHANNEL_ID = "notification_channel";
    private NotificationManager notifyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_matches);

        visitWebsiteButton = findViewById(R.id.visitWebsiteButton);
        visitWebsiteButton.setVisibility(AppCompatButton.INVISIBLE);
        visitWebsiteText = findViewById(R.id.visitWebsiteText);
        visitWebsiteText.setVisibility(TextView.INVISIBLE);
        addFavoriteButton = findViewById(R.id.add_favorite_button);
        addFavoriteButton.setVisibility(AppCompatButton.INVISIBLE);
        matchList = new ArrayList<>();
        listView = findViewById(R.id.match_listview);
        hideSystemUI();

        // needed for sending notification when match gets loaded
        createNotificationChannel();

        // used to prevent sound overlapping (mostly)
        isPlayingAudio = "false";

        // retrieves data upon screen rotation / minimize, etc, etc.
        if (savedInstanceState != null){
            matchList = savedInstanceState.getParcelableArrayList("match_list");
            server = savedInstanceState.getString("server");
            summonerName = savedInstanceState.getString("summonerName");
            userEmail = savedInstanceState.getString("userEmail");
            profileIconId = savedInstanceState.getString("profileIconId");
            setUpAdapter(matchList);
            visitWebsiteButton.setVisibility(AppCompatButton.VISIBLE);
            visitWebsiteText.setVisibility(TextView.VISIBLE);
            addFavoriteButton.setVisibility(AppCompatButton.VISIBLE);
        }

        // makes an API call to fetch match history if there is no saved data (activity opened for the first time)
        else {
            apiRequestQueue = Volley.newRequestQueue(this);

            Bundle extras = getIntent().getExtras();

            Toast.makeText(this, "Loading matches!", Toast.LENGTH_SHORT).show();

            userEmail = extras.getString("userEmail");
            summonerName = extras.getString("summonerName");
            server = extras.getString("server");
            String url = "https://stattrack.me/rest/summonersMobileAPI/" + summonerName + "/" + server;

            // fetches match history from API
            jsonParseMatches(url);
        }

        // removes background and sets background color to gray when matches finish loading
        View constraintLayout = findViewById(R.id.constraintLayoutShowMatches);
        constraintLayout.setBackground(null);
        constraintLayout.setBackgroundColor(Color.parseColor("#484c54"));
    }

    // uses implicit intent to send the user to our website upon button clicked
    public void visitWebsite(View view){
        String url = visitWebsiteButton.getTag().toString();

        Uri website = Uri.parse(url);
        Intent implicitIntent = new Intent(Intent.ACTION_VIEW, website);

        // if an app (browser) is able to handle the intent (open our website), start the activity
        if(implicitIntent.resolveActivity(getPackageManager()) != null){
            startActivity(implicitIntent);
        }
        // else throw an error
        else{
            Toast.makeText(ShowMatches.this, "Unable to open website",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void playVoicelineOnIconTap(View view) {

        // retrieves filename based on tag (similar to hint on TextView, just not visible) which was set to the Champion Image
        String audioFileName = String.valueOf(view.getTag());
        // gets Android ID for the audio file
        int resourceId = getResources().getIdentifier(audioFileName, "raw", this.getPackageName());
        // creates MediaPlayer which is used for starting / stopping sounds
        MediaPlayer mediaPlayer = MediaPlayer.create(this, resourceId);

        // plays voiceline if one isn't playing already (buggy on rotation due to onCompletion not finishing when the screen rotates)
        if (isPlayingAudio == "false") {
            isPlayingAudio = "true";
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isPlayingAudio = "false";
                    mp.reset();
                    mp.release(); // destroys the MediaPlayer to prevent MediaPlayer crashes
                }
            });
        }
        // doesn't play audio if there is some already playing and destroys the MediaPlayer to prevent MediaPlayer crashes
        else if(isPlayingAudio == "true"){
            mediaPlayer.release();
        }
    }

    // sends notification when matches finish loading
    public void sendNotification(String title, String text){
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder(title, text);
        notifyManager.notify(1, notifyBuilder.build());
    }

    // used for sending notification
    private NotificationCompat.Builder getNotificationBuilder(String title, String text){
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 1,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.background)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true);

        return notifyBuilder;
    }

    // makes sure the notification can be activated properly (it won't appear if there is not channel for it)
    public void createNotificationChannel(){
        notifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    "My notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("This is my notification!");

            notifyManager.createNotificationChannel(notificationChannel);
        }
    }

    // saves data upon screen rotation / minimize, etc, etc.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putString("profileIconId", profileIconId);
        savedInstanceState.putString("server", server);
        savedInstanceState.putString("summonerName", summonerName);
        savedInstanceState.putString("isPlayingAudio", isPlayingAudio);
        savedInstanceState.putParcelableArrayList("match_list", (ArrayList<? extends Parcelable>) matchList);
    }

    // sets up each match into the ListView
    public void setUpAdapter(List<Match> matchList){
        MatchAdapter matchAdapter = new MatchAdapter(matchList, this);
        listView.setAdapter(matchAdapter);
    }

    // main method for making the API request to our website which grabs information for displaying
    public void jsonParseMatches(String url){
        //if it's a JSON array, we will need a JsonArrayRequest
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject responseJSON) {

                JSONArray jsonArray = null;
                try {
                    profileIconId = responseJSON.getString("profileIconId");
                    jsonArray = responseJSON.getJSONArray("matches");

                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject matchInfo = jsonArray.getJSONObject(i);

                        ArrayList<String> items = new ArrayList<>();
                        // adds "i" to itemIds to match drawable names (android studio prevents numbers for image names,
                        // letter on position 0 required)
                        items.add("i" + matchInfo.getString("item0"));
                        items.add("i" + matchInfo.getString("item1"));
                        items.add("i" + matchInfo.getString("item2"));
                        items.add("i" + matchInfo.getString("item3"));
                        items.add("i" + matchInfo.getString("item4"));
                        items.add("i" + matchInfo.getString("item5"));
                        items.add("i" + matchInfo.getString("item6"));

                        ArrayList<String> spells = new ArrayList<>();
                        // same principle for summoner spells as for items (i = item, s = spell)
                        spells.add("s" + matchInfo.getString("summonerSpell1Id"));
                        spells.add("s" + matchInfo.getString("summonerSpell2Id"));

                        Match match = new Match(i, matchInfo.getString("championIcon"), matchInfo.getDouble("kda"),
                                matchInfo.getString("matchResult"), matchInfo.getString("killsDeathsAssists"),
                                matchInfo.getInt("controlWardsPlaced"), matchInfo.getInt("wardsKilled"),
                                matchInfo.getInt("wardsPlaced"), matchInfo.getInt("damageDealt"),
                                matchInfo.getInt("damageTaken"), matchInfo.getInt("minionsKilled"), items, spells);
                        matchList.add(match);
                    }

                    setUpAdapter(matchList);

                    // when matches finish loading, show the text and buttons on top (enhance user experience)
                    visitWebsiteButton.setVisibility(AppCompatButton.VISIBLE);
                    visitWebsiteText.setVisibility(TextView.VISIBLE);
                    addFavoriteButton.setVisibility(AppCompatButton.VISIBLE);

                    // send notificaitons when matches finish loading
                    sendNotification("Match history", "Match history successfully loaded");
                } catch (JSONException e) {
                    // ErrorListener below does all the work, this just needs to be here due to try-catch
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Writer writer = new StringWriter();

                // prints errors to Logcat
                error.printStackTrace(new PrintWriter(writer));

                // grabs part of error message
                String errorMessage = writer.toString();

                // checks for 403 error (API key expired)
                if (errorMessage.contains("AuthFailureError"))
                    Toast.makeText(ShowMatches.this, "API key needs to be changed",
                            Toast.LENGTH_LONG).show();

                // checks for 404 error (User Not Found unless original website changes route)
                else if(errorMessage.contains("ClientError"))
                    Toast.makeText(ShowMatches.this, "No such user found",
                            Toast.LENGTH_LONG).show();

                    sendNotification("Match history", "Match history could not be loaded");
                    error.printStackTrace();
                    Intent intent = new Intent(ShowMatches.this, MainActivity.class);
                    startActivity(intent);
            }
        });
        // add request to queue, ready for executing
        apiRequestQueue.add(request);
    }

    // add currently searched player to user's favorites
    public void addFavorite(View view){

        // if user is logged in, start adding to fav process
        if(!userEmail.isEmpty()){
            FavoritePlayer favoritePlayer = new FavoritePlayer(summonerName, server, userEmail, profileIconId);

            //if selected player isn't already a favorite, add to favorites
            if (!UserDatabase.getInstance(this).favoritePlayerDao() .exists(favoritePlayer.getSummonerName(), favoritePlayer.getServer(), userEmail)) {
                        UserDatabase.getInstance(this).favoritePlayerDao().addFavoritePlayer(favoritePlayer);
                        Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            } // selected player is already a favorite
            else {
                Toast.makeText(this, "This player was already added", Toast.LENGTH_SHORT).show();
            }
        }
        // user isn't logged in
        else{
            Toast.makeText(this, "Log in to add favorites", Toast.LENGTH_SHORT).show();
        }
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