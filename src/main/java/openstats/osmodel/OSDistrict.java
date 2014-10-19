package openstats.osmodel;

import java.util.List;

import openstats.model.*;

public class OSDistrict {
	
	private String chamber;
	private String district;
	private List<Long> aggregateValues;
	private List<Double> computationValues;

	public OSDistrict(String chamber, String district) {
		this.chamber = chamber;
		this.district = district;
	}
	public OSDistrict(DBDistrict district, OSGroup osGroup) {
		this.chamber = district.getChamber();
		this.district = district.getDistrict();
		this.aggregateValues = district.getAggregateMap().get(osGroup).getValueList();
		this.computationValues = district.getComputationMap().get(osGroup).getValueList();
	}
	public String getChamber() {
		return chamber;
	}
	public void setChamber(String chamber) {
		this.chamber = chamber;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public List<Long> getAggregateValues() {
		return aggregateValues;
	}
	public void setAggregateValues(List<Long> aggregateValues) {
		this.aggregateValues = aggregateValues;
	}
	public List<Double> getComputationValues() {
		return computationValues;
	}
	public void setComputationValues(List<Double> computationValues) {
		this.computationValues = computationValues;
	}

}
