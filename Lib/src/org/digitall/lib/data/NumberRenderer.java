package org.digitall.lib.data;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import java.util.Locale;

import javax.swing.SwingConstants;

public class NumberRenderer extends FormatRenderer
{
	/*
	 *  Use the specified number formatter and right align the text
	 */
	public NumberRenderer(NumberFormat formatter)
	{
		super(formatter);
		setHorizontalAlignment( SwingConstants.RIGHT );
	}

	/*
	 *  Use the default currency formatter for the default locale
	 */
	public static NumberRenderer getCurrencyRenderer()
	{
                return new NumberRenderer(new DecimalFormat("$ #,##0.00", new DecimalFormatSymbols(new Locale("es","AR"))));
		//return new NumberRenderer( NumberFormat.getCurrencyInstance() );
	}

	/*
	 *  Use the default integer formatter for the default locale
	 */
	public static NumberRenderer getIntegerRenderer()
	{
		return new NumberRenderer( NumberFormat.getIntegerInstance() );
	}

	/*
	 *  Use the default percent formatter for the default locale
	 */
	public static NumberRenderer getPercentRenderer()
	{
		return new NumberRenderer( NumberFormat.getPercentInstance() );
	}
}
