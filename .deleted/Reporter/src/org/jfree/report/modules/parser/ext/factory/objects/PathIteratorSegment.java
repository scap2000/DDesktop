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
 * PathIteratorSegment.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.modules.parser.ext.factory.objects;

import java.io.Serializable;

/**
 * This class represents a single segment in a path iterator.
 *
 * @author Thomas Morgner
 */
public class PathIteratorSegment implements Serializable
{
  /**
   * The segment type, one of the PathIterator constants.
   */
  private int segmentType;
  /**
     * The minX coordinate of the first parameter point.
     */
  private float x1;
  /**
     * The minY coordinate of the first parameter point.
     */
  private float y1;
  /**
     * The minX coordinate of the second parameter point.
     */
  private float x2;
  /**
     * The minY coordinate of the second parameter point.
     */
  private float y2;
  /**
     * The minX coordinate of the third parameter point.
     */
  private float x3;
  /**
     * The minY coordinate of the third parameter point.
     */
  private float y3;

  /**
   * Default constructor.
   */
  public PathIteratorSegment ()
  {
  }

  /**
   * Returns the segment type for this PathIterator segment.
   *
   * @return the type of the segment, one of the predefined constants of the class
   *         PathIterator.
   */
  public int getSegmentType ()
  {
    return segmentType;
  }

  /**
   * Defines the segment type for this PathIterator segment.
   *
   * @param segmentType the type of the segment, one of the predefined constants of the
   *                    class PathIterator.
   */
  public void setSegmentType (final int segmentType)
  {
    this.segmentType = segmentType;
  }

  /**
     * Returns the minX coordinate of the first parameter point.
     *
     * @return x coordinate of the first parameter point.
     */
  public float getX1 ()
  {
    return x1;
  }

  /**
     * Defines the minX coordinate of the first parameter point.
     *
     * @param x1 the minX coordinate of the first parameter point.
     */
  public void setX1 (final float x1)
  {
    this.x1 = x1;
  }

  /**
     * Returns the minY coordinate of the first parameter point.
     *
     * @return y coordinate of the first parameter point.
     */
  public float getY1 ()
  {
    return y1;
  }

  /**
     * Defines the minY coordinate of the first parameter point.
     *
     * @param y1 the minY coordinate of the first parameter point.
     */
  public void setY1 (final float y1)
  {
    this.y1 = y1;
  }

  /**
     * Returns the minX coordinate of the second parameter point.
     *
     * @return x coordinate of the second parameter point.
     */
  public float getX2 ()
  {
    return x2;
  }

  /**
     * Defines the minX coordinate of the second parameter point.
     *
     * @param x2 the minX coordinate of the second parameter point.
     */
  public void setX2 (final float x2)
  {
    this.x2 = x2;
  }

  /**
     * Returns the minY coordinate of the second parameter point.
     *
     * @return y coordinate of the second parameter point.
     */
  public float getY2 ()
  {
    return y2;
  }

  /**
     * Defines the minY coordinate of the second parameter point.
     *
     * @param y2 the minY coordinate of the second parameter point.
     */
  public void setY2 (final float y2)
  {
    this.y2 = y2;
  }

  /**
     * Returns the minX coordinate of the third parameter point.
     *
     * @return x coordinate of the third parameter point.
     */
  public float getX3 ()
  {
    return x3;
  }

  /**
     * Defines the minX coordinate of the third parameter point.
     *
     * @param x3 the minX coordinate of the third parameter point.
     */
  public void setX3 (final float x3)
  {
    this.x3 = x3;
  }

  /**
     * Returns the minY coordinate of the third parameter point.
     *
     * @return y coordinate of the third parameter point.
     */
  public float getY3 ()
  {
    return y3;
  }

  /**
     * Defines the minY coordinate of the third parameter point.
     *
     * @param y3 the minY coordinate of the third parameter point.
     */
  public void setY3 (final float y3)
  {
    this.y3 = y3;
  }
}
