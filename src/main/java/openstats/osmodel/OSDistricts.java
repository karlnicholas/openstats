package openstats.osmodel;

import java.util.*;

import openstats.model.*;

public class OSDistricts {

	private OSGroupInfo aggregateGroupInfo; 
	private OSGroupInfo computationGroupInfo; 
	private List<OSDistrict> osDistrictList = new ArrayList<OSDistrict>();
	
	public OSDistricts() {
		aggregateGroupInfo = null; 
		computationGroupInfo = null; 
	}
	public OSDistricts(DBGroup dbGroup, DBDistricts dbDistricts) {
		if ( dbDistricts.getAggregateGroupMap().containsKey(dbGroup) ) {
			aggregateGroupInfo = new OSGroupInfo( dbDistricts.getAggregateGroupMap().get(dbGroup) );
		} else {
			aggregateGroupInfo = null; 
		}
		if ( dbDistricts.getComputationGroupMap().containsKey(dbGroup) ) {
			computationGroupInfo = new OSGroupInfo( dbDistricts.getComputationGroupMap().get(dbGroup) );
		} else {
			computationGroupInfo = null; 
		}
		for ( DBDistrict dbDistrict: dbDistricts.getDistrictList()) {
			osDistrictList.add(new OSDistrict(dbGroup, dbDistrict));
		}
	}

	public OSGroupInfo getAggregateGroupInfo() {
		return aggregateGroupInfo;
	}
	public void setAggregateGroupInfo(OSGroupInfo aggregateGroupInfo) {
		this.aggregateGroupInfo = aggregateGroupInfo;
	}
	public OSGroupInfo getComputationGroupInfo() {
		return computationGroupInfo;
	}
	public void setComputationGroupInfo(OSGroupInfo computationGroupInfo) {
		this.computationGroupInfo = computationGroupInfo;
	}
	public List<OSDistrict> getOSDistrictList() {
		return osDistrictList;
	}
	public void setOSDistrictList(List<OSDistrict> osDistrictList) {
		this.osDistrictList = osDistrictList;
	}
	public OSDistrict findOSDistrict(String chamber, String district) {
		for ( OSDistrict d: osDistrictList ) {
			if ( d.getChamber().equals(chamber) && d.getDistrict().equals(district)) return d; 
		}
		return null;
	}
	
}
