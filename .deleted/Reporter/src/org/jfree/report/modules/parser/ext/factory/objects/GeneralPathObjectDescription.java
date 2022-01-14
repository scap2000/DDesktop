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
 * GeneralPathObjectDescription.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.ext.factory.objects;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

import java.util.ArrayList;

import org.jfree.report.modules.parser.ext.factory.base.AbstractObjectDescription;
import org.jfree.report.modules.parser.ext.factory.base.ObjectFactoryException;

/**
 * An Object Description for general shapes and the GeneralPath-class.
 *
 * @author Thomas Morgner
 */
public class GeneralPathObjectDescription extends AbstractObjectDescription
{
  /**
   * A constant for the "segments" parameter.
   */
  private static final String SEGMENTS_NAME = "segments";

  /**
   * A constant for the "windingRule" parameter.
   */
  private static final String WINDING_RULE_NAME = "windingRule";

  /**
   * A constant value for the "windingRule" parameter.
   */
  private static final String WINDING_RULE_EVEN_ODD = "wind-even-odd";

  /**
   * A constant value for the "windingRule" parameter.
   */
  private static final String WINDING_RULE_NON_ZERO = "wind-non-zero";

  /**
   * The number of maximum points in a path iterator segment.
   */
  private static final int MAX_POINTS = 6;

  /**
   * DefaultConstructor. Initializes this object description to produce GeneralPath
   * objects.
   */
  public GeneralPathObjectDescription ()
  {
    this(GeneralPath.class);
  }

  /**
   * Creates a new GeneralPathObjectDescription. The given class must be an instance of an
   * shape, the generated objects will be general path objects.
   *
   * @param c the registered base class, an instance of shape.
   */
  public GeneralPathObjectDescription (final Class c)
  {
    super(c);
    if (Shape.class.isAssignableFrom(c) == false)
    {
      throw new IllegalArgumentException("Must be a shape instance");
    }
    // "even-odd" or "non-zero"
    setParameterDefinition(WINDING_RULE_NAME, String.class);
    setParameterDefinition(SEGMENTS_NAME, PathIteratorSegment[].class);
  }

  /**
   * Creates an object based on this description.
   *
   * @return The object.
   */
  public Object createObject ()
  {
    final int wRule = parseWindingRule();
    if (wRule == -1)
    {
      return null;
    }

    final PathIteratorSegment[] segments = (PathIteratorSegment[]) getParameter(SEGMENTS_NAME);
    if (segments == null)
    {
      return null;
    }

    final GeneralPath path = new GeneralPath();
    path.setWindingRule(wRule);
    for (int i = 0; i < segments.length; i++)
    {
      final int segmentType = segments[i].getSegmentType();
      switch (segmentType)
      {
        case PathIterator.SEG_CLOSE:
          {
            path.closePath();
            break;
          }
        case PathIterator.SEG_CUBICTO:
          {
            path.curveTo(segments[i].getX1(), segments[i].getY1(),
                    segments[i].getX2(), segments[i].getY2(),
                    segments[i].getX3(), segments[i].getY3());
            break;
          }
        case PathIterator.SEG_LINETO:
          {
            path.lineTo(segments[i].getX1(), segments[i].getY1());
            break;
          }
        case PathIterator.SEG_MOVETO:
          {
            path.moveTo(segments[i].getX1(), segments[i].getY1());
            break;
          }
        case PathIterator.SEG_QUADTO:
          {
            path.quadTo(segments[i].getX1(), segments[i].getY1(),
                    segments[i].getX2(), segments[i].getY2());
            break;
          }
        default:
          throw new IllegalStateException("Unexpected result from path iterator.");
      }
    }
    return path;
  }

  /**
   * Translates the winding rule parameter into a predefined PathIterator constant.
   *
   * @return the translated winding rule or -1 if the rule was invalid.
   */
  private int parseWindingRule ()
  {
    final String windingRule = (String) getParameter(WINDING_RULE_NAME);
    int wRule = -1;
    if (windingRule == null)
    {
      return wRule;
    }
    if (windingRule.equals(WINDING_RULE_EVEN_ODD))
    {
      wRule = PathIterator.WIND_EVEN_ODD;
    }
    else if (windingRule.equals(WINDING_RULE_NON_ZERO))
    {
      wRule = PathIterator.WIND_NON_ZERO;
    }
    return wRule;
  }

  /**
   * Sets the parameters of this description object to match the supplied object.
   *
   * @param o the object (should be an instance of <code>FontDefinition</code>).
   * @throws ObjectFactoryException if the object is not an instance of <code>Float</code>.
   */
  public void setParameterFromObject (final Object o)
          throws ObjectFactoryException
  {
    if (getObjectClass().isAssignableFrom(o.getClass()) == false)
    {
      throw new ObjectFactoryException("Class is not assignable");
    }

    final Shape s = (Shape) o;
    final PathIterator pi = s.getPathIterator(AffineTransform.getTranslateInstance(0, 0));
    if (pi.getWindingRule() == PathIterator.WIND_EVEN_ODD)
    {
      setParameter(WINDING_RULE_NAME, WINDING_RULE_EVEN_ODD);
    }
    else
    {
      setParameter(WINDING_RULE_NAME, WINDING_RULE_NON_ZERO);
    }

    final float[] points = new float[MAX_POINTS];
    final ArrayList segments = new ArrayList();
    while (pi.isDone() == false)
    {
      final int type = pi.currentSegment(points);
      final PathIteratorSegment seg = new PathIteratorSegment();
      switch (type)
      {
        case PathIterator.SEG_CLOSE:
          {
            seg.setSegmentType(PathIterator.SEG_CLOSE);
            break;
          }
        case PathIterator.SEG_CUBICTO:
          {
            seg.setSegmentType(PathIterator.SEG_CUBICTO);
            seg.setX1(points[0]);
            seg.setY1(points[1]);
            seg.setX2(points[2]);
            seg.setY2(points[3]);
            seg.setX3(points[4]);
            seg.setY3(points[5]);
            break;
          }
        case PathIterator.SEG_LINETO:
          {
            seg.setSegmentType(PathIterator.SEG_LINETO);
            seg.setX1(points[0]);
            seg.setY1(points[1]);
            break;
          }
        case PathIterator.SEG_MOVETO:
          {
            seg.setSegmentType(PathIterator.SEG_MOVETO);
            seg.setX1(points[0]);
            seg.setY1(points[1]);
            break;
          }
        case PathIterator.SEG_QUADTO:
          {
            seg.setSegmentType(PathIterator.SEG_QUADTO);
            seg.setX1(points[0]);
            seg.setY1(points[1]);
            seg.setX2(points[2]);
            seg.setY2(points[3]);
            break;
          }
        default:
          throw new IllegalStateException("Unexpected result from PathIterator.");
      }
      segments.add(seg);
      pi.next();
    }

    setParameter(SEGMENTS_NAME, segments.toArray(new PathIteratorSegment[segments.size()]));
  }
}
