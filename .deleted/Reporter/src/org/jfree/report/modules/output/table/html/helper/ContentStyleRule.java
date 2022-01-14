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
 * ContentStyleRule.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.html.helper;

import org.jfree.report.ElementAlignment;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.style.TextStyleKeys;
import org.jfree.report.style.VerticalTextAlign;

/**
 * A CSS rule for ordinary content.
 *
 * @author Thomas Morgner
 */
public class ContentStyleRule implements HtmlStyleRule
{
  private String fontName;
  private double fontSize;
  private boolean bold;
  private boolean italics;
  private boolean underlined;
  private boolean strikeThrough;
  private boolean textWrap;

  private VerticalTextAlign verticalAlignment;
  private ElementAlignment textAlignment;

  private BoxDefinition boxDefinition;

  public ContentStyleRule(final RenderBox renderBox, final OutputProcessorMetaData metaData)
  {
    final StyleSheet styleSheet = renderBox.getStyleSheet();

    boxDefinition = renderBox.getBoxDefinition();
    textAlignment = (ElementAlignment) styleSheet.getStyleProperty(ElementStyleKeys.ALIGNMENT);
    verticalAlignment = renderBox.getVerticalTextAlignment();

    textWrap = "none".equals(styleSheet.getStyleProperty(TextStyleKeys.TEXT_WRAP, "wrap")) == false;
    strikeThrough = styleSheet.getBooleanStyleProperty(TextStyleKeys.STRIKETHROUGH);
    underlined = styleSheet.getBooleanStyleProperty(TextStyleKeys.UNDERLINED);
    italics = styleSheet.getBooleanStyleProperty(TextStyleKeys.ITALIC);
    bold = styleSheet.getBooleanStyleProperty(TextStyleKeys.BOLD);
    fontSize = styleSheet.getDoubleStyleProperty(TextStyleKeys.FONTSIZE, 0);
    fontName = metaData.getNormalizedFontFamilyName((String) styleSheet.getStyleProperty(TextStyleKeys.FONT));
  }

  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    final ContentStyleRule that = (ContentStyleRule) o;

    if (bold != that.bold)
    {
      return false;
    }
    if (that.fontSize != fontSize)
    {
      return false;
    }
    if (italics != that.italics)
    {
      return false;
    }
    if (strikeThrough != that.strikeThrough)
    {
      return false;
    }
    if (textWrap != that.textWrap)
    {
      return false;
    }
    if (underlined != that.underlined)
    {
      return false;
    }
    if (!boxDefinition.equals(that.boxDefinition))
    {
      return false;
    }
    if (!fontName.equals(that.fontName))
    {
      return false;
    }
    if (!textAlignment.equals(that.textAlignment))
    {
      return false;
    }
    if (!verticalAlignment.equals(that.verticalAlignment))
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result = fontName.hashCode();
    final long temp = fontSize != +0.0d ? Double.doubleToLongBits(fontSize) : 0L;
    result = 29 * result + (int) (temp ^ (temp >>> 32));
    result = 29 * result + (bold ? 1 : 0);
    result = 29 * result + (italics ? 1 : 0);
    result = 29 * result + (underlined ? 1 : 0);
    result = 29 * result + (strikeThrough ? 1 : 0);
    result = 29 * result + (textWrap ? 1 : 0);
    result = 29 * result + verticalAlignment.hashCode();
    result = 29 * result + textAlignment.hashCode();
    result = 29 * result + boxDefinition.hashCode();
    return result;
  }

  public String getCSSText(final boolean inline)
  {
    return null;
  }
}
