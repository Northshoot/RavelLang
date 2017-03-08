cmake_minimum_required(VERSION 3.6)
project(c)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES
        api/array.h
        api/base_dispatcher.h
        api/base_model.h
        api/context.h
        api/crypto.h
        api/driver.h
        api/endpoint.h
        api/growable_byte_array.h
        api/intrinsics.h
        api/keys.h
        api/packet.h
        api/ravel_frame.h
        api/ringbuf.h
        api/system.h
        api/util.h
        ravel-array.c
        ravel-base-dispatcher.c
        ravel-base-model.c
        ravel-context.c
        ravel-crypto-wolfssl.c
        ravel-growable-byte-array.c
        ravel-key-provider.c
        ravel-packet.c
        ravel-ringbuf.c
        ravel-system-api.c)

add_executable(c ${SOURCE_FILES})