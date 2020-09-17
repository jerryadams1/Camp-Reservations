package com.techelevator.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.Park;
import com.techelevator.ParkDAO;

public class JDBCParkDAO implements ParkDAO{
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCParkDAO(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	

	@Override
	public List<Park> availableParks() {
		List<Park> parkList = new ArrayList<>();
		String allParks = "SELECT * FROM park";
		SqlRowSet results = jdbcTemplate.queryForRowSet(allParks);
		
		while(results.next()) {
			Park park = mapRowToPark(results);
			parkList.add(park);
			
		}
		
	
		return parkList;
	}
	
	
	
	
	private Park mapRowToPark(SqlRowSet results) {
		Park park = new Park();
		park.setParkId(results.getInt("park_id"));
		park.setName(results.getString("name"));
		park.setLocation(results.getString("location"));
		park.setEstablishDate(results.getDate("establish_date").toLocalDate());
		park.setArea(results.getInt("area"));
		park.setVisitors(results.getInt("visitors"));
		park.setDescription(results.getString("description"));
		
		
		return park;
	}
	
	

}
