package openstats.model;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity public class GroupInfo implements DtoInterface<GroupInfo>, Serializable {
	@Id @GeneratedValue private Long id;

	@ElementCollection
	@OrderColumn
	private List<String> groupLabels;
	@ElementCollection
	@OrderColumn
	private List<String> groupDescriptions;
	
	public GroupInfo() {
		groupLabels = new ArrayList<String>();
		groupDescriptions = new ArrayList<String>();
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

	@Override
	public GroupInfo createDto(DTOTYPE dtoType) {
		GroupInfo groupInfo = new GroupInfo();
		for ( String label: getGroupLabels()) {
			groupInfo.getGroupLabels().add(label);
		}
		for ( String label: getGroupDescriptions()) {
			groupInfo.getGroupDescriptions().add(label);
		}
		switch ( dtoType ) {
		case SUMMARY:	// Intentionally left blank
		case FULL:	// Intentionally left blank
		}
		return groupInfo;
	}
	
}