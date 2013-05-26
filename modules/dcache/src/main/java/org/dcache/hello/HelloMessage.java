package org.dcache.hello;

import diskCacheV111.vehicles.Message;

public class HelloMessage extends Message
{
    private final String name;
    private String greeting;

    public HelloMessage(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public String getGreeting()
    {
        return greeting;
    }

    public void setGreeting(String greeting)
    {
        this.greeting = greeting;
    }
}
