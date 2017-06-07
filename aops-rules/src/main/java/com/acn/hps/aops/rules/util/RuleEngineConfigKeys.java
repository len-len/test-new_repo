package com.acn.hps.aops.rules.util;

/**
 * This class is used to store the constants required by rule engine.
 */
public enum RuleEngineConfigKeys {
	PROP_FILE_PATH("propFilePath"), //to get the properties file path
	LOG_FILE_PATH("logFilePath"), //to get log file path from properties file
	RULES_PATH("rules.path"); //to get path of rule files from properties file
	
	private final String string;

	private RuleEngineConfigKeys(String str) {
		this.string = str;
	}

	public String getValue() {
		return this.string;
	}
}
