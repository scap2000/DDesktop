/**
 * ===========================================
 * JFreeReport : a free Java reporting library
 * ===========================================
 *
 * Project Info:  http://reporting.pentaho.org/
 *
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ------------
 * HtmlPrinter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.html;

import java.awt.Color;
import java.awt.Image;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.net.URL;

import java.text.NumberFormat;

import java.util.HashMap;
import java.util.HashSet;

import org.jfree.fonts.encoding.EncodingRegistry;
import org.jfree.io.IOUtils;
import org.jfree.report.ElementAlignment;
import org.jfree.report.ImageContainer;
import org.jfree.report.InvalidReportStateException;
import org.jfree.report.JFreeReportInfo;
import org.jfree.report.LocalImageContainer;
import org.jfree.report.URLImageContainer;
import org.jfree.report.layout.model.Border;
import org.jfree.report.layout.model.BorderEdge;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.layout.output.ContentProcessingException;
import org.jfree.report.layout.output.LogicalPageKey;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.output.RenderUtility;
import org.jfree.report.modules.output.table.base.SheetLayout;
import org.jfree.report.modules.output.table.base.TableCellDefinition;
import org.jfree.report.modules.output.table.base.TableContentProducer;
import org.jfree.report.modules.output.table.base.TableRectangle;
import org.jfree.report.modules.output.table.html.helper.GlobalStyleManager;
import org.jfree.report.modules.output.table.html.helper.InlineStyleManager;
import org.jfree.report.modules.output.table.html.helper.StyleBuilder;
import org.jfree.report.modules.output.table.html.helper.StyleManager;
import org.jfree.report.modules.output.table.html.util.HtmlColors;
import org.jfree.report.resourceloader.ImageFactory;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.style.TextStyleKeys;
import org.jfree.report.util.MemoryByteArrayOutputStream;
import org.jfree.report.util.MemoryStringWriter;
import org.jfree.report.util.geom.StrictGeomUtility;
import org.jfree.repository.ContentCreationException;
import org.jfree.repository.ContentIOException;
import org.jfree.repository.ContentItem;
import org.jfree.repository.ContentLocation;
import org.jfree.repository.LibRepositoryBoot;
import org.jfree.repository.NameGenerator;
import org.jfree.resourceloader.ResourceData;
import org.jfree.resourceloader.ResourceKey;
import org.jfree.resourceloader.ResourceLoadingException;
import org.jfree.resourceloader.ResourceManager;
import org.jfree.util.Configuration;
import org.jfree.util.Log;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.StackableRuntimeException;
import org.jfree.util.StringUtils;
import org.jfree.xmlns.common.AttributeList;
import org.jfree.xmlns.writer.DefaultTagDescription;
import org.jfree.xmlns.writer.XmlWriter;
import org.jfree.xmlns.writer.XmlWriterSupport;

/**
 * This class is the actual HTML-emitter.
 *
 * @author Thomas Morgner
 */
public abstract class HtmlPrinter implements HtmlContentGenerator
{
  private static final String GENERATOR = JFreeReportInfo.getInstance().getName() + " version "
      + JFreeReportInfo.getInstance().getVersion();

  public static final String XHTML_NAMESPACE =
      "http://www.w3.org/1999/xhtml";


  private static final String[] XHTML_HEADER = {
      "<!DOCTYPE html",
      "     PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"",
      "     \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"};

  public static final String TAG_DEF_PREFIX = "org.jfree.layouting.modules.output.html.";


  private Configuration configuration;
  //private OutputProcessorMetaData metaData;

  private XmlWriter xmlWriter;
  private boolean assumeZeroMargins;
  private boolean assumeZeroBorders;
  private boolean assumeZeroPaddings;

  private ContentLocation contentLocation;
  private NameGenerator contentNameGenerator;
  private ContentLocation dataLocation;
  private NameGenerator dataNameGenerator;

  private ResourceManager resourceManager;
  private HashMap knownResources;
  private HashSet validRawTypes;

  private URLRewriter urlRewriter;
  private ContentItem documentContentItem;
  private StyleManager styleManager;
  private boolean allowRawLinkTargets;
  private boolean copyExternalImages;
  private StyleBuilder styleBuilder;
  private static final String[] EMPTY_CELL_ATTRNAMES = new String[]{"font-size"};
  private static final String[] EMPTY_CELL_ATTRVALS = new String[]{"1pt"};

  private MemoryStringWriter bufferWriter;
  private BufferedWriter writer;
  private ContentItem styleFile;
  private String styleFileUrl;

  protected HtmlPrinter(final ResourceManager resourceManager)
  {
    if (resourceManager == null)
    {
      throw new NullPointerException("A resource-manager must be given.");
    }

    this.resourceManager = resourceManager;
    this.knownResources = new HashMap();
    this.styleBuilder = new StyleBuilder();

    this.validRawTypes = new HashSet();
    this.validRawTypes.add("image/gif");
    this.validRawTypes.add("image/x-xbitmap");
    this.validRawTypes.add("image/gi_");
    this.validRawTypes.add("image/jpeg");
    this.validRawTypes.add("image/jpg");
    this.validRawTypes.add("image/jp_");
    this.validRawTypes.add("application/jpg");
    this.validRawTypes.add("application/x-jpg");
    this.validRawTypes.add("image/pjpeg");
    this.validRawTypes.add("image/pipeg");
    this.validRawTypes.add("image/vnd.swiftview-jpeg");
    this.validRawTypes.add("image/x-xbitmap");
    this.validRawTypes.add("image/png");
    this.validRawTypes.add("application/png");
    this.validRawTypes.add("application/x-png");

    assumeZeroMargins = true;
    assumeZeroBorders = true;
    assumeZeroPaddings = true;

    // this primitive implementation assumes that the both repositories are
    // the same ..
    urlRewriter = new FileSystemURLRewriter();
  }

  protected boolean isAllowRawLinkTargets()
  {
    return allowRawLinkTargets;
  }

  protected Configuration getConfiguration()
  {
    return configuration;
  }

  protected boolean isAssumeZeroMargins()
  {
    return assumeZeroMargins;
  }

  protected void setAssumeZeroMargins(final boolean assumeZeroMargins)
  {
    this.assumeZeroMargins = assumeZeroMargins;
  }

  protected boolean isAssumeZeroBorders()
  {
    return assumeZeroBorders;
  }

  protected void setAssumeZeroBorders(final boolean assumeZeroBorders)
  {
    this.assumeZeroBorders = assumeZeroBorders;
  }

  protected boolean isAssumeZeroPaddings()
  {
    return assumeZeroPaddings;
  }

  protected void setAssumeZeroPaddings(final boolean assumeZeroPaddings)
  {
    this.assumeZeroPaddings = assumeZeroPaddings;
  }

  public ContentLocation getContentLocation()
  {
    return contentLocation;
  }

  public NameGenerator getContentNameGenerator()
  {
    return contentNameGenerator;
  }

  public ContentLocation getDataLocation()
  {
    return dataLocation;
  }

  public NameGenerator getDataNameGenerator()
  {
    return dataNameGenerator;
  }

  public void setDataWriter(final ContentLocation dataLocation,
                            final NameGenerator dataNameGenerator)
  {
    this.dataNameGenerator = dataNameGenerator;
    this.dataLocation = dataLocation;
  }

  public void setContentWriter(final ContentLocation contentLocation,
                               final NameGenerator contentNameGenerator)
  {
    this.contentNameGenerator = contentNameGenerator;
    this.contentLocation = contentLocation;
  }

  public ResourceManager getResourceManager()
  {
    return resourceManager;
  }

  public URLRewriter getUrlRewriter()
  {
    return urlRewriter;
  }

  public void setUrlRewriter(final URLRewriter urlRewriter)
  {
    if (urlRewriter == null)
    {
      throw new NullPointerException();
    }
    this.urlRewriter = urlRewriter;
  }

  public ContentItem getDocumentContentItem()
  {
    return documentContentItem;
  }

  public void setDocumentContentItem(final ContentItem documentContentItem)
  {
    this.documentContentItem = documentContentItem;
  }

  public String writeRaw(final ResourceKey source) throws IOException
  {
    if (copyExternalImages == false)
    {
      final Object identifier = source.getIdentifier();
      if (identifier instanceof URL)
      {
        final URL url = (URL) identifier;
        final String protocol = url.getProtocol();
        if ("http".equalsIgnoreCase(protocol) ||
            "https".equalsIgnoreCase(protocol) ||
            "ftp".equalsIgnoreCase(protocol))
        {
          return url.toExternalForm();
        }
      }
    }

    if (dataLocation == null)
    {
      return null;
    }

    try
    {
      final ResourceData resourceData = resourceManager.load(source);
      final String mimeType = queryMimeType(resourceData);
      if (isValidImage(mimeType))
      {
        // lets do some voodo ..
        final ContentItem item = dataLocation.createItem
            (dataNameGenerator.generateName(extractFilename(resourceData), mimeType));
        if (item.isWriteable())
        {
          item.setAttribute(LibRepositoryBoot.REPOSITORY_DOMAIN, LibRepositoryBoot.CONTENT_TYPE, mimeType);

          // write it out ..
          final InputStream stream = new BufferedInputStream(resourceData.getResourceAsStream(resourceManager));
          try
          {
            final OutputStream outputStream = new BufferedOutputStream(item.getOutputStream());
            try
            {
              IOUtils.getInstance().copyStreams(stream, outputStream);
            }
            finally
            {
              outputStream.close();
            }
          }
          finally
          {
            stream.close();
          }

          return urlRewriter.rewrite(documentContentItem, item);
        }
      }
    }
    catch (ResourceLoadingException e)
    {
      // Ok, loading the resource failed. Not a problem, so we will
      // recode the raw-object instead ..
    }
    catch (ContentIOException e)
    {
      // ignore it ..
    }
    catch (URLRewriteException e)
    {
      Log.warn("Rewriting the URL failed.", e);
      throw new StackableRuntimeException("Failed", e);
    }
    return null;
  }


  /**
   * Tests, whether the given URL points to a supported file format for common browsers. Returns true if the URL
   * references a JPEG, PNG or GIF image, false otherwise.
   * <p/>
   * The checked filetypes are the ones recommended by the W3C.
   *
   * @param url the url that should be tested.
   * @return true, if the content type is supported by the browsers, false otherwise.
   */
  protected boolean isSupportedImageFormat(final URL url)
  {
    final String file = url.getFile();
    if (StringUtils.endsWithIgnoreCase(file, ".jpg"))
    {
      return true;
    }
    if (StringUtils.endsWithIgnoreCase(file, ".jpeg"))
    {
      return true;
    }
    if (StringUtils.endsWithIgnoreCase(file, ".png"))
    {
      return true;
    }
    if (StringUtils.endsWithIgnoreCase(file, ".gif"))
    {
      return true;
    }
    return false;
  }

  private byte[] getImageData(final ImageContainer image) throws IOException
  {
    URL url = null;
    // The image has an assigned URL ...
    if (image instanceof URLImageContainer)
    {
      final URLImageContainer urlImage = (URLImageContainer) image;

      url = urlImage.getSourceURL();
      // if we have an source to load the image data from ..
      if (url != null)
      {
        if (urlImage.isLoadable() && isSupportedImageFormat(url))
        {
          final MemoryByteArrayOutputStream bout = new MemoryByteArrayOutputStream();
          final InputStream urlIn = new BufferedInputStream(urlImage.getSourceURL().openStream());
          try
          {
            IOUtils.getInstance().copyStreams(urlIn, bout);
          }
          finally
          {
            urlIn.close();
            bout.close();
          }
          return bout.toByteArray();
        }
      }
    }

    if (image instanceof LocalImageContainer)
    {
      // Check, whether the imagereference contains an AWT image.
      // if so, then we can use that image instance for the recoding
      final LocalImageContainer li = (LocalImageContainer) image;
      Image awtImage = li.getImage();
      if (awtImage == null)
      {
        if (url != null)
        {
          awtImage = ImageFactory.getInstance().createImage(url);
        }
      }
      if (awtImage != null)
      {
        // now encode the image. We don't need to create digest data
        // for the image contents, as the image is perfectly identifyable
        // by its URL
        return RenderUtility.encodeImage(awtImage);
      }
    }
    return null;
  }

  public String writeImage(final ImageContainer image)
      throws ContentIOException, IOException
  {
    if (dataLocation == null)
    {
      return null;
    }

    final byte[] data = getImageData(image);
    if (data == null)
    {
      return null;
    }

    try
    {
      // write the encoded picture ...
      final ContentItem dataFile = dataLocation.createItem
          (dataNameGenerator.generateName("picture", "image/png"));
      final String contentURL = urlRewriter.rewrite(documentContentItem, dataFile);

      // a png encoder is included in JCommon ...
      final OutputStream out = new BufferedOutputStream(dataFile.getOutputStream());
      try
      {
        out.write(data);
        out.flush();
      }
      finally
      {
        out.close();
      }

      return contentURL;
    }
    catch (ContentCreationException cce)
    {
      // Can't create the content
      Log.warn("Failed to create the content image: Reason given was: " + cce.getMessage());
      return null;
    }
    catch (URLRewriteException re)
    {
      // cannot handle this ..
      Log.warn("Failed to write the URL: Reason given was: " + re.getMessage());
      return null;
    }
  }

  private String extractFilename(final ResourceData resourceData)
  {
    final String filename = (String)
        resourceData.getAttribute(ResourceData.FILENAME);
    if (filename == null)
    {
      return "image";
    }

    return IOUtils.getInstance().stripFileExtension(filename);
  }

  private String queryMimeType(final ResourceData resourceData)
      throws ResourceLoadingException, IOException
  {
    final Object contentType =
        resourceData.getAttribute(ResourceData.CONTENT_TYPE);
    if (contentType instanceof String)
    {
      return (String) contentType;
    }

    // now we are getting very primitive .. (Kids, dont do this at home)
    final byte[] data = new byte[12];
    resourceData.getResource(resourceManager, data, 0, data.length);
    final ByteArrayInputStream stream = new ByteArrayInputStream(data);
    if (isGIF(stream))
    {
      return "image/gif";
    }
    stream.reset();
    if (isJPEG(stream))
    {
      return "image/jpeg";
    }
    stream.reset();
    if (isPNG(stream))
    {
      return "image/png";
    }
    return null;
  }

  private boolean isPNG(final ByteArrayInputStream data)
  {
    final int[] PNF_FINGERPRINT = {137, 80, 78, 71, 13, 10, 26, 10};
    for (int i = 0; i < PNF_FINGERPRINT.length; i++)
    {
      if (PNF_FINGERPRINT[i] != data.read())
      {
        return false;
      }
    }
    return true;
  }

  private boolean isJPEG(final InputStream data) throws IOException
  {
    final int[] JPG_FINGERPRINT_1 = {0xFF, 0xD8, 0xFF, 0xE0};
    for (int i = 0; i < JPG_FINGERPRINT_1.length; i++)
    {
      if (JPG_FINGERPRINT_1[i] != data.read())
      {
        return false;
      }
    }
    // then skip two bytes ..
    if (data.read() == -1)
    {
      return false;
    }
    if (data.read() == -1)
    {
      return false;
    }

    final int[] JPG_FINGERPRINT_2 = {0x4A, 0x46, 0x49, 0x46, 0x00};
    for (int i = 0; i < JPG_FINGERPRINT_2.length; i++)
    {
      if (JPG_FINGERPRINT_2[i] != data.read())
      {
        return false;
      }
    }
    return true;
  }

  private boolean isGIF(final InputStream data) throws IOException
  {
    final int[] GIF_FINGERPRINT = {'G', 'I', 'F', '8'};
    for (int i = 0; i < GIF_FINGERPRINT.length; i++)
    {
      if (GIF_FINGERPRINT[i] != data.read())
      {
        return false;
      }
    }
    return true;
  }

  private boolean isValidImage(final String data)
  {
    return validRawTypes.contains(data);
  }

  private boolean isCreateBodyFragment()
  {
    final String key = "org.jfree.report.modules.output.table.html.BodyFragment";
    return "true".equals(getConfiguration().getConfigProperty(key, "false"));
  }

  private boolean isEmptyCellsUseCSS()
  {
    final String key = "org.jfree.report.modules.output.table.html.EmptyCellsUseCSS";
    return "true".equals(getConfiguration().getConfigProperty(key, "false"));
  }

  private boolean isTableRowBorderDefinition()
  {
    final String key = "org.jfree.report.modules.output.table.html.TableRowBorderDefinition";
    return "true".equals(getConfiguration().getConfigProperty(key, "false"));
  }

  private boolean isProportionalColumnWidths()
  {
    final String key = "org.jfree.report.modules.output.table.html.ProportionalColumnWidths";
    return "true".equals(getConfiguration().getConfigProperty(key, "false"));
  }


  private AttributeList createCellAttributes(final TableRectangle rect,
                                             final RenderBox content,
                                             final TableCellDefinition background,
                                             final String[] extraStyleKeys,
                                             final String[] extraStyleValues)
  {
    if (content == null)
    {
      styleBuilder.clear();
    }
    else
    {
      styleBuilder = produceTextStyle(styleBuilder, content, true);
    }

    // Add the extra styles
    if (extraStyleKeys != null
        && extraStyleValues != null
        && extraStyleKeys.length == extraStyleValues.length)
    {
      for (int i = 0; i < extraStyleKeys.length; ++i)
      {
        styleBuilder.append(extraStyleKeys[i], extraStyleValues[i], false);
      }
    }

    if (background != null)
    {
      final Color colorValue = (background.getBackgroundColor());
      if (colorValue != null)
      {
        styleBuilder.append("background-color", HtmlColors.getColorString(colorValue));
      }

      styleBuilder.append("border-top", styleBuilder.printEdgeAsCSS(background.getTop()));
      styleBuilder.append("border-left", styleBuilder.printEdgeAsCSS(background.getLeft()));
      styleBuilder.append("border-bottom", styleBuilder.printEdgeAsCSS(background.getBottom()));
      styleBuilder.append("border-right", styleBuilder.printEdgeAsCSS(background.getRight()));
    }

    final AttributeList attrList = new AttributeList();
    if (content != null)
    {
      // ignore for now ..
      final int rowSpan = rect.getRowSpan();
      if (rowSpan > 1)
      {
        attrList.setAttribute(XHTML_NAMESPACE, "rowspan", String.valueOf(rowSpan));
      }
      final int colSpan = rect.getColumnSpan();
      if (colSpan > 1)
      {
        attrList.setAttribute(XHTML_NAMESPACE, "colspan", String.valueOf(colSpan));
      }

      final ElementAlignment verticalAlignment = content.getNodeLayoutProperties().getVerticalAlignment();
      attrList.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "valign", translateVerticalAlignment(verticalAlignment));
    }

    styleManager.updateStyle(styleBuilder, attrList);
    return attrList;
  }


  /**
   * Translates the JFreeReport horizontal element alignment into a HTML alignment constant.
   *
   * @param ea the element alignment
   * @return the translated alignment name.
   */
  private String translateVerticalAlignment(final ElementAlignment ea)
  {
    if (ea == ElementAlignment.BOTTOM)
    {
      return "bottom";
    }
    if (ea == ElementAlignment.MIDDLE)
    {
      return "middle";
    }
    return "top";
  }


  private AttributeList createRowAttributes(final SheetLayout sheetLayout, final int row)
  {
    // todo: Check for common backgrounds and top/bottom borders
    // todo: Check for global style ..
    final AttributeList attrList = new AttributeList();
    final int rowHeight = (int) StrictGeomUtility.toExternalValue(sheetLayout.getRowHeight(row));

    if (isTableRowBorderDefinition())
    {
      styleBuilder.clear();
      final Color commonBackgroundColor = getCommonBackgroundColor(sheetLayout, row);
      final BorderEdge top = getCommonTopBorderEdge(sheetLayout, row);
      final BorderEdge bottom = getCommonBottomBorderEdge(sheetLayout, row);
      if (commonBackgroundColor != null)
      {
        styleBuilder.append("background-color", HtmlColors.getColorString(commonBackgroundColor));
      }
      styleBuilder.append("border-top", styleBuilder.printEdgeAsCSS(top));
      styleBuilder.append("border-bottom", styleBuilder.printEdgeAsCSS(bottom));
      styleBuilder.append("height", styleBuilder.getPointConverter().format(rowHeight), "pt");
      styleManager.updateStyle(styleBuilder, attrList);
    }
    else
    {
      // equally expensive and makes text more readable (helps with debugging)
      attrList.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "style", "height: " + rowHeight + "pt");
    }
    return attrList;
  }

  private BorderEdge getCommonTopBorderEdge(final SheetLayout sheetLayout, final int row)
  {
    BorderEdge bg = BorderEdge.EMPTY;
    final int columnCount = sheetLayout.getColumnCount();
    for (int col = 0; col < columnCount; col += 1)
    {
      final TableCellDefinition backgroundAt = sheetLayout.getBackgroundAt(row, col);
      if (backgroundAt == null)
      {
        return BorderEdge.EMPTY;
      }
      if (col == 0)
      {
        bg = backgroundAt.getTop();
      }
      else if (ObjectUtilities.equal(bg, backgroundAt.getTop()) == false)
      {
        return BorderEdge.EMPTY;
      }
    }
    return bg;
  }

  private BorderEdge getCommonBottomBorderEdge(final SheetLayout sheetLayout, final int row)
  {
    BorderEdge bg = BorderEdge.EMPTY;
    final int columnCount = sheetLayout.getColumnCount();
    for (int col = 0; col < columnCount; col += 1)
    {
      final TableCellDefinition backgroundAt = sheetLayout.getBackgroundAt(row, col);
      if (backgroundAt == null)
      {
        return BorderEdge.EMPTY;
      }
      if (col == 0)
      {
        bg = backgroundAt.getBottom();
      }
      else if (ObjectUtilities.equal(bg, backgroundAt.getBottom()) == false)
      {
        return BorderEdge.EMPTY;
      }
    }
    return bg;
  }

  private Color getCommonBackgroundColor(final SheetLayout sheetLayout, final int row)
  {
    Color bg = null;
    final int columnCount = sheetLayout.getColumnCount();
    for (int col = 0; col < columnCount; col += 1)
    {
      final TableCellDefinition backgroundAt = sheetLayout.getBackgroundAt(row, col);
      if (backgroundAt == null)
      {
        return null;
      }

      if (col == 0)
      {
        bg = backgroundAt.getBackgroundColor();
      }
      else
      {
        if (ObjectUtilities.equal(bg, backgroundAt.getBackgroundColor()) == false)
        {
          return null;
        }
      }
    }
    return bg;
  }

  private AttributeList createSheetNameAttributes()
  {
    final AttributeList tableAttrList = new AttributeList();

    final String additionalStyleClass =
        getConfiguration().getConfigProperty("org.jfree.report.modules.output.table.html.SheetNameClass");
    if (additionalStyleClass != null)
    {
      tableAttrList.setAttribute(XHTML_NAMESPACE, "class", additionalStyleClass);
    }

    return tableAttrList;
  }

  private AttributeList createTableAttributes(final SheetLayout sheetLayout)
  {
    final int noc = sheetLayout.getColumnCount();
    styleBuilder.clear();
    if ((noc > 0) && (isProportionalColumnWidths() == false))
    {
      final int width = (int) StrictGeomUtility.toExternalValue(sheetLayout.getCellWidth(0, noc));
      styleBuilder.append("width", width + "pt");
    }
    else
    {
      // Consume the complete width for proportional column widths
      styleBuilder.append("width", "100%");
    }

    // style += "table-layout: fixed;";
    if (isTableRowBorderDefinition())
    {
      styleBuilder.append("border-collapse", "collapse");
    }
    if (isEmptyCellsUseCSS())
    {
      styleBuilder.append("empty-cells", "show");
    }

    final String additionalStyleClass =
        getConfiguration().getConfigProperty("org.jfree.report.modules.output.table.html.StyleClass");

    final AttributeList tableAttrList = new AttributeList();
    if (additionalStyleClass != null)
    {
      tableAttrList.setAttribute(XHTML_NAMESPACE, "class", additionalStyleClass);
    }
    tableAttrList.setAttribute(XHTML_NAMESPACE, "cellspacing", "0");
    tableAttrList.setAttribute(XHTML_NAMESPACE, "cellpadding", "0");

    styleManager.updateStyle(styleBuilder, tableAttrList);
    return tableAttrList;
  }

  private void writeColumnDeclaration(final SheetLayout sheetLayout)
      throws IOException
  {
    final int colCount = sheetLayout.getColumnCount();
    final int fullWidth = (int) StrictGeomUtility.toExternalValue(sheetLayout.getMaxWidth());
    final boolean proportionalColumnWidths = isProportionalColumnWidths();

    final NumberFormat pointConverter = styleBuilder.getPointConverter();
    final NumberFormat pointIntConverter = styleBuilder.getPointIntConverter();
    for (int col = 0; col < colCount; col++)
    {
      // Print the table.
      final int width = (int) StrictGeomUtility.toExternalValue(sheetLayout.getCellWidth(col, col + 1));
      styleBuilder.clear();
      if (proportionalColumnWidths)
      {
        final double colWidth = width * 100.0d / fullWidth;
        styleBuilder.append("width", pointConverter.format(colWidth) + '%');
      }
      else
      {
        styleBuilder.append("width", pointIntConverter.format(width) + "pt");
      }

      xmlWriter.writeTag(null, "col", "style", styleBuilder.toString(), XmlWriterSupport.CLOSE);
    }
  }

  public void print(final LogicalPageKey logicalPageKey,
                    final LogicalPageBox logicalPage,
                    final TableContentProducer contentProducer,
                    final OutputProcessorMetaData metaData,
                    final boolean incremental)
      throws ContentProcessingException
  {
    this.configuration = metaData.getConfiguration();
    this.allowRawLinkTargets = "true".equals
        (configuration.getConfigProperty("org.jfree.report.modules.output.table.html.AllowRawLinkTargets"));
    this.copyExternalImages = "true".equals
        (configuration.getConfigProperty("org.jfree.report.modules.output.table.html.CopyExternalImages"));


    try
    {
      final SheetLayout sheetLayout = contentProducer.getSheetLayout();

      if (documentContentItem == null)
      {
        documentContentItem = contentLocation.createItem
            (contentNameGenerator.generateName(null, "text/html"));

        final OutputStream out = documentContentItem.getOutputStream();
        writer = new BufferedWriter(new OutputStreamWriter(out));

        final DefaultTagDescription td = new DefaultTagDescription();
        td.configure(getConfiguration(), "org.jfree.report.modules.output.table.html.");

        if (isCreateBodyFragment() == false)
        {
          if (isInlineStylesRequested())
          {
            this.styleManager = new InlineStyleManager();
            this.xmlWriter = new XmlWriter(writer, td);
            this.xmlWriter.addImpliedNamespace(HtmlPrinter.XHTML_NAMESPACE, "");
            this.xmlWriter.setHtmlCompatiblityMode(true);
            writeCompleteHeader(xmlWriter, contentProducer, null, null);
          }
          else
          {
            if (isExternalStyleSheetRequested())
            {
              this.styleFile = dataLocation.createItem(dataNameGenerator.generateName("style", "text/css"));
              this.styleFileUrl = urlRewriter.rewrite(documentContentItem, styleFile);
            }

            this.styleManager = new GlobalStyleManager();
            if (isForceBufferedWriting() == false && styleFile != null)
            {
              this.xmlWriter = new XmlWriter(writer, td);
              this.xmlWriter.addImpliedNamespace(HtmlPrinter.XHTML_NAMESPACE, "");
              this.xmlWriter.setHtmlCompatiblityMode(true);
              writeCompleteHeader(xmlWriter, contentProducer, styleFileUrl, null);
            }
            else
            {
              this.bufferWriter = new MemoryStringWriter(1024 * 512);
              this.xmlWriter = new XmlWriter(bufferWriter, td);
              this.xmlWriter.setAdditionalIndent(1);
              this.xmlWriter.addImpliedNamespace(HtmlPrinter.XHTML_NAMESPACE, "");
              this.xmlWriter.setHtmlCompatiblityMode(true);
            }
          }

          this.xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "body", XmlWriterSupport.OPEN);
        }
        else
        {
          this.styleManager = new InlineStyleManager();
          this.xmlWriter = new XmlWriter(writer, td);
          this.xmlWriter.addImpliedNamespace(HtmlPrinter.XHTML_NAMESPACE, "");
          this.xmlWriter.setHtmlCompatiblityMode(true);
        }

        // table name
        final String sheetName = contentProducer.getSheetName();
        if (sheetName != null)
        {
          xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "h1", createSheetNameAttributes(), XmlWriterSupport.OPEN);
          xmlWriter.writeText(xmlWriter.normalizeLocal(sheetName, true));
          xmlWriter.writeCloseTag();
        }

        // table
        xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "table", createTableAttributes(sheetLayout), XmlWriterSupport.OPEN);
        writeColumnDeclaration(sheetLayout);
      }

      final int colCount = sheetLayout.getColumnCount();
//      final int rowCount = sheetLayout.getRowCount();
      final boolean emptyCellsUseCSS = isEmptyCellsUseCSS();

      final int startRow = contentProducer.getFinishedRows();
      final int finishRow = contentProducer.getFilledRows();
      // Log.debug ("Processing: " + startRow + " " + finishRow + " " + incremental);
      final HtmlTextExtractor textExtractor = new HtmlTextExtractor(metaData, xmlWriter, styleManager, this);

      for (int row = startRow; row < finishRow; row++)
      {
        xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "tr", createRowAttributes(sheetLayout, row), XmlWriterSupport.OPEN);
        for (int col = 0; col < colCount; col++)
        {
          final RenderBox content = contentProducer.getContent(row, col);
          final TableCellDefinition background = sheetLayout.getBackgroundAt(row, col);

          if (content == null && background == null)
          {
            if (emptyCellsUseCSS)
            {
              xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "td", XmlWriterSupport.CLOSE);
            }
            else
            {
              final AttributeList attrs = new AttributeList();
              attrs.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "style", "font-size: 1pt");
              xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "td", attrs, XmlWriterSupport.OPEN);
              xmlWriter.writeText("&nbsp;");
              xmlWriter.writeCloseTag();
            }

            continue;
          }

          if (content != null)
          {
            if (content.isCommited() == false)
            {
              throw new InvalidReportStateException("Uncommited content encountered: " + row + ", " + col + ' ' + content);
            }

            final long contentOffset = contentProducer.getContentOffset(row, col);
            final TableRectangle rectangle = sheetLayout.getTableBounds
                (content.getX(), content.getY() + contentOffset,
                    content.getWidth(), content.getHeight(), null);
            if (rectangle.isOrigin(col, row) == false)
            {
              // A spanned cell ..
              continue;
            }

            final TableCellDefinition realBackground;
            if (background == null || (rectangle.getColumnSpan() == 1 && rectangle.getRowSpan() == 1))
            {
              realBackground = background;
            }
            else
            {
              realBackground = sheetLayout.getBackgroundAt
                  (rectangle.getX1(), rectangle.getY1(), rectangle.getColumnSpan(), rectangle.getRowSpan());
            }

            final AttributeList cellAttributes = createCellAttributes(rectangle, content, realBackground, null, null);
            xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "td", cellAttributes, XmlWriterSupport.OPEN);
            if (background != null)
            {
              final String anchor = background.getAnchor();
              if (anchor != null)
              {
                xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "a", "name", normalize(anchor, xmlWriter), XmlWriterSupport.CLOSE);
              }
            }
            // the style of the content-box itself is already contained in the <td> tag. So there is no need
            // to duplicate the style here
            textExtractor.performOutput(content);
            
            xmlWriter.writeCloseTag();
            content.setFinished(true);
          }
          else
          {
            // Background cannot be null at this point ..
            final String anchor = background.getAnchor();
            if (anchor == null && emptyCellsUseCSS)
            {
              final AttributeList cellAttributes = createCellAttributes(null, null, background, null, null);
              xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "td", cellAttributes, XmlWriterSupport.CLOSE);
            }
            else
            {
              final AttributeList cellAttributes =
                  createCellAttributes(null, null, background, EMPTY_CELL_ATTRNAMES, EMPTY_CELL_ATTRVALS);
              xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "td", cellAttributes, XmlWriterSupport.OPEN);
              if (anchor != null)
              {
                xmlWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "a", "name", normalize(anchor, xmlWriter), XmlWriterSupport.CLOSE);
              }
              xmlWriter.writeText("&nbsp;");
              xmlWriter.writeCloseTag();

            }
          }
        }
        xmlWriter.writeCloseTag();
      }


      if (incremental == false)
      {
        performCloseFile(contentProducer);

        xmlWriter = null;
        try
        {
          writer.close();
        }
        catch (IOException e)
        {
          // ignored ..
        }
        writer = null;
        bufferWriter = null;
        documentContentItem = null;
      }
    }
    catch (IOException ioe)
    {
      xmlWriter = null;
      try
      {
        writer.close();
      }
      catch (IOException e)
      {
        // ignored ..
      }
      writer = null;
      bufferWriter = null;
      documentContentItem = null;
      styleFile = null;

      // ignore for now ..
      throw new ContentProcessingException("IOError while creating content", ioe);
    }
    catch (ContentIOException e)
    {
      xmlWriter = null;
      try
      {
        writer.close();
      }
      catch (IOException ex)
      {
        // ignored ..
      }
      writer = null;
      bufferWriter = null;
      documentContentItem = null;
      styleFile = null;

      throw new ContentProcessingException("Content-IOError while creating content", e);
    }
    catch (URLRewriteException e)
    {
      xmlWriter = null;
      writer = null;
      try
      {
        writer.close();
      }
      catch (IOException ex)
      {
        // ignored ..
      }
      bufferWriter = null;
      documentContentItem = null;
      styleFile = null;

      throw new ContentProcessingException("Cannot create URL for external stylesheet", e);
    }
  }

  private void writeCompleteHeader(final XmlWriter docWriter,
                                   final TableContentProducer contentProducer,
                                   final String url,
                                   final String inlineStyleSheet) throws IOException
  {
    final String encoding = configuration.getConfigProperty
        ("org.jfree.report.modules.output.table.html.Encoding", EncodingRegistry.getPlatformDefaultEncoding());

    docWriter.writeXmlDeclaration(encoding);
    for (int i = 0; i < XHTML_HEADER.length; i++)
    {
      docWriter.writeText(HtmlPrinter.XHTML_HEADER[i]);
      docWriter.writeNewLine();
    }
    docWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "html", XmlWriterSupport.OPEN);
    docWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "head", XmlWriterSupport.OPEN);

    final String title = configuration.getConfigProperty("org.jfree.report.modules.output.table.html.Title");
    if (title != null)
    {
      docWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "title", XmlWriterSupport.OPEN);
      docWriter.writeText(xmlWriter.normalizeLocal(title, false));
      docWriter.writeCloseTag();
    }
    // if no single title defined, use the sheetname function previously computed
    else if (contentProducer.getSheetName() != null)
    {
      docWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "title", XmlWriterSupport.OPEN);
      docWriter.writeText(xmlWriter.normalizeLocal(contentProducer.getSheetName(), true));
      docWriter.writeCloseTag();
    }

    writeMeta(docWriter, "subject",
        configuration.getConfigProperty("org.jfree.report.modules.output.table.html.Subject"));
    writeMeta(docWriter, "author",
        configuration.getConfigProperty("org.jfree.report.modules.output.table.html.Author"));
    writeMeta(docWriter, "keywords",
        configuration.getConfigProperty("org.jfree.report.modules.output.table.html.Keywords"));
    writeMeta(docWriter, "generator", GENERATOR);

    if (url != null)
    {
      final AttributeList attrList = new AttributeList();
      attrList.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "type", "text/css");
      attrList.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "rel", "stylesheet");
      attrList.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "href", url);

      docWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "link", attrList, XmlWriterSupport.CLOSE);
    }
    else if (inlineStyleSheet != null)
    {
      docWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "style", "type", "text/css", XmlWriterSupport.OPEN);
      docWriter.writeText(inlineStyleSheet);
      docWriter.writeCloseTag();
    }
    docWriter.writeCloseTag();
  }

  private void performCloseFile(final TableContentProducer contentProducer)
      throws IOException, ContentIOException
  {
    xmlWriter.writeCloseTag(); // for the opening table ..

    if (isCreateBodyFragment() != false)
    {
      xmlWriter.close();
      return;
    }

    if (styleFile != null)
    {
      final Writer styleOut = new OutputStreamWriter(new BufferedOutputStream(styleFile.getOutputStream()));
      styleManager.write(styleOut);
      styleOut.flush();
      styleOut.close();

      if (isForceBufferedWriting() == false)
      {
        // A complete header had been written when the processing started ..
        this.xmlWriter.writeCloseTag(); // for the body tag
        this.xmlWriter.writeCloseTag(); // for the HTML tag
        this.xmlWriter.close();
        return;
      }
    }
    if (isInlineStylesRequested())
    {
      this.xmlWriter.writeCloseTag(); // for the body tag
      this.xmlWriter.writeCloseTag(); // for the HTML tag
      this.xmlWriter.close();
      return;
    }


    final XmlWriter docWriter = new XmlWriter(writer, xmlWriter.getTagDescription());
    docWriter.addImpliedNamespace(HtmlPrinter.XHTML_NAMESPACE, "");
    docWriter.setHtmlCompatiblityMode(true);

    if (styleFile != null)
    {
      // now its time to write the header with the link to the style-sheet-file
      writeCompleteHeader(docWriter, contentProducer, styleFileUrl, null);
    }
    else
    {
      final String globalStyleSheet = styleManager.getGlobalStyleSheet();
      writeCompleteHeader(docWriter, contentProducer, null, globalStyleSheet);
    }

    docWriter.writeTag(HtmlPrinter.XHTML_NAMESPACE, "body", XmlWriterSupport.OPEN);

    xmlWriter.writeCloseTag(); // for the body ..
    xmlWriter.flush();
    docWriter.writeText(bufferWriter.toString());

    docWriter.writeCloseTag(); // for the html ..
    docWriter.close();
  }

  private boolean isForceBufferedWriting()
  {
    return "true".equals(configuration.getConfigProperty
        ("org.jfree.report.modules.output.table.html.ForceBufferedWriting"));
  }

  protected static String normalize(final String anchor, final XmlWriter xmlWriter)
  {
    if (anchor == null)
    {
      return null;
    }

    // raw values are ok, which means that we just have to check whether the anchor breaks anything.
    if (anchor.indexOf('<') == -1 &&
        anchor.indexOf('>') == -1 &&
        anchor.indexOf('"') == -1)
    {
      return anchor;
    }

    return xmlWriter.normalizeLocal(anchor, true);
  }

  private void writeMeta(final XmlWriter writer, final String name, final String value) throws IOException
  {
    if (value == null)
    {
      return;
    }
    final AttributeList attrList = new AttributeList();
    attrList.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "name", name);
    attrList.setAttribute(HtmlPrinter.XHTML_NAMESPACE, "content", value);
    writer.writeTag(HtmlPrinter.XHTML_NAMESPACE, "meta", attrList, XmlWriterSupport.CLOSE);
  }
//
//  /**
//   * Checks, whether the engine should generate a stylesheet that is directly inserted into the header of the
//   * generated file.
//   *
//   * This method will return false, if "createBodyFragment" is enabled, as a body-fragment has no header.
//   *
//   * This methos will also return false, if inline-styles are requested.
//   *
//   * @return true, if the engine generates an stylesheet that is contained in the header, false otherwise.
//   */
//  private boolean isInternalStyleSheetRequested ()
//  {
//    if (isCreateBodyFragment())
//    {
//      // body-fragments have no header ..
//      return false;
//    }
//
//    // We will add the style-declarations directly to the HTML elements ..
//    if (isInlineStylesRequested())
//    {
//      return false;
//    }
//
//    final boolean externalStyle = "true".equals
//        (configuration.getConfigProperty("org.jfree.report.modules.output.table.html.ExternalStyle", "true"));
//    // User explicitly requested internal styles by disabeling the external-style property.
//    return externalStyle == false;
//  }

  private boolean isInlineStylesRequested()
  {
    return "true".equals(configuration.getConfigProperty("org.jfree.report.modules.output.table.html.InlineStyles"));
  }

  private boolean isExternalStyleSheetRequested()
  {
    if (isCreateBodyFragment())
    {
      // body-fragments have no header ..
      return false;
    }

    // We will add the style-declarations directly to the HTML elements ..
    if (isInlineStylesRequested())
    {
      return false;
    }

    // Without the ability to create external files, we cannot create external stylesheet.
    if (dataLocation == null)
    {
      return false;
    }

    final boolean externalStyle = "true".equals
        (configuration.getConfigProperty("org.jfree.report.modules.output.table.html.ExternalStyle", "true"));
    // User explicitly requested internal styles by disabeling the external-style property.
    return externalStyle;

  }


  public static StyleBuilder produceTextStyle(StyleBuilder styleBuilder,
                                              final RenderBox box,
                                              final boolean includeBorder)
  {
    if (styleBuilder == null)
    {
      styleBuilder = new StyleBuilder();
    }

    final StyleSheet styleSheet = box.getStyleSheet();
    final Color textColor = (Color) styleSheet.getStyleProperty(ElementStyleKeys.PAINT);
    final Color backgroundColor = (Color) styleSheet.getStyleProperty(ElementStyleKeys.BACKGROUND_COLOR);

    styleBuilder.clear();
    if (includeBorder)
    {
      if (backgroundColor != null)
      {
        styleBuilder.append("background-color", HtmlColors.getColorString(backgroundColor));
      }

      final BoxDefinition boxDefinition = box.getBoxDefinition();
      final Border border = boxDefinition.getBorder();
      final BorderEdge top = border.getTop();
      if (top != null)
      {
        styleBuilder.append("border-top", styleBuilder.printEdgeAsCSS(top));
      }
      final BorderEdge left = border.getLeft();
      if (left != null)
      {
        styleBuilder.append("border-left", styleBuilder.printEdgeAsCSS(left));
      }
      final BorderEdge bottom = border.getBottom();
      if (bottom != null)
      {
        styleBuilder.append("border-bottom", styleBuilder.printEdgeAsCSS(bottom));
      }
      final BorderEdge right = border.getRight();
      if (right != null)
      {
        styleBuilder.append("border-right", styleBuilder.printEdgeAsCSS(right));
      }

      final long paddingTop = boxDefinition.getPaddingTop();
      final long paddingLeft = boxDefinition.getPaddingLeft();
      final long paddingBottom = boxDefinition.getPaddingBottom();
      final long paddingRight = boxDefinition.getPaddingRight();
      if (paddingTop > 0)
      {
        styleBuilder.append("padding-top", String.valueOf(StrictGeomUtility.toExternalValue(paddingTop)), "pt");
      }
      if (paddingLeft > 0)
      {
        styleBuilder.append("padding-left", String.valueOf(StrictGeomUtility.toExternalValue(paddingLeft)), "pt");
      }
      if (paddingBottom > 0)
      {
        styleBuilder.append("padding-bottom", String.valueOf(StrictGeomUtility.toExternalValue(paddingBottom)), "pt");
      }
      if (paddingRight > 0)
      {
        styleBuilder.append("padding-right", String.valueOf(StrictGeomUtility.toExternalValue(paddingRight)), "pt");
      }
    }
    if (textColor != null)
    {
      styleBuilder.append("color", HtmlColors.getColorString(textColor));
    }
    styleBuilder.append("font-family", box.getStaticBoxLayoutProperties().getFontFamily());
    styleBuilder.append("font-size", String.valueOf(styleSheet.getDoubleStyleProperty(TextStyleKeys.FONTSIZE, 0)), "pt");
    if (styleSheet.getBooleanStyleProperty(TextStyleKeys.BOLD))
    {
      styleBuilder.append("font-weight", "bold");
    }
    else
    {
      styleBuilder.append("font-weight", "normal");
    }

    if (styleSheet.getBooleanStyleProperty(TextStyleKeys.ITALIC))
    {
      styleBuilder.append("font-style", "italic");
    }
    else
    {
      styleBuilder.append("font-style", "normal");
    }

    final boolean underlined = styleSheet.getBooleanStyleProperty(TextStyleKeys.UNDERLINED);
    final boolean strikeThrough = styleSheet.getBooleanStyleProperty(TextStyleKeys.STRIKETHROUGH);
    if (underlined && strikeThrough)
    {
      styleBuilder.append("text-decoration", "underline line-through");
    }
    else if (strikeThrough)
    {
      styleBuilder.append("text-decoration", "line-through");
    }
    if (underlined)
    {
      styleBuilder.append("text-decoration", "underline");
    }
    else
    {
      styleBuilder.append("text-decoration", "none");
    }

    final ElementAlignment align = (ElementAlignment) styleSheet.getStyleProperty(ElementStyleKeys.ALIGNMENT);
    styleBuilder.append("text-align", translateHorizontalAlignment(align));

    final double wordSpacing = styleSheet.getDoubleStyleProperty(TextStyleKeys.WORD_SPACING, 0);
    styleBuilder.append("word-spacing", styleBuilder.getPointConverter().format(wordSpacing), "pt");

    final double minLetterSpacing = styleSheet.getDoubleStyleProperty(TextStyleKeys.X_MIN_LETTER_SPACING, 0);
    final double maxLetterSpacing = styleSheet.getDoubleStyleProperty(TextStyleKeys.X_MAX_LETTER_SPACING, 0);
    styleBuilder.append("letter-spacing", styleBuilder.getPointConverter().format(Math.min(minLetterSpacing, maxLetterSpacing)), "pt");

    return styleBuilder;
  }

  /**
   * Translates the JFreeReport horizontal element alignment into a HTML alignment constant.
   *
   * @param ea the element alignment
   * @return the translated alignment name.
   */
  public static String translateHorizontalAlignment(final ElementAlignment ea)
  {
    if (ea == ElementAlignment.RIGHT)
    {
      return "right";
    }
    if (ea == ElementAlignment.CENTER)
    {
      return "center";
    }
    return "left";
  }


  public void registerFailure(final ResourceKey source)
  {
    knownResources.put(source, Boolean.FALSE);
  }

  public void registerContent(final ResourceKey source, final String name)
  {
    knownResources.put(source, name);
  }

  public boolean isRegistered(final ResourceKey source)
  {
    return knownResources.containsKey(source);
  }

  public String getRegisteredName(final ResourceKey source)
  {
    final Object o = knownResources.get(source);
    if (o instanceof String)
    {
      return (String) o;
    }
    return null;
  }
}
