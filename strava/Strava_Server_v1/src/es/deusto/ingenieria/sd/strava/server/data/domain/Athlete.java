package es.deusto.ingenieria.sd.strava.server.data.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Athlete {

    private String name;
    private String email;
    private String password;
    private Float weight;
    private Integer height;
    private Integer restingHeartrate;
    private Integer maxHeartrate;
    private Date dateofbirth;
    private LoginType loginType;
    private List<Activity> activities = new ArrayList<>();
    private List<Challenge> challenges = new ArrayList<>();

    public boolean checkAthlete() {
        return checkLoginType() && checkName() && checkDateofbirth() && checkEmail() && checkPassword() && checkWeight() && checkHeight() && checkRestingHeartrate() && checkMaxHeartrate();
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

    public boolean checkEmail() {
        if (email == null) {
            return false;
        }

        return email.endsWith("@gmail.com");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean checkPassword() {
        if (loginType == LoginType.LOCAL) {
            if (password == null) {
                return false;
            }
        }
        return true;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkWeight() {
        if (weight == null) {
            return true;
        }
        return weight > 0;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public boolean checkHeight() {
        if (height == null) {
            return true;
        }
        return height > 0;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public boolean checkRestingHeartrate() {
        if (restingHeartrate == null) {
            return true;
        }
        if (maxHeartrate == null) {
            return true;
        }
        if (restingHeartrate > maxHeartrate) {
            return false;
        }

        return restingHeartrate >= 0;
    }

    public Integer getRestingHeartrate() {
        return restingHeartrate;
    }

    public void setRestingHeartrate(Integer restingHeartrate) {
        this.restingHeartrate = restingHeartrate;
    }

    public boolean checkMaxHeartrate() {
        if (restingHeartrate == null) {
            return true;
        }
        if (maxHeartrate == null) {
            return true;
        }
        if (maxHeartrate < restingHeartrate) {
            return false;
        }

        return maxHeartrate >= 0;
    }

    public Integer getMaxHeartrate() {
        return maxHeartrate;
    }

    public void setMaxHeartrate(Integer maxHeartrate) {
        this.maxHeartrate = maxHeartrate;
    }

    public boolean checkDateofbirth() {
        return dateofbirth != null;
    }

    public Date getDateofbirth() {
        return dateofbirth;
    }
    public void setDateofbirth(Date dateofbirth) {
        this.dateofbirth = dateofbirth;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public void addActivities(Activity activity) {
        this.activities.add(activity);
    }

    public List<Challenge> getChallenges() {
        return challenges;
    }

    public void setChallenges(List<Challenge> challenges) {
        this.challenges = challenges;
    }

    public void addChallenge(Challenge challenge) {
        this.challenges.add(challenge);
    }

    public boolean checkLoginType() {
        return loginType != null;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    @Override
    public String toString() {
        return "Athlete [name=" + name + ", email=" + email + ", password=" + password + ", weight=" + weight
                + ", height=" + height + ", restingHeartrate=" + restingHeartrate + ", maxHeartrate=" + maxHeartrate
                + ", dateofbirth=" + dateofbirth + ", activities=" + activities + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Athlete other = (Athlete) obj;
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
        if (Float.floatToIntBits(weight) != Float.floatToIntBits(other.weight))
            return false;
        if (height != other.height)
            return false;
        if (restingHeartrate != other.restingHeartrate)
            return false;
        if (maxHeartrate != other.maxHeartrate)
            return false;
        if (dateofbirth == null) {
            if (other.dateofbirth != null)
                return false;
        } else if (!dateofbirth.equals(other.dateofbirth))
            return false;
        if (activities == null) {
            if (other.activities != null)
                return false;
        } else if (!activities.equals(other.activities))
            return false;
        return true;
    }

}
