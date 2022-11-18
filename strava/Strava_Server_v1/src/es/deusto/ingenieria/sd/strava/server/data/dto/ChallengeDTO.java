package es.deusto.ingenieria.sd.strava.server.data.dto;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


//This class implements DTO pattern
public class ChallengeDTO implements Serializable {
	//This attribute is needed to implement the "Serializable" interface.
	private static final long serialVersionUID = 1L;

    private Integer id;
	private String name;
    private Date startDate;
    private Date endDate;
    private Float distance;
    private Duration time;
    private Set<String> type = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public Duration getTime() {
        return time;
    }

    public void setTime(Duration time) {
        this.time = time;
    }

    public Set<String> getType(){
        return type;
    }

    public void setType(Set<String> type){
        this.type = type;
    }

    public void addType(String type){
        this.type.add(type);
    }

    @Override
    public String toString() {
        return "Challenge [name=" + name + ", startDate=" + startDate + ", endDate=" + endDate + ", distance="
                + distance + ", time=" + time + ", type=" + type + "]";
    }
}