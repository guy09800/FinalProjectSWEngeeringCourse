package worldCupBets;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;

public class GUI implements ActionListener {

    private static Frame frame;
    private final MySystem system;

    public GUI(MySystem system) {
        this.system = system;
    }

    public static void setFrame(Frame f) {
    	frame = f; 
    }

    // ======================== COMMANDS ==========================
    //Login/System
    public static final String CMD_LOGIN = "LOGIN";
    public static final String CMD_EXIT = "EXIT";
    public static final String CMD_LOGOUT = "LOGOUT";
    //Tournaments
    public static final String CMD_NEW_TOURNAMENT = "NEW_TOURNAMENT";
    public static final String CMD_VIEW_TOURNAMENTS = "VIEW_TOURNAMENTS";
    //Teams
    public static final String CMD_NEW_TEAM = "NEW_TEAM";
    public static final String CMD_VIEW_TEAMS = "VIEW_TEAMS";
    //Users
    public static final String CMD_VIEW_USERS = "VIEW_USERS";
    public static final String CMD_MAKE_ADMIN = "MAKE_ADMIN";
    //Matches
    public static final String CMD_NEW_MATCH = "NEW_MATCH";
    public static final String CMD_UPDATE_MATCH = "UPDATE_MATCH";
    public static final String CMD_VIEW_MATCHES = "VIEW_MATCHES";
    //Bets
    public static final String CMD_NEW_BET = "NEW_BET";
    public static final String CMD_UPDATE_BET = "UPDATE_BET";
    public static final String CMD_VIEW_BETS = "VIEW_BETS";
    //Statistics
    public static final String CMD_STATS_TOURNAMENT = "STATS_TOURNAMENT";
    public static final String CMD_STATS_GENERAL = "STATS_GENERAL";
    public static final String CMD_STATS_TEAM = "STATS_TEAM";
    //RightClick
    public static final String CMD_SKIP_TO_IDAN = "SKIP_TO_IDAN";
    
    // ======================== ACTION LISTENER ==========================
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd == null) return;

        switch (cmd) {
        	//Login/System
            case CMD_LOGIN: system.handle_login(); break;
            case CMD_LOGOUT: system.handle_logout(); break;
            case CMD_EXIT: system.handle_exit(); break;
            //Teams
            case CMD_NEW_TEAM: system.handle_new_team(); break;
            case CMD_VIEW_TEAMS: system.handle_view_teams(); break;
            //Users
            case CMD_VIEW_USERS: system.handle_view_users(); break;
            case CMD_MAKE_ADMIN: system.handle_make_admin(); break;
            //Tournaments
            case CMD_NEW_TOURNAMENT: system.handle_new_tournament(); break;
            case CMD_VIEW_TOURNAMENTS: system.handle_view_tournament(); break;
            //Matches
            case CMD_NEW_MATCH: system.handle_new_match(); break;
            case CMD_UPDATE_MATCH: system.handle_update_match(); break;
            case CMD_VIEW_MATCHES: system.handle_view_matchs(); break;
            //Bets
            case CMD_NEW_BET: system.handle_new_bet(); break;
            case CMD_UPDATE_BET: system.handle_update_bet(); break;
            case CMD_VIEW_BETS: system.handle_view_bets(); break;
            //Statistics
            case CMD_STATS_GENERAL: system.handle_stats_general(); break;
            case CMD_STATS_TOURNAMENT: system.handle_stats_tournament(); break;
            case CMD_STATS_TEAM: system.handle_stats_teams(); break;
            //RightClick
            case CMD_SKIP_TO_IDAN: system.handle_skip_to_idan(); break;
            
            default: System.out.println("Unknown command: " + cmd); break;
        }
    }
    
    // ======================== MENU ITEM BUILDING ==========================
    private MenuItem cmdItem(String text, String cmd) {
        MenuItem mi = new MenuItem(text);
        mi.setActionCommand(cmd);
        mi.addActionListener(this);
        return mi;
    }
    // ======================== MENUS ==========================
    public MenuBar buildLoginMenuBar() {
        MenuBar mb = new MenuBar();
        mb.add(createLoginMenu());
        mb.add(createSystemMenu());
        return mb;
    }

    public MenuBar buildMainMenuBar() {
        MenuBar mb = new MenuBar();
        mb.add(createOperationMenu());
        mb.add(createStatsMenu());
        mb.add(createBettingMenu());
        mb.add(createSystemMenu());
        return mb;
    }

    public Menu createOperationMenu() {
        String role = system.getLoggedInUser().getIsAdmin() ? "Admin" : "User";
        Menu adminMenu = new Menu(role + " Menu");

        if (system.getLoggedInUser().getIsAdmin()) {
            adminMenu.add(createTournamentsMenu());
            adminMenu.add(createTeamsMenu());
            adminMenu.add(createUsersMenu());
            adminMenu.add(createMatchesMenu());
        } else {
            adminMenu.add(cmdItem("View Tournaments", CMD_VIEW_TOURNAMENTS));
            adminMenu.add(cmdItem("View Matches", CMD_VIEW_MATCHES));
        }

        adminMenu.addSeparator();
        adminMenu.add(createUserInfoItem(system)); 
        adminMenu.add(cmdItem("Logout", CMD_LOGOUT));
        return adminMenu;
    }

    public Menu createTournamentsMenu() {
        Menu m = new Menu("Tournaments");
        m.add(cmdItem("New Tournament", CMD_NEW_TOURNAMENT));
        m.add(cmdItem("View Tournaments", CMD_VIEW_TOURNAMENTS));
        return m;
    }

    public Menu createTeamsMenu() {
        Menu m = new Menu("Teams");
        m.add(cmdItem("New Team", CMD_NEW_TEAM));
        m.add(cmdItem("View Teams", CMD_VIEW_TEAMS));
        return m;
    }

    public Menu createUsersMenu() {
        Menu m = new Menu("Users");
        m.add(cmdItem("Make Admin", CMD_MAKE_ADMIN));
        m.add(cmdItem("View Users", CMD_VIEW_USERS));
        return m;
    }

    public Menu createMatchesMenu() {
        Menu m = new Menu("Matches");
        m.add(cmdItem("New Match", CMD_NEW_MATCH));
        m.add(cmdItem("Update Match", CMD_UPDATE_MATCH));
        m.add(cmdItem("View Matches", CMD_VIEW_MATCHES));
        return m;
    }

    public Menu createStatsMenu() {
        Menu m = new Menu("Statistics");
        m.add(cmdItem("Show Tournament Stats", CMD_STATS_TOURNAMENT));
        m.add(cmdItem("Show General Stats", CMD_STATS_GENERAL));
        m.add(cmdItem("Show Team Stats", CMD_STATS_TEAM));
        return m;
    }

    public Menu createBettingMenu() {
        Menu m = new Menu("Betting");
        m.add(cmdItem("New Bet", CMD_NEW_BET));
        m.add(cmdItem("Update Bet", CMD_UPDATE_BET));
        m.add(cmdItem("View My Bets", CMD_VIEW_BETS));
        return m;
    }

    public Menu createSystemMenu() {
        Menu m = new Menu("System");
        m.add(cmdItem("Exit the System", CMD_EXIT));
        return m;
    }

    public Menu createLoginMenu() {
        Menu m = new Menu("Login");
        m.add(cmdItem("Login the System", CMD_LOGIN));
        return m;
    }
    
    public MenuItem createIdanItem() {
        MenuItem item = new MenuItem("skip to idan user");
        item.setActionCommand(CMD_SKIP_TO_IDAN);
        item.addActionListener(this);
        return item;
    }

    // ======================== USER INFO CHECKBOX ==========================
    public static CheckboxMenuItem createUserInfoItem(MySystem system) {
        CheckboxMenuItem item = new CheckboxMenuItem("Show User Info", false);
        item.addItemListener(e -> {
            if (item.getState()) {
                system.getFrame().setTitle("World Cup Bets - Logged in as: " +
                        system.getLoggedInUser().getName() + " (" + system.getLoggedInUser().getEmail() + ")");
            } else {
                system.getFrame().setTitle("World Cup Bets");
            }
        });
        return item;
    }

    // ======================== NEW ITEMS POPUPS ==========================
    public static User newUserGUI(String email) {
        if (frame == null) {
            throw new IllegalStateException("Main frame was not set");
        }

        final Dialog dialog = new Dialog(frame, "Complete Registration", true);
        dialog.setLayout(new BorderLayout());

        Panel form = new Panel(new GridLayout(0, 2, 8, 8));

        TextField nameField = new TextField(20);
        TextField emailField = new TextField(email, 20);
        TextField passwordField = new TextField(20);
        TextField dayField = new TextField(5);
        TextField monthField = new TextField(5);
        TextField yearField = new TextField(8);
        
        emailField.setEditable(false);
        passwordField.setEchoChar('*');

        form.add(new Label("Name:"));
        form.add(nameField);

        form.add(new Label("Email:"));
        form.add(emailField);

        form.add(new Label("Password:"));
        form.add(passwordField);

        form.add(new Label("Day of Birth:"));
        form.add(dayField);

        form.add(new Label("Month of Birth:"));
        form.add(monthField);

        form.add(new Label("Year of Birth:"));
        form.add(yearField);

        Label status = new Label(" ");
        Panel statusPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(status);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.RIGHT));
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        buttons.add(cancel);
        buttons.add(ok);

        final User[] result = new User[1];

        cancel.addActionListener(e -> {
            result[0] = null;
            dialog.dispose();
        });

        ok.addActionListener(e -> {
            String name = nameField.getText().trim();
            String password = passwordField.getText();

            if (!User.isValidName(name)) {
                status.setText("Invalid name");
                return;
            }

            if (!User.isValidPassword(password)) {
                status.setText("Password must be at least 6 characters");
                return;
            }

            int day, month, year;
            try {
                day = Integer.parseInt(dayField.getText().trim());
                month = Integer.parseInt(monthField.getText().trim()) - 1;
                year = Integer.parseInt(yearField.getText().trim());
            } catch (Exception ex) {
                status.setText("Invalid date");
                return;
            }

            GregorianCalendar dob = new GregorianCalendar(year, month, day);
            if (!User.isValidDateOfBirth(dob)) {
                status.setText("User must be at least 18 years old");
                return;
            }
            
            result[0] = new User(name, email, password, dob);
            dialog.dispose();
        });

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                result[0] = null;
                dialog.dispose();
            }
        });

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(statusPanel, BorderLayout.NORTH);
        dialog.add(buttons, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);

        return result[0];
    }

    public static Team newTeamGUI() {
        if (frame == null) throw new IllegalStateException("Main frame was not set");

        final Dialog d = new Dialog(frame, "Create New Team", true);
        d.setLayout(new BorderLayout());

        Panel form = new Panel(new GridLayout(0, 2, 8, 8));
        TextField nameField = new TextField(20);

        form.add(new Label("Team Name:"));
        form.add(nameField);

        Label status = new Label(" ");
        Panel statusPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(status);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.RIGHT));
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        buttons.add(cancel);
        buttons.add(ok);

        final Team[] result = new Team[1];

        cancel.addActionListener(e -> {
            result[0] = null;
            d.dispose();
        });

        ok.addActionListener(e -> {
            String teamName = nameField.getText().trim();
            if (teamName.isEmpty()) {
                status.setText("Team name cannot be empty");
                return;
            }
            result[0] = new Team(teamName);
            d.dispose();
        });

        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { result[0] = null; d.dispose(); }
        });

        d.add(statusPanel, BorderLayout.NORTH);
        d.add(form, BorderLayout.CENTER);
        d.add(buttons, BorderLayout.SOUTH);

        d.pack();
        d.setLocationRelativeTo(frame);
        d.setVisible(true);

        return result[0];
    }

    public static UserTournamentBet newUserTournamentBetGUI(User user, Tournament tournament) {
        if (user == null || tournament == null || tournament.getTeams().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Missing user or tournament teams");
            return null;
        }
        if (frame == null) throw new IllegalStateException("Main frame was not set");

        Team[] allTeams = tournament.getTeams().values().toArray(new Team[0]);

        final Dialog d = new Dialog(frame, "Tournament Winner Bet", true);
        d.setLayout(new BorderLayout());

        Panel form = new Panel(new GridLayout(0, 2, 8, 8));

        Choice winnerChoice = new Choice();
        winnerChoice.add("No prediction");
        for (Team t : allTeams) winnerChoice.add(t.getTeamName());

        form.add(new Label("Predicted Tournament Winner:"));
        form.add(winnerChoice);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.RIGHT));
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        buttons.add(cancel);
        buttons.add(ok);

        final UserTournamentBet[] result = new UserTournamentBet[1];

        cancel.addActionListener(e -> { 
        	result[0] = null;
        	d.dispose(); 
        });

        ok.addActionListener(e -> {
            String selected = winnerChoice.getSelectedItem();
            Team predictedWinner = null;
            if (!"No prediction".equals(selected)) {
                for (Team t : allTeams) {
                    if (t.getTeamName().equals(selected)) { 
                    	predictedWinner = t;
                    	break; 
                    }
                }
            }
            result[0] = new UserTournamentBet(user, tournament, predictedWinner);
            d.dispose();
        });

        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { result[0] = null; d.dispose(); }
        });

        d.add(form, BorderLayout.CENTER);
        d.add(buttons, BorderLayout.SOUTH);

        d.pack();
        d.setLocationRelativeTo(frame);
        d.setVisible(true);

        return result[0];
    }

    public static Tournament newTournamentGUI(HashMap<Integer, Team> allTeams) {
        if (frame == null) throw new IllegalStateException("Main frame was not set");

        if (allTeams == null || allTeams.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No teams exist. Create teams first.");
            return null;
        }

        final Dialog d = new Dialog(frame, "Create Tournament", true);
        d.setLayout(new BorderLayout());

        Panel form = new Panel(new GridLayout(0, 2, 8, 8));

        TextField nameField = new TextField(20);
        TextField sdField = new TextField(4);
        TextField smField = new TextField(4);
        TextField syField = new TextField(6);
        TextField edField = new TextField(4);
        TextField emField = new TextField(4);
        TextField eyField = new TextField(6);

        form.add(new Label("Tournament Name:"));
        form.add(nameField);
        form.add(new Label("Start Day:"));
        form.add(sdField);
        form.add(new Label("Start Month:"));
        form.add(smField);
        form.add(new Label("Start Year:"));
        form.add(syField);
        form.add(new Label("End Day:"));
        form.add(edField);
        form.add(new Label("End Month:"));
        form.add(emField);
        form.add(new Label("End Year:"));
        form.add(eyField);

        Panel teamsPanel = new Panel(new GridLayout(0, 3, 4, 4));
        HashMap<Team, Checkbox> teamChecks = new HashMap<>();

        for (Team team : allTeams.values()) {
            Checkbox cb = new Checkbox(team.getTeamName(), false);
            teamChecks.put(team, cb);
            teamsPanel.add(cb);
        }

        ScrollPane teamScroll = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        teamScroll.setPreferredSize(new Dimension(320, 160));
        teamScroll.add(teamsPanel);

        Panel teamsContainer = new Panel(new BorderLayout());
        teamsContainer.add(new Label("Select Teams:"), BorderLayout.NORTH);
        teamsContainer.add(teamScroll, BorderLayout.CENTER);

        Label status = new Label(" ");
        Panel statusPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(status);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.RIGHT));
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        buttons.add(cancel);
        buttons.add(ok);

        final Tournament[] result = new Tournament[1];

        cancel.addActionListener(e -> {
        	result[0] = null;
        	d.dispose();
        	});

        ok.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
            	status.setText("Tournament name cannot be empty");
            	return;
            }

            int sd, sm, sy, ed, em, ey;
            try {
                sd = Integer.parseInt(sdField.getText().trim());
                sm = Integer.parseInt(smField.getText().trim()) - 1;
                sy = Integer.parseInt(syField.getText().trim());

                ed = Integer.parseInt(edField.getText().trim());
                em = Integer.parseInt(emField.getText().trim()) - 1;
                ey = Integer.parseInt(eyField.getText().trim());
            } catch (Exception ex) {
                status.setText("Invalid date values");
                return;
            }

            GregorianCalendar startDate = new GregorianCalendar(sy, sm, sd);
            GregorianCalendar endDate = new GregorianCalendar(ey, em, ed);

            if (endDate.before(startDate)) {
                status.setText("End date must be after start date");
                return;
            }

            Set<Team> selectedTeams = new HashSet<>();
            for (Team t : teamChecks.keySet()) {
                if (teamChecks.get(t).getState()) selectedTeams.add(t);
            }

            if (selectedTeams.isEmpty()) {
                status.setText("You must select at least one team");
                return;
            }

            Tournament tournament = new Tournament(name, startDate, endDate);
            for (Team t : selectedTeams) {
                tournament.getTeams().put(t.getTeamID(), t);
            }

            result[0] = tournament;
            d.dispose();
        });

        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
            	result[0] = null;
            	d.dispose();
            	}
        });

        Panel center = new Panel(new BorderLayout());
        center.add(form, BorderLayout.NORTH);
        center.add(teamsContainer, BorderLayout.CENTER);

        d.add(statusPanel, BorderLayout.NORTH);
        d.add(center, BorderLayout.CENTER);
        d.add(buttons, BorderLayout.SOUTH);

        d.pack();
        d.setLocationRelativeTo(frame);
        d.setVisible(true);

        return result[0];
    }

    public static Match newMatchGUI(Tournament tournament) {
        if (tournament == null || tournament.getTeams().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No teams available for this tournament");
            return null;
        }
        if (frame == null) throw new IllegalStateException("Main frame was not set");

        Team[] allTeams = tournament.getTeams().values().toArray(new Team[0]);

        final Dialog d = new Dialog(frame, "Create New Match", true);
        d.setLayout(new BorderLayout());

        Panel form = new Panel(new GridLayout(0, 2, 5, 5));

        Choice homeChoice = new Choice();
        Choice awayChoice = new Choice();
        for (Team t : allTeams) {
            homeChoice.add(t.getTeamName());
            awayChoice.add(t.getTeamName());
        }

        TextField dayField = new TextField(4);
        TextField monthField = new TextField(4);
        TextField yearField = new TextField(6);
        TextField pointsField = new TextField(6);

        CheckboxGroup stageGroup = new CheckboxGroup();
        Panel stagePanel = new Panel(new GridLayout(0, 5, 10, 5));
        stagePanel.add(new Checkbox("Group", stageGroup, true));
        stagePanel.add(new Checkbox("Round of 16", stageGroup, false));
        stagePanel.add(new Checkbox("Quarterfinal", stageGroup, false));
        stagePanel.add(new Checkbox("Semifinal", stageGroup, false));
        stagePanel.add(new Checkbox("Final", stageGroup, false));

        form.add(new Label("Home Team:"));
        form.add(homeChoice);
        form.add(new Label("Away Team:"));
        form.add(awayChoice);
        form.add(new Label("Match Day:"));
        form.add(dayField);
        form.add(new Label("Match Month:"));
        form.add(monthField);
        form.add(new Label("Match Year:"));
        form.add(yearField);
        form.add(new Label("Stage:"));
        form.add(stagePanel);
        form.add(new Label("Points (optional):"));
        form.add(pointsField);

        Label status = new Label(" ");
        Panel statusPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(status);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.RIGHT));
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        buttons.add(cancel);
        buttons.add(ok);

        final Match[] result = new Match[1];

        cancel.addActionListener(e -> {
        	result[0] = null;
        	d.dispose();
        });

        ok.addActionListener(e -> {
            String homeName = homeChoice.getSelectedItem();
            String awayName = awayChoice.getSelectedItem();

            Team homeTeam = null;
            Team awayTeam = null;
            for (Team t : allTeams) {
                if (t.getTeamName().equals(homeName)) homeTeam = t;
                if (t.getTeamName().equals(awayName)) awayTeam = t;
            }

            if (homeTeam == null || awayTeam == null) { status.setText("Pick two teams"); return; }
            if (homeTeam == awayTeam) { status.setText("Teams cannot be the same"); return; }

            int day, month, year;
            try {
                day = Integer.parseInt(dayField.getText().trim());
                month = Integer.parseInt(monthField.getText().trim()) - 1;
                year = Integer.parseInt(yearField.getText().trim());
            } catch (Exception ex) {
                status.setText("Invalid date");
                return;
            }

            GregorianCalendar matchDate = new GregorianCalendar(year, month, day);

            if (matchDate.before(tournament.getStartDate()) || matchDate.after(tournament.getEndDate())) {
                status.setText("Match date must be within tournament dates");
                return;
            }

            Checkbox selectedStage = stageGroup.getSelectedCheckbox();
            String stage = (selectedStage != null) ? selectedStage.getLabel() : "Group";

            String ptsTxt = pointsField.getText().trim();

            try {
                if (ptsTxt.isEmpty()) {
                    result[0] = new Match(homeTeam, awayTeam, matchDate, stage);
                } else {
                    int pts = Integer.parseInt(ptsTxt);
                    result[0] = new Match(homeTeam, awayTeam, matchDate, stage, pts);
                }
            } catch (Exception ex) {
                status.setText("Invalid points value");
                return;
            }

            d.dispose();
        });

        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { result[0] = null; d.dispose(); }
        });

        d.add(statusPanel, BorderLayout.NORTH);
        d.add(form, BorderLayout.CENTER);
        d.add(buttons, BorderLayout.SOUTH);

        d.pack();
        d.setLocationRelativeTo(frame);
        d.setVisible(true);

        return result[0];
    }

    public static UserMatchBet newBetGUI(Match match) {
        if (frame == null) throw new IllegalStateException("Main frame was not set");
        if (match == null) return null;

        final Dialog d = new Dialog(frame, "Place Your Bet", true);
        d.setLayout(new BorderLayout());

        Panel form = new Panel(new GridLayout(0, 2, 8, 8));

        TextField homeGoalsField = new TextField(6);
        TextField awayGoalsField = new TextField(6);
        TextField diffField = new TextField(6);

        Choice winnerChoice = new Choice();
        winnerChoice.add(match.getHomeTeam().getTeamName()+" Win");
        winnerChoice.add("Draw");
        winnerChoice.add(match.getAwayTeam().getTeamName()+" Win");

        form.add(new Label("Predicted "+match.getHomeTeam().getTeamName()+" Goals:"));
        form.add(homeGoalsField);
        form.add(new Label("Predicted "+match.getAwayTeam().getTeamName()+" Goals:"));
        form.add(awayGoalsField);
        form.add(new Label("Predicted Winner:"));
        form.add(winnerChoice);
        form.add(new Label((String)("Predicted Goal Difference ("+match.getHomeTeam().getTeamName()+"-"+match.getAwayTeam().getTeamName())+"):"));
        form.add(diffField);

        Label status = new Label(" ");
        Panel statusPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(status);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.RIGHT));
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        buttons.add(cancel);
        buttons.add(ok);

        final UserMatchBet[] result = new UserMatchBet[1];

        cancel.addActionListener(e -> {
        	result[0] = null;
        	d.dispose();
        });

        ok.addActionListener(e -> {
            int predHome, predAway, predWinner, diff;
            try {
                predHome = Integer.parseInt(homeGoalsField.getText().trim());
                predAway = Integer.parseInt(awayGoalsField.getText().trim());
                diff = Integer.parseInt(diffField.getText().trim());

                int idx = winnerChoice.getSelectedIndex();
                predWinner = (idx == 0) ? 1 : (idx == 1) ? 0 : -1;

            } catch (Exception ex) {
                status.setText("Please enter valid numbers");
                return;
            }

            GregorianCalendar betDate = new GregorianCalendar();
            UserMatchBet bet = new UserMatchBet(betDate, predHome, predAway);
            bet.setPredWinner(predWinner);
            bet.setDifference(diff);
            result[0] = bet;

            d.dispose();
        });

        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {result[0] = null; d.dispose(); }
        });

        d.add(statusPanel, BorderLayout.NORTH);
        d.add(form, BorderLayout.CENTER);
        d.add(buttons, BorderLayout.SOUTH);

        d.pack();
        d.setLocationRelativeTo(frame);
        d.setVisible(true);

        return result[0];
    }

    // ======================== UPDATE ITEMS POPUPS ==========================
    public static void updateMatchGUI(Match match) {
        if (match == null) return;
        if (frame == null) throw new IllegalStateException("Main frame was not set");

        final Dialog d = new Dialog(frame, "Update Match Result", true);
        d.setLayout(new BorderLayout());

        Panel form = new Panel(new GridLayout(0, 2, 8, 8));

        TextField homeGoalsField = new TextField(String.valueOf(match.getHomeGoals()), 6);
        TextField awayGoalsField = new TextField(String.valueOf(match.getAwayGoals()), 6);

        form.add(new Label("Home Team (" + match.getHomeTeam().getTeamName() + ") Goals:"));
        form.add(homeGoalsField);

        form.add(new Label("Away Team (" + match.getAwayTeam().getTeamName() + ") Goals:"));
        form.add(awayGoalsField);

        Label status = new Label(" ");
        Panel statusPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(status);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.RIGHT));
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        buttons.add(cancel);
        buttons.add(ok);

        cancel.addActionListener(e -> d.dispose());

        ok.addActionListener(e -> {
            int homeGoals, awayGoals;
            try {
                homeGoals = Integer.parseInt(homeGoalsField.getText().trim());
                awayGoals = Integer.parseInt(awayGoalsField.getText().trim());
            } catch (Exception ex) {
                status.setText("Invalid goal numbers");
                return;
            }

            match.setHomeGoals(homeGoals);
            match.setAwayGoals(awayGoals);
            match.setFinished(true);

            d.dispose();

            JOptionPane.showMessageDialog(null, "Match result updated:\n" + match.getHomeTeam().getTeamName() + " " + homeGoals + " - " + awayGoals + " " + match.getAwayTeam().getTeamName());
        });

        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { d.dispose(); }
        });

        d.add(statusPanel, BorderLayout.NORTH);
        d.add(form, BorderLayout.CENTER);
        d.add(buttons, BorderLayout.SOUTH);

        d.pack();
        d.setLocationRelativeTo(frame);
        d.setVisible(true);
    }

    public static UserMatchBet updateBetGUI(Match match, UserMatchBet bet) {
        if (bet == null) return null;
        if (frame == null) throw new IllegalStateException("Main frame was not set");

        final Dialog d = new Dialog(frame, "Update Your Bet", true);
        d.setLayout(new BorderLayout());

        Panel form = new Panel(new GridLayout(0, 2, 8, 8));

        TextField homeGoalsField = new TextField(String.valueOf(bet.getPredHomeGoals()), 6);
        TextField awayGoalsField = new TextField(String.valueOf(bet.getPredAwayGoals()), 6);
        TextField diffField = new TextField(String.valueOf(bet.getDifference()), 6);

        Choice winnerChoice = new Choice();
        winnerChoice.add(match.getHomeTeam().getTeamName()+" Win");
        winnerChoice.add("Draw");
        winnerChoice.add(match.getAwayTeam().getTeamName()+" Win");

        int selectedIndex = bet.getPredWinner() == 1 ? 0 : bet.getPredWinner() == 0 ? 1 : 2;
        winnerChoice.select(selectedIndex);

        form.add(new Label("Predicted "+match.getHomeTeam().getTeamName()+" Goals:"));
        form.add(homeGoalsField);
        form.add(new Label("Predicted "+match.getAwayTeam().getTeamName()+" Goals:"));
        form.add(awayGoalsField);
        form.add(new Label("Predicted Winner:"));
        form.add(winnerChoice);
        form.add(new Label((String)("Predicted Goal Difference ("+match.getHomeTeam().getTeamName()+"-"+match.getAwayTeam().getTeamName())+"):"));
        form.add(diffField);

        Label status = new Label(" ");
        Panel statusPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(status);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.RIGHT));
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        buttons.add(cancel);
        buttons.add(ok);

        final UserMatchBet[] result = new UserMatchBet[1];

        cancel.addActionListener(e -> { result[0] = null; d.dispose(); });

        ok.addActionListener(e -> {
            int predHome, predAway, predWinner, diff;
            try {
                predHome = Integer.parseInt(homeGoalsField.getText().trim());
                predAway = Integer.parseInt(awayGoalsField.getText().trim());
                diff = Integer.parseInt(diffField.getText().trim());

                int idx = winnerChoice.getSelectedIndex();
                predWinner = (idx == 0) ? 1 : (idx == 1) ? 0 : -1;

            } catch (Exception ex) {
                status.setText("Please enter valid numbers");
                return;
            }

            bet.setPredHomeGoals(predHome);
            bet.setPredAwayGoals(predAway);
            bet.setPredWinner(predWinner);
            bet.setDifference(diff);
            bet.setBetDate(new GregorianCalendar());

            result[0] = bet;
            d.dispose();
        });

        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { result[0] = null; d.dispose(); }
        });

        d.add(statusPanel, BorderLayout.NORTH);
        d.add(form, BorderLayout.CENTER);
        d.add(buttons, BorderLayout.SOUTH);

        d.pack();
        d.setLocationRelativeTo(frame);
        d.setVisible(true);

        return result[0];
    }

    // ======================== CHOOSE POPUPS ==========================
    public static Tournament chooseTournamentPopup(HashMap<Integer, Tournament> tournaments) {
        if (frame == null) throw new IllegalStateException("Main frame was not set");
        if (tournaments == null || tournaments.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tournaments available");
            return null;
        }

        final Dialog d = new Dialog(frame, "Select Tournament", true);
        d.setLayout(new BorderLayout());

        List list = new List(10, false);
        HashMap<String, Tournament> map = new HashMap<>();

        for (Tournament t : tournaments.values()) {
            String text = t.getTournamentName() + " (ID: " + t.getTournamentID() + ")";
            list.add(text);
            map.put(text, t);
        }

        if (list.getItemCount() > 0) list.select(0);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER));
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        buttons.add(ok);
        buttons.add(cancel);

        final Tournament[] result = new Tournament[1];

        ok.addActionListener(e -> {
            String key = list.getSelectedItem();
            result[0] = (key != null) ? map.get(key) : null;
            d.dispose();
        });

        cancel.addActionListener(e -> { result[0] = null; d.dispose(); });

        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { result[0] = null; d.dispose(); }
        });

        d.add(list, BorderLayout.CENTER);
        d.add(buttons, BorderLayout.SOUTH);

        d.pack();
        d.setLocationRelativeTo(frame);
        d.setVisible(true);

        return result[0];
    }

    public static Tournament chooseActiveTournamentPopup(HashMap<Integer, Tournament> tournaments) {
        if (tournaments == null || tournaments.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tournaments available");
            return null;
        }
        HashMap<Integer, Tournament> active = new HashMap<>();
        for (Tournament t : tournaments.values()) {
            if (t.getWinner() == null) active.put(t.getTournamentID(), t);
        }
        if (active.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No active tournaments available");
            return null;
        }
        return chooseTournamentPopup(active);
    }

    public static Match chooseFutureUserBetPopup(User user, Tournament tournament) {
        if (user == null || tournament == null) return null;

        List list = new List(10, false);
        HashMap<String, Match> map = new HashMap<>();
        GregorianCalendar now = new GregorianCalendar();

        for (Match match : tournament.getMatches().values()) {
        	UserMatchBet bet = match.getBets().get(user);
            if (bet == null) continue;
            if (!match.getMatchDate().after(now)) continue;

            String text = match.getHomeTeam().getTeamName() + " vs " + match.getAwayTeam().getTeamName() + " | " + match.getMatchDate().getTime();
            list.add(text);
            map.put(text, match);
        }

        if (list.getItemCount() == 0) {
            showMessage(frame, "Info", "You have no upcoming bets in this tournament.");
            return null;
        }

        final Dialog d = new Dialog(frame, "Your Upcoming Bets", true);
        d.setLayout(new BorderLayout());

        Label countdownLabel = new Label("Starts in: -");
        Panel top = new Panel(new FlowLayout(FlowLayout.LEFT));
        top.add(countdownLabel);

        d.add(top, BorderLayout.NORTH);
        d.add(list, BorderLayout.CENTER);

        final Match[] selectedMatch = new Match[] { null };
        final boolean[] okPressed = new boolean[] { false };

        list.select(0);
        selectedMatch[0] = map.get(list.getItem(0));

        MatchCountdownTimer timer = new MatchCountdownTimer(countdownLabel, selectedMatch[0].getMatchDate());
        Thread timerThread = new Thread(timer, "MatchCountdownTimer");
        timerThread.setDaemon(true);
        timerThread.start();

        list.addItemListener(e -> {
            String key = list.getSelectedItem();
            if (key == null) return;
            Match m = map.get(key);
            selectedMatch[0] = m;
            timer.setTargetDate(m != null ? m.getMatchDate() : null);
        });

        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER));
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        buttons.add(ok);
        buttons.add(cancel);

        ok.addActionListener(e -> { okPressed[0] = true; d.dispose(); });
        cancel.addActionListener(e -> { okPressed[0] = false; d.dispose(); });

        d.add(buttons, BorderLayout.SOUTH);

        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { okPressed[0] = false; d.dispose(); }
        });

        d.pack();
        d.setLocationRelativeTo(null);
        d.setVisible(true);

        timer.stop();

        if (!okPressed[0]) return null;
        return selectedMatch[0];
    }

    public static Match chooseActiveMatchPopup(Tournament tournament) {
        if (tournament == null || tournament.getMatches().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No matches in this tournament");
            return null;
        }
        if (frame == null) throw new IllegalStateException("Main frame was not set");

        final Dialog d = new Dialog(frame, "Select Match", true);
        d.setLayout(new BorderLayout());

        List list = new List(10, false);
        HashMap<String, Match> map = new HashMap<>();

        for (Match match : tournament.getMatches().values()) {
            if (!match.getFinished()) {
                String text = match.getHomeTeam().getTeamName() + " vs " + match.getAwayTeam().getTeamName() + " (" + match.getStage() + ")";
                list.add(text);
                map.put(text, match);
            }
        }

        if (list.getItemCount() == 0) {
            JOptionPane.showMessageDialog(null, "No matches available");
            return null;
        }

        list.select(0);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER));
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        buttons.add(ok);
        buttons.add(cancel);

        final Match[] result = new Match[1];

        ok.addActionListener(e -> {
            String key = list.getSelectedItem();
            result[0] = (key != null) ? map.get(key) : null;
            d.dispose();
        });

        cancel.addActionListener(e -> {
        	result[0] = null;
        	d.dispose();
        });

        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { result[0] = null; d.dispose(); }
        });

        d.add(list, BorderLayout.CENTER);
        d.add(buttons, BorderLayout.SOUTH);

        d.pack();
        d.setLocationRelativeTo(frame);
        d.setVisible(true);

        return result[0];
    }

    public static Match chooseActiveMatchPopup(Tournament tournament, User user) {
        Frame owner = (frame != null) ? frame : new Frame();

        if (tournament == null || tournament.getMatches().isEmpty()) {
            showMessage(owner, "Info", "No matches in this tournament");
            return null;
        }

        List list = new List(10, false);
        HashMap<String, Match> map = new HashMap<>();

        for (Match match : tournament.getMatches().values()) {
            boolean alreadyBet = match.getBets().containsKey(user);
            if (!match.getFinished() && !alreadyBet) {
                String text = match.getHomeTeam().getTeamName() + " vs " + match.getAwayTeam().getTeamName() + " (" + match.getStage() + ")";
                list.add(text);
                map.put(text, match);
            }
        }

        if (list.getItemCount() == 0) {
            showMessage(owner, "Info", "No matches available for betting");
            return null;
        }

        final Dialog d = new Dialog(owner, "Select Match", true);
        d.setLayout(new BorderLayout());

        Label countdownLabel = new Label("Starts in: -");
        Panel top = new Panel(new FlowLayout(FlowLayout.LEFT));
        top.add(countdownLabel);

        d.add(top, BorderLayout.NORTH);
        d.add(list, BorderLayout.CENTER);

        final Match[] selectedMatch = new Match[] { null };
        final boolean[] okPressed = new boolean[] { false };

        list.select(0);
        Match initial = map.get(list.getItem(0));
        selectedMatch[0] = initial;

        MatchCountdownTimer timer = new MatchCountdownTimer(countdownLabel, initial.getMatchDate());
        Thread timerThread = new Thread(timer, "MatchCountdownTimer");
        timerThread.setDaemon(true);
        timerThread.start();

        list.addItemListener(e -> {
            String key = list.getSelectedItem();
            if (key == null) return;
            Match m = map.get(key);
            selectedMatch[0] = m;
            timer.setTargetDate(m != null ? m.getMatchDate() : null);
        });

        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER));
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        buttons.add(ok);
        buttons.add(cancel);

        ok.addActionListener(e -> { okPressed[0] = true; d.dispose(); });
        cancel.addActionListener(e -> { okPressed[0] = false; d.dispose(); });

        d.add(buttons, BorderLayout.SOUTH);

        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { okPressed[0] = false; d.dispose(); }
        });

        d.pack();
        d.setLocationRelativeTo(null);
        d.setVisible(true);

        timer.stop();

        if (!okPressed[0]) return null;
        return selectedMatch[0];
    }
    
    public static Team chooseTeamPopup(HashMap<Integer, Team> teams) {
        if (frame == null) throw new IllegalStateException("Main frame was not set");
        if (teams == null || teams.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No teams available");
            return null;
        }

        final Dialog d = new Dialog(frame, "Select Team", true);
        d.setLayout(new BorderLayout());

        List list = new List(10, false);
        HashMap<String, Team> map = new HashMap<>();

        for (Team t : teams.values()) {
            String text = t.getTeamName() + " (ID: " + t.getTeamID() + ")";
            list.add(text);
            map.put(text, t);
        }

        if (list.getItemCount() > 0) list.select(0);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER));
        Button ok = new Button("OK");
        Button cancel = new Button("Cancel");
        buttons.add(ok);
        buttons.add(cancel);

        final Team[] result = new Team[1];

        ok.addActionListener(e -> {
            String key = list.getSelectedItem();
            result[0] = (key != null) ? map.get(key) : null;
            d.dispose();
        });

        cancel.addActionListener(e -> {
        	result[0] = null;
        	d.dispose();
        });

        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { result[0] = null; d.dispose(); }
        });

        d.add(list, BorderLayout.CENTER);
        d.add(buttons, BorderLayout.SOUTH);

        d.pack();
        d.setLocationRelativeTo(frame);
        d.setVisible(true);

        return result[0];
    }

    // ======================== SWING JPANEL WITH JTABLE ==========================
    private static JPanel buildTable(String[] headers, String[][] rows) {
        JTable table = new JTable(rows, headers);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public static JPanel printMatchesGUI(Tournament tournament) {
        String[] cols = {"Match ID", "Home Team", "Away Team", "Date", "Stage", "Home Goals", "Away Goals", "Finished"};
        String[][] rows = new String[tournament.getMatches().size()][cols.length];

        int i = 0;
        for (Match match : tournament.getMatches().values()) {
            GregorianCalendar date = match.getMatchDate();
            String dateStr = String.format("%02d/%02d/%04d",
                    date.get(GregorianCalendar.DAY_OF_MONTH),
                    date.get(GregorianCalendar.MONTH) + 1,
                    date.get(GregorianCalendar.YEAR));

            rows[i][0] = String.valueOf(match.getMatchID());
            rows[i][1] = match.getHomeTeam().getTeamName();
            rows[i][2] = match.getAwayTeam().getTeamName();
            rows[i][3] = dateStr;
            rows[i][4] = match.getStage();
            rows[i][5] = String.valueOf(match.getHomeGoals());
            rows[i][6] = String.valueOf(match.getAwayGoals());
            rows[i][7] = match.getFinished() ? "Yes" : "No";
            i++;
        }

        return buildTable(cols, rows);
    }

    public static JPanel printUsers(HashMap<String, User> users) {
        String[] cols = {"Name", "Email", "Password", "Date of Birth", "Admin"};
        String[][] rows = new String[users.size()][5];

        int i = 0;
        for (User user : users.values()) {
            GregorianCalendar dob = user.getDateOfBirth();
            String dobStr = dob.get(GregorianCalendar.DAY_OF_MONTH) + "/" +
                    (dob.get(GregorianCalendar.MONTH) + 1) + "/" +
                    dob.get(GregorianCalendar.YEAR);

            rows[i][0] = user.getName();
            rows[i][1] = user.getEmail();
            rows[i][2] = user.getPassword();
            rows[i][3] = dobStr;
            rows[i][4] = String.valueOf(user.getIsAdmin());
            i++;
        }

        return buildTable(cols, rows);
    }

    public static JPanel printTournaments(HashMap<Integer, Tournament> tournaments) {
        String[] cols = {"ID", "Name", "Start Date", "End Date", "Winner", "Teams Count"};
        String[][] rows = new String[tournaments.size()][6];

        int i = 0;
        for (Tournament t : tournaments.values()) {
            rows[i][0] = String.valueOf(t.getTournamentID());
            rows[i][1] = t.getTournamentName();
            rows[i][2] = String.format("%02d/%02d/%04d",
                    t.getStartDate().get(GregorianCalendar.DAY_OF_MONTH),
                    t.getStartDate().get(GregorianCalendar.MONTH) + 1,
                    t.getStartDate().get(GregorianCalendar.YEAR));
            rows[i][3] = String.format("%02d/%02d/%04d",
                    t.getEndDate().get(GregorianCalendar.DAY_OF_MONTH),
                    t.getEndDate().get(GregorianCalendar.MONTH) + 1,
                    t.getEndDate().get(GregorianCalendar.YEAR));
            rows[i][4] = (t.getWinner() != null) ? t.getWinner().getTeamName() : "-";
            rows[i][5] = String.valueOf(t.getTeams().size());
            i++;
        }

        return buildTable(cols, rows);
    }

    public static JPanel printTeams(HashMap<Integer, Team> teams) {
        String[] cols = {"ID", "Team Name"};
        String[][] rows = new String[teams.size()][2];

        int i = 0;
        for (Team t : teams.values()) {
            rows[i][0] = String.valueOf(t.getTeamID());
            rows[i][1] = t.getTeamName();
            i++;
        }

        return buildTable(cols, rows);
    }

    public static Panel printLoggedInUserBets(User loggedInUser, Tournament tournament) {
        Panel container = new Panel(new BorderLayout());

        if (loggedInUser == null) {
            Panel p = new Panel();
            p.add(new Label("No user logged in."));
            container.add(p, BorderLayout.CENTER);
            return container;
        }

        int totalPoints = (tournament.getUserTournamentBets().containsKey(loggedInUser.getEmail()))
                ? tournament.getUserTournamentBets().get(loggedInUser.getEmail()).getUserPoints()
                : 0;

        Label top = new Label("Total Points in " + tournament.getTournamentName() + ": " + totalPoints);
        container.add(top, BorderLayout.NORTH);

        HashMap<Match, UserMatchBet> filteredBets = new HashMap<>();
        for (Match match : tournament.getMatches().values()) {
        	UserMatchBet bet = match.getBets().get(loggedInUser);
            if (bet != null) filteredBets.put(match, bet);
        }

        if (filteredBets.isEmpty()) {
            Panel empty = new Panel();
            empty.add(new Label("No bets placed in this tournament."));
            container.add(empty, BorderLayout.CENTER);
            return container;
        }

        String[] cols = {"Match", "Date", "Pred Home", "Pred Away", "Pred Winner", "Pred Diff"};
        String[][] rows = new String[filteredBets.size()][6];

        int i = 0;
        for (Match match : filteredBets.keySet()) {
        	UserMatchBet bet = filteredBets.get(match);

            GregorianCalendar date = match.getMatchDate();
            String dateStr = date.get(GregorianCalendar.DAY_OF_MONTH) + "/" +
                    (date.get(GregorianCalendar.MONTH) + 1) + "/" +
                    date.get(GregorianCalendar.YEAR);

            String winnerStr = bet.isPredWinner() == 1 ? "Home Win" : bet.isPredWinner() == -1 ? "Away Win" : "Draw";

            rows[i][0] = match.getHomeTeam().getTeamName() + " vs " + match.getAwayTeam().getTeamName();
            rows[i][1] = dateStr;
            rows[i][2] = String.valueOf(bet.getPredHomeGoals());
            rows[i][3] = String.valueOf(bet.getPredAwayGoals());
            rows[i][4] = winnerStr;
            rows[i][5] = String.valueOf(bet.getDifference());
            i++;
        }

        container.add(buildTable(cols, rows), BorderLayout.CENTER);
        return container;
    }

    public static JPanel printTournamentStats(Tournament tournament) {
        String[] cols = {"User", "Predicted Winner", "Points"};

        UserTournamentBet[] betsArray = tournament.getUserTournamentBets().values().toArray(new UserTournamentBet[0]);

        for (int i = 0; i < betsArray.length - 1; i++) {
            for (int j = i + 1; j < betsArray.length; j++) {
                if (betsArray[i].getUserPoints() < betsArray[j].getUserPoints()) {
                    UserTournamentBet tmp = betsArray[i];
                    betsArray[i] = betsArray[j];
                    betsArray[j] = tmp;
                }
            }
        }

        Object[][] fullData = new Object[betsArray.length][3];
        for (int i = 0; i < betsArray.length; i++) {
            UserTournamentBet tBet = betsArray[i];
            fullData[i][0] = tBet.getUser().getName() + " (" + tBet.getUser().getEmail() + ")";
            fullData[i][1] = (tBet.getPredictedWinner() != null) ? tBet.getPredictedWinner().getTeamName() : "-";
            fullData[i][2] = tBet.getUserPoints();
        }

        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(sp, BorderLayout.CENTER);

        panel.putClientProperty("statsModel", model);
        panel.putClientProperty("statsData", fullData);

        return panel;
    }

    public static JPanel printGeneralStats(HashMap<Integer, Tournament> tournaments) {
        String[] cols = {"Tournament", "Winner User", "Winner Points", "Total Participants"};
        String[][] rows = new String[tournaments.size()][4];

        int row = 0;
        for (Tournament tournament : tournaments.values()) {
            HashMap<String, UserTournamentBet> betsMap = tournament.getUserTournamentBets();
            UserTournamentBet[] betsArray = betsMap.values().toArray(new UserTournamentBet[0]);

            int maxPoints = -1;
            StringBuilder winnerNames = new StringBuilder();

            for (UserTournamentBet bet : betsArray) {
                int points = bet.getUserPoints();
                String userName = bet.getUser().getName();

                if (points > maxPoints) {
                    maxPoints = points;
                    winnerNames.setLength(0);
                    winnerNames.append(userName);
                } else if (points == maxPoints) {
                    winnerNames.append(", ").append(userName);
                }
            }

            rows[row][0] = tournament.getTournamentName();
            rows[row][1] = (maxPoints >= 0) ? winnerNames.toString() : "-";
            rows[row][2] = (maxPoints >= 0) ? String.valueOf(maxPoints) : "-";
            rows[row][3] = String.valueOf(betsArray.length);
            row++;
        }

        return buildTable(cols, rows);
    }
    
    public static JPanel printTeamStats(Team team, HashMap<TournamentTeamKey, TeamTournamentResult> teamsResults) {

        String[] cols = {"Tournament", "Tournament ID", "Final Result"};
        ArrayList<String[]> rowsList = new ArrayList<>();

        if (teamsResults != null) {
            for (TournamentTeamKey key : teamsResults.keySet()) {
                if (key == null || key.getTeam() == null) continue;
                if (key.getTeam().getTeamID() != team.getTeamID()) continue;

                TeamTournamentResult r = teamsResults.get(key);
                Tournament t = key.getTournament();

                String tournamentName = (t != null) ? t.getTournamentName() : "Unknown";
                String tournamentId = (t != null) ? String.valueOf(t.getTournamentID()) : "-";
                String finalRes = (r != null) ? String.valueOf(r.getStage()) : "-";
                rowsList.add(new String[]{tournamentName, tournamentId, finalRes});
            }
        }

        if (rowsList.isEmpty()) {
            JPanel empty = new JPanel(new BorderLayout());
            empty.add(new JLabel("No results found for team: " + team.getTeamName()), BorderLayout.CENTER);
            return empty;
        }

        String[][] rows = rowsList.toArray(new String[0][0]);
        return buildTable(cols, rows);
    }

    // ======================== USERS TO CHOOSE ==========================
    public static Panel printUsersToChoose(HashMap<String, User> users) {
        Panel inner = new Panel(new GridLayout(0, 1, 4, 4));

        for (String email : users.keySet()) {
            User user = users.get(email);

            Checkbox cb = new Checkbox(user.getName() + " (" + email + ")", user.getIsAdmin());
            if (user.getIsAdmin()) cb.setForeground(Color.GREEN);

            cb.addItemListener(e -> {
                boolean selected = cb.getState();
                user.setIsAdmin(selected);
                cb.setForeground(selected ? Color.GREEN : Color.BLACK);
            });

            inner.add(cb);
        }

        ScrollPane sp = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        sp.add(inner);

        Panel container = new Panel(new BorderLayout());
        container.add(sp, BorderLayout.CENTER);
        return container;
    }

    private static void showMessage(Frame owner, String title, String message) {
        final Dialog d = new Dialog(owner, title, true);
        d.setLayout(new BorderLayout());

        TextArea ta = new TextArea(message, 6, 45, TextArea.SCROLLBARS_VERTICAL_ONLY);
        ta.setEditable(false);
        d.add(ta, BorderLayout.CENTER);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER));
        Button ok = new Button("OK");
        ok.addActionListener(e -> d.dispose());
        buttons.add(ok);
        d.add(buttons, BorderLayout.SOUTH);

        d.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { d.dispose(); }
        });

        d.pack();
        d.setLocationRelativeTo(null);
        d.setVisible(true);
    }
}