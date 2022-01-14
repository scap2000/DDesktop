package org.jfree.report.layout.process.alignment;

import org.jfree.report.layout.model.PageGrid;
import org.jfree.report.layout.process.layoutrules.SequenceList;

/**
 * Creation-Date: 17.07.2007, 14:03:15
 *
 * @author Thomas Morgner
 */
public interface LastLineTextAlignmentProcessor
{
  public void initialize(SequenceList sequence,
                           long start,
                           long end,
                           PageGrid breaks);
  public void deinitialize();

  public void performLastLineAlignment();
}
