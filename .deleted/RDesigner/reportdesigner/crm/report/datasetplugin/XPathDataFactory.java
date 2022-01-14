package org.pentaho.reportdesigner.crm.report.datasetplugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.DataFactory;
import org.jfree.report.DataRow;
import org.jfree.report.ReportDataFactoryException;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.XPathTableModel;

import javax.swing.table.TableModel;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URL;

/**
 * User: Martin
 * Date: 06.03.2007
 * Time: 17:52:39
 */
public class XPathDataFactory implements ExtendedDataFactory
{
    @NotNull
    private String queryName;
    @NotNull
    private URL xmlResource;
    @NotNull
    private String xPathExpression;


    public XPathDataFactory(@NotNull String queryName, @NotNull URL xmlResource, @NotNull String xPathExpression)
    {
        //noinspection ConstantConditions
        if (queryName == null)
        {
            throw new IllegalArgumentException("queryName must not be null");
        }
        //noinspection ConstantConditions
        if (xmlResource == null)
        {
            throw new IllegalArgumentException("xmlResource must not be null");
        }
        //noinspection ConstantConditions
        if (xPathExpression == null)
        {
            throw new IllegalArgumentException("xPathExpression must not be null");
        }
        this.queryName = queryName;
        this.xmlResource = xmlResource;
        this.xPathExpression = xPathExpression;
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
    public TableModel queryData(@Nullable final String query, @Nullable final DataRow parameters) throws ReportDataFactoryException
    {
        XPathTableModel xPathTableModel;
        try
        {
            xPathTableModel = new XPathTableModel(xmlResource, xPathExpression, parameters, Integer.MAX_VALUE);
        }
        catch (IOException e)
        {
            throw new ReportDataFactoryException(e.getMessage(), e);
        }
        catch (XPathExpressionException e)
        {
            throw new ReportDataFactoryException(e.getMessage(), e);
        }
        return xPathTableModel;
    }


}
