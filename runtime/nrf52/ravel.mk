C_RUNTIME_SOURCES  := $(wildcard $(LOCAL_PATH)/*.c)
C_RUNTIME_SOURCES  := $(C_RUNTIME_SOURCES:$(LOCAL_PATH)/%=%)

PLATFORM_RUNTIME_SOURCES   := $(wildcard $(LOCAL_PATH)/platform/*.c)
PLATFORM_RUNTIME_SOURCES   := $(CLASSES_FILES:$(LOCAL_PATH)/%=%)


PROJECT_SOURCEFILES += $(C_RUNTIME_SOURCES) $(PLATFORM_RUNTIME_SOURCES)

PROJECTDIRS += $(RAVEL_C_RUNTIME_DIR) $(RAVEL_PLATFORM_RUNTIME_DIR)

include $(NRF52)/Makefile.include