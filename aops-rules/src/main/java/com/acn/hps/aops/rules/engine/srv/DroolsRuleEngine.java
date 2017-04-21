package com.acn.hps.aops.rules.engine.srv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;

import com.acn.hps.aops.rules.engine.RuleEngine;
import com.acn.hps.aops.rules.engine.RuleSet;
import com.acn.hps.aops.rules.util.RuleEngineConfigKeys;

/**
 * This class is implementation of Drools rule engine, to initialize rule engine
 * 
 * @author kuldeep.b.verma
 * @since 10-Mar-2017
 */
public final class DroolsRuleEngine implements RuleEngine {

	// used for default configuration
	private String SESSION_NAME = "ksession-rules";
	private static final Logger logger = Logger.getLogger(DroolsRuleEngine.class);

	DroolsRuleEngine() {
	}

	/**
	 * To initialize Rule Engine with default configuration
	 * 
	 * @param kieServices
	 *            - A KieService object to get the KieContainer
	 * @return KieContainer associated to KieServices
	 */
	private KieContainer initializeRuleEngine(KieServices kieServices) {
		return kieServices.getKieClasspathContainer();
	}

	/**
	 * Initialize the rule engine
	 * 
	 * @param Map
	 *            of parameters to be set to rule engine
	 * @param List
	 *            of rule file name to be set to knowledge base
	 * 
	 * @return RuleSet associated to knowledge base of the initialized Rule
	 *         Engine
	 * @throws IOException
	 *             - if error occurs while getting rules file path from
	 *             properties file or if no rules file is found at specified
	 *             location
	 */
	public RuleSet initializeRuleEngine(final Map<RuleEngineConfigKeys, Object> paramMap, final List<String> ruleFilesName) throws IOException {
		KieContainer kContainer;

		// load up the knowledge base
		KieServices kieServices = KieServices.Factory.get();

		if (null != paramMap && !paramMap.isEmpty()
				&& null != paramMap.get(RuleEngineConfigKeys.PROP_FILE_PATH)) {
			String propFilePath = (String) paramMap.get(RuleEngineConfigKeys.PROP_FILE_PATH);
			String rulesFilePath = null;
			List<File> files = new ArrayList<>();

			rulesFilePath = getRulesPath(propFilePath);

			if (null != rulesFilePath) {
				if (null != ruleFilesName && !ruleFilesName.isEmpty()) {
					files = getRuleFiles(rulesFilePath, ruleFilesName);
				} else {
					files = getAllFiles(rulesFilePath);
				}
			}

			if (files.isEmpty()) {
				logger.error("No rules file is found at specified location.");
				throw new FileNotFoundException("No rules file is found at specified location.");
			}

			KieFileSystem kieFileSystem = addRuleFiles(kieServices, files);

			// creating knowledge base; based upon drl files
			KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
			kieBuilder.buildAll();// building the knowledge base

			if (kieBuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
				logger.error("Error while building knowledge base. Please check drl file/s.");
				throw new IOException("Error while building knowledge base. Please check drl file/s.");
			}

			KieRepository kieRepository = kieServices.getRepository();
			kContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
			SESSION_NAME = null;
		} else {
			logger.info("No Properties file provided, using default configuration for rule engine.");
			kContainer = initializeRuleEngine(kieServices);
		}
		logger.info("Rule engine container created : " + kContainer.hashCode());
		return new DroolsRuleSet(kContainer, SESSION_NAME);
	}

	/**
	 * To get the rule file by name
	 * 
	 * @param rulesFilePath
	 *            - path of the rules file
	 * @param ruleFilesName
	 *            - name of the rules file
	 * @return list of Files
	 */
	private List<File> getRuleFiles(final String rulesFilePath, final List<String> ruleFilesName) {
		List<File> files = new ArrayList<>();

		File fl = new File(rulesFilePath);
		if (fl.exists()) {
			ruleFilesName.forEach(ruleFileName -> {
				File file = new File(rulesFilePath + ruleFileName);
				if (file.exists()) {
					files.add(file);
				}
			});
		}
		return files;
	}

	/**
	 * To add the rules file to KieFileSystem
	 * 
	 * @param kieServices
	 *            - The KieServices object to create Kie file system
	 * @param List
	 *            of rules files to be added to KieService
	 * @return KieFileSystem associated to the KieServices
	 */
	private KieFileSystem addRuleFiles(final KieServices kieServices, final List<File> files) {
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		files.forEach(file -> {
			Resource resource = kieServices.getResources().newFileSystemResource(file)
					.setResourceType(ResourceType.DRL);
			kieFileSystem.write(resource);
		});

		return kieFileSystem;
	}

	/**
	 * To get all the file from specified path
	 * 
	 * @param folderPath
	 *            - A folder path string
	 * @return List of files from the folder path
	 */
	private List<File> getAllFiles(final String folderPath) {
		List<File> listOfFile = new ArrayList<>();

		File folder = new File(folderPath);
		if (folder.exists()) {
			File[] files = folder.listFiles();
			listOfFile = Arrays.asList(files);
		}
		return listOfFile;
	}

	/**
	 * To get the rule file path from properties file
	 * 
	 * @param propFilePath
	 *            - A properties path string
	 * @return Rule file path from the properties file
	 * @throws IOException
	 *             - if properties file path is null or any error while getting
	 *             the rules file path
	 */
	private String getRulesPath(final String propFilePath) throws IOException {
		Properties prop = new Properties();
		String propVal;
		try (InputStream input = new FileInputStream(propFilePath)) {
			prop.load(input);
			propVal = prop.getProperty(RuleEngineConfigKeys.RULES_PATH.getValue());
		}
		return propVal;
	}
}