package openstats.model;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity public class ComputationValues implements Serializable {
	@Id @GeneratedValue private Long id;
	
	@ElementCollection
	@OrderColumn
	private List<Double> valueList;
	
	public ComputationValues() {
		valueList = new ArrayList<Double>();
	}
	public ComputationValues(List<Double> valueList) {
		this.valueList = valueList;
	}

	public List<Double> getValueList() {
		return valueList;
	}

	public void setValueList(List<Double> valueList) {
		this.valueList = valueList;
	}

}
