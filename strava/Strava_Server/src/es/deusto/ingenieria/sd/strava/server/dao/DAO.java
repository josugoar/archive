package es.deusto.ingenieria.sd.strava.server.dao;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import es.deusto.ingenieria.sd.strava.server.data.domain.Activity;
import es.deusto.ingenieria.sd.strava.server.data.domain.Athlete;
import es.deusto.ingenieria.sd.strava.server.data.domain.Challenge;

public class DAO implements IDAO {

    private final PersistenceManagerFactory pmf;

    private static IDAO instance;

    private DAO() {
        pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
    }

    public static IDAO getInstance() {
        if (instance == null) {
            instance = new DAO();
        }

        return instance;
    }

    private <T> void storeObject(final T object) {
        final PersistenceManager pm = pmf.getPersistenceManager();
        final Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            System.err.println("   * Storing an object: " + object);
            pm.makePersistent(object);
            tx.commit();
        } catch (final Exception ex) {
            System.err.println("   $ Error storing an object: " + ex.getMessage());
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            pm.close();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getObject(final Class<T> cls, final String condition) {
        final PersistenceManager pm = pmf.getPersistenceManager();
        pm.getFetchPlan().setMaxFetchDepth(3);

        final Transaction tx = pm.currentTransaction();
        T object = null;

        try {
            System.out.println("   * Querying a Object: " + condition);

            tx.begin();
            final Query<?> query = pm.newQuery("SELECT FROM " + cls.getName() + " WHERE " + condition);
            query.setUnique(true);
            object = (T) query.execute();
            tx.commit();

        } catch (final Exception ex) {
            System.out.println("   $ Error retreiving Object: " + ex.getMessage());
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            pm.close();
        }

        return object;
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getObjects(final Class<T> cls, final String condition) {
        final PersistenceManager pm = pmf.getPersistenceManager();
        pm.getFetchPlan().setMaxFetchDepth(3);

        final Transaction tx = pm.currentTransaction();
        final List<T> objects = new ArrayList<>();

        try {
            System.out.println("   * Retrieving an Extent for Objects.");

            tx.begin();
            final Extent<T> extent = pm.getExtent(cls, true);
            final Query<T> query = pm.newQuery(extent, condition);

            for (final T object : (List<T>) query.execute()) {
                objects.add(object);
            }

            tx.commit();
        } catch (final Exception ex) {
            System.out.println("   $ Error retrieving an extent: " + ex.getMessage());
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            pm.close();
        }

        return objects;
    }

    private <T> void deleteObject(final T object) {
        final PersistenceManager pm = pmf.getPersistenceManager();
        final Transaction tx = pm.currentTransaction();

        try {
            tx.begin();
            pm.deletePersistent(object);
            tx.commit();
        } catch (final Exception ex) {
            System.out.println("   $ Error deleting an Object: " + ex.getMessage());
        } finally {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            pm.close();
        }
    }

    @Override
    public void storeActivity(final Activity activity) {
        storeObject(activity);
    }

    @Override
    public Activity getActivity(final String name, final String email) {
        return getObject(Activity.class, "name == '" + name + "'' AND email == '" + email + "'");
    }

    public boolean containsActivity(final String name, final String email) {
        return getActivity(name, email) != null;
    }

    @Override
    public List<Activity> getActivities(final String condition) {
        return getObjects(Activity.class, condition);
    }

    @Override
    public void deleteActivity(final Activity activity) {
        deleteObject(activity);
    }

    @Override
    public void storeAthlete(final Athlete athlete) {
        storeObject(athlete);
    }

    @Override
    public Athlete getAthlete(final String email) {
        return getObject(Athlete.class, "email == '" + email + "'");
    }

    public boolean containsAthlete(final String email) {
        return getAthlete(email) != null;
    }

    @Override
    public List<Athlete> getAthletes(final String condition) {
        return getObjects(Athlete.class, condition);
    }

    @Override
    public void deleteAthlete(final Athlete athlete) {
        deleteObject(athlete);
    }

    @Override
    public void storeChallenge(final Challenge challenge) {
        storeObject(challenge);
    }

    @Override
    public Challenge getChallenge(final String name) {
        return getObject(Challenge.class, "name == '" + name + "'");
    }

    public boolean containsChallenge(final String name) {
        return getChallenge(name) != null;
    }

    @Override
    public List<Challenge> getChallenges(final String condition) {
        return getObjects(Challenge.class, condition);
    }

    @Override
    public void deleteChallenge(final Challenge challenge) {
        deleteObject(challenge);
    }

}
