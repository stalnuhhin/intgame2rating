package ee.stalnuhhin.intgame.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Team implements Serializable {
    private String id;
    private String name;
    private String city;
    private String ratingId;
    private final Player captain = new Player();
    private final List<Player> players = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public Player getCaptain() {
        return captain;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
