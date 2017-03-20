APP_DIR:=$(shell dirname $(realpath $(lastword $(MAKEFILE_LIST))))
RAVEL_C_RUNTIME_DIR = /Users/lauril/workspace/01-ravel/RavelLang/./runtime/c
RAVEL_PLATFORM_RUNTIME_DIR = /Users/lauril/workspace/01-ravel/RavelLang/runtime/nrf52

PROJECTDIRS += $(RAVEL_C_RUNTIME_DIR) $(RAVEL_PLATFORM_RUNTIME_DIR) $(RPI) $(RAVEL_C_RUNTIME_DIR)/api $(RAVEL_C_RUNTIME_DIR)

NRF52_PROJECT = nrf52832_EmbeddedSpace


PROJECT_NAME     := nrf52832_EmbeddedSpace
TARGETS          := nrf52832_EmbeddedSpace
OUTPUT_DIRECTORY := _build

SDK_ROOT := $(NRF52SDK)
PROJ_DIR := $(NRF52_PROJECT)

$(OUTPUT_DIRECTORY)/nrf52832_EmbeddedSpace.out: \
  LINKER_SCRIPT  := bas.ld


APP_FILE_DIR =


SRC_FILES += \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_ble_core.c \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_ble_rad.c \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_counter.c \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_crypto.c \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_network.c \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_print.c \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_ravel_timer.c \
    $(RAVEL_PLATFORM_RUNTIME_DIR)/platform/nrf52_ble_interface.c \
    ble_bas.c \
    main.c


# Include folders common to all targets
INC_FOLDERS += \
  $(APP_DIR)/config \
  $(APP_DIR) \
  $(APP_FILE_DIR) \
  $(PROJECTDIRS) \
  $(RAVEL_PLATFORM_RUNTIME_DIR)/ravel \
  $(RAVEL_PLATFORM_RUNTIME_DIR)/platform

CFLAGS += -Wno-unused-but-set-variable
CFLAGS += -DSTAND_ALONE=1

include $(RAVEL_PLATFORM_RUNTIME_DIR)/base.mk