package diskCacheV111.poolManager;

import java.net.UnknownHostException;
import java.util.concurrent.Callable;

import dmg.cells.nucleus.CellCommandListener;
import dmg.util.CommandSyntaxException;
import dmg.util.command.Argument;
import dmg.util.command.Command;
import org.dcache.util.Args;

/**
 * Pulled out of the PoolSelectionUnit implementation in order to delegate
 * to a particular instance of the
 * {@link diskCacheV111.poolManager.PoolSelectionUnitAccess}
 * abstraction.
 *
 * Created by arossi on 2/19/15.
 */
public class PoolSelectionUnitCommands implements CellCommandListener {

    private PoolSelectionUnitAccess psuAccess;

    public void setPsuAccess(PoolSelectionUnitAccess psuAccess) {
        this.psuAccess = psuAccess;
    }

    public final static String hh_psu_add_link = "<link> <pool>|<pool group>";

    public String ac_psu_add_link_$_2(Args args) {
        psuAccess.addLink(args.argv(0), args.argv(1));
        return "";
    }

    public final static String hh_psu_addto_pgroup = "<pool group> <pool>";

    public String ac_psu_addto_pgroup_$_2(Args args) {
        psuAccess.addToPoolGroup(args.argv(0), args.argv(1));
        return "";
    }

    public final static String hh_psu_addto_linkGroup = "<link group> <link>";

    public String ac_psu_addto_linkGroup_$_2(Args args) {
        psuAccess.addToLinkGroup(args.argv(0), args.argv(1));
        return "";
    }

    public final static String hh_psu_addto_ugroup = "<unit group> <unit>";

    public String ac_psu_addto_ugroup_$_2(Args args){
        psuAccess.addToUnitGroup(args.argv(0),
                        args.argv(1),
                        args.hasOption("net"));
        return "";
    }

    public final static String hh_psu_clear_im_really_sure = "# don't use this command";

    public String ac_psu_clear_im_really_sure(Args args) {
        psuAccess.clear();
        return "Voila, now everthing is really gone";
    }

    public final static String hh_psu_create_link = "<link> <unit group> [...]";

    public String ac_psu_create_link_$_2_99(Args args) {
        String name = args.argv(0);
        args.shift();
        psuAccess.createLink(name, args.getArguments());
        return "";
    }

    public final static String hh_psu_create_linkGroup = "<group name> [-reset]";

    public String ac_psu_create_linkGroup_$_1(Args args) {
        psuAccess.createLinkGroup(args.argv(0), args.hasOption("reset"));
        return "";
    }

    public final static String hh_psu_create_pool = "<pool> [-noping] [-disabled]";

    public String ac_psu_create_pool_$_1(Args args) {
        psuAccess.createPool(args.argv(0),
                             args.hasOption("noping"), args.hasOption("disabled"));
        return "";
    }

    public final static String hh_psu_create_pgroup = "<pool group>";

    public String ac_psu_create_pgroup_$_1(Args args) {
        psuAccess.createPoolGroup(args.argv(0));
        return "";
    }

    public final static String hh_psu_create_unit = "-net|-store|-dcache|-protocol <name>";

    public static final String fh_psu_create_unit =
                                    "NAME\n"+
                                    "\tpsu create unit\n\n"+
                                    "SYNOPSIS\n"+
                                    "\tpsu create unit UNITTYPE NAME\n\n"+
                                    "DESCRIPTION\n"+
                                    "\tCreates a new unit of the specified type.  A unit is a predicate\n" +
                                    "\tthat is used to select which pools are eligable for a specific user\n" +
                                    "\trequest (to read data from dCache or write data).  Units are\n" +
                                    "\tcombined in unit-groups; see psu create unitgroup for more details.\n\n" +
                                    "\tThe UNITTYPE is one of '-net', '-store', '-dcache' or '-protocol'\n" +
                                    "\tto create a network, store, dCache or protocol unit, respectively.\n\n" +
                                    "\tThe NAME of the unit describes which particular subset of user\n" +
                                    "\trequests will be selected; for example, a network unit with the\n" +
                                    "\tname '10.1.0.0/24' will select only those requests from a computer\n" +
                                    "\twith an IP address matching that subnet.\n\n" +
                                    "\tThe NAME for network units is either an IPv4 address, IPv6 address,\n" +
                                    "\tan IPv4 subnet or an IPv6 subnet.  Subnets may be written either\n" +
                                    "\tusing CIDR notation or as an IP address and netmask, joined by a\n"+
                                    "\t'/'.\n\n" +
                                    "\tThe NAME for store units has the form <StorageClass>@<HSM-type>.\n" +
                                    "\tBoth <StorageClass> and <HSM-type> may be replaced with a '*' to\n" +
                                    "\tmatch any value.  If the HSM-type is 'osm' then <StorageClass> is\n" +
                                    "\tconstructed by joining the store-name and store-group with a colon:\n" +
                                    "\t<StoreName>:<StoreGroup>@osm.\n\n" +
                                    "\tThe NAME for a dcache unit is an arbitrary string.  This matches\n" +
                                    "\tagainst the optional cache-class that may be set within the\n" +
                                    "\tnamespace in a similar fashion to the storage-class.\n\n" +
                                    "\tThe NAME for a protocol unit has the form <protocol>/<version>. If\n" +
                                    "\t<version> is '*' then all versions of that protocol match.\n\n" +
                                    "OPTIONS\n"+
                                    "\tnone\n";

    public String ac_psu_create_unit_$_1(Args args)
    {
        psuAccess.createUnit(args.argv(0),
                        args.hasOption("net"),
                        args.hasOption("store"),
                        args.hasOption("dcache"),
                        args.hasOption("protocol"));
        return "";
    }

    public final static String hh_psu_create_ugroup = "<unit group>";

    public String ac_psu_create_ugroup_$_1(Args args) {
        psuAccess.createUnitGroup(args.argv(0));
        return "";
    }

    public final static String hh_psu_dump_setup = "";

    public String ac_psu_dump_setup(Args args) {
        return psuAccess.dumpSetup();
    }

    public final static String hh_psu_ls_link = "[-l] [-a] [ <link> [...]]";

    public String ac_psu_ls_link_$_0_99(Args args) {
        boolean more = args.hasOption("a");
        boolean detail = args.hasOption("l") || more;
        return psuAccess.listPoolLinks(more, detail, args.getArguments());
    }

    public final static String hh_psu_ls_linkGroup
                    = "[-l] [<link group1> ... <link groupN>]";

    public String ac_psu_ls_linkGroup_$_0_99(Args args) {
        return psuAccess.listLinkGroups(args.hasOption("l"),
                        args.getArguments());
    }

    public final static String hh_psu_ls_netunits = "";

    public String ac_psu_ls_netunits(Args args) {
        return psuAccess.listNetUnits();
    }

    public final static String hh_psu_ls_pgroup = "[-l] [-a] [<pool group> [...]]";

    public String ac_psu_ls_pgroup_$_0_99(Args args) {
        boolean more = args.hasOption("a");
        boolean detail = args.hasOption("l") || more;
        return psuAccess.listPoolGroups(more, detail, args.getArguments());
    }

    public final static String hh_psu_ls_pool = "[-l] [-a] [<pool glob> [...]]";

    public String ac_psu_ls_pool_$_0_99(Args args) {
        boolean more = args.hasOption("a");
        boolean detail = args.hasOption("l") || more;
        return psuAccess.listPool(more, detail, args.getArguments());
    }

    public final static String hh_psu_ls_ugroup
                    = "[-l] [-a] [<unit group> [...]]";

    public String ac_psu_ls_ugroup_$_0_99(Args args) {
        boolean more = args.hasOption("a");
        boolean detail = args.hasOption("l") || more;
        return psuAccess.listUnitGroups(more, detail, args.getArguments());
    }

    public final static String hh_psu_ls_unit = " [-a] [<unit> [...]]";

    public String ac_psu_ls_unit_$_0_99(Args args) {
        boolean more = args.hasOption("a");
        boolean detail = args.hasOption("l") || more;
        return psuAccess.listUnits(more, detail, args.getArguments());
    }

    public final static String hh_psu_match = "[-linkGroup=<link group>] "
                    + "read|cache|write|p2p <store unit>|* <store unit>|* "
                    + "<store unit>|* <protocol unit>|* ";

    public String ac_psu_match_$_5(Args args) throws Exception {
        return psuAccess.matchLinkGroups(args.getOpt("linkGroup"), args.argv(0),
                        args.argv(1), args.argv(2), args.argv(3), args.argv(4));
    }

    public final static String hh_psu_match2 = "<unit> [...] [-net=<net unit>}";

    public String ac_psu_match2_$_1_99(Args args){
        return psuAccess.matchUnits(args.getOpt("net"), args.getArguments());
    }

    public final static String hh_psu_netmatch = "<host address>";

    public String ac_psu_netmatch_$_1(Args args) throws UnknownHostException {
        return psuAccess.netMatch(args.argv(0));
    }

    public final static String hh_psu_removefrom_linkGroup
                    = "<link group> <link>";

    public String ac_psu_removefrom_linkGroup_$_2(Args args) {
        psuAccess.removeFromLinkGroup(args.argv(0), args.argv(1));
        return "";
    }

    public final static String hh_psu_removefrom_pgroup = "<pool group> <pool>";

    public String ac_psu_removefrom_pgroup_$_2(Args args) {
        psuAccess.removeFromPoolGroup(args.argv(0), args.argv(1));
        return "";
    }

    public final static String hh_psu_removefrom_ugroup
                    = "<unit group> <unit> -net";

    public String ac_psu_removefrom_ugroup_$_2(Args args) {
        psuAccess.removeFromUnitGroup(args.argv(0), args.argv(1),
                        args.hasOption("net"));
        return "";
    }

    public final static String hh_psu_remove_link = "<link>";

    public String ac_psu_remove_link_$_1(Args args) {
        psuAccess.removeLink(args.argv(0));
        return "";
    }

    public final static String hh_psu_remove_linkGroup = "<link group>";

    public String ac_psu_remove_linkGroup_$_1(Args args) {
        psuAccess.removeLinkGroup(args.argv(0));
        return "";
    }

    public final static String hh_psu_remove_pool = "<pool>";

    public String ac_psu_remove_pool_$_1(Args args) {
        psuAccess.removePool(args.argv(0));
        return "";
    }

    public final static String hh_psu_remove_pgroup = "<pool group>";

    public String ac_psu_remove_pgroup_$_1(Args args) {
        psuAccess.removePoolGroup(args.argv(0));
        return "";
    }

    public final static String hh_psu_remove_unit = "<unit> [-net]";

    public String ac_psu_remove_unit_$_1(Args args) {
        psuAccess.removeUnit(args.argv(0), args.hasOption("net"));
        return "";
    }

    public final static String hh_psu_remove_ugroup = "<unit group>";

    public String ac_psu_remove_ugroup_$_1(Args args) {
        psuAccess.removeUnitGroup(args.argv(0));
        return "";
    }


    public final static String hh_psu_set_active = "<pool>|* [-no]";

    public String ac_psu_set_active_$_1(Args args) {
        psuAccess.setPoolActive(args.argv(0), !args.hasOption("no"));
        return "";
    }

    public final static String hh_psu_set_allpoolsactive = "on|off";

    public String ac_psu_set_allpoolsactive_$_1(Args args) throws
                    CommandSyntaxException {
        try {
            psuAccess.setAllPoolsActive(args.argv(0));
        } catch (IllegalArgumentException e) {
            throw new CommandSyntaxException(e.getMessage());
        }

        return "";
    }

    public final static String hh_psu_set_disabled = "<pool glob>";

    public String ac_psu_set_disabled_$_1(Args args) {
        return psuAccess.setPoolDisabled(args.argv(0));
    }

    public final static String hh_psu_set_enabled = "<pool glob>";

    public String ac_psu_set_enabled_$_1(Args args) {
        return psuAccess.setPoolEnabled(args.argv(0));
    }

    public final static String hh_psu_set_link
                    = "<link> [-readpref=<readpref>] [-writepref=<writepref>] "
                    + "[-cachepref=<cachepref>] [-p2ppref=<p2ppref>] "
                    + "[-section=<section>|NONE]";

    public String ac_psu_set_link_$_1(Args args) {
        psuAccess.setLink(args.argv(0), args.getOption("readpref"),
                        args.getOption("writepref"),
                        args.getOption("cachepref"), args.getOption("p2ppref"),
                        args.getOption("section"));
        return "";
    }


    public final static String hh_psu_set_linkGroup_custodialAllowed
                    = "<link group> <true|false>";

    public String ac_psu_set_linkGroup_custodialAllowed_$_2(Args args) {
        psuAccess.setLinkGroup(args.argv(0), args.argv(1), null, null, null,
                        null);
        return "";
    }

    public final static String hh_psu_set_linkGroup_nearlineAllowed
                    = "<link group> <true|false>";

    public String ac_psu_set_linkGroup_nearlineAllowed_$_2(Args args) {
        psuAccess.setLinkGroup(args.argv(0), null, args.argv(1), null, null,
                        null);
        return "";
    }

    public final static String hh_psu_set_linkGroup_onlineAllowed
                    = "<link group> <true|false>";

    public String ac_psu_set_linkGroup_onlineAllowed_$_2(Args args) {
        psuAccess.setLinkGroup(args.argv(0), null, null, args.argv(1), null,
                        null);
        return "";
    }

    public final static String hh_psu_set_linkGroup_outputAllowed
                    = "<link group> <true|false>";

    public String ac_psu_set_linkGroup_outputAllowed_$_2(Args args) {
        psuAccess.setLinkGroup(args.argv(0), null, null, null, args.argv(1),
                        null);
        return "";
    }

    public final static String hh_psu_set_linkGroup_replicaAllowed
                    = "<link group> <true|false>";

    public String ac_psu_set_linkGroup_replicaAllowed_$_2(Args args) {
        psuAccess.setLinkGroup(args.argv(0), null, null, null, null, args.argv(1));
        return "";
    }




    @Command(name = "psu set pool",
             hint = "update the mode of the pool ",
             description = "Changes the mode of the pools whos name match the given pattern (regular expression )." +
                     "Returns a the number of pools updated.")
    public class PsuSetPoolCommand implements Callable<String>
    {

        @Argument(index = 1,
                  valueSpec = "enabled|disabled|ping|noping|rdonly|notrdonly",
                  usage = "The new mode to be set.")
        String mode;

        @Argument(index = 0, metaVar = "pool glob", usage = "The patern")
        String poolgloob;


        @Override
        public String call() throws IllegalArgumentException
        {
            return psuAccess.setPool(poolgloob, mode);
        }
    }

    /*public final static String hh_psu_set_pool =
                    "<pool glob> enabled|disabled|ping|noping|rdonly|notrdonly";

    public String ac_psu_set_pool_$_2(Args args) {
        return psuAccess.setPool(args.argv(0), args.argv(1));
    }*/



    @Command(name = "psu set regex",
             hint = "turns the regex on or off",
             description ="This command is used to switch between different mode of checking whether" +
                     "there is an matching storage unit found for a given Storage Unit NAME;" +
                     "see psu create unit for more details." +
                     "The NAME for store units has the form <StorageClass>@<type-of-storage-system>."+
                     "Both <StorageClass> and <type-of-storage-system> may be replaced with a '*' (\"*@\"*) to"+
                     "match any value.  If the <type-of-storage-system> is HSM-type then <StorageClass> is"+
                     "constructed by joining the store-name and store-group with a colon:"+
                     "<StoreName>:<StoreGroup>@osm (\"run2010@osm\" or \"*@osm\")."+
                     "When the Regex is turned on the both \"*@osm\" and \"*@\"* regular expressions " +
                     "are considered valid while searching for the matching unit." +
                     " If the unit name does not match the pattern \"Unit not found\" exception" +
                     " will be raised.\n." +
                     "If the regex is turned off only \"*@\"* regular expressions is valid and only non HSM-type" +
                     "could be matched." )
    public class PsuSetRegexCommand implements Callable<String>
    {
        @Argument(valueSpec = "ON|OFF",
                  usage = "Switch used ")
        String _useRegex;

        @Override
        public String call() throws IllegalArgumentException
        {
            return psuAccess.setRegex(_useRegex);
        }
    }
    /*public final static String hh_psu_set_regex = "on | off";

    public String ac_psu_set_regex_$_1(Args args) {
        return psuAccess.setRegex(args.argv(0));
    }*/

    @Command(name = "psu unlink",
             hint = "deletes the pool from the link",
             description = "Removes the specified link form the list of links mapped to the specified pool or " +
                     "pool group (pools grouped together). Deletes as well the specified pool/pool group " +
                     "from the list of pools mapped to the given link. If the specified link  or pool/pool group" +
                     " names do not exist returns \"Not found\" exception. If the given link name is not associated " +
                     "with the given pool/ pool group returns exception that the \"given pool is not a member of the " +
                     "specified link\"")
    public class PsuUnlinkCommand implements Callable<String>
    {
        @Argument(index = 0,
                  usage = "The name of the link e.g. regular-read-link or regular-write-link." +
                          "The link is a set of rules describing which pools are permitted for which type of " +
                          "transfer-request, each link links a set of transfer-requests to a pool or group of pools." +
                          "To create a new link please use \"psu create link\" and \"psu set link\" command. ")
        String link;

        @Argument(index = 1,
                  valueSpec = "POOL|POOLGROUP",
                  usage = "The name of the pool or pool group associated to the specified link.")
        String poolOrPoolGroup;

        @Override
        public String call() throws IllegalArgumentException
        {
            psuAccess.unlink(link, poolOrPoolGroup);
            return "";
        }

    }
   /* public final static String hh_psu_unlink = "<link> <pool>|<pool group>";

    public String ac_psu_unlink_$_2(Args args) {
        psuAccess.unlink(args.argv(0), args.argv(1));
        return "";
    }*/

    /*
     * ***************************** PSUX **********************************
     */

    public final static String hh_psux_ls_link = "[<link>] [-x] [-resolve]";

    public Object ac_psux_ls_link_$_0_1(Args args) {
        String link = null;
        boolean resolve = args.hasOption("resolve");
        boolean isX = args.hasOption("x");
        if( args.argc() != 0) {
            link = args.argv(0);
        }

        return psuAccess.listLinkXml(isX, resolve, link);
    }

    public final static String hh_psux_ls_pgroup = "[<pool group>]";

    public Object ac_psux_ls_pgroup_$_0_1(Args args){
        String groupName = args.argc() == 0 ? null : args.argv(0);
        return psuAccess.listPoolGroupXml(groupName);
    }

    public final static String hh_psux_ls_pool = "[<pool>]";

    public Object ac_psux_ls_pool_$_0_1(Args args){
        String poolName = args.argc() == 0 ? null : args.argv(0);
        return psuAccess.listPoolXml(poolName);
    }

    public final static String hh_psux_ls_unit = "[<unit>]";

    public Object ac_psux_ls_unit_$_0_1(Args args) {
        String unitName = args.argc() == 0 ? null : args.argv(0);
        return psuAccess.listUnitXml(unitName);
    }

    public final static String hh_psux_ls_ugroup = "[<unit group>]";

    public Object ac_psux_ls_ugroup_$_0_1(Args args){
        String groupName = args.argc() == 0 ? null : args.argv(0);
        return psuAccess.listUnitGroupXml(groupName);
    }

    public final static String hh_psux_match = "[-linkGroup=<link group>] "
                    + "read|cache|write <store unit>|* <store unit>|* "
                    + "<store unit>|* <protocol unit>|* ";

    public Object ac_psux_match_$_5(Args args) {
        return psuAccess.matchLinkGroupsXml(args.getOpt("linkGroup"),
                        args.argv(0), args.argv(1), args.argv(2), args.argv(3),
                        args.argv(4));
    }
}
