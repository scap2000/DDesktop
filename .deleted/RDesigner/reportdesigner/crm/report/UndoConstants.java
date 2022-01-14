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
package org.pentaho.reportdesigner.crm.report;

import org.jetbrains.annotations.NotNull;

/**
 * User: Martin
 * Date: 22.04.2006
 * Time: 13:30:46
 */
public class UndoConstants
{

    @NotNull
    public static final String CLEAR_SELECTION = "clearSelection";
    @NotNull
    public static final String SET_SELECTION = "setSelection";
    @NotNull
    public static final String ADD_SELECTED_ELEMENT = "addSelectedElement";
    @NotNull
    public static final String CREATE = "create";
    @NotNull
    public static final String MOUSE_RELEASED = "mouse released";
    @NotNull
    public static final String ADD_GROUP = "add group";
    @NotNull
    public static final String ADJUST_BAND_SIZE_COMMAND = "AdjustBandSizeCommand";
    @NotNull
    public static final String CUT = "cut";
    @NotNull
    public static final String DELETE = "delete";
    @NotNull
    public static final String LAYER_DOWN = "layer down";
    @NotNull
    public static final String LAYER_UP = "layer up";
    @NotNull
    public static final String MOVE_DOWN = "move down";
    @NotNull
    public static final String MOVE_LEFT = "move left";
    @NotNull
    public static final String MOVE_RIGHT = "move right";
    @NotNull
    public static final String MOVE_UP = "move up";
    @NotNull
    public static final String PASTE = "paste";
    @NotNull
    public static final String SET_ACTIVE = "setActive";
    @NotNull
    public static final String SET_POSITION = "setPosition";
    @NotNull
    public static final String REMOVE_GUIDE_LINE = "removeGuideLine";
    @NotNull
    public static final String ADD_GUIDE_LINE = "addGuideLine";
    @NotNull
    public static final String ADD_CHILD = "addChild";
    @NotNull
    public static final String REMOVE_CHILD = "removeChild";
    @NotNull
    public static final String MOVE_CHILD_DOWN = "moveChildDown";
    @NotNull
    public static final String MOVE_CHILD_UP = "moveChildUp";
    @NotNull
    public static final String ADD_FUNCTION = "add function";
    @NotNull
    public static final String PROPERTY_SETVALUES = "property.setvalues";
    @NotNull
    public static final String CONNECTION_SETTINGS = "connectionSettings";
    @NotNull
    public static final String MORPH_INTO_TEXTFIELD = "morphIntoTextField";
    @NotNull
    public static final String MERGE_REPORTS = "mergeReports";
    @NotNull
    public static final String ALIGN_LEFT = "alignLeft";
    @NotNull
    public static final String ALIGN_RIGHT = "alignRight";
    @NotNull
    public static final String ALIGN_CENTER = "alignCenter";
    @NotNull
    public static final String ALIGN_TOP = "alignTop";
    @NotNull
    public static final String ALIGN_MIDDLE = "alignMiddle";
    @NotNull
    public static final String ALIGN_BOTTOM = "alignBottom";
    @NotNull
    public static final String DISTRIBUTE_CENTER = "distributeCenter";
    @NotNull
    public static final String DISTRIBUTE_LEFT = "distributeLeft";
    @NotNull
    public static final String DISTRIBUTE_RIGHT = "distributeRight";
    @NotNull
    public static final String DISTRIBUTE_GAPS_HORIZONTAL = "distributeGapsHorizontal";
    @NotNull
    public static final String DISTRIBUTE_TOP = "distributeTop";
    @NotNull
    public static final String DISTRIBUTE_BOTTOM = "distributeBottom";
    @NotNull
    public static final String DISTRIBUTE_MIDDLE = "distributeMiddle";
    @NotNull
    public static final String DISTRIBUTE_GAPS_VERTICAL = "distributeGapsVertical";
    @NotNull
    public static final String HIDE_ELEMENT = "hideElement";


    private UndoConstants()
    {
    }
}
