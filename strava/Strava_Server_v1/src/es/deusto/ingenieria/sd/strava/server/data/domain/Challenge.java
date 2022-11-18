package es.deusto.ingenieria.sd.strava.server.data.domain;

import java.time.Duration;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

public class Challenge {

    private Integer id;
    private String name;
    private Date startDate;
    private Date endDate;
    private Float distance;
    private Duration time;
    private Set<SportType> type = new HashSet<>();

    public boolean checkChallenge() {
        return checkId() && checkName() && checkDate() && checkDistance() && checkTime() && checkType();
    }

    public boolean isActive() {
        if (endDate == null) {
            return false;
        }

        return new Date().compareTo(endDate) < 0;
    }

    public boolean checkId() {
        return id != null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean checkName() {
        return name != null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean checkDate() {
        if (startDate == null) {
            return false;
        }
        if (endDate == null) {
            return false;
        }

        return startDate.before(endDate);
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

    public boolean checkDistance() {
        if (distance == null && time == null || distance != null && time != null) {
            return false;
        }
        if (distance == null) {
            return true;
        }
        return distance >= 0;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        this.distance = distance;
    }

    public boolean checkTime() {
        if (distance == null && time == null || distance != null && time != null) {
            return false;
        }
        if (time == null) {
            return true;
        }
        return !time.isNegative();
    }

    public Duration getTime() {
        return time;
    }

    public void setTime(Duration time) {
        this.time = time;
    }

    public boolean checkType() {
        return type != null;
    }

    public Set<SportType> getType() {
        return type;
    }

    public void setType(Set<SportType> type){
        this.type = type;
    }

    public void addType(SportType type){
        this.type.add(type);
    }


    @Override
    public String toString() {
        return "Challenge [name=" + name + ", startDate=" + startDate + ", endDate=" + endDate + ", distance="
                + distance + ", time=" + time + ", type=" + type + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Challenge other = (Challenge) obj;
        return id == other.id;
    }

}
