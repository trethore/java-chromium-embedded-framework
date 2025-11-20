// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.callback;

import org.cef.misc.NativeCleanup;

import java.util.concurrent.atomic.AtomicBoolean;

class CefAuthCallback_N extends CefNativeAdapter implements CefAuthCallback {
    private static final class NativeDisposer {
        private static final CefAuthCallback_N INVOKER = new CefAuthCallback_N(false);

        private static void dispose(long handle) {
            if (handle != 0) {
                INVOKER.cancelHandle(handle);
            }
        }
    }

    private final AtomicBoolean resolved = new AtomicBoolean(false);

    CefAuthCallback_N() {
        super(new NativeCleanup(NativeDisposer::dispose));
    }

    private CefAuthCallback_N(boolean registerCleaner) {
        super(new NativeCleanup(NativeDisposer::dispose), registerCleaner);
    }

    @Override
    public void Continue(String username, String password) {
        if (resolved.compareAndSet(false, true)) {
            continueInternal(username, password);
            getCleanup().markCleaned();
        }
        getCleanup().clean();
    }

    @Override
    public void cancel() {
        if (resolved.compareAndSet(false, true)) {
            cancelInternal();
            getCleanup().markCleaned();
        }
        getCleanup().clean();
    }

    private void continueInternal(String username, String password) {
        try {
            N_Continue(getNativeRef(null), username, password);
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    private void cancelInternal() {
        cancelHandle(getNativeRef(null));
    }

    private void cancelHandle(long handle) {
        try {
            N_Cancel(handle);
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    private final native void N_Continue(long self, String username, String password);
    private final native void N_Cancel(long self);
}
