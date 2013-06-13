Useful links
------------

- http://docs.oracle.com/javase/7/docs/api/
- http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/index.html
- http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/ (in particular chapter 4)


Example 1
------------
modules/dcache/src/main/java/org/dcache/hello/HelloWorld.java

```java
package org.dcache.hello;

import dmg.cells.nucleus.CellAdapter;

public class HelloWorld extends CellAdapter
{
    public HelloWorld(String cellName, String args)
    {
        super(cellName, args);
    }
}
```


skel/share/services/hello.batch

    create org.dcache.hello.HelloWorld hello

- Add service to layout file
- Interact with it
- Show that it isn't present in `dcache services` output

Example 2
------------
skel/share/services/hello.batch

    check -strong cell.name
    create org.dcache.hello.HelloWorld "${cell.name}"

skel/share/defaults/hello.properties

    hello/cell.name=hello

Example 3
------------

modules/dcache/src/main/resources/org/dcache/hello/hello.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
           http://www.springframework.org/schema/beans/spring-beans.xsd
           http://www.springframework.org/schema/context
           http://www.springframework.org/schema/context/spring-context.xsd">

    <context:annotation-config/>

    <bean id="properties" class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <description>Imported configuration data</description>
        <property name="location" value="arguments:"/>
    </bean>
</beans>
```

skel/share/services/hello.batch

    check -strong cell.name
    create org.dcache.cells.UniversalSpringCell "${cell.name}" \
        classpath:org/dcache/hello/hello.xml

- Interact with it. 
- Show what we get from the universal spring cell


Example 4
-------------

modules/dcache/src/main/java/org/dcache/hello/HelloWorld.java

```java
package org.dcache.hello;

import java.util.concurrent.Callable;

import dmg.util.Args;
import dmg.util.command.Argument;
import dmg.util.command.Command;
import dmg.util.command.Option;

import org.dcache.cells.CellCommandListener;

public class HelloWorld
    implements CellCommandListener
{
    public static final String hh_hello = "[-formal] <name> # prints greeting";
    public static final String fh_hello = "Prints a greeting for NAME. Outputs a formal greeting if -formal is given.";
    public String ac_hello_$_1(Args args)
    {
        String name = args.argv(0);
        boolean isFormal = args.getOption("formal") != null;
        if (isFormal) {
            return "Hello, " + name;
        } else {
            return "Hi there, " + name;
        }
    }

    @Command(name = "hi", hint = "prints greeting", usage = "Prints a greeting for NAME.")
    class HelloCommand implements Callable<String>
    {
        @Argument(help = "Your name")
        String name;

        @Option(name="formal", usage = "Output a formal greeting")
        boolean isFormal;

        @Override
        public String call()
        {
            if (isFormal) {
                return "Hello, " + name;
            } else {
                return "Hi there, " + name;
            }
        }
    }
}
```

modules/dcache/src/main/resources/org/dcache/hello/hello.xml

```xml
    <bean id="hello" class="org.dcache.hello.HelloWorld">
        <description>Says hello</description>
    </bean>
```


Example 5
------------

HelloWorld.java

```java
package org.dcache.hello;

import java.util.concurrent.Callable;

import dmg.util.Args;
import dmg.util.command.Argument;
import dmg.util.command.Command;
import dmg.util.command.Option;

import org.dcache.cells.CellCommandListener;

public class HelloWorld
    implements CellCommandListener
{
    private boolean isDefaultFormal;

    public boolean isDefaultFormal()
    {
        return isDefaultFormal;
    }

    public void setDefaultFormal(boolean defaultFormal)
    {
        isDefaultFormal = defaultFormal;
    }

    public static final String hh_hello = "[-formal] <name> # prints greeting";
    public static final String fh_hello = "Prints a greeting for NAME. Outputs a formal greeting if -formal is given.";
    public String ac_hello_$_1(Args args)
    {
        String name = args.argv(0);
        String formal = args.getOption("formal");
        boolean isFormal = (formal != null) ? formal.equals("") || Boolean.parseBoolean(formal) : isDefaultFormal;
        if (isFormal) {
            return "Hello, " + name;
        } else {
            return "Hi there, " + name;
        }
    }

    @Command(name = "hi", hint = "prints greeting", usage = "Prints a greeting for NAME.")
    class HelloCommand implements Callable<String>
    {
        @Argument(help = "Your name")
        String name;

        @Option(name="formal", usage = "Output a formal greeting")
        boolean isFormal = isDefaultFormal;

        @Override
        public String call()
        {
            if (isFormal) {
                return "Hello, " + name;
            } else {
                return "Hi there, " + name;
            }
        }
    }
}
```

hello.xml

```xml
    <bean id="hello" class="org.dcache.hello.HelloWorld">
        <description>Says hello</description>
        <property name="defaultFormal" value="${isDefaultFormal}"/>
    </bean>
```

hello.batch

    check -strong cell.name
    check -strong hello.formal
    
    create org.dcache.cells.UniversalSpringCell "${cell.name}" \
        "classpath:org/dcache/hello/hello.xml \
          -isDefaultFormal=\"${hello.formal}\"\
        "


hello.properties

    hello/cell.name=hello
    hello.formal=false

- Change default in configuration
- Inspect value through bean commands
- Maybe use json api?

Example 6
------------

HelloWorld.java

```java
    @Command(name = "pools", hint = "show pools in pool group",
            usage = "Shows the names of the pools in POOLGROUP.")
    class PoolsCommands implements Callable<ArrayList<String>>
    {
        @Argument(help = "A pool group")
        String poolGroup;

        @Override
        public ArrayList<String> call() throws CacheException, InterruptedException
        {
            PoolManagerGetPoolsByPoolGroupMessage request = new PoolManagerGetPoolsByPoolGroupMessage(poolGroup);
            PoolManagerGetPoolsByPoolGroupMessage reply = poolManager.sendAndWait(request);
            ArrayList<String> names = new ArrayList<>();
            for (PoolManagerPoolInformation pool : reply.getPools()) {
                names.add(pool.getName());
            }
            return names;
        }
    }

    public CellStub getPoolManager()
    {
        return poolManager;
    }

    @Required
    public void setPoolManager(CellStub poolManager)
    {
        this.poolManager = poolManager;
    }
```

hello.xml

```xml
    <bean id="hello" class="org.dcache.hello.HelloWorld">
        <description>Says hello</description>
        <property name="defaultFormal" value="${isDefaultFormal}"/>
        <property name="poolManager" ref="pool-manager-stub"/>
    </bean>
```

hello.batch

    check -strong cell.name
    check -strong hello.formal
    check -strong poolmanager
    
    create org.dcache.cells.UniversalSpringCell "${cell.name}" \
        "classpath:org/dcache/hello/hello.xml  \
          -isDefaultFormal=\"${hello.formal}\" \
          -poolmanager=\"${poolmanager}\"      \
        "

Example 7
------------

hello.xml

```xml
    <bean id="pnfs-handler" class="diskCacheV111.util.PnfsHandler">
        <constructor-arg>
            <bean class="org.dcache.cells.CellStub">
                <property name="destination" value="PnfsManager"/>
                <property name="timeout" value="30000"/>
            </bean>
        </constructor-arg>
    </bean>

    <bean id="hello" class="org.dcache.hello.HelloWorld">
        <description>Says hello</description>
        <property name="defaultFormal" value="${isDefaultFormal}"/>
        <property name="poolManager" ref="pool-manager-stub"/>
        <property name="pnfsHandler" ref="pnfs-handler"/>
    </bean>
```

HelloWorld.java

```java
    @Required
    public void setPnfsHandler(PnfsHandler pnfs)
    {
        this.pnfs = pnfs;
    }

    @Command(name = "mkdir", hint = "create directory",
            usage = "Create a new directory")
    class MkdirCommand implements Callable<String>
    {
        @Argument(help = "Directory name")
        String name;

        @Override
        public String call() throws CacheException
        {
            pnfs.createPnfsDirectory(name);
            return "";
        }
    }
```

Example 8
------------

HelloWorld.java

```java
    @Command(name = "hi", hint = "prints greeting", usage = "Prints a greeting for NAME.")
    class HelloCommand implements Callable<String>
    {
        @Argument(index = 0, help = "Your name")
        String name;

        @Argument(index = 1, help = "Name of a hello cell")
        String cell;

        @Override
        public String call() throws CacheException, InterruptedException
        {
            return helloStub.sendAndWait(new CellPath(cell), new HelloMessage(name)).getGreeting();
        }
    }

    @Required
    public void setHelloStub(CellStub helloStub)
    {
        this.helloStub = helloStub;
    }

    public HelloMessage messageArrived(HelloMessage message)
    {
        String name = message.getName();
        message.setGreeting(isDefaultFormal ? "Hello, " + name : "Hi there, " + name);
        return message;
    }
```

hello.xml

```xml
    <bean id="hello-stub" class="org.dcache.cells.CellStub">
        <property name="timeout" value="10000"/>
    </bean>
```

HelloMessage.java

```java
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
```

Example 9
------------

HelloWorld.java

```java
    @Command(name = "hi", hint = "prints greeting", usage = "Prints a greeting for NAME.")
    class HelloCommand extends DelayedReply implements Callable<Reply>, Runnable
    {
        @Argument(index = 0, help = "Your name")
        String name;
    
        @Argument(index = 1, help = "Name of a hello cell")
        String cell;
    
        @Override
        public Reply call()
        {
            new Thread(this).start();
            return this;
        }
    
        @Override
        public void run()
        {
             try {
                 reply(helloStub.sendAndWait(new CellPath(cell), new HelloMessage(name)).getGreeting());
             } catch (CacheException e) {
                 reply(e);
             }
        }
    }
```

Example 10
-------------

HelloWorld.java

```java
    @Command(name = "hi", hint = "prints greeting", usage = "Prints a greeting for NAME.")
    class HelloCommand extends DelayedCommand<String>
    {
        @Argument(index = 0, help = "Your name")
        String name;
    
        @Argument(index = 1, help = "Name of a hello cell")
        String cell;
    
        @Override
        public String execute() throws CacheException, InterruptedException
        {
            return helloStub.sendAndWait(new CellPath(cell), new HelloMessage(name)).getGreeting();
        }
    }
```

Example 11
------------
    
HelloWorld.java

```java
    @Command(name = "hi", hint = "prints greeting", usage = "Prints a greeting for NAME.")
    class HelloCommand extends DelayedReply implements Callable<Reply>
    {
        @Argument(index = 0, help = "Your name")
        String name;
    
        @Argument(index = 1, help = "Name of a hello cell")
        String cell;
    
        @Override
        public Reply call() throws CacheException, InterruptedException
        {
            helloStub.send(new CellPath(cell), new HelloMessage(name),
                    HelloMessage.class, new AbstractMessageCallback<HelloMessage>()
            {
                @Override
                public void success(HelloMessage message)
                {
                    reply(message.getGreeting());
                }
    
                @Override
                public void failure(int rc, Object error)
                {
                    reply(error.toString() + "(" + rc + ")");
                }
            });
            return this;
        }
    }
```

Example 12
------------

HelloWorld.java

```java
    @Required
    public void setListDirectoryHandler(ListDirectoryHandler lister)
    {
        this.lister = lister;
    }
    
    public void start() throws IOException
    {
        channel = ServerSocketChannel.open();
        channel.bind(new InetSocketAddress(2000));
        new Thread(this).start();
    }
    
    public void stop() throws IOException
    {
        if (channel != null) {
            channel.close();
        }
    }
    
    @Override
    public void run()
    {
        while (true) {
            try {
                try (SocketChannel connection = channel.accept()) {
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.socket()
                            .getOutputStream()));
                    try {
                        lister.printDirectory(Subjects.ROOT, new ListPrinter(out), new FsPath("/"),
                                null, Range.<Integer>all());
                    } catch (CacheException e) {
                        out.println(e.toString());
                    }
                    out.flush();
                }
            } catch (NotYetBoundException e) {
                // log error
                break;
            } catch (InterruptedException | ClosedChannelException e) {
                break;
            } catch (IOException e) {
                // log error
            }
        }
    }

    private static class ListPrinter implements DirectoryListPrinter
    {
        private final PrintWriter writer;
    
        private ListPrinter(PrintWriter writer)
        {
            this.writer = writer;
        }
    
        @Override
        public Set<FileAttribute> getRequiredAttributes()
        {
            return EnumSet.noneOf(FileAttribute.class);
        }
    
        @Override
        public void print(FsPath dir, FileAttributes dirAttr, DirectoryEntry entry)
                throws InterruptedException
        {
            writer.println(entry.getName());
        }
    }
```

hello.xml

```xml
    <bean id="list-handler" class="org.dcache.util.list.ListDirectoryHandler">
        <constructor-arg ref="pnfs-handler"/>
    </bean>
```

Example 13
-------------

pool.batch

    movermap define hello-1 org.dcache.hello.HelloMover

HelloProtocolInfo.java

```java
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
```

HelloMover.java

```java
    public class HelloMover implements MoverProtocol
    {
        private MoverChannel<HelloProtocolInfo> channel;

        public HelloMover(CellEndpoint endpoint)
        {
        }

        @Override
        public void runIO(FileAttributes fileAttributes, RepositoryChannel diskFile,
                          ProtocolInfo protocol, Allocator allocator, IoMode access) throws Exception
        {
            HelloProtocolInfo pi = (HelloProtocolInfo) protocol;
            channel = new MoverChannel<>(access, fileAttributes, pi, diskFile, allocator);
            try (SocketChannel connection = SocketChannel.open(pi.getSocketAddress())) {
                ByteStreams.copy(connection, channel);
            }
        }

        @Override
        public long getBytesTransferred()
        {
            return channel.getBytesTransferred();
        }

        @Override
        public long getTransferTime()
        {
            return channel.getTransferTime();
        }

        @Override
        public long getLastTransferred()
        {
            return channel.getLastTransferred();
        }
    }
```

HelloWorld.java

```java
    @Override
    public void run()
    {
        while (true) {
            try {
                try (SocketChannel connection = channel.accept()) {
                    PrintWriter out = new PrintWriter(new OutputStreamWriter(connection.socket().getOutputStream()));
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.socket().getInputStream()));
                        int port = Integer.parseInt(reader.readLine());

                        Transfer transfer = new Transfer(pnfs, Subjects.ROOT, new FsPath("/disk/" + System.currentTimeMillis()));
                        transfer.setClientAddress((InetSocketAddress) connection.getRemoteAddress());
                        transfer.setBillingStub(billing);
                        transfer.setPoolManagerStub(poolManager);
                        transfer.setPoolStub(pool);
                        transfer.setCellName(getCellName());
                        transfer.setDomainName(getCellDomainName());
                        InetSocketAddress address = new InetSocketAddress(((InetSocketAddress) connection
                                .getRemoteAddress()).getAddress(), port);
                        transfer.setProtocolInfo(new HelloProtocolInfo(address));
                        transfer.createNameSpaceEntry();
                        transfer.selectPoolAndStartMover(null, TransferRetryPolicies.tryOncePolicy(1000));
                    } catch (IOException | CacheException e) {
                        out.println(e.toString());
                    }
                    out.flush();
                }
            } catch (NotYetBoundException e) {
                // log error
                break;
            } catch (InterruptedException | ClosedChannelException e) {
                break;
            } catch (IOException e) {
                // log error
            }
        }
    }
```
