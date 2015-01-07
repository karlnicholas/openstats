package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity public class DBGroupResults implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;

	@ElementCollection(fetch=FetchType.EAGER)
	@OrderColumn
	private List<AggregateResult> aggregateResults;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@OrderColumn
	private List<ComputeResult> computeResults;
	
	public DBGroupResults() {
		aggregateResults = new ArrayList<AggregateResult>();
		computeResults  = new ArrayList<ComputeResult>();
	}
	public DBGroupResults(List<AggregateResult> aggregateResults, List<ComputeResult> computeResults ) {
		this.aggregateResults = aggregateResults;
		this.computeResults = computeResults;
	}

	public List<AggregateResult> getAggregateResults() {
		return aggregateResults;
	}
	public void setAggregateResult( List<AggregateResult> aggregateResults) {
		this.aggregateResults = aggregateResults;
	}
	public List<ComputeResult> getComputeResults() {
		return computeResults;
	}
	public void setComputeResult(List<ComputeResult> computeResults) {
		this.computeResults = computeResults;
	}

}
