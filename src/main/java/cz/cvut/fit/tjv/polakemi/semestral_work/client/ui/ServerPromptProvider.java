package cz.cvut.fit.tjv.polakemi.semestral_work.client.ui;

import cz.cvut.fit.tjv.polakemi.semestral_work.client.data.SponsorClient;
import cz.cvut.fit.tjv.polakemi.semestral_work.client.data.TeamClient;
import cz.cvut.fit.tjv.polakemi.semestral_work.client.data.VehicleClient;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

@Component
public class ServerPromptProvider implements PromptProvider {

    private final TeamClient teamClient;
    private final VehicleClient vehicleClient;
    private final SponsorClient sponsorClient;

    public ServerPromptProvider(TeamClient teamClient, VehicleClient vehicleClient, SponsorClient sponsorClient) {
        this.teamClient = teamClient;
        this.vehicleClient = vehicleClient;
        this.sponsorClient = sponsorClient;
    }

    @Override
    public AttributedString getPrompt() {

        if (teamClient.getCurrentTeamId() != null) {
            if (sponsorClient.getCurrentSponsorId() != null) {
                if (vehicleClient.getCurrentVehicleId() != null)
                    return new AttributedString(
                            "===\n" +
                                "team '" + teamClient.getCurrentTeamId() + "'\n" +
                                "sponsor '" + sponsorClient.getCurrentSponsorId() + "'\n" +
                                "vehicle '" + vehicleClient.getCurrentVehicleId() + "':> ",
                            AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));

                return new AttributedString(
                            "===\n" +
                                "team '" + teamClient.getCurrentTeamId() + "'\n" +
                                "sponsor '" + sponsorClient.getCurrentSponsorId() + "':> ",
                        AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
            }

            if (vehicleClient.getCurrentVehicleId() != null)
                return new AttributedString(
                        "===\n" +
                            "team '" + teamClient.getCurrentTeamId() + "'\n" +
                            "vehicle '" + vehicleClient.getCurrentVehicleId() + "':> ",
                        AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));

            return new AttributedString(
                    "===\n" +
                        "team '" + teamClient.getCurrentTeamId() + "':> ",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));

        }

        if (sponsorClient.getCurrentSponsorId() != null) {
            if (vehicleClient.getCurrentVehicleId() != null)
                return new AttributedString(
                        "===\n" +
                            "sponsor '" + sponsorClient.getCurrentSponsorId() + "'\n" +
                            "vehicle '" + vehicleClient.getCurrentVehicleId() + "':> ",
                        AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));

            return new AttributedString(
                    "===\n" +
                        "sponsor '" + sponsorClient.getCurrentSponsorId() + "':> ",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
        }

        if (vehicleClient.getCurrentVehicleId() != null)
            return new AttributedString(
                    "===\n" +
                        "vehicle '" + vehicleClient.getCurrentVehicleId() + "':> ",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));

        return new AttributedString("===\nregistration:> ");
    }
}
