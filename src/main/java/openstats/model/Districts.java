package openstats.model;

import java.util.*;

import openstats.dbmodel.*;

public class Districts {

	private GroupInfo aggregateGroupInfo; 
	private GroupInfo computationGroupInfo; 
	private List<District> osDistrictList = new ArrayList<District>();
	
	public Districts() {
		aggregateGroupInfo = null; 
		computationGroupInfo = null; 
	}
	public Districts(DBGroup dbGroup, DBDistricts dbDistricts) {
		if ( dbDistricts.getAggregateGroupMap().containsKey(dbGroup) ) {
			aggregateGroupInfo = new GroupInfo( dbDistricts.getAggregateGroupMap().get(dbGroup) );
		} else {
			aggregateGroupInfo = null; 
		}
		if ( dbDistricts.getComputationGroupMap().containsKey(dbGroup) ) {
			computationGroupInfo = new GroupInfo( dbDistricts.getComputationGroupMap().get(dbGroup) );
		} else {
			computationGroupInfo = null; 
		}
		for ( DBDistrict dbDistrict: dbDistricts.getDistrictList()) {
			osDistrictList.add(new District(dbGroup, dbDistrict));
		}
	}

	public GroupInfo getAggregateGroupInfo() {
		return aggregateGroupInfo;
	}
	public void setAggregateGroupInfo(GroupInfo aggregateGroupInfo) {
		this.aggregateGroupInfo = aggregateGroupInfo;
	}
	public GroupInfo getComputationGroupInfo() {
		return computationGroupInfo;
	}
	public void setComputationGroupInfo(GroupInfo computationGroupInfo) {
		this.computationGroupInfo = computationGroupInfo;
	}
	public List<District> getOSDistrictList() {
		return osDistrictList;
	}
	public void setOSDistrictList(List<District> osDistrictList) {
		this.osDistrictList = osDistrictList;
	}
	public District findOSDistrict(String chamber, String district) {
		for ( District d: osDistrictList ) {
			if ( d.getChamber().equals(chamber) && d.getDistrict().equals(district)) return d; 
		}
		return null;
	}
	
}
