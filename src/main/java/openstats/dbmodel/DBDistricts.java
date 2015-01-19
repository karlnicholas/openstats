package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

import openstats.model.*;
import openstats.model.District.CHAMBER;

@SuppressWarnings("serial")
@NamedQueries({ 
	@NamedQuery(name = DBDistricts.districtsGroupMapQuery, query = "select d from DBDistricts d join fetch d.groupInfoMap dgim where d = ?1 and key(dgim) in (?2)" ), 
	@NamedQuery(name = DBDistricts.districtsListQuery, query = "select d from DBDistricts d join fetch d.districtList where d = ?1" ), 
	@NamedQuery(name = DBDistricts.districtsResultsQuery, query = "select d from DBDistricts d join fetch d.districtList dList join fetch dList.groupResultsMap dListgrm where d = ?1 and key(dListgrm) in (?2)" )
})
@Entity
public class DBDistricts implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;
	public static final String districtsGroupMapQuery = "DBDistricts.districstGroupMapQuery";
	public static final String districtsListQuery = "DBDistricts.districtsListQuery";
	public static final String districtsResultsQuery = "DBDistricts.districtsResultsQuery";

	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<DBDistrict> districtList = new ArrayList<DBDistrict>();
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinTable(name="DBDistricts_groupInfoMap",
	    joinColumns=@JoinColumn(name="DBDistricts"),
	    inverseJoinColumns=@JoinColumn(name="DBGroupInfo"))
	@MapKeyJoinColumn(name="DBGroup")
	private Map<DBGroup, DBGroupInfo> groupInfoMap = new LinkedHashMap<DBGroup, DBGroupInfo>();
	
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
		groupInfoMap.put(dbGroup, new DBGroupInfo(districts.getInfoItems()));
	}
	
	public void removeGroup(DBGroup dbGroup) {
		for ( DBDistrict dbDistrict: districtList ) {			
			dbDistrict.removeGroup(dbGroup);
		}
		if ( groupInfoMap.containsKey(dbGroup) ) {
			groupInfoMap.remove(dbGroup);
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
	public Map<DBGroup, DBGroupInfo> getGroupInfoMap() {
		return groupInfoMap;
	}
	public void setGroupInfoMap(Map<DBGroup, DBGroupInfo> groupInfoMap) {
		this.groupInfoMap = groupInfoMap;
	}
}
