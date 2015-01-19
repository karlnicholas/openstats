package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
public class DBLegislator implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;
	
	private String name;
	private String party;
	private String term;
	@Temporal(value = TemporalType.DATE)
	private Date startDate;
	@Temporal(value = TemporalType.DATE)
	private Date endDate;

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
}
