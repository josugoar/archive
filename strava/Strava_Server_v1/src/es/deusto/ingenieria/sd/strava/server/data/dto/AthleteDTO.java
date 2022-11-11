package es.deusto.ingenieria.sd.strava.server.data.dto;

import java.io.Serializable;
import java.util.Date;

//This class implements DTO pattern
public class AthleteDTO implements Serializable {
    //This attribute is needed to implement the "Serializable" interface.
    private static final long serialVersionUID = 1L;

    private String name;
    private String email;
    private float weight;
    private int height;
    private int restingHeartrate;
    private int maxHeartrate;
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
    public float getWeight() {
        return weight;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getRestingHeartrate() {
        return restingHeartrate;
    }
    public void setRestingHeartrate(int restingHeartrate) {
        this.restingHeartrate = restingHeartrate;
    }
    public int getMaxHeartrate() {
        return maxHeartrate;
    }
    public void setMaxHeartrate(int maxHeartrate) {
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