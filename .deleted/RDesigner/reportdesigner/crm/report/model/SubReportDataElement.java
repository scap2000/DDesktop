package org.pentaho.reportdesigner.crm.report.model;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.ReportDesignerUtils;
import org.pentaho.reportdesigner.crm.report.ReportDesignerWindows;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.commands.ReportFileView;
import org.pentaho.reportdesigner.crm.report.connection.ColumnInfo;
import org.pentaho.reportdesigner.crm.report.datasetplugin.ColumnInfoTableModel;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MultiDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.Query;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportVisitor;
import org.pentaho.reportdesigner.crm.report.util.ReportElementUtilities;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 20.10.2005
 * Time: 15:40:13
 */
public class SubReportDataElement extends ReportElement
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(SubReportDataElement.class.getName());

    @Nullable
    private ArrayList<ColumnInfo> columnInfos;


    @Nullable
    private File currentMainReport;
    @NotNull
    private JTable infoTable;


    public SubReportDataElement()
    {
        infoTable = new JTable();
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /*Object newParent = */
        reportVisitor.visit(parent, this);
    }


    @Nullable
    public File getCurrentMainReport()
    {
        return currentMainReport;
    }


    public void setCurrentMainReport(@Nullable File currentMainReport)
    {
        this.currentMainReport = currentMainReport;

        loadColumnInfos();
    }


    @NotNull
    public JComponent getInfoComponent()
    {
        loadColumnInfos();

        ArrayList<ColumnInfo> columnInfos = this.columnInfos;

        @NonNls
        FormLayout formLayout = new FormLayout("2dlu, pref, 4dlu, 0dlu:grow, 2dlu",
                                               "4dlu, pref, 4dlu, pref, 4dlu, fill:default:grow, 2dlu");
        JPanel infoPanel = new JPanel(formLayout);
        @NonNls
        CellConstraints cc = new CellConstraints();

        JLabel mainReportLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "SubReportDataElement.MainReportLabel"));

        File report = getCurrentMainReport();
        JLabel mainReportPathLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "SubReportDataElement.NotSelected"));
        if (report != null)
        {
            mainReportPathLabel.setText(report.getName());
        }

        infoPanel.add(mainReportLabel, cc.xy(2, 2));
        infoPanel.add(mainReportPathLabel, cc.xy(4, 2));

        if (columnInfos != null)
        {
            infoTable = new JTable(new ColumnInfoTableModel(columnInfos));
            infoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            initDragAndDrop(infoTable, columnInfos);

            infoPanel.add(new JScrollPane(infoTable), cc.xyw(2, 6, 3, "fill, fill"));
        }
        else
        {
            infoPanel.add(new JLabel(TranslationManager.getInstance().getTranslation("R", "SubReportDataElement.CouldNotLoadColumns")), cc.xyw(2, 6, 3, "fill, fill"));
        }

        return infoPanel;
    }


    private void loadColumnInfos()
    {
        Report mainReport = null;

        File reportFile = getCurrentMainReport();

        ReportDialog dialog = null;
        if (reportFile != null)
        {
            dialog = ReportDesignerUtils.getOpenReportDialog(reportFile);

            try
            {
                mainReport = ReportDesignerUtils.openReport(reportFile);
            }
            catch (Exception e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "SubReportDataElement.getInfoComponent ", e);
            }
        }
        if (dialog != null)
        {
            mainReport = dialog.getReport();
        }

        Report sr = getReport();
        if (sr instanceof SubReport)
        {
            ReportDialog subReportDialog = ReportDesignerWindows.getInstance().getReportDialog(sr);
            if (mainReport != null && subReportDialog != null)
            {
                SubReport subReport = (SubReport) sr;
                String query = getCurrentQuery(mainReport, subReport, subReportDialog);

                if (query != null)
                {
                    DataSetsReportElement reportElement = mainReport.getDataSetsReportElement();
                    ArrayList<ReportElement> elementArrayList = reportElement.getChildren();
                    for (ReportElement element : elementArrayList)
                    {
                        if (element instanceof MultiDataSetReportElement)
                        {
                            MultiDataSetReportElement dataSetReportElement = (MultiDataSetReportElement) element;
                            ArrayList<Query> queries = dataSetReportElement.getQueries();
                            if (!queries.isEmpty() && query.equals(queries.get(0).getQueryName()))
                            {
                                ArrayList<ColumnInfo> columnInfos = dataSetReportElement.getColumnInfos();
                                this.columnInfos = columnInfos;
                                infoTable.setModel(new ColumnInfoTableModel(columnInfos));
                                return;
                            }
                        }
                    }
                }
            }
        }
        this.columnInfos = new ArrayList<ColumnInfo>();
        infoTable.setModel(new ColumnInfoTableModel(columnInfos));
    }


    @Nullable
    private String getCurrentQuery(@NotNull Report mainReport, @NotNull SubReport subReport, @NotNull ReportDialog subReportDialog)
    {
        File currentReportFile = subReportDialog.getCurrentReportFile();
        if (currentReportFile != null)
        {
            FilePath filePath = new FilePath(currentReportFile.getAbsolutePath());
            SubReportElement subReportElement = ReportElementUtilities.findSubReportElement(mainReport, filePath);
            if (subReportElement != null)
            {
                String query = subReportElement.getQuery();
                if (query != null && !"".equals(query.trim()))
                {
                    return query;
                }
            }
        }
        return subReport.getQuery();
    }


    private void initDragAndDrop(@NotNull final JTable table, @NotNull final ArrayList<ColumnInfo> columnInfos)
    {
        @NonNls
        final DataFlavor dataFlavorLibraryItems = new DataFlavor("application/x-icore-reportelement;class=" + ReportElement.class.getName(), "ReportElement " + ReportDialogConstants.UNSELECTED);

        table.setTransferHandler(new TransferHandler()
        {
            @NotNull
            protected Transferable createTransferable(@NotNull JComponent c)
            {
                TextFieldReportElement textFieldReportElement = ReportElementInfoFactory.getInstance().getTextFieldReportElementInfo().createReportElement();
                int selectedRow = table.getSelectedRow();
                ColumnInfo columnInfo = columnInfos.get(selectedRow);
                textFieldReportElement.setFieldName(columnInfo.getColumnName());
                final ReportElement reportElement = textFieldReportElement;

                return new Transferable()
                {
                    @NotNull
                    public DataFlavor[] getTransferDataFlavors()
                    {
                        return new DataFlavor[]{dataFlavorLibraryItems};
                    }


                    public boolean isDataFlavorSupported(@NotNull DataFlavor flavor)
                    {
                        return dataFlavorLibraryItems.equals(flavor);
                    }


                    @NotNull
                    public Object getTransferData(@NotNull DataFlavor flavor) throws UnsupportedFlavorException
                    {
                        if (dataFlavorLibraryItems.equals(flavor))
                        {
                            return reportElement;
                        }
                        else
                        {
                            throw new UnsupportedFlavorException(flavor);
                        }
                    }
                };
            }


            public int getSourceActions(@NotNull JComponent c)
            {
                return DnDConstants.ACTION_COPY;
            }
        });

        table.setDragEnabled(true);

    }


    public boolean canConfigure()
    {
        return true;
    }


    public void showConfigurationComponent(@NotNull ReportDialog parent)
    {
        File f = getCurrentMainReport();

        JFileChooser fileChooser;
        if (f != null && f.canRead())
        {
            fileChooser = new JFileChooser(f);
            fileChooser.setSelectedFile(f);
        }
        else
        {
            fileChooser = new JFileChooser();
        }

        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileFilter()
        {
            public boolean accept(@NotNull File f)
            {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(ReportDialogConstants.REPORT_FILE_ENDING);
            }


            @NotNull
            public String getDescription()
            {
                return TranslationManager.getInstance().getTranslation("R", "FileChooser.ReportFiles.Description");
            }
        });

        fileChooser.setFileView(new ReportFileView());

        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            setCurrentMainReport(fileChooser.getSelectedFile());

            loadColumnInfos();
        }
    }


    @NotNull
    public Collection<String> getDefinedFields()
    {
        ArrayList<ColumnInfo> columnInfos = this.columnInfos;
        if (columnInfos != null)
        {
            HashSet<String> hashSet = new HashSet<String>();
            for (ColumnInfo ci : columnInfos)
            {
                hashSet.add(ci.getColumnName());
            }
            return hashSet;
        }
        return Collections.emptySet();
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        if (currentMainReport != null)
        {
            xmlWriter.writeProperty(PropertyKeys.CURRENT_MAINREPORT, currentMainReport.getAbsolutePath());
        }
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.CURRENT_MAINREPORT.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            currentMainReport = new File(XMLUtils.readProperty(PropertyKeys.CURRENT_MAINREPORT, node));
        }
    }
}