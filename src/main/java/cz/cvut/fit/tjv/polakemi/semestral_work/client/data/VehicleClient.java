package cz.cvut.fit.tjv.polakemi.semestral_work.client.data;

import cz.cvut.fit.tjv.polakemi.semestral_work.client.model.TeamDto;
import cz.cvut.fit.tjv.polakemi.semestral_work.client.model.VehicleDto;
import cz.cvut.fit.tjv.polakemi.semestral_work.client.ui.VehicleView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.Duration;
import java.util.Collection;

@Component
public class VehicleClient {

    private static final String ONE_URI = "/{id}";
    private static final String ONEs_OWNER = "/{id}/owner";
    private final WebClient vehicleWebClient;
    private final VehicleView vehicleView;
    private Integer currentVehicleId;

    public VehicleClient(@Value("${semestral_work_backend_url}") String backendUrl, VehicleView vehicleView) {
        vehicleWebClient = WebClient.create(backendUrl + "/vehicles");
        this.vehicleView = vehicleView;
    }

    public VehicleDto create(VehicleDto vehicle) {
        return vehicleWebClient.post()// HTTP POST mapping
                .contentType(MediaType.APPLICATION_JSON) // set HTTP headers
                .bodyValue(vehicle) // POST data
                .retrieve() // request specification done
                .bodyToMono(VehicleDto.class)
                .block(Duration.ofSeconds(5));// interpret response body as one element
    }

    public Collection<VehicleDto> readAll() {
        return vehicleWebClient.get() // HTTP GET
                .retrieve() // request specification done
                .bodyToFlux(VehicleDto.class)
                .collectList()
                .block(Duration.ofSeconds(5)); // interpret response body as one element
    }

    public VehicleDto readById() {
        if (currentVehicleId == null)
            throw new IllegalStateException("currentTeamId must be set");
        return vehicleWebClient.get()
                .uri(ONE_URI, currentVehicleId)
                .retrieve()
                .bodyToMono(VehicleDto.class)
                .block();
    }

    public VehicleDto readById(Integer id) {
        return vehicleWebClient.get()
                .uri(ONE_URI, id)
                .retrieve()
                .bodyToMono(VehicleDto.class)
                .block();
    }

    public void update(VehicleDto vehicle) {
        if (currentVehicleId == null)
            throw new IllegalStateException("currentTeamId must be set");
        vehicle.setIdVehicle(currentVehicleId);
        vehicleWebClient.put()
                .uri(ONE_URI, currentVehicleId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(vehicle)
                .retrieve()
                .toBodilessEntity()
                .subscribe(x -> {},
                        e -> {
                            setCurrentVehicleId(null);
                            vehicleView.printErrorUpdate(e);
                        });
    }

    public void delete() {
        if (currentVehicleId == null)
            throw new IllegalStateException("currentTeamId must be set");
        vehicleWebClient.delete()
                .uri(ONE_URI, currentVehicleId)
                .retrieve()
                .toBodilessEntity()
                .subscribe(x -> setCurrentVehicleId(null),
                        e -> {
                            setCurrentVehicleId(null);
                            vehicleView.printErrorGeneric(e);
                        });
    }

    public Integer getCurrentVehicleId() {
        return currentVehicleId;
    }

    public void setCurrentVehicleId(Integer currentVehicleId) {
        try {
            this.currentVehicleId = currentVehicleId;
            if (currentVehicleId != null) {
                readById();
            }
        }
        catch (WebClientException e) {
            this.currentVehicleId = null;
            throw e;
        }
    }

    public TeamDto getOwner() {
        if (currentVehicleId == null)
            throw new IllegalStateException("currentTeamId must be set");
        return vehicleWebClient.get()
                .uri(ONEs_OWNER, currentVehicleId)
                .retrieve()
                .bodyToMono(TeamDto.class)
                .block();
    }
}
