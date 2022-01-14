package org.pentaho.reportdesigner.crm.report.reportexporter.jfreereport;

import org.jfree.report.DataFactory;
import org.jfree.report.DataRow;
import org.jfree.report.ReportDataFactoryException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * User: Martin
 * Date: 02.08.2007
 * Time: 07:56:57
 */
public class NullDataFactory implements DataFactory
{
    @NotNull
    public TableModel queryData(@Nullable String query, @Nullable DataRow parameters) throws ReportDataFactoryException
    {
        return new DefaultTableModel();
    }


    @NotNull
    public DataFactory derive() throws ReportDataFactoryException
    {
        return this;
    }


    public void open()
    {
    }


    public void close()
    {
    }
}
