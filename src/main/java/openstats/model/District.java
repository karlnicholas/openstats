package openstats.model;

import java.util.*;

import openstats.dbmodel.*;

public class District {
	public static enum CHAMBER {UPPER, LOWER};
	
	private String district;
	private CHAMBER chamber;
	private String name;
	private String description;
	private List<Long> aggregateValues;
	private List<Double> computationValues;

	public District() {
		aggregateValues = null;
		computationValues = null;
	}
	public District(String district, CHAMBER chamber) {
		this.district = district;
		this.chamber = chamber;
		name = null;
		description = null;
		aggregateValues = null;
		computationValues = null;
	}
	public District(DBDistrict dbDistrict) {
		this.district = dbDistrict.getDistrict();
		this.chamber = dbDistrict.getChamber();
		this.name = dbDistrict.getName();
		this.description = dbDistrict.getDescription();
	}
	public void copyGroup(DBGroup dbGroup, DBDistrict dbDistrict) {
		if ( dbDistrict.getAggregateMap().containsKey(dbGroup)) {
			this.aggregateValues = new ArrayList<Long>(dbDistrict.getAggregateMap().get(dbGroup).getValueList());
		} else {
			aggregateValues = null;
		}
		if ( dbDistrict.getComputationMap().containsKey(dbGroup)) {
			this.computationValues = new ArrayList<Double>(dbDistrict.getComputationMap().get(dbGroup).getValueList());
		} else {
			computationValues = null;
		}
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public CHAMBER getChamber() {
		return chamber;
	}
	public void setChamber(CHAMBER chamber) {
		this.chamber = chamber;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
