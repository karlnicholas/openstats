package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

import openstats.model.*;
import openstats.model.District.CHAMBER;

@SuppressWarnings("serial")
@Entity public class DBDistrict implements Comparable<DBDistrict>, Serializable {
	@Id @GeneratedValue private Long id;
	
	@Column(length=3)
	private String district;
	private CHAMBER chamber;
	private String description;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<DBLegislator> legislators = new ArrayList<DBLegislator>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private Map<DBGroup, AggregateValues> aggregateMap = new LinkedHashMap<DBGroup, AggregateValues>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private Map<DBGroup, ComputationValues> computationMap = new LinkedHashMap<DBGroup, ComputationValues>();
	
	public DBDistrict() {}
	public DBDistrict(District district) {
		this.district = district.getDistrict();
		this.chamber = district.getChamber();
		this.description = district.getDescription();
	}
	public DBDistrict copyGroup(DBGroup dbGroup, District district) {
		// skip legislators for now
		if ( district.getAggregateValues() != null ) {
			aggregateMap.put(dbGroup, new AggregateValues(district.getAggregateValues()) );
		}
		if ( district.getComputationValues() != null ) {
			computationMap.put(dbGroup, new ComputationValues(district.getComputationValues()) );
		}
		// useful for chaining
		return this;
	}

	public void removeGroup(DBGroup dbGroup) {
		// skip legislators for now
		if ( aggregateMap.containsKey(dbGroup) ) {
			aggregateMap.remove(dbGroup);
		}
		if ( computationMap.containsKey(dbGroup) ) {
			computationMap.remove(dbGroup);
		}
	}

	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public CHAMBER getChamber() {
		return chamber;
	}
	public void setChamber(CHAMBER chamber) {
		this.chamber = chamber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<DBLegislator> getLegislators() {
		return legislators;
	}
	public void setLegislators(List<DBLegislator> legislators) {
		this.legislators = legislators;
	}
	public Map<DBGroup, AggregateValues> getAggregateMap() {
		return aggregateMap;
	}

	public Map<DBGroup, ComputationValues> getComputationMap() {
		return computationMap;
	}

	@Override
	public int compareTo(DBDistrict o) {
		if ( !chamber.equals(o.chamber)) return chamber.compareTo(o.chamber);
		return district.compareTo(o.district);
	}

}
