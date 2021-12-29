package cz.cvut.fit.tjv.polakemi.semestral_work.client.ui;

import cz.cvut.fit.tjv.polakemi.semestral_work.client.model.VehicleDto;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.shell.ExitRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collection;

@Component
public class VehicleView {

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
            System.err.println("Vehicle with given ID already exists.");
        } else
            printErrorGeneric(e);
    }

    public void printVehicle(VehicleDto vehicleDto) {
        System.out.println("Vehicle " + vehicleDto.getLicensePlate());
        System.out.println("    ID: " + vehicleDto.getIdVehicle());
        System.out.println("    name: " + vehicleDto.getVehicleName());
        System.out.println("    type: " + vehicleDto.getVehicleType());
        System.out.println("    nickname: " + vehicleDto.getNickname());
        System.out.println("    ID of owner: " + vehicleDto.getOwnerId());
    }

    public void printErrorVehicle(WebClientException e) {
        if (e instanceof WebClientResponseException.NotFound)
            System.err.println("Vehicle with given ID does not exist");
        else
            printErrorGeneric(e);
    }

    public void printAllVehicles(Collection<VehicleDto> vehicles) {
        vehicles.forEach(v -> {
            System.out.println("Vehicle " + v.getLicensePlate());
            System.out.println("    ID: " + v.getIdVehicle());
        });
        System.out.println();
    }

    public void printErrorUpdate(Throwable e) {
        if (e instanceof WebClientResponseException.NotFound)
            System.err.println(AnsiOutput.toString(AnsiColor.RED, "Cannot update: vehicle does not exist", AnsiColor.DEFAULT));
        else
            printErrorGeneric(e);
    }

}
