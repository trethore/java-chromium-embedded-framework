// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.callback;

import org.cef.misc.NativeCleanup;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

class CefFileDialogCallback_N extends CefNativeAdapter implements CefFileDialogCallback {
    private static final class NativeDisposer {
        private static final CefFileDialogCallback_N INVOKER = new CefFileDialogCallback_N(false);

        private static void dispose(long handle) {
            if (handle != 0) {
                INVOKER.cancelHandle(handle);
            }
        }
    }

    private final AtomicBoolean resolved = new AtomicBoolean(false);

    CefFileDialogCallback_N() {
        super(new NativeCleanup(NativeDisposer::dispose));
    }

    private CefFileDialogCallback_N(boolean registerCleaner) {
        super(new NativeCleanup(NativeDisposer::dispose), registerCleaner);
    }

    @Override
    public void Continue(Vector<String> filePaths) {
        if (resolved.compareAndSet(false, true)) {
            continueInternal(filePaths);
            getCleanup().markCleaned();
        }
        getCleanup().clean();
    }

    @Override
    public void Cancel() {
        if (resolved.compareAndSet(false, true)) {
            cancelInternal();
            getCleanup().markCleaned();
        }
        getCleanup().clean();
    }

    private void continueInternal(Vector<String> filePaths) {
        try {
            N_Continue(getNativeRef(null), filePaths);
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

    private final native void N_Continue(long self, Vector<String> filePaths);
    private final native void N_Cancel(long self);
}
