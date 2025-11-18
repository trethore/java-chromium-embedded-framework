// Copyright (c) 2025 The Chromium Embedded Framework Authors.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#ifndef JCEF_NATIVE_AUDIO_HANDLER_H_
#define JCEF_NATIVE_AUDIO_HANDLER_H_
#pragma once

#include <jni.h>
#include <vector>

#include "include/cef_audio_handler.h"
#include "jni_scoped_helpers.h"

// AudioHandler bridges CefAudioHandler callbacks into Java.
class AudioHandler : public CefAudioHandler {
 public:
  AudioHandler(JNIEnv* env, jobject handler);

  bool GetAudioParameters(CefRefPtr<CefBrowser> browser,
                          CefAudioParameters& params) override;
  void OnAudioStreamStarted(CefRefPtr<CefBrowser> browser,
                            const CefAudioParameters& params,
                            int channels) override;
  void OnAudioStreamPacket(CefRefPtr<CefBrowser> browser,
                           const float** data,
                           int frames,
                           int64_t pts) override;
  void OnAudioStreamStopped(CefRefPtr<CefBrowser> browser) override;
  void OnAudioStreamError(CefRefPtr<CefBrowser> browser,
                          const CefString& text) override;

 protected:
  ScopedJNIObjectGlobal handle_;
  int channels_ = 0;
  std::vector<float> interleaved_;

  IMPLEMENT_REFCOUNTING(AudioHandler);
};

#endif  // JCEF_NATIVE_AUDIO_HANDLER_H_
