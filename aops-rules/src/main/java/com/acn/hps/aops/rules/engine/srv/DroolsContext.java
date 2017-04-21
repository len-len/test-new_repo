package com.acn.hps.aops.rules.engine.srv;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.kie.api.runtime.KieSession;

import com.acn.hps.aops.rules.engine.RulesEngineContext;

/**
 * This class is implementation of Drools context, to set the session, to add
 * facts to session and execute
 * 
 * @author kuldeep.b.verma
 * @since 10-Mar-2017
 */
public class DroolsContext implements RulesEngineContext {

	private KieSession kieSession;
	private static final String GLOBAL_INPUT_DATA = "globalInputData";
	private static final String GLOBAL_OUTPUT_DATA = "globalOutputData";
	private static final Logger logger = Logger.getLogger(DroolsContext.class);

	/**
	 * Set the session
	 * 
	 * @param kSession
	 *            - KieSession to be set
	 */
	public DroolsContext(final KieSession kSession) {
		this.kieSession = kSession;
		setAdditionalParams();
	}

	@Override
	public RulesEngineContext addFacts(final List<Object> listOfFacts, final Map<String, Object> globalInputMap) {
		kieSession.setGlobal(GLOBAL_INPUT_DATA, globalInputMap);
		kieSession.setGlobal(GLOBAL_OUTPUT_DATA, new HashMap<String, Object>());
		for (Object object : listOfFacts) {
			kieSession.insert(object);
		}
		logger.info("No. of facts added to Session : " + kieSession.getFactCount());
		return this;
	}

	@Override
	public RulesEngineContext executeAllRules() {
		kieSession.fireAllRules();
		return this;
	}

	@Override
	public void destroy() {
		kieSession.destroy();
		logger.info("Session disposed");
	}

	/**
	 * To set the additional parameters to the session. Currently setting :-
	 * event listeners, rules Logger
	 *
	 */
	private void setAdditionalParams() {
		logger.info("Session Id : " + kieSession.getIdentifier());
		kieSession.addEventListener(new DroolsRuleRuntimeEventListener());
		kieSession.addEventListener(new DroolsDebugAgendaEventListener());
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> getGlobalOutputDataMap() {
		return (Map<String, Object>) kieSession.getGlobal(GLOBAL_OUTPUT_DATA);
	}
}