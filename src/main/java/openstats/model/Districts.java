package openstats.model;

import java.util.*;

import openstats.dbmodel.*;
import openstats.model.District.CHAMBER;

public class Districts {

	private GroupInfo aggregateGroupInfo; 
	private GroupInfo computationGroupInfo; 
	private List<District> districtList = new ArrayList<District>();
	
	public Districts() {
		aggregateGroupInfo = null; 
		computationGroupInfo = null; 
	}
	public Districts(DBDistricts dbDistricts) {
		for ( DBDistrict dbDistrict: dbDistricts.getDistrictList()) {
			districtList.add(new District(dbDistrict));
		}
	}
	public void copyGroup(DBGroup dbGroup, DBDistricts dbDistricts) {
		if ( dbDistricts.getAggregateGroupMap().containsKey(dbGroup) ) {
			if ( aggregateGroupInfo == null )
				aggregateGroupInfo = new GroupInfo( dbDistricts.getAggregateGroupMap().get(dbGroup) );
			else 
				aggregateGroupInfo.mergeGroupInfo(dbDistricts.getAggregateGroupMap().get(dbGroup));
		}
		if ( dbDistricts.getComputationGroupMap().containsKey(dbGroup) ) {
			if (computationGroupInfo == null )
				computationGroupInfo = new GroupInfo( dbDistricts.getComputationGroupMap().get(dbGroup) );
			else 
				computationGroupInfo.mergeGroupInfo( dbDistricts.getComputationGroupMap().get(dbGroup) );
				
		}
		for ( DBDistrict dbDistrict: dbDistricts.getDistrictList()) {
			District district = findDistrict(dbDistrict.getChamber(), dbDistrict.getDistrict());
			district.copyGroup(dbGroup, dbDistrict);
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
	public List<District> getDistrictList() {
		return districtList;
	}
	public void setDistrictList(List<District> districtList) {
		this.districtList = districtList;
	}
	public District findDistrict(CHAMBER chamber, String district) {
		for ( District d: districtList ) {
			if ( d.getChamber()== chamber && d.getDistrict().equals(district)) return d; 
		}
		return null;
	}
	
}
