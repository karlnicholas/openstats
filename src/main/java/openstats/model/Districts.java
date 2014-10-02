package openstats.model;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
public class Districts implements DtoInterface<Districts>, Serializable {
	@Id @GeneratedValue private Long id;

	@OneToMany(cascade = CascadeType.ALL)
	private List<District> districtList;
	@OneToMany(cascade={CascadeType.ALL})
	@JoinTable(name="districts_aggregategroupmap")
	private Map<String, GroupInfo> aggregateGroupMap; 
	@OneToMany(cascade={CascadeType.ALL})
	@JoinTable(name="districts_computationgroupmap")
	private Map<String, GroupInfo> computationGroupMap; 
	
	public Districts() {
		districtList = new ArrayList<District>();
		aggregateGroupMap = new TreeMap<String, GroupInfo>(); 
		computationGroupMap = new TreeMap<String, GroupInfo>(); 
	}
	
	public District findDistrict(String chamber, String district) {
		for ( District d: districtList ) {
			if ( d.getChamber().equals(chamber) && d.getDistrict().equals(district)) return d; 
		}
		return null;
	}
	public List<District> getDistrictList() {
		return districtList;
	}
	public void setDistrictList(List<District> districtList) {
		this.districtList = districtList;
	}
	public Map<String, GroupInfo> getComputationGroupMap() {
		return computationGroupMap;
	}
	public void setComputationGroupMap(Map<String, GroupInfo> computationGroupMap) {
		this.computationGroupMap = computationGroupMap;
	}
	public Map<String, GroupInfo> getAggregateGroupMap() {
		return aggregateGroupMap;
	}
	public void setAggregateGroupMap(Map<String, GroupInfo> aggregateGroupMap) {
		this.aggregateGroupMap = aggregateGroupMap;
	}
	@Override
	public Districts createDto(DTOTYPE dtoType) {
		Districts districts = new Districts();
		for ( String key: getAggregateGroupMap().keySet() ) {
			GroupInfo groupInfo = aggregateGroupMap.get(key);
			districts.getAggregateGroupMap().put(key, groupInfo.createDto(dtoType));
		}
		for ( String key: getComputationGroupMap().keySet() ) {
			GroupInfo groupInfo = computationGroupMap.get(key);
			districts.getComputationGroupMap().put(key, groupInfo.createDto(dtoType));
		}
		switch ( dtoType ) {
		case FULL:	// intentionally left blank
			for(District d: getDistrictList()) {
				districts.getDistrictList().add(d.createDto(dtoType));
			}
			break;
		case SUMMARY:	// intentionally left blank
		}
		return districts;
	}

}
