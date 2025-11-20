// Copyright (c) 2024 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.browser;

import org.cef.misc.CefCleaner;
import org.cef.misc.NativeCleanup;

import java.lang.ref.Cleaner.Cleanable;

/**
 * Used internally by {@link CefDevToolsClient}.
 * <p>
 * Handle to observer registration, As long as this object is alive, the observer will stay
 * registered.
 */
abstract class CefRegistration implements AutoCloseable {
    private final NativeCleanup cleanup;
    private final Cleanable cleanable;

    CefRegistration() {
        this(new NativeCleanup(handle -> {}));
    }

    CefRegistration(NativeCleanup cleanup) {
        this(cleanup, true);
    }

    CefRegistration(NativeCleanup cleanup, boolean registerCleaner) {
        this.cleanup = cleanup;
        cleanable = registerCleaner ? CefCleaner.register(this, cleanup) : CefCleaner.noop();
    }
    /**
     * Removes the native reference from an unused object.
     */
    public final void dispose() {
        cleanable.clean();
    }

    @Override
    public final void close() {
        cleanable.clean();
    }

    protected final NativeCleanup getCleanup() {
        return cleanup;
    }
}
