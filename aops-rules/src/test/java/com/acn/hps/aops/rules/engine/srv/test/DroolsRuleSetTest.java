package com.acn.hps.aops.rules.engine.srv.test;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.acn.hps.aops.rules.engine.RuleSet;
import com.acn.hps.aops.rules.engine.RulesEngineContext;
import com.acn.hps.aops.rules.engine.srv.RuleEngineBuilder;
import com.acn.hps.aops.rules.engine.srv.test.util.SampleDataModel.Message;
import com.acn.hps.aops.rules.util.RuleEngineType;

@RunWith(PowerMockRunner.class)
public class DroolsRuleSetTest {

	/**
	 * Test when facts are added and executed, then global output map should be
	 * returned
	 * 
	 * @throws IOException
	 */
	@Test
	public void testAddFactAndExecute() throws IOException {
		RuleSet ruleSet = null;
		Map<String, Object> outMap = new HashMap<>();
		List<Object> listOfFacts = new ArrayList<Object>();
		Message message = new Message();
		message.setMessage("Hello World");
		message.setStatus(Message.HELLO);
		listOfFacts.add(message);
		ruleSet = RuleEngineBuilder.createNew().setRulesEngine(RuleEngineType.DROOLS).setParams(null, null).build();
		outMap = ruleSet.addFactsAndExecute(listOfFacts, null);
		Assert.assertNotNull(outMap);
		Assert.assertThat(outMap.get("Val1"), instanceOf(Message.class));
	}

	/**
	 * Test when facts are added and executed using Drools context then global
	 * output map should be returned
	 * 
	 * @throws IOException
	 */
	@Test
	public void testAddFactAndExecuteUsingDroolsContext() throws IOException {
		RuleSet ruleSet = null;
		Map<String, Object> outMap = new HashMap<>();
		List<Object> listOfFacts = new ArrayList<Object>();
		Message message = new Message();
		message.setMessage("Hello World");
		message.setStatus(Message.HELLO);
		listOfFacts.add(message);

		ruleSet = RuleEngineBuilder.createNew().setRulesEngine(RuleEngineType.DROOLS).setParams(null, null).build();
		RulesEngineContext rulesEngineContext = ruleSet.getContext();
		rulesEngineContext.addFacts(listOfFacts, null);
		rulesEngineContext.executeAllRules();
		outMap = rulesEngineContext.getGlobalOutputDataMap();

		Assert.assertNotNull(outMap);
		Assert.assertThat(outMap.get("Val1"), instanceOf(Message.class));
	}
}
