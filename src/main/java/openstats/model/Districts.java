package openstats.model;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
public class Districts implements DtoInterface<Districts>, Serializable {
	@Id @GeneratedValue private Long id;

	@OneToMany(cascade = CascadeType.ALL)
	private List<District> districtList = new ArrayList<District>();
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name="Districts_aggregateGroupMap")
	private Map<GroupName, GroupInfo> aggregateGroupMap = new LinkedHashMap<GroupName, GroupInfo>(); 
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name="Districts_computationGroupMap")
	private Map<GroupName, GroupInfo> computationGroupMap = new LinkedHashMap<GroupName, GroupInfo>(); 
	
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
	public Map<GroupName, GroupInfo> getComputationGroupMap() {
		return computationGroupMap;
	}
	public void setComputationGroupMap(Map<GroupName, GroupInfo> computationGroupMap) {
		this.computationGroupMap = computationGroupMap;
	}
	public Map<GroupName, GroupInfo> getAggregateGroupMap() {
		return aggregateGroupMap;
	}
	public void setAggregateGroupMap(Map<GroupName, GroupInfo> aggregateGroupMap) {
		this.aggregateGroupMap = aggregateGroupMap;
	}
	@Override
	public Districts createDto(DTOTYPE dtoType) {
		Districts districts = new Districts();
		for ( GroupName key: getAggregateGroupMap().keySet() ) {
			GroupInfo groupInfo = aggregateGroupMap.get(key);
			districts.getAggregateGroupMap().put(key, groupInfo.createDto(dtoType));
		}
		for ( GroupName key: getComputationGroupMap().keySet() ) {
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
