package org.pentaho.reportdesigner.crm.report.datasetplugin;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.DataFactory;
import org.jfree.report.DataRow;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.modules.misc.datafactory.sql.SQLReportDataFactory;

import javax.swing.table.TableModel;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 06.03.2007
 * Time: 17:43:31
 */
public class MultiDataFactory implements ExtendedDataFactory
{
    @NotNull
    private ArrayList<ExtendedDataFactory> dataFactories;


    public MultiDataFactory()
    {
        dataFactories = new ArrayList<ExtendedDataFactory>();
    }


    public void close()
    {
        for (DataFactory dataFactory : dataFactories)
        {
            dataFactory.close();
        }
    }


    public void addDataFactory(@NotNull final SQLReportDataFactory dataFactory)
    {
        dataFactories.add(new ExtendedDataFactory()
        {
            public boolean canExecuteQuery(@Nullable String query)
            {
                String[] queryNames = dataFactory.getQueryNames();
                for (String queryName : queryNames)
                {
                    if (query != null && query.equals(queryName))
                    {
                        return true;
                    }
                }
                return false;
            }


            @NotNull
            public TableModel queryData(@Nullable String query, @Nullable DataRow parameters) throws ReportDataFactoryException
            {
                return dataFactory.queryData(query, parameters);
            }


            @NotNull
            public DataFactory derive() throws ReportDataFactoryException
            {
                return dataFactory.derive();
            }


            public void open()
            {
                dataFactory.open();
            }


            public void close()
            {
                dataFactory.close();
            }
        });
    }


    public void addExtendedDataFactory(@NotNull ExtendedDataFactory extendedDataFactory)
    {
        dataFactories.add(extendedDataFactory);
    }


    @NotNull
    public DataFactory derive() throws ReportDataFactoryException
    {
        return this;
    }


    public void open()
    {
    }


    @Nullable
    public TableModel queryData(@Nullable final String query, @Nullable final DataRow parameters) throws ReportDataFactoryException
    {
        for (ExtendedDataFactory dataFactory : dataFactories)
        {
            if (dataFactory.canExecuteQuery(query))
            {
                return dataFactory.queryData(query, parameters);
            }
        }
        return null;
    }


    public boolean canExecuteQuery(@Nullable String query)
    {
        for (ExtendedDataFactory dataFactory : dataFactories)
        {
            if (dataFactory.canExecuteQuery(query))
            {
                return true;
            }
        }
        return false;
    }
}
