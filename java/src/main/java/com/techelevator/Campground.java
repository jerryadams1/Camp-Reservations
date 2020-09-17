package com.techelevator;

import java.math.BigDecimal;

public class Campground {
	
	private int campgroundId;
	private int parkId;
	private String name;
	private int openFromMonth;
	private int closeAtMonth;
	private BigDecimal dailyFee;
	
	
	public int getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(int campgroundId) {
		this.campgroundId = campgroundId;
	}
	public int getParkId() {
		return parkId;
	}
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOpenFromMonth() {
		return openFromMonth;
	}
	public void setOpenFromMonth(int openFromMonth) {
		this.openFromMonth = openFromMonth;
	}
	public int getCloseAtMonth() {
		return closeAtMonth;
	}
	public void setCloseAtMonth(int closeAtMonth) {
		this.closeAtMonth = closeAtMonth;
	}
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}
	
	

}
