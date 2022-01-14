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
package org.pentaho.reportdesigner.crm.report.reportexporter.jfreereport;

import org.apache.commons.lang.StringUtils;
import org.gjt.xpp.XmlPullNode;
import org.gjt.xpp.XmlPullParser;
import org.gjt.xpp.XmlPullParserFactory;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jfree.report.AbstractReportDefinition;
import org.jfree.report.DataFactory;
import org.jfree.report.DataRow;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.ReportDataFactoryException;
import org.jfree.report.TableDataFactory;
import org.jfree.report.modules.gui.base.PreviewFrame;
import org.jfree.report.modules.parser.ext.factory.base.ArrayClassFactory;
import org.jfree.report.modules.parser.ext.factory.base.URLClassFactory;
import org.jfree.report.modules.parser.ext.factory.datasource.DefaultDataSourceFactory;
import org.jfree.report.modules.parser.ext.factory.elements.DefaultElementFactory;
import org.jfree.report.modules.parser.ext.factory.objects.BandLayoutClassFactory;
import org.jfree.report.modules.parser.ext.factory.objects.DefaultClassFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.DefaultStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.stylekey.PageableLayoutStyleKeyFactory;
import org.jfree.report.modules.parser.ext.factory.templates.DefaultTemplateCollection;
import org.jfree.report.modules.parser.extwriter.ReportWriter;
import org.jfree.report.util.ReportConfiguration;
import org.jfree.xml.Parser;
import org.pentaho.reportdesigner.crm.report.ReportDialog;
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MultiDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.StaticImageReportElement;
import org.pentaho.reportdesigner.crm.report.model.SubReport;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportExporter;
import org.pentaho.reportdesigner.crm.report.settings.WorkspaceSettings;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;

import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 28.10.2005
 * Time: 08:37:52
 */
public class JFreeReportFileExporter extends ReportExporter
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(JFreeReportFileExporter.class.getName());

    @Nullable
    private File reportFile;
    @Nullable
    private File xactionFile;
    @Nullable
    private String reportDescription;
    private boolean exportXActionFile;
    @Nullable
    private String reportNameString;
    @Nullable
    private String type;
    private boolean useJNDIName;


    public JFreeReportFileExporter(@Nullable File file, @Nullable File xactionFile, boolean exportXActionFile, @Nullable String reportNameString, @Nullable String reportDescription, @Nullable String type, boolean useJNDIName)
    {
        this.reportFile = file;
        this.xactionFile = xactionFile;
        this.exportXActionFile = exportXActionFile;
        this.reportNameString = reportNameString;
        this.reportDescription = reportDescription;
        this.type = type;
        this.useJNDIName = useJNDIName;
    }


    public void createReport(@NotNull ReportDialog reportDialog, @NotNull Report report, @NotNull TableModel tableModel) throws ReportCreationException
    {
        JFreeReportVisitor reportVisitor = new JFreeReportVisitor();
        report.accept(null, reportVisitor);
        JFreeReport jFreeReport = reportVisitor.getJFreeReport();

        if (jFreeReport.getDataFactory() instanceof NullDataFactory)
        {
            jFreeReport.setDataFactory(new TableDataFactory(ReportDialogConstants.DEFAULT_DATA_FACTORY, tableModel));
        }

        JFreeReportBoot.getInstance().start();

        final PreviewFrame preview = new PreviewFrame(jFreeReport);
        preview.setIconImage(reportDialog.getIconImage());
        preview.setTitle(reportDialog.getTitle());

        if (!WorkspaceSettings.getInstance().restoreFrameBounds(preview, "PreviewFrame"))
        {
            preview.setSize(800, 600);
        }

        preview.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(@NotNull WindowEvent e)
            {
                WorkspaceSettings.getInstance().storeFrameBounds(preview, "PreviewFrame");
            }
        });

        preview.setVisible(true);
    }


    public void exportReport(boolean isSubReport, @NotNull Report report) throws PublishException
    {
        if (report == null)
        {
            throw new IllegalArgumentException("report must not be null");
        }

        try
        {
            //create a copy, so we can modify names, some paths and so on without affecting the original report displayed in the designer
            Report newReport = createDeepCopy(report);
            newReport.setName(reportNameString);

            File tempDirectory = reportFile.getParentFile();//NON-NLS
            tempDirectory.mkdirs();

            // write the images out first, because we will be updating the image elements
            // this must be done before the report is written (or it will not be updated when saved)
            byte[] buffer = new byte[8192];
            try
            {
                ArrayList<StaticImageReportElement> imageElements = new ArrayList<StaticImageReportElement>();
                getImageElements(newReport, imageElements);
                for (int i = 0; i < imageElements.size(); i++)
                {
                    // roll through all images
                    StaticImageReportElement imageElement = imageElements.get(i);
                    // for all file urls, update them to the new location and write their contents as well t0 the "tempDirectory" from above
                    if (!"http".equals(imageElement.getUrl().getProtocol()))//NON-NLS
                    {
                        InputStream is = imageElement.getUrl().openStream();
                        FileOutputStream fos = null;
                        File destFile = new File(tempDirectory, reportNameString + "_staticImage" + i + imageElement.getUrl().getFile().substring(imageElement.getUrl().getFile().lastIndexOf('.')));//NON-NLS
                        try
                        {
                            //noinspection IOResourceOpenedButNotSafelyClosed
                            fos = new FileOutputStream(destFile);
                            int len;
                            while ((len = is.read(buffer)) != -1)
                            {
                                fos.write(buffer, 0, len);
                            }
                        }
                        finally
                        {
                            IOUtil.closeStream(is);
                            IOUtil.closeStream(fos);
                        }
                        imageElement.setUrl(destFile.toURI().toURL());
                    }
                }
            }
            catch (Throwable e)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "JFreeReportPublishOnServerExporter.exportReport ", e);
            }


            JFreeReportVisitor reportVisitor = new JFreeReportVisitor();

            newReport.accept(null, reportVisitor);

            JFreeReport jFreeReport = reportVisitor.getJFreeReport();

            final ReportConfiguration config = new ReportConfiguration(jFreeReport.getReportConfiguration());
            config.setConfigProperty(Parser.CONTENTBASE_KEY, new File(".").toURI().toURL().toExternalForm());
            jFreeReport.getReportConfiguration().setConfigProperty("org.jfree.report.NoPrinterAvailable", Boolean.TRUE.toString());

            // MB - 6/9/07 - Hack Alert PRD-144
            //
            // There is a bug in the JFreeReport writer for the XML that causes an
            // empty <data-factory/> tag to be written to the exported XML. As a
            // result, the report cannot be read in and parsed by the platform. So,
            // I put in the following hack which needs to be removed when the JFreeReport
            // engine has been properly fixed.
            //
            hackReport(jFreeReport);

            if (newReport.isUseMaxCharBounds())
            {
                config.setConfigProperty("org.jfree.report.layout.fontrenderer.UseMaxCharBounds", Boolean.TRUE.toString());//NON-NLS
            }

            final ReportWriter writer = new ReportWriter(jFreeReport, XMLConstants.ENCODING, config);
            writer.addClassFactoryFactory(new URLClassFactory());
            writer.addClassFactoryFactory(new DefaultClassFactory());
            writer.addClassFactoryFactory(new BandLayoutClassFactory());
            writer.addClassFactoryFactory(new ArrayClassFactory());

            writer.addStyleKeyFactory(new DefaultStyleKeyFactory());
            writer.addStyleKeyFactory(new PageableLayoutStyleKeyFactory());
            writer.addTemplateCollection(new DefaultTemplateCollection());
            writer.addElementFactory(new DefaultElementFactory());
            writer.addDataSourceFactory(new DefaultDataSourceFactory());

            //export the JFreeReport xml
            Writer outputStreamWriter = null;
            try
            {
                //noinspection IOResourceOpenedButNotSafelyClosed
                outputStreamWriter = new OutputStreamWriter(new FileOutputStream(reportFile), XMLConstants.ENCODING);
                writer.write(outputStreamWriter);
            }
            finally
            {
                IOUtil.closeStream(outputStreamWriter);
            }

            //export the xaction xml
            try
            {
                if (exportXActionFile)
                {
                    //noinspection IOResourceOpenedButNotSafelyClosed
                    outputStreamWriter = new OutputStreamWriter(new FileOutputStream(xactionFile), XMLConstants.ENCODING);
                }
                DataSetsReportElement dataSetsReportElement = newReport.getDataSetsReportElement();
                String xactionName = xactionFile != null ? xactionFile.getName() : "";
                String xactionContent = XActionHelper.getXActionFile(xactionName, tempDirectory.getName(), newReport.getName(), reportDescription, type, reportFile.getName(), useJNDIName, dataSetsReportElement);

                if (exportXActionFile)
                {
                    outputStreamWriter.write(xactionContent);
                }
            }
            finally
            {
                IOUtil.closeStream(outputStreamWriter);
            }

            //prepare the XQuery data file
            //prepare the mondrian definition file
            //prepare the local image files

            ArrayList<File> filesToCopyToServer = new ArrayList<File>();

            // export all mondrian cube files and xquery data files
            for (ReportElement reportElement : newReport.getDataSetsReportElement().getChildren()) {
              if (reportElement instanceof MultiDataSetReportElement) {
                MultiDataSetReportElement multiDataSetReportElement = (MultiDataSetReportElement) reportElement;
                if (!StringUtils.isEmpty(multiDataSetReportElement.getMondrianCubeDefinitionFile())) {
                  filesToCopyToServer.add(new File(multiDataSetReportElement.getMondrianCubeDefinitionFile()));
                }
                if (!StringUtils.isEmpty(multiDataSetReportElement.getXQueryDataFile())) {
                  filesToCopyToServer.add(new File(multiDataSetReportElement.getXQueryDataFile()));
                }
              }
            }            

            //copy everything to the location
            for (File fileToCopy : filesToCopyToServer)
            {
                File destFile = new File(tempDirectory, fileToCopy.getName());
                FileInputStream fis = null;
                FileOutputStream fos = null;
                try
                {
                    //noinspection IOResourceOpenedButNotSafelyClosed
                    fis = new FileInputStream(fileToCopy);
                    //noinspection IOResourceOpenedButNotSafelyClosed
                    fos = new FileOutputStream(destFile);

                    int len;
                    while ((len = fis.read(buffer)) != -1)
                    {
                        fos.write(buffer, 0, len);
                    }
                }
                finally
                {
                    IOUtil.closeStream(fis);
                    IOUtil.closeStream(fos);
                }
            }
        }
        catch (Exception e)
        {
            throw new PublishException(e.getMessage(), e);
        }
    }


    @NotNull
    private Report createDeepCopy(@NotNull Report report) throws Exception
    {
        //this IO stuff needs no native resources, so don't bother closing streams
        ByteArrayOutputStream baos = new ByteArrayOutputStream(50 * 1024);//50 KB
        XMLWriter xmlWriter = new XMLWriter(baos, true);
        xmlWriter.writeDefaultProlog();

        XMLContext xmlContext = new XMLContext();

        if (report instanceof SubReport)
        {
            xmlWriter.startElement(XMLConstants.SUBREPORT);
            report.externalizeObject(xmlWriter, xmlContext);
            xmlWriter.closeElement(XMLConstants.SUBREPORT);
        }
        else
        {
            xmlWriter.startElement(XMLConstants.REPORT);
            report.externalizeObject(xmlWriter, xmlContext);
            xmlWriter.closeElement(XMLConstants.REPORT);
        }

        xmlWriter.close();

        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
        //noinspection IOResourceOpenedButNotSafelyClosed
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()), XMLConstants.ENCODING));
        xmlPullParser.setInput(bufferedReader);
        xmlPullParser.next(); // get first start tag
        XmlPullNode node = xmlPullParserFactory.newPullNode(xmlPullParser);

        Report newReport = ReportElementInfoFactory.getInstance().getReportReportElementInfo().createReportElement();

        if (XMLConstants.REPORT.equals(node.getRawName()))
        {
            newReport.readObject(node, xmlContext);
        }

        node.resetPullNode();

        return newReport;
    }


    /*
    * MB - 6/9/07
    *
    * This method is temporary, and clearly a hack. The goal is to satisfy a bug
    * in the writer for the report spec - essentially, the two conditions that
    * must be satisfied fort the writer to not write out an empty data-factory
    * tag. The bug is in org.jfree.report.modules.parser.reportwriter.DataFactoryWriter,
    * and it has the following flawed logic:
    *
    * Line 119:
    * if (query == null && dataFactoryClass == null)
    *
    * should be
    *
    * if (query == null || dataFactoryClass == null)
    *
    * As soon as this gets fixed, the hack can be removed. Until then, I have to be sure
    * that the dataFactoryClass is null AND the query is null. The setter for dataFactory
    * now prevents a null set (unlike 0.8.8_01). But, I can satisfy this part of the bug by
    * setting a DataFactory that doesn't have a public constructor.
    *
    * The next part of the hack is that the report.query must be null. Well, the API prevents
    * me from calling report.setQuery(null). The only way to satisfy the problem is to set the
    * field to null. I use reflection below to fix this - first, I get the query field, then
    * I force it to be a public variable (setAccessible), and then I set the value to null.
    *
    * Once these steps are done, then when the report gets written out as XML, the bug will
    * be worked around.
    *
    */
    private void hackReport(@NotNull JFreeReport report)
    {
        try
        {
            // First part of the hack - set the data factory to a class that
            // has no public constructor.
            report.setDataFactory(new PlatformDataFactory());

            // Second part of the hack - I need to make sure the query
            // field is null so that an empty data-factory tag doesn't get
            // written to the xml file. This is such a hack, but it shouldn't
            // last for very long.
            Class c2 = AbstractReportDefinition.class;
            Field query = c2.getDeclaredField("query"); //$NON-NLS-1$
            query.setAccessible(true);
            query.set(report, null);

        }
        catch (Exception ex)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "JFreeReportFileExporter.hackReport ", ex);
        }

    }


    public static class PlatformDataFactory implements DataFactory
    {
        @Nullable
        public TableModel queryData(@Nullable final String query, @Nullable final DataRow parameters)
                throws ReportDataFactoryException
        {
            return null;
        }


        @Nullable
        public DataFactory derive() throws ReportDataFactoryException
        {
            return null;
        }


        public void open()
        {
        }


        public void close()
        {

        }


        private PlatformDataFactory()
        {
            super();
        }
    }
}
