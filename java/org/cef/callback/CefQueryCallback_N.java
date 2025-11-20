// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.callback;

import org.cef.misc.NativeCleanup;

import java.util.concurrent.atomic.AtomicBoolean;

class CefQueryCallback_N extends CefNativeAdapter implements CefQueryCallback {
    private static final class NativeDisposer {
        private static final CefQueryCallback_N INVOKER = new CefQueryCallback_N(false);

        private static void dispose(long handle) {
            if (handle != 0) {
                INVOKER.failureHandle(handle, -1, "Unexpected call to CefQueryCallback_N cleanup");
            }
        }
    }

    private final AtomicBoolean resolved = new AtomicBoolean(false);

    CefQueryCallback_N() {
        super(new NativeCleanup(NativeDisposer::dispose));
    }

    private CefQueryCallback_N(boolean registerCleaner) {
        super(new NativeCleanup(NativeDisposer::dispose), registerCleaner);
    }

    @Override
    public void success(String response) {
        if (resolved.compareAndSet(false, true)) {
            successInternal(response);
            getCleanup().markCleaned();
        }
        getCleanup().clean();
    }

    @Override
    public void failure(int error_code, String error_message) {
        if (resolved.compareAndSet(false, true)) {
            failureInternal(error_code, error_message);
            getCleanup().markCleaned();
        }
        getCleanup().clean();
    }

    private void successInternal(String response) {
        try {
            N_Success(getNativeRef(null), response);
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    private void failureInternal(int error_code, String error_message) {
        failureHandle(getNativeRef(null), error_code, error_message);
    }

    private void failureHandle(long handle, int error_code, String error_message) {
        try {
            N_Failure(handle, error_code, error_message);
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    private final native void N_Success(long self, String response);
    private final native void N_Failure(long self, int error_code, String error_message);
}
