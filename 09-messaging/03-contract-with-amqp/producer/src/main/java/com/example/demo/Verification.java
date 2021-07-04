package com.example.demo;

/**
 * @author Marcin Grzejszczak
 * @since
 */
public class Verification {
	public boolean eligible;

	public Verification(boolean eligible) {
		this.eligible = eligible;
	}

	public Verification() {
	}

	public boolean isEligible() {
		return this.eligible;
	}

	public void setEligible(boolean eligible) {
		this.eligible = eligible;
	}

	@Override public String toString() {
		return "Verification{" + "eligible=" + this.eligible + '}';
	}
}
