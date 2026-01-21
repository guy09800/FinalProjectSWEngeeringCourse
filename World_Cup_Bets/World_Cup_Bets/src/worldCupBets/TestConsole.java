package worldCupBets;

import java.awt.Label;
import java.util.GregorianCalendar;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;

/*
    Manual console based testing.
    calls to MySystem class methods to simulate user actions.
    print outputs to console for verification.
    uses the same data as in the main system
*/

public class TestConsole {
    private MySystem s;

    // colors for console output
    public static final String RESET  = "\u001B[0m";
    public static final String RED    = "\u001B[31m";
    public static final String GREEN  = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE   = "\u001B[94m";

    public TestConsole() {
        FileManager fileManager = new FileManager(
                "data/test_data/users.txt",
                "data/test_data/teams.txt",
                "data/test_data/tournaments.txt",
                "data/test_data/tournaments_teams.txt",
                "data/test_data/matches.txt",
                "data/test_data/tournament_bets.txt",
                "data/test_data/bets.txt",
                "data/test_data/team_results.txt"
        );
        this.s = fileManager.loadAllData();
    }

    public void runTests() {
    	System.out.println("===========================================");
    	printColored(BLUE, "World Cup Bets - Manual Console Tests");
    	System.out.println("===========================================\n");

    	printColored(BLUE, "Tests to be executed:");
    	System.out.println("1) Load the System from files");
    	System.out.println("2) System Initialization");
    	System.out.println("3) Users: Register + Login (Simulation)");
    	System.out.println("4) Teams: Create + Insert + Fetch (Exam)");
    	System.out.println("5) Matches: Insert + toString + 3s Timer");
    	System.out.println("6) UserMatchBet Flow (3 users, 1 match)");
    	System.out.println("7) Tournament Flow (Register + Match + Final)");
    	System.out.println("8) TeamTournamentResult (Stages + Winner logic)");
    	System.out.println("9) Threads: TournamentStatsRevealThread");
    	printColored(BLUE, "\nProcesses in the Project:");
    	System.out.println( "1) New Match (test 5)");
    	System.out.println( "2) Game Result Updates (test 7)");
    	System.out.println( "3) User Betting on Matches (test 6)");
    	printColored(BLUE, "\nThreads in the Project:");
    	System.out.println( "1) MatchCountdownTimer (test 5)");
    	System.out.println( "2) TournamentStatsRevealThread (test 9)");
        System.out.println("\nStarting tests... but before that, checking system object...");
        
        if (s == null) {
            printColored(RED, "FAIL: System is NULL - cannot run tests");
            return;
        } else {
            printColored(GREEN, "PASS: System is not null");
        }

        testSystemInit();
        testUsers();
        testTeams();
        testMatches();
        testUserMatchBetFlow();
        testTournamentFlow();
        testTeamTournamentResult();
        testThreads();

        System.out.println("\n===========================================");
        printColored(BLUE, "End of World Cup Bets - Manual Console Tests");
        System.out.println("===========================================\n");
    }

    // Helper Method for Tests
    private Team pickAnyTeam() {
        return s.getTeams().values().iterator().next();
    }

    private Tournament pickAnyTournament() {
        return s.getTournaments().values().iterator().next();
    }

    private void printTeamResultsSnapshot(Tournament t) {
        if (s == null || t == null) {
            System.out.println("(snapshot) system or tournament is null");
            return;
        }
        if (s.getTeamResults() == null) {
            System.out.println("(snapshot) system.teamResults is null");
            return;
        }

        boolean any = false;
        for (java.util.Map.Entry<TournamentTeamKey, TeamTournamentResult> e : s.getTeamResults().entrySet()) {
            if (e == null || e.getKey() == null || e.getValue() == null) continue;
            if (e.getValue().getTournament() == t) {
                any = true;
                TeamTournamentResult r = e.getValue();
                System.out.println("- " + r.getTeam().getTeamName() + " => stage='" + r.getStage() + "'");
            }
        }
        if (!any) {
            System.out.println("(snapshot) No TeamTournamentResult entries found for this tournament.");
        }
    }

    public Tournament pickTournamentWithAtLeastOneUnfinishedMatch(MySystem s) {
        if (s == null || s.getTournaments() == null || s.getTournaments().isEmpty()) return null;

        for (Tournament t : s.getTournaments().values()) {
            if (t == null || t.getMatches() == null || t.getMatches().isEmpty()) continue;

            for (Match m : t.getMatches().values()) {
                if (m == null) continue;
                if (!m.getFinished()) return t;
            }
        }
        return null;
    }

    // Local safe sleep for console tests
    private void sleepSafe(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }

    /*
        Verifies system initialization, including creation of core data structures
        and correct loading and printing of initial system inventory.
    */
    private void testSystemInit() {
        printColored(YELLOW, "=============================");
        printColored(YELLOW, "=== System Initialization ===");
        printColored(YELLOW, "=============================");
        printColored(YELLOW, "----- TEST: System Initialization -----");

        System.out.println("Input: MySystem object returned from FileManager.loadAllData()");
        System.out.println("Expected:");
        System.out.println("- Core collections are initialized (not null)");
        System.out.println("- Each core collection contains at least 1 item (per assumption)");
        System.out.println("- Frame exists (optional) and does not crash when accessed");
        System.out.println();

        if (s == null) {
            printColored(RED, "FAIL: MySystem is null");
            return;
        }

        System.out.println("Checks:");

        if (s.getUsers() != null) printColored(GREEN, "PASS: users map initialized");
        else printColored(RED, "FAIL: users map is null");

        if (s.getTeams() != null) printColored(GREEN, "PASS: teams map initialized");
        else printColored(RED, "FAIL: teams map is null");

        if (s.getTournaments() != null) printColored(GREEN, "PASS: tournaments map initialized");
        else printColored(RED, "FAIL: tournaments map is null");

        if (s.getTeamResults() != null) printColored(GREEN, "PASS: teamResults map initialized");
        else printColored(RED, "FAIL: teamResults map is null");

        if (s.getUsers() != null && s.getUsers().size() > 0) printColored(GREEN, "PASS: users count > 0 (" + s.getUsers().size() + ")");
        else printColored(RED, "FAIL: users count is 0 or users map is null");

        if (s.getTeams() != null && s.getTeams().size() > 0) printColored(GREEN, "PASS: teams count > 0 (" + s.getTeams().size() + ")");
        else printColored(RED, "FAIL: teams count is 0 or teams map is null");

        if (s.getTournaments() != null && s.getTournaments().size() > 0) printColored(GREEN, "PASS: tournaments count > 0 (" + s.getTournaments().size() + ")");
        else printColored(RED, "FAIL: tournaments count is 0 or tournaments map is null");

        if (s.getTeamResults() != null && s.getTeamResults().size() > 0) printColored(GREEN, "PASS: teamResults count > 0 (" + s.getTeamResults().size() + ")");
        else printColored(RED, "FAIL: teamResults count is 0 or teamResults map is null");

        int totalMatches = 0;
		if (s.getTournaments() != null) {
			for (Tournament t : s.getTournaments().values()) {
				if (t != null && t.getMatches() != null) {
					totalMatches += t.getMatches().size();
				}
			}
		}
		if (totalMatches > 0)
			printColored(GREEN, "PASS: total matches across tournaments > 0 (" + totalMatches + ")");
		else
			printColored(RED, "FAIL: total matches across tournaments is 0");
        
        
        // Frame availability (optional)
        if (s.getFrame() != null) printColored(GREEN, "PASS: frame exists (not null)");
        else printColored(YELLOW, "NOTE: frame is null (optional)");

        System.out.println();
    }

    /*
        Verifies user registration and login flow, including prevention of duplicate
        users and correct persistence of user data in the system.
    */
    private void testUsers() {

        printColored(YELLOW, "============================================");
        printColored(YELLOW, "=== USERS: Register + Login (Simulation) ===");
        printColored(YELLOW, "============================================");

        printColored(YELLOW, "----- TEST: Register new user (insert into system data) -----");

        int beforeCount = s.getUsers().size();
        System.out.println("Before action: users.size=" + beforeCount);

        String email = "console_new_" + System.currentTimeMillis() + "@example.com";
        String password = "pass123";
        String name = "Console New User";
        GregorianCalendar dob = new GregorianCalendar(1998, 0, 1);

        boolean preNotExists = !s.getUsers().containsKey(email);
        if (preNotExists) printColored(GREEN, "PASS: Precondition email does not exist: " + email);
        else printColored(RED, "FAIL: Precondition email already exists unexpectedly: " + email);

        if (!preNotExists) {
            System.out.println();
            return;
        }

        User newUser = new User(name, email, password, dob, false);
        s.getUsers().put(email, newUser);

        int afterCount = s.getUsers().size();
        boolean exists = s.getUsers().containsKey(email);

        if ((afterCount - beforeCount) == 1) printColored(GREEN, "PASS: users.size increased by 1");
        else printColored(RED, "FAIL: users.size did not increase by 1 (before=" + beforeCount + ", after=" + afterCount + ")");

        if (exists) printColored(GREEN, "PASS: users map contains new email key");
        else printColored(RED, "FAIL: users map does not contain new email key");

        System.out.println("New User Info(using toString()): " + newUser.toString());
        System.out.println();

        // TEST 2: Login simulation and set logged-in user
        printColored(YELLOW, "----- TEST: Login simulation (no GUI) -----");

        System.out.println("Before action: loggedInUser=" + (s.getLoggedInUser() == null ? "null" : s.getLoggedInUser().getEmail()));

        User found = s.getUsers().get(email);
        boolean loginOk = (found != null && found.getPassword().equals(password));

        if (loginOk) {
            s.setLoggedInUser(found);
            printColored(GREEN, "PASS: login SUCCESS (user found + password match)");
        } else {
            printColored(RED, "FAIL: login FAILED (user not found or password mismatch)");
        }

        boolean loggedSet = (s.getLoggedInUser() != null);
        if (loginOk && loggedSet) printColored(GREEN, "PASS: loggedInUser set (not null)");
        else if (loginOk) printColored(RED, "FAIL: login ok but loggedInUser is still null");
        else printColored(YELLOW, "NOTE: loggedInUser not checked because login failed");

        if (loginOk && s.getLoggedInUser() != null) {
            if (email.equals(s.getLoggedInUser().getEmail())) printColored(GREEN, "PASS: loggedInUser email matches expected");
            else printColored(RED, "FAIL: loggedInUser email mismatch (expected=" + email + ", actual=" + s.getLoggedInUser().getEmail() + ")");
        }

        System.out.println("After action: loggedInUser=" + (s.getLoggedInUser() == null ? "null" : s.getLoggedInUser().getEmail()));
        System.out.println();
    }

    /*
        Verifies creation of teams, their registration in the system,
        and correct retrieval from system data structures.
    */
    private void testTeams() {
        printColored(YELLOW, "==============================================");
        printColored(YELLOW, "=== TEAMS: Create + Insert + Fetch (Exam)  ===");
        printColored(YELLOW, "==============================================");
        System.out.println();

        printColored(YELLOW, "----- TEST: Create team + insert into data + fetch -----");

        int beforeCount = s.getTeams().size();
        System.out.println("Before action: teams.size=" + beforeCount);

        String teamName = "ConsoleTeam_" + System.currentTimeMillis();
        System.out.println("Input: teamName=" + teamName);

        Team newTeam = new Team(teamName);
        int teamId = newTeam.getTeamID();

        boolean idNotExists = !s.getTeams().containsKey(teamId);
        if (idNotExists) printColored(GREEN, "PASS: Precondition teamId not in map: " + teamId);
        else printColored(RED, "FAIL: Generated teamId already exists in system map: " + teamId);

        if (!idNotExists) {
            System.out.println();
            return;
        }

        s.getTeams().put(teamId, newTeam);

        int afterInsertCount = s.getTeams().size();

        if ((afterInsertCount - beforeCount) == 1) printColored(GREEN, "PASS: teams.size increased by 1");
        else printColored(RED, "FAIL: teams.size did not increase by 1 (before=" + beforeCount + ", after=" + afterInsertCount + ")");

        printColored(YELLOW, "----- TEST: Fetch inserted team by ID -----");
        Team fetchedTeam = s.getTeams().get(teamId);

        if (fetchedTeam != null) printColored(GREEN, "PASS: fetchedTeam is not null");
        else printColored(RED, "FAIL: fetchedTeam is null");

        if (fetchedTeam != null) {
            if (teamName.equals(fetchedTeam.getTeamName())) printColored(GREEN, "PASS: fetched team name matches expected");
            else printColored(RED, "FAIL: fetched team name mismatch (expected=" + teamName + ", actual=" + fetchedTeam.getTeamName() + ")");
        }

        System.out.println();
    }

    /*
        Verifies the full match lifecycle: creation, addition to a tournament,
        countdown timer execution, result updates, winner determination,
        and transition to finished state.
    */
    private void testMatches() {
        printColored(YELLOW, "====================================================");
        printColored(YELLOW, "=== MATCHES EXAM: Insert + toString + 3s Timer   ===");
        printColored(YELLOW, "====================================================");
        System.out.println();

        Tournament t = pickAnyTournament();
        Team home = pickAnyTeam();
        Team away = pickAnyTeam();

        if (t == null) {
            printColored(RED, "FAIL: Cannot run testMatches: No tournaments in system.");
            System.out.println();
            return;
        } else {
            printColored(GREEN, "PASS: Tournament exists for match test");
        }

        if (home == null || away == null) {
            printColored(RED, "FAIL: Cannot run testMatches: Not enough teams in system.");
            System.out.println();
            return;
        }

        if (home.getTeamID() == away.getTeamID()) {
            for (Team candidate : s.getTeams().values()) {
                if (candidate != null && candidate.getTeamID() != home.getTeamID()) {
                    away = candidate;
                    break;
                }
            }
        }
        if (home.getTeamID() == away.getTeamID()) {
            printColored(RED, "FAIL: Cannot run testMatches: Could not find two different teams.");
            System.out.println();
            return;
        } else {
            printColored(GREEN, "PASS: Two different teams selected (home != away)");
        }

        printColored(YELLOW, "----- TEST 1: Create new Match + insert into tournament data -----");

        int beforeCount = t.getMatches().size();
        System.out.println("Before action: tournament=" + t.getTournamentName() + ", matches.size=" + beforeCount);

        GregorianCalendar matchDate = new GregorianCalendar();
        matchDate.setTimeInMillis(System.currentTimeMillis() + 3000);
        String stage = "ExamStage";

        Match m = new Match(home, away, matchDate, stage);
        int matchId = m.getMatchID();

        t.putMatch(m);

        int afterCount = t.getMatches().size();
        boolean exists = t.getMatches().containsKey(matchId);

        if ((afterCount - beforeCount) == 1) printColored(GREEN, "PASS: matches.size increased by 1");
        else printColored(RED, "FAIL: matches.size did not increase by 1 (before=" + beforeCount + ", after=" + afterCount + ")");

        if (exists) printColored(GREEN, "PASS: match exists in tournament by matchID=" + matchId);
        else printColored(RED, "FAIL: match does not exist in tournament by matchID=" + matchId);

        System.out.println();

        printColored(YELLOW, "----- TEST 2: Fetch match from data and print toString() -----");

        Match fetched = t.getMatches().get(matchId);
        if (fetched != null) printColored(GREEN, "PASS: fetchedMatch is not null");
        else printColored(RED, "FAIL: fetchedMatch is null");

        String str = (fetched == null) ? "null" : fetched.toString();
        boolean okVs = (fetched != null) && str.contains(" vs ");
        boolean okOn = (fetched != null) && str.contains(" on ");

        if (okVs) printColored(GREEN, "PASS: toString contains ' vs '");
        else printColored(RED, "FAIL: toString does not contain ' vs ' -> " + str);

        if (okOn) printColored(GREEN, "PASS: toString contains ' on '");
        else printColored(RED, "FAIL: toString does not contain ' on ' -> " + str);

        System.out.println("toString: " + str);
        System.out.println();

        printColored(YELLOW, "----- TEST 3: MatchCountdownTimer demo (3 seconds) -----");

        Label lbl = new Label();
        MatchCountdownTimer timer = new MatchCountdownTimer(lbl, m.getMatchDate());
        Thread timerThread = new Thread(timer);

        timerThread.start();

        String prev = null;
        boolean changedAtLeastOnce = false;
        for (int i = 1; i <= 3; i++) {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            String nowText = lbl.getText();
            System.out.println("Timer label (t+" + i + "s): " + nowText);
            if (prev != null && nowText != null && !nowText.equals(prev)) changedAtLeastOnce = true;
            prev = nowText;
        }

        if (changedAtLeastOnce) printColored(GREEN, "PASS: Timer label updated over time");
        else printColored(RED, "FAIL: Timer label did not change (or was always null/empty)");

        timer.stop();
        try { timerThread.join(1000); } catch (InterruptedException e) {}

        printColored(GREEN, "PASS: Timer stopped (stop() called, thread joined attempt done)");
        System.out.println();

        printColored(YELLOW, "----- TEST 4: Update match results and set finished=true -----");

        m.setHomeGoals(2);
        m.setAwayGoals(1);
        m.setFinished(true);

        if (m.getHomeGoals() == 2 && m.getAwayGoals() == 1) printColored(GREEN, "PASS: Goals updated correctly (2-1)");
        else printColored(RED, "FAIL: Goals not updated correctly (expected 2-1, actual " + m.getHomeGoals() + "-" + m.getAwayGoals() + ")");

        if (m.getFinished()) printColored(GREEN, "PASS: Match marked as finished=true");
        else printColored(RED, "FAIL: Match finished flag is false");

        System.out.println("Match after update: " + m.toString());
        System.out.println();
    }

    /*
        Verifies end-to-end flow of match betting by multiple users,
        including bet placement, accumulation of bets,
        match completion, score calculation, and user points update.
    */
    private void testUserMatchBetFlow() {
        printColored(YELLOW, "=====================================================");
        printColored(YELLOW, "=== TEST: UserMatchBet Flow (3 users, 1 match)    ===");
        printColored(YELLOW, "=====================================================");
        System.out.println();

        Tournament t = pickTournamentWithAtLeastOneUnfinishedMatch(s);
        if (t == null) {
            printColored(RED, "FAIL: No tournament with unfinished matches found.");
            System.out.println();
            return;
        } else {
            printColored(GREEN, "PASS: Found tournament with unfinished match: " + t.getTournamentName());
        }

        if (t.getMatches() == null || t.getMatches().isEmpty()) {
            printColored(RED, "FAIL: Tournament has no matches.");
            System.out.println();
            return;
        }

        Match match = null;
        for (Match m : t.getMatches().values()) {
            if (m != null && !m.getFinished()) {
                match = m;
                break;
            }
        }

        if (match == null) {
            printColored(RED, "FAIL: Could not fetch an unfinished match.");
            System.out.println();
            return;
        } else {
            printColored(GREEN, "PASS: Picked unfinished match: " + match.toString());
        }

        if (s.getUsers() == null || s.getUsers().size() < 3) {
            printColored(RED, "FAIL: Need at least 3 users in system. users.size=" + (s.getUsers() == null ? "null" : s.getUsers().size()));
            System.out.println();
            return;
        }

        Iterator<User> it = s.getUsers().values().iterator();
        User u1 = it.next();
        User u2 = it.next();
        User u3 = it.next();

        if (u1 != null && u2 != null && u3 != null) printColored(GREEN, "PASS: Selected 3 users");
        else printColored(RED, "FAIL: One of selected users is null");

        if (!t.getUserTournamentBets().containsKey(u1.getEmail()))
            t.getUserTournamentBets().put(u1.getEmail(), new UserTournamentBet(u1, t));
        if (!t.getUserTournamentBets().containsKey(u2.getEmail()))
            t.getUserTournamentBets().put(u2.getEmail(), new UserTournamentBet(u2, t));
        if (!t.getUserTournamentBets().containsKey(u3.getEmail()))
            t.getUserTournamentBets().put(u3.getEmail(), new UserTournamentBet(u3, t));

        boolean utbOk =
                t.getUserTournamentBets().containsKey(u1.getEmail()) &&
                t.getUserTournamentBets().containsKey(u2.getEmail()) &&
                t.getUserTournamentBets().containsKey(u3.getEmail());

        if (utbOk) printColored(GREEN, "PASS: UserTournamentBet exists for 3 users");
        else printColored(RED, "FAIL: Missing UserTournamentBet for one or more users");

        printColored(YELLOW, "----- TEST 1: 3 bets by 3 users + show growth in match.getBets().size -----");

        int beforeSize = match.getBets().size();

        GregorianCalendar now = new GregorianCalendar();
        UserMatchBet bet1 = new UserMatchBet(now, 2, 1);
        UserMatchBet bet2 = new UserMatchBet(now, 1, 0);
        UserMatchBet bet3 = new UserMatchBet(now, 0, 1);

        match.addBet(u1, bet1);
        u1.addBetToBetList(match, bet1);
        if (match.getBets().size() == beforeSize + 1) printColored(GREEN, "PASS: After bet #1, bets.size increased by 1");
        else printColored(RED, "FAIL: After bet #1, bets.size not as expected (expected=" + (beforeSize + 1) + ", actual=" + match.getBets().size() + ")");

        match.addBet(u2, bet2);
        u2.addBetToBetList(match, bet2);
        if (match.getBets().size() == beforeSize + 2) printColored(GREEN, "PASS: After bet #2, bets.size increased by 1");
        else printColored(RED, "FAIL: After bet #2, bets.size not as expected (expected=" + (beforeSize + 2) + ", actual=" + match.getBets().size() + ")");

        match.addBet(u3, bet3);
        u3.addBetToBetList(match, bet3);
        if (match.getBets().size() == beforeSize + 3) printColored(GREEN, "PASS: After bet #3, bets.size increased by 1");
        else printColored(RED, "FAIL: After bet #3, bets.size not as expected (expected=" + (beforeSize + 3) + ", actual=" + match.getBets().size() + ")");

        printColored(YELLOW, "----- TEST 2: Update match result + finished=true + user points accumulation -----");

        if (match.getPoints() == 0) match.setPoints(1);
        int pointsMultiplier = match.getPoints();

        match.setHomeGoals(2);
        match.setAwayGoals(1);
        match.setFinished(true);

        if (match.getFinished()) printColored(GREEN, "PASS: Match marked finished=true");
        else printColored(RED, "FAIL: Match finished flag is false");

        UserMatchBet b1 = match.getBets().get(u1);
        UserMatchBet b2 = match.getBets().get(u2);
        UserMatchBet b3 = match.getBets().get(u3);

        boolean allBetsPresent = (b1 != null && b2 != null && b3 != null);
        if (allBetsPresent) printColored(GREEN, "PASS: 3 bets exist in match.getBets()");
        else printColored(RED, "FAIL: One or more bets missing in match.getBets()");

        int r1 = (b1 == null) ? 0 : b1.getBetResult(match.getHomeGoals(), match.getAwayGoals(), pointsMultiplier);
        int r2 = (b2 == null) ? 0 : b2.getBetResult(match.getHomeGoals(), match.getAwayGoals(), pointsMultiplier);
        int r3 = (b3 == null) ? 0 : b3.getBetResult(match.getHomeGoals(), match.getAwayGoals(), pointsMultiplier);

        boolean orderOk = (r1 >= r2 && r2 >= r3);
        if (orderOk) printColored(GREEN, "PASS: Bet results order looks correct (u1 >= u2 >= u3)");
        else printColored(RED, "FAIL: Unexpected bet results order (u1=" + r1 + ", u2=" + r2 + ", u3=" + r3 + ")");

        int beforeP1 = t.getUserTournamentBets().get(u1.getEmail()).getUserPoints();
        int beforeP2 = t.getUserTournamentBets().get(u2.getEmail()).getUserPoints();
        int beforeP3 = t.getUserTournamentBets().get(u3.getEmail()).getUserPoints();

        t.updateUserPointsAfterMatchFinish(match);

        int afterP1 = t.getUserTournamentBets().get(u1.getEmail()).getUserPoints();
        int afterP2 = t.getUserTournamentBets().get(u2.getEmail()).getUserPoints();
        int afterP3 = t.getUserTournamentBets().get(u3.getEmail()).getUserPoints();

        if ((afterP1 - beforeP1) == r1) printColored(GREEN, "PASS: u1 tournament points delta equals betResult");
        else printColored(RED, "FAIL: u1 points delta mismatch (expected=" + r1 + ", actual=" + (afterP1 - beforeP1) + ")");

        if ((afterP2 - beforeP2) == r2) printColored(GREEN, "PASS: u2 tournament points delta equals betResult");
        else printColored(RED, "FAIL: u2 points delta mismatch (expected=" + r2 + ", actual=" + (afterP2 - beforeP2) + ")");

        if ((afterP3 - beforeP3) == r3) printColored(GREEN, "PASS: u3 tournament points delta equals betResult");
        else printColored(RED, "FAIL: u3 points delta mismatch (expected=" + r3 + ", actual=" + (afterP3 - beforeP3) + ")");

        System.out.println();
        printColored(GREEN, "PASS: UserMatchBet Flow test completed");
        System.out.println();
    }

    /*
        Verifies tournament lifecycle, including registration in the system,
        creation of TeamTournamentResult objects, match progression through stages,
        final match handling, and tournament winner determination.
    */
    private void testTournamentFlow() {

        printColored(YELLOW, "========================================================");
        printColored(YELLOW, "=== TEST: Tournament Flow (Register + Match + Final) ===");
        printColored(YELLOW, "========================================================");
        System.out.println();

        if (s == null) {
            printColored(RED, "FAIL: system is null.");
            System.out.println();
            return;
        }

        if (s.getTeams() == null || s.getTeams().size() < 2) {
            printColored(RED, "FAIL: need at least 2 teams in system. teams.size=" + (s.getTeams() == null ? "null" : s.getTeams().size()));
            System.out.println();
            return;
        }

        if (s.getUsers() == null || s.getUsers().size() < 2) {
            printColored(RED, "FAIL: need at least 2 users in system. users.size=" + (s.getUsers() == null ? "null" : s.getUsers().size()));
            System.out.println();
            return;
        }

        java.util.Iterator<Team> teamIt = s.getTeams().values().iterator();
        Team teamA = teamIt.next();
        Team teamB = teamIt.next();

        if (teamA == null || teamB == null || teamA.getTeamID() == teamB.getTeamID()) {
            printColored(RED, "FAIL: could not fetch two different teams.");
            System.out.println();
            return;
        } else {
            printColored(GREEN, "PASS: Selected two different teams for tournament flow");
        }

        java.util.Iterator<User> userIt = s.getUsers().values().iterator();
        User u1 = userIt.next();
        User u2 = userIt.next();

        if (u1 == null || u2 == null || u1.getEmail().equals(u2.getEmail())) {
            printColored(RED, "FAIL: could not fetch two different users.");
            System.out.println();
            return;
        } else {
            printColored(GREEN, "PASS: Selected two different users for tournament flow");
        }

        printColored(YELLOW, "----- TEST 1: Create tournament + add teams + SystemActions.registerTournament -----");

        int beforeTournaments = s.getTournaments().size();
        int beforeTeamResults = s.getTeamResults().size();

        String tournamentName = "ConsoleTournament_" + System.currentTimeMillis();
        java.util.GregorianCalendar start = new java.util.GregorianCalendar();
        java.util.GregorianCalendar end = new java.util.GregorianCalendar();
        end.setTimeInMillis(start.getTimeInMillis() + (7L * 24 * 60 * 60 * 1000));

        Tournament t = new Tournament(tournamentName, start, end);
        t.getTeams().put(teamA.getTeamID(), teamA);
        t.getTeams().put(teamB.getTeamID(), teamB);

        s.registerTournament(t);

        int afterTournaments = s.getTournaments().size();
        int afterTeamResults = s.getTeamResults().size();

        if ((afterTournaments - beforeTournaments) == 1) printColored(GREEN, "PASS: tournaments.size increased by 1");
        else printColored(RED, "FAIL: tournaments.size delta not 1 (before=" + beforeTournaments + ", after=" + afterTournaments + ")");

        boolean tournamentExists = s.getTournaments().containsKey(t.getTournamentID());
        if (tournamentExists) printColored(GREEN, "PASS: tournament exists in system map by ID");
        else printColored(RED, "FAIL: tournament not found in system map by ID");

        if ((afterTeamResults - beforeTeamResults) >= 2) printColored(GREEN, "PASS: teamResults increased (expected at least 2 for two teams)");
        else printColored(RED, "FAIL: teamResults did not increase as expected (before=" + beforeTeamResults + ", after=" + afterTeamResults + ")");

        System.out.println();

        printColored(YELLOW, "----- TEST 2: Add match to tournament + fetch toString -----");

        int beforeMatches = t.getMatches().size();

        java.util.GregorianCalendar matchDate1 = new java.util.GregorianCalendar();
        matchDate1.setTimeInMillis(System.currentTimeMillis() + 3000);
        Match m1 = new Match(teamA, teamB, matchDate1, "Group", 2);
        t.putMatch(m1);

        int afterMatches = t.getMatches().size();
        if ((afterMatches - beforeMatches) == 1) printColored(GREEN, "PASS: tournament.matches.size increased by 1");
        else printColored(RED, "FAIL: tournament.matches.size delta not 1 (before=" + beforeMatches + ", after=" + afterMatches + ")");

        Match fetchedM1 = t.getMatches().get(m1.getMatchID());
        if (fetchedM1 != null) printColored(GREEN, "PASS: fetched match by ID is not null");
        else printColored(RED, "FAIL: fetched match by ID is null");

        if (fetchedM1 != null) {
            String ts = fetchedM1.toString();
            if (ts.contains(" vs ") && ts.contains(" on ")) printColored(GREEN, "PASS: match.toString contains 'vs' and 'on'");
            else printColored(RED, "FAIL: match.toString missing expected tokens -> " + ts);
        }

        System.out.println();

        printColored(YELLOW, "----- TEST 3: Place match bets + finish match + update user points (afterMatchFinished) -----");

        if (!t.getUserTournamentBets().containsKey(u1.getEmail()))
            t.getUserTournamentBets().put(u1.getEmail(), new UserTournamentBet(u1, t));
        if (!t.getUserTournamentBets().containsKey(u2.getEmail()))
            t.getUserTournamentBets().put(u2.getEmail(), new UserTournamentBet(u2, t));

        int beforeP1 = t.getUserTournamentBets().get(u1.getEmail()).getUserPoints();
        int beforeP2 = t.getUserTournamentBets().get(u2.getEmail()).getUserPoints();

        java.util.GregorianCalendar now = new java.util.GregorianCalendar();
        UserMatchBet betU1 = new UserMatchBet(now, 2, 1);
        UserMatchBet betU2 = new UserMatchBet(now, 1, 0);

        MySystem.placeBet(u1, m1, betU1);
        MySystem.placeBet(u2, m1, betU2);

        if (m1.getBets().size() >= 2) printColored(GREEN, "PASS: matchBets.size grew after placing bets");
        else printColored(RED, "FAIL: matchBets.size did not grow as expected (size=" + m1.getBets().size() + ")");

        m1.setHomeGoals(2);
        m1.setAwayGoals(1);
        m1.setFinished(true);

        s.afterMatchFinished(t, m1);

        int afterP1 = t.getUserTournamentBets().get(u1.getEmail()).getUserPoints();
        int afterP2 = t.getUserTournamentBets().get(u2.getEmail()).getUserPoints();

        if (afterP1 > beforeP1) printColored(GREEN, "PASS: u1 points increased after match finished");
        else printColored(RED, "FAIL: u1 points did not increase (before=" + beforeP1 + ", after=" + afterP1 + ")");

        if (afterP2 > beforeP2) printColored(GREEN, "PASS: u2 points increased after match finished");
        else printColored(RED, "FAIL: u2 points did not increase (before=" + beforeP2 + ", after=" + afterP2 + ")");

        System.out.println();

        printColored(YELLOW, "----- TEST 4: Final match -> set tournament winner + winner-bonus (+10) + TeamResults final update -----");

        Team predictedWinnerU1 = teamA;
        Team predictedWinnerU2 = teamB;

        t.getUserTournamentBets().get(u1.getEmail()).setPredictedWinner(predictedWinnerU1);
        t.getUserTournamentBets().get(u2.getEmail()).setPredictedWinner(predictedWinnerU2);

        int beforeP1Final = t.getUserTournamentBets().get(u1.getEmail()).getUserPoints();
        int beforeP2Final = t.getUserTournamentBets().get(u2.getEmail()).getUserPoints();

        java.util.GregorianCalendar matchDate2 = new java.util.GregorianCalendar();
        matchDate2.setTimeInMillis(System.currentTimeMillis() + 3000);
        Match finalMatch = new Match(teamA, teamB, matchDate2, "Final", 3);
        t.putMatch(finalMatch);

        finalMatch.setHomeGoals(1);
        finalMatch.setAwayGoals(0);
        finalMatch.setFinished(true);

        s.afterMatchFinished(t, finalMatch);

        if (t.getWinner() != null && t.getWinner().getTeamID() == teamA.getTeamID()) printColored(GREEN, "PASS: tournament winner set correctly to teamA");
        else printColored(RED, "FAIL: tournament winner not set correctly (winner=" + (t.getWinner() == null ? "null" : t.getWinner().getTeamName()) + ")");

        int afterP1Final = t.getUserTournamentBets().get(u1.getEmail()).getUserPoints();
        int afterP2Final = t.getUserTournamentBets().get(u2.getEmail()).getUserPoints();

        if (afterP1Final > beforeP1Final) printColored(GREEN, "PASS: u1 points increased after final");
        else printColored(RED, "FAIL: u1 points did not increase after final");

        if (afterP2Final >= beforeP2Final) printColored(GREEN, "PASS: u2 points not decreased after final");
        else printColored(RED, "FAIL: u2 points decreased unexpectedly after final");

        System.out.println();
        printColored(GREEN, "PASS: Tournament Flow test completed");
        System.out.println();
    }

    /*
        Verifies correct computation of tournament stages for each team
        (Semifinal, Final, Winner) based on match outcomes,
        ensuring the highest elimination stage is correctly selected.
    */
    private void testTeamTournamentResult() {

        printColored(YELLOW, "==================================================================");
        printColored(YELLOW, "=== TEST: TeamTournamentResult (4 teams, tie/loss/win, winner) ===");
        printColored(YELLOW, "==================================================================");
        System.out.println();

        if (s == null) {
            printColored(RED, "FAIL: system is null.");
            System.out.println();
            return;
        }
        if (s.getTournaments() == null || s.getTournaments().isEmpty()) {
            printColored(RED, "FAIL: no tournaments in system.");
            System.out.println();
            return;
        }

        printColored(YELLOW, "----- TEST 1: Pick existing tournament + ensure 4 teams -----");

        Tournament t = pickAnyTournament();
        if (t == null) {
            printColored(RED, "FAIL: pickAnyTournament returned null");
            System.out.println();
            return;
        } else {
            printColored(GREEN, "PASS: picked tournament: " + t.getTournamentName());
        }

        printColored(YELLOW, "----- Reset tournament matches before running this test -----");

        if (t.getMatches() != null) {
            t.getMatches().clear();
            if (t.getMatches().size() == 0) printColored(GREEN, "PASS: tournament matches cleared");
            else printColored(RED, "FAIL: tournament matches not cleared");
        } else {
            printColored(RED, "FAIL: tournament.getMatches() is null");
            System.out.println();
            return;
        }

        if (t.getTeams() == null) {
            printColored(RED, "FAIL: tournament.getTeams() is null");
            System.out.println();
            return;
        }

        int beforeTeams = t.getTeams().size();

        java.util.Iterator<Team> sysTeamsIt = s.getTeams().values().iterator();
        while (t.getTeams().size() < 4 && sysTeamsIt.hasNext()) {
            Team candidate = sysTeamsIt.next();
            if (candidate == null) continue;
            if (!t.getTeams().containsKey(candidate.getTeamID())) {
                t.getTeams().put(candidate.getTeamID(), candidate);
            }
        }
        while (t.getTeams().size() < 4) {
            Team newTeam = new Team("ConsoleAutoTeam_" + System.currentTimeMillis() + "_" + t.getTeams().size());
            s.getTeams().put(newTeam.getTeamID(), newTeam);
            t.getTeams().put(newTeam.getTeamID(), newTeam);
        }

        int afterTeams = t.getTeams().size();
        if (afterTeams == 4) printColored(GREEN, "PASS: tournament has exactly 4 teams");
        else printColored(RED, "FAIL: tournament does not have 4 teams (size=" + afterTeams + ")");

        System.out.println("Teams delta: " + (afterTeams - beforeTeams));
        System.out.println();

        int beforeResults = s.getTeamResults().size();
        s.registerTournament(t);
        int afterResults = s.getTeamResults().size();

        if (afterResults >= beforeResults) printColored(GREEN, "PASS: registerTournament executed (teamResults size did not shrink)");
        else printColored(RED, "FAIL: teamResults size decreased unexpectedly");

        System.out.println("Team results snapshot for this tournament (before any matches):");
        printTeamResultsSnapshot(t);
        System.out.println();

        java.util.Iterator<Team> it4 = t.getTeams().values().iterator();
        Team team1 = it4.next();
        Team team2 = it4.next();
        Team team3 = it4.next();
        Team team4 = it4.next();

        printColored(YELLOW, "----- TEST 2: Run matches (Tie / Loss / Win) + print stages + winners -----");

        Match groupTie = new Match(team1, team2, new GregorianCalendar(), "Group", 1);
        t.putMatch(groupTie);
        groupTie.setHomeGoals(1);
        groupTie.setAwayGoals(1);
        groupTie.setFinished(true);

        if (groupTie.getWinner() == null) printColored(GREEN, "PASS: Group tie winner is null");
        else printColored(RED, "FAIL: Group tie winner should be null, got " + groupTie.getWinner().getTeamName());

        Match semi1 = new Match(team1, team3, new GregorianCalendar(), "Semifinal", 2);
        t.putMatch(semi1);
        semi1.setHomeGoals(0);
        semi1.setAwayGoals(2);
        semi1.setFinished(true);

        if (semi1.getWinner() != null && semi1.getWinner().getTeamID() == team3.getTeamID()) printColored(GREEN, "PASS: Semifinal #1 winner is team3");
        else printColored(RED, "FAIL: Semifinal #1 winner mismatch");

        Match semi2 = new Match(team4, team2, new GregorianCalendar(), "Semifinal", 2);
        t.putMatch(semi2);
        semi2.setHomeGoals(3);
        semi2.setAwayGoals(1);
        semi2.setFinished(true);

        if (semi2.getWinner() != null && semi2.getWinner().getTeamID() == team4.getTeamID()) printColored(GREEN, "PASS: Semifinal #2 winner is team4");
        else printColored(RED, "FAIL: Semifinal #2 winner mismatch");

        Match groupLoss = new Match(team2, team3, new GregorianCalendar(), "Group", 1);
        t.putMatch(groupLoss);
        groupLoss.setHomeGoals(0);
        groupLoss.setAwayGoals(1);
        groupLoss.setFinished(true);

        if (groupLoss.getWinner() != null && groupLoss.getWinner().getTeamID() == team3.getTeamID()) printColored(GREEN, "PASS: Group loss winner is team3");
        else printColored(RED, "FAIL: Group loss winner mismatch");

        Match finalMatch = new Match(team3, team4, new GregorianCalendar(), "Final", 3);
        t.putMatch(finalMatch);
        finalMatch.setHomeGoals(2);
        finalMatch.setAwayGoals(0);
        finalMatch.setFinished(true);

        if (finalMatch.getWinner() != null && finalMatch.getWinner().getTeamID() == team3.getTeamID()) printColored(GREEN, "PASS: Final match winner is team3");
        else printColored(RED, "FAIL: Final match winner mismatch");

        printColored(YELLOW, "----- TEST 3: afterMatchFinished(Final) => set tournament winner + finalize TeamTournamentResult stages -----");

        s.afterMatchFinished(t, finalMatch);

        if (t.getWinner() != null && t.getWinner().getTeamID() == team3.getTeamID()) printColored(GREEN, "PASS: Tournament winner set to team3 after final");
        else printColored(RED, "FAIL: Tournament winner not set correctly after final");

        System.out.println("TeamTournamentResult snapshot (FINAL stages):");
        printTeamResultsSnapshot(t);

        System.out.println();
        printColored(GREEN, "PASS: TeamTournamentResult test completed");
        System.out.println();
    }

    /*
        Verifies asynchronous execution of TournamentStatsRevealThread,
        demonstrating parallel execution with the main thread and
        gradual, time-controlled reveal of tournament results.
    */
    private void testThreads() {

        printColored(YELLOW, "==============================================");
        printColored(YELLOW, "=== THREADS: TournamentStatsRevealThread   ===");
        printColored(YELLOW, "==============================================");
        System.out.println();

        printColored(YELLOW, "----- TEST: Pick tournament with winner (finalized tournament) -----");

        Tournament chosen = null;
        for (Tournament t : s.getTournaments().values()) {
            if (t != null && t.getWinner() != null) {
                chosen = t;
                break;
            }
        }

        if (chosen == null) {
            printColored(RED, "FAIL: No tournament with winner found.");
            printColored(YELLOW, "NOTE: Run testTournamentFlow before this test OR ensure at least one tournament ends with a Final.");
            System.out.println();
            return;
        } else {
            printColored(GREEN, "PASS: Chosen tournament has winner: " + chosen.getTournamentName());
        }

        printColored(YELLOW, "----- TEST: Build reveal data from TeamTournamentResult -----");

        int count = 0;
        for (TeamTournamentResult r : s.getTeamResults().values()) {
            if (r != null && r.getTournament() == chosen) count++;
        }

        if (count > 0) printColored(GREEN, "PASS: Found TeamTournamentResult entries for chosen tournament (" + count + ")");
        else {
            printColored(RED, "FAIL: No TeamTournamentResult entries for chosen tournament.");
            System.out.println();
            return;
        }

        int rowsToShow = Math.min(4, count);

        Object[][] data = new Object[rowsToShow][2];
        int idx = 0;

        for (TeamTournamentResult r : s.getTeamResults().values()) {
            if (r == null) continue;
            if (r.getTournament() != chosen) continue;

            data[idx][0] = r.getTeam().getTeamName();
            data[idx][1] = r.getStage();

            idx++;
            if (idx >= rowsToShow) break;
        }

        if (idx == rowsToShow) printColored(GREEN, "PASS: Prepared reveal data rows=" + rowsToShow);
        else printColored(RED, "FAIL: Prepared fewer rows than expected (expected=" + rowsToShow + ", actual=" + idx + ")");

        printColored(YELLOW, "----- TEST: Start TournamentStatsRevealThread (gradual reveal) -----");

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Team", "Stage"}, 0);

        TournamentStatsRevealThread thread = new TournamentStatsRevealThread(model, data);

        thread.start();

        sleepSafe(100);
        if (thread.isAlive()) printColored(GREEN, "PASS: thread is alive shortly after start");
        else printColored(RED, "FAIL: thread not alive shortly after start");

        int last = model.getRowCount();
        boolean grewAtLeastOnce = false;

        for (int i = 1; i <= rowsToShow + 1; i++) {
            sleepSafe(1100);
            int now = model.getRowCount();
            if (now > last) grewAtLeastOnce = true;
            last = now;
        }

        if (grewAtLeastOnce) printColored(GREEN, "PASS: model rowCount increased gradually (at least once)");
        else printColored(RED, "FAIL: model rowCount did not increase during polling");

        try {
            thread.join(6000);
        } catch (InterruptedException ignored) {}

        sleepSafe(200);

        if (!thread.isAlive()) printColored(GREEN, "PASS: thread finished (isAlive=false)");
        else printColored(RED, "FAIL: thread still alive after join timeout");

        if (model.getRowCount() == rowsToShow) printColored(GREEN, "PASS: final model.rows equals expected (" + rowsToShow + ")");
        else printColored(RED, "FAIL: final model.rows mismatch (expected=" + rowsToShow + ", actual=" + model.getRowCount() + ")");

        System.out.println();
        System.out.println("Final table content:");
        for (int r = 0; r < model.getRowCount(); r++) {
            System.out.println("- Row " + r + ": Team=" + model.getValueAt(r, 0) + ", Stage=" + model.getValueAt(r, 1));
        }

        System.out.println();
        printColored(GREEN, "PASS: Thread test completed");
        System.out.println();
    }

    private void printColored(String color, String text) {
        System.out.println(color + text + RESET);
    }
}
