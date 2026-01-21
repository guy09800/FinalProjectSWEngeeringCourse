package worldCupBets;

public class UserTournamentBet {
	private Team predictedWinner;
	private User user;
	private Tournament tournament;
	private int userPoints;
	
	public UserTournamentBet(User user, Tournament tournament, Team predictedWinner, int userPoints) {
		this.user = user;
		this.tournament = tournament;
		this.predictedWinner = predictedWinner;
		this.userPoints = userPoints;
	}
	
	public UserTournamentBet(User user, Tournament tournament, Team predictedWinner) {
		this.user = user;
		this.tournament = tournament;
		this.predictedWinner = predictedWinner;
		this.userPoints = 0;
	}
	
	public UserTournamentBet(User user, Tournament tournament) {
		this.user = user;
		this.tournament = tournament;
		this.userPoints = 0;
	}
	
	public int getUserPoints() {
		return userPoints;
	}
	
	public void setUserPoints(int userPoints) {
		this.userPoints = userPoints;
	}
	
	public Team getPredictedWinner() {
		return predictedWinner;
	}
	
	public void setPredictedWinner(Team predictedWinner) {
		this.predictedWinner = predictedWinner;
	}
	
	public User getUser() {
		return user;
	}
	
	public Tournament getTournament() {
		return tournament;
	}
	
	public void updateUserPoints(int points) {
		this.userPoints += points;
	}
	
	public void updatePredictedWinner(Team newPredictedWinner) {
		this.predictedWinner = newPredictedWinner;
	}
	
	public String toString() {
		return "User: " + user.getName() + ", Tournament: " + tournament.getTournamentName() + ", Predicted Winner: "
				+ predictedWinner.getTeamName() + ", Points: " + userPoints;
	}

}
