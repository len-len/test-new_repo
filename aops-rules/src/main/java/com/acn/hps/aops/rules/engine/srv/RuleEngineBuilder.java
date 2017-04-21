package com.acn.hps.aops.rules.engine.srv;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.acn.hps.aops.rules.engine.RuleSet;
import com.acn.hps.aops.rules.util.RuleEngineConfigKeys;
import com.acn.hps.aops.rules.util.RuleEngineType;

/**
 * To build the RuleEngine by passing RuleEngineType and various other
 * parameters.
 * 
 * @author kuldeep.b.verma
 * @since 10-Mar-2017
 */
public final class RuleEngineBuilder {

	private RuleEngineType ruleEngineType;
	private Map<RuleEngineConfigKeys, Object> mapOfParams;
	private List<String> ruleFilesName;

	// To avoid instantiation
	private RuleEngineBuilder() {
	}

	/**
	 * To get the instance of RuleEngineBuilder
	 */
	public static RuleEngineBuilder createNew() {
		return new RuleEngineBuilder();
	}

	/**
	 * Set the type of rule engine i.e. Drools, OPA etc
	 * 
	 * @param ruleEngineType
	 *            - The rule engine type to be set
	 * @return RuleEngineBuilder
	 */
	public RuleEngineBuilder setRulesEngine(final RuleEngineType ruleEngineType) {
		this.ruleEngineType = ruleEngineType;
		return this;
	}

	/**
	 * Set the parameter for the rules engine
	 * 
	 * @param mapOfParams
	 *            - Map of parameters to be set to rule engine
	 * @param ruleFilesName
	 *            - List of rule file name to be set to knowledge base
	 * @return RuleEngineBuilder
	 */
	public RuleEngineBuilder setParams(final Map<RuleEngineConfigKeys, Object> mapOfParams, final List<String> ruleFilesName) {
		this.mapOfParams = mapOfParams;
		this.ruleFilesName = ruleFilesName;
		return this;
	}

	/**
	 * Initialize the rule engine
	 * 
	 * @return RuleSet associated to knowledge base of the initialized Rule
	 *         Engine
	 * @throws IOException
	 *             - if error occurs while getting rules file path from
	 *             properties file or if no rules file is found at specified
	 *             location
	 */
	public RuleSet build() throws IOException {
		RuleSet ruleSet;
		setLogFilePath();
		if (RuleEngineType.DROOLS.equals(ruleEngineType)) {
			ruleSet = new DroolsRuleEngine().initializeRuleEngine(mapOfParams, ruleFilesName);
		} else {
			throw new UnsupportedOperationException("Rule Engine is not supported.");
		}
		return ruleSet;
	}

	/**
	 * To set log file path to System for log4j
	 */
	private void setLogFilePath() {
		String relativePath = System.getProperty("user.dir") + File.separator;
		System.setProperty(RuleEngineConfigKeys.LOG_FILE_PATH.getValue(), relativePath);

		if (null != this.mapOfParams && !this.mapOfParams.isEmpty()) {
			String logFilePath = (String) this.mapOfParams.get(RuleEngineConfigKeys.LOG_FILE_PATH);
			if (null != logFilePath) {
				File folder = new File(logFilePath);
				if (folder.exists()) {
					System.setProperty(RuleEngineConfigKeys.LOG_FILE_PATH.getValue(), logFilePath);
				}
			}
		}
	}
}