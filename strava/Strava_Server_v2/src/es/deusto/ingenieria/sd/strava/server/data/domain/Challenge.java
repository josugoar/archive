package es.deusto.ingenieria.sd.strava.server.data.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Challenge {

    private String name;
    private Date startDate;
    private Date endDate;
    private Double distance;
    private Integer time;
    private Set<SportType> type = new HashSet<>();

    public boolean isActive() {
        if (endDate == null) {
            return false;
        }

        return new Date().before(endDate);
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

    public void setDistance(final Double distance) throws IllegalArgumentException {
        if (time != null) {
            throw new IllegalArgumentException("time != null");
        }

        if (distance == null) {
            throw new IllegalArgumentException("distance == null");
        }

        if (distance <= 0) {
            throw new IllegalArgumentException("distance <= 0");
        }

        this.distance = distance;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(final Integer time) throws IllegalArgumentException {
        if (distance != null) {
            throw new IllegalArgumentException("distance != null");
        }

        if (time == null) {
            throw new IllegalArgumentException("time == null");
        }

        if (time <= 0) {
            throw new IllegalArgumentException("time <= 0");
        }

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
        if (startDate == null) {
            if (other.startDate != null)
                return false;
        } else if (!startDate.equals(other.startDate))
            return false;
        if (endDate == null) {
            if (other.endDate != null)
                return false;
        } else if (!endDate.equals(other.endDate))
            return false;
        if (distance == null) {
            if (other.distance != null)
                return false;
        } else if (!distance.equals(other.distance))
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
