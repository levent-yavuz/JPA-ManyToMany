package com.levent.manytomany.app;

import com.levent.manytomany.repository.SponsorRepository;
import com.levent.manytomany.repository.SponsorRepositoryImpl;
import com.levent.manytomany.repository.TeamRepository;
import com.levent.manytomany.repository.TeamRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class App {

	public static void main(String[] args) {
		
		//Entity manager
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("JpaManyToManyUnit");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		TeamRepository teamRepository = new TeamRepositoryImpl(entityManager);
		SponsorRepository sponsorRepository = new SponsorRepositoryImpl(entityManager);
		sponsorRepository.prepareData();
		
		System.out.println("All Teams");
		teamRepository.getAllTeams().stream().forEach(System.out::println);
		
		System.out.println("\nAll Sponsor");
		sponsorRepository.getAllSponsors().stream().forEach(System.out::println);
		
		System.out.println("\nThe team with the specified (ID = 3) is deleting..");
		teamRepository.deleteTeam(3);
		
		System.out.println("\nThe sponsor with the specified (ID = 3) is deleting..");
		sponsorRepository.deleteSponsor(3);
		
		System.out.println("The sponsor's name (the specified ID = 2) is updating to 'Test'..");
		sponsorRepository.updateSponsorName(2, "Test");
		
		System.out.println("\nAll Teams After Delete and Update Operation");
		teamRepository.getAllTeams().stream().forEach(System.out::println);
		
		System.out.println("\nAll Sponsors After Delete and Update Operations");
		sponsorRepository.getAllSponsors().stream().forEach(System.out::println);
		
		//Close the entity manager and associated factory
        entityManager.close();
        entityManagerFactory.close();
	}
}
