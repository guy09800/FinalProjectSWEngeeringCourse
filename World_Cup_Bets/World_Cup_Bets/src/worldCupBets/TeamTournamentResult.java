package worldCupBets;

public class TeamTournamentResult {
    private Team team;
    private Tournament tournament;
    private String stage;

    public TeamTournamentResult(Team team, Tournament tournament, String stage) {
        this.team = team;
        this.tournament = tournament;
        this.stage = stage;
    }

    public TeamTournamentResult(Team team, Tournament tournament) {
        this.team = team;
        this.tournament = tournament;
        this.stage = "-";
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Team getTeam() {
        return team;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public String getStage() {
        return stage;
    }

    @Override
    public String toString() {
        return "TeamTournamentResult{" + "team=" + team.getTeamName() + ", tournament=" + tournament.getTournamentName() + ", stage='" + stage + '\'' +'}';
    }
    
    public void setFinalResult() {

        for (Match match : tournament.getMatches().values()) {
            if (!(match.getHomeTeam().equals(team) || match.getAwayTeam().equals(team)))
                continue;

            Team winner = match.getWinner();
            if (winner != null && !winner.equals(team)) {
                String matchStage = match.getStage();

                if (stage.equals("-")) {
                    stage = matchStage;
                } else {
                    if (isHigherStage(matchStage, stage)) {
                        stage = matchStage;
                    }
                }
            }
            if (match.getStage().equals("Final") && winner != null && winner.equals(team)) {
                stage = "Winner";
            }
        }
    }

    private boolean isHigherStage(String newStage, String currentStage) {
        return stageRank(newStage) > stageRank(currentStage);
    }

    private int stageRank(String stage) {
        switch (stage) {
        	case "-": return 0;
            case "Group": return 1;
            case "Round of 16": return 2;
            case "Quarterfinal": return 3;
            case "Semifinal": return 4;
            case "Final": return 5;
            case "Winner": return 6;
            default: return 0;
        }
    }
}

