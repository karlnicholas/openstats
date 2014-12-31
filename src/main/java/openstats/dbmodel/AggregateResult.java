package openstats.dbmodel;

import javax.persistence.Embeddable;

@Embeddable
public class AggregateResult {
	public long value;
	public long error;
	public AggregateResult() {}
	public AggregateResult(long value, long error) {
		this.value = value;
		this.error = error;
	}

}
