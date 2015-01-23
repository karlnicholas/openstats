package openstats.dbmodel;

import java.math.BigDecimal;

import javax.persistence.*;

@Embeddable
public class Result {
	
	private String value;
	private String error;
	public Result() {}
	public Result(BigDecimal value, BigDecimal error) {
		this.value = value.toString();
		this.error = error.toString();
	}
	public Result add(Result result) {
		BigDecimal tVal = new BigDecimal(value);
		tVal.add(result.getValue());
		value = tVal.toString();

		BigDecimal tErr = new BigDecimal(error);
		tErr.add(result.getValue());
		error = tErr.toString();

		return this;
	}
	public BigDecimal getValue() {
		return new BigDecimal(value);
	}
	public void setValue(BigDecimal value) {
		this.value = value.toString(); 
	}
	public BigDecimal getError() {
		return new BigDecimal(error);
	}
	public void setError(BigDecimal error) {
		this.error = error.toString(); 
	}
}
