package cz.cvut.fit.tjv.polakemi.semestral_work.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VehicleDto {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    public Integer idVehicle;

    public String licensePlate;

    public String vehicleName;

    public String vehicleType;

    public String nickname;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    public Integer ownerId;

    public VehicleDto () {}

    public VehicleDto(Integer idVehicle, String licensePlate, String name, String type, String nickname) {
        this.idVehicle = idVehicle;
        this.licensePlate = licensePlate;
        this.vehicleName = name;
        this.vehicleType = type;
        this.nickname = nickname;
    }

    public VehicleDto(String licensePlate, String name, String type, String nickname) {
        this.licensePlate = licensePlate;
        this.vehicleName = name;
        this.vehicleType = type;
        this.nickname = nickname;
    }

    public VehicleDto(Integer idVehicle, String licensePlate, String name, String type, String nickname, Integer ownerId) {
        this.idVehicle = idVehicle;
        this.licensePlate = licensePlate;
        this.vehicleName = name;
        this.vehicleType = type;
        this.nickname = nickname;
        this.ownerId = ownerId;
    }

    public VehicleDto(String licensePlate, String name, String type, String nickname, Integer ownerId) {
        this.licensePlate = licensePlate;
        this.vehicleName = name;
        this.vehicleType = type;
        this.nickname = nickname;
        this.ownerId = ownerId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(Integer idVehicle) {
        this.idVehicle = idVehicle;
    }

    @Override
    public String toString() {
        return "VehicleDto{" +
                "licensePlate='" + licensePlate + '\'' +
                ", name='" + vehicleName + '\'' +
                ", type='" + vehicleType + '\'' +
                ", nickname='" + nickname + '\'' +
                ", owner id =" + ownerId +
                '}';
    }
}
