package com.example.demo.model;

public class FraudCheckResult {

	private FraudCheckStatus status;

	public FraudCheckResult() {
	}

	public FraudCheckResult(FraudCheckStatus fraudCheckStatus) {
		this.status = fraudCheckStatus;
	}

	public FraudCheckStatus getStatus() {
		return this.status;
	}

	public void setStatus(FraudCheckStatus status) {
		this.status = status;
	}

}
