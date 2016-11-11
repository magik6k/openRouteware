#include <bencodetools/bencode.h>
#include <stdint.h>
#include <jansson.h>

/* Command format
 * [I4 Length of further data][I4 Command][I8 SEQ][data]
 *
 * Command registry
 * [usage class][subsystem][specific][specific]
 *
 * Usage classes:
 * 0 -> All/generic
 * 1 -> To netadmin
 * 2 -> From netadmin(incl response)
 *
 * Generic subsystems:
 * 0 - 32 -> Generic commands, specific extends command
 *
 * Generic/generic commands:
 * 0.0.0 -> Ping request
 * 0.0.1 -> Ping response
 */

#define CMD_PING_REQUEST        "generic.ping.request"
#define CMD_PING_RESPONSE       "generic.ping.response"
#define CMD_ERROR               "generic.error" // [Int: len][desc]

#define CMD_GET_INTERFACE_LIST  "link.get"

#define CMD_INTERFACE_LIST      "link.get.response"

uint64_t cmdtoi(char* cmd);
void writeCommand(const char* command, uint64_t seq, json_t* data);

void command_ping           (json_t* command, uint64_t seq);
void command_get_interfaces (json_t* command, uint64_t seq);


