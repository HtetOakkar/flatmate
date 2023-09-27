package com.lotus.flatmate.post.entity;

public enum ContractType {
	THREE_MONTH("3 month"), SIX_MONTH("6 month"), ONE_MONTH("1 month"), ONE_YEAR("1 year"), THREE_YEAR("3 year");

	private final String value;

	ContractType(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
