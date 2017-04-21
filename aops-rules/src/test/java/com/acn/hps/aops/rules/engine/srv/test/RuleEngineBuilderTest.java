package com.acn.hps.aops.rules.engine.srv.test;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.acn.hps.aops.rules.engine.RuleSet;
import com.acn.hps.aops.rules.engine.srv.RuleEngineBuilder;
import com.acn.hps.aops.rules.util.RuleEngineConfigKeys;
import com.acn.hps.aops.rules.util.RuleEngineType;

@RunWith(PowerMockRunner.class)
public class RuleEngineBuilderTest {

	/**
	 * Test whether rule engine builder object is created or not
	 */
	@Test
	public void testSetupRuleEngine() {
		RuleEngineBuilder ruleEngineBuilder = null;
		ruleEngineBuilder = RuleEngineBuilder.createNew();
		Assert.assertNotNull(ruleEngineBuilder);
		Assert.assertThat(ruleEngineBuilder, instanceOf(RuleEngineBuilder.class));
	}

	/**
	 * Test when no parameters are passed to rule engine then default
	 * configuration should be used
	 * 
	 * @throws IOException
	 */
	@Test
	public void testBuildWithNoParams() throws IOException {
		RuleSet ruleSet = RuleEngineBuilder.createNew().setRulesEngine(RuleEngineType.DROOLS).setParams(null, null)
				.build();
		Assert.assertNotNull(ruleSet);
		Assert.assertThat(ruleSet, instanceOf(RuleSet.class));
	}

	/**
	 * Test when log file path is incorrect then default path for log file
	 * should be used
	 * 
	 * @throws IOException
	 */
	@Test
	public void testBuildWithWrongLogFilePathParam() throws IOException {
		String logFileParam = "\\Any wrong path";
		Map<RuleEngineConfigKeys, Object> paramMap = new HashMap<>();
		paramMap.put(RuleEngineConfigKeys.LOG_FILE_PATH, logFileParam);

		RuleSet ruleSet = RuleEngineBuilder.createNew().setRulesEngine(RuleEngineType.DROOLS).setParams(paramMap, null)
				.build();
		Assert.assertNotNull(ruleSet);
		Assert.assertThat(ruleSet, instanceOf(RuleSet.class));
	}

	/**
	 * Test when no rules properties file, only correct log file path is passed
	 * to rule engine then rule engine should be build using log file path
	 * 
	 * @throws IOException
	 */
	@Test
	public void testBuildWithCorrectLogFilePathParam() throws IOException {
		String logFileParam = System.getProperty("user.dir");
		Map<RuleEngineConfigKeys, Object> paramMap = new HashMap<>();
		paramMap.put(RuleEngineConfigKeys.LOG_FILE_PATH, logFileParam);

		RuleSet ruleSet = RuleEngineBuilder.createNew().setRulesEngine(RuleEngineType.DROOLS).setParams(paramMap, null)
				.build();
		Assert.assertNotNull(ruleSet);
		Assert.assertThat(ruleSet, instanceOf(RuleSet.class));
	}

	/**
	 * Test when rule engine type is null then rule engine should not be build
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testRuleEngineTypeNull() {
		try {
			RuleEngineBuilder.createNew().setRulesEngine(null).build();
		} catch (IOException e) {
			Assert.assertEquals(e.getMessage(), "Rule Engine is not supported.");
		}
	}
}
