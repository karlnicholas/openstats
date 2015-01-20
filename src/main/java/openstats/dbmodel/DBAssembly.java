package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import openstats.model.*;
import openstats.model.District.CHAMBER;

@SuppressWarnings("serial")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = DBAssembly.assemblyGroup, query = "select a from DBAssembly a join fetch a.groupInfoMap agim where a.state = ?1 and a.session = ?2 and key(agim) in (?3)" ), 
	@NamedQuery(name = DBAssembly.assemblyResults, query = "select a from DBAssembly a join fetch a.groupInfoMap agim join fetch a.groupResultsMap agrm join fetch a.districtList d where a.state = ?1 and a.session = ?2 and key(agim) in (?3) and key(agrm) in (?4) " )
})
@Entity public class DBAssembly implements Comparable<DBAssembly>, Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;

	public static final String assemblyGroup = "DBAssembly.getAssemblyGroup";
	public static final String assemblyResults = "DBAssembly.assemblyResults";
	private String state;
	private String session;
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<DBDistrict> districtList;
		
	@OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	@JoinTable(name="DBAssembly_groupInfoMap",
	    joinColumns=@JoinColumn(name="DBAssembly"),
	    inverseJoinColumns=@JoinColumn(name="DBGroupInfo"))
	@MapKeyJoinColumn(name="DBGroup")
	private Map<DBGroup, DBGroupInfo> groupInfoMap = new LinkedHashMap<DBGroup, DBGroupInfo>();
	
	@OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	@JoinTable(name="DBAssembly_groupResultsMap",
	    joinColumns=@JoinColumn(name="DBAssembly"),
	    inverseJoinColumns=@JoinColumn(name="DBGroupResults"))
	@MapKeyJoinColumn(name="DBGroup")
	private Map<DBGroup, DBGroupResults> groupResultsMap = new LinkedHashMap<DBGroup, DBGroupResults>();
		
	public DBAssembly() {
		districtList = new ArrayList<DBDistrict>();
		groupInfoMap = new LinkedHashMap<DBGroup, DBGroupInfo>();
		groupResultsMap = new LinkedHashMap<DBGroup, DBGroupResults>();
	}
	
	// empty, for templates
	public DBAssembly(Assembly assembly) {
		state = assembly.getState();
		session = assembly.getSession();
		districtList = new ArrayList<DBDistrict>();
		for ( District district: assembly.getDistrictList() ) {			
			districtList.add(new DBDistrict(district));
		}
		groupInfoMap = new LinkedHashMap<DBGroup, DBGroupInfo>();
		groupResultsMap = new LinkedHashMap<DBGroup, DBGroupResults>();
	}
	
	public void copyGroup(DBGroup dbGroup, Assembly assembly) {
		groupInfoMap.put(dbGroup, new DBGroupInfo(assembly.getInfoItems()));
		groupResultsMap.put(dbGroup, new DBGroupResults(assembly.getResults()) );

		for ( District district: assembly.getDistrictList() ) {
			findDistrict(district.getChamber(), district.getDistrict())
				.copyGroup(dbGroup, district);
		}
	}
	
	public void removeGroup(DBGroup dbGroup) {
		groupInfoMap.remove(dbGroup);
		groupResultsMap.remove(dbGroup);		

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
	public Map<DBGroup, DBGroupInfo> getGroupInfoMap() {
		return groupInfoMap;
	}
	public void setGroupInfoMap(Map<DBGroup, DBGroupInfo> groupInfoMap) {
		this.groupInfoMap = groupInfoMap;
	}
	public Map<DBGroup, DBGroupResults> getGroupResultsMap() {
		return groupResultsMap;
	}
	public void setGroupResultsMap(Map<DBGroup, DBGroupResults> groupResultsMap) {
		this.groupResultsMap = groupResultsMap;
	}
	public DBDistrict findDistrict(CHAMBER chamber, String district) {
		for ( DBDistrict d: districtList ) {
			if ( d.getChamber() == chamber && d.getDistrict().equals(district)) return d; 
		}
		return null;
	}
	public List<DBDistrict> getDistrictList() {
		return districtList;
	}
	public void setDistrictList(List<DBDistrict> districtList) {
		this.districtList = districtList;
	}
	@Override
	public int compareTo(DBAssembly dbAssembly) {
		int s = state.compareTo(dbAssembly.state);
		if ( s != 0 ) return s;
		return this.session.compareTo(dbAssembly.session);
	}

}
