package openstats.model;

import java.io.Serializable;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
public class Legislator implements DtoInterface<Legislator>, Serializable {
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
	public Legislator createDto(DTOTYPE dtoType) {
		Legislator legislator = new Legislator();
		legislator.setName(getName());
		legislator.setParty(getParty());
		return legislator;
	}

}
