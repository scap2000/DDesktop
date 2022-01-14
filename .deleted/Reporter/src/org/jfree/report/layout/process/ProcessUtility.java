package org.jfree.report.layout.process;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import org.jfree.report.layout.model.BorderEdge;
import org.jfree.report.layout.model.CanvasRenderBox;
import org.jfree.report.layout.model.InlineRenderBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.RenderableReplacedContent;
import org.jfree.report.layout.model.RenderableText;
import org.jfree.report.style.BorderStyle;
import org.jfree.report.style.ElementStyleKeys;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.StrokeUtility;
import org.jfree.report.util.geom.StrictGeomUtility;

/**
 * Creation-Date: 16.07.2007, 13:42:43
 *
 * @author Thomas Morgner
 */
public class ProcessUtility
{
  private ProcessUtility()
  {
  }
//  public static boolean isContent(final RenderBox element,
//                                  final boolean recursive)
//  {
//    return isContent(element, recursive, false, false);
//  }

  public static boolean isContent(final RenderBox element,
                                  final boolean recursive,
                                  final boolean ellipseAsBackground,
                                  final boolean shapesAsContent)
  {

    // For legacy reasons: A single ReplacedContent in a paragraph means, we may have a old-style border and
    // background definition.
    final RenderNode firstChild = element.getFirstChild();
    if (element instanceof CanvasRenderBox &&
        firstChild == element.getLastChild() &&
        firstChild instanceof RenderableReplacedContent)
    {

      final RenderableReplacedContent rpc = (RenderableReplacedContent) firstChild;
      final Object rawObject = rpc.getRawObject();
      final StyleSheet styleSheet = element.getStyleSheet();
      if (shapesAsContent == false)
      {
        if (rawObject instanceof Shape)
        {
          return false;
        }
      }
      if (rawObject instanceof Line2D)
      {
        if (hasBorderEdge(styleSheet))
        {
          final Line2D line = (Line2D) rawObject;
          if (line.getY1() == line.getY2())
          {
            return false;
          }
          else if (line.getX1() == line.getX2())
          {
            return false;
          }
        }
      }
      else if (rawObject instanceof Rectangle2D)
      {
        return false;
      }
      else if (ellipseAsBackground && rawObject instanceof Ellipse2D)
      {
        return false;
      }
      else if (rawObject instanceof RoundRectangle2D)
      {
        return false;
      }
    }

    RenderNode child = element.getFirstChild();
    while (child != null)
    {
      if (child instanceof InlineRenderBox)
      {
        return true;
      }
      else if (child instanceof RenderableText)
      {
        return true;
      }
      else if (child instanceof RenderableReplacedContent)
      {
        return true;
      }
      else if (recursive && child instanceof RenderBox)
      {
        if (isContent((RenderBox) child, true, ellipseAsBackground, shapesAsContent))
        {
          return true;
        }
      }
      child = child.getNext();
    }
    return false;
  }

  public static boolean hasBorderEdge(final StyleSheet style)
  {
    final Stroke s = (Stroke) style.getStyleProperty(ElementStyleKeys.STROKE);
    if (s instanceof BasicStroke == false)
    {
      return false;
    }
    final BasicStroke bs = (BasicStroke) s;
    final BorderStyle borderStyle = translateStrokeStyle(s);
    if (BorderStyle.NONE.equals(borderStyle))
    {
      return false;
    }
    return true;
  }

  public static BorderEdge produceBorderEdge(final StyleSheet style)
  {
    final Stroke s = (Stroke) style.getStyleProperty(ElementStyleKeys.STROKE);
    if (s instanceof BasicStroke == false)
    {
      return null;
    }
    final BasicStroke bs = (BasicStroke) s;
    final BorderStyle borderStyle = translateStrokeStyle(s);
    if (BorderStyle.NONE.equals(borderStyle))
    {
      return null;
    }

    final Color c = (Color) style.getStyleProperty(ElementStyleKeys.PAINT);
    return new BorderEdge(borderStyle, c, StrictGeomUtility.toInternalValue(bs.getLineWidth()));
  }

  public static BorderStyle translateStrokeStyle(final Stroke s)
  {
    final int style = StrokeUtility.getStrokeType(s);
    switch (style)
    {
      case StrokeUtility.STROKE_NONE:
        return BorderStyle.NONE;
      case StrokeUtility.STROKE_DASHED:
        return BorderStyle.DASHED;
      case StrokeUtility.STROKE_DOTTED:
        return BorderStyle.DOTTED;
      case StrokeUtility.STROKE_DOT_DASH:
        return BorderStyle.DOT_DASH;
      case StrokeUtility.STROKE_DOT_DOT_DASH:
        return BorderStyle.DOT_DOT_DASH;
      default:
        return BorderStyle.SOLID;
    }
  }
}
