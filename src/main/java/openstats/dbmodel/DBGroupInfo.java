package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

import openstats.osmodel.OSGroupInfo;
import openstats.osmodel.OSInfoItem;

@SuppressWarnings("serial")
@Entity public class DBGroupInfo implements Serializable {
	@Id @GeneratedValue private Long id;

	@OneToMany(cascade=CascadeType.ALL)
	@OrderColumn
	private List<DBInfoItem> groupItems = new ArrayList<DBInfoItem>();
	
	public DBGroupInfo() {}
	public DBGroupInfo(OSGroupInfo osGroupInfo) {
		for ( OSInfoItem infoItem: osGroupInfo.getInfoItems()) {
			groupItems.add(new DBInfoItem(infoItem));
		}
	}
	public List<DBInfoItem> getGroupItems() {
		return groupItems;
	}
	public void setGroupItems(List<DBInfoItem> groupItems) {
		this.groupItems = groupItems;
	}
	
}