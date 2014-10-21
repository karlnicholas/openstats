package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

import openstats.osmodel.OSGroupInfo;

@SuppressWarnings("serial")
@Entity public class DBGroupInfo implements Serializable {
	@Id @GeneratedValue private Long id;

	@ElementCollection
	@OrderColumn
	private List<String> groupLabels = new ArrayList<String>();
	@ElementCollection
	@OrderColumn
	private List<String> groupDescriptions = new ArrayList<String>();
	
	public DBGroupInfo() {}
	public DBGroupInfo(OSGroupInfo osGroupInfo) {
		groupLabels.addAll(osGroupInfo.getGroupLabels());
		groupDescriptions.addAll(osGroupInfo.getGroupDescriptions());
	}
	
	public List<String> getGroupLabels() {
		return groupLabels;
	}

	public void setGroupLabels(List<String> groupLabels) {
		this.groupLabels = groupLabels;
	}

	public List<String> getGroupDescriptions() {
		return groupDescriptions;
	}

	public void setGroupDescriptions(List<String> groupDescriptions) {
		this.groupDescriptions = groupDescriptions;
	}
	
}