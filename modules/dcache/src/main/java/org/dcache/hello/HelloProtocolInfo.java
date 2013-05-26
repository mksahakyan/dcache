package org.dcache.hello;

import java.net.InetSocketAddress;

import diskCacheV111.vehicles.IpProtocolInfo;

public class HelloProtocolInfo implements IpProtocolInfo
{
    private final InetSocketAddress address;

    public HelloProtocolInfo(InetSocketAddress address)
    {
        this.address = address;
    }

    @Override
    public String getProtocol()
    {
        return "hello";
    }

    @Override
    public int getMinorVersion()
    {
        return 0;
    }

    @Override
    public int getMajorVersion()
    {
        return 1;
    }

    @Override
    public String getVersionString()
    {
        return "hello-1.0";
    }

    @Override
    public InetSocketAddress getSocketAddress()
    {
        return address;
    }
}
