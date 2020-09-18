package database.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@XmlRootElement
@Entity
@Table(uniqueConstraints={
	    @UniqueConstraint(columnNames = {"strasse_id", "nummer"})})

//"Die Frontend-Anwendung soll alle Bäume einer Straße als Punkte auf einer Karte zeigen
//und benötigt daher die Geokoordinaten der Bäume"
@NamedQuery(name = "Baum.findGeoLocationofAllBaeumeByStrasseId", query = "SELECT b.geoLocation FROM Baum b where b.strasse.id = :strasseId")
@NamedQuery(name = "Baum.findAllBaeumeByStrasseId", query = "SELECT b FROM Baum b where b.strasse.id =: strasseId")

//"Das Frontend soll per Pop-up die Details wie Spezies und Alter eines Baumes anzeigen
//und benötigt daher Zugriff auf diese Informationen"
@NamedQuery(name = "Baum.findSpeziesByBaumId", query = "SELECT b.spezies FROM Baum b where b.id = :baumId")
@NamedQuery(name = "Baum.findPflanzdatumByBaumId", query = "SELECT b.pflanzdatum FROM Baum b where b.id = :baumId")
@NamedQuery(name = "Baum.findAll", query = "SELECT b FROM Baum b")



public class Baum implements Serializable 
{	
	private static final long serialVersionUID = 4979515288031153830L;	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	//Semantik von diesem Attribut nummer? Ist aus dem UML nicht klar
	//Nach eigener Recherche sind Baumnummern nur in einer geg. Straße eindeutig
	//   -> unter der Semantik müsste (nummer, strasse) unique sein, aber nicht nummer alleine
	//   -> aber: im UML ist die Beziehung zwischen Baum und Strasse als Aggregation - und nicht als Komposition - festgelegt
	//   -> zum Zweck dieser Aufgabe wird die Beziehung im UML trotzdem als Komposition implementiert
	private long nummer;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate pflanzdatum;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "strasse_id")
	private Strasse strasse;
	
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "spezies_id")
	//@JsonManagedReference
	private Spezies spezies;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "geolocation_id")
	//@JsonManagedReference
	private GeoLocation geoLocation;
	

	public Baum() 
	{
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getNummer() {
		return nummer;
	}

	public void setNummer(long nummer) {
		this.nummer = nummer;
	}

	public LocalDate getPflanzdatum() {
		return pflanzdatum;
	}



	public void setPflanzdatum(LocalDate pflanzdatum) {
		this.pflanzdatum = pflanzdatum;
	}

	public Strasse getStrasse() {
		return strasse;
	}

	public void setStrasse(Strasse strasse) {
		this.strasse = strasse;
	}

	public Spezies getSpezies() {
		return spezies;
	}

	public void setSpezies(Spezies spezies) {
		this.spezies = spezies;
	}



	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}
	
}
