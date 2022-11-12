package es.deusto.ingenieria.sd.strava.server.data.dto;

import java.io.Serializable;
import java.util.Date;

//This class implements DTO pattern
public class AthleteDTO implements Serializable {
    //This attribute is needed to implement the "Serializable" Integererface.
    private static final long serialVersionUID = 1L;

    private String name;
    private String email;
    private Float weight;
    private Integer height;
    private Integer restingHeartrate;
    private Integer maxHeartrate;
    private Date dateofbirth;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getRestingHeartrate() {
        return restingHeartrate;
    }

    public void setRestingHeartrate(Integer restingHeartrate) {
        this.restingHeartrate = restingHeartrate;
    }

    public Integer getMaxHeartrate() {
        return maxHeartrate;
    }

    public void setMaxHeartrate(Integer maxHeartrate) {
        this.maxHeartrate = maxHeartrate;
    }

    public Date getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(Date dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    @Override
    public String toString() {
        return "Athlete [name=" + name + ", email=" + email + ", weight=" + weight
                + ", height=" + height + ", restingHeartrate=" + restingHeartrate + ", maxHeartrate=" + maxHeartrate
                + ", dateofbirth=" + dateofbirth + "]";
    }
}