package com.levent.manytomany.repository;

import java.util.List;
import com.levent.manytomany.entity.Team;

public interface TeamRepository {

	void saveTeam(Team team);
	List<Team> getAllTeams();
	Team findTeamById(int id);
	void deleteTeam(int id);
}
