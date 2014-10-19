package openstats.osmodel;

import java.io.Serializable;
import java.util.*;

public class OSGroupInfo implements Serializable {
	private List<String> groupLabels = new ArrayList<String>();
	private List<String> groupDescriptions = new ArrayList<String>();
	
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