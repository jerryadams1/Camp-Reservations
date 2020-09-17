package com.techelevator;

import java.time.LocalDate;
import java.util.List;

public interface ReservationsDAO {
	
	public void createReservation(Reservation reservation);
	public List<Reservation> overlapReservations(int CampgroundId, LocalDate fromDate, LocalDate toDate);
	public int nextReservationId();

}
