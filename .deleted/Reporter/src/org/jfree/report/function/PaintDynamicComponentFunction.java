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
 * PaintDynamicComponentFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.function;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.io.IOException;

import org.jfree.report.DefaultImageReference;
import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.util.ComponentDrawable;
import org.jfree.report.util.ImageUtils;
import org.jfree.util.Configuration;
import org.jfree.util.Log;

/**
 * Paints a AWT or Swing Component. The component must be contained in the dataRow.
 *
 * @author Thomas Morgner
 * @deprecated Use the new Component-Element instead. It uses drawables for this
 * job, and therefore the result looks much better.
 */
public class PaintDynamicComponentFunction extends AbstractFunction implements PageEventListener
{
  /**
   * the created image, cached for getValue().
   */
  private transient Image image;

  /**
   * The field from where to read the AWT-Component.
   */
  private String field;

  /**
   * The scale factor.
   */
  private float scale;

  /**
   * DefaultConstructor.
   *
   * @throws IllegalStateException (HeadlessException) if no full AWT is available. This
   *                               function needs a working layout manager.
   */
  public PaintDynamicComponentFunction ()
  {
    scale = 1;
  }

  /**
     * Returns the field used by the function. The field name corresponds to a destColumn name in the report's data-row.
     *
     * @return The field name.
     */
  public String getField()
  {
    return field;
  }

  /**
     * Sets the field name for the function. The field name corresponds to a destColumn name in the report's data-row.
     *
     * @param field the field name.
     */
  public void setField(final String field)
  {
    this.field = field;
  }

  /**
   * Receives notification that the report has started.
   *
   * @param event the event.
   */
  public void reportStarted (final ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that report generation initializes the current run. <P> The
   * event carries a ReportState.Started state.  Use this to initialize the report.
   *
   * @param event The event.
   */
  public void reportInitialized (final ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that the report has finished.
   *
   * @param event the event.
   */
  public void reportFinished (final ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that a page has started.
   *
   * @param event the event.
   */
  public void pageStarted (final ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that a page has ended.
   *
   * @param event the event.
   */
  public void pageFinished (final ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that a group has started.
   *
   * @param event the event.
   */
  public void groupStarted (final ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that a group has finished.
   *
   * @param event the event.
   */
  public void groupFinished (final ReportEvent event)
  {
    image = null;
  }

  /**
   * Receives notification that a row of data is being processed.
   *
   * @param event the event.
   */
  public void itemsAdvanced (final ReportEvent event)
  {
    image = null;
  }

  /**
   * Returns the device-resolution from the report-configuration.
   *
   * @return the resolution defined in the report-configuration.
   */
  private float getDeviceScale ()
  {
    final Configuration config = getReportConfiguration();
    final String resolution = config.getConfigProperty("org.jfree.report.layout.DeviceResolution");
    if (resolution == null)
    {
      return 1;
    }
    try
    {
      return Float.parseFloat(resolution) / 72.0f;
    }
    catch(NumberFormatException nfe)
    {
      return 1;
    }
  }

  /**
   * Creates the component.
   *
   * @return the created image or null, if no image could be created.
   */
  private Image createComponentImage ()
  {
    final Object o = getDataRow().get(getField());
    if ((o instanceof Component) == false)
    {
      return null;
    }

    final float scale = getScale() * getDeviceScale();

    final ComponentDrawable drawable = new ComponentDrawable();
    drawable.setComponent((Component) o);
    drawable.setAllowOwnPeer(true);
    drawable.setPaintSynchronized(true);
    final Dimension dim = drawable.getSize();

    final int width = Math.max (1, (int) (scale * dim.width));
    final int height = Math.max (1, (int) (scale * dim.height));

    final BufferedImage bi = ImageUtils.createTransparentImage(width, height);
    final Graphics2D graph = bi.createGraphics();
    graph.setBackground(new Color(0, 0, 0, 0));
    graph.setTransform(AffineTransform.getScaleInstance(scale, scale));
    drawable.draw(graph, new Rectangle2D.Float(0, 0, dim.width, dim.height));
    graph.dispose();

    return bi;
  }

  /**
   * Return the current expression value. <P> The value depends (obviously) on the
   * expression implementation.
   *
   * @return the value of the function.
   */
  public Object getValue ()
  {
    if (image == null)
    {
      image = createComponentImage();
    }

    try
    {
      final DefaultImageReference ref = new DefaultImageReference(image);
      ref.setScale(1.0f / getScale(), 1.0f / getScale());
      return ref;
    }
    catch (IOException e)
    {
      Log.warn ("Unable to fully load a given image. (It should not happen here.)");
      return null;
    }
  }

  /**
   * Define a scale factor for the created image. Using a higher scale factor will produce
   * better results. A scale factor of 2 will double the resolution. A scale factor of 1
   * will create 72 dpi images.
   *
   * @param scale the scale factor.
   */
  public void setScale (final float scale)
  {
    this.scale = scale;
  }


  /**
   * Gets the scale factor for the created image. Using a higher scale factor will produce
   * better results. A scale factor of 2 will double the resolution. A scale factor of 1
   * will create 72 dpi images.
   *
   * @return the scale factor.
   */
  public float getScale ()
  {
    return scale;
  }
}
