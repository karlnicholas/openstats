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
	private Districts districts;
	@OneToMany(cascade={CascadeType.ALL})
	@JoinTable(name="assembly_aggregategroupmap")
	private Map<String, GroupInfo> aggregateGroupMap; 
	@OneToMany(cascade={CascadeType.ALL})
	@JoinTable(name="assembly_computationgroupmap")
	private Map<String, GroupInfo> computationGroupMap; 
	@ElementCollection
	@OrderColumn
	private Map<String, ArrayList<Long>> aggregates;
	@ElementCollection
	@OrderColumn
	private Map<String, ArrayList<Double>> computations;	
	
	public Assembly() {
		districts = new Districts();
		aggregates = new TreeMap<String, ArrayList<Long>>();
		computations = new TreeMap<String, ArrayList<Double>>();
		aggregateGroupMap = new TreeMap<String, GroupInfo>(); 
		computationGroupMap = new TreeMap<String, GroupInfo>(); 
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
	public Districts getDistricts() {
		return districts;
	}
	public void setDistricts(Districts districts) {
		this.districts = districts;
	}
	public Map<String, GroupInfo> getComputationGroupMap() {
		return computationGroupMap;
	}
	public void setComputationGroupMap(Map<String, GroupInfo> computationGroupMap) {
		this.computationGroupMap = computationGroupMap;
	}
	public Map<String, GroupInfo> getAggregateGroupMap() {
		return aggregateGroupMap;
	}
	public void setAggregateGroupMap(Map<String, GroupInfo> aggregateGroupMap) {
		this.aggregateGroupMap = aggregateGroupMap;
	}
	public Map<String, ArrayList<Long>> getAggregates() {
		return aggregates;
	}
	public void setAggregates(Map<String, ArrayList<Long>> aggregates) {
		this.aggregates = aggregates;
	}
	public Map<String, ArrayList<Double>> getComputations() {
		return computations;
	}
	public void setComputations(Map<String, ArrayList<Double>> computations) {
		this.computations = computations;
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
		for ( String key: getAggregateGroupMap().keySet() ) {
			GroupInfo groupInfo = aggregateGroupMap.get(key);
			assembly.getAggregateGroupMap().put(key, groupInfo.createDto(dtoType));
		}
		for ( String key: getComputationGroupMap().keySet() ) {
			GroupInfo groupInfo = computationGroupMap.get(key);
			assembly.getComputationGroupMap().put(key, groupInfo.createDto(dtoType));
		}
		switch ( dtoType ) {
		case FULL:
			for ( String key: getAggregates().keySet()) {
				assembly.getAggregates().put(key, aggregates.get(key) );
			}
			for ( String key: getComputations().keySet()) {
				assembly.getComputations().put(key, computations.get(key) );
			}
			break;
		case SUMMARY:
			break;
		}
		return assembly;
	}
}
