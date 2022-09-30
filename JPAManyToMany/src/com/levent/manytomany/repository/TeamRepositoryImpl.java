package com.levent.manytomany.repository;

import java.util.List;
import com.levent.manytomany.entity.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

/**
 * Implements persistence methods for teams.
 */
public class TeamRepositoryImpl implements TeamRepository {

	private EntityManager entityManager;
	
	public TeamRepositoryImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}
	
	/**
     * Saves the specified team to the database.
     *
     * @param team The team to save to the database.
     */
	@Override
	public void saveTeam(Team team) {
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(team);
		transaction.commit();
	}
	
	 /**
     * Returns a list of all teams in the database.
     *
     * @return A list of all teams in the database.
     */
	@Override
	public List<Team> getAllTeams() {
		
		List<Team> allTeams = entityManager.createQuery("Select t From Team t", Team.class).getResultList();
		
		return allTeams;
	}
	
	/**
     * Finds the team with the specified ID.
     *
     * @param id The ID of the team to find.
     * @return The requested sponsor
     */
	@Override
	public Team findTeamById(int id) {
		
		Team team = entityManager.find(Team.class, id);
		
		return team;
	}
	
	/**
     * Deletes the team with the specified ID. This method deletes the team and all references to its Sponpors,
     * but does not delete the Sponsors themselves.
     *
     * @param id The ID of the team to delete.
     */
	@Override
	public void deleteTeam(int id) {
		
		Team team = findTeamById(id);
		entityManager.getTransaction().begin();
		
		team.getSponsors().forEach(sponsor -> {
			sponsor.getTeams().remove(team);
        });
		entityManager.remove(team);
		
		entityManager.getTransaction().commit();
	}
}
