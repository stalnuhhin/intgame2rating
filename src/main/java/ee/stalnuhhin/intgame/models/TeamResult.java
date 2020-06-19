package ee.stalnuhhin.intgame.models;

import org.json.simple.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TeamResult implements Serializable {
    private final Team team = new Team();
    private JSONArray possibleTeams;

    private final List<Long> takenQuestions = new ArrayList<>();
    private boolean played;
    private int questionsCount;

    public Team getTeam() {
        return team;
    }

    public JSONArray getPossibleTeams() {
        return possibleTeams;
    }

    public void setPossibleTeams(JSONArray possibleTeams) {
        this.possibleTeams = possibleTeams;
    }

    public List<Long> getTakenQuestions() {
        return takenQuestions;
    }

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public int getQuestionsCount() {
        return questionsCount;
    }

    public void setQuestionsCount(int questionsCount) {
        this.questionsCount = questionsCount;
    }
}
