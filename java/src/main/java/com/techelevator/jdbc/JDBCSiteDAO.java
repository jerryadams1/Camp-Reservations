package com.techelevator.jdbc;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.Campground;
import com.techelevator.ParkDAO;
import com.techelevator.Site;
import com.techelevator.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {
	
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate jdbcSpecial;
	
	public JDBCSiteDAO(DataSource dataSource) {
		 jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcSpecial = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public List<Site> allCampsitePerCampground(int campId, String fromDate, String toDate) {
		
		int yearArr = Integer.parseInt(fromDate.substring(0, 4));
		int yearDep = Integer.parseInt(toDate.substring(0, 4));
		int dayArr = Integer.parseInt(fromDate.substring(fromDate.length()-2));
		int dayDep = Integer.parseInt(toDate.substring(toDate.length()-2));
		int monthArr = Integer.parseInt(fromDate.substring(5, 7));
		int monthDep = Integer.parseInt(toDate.substring(5, 7));
		
		Set <Integer> campIds = new HashSet<Integer>();
		Set <LocalDate> dates = new HashSet<LocalDate>();
		dates.add(LocalDate.of(yearArr, monthArr, dayArr));
		dates.add(LocalDate.of(yearDep, monthDep, dayDep));
		campIds.add(campId);

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("dates", dates);
		parameters.addValue("campIds", campIds);
		
		List<Site> allSites = new ArrayList<>();
		String allSitesPerCampground = "SELECT * FROM site WHERE campground_id = :campIds AND site_id " + 
				"NOT IN (SELECT site_id FROM reservation WHERE (from_date, to_date) " + 
				"OVERLAPS (:dates ))";
		SqlRowSet results = jdbcSpecial.queryForRowSet(allSitesPerCampground, parameters);
		
		while(results.next()) {
			Site site = mapRowToSite(results);
			allSites.add(site);
		}
		
		return allSites;
	}

	@Override
	public List<Site> topFiveCampsites() {
		return null;
	}

	private Site mapRowToSite(SqlRowSet results) {
		Site site = new Site();
		site.setSiteId(results.getInt("site_id"));
		site.setCampgroundId(results.getInt("campground_id"));
		site.setSiteNumber(results.getInt("site_number"));
		site.setMaxOccupancy(results.getInt("max_occupancy"));
		site.setAccessible(results.getBoolean("accessible"));
		site.setMaxRvLength(results.getInt("max_rv_length"));
		site.setUtilities(results.getBoolean("utilities"));
		
		return site;
	}



}
