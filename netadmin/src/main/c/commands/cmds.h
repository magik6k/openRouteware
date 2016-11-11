#include <bencodetools/bencode.h>
#include <stdint.h>

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

#define CMD_PING_REQUEST        "\0\0\0\0"
#define CMD_PING_RESPONSE       "\0\0\0\1"
#define CMD_ERROR               "\0\0\0\2" // [Int: len][desc]

#define CMD_GET_INTERFACE_LIST  "\1\0\0\0"

#define CMD_INTERFACE_LIST      "\2\0\0\0"

uint64_t cmdtoi(char* cmd);
void writeCommand(const char* command, uint64_t seq, int datalen, void* data);

void command_ping           (const char* command, int dlen, uint64_t seq);
void command_get_interfaces (const char* command, int dlen, uint64_t seq);


