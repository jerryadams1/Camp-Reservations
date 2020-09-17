package com.techelevator;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.davidmoten.text.utils.WordWrap;


import com.techelevator.jdbc.JDBCCampgroundDAO;
import com.techelevator.jdbc.JDBCParkDAO;
import com.techelevator.jdbc.JDBCReservationDAO;
import com.techelevator.jdbc.JDBCSiteDAO;
import com.techelevator.view.Menu;
public class CampgroundCLI {
	
	
	private Menu menu = new Menu(System.in, System.out);
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private SiteDAO siteDAO;
	private ReservationsDAO reservationsDAO;

	private static final String PARK_MENU_MAIN = "See All Parks";
	private static final String[] PARK_MENU_CAMPGROUNDS = { "View Campground", "Search for Reservation", "Back" };
	private static final String BACK_BUTTON = "Back";
	private static final String QUIT_MENU = "End Program";

	private static final String[] PARK_MENU_OPTIONS = { PARK_MENU_MAIN, QUIT_MENU };

	private static final String RESERVATION_MENU = "Make a Reservation";
	private static final String[] CAMPGROUND_MENU_OPTIONS = { RESERVATION_MENU, BACK_BUTTON };
	//private static final String[] CAMPGROUND_TO_SITE = {};

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource dataSource) {
		this.menu = new Menu(System.in, System.out);

		parkDAO = new JDBCParkDAO(dataSource);
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
		siteDAO = new JDBCSiteDAO(dataSource);
		reservationsDAO = new JDBCReservationDAO(dataSource);

	}

	public void run() {
		System.out.println("Hello!\n");

		String choice = (String) menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
		if (choice.equals(PARK_MENU_MAIN)) {
			handleParkMenuMain();
		} else if (choice.equals(QUIT_MENU)) {
			System.exit(0);
		}

	}

	private void handleParkMenuMain() {
		System.out.println("\nSelect a National Park for Further Details");

		List<Park> newPark = parkDAO.availableParks();
		String[] newArray = new String[newPark.size() + 1];

		for (int i = 0; i < newPark.size(); i++) {
		
			Park tempPark = newPark.get(i);
			String tempParkName = tempPark.getName();
			newArray[i] = tempParkName;
		}
		newArray[newArray.length - 1] = "Back";
		String choice = (String) menu.getChoiceFromOptions(newArray);

		Park selectedPark = null;
		for (Park park : parkDAO.availableParks())
			if (choice.equals(park.getName())) {
				selectedPark = park;
			}

		if (choice.equals("Acadia") || choice.equals("Arches") || choice.equals("Cuyahoga Valley")) {
			handleCampgroundSelection(selectedPark);
		} else if (choice.equals("Back")) {
			run();
		}

	}

	private void handleCampgroundSelection(Park selectedPark) {
		String desc = selectedPark.getDescription();
		String wrapped = WordWrap.from(desc).maxWidth(100).insertHyphens(true).wrap();

		System.out.println("\nPark Name: " + selectedPark.getName() + " National Park");
		System.out.println("Location: " + selectedPark.getLocation());
		System.out.println("Established: " + selectedPark.getEstablishDate());
		System.out.println("Area: " + selectedPark.getArea());
		System.out.println("Annual Visitors: " + selectedPark.getVisitors() + "\n");
		System.out.println("Park Description: " + wrapped + "\n\n");

		System.out.println("Select an Option\n");

		String choice = (String) menu.getChoiceFromOptions(PARK_MENU_CAMPGROUNDS);
		if (choice.equals("View Campground")) {
			handleViewCampground(selectedPark);
		} else if (choice.equals("Search for Reservation")) {
			handleSearchForReservation(selectedPark);
		} else if (choice.equals("Back")) {
			handleParkMenuMain();
		}

	}

	private void handleSearchForReservation(Park selectedPark) {
		System.out.println("\nSearch for Campground Reservation\n");

		System.out.println(selectedPark.getName() + " National Park");

		List<Campground> newCampground = campgroundDAO.displayCampgroundsByPark(selectedPark.getName());

		int campNum = 0;

		System.out.printf("\n %-1s %-25s \t%10s \t%10s \t%10s\n", "", "Name", "Open", "Close", "Daily Fee");
		for (Campground camp : newCampground) {
			campNum++;
			System.out.printf("\n%-1s %-25s \t%10s \t%10s \t%10s", "#" + camp.getCampgroundId(), camp.getName(),
					Month.of(camp.getOpenFromMonth()), Month.of(camp.getCloseAtMonth()),
					"$" + camp.getDailyFee() + "0");

		}

		int campId = 0;

		Scanner userInput = new Scanner(System.in);
		System.out.println("\n\nSelect a Campground number (0 to go back): ");
		String campgroundInput = userInput.nextLine();

		System.out.println("Enter an arrival date (YYYY/MM/DD): ");
		String fromDate = userInput.nextLine();

		System.out.println("Enter a departure date (YYYY/MM/DD): ");
		String toDate = userInput.nextLine();

		if (campgroundInput.equals("1") || campgroundInput.equals("2") || campgroundInput.equals("3") || campgroundInput.equals("4") || campgroundInput.equals("5") || campgroundInput.equals("6") || campgroundInput.equals("7")) {
			campId = Integer.parseInt(campgroundInput);
			displaySitesByCampground(campId, fromDate, toDate);
		} else if (campgroundInput.equals("0")) {
			handleCampgroundSelection(selectedPark);

		}
	}

	private void displaySitesByCampground(int campId, String fromDate, String toDate) {
		System.out.println("\nResults Matching Your Search Criteria");

		List<Site> allSites = siteDAO.allCampsitePerCampground(campId, fromDate, toDate);

		System.out.println(allSites.size());
		
		System.out.printf("\n %-15s %-15s \t%-15s \t%-15s \t%-15s \n", "Site No.", "Max Occup.", "Accessible?", "RV length", "Utility");
		for (Site site : allSites) {
			System.out.printf("\n%-15s %-15s \t%-15s \t%-15s \t%-15s", site.getSiteNumber(),
					site.getMaxOccupancy(), site.isAccessible(), site.getMaxRvLength(), site.isAccessible());

		}
		Scanner reservationInput = new Scanner (System.in);

		System.out.println("\n\nWhich site should be reserved? (enter 0 to cancel) ");
		String siteReserve = reservationInput.nextLine();
		
		System.out.println("What name should the reservation be made under? ");
		String nameReserve = reservationInput.nextLine();
		
		int x = reservationsDAO.nextReservationId();
		int y = Integer.parseInt(siteReserve);
		
		String fromDateTrans = fromDate.substring(0, 4) + "-" + fromDate.substring(5, 7) + "-" + fromDate.substring(8);
		String toDateTrans = toDate.substring(0, 4) + "-" + toDate.substring(5, 7) + "-" + toDate.substring(8);
		
		
		
		Reservation reservation = new Reservation();
		reservation.setReservationId(x);
		reservation.setSiteId(y);
		reservation.setName(nameReserve);
		reservation.setFromDate(LocalDate.parse(fromDateTrans));	
		reservation.setToDate(LocalDate.parse(toDateTrans));		
		
		
		handleReservations(reservation);
		
	}

	private void handleReservations(Reservation reservation) {
		reservationsDAO.createReservation(reservation);
		System.out.println("Your reservation has been made and the confirmation ID is : " + reservation.getReservationId());
	}

	private void handleViewCampground(Park selectedPark) {
		String campgroundHeader = "\nPARK CAMPGROUNDS\n";
		System.out.println(campgroundHeader);

		System.out.println(selectedPark.getName() + " National Park");

		List<Campground> newCampground = campgroundDAO.displayCampgroundsByPark(selectedPark.getName());

		int campNum = 0;

		System.out.printf("\n %-1s %-25s \t%10s \t%10s \t%10s\n", "", "Name", "Open", "Close", "Daily Fee");
		for (Campground camp : newCampground) {
			campNum++;
			System.out.printf("\n%-1s %-25s \t%10s \t%10s \t%10s", "#" + campNum, camp.getName(),
					Month.of(camp.getOpenFromMonth()), Month.of(camp.getCloseAtMonth()),
					"$" + camp.getDailyFee() + "0");
		}

		System.out.println("");

		String choice = (String) menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);
		if (choice.equals("Make a Reservation")) {
			handleSearchForReservation(selectedPark);
		} else if (choice.equals("Back")) {
			handleCampgroundSelection(selectedPark);
		}

	}

}

