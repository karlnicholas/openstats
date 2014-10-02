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
	private List<Legislator> legislators;
	@ElementCollection
	private Map<String, ArrayList<Long>> aggregates;
	@ElementCollection
	private Map<String, ArrayList<Double>> computations;
	
	public District() {
		legislators = new ArrayList<Legislator>();
		aggregates = new TreeMap<String, ArrayList<Long>>();
		computations = new TreeMap<String, ArrayList<Double>>();
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
	
	public Map<String, ArrayList<Long>> getAggregates() {
		return aggregates;
	}

	public void setAggregates(Map<String, ArrayList<Long>> aggregates) {
		this.aggregates = aggregates;
	}

	public Map<String, ArrayList<Double>> getComputations() {
		return computations;
	}

	public void setComputations(Map<String, ArrayList<Double>> computations) {
		this.computations = computations;
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
			for ( String key: getAggregates().keySet() ) {
				district.getAggregates().put(key, aggregates.get(key));
			}
			for ( String key: getComputations().keySet() ) {
				district.getComputations().put(key, computations.get(key));
			}
			break;
		case SUMMARY: // intentionally left blank
			break;
		}
		return district;
	}

}
