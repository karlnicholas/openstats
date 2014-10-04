package openstats.model;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity public class District implements Comparable<District>, DtoInterface<District>, Serializable {
	@Id @GeneratedValue private Long id;
	
	private String chamber;
	private String district;
	@OneToMany(cascade = CascadeType.ALL)
	private List<Legislator> legislators = new ArrayList<Legislator>();
	@OneToMany(cascade = CascadeType.ALL)
	private Map<GroupName, AggregateValues> aggregateMap = new LinkedHashMap<GroupName, AggregateValues>();
	@OneToMany(cascade = CascadeType.ALL)
	private Map<GroupName, ComputationValues> computationMap = new LinkedHashMap<GroupName, ComputationValues>();
	
	public District() {
		
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
	public List<Legislator> getLegislators() {
		return legislators;
	}
	public void setLegislators(List<Legislator> legislators) {
		this.legislators = legislators;
	}
	public Map<GroupName, AggregateValues> getAggregateMap() {
		return aggregateMap;
	}

	public Map<GroupName, ComputationValues> getComputationMap() {
		return computationMap;
	}

	@Override
	public int compareTo(District o) {
		if ( !chamber.equals(o.chamber)) return chamber.compareTo(o.chamber);
		return district.compareTo(o.district);
	}
	@Override
	public District createDto(openstats.model.DtoInterface.DTOTYPE dtoType) {
		District district = new District();
		district.setChamber(getChamber());
		district.setDistrict(getDistrict());
		switch ( dtoType ) {
		case FULL:
			for ( Legislator l: getLegislators() ) {
				district.getLegislators().add(l.createDto(dtoType));
			}
			for ( GroupName key: getAggregateMap().keySet() ) {
				district.getAggregateMap().put(key, aggregateMap.get(key));
			}
			for ( GroupName key: getComputationMap().keySet() ) {
				district.getComputationMap().put(key, computationMap.get(key));
			}
			break;
		case SUMMARY: // intentionally left blank
			break;
		}
		return district;
	}

}
