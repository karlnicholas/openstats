package openstats.osmodel;

import java.util.*;

public class OSGroupInfo {
	private List<String> groupLabels = new ArrayList<String>();
	private List<String> groupDescriptions = new ArrayList<String>();
	
	public OSGroupInfo(	List<String> groupLabels, List<String> groupDescriptions ) {
		this.groupLabels = groupLabels;
		this.groupDescriptions = groupDescriptions;
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