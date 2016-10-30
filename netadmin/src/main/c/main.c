#include <netlink/route/link.h>
#include <netlink/socket.h>

int main() {
    struct nl_cache* cache;
    struct rtnl_link* link;
    struct nl_sock* sock = nl_socket_alloc();
    nl_connect(sock, NETLINK_ROUTE);

    if(!sock) {
        printf("error: nl_socket_alloc\n");
        return 1;
    }

    if(rtnl_link_alloc_cache(sock, AF_UNSPEC, &cache) < 0) {
        printf("error: rtnl_link_alloc_cache\n");
        return 1;
    }

    int i = 1;
    while(link = rtnl_link_get(cache, i)) {
        char b[32];
        memset(b, 0, 32);
        rtnl_link_i2name(cache, i, b, 32);
        printf("i: %s\n", b);
        i++;
    }


    rtnl_link_put(link);
    nl_cache_put(cache);
    nl_socket_free(sock);

    return 0;
}