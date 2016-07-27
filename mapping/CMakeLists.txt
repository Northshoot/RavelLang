cmake_minimum_required(VERSION 3.5)
project(mapping)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES
    main.cpp)

add_executable(mapping ${SOURCE_FILES})