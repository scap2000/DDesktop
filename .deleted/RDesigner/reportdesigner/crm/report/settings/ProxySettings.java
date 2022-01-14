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
package org.pentaho.reportdesigner.crm.report.settings;

import org.gjt.xpp.XmlPullNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.pentaho.reportdesigner.crm.report.PropertyKeys;
import org.pentaho.reportdesigner.lib.common.xml.XMLExternalizable;
import org.pentaho.reportdesigner.lib.common.xml.XMLWriter;
import org.pentaho.reportdesigner.lib.common.xml.XMLContext;

import java.io.IOException;

/**
 * User: Martin
 * Date: 03.03.2006
 * Time: 14:12:13
 */
public class ProxySettings implements XMLExternalizable
{
    @NotNull
    private static final String LOCALHOST = "localhost";


    @NotNull
    private ProxyType proxyType;
    @NotNull
    private String httpProxyHost;
    @NotNull
    private String httpProxyPort;

    private boolean useSocksProxy;
    @NotNull
    private String socksProxyPort;
    @NotNull
    private String socksProxyHost;

    @NotNull
    private String proxyUser;
    @NotNull
    private String proxyPassword;


    public ProxySettings()
    {
        proxyType = ProxyType.AUTO_DETECT_PROXY;
        String httpProxyHost = System.getProperty(PropertyKeys.HTTP_DOT_PROXY_HOST);
        String httpProxyPort = System.getProperty(PropertyKeys.HTTP_DOT_PROXY_PORT);

        if (httpProxyHost == null)
        {
            this.httpProxyHost = "";
        }
        else
        {
            this.httpProxyHost = httpProxyHost;
        }

        if (httpProxyPort == null)
        {
            this.httpProxyPort = "";
        }
        else
        {
            this.httpProxyPort = httpProxyPort;
        }

        socksProxyHost = "";
        socksProxyPort = "";

        proxyUser = "";
        proxyPassword = "";
    }


    @NotNull
    public ProxyType getProxyType()
    {
        return proxyType;
    }


    public void setProxyType(@NotNull ProxyType proxyType)
    {
        //noinspection ConstantConditions
        if (proxyType == null)
        {
            throw new IllegalArgumentException("proxyType must not be null");
        }

        this.proxyType = proxyType;
    }


    @NotNull
    public String getHTTPProxyHost()
    {
        return httpProxyHost;
    }


    public void setHTTPProxyHost(@NotNull String httpProxyHost)
    {
        //noinspection ConstantConditions
        if (httpProxyHost == null)
        {
            throw new IllegalArgumentException("httpProxyHost must not be null");
        }

        this.httpProxyHost = httpProxyHost;
    }


    @NotNull
    public String getHTTPProxyPort()
    {
        return httpProxyPort;
    }


    public void setHTTPProxyPort(@NotNull String httpProxyPort)
    {
        //noinspection ConstantConditions
        if (httpProxyPort == null)
        {
            throw new IllegalArgumentException("httpProxyPort must not be null");
        }

        this.httpProxyPort = httpProxyPort;
    }


    public void setProxyUser(@NotNull String proxyUser)
    {
        //noinspection ConstantConditions
        if (proxyUser == null)
        {
            throw new IllegalArgumentException("proxyUser must not be null");
        }
        this.proxyUser = proxyUser;
    }


    public void setProxyPassword(@NotNull String proxyPassword)
    {
        //noinspection ConstantConditions
        if (proxyPassword == null)
        {
            throw new IllegalArgumentException("proxyPassword must not be null");
        }
        this.proxyPassword = proxyPassword;
    }


    public boolean isUseSocksProxy()
    {
        return useSocksProxy;
    }


    public void setUseSocksProxy(boolean useSocksProxy)
    {
        this.useSocksProxy = useSocksProxy;
    }


    @NotNull
    public String getSocksProxyPort()
    {
        return socksProxyPort;
    }


    public void setSocksProxyPort(@NotNull String socksProxyPort)
    {
        //noinspection ConstantConditions
        if (socksProxyPort == null)
        {
            throw new IllegalArgumentException("socksProxyPort must not be null");
        }

        this.socksProxyPort = socksProxyPort;
    }


    @NotNull
    public String getSocksProxyHost()
    {
        return socksProxyHost;
    }


    public void setSocksProxyHost(@NotNull String socksProxyHost)
    {
        //noinspection ConstantConditions
        if (socksProxyHost == null)
        {
            throw new IllegalArgumentException("socksProxyHost must not be null");
        }

        this.socksProxyHost = socksProxyHost;
    }


    @NotNull
    public String getHTTPNonProxyHosts()
    {
        return LOCALHOST;
    }


    @NotNull
    public String getSOCKSNonProxyHosts()
    {
        return LOCALHOST;
    }


    @NotNull
    public String getProxyUser()
    {
        if (proxyType == ProxyType.NO_PROXY)
        {
            return "";
        }
        return proxyUser;
    }


    @NotNull
    public String getProxyPassword()
    {
        if (proxyType == ProxyType.NO_PROXY)
        {
            return "";
        }
        return proxyPassword;
    }


    public void applySettings()
    {
        switch (proxyType)
        {
            case AUTO_DETECT_PROXY:
            {
                System.setProperty(PropertyKeys.HTTP_DOT_PROXY_HOST, "");
                System.setProperty(PropertyKeys.HTTP_DOT_PROXY_PORT, "");
                System.setProperty(PropertyKeys.SOCKS_PROXY_HOST, "");
                System.setProperty(PropertyKeys.SOCKS_PROXY_PORT, "");

                String host = getWebstartHTTPProxyHost();
                boolean httpProxySet = false;
                if (host != null && host.trim().length() > 0)
                {
                    System.setProperty(PropertyKeys.HTTP_DOT_PROXY_HOST, host);
                    httpProxySet = true;
                }
                String port = getWebstartHTTPProxyPort();
                if (port != null)
                {
                    System.setProperty(PropertyKeys.HTTP_DOT_PROXY_PORT, port);
                }

                if (!httpProxySet)
                {
                    String socksHost = getWebstartSOCKSProxyHost();
                    if (socksHost != null && socksHost.trim().length() > 0)
                    {
                        System.setProperty(PropertyKeys.SOCKS_PROXY_HOST, socksHost);
                    }

                    String socksPort = getWebstartSOCKSProxyPort();
                    if (socksPort != null && socksPort.trim().length() > 0)
                    {
                        System.setProperty(PropertyKeys.SOCKS_PROXY_PORT, socksPort);
                    }
                }
            }
            break;
            case NO_PROXY:
            {
                System.setProperty(PropertyKeys.HTTP_DOT_PROXY_HOST, "");
                System.setProperty(PropertyKeys.HTTP_DOT_PROXY_PORT, "");
                System.setProperty(PropertyKeys.SOCKS_PROXY_HOST, "");
                System.setProperty(PropertyKeys.SOCKS_PROXY_PORT, "");
            }
            break;
            case USER_PROXY:
            {
                if (useSocksProxy)
                {
                    System.setProperty(PropertyKeys.HTTP_DOT_PROXY_HOST, "");
                    System.setProperty(PropertyKeys.HTTP_DOT_PROXY_PORT, "");
                    System.setProperty(PropertyKeys.SOCKS_PROXY_HOST, socksProxyHost);
                    System.setProperty(PropertyKeys.SOCKS_PROXY_PORT, socksProxyPort);
                }
                else
                {
                    System.setProperty(PropertyKeys.HTTP_DOT_PROXY_HOST, httpProxyHost);
                    System.setProperty(PropertyKeys.HTTP_DOT_PROXY_PORT, httpProxyPort);
                    System.setProperty(PropertyKeys.SOCKS_PROXY_HOST, "");
                    System.setProperty(PropertyKeys.SOCKS_PROXY_PORT, "");
                }
            }
            break;
        }
    }


    public void externalizeObject(@NotNull XMLWriter xmlWriter, @NotNull XMLContext xmlContext) throws IOException
    {
        xmlWriter.writeAttribute("v", "1");

        if (httpProxyHost != null) xmlWriter.writeAttribute(PropertyKeys.HTTP_PROXY_HOST, httpProxyHost);
        if (httpProxyPort != null) xmlWriter.writeAttribute(PropertyKeys.HTTP_PROXY_PORT, httpProxyPort);

        String proxyTypeName = "AUTO_DETECT_PROXY";//NON-NLS
        if (proxyType == ProxyType.NO_PROXY)
        {
            proxyTypeName = "NO_PROXY";//NON-NLS
        }
        else if (proxyType == ProxyType.USER_PROXY)
        {
            proxyTypeName = "USER_PROXY";//NON-NLS
        }
        xmlWriter.writeAttribute(PropertyKeys.PROXY_TYPE_NAME, proxyTypeName);

        xmlWriter.writeAttribute(PropertyKeys.USE_SOCKS_PROXY, String.valueOf(useSocksProxy));
        if (socksProxyHost != null) xmlWriter.writeAttribute(PropertyKeys.SOCKS_PROXY_HOST, socksProxyHost);
        if (socksProxyPort != null) xmlWriter.writeAttribute(PropertyKeys.SOCKS_PROXY_PORT, socksProxyPort);

        xmlWriter.writeAttribute(PropertyKeys.PROXY_USER, proxyUser);
        xmlWriter.writeAttribute(PropertyKeys.PROXY_PASSWORD, proxyPassword);
    }


    public void readObject(@NotNull XmlPullNode node, @NotNull XMLContext xmlContext)
    {
        String httpProxyHost = node.getAttributeValueFromRawName(PropertyKeys.PROXY_HOST);//backward compatibility
        if (httpProxyHost == null)
        {
            httpProxyHost = node.getAttributeValueFromRawName(PropertyKeys.HTTP_PROXY_HOST);
        }
        if (httpProxyHost == null)
        {
            this.httpProxyHost = "";
        }
        else
        {
            this.httpProxyHost = httpProxyHost;
        }

        String httpProxyPort = node.getAttributeValueFromRawName(PropertyKeys.PROXY_PORT);//backward compatibility
        if (httpProxyPort == null)
        {
            httpProxyPort = node.getAttributeValueFromRawName(PropertyKeys.HTTP_PROXY_PORT);
        }

        if (httpProxyPort == null)
        {
            this.httpProxyPort = "";
        }
        else
        {
            this.httpProxyPort = httpProxyPort;
        }

        String proxyTypeName = node.getAttributeValueFromRawName(PropertyKeys.PROXY_TYPE_NAME);

        if ("NO_PROXY".equals(proxyTypeName))//NON-NLS
        {
            proxyType = ProxyType.NO_PROXY;
        }
        else if ("USER_PROXY".equals(proxyTypeName))//NON-NLS
        {
            proxyType = ProxyType.USER_PROXY;
        }
        else
        {
            proxyType = ProxyType.AUTO_DETECT_PROXY;
        }

        //SOCKS
        useSocksProxy = Boolean.parseBoolean(node.getAttributeValueFromRawName(PropertyKeys.USE_SOCKS_PROXY));
        String socksProxyHost = node.getAttributeValueFromRawName(PropertyKeys.SOCKS_PROXY_HOST);
        String socksProxyPort = node.getAttributeValueFromRawName(PropertyKeys.SOCKS_PROXY_PORT);

        if (socksProxyHost == null)
        {
            this.socksProxyHost = "";
        }
        else
        {
            this.socksProxyHost = socksProxyHost;
        }

        if (socksProxyPort == null)
        {
            this.socksProxyPort = "";
        }
        else
        {
            this.socksProxyPort = socksProxyPort;
        }

        String proxyUser = node.getAttributeValueFromRawName(PropertyKeys.PROXY_USER);
        String proxyPassword = node.getAttributeValueFromRawName(PropertyKeys.PROXY_PASSWORD);

        if (proxyUser == null)
        {
            this.proxyUser = "";
        }
        else
        {
            this.proxyUser = proxyUser;
        }

        if (proxyPassword == null)
        {
            this.proxyPassword = "";
        }
        else
        {
            this.proxyPassword = proxyPassword;
        }
        applySettings();
    }


    @Nullable
    public static String getWebstartHTTPProxyHost()
    {
        String host = System.getProperty(PropertyKeys.DEPLOYMENT_PROXY_HTTP_HOST);
        if (host != null)
        {
            return host;
        }

        host = System.getProperty(PropertyKeys.PROXY_HOST);
        return host;
    }


    @Nullable
    public static String getWebstartHTTPProxyPort()
    {
        String port = System.getProperty(PropertyKeys.DEPLOYMENT_PROXY_HTTP_PORT);
        if (port != null)
        {
            return port;
        }

        port = System.getProperty(PropertyKeys.PROXY_PORT);
        return port;
    }


    @Nullable
    public static String getWebstartSOCKSProxyHost()
    {
        String host = System.getProperty(PropertyKeys.DEPLOYMENT_PROXY_SOCKS_HOST);
        return host;
    }


    @Nullable
    public static String getWebstartSOCKSProxyPort()
    {
        String port = System.getProperty(PropertyKeys.DEPLOYMENT_PROXY_SOCKS_PORT);
        return port;
    }

}
