// Copyright (c) 2025 The Chromium Embedded Framework Authors.
// BSD-style license; see the LICENSE file.

package org.cef.handler;

import org.cef.browser.CefBrowser;
import org.cef.misc.CefAudioParameters;
import org.cef.misc.DataPointer;

/**
 * Handles audio playback callbacks. Called on the UI thread.
 */
public interface CefAudioHandler {
    /**
     * Provide audio stream parameters. Return true to proceed with audio capture.
     */
    boolean getAudioParameters(CefBrowser browser, CefAudioParameters params);

    /** Audio stream started with the given parameters and channel count. */
    void onAudioStreamStarted(CefBrowser browser, CefAudioParameters params, int channels);

    /**
     * Audio packet callback. Data is interleaved float PCM. Use {@link DataPointer#forCapacity}
     * to wrap the native buffer before reading.
     */
    void onAudioStreamPacket(CefBrowser browser, DataPointer data, int frames, long pts);

    /** Stream stopped; release resources. */
    void onAudioStreamStopped(CefBrowser browser);

    /** Called on audio errors. */
    void onAudioStreamError(CefBrowser browser, String text);
}
