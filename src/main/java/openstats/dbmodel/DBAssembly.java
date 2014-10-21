package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import openstats.osmodel.*;

@NamedQueries({ 
	@NamedQuery(name = DBAssembly.LISTASSEMBLIES, query = "SELECT a FROM DBAssembly a") 
})

@SuppressWarnings("serial")
@XmlRootElement
@Entity public class DBAssembly implements Comparable<DBAssembly>, DtoInterface<DBAssembly>, Serializable {
	public static final String LISTASSEMBLIES  = "DBAssembly.listAssemblies";
	@Id @GeneratedValue private Long id;

	private String state;
	private String session;
	
	@OneToOne(cascade={CascadeType.ALL})
	private DBDistricts districts = new DBDistricts();
	
	@OneToMany(cascade={CascadeType.ALL})
	@JoinTable(name="assembly_aggregategroupmap")
	private Map<DBGroup, DBGroupInfo> aggregateGroupMap = new LinkedHashMap<DBGroup, DBGroupInfo>();
	
	@OneToMany(cascade={CascadeType.ALL})
	@JoinTable(name="assembly_computationgroupmap")
	private Map<DBGroup, DBGroupInfo> computationGroupMap = new LinkedHashMap<DBGroup, DBGroupInfo>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private Map<DBGroup, AggregateValues> aggregateMap = new LinkedHashMap<DBGroup, AggregateValues>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private Map<DBGroup, ComputationValues> computationMap = new LinkedHashMap<DBGroup, ComputationValues>();
	
	public DBAssembly() {}
	
	public void update(DBGroup dbGroup, OSAssembly osAssembly) {
		this.state = osAssembly.getState();
		this.session = osAssembly.getSession();
		this.districts = new DBDistricts(dbGroup, osAssembly.getOSDistricts());
		
		if ( osAssembly.getAggregateGroupInfo() != null || osAssembly.getAggregateValues() != null ) {
			aggregateGroupMap.put(dbGroup, new DBGroupInfo(osAssembly.getAggregateGroupInfo()));
			aggregateMap.put(dbGroup, new AggregateValues(osAssembly.getAggregateValues()) );
		}

		if ( osAssembly.getComputationGroupInfo() != null || osAssembly.getComputationValues() != null ) {
			computationGroupMap.put(dbGroup, new DBGroupInfo(osAssembly.getComputationGroupInfo()));
			computationMap.put(dbGroup, new ComputationValues(osAssembly.getComputationValues()) );
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
	public int compareTo(DBAssembly assembly) {
		int s = state.compareTo(assembly.state);
		if ( s != 0 ) return s;
		return this.session.compareTo(assembly.session);
	}
	@Override
	public DBAssembly createDto(DTOTYPE dtoType) {
		DBAssembly assembly = new DBAssembly();
		assembly.setState(getState());
		assembly.setSession(getSession());
		assembly.setDistricts(getDistricts().createDto(dtoType));
		for ( DBGroup key: getAggregateGroupMap().keySet() ) {
			DBGroupInfo groupInfo = aggregateGroupMap.get(key);
			assembly.getAggregateGroupMap().put(key, groupInfo.createDto(dtoType));
		}
		for ( DBGroup key: getComputationGroupMap().keySet() ) {
			DBGroupInfo groupInfo = computationGroupMap.get(key);
			assembly.getComputationGroupMap().put(key, groupInfo.createDto(dtoType));
		}
		switch ( dtoType ) {
		case FULL:
			for ( DBGroup key: getAggregateMap().keySet()) {
				assembly.getAggregateMap().put(key, aggregateMap.get(key) );
			}
			for ( DBGroup key: getComputationMap().keySet()) {
				assembly.getComputationMap().put(key, computationMap.get(key) );
			}
			break;
		case SUMMARY:
			break;
		}
		return assembly;
	}
}
