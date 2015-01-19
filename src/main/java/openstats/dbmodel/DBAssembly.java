package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import openstats.model.*;

@SuppressWarnings("serial")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = DBAssembly.getAssemblyGroup, query = "select a from DBAssembly a join fetch a.groupInfoMap agim join fetch a.groupResultsMap agrm join fetch a.districts d where a.state = ?1 and a.session = ?2 and key(agim) in (?3) and key(agrm) in (?4)" )
		
})
@Entity public class DBAssembly implements Comparable<DBAssembly>, Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;

	public static final String getAssemblyGroup = "DBAssembly.getAssemblyGroup";
	private String state;
	private String session;
	
	@OneToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	private DBDistricts districts = new DBDistricts();
	
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
		districts = new DBDistricts();
		groupInfoMap = new LinkedHashMap<DBGroup, DBGroupInfo>();
		groupResultsMap = new LinkedHashMap<DBGroup, DBGroupResults>();
	}
	
	// empty, for templates
	public DBAssembly(Assembly assembly) {
		this.state = assembly.getState();
		this.session = assembly.getSession();
		this.districts = new DBDistricts(assembly.getDistricts());
		groupInfoMap = new LinkedHashMap<DBGroup, DBGroupInfo>();
		groupResultsMap = new LinkedHashMap<DBGroup, DBGroupResults>();
	}
	
	public void copyGroup(DBGroup dbGroup, Assembly assembly) {
		groupInfoMap.put(dbGroup, new DBGroupInfo(assembly.getInfoItems()));
		groupResultsMap.put(dbGroup, new DBGroupResults(assembly.getResults()) );

		districts.copyGroup(dbGroup, assembly.getDistricts());
	}
	
	public void removeGroup(DBGroup dbGroup) {
		groupInfoMap.remove(dbGroup);
		groupResultsMap.remove(dbGroup);		

		districts.removeGroup(dbGroup);
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
	public DBDistricts getDistricts() {
		return districts;
	}
	public void setDistricts(DBDistricts districts) {
		this.districts = districts;
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
	@Override
	public int compareTo(DBAssembly dbAssembly) {
		int s = state.compareTo(dbAssembly.state);
		if ( s != 0 ) return s;
		return this.session.compareTo(dbAssembly.session);
	}
}
