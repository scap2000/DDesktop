package org.pentaho.reportdesigner.crm.report.properties.editors;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jfree.report.function.Expression;
import org.pentaho.plugin.jfreereport.reportcharts.AreaChartExpression;
import org.pentaho.plugin.jfreereport.reportcharts.BarChartExpression;
import org.pentaho.plugin.jfreereport.reportcharts.CategorySetCollectorFunction;
import org.pentaho.plugin.jfreereport.reportcharts.LineChartExpression;
import org.pentaho.plugin.jfreereport.reportcharts.MultiPieChartExpression;
import org.pentaho.plugin.jfreereport.reportcharts.PieChartExpression;
import org.pentaho.plugin.jfreereport.reportcharts.PieSetCollectorFunction;
import org.pentaho.plugin.jfreereport.reportcharts.RingChartExpression;
import org.pentaho.plugin.jfreereport.reportcharts.WaterfallChartExpressions;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportElementSelectionModel;
import org.pentaho.reportdesigner.lib.client.util.WindowUtils;
import org.pentaho.reportdesigner.crm.report.model.ChartReportElement;
import org.pentaho.reportdesigner.crm.report.model.ChartType;
import org.pentaho.reportdesigner.crm.report.model.ReportFunctionElement;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.properties.PropertyEditorPanel;
import org.pentaho.reportdesigner.lib.client.components.CenterPanelDialog;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 11.01.2006
 * Time: 11:02:54
 */
public class ChartEditor
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(ChartEditor.class.getName());


    private ChartEditor()
    {
    }


    public static void showChartEditor(@NotNull JComponent parent, @NotNull String title, @NotNull final ChartReportElement chartReportElement, @NotNull ReportDialog reportDialog)
    {
        //noinspection ConstantConditions
        if (parent == null)
        {
            throw new IllegalArgumentException("parent must not be null");
        }
        //noinspection ConstantConditions
        if (title == null)
        {
            throw new IllegalArgumentException("title must not be null");
        }
        //noinspection ConstantConditions
        if (chartReportElement == null)
        {
            throw new IllegalArgumentException("chartReportElement must not be null");
        }
        //noinspection ConstantConditions
        if (reportDialog == null)
        {
            throw new IllegalArgumentException("reportDialog must not be null");
        }

        final CenterPanelDialog centerPanelDialog;
        Window windowAncestor = SwingUtilities.getWindowAncestor(parent);

        if (windowAncestor instanceof Dialog)
        {
            centerPanelDialog = new CenterPanelDialog((Dialog) windowAncestor, title, true);
        }
        else
        {
            centerPanelDialog = new CenterPanelDialog((Frame) windowAncestor, title, true);
        }

        @NonNls
        final FormLayout formLayout = new FormLayout("4dlu, pref, 10dlu, default, 4dlu, fill:default:grow, 4dlu", "4dlu, default, 4dlu, fill:default:grow, 4dlu");
        @NonNls
        CellConstraints cc = new CellConstraints();
        final JPanel centerPanel = new JPanel(formLayout);

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        ButtonGroup buttonGroup = new ButtonGroup();

        JToggleButton toggleButtonArea = new JToggleButton(TranslationManager.getInstance().getTranslation("R", "Chart.Area"));
        JToggleButton toggleButtonBar = new JToggleButton(TranslationManager.getInstance().getTranslation("R", "Chart.Bar"));
        JToggleButton toggleButtonLine = new JToggleButton(TranslationManager.getInstance().getTranslation("R", "Chart.Line"));
        JToggleButton toggleButtonPie = new JToggleButton(TranslationManager.getInstance().getTranslation("R", "Chart.Pie"));
        JToggleButton toggleButtonRing = new JToggleButton(TranslationManager.getInstance().getTranslation("R", "Chart.Ring"));
        JToggleButton toggleButtonMultiPie = new JToggleButton(TranslationManager.getInstance().getTranslation("R", "Chart.MultiPie"));
        JToggleButton toggleButtonWaterfall = new JToggleButton(TranslationManager.getInstance().getTranslation("R", "Chart.Waterfall"));

        buttonPanel.add(toggleButtonArea);
        buttonPanel.add(toggleButtonBar);
        buttonPanel.add(toggleButtonLine);
        buttonPanel.add(toggleButtonPie);
        buttonPanel.add(toggleButtonRing);
        buttonPanel.add(toggleButtonMultiPie);
        buttonPanel.add(toggleButtonWaterfall);

        toggleButtonArea.setSelected(chartReportElement.getChartType() == ChartType.AREA);
        toggleButtonBar.setSelected(chartReportElement.getChartType() == ChartType.BAR);
        toggleButtonLine.setSelected(chartReportElement.getChartType() == ChartType.LINE);
        toggleButtonPie.setSelected(chartReportElement.getChartType() == ChartType.PIE);
        toggleButtonRing.setSelected(chartReportElement.getChartType() == ChartType.RING);
        toggleButtonMultiPie.setSelected(chartReportElement.getChartType() == ChartType.MULTI_PIE);
        toggleButtonWaterfall.setSelected(chartReportElement.getChartType() == ChartType.WATERFALL);

        ReportElementSelectionModel reportElementSelectionModel = reportDialog.getReportElementModel();
        if (reportElementSelectionModel == null)
        {
            //noinspection ThrowableInstanceNeverThrown
            UncaughtExcpetionsModel.getInstance().addException(new Throwable("reportElementSelectionModel must not be null"));//NON-NLS
            return;
        }
        final PropertyEditorPanel propertyEditorPanelData = new PropertyEditorPanel(reportDialog, reportElementSelectionModel);
        final PropertyEditorPanel propertyEditorPanelChart = new PropertyEditorPanel(reportDialog, reportElementSelectionModel);

        final PropertyChangeListener propertyChangeListener = new PropertyChangeListener()
        {
            public void propertyChange(@NotNull PropertyChangeEvent evt)
            {
                try
                {
                    Method declaredMethod = chartReportElement.getChartFunction().getClass().getMethod("setDataSource", String.class);//NON-NLS
                    declaredMethod.invoke(chartReportElement.getChartFunction(), evt.getNewValue());
                }
                catch (Exception ex)
                {
                    if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ChartEditor.propertyChange ", ex);
                }
            }
        };

        toggleButtonArea.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                Undo undo = chartReportElement.getUndo();
                if (undo != null)
                {
                    undo.startTransaction("charttype");//NON-NLS

                    chartReportElement.getDataCollectorFunction().removePropertyChangeListener(PropertyKeys.NAME, propertyChangeListener);
                    chartReportElement.setDataCollectorFunction(getDataFunction(chartReportElement.getChartType(), ChartType.AREA, chartReportElement.getDataCollectorFunction()));
                    chartReportElement.getDataCollectorFunction().addPropertyChangeListener(PropertyKeys.NAME, propertyChangeListener);

                    chartReportElement.setChartFunction(getChartFunction(chartReportElement, ExpressionRegistry.getInstance().createWrapperInstance(new AreaChartExpression())));

                    chartReportElement.setChartType(ChartType.AREA);
                    undo.endTransaction();

                    propertyEditorPanelData.setBeans(new Object[]{chartReportElement.getDataCollectorFunction()});
                    propertyEditorPanelChart.setBeans(new Object[]{chartReportElement.getChartFunction()});
                }
            }
        });

        toggleButtonBar.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                Undo undo = chartReportElement.getUndo();
                if (undo != null)
                {
                    undo.startTransaction("charttype");//NON-NLS

                    chartReportElement.getDataCollectorFunction().removePropertyChangeListener(PropertyKeys.NAME, propertyChangeListener);
                    chartReportElement.setDataCollectorFunction(getDataFunction(chartReportElement.getChartType(), ChartType.BAR, chartReportElement.getDataCollectorFunction()));
                    chartReportElement.getDataCollectorFunction().addPropertyChangeListener(PropertyKeys.NAME, propertyChangeListener);

                    chartReportElement.setChartFunction(getChartFunction(chartReportElement, ExpressionRegistry.getInstance().createWrapperInstance(new BarChartExpression())));
                    chartReportElement.setChartType(ChartType.BAR);
                    undo.endTransaction();

                    propertyEditorPanelData.setBeans(new Object[]{chartReportElement.getDataCollectorFunction()});
                    propertyEditorPanelChart.setBeans(new Object[]{chartReportElement.getChartFunction()});
                }
            }
        });

        toggleButtonLine.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                Undo undo = chartReportElement.getUndo();
                if (undo != null)
                {
                    undo.startTransaction("charttype");//NON-NLS

                    chartReportElement.getDataCollectorFunction().removePropertyChangeListener(PropertyKeys.NAME, propertyChangeListener);
                    chartReportElement.setDataCollectorFunction(getDataFunction(chartReportElement.getChartType(), ChartType.LINE, chartReportElement.getDataCollectorFunction()));
                    chartReportElement.getDataCollectorFunction().addPropertyChangeListener(PropertyKeys.NAME, propertyChangeListener);

                    chartReportElement.setChartFunction(getChartFunction(chartReportElement, ExpressionRegistry.getInstance().createWrapperInstance(new LineChartExpression())));
                    chartReportElement.setChartType(ChartType.LINE);
                    undo.endTransaction();

                    propertyEditorPanelData.setBeans(new Object[]{chartReportElement.getDataCollectorFunction()});
                    propertyEditorPanelChart.setBeans(new Object[]{chartReportElement.getChartFunction()});
                }
            }
        });

        toggleButtonPie.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                Undo undo = chartReportElement.getUndo();
                if (undo != null)
                {
                    undo.startTransaction("charttype");//NON-NLS

                    chartReportElement.getDataCollectorFunction().removePropertyChangeListener(PropertyKeys.NAME, propertyChangeListener);
                    chartReportElement.setDataCollectorFunction(getDataFunction(chartReportElement.getChartType(), ChartType.PIE, chartReportElement.getDataCollectorFunction()));
                    chartReportElement.getDataCollectorFunction().addPropertyChangeListener(PropertyKeys.NAME, propertyChangeListener);

                    chartReportElement.setChartFunction(getChartFunction(chartReportElement, ExpressionRegistry.getInstance().createWrapperInstance(new PieChartExpression())));
                    chartReportElement.setChartType(ChartType.PIE);
                    undo.endTransaction();

                    propertyEditorPanelData.setBeans(new Object[]{chartReportElement.getDataCollectorFunction()});
                    propertyEditorPanelChart.setBeans(new Object[]{chartReportElement.getChartFunction()});
                }
            }
        });

        toggleButtonRing.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                Undo undo = chartReportElement.getUndo();
                if (undo != null)
                {
                    undo.startTransaction("charttype");//NON-NLS

                    chartReportElement.getDataCollectorFunction().removePropertyChangeListener(PropertyKeys.NAME, propertyChangeListener);
                    chartReportElement.setDataCollectorFunction(getDataFunction(chartReportElement.getChartType(), ChartType.RING, chartReportElement.getDataCollectorFunction()));
                    chartReportElement.getDataCollectorFunction().addPropertyChangeListener(PropertyKeys.NAME, propertyChangeListener);

                    chartReportElement.setChartFunction(getChartFunction(chartReportElement, ExpressionRegistry.getInstance().createWrapperInstance(new RingChartExpression())));
                    chartReportElement.setChartType(ChartType.RING);
                    undo.endTransaction();

                    propertyEditorPanelData.setBeans(new Object[]{chartReportElement.getDataCollectorFunction()});
                    propertyEditorPanelChart.setBeans(new Object[]{chartReportElement.getChartFunction()});
                }
            }
        });

        toggleButtonMultiPie.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                Undo undo = chartReportElement.getUndo();
                if (undo != null)
                {
                    undo.startTransaction("charttype");//NON-NLS

                    chartReportElement.getDataCollectorFunction().removePropertyChangeListener(PropertyKeys.NAME, propertyChangeListener);
                    chartReportElement.setDataCollectorFunction(getDataFunction(chartReportElement.getChartType(), ChartType.MULTI_PIE, chartReportElement.getDataCollectorFunction()));
                    chartReportElement.getDataCollectorFunction().addPropertyChangeListener(PropertyKeys.NAME, propertyChangeListener);

                    chartReportElement.setChartFunction(getChartFunction(chartReportElement, ExpressionRegistry.getInstance().createWrapperInstance(new MultiPieChartExpression())));
                    chartReportElement.setChartType(ChartType.MULTI_PIE);
                    undo.endTransaction();

                    propertyEditorPanelData.setBeans(new Object[]{chartReportElement.getDataCollectorFunction()});
                    propertyEditorPanelChart.setBeans(new Object[]{chartReportElement.getChartFunction()});
                }
            }
        });

        toggleButtonWaterfall.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                Undo undo = chartReportElement.getUndo();
                if (undo != null)
                {
                    undo.startTransaction("charttype");//NON-NLS

                    chartReportElement.getDataCollectorFunction().removePropertyChangeListener(PropertyKeys.NAME, propertyChangeListener);
                    chartReportElement.setDataCollectorFunction(getDataFunction(chartReportElement.getChartType(), ChartType.WATERFALL, chartReportElement.getDataCollectorFunction()));
                    chartReportElement.getDataCollectorFunction().addPropertyChangeListener(PropertyKeys.NAME, propertyChangeListener);

                    chartReportElement.setChartFunction(getChartFunction(chartReportElement, ExpressionRegistry.getInstance().createWrapperInstance(new WaterfallChartExpressions())));
                    chartReportElement.setChartType(ChartType.WATERFALL);
                    undo.endTransaction();

                    propertyEditorPanelData.setBeans(new Object[]{chartReportElement.getDataCollectorFunction()});
                    propertyEditorPanelChart.setBeans(new Object[]{chartReportElement.getChartFunction()});
                }
            }
        });

        buttonGroup.add(toggleButtonArea);
        buttonGroup.add(toggleButtonBar);
        buttonGroup.add(toggleButtonLine);
        buttonGroup.add(toggleButtonPie);
        buttonGroup.add(toggleButtonRing);
        buttonGroup.add(toggleButtonMultiPie);
        buttonGroup.add(toggleButtonWaterfall);

        centerPanel.add(buttonPanel, cc.xywh(2, 2, 1, 3, "center, top"));

        JLabel nameLabel = new JLabel(TranslationManager.getInstance().getTranslation("R", "Property.name"));
        final JTextField nameTextField = new JTextField(chartReportElement.getName());
        centerPanel.add(nameLabel, cc.xy(4, 2));
        centerPanel.add(nameTextField, cc.xy(6, 2));

        propertyEditorPanelData.setBeans(new Object[]{chartReportElement.getDataCollectorFunction()});
        propertyEditorPanelChart.setBeans(new Object[]{chartReportElement.getChartFunction()});

        chartReportElement.getDataCollectorFunction().addPropertyChangeListener(PropertyKeys.NAME, propertyChangeListener);

        JPanel editorPanel = new JPanel(new GridLayout(1, 0, 5, 0));
        editorPanel.add(new JScrollPane(propertyEditorPanelChart));
        editorPanel.add(new JScrollPane(propertyEditorPanelData));

        centerPanel.add(editorPanel, cc.xyw(4, 4, 3));

        JButton okButton = new JButton(TranslationManager.getInstance().getTranslation("R", "Button.ok"));
        okButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                chartReportElement.setName(nameTextField.getText());
                centerPanelDialog.dispose();
            }
        });

        centerPanelDialog.setButtons(okButton, okButton, okButton);

        centerPanelDialog.setCenterPanel(centerPanel);
        centerPanelDialog.pack();
        centerPanelDialog.setSize(800, 600);
        WindowUtils.setLocationRelativeTo(centerPanelDialog, parent);
        centerPanelDialog.setVisible(true);
    }


    @NotNull
    private static ReportFunctionElement getChartFunction(@NotNull ChartReportElement chartReportElement, @NotNull ReportFunctionElement wrapperInstance)
    {
        copyAsMuchAsPossible(chartReportElement.getChartFunction(), wrapperInstance);
        return wrapperInstance;
    }


    @NotNull
    private static ReportFunctionElement getDataFunction(@NotNull ChartType oldChartType, @NotNull ChartType newChartType, @NotNull ReportFunctionElement currentFunctionElement)
    {
        if (oldChartType.isPieSet() && newChartType.isCategorySet())
        {
            CategorySetCollectorFunction categorySetCollectorFunction = new CategorySetCollectorFunction();
            categorySetCollectorFunction.setName(currentFunctionElement.getName());
            ReportFunctionElement element = ExpressionRegistry.getInstance().createWrapperInstance(categorySetCollectorFunction);
            copyAsMuchAsPossible(currentFunctionElement, element);
            return element;
        }

        if (oldChartType.isCategorySet() && newChartType.isPieSet())
        {
            PieSetCollectorFunction pieSetCollectorFunction = new PieSetCollectorFunction();
            pieSetCollectorFunction.setName(currentFunctionElement.getName());
            ReportFunctionElement element = ExpressionRegistry.getInstance().createWrapperInstance(pieSetCollectorFunction);
            copyAsMuchAsPossible(currentFunctionElement, element);
            return element;
        }

        return currentFunctionElement;
    }


    private static void copyAsMuchAsPossible(@NotNull ReportFunctionElement reportElementSource, @NotNull ReportFunctionElement reportElementTarget)
    {
        try
        {
            Expression jFreeReportExpressionInstance = ExpressionRegistry.getInstance().createJFreeReportExpressionInstance(reportElementSource);
            ExpressionRegistry.getInstance().fillWrapper(reportElementTarget, jFreeReportExpressionInstance);
        }
        catch (Exception e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "ChartEditor.copyAsMuchAsPossible ", e);
        }
    }

}
