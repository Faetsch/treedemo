package mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javax.ejb.Stateless;
import javax.inject.Inject;

import database.DatabaseService;
import database.model.Baum;
import database.model.Befund;
import database.model.GeoLocation;
import database.model.Spezies;
import database.model.Stadt;
import database.model.Strasse;


//TODO bessere erzeugung von dummies, ugly ugly ugly

@Stateless
public class MockObjectCreator 
{	
	@Inject
	DatabaseService dao;
	LocalDate today = LocalDate.now();
	ArrayList<Spezies> speziesList = new ArrayList<Spezies>();
	public static int currStadtCounter = 0;
	
	
	public void createAndPersistMockObjects()
	{
		for(int i = 0; i < 250; i++)
		{
			Spezies spezies = new Spezies();
			spezies.setBotanischerName("BotanischerName" + i);
			spezies.setTrivialName("TrivialName" + i);
			speziesList.add(spezies);
			dao.persistObject(spezies);
		}
		Stadt stadt;
		short anzahlStrassen;
		for(int i = 0; i < 10; i++)
		{
			stadt = new Stadt();
			stadt.setName("Stadt" + i);
			stadt.setPlz("Plz" + i);
			
			anzahlStrassen = (short)ThreadLocalRandom.current().nextInt(10, 15 + 1);
			Strasse strasse;
			short anzahlBäume;
			for(int j = 0; j < anzahlStrassen; j++)
			{
//				System.out.println("Stadt: " + i + " | Strasse: " + j);
				strasse = new Strasse();
				strasse.setStadt(stadt);
				strasse.setName("Strasse" + j);
				strasse.setVerwaltungskuerzel("Verwaltungskuerzel" + j);
				anzahlBäume = (short)ThreadLocalRandom.current().nextInt(0, 15 + 1);
				Baum baum;
				short anzahlBefunde;
				Spezies currSpezies;
				for(int k = 0; k < anzahlBäume; k++)
				{
					baum = new Baum();
					baum.setStrasse(strasse);
					baum.setNummer(k);
					baum.setPflanzdatum(today);
					GeoLocation location = new GeoLocation();
					location.setLatitude(j);
					location.setLongitude(k);
					location.setBaum(baum);
					baum.setGeoLocation(location);
					//im Mittel 5 Befunde: (1+2+3+4+5+6+7+8+9)/9 = 5
					anzahlBefunde = (short)ThreadLocalRandom.current().nextInt(1, 9+1);
					Befund befund = null;
					for(int l = 0; l < anzahlBefunde; l++)
					{
						befund = new Befund();
						befund.setErhobenAm(today);
						befund.setBeschreibung("Beschreibung" + l);
						befund.setBaum(baum);
						dao.persistObject(befund);
					}
					
					ArrayList<Integer> speziesIndeces = generateRandomSpeziesIndecesForStrasse();
					currSpezies = speziesList.get(speziesIndeces.get(ThreadLocalRandom.current().nextInt(0, 10)));
					baum.setSpezies(currSpezies);
					currSpezies.addBaum(baum);
					dao.persistObject(baum);
					
				}
			}	
		}	
	}
	
	
//	public void createSpezies() 
//	{
//		for(int i = 0; i < 250; i++)
//		{
//			Spezies spezies = new Spezies();
//			spezies.setBotanischerName("BotanischerName" + i);
//			spezies.setTrivialName("TrivialName" + i);
//			//spezies.setBaum(bäume);
//			speziesList.add(spezies);
//			dao.persistObject(spezies);
//		}	
//	}
//	
//	public void createAndPersistDummies()
//	{
//		createSpezies();
//		for(short i = 0; i < 10; i++)
//		{
//			createAndPersistStadt();
//		}
//	}
//	
//	public void createAndPersistStadt()
//	{
//		Stadt stadt = new Stadt();
//		stadt.setName("Stadt" + currStadtCounter);
//		stadt.setPlz("Plz" + currStadtCounter);
//		currStadtCounter++;
//		
//		short anzahlStrassen = (short)ThreadLocalRandom.current().nextInt(10, 20 + 1);
//		for(short i = 0; i < anzahlStrassen; i++)
//		{
//			createAndPersistStrasse(stadt, i);
//		}
//	}
//	
//	public void createAndPersistStrasse(Stadt stadt, short nummer)
//	{
//		Strasse strasse = new Strasse();
//		strasse.setStadt(stadt);
//		strasse.setName("Strasse" + nummer);
//		strasse.setVerwaltungskuerzel("Verwaltungskuerzel" + nummer);
//		short anzahlBäume = (short)ThreadLocalRandom.current().nextInt(0, 20 + 1);
//		for(short i = 0; i < anzahlBäume; i++)
//		{
//			createAndPersistBaumWithBefunde(strasse, i, i, i);
//		}
//	}
//	
//	public void createAndPersistBaumWithBefunde(Strasse strasse, long nummer, int latitude, int longitude)
//	{
//		Baum baum = new Baum();
//		baum.setStrasse(strasse);
//		baum.setNummer(nummer);
//		baum.setPflanzdatum(today);
//		GeoLocation location = new GeoLocation();
//		location.setLatitude(latitude);
//		location.setLongitude(longitude);
//		location.setBaum(baum);
//		baum.setGeoLocation(location);
//		//im Mittel 5 Befunde: (1+2+3+4+5+6+7+8+9)/9 = 5
//		short anzahlBefunde = (short)ThreadLocalRandom.current().nextInt(1, 9+1);
//		for(int i = 0; i < anzahlBefunde; i++)
//		{
//			createAndPersistBefunde(baum, i);
//		}
//		
//		ArrayList<Integer> speziesIndeces = generateRandomSpeziesIndecesForStrasse();
//		Spezies currSpezies = speziesList.get(speziesIndeces.get(ThreadLocalRandom.current().nextInt(0, 10)));
//		baum.setSpezies(currSpezies);
//		currSpezies.addBaum(baum);
//		dao.persistObject(baum);
//	}
//	
//	public void createAndPersistBefunde(Baum baum, int nummer)
//	{
//		Befund befund = new Befund();
//		befund.setErhobenAm(today);
//		befund.setBeschreibung("Beschreibung" + nummer);
//		befund.setBaum(baum);
//		dao.persistObject(befund);
//	}
	
	private ArrayList<Integer> generateRandomSpeziesIndecesForStrasse()
	{
		ArrayList<Integer> indeces = new ArrayList<Integer>();
		for(int i = 0; i < 10; i++)
		{
			indeces.add(ThreadLocalRandom.current().nextInt(0, 250));
		}
		return indeces;
	}
	

}
