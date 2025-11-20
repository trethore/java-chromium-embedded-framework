package org.cef.callback;

import org.cef.misc.CefCleaner;
import org.cef.misc.NativeCleanup;

import java.lang.ref.Cleaner.Cleanable;

public class CefNativeAdapter implements CefNative {
    // Used internally to store a pointer to the CEF object.
    private long N_CefHandle = 0;
    private final NativeCleanup cleanup;
    private final Cleanable cleanable;

    public CefNativeAdapter() {
        this(null);
    }

    protected CefNativeAdapter(NativeCleanup cleanup) {
        this(cleanup, true);
    }

    protected CefNativeAdapter(NativeCleanup cleanup, boolean registerCleaner) {
        this.cleanup = cleanup;
        this.cleanable = (cleanup != null && registerCleaner)
                ? CefCleaner.register(this, cleanup)
                : CefCleaner.noop();
    }

    @Override
    public void setNativeRef(String identifer, long nativeRef) {
        N_CefHandle = nativeRef;
        if (cleanup != null) cleanup.setHandle(nativeRef);
    }

    @Override
    public long getNativeRef(String identifer) {
        return N_CefHandle;
    }

    protected NativeCleanup getCleanup() {
        return cleanup;
    }

    protected void cleanNative() {
        if (cleanable != null) cleanable.clean();
    }
}
