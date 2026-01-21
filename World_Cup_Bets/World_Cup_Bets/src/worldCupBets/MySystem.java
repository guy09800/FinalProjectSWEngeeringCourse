package worldCupBets;

import java.awt.Component;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

public class MySystem {
    private HashMap<String, User> users;
    private HashMap<Integer, Tournament> tournaments;
    private HashMap<Integer, Team> teams;
    private static HashMap<TournamentTeamKey, TeamTournamentResult> teamsResults;
    private Frame frame;
    private RightClickMenu rightClickMenu;
    private MenuBar menuBar;
    private Component welcomeScreen;
    private User loggedInUser;
    private FileManager fileManager;
    private GUI gui;
    private TournamentStatsRevealThread statsRevealThread;

    

    public MySystem(FileManager fileManager) {
    	this.fileManager = fileManager;
    	this.gui = new GUI(this);
        users = new HashMap<>();
        tournaments = new HashMap<>();
        teams = new HashMap<>();
        teamsResults = new HashMap<>();
        
        frame = new Frame("World Cup Bets");
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        Label welcome = new Label("Welcome to World Cup Bets System!", Label.CENTER);
        frame.add(welcome, BorderLayout.CENTER);
        welcomeScreen = new Label("Welcome to World Cup Bets System!", Label.CENTER);
        frame.add(welcomeScreen, BorderLayout.CENTER);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                exitSystem();
            }
        });
        
        rightClickMenu = new RightClickMenu(this, gui);
        rightClickMenu.enable();
    }

	public User getLoggedInUser() {
		return loggedInUser;
	}
	
	public void setLoggedInUser(User user) {
		this.loggedInUser = user;
	}
	
	public HashMap<TournamentTeamKey, TeamTournamentResult> getTeamResults() {
		return teamsResults;
	}
	
	public HashMap<String, User> getUsers() {
		return users;
	}
	
	public HashMap<Integer, Tournament> getTournaments() {
		return tournaments;
	}
	public HashMap<Integer, Team> getTeams() {
		return teams;
	}
	
	public Frame getFrame() {
		return frame;
	}
    
    public void run() {
    	frame.setVisible(true);
    	setupLoginMenuBar();
        frame.setVisible(true);
    }
    
    //------------------------------------------------------------------
    //menu bar and menus setup methods:
    public void setupLoginMenuBar() {
        menuBar = new MenuBar();
        Menu loginMenu = new Menu("Login");
        Menu systemMenu = new Menu("System");
        
        MenuItem login = new MenuItem("Login the System");
        MenuItem exit = new MenuItem("Exit the System");
        
        login.setActionCommand(GUI.CMD_LOGIN);
        exit.setActionCommand(GUI.CMD_EXIT);
        
        login.addActionListener(gui);
        exit.addActionListener(gui);
        
        loginMenu.add(login);
        systemMenu.add(exit);

        menuBar.add(loginMenu);
        menuBar.add(systemMenu);

        frame.setMenuBar(menuBar);

        frame.removeAll();
        frame.setLayout(new BorderLayout());
        frame.add(welcomeScreen, BorderLayout.CENTER);

        rightClickMenu.enable();
        rightClickMenu.enableOn(welcomeScreen);

        frame.validate();
        frame.repaint();
    }


    public void setupMenuBar() {
        rightClickMenu.disableOn(welcomeScreen);
        frame.removeAll();

        menuBar = new MenuBar();

        menuBar.add(gui.createOperationMenu());
        menuBar.add(gui.createStatsMenu());
        menuBar.add(gui.createBettingMenu());

        Menu systemMenu = new Menu("System");
        MenuItem exit = new MenuItem("Exit the System");
        exit.setActionCommand(GUI.CMD_EXIT);
        exit.addActionListener(gui);
        systemMenu.add(exit);
        
        menuBar.add(systemMenu);
        
        frame.setMenuBar(menuBar);
        frame.validate();
        frame.repaint();
    }

    //========================== USER MANAGEMENT METHODS ===========================
    public void exitSystem() {
        fileManager.saveAllData(users, tournaments, teams, teamsResults);
        frame.dispose();
    }

    public User showLoginDialog() {
        while (true) {
            String email = JOptionPane.showInputDialog(frame, "Enter email:", "Login", JOptionPane.QUESTION_MESSAGE);
            if (email == null) return null;
            if (!User.isValidEmail(email)) {
                JOptionPane.showMessageDialog(frame, "Invalid email format.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            if (!users.containsKey(email)) {
                int option = JOptionPane.showConfirmDialog(frame, "Email not found. Do you want to register?", "User Not Found", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    User user = registerUser(email);
                    if (user != null) return user;
                }
                continue;
            }

            JPasswordField passField = new JPasswordField();
            int passOption = JOptionPane.showConfirmDialog(frame, passField, "Enter password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (passOption != JOptionPane.OK_OPTION) return null;

            String password = new String(passField.getPassword());
            User user = login(email, password);
            if (user != null) return user;

            JOptionPane.showMessageDialog(frame, "Incorrect password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private User login(String email, String password) {
        User user = users.get(email);
        if (user != null && user.getPassword().equals(password)) return user;
        return null;
    }

    private User registerUser(String email) {
        if (users.containsKey(email)) {
            JOptionPane.showMessageDialog(frame, "User already exists.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        User newUser = GUI.newUserGUI(email);
        if (newUser == null) return null;

        users.put(email, newUser);
        JOptionPane.showMessageDialog(frame, "Registration successful.", "Registered", JOptionPane.INFORMATION_MESSAGE);

        return newUser;
    }

	public void showScreen(Component c) {
	     frame.removeAll();
	     frame.add(c);
	     frame.validate();
	     frame.repaint();
	}
	
	public void registerTournament(Tournament tournament) {
        for (Team team : tournament.getTeams().values()) {
            TeamTournamentResult result = new TeamTournamentResult(team, tournament);
            TournamentTeamKey key = new TournamentTeamKey(tournament, team);
            teamsResults.put(key, result);
        }
        tournaments.put(tournament.getTournamentID(), tournament);
    }

    public void afterMatchFinished(Tournament tournament, Match match) {
        tournament.updateUserPointsAfterMatchFinish(match);

        if ("Final".equals(match.getStage())) {
            Team winner = (match.getHomeGoals() > match.getAwayGoals()) ? match.getHomeTeam() : match.getAwayTeam();
            tournament.setWinner(winner);

            for (TeamTournamentResult teamResult : teamsResults.values()) {
                if (teamResult.getTournament() == tournament) {
                    teamResult.setFinalResult();
                }
            }
        }
    }

    public static void placeBet(User user, Match match, UserMatchBet bet) {
        match.addBet(user, bet);
        user.addBetToBetList(match, bet);
    }

    public static void ensureTournamentWinnerBet(User user, Tournament tournament) {
        String email = user.getEmail();
        boolean already = tournament.getUserTournamentBets().containsKey(email);

        boolean anyFinished = false;
        for (Match m : tournament.getMatches().values()) {
            if (m.getFinished()) {
            	anyFinished = true;
            	break;
            }
        }

        if (!already && !anyFinished) {
            UserTournamentBet tBet = GUI.newUserTournamentBetGUI(user, tournament);
            if (tBet != null) {
                tournament.getUserTournamentBets().put(email, tBet);
                JOptionPane.showMessageDialog(null, "Tournament winner bet saved.");
            }
        }
    }

	//============ ACTION HANDLE METHODS ============
	public void handle_login() {
	    User loggedUser = showLoginDialog();
	    if (loggedUser != null) {
	        JOptionPane.showMessageDialog(null, "Welcome, " + loggedUser.getName(), "Login Successful", JOptionPane.INFORMATION_MESSAGE);

	        setLoggedInUser(loggedUser);
	        setupMenuBar();
	    }
	}

	public void handle_logout() {
	    getFrame().removeAll();
	    getFrame().setMenuBar(null);
	    run();
	}

	public void handle_exit() {
	    exitSystem();
	}

	public void handle_new_team() {
	    Team newTeam = GUI.newTeamGUI();
	    if (newTeam != null) {
	        getTeams().put(newTeam.getTeamID(), newTeam);
	        JOptionPane.showMessageDialog(null, "Team " + newTeam.getTeamName() + " created successfully.");
	    }
	}

	public void handle_view_teams() {
	    showScreen(GUI.printTeams(getTeams()));
	}

	public void handle_view_users() {
	    showScreen(GUI.printUsers(getUsers()));
	}

	public void handle_make_admin() {
	    showScreen(GUI.printUsersToChoose(getUsers()));
	}

	public void handle_new_tournament() {
	    Tournament newTournament = GUI.newTournamentGUI(getTeams());
	    if (newTournament == null)
	    	return;

	    for (Team team : newTournament.getTeams().values()) {
	        TeamTournamentResult result = new TeamTournamentResult(team, newTournament);
	        TournamentTeamKey key = new TournamentTeamKey(newTournament, team);
	        getTeamResults().put(key, result);
	    }
	    getTournaments().put(newTournament.getTournamentID(), newTournament);
	    JOptionPane.showMessageDialog(null, "Tournament " + newTournament.getTournamentName() + " created successfully.");
	}

	public void handle_view_tournament() {
	    showScreen(GUI.printTournaments(getTournaments()));
	}

	public void handle_new_match() {
	    Tournament selectedTournament = GUI.chooseActiveTournamentPopup(getTournaments());
	    if (selectedTournament == null) {
	        JOptionPane.showMessageDialog(null, "No tournament selected.");
	        return;
	    }

	    Match newMatch = GUI.newMatchGUI(selectedTournament);
	    if (newMatch != null) {
	        selectedTournament.putMatch(newMatch);
	        JOptionPane.showMessageDialog(null, "Match created successfully in tournament " + selectedTournament.getTournamentName() + ".");
	    }
	}

	public void handle_update_match() {
	    Tournament selectedTournament = GUI.chooseActiveTournamentPopup(getTournaments());
	    if (selectedTournament == null) {
	        JOptionPane.showMessageDialog(null, "No tournament selected.");
	        return;
	    }

	    Match matchToUpdate = GUI.chooseActiveMatchPopup(selectedTournament);
	    if (matchToUpdate == null) return;

	    GUI.updateMatchGUI(matchToUpdate);

	    if (matchToUpdate.getFinished()) {
	        selectedTournament.updateUserPointsAfterMatchFinish(matchToUpdate);

	        if ("Final".equals(matchToUpdate.getStage())) {
	            Team winner = (matchToUpdate.getHomeGoals() > matchToUpdate.getAwayGoals()) ? matchToUpdate.getHomeTeam() : matchToUpdate.getAwayTeam();
	            selectedTournament.setWinner(winner);

	            for (TeamTournamentResult teamResult : getTeamResults().values()) {
	                if (teamResult.getTournament() == selectedTournament) {
	                    teamResult.setFinalResult();
	                }
	            }
	        }
	    }
	}

	public void handle_view_matchs() {
	    Tournament selectedTournament = GUI.chooseTournamentPopup(getTournaments());
	    if (selectedTournament == null) {
	        JOptionPane.showMessageDialog(null, "No tournament selected.");
	        return;
	    }
	    showScreen(GUI.printMatchesGUI(selectedTournament));
	}

	public void handle_new_bet() {
	    Tournament selectedTournament = GUI.chooseActiveTournamentPopup(getTournaments());
	    if (selectedTournament == null)
	    	return;

	    while (true) {
	        Match selectedMatch = GUI.chooseActiveMatchPopup(selectedTournament, getLoggedInUser());
	        if (selectedMatch == null)
	        	break;

	        UserMatchBet newBet = GUI.newBetGUI(selectedMatch);
	        if (newBet == null)
	        	break;

	        selectedMatch.addBet(getLoggedInUser(), newBet);
	        getLoggedInUser().addBetToBetList(selectedMatch, newBet);

	        JOptionPane.showMessageDialog(null, "Bet placed successfully.");

	        int choice = JOptionPane.showConfirmDialog( null, "Do you want to bet on another match in this tournament?", "Continue Betting", JOptionPane.YES_NO_OPTION);
	        if (choice != JOptionPane.YES_OPTION)
	        	break;
	    }

	    String userEmail = getLoggedInUser().getEmail();
	    boolean alreadyBetOnTournament = selectedTournament.getUserTournamentBets().containsKey(userEmail);

	    boolean anyMatchFinished = false;
	    for (Match m : selectedTournament.getMatches().values()) {
	        if (m.getFinished()) {
	        	anyMatchFinished = true;
	        	break;
	        }
	    }

	    if (!alreadyBetOnTournament && !anyMatchFinished) {
	        UserTournamentBet tBet = GUI.newUserTournamentBetGUI(getLoggedInUser(), selectedTournament);
	        if (tBet != null) {
	            selectedTournament.getUserTournamentBets().put(userEmail, tBet);
	            JOptionPane.showMessageDialog(null, "Tournament winner bet saved.");
	        }
	    }
	}

	public void handle_update_bet() {
	    Tournament selectedTournament = GUI.chooseActiveTournamentPopup(getTournaments());
	    if (selectedTournament == null) {
	        JOptionPane.showMessageDialog(null, "No tournament selected.");
	        return;
	    }

	    Match selectedMatch = GUI.chooseFutureUserBetPopup(getLoggedInUser(), selectedTournament);
	    if (selectedMatch == null) return;

	    User user = getLoggedInUser();
	    UserMatchBet existingBet = selectedMatch.getBets().get(user);
	    if (existingBet == null) {
	        JOptionPane.showMessageDialog(null, "No bet found for the selected match.");
	        return;
	    }

	    UserMatchBet updatedBet = GUI.updateBetGUI(selectedMatch, existingBet);
	    if (updatedBet == null) return;

	    selectedMatch.addBet(user, updatedBet);
	    user.addBetToBetList(selectedMatch, updatedBet);

	    JOptionPane.showMessageDialog(null, "Bet updated successfully.");
	}

	public void handle_view_bets() {
	    Tournament selectedTournament = GUI.chooseActiveTournamentPopup(getTournaments());
	    if (selectedTournament == null) {
	        JOptionPane.showMessageDialog(null, "No tournament selected.");
	        return;
	    }
	    showScreen(GUI.printLoggedInUserBets(getLoggedInUser(), selectedTournament));
	}

	public void handle_stats_general() {
	    showScreen(GUI.printGeneralStats(getTournaments()));
	}

	public void handle_stats_tournament() {
	    Tournament selectedTournament = GUI.chooseTournamentPopup(getTournaments());
	    if (selectedTournament == null) {
	        JOptionPane.showMessageDialog(getFrame(), "No tournament selected.");
	        return;
	    }

	    JPanel statsPanel = GUI.printTournamentStats(selectedTournament);
	    showScreen(statsPanel);

	    if (statsRevealThread != null) {
	        statsRevealThread.stopAnimation();
	        statsRevealThread = null;
	    }

	    DefaultTableModel model = (DefaultTableModel) statsPanel.getClientProperty("statsModel");
	    Object[][] data = (Object[][]) statsPanel.getClientProperty("statsData");

	    if (model != null && data != null) {
	        statsRevealThread = new TournamentStatsRevealThread(model, data);
	        statsRevealThread.start();
	    }
	}

	public void handle_stats_teams() {

	    if (getTeams() == null || getTeams().isEmpty()) {
	        JOptionPane.showMessageDialog(getFrame(), "No teams available.");
	        return;
	    }

	    Team selectedTeam = GUI.chooseTeamPopup(getTeams());
	    if (selectedTeam == null) {
	        JOptionPane.showMessageDialog(getFrame(), "No team selected.");
	        return;
	    }

	    JPanel panel = GUI.printTeamStats(selectedTeam, teamsResults);
	    showScreen(panel);
	}

	public void handle_skip_to_idan() {
	    setLoggedInUser(getUsers().get("idanlin97@gmail.com"));
	    setupMenuBar();
	}
}