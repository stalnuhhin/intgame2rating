package ee.stalnuhhin.intgame.models;

import org.json.simple.JSONArray;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private String surname;
    private String patronym;
    private String ratingId;
    private JSONArray possiblePlayers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronym() {
        return patronym;
    }

    public void setPatronym(String patronym) {
        this.patronym = patronym;
    }

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public JSONArray getPossiblePlayers() {
        return possiblePlayers;
    }

    public void setPossiblePlayers(JSONArray possiblePlayers) {
        this.possiblePlayers = possiblePlayers;
    }
}
