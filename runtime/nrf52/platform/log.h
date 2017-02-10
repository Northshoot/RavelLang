#ifndef LOG_H_
#define LOG_H_

#include "SEGGER_RTT.h"

#ifdef DEBUG_NRF_USER
#define LOG(msg, ...) (SEGGER_RTT_printf(0, "%s:%d - " msg "\n", __FILE__, __LINE__, ##__VA_ARGS__))
#else
#define LOG(msg, ...)
#endif

#endif