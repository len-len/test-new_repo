package com.acn.hps.aops.rules.engine.srv;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.acn.hps.aops.rules.engine.RuleSet;
import com.acn.hps.aops.rules.engine.RulesEngineContext;

/**
 * This class is implementation for Drools rules set, to set the KieContainer,
 * to generate the session, to add the facts and execute
 * 
 * @author kuldeep.b.verma
 * @since 10-Mar-2017
 */
public class DroolsRuleSet implements RuleSet {

	private KieContainer kContainer;
	private String sessionName;
	private static final Logger logger = Logger.getLogger(DroolsRuleSet.class);

	/**
	 * @param kContainer
	 *            - KieContainer associated to knowledge base
	 * @param sessionName
	 *            - Name of the session
	 */
	public DroolsRuleSet(final KieContainer kContainer, final String sessionName) {
		this.kContainer = kContainer;
		this.sessionName = sessionName;
	}

	@Override
	public RulesEngineContext getContext() {
		return new DroolsContext(getSession(kContainer, sessionName));
	}

	@Override
	public Map<String, Object> addFactsAndExecute(final List<Object> listOfFacts, final Map<String, Object> globalInuptMap) {
		RulesEngineContext droolsContext = new DroolsContext(getSession(kContainer, sessionName)).addFacts(listOfFacts, globalInuptMap)
				.executeAllRules();
		Map<String, Object> globalOutputMap = droolsContext.getGlobalOutputDataMap();
		droolsContext.destroy();
		return globalOutputMap;
	}

	/**
	 * To get the session of rules engine
	 * 
	 * @param kContainer
	 *            - KieContainer associated to knowledge base
	 * @param sessionName
	 *            - Name of the session
	 * @return KieSession - A new rule engine session based upon KieContainer
	 */
	private KieSession getSession(final KieContainer kContainer, final String sessionName) {
		logger.info("Creating rule engine session");
		return kContainer.newKieSession(sessionName);
	}
}