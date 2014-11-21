package openstats.osmodel;

import java.util.*;

import openstats.dbmodel.DBGroupInfo;
import openstats.dbmodel.DBInfoItem;

public class OSGroupInfo {
	private List<OSInfoItem> groupItems;
	
	public OSGroupInfo() {}

	public OSGroupInfo(	DBGroupInfo dbGroupInfo ) {
		this.groupItems = new ArrayList<OSInfoItem>();
		for ( DBInfoItem infoItem: dbGroupInfo.getGroupItems() ) {
			groupItems.add(new OSInfoItem(infoItem));
		}
	}

	public OSGroupInfo(List<OSInfoItem> groupItems) {
		this.groupItems = groupItems;
	}

	public List<OSInfoItem> getInfoItems() {
		return groupItems;
	}

	public void setGroupItems(List<OSInfoItem> groupItems) {
		this.groupItems = groupItems;
	}

}