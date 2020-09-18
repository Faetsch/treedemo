package database.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

@XmlRootElement
@Entity
public class Spezies implements Serializable {

	
	private static final long serialVersionUID = 2L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(unique = true, columnDefinition = "VARCHAR(64)")
	private String botanischerName;
	
	@Column(columnDefinition= "VARCHAR(64)")
	private String trivialName;
	
	//@JsonBackReference
	@JsonIgnore
	@OneToMany(mappedBy = "spezies")
	private List<Baum> baum;

	public Spezies() 
	{
		super();
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBotanischerName() {
		return botanischerName;
	}

	public void setBotanischerName(String botanischerName) {
		this.botanischerName = botanischerName;
	}

	public String getTrivialName() {
		return trivialName;
	}

	public void setTrivialName(String trivialName) {
		this.trivialName = trivialName;
	}

	public List<Baum> getBaum() {
		return baum;
	}

	public void setBaum(List<Baum> baum) {
		this.baum = baum;
	}
	
	public void addBaum(Baum baum)
	{
		if(this.baum==null)
		{
			this.baum = new ArrayList<Baum>();
		}
		this.baum.add(baum);
	}
	
	
   
}
