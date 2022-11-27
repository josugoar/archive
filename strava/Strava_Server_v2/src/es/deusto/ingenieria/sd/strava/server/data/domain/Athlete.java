package es.deusto.ingenieria.sd.strava.server.data.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Athlete {

    private String name;
    private String email;
    private String password;
    private Double weight;
    private Integer height;
    private Integer restingHeartRate;
    private Integer maxHeartRate;
    private Date dateOfBirth;
    private LoginType loginType;
    private Set<Activity> activities = new HashSet<>();
    private Set<Challenge> challenges = new HashSet<>();

    public boolean hasChallenge(final Challenge challenge) {
        return challenges.contains(challenge);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) throws IllegalArgumentException {
        if (email == null) {
            throw new IllegalArgumentException("email == null");
        }

        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    // TODO
    public void setPassword(final String password) throws IllegalArgumentException {
        this.password = password;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(final Double weight) throws IllegalArgumentException {
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("weight != null && weight < 0");
        }

        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(final Integer height) throws IllegalArgumentException {
        if (height != null && height < 0) {
            throw new IllegalArgumentException("height != null && height < 0");
        }

        this.height = height;
    }

    public Integer getRestingHeartRate() {
        return restingHeartRate;
    }

    public void setRestingHeartRate(final Integer restingHeartRate) throws IllegalArgumentException {
        if (restingHeartRate < 0) {
            throw new IllegalArgumentException("restingHeartRate < 0");
        }

        if (maxHeartRate != null && restingHeartRate > maxHeartRate) {
            throw new IllegalArgumentException("maxHeartRate != null && restingHeartRate > maxHeartRate");
        }

        this.restingHeartRate = restingHeartRate;
    }

    public Integer getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(final Integer maxHeartRate) throws IllegalArgumentException {
        if (maxHeartRate < 0) {
            throw new IllegalArgumentException("maxHeartRate < 0");
        }

        if (restingHeartRate != null && maxHeartRate < restingHeartRate) {
            throw new IllegalArgumentException("restingHeartRate != null && maxHeartRate < restingHeartRate");
        }

        this.maxHeartRate = maxHeartRate;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(final Date dateOfBirth) throws IllegalArgumentException {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("dateOfBirth == null");
        }

        this.dateOfBirth = dateOfBirth;
    }

    public Set<Activity> getActivities() {
        return activities;
    }

    public void setActivities(final Set<Activity> activities) throws IllegalArgumentException {
        if (activities == null) {
            throw new IllegalArgumentException("activities == null");
        }

        if (activities.contains(null)) {
            throw new IllegalArgumentException("activities.contains(null)");
        }

        this.activities = activities;
    }

    public void addActivity(final Activity activity) throws IllegalArgumentException {
        if (activity == null) {
            throw new IllegalArgumentException("activity == null");
        }

        if (activities.contains(activity)) {
            throw new IllegalArgumentException("activities.contains(activity)");
        }

        this.activities.add(activity);
    }

    public Set<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(final Set<Challenge> challenges) throws IllegalArgumentException {
        if (challenges == null) {
            throw new IllegalArgumentException("challenges == null");
        }

        if (challenges.contains(null)) {
            throw new IllegalArgumentException("challenges.contains(null)");
        }

        this.challenges = challenges;
    }

    public void addChallenge(final Challenge challenge) throws IllegalArgumentException {
        if (challenges == null) {
            throw new IllegalArgumentException("challenges == null");
        }

        if (challenges.contains(challenge)) {
            throw new IllegalArgumentException("challenges.contains(challenge)");
        }

        this.challenges.add(challenge);
    }

    public LoginType getLoginType() {
        return loginType;
    }

    // TODO
    public void setLoginType(final LoginType loginType) throws IllegalArgumentException {
        if (loginType == null) {
            throw new IllegalArgumentException("loginType == null");
        }

        this.loginType = loginType;
    }

    @Override
    public String toString() {
        return "Athlete [name=" + name + ", email=" + email + ", password=" + password + ", weight=" + weight
                + ", height=" + height + ", restingHeartRate=" + restingHeartRate + ", maxHeartRate=" + maxHeartRate
                + ", dateOfBirth=" + dateOfBirth + ", loginType=" + loginType + ", activities=" + activities
                + ", challenges=" + challenges + "]";
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Athlete other = (Athlete) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (weight == null) {
            if (other.weight != null)
                return false;
        } else if (!weight.equals(other.weight))
            return false;
        if (height == null) {
            if (other.height != null)
                return false;
        } else if (!height.equals(other.height))
            return false;
        if (restingHeartRate == null) {
            if (other.restingHeartRate != null)
                return false;
        } else if (!restingHeartRate.equals(other.restingHeartRate))
            return false;
        if (maxHeartRate == null) {
            if (other.maxHeartRate != null)
                return false;
        } else if (!maxHeartRate.equals(other.maxHeartRate))
            return false;
        if (dateOfBirth == null) {
            if (other.dateOfBirth != null)
                return false;
        } else if (!dateOfBirth.equals(other.dateOfBirth))
            return false;
        if (loginType != other.loginType)
            return false;
        if (activities == null) {
            if (other.activities != null)
                return false;
        } else if (!activities.equals(other.activities))
            return false;
        if (challenges == null) {
            if (other.challenges != null)
                return false;
        } else if (!challenges.equals(other.challenges))
            return false;
        return true;
    }

}
