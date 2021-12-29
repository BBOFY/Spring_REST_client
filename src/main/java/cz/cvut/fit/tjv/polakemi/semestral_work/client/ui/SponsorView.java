package cz.cvut.fit.tjv.polakemi.semestral_work.client.ui;

import cz.cvut.fit.tjv.polakemi.semestral_work.client.model.SponsorDto;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.shell.ExitRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collection;

@Component
public class SponsorView {

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
            System.err.println("Sponsor with given ID already exists.");
        } else
            printErrorGeneric(e);
    }

    public void printSponsor(SponsorDto sponsorDto) {
        System.out.println("Sponsor '" + sponsorDto.getSponsorName() + "'");
        System.out.println("    ID: " + sponsorDto.getIdSponsor());
        System.out.println("    industry: " + sponsorDto.getIndustry());
        System.out.println("    IDs of sponsored teams:");
        System.out.print("        [ ");
        sponsorDto.getSponsoredTeamsIds().forEach(t -> System.out.print("" + t + " "));
        System.out.println("]");
    }

    public void printErrorSponsor(WebClientException e) {
        if (e instanceof WebClientResponseException.NotFound)
            System.err.println("Sponsor with given ID does not exist");
        else
            printErrorGeneric(e);
    }

    public void printAllSponsors(Collection<SponsorDto> sponsor) {
        sponsor.forEach(s -> {
            System.out.println("Sponsor '" + s.getSponsorName() + "'");
            System.out.println("    ID: " + s.getIdSponsor());
        });
        System.out.println();
    }

    public void printErrorUpdate(Throwable e) {
        if (e instanceof WebClientResponseException.NotFound)
            System.err.println(AnsiOutput.toString(AnsiColor.RED, "Cannot update: sponsor does not exist", AnsiColor.DEFAULT));
        else
            printErrorGeneric(e);
    }
}
