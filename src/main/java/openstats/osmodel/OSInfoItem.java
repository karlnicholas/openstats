package openstats.osmodel;

import openstats.dbmodel.DBInfoItem;

public class OSInfoItem {
	private String label;
	private String description;
	public OSInfoItem() {}
	public OSInfoItem(DBInfoItem dbInfoItem) {
		this.label = dbInfoItem.getLabel();
		this.description = dbInfoItem.getDescription();
	}
	public OSInfoItem(String label, String description) {
		this.label = label;
		this.description = description;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
