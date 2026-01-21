package worldCupBets;

import javax.swing.JOptionPane;
/*
Project Name: World Cup Bets

Submitted By:
	Idan Lin - 318265378
	Guy Sahar - 206779480


4 Objects:
	1. User
	2. Tournament
	3. Team
	4. Match
	
Class Relationships:
	1. UserMatchBet
	2. UserTournamentBet
	3. TeamTournamentResult
	
Thread Process 1 (Runnable):
	MatchCountdownTimer -> to update match countdowns in GUI (GUI Class->chooseFutureUserBetPopup)
	
Thread Process 2 (Thread Class):
	TournamentStatsRevealThread -> to reveal tournament stats after it ends (GUI->createTournamentStatsItem)

Main System Class:
	MySystem Class: contains main system logic and data structures

Data Storage Class:
	FileManager Class: handles loading and saving data to files
	
GUI Class:
	GUI Class: manages graphical user interface and user interactions
	
Must Have Menu Components (AWT):
	MenuBar - MySystem -> private MenuBar menuBar;
	Menu - "Tournaments" Menu
	MenuItem - "Tournaments" Menu -> "New Tournament" and "View Tournaments"
	CheckboxMenuItem - "Admin Menu"/"User Menu" -> "Show User Info"
	PopupMenu - in the Login frame -> RightClick -> "skip to idan user"
	
Must Have Graphic Components (AWT):
	Button - in AWT Popups -> "OK" or "Cancel"
	Label - in the Login frame -> "Welcome to World Cup Bets System!"
	TextField - GUI -> newUserGUI(String email) -> TextField nameField = new TextField(20);
	TextArea - GUI -> showMessage(Frame owner, String title, String message) -> TextArea ta = new TextArea(message, 6, 45, TextArea.SCROLLBARS_VERTICAL_ONLY);
	Checkbox - GUI -> printUsersToChoose(HashMap<String, User> users) -> Checkbox cb = new Checkbox(user.getName() + " (" + email + ")", user.getIsAdmin());
	CheckboxGroup - GUI -> newMatchGUI -> CheckboxGroup stageGroup = new CheckboxGroup();
	Choice - GUI -> newUserTournamentBetGUI -> Choice winnerChoice = new Choice();
	List - GUI -> chooseTournamentPopup -> List list = new List(10, false);
	
Main Class: 
	Main Class: creates MySystem instance, loads data, runs system, saves data on exit
	
Testing Class: Testing Class: 
*/


public class Main {
	
    public static void main(String[] args) {
    	String[] options = { "Run System", "Run Tests" };

        int choice = JOptionPane.showOptionDialog(
                null,
                "Do you want to run the system or run tests?",
                "Startup Choice",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == 0) {
        	FileManager fileManager = new FileManager(
        			"data/users.txt",
        			"data/teams.txt",
        			"data/tournaments.txt",
        			"data/tournaments_teams.txt",
        			"data/matches.txt",
        			"data/tournament_bets.txt",
        			"data/bets.txt",
        			"data/team_results.txt"
        			);
                MySystem mySystem = fileManager.loadAllData();
                GUI.setFrame(mySystem.getFrame());
                mySystem.run();  
        } else if (choice == 1) {
        	TestConsole testConsole = new TestConsole();
        	testConsole.runTests();
        }
    }  
}