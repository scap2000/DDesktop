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

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
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
import org.jfree.report.ReportDataFactoryException;
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
import org.pentaho.reportdesigner.crm.report.ReportDialogConstants;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.JNDISource;
import org.pentaho.reportdesigner.crm.report.datasetplugin.multidataset.MultiDataSetReportElement;
import org.pentaho.reportdesigner.crm.report.model.Report;
import org.pentaho.reportdesigner.crm.report.model.ReportElement;
import org.pentaho.reportdesigner.crm.report.model.StaticImageReportElement;
import org.pentaho.reportdesigner.crm.report.model.SubReport;
import org.pentaho.reportdesigner.crm.report.model.SubReportElement;
import org.pentaho.reportdesigner.crm.report.model.dataset.DataSetsReportElement;
import org.pentaho.reportdesigner.crm.report.reportelementinfo.ReportElementInfoFactory;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportExporter;
import org.pentaho.reportdesigner.crm.report.util.XMLContextKeys;
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.util.IOUtil;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;

import javax.swing.table.TableModel;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin Date: 28.10.2005 Time: 08:37:52
 */
@SuppressWarnings( { "ALL" })
// TODO temporary
public class JFreeReportPublishOnServerExporter extends ReportExporter {
  @NonNls
  @NotNull
  private static final Logger LOG = Logger.getLogger(JFreeReportPublishOnServerExporter.class.getName());

  @NotNull
  private String reportNameString;

  @NotNull
  private String publishLocation;

  @NotNull
  private String type;

  @NotNull
  private String webPublishURL;

  private boolean publishXActionFile;

  @NotNull
  private char[] publishPassword;

  @NotNull
  private String serverUserId;

  @NotNull
  private char[] serverPassword;

  private boolean useJNDIName;

  @NotNull
  private String message = "";

  public JFreeReportPublishOnServerExporter(@NotNull
  String reportNameString, @NotNull
  String publishLocation, @NotNull
  String type, @NotNull
  String webPublishURL, boolean publishXActionFile, @NotNull
  char[] publishPassword, @NotNull
  String serverUserId, @NotNull
  char[] serverPassword, boolean useJNDIName) {
    this.reportNameString = reportNameString;
    this.publishLocation = publishLocation;
    this.type = type;
    this.webPublishURL = webPublishURL;
    this.publishXActionFile = publishXActionFile;
    this.publishPassword = publishPassword;
    this.serverUserId = serverUserId;
    this.serverPassword = serverPassword;
    this.useJNDIName = useJNDIName;
  }

  public Report loadReport(File reportFile) {
    Report report = null;
    try {
      BufferedReader bufferedReader = null;
      XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
      XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
      // noinspection IOResourceOpenedButNotSafelyClosed
      bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(reportFile), XMLConstants.ENCODING));
      xmlPullParser.setInput(bufferedReader);
        XMLContext xmlContext = new XMLContext();
        XMLContextKeys.CONTEXT_PATH.putObject(xmlContext, reportFile.getParentFile());
      XmlPullNode node;
      xmlPullParser.next(); // get first start tag
      node = xmlPullParserFactory.newPullNode(xmlPullParser);
      // Thread.sleep(15000);//for testing purposes only
      report = ReportElementInfoFactory.getInstance().getReportReportElementInfo().createReportElement();
      if (XMLConstants.REPORT.equals(node.getRawName())) {
        report.readObject(node, xmlContext);
      }
      node.resetPullNode();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return report;
  }

  public void exportReport(boolean isSubReport, @NotNull
  Report report) throws PublishException {
    if (report == null) {
      throw new IllegalArgumentException("report must not be null");
    }
    try {
      // create a copy, so we can modify names, some paths and so on without affecting the original report displayed in
      // the designer
      Report newReport = createDeepCopy(report);
      newReport.setName(reportNameString);
      ArrayList<File> filesToCopyToServer = new ArrayList<File>();
      File reportDirectory = new File(System.getProperty(ReportDialogConstants.USER_HOME), ReportDialogConstants.REPORT_DIRECTORY);
      File tempDirectory = new File(reportDirectory, "temp");// NON-NLS
      tempDirectory.mkdir();
      //
      // MB - Publish Location should NOT start with a leading "/"
      // PRD-113
      //
      String correctedPublishLocation = publishLocation;
      if (correctedPublishLocation.startsWith("/")) {
        correctedPublishLocation = correctedPublishLocation.substring(1);
      }
      if (!correctedPublishLocation.endsWith("/")) {
        correctedPublishLocation = correctedPublishLocation + "/";
      }
      // write the images out first, because we will be updating the image elements
      // this must be done before the report is written (or it will not be updated when saved)

      ArrayList<SubReportElement> subReportElements = new ArrayList<SubReportElement>();
      getSubReportElements(newReport, subReportElements);
      for (int i = 0; i < subReportElements.size(); i++) {
        SubReportElement subReportElement = subReportElements.get(i);
        // System.out.println(subReportElement.getFilePath().getPath());
      }

      byte[] buffer = new byte[8192];
      try {
        ArrayList<StaticImageReportElement> imageElements = new ArrayList<StaticImageReportElement>();
        getImageElements(newReport, imageElements);
        URL serverURL = new URL(webPublishURL);
        for (int i = 0; i < imageElements.size(); i++) {
          // roll through all images
          StaticImageReportElement imageElement = imageElements.get(i);
          if (!"http".equals(imageElement.getUrl().getProtocol()))// NON-NLS
          {
            // for all file urls, update them to the new location and write their contents as well to the
            // "tempDirectory" from above
            InputStream is = imageElement.getUrl().openStream();
            FileOutputStream fos = null;
            File destFile = new File(tempDirectory, newReport.getName().replace(' ', '_') + "_staticImage" + i + imageElement.getUrl().getFile().substring(imageElement.getUrl().getFile().lastIndexOf('.')));// NON-NLS
            try {
              // noinspection IOResourceOpenedButNotSafelyClosed
              fos = new FileOutputStream(destFile);
              int len;
              while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
              }
              filesToCopyToServer.add(destFile);
            } finally {
              IOUtil.closeStream(is);
              IOUtil.closeStream(fos);
            }
            // create the URL the best we can, taking advantage of the platform putting in parser-config settings
            // the setting we are targetting is ${hostColonPort}
            URL url = new URL(serverURL.getProtocol() + "://${hostColonPort}/pentaho/GetResource?resource=" + correctedPublishLocation + destFile.getName());// NON-NLS
            imageElement.setUrl(url);
          }
        }
      } catch (Throwable e) {
        if (LOG.isLoggable(Level.FINE))
          LOG.log(Level.FINE, "JFreeReportPublishOnServerExporter.exportReport ", e);
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

      if (newReport.isUseMaxCharBounds()) {
        config.setConfigProperty("org.jfree.report.layout.fontrenderer.UseMaxCharBounds", Boolean.TRUE.toString());// NON-NLS
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
      // export the JFreeReport xml
      File jFreeReportFile = new File(tempDirectory, newReport.getName() + ReportDialogConstants.XML_FILE_ENDING);// NON-NLS
      OutputStreamWriter outputStreamWriter = null;
      try {
        // noinspection IOResourceOpenedButNotSafelyClosed
        outputStreamWriter = new OutputStreamWriter(new FileOutputStream(jFreeReportFile), XMLConstants.ENCODING);
        writer.write(outputStreamWriter);
      } finally {
        IOUtil.closeStream(outputStreamWriter);
      }
      // export the xaction xml
      File xactionFile = new File(tempDirectory, newReport.getName() + ReportDialogConstants.XACTION_FILE_ENDING);// NON-NLS
      try {
        // noinspection IOResourceOpenedButNotSafelyClosed
        outputStreamWriter = new OutputStreamWriter(new FileOutputStream(xactionFile), XMLConstants.ENCODING);
        DataSetsReportElement dataSetsReportElement = newReport.getDataSetsReportElement();
        
        String publishSolutionLocation = correctedPublishLocation.substring(0, correctedPublishLocation.indexOf("/"));
        
        String xactionContent = XActionHelper.getXActionFile(xactionFile.getName(), publishSolutionLocation, newReport.getName(), "", type, jFreeReportFile.getName(), useJNDIName, dataSetsReportElement);
        outputStreamWriter.write(xactionContent);
      } finally {
        IOUtil.closeStream(outputStreamWriter);
      }
      
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
      // export the properties
      Properties properties = new Properties();
      properties.setProperty("title", newReport.getName());// NON-NLS
      properties.setProperty("description", "");// NON-NLS
      ArrayList<ReportElement> resourceElements = new ArrayList<ReportElement>();
      getResourceElements(newReport, resourceElements);
      FileFilter bundleFilter = new FileFilter() {
        public boolean accept(File pathname) {
          return pathname.getName().endsWith(".properties");// NON-NLS
        }
      };
      if (newReport.getResourceBundleClasspath() != null) {
        File folder = new File(newReport.getResourceBundleClasspath().toURI());
        // give me a list of *ALL* resource files for this guys resource base
        File[] propertiesFiles = folder.listFiles(bundleFilter);
          if (propertiesFiles!=null)
          {
              filesToCopyToServer.addAll(Arrays.asList(propertiesFiles));
          }

      }
      File propertiesFile = new File(tempDirectory, URLEncoder.encode(newReport.getName(), "UTF-8") + ".properties");// NON-NLS
      FileOutputStream out = null;
      try {
        // noinspection IOResourceOpenedButNotSafelyClosed
        out = new FileOutputStream(propertiesFile);
        properties.store(out, null);
      } finally {
        IOUtil.closeStream(out);
      }
      // prepare the XQuery data file
      // prepare the mondrian definition file
      // prepare the local image files
      filesToCopyToServer.add(jFreeReportFile);
      if (publishXActionFile) {
        filesToCopyToServer.add(xactionFile);
      }
      filesToCopyToServer.add(propertiesFile);
      // copy everyting to the server
      publish(webPublishURL, correctedPublishLocation, filesToCopyToServer.toArray(new File[filesToCopyToServer.size()]), null);
    } catch (Exception e) {
      throw new PublishException(e.getMessage(), e);
    }
  }

  @NotNull
  public String getMessage() {
    return message;
  }

  @NotNull
  private Report createDeepCopy(@NotNull
  Report report) throws Exception {
    // this IO stuff needs no native resources, so don't bother closing streams
    ByteArrayOutputStream baos = new ByteArrayOutputStream(50 * 1024);// 50 KB
      XMLContext xmlContext = new XMLContext();
    XMLWriter xmlWriter = new XMLWriter(baos, true);
    xmlWriter.writeDefaultProlog();
    if (report instanceof SubReport) {
      xmlWriter.startElement(XMLConstants.SUBREPORT);
      report.externalizeObject(xmlWriter, xmlContext);
      xmlWriter.closeElement(XMLConstants.SUBREPORT);
    } else {
      xmlWriter.startElement(XMLConstants.REPORT);
      report.externalizeObject(xmlWriter, xmlContext);
      xmlWriter.closeElement(XMLConstants.REPORT);
    }
    xmlWriter.close();
    XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
    XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
    // noinspection IOResourceOpenedButNotSafelyClosed
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(baos.toByteArray()), XMLConstants.ENCODING));
    xmlPullParser.setInput(bufferedReader);
    xmlPullParser.next(); // get first start tag
    XmlPullNode node = xmlPullParserFactory.newPullNode(xmlPullParser);
    Report newReport = ReportElementInfoFactory.getInstance().getReportReportElementInfo().createReportElement();
    if (XMLConstants.REPORT.equals(node.getRawName())) {
      newReport.readObject(node, xmlContext);
    }
    node.resetPullNode();
    return newReport;
  }

  private void publish(@NotNull
  String publishURL, @NotNull
  String publishPath, @NotNull
  File[] publishFiles, @Nullable
  JNDISource dataSource) throws PublishException {
    String fullURL = publishURL + "?publishPath=" + publishPath;// NON-NLS
    fullURL += "&publishKey=" + getPasswordKey(new String(publishPassword)); //$NON-NLS-1$
    fullURL += "&overwrite=true"; //$NON-NLS-1$
    if (dataSource != null) {
      fullURL = fullURL + "&jndiName=" + dataSource.getJndiName();// NON-NLS
      fullURL = fullURL + "&jdbcDriver=" + dataSource.getDriverClass();// NON-NLS
      fullURL = fullURL + "&jdbcUrl=" + dataSource.getConnectionString();// NON-NLS
      fullURL = fullURL + "&jdbcUserId=" + dataSource.getUsername();// NON-NLS
      fullURL = fullURL + "&jdbcPassword=" + dataSource.getPassword();// NON-NLS
    }
    PostMethod filePost = new PostMethod(fullURL);
    ArrayList<Part> parts = new ArrayList<Part>();
    try {
      String[] imagesInJars = new String[] { "PentahoReporting.png", "PentahoReportingMDX.png", "PentahoReportingXQuery.png" };// NON-NLS
      for (String imageName : imagesInJars) {
        parts.add(new FilePart(imageName, new ByteArrayPartSource(imageName, getImageData("/res/icons/" + imageName))));// NON-NLS
      }
      for (File publishFile : publishFiles) {
        parts.add(new FilePart(publishFile.getName(), publishFile));
      }
    } catch (FileNotFoundException e) {
      // file is not existing or not readable, this should not happen
      UncaughtExcpetionsModel.getInstance().addException(e);
    } catch (IOException e) {
      UncaughtExcpetionsModel.getInstance().addException(e);
    }
    filePost.setRequestEntity(new MultipartRequestEntity(parts.toArray(new Part[parts.size()]), filePost.getParams()));
    HttpClient client = new HttpClient();
    // If server userid/password was supplied, use basic authentication to
    // authenticate with the server.
    if (serverUserId != null && serverUserId.length() > 0 && serverPassword != null && serverPassword.length > 0) {
      Credentials creds = new UsernamePasswordCredentials(serverUserId, new String(serverPassword));
      client.getState().setCredentials(AuthScope.ANY, creds);
      client.getParams().setAuthenticationPreemptive(true);
    }
    int status;
    try {
      status = client.executeMethod(filePost);
    } catch (IOException e) {
      throw new PublishException(e.getMessage(), e);
    }
    if (status != HttpStatus.SC_OK) {
      if (status == HttpStatus.SC_MOVED_TEMPORARILY) {
        throw new PublishException(TranslationManager.getInstance().getTranslation("R", "JFreeReportPublishOnServerExporter.InvalidUsernameOrPassword"));
      } else {
        throw new PublishException("Unknown server error: HTTP status code " + status);
      }
    } else {
      try {
        String postResult = filePost.getResponseBodyAsString();
        int rtn = Integer.parseInt(postResult.trim());
        if (rtn == 3) {
          message = TranslationManager.getInstance().getTranslation("R", "JFreeReportPublishOnServerExporter.Successful");
        } else if (rtn == 2) {
          message = TranslationManager.getInstance().getTranslation("R", "JFreeReportPublishOnServerExporter.Failed");
        } else if (rtn == 4) {
          message = TranslationManager.getInstance().getTranslation("R", "JFreeReportPublishOnServerExporter.InvalidPassword");
        } else if (rtn == 5) {
          message = TranslationManager.getInstance().getTranslation("R", "JFreeReportPublishOnServerExporter.InvalidUsernameOrPassword");
        } else if (rtn == 1) {
          message = TranslationManager.getInstance().getTranslation("R", "JFreeReportPublishOnServerExporter.FileExistsOverride");
        }
      } catch (IOException e) {
        throw new PublishException(e);
      }
    }
  }

  /**
   * Utility for getting the MD5 hash from the provided key for sending the publishPassword.
   * 
   * @param passWord
   *          The password to get an MD5 hash of
   * @return zero-padded MD5 hash of the password
   */
  @Nullable
  public static String getPasswordKey(@NotNull
  String passWord) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");// NON-NLS
      md.reset();
      md.update(passWord.getBytes("UTF-8"));// NON-NLS
      byte[] digest = md.digest("P3ntah0Publ1shPa55w0rd".getBytes("UTF-8"));// NON-NLS
      StringBuilder buf = new StringBuilder(digest.length + 1);
      String s;
      for (byte aDigest : digest) {
        s = Integer.toHexString(0xFF & aDigest);
        buf.append((s.length() == 1) ? "0" : "").append(s);
      }
      return buf.toString();
    } catch (Exception ex) {
      UncaughtExcpetionsModel.getInstance().addException(ex);
    }
    return null;
  }

  @NotNull
  private byte[] getImageData(@NotNull
  String imageName) throws IOException, PublishException {
    InputStream resourceAsStream = null;
    try {
      resourceAsStream = getClass().getResourceAsStream(imageName);
      if (resourceAsStream == null) {
        throw new PublishException("Resource is missing " + imageName);
      }
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buffer = new byte[8192];
      int len;
      while ((len = resourceAsStream.read(buffer)) != -1) {
        baos.write(buffer, 0, len);
      }
      return baos.toByteArray();
    } finally {
      IOUtil.closeStream(resourceAsStream);
    }
  }

  /*
   * MB - 6/9/07
   * 
   * This method is temporary, and clearly a hack. The goal is to satisfy a bug in the writer for the report spec - essentially, the two conditions that must be satisfied fort the writer to not write out an empty data-factory tag. The bug
   * is in org.jfree.report.modules.parser.reportwriter.DataFactoryWriter, and it has the following flawed logic:
   * 
   * Line 119: if (query == null && dataFactoryClass == null)
   * 
   * should be
   * 
   * if (query == null || dataFactoryClass == null)
   * 
   * As soon as this gets fixed, the hack can be removed. Until then, I have to be sure that the dataFactoryClass is null AND the query is null. The setter for dataFactory now prevents a null set (unlike 0.8.8_01). But, I can satisfy this
   * part of the bug by setting a DataFactory that doesn't have a public constructor.
   * 
   * The next part of the hack is that the report.query must be null. Well, the API prevents me from calling report.setQuery(null). The only way to satisfy the problem is to set the field to null. I use reflection below to fix this - first,
   * I get the query field, then I force it to be a public variable (setAccessible), and then I set the value to null.
   * 
   * Once these steps are done, then when the report gets written out as XML, the bug will be worked around.
   * 
   */
  private void hackReport(JFreeReport report) {
    try {
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

    } catch (Exception ex) {
      if (LOG.isLoggable(Level.FINE))
        LOG.log(Level.FINE, "JFreeReportPublishOnServerExporter.hackReport ", ex);
    }
  }

  public static class PlatformDataFactory implements DataFactory {
    public TableModel queryData(final String query, final DataRow parameters) throws ReportDataFactoryException {
      return null;
    }

    public DataFactory derive() throws ReportDataFactoryException {
      return null;
    }

    public void open() {
    }

    public void close() {

    }

    private PlatformDataFactory() {
      super();
    }
  }

}
