/*
 * Copyright 2006 Pentaho Corporation.  All rights reserved.
 * This software was developed by Pentaho Corporation and is provided under the terms
 * of the Mozilla Public License, Version 1.1, or any later version. You may not use
 * this file except in compliance with the license. If you need a copy of the license,
 * please go to http://www.mozilla.org/MPL/MPL-1.1.txt.
 *
 * Software distributed under the Mozilla Public License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or  implied. Please refer to
 * the license for the specific language governing your rights and limitations.
 *
 * Additional Contributor(s): Martin Schmid gridvision engineering GmbH
 */
package org.pentaho.reportdesigner.crm.report.tests;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.awt.*;


/**
 * User: Martin
 * Date: 23.03.2006
 * Time: 20:29:34
 */
@SuppressWarnings({"ALL"})
public class DesktopPropertyTest
{

    @NonNls
    @NotNull
    public static String[] sortedProperties = new String[]{"DnD.gestureMotionThreshold",
                                                           "awt.dynamicLayoutSupported",
                                                           "awt.file.showAttribCol",
                                                           "awt.file.showHiddenFiles",
                                                           "awt.mouse.numButtons",
                                                           "awt.multiClickInterval",
                                                           "awt.wheelMousePresent",
                                                           "win.3d.backgroundColor",
                                                           "win.3d.darkShadowColor",
                                                           "win.3d.highlightColor",
                                                           "win.3d.lightColor",
                                                           "win.3d.shadowColor",
                                                           "win.ansiFixed.font",
                                                           "win.ansiFixed.font.height",
                                                           "win.ansiVar.font",
                                                           "win.ansiVar.font.height",
                                                           "win.button.textColor",
                                                           "win.defaultGUI.font",
                                                           "win.defaultGUI.font.height",
                                                           "win.desktop.backgroundColor",
                                                           "win.deviceDefault.font",
                                                           "win.deviceDefault.font.height",
                                                           "win.drag.height",
                                                           "win.drag.width",
                                                           "win.frame.activeBorderColor",
                                                           "win.frame.activeCaptionColor",
                                                           "win.frame.activeCaptionGradientColor",
                                                           "win.frame.backgroundColor",
                                                           "win.frame.captionButtonHeight",
                                                           "win.frame.captionButtonWidth",
                                                           "win.frame.captionFont",
                                                           "win.frame.captionFont.height",
                                                           "win.frame.captionGradientsOn",
                                                           "win.frame.captionHeight",
                                                           "win.frame.captionTextColor",
                                                           "win.frame.color",
                                                           "win.frame.fullWindowDragsOn",
                                                           "win.frame.inactiveBorderColor",
                                                           "win.frame.inactiveCaptionColor",
                                                           "win.frame.inactiveCaptionGradientColor",
                                                           "win.frame.inactiveCaptionTextColor",
                                                           "win.frame.sizingBorderWidth",
                                                           "win.frame.smallCaptionButtonHeight",
                                                           "win.frame.smallCaptionButtonWidth",
                                                           "win.frame.smallCaptionFont",
                                                           "win.frame.smallCaptionFont.height",
                                                           "win.frame.smallCaptionHeight",
                                                           "win.frame.textColor",
                                                           "win.highContrast.on",
                                                           "win.icon.font",
                                                           "win.icon.font.height",
                                                           "win.icon.hspacing",
                                                           "win.icon.titleWrappingOn",
                                                           "win.icon.vspacing",
                                                           "win.item.highlightColor",
                                                           "win.item.highlightTextColor",
                                                           "win.item.hotTrackedColor",
                                                           "win.item.hotTrackingOn",
                                                           "win.mdi.backgroundColor",
                                                           "win.menu.backgroundColor",
                                                           "win.menu.buttonWidth",
                                                           "win.menu.font",
                                                           "win.menu.font.height",
                                                           "win.menu.height",
                                                           "win.menu.keyboardCuesOn",
                                                           "win.menu.textColor",
                                                           "win.menubar.backgroundColor",
                                                           "win.messagebox.font",
                                                           "win.messagebox.font.height",
                                                           "win.oemFixed.font",
                                                           "win.oemFixed.font.height",
                                                           "win.properties.version",
                                                           "win.scrollbar.backgroundColor",
                                                           "win.scrollbar.height",
                                                           "win.scrollbar.width",
                                                           "win.sound.asterisk",
                                                           "win.sound.close",
                                                           "win.sound.default",
                                                           "win.sound.exclamation",
                                                           "win.sound.exit",
                                                           "win.sound.hand",
                                                           "win.sound.maximize",
                                                           "win.sound.menuCommand",
                                                           "win.sound.menuPopup",
                                                           "win.sound.minimize",
                                                           "win.sound.open",
                                                           "win.sound.question",
                                                           "win.sound.restoreDown",
                                                           "win.sound.restoreUp",
                                                           "win.sound.start",
                                                           "win.status.font",
                                                           "win.status.font.height",
                                                           "win.system.font",
                                                           "win.system.font.height",
                                                           "win.systemFixed.font",
                                                           "win.systemFixed.font.height",
                                                           "win.text.fontSmoothingContrast",
                                                           "win.text.fontSmoothingOn",
                                                           "win.text.fontSmoothingType",
                                                           "win.text.grayedTextColor",
                                                           "win.tooltip.backgroundColor",
                                                           "win.tooltip.font",
                                                           "win.tooltip.font.height",
                                                           "win.tooltip.textColor",
                                                           "win.xpstyle.colorName",
                                                           "win.xpstyle.dllName",
                                                           "win.xpstyle.sizeName",
                                                           "win.xpstyle.themeActive"};


    public static void main(@NotNull String[] args)
    {
        System.out.println(Toolkit.getDefaultToolkit().getDesktopProperty("DnD.gestureMotionThreshold"));//NON-NLS
        //TreeSet ts = new TreeSet(Arrays.asList(properties));
        //for (Object o : ts)
        //{
        //    System.out.println("\""+o+"\", ");
        //}
        //
        //Object desktopProperty = Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.asterisk");
        //Object desktopProperty2 = Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.asterisk");
        //if (desktopProperty != null)
        //{
        //    System.out.println("desktopProperty = " + desktopProperty.getClass());
        //    System.out.println("desktopProperty = " + desktopProperty.getClass().getSuperclass());
        //    Method[] methods = desktopProperty.getClass().getMethods();
        //    for (int i = 0; i < methods.length; i++)
        //    {
        //        Method method = methods[i];
        //        System.out.println("method = " + method);
        //    }
        //    try
        //    {
        //
        //        Method method = desktopProperty.getClass().getMethod("run");
        //        method.setAccessible(true);
        //        method.invoke(desktopProperty);
        //    }
        //    catch (NoSuchMethodException e)
        //    {
        //        e.printStackTrace();
        //    }
        //    catch (IllegalAccessException e)
        //    {
        //        e.printStackTrace();
        //    }
        //    catch (InvocationTargetException e)
        //    {
        //        e.printStackTrace();
        //    }
        //}
    }
}
