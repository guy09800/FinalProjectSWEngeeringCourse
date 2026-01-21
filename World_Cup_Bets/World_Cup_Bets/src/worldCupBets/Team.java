package worldCupBets;
import java.util.HashSet;
import java.util.Random;

public class Team {
	private static HashSet<Integer> usedTeamIDs = new HashSet<Integer>();
	private int teamID;
	private String teamName;

	public Team(int teamID, String teamName) {
		if (usedTeamIDs.contains(teamID)) {
			throw new IllegalArgumentException("Team ID already in use");
		}
		this.teamID = teamID;
		usedTeamIDs.add(teamID);
		this.teamName = teamName;
	}
	
	public Team(String teamName) {
		Random rand = new Random();
        int newID;
		do {
			newID = rand.nextInt(10000);
		} while (usedTeamIDs.contains(newID));
		this.teamID = newID;
		usedTeamIDs.add(newID);
		this.teamName = teamName;
	}
	
	public int getTeamID() {
		return teamID;
	}
	
	public String getTeamName() {
		return teamName;
	}
	
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	
	public String toString() {
		return teamName;
	}
	public boolean participatesInMatch(Match match) {
		return match.getHomeTeam().equals(this) || match.getAwayTeam().equals(this);
	}
}
