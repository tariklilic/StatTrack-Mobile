package learnprogramming.academy.stattrack;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

// implements Parcelable so multiple matches can be placed into extras Bundle to prevent
// calling our API multiple times (faster + lower chance of our API key getting blocked)
public class Match implements Parcelable {

    private int id;
    private String championIcon;
    private double kda;
    private String matchResult;
    private String killsDeathsAssists;
    private int controlWardsPlaced;
    private int wardsKilled;
    private int wardsPlaced;
    private int damageDealt;
    private int damageTaken;
    private int minionsKilled;
    private ArrayList<String> items;
    private ArrayList<String> spells;

    // Parcelable function for reading from "Bundle" (Parcel)
    public Match(Parcel in){
        String[] data = new String[11];

        in.readStringArray(data);

        this.id = Integer.parseInt(data[0]);
        this.championIcon = data[1];
        this.kda = Double.parseDouble(data[2]);
        this.matchResult = data[3];
        this.killsDeathsAssists = data[4];
        this.controlWardsPlaced = Integer.parseInt(data[5]);
        this.wardsKilled = Integer.parseInt(data[6]);
        this.wardsPlaced = Integer.parseInt(data[7]);
        this.damageDealt = Integer.parseInt(data[8]);
        this.damageTaken = Integer.parseInt(data[9]);
        this.minionsKilled = Integer.parseInt(data[10]);
        this.items = in.readArrayList(Match.class.getClassLoader());
        this.spells = in.readArrayList(Match.class.getClassLoader());
    }

    // mandatory method for Parcelable interface. Implementation not important in our case
    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable function for writing to "Bundle" (Parcel)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(championIcon);
        parcel.writeDouble(kda);
        parcel.writeString(matchResult);
        parcel.writeString(killsDeathsAssists);
        parcel.writeInt(controlWardsPlaced);
        parcel.writeInt(wardsKilled);
        parcel.writeInt(wardsPlaced);
        parcel.writeInt(damageDealt);
        parcel.writeInt(damageTaken);
        parcel.writeInt(minionsKilled);
        parcel.writeList(items);
        parcel.writeList(spells);
    }

    // https://developer.android.com/reference/android/os/Parcelable.Creator
    // "Interface that must be implemented and provided as a public CREATOR field that generates
    // instances of your Parcelable class from a Parcel."
    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

    public ArrayList<String> getSpells() {
        return spells;
    }

    public void setSpells(ArrayList<String> spells) {
        this.spells = spells;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    public Match(int id, String championIcon, double kda, String matchResult, String killsDeathsAssists, int controlWardsPlaced,
                 int wardsKilled, int wardsPlaced, int damageDealt, int damageTaken, int minionsKilled, ArrayList<String> items,
                 ArrayList<String> spells) {
        this.id = id;
        this.championIcon = championIcon;
        this.kda = kda;
        this.matchResult = matchResult;
        this.killsDeathsAssists = killsDeathsAssists;
        this.controlWardsPlaced = controlWardsPlaced;
        this.wardsKilled = wardsKilled;
        this.wardsPlaced = wardsPlaced;
        this.damageDealt = damageDealt;
        this.damageTaken = damageTaken;
        this.minionsKilled = minionsKilled;
        this.items = items;
        this.spells = spells;
    }

    public String getChampionIcon() {
        return championIcon;
    }

    public void setChampionIcon(String championIcon) {
        this.championIcon = championIcon;
    }

    public double getKda() {
        return kda;
    }

    public void setKda(double kda) {
        this.kda = kda;
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    public String getKillsDeathsAssists() {
        return killsDeathsAssists;
    }

    public void setKillsDeathsAssists(String killsDeathsAssists) {
        this.killsDeathsAssists = killsDeathsAssists;
    }

    public int getControlWardsPlaced() {
        return controlWardsPlaced;
    }

    public void setControlWardsPlaced(int controlWardsPlaced) {
        this.controlWardsPlaced = controlWardsPlaced;
    }

    public int getWardsKilled() {
        return wardsKilled;
    }

    public void setWardsKilled(int wardsKilled) {
        this.wardsKilled = wardsKilled;
    }

    public int getWardsPlaced() {
        return wardsPlaced;
    }

    public void setWardsPlaced(int wardsPlaced) {
        this.wardsPlaced = wardsPlaced;
    }

    public int getDamageDealt() {
        return damageDealt;
    }

    public void setDamageDealt(int damageDealt) {
        this.damageDealt = damageDealt;
    }

    public int getDamageTaken() {
        return damageTaken;
    }

    public void setDamageTaken(int damageTaken) {
        this.damageTaken = damageTaken;
    }

    public int getMinionsKilled() {
        return minionsKilled;
    }

    public void setMinionsKilled(int minionsKilled) {
        this.minionsKilled = minionsKilled;
    }

}
