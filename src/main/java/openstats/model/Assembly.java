package openstats.model;

import java.util.*;

import javax.xml.bind.annotation.XmlRootElement;

import openstats.dbmodel.*;

@XmlRootElement
public class Assembly implements Comparable<Assembly> {

	private String state;
	private String session;
	private Group group;
	private Districts districts;
	private List<InfoItem> infoItems;
	private List<Result> results;
	
	public Assembly() {
		infoItems = new ArrayList<InfoItem>();
		results = new ArrayList<Result>();
	}
	public Assembly(String state, String session) {
		this.state = state;
		this.session = session;
		this.districts = new Districts();
		infoItems = new ArrayList<InfoItem>();
		results = new ArrayList<Result>();
	}

	// empty, for templates
	public Assembly(Assembly assembly) {
		state = assembly.getState();
		session = assembly.getSession();
		districts = new Districts(assembly.getDistricts());
		infoItems = new ArrayList<InfoItem>();
		results = new ArrayList<Result>();
	}

	
	public Assembly(DBAssembly dbAssembly) {
		state = dbAssembly.getState();
		session = dbAssembly.getSession();
		districts = new Districts(dbAssembly.getDistricts());
		infoItems = new ArrayList<InfoItem>();
		results = new ArrayList<Result>();
	}

	public void addInfoItems(List<InfoItem> infoItems) {
		this.infoItems.addAll(infoItems);
	}

	public void addResults(List<Result> results) {
		this.results.addAll(results);
	}

	public void copyGroup(DBGroup dbGroup, DBAssembly dbAssembly) {

		group = new Group(dbGroup.getGroupName(), dbGroup.getGroupDescription());
		districts.copyGroup(dbGroup, dbAssembly.getDistricts());

		for( DBInfoItem dbInfoItem: dbAssembly.getGroupInfoMap().get(dbGroup).getGroupItems() ) {
			infoItems.add(new InfoItem(dbInfoItem));
		}
		for ( Result Result: dbAssembly.getGroupResultsMap().get(dbGroup).getResults()) {
			results.add(Result);
		}

	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public Districts getDistricts() {
		return districts;
	}
	public void setDistricts(Districts districts) {
		this.districts = districts;
	}
	public List<InfoItem> getInfoItems() {
		return infoItems;
	}
	public void setInfoItems(List<InfoItem> InfoItems) {
		this.infoItems = InfoItems;
	}
	public List<Result> getResults() {
		return results;
	}
	public void setResults(List<Result> results) {
		this.results = results;
	}
	@Override
	public int compareTo(Assembly assembly) {
		int s = state.compareTo(assembly.state);
		if ( s != 0 ) return s;
		return this.session.compareTo(assembly.session);
	}
}
