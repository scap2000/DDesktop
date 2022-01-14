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
package org.pentaho.reportdesigner.crm.report.model;

import org.jetbrains.annotations.NotNull;
import org.pentaho.reportdesigner.lib.client.util.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * User: Martin
 * Date: 20.01.2006
 * Time: 15:43:29
 */
public enum PageFormatPreset
{
    @NotNull CUSTOM(-1, -1),
    @NotNull A0(2384, 3370),
    @NotNull A1(1684, 2384),
    @NotNull A10(73, 105),
    @NotNull A2(1191, 1684),
    @NotNull A3(842, 1191),
    @NotNull A3_EXTRA(913, 1262),
    @NotNull A3_EXTRATRANSVERSE(913, 1262),
    //A3_ROTATED(1191, 842),
    //A3_TRANSVERSE(842, 1191),
    @NotNull A4(595, 842),
    @NotNull A4_EXTRA(667, 914),
    @NotNull A4_PLUS(595, 936),
    //A4_ROTATED(842, 595),
    //A4_SMALL(595, 842),
    //A4_TRANSVERSE(595, 842),
    @NotNull A5(420, 595),
    @NotNull A5_EXTRA(492, 668),
    //A5_ROTATED(595, 420),
    //A5_TRANSVERSE(420, 595),
    @NotNull A6(297, 420),
    //A6_ROTATED(420, 297),
    @NotNull A7(210, 297),
    @NotNull A8(148, 210),
    @NotNull A9(105, 148),
    @NotNull ANSIC(1224, 1584),
    @NotNull ANSID(1584, 2448),
    @NotNull ANSIE(2448, 3168),
    @NotNull ARCHA(648, 864),
    @NotNull ARCHB(864, 1296),
    @NotNull ARCHC(1296, 1728),
    @NotNull ARCHD(1728, 2592),
    @NotNull ARCHE(2592, 3456),
    @NotNull B0(2920, 4127),
    @NotNull B1(2064, 2920),
    @NotNull B10(91, 127),
    @NotNull B2(1460, 2064),
    @NotNull B3(1032, 1460),
    @NotNull B4(729, 1032),
    //B4_ROTATED(1032, 729),
    @NotNull B5(516, 729),
    //B5_ROTATED(729, 516),
    //B5_TRANSVERSE(516, 729),
    @NotNull B6(363, 516),
    //B6_ROTATED(516, 363),
    @NotNull B7(258, 363),
    @NotNull B8(181, 258),
    @NotNull B9(127, 181),
    @NotNull C4(649, 918),
    @NotNull C5(459, 649),
    @NotNull C6(323, 459),
    //COMM10(297, 684),
    //DL(312, 624),
    @NotNull DOUBLEPOSTCARD(567, 419),
    //DOUBLEPOSTCARD_ROTATED(419, 567),
    @NotNull ENV10(297, 684),
    @NotNull ENV11(324, 747),
    @NotNull ENV12(342, 792),
    @NotNull ENV14(360, 828),
    @NotNull ENV9(279, 639),
    @NotNull ENVC0(2599, 3676),
    @NotNull ENVC1(1837, 2599),
    @NotNull ENVC2(1298, 1837),
    @NotNull ENVC3(918, 1296),
    //ENVC4(649, 918),
    //ENVC5(459, 649),
    //ENVC6(323, 459),
    @NotNull ENVC65(324, 648),
    @NotNull ENVC7(230, 323),
    @NotNull ENVCHOU3(340, 666),
    //ENVCHOU3_ROTATED(666, 340),
    @NotNull ENVCHOU4(255, 581),
    //ENVCHOU4_ROTATED(581, 255),
    //ENVDL(312, 624),
    @NotNull ENVELOPE(312, 652),
    @NotNull ENVINVITE(624, 624),
    @NotNull ENVISOB4(708, 1001),
    //ENVISOB5(499, 709),
    @NotNull ENVISOB6(499, 354),
    //ENVITALIAN(312, 652),
    @NotNull ENVKAKU2(680, 941),
    //ENVKAKU2_ROTATED(941, 680),
    @NotNull ENVKAKU3(612, 785),
    //ENVKAKU3_ROTATED(785, 612),
    //ENVMONARCH(279, 540),
    @NotNull ENVPERSONAL(261, 468),
    @NotNull ENVPRC1(289, 468),
    @NotNull ENVPRC10(918, 1298),
    //ENVPRC10_ROTATED(1298, 918),
    //ENVPRC1_ROTATED(468, 289),
    @NotNull ENVPRC2(289, 499),
    //ENVPRC2_ROTATED(499, 289),
    //ENVPRC3(354, 499),
    //ENVPRC3_ROTATED(499, 354),
    @NotNull ENVPRC4(312, 590),
    //ENVPRC4_ROTATED(590, 312),
    @NotNull ENVPRC5(312, 624),
    //ENVPRC5_ROTATED(624, 312),
    @NotNull ENVPRC6(340, 652),
    //ENVPRC6_ROTATED(652, 340),
    @NotNull ENVPRC7(454, 652),
    //ENVPRC7_ROTATED(652, 454),
    @NotNull ENVPRC8(340, 876),
    //ENVPRC8_ROTATED(876, 340),
    //ENVPRC9(649, 918),
    //ENVPRC9_ROTATED(918, 649),
    @NotNull ENVYOU4(298, 666),
    //ENVYOU4_ROTATED(666, 298),
    @NotNull EXECUTIVE(522, 756),
    @NotNull FANFOLDGERMAN(612, 864),
    @NotNull FANFOLDGERMANLEGAL(612, 936),
    @NotNull FANFOLDUS(1071, 792),
    @NotNull FOLIO(595, 935),
    @NotNull ISOB0(2835, 4008),
    @NotNull ISOB1(2004, 2835),
    @NotNull ISOB10(88, 125),
    @NotNull ISOB2(1417, 2004),
    @NotNull ISOB3(1001, 1417),
    @NotNull ISOB4(709, 1001),
    @NotNull ISOB5(499, 709),
    @NotNull ISOB5_EXTRA(570, 782),
    @NotNull ISOB6(354, 499),
    @NotNull ISOB7(249, 354),
    @NotNull ISOB8(176, 249),
    @NotNull ISOB9(125, 176),
    @NotNull LEDGER(1224, 792),
    @NotNull LEGAL(612, 1008),
    @NotNull LEGAL_EXTRA(684, 1080),
    @NotNull LETTER(612, 792),
    @NotNull LETTER_EXTRA(684, 864),
    //LETTER_EXTRATRANSVERSE(684, 864),
    @NotNull LETTER_PLUS(612, 914),
    //LETTER_ROTATED(792, 612),
    //LETTER_SMALL(612, 792),
    //LETTER_TRANSVERSE(612, 792),
    @NotNull MONARCH(279, 540),
    //NOTE(612, 792),
    @NotNull PAPER10X11(720, 792),
    @NotNull PAPER10X13(720, 936),
    @NotNull PAPER10X14(720, 1008),
    @NotNull PAPER11X17(792, 1224),
    @NotNull PAPER12X11(864, 792),
    @NotNull PAPER15X11(1080, 792),
    @NotNull PAPER7X9(504, 648),
    @NotNull PAPER8X10(576, 720),
    @NotNull PAPER9X11(648, 792),
    //PAPER9X12(648, 864),
    @NotNull POSTCARD(284, 419),
    //POSTCARD_ROTATED(419, 284),
    @NotNull PRC16K(414, 610),
    //PRC16K_ROTATED(610, 414),
    @NotNull PRC32K(275, 428),
    //PRC32K_BIG(275, 428),
    //PRC32K_BIGROTATED(428, 275),
    //PRC32K_ROTATED(428, 275),
    @NotNull QUARTO(610, 780),
    @NotNull STATEMENT(396, 612),
    @NotNull SUPERA(643, 1009),
    @NotNull SUPERB(864, 1380),
    //TABLOID(792, 1224),
    //TABLOIDEXTRA(864, 1296),
    ;

    private double width;
    private double height;


    public static void main(@NotNull String[] args)
    {
        ArrayList<PageFormatPreset> pageFormatPresets = new ArrayList<PageFormatPreset>(Arrays.asList(PageFormatPreset.values()));
        Collections.sort(pageFormatPresets, new Comparator<PageFormatPreset>()
        {
            public int compare(@NotNull PageFormatPreset o1, @NotNull PageFormatPreset o2)
            {
                if (o1.getWidth() > o2.getWidth())
                {
                    return 1;
                }
                else if (o1.getWidth() < o2.getWidth())
                {
                    return -1;
                }
                else
                {
                    if (o1.getHeight() > o2.getHeight())
                    {
                        return 1;
                    }
                    else if (o1.getHeight() < o2.getHeight())
                    {
                        return -1;
                    }
                }
                return 0;
            }
        });

        for (PageFormatPreset pageFormatPreset : pageFormatPresets)
        {
            //noinspection UseOfSystemOutOrSystemErr
            System.out.println("pageFormatPreset = " + pageFormatPreset + " " + pageFormatPreset.getWidth() + ", " + pageFormatPreset.getHeight());//NON-NLS
        }
    }


    PageFormatPreset(double width, double height)
    {
        this.width = width;
        this.height = height;
    }


    public double getWidth()
    {
        return width;
    }


    public double getHeight()
    {
        return height;
    }


    @NotNull
    public static PageFormatPreset getPreset(@NotNull PageDefinition pageDefinition)
    {
        if (pageDefinition.getPageOrientation() == PageOrientation.PORTRAIT)
        {
            double width = pageDefinition.getInnerPageSize().getWidth() + pageDefinition.getLeftBorder() + pageDefinition.getRightBorder();
            double height = pageDefinition.getInnerPageSize().getHeight() + pageDefinition.getTopBorder() + pageDefinition.getBottomBorder();

            for (PageFormatPreset pageFormatPreset : PageFormatPreset.values())
            {
                if (MathUtils.approxEquals(pageFormatPreset.getWidth(), width) &&
                    MathUtils.approxEquals(pageFormatPreset.getHeight(), height))
                {
                    return pageFormatPreset;
                }
            }

            return CUSTOM;
        }
        else
        {
            double width = pageDefinition.getInnerPageSize().getHeight() + pageDefinition.getLeftBorder() + pageDefinition.getRightBorder();
            double height = pageDefinition.getInnerPageSize().getWidth() + pageDefinition.getTopBorder() + pageDefinition.getBottomBorder();

            for (PageFormatPreset pageFormatPreset : PageFormatPreset.values())
            {
                if (MathUtils.approxEquals(pageFormatPreset.getWidth(), width) &&
                    MathUtils.approxEquals(pageFormatPreset.getHeight(), height))
                {
                    return pageFormatPreset;
                }
            }

            return CUSTOM;
        }
    }


    @NotNull
    public static PageFormatPreset getPreset(double width, double height)
    {
        for (PageFormatPreset pageFormatPreset : PageFormatPreset.values())
        {
            if (MathUtils.approxEquals(pageFormatPreset.getWidth(), width) &&
                MathUtils.approxEquals(pageFormatPreset.getHeight(), height))
            {
                return pageFormatPreset;
            }
        }

        for (PageFormatPreset pageFormatPreset : PageFormatPreset.values())
        {
            if (MathUtils.approxEquals(pageFormatPreset.getWidth(), height) &&
                MathUtils.approxEquals(pageFormatPreset.getHeight(), width))
            {
                return pageFormatPreset;
            }
        }

        return CUSTOM;
    }
}
