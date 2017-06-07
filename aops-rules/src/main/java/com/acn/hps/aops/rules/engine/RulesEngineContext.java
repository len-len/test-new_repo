package com.acn.hps.aops.rules.engine;

import java.util.List;
import java.util.Map;

/**
 * This interface provides a way to add facts and execute them
 * 
 * @author kuldeep.b.verma
 * @since 10-Mar-2017
 */
public interface RulesEngineContext {

	/**
	 * Add Facts to Context
	 * 
	 * @param listOfFacts
	 *            - List of facts to be added to the rule engine session
	 * @param globalInputMap
	 *            - input data to all rule files
	 * @return RulesEngineContext based upon rule engine session
	 */
	RulesEngineContext addFacts(final List<Object> listOfFacts, final Map<String, Object> globalInputMap);

	/**
	 * Execute All Rules
	 */
	RulesEngineContext executeAllRules();

	/**
	 * Destroy current session of rule engine
	 */
	void destroy();
	
	/**
	 * Get global output data map from RuleEngine session
	 * 
	 * @return map of object returned by rule engine
	 */
	Map<String, Object> getGlobalOutputDataMap();

}