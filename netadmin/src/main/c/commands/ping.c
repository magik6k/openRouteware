#include "cmds.h"
#include <stdio.h>
#include <unistd.h>

void command_ping(json_t* command, uint64_t seq) {
    writeCommand(CMD_PING_RESPONSE, seq, NULL);
}
