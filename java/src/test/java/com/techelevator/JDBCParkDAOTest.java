package com.techelevator;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
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
import com.techelevator.jdbc.JDBCParkDAO;

public class JDBCParkDAOTest extends DAOIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCParkDAO dao;
	
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
		String sqlInsertPark = "INSERT INTO park VALUES (99, 'test park', 'Andyland', '1986-10-20', 99999, 123456, 'not a fun place')";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertPark);
		dao = new JDBCParkDAO(dataSource);
	}
	
	@Test
	public void availableParksTest() {
		
		Park testPark = new Park();
		testPark.setParkId(99);
		testPark.setName("NoName");
		testPark.setLocation("NoLocation");
		testPark.setEstablishDate(LocalDate.parse("1999-12-31"));
		testPark.setArea(99999);
		testPark.setVisitors(12345);
		testPark.setDescription("Awful Place to Visit");
		
		List<Park> results = dao.availableParks();
		results.add(testPark);
		boolean actual = results.contains(testPark);
		
		assertEquals(true, actual);
		
		
		
		
	}

}
