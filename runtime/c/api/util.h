/**
 * util.h: C utilities
 *
 */

#ifndef API_UTIL_H
#define API_UTIL_H

#define container_of(ptr, type, member) ({				\
    const __typeof__( ((type *)0)->member ) *__mptr = (ptr);	\
    (type *)( (char *)__mptr - offsetof(type,member) );})

#endif /* API_UTIL_H */