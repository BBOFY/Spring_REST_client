package cz.cvut.fit.tjv.polakemi.semestral_work.client.data;

import cz.cvut.fit.tjv.polakemi.semestral_work.client.model.SponsorDto;
import cz.cvut.fit.tjv.polakemi.semestral_work.client.model.TeamDto;
import cz.cvut.fit.tjv.polakemi.semestral_work.client.model.VehicleDto;
import cz.cvut.fit.tjv.polakemi.semestral_work.client.ui.TeamView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.Duration;
import java.util.Collection;

@Component
public class TeamClient {

    private static final String ONE_URI = "/{id}";
    private static final String ONEs_VEHICLE = "/{id_t}/vehicles/{id_v}";
    private static final String ONEs_VEHICLES = "/{id}/vehicles";
    private static final String ONEs_SPONSOR = "/{id_t}/sponsors/{id_s}";
    private static final String ONEs_SPONSORS = "/{id}/sponsors";
    private final WebClient teamWebClient;
    private final TeamView teamView;
    private Integer currentTeamId;

    public TeamClient(@Value("${semestral_work_backend_url}") String backendUrl, TeamView teamView) {
        teamWebClient = WebClient.create(backendUrl + "/teams");
        this.teamView = teamView;
    }

    public TeamDto create(TeamDto team) {
        return teamWebClient.post()// HTTP POST mapping
                .contentType(MediaType.APPLICATION_JSON) // set HTTP headers
                .bodyValue(team) // POST data
                .retrieve() // request specification done
                .bodyToMono(TeamDto.class)
                .block(Duration.ofSeconds(5));// interpret response body as one element
    }

    public Collection<TeamDto> readAll() {
        return teamWebClient.get() // HTTP GET
                .retrieve() // request specification done
                .bodyToFlux(TeamDto.class)
                .collectList()
                .block(Duration.ofSeconds(5)); // interpret response body as one element
    }

    public TeamDto readById() {
        if (currentTeamId == null)
            throw new IllegalStateException("currentTeamId must be set");
        return teamWebClient.get()
                .uri(ONE_URI, currentTeamId)
                .retrieve()
                .bodyToMono(TeamDto.class)
                .block();
    }

    public TeamDto readById(Integer id) {
        return teamWebClient.get()
                .uri(ONE_URI, id)
                .retrieve()
                .bodyToMono(TeamDto.class)
                .block();
    }

    public void update(TeamDto team) {
        if (currentTeamId == null)
            throw new IllegalStateException("currentTeamId must be set");
        team.setIdTeam(currentTeamId);
        teamWebClient.put()
                .uri(ONE_URI, currentTeamId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(team)
                .retrieve()
                .toBodilessEntity()
                .subscribe(x -> {},
                        e -> {
                    setCurrentTeamId(null);
                    teamView.printErrorUpdate(e);
                });
    }

    public void delete() {
        if (currentTeamId == null)
            throw new IllegalStateException("currentTeamId must be set");
        teamWebClient.delete()
                .uri(ONE_URI, currentTeamId)
                .retrieve()
                .toBodilessEntity()
                .subscribe(x -> setCurrentTeamId(null),
                        e -> {
                    setCurrentTeamId(null);
                    teamView.printErrorGeneric(e);
                        });
    }

    public Integer getCurrentTeamId() {
        return currentTeamId;
    }

    public void setCurrentTeamId(Integer currentTeamId) {
        try {
            this.currentTeamId = currentTeamId;
            if (currentTeamId != null) {
                readById();
            }
        }
        catch (WebClientException e) {
            this.currentTeamId = null;
            throw e;
        }
    }

    public Collection<VehicleDto> getOwnedVehicles() {
        if (currentTeamId == null)
            throw new IllegalStateException("currentTeamId must be set");
        return teamWebClient.get()
                .uri(ONEs_VEHICLES)
                .retrieve()
                .bodyToFlux(VehicleDto.class)
                .collectList()
                .block(Duration.ofSeconds(5));
    }

    public Collection<SponsorDto> getSponsors() {
        if (currentTeamId == null)
            throw new IllegalStateException("currentTeamId must be set");
        return teamWebClient.get()
                .uri(ONEs_SPONSORS)
                .retrieve()
                .bodyToFlux(SponsorDto.class)
                .collectList()
                .block(Duration.ofSeconds(5));
    }

    public void assignVehicle(Integer vehicleId) {
        if (currentTeamId == null)
            throw new IllegalStateException("currentTeamId must be set");
        teamWebClient.put()
                .uri(ONEs_VEHICLE, currentTeamId, vehicleId)
                .retrieve()
                .bodyToMono(TeamDto.class)
                .block(Duration.ofSeconds(5));
    }

    public void removeVehicle(Integer vehicleId) {
        if (currentTeamId == null)
            throw new IllegalStateException("currentTeamId must be set");
        teamWebClient.delete()
                .uri(ONEs_VEHICLE, currentTeamId, vehicleId)
                .retrieve()
                .bodyToMono(TeamDto.class)
                .block(Duration.ofSeconds(5));
    }

    public void assignSponsor(Integer sponsorId) {
        if (currentTeamId == null)
            throw new IllegalStateException("currentTeamId must be set");
        teamWebClient.put()
                .uri(ONEs_SPONSOR, currentTeamId, sponsorId)
                .retrieve()
                .bodyToMono(TeamDto.class)
                .block(Duration.ofSeconds(5));
    }

    public void removeSponsor(Integer sponsorId) {
        if (currentTeamId == null)
            throw new IllegalStateException("currentTeamId must be set");
        teamWebClient.delete()
                .uri(ONEs_SPONSOR, currentTeamId, sponsorId)
                .retrieve()
                .bodyToMono(TeamDto.class)
                .block(Duration.ofSeconds(5));
    }
}
