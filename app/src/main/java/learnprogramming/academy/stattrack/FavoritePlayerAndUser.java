package learnprogramming.academy.stattrack;

import androidx.room.Embedded;

public class FavoritePlayerAndUser {

    //class used for retrieving JOIN query information (email and a few other fields were left out)
    @Embedded
    FavoritePlayer favoritePlayer;
    String username;
    String password;
}
