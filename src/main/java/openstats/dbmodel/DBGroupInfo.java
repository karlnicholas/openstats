package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

import openstats.model.GroupInfo;
import openstats.model.InfoItem;

@SuppressWarnings("serial")
@Entity public class DBGroupInfo implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@OrderColumn
	private List<DBInfoItem> groupItems = new ArrayList<DBInfoItem>();
	
	public DBGroupInfo() {}
	public DBGroupInfo(GroupInfo osGroupInfo) {
		for ( InfoItem infoItem: osGroupInfo.getInfoItems()) {
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