package openstats.dbmodel;

import javax.persistence.Embeddable;

@Embeddable
public class ComputationResult {
	public double value;
	public double error;
	public ComputationResult() {}
	public ComputationResult(double value, double error) {
		this.value = value;
		this.error = error;
	}
}
