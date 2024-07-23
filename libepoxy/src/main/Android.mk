LOCAL_PATH := $(call my-dir)
LOCAL_MODULE := epoxy-prebuilt
LOCAL_SRC_FILES := $(LOCAL_PATH)/jniLibs/$(TARGET_ARCH_ABI)/libepoxy.so
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/include
include $(PREBUILT_SHARED_LIBRARY)