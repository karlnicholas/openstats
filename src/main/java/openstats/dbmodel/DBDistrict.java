package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

import openstats.osmodel.*;

@SuppressWarnings("serial")
@Entity public class DBDistrict implements Comparable<DBDistrict>, Serializable {
	@Id @GeneratedValue private Long id;
	
	private String chamber;
	private String district;
	
	@OneToMany(cascade = CascadeType.ALL)
	private List<DBLegislator> legislators = new ArrayList<DBLegislator>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private Map<DBGroup, AggregateValues> aggregateMap = new LinkedHashMap<DBGroup, AggregateValues>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private Map<DBGroup, ComputationValues> computationMap = new LinkedHashMap<DBGroup, ComputationValues>();
	
	public DBDistrict() {}
	public DBDistrict(DBGroup dbGroup, OSDistrict osDistrict) {
		this.chamber = osDistrict.getChamber();
		this.district = osDistrict.getDistrict();
		// skip legislators for now
		if ( osDistrict.getAggregateValues() != null ) {
			aggregateMap.put(dbGroup, new AggregateValues(osDistrict.getAggregateValues()) );
		}
		if ( osDistrict.getComputationValues() != null ) {
			computationMap.put(dbGroup, new ComputationValues(osDistrict.getComputationValues()) );
		}
	}

	public String getChamber() {
		return chamber;
	}
	public void setChamber(String chamber) {
		this.chamber = chamber;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
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
