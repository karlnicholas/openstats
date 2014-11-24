package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

import openstats.model.*;
import openstats.model.District.CHAMBER;

@SuppressWarnings("serial")
@Entity
public class DBDistricts implements Serializable {
	@Id @GeneratedValue private Long id;

	@OneToMany(cascade = CascadeType.ALL)
	private List<DBDistrict> districtList = new ArrayList<DBDistrict>();
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name="DBDistricts_aggregateGroupMap")
	private Map<DBGroup, DBGroupInfo> aggregateGroupMap = new LinkedHashMap<DBGroup, DBGroupInfo>();
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name="DBDistricts_computationGroupMap")
	private Map<DBGroup, DBGroupInfo> computationGroupMap = new LinkedHashMap<DBGroup, DBGroupInfo>();
	
	public DBDistricts() {}

	public DBDistricts(Districts districts) {
		for ( District district: districts.getDistrictList() ) {			
			districtList.add(new DBDistrict(district));
		}
	}
	public void copyGroup(DBGroup dbGroup, Districts districts) {
		for ( District district: districts.getDistrictList() ) {
			findDistrict(district.getChamber(), district.getDistrict())
			.copyGroup(dbGroup, district);
		}
		if ( districts.getAggregateGroupInfo() != null ) {
			aggregateGroupMap.put(dbGroup, new DBGroupInfo(districts.getAggregateGroupInfo()));
		}
		if ( districts.getComputationGroupInfo() != null ) {
			computationGroupMap.put(dbGroup, new DBGroupInfo(districts.getComputationGroupInfo()));
		}
	}
	
	public void removeGroup(DBGroup dbGroup) {
		for ( DBDistrict dbDistrict: districtList ) {			
			dbDistrict.removeGroup(dbGroup);
		}
		if ( aggregateGroupMap.containsKey(dbGroup) ) {
			aggregateGroupMap.remove(dbGroup);
		}
		if ( computationGroupMap.containsKey(dbGroup) ) {
			computationGroupMap.remove(dbGroup);
		}
	}

	public DBDistrict findDistrict(CHAMBER chamber, String district) {
		for ( DBDistrict d: districtList ) {
			if ( d.getChamber() == chamber && d.getDistrict().equals(district)) return d; 
		}
		return null;
	}
	public List<DBDistrict> getDistrictList() {
		return districtList;
	}
	public void setDistrictList(List<DBDistrict> districtList) {
		this.districtList = districtList;
	}
	public Map<DBGroup, DBGroupInfo> getComputationGroupMap() {
		return computationGroupMap;
	}
	public void setComputationGroupMap(Map<DBGroup, DBGroupInfo> computationGroupMap) {
		this.computationGroupMap = computationGroupMap;
	}
	public Map<DBGroup, DBGroupInfo> getAggregateGroupMap() {
		return aggregateGroupMap;
	}
	public void setAggregateGroupMap(Map<DBGroup, DBGroupInfo> aggregateGroupMap) {
		this.aggregateGroupMap = aggregateGroupMap;
	}

}
