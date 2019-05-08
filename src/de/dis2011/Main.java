package de.dis2011;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.ibm.db2.jcc.c.m;

import de.dis2011.data.Contract;
import de.dis2011.data.Estate;
import de.dis2011.data.EstateAgent;
import de.dis2011.data.Person;

/**
 * Hauptklasse
 */
public class Main {
	
	public static final String ESTATE_AGENT_MANAGEMENT_PASSWORD = "admin";
	
	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {
		showMainMenu();
	}
	
	/**
	 * Zeigt das Hauptmenü
	 */
	public static void showMainMenu() {
		//Menüoptionen
		final int MENU_ESTATE_AGENT_MANAGEMENT_LOGIN = 0;
		final int MENU_ESTATE_AGENTS_LOGIN = 1;
		final int MENU_CONTRACT_MANAGEMENT = 2;
		final int QUIT = 3;
		
		//Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("Estate Agent Management", MENU_ESTATE_AGENT_MANAGEMENT_LOGIN);
		mainMenu.addEntry("Estate Agent Login", MENU_ESTATE_AGENTS_LOGIN);
		mainMenu.addEntry("Contract Management", MENU_CONTRACT_MANAGEMENT);
		mainMenu.addEntry("Beenden", QUIT);
		
		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();
			
			switch(response) {
				case MENU_ESTATE_AGENT_MANAGEMENT_LOGIN:
					estateAgentManagementLogin();
					break;
				case MENU_ESTATE_AGENTS_LOGIN:
					estateAgentLogin();
				case MENU_CONTRACT_MANAGEMENT:
					showContractManagementMenu();
					break;
				case QUIT:
					return;
			}
		}
	}
	
	/**
	 * Zeigt die Maklerverwaltung
	 */
	public static void showMaklerMenu() {
		//Menüoptionen
		final int NEW_MAKLER = 0;
		final int EDIT_MAKLER = 1;
		final int DELETE_MAKLER = 2;
		final int BACK = 3;
		
		//Maklerverwaltungsmenü
		Menu maklerMenu = new Menu("Makler-Verwaltung");
		maklerMenu.addEntry("Neuer Makler", NEW_MAKLER);
		maklerMenu.addEntry("Markler editieren", EDIT_MAKLER);
		maklerMenu.addEntry("Markler löschen", DELETE_MAKLER);
		maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);
		
		//Verarbeite Eingabe
		while(true) {
			int response = maklerMenu.show();
			
			switch(response) {
				case NEW_MAKLER:
					newMakler();
					break;
				case EDIT_MAKLER:
					editMakler();
					break;
				case DELETE_MAKLER:
					deleteMakler();
					break;
				case BACK:
					showMainMenu();
					return;
			}
		}
	}
	
	public static void showEstateManagementMenu() {
		final int NEW_ESTATE = 0;
		final int EDIT_ESTATE = 1;
		final int DELETE_ESTATE = 2;
		final int BACK = 3;
		
		Menu estateManagementMenu = new Menu("Estage Management Menu");
		estateManagementMenu.addEntry("New Estate", NEW_ESTATE);
		estateManagementMenu.addEntry("Edit Estate", EDIT_ESTATE);
		estateManagementMenu.addEntry("Delete Estate", DELETE_ESTATE);
		estateManagementMenu.addEntry("Back to main menu", BACK);
		
		while(true) {
			int response = estateManagementMenu.show();
			
			switch(response) {
			case NEW_ESTATE:
				newEstate();
				break;
			case EDIT_ESTATE:
				editEstate();
				break;
			case DELETE_ESTATE:
				deleteEstate();
				break;
			case BACK:
				showMainMenu();
			}
		}
	}
	
	public static void showContractManagementMenu() {
		final int ADD_PERSON = 0;
		final int SIGN_CONTRACT = 1;
		final int CONTRACT_OVERVIEW = 2;
		final int BACK = 3; 
		
		Menu contractManagementMenu = new Menu("Contract Management Menu");
		contractManagementMenu.addEntry("Add Person", ADD_PERSON);
		contractManagementMenu.addEntry("Sign Contract", SIGN_CONTRACT);
		contractManagementMenu.addEntry("Contract Overview", CONTRACT_OVERVIEW);
		contractManagementMenu.addEntry("Back to main menu", BACK);
		
		while(true) {
			int response = contractManagementMenu.show();
			
			switch(response) {
			case ADD_PERSON:
				addPerson();
				break;
			case SIGN_CONTRACT:
				signContract();
				break;
			case CONTRACT_OVERVIEW:
				contractOverview();
			case BACK:
				showMainMenu();
				break;
			}
		}

	}
	
	/**
	 * Legt einen neuen Makler an, nachdem der Benutzer
	 * die entprechenden Daten eingegeben hat.
	 */
	public static void newMakler() {
		EstateAgent m = new EstateAgent();
		
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Address"));
		m.setLogin(FormUtil.readString("Login"));
		m.setPassword(FormUtil.readString("Password"));
		m.save();
		
		System.out.println("Makler mit der ID "+m.getId()+" wurde erzeugt.");
	}
	
	public static void editMakler() {
		EstateAgent m = EstateAgent.load(FormUtil.readInt("ID"));
		System.out.println("Press Enter to keep current value");
		
		String name = FormUtil.readString("Name");
		String address = FormUtil.readString("Address");
		String login = FormUtil.readString("Login");
		String password = FormUtil.readString("Password");

		m.setName(name == "" ? m.getName() : name);
		m.setAddress(address == "" ? m.getAddress() : address);
		m.setLogin(login == "" ? m.getLogin() : login);
		m.setPassword(password == "" ? m.getPassword() : password);
		m.save();
		
		System.out.println("Makler mit der ID "+m.getId()+" wurde editiert.");
	}
	
	public static void deleteMakler() {
		EstateAgent m = EstateAgent.load(FormUtil.readInt("ID"));
		m.delete();
		
		System.out.println("Makler mit der ID "+m.getId()+" wurde gelöscht.");
	}
	
	public static void estateAgentManagementLogin() {
		System.out.println("Please enter the password for the estate agent management");
		if (FormUtil.readString("Password").equals(ESTATE_AGENT_MANAGEMENT_PASSWORD)) {
			showMaklerMenu();
		} else {
			System.out.println("The password is not correct. You will be redirected to the main menu");
			showMainMenu();
		}
	}
	
	public static void estateAgentLogin() {
		EstateAgent m = EstateAgent.load(FormUtil.readInt("Enter Estate Agent Login"));
		
		if (FormUtil.readString("Enter Password for Estate Agent " + m.getLogin()).equals(m.getPassword())) {
			showEstateManagementMenu();
		} else {
			System.out.println("The password is not correct. You will be redirected to the main menu");
			showMainMenu();
		}
	}
	
	public static void newEstate() {
		Estate m = new Estate();
		
		m.setCity(FormUtil.readString("City"));
		m.setEstateAgentLogin(FormUtil.readString("Estate Agent Login"));
		m.setPostalCode(FormUtil.readInt("Postal Code"));
		m.setSquareArea(FormUtil.readInt("Square Area"));
		m.setStreet(FormUtil.readString("Street"));
		m.setStreetNumber(FormUtil.readInt("Street Number"));
		m.save();
		
		System.out.println("Estate with ID "+m.getId()+" has been created successfully.");
		
	}
	
	public static void editEstate() {
		Estate m = Estate.load(FormUtil.readInt("ID"));
		
		System.out.println("Press Enter to ceep current value");
		
		String city = FormUtil.readString("City");
		String estateAgentLogin = FormUtil.readString("Estate Agent Login");
		int postalCode = FormUtil.readIntWithNull("Postal Code");
		String street = FormUtil.readString("Street");
		int squareArea = FormUtil.readIntWithNull("Square Area");
		int streetNumber = FormUtil.readIntWithNull("Street Number");
		
		m.setCity(city == "" ? m.getCity() : city);
		m.setEstateAgentLogin(estateAgentLogin == "" ? m.getEstateAgentLogin() : estateAgentLogin);
		m.setPostalCode(postalCode == -1 ? m.getPostalCode() : postalCode);
		m.setCity(street == "" ? m.getStreet() : street);
		m.setStreetNumber(streetNumber == -1 ? m.getStreetNumber() : streetNumber);
		m.setSquareArea(squareArea == -1 ? m.getSquareArea() : squareArea);
		m.save();
		
		System.out.println("Estate with ID "+m.getId()+" has been updated successfully.");
	}
	
	public static void deleteEstate() {
		Estate m = Estate.load(FormUtil.readInt("ID"));
		m.delete();
		
		System.out.println("Estate with ID "+m.getId()+" has been deleted successfully.");
	}
	
	public static void addPerson() {
		Person m = new Person();
		
		m.setFirstName(FormUtil.readString("First Name"));
		m.setName(FormUtil.readString("Name"));
		m.setAddress(FormUtil.readString("Address"));
		
		m.save();
		
		System.out.println("Person with ID "+m.getId()+" has been added successfully.");
	}
	
	public static void signContract() {
		Contract m = new Contract();
				
		m.setContractNumber(FormUtil.readInt("Contract Number"));
		m.setPlace(FormUtil.readString("Place"));
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
		Date date;
		
		try {
			date = simpleDateFormat.parse(FormUtil.readString("Start Date (mm-dd-yyy)"));
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			m.setStartDate(sqlDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		m.save();
		
		System.out.println("Contract with ID "+m.getId()+" has been signed successfully.");
	}
	
	public static void contractOverview() {
		ArrayList<Contract> allContracts = Contract.getAll();
		
		System.out.println("Contract Overview \n");
		
		allContracts.forEach((contract) -> {
			System.out.println(contract.getId() + " " + contract.getContractNumber()
			+ " " + contract.getStartDate().toGMTString() + " " + contract.getPlace());
		});
	}
}
