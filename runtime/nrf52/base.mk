# Source files common to all targets
SRC_FILES += \
  $(SDK_ROOT)/components/libraries/scheduler/app_scheduler.c \
  $(SDK_ROOT)/components/libraries/log/src/nrf_log_backend_serial.c \
  $(SDK_ROOT)/components/libraries/log/src/nrf_log_frontend.c \
  $(SDK_ROOT)/components/libraries/button/app_button.c \
  $(SDK_ROOT)/components/libraries/util/app_error.c \
  $(SDK_ROOT)/components/libraries/util/app_error_weak.c \
  $(SDK_ROOT)/components/libraries/fifo/app_fifo.c \
  $(SDK_ROOT)/components/libraries/timer/app_timer.c \
  $(SDK_ROOT)/components/drivers_nrf/rtc/nrf_drv_rtc.c \
  $(SDK_ROOT)/components/libraries/uart/app_uart_fifo.c \
  $(SDK_ROOT)/components/libraries/util/app_util_platform.c \
  $(SDK_ROOT)/components/libraries/fstorage/fstorage.c \
  $(SDK_ROOT)/components/libraries/hardfault/hardfault_implementation.c \
  $(SDK_ROOT)/components/libraries/util/nrf_assert.c \
  $(SDK_ROOT)/components/libraries/strerror/nrf_strerror.c \
  $(SDK_ROOT)/components/libraries/uart/retarget.c \
  $(SDK_ROOT)/components/ble/nrf_ble_gatt/nrf_ble_gatt.c \
  $(SDK_ROOT)/components/libraries/queue/nrf_queue.c \
  $(SDK_ROOT)/components/boards/boards.c \
  $(SDK_ROOT)/components/drivers_nrf/clock/nrf_drv_clock.c \
  $(SDK_ROOT)/components/drivers_nrf/common/nrf_drv_common.c \
  $(SDK_ROOT)/components/drivers_nrf/gpiote/nrf_drv_gpiote.c \
  $(SDK_ROOT)/components/drivers_nrf/uart/nrf_drv_uart.c \
  $(SDK_ROOT)/components/drivers_nrf/rng/nrf_drv_rng.c \
  $(SDK_ROOT)/components/libraries/bsp/bsp.c \
  $(SDK_ROOT)/components/libraries/bsp/bsp_btn_ble.c \
  $(SDK_ROOT)/components/libraries/bsp/bsp_nfc.c \
  $(SDK_ROOT)/components/libraries/scheduler/app_scheduler.c \
  $(SDK_ROOT)/components/ble/common/ble_advdata.c \
  $(SDK_ROOT)/components/ble/ble_advertising/ble_advertising.c \
  $(SDK_ROOT)/components/ble/common/ble_conn_params.c \
  $(SDK_ROOT)/components/ble/common/ble_srv_common.c \
  $(SDK_ROOT)/components/toolchain/gcc/gcc_startup_nrf52.S \
  $(SDK_ROOT)/components/toolchain/system_nrf52.c \
  $(SDK_ROOT)/components/softdevice/common/softdevice_handler/softdevice_handler.c \
  $(SDK_ROOT)/components/libraries/sha256/sha256.c \
  $(SDK_ROOT)/external/segger_rtt/RTT_Syscalls_GCC.c \
  $(SDK_ROOT)/external/segger_rtt/SEGGER_RTT.c \
  $(SDK_ROOT)/external/segger_rtt/SEGGER_RTT_printf.c \


# Include folders common to all targets
INC_FOLDERS += \
  $(SDK_ROOT) \
  $(SDK_ROOT)/components/libraries/strerror \
  $(SDK_ROOT)/components/ble/nrf_ble_gatt \
  $(SDK_ROOT)/components/libraries/scheduler \
  $(SDK_ROOT)/components/drivers_nrf/comp \
  $(SDK_ROOT)/components/drivers_nrf/twi_master \
  $(SDK_ROOT)/components/libraries/pwm \
  $(SDK_ROOT)/components/softdevice/s132/headers/nrf52 \
  $(SDK_ROOT)/components/libraries/usbd/class/cdc/acm \
  $(SDK_ROOT)/components/libraries/usbd/class/hid/generic \
  $(SDK_ROOT)/components/libraries/usbd/class/msc \
  $(SDK_ROOT)/components/libraries/usbd/class/hid \
  $(SDK_ROOT)/components/libraries/log \
  $(SDK_ROOT)/components/libraries/fstorage \
  $(SDK_ROOT)/components/drivers_nrf/i2s \
  $(SDK_ROOT)/components/libraries/gpiote \
  $(SDK_ROOT)/components/drivers_nrf/gpiote \
  $(SDK_ROOT)/components/libraries/fifo \
  $(SDK_ROOT)/components/boards \
  $(SDK_ROOT)/components/drivers_nrf/common \
  $(SDK_ROOT)/components/ble/ble_advertising \
  $(SDK_ROOT)/components/libraries/queue \
  $(SDK_ROOT)/components/drivers_nrf/saadc \
  $(SDK_ROOT)/components/ble/ble_dtm \
  $(SDK_ROOT)/components/toolchain/cmsis/include \
  $(SDK_ROOT)/components/drivers_nrf/uart \
  $(SDK_ROOT)/components/ble/common \
  $(SDK_ROOT)/components/drivers_nrf/wdt \
  $(SDK_ROOT)/components/libraries/bsp \
  $(SDK_ROOT)/components/libraries/experimental_section_vars \
  $(SDK_ROOT)/components/softdevice/s132/headers \
  $(SDK_ROOT)/components/libraries/slip \
  $(SDK_ROOT)/components/libraries/mem_manager \
  $(SDK_ROOT)/components/libraries/csense_drv \
  $(SDK_ROOT)/components/drivers_nrf/hal \
  $(SDK_ROOT)/components/drivers_nrf/rtc \
  $(SDK_ROOT)/components/drivers_nrf/ppi \
  $(SDK_ROOT)/components/ble/ble_services/ble_dfu \
  $(SDK_ROOT)/components/drivers_nrf/twis_slave \
  $(SDK_ROOT)/components \
  $(SDK_ROOT)/components/libraries/scheduler \
  $(SDK_ROOT)/components/drivers_nrf/delay \
  $(SDK_ROOT)/components/libraries/crc16 \
  $(SDK_ROOT)/components/drivers_nrf/timer \
  $(SDK_ROOT)/components/libraries/util \
  $(SDK_ROOT)/components/drivers_nrf/pwm \
  $(SDK_ROOT)/components/libraries/usbd/class/cdc \
  $(SDK_ROOT)/components/libraries/csense \
  $(SDK_ROOT)/components/drivers_nrf/rng \
  $(SDK_ROOT)/components/libraries/low_power_pwm \
  $(SDK_ROOT)/components/libraries/hardfault \
  $(SDK_ROOT)/components/libraries/uart \
  $(SDK_ROOT)/components/libraries/hci \
  $(SDK_ROOT)/components/drivers_nrf/spi_slave \
  $(SDK_ROOT)/components/drivers_nrf/lpcomp \
  $(SDK_ROOT)/components/libraries/timer \
  $(SDK_ROOT)/components/drivers_nrf/power \
  $(SDK_ROOT)/components/libraries/usbd/config \
  $(SDK_ROOT)/components/toolchain \
  $(SDK_ROOT)/components/drivers_nrf/qdec \
  $(SDK_ROOT)/components/drivers_nrf/spi_master \
  $(SDK_ROOT)/components/drivers_nrf/pdm \
  $(SDK_ROOT)/components/libraries/crc32 \
  $(SDK_ROOT)/components/libraries/usbd/class/audio \
  $(SDK_ROOT)/components/ble/peer_manager \
  $(SDK_ROOT)/components/drivers_nrf/swi \
  $(SDK_ROOT)/components/device \
  $(SDK_ROOT)/components/ble/nrf_ble_qwr \
  $(SDK_ROOT)/components/libraries/button \
  $(SDK_ROOT)/components/libraries/usbd \
  $(SDK_ROOT)/components/ble/ble_racp \
  $(SDK_ROOT)/components/toolchain/gcc \
  $(SDK_ROOT)/components/libraries/fds \
  $(SDK_ROOT)/components/libraries/twi \
  $(SDK_ROOT)/components/drivers_nrf/clock \
  $(SDK_ROOT)/components/drivers_nrf/usbd \
  $(SDK_ROOT)/components/softdevice/common/softdevice_handler \
  $(SDK_ROOT)/components/libraries/log/src \
  $(SDK_ROOT)/components/libraries/sha256 \
  $(SDK_ROOT)/external/segger_rtt \


# Libraries common to all targets
LIB_FILES += \

# C flags common to all targets
CFLAGS += -DBLE_STACK_SUPPORT_REQD
CFLAGS += -DBOARD_PCA10040
CFLAGS += -DCONFIG_GPIO_AS_PINRESET
CFLAGS += -DNRF52
CFLAGS += -DNRF52832_XXAA
CFLAGS += -DNRF52_PAN_12
CFLAGS += -DNRF52_PAN_15
CFLAGS += -DNRF52_PAN_20
CFLAGS += -DNRF52_PAN_31
CFLAGS += -DNRF52_PAN_36
CFLAGS += -DNRF52_PAN_51
CFLAGS += -DNRF52_PAN_54
CFLAGS += -DNRF52_PAN_55
CFLAGS += -DNRF52_PAN_58
CFLAGS += -DNRF52_PAN_64
CFLAGS += -DNRF52_PAN_74
CFLAGS += -DNRF_SD_BLE_API_VERSION=4
CFLAGS += -DS132
CFLAGS += -DSOFTDEVICE_PRESENT
CFLAGS += -DSWI_DISABLE0
CFLAGS += -mcpu=cortex-m4
CFLAGS += -mthumb -mabi=aapcs
CFLAGS +=  -Wall  -O3
# need to disable some errors due to how IR is generated -Werror
CFLAGS += -mfloat-abi=hard -mfpu=fpv4-sp-d16
# keep every function in separate section, this allows linker to discard unused ones
CFLAGS += -ffunction-sections -fdata-sections -fno-strict-aliasing
CFLAGS += -fno-builtin --short-enums
CFLAGS += -std=gnu11

# C++ flags common to all targets
CXXFLAGS += \

# Assembler flags common to all targets
ASMFLAGS += -x assembler-with-cpp
ASMFLAGS += -DBLE_STACK_SUPPORT_REQD
ASMFLAGS += -DBOARD_PCA10040
ASMFLAGS += -DCONFIG_GPIO_AS_PINRESET
ASMFLAGS += -DNRF52
ASMFLAGS += -DNRF52832_XXAA
ASMFLAGS += -DNRF52_PAN_12
ASMFLAGS += -DNRF52_PAN_15
ASMFLAGS += -DNRF52_PAN_20
ASMFLAGS += -DNRF52_PAN_31
ASMFLAGS += -DNRF52_PAN_36
ASMFLAGS += -DNRF52_PAN_51
ASMFLAGS += -DNRF52_PAN_54
ASMFLAGS += -DNRF52_PAN_55
ASMFLAGS += -DNRF52_PAN_58
ASMFLAGS += -DNRF52_PAN_64
ASMFLAGS += -DNRF52_PAN_74
ASMFLAGS += -DNRF_SD_BLE_API_VERSION=4
ASMFLAGS += -DS132
ASMFLAGS += -DSOFTDEVICE_PRESENT
ASMFLAGS += -DSWI_DISABLE0

# Linker flags
LDFLAGS += -mthumb -mabi=aapcs -L $(TEMPLATE_PATH) -T$(LINKER_SCRIPT)
LDFLAGS += -mcpu=cortex-m4
LDFLAGS += -mfloat-abi=hard -mfpu=fpv4-sp-d16
# let linker to dump unused sections
LDFLAGS += -Wl,--gc-sections
# use newlib in nano version
LDFLAGS += --specs=nano.specs -lc -lnosys


.PHONY: $(TARGETS) default all clean help flash flash_softdevice



# Print all targets that can be built
help:
	@echo following targets are available:
	@echo 	$(TARGETS)

TEMPLATE_PATH := $(SDK_ROOT)/components/toolchain/gcc

# Copyright (c) 2016 Nordic Semiconductor. All Rights Reserved.
#
# The information contained herein is property of Nordic Semiconductor ASA.
# Terms and conditions of usage are described in detail in NORDIC
# SEMICONDUCTOR STANDARD SOFTWARE LICENSE AGREEMENT.
#
# Licensees are granted free, non-transferable use of the information. NO
# WARRANTY of ANY KIND is provided. This heading must NOT be removed from
# the file.

#PLATFORM_SUFFIX := $(if $(filter Windows%,$(OS)),windows,posix)
#TOOLCHAIN_CONFIG_FILE := $(TEMPLATE_PATH)/Makefile.$(PLATFORM_SUFFIX)
include $(TOOLCHAIN_CONFIG_FILE)

# Toolchain commands
CC      := "$(GNU_INSTALL_ROOT)/bin/$(GNU_PREFIX)-gcc"
CXX     := "$(GNU_INSTALL_ROOT)/bin/$(GNU_PREFIX)-c++"
AS      := "$(GNU_INSTALL_ROOT)/bin/$(GNU_PREFIX)-as"
AR      := "$(GNU_INSTALL_ROOT)/bin/$(GNU_PREFIX)-ar" -r
LD      := "$(GNU_INSTALL_ROOT)/bin/$(GNU_PREFIX)-ld"
NM      := "$(GNU_INSTALL_ROOT)/bin/$(GNU_PREFIX)-nm"
OBJDUMP := "$(GNU_INSTALL_ROOT)/bin/$(GNU_PREFIX)-objdump"
OBJCOPY := "$(GNU_INSTALL_ROOT)/bin/$(GNU_PREFIX)-objcopy"
SIZE    := "$(GNU_INSTALL_ROOT)/bin/$(GNU_PREFIX)-size"
$(if $(shell $(CC) --version),,$(info Cannot find: $(CC).) \
  $(info Please set values in: "$(abspath $(TOOLCHAIN_CONFIG_FILE))") \
  $(info according to the actual configuration of your system.) \
  $(error Cannot continue))

# Use ccache on linux if available
CCACHE := $(if $(filter Windows%,$(OS)),, \
               $(if $(wildcard /usr/bin/ccache),ccache))
CC     := $(CCACHE) $(CC)

MK := mkdir
RM := rm -rf

# echo suspend
ifeq ($(VERBOSE),1)
  NO_ECHO :=
else
  NO_ECHO := @
endif

# $1 type of item
# $2 path to check
define ensure_exists
$(if $(wildcard $(2)),, $(warning Cannot find $(1): $(2)))
endef

# $1 object file
# $2 source file
define bind_obj_with_src
$(eval $(1) := $(2))
endef

# $1 object file
# $2 target name
define bind_obj_with_target
$(eval $(1)T := $(2))
endef

# $1 target name
# $2 source file name
# Note: this additional .o for .s-files is a workaround for issues with make 4.1
#       from MinGW (it does nothing to remake .s.o files when a rule for .S.o
#       files is defined as well).
define get_object_file_name
$(OUTPUT_DIRECTORY)/$(strip $(1))_$(patsubst %.s,%.s.o,$(notdir $(2))).o
endef

# $1 target name
# $2 list of source files
define get_object_files
$(foreach src_file, $(2), \
  $(call ensure_exists,source file, $(src_file)) \
  $(eval obj_file := $(call get_object_file_name, $(1), $(src_file))) \
  $(eval DEPENDENCIES += $(obj_file:.o=.d)) \
  $(call bind_obj_with_src, $(obj_file), $(src_file)) \
  $(call bind_obj_with_target, $(obj_file), $(1)) \
  $(eval $(obj_file): Makefile) \
  $(obj_file))
endef

# $1 variable name
# $2 target name
define target_specific
$($(addsuffix _$(strip $(2)), $(1)))
endef

# $1 target name
# $2 link target name
define prepare_build
$(eval DEPENDENCIES :=) \
$(eval $(2): \
  $(call get_object_files, $(1), $(SRC_FILES) \
    $(call target_specific, SRC_FILES, $(1)))) \
$(eval -include $(DEPENDENCIES)) \
$(eval INC_PATHS_$(strip $(1)) := \
  $(foreach folder, $(INC_FOLDERS) $(call target_specific, INC_FOLDERS, $(1)), \
    $(call ensure_exists,include folder, $(folder)) \
    -I"$(folder)"))
endef

INC_PATHS = $(call target_specific, INC_PATHS, $($@T))

# $1 target name
define define_target
$(eval OUTPUT_FILE := $(OUTPUT_DIRECTORY)/$(strip $(1))) \
$(eval $(1): $(OUTPUT_FILE).out $(OUTPUT_FILE).hex $(OUTPUT_FILE).bin) \
$(call prepare_build, $(1), $(OUTPUT_FILE).out)
endef

# $1 target name
# $2 library file name
define define_library
$(eval $(1) := $(2)) \
$(call prepare_build, $(1), $(1))
endef

.PHONY: $(TARGETS) default all clean help flash

all: $(TARGETS)

clean:
	$(RM) $(OUTPUT_DIRECTORY)

# Create build directories
$(OUTPUT_DIRECTORY):
	$(MK) $@

# Create objects from C source files
$(OUTPUT_DIRECTORY)/%.c.o: | $(OUTPUT_DIRECTORY)
	@echo Compiling file: $(notdir $($@))
	$(NO_ECHO)$(CC) -MP -MD -std=c99 $(CFLAGS) $(INC_PATHS) -c -o $@ "$($@)"

# Create objects from C++ source files
$(OUTPUT_DIRECTORY)/%.cpp.o: | $(OUTPUT_DIRECTORY)
	@echo Compiling file: $(notdir $($@))
	$(NO_ECHO)$(CXX) -MP -MD $(CFLAGS) $(CXXFLAGS) $(INC_PATHS) -c -o $@ "$($@)"

# Create objects from assembly files
$(OUTPUT_DIRECTORY)/%.S.o \
$(OUTPUT_DIRECTORY)/%.s.o.o: | $(OUTPUT_DIRECTORY)
	@echo Assembling file: $(notdir $($@))
	$(NO_ECHO)$(CC) -MP -MD -std=c99 $(ASMFLAGS) $(INC_PATHS) -c -o $@ "$($@)"

export FILE_LIST
DUMP_FILE_LIST := \
  "$(MAKE)" -s --no-print-directory -f $(TEMPLATE_PATH)/file_list.mk
# Link object files
%.out:
	$(eval FILE_LIST := $^ $(LIB_FILES))
	$(NO_ECHO)$(DUMP_FILE_LIST) > $(@:.out=.in)
	@echo Linking target: $@
	$(NO_ECHO)$(CC) -Wl,-Map=$(@:.out=.map) $(LDFLAGS) @$(@:.out=.in) -lm -o $@
	-@echo ''
	$(NO_ECHO)$(SIZE) $@
	-@echo ''

# Create binary .bin file from the .out file
%.bin: %.out
	@echo Preparing: $@
	$(NO_ECHO)$(OBJCOPY) -O binary $< $@

# Create binary .hex file from the .out file
%.hex: %.out
	@echo Preparing: $@
	$(NO_ECHO)$(OBJCOPY) -O ihex $< $@


$(foreach target, $(TARGETS), $(call define_target, $(target)))

# Flash the program
flash: $(OUTPUT_DIRECTORY)/nrf52832_EmbeddedSpace.hex
	@echo Flashing: $<
	nrfjprog --program $< -f nrf52 --sectorerase
	nrfjprog --reset -f nrf52

# Flash softdevice
flash_softdevice_3:
	@echo Flashing: s132_nrf52_3.0.0_softdevice.hex
	nrfjprog --program $(SDK_ROOT)/components/softdevice/s132/hex/s132_nrf52_3.0.0_softdevice.hex -f nrf52 --sectorerase
	nrfjprog --reset -f nrf52

# Flash softdevice
flash_softdevice:
	@echo Flashing: s132_nrf52_4.0.2_softdevice.hex
	nrfjprog --program $(SDK_ROOT)/components/softdevice/s132/hex/s132_nrf52_4.0.2_softdevice.hex -f nrf52 --sectorerase
	nrfjprog --reset -f nrf52

erase:
	nrfjprog --eraseall -f nrf52
