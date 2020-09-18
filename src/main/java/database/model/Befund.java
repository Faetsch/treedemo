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
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@XmlRootElement
@Entity
//"Das Frontend soll die Baume in der Karte je nach Befunddaten einfärben und benötigt
//daher die Liste der Befunde"
@NamedQuery(name = "Befund.findAllBefundeByBaumId", query = "SELECT b FROM Befund b WHERE b.baum.id = :baumId")
public class Befund implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "befund_id")
	private int id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@Column(columnDefinition = "DATE")
	private LocalDate erhobenAm;
	
	@Column(columnDefinition= "VARCHAR(64)")
	private String beschreibung;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "baum_id")
	private Baum baum;
	

	public Befund() {
		super();
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public LocalDate getErhobenAm() {
		return erhobenAm;
	}


	public void setErhobenAm(LocalDate erhobenAm) {
		this.erhobenAm = erhobenAm;
	}


	public String getBeschreibung() {
		return beschreibung;
	}


	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}


	public Baum getBaum() {
		return baum;
	}


	public void setBaum(Baum baum) {
		this.baum = baum;
	}
	
	
   
}
