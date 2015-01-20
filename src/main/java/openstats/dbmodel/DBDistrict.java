package openstats.dbmodel;

import java.io.Serializable;

import java.util.*;

import javax.persistence.*;

import openstats.model.*;
import openstats.model.District.CHAMBER;

@NamedQueries({ 
	@NamedQuery(name = DBDistrict.districtResultsQuery, query = "select d from DBDistrict d join fetch d.groupResultsMap dListgrm join fetch d.legislators where d = ?1 and key(dListgrm) in (?2)" )
})
@SuppressWarnings("serial")
@Entity public class DBDistrict implements Comparable<DBDistrict>, Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;
	public static final String districtResultsQuery = "DBDistrict.districtResultsQuery";
	
	@Column(length=3)
	private String district;
	private CHAMBER chamber;
	private String description;
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private List<DBLegislator> legislators = new ArrayList<DBLegislator>();
	
	@OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY)
	@JoinTable(name="DBDistrict_groupResultsMap",
	    joinColumns=@JoinColumn(name="DBDistrict"),
	    inverseJoinColumns=@JoinColumn(name="DBGroupResults"))
	@MapKeyJoinColumn(name="DBGroup")
	private Map<DBGroup, DBGroupResults> groupResultsMap = new LinkedHashMap<DBGroup, DBGroupResults>();
		
	public DBDistrict() {}
	public DBDistrict(District district) {
		this.district = district.getDistrict();
		this.chamber = district.getChamber();
		this.description = district.getDescription();
	}
	public DBDistrict copyGroup(DBGroup dbGroup, District district) {
		// skip legislators for now
		// copy even if blank
		groupResultsMap.put(dbGroup, new DBGroupResults(district.getResults()) );
		for ( Legislator legislator: district.getLegislators() ) {
			DBLegislator dbLegislator = findLegislator(legislator);
			if ( dbLegislator == null ) {
				dbLegislator = new DBLegislator(legislator);
				legislators.add(dbLegislator);
			}
			dbLegislator.copyGroup(dbGroup, legislator);
		}
		// useful for chaining
		return this;
	}
	
	public DBLegislator findLegislator(Legislator legislator) {
		for ( DBLegislator tLeg: legislators ) {
			if ( tLeg.getName().equals( legislator.getName() ) ) return tLeg;
		}
		return null;
	}

	public void removeGroup(DBGroup dbGroup) {
		// skip legislators for now
		groupResultsMap.remove(dbGroup);
	}

	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public CHAMBER getChamber() {
		return chamber;
	}
	public void setChamber(CHAMBER chamber) {
		this.chamber = chamber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<DBLegislator> getLegislators() {
		return legislators;
	}
	public void setLegislators(List<DBLegislator> legislators) {
		this.legislators = legislators;
	}	
	public Map<DBGroup, DBGroupResults> getGroupResultsMap() {
		return groupResultsMap;
	}
	public void setGroupResultsMap(Map<DBGroup, DBGroupResults> groupResultsMap) {
		this.groupResultsMap = groupResultsMap;
	}
	@Override
	public int compareTo(DBDistrict o) {
		if ( !chamber.equals(o.chamber)) return chamber.compareTo(o.chamber);
		return district.compareTo(o.district);
	}

}
