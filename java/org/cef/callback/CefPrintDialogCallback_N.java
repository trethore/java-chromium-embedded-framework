// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.callback;

import org.cef.misc.NativeCleanup;
import org.cef.misc.CefPrintSettings;

import java.util.concurrent.atomic.AtomicBoolean;

class CefPrintDialogCallback_N extends CefNativeAdapter implements CefPrintDialogCallback {
    private static final class NativeDisposer {
        private static final CefPrintDialogCallback_N INVOKER =
                new CefPrintDialogCallback_N(false);

        private static void dispose(long handle) {
            if (handle != 0) {
                INVOKER.cancelHandle(handle);
            }
        }
    }

    private final AtomicBoolean resolved = new AtomicBoolean(false);

    CefPrintDialogCallback_N() {
        super(new NativeCleanup(NativeDisposer::dispose));
    }

    private CefPrintDialogCallback_N(boolean registerCleaner) {
        super(new NativeCleanup(NativeDisposer::dispose), registerCleaner);
    }

    @Override
    public void Continue(CefPrintSettings settings) {
        if (resolved.compareAndSet(false, true)) {
            continueInternal(settings);
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

    private void continueInternal(CefPrintSettings settings) {
        try {
            N_Continue(getNativeRef(null), settings);
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

    private final native void N_Continue(long self, CefPrintSettings settings);
    private final native void N_Cancel(long self);
}
