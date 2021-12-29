package cz.cvut.fit.tjv.polakemi.semestral_work.client.ui;

import cz.cvut.fit.tjv.polakemi.semestral_work.client.model.TeamDto;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.shell.ExitRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collection;

@Component
public class TeamView {

    public void printErrorGeneric(Throwable e) {
        if (e instanceof WebClientRequestException wcre) {
            System.err.println("Cannot connect to server API. Is it accessible at `" + wcre.getUri() + "'?");
            throw new ExitRequest();
        }
        else if (e instanceof WebClientResponseException.InternalServerError)
            System.err.println("Technical server error: ");
        else
            System.err.println("Unknown error. Is server API running?");
    }

    void printErrorCreate(WebClientException e) {
        if (e instanceof WebClientResponseException.Conflict) {
            System.err.println("Team with given ID already exists.");
        } else
            printErrorGeneric(e);
    }

    public void printTeam(TeamDto teamDto) {
        System.out.println("Team '" + teamDto.getTeamName() + "'");
        System.out.println("    ID: " + teamDto.getIdTeam());
        System.out.println("    score: " + teamDto.getScore());
        System.out.println("    number of players: " + teamDto.getNumberOfPlayers());
        System.out.println("    IDs of owned vehicles:");
        System.out.print("        [ ");
        teamDto.getOwnedVehiclesIds().forEach(v -> System.out.print("" + v + " "));
        System.out.println("]");
        System.out.println("    IDs of sponsors:");
        System.out.print("        [ ");
        teamDto.getTeamSponsorsIds().forEach(s -> System.out.print("" + s + " "));
        System.out.println("]");
    }

    public void printErrorTeam(WebClientException e) {
        if (e instanceof WebClientResponseException.NotFound)
            System.err.println("Team with given ID does not exist");
        else
            printErrorGeneric(e);
    }

    public void printAllTeams(Collection<TeamDto> teams) {
        teams.forEach(t -> {
            System.out.println("Team '" + t.getTeamName() + "'");
            System.out.println("    ID: " + t.getIdTeam());
        });
        System.out.println();
    }

    public void printErrorUpdate(Throwable e) {
        if (e instanceof WebClientResponseException.NotFound)
            System.err.println(AnsiOutput.toString(AnsiColor.RED, "Cannot update: team does not exist", AnsiColor.DEFAULT));
        else
            printErrorGeneric(e);
    }
}

