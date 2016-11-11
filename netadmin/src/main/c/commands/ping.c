#include "cmds.h"
#include <stdio.h>
#include <unistd.h>

void command_ping(const char* command, int dlen, uint64_t seq) {
    writeCommand(CMD_PING_RESPONSE, seq, 0, NULL);
}
