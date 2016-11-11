#include "command.h"
#include "commands/cmds.h"
#include <stdbool.h>
#include <string.h>
#include <endian.h>
#include <unistd.h>

#define MIN(a,b) (((a)<(b))?(a):(b))

static bool try_command(const char* cmdname, void (*callback)(const char*, int, uint64_t), const char* command, int len) {
    if(strncmp(cmdname, command, 4) == 0) {
        uint64_t* seq = (uint64_t*) (command + 4);
        callback(command, len, be64toh(*seq));
        return true;
    }
    return false;
}

void process_command(int len, const char* command) {
    if(len < 12) {
        fprintf(stderr, "Command too short\n");
    }
    if(try_command(CMD_PING_REQUEST,        command_ping,           command, len)) return;
    if(try_command(CMD_GET_INTERFACE_LIST,  command_get_interfaces, command, len)) return;
}

uint64_t cmdtoi(char* cmd) {
    return *((uint64_t*) cmd);
}

void writeCommand(const char* command, uint64_t seq, int datalen, void* data) {
    // [I4 Length of further data][I4 Command][I8 SEQ][data]
    int len = 4 + 4 + 8 + datalen;
    void* commandBuf = malloc(len);
    memset(commandBuf, 0, len);
    *((uint32_t*) commandBuf) = htobe32(4 + 8 + datalen);
    *((uint32_t*) (commandBuf + 4)) = *((uint32_t*) command);
    *((uint64_t*) (commandBuf + 8)) = htobe64(seq);
    if(datalen > 0 && data) {
        memcpy(commandBuf + 16, data, datalen);
    }

    write(STDOUT_FILENO, commandBuf, len);

    free(commandBuf);
}
