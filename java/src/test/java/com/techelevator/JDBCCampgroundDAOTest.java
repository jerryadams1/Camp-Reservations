package com.techelevator;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.SQLException;
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


public class JDBCCampgroundDAOTest extends DAOIntegrationTest {
	
	private JDBCCampgroundDAO dao;
	
	private static SingleConnectionDataSource dataSource;

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
		String sqlInsertCampground = "INSERT INTO campground VALUES (99, 1, 'test campground', 02, 03, 99.99)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertCampground);
		dao = new JDBCCampgroundDAO(dataSource);
	}
	
	@Test
	public void displayCampgroundsByParkTest() {
		
		Campground testCamp = new Campground();
		testCamp.setCampgroundId(99);
		testCamp.setParkId(1);
		testCamp.setName("test campground");
		testCamp.setOpenFromMonth(02);
		testCamp.setCloseAtMonth(03);
		testCamp.setDailyFee(new BigDecimal(99.99));
		
		List<Campground> results = dao.displayCampgroundsByPark();
		results.add(testCamp);
		boolean actual = results.contains(testCamp);
		
		assertEquals(true, actual);
		
		
		
		
	}

}
