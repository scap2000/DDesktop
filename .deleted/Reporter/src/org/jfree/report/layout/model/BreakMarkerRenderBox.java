package org.jfree.report.layout.model;

import org.jfree.report.layout.model.context.BoxDefinition;
import org.jfree.report.style.StyleSheet;

/**
 * A special box that simply marks the position of an automatic pagebreak. This is needed in the
 * process of the event ordering and rollback processing.
 *
 * @author Thomas Morgner
 */
public class BreakMarkerRenderBox extends BlockRenderBox
{
  public BreakMarkerRenderBox(final StyleSheet styleSheet,
                              final BoxDefinition boxDefinition,
                              final Object stateKey)
  {
    super(styleSheet, boxDefinition, stateKey);
  }

  public boolean isIgnorableForRendering()
  {
    return false;
  }


}
