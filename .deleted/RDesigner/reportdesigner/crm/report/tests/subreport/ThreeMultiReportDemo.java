package org.pentaho.reportdesigner.crm.report.tests.subreport;

import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.misc.tablemodel.JoiningTableModel;
import org.jfree.report.modules.parser.base.ReportGenerator;
import org.jfree.util.ObjectUtilities;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.net.URL;

@SuppressWarnings({"ALL"})
public class ThreeMultiReportDemo
{
    private final TableModel data;


    public ThreeMultiReportDemo()
    {
        this.data = createJoinedTableModel();
    }


    public JFreeReport createReport() throws Exception
    {
        ReportGenerator generator = ReportGenerator.getInstance();
        final JFreeReport report = generator.parseReport(getReportDefinitionSource());

        report.setData(this.data);
        return report;
    }


    public URL getReportDefinitionSource()
    {
        return ObjectUtilities.getResourceRelative
                ("joined3-report.xml", ThreeMultiReportDemo.class);
    }


    private TableModel createFruitTableModel()
    {
        final String[] names = new String[]{"Id Number", "Cat", "Fruit"};
        final Object[][] data = new Object[][]{
                {"I1", "A", "Apple"},
                {"I2", "A", "Orange"},
                {"I3", "B", "Water melon"},
                {"I4", "B", "Strawberry"},
        };
        return new DefaultTableModel(data, names);
    }


    private TableModel createAnimalTableModel()
    {
        final String[] names = new String[]{"Id Number", "Cat", "Animal"};
        final Object[][] data = new Object[][]{
                {"A1", "A", "Ape"},
                {"A2", "A", "Dog"},
                {"A3", "B", "Frog"},
                {"A4", "B", "Snake"},
                {"A5", "B", "Aligator"},
        };
        return new DefaultTableModel(data, names);
    }


    private TableModel createColorTableModel()
    {
        final String[] names = new String[]{"Number", "Group", "Color"};
        final Object[][] data = new Object[][]{
                {new Integer(1), "X", "Red"},
                {new Integer(2), "X", "Green"},
                {new Integer(3), "Y", "Yellow"},
                {new Integer(4), "Y", "Blue"},
                {new Integer(5), "Z", "Orange"},
                {new Integer(6), "Z", "White"},
        };
        return new DefaultTableModel(data, names);
    }


    private TableModel createJoinedTableModel()
    {
        final JoiningTableModel jtm = new JoiningTableModel();
        jtm.addTableModel("Color", createColorTableModel());
        jtm.addTableModel("Fruit", createFruitTableModel());
        jtm.addTableModel("Animal", createAnimalTableModel());
        return jtm;
    }


    public static void main(final String[] args) throws Exception
    {
        JFreeReportBoot.getInstance().start();

        ThreeMultiReportDemo demoHandler = new ThreeMultiReportDemo();

        PreviewFrame preview = new PreviewFrame(demoHandler.createReport());
        preview.pack();
        preview.setVisible(true);
    }
}
