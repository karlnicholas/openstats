package openstats.model;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

import openstats.osmodel.OSGroup;

@SuppressWarnings("serial")
@Entity public class DBDistrict implements Comparable<DBDistrict>, DtoInterface<DBDistrict>, Serializable {
	@Id @GeneratedValue private Long id;
	
	private String chamber;
	private String district;
	@OneToMany(cascade = CascadeType.ALL)
	private List<DBLegislator> legislators = new ArrayList<DBLegislator>();
	@OneToMany(cascade = CascadeType.ALL)
	private Map<OSGroup, AggregateValues> aggregateMap = new LinkedHashMap<OSGroup, AggregateValues>();
	@OneToMany(cascade = CascadeType.ALL)
	private Map<OSGroup, ComputationValues> computationMap = new LinkedHashMap<OSGroup, ComputationValues>();

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
	public Map<OSGroup, AggregateValues> getAggregateMap() {
		return aggregateMap;
	}

	public Map<OSGroup, ComputationValues> getComputationMap() {
		return computationMap;
	}

	@Override
	public int compareTo(DBDistrict o) {
		if ( !chamber.equals(o.chamber)) return chamber.compareTo(o.chamber);
		return district.compareTo(o.district);
	}
	@Override
	public DBDistrict createDto(DTOTYPE dtoType) {
		DBDistrict district = new DBDistrict();
		district.setChamber(getChamber());
		district.setDistrict(getDistrict());
		switch ( dtoType ) {
		case FULL:
			for ( DBLegislator l: getLegislators() ) {
				district.getLegislators().add(l.createDto(dtoType));
			}
			for ( OSGroup osGroup: getAggregateMap().keySet() ) {
				district.getAggregateMap().put(osGroup, aggregateMap.get(osGroup));
			}
			for ( OSGroup osGroup: getComputationMap().keySet() ) {
				district.getComputationMap().put(osGroup, computationMap.get(osGroup));
			}
			break;
		case SUMMARY: // intentionally left blank
			break;
		}
		return district;
	}

}
