package com.acn.hps.aops.rules.engine.srv.test;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.acn.hps.aops.rules.engine.RuleSet;
import com.acn.hps.aops.rules.engine.srv.RuleEngineBuilder;
import com.acn.hps.aops.rules.util.RuleEngineConfigKeys;
import com.acn.hps.aops.rules.util.RuleEngineType;

@RunWith(PowerMockRunner.class)
public class DroolsRuleEngineTest {
	public static final String USER_DIR = System.getProperty("user.dir");
	public static final String PROP_FILE_PATH = USER_DIR + "\\src\\test\\resources\\rules_config.properties";
	public static final String RULE_FILE_RELATIVE_PATH = "\\src\\main\\resources\\rules\\";

	/**
	 * Test when wrong rule file path is passed then session won't be created
	 * 
	 * @throws IOException
	 */
	@Test(expected = IOException.class)
	public void testInitRuleEngineWrongRuleFilePath() throws IOException {
		Map<RuleEngineConfigKeys, Object> paramMap = new HashMap<>();
		paramMap.put(RuleEngineConfigKeys.PROP_FILE_PATH, PROP_FILE_PATH);
		RuleSet ruleSet = null;

		try {
			setValueInPropertyFile(PROP_FILE_PATH, "", RULE_FILE_RELATIVE_PATH);
			ruleSet = RuleEngineBuilder.createNew().setRulesEngine(RuleEngineType.DROOLS).setParams(paramMap, null)
					.build();
		} catch (IOException e) {
			Assert.assertEquals(e.getMessage(), "No rules file is found at specified location.");
			throw e;
		}
		Assert.assertNull(ruleSet);
	}

	/**
	 * Test when properties file path is passed so all the rule files will be
	 * loaded to session from mentioned path
	 */
	@Test
	public void testInitRuleEngineRuleFilePath() {
		RuleSet ruleSet = null;
		try {
			setValueInPropertyFile(PROP_FILE_PATH, USER_DIR, RULE_FILE_RELATIVE_PATH);
			Map<RuleEngineConfigKeys, Object> paramMap = new HashMap<>();
			paramMap.put(RuleEngineConfigKeys.PROP_FILE_PATH, PROP_FILE_PATH);

			ruleSet = RuleEngineBuilder.createNew().setRulesEngine(RuleEngineType.DROOLS).setParams(paramMap, null)
					.build();
		} catch (IOException e) {
		}
		Assert.assertNotNull(ruleSet);
		Assert.assertThat(ruleSet, instanceOf(RuleSet.class));
	}

	/**
	 * Test when rule file name is passed as parameter then specific rule file
	 * will be added to session
	 */
	@Test
	public void testInitRuleEngineRuleFileWithName() {
		RuleSet ruleSet = null;
		List<String> ruleFilesName = new ArrayList<>();
		ruleFilesName.add("Sample.drl");
		try {
			setValueInPropertyFile(PROP_FILE_PATH, USER_DIR, RULE_FILE_RELATIVE_PATH);
			Map<RuleEngineConfigKeys, Object> paramMap = new HashMap<>();
			paramMap.put(RuleEngineConfigKeys.PROP_FILE_PATH, PROP_FILE_PATH);

			ruleSet = RuleEngineBuilder.createNew().setRulesEngine(RuleEngineType.DROOLS)
					.setParams(paramMap, ruleFilesName).build();
		} catch (IOException e) {
		}
		Assert.assertNotNull(ruleSet);
		Assert.assertThat(ruleSet, instanceOf(RuleSet.class));
	}

	/**
	 * Test with default setting i.e. no properties file, no log file, no rule
	 * file names then all the rule files will be loaded from default
	 * configuration which is mentioned in kmodule.xml
	 */
	@Test
	public void testInitRuleEngineDefaultConfig() {
		RuleSet ruleSet = null;
		try {
			ruleSet = RuleEngineBuilder.createNew().setRulesEngine(RuleEngineType.DROOLS).setParams(null, null).build();
		} catch (IOException e) {
		}
		Assert.assertNotNull(ruleSet);
		Assert.assertThat(ruleSet, instanceOf(RuleSet.class));
	}

	/**
	 * Test when rule file contains syntax error then session won't be created
	 */
	@Test
	public void testInitRuleEngineRuleFileWithNameErr() {
		RuleSet ruleSet = null;
		List<String> ruleFilesName = new ArrayList<>();
		ruleFilesName.add("Sample_Err_For_Test.drl");
		try {
			setValueInPropertyFile(PROP_FILE_PATH, USER_DIR, "\\src\\test\\resources\\");
			Map<RuleEngineConfigKeys, Object> paramMap = new HashMap<>();
			paramMap.put(RuleEngineConfigKeys.PROP_FILE_PATH, PROP_FILE_PATH);

			ruleSet = RuleEngineBuilder.createNew().setRulesEngine(RuleEngineType.DROOLS)
					.setParams(paramMap, ruleFilesName).build();
		} catch (IOException e) {
			Assert.assertEquals(e.getMessage(), "Error while building knowledge base. Please check drl file/s.");
		}
		Assert.assertNull(ruleSet);
	}

	/**
	 * Test when rules file name is not mentioned, but blank object of list of
	 * rule files names is added then session won't be created
	 */
	@Test
	public void testInitRuleEngineRuleFileBlank() {
		RuleSet ruleSet = null;
		List<String> ruleFilesName = new ArrayList<>();
		try {
			setValueInPropertyFile(PROP_FILE_PATH, USER_DIR, "\\src\\test\\resources\\");
			Map<RuleEngineConfigKeys, Object> paramMap = new HashMap<>();
			paramMap.put(RuleEngineConfigKeys.PROP_FILE_PATH, PROP_FILE_PATH);

			ruleSet = RuleEngineBuilder.createNew().setRulesEngine(RuleEngineType.DROOLS)
					.setParams(paramMap, ruleFilesName).build();
		} catch (IOException e) {
			Assert.assertEquals(e.getMessage(), "Error while building knowledge base. Please check drl file/s.");
		}
		Assert.assertNull(ruleSet);
	}

	/***
	 * Test when parameter is blank i.e. properties/log file path is not added
	 * to parameter map then it will use default settings
	 */
	@Test
	public void testInitRuleEngineParamBlank() {
		RuleSet ruleSet = null;
		try {
			setValueInPropertyFile(PROP_FILE_PATH, USER_DIR, "\\src\\test\\resources\\");
			Map<RuleEngineConfigKeys, Object> paramMap = new HashMap<>();

			ruleSet = RuleEngineBuilder.createNew().setRulesEngine(RuleEngineType.DROOLS).setParams(paramMap, null)
					.build();
		} catch (IOException e) {
		}
		Assert.assertNotNull(ruleSet);
	}

	/**
	 * Test when properties file is set with different key in parameter map then
	 * it will use the default settings
	 */
	@Test
	public void testInitRuleEnginePropFileBlank() {
		RuleSet ruleSet = null;
		try {
			setValueInPropertyFile(PROP_FILE_PATH, USER_DIR, "\\src\\test\\resources\\");
			Map<RuleEngineConfigKeys, Object> paramMap = new HashMap<>();
			paramMap.put(RuleEngineConfigKeys.RULES_PATH, PROP_FILE_PATH);

			ruleSet = RuleEngineBuilder.createNew().setRulesEngine(RuleEngineType.DROOLS).setParams(paramMap, null)
					.build();
		} catch (IOException e) {
		}
		Assert.assertNotNull(ruleSet);
	}

	/**
	 * This method is used to set the rule.path value in rules_config.properties
	 * file by test cases.
	 * 
	 * @param propFilePath
	 * @param userDir
	 * @param ruleFileRelativePath
	 * @throws IOException
	 */
	private void setValueInPropertyFile(String propFilePath, String userDir, String ruleFileRelativePath)
			throws IOException {
		FileInputStream in = new FileInputStream(propFilePath);
		Properties props = new Properties();
		props.load(in);
		in.close();

		FileOutputStream out = new FileOutputStream(propFilePath);
		props.setProperty(RuleEngineConfigKeys.RULES_PATH.getValue(), userDir + ruleFileRelativePath);
		props.store(out, null);
		out.close();
	}
}
