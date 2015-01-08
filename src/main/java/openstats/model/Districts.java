package openstats.model;

import java.util.*;

import openstats.dbmodel.*;
import openstats.model.District.CHAMBER;

public class Districts {

	private List<District> districtList;
	private List<InfoItem> infoItems;
	
	public Districts() {
		infoItems = new ArrayList<InfoItem>();
		districtList = new ArrayList<District>();
	}

	// empty, for templates
	public Districts(DBDistricts dbDistricts) {
		infoItems = new ArrayList<InfoItem>();
		districtList = new ArrayList<District>();
		for ( DBDistrict dbDistrict: dbDistricts.getDistrictList()) {
			districtList.add(new District(dbDistrict));
		}
	}
	public Districts(Districts districts) {
		infoItems = new ArrayList<InfoItem>();
		for( InfoItem infoItem: districts.getInfoItems() ) {
			infoItems.add(new InfoItem(infoItem));
		}
		districtList = new ArrayList<District>();
		for ( District district: districts.getDistrictList()) {
			districtList.add(new District(district));
		}
	}
	public void copyGroup(DBGroup dbGroup, DBDistricts dbDistricts) {
		for( DBInfoItem dbInfoItem: dbDistricts.getGroupInfoMap().get(dbGroup).getGroupItems() ) {
			infoItems.add(new InfoItem(dbInfoItem));
		}
		for ( DBDistrict dbDistrict: dbDistricts.getDistrictList()) {
			District district = findDistrict(dbDistrict.getChamber(), dbDistrict.getDistrict());
			district.copyGroup(dbGroup, dbDistrict);
		}
	}

	public List<InfoItem> getInfoItems() {
		return infoItems;
	}
	public void setInfoItems(List<InfoItem> InfoItems) {
		this.infoItems = InfoItems;
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
