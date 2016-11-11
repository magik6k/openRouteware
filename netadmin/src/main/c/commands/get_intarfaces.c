#include "cmds.h"
#include <netlink/route/link.h>
#include <netlink/socket.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <unistd.h>
#define MIN(a,b) (((a)<(b))?(a):(b))

void command_get_interfaces(const char* command, int dlen, uint64_t seq) {
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

    int nifaces = nl_cache_nitems(cache);

    void* response = malloc(4 + 16 * nifaces);
    memset(response, 0, 4 + 16 * nifaces);

    *((uint32_t*) response) = htobe32(nifaces);
    char* rbuf = response + 4;

    int i = 0;
    for(struct nl_object* iface = nl_cache_get_first(cache); iface; iface = nl_cache_get_next(iface)) {
        const char* name = rtnl_link_get_name((struct rtnl_link*) iface);
        memcpy(rbuf + i * 16, name, MIN(16, strlen(name)));
        i++;
    }

    nl_cache_put(cache);
    nl_socket_free(sock);

    writeCommand(CMD_INTERFACE_LIST, seq, 4 + 16 * nifaces, response);
    free(response);
}
