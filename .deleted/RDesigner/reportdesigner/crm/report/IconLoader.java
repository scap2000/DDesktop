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
package org.pentaho.reportdesigner.crm.report;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * User: Martin
 * Date: 27.10.2005
 * Time: 15:45:56
 */
@SuppressWarnings({"HardCodedStringLiteral"})
public class IconLoader
{
    @NotNull
    private static final IconLoader instance = new IconLoader();

    @NotNull
    private ImageIcon reportFrameIcon;

    @NotNull
    private ImageIcon nullReportElementIcon;
    @NotNull
    private ImageIcon labelReportElementIcon;
    @NotNull
    private ImageIcon resourceReportElementIcon;
    @NotNull
    private ImageIcon textFieldReportElementIcon;
    @NotNull
    private ImageIcon resourceFieldReportElementIcon;
    @NotNull
    private ImageIcon resourceLabelReportElementIcon;
    @NotNull
    private ImageIcon resourceMessageReportElementIcon;
    @NotNull
    private ImageIcon dateFieldReportElementIcon;
    @NotNull
    private ImageIcon numberFieldReportElementIcon;
    @NotNull
    private ImageIcon messageFieldReportElementIcon;
    @NotNull
    private ImageIcon rectangleReportElementIcon;
    @NotNull
    private ImageIcon ellipseReportElementIcon;

    @NotNull
    private ImageIcon groupsReportElementIcon;
    @NotNull
    private ImageIcon groupReportElementIcon;

    @NotNull
    private ImageIcon bandReportElementIcon;
    @NotNull
    private ImageIcon bandHeaderReportElementIcon;
    @NotNull
    private ImageIcon bandFooterReportElementIcon;
    @NotNull
    private ImageIcon bandWatermarkReportElementIcon;
    @NotNull
    private ImageIcon reportReportElementIcon;

    @NotNull
    private ImageIcon functionsIcon;
    @NotNull
    private ImageIcon functionIcon;

    @NotNull
    private ImageIcon lineReportElementIcon;
    @NotNull
    private ImageIcon lineVerticalReportElementIcon;
    @NotNull
    private ImageIcon staticImageReportElement;
    @NotNull
    private ImageIcon imageFieldReportElementIcon;
    @NotNull
    private ImageIcon imageURLFieldReportElementIcon;
    @NotNull
    private ImageIcon anchorFieldReportElementIcon;

    @NotNull
    private ImageIcon drawableFieldReportElementIcon;

    @NotNull
    private ImageIcon subReportElementIcon;

    @NotNull
    private ImageIcon undoIconSmall;
    @NotNull
    private ImageIcon redoIconSmall;

    @NotNull
    private ImageIcon deleteIconSmall;

    @NotNull
    private ImageIcon dataSetsIcon;
    @NotNull
    private ImageIcon propertiesDataSetIcon;

    @NotNull
    private ImageIcon infoIcon;
    @NotNull
    private ImageIcon warningIcon;
    @NotNull
    private ImageIcon errorIcon;
    @NotNull
    private ImageIcon noErrorIcon;

    @NotNull
    private ImageIcon drawSelectionTypeClampIcon;
    @NotNull
    private ImageIcon drawSelectionTypeOutlineIcon;

    @NotNull
    private ImageIcon paletteIcon;
    @NotNull
    private ImageIcon messagesIcon;
    @NotNull
    private ImageIcon propertyTableIcon;
    @NotNull
    private ImageIcon reportTreeIcon;

    @NotNull
    private ImageIcon wizardIcon;
    @NotNull
    private ImageIcon reportWizardIcon;

    @NotNull
    private ImageIcon databaseSchemaIcon;
    @NotNull
    private ImageIcon databaseTableIcon;
    @NotNull
    private ImageIcon databaseColumnIcon;

    @NotNull
    private ImageIcon templateColoredSampleIcon;
    @NotNull
    private ImageIcon templateGridSampleIcon;

    @NotNull
    private ImageIcon pageFirstIcon;
    @NotNull
    private ImageIcon pageUpIcon;
    @NotNull
    private ImageIcon pageDownIcon;
    @NotNull
    private ImageIcon pageLastIcon;

    @NotNull
    private ImageIcon aboutIcon;

    @NotNull
    private ImageIcon[] sanduhrs;

    @NotNull
    private ImageIcon aboutDialogPicture;

    @NotNull
    private ImageIcon templateIndentedIcon;
    @NotNull
    private ImageIcon templateStructuredIcon;

    @NotNull
    private ImageIcon languageIcon32;
    @NotNull
    private ImageIcon browserIcon32;
    @NotNull
    private ImageIcon networkIcon32;
    @NotNull
    private ImageIcon externalToolsIcon32;

    @NotNull
    private ImageIcon cutIcon;
    @NotNull
    private ImageIcon copyIcon;
    @NotNull
    private ImageIcon pasteIcon;

    @NotNull
    private ImageIcon onlineHelpHTMLIcon;
    @NotNull
    private ImageIcon onlineHelpPDFIcon;
    @NotNull
    private ImageIcon onlineForumIcon;

    @NotNull
    private ImageIcon selectionEdge;

    @NotNull
    private ImageIcon dontShowInLayoutGUISelectionEdge;

    @NotNull
    private ImageIcon newIcon;
    @NotNull
    private ImageIcon openIcon;
    @NotNull
    private ImageIcon saveIcon;

    @NotNull
    private ImageIcon mergeIcon;

    @NotNull
    private ImageIcon openWizardIcon;

    @NotNull
    private ImageIcon settingsIcon;

    @NotNull
    private ImageIcon selectAllIcon;
    @NotNull
    private ImageIcon deselectAllIcon;

    @NotNull
    private ImageIcon gotoOverlayIcon;

    @NotNull
    private ImageIcon layerUpIcon;
    @NotNull
    private ImageIcon layerDownIcon;

    @NotNull
    private ImageIcon zoomIcon;

    @NotNull
    private ImageIcon zoomOverlay50Icon;
    @NotNull
    private ImageIcon zoomOverlay100Icon;
    @NotNull
    private ImageIcon zoomOverlay200Icon;
    @NotNull
    private ImageIcon zoomOverlay400Icon;

    @NotNull
    private ImageIcon layoutBandsIcon;

    @NotNull
    private ImageIcon importXMLIcon;
    @NotNull
    private ImageIcon exportXMLIcon;

    @NotNull
    private ImageIcon createReportIcon;

    @NotNull
    private ImageIcon previewPDFIcon;
    @NotNull
    private ImageIcon previewHTMLIcon;
    @NotNull
    private ImageIcon previewRTFIcon;
    @NotNull
    private ImageIcon previewXLSIcon;
    @NotNull
    private ImageIcon previewCSVIcon;
    @NotNull
    private ImageIcon previewXMLIcon;

    @NotNull
    private ImageIcon alignLeftIcon;
    @NotNull
    private ImageIcon alignCenterIcon;
    @NotNull
    private ImageIcon alignRightIcon;
    @NotNull
    private ImageIcon alignTopIcon;
    @NotNull
    private ImageIcon alignMiddleIcon;
    @NotNull
    private ImageIcon alignBottomIcon;

    @NotNull
    private ImageIcon distributeLeftIcon;
    @NotNull
    private ImageIcon distributeCenterIcon;
    @NotNull
    private ImageIcon distributeRightIcon;
    @NotNull
    private ImageIcon distributeGapsHorizontalIcon;
    @NotNull
    private ImageIcon distributeTopIcon;
    @NotNull
    private ImageIcon distributeMiddleIcon;
    @NotNull
    private ImageIcon distributeBottomIcon;
    @NotNull
    private ImageIcon distributeGapsVerticalIcon;

    @NotNull
    private ImageIcon publishToServerIcon;

    @NotNull
    private ImageIcon groupIcon;
    @NotNull
    private ImageIcon sortAscendingIcon;
    @NotNull
    private ImageIcon sortDescendingIcon;
    @NotNull
    private ImageIcon chartReportElementIcon;


    @NotNull
    public static IconLoader getInstance()
    {
        return instance;
    }


    private IconLoader()
    {
        reportFrameIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/ReportFrameIcon.png"));

        nullReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/NullReportElement.png"));
        labelReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/LabelReportElement.png"));
        resourceReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/ResourceReportElement.png"));
        resourceLabelReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/ResourceLabelReportElement.png"));
        resourceMessageReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/ResourceMessageReportElement.png"));
        textFieldReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/TextFieldReportElement.png"));
        resourceFieldReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/ResourceFieldReportElement.png"));
        resourceLabelReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/ResourceLabelReportElement.png"));
        resourceMessageReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/ResourceMessageReportElement.png"));
        dateFieldReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/DateFieldReportElement.png"));
        numberFieldReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/NumberFieldReportElement.png"));
        messageFieldReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/MessageFieldReportElement.png"));
        rectangleReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/RectangleReportElement.png"));
        ellipseReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/EllipseReportElement.png"));
        chartReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/ChartReportElement.png"));

        groupsReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/GroupsReportElement.png"));
        groupReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/GroupReportElement.png"));

        bandReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/BandReportElement.png"));
        bandHeaderReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/BandHeaderReportElement.png"));
        bandFooterReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/BandFooterReportElement.png"));
        bandWatermarkReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/BandWatermarkReportElement.png"));
        reportReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/ReportReportElement.png"));

        functionsIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/Functions.png"));
        functionIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/Function.png"));

        lineReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/LineReportElement.png"));
        lineVerticalReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/LineVerticalReportElement.png"));
        staticImageReportElement = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/StaticImageReportElement.png"));
        imageFieldReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/ImageFieldReportElement.png"));
        imageURLFieldReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/ImageURLFieldReportElement.png"));
        anchorFieldReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/AnchorFieldReportElement.png"));

        drawableFieldReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/DrawableFieldReportElement.png"));

        subReportElementIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/SubReportElement.png"));

        undoIconSmall = new ImageIcon(IconLoader.class.getResource("/res/icons/UndoIcon.png"));
        redoIconSmall = new ImageIcon(IconLoader.class.getResource("/res/icons/RedoIcon.png"));

        deleteIconSmall = new ImageIcon(IconLoader.class.getResource("/res/icons/DeleteIcon.png"));

        dataSetsIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/DataSets.png"));
        propertiesDataSetIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/reportelements/PropertiesDataSetIcon.png"));

        infoIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/InfoIcon.png"));
        warningIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/WarningIcon.png"));
        errorIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/ErrorIcon.png"));
        noErrorIcon = new ImageIcon(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));

        drawSelectionTypeOutlineIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/DrawSelectionTypeOutlineIcon.png"));
        drawSelectionTypeClampIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/DrawSelectionTypeClampIcon.png"));

        paletteIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/PaletteIcon.png"));
        messagesIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/MessagesIcon.png"));
        propertyTableIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/PropertyTableIcon.png"));
        reportTreeIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/ReportTreeIcon.png"));

        wizardIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/WizardIcon.png"));
        reportWizardIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/ReportWizardIcon.png"));

        databaseSchemaIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/DatabaseSchemaIcon.png"));
        databaseTableIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/DatabaseTableIcon.png"));
        databaseColumnIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/DatabaseColumnIcon.png"));

        sanduhrs = new ImageIcon[14];
        for (int i = 0; i < 13; i++)
        {
            String number = "0" + (i % 12 + 1);
            number = number.substring(number.length() - 2, number.length());
            sanduhrs[i] = new ImageIcon(IconLoader.class.getResource("/res/icons/hourglass/Sanduhr" + number + ".png"));
        }
        sanduhrs[13] = new ImageIcon(IconLoader.class.getResource("/res/icons/hourglass/Sanduhr01.png"));

        templateColoredSampleIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/TemplateColoredSampleIcon.png"));
        templateGridSampleIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/TemplateGridSampleIcon.png"));

        pageFirstIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/PageFirstIcon.png"));
        pageUpIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/PageUpIcon.png"));
        pageDownIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/PageDownIcon.png"));
        pageLastIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/PageLastIcon.png"));

        aboutIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/AboutIcon.png"));
        aboutDialogPicture = new ImageIcon(IconLoader.class.getResource("/res/icons/SplashScreen.png"));

        templateStructuredIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/TemplateStructuredIcon.png"));
        templateIndentedIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/TemplateIndentedIcon.png"));

        languageIcon32 = new ImageIcon(IconLoader.class.getResource("/res/icons/LanguageIcon32.png"));
        browserIcon32 = new ImageIcon(IconLoader.class.getResource("/res/icons/BrowserIcon32.png"));
        networkIcon32 = new ImageIcon(IconLoader.class.getResource("/res/icons/NetworkIcon32.png"));
        externalToolsIcon32 = new ImageIcon(IconLoader.class.getResource("/res/icons/ExternalToolsIcon32.png"));

        cutIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/CutIcon.png"));
        copyIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/CopyIcon.png"));
        pasteIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/PasteIcon.png"));

        onlineHelpHTMLIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/OnlineHelpHTMLIcon.png"));
        onlineHelpPDFIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/OnlineHelpPDFIcon.png"));
        onlineForumIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/OnlineForumIcon.png"));

        selectionEdge = new ImageIcon(IconLoader.class.getResource("/res/icons/SelectionEdge1.png"));
        dontShowInLayoutGUISelectionEdge = new ImageIcon(IconLoader.class.getResource("/res/icons/SelectionEdge2.png"));

        newIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/NewIcon.png"));
        openIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/OpenIcon.png"));
        saveIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/SaveIcon.png"));

        mergeIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/MergeIcon.png"));

        openWizardIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/OpenWizardIcon.png"));

        settingsIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/AdvancedIcon.png"));

        selectAllIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/SelectAllIcon.png"));
        deselectAllIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/DeselectAllIcon.png"));

        gotoOverlayIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/GotoOverlayIcon.png"));

        layerUpIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/LayerUpIcon.png"));
        layerDownIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/LayerDownIcon.png"));

        zoomIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/ZoomIcon.png"));
        zoomOverlay50Icon = new ImageIcon(IconLoader.class.getResource("/res/icons/ZoomOverlay50Icon.png"));
        zoomOverlay100Icon = new ImageIcon(IconLoader.class.getResource("/res/icons/ZoomOverlay100Icon.png"));
        zoomOverlay200Icon = new ImageIcon(IconLoader.class.getResource("/res/icons/ZoomOverlay200Icon.png"));
        zoomOverlay400Icon = new ImageIcon(IconLoader.class.getResource("/res/icons/ZoomOverlay400Icon.png"));

        layoutBandsIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/LayoutBandsIcon.png"));

        importXMLIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/ImportXMLIcon.png"));
        exportXMLIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/ExportXMLIcon.png"));

        createReportIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/CreateReportIcon.png"));

        previewPDFIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/PreviewPDFIcon.png"));
        previewHTMLIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/PreviewHTMLIcon.png"));
        previewRTFIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/PreviewRTFIcon.png"));
        previewXLSIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/PreviewXLSIcon.png"));
        previewCSVIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/PreviewCSVIcon.png"));
        previewXMLIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/PreviewXMLIcon.png"));

        alignLeftIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/AlignLeftIcon.png"));
        alignCenterIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/AlignCenterIcon.png"));
        alignRightIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/AlignRightIcon.png"));
        alignTopIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/AlignTopIcon.png"));
        alignMiddleIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/AlignMiddleIcon.png"));
        alignBottomIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/AlignBottomIcon.png"));

        distributeLeftIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/DistributeLeftIcon.png"));
        distributeCenterIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/DistributeCenterIcon.png"));
        distributeRightIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/DistributeRightIcon.png"));
        distributeGapsHorizontalIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/DistributeGapsHorizontalIcon.png"));
        distributeTopIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/DistributeTopIcon.png"));
        distributeMiddleIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/DistributeMiddleIcon.png"));
        distributeBottomIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/DistributeBottomIcon.png"));
        distributeGapsVerticalIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/DistributeGapsVerticalIcon.png"));

        publishToServerIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/PublishToServerIcon.png"));

        groupIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/GroupIcon.png"));
        sortAscendingIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/SortAscendingIcon.png"));
        sortDescendingIcon = new ImageIcon(IconLoader.class.getResource("/res/icons/SortDescendingIcon.png"));
    }


    @NotNull
    public ImageIcon getDistributeBottomIcon()
    {
        return distributeBottomIcon;
    }


    @NotNull
    public ImageIcon getDistributeGapsVerticalIcon()
    {
        return distributeGapsVerticalIcon;
    }


    @NotNull
    public ImageIcon getDistributeMiddleIcon()
    {
        return distributeMiddleIcon;
    }


    @NotNull
    public ImageIcon getDistributeTopIcon()
    {
        return distributeTopIcon;
    }


    @NotNull
    public ImageIcon getDistributeCenterIcon()
    {
        return distributeCenterIcon;
    }


    @NotNull
    public ImageIcon getDistributeGapsHorizontalIcon()
    {
        return distributeGapsHorizontalIcon;
    }


    @NotNull
    public ImageIcon getDistributeLeftIcon()
    {
        return distributeLeftIcon;
    }


    @NotNull
    public ImageIcon getDistributeRightIcon()
    {
        return distributeRightIcon;
    }


    @NotNull
    public ImageIcon getAlignCenterIcon()
    {
        return alignCenterIcon;
    }


    @NotNull
    public ImageIcon getAlignLeftIcon()
    {
        return alignLeftIcon;
    }


    @NotNull
    public ImageIcon getAlignRightIcon()
    {
        return alignRightIcon;
    }


    @NotNull
    public ImageIcon getAlignTopIcon()
    {
        return alignTopIcon;
    }


    @NotNull
    public ImageIcon getAlignMiddleIcon()
    {
        return alignMiddleIcon;
    }


    @NotNull
    public ImageIcon getAlignBottomIcon()
    {
        return alignBottomIcon;
    }


    @NotNull
    public ImageIcon getPreviewPDFIcon()
    {
        return previewPDFIcon;
    }


    @NotNull
    public ImageIcon getPreviewRTFIcon()
    {
        return previewRTFIcon;
    }


    @NotNull
    public ImageIcon getPreviewXLSIcon()
    {
        return previewXLSIcon;
    }


    @NotNull
    public ImageIcon getPreviewCSVIcon()
    {
        return previewCSVIcon;
    }


    @NotNull
    public ImageIcon getPreviewXMLIcon()
    {
        return previewXMLIcon;
    }


    @NotNull
    public ImageIcon getPreviewHTMLIcon()
    {
        return previewHTMLIcon;
    }


    @NotNull
    public ImageIcon getExternalToolsIcon32()
    {
        return externalToolsIcon32;
    }


    @NotNull
    public ImageIcon getCreateReportIcon()
    {
        return createReportIcon;
    }


    @NotNull
    public ImageIcon getImportXMLIcon()
    {
        return importXMLIcon;
    }


    @NotNull
    public ImageIcon getExportXMLIcon()
    {
        return exportXMLIcon;
    }


    @NotNull
    public ImageIcon getLayoutBandsIcon()
    {
        return layoutBandsIcon;
    }


    @NotNull
    public ImageIcon getZoomOverlay50Icon()
    {
        return zoomOverlay50Icon;
    }


    @NotNull
    public ImageIcon getZoomOverlay100Icon()
    {
        return zoomOverlay100Icon;
    }


    @NotNull
    public ImageIcon getZoomOverlay200Icon()
    {
        return zoomOverlay200Icon;
    }


    @NotNull
    public ImageIcon getZoomOverlay400Icon()
    {
        return zoomOverlay400Icon;
    }


    @NotNull
    public ImageIcon getLayerUpIcon()
    {
        return layerUpIcon;
    }


    @NotNull
    public ImageIcon getLayerDownIcon()
    {
        return layerDownIcon;
    }


    @NotNull
    public ImageIcon getGotoOverlayIcon()
    {
        return gotoOverlayIcon;
    }


    @NotNull
    public ImageIcon getSelectAllIcon()
    {
        return selectAllIcon;
    }


    @NotNull
    public ImageIcon getDeselectAllIcon()
    {
        return deselectAllIcon;
    }


    @NotNull
    public ImageIcon getMergeIcon()
    {
        return mergeIcon;
    }


    @NotNull
    public ImageIcon getSettingsIcon()
    {
        return settingsIcon;
    }


    @NotNull
    public ImageIcon getOpenWizardIcon()
    {
        return openWizardIcon;
    }


    @NotNull
    public ImageIcon getNewIcon()
    {
        return newIcon;
    }


    @NotNull
    public ImageIcon getOpenIcon()
    {
        return openIcon;
    }


    @NotNull
    public ImageIcon getSaveIcon()
    {
        return saveIcon;
    }


    @NotNull
    public ImageIcon getPropertiesDataSetIcon()
    {
        return propertiesDataSetIcon;
    }


    @NotNull
    public ImageIcon getSelectionEdge()
    {
        return selectionEdge;
    }


    @NotNull
    public ImageIcon getDontShowInLayoutGUISelectionEdge()
    {
        return dontShowInLayoutGUISelectionEdge;
    }


    @NotNull
    public ImageIcon getCutIcon()
    {
        return cutIcon;
    }


    @NotNull
    public ImageIcon getCopyIcon()
    {
        return copyIcon;
    }


    @NotNull
    public ImageIcon getPasteIcon()
    {
        return pasteIcon;
    }


    @NotNull
    public ImageIcon getDrawableFieldReportElementIcon()
    {
        return drawableFieldReportElementIcon;
    }


    @NotNull
    public ImageIcon getPageFirstIcon()
    {
        return pageFirstIcon;
    }


    @NotNull
    public ImageIcon getPageUpIcon()
    {
        return pageUpIcon;
    }


    @NotNull
    public ImageIcon getPageDownIcon()
    {
        return pageDownIcon;
    }


    @NotNull
    public ImageIcon getPageLastIcon()
    {
        return pageLastIcon;
    }


    @NotNull
    public ImageIcon[] getSanduhrs()
    {
        return sanduhrs;
    }


    @NotNull
    public ImageIcon getDatabaseSchemaIcon()
    {
        return databaseSchemaIcon;
    }


    @NotNull
    public ImageIcon getDatabaseTableIcon()
    {
        return databaseTableIcon;
    }


    @NotNull
    public ImageIcon getDatabaseColumnIcon()
    {
        return databaseColumnIcon;
    }


    @NotNull
    public ImageIcon getReportFrameIcon()
    {
        return reportFrameIcon;
    }


    @NotNull
    public ImageIcon getBandHeaderReportElementIcon()
    {
        return bandHeaderReportElementIcon;
    }


    @NotNull
    public ImageIcon getBandFooterReportElementIcon()
    {
        return bandFooterReportElementIcon;
    }


    @NotNull
    public ImageIcon getBandWatermarkReportElementIcon()
    {
        return bandWatermarkReportElementIcon;
    }


    @NotNull
    public ImageIcon getGroupsReportElementIcon()
    {
        return groupsReportElementIcon;
    }


    @NotNull
    public ImageIcon getGroupReportElementIcon()
    {
        return groupReportElementIcon;
    }


    @NotNull
    public ImageIcon getPaletteIcon()
    {
        return paletteIcon;
    }


    @NotNull
    public ImageIcon getMessagesIcon()
    {
        return messagesIcon;
    }


    @NotNull
    public ImageIcon getPropertyTableIcon()
    {
        return propertyTableIcon;
    }


    @NotNull
    public ImageIcon getReportTreeIcon()
    {
        return reportTreeIcon;
    }


    @NotNull
    public ImageIcon getDrawSelectionTypeClampIcon()
    {
        return drawSelectionTypeClampIcon;
    }


    @NotNull
    public ImageIcon getDrawSelectionTypeOutlineIcon()
    {
        return drawSelectionTypeOutlineIcon;
    }


    @NotNull
    public ImageIcon getInfoIcon()
    {
        return infoIcon;
    }


    @NotNull
    public ImageIcon getWarningIcon()
    {
        return warningIcon;
    }


    @NotNull
    public ImageIcon getErrorIcon()
    {
        return errorIcon;
    }


    @NotNull
    public ImageIcon getNullReportElementIcon()
    {
        return nullReportElementIcon;
    }


    @NotNull
    public ImageIcon getLabelReportElementIcon()
    {
        return labelReportElementIcon;
    }


    @NotNull
    public ImageIcon getResourceReportElementIcon()
    {
        return resourceReportElementIcon;
    }


    @NotNull
    public ImageIcon getTextFieldReportElementIcon()
    {
        return textFieldReportElementIcon;
    }


    @NotNull
    public ImageIcon getResourceFieldReportElementIcon()
    {
        return resourceFieldReportElementIcon;
    }


    @NotNull
    public ImageIcon getDateFieldReportElementIcon()
    {
        return dateFieldReportElementIcon;
    }


    @NotNull
    public ImageIcon getReportReportElementIcon()
    {
        return reportReportElementIcon;
    }


    @NotNull
    public ImageIcon getFunctionsIcon()
    {
        return functionsIcon;
    }


    @NotNull
    public ImageIcon getFunctionIcon()
    {
        return functionIcon;
    }


    @NotNull
    public ImageIcon getBandReportElementIcon()
    {
        return bandReportElementIcon;
    }


    @NotNull
    public ImageIcon getMessageFieldReportElementIcon()
    {
        return messageFieldReportElementIcon;
    }


    @NotNull
    public ImageIcon getNumberFieldReportElementIcon()
    {
        return numberFieldReportElementIcon;
    }


    @NotNull
    public ImageIcon getLineReportElementIcon()
    {
        return lineReportElementIcon;
    }


    @NotNull
    public ImageIcon getStaticImageReportElementIcon()
    {
        return staticImageReportElement;
    }


    @NotNull
    public ImageIcon getUndoIconSmall()
    {
        return undoIconSmall;
    }


    @NotNull
    public ImageIcon getRedoIconSmall()
    {
        return redoIconSmall;
    }


    @NotNull
    public ImageIcon getDeleteIconSmall()
    {
        return deleteIconSmall;
    }


    @NotNull
    public ImageIcon getLineVerticalReportElementIcon()
    {
        return lineVerticalReportElementIcon;
    }


    @NotNull
    public ImageIcon getDataSetsIcon()
    {
        return dataSetsIcon;
    }


    @NotNull
    public ImageIcon getStaticImageReportElement()
    {
        return staticImageReportElement;
    }


    @NotNull
    public ImageIcon getZoomIcon()
    {
        return zoomIcon;
    }


    @NotNull
    public ImageIcon getRectangleReportElementIcon()
    {
        return rectangleReportElementIcon;
    }


    @NotNull
    public ImageIcon getEllipseReportElementIcon()
    {
        return ellipseReportElementIcon;
    }


    @NotNull
    public ImageIcon getImageFieldReportElementIcon()
    {
        return imageFieldReportElementIcon;
    }

    @NotNull
    public ImageIcon getImageURLFieldReportElementIcon()
    {
        return imageURLFieldReportElementIcon;
    }


    @NotNull
    public ImageIcon getAnchorFieldReportElementIcon()
    {
        return anchorFieldReportElementIcon;
    }


    @Nullable
    public ImageIcon getWizardPageGroupIcon()
    {
        return null;
    }


    @Nullable
    public ImageIcon getWizardPageConnectionSettingsIcon()
    {
        return null;
    }


    @NotNull
    public ImageIcon getWizardPageWelcomeIcon()
    {
        return getReportWizardIcon();
    }


    @NotNull
    public ImageIcon getWizardPageSQLQueryIcon()
    {
        return getReportWizardIcon();
    }


    @NotNull
    public ImageIcon getWizardPageFieldExpressionsIcon()
    {
        return getReportWizardIcon();
    }


    @NotNull
    public ImageIcon getWizardPagePageSetupIcon()
    {
        return getReportWizardIcon();
    }


    @NotNull
    public ImageIcon getWizardIcon()
    {
        return wizardIcon;
    }


    @NotNull
    public ImageIcon getReportWizardIcon()
    {
        return reportWizardIcon;
    }


    @NotNull
    public ImageIcon getTemplateColoredSampleIcon()
    {
        return templateColoredSampleIcon;
    }


    @NotNull
    public ImageIcon getTemplateGridSampleIcon()
    {
        return templateGridSampleIcon;
    }


    @NotNull
    public ImageIcon getAboutIcon()
    {
        return aboutIcon;
    }


    @NotNull
    public ImageIcon getAboutDialogPicture()
    {
        return aboutDialogPicture;
    }


    @NotNull
    public ImageIcon getNoErrorIcon()
    {
        return noErrorIcon;
    }


    @NotNull
    public ImageIcon getTemplateIndentedIcon()
    {
        return templateIndentedIcon;
    }


    @NotNull
    public ImageIcon getTemplateStructuredIcon()
    {
        return templateStructuredIcon;
    }


    @NotNull
    public ImageIcon getLanguageIcon32()
    {
        return languageIcon32;
    }


    @NotNull
    public ImageIcon getBrowserIcon32()
    {
        return browserIcon32;
    }


    @NotNull
    public ImageIcon getNetworkIcon32()
    {
        return networkIcon32;
    }


    @NotNull
    public ImageIcon getVisitOnlineHelpPDFIcon()
    {
        return onlineHelpPDFIcon;
    }


    @NotNull
    public ImageIcon getVisitOnlineHelpHTMLIcon()
    {
        return onlineHelpHTMLIcon;
    }


    @NotNull
    public ImageIcon getVisitOnlineForumIcon()
    {
        return onlineForumIcon;
    }


    @NotNull
    public ImageIcon getShowLogIcon()
    {
        return newIcon;
    }


    @NotNull
    public ImageIcon getPublishToServerIcon()
    {
        return publishToServerIcon;
    }


    @NotNull
    public ImageIcon getResourceLabelReportElementIcon()
    {
        return resourceLabelReportElementIcon;
    }


    @NotNull
    public ImageIcon getResourceMessageReportElementIcon()
    {
        return resourceMessageReportElementIcon;
    }


    @NotNull
    public ImageIcon getGroupIcon()
    {
        return groupIcon;
    }


    @NotNull
    public ImageIcon getSortAscendingIcon()
    {
        return sortAscendingIcon;
    }


    @NotNull
    public ImageIcon getSortDescendingIcon()
    {
        return sortDescendingIcon;
    }


    @NotNull
    public Icon getSubReportElementIcon()
    {
        return subReportElementIcon;
    }


    @NotNull
    public ImageIcon getChartReportElementIcon()
    {
        return chartReportElementIcon;
    }
}
