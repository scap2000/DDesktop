package org.pentaho.reportdesigner.crm.report.tests.subreport;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * User: Martin
 * Date: 09.01.2007
 * Time: 17:00:38
 */
@SuppressWarnings({"ALL"})
public class TestDataFactory
{
    public static TableModel createMainTableModel()
    {
        System.out.println("TestDataFactory.createTableModel");
        return new DefaultTableModel(new String[][]{{"1.1", "1.2"}, {"2.1", "2.2"}}, new String[]{"c1", "c2"});
    }


    public static TableModel createSubReportTableModel(final String param1)
    {
        System.out.println("TestDataFactory.createTableModel");
        System.out.println("param1 = " + param1);

        return new DefaultTableModel(new String[][]{{"1.1:" + param1, "1.2:" + param1}, {"2.1:" + param1, "2.2:" + param1}}, new String[]{"t1", "t2"})
        {
            public int getRowCount()
            {
                return 20;
            }


            public Object getValueAt(int row, int column)
            {
                return row + "." + column + ":" + param1;
            }
        };
    }


    public static TableModel createSubReport2TableModel(final String param1)
    {
        System.out.println("TestDataFactory.createTableModel");
        System.out.println("param1 = " + param1);

        return new DefaultTableModel(new String[][]{{"1.1:" + param1, "1.2:" + param1}, {"2.1:" + param1, "2.2:" + param1}}, new String[]{"s2t1", "s2t2"})
        {
            public int getRowCount()
            {
                return 20;
            }


            public Object getValueAt(int row, int column)
            {
                return row + "." + column + ":" + param1;
            }
        };
    }
}
