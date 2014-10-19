package openstats.model;

import java.io.Serializable;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
public class DBLegislator implements DtoInterface<DBLegislator>, Serializable {
	@Id @GeneratedValue private Long id;
	
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
	@Override
	public DBLegislator createDto(DTOTYPE dtoType) {
		DBLegislator legislator = new DBLegislator();
		legislator.setName(getName());
		legislator.setParty(getParty());
		return legislator;
	}

}
