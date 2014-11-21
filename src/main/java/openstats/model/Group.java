package openstats.model;

public class Group implements Comparable<Group> {
	private String groupName;
	private String groupDescription;
	
	public Group() {}

	public Group(String groupName, String groupDescription) {
		this.groupName = groupName;
		this.groupDescription = groupDescription;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDescription() {
		return groupDescription;
	}
	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}
	@Override
	public int compareTo(Group o) {
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
		Group other = (Group) obj;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		return true;
	}


}
