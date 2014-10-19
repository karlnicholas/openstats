package openstats.osmodel;

import java.util.List;

public class OSAssembly implements Comparable<OSAssembly> {

	private String state;
	private String session;
	private OSGroup osGroup;
	private OSDistricts osDistricts;
	private OSGroupInfo aggregateGroupInfo; 
	private OSGroupInfo computationGroupInfo; 
	private List<Long> aggregateValues;
	private List<Double> computationValues;
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
	public OSGroup getOSGroup() {
		return osGroup;
	}
	public void setOSGroup(OSGroup osGroup) {
		this.osGroup = osGroup;
	}
	public OSDistricts getOSDistricts() {
		return osDistricts;
	}
	public void setOSDistricts(OSDistricts osDistricts) {
		this.osDistricts = osDistricts;
	}
	public OSGroupInfo getAggregateGroupInfo() {
		return aggregateGroupInfo;
	}
	public void setAggregateGroupInfo(OSGroupInfo aggregateGroupInfo) {
		this.aggregateGroupInfo = aggregateGroupInfo;
	}
	public OSGroupInfo getComputationGroupInfo() {
		return computationGroupInfo;
	}
	public void setComputationGroupInfo(OSGroupInfo computationGroupInfo) {
		this.computationGroupInfo = computationGroupInfo;
	}
	public List<Long> getAggregateValues() {
		return aggregateValues;
	}
	public void setAggregateValues(List<Long> aggregateValues) {
		this.aggregateValues = aggregateValues;
	}
	public List<Double> getComputationValues() {
		return computationValues;
	}
	public void setComputationValues(List<Double> computationValues) {
		this.computationValues = computationValues;
	}
	@Override
	public int compareTo(OSAssembly assembly) {
		int s = state.compareTo(assembly.state);
		if ( s != 0 ) return s;
		return this.session.compareTo(assembly.session);
	}
}
