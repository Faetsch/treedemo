package database.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
//laut wikipedia, in Deutschland sind Straßen eindeutig durch plz und Namen identifizierbar. plz ist im fk stadt, daher (stadt, name) unique
@Table(uniqueConstraints={
	    @UniqueConstraint(columnNames = {"stadt_id", "name"})})


public class Strasse implements Serializable 
{
	private static final long serialVersionUID = -6585443169195024531L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(columnDefinition = "VARCHAR(64)")
	private String name;
	
	@Column(columnDefinition = "VARCHAR(64)")
	private String verwaltungskuerzel;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "stadt_id")
	private Stadt stadt;

	
	public Strasse() {
		super();
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getVerwaltungskuerzel() {
		return verwaltungskuerzel;
	}


	public void setVerwaltungskuerzel(String verwaltungskuerzel) {
		this.verwaltungskuerzel = verwaltungskuerzel;
	}


	public Stadt getStadt() {
		return stadt;
	}


	public void setStadt(Stadt stadt) {
		this.stadt = stadt;
	}
	
	
   
}
