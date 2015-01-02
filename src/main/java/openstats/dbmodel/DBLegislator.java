package openstats.dbmodel;

import java.io.Serializable;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
public class DBLegislator implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;
	
	private String name;
	private String party;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParty() {
		return party;
	}
	public void setParty(String party) {
		this.party = party;
	}
}
