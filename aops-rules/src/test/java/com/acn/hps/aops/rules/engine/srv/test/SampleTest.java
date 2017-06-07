package com.acn.hps.aops.rules.engine.srv.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.acn.hps.aops.rules.engine.RuleSet;
import com.acn.hps.aops.rules.engine.srv.RuleEngineBuilder;
import com.acn.hps.aops.rules.engine.srv.test.util.SampleDataModel.Message;
import com.acn.hps.aops.rules.util.RuleEngineConfigKeys;
import com.acn.hps.aops.rules.util.RuleEngineType;

/**
 * This is a sample class to launch a rule.
 */
public class SampleTest {

	static String PROP_FILE_PATH = "C:/Users/kuldeep.b.verma/Desktop/rules_config.properties";
	static String LOG_FILE_PATH = "C:/Users/kuldeep.b.verma/Desktop/log/";
	
	public static void main(String[] args) {

		Map<RuleEngineConfigKeys, Object> paramMap = new HashMap<>();
		
		paramMap.put(RuleEngineConfigKeys.PROP_FILE_PATH, PROP_FILE_PATH);
		paramMap.put(RuleEngineConfigKeys.LOG_FILE_PATH, LOG_FILE_PATH);
		
		List<String> ruleFilesName = new ArrayList<>();
		ruleFilesName.add("Sample.drl");
		ruleFilesName.add("Sample_2.drl");

		List<Object> listOfFacts = new ArrayList<Object>();
		Message message = new Message();
		message.setMessage("Hello World");
		message.setStatus(Message.HELLO);

		// Spring beans: JobsRuleSet, reportRulesSet etc
		RuleSet ruleSet = null;
		try {
			ruleSet = RuleEngineBuilder
					.createNew()
					.setRulesEngine(RuleEngineType.DROOLS)
//					.setParams(paramList, null) //only properties file, so all the drl files will be loaded
//					.setParams(null, null) // neither properties file nor drl file name - Defalut config, all the drl files will be loaded from default location
//					.setParams(paramMap, ruleFilesName) //drl file name, specific drl file/s will be loaded
					.setParams(paramMap, null)// no prop file but log file path and no drl files
					.build();
			
			listOfFacts.add(message);	
			Map<String, Object> myMap = new HashMap<>();
//			myMap.put("MYKEY", "MYVALUE");
			myMap = ruleSet.addFactsAndExecute(listOfFacts, myMap);
			
			// after return from fireAllRules
			System.out.println("Return Map Message : " + Message.class.cast(myMap.get("Val1")).getMessage());
						
		} catch (IOException e) {
		}
	}
}
