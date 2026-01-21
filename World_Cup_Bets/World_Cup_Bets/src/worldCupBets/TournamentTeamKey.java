package worldCupBets;

import java.util.Objects;

public class TournamentTeamKey {
    private Tournament tournament;
    private Team team;

    public TournamentTeamKey(Tournament tournament, Team team) {
        this.tournament = tournament;
        this.team = team;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TournamentTeamKey)) return false;
        TournamentTeamKey key = (TournamentTeamKey) o;
        return Objects.equals(tournament, key.tournament) &&
               Objects.equals(team, key.team);
    }

    @Override
    public int hashCode() { //for the key in hashmap
        return Objects.hash(tournament, team);
    }
}
