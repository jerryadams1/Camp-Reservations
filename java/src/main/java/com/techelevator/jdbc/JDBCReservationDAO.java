package com.techelevator.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.Reservation;
import com.techelevator.ReservationsDAO;

public class JDBCReservationDAO implements ReservationsDAO {
private JdbcTemplate jdbcTemplate;
public JDBCReservationDAO(DataSource dataSource) {
	jdbcTemplate = new JdbcTemplate(dataSource);
}

	@Override
	public void createReservation(Reservation reservation) {
		reservation.setReservationId(nextReservationId());
		String sql = "INSERT INTO reservation ( site_id, name, from_date, to_date) VALUES(?, ?, ?, ?)";
		jdbcTemplate.update(sql, reservation.getSiteId(),reservation.getName(),reservation.getFromDate(), reservation.getToDate());
		
	}


	@Override
	public int nextReservationId() {
		int nextIdValue = 0;
		SqlRowSet nextId = jdbcTemplate.queryForRowSet("SELECT nextval('reservation_reservation_id_seq')");
		if(nextId.next()) {
			nextIdValue =  nextId.getInt(1);
		} else {
			throw new RuntimeException("Error getting the next Id.");
		}
		
		return nextIdValue;
	}

	@Override
	public List<Reservation> overlapReservations(int CampgroundId, LocalDate fromDate, LocalDate toDate) {
		List<Reservation> overlapReservations = new ArrayList<>();
		String overlappingReserves = "SELECT * FROM reservation R " + 
				"JOIN site S ON R.site_id = S.site_id " + 
				"WHERE S.campground_id = ? " + 
				"AND (from_date, to_date) " + 
				"OVERLAPS (DATE ?, DATE ?)";
		SqlRowSet results = jdbcTemplate.queryForRowSet(overlappingReserves);
		
		while(results.next()) {
			Reservation reservation = mapRowToReservation(results);
			overlapReservations.add(reservation);
		}
		
		return overlapReservations;
	}
	
	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation reservation = new Reservation();
		reservation.setReservationId(results.getInt("reservation_id"));
		reservation.setSiteId(results.getInt("site_id"));
		reservation.setName(results.getString("name"));
		reservation.setFromDate(results.getDate("from_date").toLocalDate());	
		reservation.setToDate(results.getDate("to_date").toLocalDate());
		
		return reservation;
		
	}



}
