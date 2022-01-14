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
 * ImageRenderFunction.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */
package org.jfree.report.ancient.demo.functions;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JRadioButton;

import org.jfree.report.DefaultImageReference;
import org.jfree.report.event.PageEventListener;
import org.jfree.report.event.ReportEvent;
import org.jfree.report.function.AbstractFunction;
import org.jfree.util.Log;

/**
 * The ImageRenderFunction creates a simple Image using a BufferedImage within a function
 * to show the use of the ImageFunctionElement. The image is created whenever a new page
 * is started.
 *
 * @author Thomas Morgner
 */
public class ImageRenderFunction extends AbstractFunction
        implements Serializable, PageEventListener
{
  /**
   * The function value.
   */
  private transient DefaultImageReference functionValue;

  /**
   * Creates an unnamed function. Make sure the name of the function is set using {@link
   * #setName} before the function is added to the report's function collection.
   */
  public ImageRenderFunction ()
  {
  }

  /**
   * Create a image according to the current state, simple and silly ...
   *
   * @param event the report event.
   */
  public void pageStarted (final ReportEvent event)
  {
    final BufferedImage image = new BufferedImage(150, 50, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D g2 = image.createGraphics();
    final JButton bt = new JButton("A Button");
    bt.setSize(90, 20);
    final JRadioButton radio = new JRadioButton("A radio button");
    radio.setSize(100, 20);

    g2.setColor(Color.darkGray);
    bt.paint(g2);
    g2.setColor(Color.blue);
    g2.setTransform(AffineTransform.getTranslateInstance(20, 20));
    radio.paint(g2);
    g2.setTransform(AffineTransform.getTranslateInstance(0, 0));
    g2.setPaint(Color.green);
    g2.setFont(new Font("Serif", Font.PLAIN, 10));
    g2.drawString("You are viewing a graphics of JFreeReport on index "
            + event.getState().getCurrentDataItem(), 10, 10);
    g2.dispose();
    try
    {
      functionValue = new DefaultImageReference(image);
    }
    catch (IOException e)
    {
      Log.warn ("Unable to fully load a given image. (It should not happen here.)");
      functionValue = null;
    }
  }

  /**
   * Receives notification that a page is completed.
   *
   * @param event The event.
   */
  public void pageFinished (final ReportEvent event)
  {
  }

  /**
   * Return the last generated Image.
   *
   * @return the function value.
   */
  public Object getValue ()
  {
    return functionValue;
  }
}
