package model;

public class Fixture {

    private Team homeTeam;
    private Team awayTeam;
    private String date;
    private CompetitionEnum competitionEnum;

    public Fixture(Team homeTeam, Team awayTeam, String date) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.date = date;
    }

    public Fixture() { }

    public CompetitionEnum getCompetitionEnum() {
        return competitionEnum;
    }

    public void setCompetitionEnum(CompetitionEnum competitionEnum) {
        this.competitionEnum = competitionEnum;
    }

    public Team getHomeTeam() {
        return this.homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return this.awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
