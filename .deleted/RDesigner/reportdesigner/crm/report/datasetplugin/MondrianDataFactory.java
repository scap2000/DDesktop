package org.pentaho.reportdesigner.crm.report.datasetplugin;

import java.io.File;

import javax.swing.table.TableModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.DataFactory;
import org.jfree.report.DataRow;
import org.jfree.report.ReportDataFactoryException;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.JNDISource;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MondrianTableModel;

/**
 * User: mdamour Date: 06.03.2007 Time: 17:52:39
 */
public class MondrianDataFactory implements ExtendedDataFactory {
  @NotNull
  private String queryName;
  @NotNull
  private String mondrianCubeDefinitionFile;
  @NotNull
  private String query;
  @NotNull
  JNDISource selectedJNDIDataSource;

  public MondrianDataFactory(JNDISource selectedJNDIDataSource, @NotNull
  String queryName, @NotNull
  String cubeFile, @NotNull
  String query) {
    // noinspection ConstantConditions
    if (queryName == null) {
      throw new IllegalArgumentException("queryName must not be null");
    }
    // noinspection ConstantConditions
    if (cubeFile == null) {
      throw new IllegalArgumentException("xmiFile must not be null");
    }
    // noinspection ConstantConditions
    if (query == null) {
      throw new IllegalArgumentException("query must not be null");
    }
    if (selectedJNDIDataSource == null) {
      throw new IllegalArgumentException("selectedJNDIDataSource must not be null");
    }
    this.queryName = queryName;
    this.mondrianCubeDefinitionFile = cubeFile;
    this.query = query;
    this.selectedJNDIDataSource = selectedJNDIDataSource;
  }

  public boolean canExecuteQuery(@Nullable
  String query) {
    return queryName.equals(query);
  }

  public void close() {
  }

  @NotNull
  public DataFactory derive() throws ReportDataFactoryException {
    return this;
  }

  public void open() {
  }

  @NotNull
  public TableModel queryData(@Nullable
  final String inQuery, @Nullable
  final DataRow parameters) throws ReportDataFactoryException {
    MondrianTableModel mdxTableModel;
    try {
      mdxTableModel = new MondrianTableModel(selectedJNDIDataSource, new File(mondrianCubeDefinitionFile).toURI().toURL(), query, Integer.MAX_VALUE);
    } catch (Exception e) {
      throw new ReportDataFactoryException(e.getMessage(), e);
    }
    return mdxTableModel;
  }

}
