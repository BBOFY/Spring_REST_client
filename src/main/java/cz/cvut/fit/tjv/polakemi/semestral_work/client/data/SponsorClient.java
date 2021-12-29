package cz.cvut.fit.tjv.polakemi.semestral_work.client.data;

import cz.cvut.fit.tjv.polakemi.semestral_work.client.model.SponsorDto;
import cz.cvut.fit.tjv.polakemi.semestral_work.client.model.TeamDto;
import cz.cvut.fit.tjv.polakemi.semestral_work.client.ui.SponsorView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.Duration;
import java.util.Collection;

@Component
public class SponsorClient {

    private static final String ONE_URI = "/{id}";
    private static final String ONEs_TEAM = "/{id_s}/teams/{id_t}";
    private static final String ONEs_TEAMS = "/{id}/teams";
    private final WebClient sponsorWebClient;
    private final SponsorView sponsorView;
    private Integer currentSponsorId;

    public SponsorClient(@Value("${semestral_work_backend_url}") String backendUrl, SponsorView sponsorView) {
        sponsorWebClient = WebClient.create(backendUrl + "/sponsors");
        this.sponsorView = sponsorView;
    }

    public SponsorDto create(SponsorDto sponsor) {
        return sponsorWebClient.post()// HTTP POST mapping
                .contentType(MediaType.APPLICATION_JSON) // set HTTP headers
                .bodyValue(sponsor) // POST data
                .retrieve() // request specification done
                .bodyToMono(SponsorDto.class)
                .block(Duration.ofSeconds(5));// interpret response body as one element
    }

    public Collection<SponsorDto> readAll() {
        return sponsorWebClient.get() // HTTP GET
                .retrieve() // request specification done
                .bodyToFlux(SponsorDto.class)
                .collectList()
                .block(Duration.ofSeconds(5)); // interpret response body as one element
    }

    public SponsorDto readById() {
        if (currentSponsorId == null)
            throw new IllegalStateException("currentSponsorId must be set");
        return sponsorWebClient.get()
                .uri(ONE_URI, currentSponsorId)
                .retrieve()
                .bodyToMono(SponsorDto.class)
                .block();
    }

    public SponsorDto readById(Integer id) {
        return sponsorWebClient.get()
                .uri(ONE_URI, id)
                .retrieve()
                .bodyToMono(SponsorDto.class)
                .block();
    }

    public void update(SponsorDto sponsor) {
        if (currentSponsorId == null)
            throw new IllegalStateException("currentSponsorId must be set");
        sponsor.setIdSponsor(currentSponsorId);
        sponsorWebClient.put()
                .uri(ONE_URI, currentSponsorId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sponsor)
                .retrieve()
                .toBodilessEntity()
                .subscribe(x -> {},
                        e -> {
                            setCurrentSponsorId(null);
                            sponsorView.printErrorUpdate(e);
                        });
    }

    public void delete() {
        if (currentSponsorId == null)
            throw new IllegalStateException("currentSponsorId must be set");
        sponsorWebClient.delete()
                .uri(ONE_URI, currentSponsorId)
                .retrieve()
                .toBodilessEntity()
                .subscribe(x -> setCurrentSponsorId(null),
                        e -> {
                            setCurrentSponsorId(null);
                            sponsorView.printErrorGeneric(e);
                        });
    }

    public Integer getCurrentSponsorId() {
        return currentSponsorId;
    }

    public void setCurrentSponsorId(Integer currentSponsorId) {
        try {
            this.currentSponsorId = currentSponsorId;
            if (currentSponsorId != null) {
                readById();
            }
        }
        catch (WebClientException e) {
            this.currentSponsorId = null;
            throw e;
        }
    }

    public Collection<TeamDto> getTeams() {
        if (currentSponsorId == null)
            throw new IllegalStateException("currentSponsorId must be set");
        return sponsorWebClient.get()
                .uri(ONEs_TEAMS, currentSponsorId)
                .retrieve()
                .bodyToFlux(TeamDto.class)
                .collectList()
                .block(Duration.ofSeconds(5));
    }

    public void assignTeam(Integer teamId) {
        if (currentSponsorId == null)
            throw new IllegalStateException("currentSponsorId must be set");
        sponsorWebClient.put()
                .uri(ONEs_TEAM, currentSponsorId, teamId)
                .retrieve()
                .bodyToMono(SponsorDto.class)
                .block(Duration.ofSeconds(5));
    }

    public void removeTeam(Integer teamId) {
        if (currentSponsorId == null)
            throw new IllegalStateException("currentSponsorId must be set");
        sponsorWebClient.delete()
                .uri(ONEs_TEAM, currentSponsorId, teamId)
                .retrieve()
                .bodyToMono(SponsorDto.class)
                .block(Duration.ofSeconds(5));
    }
}
