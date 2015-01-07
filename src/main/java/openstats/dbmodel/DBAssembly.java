package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import openstats.model.*;

@SuppressWarnings("serial")
@XmlRootElement
@Entity public class DBAssembly implements Comparable<DBAssembly>, Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;

/*
	@NamedQueries({
		@NamedQuery(name = DBAssembly.getAggregateGroupMap, query = "select new openstats.data.AssemblyRepository$GroupMapEntry(key(m), value(m)) from DBAssembly a join a.aggregateGroupMap m where a = ?1 and key(m) in( ?2 )"),
		@NamedQuery(name = DBAssembly.getComputationGroupMap, query = "select new openstats.data.AssemblyRepository$GroupMapEntry(key(m), value(m)) from DBAssembly a join a.computationGroupMap m where a = ?1 and key(m) in( ?2 )" ), 
		@NamedQuery(name = DBAssembly.getAggregateMap, query = "select new openstats.data.AssemblyRepository$AggregateMapEntry(key(m), value(m)) from DBAssembly a join a.aggregateMap m where a = ?1 and key(m) in( ?2 )"), 
		@NamedQuery(name = DBAssembly.getComputationMap, query = "select new openstats.data.AssemblyRepository$ComputationMapEntry(key(m), value(m)) from DBAssembly a join a.computationMap m where a = ?1 and key(m) in( ?2 )")	
	})
	public static final String getAggregateGroupMap = "DBAssembly.getAggregateGroupMap";
	public static final String getComputationGroupMap = "DBAssembly.getComputationGroupMap";
	public static final String getAggregateMap = "DBAssembly.getAggregateMap";
	public static final String getComputationMap = "DBAssembly.getComputationMap";
*/
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
		groupInfoMap.put(dbGroup, new DBGroupInfo(assembly.getAggregateInfoItems(), assembly.getComputeInfoItems()));
		groupResultsMap.put(dbGroup, new DBGroupResults(assembly.getAggregateResults(), assembly.getComputeResults()) );

		districts.copyGroup(dbGroup, assembly.getDistricts());
	}
	
	public void removeGroup(DBGroup dbGroup) {
		groupInfoMap.remove(dbGroup);
		groupResultsMap.remove(dbGroup);		

		districts.removeGroup(dbGroup);
	}

	@XmlTransient
	public Long getId() {
		return id;
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
