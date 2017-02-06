all: libravel.a

OBJS = ravel-context.o ravel-ringbuf.o

clean:
	rm -f libravel.a $(OBJS)

.c.o:
	$(CC) $(CPPFLAGS) $(CFLAGS) -Wall -I. -c -o $@ $<

libravel.a: $(OBJS)
	$(AR) crs -o $@ $^
