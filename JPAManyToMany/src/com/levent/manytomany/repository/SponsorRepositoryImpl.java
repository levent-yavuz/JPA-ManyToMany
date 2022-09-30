package com.levent.manytomany.repository;

import java.util.List;
import com.levent.manytomany.entity.Sponsor;
import com.levent.manytomany.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

/**
 * Implements persistence methods for sponsors.
 */
public class SponsorRepositoryImpl implements SponsorRepository{

	private EntityManager entityManager;
	
	public SponsorRepositoryImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}

	 /**
     * Saves the specified sponsor to the database.
     *
     * @param sponsor The Sponsor to save to the database.
     */
	@Override
	public void saveSponsor(Sponsor sponsor) {
		
		entityManager.getTransaction().begin();
		entityManager.persist(sponsor);
		entityManager.getTransaction().commit();
	}
	/**
     * Returns a list of all sponsors in the database.
     *
     * @return A list of all sponsors in the database.
     */
	@Override
	public List<Sponsor> getAllSponsors() {
		
		List<Sponsor> allSponsors = entityManager.createQuery("Select s From Sponsor s", Sponsor.class).getResultList();
		
		return allSponsors;
	}
	
	/**
     * Finds the sponsor with the specified ID.
     *
     * @param id The ID of the sponsor to find.
     * @return The requested sponsor
     */
	@Override
	public Sponsor findSponsorById(int id) {
		
		Sponsor sponsor = entityManager.find(Sponsor.class, id);
		
		return sponsor;
	}
	
	/**
     * Updates the sponsor with the specified ID. This method updates the sponsor's name and all references to its Teams,
     *
     * @param id The ID of the sponsor to update.
     */
	@Override
	public void updateSponsorName(int id, String name) {
		entityManager.getTransaction().begin();
		Query updateQuery = entityManager.createQuery("Update Sponsor s Set s.name =:p1 where s.id = :p2  ", Sponsor.class);
		updateQuery.setParameter("p1", name).setParameter("p2", id).executeUpdate();
		
		Sponsor s = entityManager.find(Sponsor.class, id);
		s.setName(name);
		
		s.getTeams().forEach(team -> {
			
			team.getSponsors().remove(findSponsorById(id));
			team.getSponsors().add(s);	
		});
		
		entityManager.getTransaction().commit();
	}
	
	/**
     * Deletes the sponsor with the specified ID. This method deletes the sponsor and all references to its Teams,
     * but does not delete the Teams themselves.
     *
     * @param id The ID of the sponsor to delete.
     */
	@Override
	public void deleteSponsor(int id) {
		
		Sponsor sponsor = findSponsorById(id);
		
		entityManager.getTransaction().begin();
		sponsor.getTeams().forEach(team -> {
			team.getSponsors().remove(sponsor);
		});
		entityManager.remove(sponsor);
		entityManager.getTransaction().commit();
	}
	
	@Override
	public void prepareData() {
		
		// Create some teams
		Team mCity = new Team("Manchester City", "The Sky Blues", "Blue and White", 6);
		Team mUtd = new Team("Liverpool", "The Reds", "Red", 19);
		Team lpool = new Team("Manchester United", "The Red Devils ", "Red and White", 20);
		Team che = new Team("Chelsea", "The Blues", "White and Blue", 6);
		
		// Create some sponsors
		Sponsor etihadAirways = new Sponsor("Etihad Airways","United Arab Emirates");
		Sponsor eASports = new Sponsor("EA Sports","United States");
		Sponsor nike = new Sponsor("Nike","United States");
		Sponsor rexona = new Sponsor("Rexona","Australia");
		
		rexona.addTeam(che);
		rexona.addTeam(mCity);
		nike.addTeam(lpool);
		nike.addTeam(che);
		eASports.addTeam(che);
		eASports.addTeam(lpool);
		eASports.addTeam(mUtd);
		eASports.addTeam(mCity);
		etihadAirways.addTeam(mCity);
		
		// Save the sponsors
		saveSponsor(rexona);
		saveSponsor(nike);
		saveSponsor(eASports);
		saveSponsor(etihadAirways);
	}
}
