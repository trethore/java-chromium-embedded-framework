package tests.junittests;

import org.cef.CefApp;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.OS;
import org.cef.browser.CefBrowser;
import org.cef.event.CefKeyEvent;
import org.cef.handler.CefKeyboardHandlerAdapter;
import org.cef.handler.CefLifeSpanHandlerAdapter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Minimal console harness that injects a direct {@link CefKeyEvent} into an
 * off-screen (OSR) browser to prove the native direct-path works on Windows.
 *
 * How it works:
 *  - Starts CefApp with windowless rendering enabled.
 *  - Creates an OSR browser and focuses it.
 *  - Sends a KEY_PRESS (with scancode 0x1E for the 'A' key) followed by a
 *    KEY_RELEASE via the direct sendKeyEvent path.
 *  - Listens for the resulting CefKeyboardHandler events; if they arrive within
 *    the timeout the test passes.
 *
 * Exit codes:
 *  0 = success, direct key event observed
 *  1 = unsupported platform (non-Windows)
 *  2 = browser creation timeout
 *  3 = key event not observed
 */
public class KeyInjectDirectTest {
    private static final int TIMEOUT_SECONDS = 5;

    public static void main(String[] args) throws Exception {
        if (!OS.isWindows()) {
            System.err.println("KeyInjectDirectTest is Windows-only; skipping.");
            System.exit(1);
        }

        CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = true;
        CefApp app = CefApp.getInstance(settings);

        CountDownLatch createdLatch = new CountDownLatch(1);
        CountDownLatch keySeenLatch = new CountDownLatch(1);

        CefClient client = app.createClient();
        client.addLifeSpanHandler(new CefLifeSpanHandlerAdapter() {
            @Override
            public void onAfterCreated(CefBrowser browser) {
                createdLatch.countDown();
                browser.setFocus(true);
            }
        });
        client.addKeyboardHandler(new CefKeyboardHandlerAdapter() {
            @Override
            public boolean onKeyEvent(
                    CefBrowser browser,
                    org.cef.handler.CefKeyboardHandler.CefKeyEvent event) {
                System.out.println("Observed key event: " + event);
                keySeenLatch.countDown();
                return false;
            }
        });

        CefBrowser browser = client.createBrowser("about:blank", true, false);

        if (!createdLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            System.err.println("Timed out waiting for browser creation.");
            cleanup(app, browser);
            System.exit(2);
        }

        CefKeyEvent press = new CefKeyEvent(CefKeyEvent.KEY_PRESS, 65, 'a', 0);
        press.setScancode(0x1E); // GLFW scancode for 'A'
        browser.sendKeyEvent(press);

        CefKeyEvent release = new CefKeyEvent(CefKeyEvent.KEY_RELEASE, 65, 'a', 0);
        release.setScancode(0x1E);
        browser.sendKeyEvent(release);

        if (!keySeenLatch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
            System.err.println("No CefKeyboardHandler onKeyEvent received.");
            cleanup(app, browser);
            System.exit(3);
        }

        System.out.println("PASS: Direct CefKeyEvent reached CEF (RAWKEYDOWN/CHAR).");
        cleanup(app, browser);
        System.exit(0);
    }

    private static void cleanup(CefApp app, CefBrowser browser) {
        try {
            if (browser != null) browser.close(true);
        } catch (Throwable ignored) {
        }
        try {
            if (app != null) app.dispose();
        } catch (Throwable ignored) {
        }
    }
}
