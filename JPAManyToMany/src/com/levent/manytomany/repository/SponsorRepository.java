package com.levent.manytomany.repository;

import java.util.List;

import com.levent.manytomany.entity.Sponsor;

public interface SponsorRepository {

	void saveSponsor(Sponsor sponsor);
	List<Sponsor> getAllSponsors();
	Sponsor findSponsorById(int id);
	void updateSponsorName(int id, String name);
	void deleteSponsor(int id);
	void prepareData();
}
