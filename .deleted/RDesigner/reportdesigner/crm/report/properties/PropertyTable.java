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
package org.pentaho.reportdesigner.crm.report.properties;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.components.PaletteColorChooser;
import org.pentaho.reportdesigner.crm.report.configuration.ReportConfiguration;
import org.pentaho.reportdesigner.crm.report.model.BorderDefinition;
import org.pentaho.reportdesigner.crm.report.model.ElementBorderDefinition;
import org.pentaho.reportdesigner.crm.report.model.ElementPadding;
import org.pentaho.reportdesigner.crm.report.model.FilePath;
import org.pentaho.reportdesigner.crm.report.model.Formula;
import org.pentaho.reportdesigner.crm.report.model.GroupingPropertyDescriptor;
import org.pentaho.reportdesigner.crm.report.model.LineDefinition;
import org.pentaho.reportdesigner.crm.report.model.LineDirection;
import org.pentaho.reportdesigner.crm.report.model.PageDefinition;
import org.pentaho.reportdesigner.crm.report.model.PageFormatPreset;
import org.pentaho.reportdesigner.crm.report.model.PageOrientation;
import org.pentaho.reportdesigner.crm.report.model.RowBandingDefinition;
import org.pentaho.reportdesigner.crm.report.model.StyleExpressions;
import org.pentaho.reportdesigner.crm.report.model.SubReportParameters;
import org.pentaho.reportdesigner.crm.report.model.TextReportElementHorizontalAlignment;
import org.pentaho.reportdesigner.crm.report.model.TextReportElementVerticalAlignment;
import org.pentaho.reportdesigner.crm.report.model.layoutmanager.ReportLayoutManager;
import org.pentaho.reportdesigner.crm.report.properties.editors.BorderDefinitionChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorCheckBox;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJLabelWithEllipsisBorderDefinition;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJLabelWithEllipsisColorArray;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJLabelWithEllipsisElementBorderDefinition;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJLabelWithEllipsisFont;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJLabelWithEllipsisFormula;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJLabelWithEllipsisNumberArray;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJLabelWithEllipsisPageDefinition;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJLabelWithEllipsisReportConfiguration;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJLabelWithEllipsisRowBandingDefinition;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJLabelWithEllipsisStringArray;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJLabelWithEllipsisStringColorArray;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJLabelWithEllipsisStyleExpressions;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJLabelWithEllipsisSubReportParameters;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldAreaWithEllipsis;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldWithEllipsis;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldWithEllipsisColor;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldWithEllipsisDate;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldWithEllipsisDecimalFormat;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldWithEllipsisDoubleDimension;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldWithEllipsisElement;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldWithEllipsisField;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldWithEllipsisFilePath;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldWithEllipsisPadding;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldWithEllipsisPoint2D;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldWithEllipsisQuery;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldWithEllipsisSimpleDateFormat;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldWithEllipsisStringColor;
import org.pentaho.reportdesigner.crm.report.properties.editors.CellEditorJTextFieldWithEllipsisURL;
import org.pentaho.reportdesigner.crm.report.properties.editors.ColorArrayChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.CommonCellEditor;
import org.pentaho.reportdesigner.crm.report.properties.editors.DoubleValueChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.ElementBorderDefinitionChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.ElementNameChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.FieldChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.FontChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.FormulaChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.NumberArrayChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.PaddingChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.PageDefinitionChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.QueryChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.ReportConfigurationChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.RowBandingDefinitionChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.StringArrayChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.StringChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.StringColorArrayChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.StyleExpressionsChooser;
import org.pentaho.reportdesigner.crm.report.properties.editors.SubReportParametersChooser;
import org.pentaho.reportdesigner.crm.report.properties.renderers.BooleanTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.BorderDefinitionTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.ColorArrayTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.ColorTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.DateTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.DecimalFormatTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.Dimension2DTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.DoubleTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.ElementBorderDefinitionTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.FilePathTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.FloatTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.FontTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.FormulaTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.GroupCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.HorizontalAlignmentTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.IntegerTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.LineDirectionTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.LocaleTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.LongTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.NumberArrayTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.PaddingTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.PageDefinitionTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.Point2DTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.PropertyDescriptorCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.ReportConfigurationCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.ReportLayoutManagerTypeTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.RowBandingDefinitionTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.SimpleDateFormatTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.StringArrayTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.StringColorTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.StringTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.StyleExpressionsTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.SubReportParametersTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.TimeZoneTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.URLTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.properties.renderers.VerticalAlignmentTableCellRenderer;
import org.pentaho.reportdesigner.crm.report.util.ColorHelper;
import org.pentaho.reportdesigner.lib.client.components.TextComponentHelper;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.DoubleDimension;
import org.pentaho.reportdesigner.lib.client.util.UndoHelper;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 21.10.2005
 * Time: 08:15:34
 */
public class PropertyTable extends JTable
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(PropertyTable.class.getName());

    @NotNull
    private PropertyTableModel propertyTableModel;

    @NotNull
    private GroupCellRenderer groupCellRenderer;

    @NotNull
    private PropertyDescriptorCellRenderer propertyDescriptorCellRenderer;

    @NotNull
    private DecimalFormatTableCellRenderer decimalFormatTableCellRenderer;
    @NotNull
    private SimpleDateFormatTableCellRenderer simpleDateFormatTableCellRenderer;
    @NotNull
    private Point2DTableCellRenderer point2DTableCellRenderer;
    @NotNull
    private Dimension2DTableCellRenderer dimension2DTableCellRenderer;
    @NotNull
    private PaddingTableCellRenderer paddingTableCellRenderer;
    @NotNull
    private BooleanTableCellRenderer booleanTableCellRenderer;
    @NotNull
    private FontTableCellRenderer fontTableCellRenderer;
    @NotNull
    private BorderDefinitionTableCellRenderer borderDefinitionTableCellRenderer;
    @NotNull
    private ElementBorderDefinitionTableCellRenderer elementBorderDefinitionTableCellRenderer;
    @NotNull
    private RowBandingDefinitionTableCellRenderer rowBandingDefinitionTableCellRenderer;
    @NotNull
    private StyleExpressionsTableCellRenderer styleExpressionsTableCellRenderer;
    @NotNull
    private PageDefinitionTableCellRenderer pageDefinitionTableCellRenderer;
    @NotNull
    private StringTableCellRenderer stringTableCellRenderer;
    @NotNull
    private URLTableCellRenderer urlTableCellRenderer;
    @NotNull
    private ColorTableCellRenderer colorTableCellRenderer;
    @NotNull
    private StringColorTableCellRenderer stringColorTableCellRenderer;
    @NotNull
    private IntegerTableCellRenderer integerTableCellRenderer;
    @NotNull
    private LongTableCellRenderer longTableCellRenderer;
    @NotNull
    private FloatTableCellRenderer floatTableCellRenderer;
    @NotNull
    private HorizontalAlignmentTableCellRenderer horizontalAlignmentTableCellRenderer;
    @NotNull
    private VerticalAlignmentTableCellRenderer verticalAlignmentTableCellRenderer;
    @NotNull
    private LineDirectionTableCellRenderer lineDirectionTableCellRenderer;
    @NotNull
    private ReportLayoutManagerTypeTableCellRenderer reportLayoutManagerTypeTableCellRenderer;
    @NotNull
    private StringArrayTableCellRenderer stringArrayTableCellRenderer;
    @NotNull
    private ColorArrayTableCellRenderer colorArrayTableCellRenderer;
    @NotNull
    private NumberArrayTableCellRenderer numberArrayTableCellRenderer;
    @NotNull
    private DoubleTableCellRenderer doubleTableCellRenderer;
    @NotNull
    private LocaleTableCellRenderer localeTableCellRenderer;
    @NotNull
    private TimeZoneTableCellRenderer timeZoneTableCellRenderer;
    @NotNull
    private DateTableCellRenderer dateTableCellRenderer;
    @NotNull
    private FormulaTableCellRenderer formulaTableCellRenderer;
    @NotNull
    private FilePathTableCellRenderer filePathTableCellRenderer;
    @NotNull
    private SubReportParametersTableCellRenderer subReportParametersTableCellRenderer;
    @NotNull
    private ReportConfigurationCellRenderer reportConfigurationCellRenderer;


    @NotNull
    private CommonCellEditor booleanCellEditor;
    @NotNull
    private CommonCellEditor stringCellEditor;
    @NotNull
    private CommonCellEditor urlCellEditor;
    @NotNull
    private CommonCellEditor integerCellEditor;
    @NotNull
    private CommonCellEditor longCellEditor;
    @NotNull
    private CommonCellEditor floatCellEditor;
    @NotNull
    private CommonCellEditor doubleCellEditor;

    @NotNull
    private CommonCellEditor point2DCellEditor;
    @NotNull
    private CommonCellEditor decimalFormatCellEditor;
    @NotNull
    private CommonCellEditor queryCellEditor;
    @NotNull
    private CommonCellEditor fieldCellEditor;
    @NotNull
    private CommonCellEditor elementCellEditor;
    @NotNull
    private CommonCellEditor simpleDateFormatCellEditor;
    @NotNull
    private CommonCellEditor dimension2DCellEditor;
    @NotNull
    private CommonCellEditor paddingCellEditor;
    @NotNull
    private CommonCellEditor dateCellEditor;
    @NotNull
    private CommonCellEditor colorCellEditor;
    @NotNull
    private CommonCellEditor stringColorCellEditor;
    @NotNull
    private CommonCellEditor fontCellEditor;
    @NotNull
    private CommonCellEditor borderDefinitionCellEditor;
    @NotNull
    private CommonCellEditor elementBorderDefinitionCellEditor;
    @NotNull
    private CommonCellEditor reportConfigurationCellEditor;
    @NotNull
    private CommonCellEditor rowBandingDefinitionCellEditor;
    @NotNull
    private CommonCellEditor styleExpressionsCellEditor;
    @NotNull
    private CommonCellEditor pageDefinitionCellEditor;
    @NotNull
    private CommonCellEditor horizontalAlignmentCellEditor;
    @NotNull
    private CommonCellEditor verticalAlignmentCellEditor;
    @NotNull
    private CommonCellEditor lineDirectionCellEditor;
    @NotNull
    private CommonCellEditor reportLayoutManagerTypeCellEditor;
    @NotNull
    private CommonCellEditor stringArrayCellEditor;
    @NotNull
    private CommonCellEditor numberArrayCellEditor;
    @NotNull
    private CommonCellEditor fieldArrayCellEditor;
    @NotNull
    private CommonCellEditor elementArrayCellEditor;
    @NotNull
    private CommonCellEditor colorArrayCellEditor;
    @NotNull
    private CommonCellEditor stringColorArrayCellEditor;
    @NotNull
    private CommonCellEditor localeCellEditor;
    @NotNull
    private CommonCellEditor timeZoneCellEditor;
    @NotNull
    private CommonCellEditor formulaCellEditor;
    @NotNull
    private CommonCellEditor filePathCellEditor;
    @NotNull
    private CommonCellEditor regularFilePathCellEditor;
    @NotNull
    private CommonCellEditor subReportParametersCellEditor;


    public PropertyTable(@NotNull final PropertyEditorPanel propertyEditorPanel, @NotNull PropertyTableModel propertyTableModel)
    {
        super(propertyTableModel);

        putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);//NON-NLS

        initColumnResizing();

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.propertyTableModel = propertyTableModel;

        groupCellRenderer = new GroupCellRenderer();

        propertyDescriptorCellRenderer = new PropertyDescriptorCellRenderer(propertyEditorPanel);

        decimalFormatTableCellRenderer = new DecimalFormatTableCellRenderer();
        simpleDateFormatTableCellRenderer = new SimpleDateFormatTableCellRenderer();
        point2DTableCellRenderer = new Point2DTableCellRenderer();
        dimension2DTableCellRenderer = new Dimension2DTableCellRenderer();
        paddingTableCellRenderer = new PaddingTableCellRenderer();
        booleanTableCellRenderer = new BooleanTableCellRenderer();
        fontTableCellRenderer = new FontTableCellRenderer();
        borderDefinitionTableCellRenderer = new BorderDefinitionTableCellRenderer();
        elementBorderDefinitionTableCellRenderer = new ElementBorderDefinitionTableCellRenderer();
        rowBandingDefinitionTableCellRenderer = new RowBandingDefinitionTableCellRenderer();
        styleExpressionsTableCellRenderer = new StyleExpressionsTableCellRenderer();
        pageDefinitionTableCellRenderer = new PageDefinitionTableCellRenderer();
        stringTableCellRenderer = new StringTableCellRenderer();
        urlTableCellRenderer = new URLTableCellRenderer();
        colorTableCellRenderer = new ColorTableCellRenderer();
        stringColorTableCellRenderer = new StringColorTableCellRenderer();
        integerTableCellRenderer = new IntegerTableCellRenderer();
        longTableCellRenderer = new LongTableCellRenderer();
        floatTableCellRenderer = new FloatTableCellRenderer();
        horizontalAlignmentTableCellRenderer = new HorizontalAlignmentTableCellRenderer();
        verticalAlignmentTableCellRenderer = new VerticalAlignmentTableCellRenderer();
        lineDirectionTableCellRenderer = new LineDirectionTableCellRenderer();
        reportLayoutManagerTypeTableCellRenderer = new ReportLayoutManagerTypeTableCellRenderer();
        stringArrayTableCellRenderer = new StringArrayTableCellRenderer();
        colorArrayTableCellRenderer = new ColorArrayTableCellRenderer();
        numberArrayTableCellRenderer = new NumberArrayTableCellRenderer();
        doubleTableCellRenderer = new DoubleTableCellRenderer();
        localeTableCellRenderer = new LocaleTableCellRenderer();
        timeZoneTableCellRenderer = new TimeZoneTableCellRenderer();
        dateTableCellRenderer = new DateTableCellRenderer();
        formulaTableCellRenderer = new FormulaTableCellRenderer();
        filePathTableCellRenderer = new FilePathTableCellRenderer();
        subReportParametersTableCellRenderer = new SubReportParametersTableCellRenderer();
        reportConfigurationCellRenderer = new ReportConfigurationCellRenderer();

        booleanCellEditor = new CommonCellEditor(new CellEditorCheckBox());

        JTextField urlTextField = new JTextField();
        UndoHelper.installUndoSupport(urlTextField);
        TextComponentHelper.installDefaultPopupMenu(urlTextField);

        final CellEditorJTextFieldAreaWithEllipsis textFieldWithEllipsis = new CellEditorJTextFieldAreaWithEllipsis();
        stringCellEditor = new CommonCellEditor(textFieldWithEllipsis);
        textFieldWithEllipsis.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                String value = textFieldWithEllipsis.getValue();
                String val = StringChooser.showStringArrayChooser(PropertyTable.this, TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Text.Title"), value);
                if (val != null)
                {
                    textFieldWithEllipsis.setValue(val, true);
                }
            }
        });

        JTextField integerTextField = new JTextField();
        UndoHelper.installUndoSupport(integerTextField);
        TextComponentHelper.installDefaultPopupMenu(integerTextField);

        integerCellEditor = new CommonCellEditor(integerTextField, CommonCellEditor.TextFieldType.INTEGER);
        integerCellEditor.setClickCountToStart(1);

        JTextField longTextField = new JTextField();
        UndoHelper.installUndoSupport(longTextField);
        TextComponentHelper.installDefaultPopupMenu(longTextField);

        longCellEditor = new CommonCellEditor(longTextField, CommonCellEditor.TextFieldType.LONG);
        longCellEditor.setClickCountToStart(1);

        JTextField floatTextField = new JTextField();
        UndoHelper.installUndoSupport(floatTextField);
        TextComponentHelper.installDefaultPopupMenu(floatTextField);

        floatCellEditor = new CommonCellEditor(floatTextField, CommonCellEditor.TextFieldType.FLOAT);
        floatCellEditor.setClickCountToStart(1);

        JTextField doubleTextField = new JTextField();
        UndoHelper.installUndoSupport(doubleTextField);
        TextComponentHelper.installDefaultPopupMenu(doubleTextField);

        doubleCellEditor = new CommonCellEditor(doubleTextField, CommonCellEditor.TextFieldType.DOUBLE);
        doubleCellEditor.setClickCountToStart(1);

        JComboBox timeZoneComboBox = new JComboBox();
        TimeZoneDataLoader.fillLocales(timeZoneComboBox);
        timeZoneComboBox.setRenderer(new DefaultListCellRenderer()
        {
            @NotNull
            public Component getListCellRendererComponent(@NotNull JList list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                TimeZone timeZone = (TimeZone) value;
                if (timeZone != null)
                {
                    label.setText(timeZone.getID() + " (" + timeZone.getDisplayName() + ")");
                }
                else
                {
                    label.setText(TranslationManager.getInstance().getTranslation("R", "Property.None"));
                }

                return label;
            }
        });
        timeZoneCellEditor = new CommonCellEditor(timeZoneComboBox);


        final JComboBox localeComboBox = new JComboBox();
        LocaleDataLoader.fillLocales(localeComboBox);
        localeComboBox.setRenderer(new DefaultListCellRenderer()
        {
            @NotNull
            public Component getListCellRendererComponent(@NotNull JList list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Locale)
                {
                    Locale locale = (Locale) value;
                    StringBuilder sb = new StringBuilder(locale.toString());
                    String displayName = locale.getDisplayName();
                    if (displayName != null && displayName.length() > 0)
                    {
                        sb.append(" (");
                        sb.append(displayName);
                        sb.append(")");
                    }
                    label.setText(sb.toString());
                }
                return label;
            }
        });
        localeCellEditor = new CommonCellEditor(localeComboBox);

        JComboBox horizontalAlignmentComboBox = new JComboBox(new TextReportElementHorizontalAlignment[]{TextReportElementHorizontalAlignment.LEFT, TextReportElementHorizontalAlignment.CENTER, TextReportElementHorizontalAlignment.RIGHT});
        horizontalAlignmentComboBox.setRenderer(new DefaultListCellRenderer()
        {
            @NotNull
            public Component getListCellRendererComponent(@NotNull JList list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TextReportElementHorizontalAlignment)
                {
                    TextReportElementHorizontalAlignment text = (TextReportElementHorizontalAlignment) value;
                    label.setText(TranslationManager.getInstance().getTranslation("R", "HorizontalAlignment." + text.toString()));
                }
                return label;
            }
        });
        horizontalAlignmentCellEditor = new CommonCellEditor(horizontalAlignmentComboBox);

        JComboBox verticalAlignmentComboBox = new JComboBox(new TextReportElementVerticalAlignment[]{TextReportElementVerticalAlignment.TOP, TextReportElementVerticalAlignment.MIDDLE, TextReportElementVerticalAlignment.BOTTOM});
        verticalAlignmentComboBox.setRenderer(new DefaultListCellRenderer()
        {
            @NotNull
            public Component getListCellRendererComponent(@NotNull JList list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof TextReportElementVerticalAlignment)
                {
                    TextReportElementVerticalAlignment text = (TextReportElementVerticalAlignment) value;
                    label.setText(TranslationManager.getInstance().getTranslation("R", "VerticalAlignment." + text.toString()));
                }
                return label;
            }
        });
        verticalAlignmentCellEditor = new CommonCellEditor(verticalAlignmentComboBox);

        JComboBox reportLayoutManagerTypeComboBox = new JComboBox(new ReportLayoutManager.Type[]{ReportLayoutManager.Type.STACKED, ReportLayoutManager.Type.NULL});
        reportLayoutManagerTypeComboBox.setRenderer(new DefaultListCellRenderer()
        {
            @NotNull
            public Component getListCellRendererComponent(@NotNull JList list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ReportLayoutManager.Type)
                {
                    ReportLayoutManager.Type type = (ReportLayoutManager.Type) value;
                    label.setText(TranslationManager.getInstance().getTranslation("R", "ReportLayoutManager.Type." + type.toString()));
                }
                return label;
            }
        });
        reportLayoutManagerTypeCellEditor = new CommonCellEditor(reportLayoutManagerTypeComboBox);

        JComboBox lineDirectionComboBox = new JComboBox(new LineDirection[]{LineDirection.HORIZONTAL, LineDirection.VERTICAL});
        lineDirectionComboBox.setRenderer(new DefaultListCellRenderer()
        {
            @NotNull
            public Component getListCellRendererComponent(@NotNull JList list, @Nullable Object value, int index, boolean isSelected, boolean cellHasFocus)
            {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof LineDirection)
                {
                    LineDirection lineDirection = (LineDirection) value;
                    label.setText(TranslationManager.getInstance().getTranslation("R", "LineDirection." + lineDirection.toString()));
                }
                return label;
            }
        });
        lineDirectionCellEditor = new CommonCellEditor(lineDirectionComboBox);


        final CellEditorJTextFieldWithEllipsisPoint2D textFieldWithEllipsisPoint2D = new CellEditorJTextFieldWithEllipsisPoint2D();
        point2DCellEditor = new CommonCellEditor(textFieldWithEllipsisPoint2D);
        textFieldWithEllipsisPoint2D.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                Point2D value = textFieldWithEllipsisPoint2D.getValue();

                DoubleValueChooser.Pair<Double> val = DoubleValueChooser.showValueChooser(PropertyTable.this,
                                                                                          TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Position.Title"),
                                                                                          TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Position.Label1"),
                                                                                          TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Position.Label2"),
                                                                                          value == null ? null : new DoubleValueChooser.Pair<Double>(Double.valueOf(value.getX()), Double.valueOf(value.getY())));
                if (val != null)
                {
                    textFieldWithEllipsisPoint2D.setValue(new Point2D.Double(val.getValue1().doubleValue(), val.getValue2().doubleValue()), true);
                }
            }
        });

        final CellEditorJTextFieldWithEllipsisElement textFieldWithEllipsisElement = new CellEditorJTextFieldWithEllipsisElement();
        elementCellEditor = new CommonCellEditor(textFieldWithEllipsisElement);
        textFieldWithEllipsisElement.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                String value = textFieldWithEllipsisElement.getValue();
                String val = ElementNameChooser.showElementChooser(propertyEditorPanel.getReportDialog().getReport(),
                                                                   PropertyTable.this,
                                                                   TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Element.Title"),
                                                                   value);
                if (val != null)
                {
                    textFieldWithEllipsisElement.setValue(val, true);
                }
            }
        });

        final CellEditorJTextFieldWithEllipsisQuery textFieldWithEllipsisQuery = new CellEditorJTextFieldWithEllipsisQuery();
        queryCellEditor = new CommonCellEditor(textFieldWithEllipsisQuery);
        textFieldWithEllipsisQuery.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                String value = textFieldWithEllipsisQuery.getValue();
                String val = QueryChooser.showQueryChooser(propertyEditorPanel.getReportDialog().getReport(),
                                                           PropertyTable.this,
                                                           TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Query.Title"),
                                                           value);
                if (val != null)
                {
                    textFieldWithEllipsisQuery.setValue(val, true);
                }
            }
        });

        final CellEditorJTextFieldWithEllipsisField textFieldWithEllipsisField = new CellEditorJTextFieldWithEllipsisField();
        fieldCellEditor = new CommonCellEditor(textFieldWithEllipsisField);
        textFieldWithEllipsisField.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                String value = textFieldWithEllipsisField.getValue();
                String val = FieldChooser.showFieldChooser(propertyEditorPanel.getReportDialog().getReport(),
                                                           PropertyTable.this,
                                                           TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Field.Title"),
                                                           value);
                if (val != null)
                {
                    textFieldWithEllipsisField.setValue(val, true);
                }
            }
        });

        final CellEditorJTextFieldWithEllipsisDecimalFormat textFieldWithEllipsisDecimalFormat = new CellEditorJTextFieldWithEllipsisDecimalFormat();
        decimalFormatCellEditor = new CommonCellEditor(textFieldWithEllipsisDecimalFormat);
        textFieldWithEllipsisDecimalFormat.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                DecimalFormat value = textFieldWithEllipsisDecimalFormat.getValue();
                String val = StringChooser.showStringArrayChooser(PropertyTable.this, TranslationManager.getInstance().getTranslation("R", "PropertyEditor.SimpleDateFormat.Title"), value == null ? null : value.toPattern());
                if (val != null)
                {
                    textFieldWithEllipsisDecimalFormat.setValue(new DecimalFormat(val), true);
                }
            }
        });

        final CellEditorJTextFieldWithEllipsisSimpleDateFormat textFieldWithEllipsisSimpleDateFormat = new CellEditorJTextFieldWithEllipsisSimpleDateFormat();
        simpleDateFormatCellEditor = new CommonCellEditor(textFieldWithEllipsisSimpleDateFormat);
        textFieldWithEllipsisSimpleDateFormat.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                SimpleDateFormat value = textFieldWithEllipsisSimpleDateFormat.getValue();
                String val = StringChooser.showStringArrayChooser(PropertyTable.this, TranslationManager.getInstance().getTranslation("R", "PropertyEditor.SimpleDateFormat.Title"), value == null ? null : value.toPattern());
                if (val != null)
                {
                    textFieldWithEllipsisSimpleDateFormat.setValue(new SimpleDateFormat(val), true);
                }
            }
        });

        final CellEditorJTextFieldWithEllipsisPadding textFieldWithEllipsisPadding = new CellEditorJTextFieldWithEllipsisPadding();
        paddingCellEditor = new CommonCellEditor(textFieldWithEllipsisPadding);
        textFieldWithEllipsisPadding.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                ElementPadding value = textFieldWithEllipsisPadding.getValue();
                ElementPadding val = PaddingChooser.showValueChooser(PropertyTable.this,
                                                                     TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Padding.Title"),
                                                                     value);
                if (val != null)
                {
                    textFieldWithEllipsisPadding.setValue(val, true);
                }
            }
        });

        final CellEditorJTextFieldWithEllipsisDoubleDimension textFieldWithEllipsisDimension2D = new CellEditorJTextFieldWithEllipsisDoubleDimension();
        dimension2DCellEditor = new CommonCellEditor(textFieldWithEllipsisDimension2D);
        textFieldWithEllipsisDimension2D.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                DoubleDimension value = textFieldWithEllipsisDimension2D.getValue();
                DoubleValueChooser.Pair<Double> val = DoubleValueChooser.showValueChooser(PropertyTable.this,
                                                                                          TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Size.Title"),
                                                                                          TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Size.Label1"),
                                                                                          TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Size.Label2"),
                                                                                          value == null ? null : new DoubleValueChooser.Pair<Double>(Double.valueOf(value.getWidth()), Double.valueOf(value.getHeight())));
                if (val != null)
                {
                    textFieldWithEllipsisDimension2D.setValue(new DoubleDimension(val.getValue1().doubleValue(), val.getValue2().doubleValue()), true);
                }
            }
        });

        final CellEditorJTextFieldWithEllipsisFilePath textFieldWithEllipsisRegularFilePath = new CellEditorJTextFieldWithEllipsisFilePath();
        regularFilePathCellEditor = new CommonCellEditor(textFieldWithEllipsisRegularFilePath);
        textFieldWithEllipsisRegularFilePath.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");
                FilePath value = textFieldWithEllipsisRegularFilePath.getValue();
                File file = null;
                if (value != null)
                {
                    try
                    {
                        file = new File(value.getPath());
                    }
                    catch (IllegalArgumentException e1)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ", e1);
                    }
                }
                else
                {
                    file = propertyEditorPanel.getReportDialog().getWorkspaceSettings().getFile("PropertyTable.LastSelectedLocalFilePath");
                }
                JFileChooser fileChooser;
                if (file != null)
                {
                    fileChooser = new JFileChooser(file);
                    fileChooser.setSelectedFile(file);
                }
                else
                {
                    fileChooser = new JFileChooser();
                }
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int option = fileChooser.showOpenDialog(PropertyTable.this);
                if (option == JFileChooser.APPROVE_OPTION)
                {
                    File newFile = fileChooser.getSelectedFile();
                    try
                    {
                        textFieldWithEllipsisRegularFilePath.setValue(new FilePath(newFile.getCanonicalPath()), true);
                        propertyEditorPanel.getReportDialog().getWorkspaceSettings().put("PropertyTable.LastSelectedLocalFilePath", newFile.getCanonicalPath());
                    }
                    catch (IOException e1)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ", e1);
                    }
                }
            }
        });

        final CellEditorJTextFieldWithEllipsisFilePath textFieldWithEllipsisFilePath = new CellEditorJTextFieldWithEllipsisFilePath();
        filePathCellEditor = new CommonCellEditor(textFieldWithEllipsisFilePath);
        textFieldWithEllipsisFilePath.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");
                FilePath value = textFieldWithEllipsisFilePath.getValue();
                File file = null;
                if (value != null)
                {
                    try
                    {
                        file = new File(value.getPath());
                    }
                    catch (IllegalArgumentException e1)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ", e1);
                    }
                }
                else
                {
                    file = propertyEditorPanel.getReportDialog().getWorkspaceSettings().getFile("PropertyTable.LastSelectedLocalFilePath");
                }
                JFileChooser fileChooser;
                if (file != null)
                {
                    fileChooser = new JFileChooser(file);
                    fileChooser.setSelectedFile(file);
                }
                else
                {
                    fileChooser = new JFileChooser();
                }
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int option = fileChooser.showOpenDialog(PropertyTable.this);
                if (option == JFileChooser.APPROVE_OPTION)
                {
                    File newFile = fileChooser.getSelectedFile();
                    try
                    {
                        textFieldWithEllipsisFilePath.setValue(new FilePath(newFile.getCanonicalPath()), true);
                        propertyEditorPanel.getReportDialog().getWorkspaceSettings().put("PropertyTable.LastSelectedLocalFilePath", newFile.getCanonicalPath());
                    }
                    catch (IOException e1)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ", e1);
                    }
                }
            }
        });

        final CellEditorJTextFieldWithEllipsisURL textFieldWithEllipsisURL = new CellEditorJTextFieldWithEllipsisURL();
        urlCellEditor = new CommonCellEditor(textFieldWithEllipsisURL);
        textFieldWithEllipsisURL.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");
                URL value = textFieldWithEllipsisURL.getValue();
                File file = null;
                if (value != null)
                {
                    try
                    {
                        file = new File(value.toURI());
                    }
                    catch (URISyntaxException e1)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ", e1);
                    }
                    catch (IllegalArgumentException e1)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ", e1);
                    }
                }
                else
                {
                    file = propertyEditorPanel.getReportDialog().getWorkspaceSettings().getFile("PropertyTable.LastSelectedLocalURL");
                }
                JFileChooser fileChooser;
                if (file != null)
                {
                    fileChooser = new JFileChooser(file);
                    fileChooser.setSelectedFile(file);
                }
                else
                {
                    fileChooser = new JFileChooser();
                }
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                int option = fileChooser.showOpenDialog(PropertyTable.this);
                if (option == JFileChooser.APPROVE_OPTION)
                {
                    File newFile = fileChooser.getSelectedFile();
                    try
                    {
                        textFieldWithEllipsisURL.setValue(newFile.toURI().toURL(), true);
                        propertyEditorPanel.getReportDialog().getWorkspaceSettings().put("PropertyTable.LastSelectedLocalURL", newFile.getCanonicalPath());
                    }
                    catch (MalformedURLException e1)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ", e1);
                    }
                    catch (IOException e1)
                    {
                        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ", e1);
                    }
                }
            }
        });

        final CellEditorJTextFieldWithEllipsisDate textFieldWithEllipsisDate = new CellEditorJTextFieldWithEllipsisDate();
        dateCellEditor = new CommonCellEditor(textFieldWithEllipsisDate);
        textFieldWithEllipsisDate.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");//MARKED already quite ok, perhaps add a full fletched date editor
                textFieldWithEllipsisDate.setValue(new Date(), true);
            }
        });


        final CellEditorJTextFieldWithEllipsisColor textFieldWithEllipsisColor = new CellEditorJTextFieldWithEllipsisColor();
        colorCellEditor = new CommonCellEditor(textFieldWithEllipsisColor);
        textFieldWithEllipsisColor.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                Color col = PaletteColorChooser.showDialog(PropertyTable.this,
                                                           TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Color.Title"),
                                                           textFieldWithEllipsisColor.getValue());
                if (col != null)
                {
                    textFieldWithEllipsisColor.setValue(col, true);
                }
            }
        });

        final CellEditorJTextFieldWithEllipsisStringColor textFieldWithEllipsisStringColor = new CellEditorJTextFieldWithEllipsisStringColor();
        stringColorCellEditor = new CommonCellEditor(textFieldWithEllipsisStringColor);
        textFieldWithEllipsisStringColor.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                Color col = PaletteColorChooser.showDialog(PropertyTable.this,
                                                           TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Color.Title"),
                                                           ColorHelper.lookupColor(textFieldWithEllipsisStringColor.getValue()));
                if (col != null)
                {
                    textFieldWithEllipsisStringColor.setValue("#" + Integer.toHexString(col.getRGB() & 0x00FFFFFF).toUpperCase(), true);
                }
            }
        });

        final CellEditorJLabelWithEllipsisPageDefinition labelWithEllipsisPageDefinition = new CellEditorJLabelWithEllipsisPageDefinition();
        pageDefinitionCellEditor = new CommonCellEditor(labelWithEllipsisPageDefinition);
        labelWithEllipsisPageDefinition.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                PageDefinition oldPageDefinition = labelWithEllipsisPageDefinition.getValue();

                //will not likely be the case, but this guarantees that oldPageDefinition can not be null
                if (oldPageDefinition == null)
                {
                    oldPageDefinition = new PageDefinition(PageFormatPreset.A4, PageOrientation.PORTRAIT, 20, 20, 20, 20);
                }

                PageDefinition newPageDefinition = PageDefinitionChooser.showPageDefinitionChooser(propertyEditorPanel.getReportDialog(),
                                                                                                   PropertyTable.this,
                                                                                                   TranslationManager.getInstance().getTranslation("R", "PropertyEditor.PageDefinition.Title"),
                                                                                                   oldPageDefinition);

                labelWithEllipsisPageDefinition.setValue(newPageDefinition, true);
            }
        });

        final CellEditorJLabelWithEllipsisFont labelWithEllipsisFont = new CellEditorJLabelWithEllipsisFont(getFont());
        fontCellEditor = new CommonCellEditor(labelWithEllipsisFont);
        labelWithEllipsisFont.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                Font font = FontChooser.showFontChooser(PropertyTable.this,
                                                        TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Font.Title"),
                                                        labelWithEllipsisFont.getValue());
                if (font != null)
                {
                    labelWithEllipsisFont.setValue(font, true);
                }
            }
        });

        final CellEditorJLabelWithEllipsisReportConfiguration labelWithEllipsisReportConfiguration = new CellEditorJLabelWithEllipsisReportConfiguration();
        reportConfigurationCellEditor = new CommonCellEditor(labelWithEllipsisReportConfiguration);
        labelWithEllipsisReportConfiguration.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                ReportConfiguration origReportConfiguration = labelWithEllipsisReportConfiguration.getValue();

                if (origReportConfiguration == null)
                {
                    origReportConfiguration = new ReportConfiguration();
                }
                ReportConfiguration reportConfiguration = ReportConfigurationChooser.showReportConfigurationChooser(PropertyTable.this,
                                                                                                                    TranslationManager.getInstance().getTranslation("R", "PropertyEditor.ReportConfiguration.Title"),
                                                                                                                    origReportConfiguration);
                labelWithEllipsisReportConfiguration.setValue(reportConfiguration, true);
            }
        });

        final CellEditorJLabelWithEllipsisElementBorderDefinition labelWithEllipsisElementBorderDefinition = new CellEditorJLabelWithEllipsisElementBorderDefinition();
        elementBorderDefinitionCellEditor = new CommonCellEditor(labelWithEllipsisElementBorderDefinition);
        labelWithEllipsisElementBorderDefinition.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                ElementBorderDefinition origBorderDefinition = labelWithEllipsisElementBorderDefinition.getValue();

                if (origBorderDefinition == null)
                {
                    origBorderDefinition = new ElementBorderDefinition();
                }
                ElementBorderDefinition borderDefinition = ElementBorderDefinitionChooser.showElementBorderDefinitionChooser(PropertyTable.this,
                                                                                                                             TranslationManager.getInstance().getTranslation("R", "PropertyEditor.BorderDefinition.Title"),
                                                                                                                             origBorderDefinition);
                labelWithEllipsisElementBorderDefinition.setValue(borderDefinition, true);
            }
        });

        final CellEditorJLabelWithEllipsisBorderDefinition labelWithEllipsisBorderDefinition = new CellEditorJLabelWithEllipsisBorderDefinition();
        borderDefinitionCellEditor = new CommonCellEditor(labelWithEllipsisBorderDefinition);
        labelWithEllipsisBorderDefinition.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                BorderDefinition origBorderDefinition = labelWithEllipsisBorderDefinition.getValue();

                if (origBorderDefinition == null)
                {
                    origBorderDefinition = new BorderDefinition();
                }
                BorderDefinition borderDefinition = BorderDefinitionChooser.showBorderDefinitionChooser(PropertyTable.this,
                                                                                                        TranslationManager.getInstance().getTranslation("R", "PropertyEditor.BorderDefinition.Title"),
                                                                                                        origBorderDefinition,
                                                                                                        origBorderDefinition instanceof LineDefinition);
                labelWithEllipsisBorderDefinition.setValue(borderDefinition, true);
            }
        });

        final CellEditorJLabelWithEllipsisSubReportParameters labelWithEllipsisSubReportParameters = new CellEditorJLabelWithEllipsisSubReportParameters();
        subReportParametersCellEditor = new CommonCellEditor(labelWithEllipsisSubReportParameters);
        labelWithEllipsisSubReportParameters.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");


                SubReportParameters oldValue = labelWithEllipsisSubReportParameters.getValue();
                //should never be the case
                if (oldValue == null)
                {
                    oldValue = new SubReportParameters();
                }
                SubReportParameters formula = SubReportParametersChooser.showSubReportParametersChooser(PropertyTable.this,
                                                                                                        TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Formula.Title"),
                                                                                                        oldValue);
                labelWithEllipsisSubReportParameters.setValue(formula, true);
            }
        });

        final CellEditorJLabelWithEllipsisFormula labelWithEllipsisFormula = new CellEditorJLabelWithEllipsisFormula();
        formulaCellEditor = new CommonCellEditor(labelWithEllipsisFormula);
        labelWithEllipsisFormula.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");


                Formula oldFormula = labelWithEllipsisFormula.getValue();
                //should never be the case
                if (oldFormula == null)
                {
                    oldFormula = new Formula("");
                }
                Formula formula = FormulaChooser.showFormulaChooser(PropertyTable.this,
                                                                    TranslationManager.getInstance().getTranslation("R", "PropertyEditor.Formula.Title"),
                                                                    oldFormula);
                labelWithEllipsisFormula.setValue(formula, true);
            }
        });


        final CellEditorJLabelWithEllipsisStyleExpressions labelWithEllipsisStyleExpressions = new CellEditorJLabelWithEllipsisStyleExpressions();
        styleExpressionsCellEditor = new CommonCellEditor(labelWithEllipsisStyleExpressions);
        labelWithEllipsisStyleExpressions.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");


                StyleExpressions oldStyleExpressions = labelWithEllipsisStyleExpressions.getValue();
                //should never be the case
                if (oldStyleExpressions == null)
                {
                    oldStyleExpressions = new StyleExpressions();
                }
                StyleExpressions styleExpressions = StyleExpressionsChooser.showStyleExpressionsChooser(PropertyTable.this,
                                                                                                        TranslationManager.getInstance().getTranslation("R", "PropertyEditor.StyleExpressions.Title"),
                                                                                                        oldStyleExpressions);
                labelWithEllipsisStyleExpressions.setValue(styleExpressions, true);
            }
        });

        final CellEditorJLabelWithEllipsisRowBandingDefinition labelWithEllipsisRowBandingDefinition = new CellEditorJLabelWithEllipsisRowBandingDefinition();
        rowBandingDefinitionCellEditor = new CommonCellEditor(labelWithEllipsisRowBandingDefinition);
        labelWithEllipsisRowBandingDefinition.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");


                RowBandingDefinition oldRowBandingDefinition = labelWithEllipsisRowBandingDefinition.getValue();
                //should never be the case
                if (oldRowBandingDefinition == null)
                {
                    oldRowBandingDefinition = new RowBandingDefinition();
                }
                RowBandingDefinition rowBandingDefinition = RowBandingDefinitionChooser.showBorderDefinitionChooser(PropertyTable.this,
                                                                                                                    TranslationManager.getInstance().getTranslation("R", "PropertyEditor.RowBandingDefinition.Title"),
                                                                                                                    oldRowBandingDefinition);
                labelWithEllipsisRowBandingDefinition.setValue(rowBandingDefinition, true);
            }
        });

        final CellEditorJLabelWithEllipsisColorArray labelWithEllipsisColorArray = new CellEditorJLabelWithEllipsisColorArray();
        colorArrayCellEditor = new CommonCellEditor(labelWithEllipsisColorArray);
        labelWithEllipsisColorArray.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                PropertyDescriptor valueAt = (PropertyDescriptor) getValueAt(getEditingRow(), 0);
                Color[] array = ColorArrayChooser.showColorArrayChooser(PropertyTable.this,
                                                                        TranslationManager.getInstance().getTranslation("R", "PropertyEditor.ColorArray.Title"),
                                                                        valueAt.getName(),
                                                                        labelWithEllipsisColorArray.getValue());
                if (array != null)
                {
                    labelWithEllipsisColorArray.setValue(array, true);
                }
            }
        });

        final CellEditorJLabelWithEllipsisStringColorArray labelWithEllipsisStringColorArray = new CellEditorJLabelWithEllipsisStringColorArray();
        stringColorArrayCellEditor = new CommonCellEditor(labelWithEllipsisStringColorArray);
        labelWithEllipsisStringColorArray.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                PropertyDescriptor valueAt = (PropertyDescriptor) getValueAt(getEditingRow(), 0);
                String[] array = StringColorArrayChooser.showColorArrayChooser(PropertyTable.this,
                                                                               TranslationManager.getInstance().getTranslation("R", "PropertyEditor.ColorArray.Title"),
                                                                               valueAt.getName(),
                                                                               labelWithEllipsisStringColorArray.getValue());
                if (array != null)
                {
                    labelWithEllipsisStringColorArray.setValue(array, true);
                }
            }
        });

        final CellEditorJLabelWithEllipsisStringArray labelWithEllipsisElementArray = new CellEditorJLabelWithEllipsisStringArray();
        elementArrayCellEditor = new CommonCellEditor(labelWithEllipsisElementArray);
        labelWithEllipsisElementArray.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                PropertyDescriptor valueAt = (PropertyDescriptor) getValueAt(getEditingRow(), 0);
                String[] array = StringArrayChooser.showStringArrayChooser(StringArrayChooser.Type.ELEMENT,
                                                                           propertyEditorPanel.getReportDialog().getReport(),
                                                                           PropertyTable.this,
                                                                           TranslationManager.getInstance().getTranslation("R", "PropertyEditor.StringArray.Title"),
                                                                           valueAt.getName(),
                                                                           labelWithEllipsisElementArray.getValue());
                if (array != null)
                {
                    labelWithEllipsisElementArray.setValue(array, true);
                }
            }
        });

        final CellEditorJLabelWithEllipsisStringArray labelWithEllipsisFieldArray = new CellEditorJLabelWithEllipsisStringArray();
        fieldArrayCellEditor = new CommonCellEditor(labelWithEllipsisFieldArray);
        labelWithEllipsisFieldArray.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                PropertyDescriptor valueAt = (PropertyDescriptor) getValueAt(getEditingRow(), 0);
                String[] array = StringArrayChooser.showStringArrayChooser(StringArrayChooser.Type.FIELD,
                                                                           propertyEditorPanel.getReportDialog().getReport(),
                                                                           PropertyTable.this,
                                                                           TranslationManager.getInstance().getTranslation("R", "PropertyEditor.StringArray.Title"),
                                                                           valueAt.getName(),
                                                                           labelWithEllipsisFieldArray.getValue());
                if (array != null)
                {
                    labelWithEllipsisFieldArray.setValue(array, true);
                }
            }
        });

        final CellEditorJLabelWithEllipsisStringArray labelWithEllipsisStringArray = new CellEditorJLabelWithEllipsisStringArray();
        stringArrayCellEditor = new CommonCellEditor(labelWithEllipsisStringArray);
        labelWithEllipsisStringArray.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                PropertyDescriptor valueAt = (PropertyDescriptor) getValueAt(getEditingRow(), 0);
                String[] array = StringArrayChooser.showStringArrayChooser(StringArrayChooser.Type.STRING,
                                                                           null,
                                                                           PropertyTable.this,
                                                                           TranslationManager.getInstance().getTranslation("R", "PropertyEditor.StringArray.Title"),
                                                                           valueAt.getName(),
                                                                           labelWithEllipsisStringArray.getValue());
                if (array != null)
                {
                    labelWithEllipsisStringArray.setValue(array, true);
                }
            }
        });

        final CellEditorJLabelWithEllipsisNumberArray labelWithEllipsisNumberArray = new CellEditorJLabelWithEllipsisNumberArray();
        numberArrayCellEditor = new CommonCellEditor(labelWithEllipsisNumberArray);
        labelWithEllipsisNumberArray.getEllipsisButton().addActionListener(new ActionListener()
        {
            public void actionPerformed(@NotNull ActionEvent e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "PropertyTable.actionPerformed ");

                PropertyDescriptor valueAt = (PropertyDescriptor) getValueAt(getEditingRow(), 0);
                Number[] array = NumberArrayChooser.showNumberArrayChooser(PropertyTable.this,
                                                                           TranslationManager.getInstance().getTranslation("R", "PropertyEditor.NumberArray.Title"),
                                                                           valueAt.getName(),
                                                                           labelWithEllipsisNumberArray.getValue());
                if (array != null)
                {
                    labelWithEllipsisNumberArray.setValue(array, true);
                }
            }
        });


        getTableHeader().setReorderingAllowed(false);
    }


    private void initColumnResizing()
    {
        final int[] startX = new int[1];

        getTableHeader().setResizingAllowed(true);
        getTableHeader().getColumnModel().getColumn(0).setResizable(true);
        getTableHeader().getColumnModel().getColumn(1).setResizable(true);


        addMouseListener(new MouseAdapter()
        {
            public void mousePressed(@NotNull MouseEvent e)
            {
                startX[0] = e.getX();

                int i = getTableHeader().columnAtPoint(e.getPoint());
                if (i != -1)
                {
                    Rectangle r = getTableHeader().getHeaderRect(i);
                    r.grow(-3, 0);
                    if (e.getX() <= r.x || e.getX() >= r.x + r.width)
                    {
                        int midPoint = r.x + r.width / 2;
                        i = e.getX() < midPoint ? i - 1 : i;
                        if (i != -1)
                        {
                            getTableHeader().setResizingColumn(getTableHeader().getColumnModel().getColumn(i));
                        }
                        return;
                    }
                }
                getTableHeader().setResizingColumn(null);
            }


            public void mouseReleased(@NotNull MouseEvent e)
            {
                getTableHeader().setResizingColumn(null);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter()
        {

            public void mouseMoved(@NotNull MouseEvent e)
            {
                int i = getTableHeader().columnAtPoint(e.getPoint());
                if (i != -1)
                {
                    Rectangle r = getTableHeader().getHeaderRect(i);
                    r.grow(-3, 0);
                    if (e.getX() <= r.x || e.getX() >= r.x + r.width)
                    {
                        int midPoint = r.x + r.width / 2;
                        i = e.getX() < midPoint ? i - 1 : i;
                        if (i != -1)
                        {
                            setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                        }
                        return;
                    }
                }
                setCursor(Cursor.getDefaultCursor());
            }


            public void mouseDragged(@NotNull MouseEvent e)
            {
                TableColumn tc = getTableHeader().getResizingColumn();
                if (tc != null)
                {
                    int diff = startX[0] - e.getX();
                    tc.setWidth(tc.getWidth() - diff);

                    startX[0] = e.getX();
                }
            }
        });
    }


    public void editingStopped(@NotNull ChangeEvent e)
    {
        int row = editingRow;
        super.editingStopped(e);
        if (row != -1)
        {
            requestFocusInWindow();
            getSelectionModel().setSelectionInterval(row, row);
        }
    }


    public boolean getSurrendersFocusOnKeystroke()
    {
        return true;
    }


    @Nullable
    public TableCellEditor getCellEditor(int row, int column)
    {
        switch (column)
        {
            case 0:
                return null;
            case 1:
                Object obj = propertyTableModel.getObject(row);
                if (obj instanceof PropertyDescriptor)
                {
                    PropertyDescriptor propertyDescriptor = (PropertyDescriptor) obj;

                    if (Point2D.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return point2DCellEditor;
                    }
                    else if (Dimension2D.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return dimension2DCellEditor;
                    }
                    else if (ElementPadding.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return paddingCellEditor;
                    }
                    else if (Boolean.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return booleanCellEditor;
                    }
                    else if (Boolean.TYPE.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return booleanCellEditor;
                    }
                    else if (String.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        if (propertyDescriptor instanceof GroupingPropertyDescriptor)
                        {
                            GroupingPropertyDescriptor groupingPropertyDescriptor = (GroupingPropertyDescriptor) propertyDescriptor;
                            if (groupingPropertyDescriptor.isField())
                            {
                                ((CellEditorJTextFieldWithEllipsis<?>) fieldCellEditor.getEditorComponent()).setNullable(groupingPropertyDescriptor.isNullable());
                                return fieldCellEditor;
                            }
                            else if (groupingPropertyDescriptor.isElement())
                            {
                                ((CellEditorJTextFieldWithEllipsis<?>) elementCellEditor.getEditorComponent()).setNullable(groupingPropertyDescriptor.isNullable());
                                return elementCellEditor;
                            }
                            else if (groupingPropertyDescriptor.isStringColor())
                            {
                                ((CellEditorJTextFieldWithEllipsis<?>) stringColorCellEditor.getEditorComponent()).setNullable(groupingPropertyDescriptor.isNullable());
                                return stringColorCellEditor;
                            }
                            else if (groupingPropertyDescriptor.isQuery())
                            {
                                ((CellEditorJTextFieldWithEllipsis<?>) queryCellEditor.getEditorComponent()).setNullable(groupingPropertyDescriptor.isNullable());
                                return queryCellEditor;
                            }
                            else if (groupingPropertyDescriptor.isNullable())
                            {
                                ((CellEditorJTextFieldWithEllipsis<?>) stringCellEditor.getEditorComponent()).setNullable(groupingPropertyDescriptor.isNullable());
                                return stringCellEditor;
                            }
                        }
                        ((CellEditorJTextFieldWithEllipsis<?>) stringCellEditor.getEditorComponent()).setNullable(false);
                        return stringCellEditor;
                    }
                    else if (DateFormat.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return simpleDateFormatCellEditor;
                    }
                    else if (NumberFormat.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return decimalFormatCellEditor;
                    }
                    else if (URL.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return urlCellEditor;
                    }
                    else if (Color.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return colorCellEditor;
                    }
                    else if (Font.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return fontCellEditor;
                    }
                    else if (BorderDefinition.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return borderDefinitionCellEditor;
                    }
                    else if (ElementBorderDefinition.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return elementBorderDefinitionCellEditor;
                    }
                    else if (RowBandingDefinition.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return rowBandingDefinitionCellEditor;
                    }
                    else if (StyleExpressions.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return styleExpressionsCellEditor;
                    }
                    else if (Formula.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return formulaCellEditor;
                    }
                    else if (PageDefinition.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return pageDefinitionCellEditor;
                    }
                    else if (TextReportElementHorizontalAlignment.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return horizontalAlignmentCellEditor;
                    }
                    else if (TextReportElementVerticalAlignment.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return verticalAlignmentCellEditor;
                    }
                    else if (LineDirection.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return lineDirectionCellEditor;
                    }
                    else if (ReportLayoutManager.Type.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return reportLayoutManagerTypeCellEditor;
                    }
                    else if (Integer.TYPE.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return integerCellEditor;
                    }
                    else if (Integer.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return integerCellEditor;
                    }
                    else if (Long.TYPE.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return longCellEditor;
                    }
                    else if (Long.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return longCellEditor;
                    }
                    else if (String[].class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        if (propertyDescriptor instanceof GroupingPropertyDescriptor)
                        {
                            GroupingPropertyDescriptor groupingPropertyDescriptor = (GroupingPropertyDescriptor) propertyDescriptor;
                            if (groupingPropertyDescriptor.isField())
                            {
                                return fieldArrayCellEditor;
                            }
                            else if (groupingPropertyDescriptor.isElement())
                            {
                                return elementArrayCellEditor;
                            }
                            else if (groupingPropertyDescriptor.isStringColor())
                            {
                                return stringColorArrayCellEditor;
                            }
                        }
                        return stringArrayCellEditor;
                    }
                    else if (Color[].class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return colorArrayCellEditor;
                    }
                    else if (Number[].class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return numberArrayCellEditor;
                    }
                    else if (Double.TYPE.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return doubleCellEditor;
                    }
                    else if (Double.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return doubleCellEditor;
                    }
                    else if (Float.TYPE.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return floatCellEditor;
                    }
                    else if (Float.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return floatCellEditor;
                    }
                    else if (Locale.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        if (!LocaleDataLoader.hasLocales())
                        {
                            return null;
                        }
                        return localeCellEditor;
                    }
                    else if (TimeZone.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        if (!TimeZoneDataLoader.hasTimeZones())
                        {
                            return null;
                        }
                        return timeZoneCellEditor;
                    }
                    else if (Date.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return dateCellEditor;
                    }
                    else if (FilePath.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        if (propertyDescriptor instanceof GroupingPropertyDescriptor)
                        {
                            GroupingPropertyDescriptor groupingPropertyDescriptor = (GroupingPropertyDescriptor) propertyDescriptor;
                            if (groupingPropertyDescriptor.isRegularFile())
                            {
                                return regularFilePathCellEditor;
                            }
                        }
                        return filePathCellEditor;
                    }
                    else if (SubReportParameters.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return subReportParametersCellEditor;
                    }
                    else if (ReportConfiguration.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return reportConfigurationCellEditor;
                    }
                }
        }

        return null;
    }


    @NotNull
    public TableCellRenderer getCellRenderer(int row, int column)
    {
        switch (column)
        {
            case 0:
            {
                Object obj = propertyTableModel.getObject(row);
                if (obj instanceof PropertyDescriptor)
                {
                    return propertyDescriptorCellRenderer;
                }
                else
                {
                    return groupCellRenderer;
                }
            }
            case 1:
            {
                Object obj = propertyTableModel.getObject(row);
                if (obj instanceof PropertyDescriptor)
                {
                    PropertyDescriptor propertyDescriptor = (PropertyDescriptor) obj;
                    if (Point2D.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return point2DTableCellRenderer;
                    }
                    else if (Dimension2D.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return dimension2DTableCellRenderer;
                    }
                    else if (ElementPadding.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return paddingTableCellRenderer;
                    }
                    else if (Boolean.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return booleanTableCellRenderer;
                    }
                    else if (Boolean.TYPE.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return booleanTableCellRenderer;
                    }
                    else if (Font.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return fontTableCellRenderer;
                    }
                    else if (BorderDefinition.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return borderDefinitionTableCellRenderer;
                    }
                    else if (ElementBorderDefinition.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return elementBorderDefinitionTableCellRenderer;
                    }
                    else if (RowBandingDefinition.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return rowBandingDefinitionTableCellRenderer;
                    }
                    else if (StyleExpressions.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return styleExpressionsTableCellRenderer;
                    }
                    else if (PageDefinition.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return pageDefinitionTableCellRenderer;
                    }
                    else if (String.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        if (propertyDescriptor instanceof GroupingPropertyDescriptor)
                        {
                            GroupingPropertyDescriptor groupingPropertyDescriptor = (GroupingPropertyDescriptor) propertyDescriptor;
                            if (groupingPropertyDescriptor.isStringColor())
                            {
                                return stringColorTableCellRenderer;
                            }
                        }
                        return stringTableCellRenderer;
                    }
                    else if (DateFormat.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return simpleDateFormatTableCellRenderer;
                    }
                    else if (NumberFormat.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return decimalFormatTableCellRenderer;
                    }
                    else if (URL.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return urlTableCellRenderer;
                    }
                    else if (Color.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return colorTableCellRenderer;
                    }
                    else if (TextReportElementHorizontalAlignment.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return horizontalAlignmentTableCellRenderer;
                    }
                    else if (TextReportElementVerticalAlignment.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return verticalAlignmentTableCellRenderer;
                    }
                    else if (LineDirection.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return lineDirectionTableCellRenderer;
                    }
                    else if (ReportLayoutManager.Type.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return reportLayoutManagerTypeTableCellRenderer;
                    }
                    else if (Integer.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return integerTableCellRenderer;
                    }
                    else if (Integer.TYPE.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return integerTableCellRenderer;
                    }
                    else if (Long.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return longTableCellRenderer;
                    }
                    else if (Long.TYPE.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return longTableCellRenderer;
                    }
                    else if (String[].class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return stringArrayTableCellRenderer;
                    }
                    else if (Color[].class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return colorArrayTableCellRenderer;
                    }
                    else if (Number[].class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return numberArrayTableCellRenderer;
                    }
                    else if (Double.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return doubleTableCellRenderer;
                    }
                    else if (Double.TYPE.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return doubleTableCellRenderer;
                    }
                    else if (Float.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return floatTableCellRenderer;
                    }
                    else if (Float.TYPE.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return floatTableCellRenderer;
                    }
                    else if (Locale.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return localeTableCellRenderer;
                    }
                    else if (TimeZone.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return timeZoneTableCellRenderer;
                    }
                    else if (Date.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return dateTableCellRenderer;
                    }
                    else if (Formula.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return formulaTableCellRenderer;
                    }
                    else if (FilePath.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return filePathTableCellRenderer;
                    }
                    else if (SubReportParameters.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return subReportParametersTableCellRenderer;
                    }
                    else if (ReportConfiguration.class.isAssignableFrom(propertyDescriptor.getPropertyType()))
                    {
                        return reportConfigurationCellRenderer;
                    }
                }
                else
                {
                    return groupCellRenderer;
                }
            }
        }

        return super.getCellRenderer(row, column);
    }


}
