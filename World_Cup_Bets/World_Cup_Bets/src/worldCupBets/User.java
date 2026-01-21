package worldCupBets;

import java.util.GregorianCalendar;
import java.util.HashMap;

public class User {
	private String name;
	private String email;
	private String password;
	private GregorianCalendar dateOfBirth;
	private HashMap<Match, UserMatchBet> bets;
	private Boolean isAdmin = false;
	
	public User(String name, String email, String password, GregorianCalendar dateOfBirth) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.dateOfBirth = dateOfBirth;
		this.bets = new HashMap<Match, UserMatchBet>();
		this.isAdmin = false;
	}
	
	public User(String name, String email, String password, GregorianCalendar dateOfBirth, Boolean isAdmin) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.dateOfBirth = dateOfBirth;
		this.bets = new HashMap<Match, UserMatchBet>();
		this.isAdmin = isAdmin;
	}
	
	public User(String email, String password) {
		this.email = email;
		this.password = password;
		this.bets = new HashMap<Match, UserMatchBet>();
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public GregorianCalendar getDateOfBirth() {
		return dateOfBirth;
	}
	
	public HashMap<Match, UserMatchBet> getBets() {
		return bets;
	}
	
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setDateOfBirth(GregorianCalendar dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	public void addBetToBetList(Match match, UserMatchBet bet) {
		bets.put(match, bet);
	}
	
	public static boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		return email.matches(emailRegex);
	}
	
	public static boolean isValidPassword(String password) {
		return password.length() >= 6;
	}
	
	public static boolean isValidName(String name) {
		return name != null && !name.trim().isEmpty();
	}
	
	public static boolean isValidDateOfBirth(GregorianCalendar dateOfBirth) {
		GregorianCalendar today = new GregorianCalendar();
		int age = today.get(GregorianCalendar.YEAR) - dateOfBirth.get(GregorianCalendar.YEAR);
		if (today.get(GregorianCalendar.DAY_OF_YEAR) < dateOfBirth.get(GregorianCalendar.DAY_OF_YEAR)) {
			age--;
		}
		return age >= 18;
	}
	
	public String toString() {
		return "Name: " + name + ", Email: " + email + ", Date of Birth: "
				+ dateOfBirth.get(GregorianCalendar.DAY_OF_MONTH) + "/" + (dateOfBirth.get(GregorianCalendar.MONTH) + 1)
				+ "/" + dateOfBirth.get(GregorianCalendar.YEAR);
	}
}
