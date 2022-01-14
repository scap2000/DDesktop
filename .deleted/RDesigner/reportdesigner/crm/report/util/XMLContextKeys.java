package org.pentaho.reportdesigner.crm.report.util;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.io.File;

/**
 * User: Martin
 * Date: 28.09.2007
 * Time: 13:07:23
 */
public class XMLContextKeys<T>
{
    @NotNull
    public static final XMLContextKeys<File> CONTEXT_PATH = new XMLContextKeys<File>("ContextPath");


    @NotNull
    private String key;


    private XMLContextKeys(@NotNull @NonNls String key)
    {
        this.key = key;
    }


    public void putObject(@NotNull XMLContext xmlWriter, @Nullable T object)
    {
        xmlWriter.putContextObject(key, object);
    }


    @Nullable
    public T getObject(@Nullable XMLContext xmlContext)
    {
        if (xmlContext == null)
        {
            return null;
        }

        //noinspection unchecked
        return (T) xmlContext.getContextObject(key);
    }
}
