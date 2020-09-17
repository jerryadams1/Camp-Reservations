package com.techelevator.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.Campground;
import com.techelevator.CampgroundDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {
	private JdbcTemplate jdbcTemplate;
	
	public JDBCCampgroundDAO(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	

	@Override
	public List<Campground> displayCampgroundsByPark(String park) {
		List<Campground> allCampgrounds = new ArrayList<>();
		String allCamps = "SELECT * from campground C join park P on P.park_id = c.park_id " + 
				"where P.name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(allCamps, park);
		while(results.next()) {
			Campground camp = mapRowToCamp(results);
			allCampgrounds.add(camp);
		}
		return allCampgrounds;
	}

	
	private Campground mapRowToCamp(SqlRowSet results) {
		Campground camp = new Campground();
		camp.setCampgroundId(results.getInt("campground_id"));
		camp.setParkId(results.getInt("park_id"));
		camp.setName(results.getString("name"));
		camp.setOpenFromMonth(results.getInt("open_from_mm"));
		camp.setCloseAtMonth(results.getInt("open_to_mm"));
		camp.setDailyFee(results.getBigDecimal("daily_fee"));
		return camp;
	}
}
