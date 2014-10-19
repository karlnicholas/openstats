package openstats.osmodel;

import java.util.*;

public class OSDistricts {

	private OSGroupInfo aggregateGroupInfo = new OSGroupInfo(); 
	private OSGroupInfo computationGroupInfo = new OSGroupInfo(); 
	private List<OSDistrict> osDistrictList = new ArrayList<OSDistrict>();

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
