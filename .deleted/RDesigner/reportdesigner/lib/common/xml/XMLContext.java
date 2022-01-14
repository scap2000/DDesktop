package org.pentaho.reportdesigner.lib.common.xml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * User: Martin
 * Date: 28.09.2007
 * Time: 13:32:44
 */
public class XMLContext
{
    @NotNull
    private HashMap<String, Object> contextObjects;


    public XMLContext()
    {
        contextObjects = new HashMap<String, Object>();
    }


    public void putContextObject(@NotNull String key, @Nullable Object value)
    {
        contextObjects.put(key, value);
    }


    @Nullable
    public Object getContextObject(@NotNull String key)
    {
        return contextObjects.get(key);
    }
}
