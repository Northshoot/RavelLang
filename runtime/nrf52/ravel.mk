C_RUNTIME_SOURCES  := $(wildcard $(RAVEL_C_RUNTIME_DIR)/*.c)

PLATFORM_RUNTIME_SOURCES   += \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_ble_core.c \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_ble_rad.c \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_counter.c \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_crypto.c \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_network.c \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_print.c \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_ravel_timer.c \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_ravel_packet_queue.c \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_ble_interface.c \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/nrf52-driver.c
RPI := $(RAVEL_PLATFORM_RUNTIME_DIR)/platform

PROJECT_SOURCEFILES := $(C_RUNTIME_SOURCES) $(PLATFORM_RUNTIME_SOURCES)
PROJECTDIRS += $(RAVEL_C_RUNTIME_DIR) $(RAVEL_PLATFORM_RUNTIME_DIR) $(RPI) $(RAVEL_C_RUNTIME_DIR)/api $(RAVEL_C_RUNTIME_DIR)



# Source files common to all targets
SRC_FILES += \
  $(SRC_APP) \
  $(PROJECT_SOURCEFILES) \

# Include folders common to all targets
INC_FOLDERS += \
  $(APP_DIR) \
  $(APP_FILE_DIR) \
  $(PROJECTDIRS) \
  $(RAVEL_PLATFORM_RUNTIME_DIR)/ravel \


#would be in base make for deployment
# CFLAGS +=  -Wall  -O3 -g3
# need to disable some errors due to how IR is generated -Werror