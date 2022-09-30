package com.levent.manytomany.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

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
	
	public Team() {
		super();
	}
	
	public Team(String name, String nickName, String shirtColor, int championshipsWon) {
		super();
		this.name = name;
		this.nickName = nickName;
		this.shirtColor = shirtColor;
		this.championshipsWon = championshipsWon;
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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getShirtColor() {
		return shirtColor;
	}

	public void setShirtColor(String shirtColor) {
		this.shirtColor = shirtColor;
	}

	public int getChampionshipsWon() {
		return championshipsWon;
	} 

	public void setChampionshipsWon(int championshipsWon) {
		this.championshipsWon = championshipsWon;
	}

	public Set<Sponsor> getSponsors() {
		return sponsors;
	}
	
	@Override
	public String toString() {
		return "Team [id=" + id + ", name=" + name +  ", sponsors=" + sponsors.stream().map(Sponsor::getName).collect(Collectors.toList()) + "]";
	}	
}
