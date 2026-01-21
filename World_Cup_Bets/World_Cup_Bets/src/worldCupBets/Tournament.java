package worldCupBets;
import java.util.Random;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;

public class Tournament {
	private static HashSet<Integer> usedTournamentIDs = new HashSet<Integer>();
	private int tournamentID;
	private String tournamentName;
	private Team winner;
	private GregorianCalendar startDate;
	private GregorianCalendar endDate;
	private HashMap<Integer, Match> matches;
	private HashMap<Integer, Team> teams;
	private HashMap<String, UserTournamentBet> UserTournamentBets;
	
	public Tournament(int tournamentID, String tournamentName, GregorianCalendar startDate, GregorianCalendar endDate) {
		this.tournamentID = tournamentID;
		this.tournamentName = tournamentName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.matches = new HashMap<Integer, Match>();
		this.teams = new HashMap<Integer, Team>();
		this.UserTournamentBets = new HashMap<String, UserTournamentBet>();
	}
	
	public Tournament(String tournamentName, GregorianCalendar startDate, GregorianCalendar endDate) {
		Random rand = new Random();
		int newID;
		do {
			newID = rand.nextInt(10000);
		} while (usedTournamentIDs.contains(newID));
		this.tournamentID = newID;
		usedTournamentIDs.add(newID);
		this.tournamentName = tournamentName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.matches = new HashMap<Integer, Match>();
		this.teams = new HashMap<Integer, Team>();
		this.UserTournamentBets = new HashMap<String, UserTournamentBet>();
	}
	
	public int getTournamentID() {
		return tournamentID;
	}
	
	public String getTournamentName() {
		return tournamentName;
	}
	
	public Team getWinner() {
		return winner;
	}
	
	public GregorianCalendar getStartDate() {
		return startDate;
	}
	
	public GregorianCalendar getEndDate() {
		return endDate;
	}
	
	public HashMap<Integer, Match> getMatches() {
		return matches;
	}
	
	public HashMap<Integer, Team> getTeams() {
		return teams;
	}
	
	public HashMap<String, UserTournamentBet> getUserTournamentBets() {
		return UserTournamentBets;
	}
	public void setDatetuehs(GregorianCalendar startDate, GregorianCalendar endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public void setWinner(Team winner) {
		this.winner = winner;
		updateUserTournamentBetsPointForWinner();
	}

	public void putMatch(Match match) {
		this.matches.put(match.getMatchID(), match);
	}

	public void updateUserPointsAfterMatchFinish(Match match) {
		for (User user : match.getBets().keySet()) {
			UserMatchBet bet = match.getBets().get(user);
			int points = bet.getBetResult(match.getHomeGoals(), match.getAwayGoals(), match.getPoints());
			this.getUserTournamentBets().get(user.getEmail()).updateUserPoints(points);

		}
	}

	public void updateUserTournamentBetsPointForWinner() {
		for (UserTournamentBet tBet : UserTournamentBets.values()) {
			if (tBet.getPredictedWinner() != null && tBet.getPredictedWinner().equals(winner)) {
				tBet.updateUserPoints(10);
			}
		}
	}
	
	public String toString() {
		return tournamentName;
	}
}
