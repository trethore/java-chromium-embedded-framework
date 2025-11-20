// Copyright (c) 2024 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.browser;

import org.cef.callback.CefNative;
import org.cef.misc.NativeCleanup;

class CefRegistration_N extends CefRegistration implements CefNative {
    // Used internally to store a pointer to the CEF object.
    private long N_CefHandle = 0;
    private static final class NativeDisposer {
        private static final CefRegistration_N INVOKER = new CefRegistration_N(false);

        private static void dispose(long handle) {
            if (handle != 0) {
                INVOKER.disposeHandle(handle);
            }
        }
    }

    @Override
    public void setNativeRef(String identifier, long nativeRef) {
        N_CefHandle = nativeRef;
        getCleanup().setHandle(nativeRef);
    }

    @Override
    public long getNativeRef(String identifier) {
        return N_CefHandle;
    }

    CefRegistration_N() {
        this(true);
    }

    private CefRegistration_N(boolean registerCleaner) {
        super(new org.cef.misc.NativeCleanup(NativeDisposer::dispose), registerCleaner);
    }

    private void disposeHandle(long handle) {
        try {
            N_Dispose(handle);
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    private final native void N_Dispose(long self);
}
