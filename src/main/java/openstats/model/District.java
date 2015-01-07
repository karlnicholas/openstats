package openstats.model;

import java.util.*;

import openstats.dbmodel.*;

public class District {
	public static enum CHAMBER {UPPER, LOWER};
	
	private String district;
	private CHAMBER chamber;
	private String description;
	private List<AggregateResult> aggregateResults;
	private List<ComputeResult> computeResults;

	public District() {}
	
	public District(String district, CHAMBER chamber) {
		this.district = district;
		this.chamber = chamber;
		description = null;
		aggregateResults = new ArrayList<AggregateResult>();
		computeResults = new ArrayList<ComputeResult>();
	}
	// empty, for templates
	public District(DBDistrict dbDistrict) {
		district = dbDistrict.getDistrict();
		chamber = dbDistrict.getChamber();
		description = dbDistrict.getDescription();
		aggregateResults = new ArrayList<AggregateResult>();
		computeResults = new ArrayList<ComputeResult>();
	}
	// deep copy constructor
	public District(District district) {
		this.district = district.getDistrict();
		this.chamber = district.getChamber();
		this.description = district.getDescription();
		this.aggregateResults = new ArrayList<AggregateResult>(district.getAggregateResults());
		this.computeResults = new ArrayList<ComputeResult>(district.getComputeResults());
	}
	public void copyGroup(DBGroup dbGroup, DBDistrict dbDistrict) {
		for ( AggregateResult aggregateResult: dbDistrict.getGroupResultsMap().get(dbGroup).getAggregateResults()) {
			aggregateResults.add(aggregateResult);
		}
		for ( ComputeResult computeResult: dbDistrict.getGroupResultsMap().get(dbGroup).getComputeResults()) {
			computeResults.add(computeResult);
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<AggregateResult> getAggregateResults() {
		return aggregateResults;
	}
	public void setAggregateResults(List<AggregateResult> aggregateResults) {
		this.aggregateResults = aggregateResults;
	}
	public List<ComputeResult> getComputeResults() {
		return computeResults;
	}
	public void setComputeResults(List<ComputeResult> computeResults) {
		this.computeResults = computeResults;
	}

}
