package es.deusto.ingenieria.sd.strava.server.data.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ChallengeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Date startDate;
    private Date endDate;
    private Double distance;
    private Integer time;
    private Set<String> type = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(final Double distance) {
        this.distance = distance;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(final Integer time) {
        this.time = time;
    }

    public Set<String> getType() {
        return type;
    }

    public void setType(final Set<String> type) {
        this.type = type;
    }

    public void addType(final String type) {
        this.type.add(type);
    }

    @Override
    public String toString() {
        return "Challenge [name=" + name + ", startDate=" + startDate + ", endDate=" + endDate + ", distance="
                + distance + ", time=" + time + ", type=" + type + "]";
    }

}