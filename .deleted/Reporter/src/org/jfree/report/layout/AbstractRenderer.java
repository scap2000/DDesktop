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
 * LayoutSystem.java
 * ------------
 * (C) Copyright 2001-2007, by Object Refinery Ltd, Pentaho Corporation and Contributors.
 */

package org.jfree.report.layout;

import org.jfree.report.Band;
import org.jfree.report.InvalidReportStateException;
import org.jfree.report.PageDefinition;
import org.jfree.report.function.ExpressionRuntime;
import org.jfree.report.layout.model.BlockRenderBox;
import org.jfree.report.layout.model.BreakMarkerRenderBox;
import org.jfree.report.layout.model.LogicalPageBox;
import org.jfree.report.layout.model.RenderBox;
import org.jfree.report.layout.model.RenderLength;
import org.jfree.report.layout.model.RenderNode;
import org.jfree.report.layout.model.WatermarkAreaBox;
import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.layout.output.ContentProcessingException;
import org.jfree.report.layout.output.LayoutPagebreakHandler;
import org.jfree.report.layout.output.OutputProcessor;
import org.jfree.report.layout.output.OutputProcessorFeature;
import org.jfree.report.layout.output.OutputProcessorMetaData;
import org.jfree.report.layout.process.ApplyAutoCommitStep;
import org.jfree.report.layout.process.ApplyCachedValuesStep;
import org.jfree.report.layout.process.ApplyCommitStep;
import org.jfree.report.layout.process.CanvasMajorAxisLayoutStep;
import org.jfree.report.layout.process.CommitStep;
import org.jfree.report.layout.process.ComputeStaticPropertiesProcessStep;
import org.jfree.report.layout.process.InfiniteMajorAxisLayoutStep;
import org.jfree.report.layout.process.InfiniteMinorAxisLayoutStep;
import org.jfree.report.layout.process.ParagraphLineBreakStep;
import org.jfree.report.layout.process.RevalidateAllAxisLayoutStep;
import org.jfree.report.layout.process.RollbackStep;
import org.jfree.report.layout.process.ValidateModelStep;
import org.jfree.report.layout.style.ManualBreakIndicatorStyleSheet;
import org.jfree.report.layout.style.SectionKeepTogetherStyleSheet;
import org.jfree.report.layout.style.SimpleStyleSheet;
import org.jfree.report.style.BandDefaultStyleSheet;
import org.jfree.report.style.StyleKey;
import org.jfree.report.style.StyleSheet;
import org.jfree.report.util.InstanceID;
import org.jfree.util.FastStack;

/**
 * The LayoutSystem is a simplified version of the LibLayout-rendering system.
 *
 * @author Thomas Morgner
 */
public abstract class AbstractRenderer implements Renderer
{
  private static final boolean ENABLE_GROUP_SECTION = true;
  private static final int INITIAL_COMMON_SIZE = 50;
  private static final double COMMON_GROWTH = 1.2;

  private static class Section
  {
    private int type;
    private RenderBox sectionBox;

    protected Section(final int type, final RenderBox sectionBox)
    {
      this.type = type;
      this.sectionBox = sectionBox;
    }

    public int getType()
    {
      return type;
    }

    public RenderBox getSectionBox()
    {
      return sectionBox;
    }
  }

  private static class GroupSection
  {
    private RenderBox addBox;
    private RenderBox groupBox;
    private int childCount;
    private int nextBoxStart;
    private StyleSheet styleSheet;

    protected GroupSection(final RenderBox groupBox,
                           final StyleSheet styleSheet)
    {
      if (groupBox == null)
      {
        throw new NullPointerException();
      }
      this.styleSheet = styleSheet;
      this.groupBox = groupBox;
      this.childCount = 0;
      this.nextBoxStart = INITIAL_COMMON_SIZE;
      this.addBox = groupBox;
    }

    protected GroupSection(final RenderBox groupBox,
                           final RenderBox addBox,
                           final int childCount,
                           final int nextBoxStart,
                           final StyleSheet styleSheet)
    {
      if (groupBox == null)
      {
        throw new NullPointerException();
      }
      this.groupBox = groupBox;
      this.addBox = addBox;
      this.childCount = childCount;
      this.nextBoxStart = nextBoxStart;
      this.styleSheet = styleSheet;
    }


    public RenderBox getAddBox()
    {
      return addBox;
    }

    public RenderBox getGroupBox()
    {
      return groupBox;
    }

    public void addedSection(final RenderNode node, final Object stateKey)
    {
      childCount += 1;
      if (childCount == nextBoxStart)
      {
        if (addBox != groupBox)
        {
          addBox.close();
        }
        final BlockRenderBox commonBox = new BlockRenderBox(styleSheet, BoxDefinition.EMPTY, stateKey);
        commonBox.setName("Common-Section");
        groupBox.addChild(commonBox);
        addBox = commonBox;

        nextBoxStart += nextBoxStart * COMMON_GROWTH;
      }
      addBox.addChild(node);
    }

    public void close()
    {
      if (addBox != groupBox)
      {
        addBox.close();
      }
      groupBox.close();
    }

    public int getChildCount()
    {
      return childCount;
    }


    public int getNextBoxStart()
    {
      return nextBoxStart;
    }

    public StyleSheet getStyleSheet()
    {
      return styleSheet;
    }
  }


  private LogicalPageBox pageBox;
  private LayoutBuilder layoutBuilder;

  private ValidateModelStep validateModelStep;
  private ComputeStaticPropertiesProcessStep staticPropertiesStep;
  private ParagraphLineBreakStep paragraphLineBreakStep;
  private InfiniteMinorAxisLayoutStep minorAxisLayoutStep;
  private InfiniteMajorAxisLayoutStep majorAxisLayoutStep;
  private CanvasMajorAxisLayoutStep canvasMajorAxisLayoutStep;
  private RevalidateAllAxisLayoutStep revalidateAllAxisLayoutStep;
  private CommitStep commitStep;
  private ApplyCommitStep applyCommitStep;
  private RollbackStep rollbackStep;
  private ApplyAutoCommitStep applyAutoCommitStep;

  private OutputProcessorMetaData metaData;
  private OutputProcessor outputProcessor;
  private Section section;
  private int pagebreaks;
  private boolean dirty;
  private Object lastStateKey;
  private ApplyCachedValuesStep applyCachedValuesStep;
  private SimpleStyleSheet manualBreakBoxStyle;
  private SimpleStyleSheet bandWithoutKeepTogetherStyle;
  private SimpleStyleSheet bandWithKeepTogetherStyle;
  private boolean readOnly;
  private FastStack groupStack;
  private Object stateKey;
  private boolean paranoidChecks;
  
  protected AbstractRenderer(final OutputProcessor outputProcessor)
  {
    this.outputProcessor = outputProcessor;
    this.metaData = outputProcessor.getMetaData();
    this.layoutBuilder = new LayoutBuilder(metaData);
    this.paranoidChecks = "true".equals
        (metaData.getConfiguration().getConfigProperty("org.jfree.report.layout.ParanoidChecks"));
    this.validateModelStep = new ValidateModelStep();
    this.staticPropertiesStep = new ComputeStaticPropertiesProcessStep();
    this.paragraphLineBreakStep = new ParagraphLineBreakStep();
    this.minorAxisLayoutStep = new InfiniteMinorAxisLayoutStep();
    this.majorAxisLayoutStep = new InfiniteMajorAxisLayoutStep();
    this.canvasMajorAxisLayoutStep = new CanvasMajorAxisLayoutStep();
    this.revalidateAllAxisLayoutStep = new RevalidateAllAxisLayoutStep();
    this.applyCachedValuesStep = new ApplyCachedValuesStep();
    this.commitStep = new CommitStep();
    this.applyAutoCommitStep = new ApplyAutoCommitStep();
    this.applyCommitStep = new ApplyCommitStep();
    this.rollbackStep = new RollbackStep();

    this.groupStack = new FastStack();

    bandWithKeepTogetherStyle = new SimpleStyleSheet
        (new SectionKeepTogetherStyleSheet(true), StyleKey.getDefinedStyleKeys());
    bandWithoutKeepTogetherStyle = new SimpleStyleSheet
        (new SectionKeepTogetherStyleSheet(false), StyleKey.getDefinedStyleKeys());

  }


  public Object getStateKey()
  {
    return stateKey;
  }

  public void setStateKey(final Object stateKey)
  {
    this.stateKey = stateKey;
  }

  public OutputProcessor getOutputProcessor()
  {
    return outputProcessor;
  }

  public void startReport(final PageDefinition pageDefinition)
  {
    if (readOnly)
    {
      throw new IllegalStateException();
    }
    this.pageBox = new LogicalPageBox(pageDefinition);
    this.groupStack.push(new GroupSection(pageBox.getContentArea(), bandWithoutKeepTogetherStyle));
    this.dirty = true;
  }

  public void startGroup(final boolean keepTogether)
  {
    if (readOnly)
    {
      throw new IllegalStateException();
    }
    if (ENABLE_GROUP_SECTION)
    {
      final SimpleStyleSheet style;
      if (keepTogether)
      {
        style = bandWithKeepTogetherStyle;
      }
      else
      {
        style = bandWithoutKeepTogetherStyle;
      }

      final BlockRenderBox groupBox = new BlockRenderBox(style, BoxDefinition.EMPTY, null);
      groupBox.setName("Group-Section-" + groupStack.size());
      addBox(groupBox);
      this.groupStack.push(new GroupSection(groupBox, bandWithoutKeepTogetherStyle));
    }
  }

  private void addBox(final RenderNode node, final Object stateKey)
  {
    final GroupSection groupSection = (GroupSection) groupStack.peek();
    groupSection.addedSection(node, stateKey);
  }

  private void addBox(final RenderNode node)
  {
    final GroupSection groupSection = (GroupSection) groupStack.peek();
    groupSection.addedSection(node, stateKey);
  }

  public void startSection(final int type)
  {
    if (readOnly)
    {
      throw new IllegalStateException();
    }
    final BlockRenderBox sectionBox;

    if (type == Renderer.TYPE_WATERMARK)
    {
      final BoxDefinition boxDefinition = new BoxDefinition();
      boxDefinition.setPreferredHeight(new RenderLength(this.pageBox.getPageHeight(), false));
      sectionBox = new BlockRenderBox(bandWithoutKeepTogetherStyle, boxDefinition, null);
    }
    else
    {
      sectionBox = new BlockRenderBox(bandWithoutKeepTogetherStyle, BoxDefinition.EMPTY, null);
    }

    sectionBox.setName("Section-" + type);
    this.section = new Section(type, sectionBox);
  }

  public void endSection()
  {
    if (readOnly)
    {
      throw new IllegalStateException();
    }
    final Section section = this.section;
    this.section = null;
    final RenderBox sectionBox = section.getSectionBox();
    sectionBox.close();

    if (sectionBox.getFirstChild() == null)
    {
      // the whole section is empty; therefore we can ignore it.
      return;
    }

    switch (section.getType())
    {
      case TYPE_NORMALFLOW:
      {
        addBox(sectionBox);
        break;
      }
      case TYPE_FOOTER:
      {
        final BlockRenderBox footerArea = pageBox.getFooterArea();
        footerArea.clear();
        footerArea.addChild(sectionBox);
        break;
      }
      case TYPE_HEADER:
      {
        final BlockRenderBox headerArea = pageBox.getHeaderArea();
        headerArea.clear();
        headerArea.addChild(sectionBox);
        break;
      }
      case TYPE_WATERMARK:
      {
        // ignore for now.
        final WatermarkAreaBox watermarkArea = pageBox.getWatermarkArea();
        watermarkArea.clear();
        watermarkArea.addChild(sectionBox);
        break;
      }
      default:
        throw new IllegalStateException("Type " + section.getType() + " not recognized");
    }
    this.dirty = true;
  }

  public void endGroup()
  {
    if (readOnly)
    {
      throw new IllegalStateException();
    }
    if (ENABLE_GROUP_SECTION)
    {
      final GroupSection groupSection = (GroupSection) groupStack.pop();
      groupSection.close();
    }
  }

  protected LogicalPageBox getPageBox()
  {
    return pageBox;
  }

  public void endReport()
  {
    if (readOnly)
    {
      throw new IllegalStateException();
    }
    final GroupSection groupSection = (GroupSection) groupStack.pop();
    groupSection.close();

    pageBox.close();
    this.dirty = true;
  }

  public void add(final Band band, final ExpressionRuntime runtime, final Object stateKey)
  {
    if (readOnly)
    {
      throw new IllegalStateException();
    }
    final RenderBox sectionBox = section.getSectionBox();
    layoutBuilder.add(sectionBox, band, runtime, stateKey);
  }

  public boolean validatePages()
      throws ContentProcessingException
  {
    if (readOnly)
    {
      throw new IllegalStateException();
    }
    // Pagination time without dirty-flag: 875067
    if (pageBox == null)
    {
      // StartReport has not been called yet ..
      return false;
    }

    if (dirty)
    {
      setLastStateKey(null);
      setPagebreaks(0);
      if (validateModelStep.isLayoutable(pageBox) == false)
      {
        return false;
      }

      // These structural processors will skip old nodes. These beasts cannot be cached otherwise.
      staticPropertiesStep.compute(pageBox, metaData);
      paragraphLineBreakStep.compute(pageBox);

      minorAxisLayoutStep.compute(pageBox);
      majorAxisLayoutStep.compute(pageBox);
      canvasMajorAxisLayoutStep.compute(pageBox);
      revalidateAllAxisLayoutStep.compute(pageBox, metaData);

      applyCachedValuesStep.compute(pageBox);
      return isPageFinished();
    }
    return false;
  }

  protected void clearDirty()
  {
    dirty = false;
  }

  protected abstract boolean isPageFinished();

  public void processIncrementalUpdate(final boolean performOutput) throws ContentProcessingException
  {
    dirty = false;
  }

  public boolean processPage(final LayoutPagebreakHandler handler,
                             final Object commitMarker,
                             final boolean performOutput) throws ContentProcessingException
  {
    if (readOnly)
    {
      throw new IllegalStateException();
    }
    // Pagination time without dirty-flag: 875067
    if (pageBox == null)
    {
      // StartReport has not been called yet ..
//      Log.debug ("PageBox null");
      return false;
    }

    if (dirty == false)
    {
//      Log.debug ("Not dirty");
      return false;
    }

    setLastStateKey(null);
    setPagebreaks(0);
    if (validateModelStep.isLayoutable(pageBox) == false)
    {
//      Log.debug ("Not layoutable");
      return false;
    }

    // processes the current page
    boolean repeat = true;
    while (repeat)
    {
      if (handler != null)
      {
        handler.pageFinished();
      }
      
      if (outputProcessor.getMetaData().isFeatureSupported(OutputProcessorFeature.PAGEBREAKS))
      {
        createRollbackInformation();
        applyRollbackInformation();
        performParanoidModelCheck();
      }

      staticPropertiesStep.compute(pageBox, metaData);
      paragraphLineBreakStep.compute(pageBox);

      minorAxisLayoutStep.compute(pageBox);
      majorAxisLayoutStep.compute(pageBox);
      canvasMajorAxisLayoutStep.compute(pageBox);
      revalidateAllAxisLayoutStep.compute(pageBox, metaData);
      
      applyCachedValuesStep.compute(pageBox);

      repeat = performPagination(handler, performOutput);
    }
    dirty = false;
    return (pagebreaks > 0);
  }

  protected abstract boolean performPagination(LayoutPagebreakHandler handler,
                                               final boolean performOutput)
      throws ContentProcessingException;

  protected void debugPrint(final LogicalPageBox pageBox)
  {
  }

  public Object getLastStateKey()
  {
    return lastStateKey;
  }

  public void setLastStateKey(final Object lastStateKey)
  {
    this.lastStateKey = lastStateKey;
  }

  protected void setPagebreaks(final int pagebreaks)
  {
    this.pagebreaks = pagebreaks;
  }

  public int getPagebreaks()
  {
    return pagebreaks;
  }

  public boolean isOpen()
  {
    if (pageBox == null)
    {
      return false;
    }
    return pageBox.isOpen();
  }

  public boolean isValid()
  {
    return readOnly == false;
  }

  public Renderer deriveForStorage()
  {
    try
    {
      final AbstractRenderer renderer = (AbstractRenderer) clone();
      renderer.readOnly = false;
      if (pageBox != null)
      {
        renderer.pageBox = (LogicalPageBox) pageBox.derive(true);
        if (section != null)
        {
          final RenderNode nodeById = renderer.pageBox.findNodeById(section.getSectionBox().getInstanceId());
          renderer.section = new Section(section.getType(), (RenderBox) nodeById);
        }
      }

      final int stackSize = groupStack.size();
      final Object[] tempList = new Object[stackSize];


      renderer.groupStack = (FastStack) groupStack.clone();
      for (int i = 0; i < tempList.length; i++)
      {
        tempList[i] = renderer.groupStack.pop();
      }

      // the stack is empty now ..
      // lets fill it again ..
      for (int i = tempList.length - 1; i >= 0; i--)
      {
        final GroupSection section = (GroupSection) tempList[i];

        final RenderBox groupBox = section.getGroupBox();
        final InstanceID groupBoxInstanceId = groupBox.getInstanceId();
        final RenderBox groupBoxClone = (RenderBox) renderer.pageBox.findNodeById(groupBoxInstanceId);
        if (groupBoxClone == null)
        {
          throw new IllegalStateException("The pagebox did no longer contain the stored node.");
        }
        if (groupBoxClone == groupBox)
        {
          throw new IllegalStateException("Thought you wanted a groupBoxClone");
        }

        final RenderBox addBox = section.getAddBox();
        final RenderBox addBoxClone;
        if (addBox == groupBox)
        {
          addBoxClone = groupBoxClone;
        }
        else
        {
          final InstanceID addBoxInstanceId = addBox.getInstanceId();
          addBoxClone = (RenderBox) renderer.pageBox.findNodeById(addBoxInstanceId);
          if (addBoxClone == null)
          {
            throw new IllegalStateException("The pagebox did no longer contain the stored node.");
          }
          if (addBoxClone == addBox)
          {
            throw new IllegalStateException("Thought you wanted a groupBoxClone");
          }
        }
        renderer.groupStack.push(new GroupSection(groupBoxClone, addBoxClone,
            section.getChildCount(), section.getNextBoxStart(), section.getStyleSheet()));
      }
      return renderer;
    }
    catch (CloneNotSupportedException cne)
    {
      throw new InvalidReportStateException("Failed to derive Renderer", cne);
    }
  }

  public Renderer deriveForPagebreak()
  {
    try
    {
      final AbstractRenderer renderer = (AbstractRenderer) clone();
      renderer.readOnly = true;
      if (pageBox != null)
      {
        if (section != null)
        {
          renderer.section = new Section(section.getType(), section.getSectionBox());
        }
      }

      final int stackSize = groupStack.size();
      final Object[] tempList = new Object[stackSize];
      renderer.groupStack = (FastStack) groupStack.clone();
      for (int i = 0; i < tempList.length; i++)
      {
        tempList[i] = renderer.groupStack.pop();
      }

      // the stack is empty now ..
      // lets fill it again ..
      for (int i = tempList.length - 1; i >= 0; i--)
      {
        final GroupSection section = (GroupSection) tempList[i];

        final RenderBox groupBox = section.getGroupBox();
        final RenderBox addBox = section.getAddBox();

//        validate(addBox, groupBox);
        renderer.groupStack.push(new GroupSection(groupBox, addBox,
            section.getChildCount(), section.getNextBoxStart(), section.getStyleSheet()));
      }
      return renderer;
    }
    catch (CloneNotSupportedException cne)
    {
      throw new InvalidReportStateException("Failed to derive Renderer", cne);
    }
  }

  public void performParanoidModelCheck()
  {
    if (paranoidChecks)
    {
      final int stackSize = groupStack.size();

      // the stack is empty now ..
      // lets fill it again ..
      for (int i = 0; i < stackSize; i++)
      {
        final GroupSection section = (GroupSection) groupStack.get(i);

        final RenderBox groupBox = section.getGroupBox();
        final RenderBox addBox = section.getAddBox();

        // step 1: Check whether addbox is a child of groupbox
        RenderBox c = addBox;
        while (c != groupBox)
        {
          c = c.getParent();
          if (c == null)
          {
            throw new NullPointerException("Failed to locate parent");
          }
        }

        c = addBox;
        while (c != null)
        {
          if (c.isOpen() == false)
          {
            throw new NullPointerException("Add-Box is not open: " + c.isMarkedOpen() + ' ' + c.isMarkedSeen());
          }
          c = c.getParent();
        }
      }
    }
  }

  public Object clone() throws CloneNotSupportedException
  {
    return super.clone();
  }

  public void addPagebreak(final Object stateKey)
  {
    if (readOnly)
    {
      throw new IllegalStateException();
    }
    if (this.manualBreakBoxStyle == null)
    {
      final ManualBreakIndicatorStyleSheet mbis =
          new ManualBreakIndicatorStyleSheet(BandDefaultStyleSheet.getBandDefaultStyle());
      this.manualBreakBoxStyle = new SimpleStyleSheet(mbis, StyleKey.getDefinedStyleKeys());
    }

    final RenderBox sectionBox = new BreakMarkerRenderBox(manualBreakBoxStyle, BoxDefinition.EMPTY, stateKey);
    sectionBox.setName("pagebreak");
    sectionBox.close();
    addBox(sectionBox, stateKey);
  }

  public boolean clearPendingPageStart(final LayoutPagebreakHandler layoutPagebreakHandler)
  {
    // intentionally left empty.
    return false;
  }


  public boolean isPageStartPending()
  {
    return false;
  }

  public boolean isDirty()
  {
    return dirty;
  }

  public void createRollbackInformation()
  {
    if (pageBox != null)
    {
      commitStep.compute(pageBox);
      validateAfterCommit();
    }
  }


  public void applyRollbackInformation()
  {
    if (pageBox != null)
    {
      applyCommitStep.compute(pageBox);
    }
  }

  public void validateAfterCommit()
  {
    if (paranoidChecks)
    {
      final int stackSize = groupStack.size();
      for (int i = 0; i < stackSize; i++)
      {
        final GroupSection section = (GroupSection) groupStack.get(i);

        final RenderBox groupBox = section.getGroupBox();
        final RenderBox addBox = section.getAddBox();

        if (groupBox.isMarkedSeen() == false)
        {
          throw new IllegalStateException();
        }
        if (addBox.isMarkedOpen() == false)
        {
          throw new IllegalStateException();
        }
      }
    }
  }

  public void rollback()
  {
    readOnly = false;
    if (pageBox != null)
    {
      rollbackStep.compute(pageBox);
    }
  }


  public void applyAutoCommit()
  {
    if (pageBox != null)
    {
      applyAutoCommitStep.compute(pageBox);
    }
  }
}
