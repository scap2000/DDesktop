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
 * DefaultImageReference.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report;

import java.awt.Image;

import java.io.IOException;
import java.io.Serializable;

import java.net.URL;

import org.jfree.report.resourceloader.ImageFactory;
import org.jfree.util.ObjectUtilities;
import org.jfree.util.WaitingImageObserver;

/**
 * An DefaultImageReference encapsulates the source of an image together with a <code>java.awt.Image</code>. The source
 * is used to create a higher resolution version if needed. The source file/URL may also be inlined into the output
 * target, to create better results.
 * <p/>
 * This implementation provides a reasonable default implementation to encapsualte local AWT-images into reports.
 * <p/>
 * The given image might specify a fixed scale factor for the given image. The scaling will be applied before any layout
 * computations will be performed.
 *
 * @author Thomas Morgner
 */
public class DefaultImageReference
    implements Serializable, URLImageContainer, LocalImageContainer
{
  /**
   * A unique identifier for long term persistance.
   */
  private static final long serialVersionUID = 3223926147102983309L;

  /**
   * The image.
   */
  private Image image;

  /**
   * The image URL.
   */
  private URL url;

  /**
   * The width of the (unscaled) image.
   */
  private int width;

  /**
   * The height of the (unscaled) image.
   */
  private int height;

  /**
   * The scale factor.
   */
  private float scaleX = 1.0f;

  /**
   * The scale factor.
   */
  private float scaleY = 1.0f;

  /**
   * Creates a new ImageReference with an origin of 0,0 and the desired width. The image data is read from the given
   * URL.
   *
   * @param url the source url. The url must be readable during the report generation.
   * @throws IOException          if the url could not be read.
   * @throws NullPointerException if the given URL is null.
   */
  public DefaultImageReference(final URL url)
      throws IOException
  {
    if (url == null)
    {
      throw new NullPointerException("URL must not be null.");
    }
    this.url = url;
    this.image = ImageFactory.getInstance().createImage(url);
    if (image == null)
    {
      // no image ...
      throw new IOException("The image could not be loaded.");
    }

    final WaitingImageObserver wob = new WaitingImageObserver(image);
    wob.waitImageLoaded();
    if (wob.isError())
    {
      throw new IOException("Failed to load the image. ImageObserver signaled an error.");
    }
    this.width = image.getWidth(null);
    this.height = image.getHeight(null);
  }

  /**
   * Creates a new ImageReference without an assigned URL for the Image. This image reference will not be loadable and
   * cannot be used to embedd the original rawdata of the image into the generated content.
   *
   * @param img the image for this reference.
   * @throws NullPointerException if the image is null.
   * @throws java.io.IOException  if an IOError occured while loading the image.
   */
  public DefaultImageReference(final Image img)
      throws IOException
  {
    if (img == null)
    {
      throw new NullPointerException();
    }
    this.image = img;
    final WaitingImageObserver obs = new WaitingImageObserver(image);
    obs.waitImageLoaded();
    if (obs.isError())
    {
      throw new IOException("Failed to load the image. ImageObserver signaled an error.");
    }
    this.width = image.getWidth(null);
    this.height = image.getHeight(null);
  }

  /**
   * Creates a new image reference without assigning either an Image or an URL. This DefaultImageReference will act as
   * place holder to reserve space during the layouting, no content will be generated.
   *
   * @param w the width of the unscaled image.
   * @param h the height of the unscaled image.
   */
  public DefaultImageReference(final int w, final int h)
  {
    this.width = w;
    this.height = h;
  }

  /**
   * Copies the contents of the given DefaultImageReference.
   *
   * @param parent the parent.
   */
  public DefaultImageReference(final DefaultImageReference parent)
  {
    if (parent == null)
    {
      throw new NullPointerException("The given parent must not be null.");
    }
    this.width = parent.width;
    this.height = parent.height;
    this.image = parent.image;
    this.url = parent.url;
  }

  /**
   * Returns the original image if available.
   *
   * @return The current image instance, or null, if no image has been assigned.
   */
  public Image getImage()
  {
    return image;
  }

  /**
   * Returns the source URL for the image.
   *
   * @return The URL from where the image has been loaded, or null, if the source URL is not known.
   */
  public URL getSourceURL()
  {
    return url;
  }

  /**
   * Returns the a string version of the source URL. If no URL has been assigned, this method will return null.
   *
   * @return a String representing the assigned URL.
   */
  public String getSourceURLString()
  {
    if (url == null)
    {
      return null;
    }
    return url.toExternalForm();
  }

  /**
   * Returns a String representing this object.  Useful for debugging.
   *
   * @return The string.
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();

    buf.append("ImageReference={ URL=");
    buf.append(getSourceURL());
    buf.append(", image=");
    buf.append(getImage());
    buf.append(", width=");
    buf.append(getImageWidth());
    buf.append(", height=");
    buf.append(getImageHeight());
    buf.append(", scaleX=");
    buf.append(getScaleX());
    buf.append(", scaleY=");
    buf.append(getScaleY());
    buf.append('}');

    return buf.toString();
  }

  /**
   * Checks for equality.
   *
   * @param obj the object to test.
   * @return true if the specified object is equal to this one.
   */
  public boolean equals(final Object obj)
  {
    if (obj == null)
    {
      return false;
    }
    if (obj instanceof DefaultImageReference == false)
    {
      return false;
    }
    final DefaultImageReference ref = (DefaultImageReference) obj;
    if (ObjectUtilities.equal(url, ref.url) == false)
    {
      return false;
    }
    if (width != ref.width)
    {
      return false;
    }
    if (height != ref.height)
    {
      return false;
    }
    if (scaleX != ref.scaleX)
    {
      return false;
    }
    if (scaleY != ref.scaleY)
    {
      return false;
    }
    return true;
  }

  /**
   * Compute a hashcode for this imageReference.
   *
   * @return the hashcode
   */
  public int hashCode()
  {
    int result = width;
    result = 29 * result + height;
    result = 29 * result + Float.floatToIntBits(scaleX);
    result = 29 * result + Float.floatToIntBits(scaleY);
    result = 29 * result + (image != null ? image.hashCode() : 0);
    result = 29 * result + (url != null ? url.hashCode() : 0);
    return result;
  }

  /**
   * Clones this Element.
   *
   * @return a clone of this element.
   * @throws CloneNotSupportedException this should never be thrown.
   */
  public Object clone()
      throws CloneNotSupportedException
  {
    return super.clone();
  }

  /**
   * Returns the (unscaled) image width.
   *
   * @return the image width.
   */
  public int getImageWidth()
  {
    return width;
  }

  /**
   * Returns the (unscaled) image height.
   *
   * @return the image height.
   */
  public int getImageHeight()
  {
    return height;
  }

  /**
   * Checks, whether this image reference is loadable. A default image reference is loadable, if a valid URL has been
   * set.
   *
   * @return true, if it is loadable, false otherwise.
   */
  public boolean isLoadable()
  {
    return getSourceURL() != null;
  }

  /**
   * Returns the identity information. This instance returns the URL of the image, if any.
   *
   * @return the image identifier.
   */
  public Object getIdentity()
  {
    return url;
  }

  /**
   * Returns the name of this image reference. If an URL has been set, this will return the URL of the image, else null
   * is returned.
   *
   * @return the name.
   */
  public String getName()
  {
    if (url != null)
    {
      return url.toExternalForm();
    }
    return null;
  }

  /**
   * Checks, whether this image has a assigned identity. Two identities should be equal, if the image contents are
   * equal.
   *
   * @return true, if that image contains contains identity information, false otherwise.
   */
  public boolean isIdentifiable()
  {
    return url != null;
  }

  /**
   * Returns a predefined scaling factor. That scaling will be applied before any layout specific scaling is done.
   *
   * @return the scale factor.
   */
  public float getScaleX()
  {
    return scaleX;
  }

  /**
   * Returns a predefined scaling factor. That scaling will be applied before any layout specific scaling is done.
   *
   * @return the scale factor.
   */
  public float getScaleY()
  {
    return scaleY;
  }

  /**
   * Defines a predefined scaling factor. That scaling will be applied before any layout specific scaling is done.
   * <p/>
   * If your image has a higher resolution than 72dpi, this factor should be a value lower than 1 (the image will be
   * scaled down).
   *
   * @param sx the scale factor.
   * @param sy the scale factor.
   */
  public void setScale(final float sx, final float sy)
  {
    this.scaleX = sx;
    this.scaleY = sy;
  }
}
