package cz.cvut.fit.tjv.polakemi.semestral_work.client.ui;

import cz.cvut.fit.tjv.polakemi.semestral_work.client.data.SponsorClient;
import cz.cvut.fit.tjv.polakemi.semestral_work.client.data.TeamClient;
import cz.cvut.fit.tjv.polakemi.semestral_work.client.model.SponsorDto;
import cz.cvut.fit.tjv.polakemi.semestral_work.client.model.TeamDto;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.web.reactive.function.client.WebClientException;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@ShellComponent
public class SponsorConsole {

    private final SponsorView sponsorView;
    private final SponsorClient sponsorClient;
    private final TeamView teamView;
    private final TeamClient teamClient;

    public SponsorConsole(SponsorView sponsorView, SponsorClient sponsorClient, TeamView teamView, TeamClient teamClient) {
        this.sponsorView = sponsorView;
        this.sponsorClient = sponsorClient;
        this.teamView = teamView;
        this.teamClient = teamClient;
    }

    @ShellMethod("List all sponsors")
    public void printAllSponsors() {
        try {
            var sponsors = sponsorClient.readAll();
            sponsorView.printAllSponsors(sponsors);
        } catch (WebClientException e) {
            teamView.printErrorGeneric(e);
        }
    }

    @ShellMethod("Register new sponsor ('name' 'industry')")
    public void createSponsor(@Size(min = 3) String sponsorName, String industry) {
        try {
            sponsorView.printSponsor(sponsorClient.create(new SponsorDto(sponsorName, industry)));
        } catch (WebClientException e) {
            teamView.printErrorGeneric(e);
        }
    }

    @ShellMethod("Set current sponsor by ID")
    public void setSponsorId(Integer id) {
        try {
            sponsorClient.readById(id);
        }
        catch (WebClientException e) {
            System.err.println("Sponsor with ID " + id + " does not exist");
            return;
        }

        try {
            sponsorClient.setCurrentSponsorId(id);
        }
        catch (WebClientException e) {
            System.err.println("Sponsor with ID " + id + " does not exist");
            sponsorView.printErrorSponsor(e);
        }
    }

    public Availability currentSponsorNeededAvailability() {
        return sponsorClient.getCurrentSponsorId() == null
                ? Availability.unavailable("Current sponsor needs to be set first")
                : Availability.available();
    }

    @ShellMethod("Unset current sponsor (go to scope of all sponsors)")
    @ShellMethodAvailability("currentSponsorNeededAvailability")
    public void unsetSponsor() {
        sponsorClient.setCurrentSponsorId(null);
    }

    @ShellMethod("List details on selected sponsor")
    @ShellMethodAvailability("currentSponsorNeededAvailability")
    public void printSponsor() {
        try {
            var sponsor = sponsorClient.readById();
            sponsorView.printSponsor(sponsor);
        }
        catch (WebClientException e) {
            unsetSponsor();
            sponsorView.printErrorSponsor(e);
        }
    }

    @ShellMethod("Update sponsor ('new name' 'new industry')")
    @ShellMethodAvailability("currentSponsorNeededAvailability")
    public void updateSponsor(@Size(min = 3) String name, String industry) {
        try {
            var sponsor = new SponsorDto(name, industry);
            sponsorClient.update(sponsor);
        }
        catch (WebClientException e) {
            sponsorView.printErrorUpdate(e);
        }
    }

    @ShellMethod("Delete sponsor")
    @ShellMethodAvailability("currentSponsorNeededAvailability")
    public void deleteSponsor() {
        try {
            sponsorClient.delete();
        }
        catch (WebClientException e) {
            sponsorView.printErrorGeneric(e);
        }
    }

    @ShellMethod("List all sponsored teams")
    @ShellMethodAvailability("currentSponsorNeededAvailability")
    public void printSponsoredTeams() {
        try {
            var sponsor = sponsorClient.readById();
            if (sponsor.getSponsoredTeamsIds().isEmpty()) {
                System.out.println("    Desired sponsor is not sponsoring any team");
                return;
            }
            sponsorClient.getTeams().forEach(t -> {
                teamView.printTeam(t);
            });
        }
        catch (WebClientException e) {
            unsetSponsor();
            sponsorView.printErrorSponsor(e);
        }
    }

    @ShellMethod("Assign existing team to sponsor")
    @ShellMethodAvailability("currentSponsorNeededAvailability")
    public void addTeamToSponsor(Integer teamId) {
        try {
            teamClient.readById(teamId);
        }
        catch (WebClientException e) {
            System.err.println("Team with ID " + teamId + " does not exist");
            return;
        }

        try {
            sponsorClient.assignTeam(teamId);
        }
        catch (WebClientException e) {
            sponsorView.printErrorGeneric(e);
        }
    }

    @ShellMethod("Remove existing team from sponsor")
    @ShellMethodAvailability("currentSponsorNeededAvailability")
    public void removeTeamFromSponsor(Integer teamId) {
        try {
            teamClient.readById(teamId);
        }
        catch (WebClientException e) {
            System.err.println("Team with ID " + teamId + " does not exist");
            return;
        }

        try {
            sponsorClient.removeTeam(teamId);
        }
        catch (WebClientException e) {
            sponsorView.printErrorGeneric(e);
        }
    }
}
