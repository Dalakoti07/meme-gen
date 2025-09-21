cmake_minimum_required(VERSION 3.22.1)
project(nativefilters)

add_library(nativefilters SHARED
    nativefilters.cpp
    nativefilters_sketch.cpp
    nativefilters_oilpaint.cpp
    nativefilters_watercolor.cpp
    nativefilters_halftone.cpp
)

find_library(log-lib log)
find_library(jnigraphics-lib jnigraphics)

target_link_libraries(nativefilters ${log-lib} ${jnigraphics-lib})


