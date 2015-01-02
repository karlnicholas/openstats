package openstats.dbmodel;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity public class ComputationResults implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private Long id;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@OrderColumn
	private List<ComputationResult> resultList;
	
	public ComputationResults() {
		resultList = new ArrayList<ComputationResult>();
	}
	public ComputationResults(List<ComputationResult> resultList) {
		this.resultList = resultList;
	}

	public List<ComputationResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<ComputationResult> resultList) {
		this.resultList = resultList;
	}

}
