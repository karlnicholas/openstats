package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import openstats.model.*;

@SuppressWarnings("serial")
@XmlRootElement
@Entity public class DBAssembly implements Comparable<DBAssembly>, Serializable {
	@Id @GeneratedValue private Long id;

	private String state;
	private String session;
	
	@OneToOne(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	private DBDistricts districts = new DBDistricts();
	
	@OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	@JoinTable(name="assembly_aggregategroupmap")
	private Map<DBGroup, DBGroupInfo> aggregateGroupMap = new LinkedHashMap<DBGroup, DBGroupInfo>();
	
	@OneToMany(fetch=FetchType.LAZY, cascade={CascadeType.ALL})
	@JoinTable(name="assembly_computationgroupmap")
	private Map<DBGroup, DBGroupInfo> computationGroupMap = new LinkedHashMap<DBGroup, DBGroupInfo>();
	
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	private Map<DBGroup, AggregateValues> aggregateMap = new LinkedHashMap<DBGroup, AggregateValues>();
	
	@OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	private Map<DBGroup, ComputationValues> computationMap = new LinkedHashMap<DBGroup, ComputationValues>();
	
	public DBAssembly() {}
	
	public DBAssembly(Assembly assembly) {
		this.state = assembly.getState();
		this.session = assembly.getSession();
		this.districts = new DBDistricts(assembly.getDistricts());
	}
	
	public void copyGroup(DBGroup dbGroup, Assembly assembly) {
		districts.copyGroup(dbGroup, assembly.getDistricts());
		
		if ( assembly.getAggregateGroupInfo() != null || assembly.getAggregateValues() != null ) {
			aggregateGroupMap.put(dbGroup, new DBGroupInfo(assembly.getAggregateGroupInfo()));
			aggregateMap.put(dbGroup, new AggregateValues(assembly.getAggregateValues()) );
		}

		if ( assembly.getComputationGroupInfo() != null || assembly.getComputationValues() != null ) {
			computationGroupMap.put(dbGroup, new DBGroupInfo(assembly.getComputationGroupInfo()));
			computationMap.put(dbGroup, new ComputationValues(assembly.getComputationValues()) );
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
	public Map<DBGroup, AggregateValues> getAggregateMap() {
		return aggregateMap;
	}
	public Map<DBGroup, ComputationValues> getComputationMap() {
		return computationMap;
	}
	@Override
	public int compareTo(DBAssembly dbAssembly) {
		int s = state.compareTo(dbAssembly.state);
		if ( s != 0 ) return s;
		return this.session.compareTo(dbAssembly.session);
	}
}
