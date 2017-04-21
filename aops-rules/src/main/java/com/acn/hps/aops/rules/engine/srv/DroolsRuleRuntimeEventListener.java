package com.acn.hps.aops.rules.engine.srv;

import org.apache.log4j.Logger;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;

/**
 * To log the runtime information of rule engine
 * 
 * @author kuldeep.b.verma
 * @since 06-Apr-2017
 */
public class DroolsRuleRuntimeEventListener implements RuleRuntimeEventListener {

	private static final Logger logger = Logger.getLogger(DroolsRuleRuntimeEventListener.class);

	@Override
	public void objectInserted(ObjectInsertedEvent event) {
		logger.info("Fact inserted : " + event.getObject().getClass().getSimpleName() + " : "
				+ event.getFactHandle().toExternalForm());
	}

	@Override
	public void objectUpdated(ObjectUpdatedEvent event) {
		logger.info("Fact updated : " + event.getObject() + " : Rule : " + event.getRule());
	}

	@Override
	public void objectDeleted(ObjectDeletedEvent event) {
		logger.info("Fact deleted : " + event.getOldObject());
	}

}
