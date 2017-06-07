package com.acn.hps.aops.rules.engine;

import java.util.List;
import java.util.Map;

/**
 * This interface provide a way to get Rule engine context, based upon context
 * add fact and execute them
 * 
 * @author kuldeep.b.verma
 * @since 10-Mar-2017
 */
public interface RuleSet {

	/**
	 * To get the rule engine context
	 * 
	 * @return RulesEngineContext based upon rule engine session
	 */
	RulesEngineContext getContext();

	/**
	 * To add facts and execute/ fire all rules
	 * 
	 * @param listOfFacts
	 *            - List of facts to be added to the rule engine session
	 * @param globalInuptMap
	 *            - input data to all rule files
	 * @return global output data map
	 */
	Map<String, Object> addFactsAndExecute(final List<Object> listOfFacts, final Map<String, Object> globalInuptMap);
}