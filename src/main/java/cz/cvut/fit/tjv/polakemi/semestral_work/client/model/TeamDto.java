package cz.cvut.fit.tjv.polakemi.semestral_work.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Collection;
import java.util.HashSet;

public class TeamDto {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    public Integer idTeam;

    public String teamName;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    public Integer score;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    public Integer numberOfPlayers;

    public Collection<Integer> ownedVehiclesIds = new HashSet<>();

    public Collection<Integer> teamSponsorsIds = new HashSet<>();

    public TeamDto () {}

    public TeamDto(String teamName, Integer score, Integer numberOfPlayers) {
        this.teamName = teamName;
        this.score = score;
        this.numberOfPlayers = numberOfPlayers;
    }

    public TeamDto(Integer idTeam, String teamName, Integer score, Integer numberOfPlayers) {
        this.idTeam = idTeam;
        this.teamName = teamName;
        this.score = score;
        this.numberOfPlayers = numberOfPlayers;
    }

    public TeamDto(Integer idTeam, String teamName, Integer score, Integer numberOfPlayers, Collection<Integer> ownedVehiclesIds, Collection<Integer> teamSponsorsIds) {
        this.idTeam = idTeam;
        this.teamName = teamName;
        this.score = score;
        this.numberOfPlayers = numberOfPlayers;
        this.ownedVehiclesIds = ownedVehiclesIds;
        this.teamSponsorsIds = teamSponsorsIds;
    }

    public Integer getIdTeam() {
        return idTeam;
    }

    public void setIdTeam(Integer idTeam) {
        this.idTeam = idTeam;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(Integer numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public Collection<Integer> getOwnedVehiclesIds() {
        return ownedVehiclesIds;
    }

    public void setOwnedVehiclesIds(Collection<Integer> ownedVehiclesIds) {
        this.ownedVehiclesIds = ownedVehiclesIds;
    }

    public Collection<Integer> getTeamSponsorsIds() {
        return teamSponsorsIds;
    }

    public void setTeamSponsorsIds(Collection<Integer> teamSponsorsIds) {
        this.teamSponsorsIds = teamSponsorsIds;
    }
}
