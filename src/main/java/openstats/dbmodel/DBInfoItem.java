package openstats.dbmodel;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import openstats.model.InfoItem;

@SuppressWarnings("serial")
@Entity public class DBInfoItem implements Serializable {
	@Id @GeneratedValue private Long id;
	
	private String Label;
	@Column(length=1023)
	private String description;
	public DBInfoItem() {}
	public DBInfoItem(InfoItem osInfoItem) {
		this.Label = osInfoItem.getLabel();
		this.description = osInfoItem.getDescription();
	}
	public String getLabel() {
		return Label;
	}
	public void setLabel(String label) {
		Label = label;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
