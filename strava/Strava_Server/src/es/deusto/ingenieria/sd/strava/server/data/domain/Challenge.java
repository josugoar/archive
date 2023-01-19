package es.deusto.ingenieria.sd.strava.server.data.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.Join;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.datanucleus.api.jdo.annotations.CreateTimestamp;

@PersistenceCapable(detachable = "true")
public class Challenge {

    @PrimaryKey
    private String name;

    @CreateTimestamp
    private Date startDate;

    @CreateTimestamp
    private Date endDate;
    private Double distance;
    private Integer time;

    @Join
    @Persistent(defaultFetchGroup = "true")
    private Set<SportType> type = new HashSet<>();

    public boolean isActive() {
        if (startDate == null || endDate == null) {
            return false;
        }

        Date date = new Date();

        return date.before(endDate) && date.after(startDate);
    }

    public boolean hasType(final SportType type) {
        return this.type.contains(type);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("name == null");
        }

        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(final Date startDate) throws IllegalArgumentException {
        if (startDate == null) {
            throw new IllegalArgumentException("startDate == null");
        }

        if (endDate != null && startDate.after(endDate)) {
            throw new IllegalArgumentException("endDate != null && startDate.after(endDate)");
        }

        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(final Date endDate) throws IllegalArgumentException {
        if (startDate == null) {
            throw new IllegalArgumentException("startDate == null");
        }

        if (startDate != null && endDate.before(startDate)) {
            throw new IllegalArgumentException("startDate != null && endDate.before(startDate)");
        }

        this.endDate = endDate;
    }

    public Double getDistance() {
        return distance;
    }

    public Integer getTime() {
        return time;
    }

    public void setDistanceOrTime(final Double distance, final Integer time) throws IllegalArgumentException {
        if (distance == null && time == null) {
            throw new IllegalArgumentException("distance == null && time == null");
        }

        if (distance != null && time != null) {
            throw new IllegalArgumentException("distance != null && time != null");
        }

        if (distance != null && distance <= 0) {
            throw new IllegalArgumentException("distance != null && distance <= 0");
        }

        if (time != null && time <= 0) {
            throw new IllegalArgumentException("time != null && time <= 0");
        }

        this.distance = distance;
        this.time = time;
    }

    public Set<SportType> getType() {
        return type;
    }

    public void setType(final Set<SportType> type) throws IllegalArgumentException {
        if (type == null) {
            throw new IllegalArgumentException("type == null");
        }

        if (type.contains(null)) {
            throw new IllegalArgumentException("type.contains(null)");
        }

        this.type = type;
    }

    public void addType(final SportType type) throws IllegalArgumentException {
        if (type == null) {
            throw new IllegalArgumentException("type == null");
        }

        if (this.type.contains(type)) {
            throw new IllegalArgumentException("this.type.contains(type)");
        }

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
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
