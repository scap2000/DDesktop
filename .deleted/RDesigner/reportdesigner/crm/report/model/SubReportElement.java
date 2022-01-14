package org.pentaho.reportdesigner.crm.report.model;

import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.GraphicsContext;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportCreationException;
import org.pentaho.reportdesigner.crm.report.reportexporter.ReportVisitor;
import org.pentaho.reportdesigner.crm.report.util.FileRelativator;
import org.pentaho.reportdesigner.crm.report.util.XMLContextKeys;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverterFactory;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 20.10.2005
 * Time: 15:40:13
 */
public class SubReportElement extends TextReportElement
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(SubReportElement.class.getName());

    @NotNull
    private FilePath filePath;
    @Nullable
    private String query;
    @NotNull
    private SubReportParameters parameters;


    public SubReportElement()
    {
        filePath = new FilePath("");
        parameters = new SubReportParameters();
    }


    @NotNull
    public FilePath getFilePath()
    {
        return filePath;
    }


    public void setFilePath(@NotNull final FilePath filePath)
    {
        //noinspection ConstantConditions
        if (filePath == null)
        {
            throw new IllegalArgumentException("filePath must not be null");
        }

        final FilePath oldFilePath = this.filePath;
        this.filePath = filePath;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.FILE_PATH);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setFilePath(oldFilePath);
                }


                public void redo()
                {
                    setFilePath(filePath);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.FILE_PATH, oldFilePath, filePath);
    }


    @Nullable
    public String getQuery()
    {
        return query;
    }


    public void setQuery(@Nullable final String query)
    {
        final String oldQuery = this.query;
        this.query = query;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.QUERY);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setQuery(oldQuery);
                }


                public void redo()
                {
                    setQuery(query);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.QUERY, oldQuery, query);
    }


    @NotNull
    public SubReportParameters getParameters()
    {
        return parameters;
    }


    public void setParameters(@NotNull final SubReportParameters parameters)
    {
        //noinspection ConstantConditions
        if (parameters == null)
        {
            throw new IllegalArgumentException("parameters must not be null");
        }

        final SubReportParameters oldParameters = this.parameters;
        this.parameters = parameters;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.PARAMETERS);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setParameters(oldParameters);
                }


                public void redo()
                {
                    setParameters(parameters);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.PARAMETERS, oldParameters, parameters);
    }


    @NotNull
    public String getPaintText()
    {
        String fileName;
        try
        {
            File file = new File(filePath.getPath());
            fileName = file.getName();
            if (!"".equals(fileName))
            {
                return fileName;
            }
        }
        catch (Exception e)
        {
            if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "SubReportElement.getPaintText ", e);
        }
        return "SubReport";//NON-NLS
    }


    public void paint(@NotNull GraphicsContext graphicsContext, @NotNull Graphics2D g2d)
    {
        Shape origClip = g2d.getClip();
        Color origColor = g2d.getColor();
        Composite origComposite = g2d.getComposite();
        Stroke origStroke = g2d.getStroke();

        Rectangle2D.Double rect = getRectangle();

        g2d.setColor(new Color(200, 200, 255));
        g2d.fill(rect);

        g2d.setColor(new Color(50, 50, 255));
        g2d.draw(rect);

        g2d.setStroke(origStroke);
        g2d.setComposite(origComposite);
        g2d.setColor(origColor);
        g2d.setClip(origClip);

        super.paint(graphicsContext, g2d);
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /*Object newParent = */
        reportVisitor.visit(parent, this);
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        String relativeURL = FileRelativator.getRelativePathFromFile(XMLContextKeys.CONTEXT_PATH.getObject(xmlContext), filePath.getPath());
        xmlWriter.writeProperty(PropertyKeys.FILE_PATH, relativeURL);
        if (query != null)
        {
            xmlWriter.writeProperty(PropertyKeys.QUERY, query);
        }

        xmlWriter.startElement(PropertyKeys.PARAMETERS);
        parameters.externalizeObject(xmlWriter, xmlContext);
        xmlWriter.closeElement(PropertyKeys.PARAMETERS);
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.FILE_PATH.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            filePath = ObjectConverterFactory.getInstance().getFilePathConverter(xmlContext).getObject(XMLUtils.readProperty(PropertyKeys.FILE_PATH, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.QUERY.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            query = XMLUtils.readProperty(PropertyKeys.QUERY, node);
        }
        else if (PropertyKeys.PARAMETERS.equals(node.getRawName()))
        {
            parameters = new SubReportParameters();
            parameters.readObject(node, xmlContext);
        }
    }
}
