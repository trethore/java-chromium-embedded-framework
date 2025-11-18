// Copyright (c) 2025 The Chromium Embedded Framework Authors.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

#include "audio_handler.h"

#include "jni_util.h"

AudioHandler::AudioHandler(JNIEnv* env, jobject handler)
    : handle_(env, handler) {}

namespace {

jobject MakeJniParams(ScopedJNIEnv env,
                      jclass paramsClass,
                      const CefAudioParameters& params) {
  jclass layoutClass = env->FindClass("org/cef/misc/CefChannelLayout");
  if (!layoutClass)
    return nullptr;
  jmethodID forId =
      env->GetStaticMethodID(layoutClass, "forId",
                             "(I)Lorg/cef/misc/CefChannelLayout;");
  if (!forId)
    return nullptr;
  jobject layout =
      env->CallStaticObjectMethod(layoutClass, forId, (jint)params.channel_layout);

  jmethodID ctor =
      env->GetMethodID(paramsClass, "<init>",
                       "(Lorg/cef/misc/CefChannelLayout;II)V");
  if (!ctor)
    return nullptr;

  return env->NewObject(paramsClass, ctor, layout, params.sample_rate,
                        params.frames_per_buffer);
}

jobject MakeJniParams(ScopedJNIEnv env, const CefAudioParameters& params) {
  jclass cls = env->FindClass("org/cef/misc/CefAudioParameters");
  return MakeJniParams(env, cls, params);
}

}  // namespace

bool AudioHandler::GetAudioParameters(CefRefPtr<CefBrowser> browser,
                                      CefAudioParameters& params) {
  ScopedJNIEnv env;
  if (!env)
    return true;

  ScopedJNIBrowser jbrowser(env, browser);

  jboolean jreturn = JNI_FALSE;
  jobject paramsJni = MakeJniParams(env, params);

  JNI_CALL_METHOD(env, handle_, "getAudioParameters",
                  "(Lorg/cef/browser/CefBrowser;Lorg/cef/misc/CefAudioParameters;)Z",
                  Boolean, jreturn, jbrowser.get(), paramsJni);

  return (jreturn != JNI_FALSE);
}

void AudioHandler::OnAudioStreamStarted(CefRefPtr<CefBrowser> browser,
                                        const CefAudioParameters& params,
                                        int channels) {
  ScopedJNIEnv env;
  if (!env)
    return;

  ScopedJNIBrowser jbrowser(env, browser);
  jobject paramsJni = MakeJniParams(env, params);

  channels_ = channels;

  JNI_CALL_VOID_METHOD(env, handle_, "onAudioStreamStarted",
                       "(Lorg/cef/browser/CefBrowser;Lorg/cef/misc/CefAudioParameters;I)V",
                       jbrowser.get(), paramsJni, channels);
}

void AudioHandler::OnAudioStreamPacket(CefRefPtr<CefBrowser> browser,
                                       const float** data,
                                       int frames,
                                       int64_t pts) {
  ScopedJNIEnv env;
  if (!env)
    return;

  ScopedJNIBrowser jbrowser(env, browser);

  if (channels_ <= 0)
    return;

  // CEF provides planar float buffers (data[channel][frame]).
  // The Java side expects a single contiguous interleaved buffer.
  const size_t total = static_cast<size_t>(frames) * channels_;
  interleaved_.resize(total);
  for (int c = 0; c < channels_; ++c) {
    const float* src = data[c];
    float* dst = interleaved_.data() + c;
    for (int f = 0; f < frames; ++f, dst += channels_)
      *dst = src[f];
  }

  ScopedJNIObjectLocal dataPtr(
      env, NewJNIObject(env, "org/cef/misc/DataPointer", "(J)V",
                        (jlong)interleaved_.data()));

  JNI_CALL_VOID_METHOD(env, handle_, "onAudioStreamPacket",
                       "(Lorg/cef/browser/CefBrowser;Lorg/cef/misc/DataPointer;IJ)V",
                       jbrowser.get(), dataPtr.get(), frames, (jlong)pts);
}

void AudioHandler::OnAudioStreamStopped(CefRefPtr<CefBrowser> browser) {
  ScopedJNIEnv env;
  if (!env)
    return;

  ScopedJNIBrowser jbrowser(env, browser);

  JNI_CALL_VOID_METHOD(env, handle_, "onAudioStreamStopped",
                       "(Lorg/cef/browser/CefBrowser;)V", jbrowser.get());
}

void AudioHandler::OnAudioStreamError(CefRefPtr<CefBrowser> browser,
                                      const CefString& text) {
  ScopedJNIEnv env;
  if (!env)
    return;

  ScopedJNIBrowser jbrowser(env, browser);
  ScopedJNIString jtext(env, text);

  JNI_CALL_VOID_METHOD(env, handle_, "onAudioStreamError",
                       "(Lorg/cef/browser/CefBrowser;Ljava/lang/String;)V",
                       jbrowser.get(), jtext.get());
}
