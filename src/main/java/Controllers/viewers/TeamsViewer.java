package Controllers.viewers;

import javafx.beans.property.SimpleStringProperty;

public class TeamsViewer {

    private SimpleStringProperty teamName;

    public TeamsViewer(String teamName) {
        this.teamName = new SimpleStringProperty(teamName);
    }

    public String getTeamName() {
        return this.teamName.get();
    }

    public void setTeamName(String teamName) {
        this.teamName.set(teamName);
    }

    public SimpleStringProperty teamNameProperty() {
        return teamName;
    }
}
