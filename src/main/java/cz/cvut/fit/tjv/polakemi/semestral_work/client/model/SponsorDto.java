package cz.cvut.fit.tjv.polakemi.semestral_work.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Collection;
import java.util.HashSet;

public class SponsorDto {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    public Integer idSponsor;

    public String sponsorName;

    public String industry;

    public Collection<Integer> sponsoredTeamsIds = new HashSet<>();

    public SponsorDto() {}

    public SponsorDto(Integer idSponsor, String sponsorName, String industry) {
        this.idSponsor = idSponsor;
        this.sponsorName = sponsorName;
        this.industry = industry;
    }

    public SponsorDto(String sponsorName, String industry) {
        this.sponsorName = sponsorName;
        this.industry = industry;
    }

    public SponsorDto(Integer idSponsor, String sponsorName, String industry, Collection<Integer> sponsoredTeamsIds) {
        this.idSponsor = idSponsor;
        this.sponsorName = sponsorName;
        this.industry = industry;
        this.sponsoredTeamsIds = sponsoredTeamsIds;
    }

    public SponsorDto(String sponsorName, String industry, Collection<Integer> sponsoredTeamsIds) {
        this.sponsorName = sponsorName;
        this.industry = industry;
        this.sponsoredTeamsIds = sponsoredTeamsIds;
    }

    public Integer getIdSponsor() {
        return idSponsor;
    }

    public void setIdSponsor(Integer idSponsor) {
        this.idSponsor = idSponsor;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Collection<Integer> getSponsoredTeamsIds() {
        return sponsoredTeamsIds;
    }

    public void setSponsoredTeamsIds(Collection<Integer> sponsoredTeamsIds) {
        this.sponsoredTeamsIds = sponsoredTeamsIds;
    }

}
