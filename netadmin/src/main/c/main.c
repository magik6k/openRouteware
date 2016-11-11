#include <bencodetools/bencode.h>
#include <stdio.h>
#include <stdbool.h>
#include <errno.h>
#include <unistd.h>
#include "command.h"
#include <endian.h>
#include <string.h>

int main() {
    setvbuf(stdout, NULL, _IONBF, 0);

    while(true) {
        int toRead = 0;
        int r = read(STDIN_FILENO, &toRead, 4);
        if(r != 4) {
            fprintf(stderr, "PANIC: desync cmd read\n");
            exit(1);
            //TODO: make it not do that
        }
        toRead = be32toh(toRead);

        char* buf = malloc(toRead);
        memset(buf, 0, toRead);
        int offset = 0;

        while(toRead > 0) {
            int r = read(STDIN_FILENO, buf + offset, toRead - offset);
            if(r > 0) {
                toRead -= r;
                offset += r;
            } else if(errno == EAGAIN || errno == EINTR) {
                continue;
            } else {
                toRead = -1;
            }
        }

        if(toRead < 0) {
            fprintf(stderr, "PANIC: read error\n");
            exit(1);
        }

        process_command(offset, buf);

        free(buf);
    }

    return 0;
}