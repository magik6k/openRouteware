CC = gcc
CFLAGS?=-O2 -std=c99 -fdata-sections -ffunction-sections -pthread
LDFLAGS+= -O2 -Wl,--gc-sections -Ltarget/lib -pthread

SOURCE = src/main/c

BUILD = target/native/

LIBS = -lnl-3 -lnl-route-3

SRCDIRECTORIES = $(shell find $(SOURCE) -type d)
BUILDDIRECTORIES = $(BUILD) $(patsubst $(SOURCE)/%, $(BUILD)%, $(SRCDIRECTORIES))

CFILES = $(shell find $(SOURCE) -type f -name '*.c')
OBJECTS :=	$(patsubst $(SOURCE)/%.c, $(BUILD)%.c.o, $(CFILES))

INCLUDES = -I$(SOURCE) -Itarget/include

OUTNAME = netadmin

all: $(BUILDDIRECTORIES) $(BUILD)$(OUTNAME)

$(BUILDDIRECTORIES):
	mkdir -p $@

$(BUILD)$(OUTNAME): $(OBJECTS)
	$(CC) $(LDFLAGS) $(OBJECTS) -o $@ $(LIBS)

$(BUILD)%.c.o: $(SOURCE)/%.c
	$(CC) -c $(CFLAGS) $(INCLUDES) $< -o $@

clean: cleanresources
	-rm -rf $(BUILD)
