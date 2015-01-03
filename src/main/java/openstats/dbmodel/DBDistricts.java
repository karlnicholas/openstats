package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

import openstats.model.*;
import openstats.model.District.CHAMBER;

@SuppressWarnings("serial")
@NamedQueries({ 
	@NamedQuery(name = DBDistricts.districtsAggregateGroupMapQuery, query = "select new openstats.data.AssemblyRepository$GroupMapEntry(key(m), value(m)) from DBDistricts d join d.aggregateGroupMap m where d = ?1 and key(m) in( ?2 )"),  
	@NamedQuery(name = DBDistricts.districtsComputationGroupMapQuery, query = "select new openstats.data.AssemblyRepository$GroupMapEntry(key(m), value(m)) from DBDistricts d join d.computationGroupMap m where d = ?1 and key(m) in( ?2 )")  
})
@Entity
public class DBDistricts implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;
	
	public static final String districtsAggregateGroupMapQuery = "DBDistricts.districtsAggregateGroupMapQuery";  
	public static final String districtsComputationGroupMapQuery = "DBDistricts.districtsComputationGroupMapQuery";  

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<DBDistrict> districtList = new ArrayList<DBDistrict>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinTable(name="DBDistricts_aggregateGroupMap",
	    joinColumns=@JoinColumn(name="DBDistricts"),
	    inverseJoinColumns=@JoinColumn(name="DBGroupInfo"))
	@MapKeyJoinColumn(name="DBGroup")
	private Map<DBGroup, DBGroupInfo> aggregateGroupMap = new LinkedHashMap<DBGroup, DBGroupInfo>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinTable(name="DBDistricts_computationGroupMap",
	    joinColumns=@JoinColumn(name="DBDistricts"),
	    inverseJoinColumns=@JoinColumn(name="DBGroupInfo"))
	@MapKeyJoinColumn(name="DBGroup")
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
	public Long getId() { return id; }
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
