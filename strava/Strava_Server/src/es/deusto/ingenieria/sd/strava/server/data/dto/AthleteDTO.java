package es.deusto.ingenieria.sd.strava.server.data.dto;

import java.io.Serializable;
import java.util.Date;

public class AthleteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String email;
    private Double weight;
    private Integer height;
    private Integer restingHeartRate;
    private Integer maxHeartRate;
    private Date dateOfBirth;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(final Double weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(final Integer height) {
        this.height = height;
    }

    public Integer getRestingHeartRate() {
        return restingHeartRate;
    }

    public void setRestingHeartRate(final Integer restingHeartRate) {
        this.restingHeartRate = restingHeartRate;
    }

    public Integer getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(final Integer maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(final Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "AthleteDTO [name=" + name + ", email=" + email + ", weight=" + weight + ", height=" + height
                + ", restingHeartRate=" + restingHeartRate + ", maxHeartRate=" + maxHeartRate + ", dateOfBirth="
                + dateOfBirth + "]";
    }

}