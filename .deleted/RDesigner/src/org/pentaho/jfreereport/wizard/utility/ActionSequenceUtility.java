/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved. 
 * This software was developed by Pentaho Corporation and is provided under the terms 
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use 
 * this file except in compliance with the license. If you need a copy of the license, 
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt. The Original Code is the Pentaho 
 * BI Platform.  The Initial Developer is Pentaho Corporation.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS" 
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to 
 * the license for the specific language governing your rights and limitations.
 *
 * @created Feb 01, 2006
 * @author Michael D'Amour
 */
package org.pentaho.jfreereport.wizard.utility;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.pentaho.core.connection.IPentahoResultSet;
import org.pentaho.core.runtime.IRuntimeContext;
import org.pentaho.core.session.StandaloneSession;
import org.pentaho.core.solution.IActionCompleteListener;
import org.pentaho.core.solution.ISolutionEngine;
import org.pentaho.core.solution.SimpleOutputHandler;
import org.pentaho.core.solution.SimpleParameterProvider;
import org.pentaho.core.system.PentahoSystem;
import org.pentaho.core.ui.IPentahoUrlFactory;
import org.pentaho.core.ui.SimpleUrlFactory;
import org.pentaho.jfreereport.wizard.utility.report.ReportParameterUtility;
import org.pentaho.util.logging.ILogger;

public class ActionSequenceUtility implements IActionCompleteListener {
  private static ActionSequenceUtility instance;

  public static ActionSequenceUtility getInstance() {
    if (instance == null) {
      instance = new ActionSequenceUtility();
    }
    return instance;
  }

  public void createKettleActionSequence(String actionSeqDocInput, String actionSeqDocOutput, String kettleTransformationFile, String kettleStep) {
    SAXReader reader = new SAXReader();
    try {
      File in = new File(actionSeqDocInput);
      File out = new File(actionSeqDocOutput);
      File ktrFile = new File(kettleTransformationFile);
      Document document = reader.read(in);
      document.selectSingleNode("/action-sequence/name").setText(out.getName()); //$NON-NLS-1$
      document.selectSingleNode("/action-sequence/resources/transformation-file/solution-file/location").setText(ktrFile.getName()); //$NON-NLS-1$
      List actionDefinitionList = document.selectNodes("/action-sequence/actions/action-definition"); //$NON-NLS-1$
      for (int i = 0; i < actionDefinitionList.size(); i++) {
        Node actionDefinition = (Node) actionDefinitionList.get(i);
        String compDefName = actionDefinition.selectSingleNode("component-name").getText(); //$NON-NLS-1$
        if (compDefName != null && compDefName.equals("KettleComponent")) { //$NON-NLS-1$
          Node kettleStepNode = actionDefinition.selectSingleNode("component-definition/importstep"); //$NON-NLS-1$
          kettleStepNode.setText(kettleStep);
          break;
        }
      }
      FileOutputStream outStream = new FileOutputStream(out);
      OutputFormat format = OutputFormat.createPrettyPrint();
      XMLWriter writer = new XMLWriter(outStream, format);
      writer.write(document);
      writer.close();
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }
  
  public void createXQueryActionSequence(String actionSeqDocInput, String actionSeqDocOutput, String outputType, String jfreeDoc, String xQuerySource, String query) {
    SAXReader reader = new SAXReader();
    try {
      File in = new File(actionSeqDocInput);
      File out = new File(actionSeqDocOutput);
      File jfreeFile = new File(jfreeDoc);
      Document document = reader.read(in);
      document.selectSingleNode("/action-sequence/name").setText(out.getName()); //$NON-NLS-1$
      document.selectSingleNode("/action-sequence/inputs/output-type/default-value").setText(outputType); //$NON-NLS-1$
      document.selectSingleNode("/action-sequence/resources/report-definition/solution-file/location").setText(jfreeFile.getName()); //$NON-NLS-1$
      document.selectSingleNode("/action-sequence/resources/document/solution-file/location").setText(xQuerySource); //$NON-NLS-1$
      List actionDefinitionList = document.selectNodes("/action-sequence/actions/action-definition"); //$NON-NLS-1$
      for (int i = 0; i < actionDefinitionList.size(); i++) {
        Node actionDefinition = (Node) actionDefinitionList.get(i);
        String compDefName = actionDefinition.selectSingleNode("component-name").getText(); //$NON-NLS-1$
        if (compDefName != null && compDefName.equals("XQueryLookupRule")) { //$NON-NLS-1$
          Node queryNode = actionDefinition.selectSingleNode("component-definition/query"); //$NON-NLS-1$
          queryNode.setText(query);
        }
      }
      FileOutputStream outStream = new FileOutputStream(out);
      OutputFormat format = OutputFormat.createPrettyPrint();
      XMLWriter writer = new XMLWriter(outStream, format);
      writer.write(document);
      writer.close();
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public void createResultSetActionSequence(String actionSeqDocInput, String actionSeqDocOutput, String outputType, String jfreeDoc, IPentahoResultSet resultSet) {
    SAXReader reader = new SAXReader();
    try {
      File in = new File(actionSeqDocInput);
      File out = new File(actionSeqDocOutput);
      File jfreeFile = new File(jfreeDoc);
      Document document = reader.read(in);
      document.selectSingleNode("/action-sequence/name").setText(out.getName()); //$NON-NLS-1$
      document.selectSingleNode("/action-sequence/inputs/output-type/default-value").setText(outputType); //$NON-NLS-1$
      document.selectSingleNode("/action-sequence/resources/report-definition/solution-file/location").setText(jfreeFile.getName()); //$NON-NLS-1$
      FileOutputStream outStream = new FileOutputStream(out);
      OutputFormat format = OutputFormat.createPrettyPrint();
      XMLWriter writer = new XMLWriter(outStream, format);
      writer.write(document);
      writer.close();
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public void createActionSequence(String actionSeqDocInput, String actionSeqDocOutput, String outputType, String jfreeDoc, String mondrianCubeDef, String source, String live, String jndi, String driver, String connectInfo,
      String username, String password, String query) {
    SAXReader reader = new SAXReader();
    try {
      File in = new File(actionSeqDocInput);
      File out = new File(actionSeqDocOutput);
      File jfreeFile = new File(jfreeDoc);
      Document document = reader.read(in);
      List actionDefinitionList = document.selectNodes("/action-sequence/actions/*"); //$NON-NLS-1$
      Element lookupRuleElement = null;
      for (int i = 0; i < actionDefinitionList.size(); i++) {
        Element possibleActionDefinitionElement = (Element) actionDefinitionList.get(i);
        if (possibleActionDefinitionElement.selectSingleNode("component-name").getText().equalsIgnoreCase("SQLLookupRule")) { //$NON-NLS-1$ //$NON-NLS-2$
          lookupRuleElement = possibleActionDefinitionElement;
        } else if (possibleActionDefinitionElement.selectSingleNode("component-name").getText().equalsIgnoreCase("MDXLookupRule")) { //$NON-NLS-1$ //$NON-NLS-2$
          lookupRuleElement = possibleActionDefinitionElement;
        } else if (possibleActionDefinitionElement.selectSingleNode("component-name").getText().equalsIgnoreCase("MQLRelationalDataComponent")) {
          lookupRuleElement = possibleActionDefinitionElement;
        }
      }
      if (source.equals("sql")) { //$NON-NLS-1$
        // add inputs to the action sequence for each {PARAMETER} found in the query
        Element seqInputsElement = (Element) document.selectSingleNode("/action-sequence/inputs"); //$NON-NLS-1$
        List parameterNames = ReportParameterUtility.getParametersKeys(query);
        for (int i = 0; i < parameterNames.size(); i++) {
          String param = (String) parameterNames.get(i);
          Element inputElement = seqInputsElement.addElement(param);
          inputElement.addAttribute("type", "string"); //$NON-NLS-1$ //$NON-NLS-2$
          Element defaultValueElement = inputElement.addElement("default-value"); //$NON-NLS-1$
          defaultValueElement.setText(""); //$NON-NLS-1$
          Element sourcesElement = inputElement.addElement("sources"); //$NON-NLS-1$
          Element requestElement = sourcesElement.addElement("request"); //$NON-NLS-1$
          requestElement.setText(param);
        }
        // add these inputs to the component inputs
        Element componentInputs = (Element) lookupRuleElement.selectSingleNode("action-inputs"); //$NON-NLS-1$
        for (int i = 0; i < parameterNames.size(); i++) {
          String param = (String) parameterNames.get(i);
          Element inputElement = componentInputs.addElement(param);
          inputElement.addAttribute("type", "string"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        // patch up query for use in action-sequence
        query = ReportParameterUtility.setupParametersForActionSequence(query);
      }
      
      document.selectSingleNode("/action-sequence/name").setText(out.getName()); //$NON-NLS-1$
      document.selectSingleNode("/action-sequence/inputs/output-type/default-value").setText(outputType); //$NON-NLS-1$
      document.selectSingleNode("/action-sequence/resources/report-definition/solution-file/location").setText(jfreeFile.getName()); //$NON-NLS-1$
      // ((Element)compDefNode.selectSingleNode("query")).setText("<![CDATA[" + // query + "]]>"); //$NON-NLS-1$
      // ((Element)compDefNode.selectSingleNode("query")).setText(query); //$NON-NLS-1$
      if (lookupRuleElement != null && !source.equals("mql")) {
        Element queryElement = (Element)lookupRuleElement.selectSingleNode("component-definition/query"); //$NON-NLS-1$
        queryElement.clearContent(); //$NON-NLS-1$
        queryElement.addCDATA(query); //$NON-NLS-1$
      } else if (source.equals("mql")) {

        reader = new SAXReader();
        Document queryDocument = reader.read(new ByteArrayInputStream(query.getBytes()));
        
        Element queryElement = (Element)lookupRuleElement.selectSingleNode("component-definition/mql"); //$NON-NLS-1$
        Element componentDefinitionElement = (Element)lookupRuleElement.selectSingleNode("component-definition"); //$NON-NLS-1$
        componentDefinitionElement.remove(queryElement);
        componentDefinitionElement.add(queryDocument.selectSingleNode("/mql"));
      }
      if (source.equals("mdx")) { //$NON-NLS-1$
        String cubeDef = new File(mondrianCubeDef).getName();
        document.selectSingleNode("/action-sequence/resources/catalog/solution-file/location").setText(cubeDef); //$NON-NLS-1$
        Element temp = (Element) lookupRuleElement.selectSingleNode("component-definition/user-id"); //$NON-NLS-1$
//        temp.setText(username);
        temp.detach();
        temp = (Element) lookupRuleElement.selectSingleNode("component-definition/password"); //$NON-NLS-1$
//        temp.setText(password);
        temp.detach();
        temp = (Element) lookupRuleElement.selectSingleNode("component-definition/connection"); //$NON-NLS-1$
//        temp.clearContent();
//        temp.addCDATA(connectInfo);
        temp.detach();
        temp = (Element) lookupRuleElement.selectSingleNode("component-definition/location"); //$NON-NLS-1$
        temp.setText("mondrian");
        Node jndiNode = lookupRuleElement.selectSingleNode("component-definition/jndi"); //$NON-NLS-1$
        jndiNode.setText(jndi);
      } else if (source.equals("sql")){
        Node jndiNode = lookupRuleElement.selectSingleNode("component-definition/jndi"); //$NON-NLS-1$
        jndiNode.setText(jndi);
      }
      FileOutputStream outStream = new FileOutputStream(out);
      OutputFormat format = OutputFormat.createPrettyPrint();
      XMLWriter writer = new XMLWriter(outStream, format);
      writer.write(document);
      writer.close();
    } catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  
  public IRuntimeContext executeKettleSequence(String solutionRootPath, String solutionId, String path, String actionName, IActionCompleteListener acl) throws Exception {
    IRuntimeContext runtimeContext = null;
    try {
      PentahoUtility.startup();
      List messages = new ArrayList();
      // PentahoSystem.systemEntryPoint();
      String instanceId = null;
      SimpleParameterProvider parameterProvider = new SimpleParameterProvider();
      parameterProvider.setParameter("logging-level", "error"); //$NON-NLS-1$ //$NON-NLS-2$
      parameterProvider.setParameter("level", "error"); //$NON-NLS-1$ //$NON-NLS-2$
      StandaloneSession session = new StandaloneSession("Kettle-Session"); //$NON-NLS-1$
      session.setLoggingLevel(ILogger.ERROR);
      ISolutionEngine solutionEngine = PentahoSystem.getSolutionEngineInstance(null);
      solutionEngine.setLoggingLevel(ILogger.ERROR);
      solutionEngine.init(session);
      String baseUrl = ""; //$NON-NLS-1$  
      HashMap parameterProviderMap = new HashMap();
      parameterProviderMap.put("request", parameterProvider); //$NON-NLS-1$ 
      IPentahoUrlFactory urlFactory = new SimpleUrlFactory(baseUrl);
      SimpleOutputHandler outputHandler = null;
      solutionEngine.setSession(session);
      runtimeContext = solutionEngine.execute(solutionId, path, actionName, "JFreeReport Wizard:Kettle", false, true, instanceId, false, parameterProviderMap, outputHandler, (acl == null ? this : acl), urlFactory, messages); //$NON-NLS-1$
    } finally {
      // PentahoSystem.systemExitPoint();
      try {
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      try {
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return runtimeContext;
  }
  
  
  public File executeActionSequence(String solutionRootPath, String solutionId, String path, String actionName, String outputType, IPentahoResultSet resultSet, IActionCompleteListener acl) throws Exception {
    OutputStream outputStream = null;
    File generatedReportFile = null;
    try {
      PentahoUtility.startup();
      generatedReportFile = File.createTempFile("JFreeDesigner", "." + outputType); //$NON-NLS-1$ //$NON-NLS-2$
      List messages = new ArrayList();
      // PentahoSystem.systemEntryPoint();
      String instanceId = null;
      SimpleParameterProvider parameterProvider = new SimpleParameterProvider();
      if (resultSet != null) {
        parameterProvider.setParameter("result-set", resultSet); //$NON-NLS-1$
      }
      parameterProvider.setParameter("type", outputType); //$NON-NLS-1$
      parameterProvider.setParameter("logging-level", "error"); //$NON-NLS-1$ //$NON-NLS-2$
      parameterProvider.setParameter("level", "error"); //$NON-NLS-1$ //$NON-NLS-2$
      StandaloneSession session = new StandaloneSession("JFreeReport-Session"); //$NON-NLS-1$
      session.setLoggingLevel(ILogger.ERROR);
      ISolutionEngine solutionEngine = PentahoSystem.getSolutionEngineInstance(null);
      solutionEngine.setLoggingLevel(ILogger.ERROR);
      solutionEngine.init(session);
      String baseUrl = ""; //$NON-NLS-1$  
      HashMap parameterProviderMap = new HashMap();
      parameterProviderMap.put("request", parameterProvider); //$NON-NLS-1$ 
      IPentahoUrlFactory urlFactory = new SimpleUrlFactory(baseUrl);
      SimpleOutputHandler outputHandler = null;
      outputStream = new FileOutputStream(generatedReportFile);
      if (outputStream != null) {
        outputHandler = new SimpleOutputHandler(outputStream, false);
      }
      solutionEngine.setSession(session);
      solutionEngine.execute(solutionId, path, actionName, "JFreeReport Designer", false, true, instanceId, false, parameterProviderMap, outputHandler, (acl == null ? this : acl), urlFactory, messages); //$NON-NLS-1$
    } finally {
      // PentahoSystem.systemExitPoint();
      try {
        outputStream.flush();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      try {
        outputStream.close();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return generatedReportFile;
  }

  public void actionComplete(IRuntimeContext runtimeContext) {
  }
}
