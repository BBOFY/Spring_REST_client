package cz.cvut.fit.tjv.polakemi.semestral_work.client.ui;

import cz.cvut.fit.tjv.polakemi.semestral_work.client.data.VehicleClient;
import cz.cvut.fit.tjv.polakemi.semestral_work.client.model.VehicleDto;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.web.reactive.function.client.WebClientException;

import javax.validation.constraints.Size;

@ShellComponent
public class VehicleConsole {

    private final VehicleView vehicleView;
    private final VehicleClient vehicleClient;
    private final TeamView teamView;

    public VehicleConsole(VehicleView vehicleView, VehicleClient vehicleClient, TeamView teamView) {
        this.vehicleView = vehicleView;
        this.vehicleClient = vehicleClient;
        this.teamView = teamView;
    }

    @ShellMethod("List all vehicles")
    public void printAllVehicles() {
        try {
        var vehicles = vehicleClient.readAll();
            vehicleView.printAllVehicles(vehicles);
        }
        catch (WebClientException e) {
            vehicleView.printErrorGeneric(e);
        }
    }

    @ShellMethod("Register new vehicle ('license plate' 'name' 'type' 'nickname')")
    public void createVehicle(@Size(min = 3) String licensePlate,
                              @Size(min = 3) String vehicleName,
                              @Size(min = 3) String vehicleType,
                              String nickname) {
        try {
            vehicleView.printVehicle(vehicleClient.create(
                    new VehicleDto(
                            licensePlate,
                            vehicleName,
                            vehicleType,
                            nickname)));
        } catch (WebClientException e) {
            vehicleView.printErrorGeneric(e);
        }
    }

    @ShellMethod("Set current vehicle by ID")
    public void setVehicleId(Integer id) {
        try {
            vehicleClient.setCurrentVehicleId(id);
        }
        catch (WebClientException e) {
            System.err.println("Vehicle with ID " + id + " does not exist");
            vehicleView.printErrorVehicle(e);
        }
    }

    public Availability currentVehicleNeededAvailability() {
        return vehicleClient.getCurrentVehicleId() == null
                ? Availability.unavailable("Current vehicle needs to be set first")
                : Availability.available();
    }

    @ShellMethod("Unset current vehicle (go to scope of all vehicles)")
    @ShellMethodAvailability("currentVehicleNeededAvailability")
    public void unsetVehicle() {
        vehicleClient.setCurrentVehicleId(null);
    }

    @ShellMethod("List details on selected vehicle")
    @ShellMethodAvailability("currentVehicleNeededAvailability")
    public void printVehicle() {
        try {
            var vehicle = vehicleClient.readById();
            vehicleView.printVehicle(vehicle);
        }
        catch (WebClientException e) {
            unsetVehicle();
            vehicleView.printErrorVehicle(e);
        }
    }

    @ShellMethod("Update vehicle ('new license plate' 'new name' 'new type' 'new nickname')")
    @ShellMethodAvailability("currentVehicleNeededAvailability")
    public void updateVehicle(
            @Size(min = 3) String licensePlate,
            @Size(min = 3) String vehicleName,
            @Size(min = 3) String vehicleType,
            String nickname
    ) {
        try {
            var vehicle = new VehicleDto(
                    licensePlate,
                    vehicleName,
                    vehicleType,
                    nickname
                    );
            vehicleClient.update(vehicle);
        }
        catch (WebClientException e) {
            vehicleView.printErrorUpdate(e);
        }
    }

    @ShellMethod("Delete vehicle")
    @ShellMethodAvailability("currentVehicleNeededAvailability")
    public void deleteVehicle() {
        try {
            vehicleClient.delete();
        }
        catch (WebClientException e) {
            vehicleView.printErrorGeneric(e);
        }
    }

    @ShellMethod("List details about owner")
    @ShellMethodAvailability("currentVehicleNeededAvailability")
    public void printOwner() {
        if (vehicleClient.readById().getOwnerId() == null) {
            System.out.println("    This vehicle has no owner");
            return;
        }
        try {
            teamView.printTeam(vehicleClient.getOwner());
        }
        catch (WebClientException e) {
            vehicleView.printErrorVehicle(e);
        }
    }
}
