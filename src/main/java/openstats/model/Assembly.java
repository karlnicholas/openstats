package openstats.model;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

@NamedQueries({ 
	@NamedQuery(name = "Assembly.listAssemblies", query = "SELECT s FROM Assembly s") 
})

@SuppressWarnings("serial")
@XmlRootElement
@Entity public class Assembly implements Comparable<Assembly>, DtoInterface<Assembly>, Serializable {
	@Id @GeneratedValue private Long id;

	private String state;
	private String session;
	@OneToOne(cascade={CascadeType.ALL})
	private Districts districts = new Districts();
	@OneToMany(cascade={CascadeType.ALL})
	@JoinTable(name="assembly_aggregategroupmap")
	private Map<GroupName, GroupInfo> aggregateGroupMap = new LinkedHashMap<GroupName, GroupInfo>(); 
	@OneToMany(cascade={CascadeType.ALL})
	@JoinTable(name="assembly_computationgroupmap")
	private Map<GroupName, GroupInfo> computationGroupMap = new LinkedHashMap<GroupName, GroupInfo>(); 
	@OneToMany(cascade = CascadeType.ALL)
	private Map<GroupName, AggregateValues> aggregateMap = new LinkedHashMap<GroupName, AggregateValues>();
	@OneToMany(cascade = CascadeType.ALL)
	private Map<GroupName, ComputationValues> computationMap = new LinkedHashMap<GroupName, ComputationValues>();
	
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
	public Districts getDistricts() {
		return districts;
	}
	public void setDistricts(Districts districts) {
		this.districts = districts;
	}
	public Map<GroupName, GroupInfo> getComputationGroupMap() {
		return computationGroupMap;
	}
	public void setComputationGroupMap(Map<GroupName, GroupInfo> computationGroupMap) {
		this.computationGroupMap = computationGroupMap;
	}
	public Map<GroupName, GroupInfo> getAggregateGroupMap() {
		return aggregateGroupMap;
	}
	public void setAggregateGroupMap(Map<GroupName, GroupInfo> aggregateGroupMap) {
		this.aggregateGroupMap = aggregateGroupMap;
	}	
	public Map<GroupName, AggregateValues> getAggregateMap() {
		return aggregateMap;
	}
	public Map<GroupName, ComputationValues> getComputationMap() {
		return computationMap;
	}
	@Override
	public int compareTo(Assembly assembly) {
		int s = state.compareTo(assembly.state);
		if ( s != 0 ) return s;
		return this.session.compareTo(assembly.session);
	}
	@Override
	public Assembly createDto(openstats.model.DtoInterface.DTOTYPE dtoType) {
		Assembly assembly = new Assembly();
		assembly.setState(getState());
		assembly.setSession(getSession());
		assembly.setDistricts(getDistricts().createDto(dtoType));
		for ( GroupName key: getAggregateGroupMap().keySet() ) {
			GroupInfo groupInfo = aggregateGroupMap.get(key);
			assembly.getAggregateGroupMap().put(key, groupInfo.createDto(dtoType));
		}
		for ( GroupName key: getComputationGroupMap().keySet() ) {
			GroupInfo groupInfo = computationGroupMap.get(key);
			assembly.getComputationGroupMap().put(key, groupInfo.createDto(dtoType));
		}
		switch ( dtoType ) {
		case FULL:
			for ( GroupName key: getAggregateMap().keySet()) {
				assembly.getAggregateMap().put(key, aggregateMap.get(key) );
			}
			for ( GroupName key: getComputationMap().keySet()) {
				assembly.getComputationMap().put(key, computationMap.get(key) );
			}
			break;
		case SUMMARY:
			break;
		}
		return assembly;
	}
}
