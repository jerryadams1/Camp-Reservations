package com.techelevator;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.jdbc.JDBCCampgroundDAO;
import com.techelevator.jdbc.JDBCSiteDAO;

public class JDBCSiteDAOTest extends DAOIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCSiteDAO dao;
	private JDBCCampgroundDAO campDAO;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	protected DataSource getDataSource() {
		return dataSource;
	}
	
	@Before
	public void beforeEachTest() {
		System.out.println("Starting test");
		String sqlInsertSite = "INSERT INTO site VALUES (998, 1, 999, 100, true, 99, true)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertSite);
		dao = new JDBCSiteDAO(dataSource);
		campDAO = new JDBCCampgroundDAO(dataSource);
	}
	
	@Test
	public void allCampsitePerCampgroundTest() {
		
		Campground camp = (Campground) campDAO.displayCampgroundsByPark("Acadia");
		
		Site testSite = new Site();
		testSite.setSiteId(999);
		testSite.setCampgroundId(1);
		testSite.setSiteNumber(100);
		testSite.setMaxOccupancy(999);
		testSite.setAccessible(true);
		testSite.setMaxRvLength(99);
		testSite.setUtilities(true);
		
		List<Site> results = dao.allCampsitePerCampground(camp.getCampgroundId());
		results.add(testSite);
		boolean actual = results.contains(testSite);
		
		assertEquals(true, actual);
		
		
		
		
	}

}
