package openstats.osmodel;

import java.util.*;

import openstats.dbmodel.DBGroupInfo;

public class OSGroupInfo {
	private List<String> groupLabels;
	private List<String> groupDescriptions;
	
	public OSGroupInfo(	List<String> groupLabels, List<String> groupDescriptions ) {
		this.groupLabels = groupLabels;
		this.groupDescriptions = groupDescriptions;
	}
	
	public OSGroupInfo(	DBGroupInfo dbGroupInfo ) {
		this.groupLabels = new ArrayList<String>(dbGroupInfo.getGroupLabels());
		this.groupDescriptions = new ArrayList<String>(dbGroupInfo.getGroupDescriptions());
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