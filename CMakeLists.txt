cmake_minimum_required(VERSION 3.4.1)

set (WITH_OPENGL "epoxy")
set (WITH_SDL "2")
set (BUILD_TOOLS OFF)
set (INSTALL_BLENDER_PLUGIN OFF)

set(FREETYPE_LOCATION ${CMAKE_SOURCE_DIR}/freetype2)
set(FREETYPE_INCLUDE_DIRS ${FREETYPE_LOCATION}/src/main/jni/freetype2/include)

set(FREETYPE_LIBRARY ${FREETYPE_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libfreetype.so)

set(EPOXY_LOCATION ${CMAKE_SOURCE_DIR}/libepoxy)
set(Epoxy_INCLUDE_DIR ${EPOXY_LOCATION}/src/main/include)
set(Epoxy_LIBRARY ${EPOXY_LOCATION}/src/main/jniLibs/${ANDROID_ABI}/libepoxy.so)

set(OPENAL_LOCATION ${CMAKE_SOURCE_DIR}/libopenal)
set(OPENAL_INCLUDE_DIR ${OPENAL_LOCATION}/src/main/jni/openal-soft/include/AL)
set(OPENAL_EFX_INCLUDE_DIR ${OPENAL_LOCATION}/src/main/jni/openal-soft/include/AL)
set(OPENAL_LIBRARY ${OPENAL_LOCATION}/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libopenal.so)

set(SDL_LOCATION ${CMAKE_SOURCE_DIR}/sdl2)
set(SDL2_INCLUDE_DIR ${SDL_LOCATION}/include)
set(SDL2_LIBRARY ${SDL_LOCATION}/android-project/app/build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/${ANDROID_ABI}/libSDL2.so)

set(Boost_INCLUDE_DIR ${CMAKE_SOURCE_DIR}/app/src/main/jni/include)

set(GLM_INCLUDE_DIR ${CMAKE_SOURCE_DIR}/glm/src/main/jni/glm)

add_subdirectory("${CMAKE_SOURCE_DIR}/app/src/main/jni/ArxLibertatis")