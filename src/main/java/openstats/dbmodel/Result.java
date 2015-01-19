package openstats.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable
public class Result {
	public BigDecimal value;
	public BigDecimal error;
	public Result() {}
	public Result(BigDecimal value, BigDecimal error) {
		this.value = value;
		this.error = error;
	}
	public Result add(Result result) {
		value.add(result.value);
		error.add(result.error);	// not the right way to do this.
		return this;
	}
}
