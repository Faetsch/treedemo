package database.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
//z.B. 97074 Würzburg, 97070 Würzburg wären 2 Datensätze in der DB mit unterschiedlichen ids
@Table(uniqueConstraints={
	    @UniqueConstraint(columnNames = {"plz", "name"})})
public class Stadt implements Serializable 
{
	private static final long serialVersionUID = 2626065564276739219L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	@Column(columnDefinition = "VARCHAR(64)")
	private String plz;
	
	@NotNull
	@Column(columnDefinition = "VARCHAR(64)")
	private String name;

	public Stadt() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
   
}
