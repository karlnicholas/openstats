package openstats.model;

import java.util.*;

import openstats.dbmodel.*;
import openstats.model.District.CHAMBER;

public class Districts {

	private List<District> districtList;
	private List<InfoItem> aggregateInfoItems;
	private List<InfoItem> computeInfoItems;
	
	public Districts() {
		aggregateInfoItems = new ArrayList<InfoItem>();
		computeInfoItems = new ArrayList<InfoItem>();
		districtList = new ArrayList<District>();
	}

	// empty, for templates
	public Districts(DBDistricts dbDistricts) {
		aggregateInfoItems = new ArrayList<InfoItem>();
		computeInfoItems = new ArrayList<InfoItem>();
		districtList = new ArrayList<District>();
		for ( DBDistrict dbDistrict: dbDistricts.getDistrictList()) {
			districtList.add(new District(dbDistrict));
		}
	}
	public Districts(Districts districts) {
		aggregateInfoItems = new ArrayList<InfoItem>();
		for( InfoItem infoItem: districts.getAggregateInfoItems() ) {
			aggregateInfoItems.add(new InfoItem(infoItem));
		}
		computeInfoItems = new ArrayList<InfoItem>();
		for( InfoItem infoItem: districts.getComputeInfoItems() ) {
			computeInfoItems.add(new InfoItem(infoItem));
		}
		districtList = new ArrayList<District>();
		for ( District district: districts.getDistrictList()) {
			districtList.add(new District(district));
		}
	}
	public void copyGroup(DBGroup dbGroup, DBDistricts dbDistricts) {
		for( DBInfoItem dbInfoItem: dbDistricts.getGroupInfoMap().get(dbGroup).getAggregateGroupItems() ) {
			aggregateInfoItems.add(new InfoItem(dbInfoItem));
		}
		for( DBInfoItem dbInfoItem: dbDistricts.getGroupInfoMap().get(dbGroup).getComputeGroupItems() ) {
			computeInfoItems.add(new InfoItem(dbInfoItem));
		}
		for ( DBDistrict dbDistrict: dbDistricts.getDistrictList()) {
			District district = findDistrict(dbDistrict.getChamber(), dbDistrict.getDistrict());
			district.copyGroup(dbGroup, dbDistrict);
		}
	}

	public List<InfoItem> getAggregateInfoItems() {
		return aggregateInfoItems;
	}
	public void setAggregateInfoItems(List<InfoItem> aggregateInfoItems) {
		this.aggregateInfoItems = aggregateInfoItems;
	}
	public List<InfoItem> getComputeInfoItems() {
		return computeInfoItems;
	}
	public void setComputeInfoItems(List<InfoItem> computeInfoItems) {
		this.computeInfoItems = computeInfoItems;
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
