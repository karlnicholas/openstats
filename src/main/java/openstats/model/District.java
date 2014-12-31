package openstats.model;

import java.util.*;

import openstats.dbmodel.*;

public class District {
	public static enum CHAMBER {UPPER, LOWER};
	
	private String district;
	private CHAMBER chamber;
	private String description;
	private List<AggregateResult> aggregateResults = new ArrayList<AggregateResult>();
	private List<ComputationResult> computationResults = new ArrayList<ComputationResult>();

	public District() {
		aggregateResults = null;
		computationResults = null;
	}
	public District(String district, CHAMBER chamber) {
		this.district = district;
		this.chamber = chamber;
		description = null;
		aggregateResults = null;
		computationResults = null;
	}
	public District(DBDistrict dbDistrict) {
		this.district = dbDistrict.getDistrict();
		this.chamber = dbDistrict.getChamber();
		this.description = dbDistrict.getDescription();
	}
	public District(District district) {
		this.district = district.getDistrict();
		this.chamber = district.getChamber();
		this.description = district.getDescription();
		if ( district.getAggregateResults() != null )
			aggregateResults = new ArrayList<AggregateResult>(district.getAggregateResults());
		if ( district.getComputationResults() != null )
			computationResults = new ArrayList<ComputationResult>(district.getComputationResults());
	}
	public void copyGroup(DBGroup dbGroup, DBDistrict dbDistrict) {
		if ( dbDistrict.getAggregateMap().containsKey(dbGroup)) {
			for ( AggregateResult result: dbDistrict.getAggregateMap().get(dbGroup).getResultList() ) {
				aggregateResults.add( result );
			}
		}
		if ( dbDistrict.getComputationMap().containsKey(dbGroup)) {
			for ( ComputationResult result: dbDistrict.getComputationMap().get(dbGroup).getResultList() ) {
				computationResults.add( result );
			}
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
	public List<ComputationResult> getComputationResults() {
		return computationResults;
	}
	public void setComputationResults(List<ComputationResult> computationResults) {
		this.computationResults = computationResults;
	}

}
