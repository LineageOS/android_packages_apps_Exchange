# Copyright 2008, The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)

#
# Exchange2
#
LOCAL_MODULE_TAGS := optional

# Include res dir from emailcommon
emailcommon_dir := ../Email/emailcommon
res_dir := res $(emailcommon_dir)/res

LOCAL_RESOURCE_DIR := $(addprefix $(LOCAL_PATH)/, $(res_dir))

LOCAL_AAPT_FLAGS := --auto-add-overlay
LOCAL_AAPT_FLAGS += --extra-packages com.android.emailcommon

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_STATIC_ANDROID_LIBRARIES := androidx.core_core
LOCAL_STATIC_JAVA_LIBRARIES := android-common com.android.emailcommon
LOCAL_STATIC_JAVA_LIBRARIES += calendar-common

LOCAL_PACKAGE_NAME := Exchange2
LOCAL_OVERRIDES_PACKAGES := Exchange

LOCAL_PRODUCT_MODULE := true

LOCAL_PROGUARD_FLAG_FILES := proguard.flags
LOCAL_SDK_VERSION := 19

LOCAL_JACK_COVERAGE_INCLUDE_FILTER += com.android.exchange.*

LOCAL_REQUIRED_MODULES += default_permissions_com.android.exchange.xml

include $(BUILD_PACKAGE)

include $(CLEAR_VARS)
LOCAL_MODULE := default_permissions_com.android.exchange.xml
LOCAL_MODULE_CLASS := ETC
LOCAL_MODULE_TAGS := optional
LOCAL_MODULE_PATH := $(TARGET_OUT_PRODUCT_ETC)/default-permissions
LOCAL_PRODUCT_MODULE := true
LOCAL_SRC_FILES := $(LOCAL_MODULE)
include $(BUILD_PREBUILT)

# additionally, build unit tests in a separate .apk
include $(call all-makefiles-under,$(LOCAL_PATH))
