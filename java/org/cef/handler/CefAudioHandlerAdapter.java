// Copyright (c) 2025 The Chromium Embedded Framework Authors.
// BSD-style license; see the LICENSE file.

package org.cef.handler;

import org.cef.browser.CefBrowser;
import org.cef.misc.CefAudioParameters;
import org.cef.misc.DataPointer;

/** Convenience adapter with no-op defaults. */
public abstract class CefAudioHandlerAdapter implements CefAudioHandler {
    @Override
    public boolean getAudioParameters(CefBrowser browser, CefAudioParameters params) {
        return false;
    }

    @Override
    public void onAudioStreamStarted(CefBrowser browser, CefAudioParameters params, int channels) {}

    @Override
    public void onAudioStreamPacket(CefBrowser browser, DataPointer data, int frames, long pts) {}

    @Override
    public void onAudioStreamStopped(CefBrowser browser) {}

    @Override
    public void onAudioStreamError(CefBrowser browser, String text) {}
}
