// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.callback;

import org.cef.misc.NativeCleanup;

import java.util.concurrent.atomic.AtomicBoolean;

class CefJSDialogCallback_N extends CefNativeAdapter implements CefJSDialogCallback {
    private static final class NativeDisposer {
        private static final CefJSDialogCallback_N INVOKER = new CefJSDialogCallback_N(false);

        private static void dispose(long handle) {
            if (handle != 0) {
                INVOKER.continueHandle(handle, false, "");
            }
        }
    }

    private final AtomicBoolean resolved = new AtomicBoolean(false);

    CefJSDialogCallback_N() {
        super(new NativeCleanup(NativeDisposer::dispose));
    }

    private CefJSDialogCallback_N(boolean registerCleaner) {
        super(new NativeCleanup(NativeDisposer::dispose), registerCleaner);
    }

    @Override
    public void Continue(boolean success, String user_input) {
        if (resolved.compareAndSet(false, true)) {
            continueInternal(success, user_input);
            getCleanup().markCleaned();
        }
        getCleanup().clean();
    }

    private void continueInternal(boolean success, String user_input) {
        continueHandle(getNativeRef(null), success, user_input);
    }

    private void continueHandle(long handle, boolean success, String user_input) {
        try {
            N_Continue(handle, success, user_input);
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    private final native void N_Continue(long self, boolean success, String user_input);
}
