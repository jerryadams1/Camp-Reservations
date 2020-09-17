package com.techelevator;

import java.util.List;

public interface SiteDAO {
	
	public List<Site> allCampsitePerCampground(int siteId, String fromDate, String toDate);
	public List<Site> topFiveCampsites();

}
