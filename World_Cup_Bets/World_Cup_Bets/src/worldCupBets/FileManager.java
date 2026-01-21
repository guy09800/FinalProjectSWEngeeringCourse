package worldCupBets;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class FileManager {
	
    private final String usersFile;
    private final String teamsFile;
    private final String tournamentsFile;
    private final String tournamentTeamsFile;
    private final String matchesFile;
    private final String userTournamentBetsFile;
    private final String betsFile;
    private final String teamResultsFile;
    
    public FileManager(String usersFile, String teamsFile, String tournamentsFile, String tournamentTeamsFile, String matchesFile, String userTournamentBetsFile, String betsFile, String teamResultsFile) {
        this.usersFile = usersFile;
        this.teamsFile = teamsFile;
        this.tournamentsFile = tournamentsFile;
        this.tournamentTeamsFile = tournamentTeamsFile;
        this.matchesFile = matchesFile;
        this.userTournamentBetsFile = userTournamentBetsFile;
        this.betsFile = betsFile;
        this.teamResultsFile = teamResultsFile;
    }
	
	public MySystem loadAllData() {
		
		MySystem mySystem = new MySystem(this);
		
		readUsersFromFile(mySystem.getUsers());
		readTeamsFromFile(mySystem.getTeams());
		readTournamentsFromFile(mySystem.getTournaments(), mySystem.getTeams());
		readTournamentsTeamsFromFile(mySystem.getTournaments(), mySystem.getTeams());
		readMatchesFromFile(mySystem.getTournaments(), mySystem.getTeams(), mySystem.getUsers());
		readUserTournamentBetsFromFile(mySystem.getUsers(), mySystem.getTournaments(), mySystem.getTeams());
		readBetsFromFile(mySystem.getUsers(), mySystem.getTournaments());
		readTeamsResultsFromFile(mySystem.getTeamResults(), mySystem.getTournaments(), mySystem.getTeams());
		
		return mySystem;
	}

	public void saveAllData(HashMap<String, User> users, HashMap<Integer, Tournament> tournaments, HashMap<Integer, Team> teams, HashMap<TournamentTeamKey, TeamTournamentResult> teamsResults) {

		writeUsersToFile(users);
		writeTeamsToFile(teams);
		writeTournamentsToFile(tournaments);
		writeTournamentsTeamsToFile(tournaments);
		writeMatchesToFile(tournaments);
		writeUserTournamentBetsToFile(tournaments);
		writeBetsToFile(tournaments);
		writeTeamsResultsToFile(teamsResults);
	}
	
	//read methods:
    private void readUsersFromFile(HashMap<String, User> users) {
        File file = new File(usersFile);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 7) continue;

                String name = parts[0];
                String email = parts[1];
                String password = parts[2];
                int day = Integer.parseInt(parts[3]);
                int month = Integer.parseInt(parts[4]) - 1;
                int year = Integer.parseInt(parts[5]);
                boolean isAdmin = Boolean.parseBoolean(parts[6]);
                GregorianCalendar dob = new GregorianCalendar(year, month, day);

                User user;
                if (isAdmin) user = new User(name, email, password, dob, true);
                else user = new User(name, email, password, dob);

                users.put(email, user);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading users from file", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void readTeamsFromFile(HashMap<Integer, Team> teams) {
        File file = new File(teamsFile);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 2) continue;

                int teamID = Integer.parseInt(parts[0]);
                String teamName = parts[1];

                Team team = new Team(teamID, teamName);
                teams.put(teamID, team);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading teams from file", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void readTournamentsFromFile(HashMap<Integer, Tournament> tournaments, HashMap<Integer, Team> teams) {
        File file = new File(tournamentsFile);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 9) continue;

                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                int sd = Integer.parseInt(parts[2]);
                int sm = Integer.parseInt(parts[3]) - 1;
                int sy = Integer.parseInt(parts[4]);
                int ed = Integer.parseInt(parts[5]);
                int em = Integer.parseInt(parts[6]) - 1;
                int ey = Integer.parseInt(parts[7]);
                int winnerID = Integer.parseInt(parts[8]);

                GregorianCalendar startDate = new GregorianCalendar(sy, sm, sd);
                GregorianCalendar endDate = new GregorianCalendar(ey, em, ed);

                Tournament t = new Tournament(id, name, startDate, endDate);
                if (winnerID != -1 && teams.containsKey(winnerID)) t.setWinner(teams.get(winnerID));

                tournaments.put(id, t);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading tournaments from file", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void readTournamentsTeamsFromFile(HashMap<Integer, Tournament> tournaments, HashMap<Integer, Team> teams) {
        File file = new File(tournamentTeamsFile);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 2) continue;

                int tournamentID = Integer.parseInt(parts[0]);
                int teamID = Integer.parseInt(parts[1]);

                if (tournaments.containsKey(tournamentID) && teams.containsKey(teamID)) {
                    tournaments.get(tournamentID).getTeams().put(teamID, teams.get(teamID));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading tournament teams from file", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void readMatchesFromFile(HashMap<Integer, Tournament> tournaments, HashMap<Integer, Team> teams, HashMap<String, User> users) {
        File file = new File(matchesFile);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 12) continue;

                int matchID = Integer.parseInt(parts[0]);
                int homeTeamID = Integer.parseInt(parts[1]);
                int awayTeamID = Integer.parseInt(parts[2]);
                int day = Integer.parseInt(parts[3]);
                int month = Integer.parseInt(parts[4]) - 1;
                int year = Integer.parseInt(parts[5]);
                String stage = parts[6];
                boolean finished = Boolean.parseBoolean(parts[7]);
                int homeGoals = Integer.parseInt(parts[8]);
                int awayGoals = Integer.parseInt(parts[9]);
                int points = Integer.parseInt(parts[10]);
                int tournamentID = Integer.parseInt(parts[11]);

                Team homeTeam = teams.get(homeTeamID);
                Team awayTeam = teams.get(awayTeamID);
                Tournament tournament = tournaments.get(tournamentID);

                if (homeTeam == null || awayTeam == null || tournament == null) continue;

                GregorianCalendar matchDate = new GregorianCalendar(year, month, day);
                Match match = new Match(matchID, homeTeam, awayTeam, matchDate, stage, points);
                match.setFinished(finished);
                match.setHomeGoals(homeGoals);
                match.setAwayGoals(awayGoals);

                tournament.getMatches().put(matchID, match);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading matches from file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void readUserTournamentBetsFromFile(HashMap<String, User> users, HashMap<Integer, Tournament> tournaments, HashMap<Integer, Team> teams) {
        File file = new File(userTournamentBetsFile);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 4) continue;

                String userEmail = parts[0];
                int tournamentID = Integer.parseInt(parts[1]);
                int predictedWinnerID = Integer.parseInt(parts[2]);
                int points = Integer.parseInt(parts[3]);

                User user = users.get(userEmail);
                Tournament tournament = tournaments.get(tournamentID);
                Team predictedWinner = teams.get(predictedWinnerID);

                if (user == null || tournament == null || predictedWinner == null) continue;

                UserTournamentBet bet = new UserTournamentBet(user, tournament, predictedWinner);
                bet.setUserPoints(points);

                tournament.getUserTournamentBets().put(userEmail, bet);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading tournament bets: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void readBetsFromFile(HashMap<String, User> users, HashMap<Integer, Tournament> tournaments) {
        File file = new File(betsFile);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 9) continue;

                int matchID = Integer.parseInt(parts[0]);
                String userEmail = parts[1];
                int day = Integer.parseInt(parts[2]);
                int month = Integer.parseInt(parts[3]) - 1;
                int year = Integer.parseInt(parts[4]);
                int predHomeGoals = Integer.parseInt(parts[5]);
                int predAwayGoals = Integer.parseInt(parts[6]);
                int predWinner = Integer.parseInt(parts[7]);
                int difference = Integer.parseInt(parts[8]);

                User user = users.get(userEmail);
                Match match = null;
                for (Tournament t : tournaments.values()) {
                    if (t.getMatches().containsKey(matchID)) {
                        match = t.getMatches().get(matchID);
                        break;
                    }
                }
                if (match == null || user == null) continue;

                GregorianCalendar betDate = new GregorianCalendar(year, month, day);
                UserMatchBet bet = new UserMatchBet(betDate, predHomeGoals, predAwayGoals);
                bet.setPredWinner(predWinner);
                bet.setDifference(difference);

                match.addBet(user, bet);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error reading bets from file: " + e.getMessage());
        }
    }
    
	private void readTeamsResultsFromFile(HashMap<TournamentTeamKey, TeamTournamentResult> teamsResults, HashMap<Integer, Tournament> tournaments, HashMap<Integer, Team> teams) {
		File file = new File(teamResultsFile);
		if (!file.exists()) return;
		
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.isEmpty()) continue;
				
				String[] parts = line.split(";");
				if (parts.length != 3) continue;
				
				int tournamentID = Integer.parseInt(parts[0]);
				int teamID = Integer.parseInt(parts[1]);
				String result = parts[2];
				
				Tournament tournament = tournaments.get(tournamentID);
				Team team = teams.get(teamID);
				
				if (tournament == null || team == null) continue;
				
				TournamentTeamKey key = new TournamentTeamKey(tournament, team);
				TeamTournamentResult teamResult = new TeamTournamentResult(team, tournament, result);
				teamsResults.put(key, teamResult);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error reading teams results from file", "File Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	
	//write methods:
    private void writeUsersToFile(HashMap<String, User> users) {
        try (FileWriter writer = new FileWriter(usersFile)) {
            for (User user : users.values()) {
                GregorianCalendar dob = user.getDateOfBirth();
                writer.write(user.getName() + ";" + user.getEmail() + ";" + user.getPassword() + ";" +
                        dob.get(GregorianCalendar.DAY_OF_MONTH) + ";" + (dob.get(GregorianCalendar.MONTH) + 1) + ";" +
                        dob.get(GregorianCalendar.YEAR) + ";" + user.getIsAdmin() + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving users to file", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void writeTeamsToFile(HashMap<Integer, Team> teams) {
        try (FileWriter writer = new FileWriter(teamsFile)) {
            for (Team team : teams.values()) {
                writer.write(team.getTeamID() + ";" + team.getTeamName() + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving teams to file", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void writeTournamentsToFile(HashMap<Integer, Tournament> tournaments) {
        try (FileWriter writer = new FileWriter(tournamentsFile)) {
            for (Tournament t : tournaments.values()) {
                writer.write(t.getTournamentID() + ";" + t.getTournamentName() + ";" +
                        t.getStartDate().get(GregorianCalendar.DAY_OF_MONTH) + ";" +
                        (t.getStartDate().get(GregorianCalendar.MONTH) + 1) + ";" +
                        t.getStartDate().get(GregorianCalendar.YEAR) + ";" +
                        t.getEndDate().get(GregorianCalendar.DAY_OF_MONTH) + ";" +
                        (t.getEndDate().get(GregorianCalendar.MONTH) + 1) + ";" +
                        t.getEndDate().get(GregorianCalendar.YEAR) + ";" +
                        ((t.getWinner() != null) ? t.getWinner().getTeamID() : -1) + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving tournaments to file", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void writeTournamentsTeamsToFile(HashMap<Integer, Tournament> tournaments) {
        try (FileWriter writer = new FileWriter(tournamentTeamsFile)) {
            for (Tournament t : tournaments.values()) {
                for (Team team : t.getTeams().values()) {
                    writer.write(t.getTournamentID() + ";" + team.getTeamID() + "\n");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving tournament teams to file", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void writeMatchesToFile(HashMap<Integer, Tournament> tournaments) {
        try (FileWriter writer = new FileWriter(matchesFile)) {
            for (Tournament t : tournaments.values()) {
                for (Match match : t.getMatches().values()) {
                    GregorianCalendar date = match.getMatchDate();
                    writer.write(match.getMatchID() + ";" + match.getHomeTeam().getTeamID() + ";" +
                            match.getAwayTeam().getTeamID() + ";" + date.get(GregorianCalendar.DAY_OF_MONTH) + ";" +
                            (date.get(GregorianCalendar.MONTH) + 1) + ";" + date.get(GregorianCalendar.YEAR) + ";" +
                            match.getStage() + ";" + match.getFinished() + ";" + match.getHomeGoals() + ";" +
                            match.getAwayGoals() + ";" + match.getPoints() + ";" + t.getTournamentID() + "\n");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error writing matches to file", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void writeUserTournamentBetsToFile(HashMap<Integer, Tournament> tournaments) {
        try (FileWriter writer = new FileWriter(userTournamentBetsFile)) {
            for (Tournament t : tournaments.values()) {
                for (UserTournamentBet bet : t.getUserTournamentBets().values()) {
                    writer.write(bet.getUser().getEmail() + ";" + t.getTournamentID() + ";" +
                            bet.getPredictedWinner().getTeamID() + ";" + bet.getUserPoints() + "\n");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error writing tournament bets: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void writeBetsToFile(HashMap<Integer, Tournament> tournaments) {
        try (FileWriter writer = new FileWriter(betsFile)) {
            for (Tournament t : tournaments.values()) {
                for (Match match : t.getMatches().values()) {
                    for (User user : match.getBets().keySet()) {
                    	UserMatchBet bet = match.getBets().get(user);
                        GregorianCalendar date = bet.getBetDate();
                        String line = match.getMatchID() + ";" + user.getEmail() + ";" +
                                date.get(GregorianCalendar.DAY_OF_MONTH) + ";" +
                                (date.get(GregorianCalendar.MONTH) + 1) + ";" +
                                date.get(GregorianCalendar.YEAR) + ";" +
                                bet.getPredHomeGoals() + ";" +
                                bet.getPredAwayGoals() + ";" +
                                bet.isPredWinner() + ";" +
                                bet.getDifference() + "\n";
                        writer.write(line);
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error writing bets to file: " + e.getMessage());
        }
    }
    
    private void writeTeamsResultsToFile(HashMap<TournamentTeamKey, TeamTournamentResult> teamsResults) {

        try (FileWriter writer = new FileWriter(teamResultsFile)) {
            for (TeamTournamentResult result : teamsResults.values()) {
                Tournament tournament = result.getTournament();
                Team team = result.getTeam();
                String stage = result.getStage();
                

                writer.write(tournament.getTournamentID() + ";" + team.getTeamID() + ";" + stage + "\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error writing teams results to file", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
