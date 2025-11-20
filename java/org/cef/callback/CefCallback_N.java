// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.callback;

import org.cef.misc.NativeCleanup;

import java.util.concurrent.atomic.AtomicBoolean;

class CefCallback_N extends CefNativeAdapter implements CefCallback {
    private static final class NativeDisposer {
        private static final CefCallback_N INVOKER = new CefCallback_N(false);

        private static void dispose(long handle) {
            if (handle != 0) {
                INVOKER.cancelHandle(handle);
            }
        }
    }

    private final AtomicBoolean completed = new AtomicBoolean(false);

    CefCallback_N() {
        super(new NativeCleanup(NativeDisposer::dispose));
    }

    private CefCallback_N(boolean registerCleaner) {
        super(new NativeCleanup(NativeDisposer::dispose), registerCleaner);
    }

    @Override
    public void Continue() {
        if (completed.compareAndSet(false, true)) {
            continueInternal();
            if (getCleanup() != null) getCleanup().markCleaned();
        }
        if (getCleanup() != null) getCleanup().clean();
    }

    @Override
    public void cancel() {
        if (completed.compareAndSet(false, true)) {
            cancelInternal();
            if (getCleanup() != null) getCleanup().markCleaned();
        }
        if (getCleanup() != null) getCleanup().clean();
    }

    private void continueInternal() {
        try {
            N_Continue(getNativeRef(null));
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

    private final native void N_Continue(long self);
    private final native void N_Cancel(long self);
}
