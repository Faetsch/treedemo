package database;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import database.model.Baum;
import database.model.Befund;
import database.model.GeoLocation;
import database.model.Spezies;


@Stateless
public class DatabaseService 
{
	@PersistenceContext(name = "baumpu")
	private EntityManager em;
	
	public List<GeoLocation> getGeoLocationOfAllTreesByStrasseId(int strasseId)
	{
		TypedQuery<GeoLocation> geoLocationsQuery = em.createNamedQuery("Baum.findGeoLocationofAllBaeumeByStrasseId", GeoLocation.class);
		geoLocationsQuery.setParameter("strasseId", strasseId);
		return geoLocationsQuery.getResultList();
	}
	
	public List<Baum> getBäumeByStrasseId(int strasseId)
	{
		TypedQuery<Baum> bäumeQuery = em.createNamedQuery("Baum.findAllBaeumeByStrasseId", Baum.class);
		bäumeQuery.setParameter("strasseId", strasseId);
		return bäumeQuery.getResultList();
	}
	
	public int getAgeOfBaumByBaumId(int baumId)
	{
		try
		{
			TypedQuery<LocalDate> pflanzdatumQuery = em.createNamedQuery("Baum.findPflanzdatumByBaumId", LocalDate.class);
			pflanzdatumQuery.setParameter("baumId", baumId);
			LocalDate pflanzdatum = pflanzdatumQuery.getSingleResult();
			LocalDate currentDate = LocalDate.now();
			return Period.between(pflanzdatum, currentDate).getYears();	
		}
		
		catch(NoResultException e)
		{
			return -1;
		}
		
	}

	public Spezies getSpeziesOfBaumByBaumId(int baumId)
	{
		try
		{
			TypedQuery<Spezies> speziesQuery = em.createNamedQuery("Baum.findSpeziesByBaumId", Spezies.class);
			speziesQuery.setParameter("baumId", baumId);
			return speziesQuery.getSingleResult();	
		}
		catch(NoResultException e)
		{
			return new Spezies();
		}
	}
	
	public List<Befund> getBefundeOfBaumByBaumId(int baumId)
	{
		TypedQuery<Befund> befundeQuery = em.createNamedQuery("Befund.findAllBefundeByBaumId", Befund.class);
		befundeQuery.setParameter("baumId", baumId);
		return befundeQuery.getResultList();
	}
	
	public List<Baum> getAllBäume()
	{
		TypedQuery<Baum> bäumeQuery = em.createNamedQuery("Baum.findAll", Baum.class);
		return bäumeQuery.getResultList();
	}
	
	public void persistObject(Object b)
	{
		//TODO cleanup
		em.persist(b);
	}
	
	public void mergeObject(Object b)
	{
		//TODO cleanup
		em.merge(b);
	}
	

}
