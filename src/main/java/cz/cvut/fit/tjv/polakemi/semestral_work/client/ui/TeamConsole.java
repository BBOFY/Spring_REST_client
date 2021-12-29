package cz.cvut.fit.tjv.polakemi.semestral_work.client.ui;

import cz.cvut.fit.tjv.polakemi.semestral_work.client.data.SponsorClient;
import cz.cvut.fit.tjv.polakemi.semestral_work.client.data.TeamClient;
import cz.cvut.fit.tjv.polakemi.semestral_work.client.data.VehicleClient;
import cz.cvut.fit.tjv.polakemi.semestral_work.client.model.TeamDto;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.web.reactive.function.client.WebClientException;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@ShellComponent
public class TeamConsole {
    private final TeamView teamView;
    private final TeamClient teamClient;
    private final VehicleView vehicleView;
    private final VehicleClient vehicleClient;
    private final SponsorView sponsorView;
    private final SponsorClient sponsorClient;

    public TeamConsole(TeamClient teamClient, TeamView teamView, VehicleView vehicleView, VehicleClient vehicleClient, SponsorView sponsorView, SponsorClient sponsorClient) {
        this.teamClient = teamClient;
        this.teamView = teamView;
        this.vehicleView = vehicleView;
        this.vehicleClient = vehicleClient;
        this.sponsorView = sponsorView;
        this.sponsorClient = sponsorClient;
    }

    @ShellMethod("List all teams")
    public void printAllTeams() {
        try {
            var teams = teamClient.readAll();
            teamView.printAllTeams(teams);
        } catch (WebClientException e) {
            teamView.printErrorGeneric(e);
        }
    }

    @ShellMethod("Register new team ('name' 'score' 'number of players')")
    public void createTeam(@Size(min = 3) String teamName, Integer score, @Min(0) Integer numberOfPlayers) {
        try {
            teamView.printTeam(teamClient.create(new TeamDto(teamName, score, numberOfPlayers)));
        } catch (WebClientException e) {
            teamView.printErrorGeneric(e);
        }
    }

    @ShellMethod("Set current team by ID")
    public void setTeamId(Integer id) {
        try {
            teamClient.setCurrentTeamId(id);
        }
        catch (WebClientException e) {
            System.err.println("Team with ID " + id + " does not exist");
            teamView.printErrorTeam(e);
        }
    }

    public Availability currentTeamNeededAvailability() {
        return teamClient.getCurrentTeamId() == null
                ? Availability.unavailable("Current team needs to be set first")
                : Availability.available();
    }

    @ShellMethod("Unset current team (go to scope of all teams)")
    @ShellMethodAvailability("currentTeamNeededAvailability")
    public void unsetTeam() {
        teamClient.setCurrentTeamId(null);
    }

    @ShellMethod("List details on selected team")
    @ShellMethodAvailability("currentTeamNeededAvailability")
    public void printTeam() {
        try {
            var team = teamClient.readById();
            teamView.printTeam(team);
        }
        catch (WebClientException e) {
            unsetTeam();
            teamView.printErrorTeam(e);
        }
    }

    @ShellMethod("Update team ('new name' 'new score' 'new number of players')")
    @ShellMethodAvailability("currentTeamNeededAvailability")
    public void updateTeam(@Size(min = 3) String name, Integer score, Integer numberOfPlayers) {
        try {
            var team = new TeamDto(name, score, numberOfPlayers);
            teamClient.update(team);
        }
        catch (WebClientException e) {
            teamView.printErrorUpdate(e);
        }
    }

    @ShellMethod("Delete team")
    @ShellMethodAvailability("currentTeamNeededAvailability")
    public void deleteTeam() {
        try {
            teamClient.delete();
        }
        catch (WebClientException e) {
            teamView.printErrorGeneric(e);
        }
    }

    @ShellMethod("List all owned vehicles")
    @ShellMethodAvailability("currentTeamNeededAvailability")
    public void printOwnedVehicles() {
        try {
            var team = teamClient.readById();
            if (team.getOwnedVehiclesIds().isEmpty()) {
                System.out.println("    Desired team does not own any vehicles");
                return;
            }
            team.getOwnedVehiclesIds().forEach(v -> {
                var vehicle = vehicleClient.readById(v);
                vehicleView.printVehicle(vehicle);
            });
        }
        catch (WebClientException e) {
            unsetTeam();
            teamView.printErrorTeam(e);
        }
    }

    @ShellMethod("Assign existing vehicle to team ('id of assigning vehicle')")
    @ShellMethodAvailability("currentTeamNeededAvailability")
    public void addVehicleToTeam(Integer vehicleId) {
        try {
            vehicleClient.readById(vehicleId);
        }
        catch (WebClientException e) {
            System.err.println("Vehicle with ID " + vehicleId + " does not exist");
            return;
        }

        try {
            teamClient.assignVehicle(vehicleId);
        }
        catch (WebClientException e) {
            teamView.printErrorGeneric(e);
        }
    }

    @ShellMethod("Remove existing vehicle from team ('id of removing vehicle')")
    @ShellMethodAvailability("currentTeamNeededAvailability")
    public void removeVehicleFromTeam(Integer vehicleId) {
        try {
            vehicleClient.readById(vehicleId);
        }
        catch (WebClientException e) {
            System.err.println("Vehicle with ID " + vehicleId + " does not exist");
            return;
        }

        try {
            teamClient.removeVehicle(vehicleId);
        }
        catch (WebClientException e) {
            teamView.printErrorGeneric(e);
        }
    }

    @ShellMethod("List all team's sponsors")
    @ShellMethodAvailability("currentTeamNeededAvailability")
    public void printSponsors() {
        try {
            var team = teamClient.readById();
            if (team.getTeamSponsorsIds().isEmpty()) {
                System.out.println("    Desired team does not have any sponsors");
                return;
            }
            team.getTeamSponsorsIds().forEach(s -> {
                var sponsor = sponsorClient.readById(s);
                sponsorView.printSponsor(sponsor);
            });
        }
        catch (WebClientException e) {
            unsetTeam();
            teamView.printErrorTeam(e);
        }
    }

    @ShellMethod("Assign existing sponsor to team ('id of assigning sponsor')")
    @ShellMethodAvailability("currentTeamNeededAvailability")
    public void addSponsorToTeam(Integer sponsorId) {
        try {
            sponsorClient.readById(sponsorId);
        }
        catch (WebClientException e) {
            System.err.println("Sponsor with ID " + sponsorId + " does not exist");
            return;
        }

        try {
            teamClient.assignSponsor(sponsorId);
        }
        catch (WebClientException e) {
            teamView.printErrorGeneric(e);
        }
    }

    @ShellMethod("Remove existing sponsor from team ('id of removing sponsor')")
    @ShellMethodAvailability("currentTeamNeededAvailability")
    public void removeSponsorFromTeam(Integer sponsorId) {
        try {
            sponsorClient.readById(sponsorId);
        }
        catch (WebClientException e) {
            System.err.println("Sponsor with ID " + sponsorId + " does not exist");
            return;
        }

        try {
            teamClient.removeSponsor(sponsorId);
        }
        catch (WebClientException e) {
            teamView.printErrorGeneric(e);
        }
    }
}
