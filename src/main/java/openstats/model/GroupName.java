package openstats.model;

import java.io.Serializable;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity public class GroupName implements Comparable<GroupName>, Serializable {
	@Id @GeneratedValue private Long id;
	
	@Column(unique=true)
	private String groupName;
	
	public GroupName() {}
	public GroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public int compareTo(GroupName o) {
		return groupName.compareTo(o.groupName);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroupName other = (GroupName) obj;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		return true;
	}


}
