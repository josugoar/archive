package es.deusto.ingenieria.sd.strava.server.data.domain;


public enum SportType { 
    CYCLING("Cycling"), 
    RUNNING("Running"); 

    private String name;

    SportType (String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static SportType fromString(String sport) throws IllegalArgumentException{
        switch (sport.toUpperCase()) {
            case "CYCLING":
                return CYCLING;
            case "RUNNING":
                return RUNNING;
            default:
                throw new IllegalArgumentException();
        }
    }
}