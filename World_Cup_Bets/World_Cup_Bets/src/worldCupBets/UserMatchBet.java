package worldCupBets;
import java.util.GregorianCalendar;

public class UserMatchBet {
	private GregorianCalendar betDate;
	private int predHomeGoals;
	private int predAwayGoals;
	private int predWinner; // 1: home win, 0: draw, -1: away win
	private int difference; // predicted goal difference (home - away)
	
	public UserMatchBet(GregorianCalendar betDate, int predHomeGoals, int predAwayGoals) {
		this.betDate = betDate;
		this.predHomeGoals = predHomeGoals;
		this.predAwayGoals = predAwayGoals;
		this.difference = predHomeGoals - predAwayGoals;
		if (predHomeGoals > predAwayGoals)
			this.predWinner = 1;
		else if (predHomeGoals < predAwayGoals)
			this.predWinner = -1;
		else
			this.predWinner = 0;
	}
	
	public GregorianCalendar getBetDate() {
		return betDate;
	}
	
	public int getPredHomeGoals() {
		return predHomeGoals;
	}
	
	public int getPredAwayGoals() {
		return predAwayGoals;
	}
	
	public int isPredWinner() {
		return predWinner;
	}
	
	public int getDifference() {
		return difference;
	}
	
	public void setPredHomeGoals(int predHomeGoals) {
		this.predHomeGoals = predHomeGoals;
	}
	
	public void setPredAwayGoals(int predAwayGoals) {
		this.predAwayGoals = predAwayGoals;
	}
	
	public void setPredWinner(int predWinner) {
		this.predWinner = predWinner;
	}
	
	public int getPredWinner() {
		return predWinner;
	}
	
	public void setDifference(int difference) {
		this.difference = difference;
	}
	
	public void setBetDate(GregorianCalendar betDate) {
		this.betDate = betDate;
	}
	
	public int getBetResult(int homeGoals, int awayGoals, int points) {
		int actualDifference = homeGoals - awayGoals;
		
		int c = 0;
		if (actualDifference == difference)
			c = c+2; //2 points for correct goal difference
		if (homeGoals == predHomeGoals &&  awayGoals == predAwayGoals)
            c = c+3; //3 points for exact score

		if ((homeGoals > awayGoals && predWinner == 1) ||
            (homeGoals < awayGoals && predWinner == -1) ||
            (homeGoals == awayGoals && predWinner == 0))
            c++; //1 point for correct outcome
		
		return c*points;
    }
	
	public String toString() {
		return "Predicted Score: " + predHomeGoals + " - " + predAwayGoals + " | Predicted Winner: "
				+ (predWinner == 1 ? "Home" : (predWinner == -1 ? "Away" : "Draw")) + " | Goal Difference: "
				+ difference + " | Bet Date: " + betDate.getTime().toString();
	}
	
}
