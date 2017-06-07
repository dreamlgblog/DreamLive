
cmake_minimum_required(VERSION 3.4.1)

set(lib_src_DIR ${CMAKE_SOURCE_DIR}/src/main/cpp)
include_directories(
    ${CMAKE_SOURCE_DIR}/src/main/cpp/faac/include
    ${CMAKE_SOURCE_DIR}/src/main/cpp/x264/include
    ${CMAKE_SOURCE_DIR}/src/main/cpp/rtmpdump/include
)

add_library(faac SHARED IMPORTED)
set_target_properties(faac PROPERTIES IMPORTED_LOCATION
        ${lib_src_DIR}/faac/libfaac.a
)
add_library(rtmpdump SHARED IMPORTED)
set_target_properties(rtmpdump PROPERTIES IMPORTED_LOCATION
        ${lib_src_DIR}/rtmpdump/librtmp.a
)
add_library(x264 SHARED IMPORTED)
set_target_properties(x264 PROPERTIES IMPORTED_LOCATION
        ${lib_src_DIR}/x264/libx264.a
)

add_library(
            dn_live
             SHARED
             src/main/cpp/dn_live.c
             src/main/cpp/queue.c )

find_library(
              log-lib

              log )

target_link_libraries(
                       dn_live
                       x264
                       faac
                       rtmpdump
                       ${log-lib} )
