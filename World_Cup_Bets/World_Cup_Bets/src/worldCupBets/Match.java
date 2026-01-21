package worldCupBets;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class Match {
	private static HashSet<Integer> usedMatchIDs = new HashSet<Integer>();
	private int matchID;
	private Team homeTeam;
	private Team awayTeam;
	private GregorianCalendar matchDate;
	private  int homeGoals;
	private  int awayGoals;
	private Boolean finished;
	private int points;
	private String stage;
	private HashMap<User,UserMatchBet> bets;
	
	public Match(int matchID, Team homeTeam, Team awayTeam, GregorianCalendar matchDate, String stage) {
		if (usedMatchIDs.contains(matchID)) {
			throw new IllegalArgumentException("Match ID already in use");
		}
		this.matchID = matchID;
		usedMatchIDs.add(matchID);
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.matchDate = matchDate;
		this.homeGoals = 0;
		this.awayGoals = 0;
		this.finished = false;
		this.points = 1;
		this.stage = stage;
		this.bets = new HashMap<User, UserMatchBet>();
	}
	
	public Match(int matchID, Team homeTeam, Team awayTeam, GregorianCalendar matchDate, String stage, int points) {
		if (usedMatchIDs.contains(matchID)) {
			throw new IllegalArgumentException("Match ID already in use");
		}
		this.matchID = matchID;
		usedMatchIDs.add(matchID);
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.matchDate = matchDate;
		this.homeGoals = 0;
		this.awayGoals = 0;
		this.finished = false;
		this.points = points;
		this.stage = stage;
		this.bets = new HashMap<User, UserMatchBet>();
	}
	
	public Match(Team homeTeam, Team awayTeam, GregorianCalendar matchDate, String stage) {
		Random rand = new Random();
		int matchID;
		do {
			matchID = rand.nextInt(10000);
		} while (usedMatchIDs.contains(matchID));
		this.matchID = matchID;
		usedMatchIDs.add(matchID);
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.matchDate = matchDate;
		this.homeGoals = 0;
		this.awayGoals = 0;
		this.finished = false;
		this.points = 0;
		this.stage = stage;
		this.bets = new HashMap<User, UserMatchBet>();
	}
	
	public Match(Team homeTeam, Team awayTeam, GregorianCalendar matchDate, String stage, int points) {
		Random rand = new Random();
		int matchID;
		do {
			matchID = rand.nextInt(10000);
		} while (usedMatchIDs.contains(matchID));
		this.matchID = matchID;
		usedMatchIDs.add(matchID);
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.matchDate = matchDate;
		this.homeGoals = 0;
		this.awayGoals = 0;
		this.finished = false;
		this.points = points;
		this.stage = stage;
		this.bets = new HashMap<User, UserMatchBet>();
	}
	
	public int getMatchID() {
		return matchID;
	}
	
	public Team getHomeTeam() {
		return homeTeam;
	}
	
	public Team getAwayTeam() {
		return awayTeam;
	}
	
	public GregorianCalendar getMatchDate() {
		return matchDate;
	}
	
	public int getHomeGoals() {
		return homeGoals;
	}
	
	public int getAwayGoals() {
		return awayGoals;
	}
	
	
	public Boolean getFinished() {
		return finished;
	}
	
	public int getPoints() {
		return points;
	}
	
	public String getStage() {
		return stage;
	}
	
	public HashMap<User, UserMatchBet> getBets() {
		return bets;
	}
	
	public void setHomeGoals(int homeGoals) {
		this.homeGoals = homeGoals;
	}
	
	public void setAwayGoals(int awayGoals) {
		this.awayGoals = awayGoals;
	}
	
	public void setFinished(Boolean finished) {
		this.finished = finished;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}
	
	public void setStage(String stage) {
		this.stage = stage;
	}
	
	public void addBet(User user, UserMatchBet bet) {
		this.bets.put(user, bet);
	}
	public String toString() {
		if (finished) {
			return homeTeam.toString() + " " + homeGoals + " - " + awayGoals + " " + awayTeam.toString() + " on " + matchDate.getTime().toString()+ " (Finished)";
		}
		else
		return homeTeam.toString() + " vs " + awayTeam.toString() + " on " + matchDate.getTime().toString();
	}
	
	public Team getWinner() {
		if (homeGoals > awayGoals)
			return homeTeam;
		else if (homeGoals < awayGoals)
			return awayTeam;
		return null;
	}
}
