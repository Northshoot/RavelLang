C_RUNTIME_SOURCES = \
    ravel-context.c \
    ravel-ringbuf.c \
    ravel-base-model.c

PLATFORM_RUNTIME_SOURCES = \
    ravel-driver.c

PROJECT_SOURCEFILES += $(C_RUNTIME_SOURCES) $(PLATFORM_RUNTIME_SOURCES)

PROJECTDIRS += $(RAVEL_C_RUNTIME_DIR) $(RAVEL_PLATFORM_RUNTIME_DIR)