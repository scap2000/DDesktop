/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */
package org.pentaho.reportdesigner.crm.report.reportexporter.jfreereport;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.table.DefaultTableModel;

import org.jetbrains.annotations.NotNull;
import org.pentaho.actionsequence.dom.ActionOutput;
import org.pentaho.actionsequence.dom.ActionSequenceDocument;
import org.pentaho.actionsequence.dom.ActionSequenceInput;
import org.pentaho.actionsequence.dom.ActionSequenceOutput;
import org.pentaho.actionsequence.dom.actions.JFreeReportAction;
import org.pentaho.actionsequence.dom.actions.MQLAction;
import org.pentaho.actionsequence.dom.actions.MdxConnectionAction;
import org.pentaho.actionsequence.dom.actions.MdxQueryAction;
import org.pentaho.actionsequence.dom.actions.SecureFilterAction;
import org.pentaho.actionsequence.dom.actions.SqlQueryAction;
import org.pentaho.actionsequence.dom.actions.XQueryAction;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MultiDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.Query;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;

/**
 * User: Martin Date: 04.08.2006 Time: 11:14:14
 */
@SuppressWarnings( { "HardCodedStringLiteral" })
public class XActionHelper {

  private XActionHelper() {
  }

  /**
   * convert ${NAME} to ${PREPAREDLATER:NAME}
   * 
   * @param text
   *          report designer syntax
   * @return action sequence syntax
   */
  private static String convertProperties(String text) {
    StringBuffer buf = new StringBuffer(text.length() + 200);
    Pattern pattern = Pattern.compile("\\$\\{([^${}]+)\\}");
    Matcher matcher = pattern.matcher(text);
    while (matcher.find()) {
      String varName = matcher.group(1);
      matcher.appendReplacement(buf, "{PREPARELATER:" + varName + "}");
    }
    matcher.appendTail(buf);
    return buf.toString();
  }

  /**
   * setup the action sequence
   * 
   * @param reportTitle
   * @param reportDescription
   * @param type
   * @param jFreeReportFileName
   * @return
   */
  private static ActionSequenceDocument initActionSequenceDoc(String reportTitle, String reportDescription, String type, String jFreeReportFileName) {
    ActionSequenceDocument actionSequenceDocument = new ActionSequenceDocument();
    actionSequenceDocument.setTitle(reportTitle);
    actionSequenceDocument.setVersion("1");
    actionSequenceDocument.setAuthor("Report Designer");
    actionSequenceDocument.setDescription(reportDescription);
    actionSequenceDocument.setIconLocation("PentahoReporting.png");
    actionSequenceDocument.setHelp("Choose Report Output Format.");
    actionSequenceDocument.setResultType("report");
    ActionSequenceInput outputType = actionSequenceDocument.createInput("outputType", ActionSequenceDocument.STRING_TYPE);
    outputType.setDefaultValue(type);
    outputType.addSource(ActionSequenceDocument.REQUEST_INPUT_SOURCE, "type");
    ActionSequenceInput outputTypeList = actionSequenceDocument.createInput("outputTypeList", ActionSequenceDocument.PROPERTY_MAP_LIST_TYPE);
    outputTypeList.addSource(ActionSequenceDocument.RUNTIME_INPUT_SOURCE, "outputTypeList");
    DefaultTableModel defaultTableModel = new DefaultTableModel();
    defaultTableModel.addColumn("report-output-desc");
    defaultTableModel.addColumn("report-output-type-id");
    defaultTableModel.addRow(new String[] { "PDF", "pdf" });
    defaultTableModel.addRow(new String[] { "Excel", "xls" });
    defaultTableModel.addRow(new String[] { "Word", "rtf" });
    defaultTableModel.addRow(new String[] { "Web Page", "html" });
    defaultTableModel.addRow(new String[] { "Comma Separated Value", "csv" });
    outputTypeList.setDefaultValue(defaultTableModel, true);
    actionSequenceDocument.createResource("report-definition", "solution-file", jFreeReportFileName, "text/xml");
    SecureFilterAction secureFilterAction = new SecureFilterAction();
    actionSequenceDocument.add(secureFilterAction);
    secureFilterAction.setDescription("Prompt for Report Export Type");
    secureFilterAction.addInputParam("outputTypeList", "property-map-list");
    secureFilterAction.addInputParam("outputType", "string");
    secureFilterAction.setComponentDefinitionAttribute("selections/outputType", "style", "radio");
    secureFilterAction.setComponentDefinition("selections/outputType/title", "Choose Report Format");
    secureFilterAction.setComponentDefinition("selections/outputType/filter", "outputTypeList");
    secureFilterAction.setComponentDefinitionAttribute("selections/outputType/filter", "value-col-name", "report-output-type-id");
    secureFilterAction.setComponentDefinitionAttribute("selections/outputType/filter", "display-col-name", "report-output-desc");
    return actionSequenceDocument;
  }

  /**
   * complete the processing of the action sequence
   * 
   * @param actionSequenceDocument
   * @param mainResultSet
   * @param queries
   */
  private static void finishProcessing(ActionSequenceDocument actionSequenceDocument, ActionOutput mainResultSet, ArrayList<Query> queries) {
    ActionSequenceInput outputType = actionSequenceDocument.getInput("outputType");
    JFreeReportAction jfreeReportAction = new JFreeReportAction();
    actionSequenceDocument.add(jfreeReportAction);
    jfreeReportAction.setDescription("report");
    jfreeReportAction.setOutputTypeParam(outputType);
    jfreeReportAction.setDataParam(mainResultSet);
    jfreeReportAction.setOutputReportName("content");
    for (int i = 1; i < queries.size(); i++) {
      Query query = queries.get(i);
      jfreeReportAction.setInputParam(query.getQueryName(), query.getQueryName());
    }
    ActionSequenceOutput output = actionSequenceDocument.createOutput("content", ActionSequenceDocument.CONTENT_TYPE);
    output.addDestination(ActionSequenceDocument.RESPONSE_OUTPUT_DESTINATION, "content");
  }

  @NotNull
  public static String getXActionFile(@NotNull
  String xactionFileName, @NotNull
  String publishSolutionPath, @NotNull
  String reportTitle, @NotNull
  String reportDescription, @NotNull
  String type, @NotNull
  String jFreeReportFileName, boolean useJNDI, DataSetsReportElement dataSetsReportElement) {
    ActionSequenceDocument actionSequenceDocument = initActionSequenceDoc(reportTitle, reportDescription, type, jFreeReportFileName);
    ArrayList<MultiDataSetReportElement> dataSets = new ArrayList<MultiDataSetReportElement>();
    ArrayList<Query> queries = new ArrayList<Query>();
    // add the "default" query first
    for (ReportElement reportElement : dataSetsReportElement.getChildren()) {
      if (reportElement instanceof MultiDataSetReportElement) {
        MultiDataSetReportElement multiDataSetReportElement = (MultiDataSetReportElement) reportElement;
        Query query = multiDataSetReportElement.getQueries().get(0);
        if (query.getQueryName().equalsIgnoreCase("default")) {
          dataSets.add(multiDataSetReportElement);
          queries.add(query);
        }
      }
    }
    // now add the rest of the queries (not default)
    for (ReportElement reportElement : dataSetsReportElement.getChildren()) {
      if (reportElement instanceof MultiDataSetReportElement) {
        MultiDataSetReportElement multiDataSetReportElement = (MultiDataSetReportElement) reportElement;
        Query query = multiDataSetReportElement.getQueries().get(0);
        if (!query.getQueryName().equalsIgnoreCase("default")) {
          dataSets.add(multiDataSetReportElement);
          queries.add(query);
        }
      }
    }

    ActionOutput mainActionOutput = null;
    for (int i = 0; i < dataSets.size(); i++) {
      MultiDataSetReportElement dataSet = dataSets.get(i);
      Query query = dataSet.getQueries().get(0);
      // setup sql connection component
      if (dataSet.getConnectionType() == MultiDataSetReportElement.ConnectionType.JNDI) {
        //
        // handle JNDI based connections
        //
        if (dataSet.isUseMondrianCubeDefinition()) {
          //
          // handle mondrian
          //
          String mondrianCubeDefinitionSolutionPath = (new File(dataSet.getMondrianCubeDefinitionFile())).getName();
          actionSequenceDocument.createResource("catalog" + i, "solution-file", mondrianCubeDefinitionSolutionPath, "text/xml");
          // add mdx connection component
          MdxConnectionAction mdxConnectionAction = new MdxConnectionAction();
          actionSequenceDocument.add(mdxConnectionAction);
          mdxConnectionAction.setOutputConnectionName("conn" + i);
          mdxConnectionAction.setMdxConnectionString(mondrianCubeDefinitionSolutionPath);
          mdxConnectionAction.addResourceParam("catalog", "catalog" + i);
          mdxConnectionAction.setLocation("mondrian");
          if (useJNDI) {
            mdxConnectionAction.setJndi(dataSet.getSelectedJNDIDataSource().getJndiName());
          } else {
            mdxConnectionAction.setDriver(dataSet.getSelectedJNDIDataSource().getDriverClass());
            mdxConnectionAction.setUserId(dataSet.getSelectedJNDIDataSource().getUsername());
            mdxConnectionAction.setPassword(dataSet.getSelectedJNDIDataSource().getPassword());
            mdxConnectionAction.setConnection(dataSet.getSelectedJNDIDataSource().getConnectionString());
          }
          // add mdx query component
          MdxQueryAction mainMdxLookupRule = new MdxQueryAction();
          actionSequenceDocument.add(mainMdxLookupRule);
          mainMdxLookupRule.setMdxConnectionParam(mdxConnectionAction.getOutputConnectionParam());
          if (query.getQueryName().equals("default")) {
            mainActionOutput = mainMdxLookupRule.getOutputResultSetParam();
            mainMdxLookupRule.setQuery(query.getQuery());
          } else {
            mainMdxLookupRule.setOutputPreparedStatementName(query.getQueryName());
            String preparedSubquery = convertProperties(query.getQuery());
            mainMdxLookupRule.setQuery(preparedSubquery);
          }
        } else {
          //
          // handle sql
          //
          SqlQueryAction sqlLookupRule = new SqlQueryAction();
          actionSequenceDocument.add(sqlLookupRule);
          if (useJNDI) {
            sqlLookupRule.setJndi(dataSet.getSelectedJNDIDataSource().getJndiName());
          } else {
            sqlLookupRule.setDriver(dataSet.getSelectedJNDIDataSource().getDriverClass());
            sqlLookupRule.setUserId(dataSet.getSelectedJNDIDataSource().getUsername());
            sqlLookupRule.setPassword(dataSet.getSelectedJNDIDataSource().getPassword());
            sqlLookupRule.setDbUrl(dataSet.getSelectedJNDIDataSource().getConnectionString());
          }
          // if the dataset name is "default" it will override the existing "default" dataset
          if (query.getQueryName().equals("default")) {
            mainActionOutput = sqlLookupRule.getOutputResultSetParam();
            sqlLookupRule.setQuery(query.getQuery());
          } else {
            sqlLookupRule.setOutputPreparedStatementName(query.getQueryName());
            String preparedSubquery = convertProperties(query.getQuery());
            sqlLookupRule.setQuery(preparedSubquery);
          }
        }
      } else if (dataSet.getConnectionType() == MultiDataSetReportElement.ConnectionType.XQuery) {
        //
        // handle xquery
        //
        File xQueryDataFile = new File(dataSet.getXQueryDataFile());
        actionSequenceDocument.createResource("document" + i, "solution-file", xQueryDataFile.getName(), "text/xml");
        XQueryAction xqueryAction = new XQueryAction();
        actionSequenceDocument.add(xqueryAction);
        xqueryAction.setOutputResultSetName("list");
        xqueryAction.addResourceParam("document", "document" + i);
        if (query.getQueryName().equals("default")) {
          mainActionOutput = xqueryAction.getOutputResultSetParam();
          xqueryAction.setQuery(query.getQuery());
          xqueryAction.setInputValue("live", "false");
        } else {
          xqueryAction.setOutputPreparedStatementName(query.getQueryName());
          // need to replace ${NAME} with {PREPARELATER:NAME}
          String preparedSubquery = convertProperties(query.getQuery());
          xqueryAction.setQuery(preparedSubquery);
        }
      } else if (dataSet.getConnectionType() == MultiDataSetReportElement.ConnectionType.MQL) {
        //
        // handle MQL
        //
        MQLAction mqlAction = new MQLAction();
        actionSequenceDocument.add(mqlAction);
        mqlAction.setOutputResultSetName("list");
        if (query.getQueryName().equals("default")) {
          mainActionOutput = mqlAction.getOutputResultSetParam();
          String mqlquery = query.getQuery().replaceAll("domain_id.*domain_id", "domain_id>" + publishSolutionPath + "</domain_id");
          mqlAction.setQuery(mqlquery);
        } else {
          // out not supported!
          // mqlAction.setOutputPreparedStatementName(query.getQueryName());
          // need to replace ${NAME} with {PREPARELATER:NAME}
          // String preparedSubquery = convertProperties(query.getQuery());
          mqlAction.setQuery(query.getQuery());
        }
      }
    }
    finishProcessing(actionSequenceDocument, mainActionOutput, queries);
    return actionSequenceDocument.toString();
  }
}
