#### Other JPA Projects:
- [JPA-OneToOne](https://github.com/levent-yavuz/JPA-OneToOne)
- [JPA-OneToMany](https://github.com/levent-yavuz/JPA-OneToMany)

# JPA - ManyToMany Relationship

In ManyToMany relationships, the owning side is optional. The mappedBy parameter specifies the inverse side. If the mappedBy parameter is not used, then there will be 2 Unidirectional ManyToMany relationships.



## Team Class

```
@Entity
public class Team {
	
	@Id
	@GeneratedValue
	private int id;
	private String name;
	private String nickName;
	private String shirtColor;
	private int championshipsWon;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(name="sponsorship", 
	joinColumns = @JoinColumn(name="team_id"),
	inverseJoinColumns = @JoinColumn(name="sponsor_id")
	) 
	private Set<Sponsor> sponsors = new HashSet<>();
	
	@Override
	public String toString() {
		return "Team [id=" + id + ", name=" + name +  ", sponsors=" + sponsors.stream().map(Sponsor::getName).collect(Collectors.toList()) + "]";
	}	
  ```
## Sponsor Class
  ```
@Entity
public class Sponsor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String origin;
	
	@ManyToMany(mappedBy = "sponsors", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<Team> teams = new HashSet<>();
	
	public void addTeam(Team team) {
		
		teams.add(team);
		team.getSponsors().add(this);
	}

	@Override
	public String toString() {
		return "Sponsor [id=" + id + ", name=" + name + ", teams=" + teams.stream().map(Team::getName).collect(Collectors.toList()) + "]";
	}
  ```
  
  ## SponsorRepository Update and Delete Methods
   ```
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
  ```
  ## App Class and Outputs
  ```	
	// Entity manager
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
		
	System.out.println("\nAll Teams After Deleting Operation");
	teamRepository.getAllTeams().stream().forEach(System.out::println);
		
	System.out.println("\nThe sponsor with the specified (ID = 3) is deleting..");
	sponsorRepository.deleteSponsor(3);
		
	System.out.println("The sponsor's name (the specified ID = 2) is updating to 'Test'..");
	sponsorRepository.updateSponsorName(2, "Test");
		
	System.out.println("\nAll Sponsors After Deleting and Updating Operations");
	sponsorRepository.getAllSponsors().stream().forEach(System.out::println);
		
	// Close the entity manager and associated factory
        entityManager.close();
        entityManagerFactory.close();
  ```
  ```
All Teams
Team [id=1, name=Chelsea, sponsors=[Rexona, Nike, EA Sports]]
Team [id=2, name=Manchester United, sponsors=[Nike, EA Sports]]
Team [id=3, name=Liverpool, sponsors=[EA Sports]]
Team [id=4, name=Manchester City, sponsors=[Etihad Airways, Rexona, EA Sports]]

All Sponsor
Sponsor [id=1, name=Etihad Airways, teams=[Manchester City]]
Sponsor [id=2, name=Rexona, teams=[Chelsea, Manchester City]]
Sponsor [id=3, name=Nike, teams=[Manchester United, Chelsea]]
Sponsor [id=4, name=EA Sports, teams=[Chelsea, Manchester United, Liverpool, Manchester City]]

The team with the specified (ID = 3) is deleting..

The sponsor with the specified (ID = 3) is deleting..
The sponsor's name (the specified ID = 2) is updating to 'Test'..

All Teams After Delete and Update Operation
Team [id=1, name=Chelsea, sponsors=[Test, EA Sports]]
Team [id=2, name=Manchester United, sponsors=[EA Sports]]
Team [id=4, name=Manchester City, sponsors=[Etihad Airways, Test, EA Sports]]

All Sponsors After Delete and Update Operations
Sponsor [id=1, name=Etihad Airways, teams=[Manchester City]]
Sponsor [id=2, name=Test, teams=[Chelsea, Manchester City]]
Sponsor [id=4, name=EA Sports, teams=[Chelsea, Manchester United, Manchester City]]
  ```
  
