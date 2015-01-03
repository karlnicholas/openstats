package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import openstats.model.*;

@SuppressWarnings("serial")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = DBAssembly.getAggregateGroupMap, query = "select new openstats.data.AssemblyRepository$GroupMapEntry(key(m), value(m)) from DBAssembly a join a.aggregateGroupMap m where a = ?1 and key(m) in( ?2 )"),
	@NamedQuery(name = DBAssembly.getComputationGroupMap, query = "select new openstats.data.AssemblyRepository$GroupMapEntry(key(m), value(m)) from DBAssembly a join a.computationGroupMap m where a = ?1 and key(m) in( ?2 )" ), 
	@NamedQuery(name = DBAssembly.getAggregateMap, query = "select new openstats.data.AssemblyRepository$AggregateMapEntry(key(m), value(m)) from DBAssembly a join a.aggregateMap m where a = ?1 and key(m) in( ?2 )"), 
	@NamedQuery(name = DBAssembly.getComputationMap, query = "select new openstats.data.AssemblyRepository$ComputationMapEntry(key(m), value(m)) from DBAssembly a join a.computationMap m where a = ?1 and key(m) in( ?2 )")	
})
@Entity public class DBAssembly implements Comparable<DBAssembly>, Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;
	
	public static final String getAggregateGroupMap = "DBAssembly.getAggregateGroupMap";
	public static final String getComputationGroupMap = "DBAssembly.getComputationGroupMap";
	public static final String getAggregateMap = "DBAssembly.getAggregateMap";
	public static final String getComputationMap = "DBAssembly.getComputationMap";

	private String state;
	private String session;
	
	@OneToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	private DBDistricts districts = new DBDistricts();
	
	@OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	@JoinTable(name="DBAssembly_aggregateGroupMap",
	    joinColumns=@JoinColumn(name="DBAssembly"),
	    inverseJoinColumns=@JoinColumn(name="DBGroupInfo"))
	@MapKeyJoinColumn(name="DBGroup")
	private Map<DBGroup, DBGroupInfo> aggregateGroupMap = new LinkedHashMap<DBGroup, DBGroupInfo>();
	
	@OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	@JoinTable(name="DBAssembly_computationGroupMap",
	    joinColumns=@JoinColumn(name="DBAssembly"),
	    inverseJoinColumns=@JoinColumn(name="DBGroupInfo"))
	@MapKeyJoinColumn(name="DBGroup")
	private Map<DBGroup, DBGroupInfo> computationGroupMap = new LinkedHashMap<DBGroup, DBGroupInfo>();
	
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="DBAssembly_aggregateMap",
	    joinColumns=@JoinColumn(name="DBAssembly"),
	    inverseJoinColumns=@JoinColumn(name="AggregateResults"))
	@MapKeyJoinColumn(name="DBGroup")
	private Map<DBGroup, AggregateResults> aggregateMap = new LinkedHashMap<DBGroup, AggregateResults>();
	
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name="DBAssembly_computationMap",
	    joinColumns=@JoinColumn(name="DBAssembly"),
	    inverseJoinColumns=@JoinColumn(name="ComputationResults"))
	@MapKeyJoinColumn(name="DBGroup")
	private Map<DBGroup, ComputationResults> computationMap = new LinkedHashMap<DBGroup, ComputationResults>();
	
	public DBAssembly() {}
	
	public DBAssembly(Assembly assembly) {
		this.state = assembly.getState();
		this.session = assembly.getSession();
		this.districts = new DBDistricts(assembly.getDistricts());
	}
	
	public void copyGroup(DBGroup dbGroup, Assembly assembly) {
		districts.copyGroup(dbGroup, assembly.getDistricts());
		
		if ( assembly.getAggregateGroupInfo() != null || assembly.getAggregateResults() != null ) {
			aggregateGroupMap.put(dbGroup, new DBGroupInfo(assembly.getAggregateGroupInfo()));
			aggregateMap.put(dbGroup, new AggregateResults(assembly.getAggregateResults()) );
		}

		if ( assembly.getComputationGroupInfo() != null || assembly.getComputationResults() != null ) {
			computationGroupMap.put(dbGroup, new DBGroupInfo(assembly.getComputationGroupInfo()));
			computationMap.put(dbGroup, new ComputationResults(assembly.getComputationResults()) );
		}
	}
	
	public void removeGroup(DBGroup dbGroup) {
		districts.removeGroup(dbGroup);
		
		if ( aggregateGroupMap.containsKey(dbGroup) || aggregateMap.containsKey(dbGroup) ) {
			aggregateGroupMap.remove(dbGroup);
			aggregateMap.remove(dbGroup);
		}

		if ( computationGroupMap.containsKey(dbGroup) || computationMap.containsKey(dbGroup) ) {
			computationGroupMap.remove(dbGroup);
			computationMap.remove(dbGroup);
		}
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
	public Map<DBGroup, DBGroupInfo> getComputationGroupMap() {
		return computationGroupMap;
	}
	public void setComputationGroupMap(Map<DBGroup, DBGroupInfo> computationGroupMap) {
		this.computationGroupMap = computationGroupMap;
	}
	public Map<DBGroup, DBGroupInfo> getAggregateGroupMap() {
		return aggregateGroupMap;
	}
	public void setAggregateGroupMap(Map<DBGroup, DBGroupInfo> aggregateGroupMap) {
		this.aggregateGroupMap = aggregateGroupMap;
	}	
	public Map<DBGroup, AggregateResults> getAggregateMap() {
		return aggregateMap;
	}
	public void setAggregateMap(Map<DBGroup, AggregateResults> aggregateMap) {
		this.aggregateMap = aggregateMap;
	}
	public Map<DBGroup, ComputationResults> getComputationMap() {
		return computationMap;
	}
	public void setComputationMap(Map<DBGroup, ComputationResults> computationMap) {
		this.computationMap = computationMap;
	}
	@Override
	public int compareTo(DBAssembly dbAssembly) {
		int s = state.compareTo(dbAssembly.state);
		if ( s != 0 ) return s;
		return this.session.compareTo(dbAssembly.session);
	}
}
