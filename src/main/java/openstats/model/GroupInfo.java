package openstats.model;

import java.util.*;

import openstats.dbmodel.DBGroupInfo;
import openstats.dbmodel.DBInfoItem;

public class GroupInfo {
	private List<InfoItem> infoItems;
	
	public GroupInfo() {}

	public GroupInfo( DBGroupInfo dbGroupInfo ) {
		this.infoItems = new ArrayList<InfoItem>();
		for ( DBInfoItem infoItem: dbGroupInfo.getGroupItems() ) {
			infoItems.add(new InfoItem(infoItem));
		}
	}

	public GroupInfo(List<InfoItem> infoItems) {
		this.infoItems = infoItems;
	}

	public List<InfoItem> getInfoItems() {
		return infoItems;
	}

	public void setGroupItems(List<InfoItem> infoItems) {
		this.infoItems = infoItems;
	}

}