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
 * SingleItemHtmlPrinter.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.modules.output.table.html;

import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.output.ContentProcessingException;
import org.jfree.report.layout.output.LogicalPageKey;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.modules.output.table.base.TableContentProducer;
import org.jfree.repository.ContentItem;
import org.jfree.resourceloader.ResourceManager;

/**
 * A HTML-Generator that generates one file in a predefined location. Only the first stream is written, all other
 * attempts to generate content will be silently ignored.
 *
 * @author Thomas Morgner
 */
public class SingleItemHtmlPrinter extends HtmlPrinter
{
  private boolean printed;

  public SingleItemHtmlPrinter(final ResourceManager resourceManager, final ContentItem documentContentItem)
  {
    super(resourceManager);
    setDocumentContentItem(documentContentItem);
  }

  public void print(final LogicalPageKey logicalPageKey,
                    final LogicalPageBox logicalPage,
                    final TableContentProducer contentProducer,
                    final OutputProcessorMetaData metaData,
                    final boolean incremental) throws ContentProcessingException
  {
    if (printed)
    {
      return;
    }

    super.print(logicalPageKey, logicalPage, contentProducer, metaData, incremental);
    if (incremental == false)
    {
      printed = true;
    }
  }
}
