package com.acn.hps.aops.rules.engine;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.acn.hps.aops.rules.util.RuleEngineConfigKeys;

/**
 * This interface provide a way to initialize rule engine
 * 
 * @author kuldeep.b.verma
 * @since 10-Mar-2017
 */
public interface RuleEngine {

	/**
	 * @param map
	 *            of parameters to be set to rule engine
	 * @param list
	 *            of rule file name to be set to knowledge base
	 * @return RuleSet associated to knowledge base of the initialized Rule
	 *         engine
	 * @throws IOException
	 */
	RuleSet initializeRuleEngine(final Map<RuleEngineConfigKeys, Object> map, final List<String> ruleFilesName) throws IOException;

}