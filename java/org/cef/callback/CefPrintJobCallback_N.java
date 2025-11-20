// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.callback;

import org.cef.misc.NativeCleanup;

import java.util.concurrent.atomic.AtomicBoolean;

class CefPrintJobCallback_N extends CefNativeAdapter implements CefPrintJobCallback {
    private static final class NativeDisposer {
        private static final CefPrintJobCallback_N INVOKER = new CefPrintJobCallback_N(false);

        private static void dispose(long handle) {
            if (handle != 0) {
                INVOKER.continueHandle(handle);
            }
        }
    }

    private final AtomicBoolean resolved = new AtomicBoolean(false);

    CefPrintJobCallback_N() {
        super(new NativeCleanup(NativeDisposer::dispose));
    }

    private CefPrintJobCallback_N(boolean registerCleaner) {
        super(new NativeCleanup(NativeDisposer::dispose), registerCleaner);
    }

    @Override
    public void Continue() {
        if (resolved.compareAndSet(false, true)) {
            continueInternal();
            getCleanup().markCleaned();
        }
        getCleanup().clean();
    }

    private void continueInternal() {
        continueHandle(getNativeRef(null));
    }

    private void continueHandle(long handle) {
        try {
            N_Continue(handle);
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    private final native void N_Continue(long self);
}
