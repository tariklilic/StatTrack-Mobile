package learnprogramming.academy.stattrack;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// class used for specifying whether a person is logged in. Automatically logs in user on app start
// if the user didn't log out before closing the app
@Entity
public class LoginInstance {
    private String username;
    private String password;
    @NonNull
    @PrimaryKey
    private String email;

    public LoginInstance(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
