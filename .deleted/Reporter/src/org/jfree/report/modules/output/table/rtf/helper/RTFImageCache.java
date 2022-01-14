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
 * RTFImageCache.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.rtf.helper;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;

import java.util.HashMap;

import org.jfree.io.IOUtils;
import org.jfree.report.ImageContainer;
import org.jfree.report.LocalImageContainer;
import org.jfree.report.URLImageContainer;
import org.jfree.report.layout.output.RenderUtility;
import org.jfree.report.resourceloader.ImageFactory;
import org.jfree.report.util.MemoryByteArrayOutputStream;
import org.jfree.util.Log;
import org.jfree.util.StringUtils;
import org.jfree.util.WaitingImageObserver;

/**
 * Todo: Document me!
 *
 * @author : Thomas Morgner
 */
public class RTFImageCache
{
  private HashMap cachedImages;

  public RTFImageCache()
  {
    this.cachedImages = new HashMap();
  }

  private boolean isSupportedFormat(final URL sourceURL)
  {
    final String file = sourceURL.getFile();
    if (StringUtils.endsWithIgnoreCase(file, ".png"))
    {
      return true;
    }
    if (StringUtils.endsWithIgnoreCase(file, ".jpg") ||
        StringUtils.endsWithIgnoreCase(file, ".jpeg"))
    {
      return true;
    }
    if (StringUtils.endsWithIgnoreCase(file, ".bmp") ||
        StringUtils.endsWithIgnoreCase(file, ".ico"))
    {
      return true;
    }
    return false;
  }

  /**
   * Helperfunction to extract an image from an imagereference. If the image is contained
   * as java.awt.Image object only, the image is recoded into an PNG-Image.
   *
   * @param reference the image reference.
   * @return an image.
   * @throws com.lowagie.text.DocumentException
   *                             if no PDFImageElement could be created using the given
   *                             ImageReference.
   * @throws java.io.IOException if the image could not be read.
   */
  public Image getImage(final ImageContainer reference)
      throws DocumentException, IOException
  {
    Object identity = null;
    java.awt.Image image = null;
    if (reference instanceof URLImageContainer)
    {
      final URLImageContainer urlImageContainer = (URLImageContainer) reference;
      final URL url = urlImageContainer.getSourceURL();
      if (url != null && urlImageContainer.isLoadable())
      {
        try
        {
          identity = String.valueOf(url);
          final Image cached = (Image) cachedImages.get(identity);
          if (cached != null)
          {
            return cached;
          }

          if (isSupportedFormat(url) == false)
          {
            // This is a unsupported image format.
            if (reference instanceof LocalImageContainer)
            {
              final LocalImageContainer li = (LocalImageContainer) reference;
              image = li.getImage();
            }
            if (image == null)
            {
              image = ImageFactory.getInstance().createImage(url);
            }
          }
          else
          {
            final MemoryByteArrayOutputStream bout = new MemoryByteArrayOutputStream();
            final InputStream urlIn = new BufferedInputStream(url.openStream());
            try
            {
              IOUtils.getInstance().copyStreams(urlIn, bout);
            }
            finally
            {
              bout.close();
              urlIn.close();
            }

            final byte[] data = bout.toByteArray();
            final Image itextimage = Image.getInstance(data);
            cachedImages.put(identity, itextimage);
            return itextimage;
          }
        }
        catch (BadElementException be)
        {
          Log.info("Caught illegal Image, will recode to PNG instead", be);
        }
        catch (IOException ioe)
        {
          Log.info("Unable to read the raw-data, will try to recode image-data.", ioe);
        }
      }
    }

    if (reference instanceof LocalImageContainer && image == null)
    {
      final LocalImageContainer localImageContainer =
          (LocalImageContainer) reference;
      image = localImageContainer.getImage();
      if (image != null)
      {
        // check, if the content was cached ...
        identity = localImageContainer.getIdentity();
        if (identity != null)
        {
          final Image cachedImage = (Image) cachedImages.get(identity);
          if (cachedImage != null)
          {
            return cachedImage;
          }
        }

      }
    }
    if (image == null)
    {
      return null;
    }

    final WaitingImageObserver obs = new WaitingImageObserver(image);
    obs.waitImageLoaded();

    final byte[] data = RenderUtility.encodeImage(image);
    final Image itextimage = Image.getInstance(data);
    if (identity != null)
    {
      cachedImages.put(identity, itextimage);
    }
    return itextimage;
  }

}
