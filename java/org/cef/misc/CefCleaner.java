// Copyright (c) 2024 The Chromium Embedded Framework Authors. All rights
// reserved. Use of this source code is governed by a BSD-style license that
// can be found in the LICENSE file.

package org.cef.misc;

import java.lang.ref.Cleaner;

/**
 * Shared Cleaner for native-backed resources. Replaces deprecated finalization with an explicit,
 * JDK 17-friendly mechanism that performs cleanup exactly once.
 */
public final class CefCleaner {
    private static final Cleaner CLEANER = Cleaner.create();
    private static final Cleaner.Cleanable NOOP = new Cleaner.Cleanable() {
        @Override
        public void clean() {}
    };

    private CefCleaner() {}

    public static Cleaner.Cleanable register(Object target, Runnable action) {
        return CLEANER.register(target, action);
    }

    public static Cleaner.Cleanable noop() {
        return NOOP;
    }
}
