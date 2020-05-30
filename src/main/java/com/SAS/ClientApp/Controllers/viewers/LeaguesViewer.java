package com.SAS.ClientApp.Controllers.viewers;

import javafx.beans.property.SimpleStringProperty;

public class LeaguesViewer {

    private SimpleStringProperty leagueName;

    public LeaguesViewer(String leagueName) {
        this.leagueName = new SimpleStringProperty(leagueName);
    }

    public String getLeagueName() {
        return this.leagueName.get();
    }

    public void setLeagueName(String teamName) {
        this.leagueName.set(teamName);
    }

    public SimpleStringProperty leagueNameProperty() {
        return leagueName;
    }
}