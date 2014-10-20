package openstats.model;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity public class AggregateValues implements Serializable {
	@Id @GeneratedValue private Long id;
	
	@ElementCollection
	@OrderColumn
	private List<Long> valueList;
	
	public AggregateValues() {
		valueList = new ArrayList<Long>();
	}
	public AggregateValues(List<Long> valueList) {
		this.valueList = valueList;
	}

	public List<Long> getValueList() {
		return valueList;
	}

	public void setValueList(List<Long> valueList) {
		this.valueList = valueList;
	}

}
