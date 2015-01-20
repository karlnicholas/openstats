package openstats.model;

import java.util.*;

import javax.xml.bind.annotation.XmlRootElement;

import openstats.dbmodel.*;

@XmlRootElement
public class Legislator implements Comparable<Legislator> {

	private String name;
	private String party;
	private String term;
	private Date startDate;
	private Date endDate;
	private List<Result> results;
	
	public Legislator() {
		results = new ArrayList<Result>();
	}
	public Legislator(String name, String party, String term) {
		this.name = name;
		this.party = party;
		this.term = term;
		results = new ArrayList<Result>();
	}

	// empty, for templates
	public Legislator(Legislator legislator) {
		name = legislator.getName();
		party = legislator.getParty();
		term = legislator.getTerm();
		results = new ArrayList<Result>();
	}

	
	public Legislator(DBLegislator dbLegislator) {
		name = dbLegislator.getName();
		party = dbLegislator.getParty();
		term = dbLegislator.getTerm();
		results = new ArrayList<Result>();
	}

	public void addResults(List<Result> results) {
		this.results.addAll(results);
	}

	public void copyGroup(DBGroup dbGroup, DBLegislator dbLegislator) {

		for ( Result Result: dbLegislator.getGroupResultsMap().get(dbGroup).getResults()) {
			results.add(Result);
		}

	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParty() {
		return party;
	}
	public void setParty(String party) {
		this.party = party;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public List<Result> getResults() {
		return results;
	}
	public void setResults(List<Result> results) {
		this.results = results;
	}
	@Override
	public int compareTo(Legislator legislator) {
		int s = name.compareTo(legislator.name);
		if ( s != 0 ) return s;
		return this.party.compareTo(legislator.party);
	}
}
