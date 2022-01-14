package org.pentaho.reportdesigner.crm.report.datasetplugin;

import javax.swing.table.TableModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.DataFactory;
import org.jfree.report.DataRow;
import org.jfree.report.ReportDataFactoryException;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MQLTableModel;

/**
 * User: mdamour
 * Date: 06.03.2007
 * Time: 17:52:39
 */
public class MQLDataFactory implements ExtendedDataFactory
{
    @NotNull
    private String queryName;
    @NotNull
    private String xmiFile;
    @NotNull
    private String query;


    public MQLDataFactory(@NotNull String queryName, @NotNull String xmiFile, @NotNull String query)
    {
        //noinspection ConstantConditions
        if (queryName == null)
        {
            throw new IllegalArgumentException("queryName must not be null");
        }
        //noinspection ConstantConditions
        if (xmiFile == null)
        {
            throw new IllegalArgumentException("xmiFile must not be null");
        }
        //noinspection ConstantConditions
        if (query == null)
        {
            throw new IllegalArgumentException("query must not be null");
        }
        this.queryName = queryName;
        this.xmiFile = xmiFile;
        this.query = query;
    }


    public boolean canExecuteQuery(@Nullable String query)
    {
        return queryName.equals(query);
    }


    public void close()
    {
    }


    @NotNull
    public DataFactory derive() throws ReportDataFactoryException
    {
        return this;
    }


    public void open()
    {
    }


    @NotNull
    public TableModel queryData(@Nullable final String inQuery, @Nullable final DataRow parameters) throws ReportDataFactoryException
    {
        MQLTableModel mqlTableModel;
        try
        {
          mqlTableModel = new MQLTableModel(xmiFile, query, Integer.MAX_VALUE);
        }
        catch (Exception e)
        {
            throw new ReportDataFactoryException(e.getMessage(), e);
        }
        return mqlTableModel;
    }


}
