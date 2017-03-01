all: libravel.a

C_RUNTIME_SOURCES = \
    ravel-context.c \
    ravel-ringbuf.c \
    ravel-base-model.c \
    ravel-base-dispatcher.c \
    ravel-system-api.c \
    ravel-packet.c \
    ravel-growable-byte-array.c \
    ravel-array.c \
    ravel-key-provider.c \
    ravel-crypto-wolfssl.c

PLATFORM_RUNTIME_SOURCES = \
    ravel-driver.c

RAVEL_C_RUNTIME_DIR = ../c/

OBJS := $(patsubst %.c,%.o,$(addprefix $(RAVEL_C_RUNTIME_DIR),$(C_RUNTIME_SOURCES)) $(PLATFORM_RUNTIME_SOURCES))

CFLAGS ?= -O2 -g

WOLFSSL_CFLAGS := $(shell pkg-config --cflags wolfssl)

clean:
	rm -f libravel.a $(OBJS)

.c.o:
	$(CC) $(CPPFLAGS) $(CFLAGS) $(WOLFSSL_CFLAGS) -Wall -Werror=implicit-function-declaration -Werror=return-type -I$(RAVEL_C_RUNTIME_DIR) -I. -c -o $@ $<

libravel.a: $(OBJS)
	$(AR) crs -o $@ $^
