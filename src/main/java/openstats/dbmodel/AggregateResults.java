package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity public class AggregateResults implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@OrderColumn
	private List<AggregateResult> resultList;
	
	public AggregateResults() {
		resultList = new ArrayList<AggregateResult>();
	}
	public AggregateResults(List<AggregateResult> resultList) {
		this.resultList = resultList;
	}

	public List<AggregateResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<AggregateResult> resultList) {
		this.resultList = resultList;
	}

}
