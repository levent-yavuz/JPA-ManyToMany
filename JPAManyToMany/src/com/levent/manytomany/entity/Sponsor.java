package com.levent.manytomany.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Sponsor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String origin;
	
	@ManyToMany(mappedBy = "sponsors", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<Team> teams = new HashSet<>();

	public Sponsor() {
		super();
	} 

	public Sponsor(String name, String origin) {
		super();
		this.name = name;
		this.origin = origin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Set<Team> getTeams() {
		return teams;
	}
	
	public void addTeam(Team team) {
		
		teams.add(team);
		team.getSponsors().add(this);
	}

	@Override
	public String toString() {
		return "Sponsor [id=" + id + ", name=" + name + ", teams=" + teams.stream().map(Team::getName).collect(Collectors.toList()) + "]";
	}	
}
