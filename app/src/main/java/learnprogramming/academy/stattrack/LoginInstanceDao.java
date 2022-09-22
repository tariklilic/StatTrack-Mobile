package learnprogramming.academy.stattrack;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface LoginInstanceDao {

    // used upon successful login
    @Insert
    public void addLoginInstance(LoginInstance loginInstance);

    // used to change button visibilities based on whether the user is logged in or not
    @Query("SELECT * FROM LoginInstance LIMIT 1")
    LoginInstance checkIfLoggedIn();

    // used for logging out
    @Query("DELETE FROM LoginInstance")
    public void logout();
}
