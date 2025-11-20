// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.callback;

import org.cef.misc.NativeCleanup;

class CefDownloadItemCallback_N extends CefNativeAdapter implements CefDownloadItemCallback {
    private static final class NativeDisposer {
        private static final CefDownloadItemCallback_N INVOKER =
                new CefDownloadItemCallback_N(false);

        private static void dispose(long handle) {
            if (handle != 0) {
                INVOKER.disposeHandle(handle);
            }
        }
    }

    CefDownloadItemCallback_N() {
        super(new NativeCleanup(NativeDisposer::dispose));
    }

    private CefDownloadItemCallback_N(boolean registerCleaner) {
        super(new NativeCleanup(NativeDisposer::dispose), registerCleaner);
    }

    public void dispose() {
        if (getCleanup() != null) getCleanup().clean();
    }

    @Override
    public void cancel() {
        try {
            N_Cancel(getNativeRef(null));
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    @Override
    public void pause() {
        try {
            N_Pause(getNativeRef(null));
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    @Override
    public void resume() {
        try {
            N_Resume(getNativeRef(null));
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    private void disposeHandle(long handle) {
        try {
            N_Dispose(handle);
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    private final native void N_Dispose(long self);
    private final native void N_Cancel(long self);
    private final native void N_Pause(long self);
    private final native void N_Resume(long self);
}
