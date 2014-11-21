package openstats.model;

import java.util.*;

import openstats.dbmodel.*;

public class District {
	
	private String chamber;
	private String district;
	private List<Long> aggregateValues;
	private List<Double> computationValues;

	public District() {
		aggregateValues = null;
		computationValues = null;
	}
	public District(String chamber, String district) {
		this.chamber = chamber;
		this.district = district;
		aggregateValues = null;
		computationValues = null;
	}
	public District(DBGroup dbGroup, DBDistrict district) {
		this.chamber = district.getChamber();
		this.district = district.getDistrict();
		if ( district.getAggregateMap().containsKey(dbGroup)) {
			this.aggregateValues = new ArrayList<Long>(district.getAggregateMap().get(dbGroup).getValueList());
		} else {
			aggregateValues = null;
		}
		if ( district.getComputationMap().containsKey(dbGroup)) {
			this.computationValues = new ArrayList<Double>(district.getComputationMap().get(dbGroup).getValueList());
		} else {
			computationValues = null;
		}
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
