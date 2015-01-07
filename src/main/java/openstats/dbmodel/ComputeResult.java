package openstats.dbmodel;

import javax.persistence.Embeddable;

@Embeddable
public class ComputeResult {
	public double value;
	public double error;
	public ComputeResult() {}
	public ComputeResult(double value, double error) {
		this.value = value;
		this.error = error;
	}
}
