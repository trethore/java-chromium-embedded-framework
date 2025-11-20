// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.callback;

import java.util.concurrent.atomic.AtomicBoolean;

class CefBeforeDownloadCallback_N extends CefNativeAdapter implements CefBeforeDownloadCallback {
    private static final class NativeDisposer {
        private static final CefBeforeDownloadCallback_N INVOKER =
                new CefBeforeDownloadCallback_N(false);

        private static void dispose(long handle) {
            if (handle != 0) {
                INVOKER.continueHandle(handle, "", false);
            }
        }
    }

    private final AtomicBoolean completed = new AtomicBoolean(false);

    CefBeforeDownloadCallback_N() {
        super(new org.cef.misc.NativeCleanup(NativeDisposer::dispose));
    }

    private CefBeforeDownloadCallback_N(boolean registerCleaner) {
        super(new org.cef.misc.NativeCleanup(NativeDisposer::dispose), registerCleaner);
    }

    @Override
    public void Continue(String downloadPath, boolean showDialog) {
        if (completed.compareAndSet(false, true)) {
            continueInternal(downloadPath, showDialog);
            getCleanup().markCleaned();
        }
        getCleanup().clean();
    }

    private void continueInternal(String downloadPath, boolean showDialog) {
        continueHandle(getNativeRef(null), downloadPath, showDialog);
    }

    private void continueHandle(long handle, String downloadPath, boolean showDialog) {
        try {
            N_Continue(handle, downloadPath, showDialog);
        } catch (UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }

    private final native void N_Continue(long self, String downloadPath, boolean showDialog);
}
