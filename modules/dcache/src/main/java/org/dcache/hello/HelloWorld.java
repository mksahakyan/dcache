package org.dcache.hello;

import org.springframework.beans.factory.annotation.Required;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NotYetBoundException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import diskCacheV111.util.CacheException;
import diskCacheV111.util.FsPath;
import diskCacheV111.util.PnfsHandler;
import diskCacheV111.vehicles.PoolManagerGetPoolsByPoolGroupMessage;
import diskCacheV111.vehicles.PoolManagerPoolInformation;

import dmg.cells.nucleus.CellPath;
import dmg.cells.nucleus.DelayedReply;
import dmg.cells.nucleus.Reply;
import dmg.util.Args;
import dmg.util.command.Argument;
import dmg.util.command.Command;

import org.dcache.auth.Subjects;
import org.dcache.cells.AbstractCellComponent;
import org.dcache.cells.AbstractMessageCallback;
import org.dcache.cells.CellCommandListener;
import org.dcache.cells.CellMessageReceiver;
import org.dcache.cells.CellStub;
import org.dcache.util.Transfer;
import org.dcache.util.TransferRetryPolicies;
import org.dcache.util.list.ListDirectoryHandler;

public class HelloWorld
    extends AbstractCellComponent
    implements CellCommandListener, CellMessageReceiver, Runnable
{
    private boolean isDefaultFormal;
    private CellStub poolManager;
    private CellStub helloStub;
    private PnfsHandler pnfs;
    private ServerSocketChannel channel;
    private ListDirectoryHandler lister;
    private CellStub billing;
    private CellStub pool;

    public boolean isDefaultFormal()
    {
        return isDefaultFormal;
    }

    public void setDefaultFormal(boolean defaultFormal)
    {
        isDefaultFormal = defaultFormal;
    }

    @Required
    public void setPoolManager(CellStub poolManager)
    {
        this.poolManager = poolManager;
    }

    @Required
    public void setPnfsHandler(PnfsHandler pnfs)
    {
        this.pnfs = pnfs;
    }

    @Required
    public void setHelloStub(CellStub helloStub)
    {
        this.helloStub = helloStub;
    }

    @Required
    public void setBilling(CellStub billing)
    {
        this.billing = billing;
    }

    @Required
    public void setPool(CellStub pool)
    {
        this.pool = pool;
    }

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


    public HelloMessage messageArrived(HelloMessage message)
    {
        String name = message.getName();
        message.setGreeting(isDefaultFormal ? "Hello, " + name : "Hi there, " + name);
        return message;
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
}
