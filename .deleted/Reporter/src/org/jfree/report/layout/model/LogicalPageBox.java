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
 * LogicalPage.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout.model;

import org.jfree.report.PageDefinition;
import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.layout.style.SimpleStyleSheet;
import org.jfree.report.util.InstanceID;

/**
 * The logical page is the root-structure of the generated content. This object is a slotted container.
 *
 * @author Thomas Morgner
 */
public class LogicalPageBox extends BlockRenderBox
{

  // All breaks along the major-axis.
  private PageBreakPositionList allVerticalBreaks;
  private long pageOffset;
  private long pageEnd;

  private WatermarkAreaBox watermarkArea;
  private PageAreaBox headerArea;
  private PageAreaBox footerArea;
  private DefaultPageGrid pageGrid;
  private InstanceID contentAreaId;
  private String pageName;

  public LogicalPageBox(final PageDefinition pageDefinition)
  {
    super(SimpleStyleSheet.EMPTY_STYLE, BoxDefinition.EMPTY, null);
    this.headerArea = new PageAreaBox(getStyleSheet(), BoxDefinition.EMPTY);
    this.headerArea.setName("Logical-Page-Header-Area");
    this.headerArea.setLogicalPage(this);
    this.footerArea = new PageAreaBox(getStyleSheet(), BoxDefinition.EMPTY);
    this.footerArea.setName("Logical-Page-Footer-Area");
    this.footerArea.setLogicalPage(this);

    final BoxDefinition boxDefinition = new BoxDefinition();
    boxDefinition.setPreferredHeight(new RenderLength(100000, true));
    this.watermarkArea = new WatermarkAreaBox(getStyleSheet(), boxDefinition);
    this.watermarkArea.setName("Logical-Page-Watermark-Area");
    this.watermarkArea.setLogicalPage(this);

    final BlockRenderBox contentArea = new BlockRenderBox(getStyleSheet(), BoxDefinition.EMPTY, null);
    contentArea.setName("Logical-Page-Content-Area");
    addChild(contentArea);
    contentAreaId = contentArea.getInstanceId();
    this.pageGrid = new DefaultPageGrid(pageDefinition);

    this.allVerticalBreaks = new PageBreakPositionList(null, 1);
  }

  public BlockRenderBox getContentArea()
  {
    final BlockRenderBox blockRenderBox = (BlockRenderBox) findNodeById(contentAreaId);
    if (blockRenderBox == null)
    {
      throw new IllegalStateException("Cloning or deriving must have failed: No content area.");
    }
    return blockRenderBox;
  }

  public BlockRenderBox getHeaderArea()
  {
    return headerArea;
  }

  public BlockRenderBox getFooterArea()
  {
    return footerArea;
  }

  public WatermarkAreaBox getWatermarkArea()
  {
    return watermarkArea;
  }

  public LogicalPageBox getLogicalPage()
  {
    return this;
  }

  public long getPageWidth()
  {
    return pageGrid.getMaximumPageWidth();
  }

  public PageGrid getPageGrid()
  {
    return pageGrid;
  }

  public long getPageOffset()
  {
    return pageOffset;
  }

  public void setPageOffset(final long pageOffset)
  {
    this.pageOffset = pageOffset;
  }

  public long getPageEnd()
  {
    return pageEnd;
  }

  public void setPageEnd(final long pageEnd)
  {
    this.pageEnd = pageEnd;
  }

  public long[] getPhysicalBreaks(final int axis)
  {
    if (axis == RenderNode.HORIZONTAL_AXIS)
    {
      return pageGrid.getHorizontalBreaks();
    }
    return pageGrid.getVerticalBreaks();
  }

  public long getPageHeight()
  {
    return pageGrid.getMaximumPageHeight();
  }

  /**
   * Derive creates a disconnected node that shares all the properties of the
   * original node. The derived node will no longer have any parent, silbling,
   * child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode deriveFrozen(final boolean deepDerive)
  {
    final LogicalPageBox box = (LogicalPageBox) super.deriveFrozen(deepDerive);
    box.headerArea = (PageAreaBox) headerArea.deriveFrozen(deepDerive);
    box.headerArea.setLogicalPage(box);
    box.footerArea = (PageAreaBox) footerArea.deriveFrozen(deepDerive);
    box.footerArea.setLogicalPage(box);
    box.watermarkArea = (WatermarkAreaBox) watermarkArea.deriveFrozen(deepDerive);
    box.watermarkArea.setLogicalPage(box);
//
//    box.subFlows.clear();
//
//    for (int i = 0; i < subFlows.size(); i++)
//    {
//      NormalFlowRenderBox flowRenderBox = (NormalFlowRenderBox) subFlows.get(i);
//      box.subFlows.add(flowRenderBox.deriveFrozen(deepDerive));
//    }

    return box;
  }
  
  /**
   * Derive creates a disconnected node that shares all the properties of the
   * original node. The derived node will no longer have any parent, silbling,
   * child or any other relationships with other nodes.
   *
   * @return
   */
  public RenderNode derive(final boolean deepDerive)
  {
    final LogicalPageBox box = (LogicalPageBox) super.derive(deepDerive);
    box.headerArea = (PageAreaBox) headerArea.derive(deepDerive);
    box.headerArea.setLogicalPage(box);
    box.footerArea = (PageAreaBox) footerArea.derive(deepDerive);
    box.footerArea.setLogicalPage(box);
    box.watermarkArea = (WatermarkAreaBox) watermarkArea.derive(deepDerive);
    box.watermarkArea.setLogicalPage(box);
//    box.subFlows.clear();
//
//    for (int i = 0; i < subFlows.size(); i++)
//    {
//      NormalFlowRenderBox flowRenderBox = (NormalFlowRenderBox) subFlows.get(i);
//      box.subFlows.add(flowRenderBox.deriveForAdvance(deepDerive));
//    }

    return box;
  }

  /**
   * Derives an hibernation copy. The resulting object should get stripped of
   * all unnecessary caching information and all objects, which will be
   * regenerated when the layouting restarts. Size does matter here.
   *
   * @return
   */
  public RenderNode hibernate()
  {
    final LogicalPageBox box = (LogicalPageBox) super.hibernate();
    box.headerArea = (PageAreaBox) headerArea.hibernate();
    box.headerArea.setLogicalPage(box);
    box.footerArea = (PageAreaBox) footerArea.hibernate();
    box.footerArea.setLogicalPage(box);
    box.watermarkArea = (WatermarkAreaBox) watermarkArea.hibernate();
    box.watermarkArea.setLogicalPage(box);

//    box.subFlows.clear();
//
//    for (int i = 0; i < subFlows.size(); i++)
//    {
//      NormalFlowRenderBox flowRenderBox = (NormalFlowRenderBox) subFlows.get(i);
//      box.subFlows.add(flowRenderBox.hibernate());
//    }

    return box;
  }

  /**
   * Clones this node. Be aware that cloning can get you into deep trouble, as
   * the relations this node has may no longer be valid.
   *
   * @return
   */
  public Object clone()
  {
    try
    {
      final LogicalPageBox o = (LogicalPageBox) super.clone();
      o.pageGrid = (DefaultPageGrid) pageGrid.clone();
      o.allVerticalBreaks = allVerticalBreaks;
      return o;
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalStateException("Cloning *must* be supported.");
    }
  }

  public void setAllVerticalBreaks(final PageBreakPositionList allVerticalBreaks)
  {
    this.allVerticalBreaks = allVerticalBreaks;
  }

  public PageBreakPositionList getAllVerticalBreaks ()
  {
    return allVerticalBreaks;
  }

  public long computePageEnd()
  {
    final long pageOffset = getPageOffset();
    final PageBreakPositionList allVerticalBreaks = getAllVerticalBreaks();
    final long lastMasterBreak = allVerticalBreaks.getLastMasterBreak();
    if (pageOffset == lastMasterBreak)
    {
      return getHeight();
    }

    return allVerticalBreaks.findNextMajorBreakPosition(pageOffset + 1);
  }

  public String getPageName()
  {
    return pageName;
  }

  public void setPageName(final String pageName)
  {
    this.pageName = pageName;
  }
}

