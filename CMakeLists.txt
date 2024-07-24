cmake_minimum_required(VERSION 3.4.1)

set (WITH_OPENGL "epoxy")
set (WITH_SDL "2")

string(TOLOWER ${CMAKE_BUILD_TYPE} ANDROID_BUILD_DIR)

set(FREETYPE_LOCATION ${CMAKE_SOURCE_DIR}/freetype2)
set(FREETYPE_INCLUDE_DIRS ${FREETYPE_LOCATION}/src/main/jni/freetype2/include)
set(FREETYPE_LIBRARY ${FREETYPE_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libfreetype.so)

set(EPOXY_LOCATION ${CMAKE_SOURCE_DIR}/libepoxy)
set(Epoxy_INCLUDE_DIR ${EPOXY_LOCATION}/src/main/include)
set(Epoxy_LIBRARY ${EPOXY_LOCATION}/src/main/jniLibs/${ANDROID_ABI}/libepoxy.so)

add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/ArxLibertatis")