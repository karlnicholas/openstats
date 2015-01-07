package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

import openstats.model.InfoItem;

@SuppressWarnings("serial")
@Entity public class DBGroupInfo implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="DBGroupInfo_aggregateGroupItems")
	@OrderColumn
	private List<DBInfoItem> aggregateGroupItems = new ArrayList<DBInfoItem>();
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name="DBGroupInfo_computeGroupItems")
	@OrderColumn
	private List<DBInfoItem> computeGroupItems = new ArrayList<DBInfoItem>();

	public DBGroupInfo() {}
	public DBGroupInfo(List<InfoItem> aggregateInfoItems, List<InfoItem> computeInfoItems ) {
		for ( InfoItem infoItem: aggregateInfoItems) {
			aggregateGroupItems.add(new DBInfoItem(infoItem));
		}
		for ( InfoItem infoItem: computeInfoItems) {
			computeGroupItems.add(new DBInfoItem(infoItem));
		}
	}
	public List<DBInfoItem> getAggregateGroupItems() {
		return aggregateGroupItems;
	}
	public void setAggregateGroupItems(List<DBInfoItem> aggregateGroupItems) {
		this.aggregateGroupItems = aggregateGroupItems;
	}
	public List<DBInfoItem> getComputeGroupItems() {
		return computeGroupItems;
	}
	public void setComputeGroupItems(List<DBInfoItem> computeGroupItems) {
		this.computeGroupItems = computeGroupItems;
	}
}