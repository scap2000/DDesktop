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
import org.pentaho.reportdesigner.lib.client.i18n.TranslationManager;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverterFactory;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: Martin
 * Date: 20.10.2005
 * Time: 15:40:13
 */
public class StaticImageReportElement extends ReportElement implements ImageObserver
{
    @NonNls
    @NotNull
    private static final Logger LOG = Logger.getLogger(StaticImageReportElement.class.getName());


    public static boolean isValidImageType(@NotNull String fileName)
    {
        String name = fileName.toLowerCase();
        if (name.endsWith(".png"))//NON-NLS
        {
            return true;
        }
        else if (name.endsWith(".jpg"))//NON-NLS
        {
            return true;
        }
        else if (name.endsWith(".jpeg"))//NON-NLS
        {
            return true;
        }
        else if (name.endsWith(".bmp"))//NON-NLS
        {
            return true;
        }
        else if (name.endsWith(".wbmp"))//NON-NLS
        {
            return true;
        }
        else if (name.endsWith(".wmf"))//NON-NLS
        {
            return true;
        }
        return false;
    }


    @NotNull
    private static final Color OUTLINE_COLOR = new Color(230, 230, 230);
    @NotNull
    private static final Color FILL_COLOR = new Color(240, 240, 240);

    @Nullable
    private URL url;
    private boolean keepAspect;

    private boolean loading;
    private boolean error;

    @Nullable
    private Image image;


    public StaticImageReportElement()
    {
        keepAspect = true;
    }


    public boolean isKeepAspect()
    {
        return keepAspect;
    }


    public void setKeepAspect(final boolean keepAspect)
    {
        final boolean oldKeepAspect = this.keepAspect;
        this.keepAspect = keepAspect;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.KEEP_ASPECT);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setKeepAspect(oldKeepAspect);
                }


                public void redo()
                {
                    setKeepAspect(keepAspect);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.KEEP_ASPECT, Boolean.valueOf(oldKeepAspect), Boolean.valueOf(keepAspect));
    }


    @Nullable
    public URL getUrl()
    {
        return url;
    }


    public void setUrl(@Nullable final URL url)
    {
        final URL oldUrl = this.url;
        this.url = url;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.URL);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setUrl(oldUrl);
                }


                public void redo()
                {
                    setUrl(url);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.URL, oldUrl, url);

        if (url != null)
        {
            loading = true;
            error = false;

            image = Toolkit.getDefaultToolkit().createImage(url);
            firePropertyChange(PropertyKeys.IMAGE, null, image);
        }
        else
        {
            Image oldImage = image;
            image = null;
            firePropertyChange(PropertyKeys.IMAGE, oldImage, null);
        }
    }


    public void setParent(@Nullable final ReportElement parent)
    {
        super.setParent(parent);

        if (parent == null)
        {
            disposeCachedData();
        }
        else
        {
            if (url != null)
            {
                if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "StaticImageReportElement.setParent load image again");
                image = Toolkit.getDefaultToolkit().createImage(url);
                firePropertyChange(PropertyKeys.IMAGE, null, image);
            }
        }
    }


    public void paint(@NotNull GraphicsContext graphicsContext, @NotNull Graphics2D g2d)
    {
        Shape origClip = g2d.getClip();
        Color origColor = g2d.getColor();
        Composite origComposite = g2d.getComposite();
        Stroke origStroke = g2d.getStroke();

        Rectangle2D.Double rect = getRectangle();

        g2d.clip(rect);

        paintBackroundAndBorder(g2d);

        rect = getContentRectangle();

        if (image == null || loading)
        {
            g2d.setColor(OUTLINE_COLOR);
            g2d.drawRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
            g2d.setColor(FILL_COLOR);
            g2d.fillRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
        }

        if (loading)
        {
            g2d.setColor(origColor);
            g2d.drawString(TranslationManager.getInstance().getTranslation("R", "StaticImageReportElement.Loading"),
                           (int) rect.getX() + 20, (int) rect.getY() + 20);
        }
        if (error)
        {
            g2d.setColor(origColor);
            g2d.drawString(TranslationManager.getInstance().getTranslation("R", "StaticImageReportElement.Error"),
                           (int) rect.getX() + 20, (int) rect.getY() + 20);
        }

        Image image = this.image;

        if (image != null)
        {
            if (keepAspect)
            {
                int width = image.getWidth(this);
                int height = image.getHeight(this);

                double factor = height / (double) width;
                double f = rect.getHeight() / rect.getWidth();

                if (factor > f)
                {
                    g2d.drawImage(image, (int) rect.getX(), (int) rect.getY(), (int) ((rect.getWidth() * f) / factor + 0.5), (int) rect.getHeight(), this);
                }
                else
                {
                    g2d.drawImage(image, (int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) (rect.getHeight() * (factor / f) + 0.5), this);
                }
            }
            else
            {
                g2d.drawImage(image, (int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight(), this);
            }
        }
        else
        {
            g2d.setColor(new Color(240, 240, 240));
            g2d.fill(rect);

            g2d.setColor(new Color(230, 255, 230));
            g2d.fillOval((int) (rect.getX() + 0.1 * rect.getWidth()), (int) (rect.getY() + 0.1 * rect.getHeight()), (int) (0.5 * rect.getWidth()), (int) (0.7 * rect.getHeight()));

            g2d.setColor(new Color(230, 230, 255));
            g2d.fillPolygon(new int[]{(int) (rect.getX() + 0.4 * rect.getWidth()), (int) (rect.getX() + 0.675 * rect.getWidth()), (int) (rect.getX() + 0.95 * rect.getWidth())},
                            new int[]{(int) (rect.getY() + 0.7 * rect.getHeight()), (int) (rect.getY() + 0.1 * rect.getHeight()), (int) (rect.getY() + 0.7 * rect.getHeight())},
                            3);

            g2d.setColor(new Color(255, 230, 230));
            g2d.fillRect((int) (rect.getX() + 0.15 * rect.getWidth()), (int) (rect.getY() + 0.6 * rect.getHeight()), (int) (0.7 * rect.getWidth()), (int) (0.3 * rect.getHeight()));
        }

        g2d.setStroke(origStroke);
        g2d.setComposite(origComposite);
        g2d.setColor(origColor);
        g2d.setClip(origClip);
    }


    public boolean isError()
    {
        return error;
    }


    public void disposeCachedData()
    {
        if (LOG.isLoggable(Level.FINE)) LOG.log(Level.FINE, "StaticImageReportElement.disposeCachedData ");
        Image oldImage = image;
        image = null;
        firePropertyChange(PropertyKeys.IMAGE, oldImage, null);
    }


    public void accept(@Nullable Object parent, @NotNull ReportVisitor reportVisitor) throws ReportCreationException
    {
        /*Object newParent = */
        reportVisitor.visit(parent, this);
    }


    public boolean imageUpdate(@Nullable Image img, int infoflags, int x, int y, int width, int height)
    {
        int rate = -1;
        if ((infoflags & (FRAMEBITS | ALLBITS)) != 0)
        {
            rate = 0;
        }
        else if ((infoflags & ERROR) != 0)
        {
            error = true;
            loading = false;

            image = null;
            firePropertyChange(PropertyKeys.IMAGE, null, img);
            return false;
        }
        if (rate >= 0)
        {
            error = false;
            loading = false;

            firePropertyChange(PropertyKeys.IMAGE, null, img);
        }
        return (infoflags & (ALLBITS | ABORT)) == 0;
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        URL url = this.url;
        if (url != null)
        {
            xmlWriter.writeProperty(PropertyKeys.URL, ObjectConverterFactory.getInstance().getURLConverter(xmlContext).getString(url));
        }
        xmlWriter.writeProperty(PropertyKeys.KEEP_ASPECT, String.valueOf(keepAspect));
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.URL.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            url = ObjectConverterFactory.getInstance().getURLConverter(xmlContext).getObject(XMLUtils.readProperty(PropertyKeys.URL, node));
            loading = true;
            error = false;
            image = Toolkit.getDefaultToolkit().createImage(url);
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.KEEP_ASPECT.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            keepAspect = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.KEEP_ASPECT, node));
        }
    }
}
