#include "command.h"
#include "commands/cmds.h"
#include <stdbool.h>
#include <string.h>
#include <endian.h>
#include <unistd.h>
#include <jansson.h>

#define MIN(a,b) (((a)<(b))?(a):(b))

static bool try_command(const char* cmdname, void (*callback)(json_t*, uint64_t), json_t* json) {
    if(strcmp(cmdname, json_string_value(json_object_get(json, "cmd"))) == 0) {
        uint64_t seq = json_integer_value(json_object_get(json, "seq"));
        callback(json, seq);
        return true;
    }
    return false;
}

void process_command(int len, const char* command) {
    json_error_t error;
    json_t* json = json_loadb(command, len, 0, &error);
    if(json) {
        if(try_command(CMD_PING_REQUEST,        command_ping,           json)) goto decref;
        if(try_command(CMD_GET_INTERFACE_LIST,  command_get_interfaces, json)) goto decref;
decref:
        json_decref(json);
    } else {
        //TODO: Print actual error here
        fprintf(stderr, "NetAdmin: cmd json parse error\n");
    }

    fprintf(stderr, "cmd(%d): %.*s\n", len, len, command);
}

uint64_t cmdtoi(char* cmd) {
    return *((uint64_t*) cmd);
}

void writeCommand(const char* command, uint64_t seq, json_t* data) {
    // [I4 Length of further data][I4 Command][I8 SEQ][data]

    if(!data) {
        data = json_object();
    }

    json_object_set_new(data, "cmd", json_string(command));
    json_object_set_new(data, "seq", json_integer(seq));

    char* serialized = json_dumps(data, JSON_COMPACT);

    int len = 4 + strlen(serialized);
    void* commandBuf = malloc(len);
    memset(commandBuf, 0, len);

    *((uint32_t*) commandBuf) = htobe32(len - 4);
    memcpy(commandBuf + 4, serialized, len - 4);

    write(STDOUT_FILENO, commandBuf, len);

    free(commandBuf);
    free(serialized);
    json_decref(data);
}
