package com.acn.hps.aops.rules.engine.srv;

import org.apache.log4j.Logger;
import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.event.rule.MatchCancelledEvent;
import org.kie.api.event.rule.MatchCreatedEvent;

/**
 * To log rules execution information
 * 
 * @author kuldeep.b.verma
 * @since 06-Apr-2017
 */
public class DroolsDebugAgendaEventListener extends DefaultAgendaEventListener {

	private static final Logger logger = Logger.getLogger(DroolsDebugAgendaEventListener.class);

	@Override
	public void afterMatchFired(AfterMatchFiredEvent event) {
		Rule rule = event.getMatch().getRule();
		logger.info("Rule fired : " + rule.getName());
	}

	@Override
	public void matchCreated(MatchCreatedEvent event) {
		event.getMatch().getFactHandles().forEach(x -> {
			logger.info("Processing fact : " + x.toExternalForm());
		});
		event.getMatch().getDeclarationIds().forEach(x -> {
			logger.info("Rule object : " + x);
		});
	}

	@Override
	public void matchCancelled(MatchCancelledEvent event) {
		logger.info("Rule match cancelled : " + event.getCause());
	}

}