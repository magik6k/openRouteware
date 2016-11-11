#include "cmds.h"
#include <netlink/route/link.h>
#include <netlink/socket.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <unistd.h>
#define MIN(a,b) (((a)<(b))?(a):(b))

void command_get_interfaces(json_t* command, uint64_t seq) {
    struct nl_cache* cache;
    struct nl_sock* sock = nl_socket_alloc();
    nl_connect(sock, NETLINK_ROUTE);

    if(!sock) {
        //printf("error: nl_socket_alloc\n");
        return;
    }

    if(rtnl_link_alloc_cache(sock, AF_UNSPEC, &cache) < 0) {
        //printf("error: rtnl_link_alloc_cache\n");
        return;
    }

    json_t* response = json_object();
    json_t* interfaces = json_object();

    for(struct nl_object* iface = nl_cache_get_first(cache); iface; iface = nl_cache_get_next(iface)) {
        const char* name = rtnl_link_get_name((struct rtnl_link*) iface);
        int ifindex = rtnl_link_get_ifindex((struct rtnl_link*) iface);
        json_object_set_new(interfaces, name, json_integer(ifindex));
    }

    nl_cache_put(cache);
    nl_socket_free(sock);

    json_object_set_new(response, "interfaces", interfaces);
    writeCommand(CMD_INTERFACE_LIST, seq, response);
}
