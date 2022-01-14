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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.GraphicsContext;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.crm.report.model.functions.ExpressionRegistry;
import org.pentaho.reportdesigner.crm.report.model.textlayout.TextParagraph;
import org.pentaho.reportdesigner.lib.client.undo.Undo;
import org.pentaho.reportdesigner.lib.client.undo.UndoEntry;
import org.pentaho.reportdesigner.lib.client.util.UncaughtExcpetionsModel;
import org.pentaho.reportdesigner.lib.common.xml.ObjectConverterFactory;
import org.pentaho.reportdesigner.lib.common.xml.XMLConstants;
import org.pentaho.reportdesigner.lib.common.xml.XMLUtils;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;

/**
 * User: Martin
 * Date: 20.10.2005
 * Time: 15:40:13
 */
public abstract class TextReportElement extends ReportElement
{
    @NotNull
    public static final String DEFAULT_FONT = "dialog";

    @NotNull
    private Font font;
    @NotNull
    private Color foreground;

    private boolean underline;
    private boolean strikethrough;
    private boolean embedFont;
    private double lineHeight;
    @Nullable
    private TextReportElementVerticalAlignment verticalAlignment;
    @Nullable
    private TextReportElementHorizontalAlignment horizontalAlignment;
    @Nullable
    private String reservedLiteral;
    private boolean trimTextContent;
    private boolean wrapTextInExcel;
    @Nullable
    private String encoding;
    public static final double TOLERANCE = 0.01;


    public TextReportElement()
    {
        this(new Font(DEFAULT_FONT, Font.PLAIN, 12));
        trimTextContent = true;
    }


    public TextReportElement(@NotNull Font font)
    {
        //noinspection ConstantConditions
        if (font == null)
        {
            throw new IllegalArgumentException("font must not be null");
        }

        this.font = font;
        foreground = Color.BLACK;

        verticalAlignment = TextReportElementVerticalAlignment.TOP;
        horizontalAlignment = TextReportElementHorizontalAlignment.LEFT;

        lineHeight = 0;

        reservedLiteral = "..";

        trimTextContent = true;

        textParagraphs = new ArrayList<TextParagraph>();
        previousPaintText = "";
    }


    @NotNull
    public Font getFont()
    {
        return font;
    }


    public void setFont(@NotNull final Font font)
    {
        //noinspection ConstantConditions
        if (font == null)
        {
            throw new IllegalArgumentException("font must not be null");
        }

        final Font oldFont = this.font;
        this.font = font;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.FONT);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setFont(oldFont);
                }


                public void redo()
                {
                    setFont(font);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.FONT, oldFont, font);
    }


    @NotNull
    public Color getForeground()
    {
        return foreground;
    }


    public void setForeground(@NotNull final Color foreground)
    {
        //noinspection ConstantConditions
        if (foreground == null)
        {
            throw new IllegalArgumentException("foreground must not be null");
        }

        final Color oldForeground = this.foreground;
        this.foreground = foreground;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.FOREGROUND);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setForeground(oldForeground);
                }


                public void redo()
                {
                    setForeground(foreground);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.FOREGROUND, oldForeground, foreground);
    }


    public boolean isUnderline()
    {
        return underline;
    }


    public void setUnderline(final boolean underline)
    {
        final boolean oldUnderline = this.underline;
        this.underline = underline;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.UNDERLINE);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setUnderline(oldUnderline);
                }


                public void redo()
                {
                    setUnderline(underline);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.UNDERLINE, oldUnderline, underline);
    }


    public boolean isStrikethrough()
    {
        return strikethrough;
    }


    public void setStrikethrough(final boolean strikethrough)
    {
        final boolean oldStrikethrough = this.strikethrough;
        this.strikethrough = strikethrough;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.STRIKETHROUGH);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setStrikethrough(oldStrikethrough);
                }


                public void redo()
                {
                    setStrikethrough(strikethrough);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.STRIKETHROUGH, oldStrikethrough, strikethrough);
    }


    public boolean isEmbedFont()
    {
        return embedFont;
    }


    public void setEmbedFont(final boolean embedFont)
    {
        final boolean oldEmbedFont = this.embedFont;
        this.embedFont = embedFont;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.EMBED_FONT);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setEmbedFont(oldEmbedFont);
                }


                public void redo()
                {
                    setEmbedFont(embedFont);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.EMBED_FONT, oldEmbedFont, embedFont);
    }


    public double getLineHeight()
    {
        return lineHeight;
    }


    public void setLineHeight(final double lineHeight)
    {
        final double oldLineHeight = this.lineHeight;
        this.lineHeight = lineHeight;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.LINE_HEIGHT);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setLineHeight(oldLineHeight);
                }


                public void redo()
                {
                    setLineHeight(lineHeight);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.LINE_HEIGHT, oldLineHeight, lineHeight);
    }


    @Nullable
    public TextReportElementVerticalAlignment getVerticalAlignment()
    {
        return verticalAlignment;
    }


    public void setVerticalAlignment(@Nullable final TextReportElementVerticalAlignment verticalAlignment)
    {
        final TextReportElementVerticalAlignment oldVerticalAlignment = this.verticalAlignment;
        this.verticalAlignment = verticalAlignment;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.VERTICAL_ALIGNMENT);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setVerticalAlignment(oldVerticalAlignment);
                }


                public void redo()
                {
                    setVerticalAlignment(verticalAlignment);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.VERTICAL_ALIGNMENT, oldVerticalAlignment, verticalAlignment);
    }


    @Nullable
    public TextReportElementHorizontalAlignment getHorizontalAlignment()
    {
        return horizontalAlignment;
    }


    public void setHorizontalAlignment(@Nullable final TextReportElementHorizontalAlignment horizontalAlignment)
    {
        final TextReportElementHorizontalAlignment oldHorizontalAlignment = this.horizontalAlignment;
        this.horizontalAlignment = horizontalAlignment;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.HORIZONTAL_ALIGNMENT);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setHorizontalAlignment(oldHorizontalAlignment);
                }


                public void redo()
                {
                    setHorizontalAlignment(horizontalAlignment);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.HORIZONTAL_ALIGNMENT, oldHorizontalAlignment, horizontalAlignment);
    }


    @Nullable
    public String getReservedLiteral()
    {
        return reservedLiteral;
    }


    public void setReservedLiteral(@Nullable final String reservedLiteral)
    {
        final String oldReservedLiteral = this.reservedLiteral;
        this.reservedLiteral = reservedLiteral;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.RESERVED_LITERAL);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setReservedLiteral(oldReservedLiteral);
                }


                public void redo()
                {
                    setReservedLiteral(reservedLiteral);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.RESERVED_LITERAL, oldReservedLiteral, reservedLiteral);
    }


    public boolean isTrimTextContent()
    {
        return trimTextContent;
    }


    public void setTrimTextContent(final boolean trimTextContent)
    {
        final boolean oldTrimTextContent = this.trimTextContent;
        this.trimTextContent = trimTextContent;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.TRIM_TEXT_CONTENT);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setTrimTextContent(oldTrimTextContent);
                }


                public void redo()
                {
                    setTrimTextContent(trimTextContent);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.TRIM_TEXT_CONTENT, oldTrimTextContent, trimTextContent);
    }


    public boolean isWrapTextInExcel()
    {
        return wrapTextInExcel;
    }


    public void setWrapTextInExcel(final boolean wrapTextInExcel)
    {
        final boolean oldWrapTextInExcel = this.wrapTextInExcel;
        this.wrapTextInExcel = wrapTextInExcel;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.WRAP_TEXT_IN_EXCEL);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setWrapTextInExcel(oldWrapTextInExcel);
                }


                public void redo()
                {
                    setWrapTextInExcel(wrapTextInExcel);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.WRAP_TEXT_IN_EXCEL, oldWrapTextInExcel, wrapTextInExcel);
    }


    @Nullable
    public String getEncoding()
    {
        return encoding;
    }


    public void setEncoding(@Nullable final String encoding)
    {
        final String oldEncoding = this.encoding;
        this.encoding = encoding;

        Undo undo = getUndo();
        if (undo != null && !undo.isInProgress())
        {
            undo.startTransaction(PropertyKeys.ENCODING);
            undo.add(new UndoEntry()
            {
                public void undo()
                {
                    setEncoding(oldEncoding);
                }


                public void redo()
                {
                    setEncoding(encoding);
                }
            });
            undo.endTransaction();
        }

        firePropertyChange(PropertyKeys.ENCODING, oldEncoding, encoding);
    }


    @NotNull
    public String getPaintText()
    {
        return getName();
    }


    @NotNull
    private String previousPaintText;
    @NotNull
    private ArrayList<TextParagraph> textParagraphs;


    public void paint(@NotNull GraphicsContext graphicsContext, @NotNull Graphics2D g2d)
    {
        Font origFont = g2d.getFont();
        Shape origClip = g2d.getClip();
        Color origColor = g2d.getColor();
        Composite origComposite = g2d.getComposite();

        Font font = getFont();
        g2d.setFont(font);

        Rectangle2D.Double rect = getRectangle();
        g2d.clip(rect);

        paintBackroundAndBorder(g2d);

        g2d.setColor(foreground);

        String paintText = getPaintText();
        if (!paintText.equals(previousPaintText))
        {
            previousPaintText = paintText;
            textParagraphs = TextParagraph.breakStringIntoParagraphs(paintText, false);
        }

        Rectangle2D.Double contentRectangle = getContentRectangle();
        paintText(textParagraphs, g2d, contentRectangle);


        if (isDynamicContent())
        {
            Object origAntialias = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));

            double x = rect.x + rect.width - 25;
            double y = rect.y + rect.height - 25;

            Ellipse2D.Double ellipse = new Ellipse2D.Double(x + 10, y + 10, 12, 12);

            g2d.setColor(Color.WHITE);
            g2d.fill(ellipse);
            g2d.setColor(Color.BLACK);
            g2d.draw(ellipse);
            g2d.setColor(Color.BLACK);

            Line2D.Double line = new Line2D.Double(x + 10 + 6, y + 12, x + 10 + 6, y + 20);
            Line2D.Double line2 = new Line2D.Double(x + 10 + 3, y + 20 - 3, x + 10 + 6, y + 20);
            Line2D.Double line3 = new Line2D.Double(x + 10 + 6 + 3, y + 20 - 3, x + 10 + 6, y + 20);

            g2d.draw(line);
            g2d.draw(line2);
            g2d.draw(line3);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, origAntialias);
        }

        g2d.setComposite(origComposite);
        g2d.setColor(origColor);
        g2d.setClip(origClip);
        g2d.setFont(origFont);
    }


    private void paintText(@NotNull ArrayList<TextParagraph> textParagraphs, @NotNull Graphics2D g2d, @NotNull Rectangle2D.Double rect)
    {
        try
        {
            double heigth;

            FontRenderContext frc = g2d.getFontRenderContext();

            float wrappingWidth = (float) (rect.getWidth());


            float penY = (float) rect.getY();

            ArrayList<TextLayout> preparedTextLayouts = new ArrayList<TextLayout>();
            Rectangle2D charBounds;
            if (getReport() != null && getReport().isUseMaxCharBounds())
            {
                charBounds = font.getMaxCharBounds(frc);
            }
            else
            {
                charBounds = new Rectangle2D.Double(0, -font.getSize(), 0, font.getSize());
            }

            for (TextParagraph textParagraph : textParagraphs)
            {
                if (textParagraph.getContent().length() == 0)
                {
                    preparedTextLayouts.add(null);
                    penY += -charBounds.getY();
                    if (penY + Math.max(charBounds.getHeight(), lineHeight) + charBounds.getY() > rect.getY() + rect.getHeight())
                    {
                        continue;
                    }
                    else
                    {
                        penY += Math.max(charBounds.getHeight(), lineHeight) + charBounds.getY();//getY()=-40.21844, getHeight()=50.3125
                        continue;
                    }
                }
                AttributedString attributedString = new AttributedString(textParagraph.getContent(), font.getAttributes());
                if (underline)
                {
                    attributedString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                }
                if (strikethrough)
                {
                    attributedString.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                }

                AttributedCharacterIterator styledText = attributedString.getIterator();
                LineBreakMeasurer measurer = new LineBreakMeasurer(styledText, frc);

                double lineHeight = getLineHeight();

                while (measurer.getPosition() < textParagraph.getContent().length())
                {
                    TextLayout layout = measurer.nextLayout(wrappingWidth);


                    penY += -charBounds.getY();
                    if (penY + Math.max(charBounds.getHeight(), lineHeight) + charBounds.getY() > rect.getY() + rect.getHeight())
                    {
                        penY -= -charBounds.getY();
                        break;
                    }
                    penY += Math.max(charBounds.getHeight(), lineHeight) + charBounds.getY();//getY()=-40.21844, getHeight()=50.3125
                    if (penY > (rect.getY() + rect.getHeight()))
                    {
                        penY -= Math.max(charBounds.getHeight(), lineHeight) + charBounds.getY();//getY()=-40.21844, getHeight()=50.3125
                        break;
                    }

                    preparedTextLayouts.add(layout);
                }
            }

            heigth = penY - rect.getY();

            //paint
            float penX = (float) rect.getX();
            penY = (float) rect.getY();
            if (getVerticalAlignment() == TextReportElementVerticalAlignment.MIDDLE)
            {
                penY += ((rect.getHeight() - heigth) / 2);
            }
            else if (getVerticalAlignment() == TextReportElementVerticalAlignment.BOTTOM)
            {
                penY += (rect.getHeight() - heigth);
            }

            double lineHeight = getLineHeight();

            for (TextLayout layout : preparedTextLayouts)
            {
                penY += -charBounds.getY();

                float x = penX;
                if (layout != null)
                {
                    double width = layout.getBounds().getWidth();
                    if (horizontalAlignment == TextReportElementHorizontalAlignment.RIGHT)
                    {
                        x = (float) (penX + rect.getWidth() - 1 - width);
                    }
                    else if (horizontalAlignment == TextReportElementHorizontalAlignment.CENTER)
                    {
                        x = (float) (((penX + rect.getWidth() - 1 - width) + penX) / 2);
                    }
                }

                double ySpaceUsed = penY + Math.max(charBounds.getHeight(), lineHeight) + charBounds.getY();
                double yAllowed = rect.getY() + rect.getHeight();

                //TOLERANCE is used for bottom aligned text. When BottomAligned Text is used ySpaceUsed==yAllowed.
                //Then in depends on the roundoff error if the text is drawn or not->BAD
                //To overcome this problem we increase yAllowed a little bit
                if (ySpaceUsed > yAllowed + TOLERANCE && !isDynamicContent())
                {
                    break;
                }
                if (layout != null)
                {
                    layout.draw(g2d, x, penY);
                }

                penY += Math.max(charBounds.getHeight(), lineHeight) + charBounds.getY();

                if (penY > (rect.getY() + rect.getHeight()) && !isDynamicContent())
                {
                    break;
                }
            }
        }
        catch (Exception e)
        {
            UncaughtExcpetionsModel.getInstance().addException(e);
        }
    }


    protected void externalizeElements(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        super.externalizeElements(xmlWriter, xmlContext);

        xmlWriter.writeProperty(PropertyKeys.FONT, ObjectConverterFactory.getInstance().getFontConverter().getString(font));

        xmlWriter.writeProperty(PropertyKeys.FOREGROUND, ObjectConverterFactory.getInstance().getColorConverter().getString(foreground));
        xmlWriter.writeProperty(PropertyKeys.UNDERLINE, String.valueOf(underline));
        xmlWriter.writeProperty(PropertyKeys.STRIKETHROUGH, String.valueOf(strikethrough));
        xmlWriter.writeProperty(PropertyKeys.EMBED_FONT, String.valueOf(embedFont));
        xmlWriter.writeProperty(PropertyKeys.LINE_HEIGHT, String.valueOf(lineHeight));
        if (verticalAlignment != null)
        {
            xmlWriter.writeProperty(PropertyKeys.VERTICAL_ALIGNMENT, String.valueOf(verticalAlignment));
        }
        if (horizontalAlignment != null)
        {
            xmlWriter.writeProperty(PropertyKeys.HORIZONTAL_ALIGNMENT, String.valueOf(horizontalAlignment));
        }

        if (reservedLiteral != null)
        {
            xmlWriter.writeProperty(PropertyKeys.RESERVED_LITERAL, reservedLiteral);
        }
        xmlWriter.writeProperty(PropertyKeys.TRIM_TEXT_CONTENT, String.valueOf(trimTextContent));
        xmlWriter.writeProperty(PropertyKeys.WRAP_TEXT_IN_EXCEL, String.valueOf(wrapTextInExcel));

        if (encoding != null)
        {
            xmlWriter.writeProperty(PropertyKeys.ENCODING, encoding);
        }
    }


    protected void readElement(@NotNull ExpressionRegistry expressions, @NotNull XmlPullNode node, @NotNull XMLContext xmlContext) throws Exception
    {
        super.readElement(expressions, node, xmlContext);

        if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.FONT.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            font = ObjectConverterFactory.getInstance().getFontConverter().getObject(XMLUtils.readProperty(PropertyKeys.FONT, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.FOREGROUND.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            foreground = ObjectConverterFactory.getInstance().getColorConverter().getObject(XMLUtils.readProperty(PropertyKeys.FOREGROUND, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.UNDERLINE.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            underline = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.UNDERLINE, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.STRIKETHROUGH.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            strikethrough = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.STRIKETHROUGH, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.EMBED_FONT.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            embedFont = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.EMBED_FONT, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.LINE_HEIGHT.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            lineHeight = Double.parseDouble(XMLUtils.readProperty(PropertyKeys.LINE_HEIGHT, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.VERTICAL_ALIGNMENT.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            verticalAlignment = TextReportElementVerticalAlignment.valueOf(XMLUtils.readProperty(PropertyKeys.VERTICAL_ALIGNMENT, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.HORIZONTAL_ALIGNMENT.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            horizontalAlignment = TextReportElementHorizontalAlignment.valueOf(XMLUtils.readProperty(PropertyKeys.HORIZONTAL_ALIGNMENT, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.RESERVED_LITERAL.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            reservedLiteral = XMLUtils.readProperty(PropertyKeys.RESERVED_LITERAL, node);
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.TRIM_TEXT_CONTENT.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            trimTextContent = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.TRIM_TEXT_CONTENT, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.WRAP_TEXT_IN_EXCEL.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            wrapTextInExcel = Boolean.parseBoolean(XMLUtils.readProperty(PropertyKeys.WRAP_TEXT_IN_EXCEL, node));
        }
        else if (XMLConstants.PROPERTY.equals(node.getRawName()) && PropertyKeys.ENCODING.equals(node.getAttributeValueFromRawName(XMLConstants.NAME)))
        {
            encoding = XMLUtils.readProperty(PropertyKeys.ENCODING, node);
        }
    }

}
