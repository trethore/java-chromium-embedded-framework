package org.cef.misc;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Lightweight wrapper around a native address for reading interleaved audio
 * floats. Backed by LWJGL's MemoryUtil#memByteBuffer.
 */
public class DataPointer {
    private final long address;
    private ByteBuffer dataBuffer;
    private boolean initialized = false;
    // Bit-shift used to move between logical element index and byte index.
    // Default 2 = sizeof(float); callers can override via withAlignment().
    private int alignment = 2;

    public DataPointer(long address) {
        this.address = address;
    }

    public DataPointer forCapacity(int capacity) {
        try {
            dataBuffer = (ByteBuffer) MEM_BYTE_BUFFER.invoke(address, capacity);
            // Ensure native endianness so primitive reads match the source buffer.
            dataBuffer.order(ByteOrder.nativeOrder());
            initialized = true;
            return this;
        } catch (Throwable err) {
            throw new RuntimeException("Failed to wrap native buffer", err);
        }
    }

    /** Set alignment shift (e.g. 3 for 8-byte longs, 2 for 4-byte ints). */
    public DataPointer withAlignment(int alignment) {
        this.alignment = alignment;
        return this;
    }

    public long getAddress() {
        return address;
    }

    /**
     * Read a pointer-sized value at the given element offset and wrap it as a
     * new DataPointer (useful for buffers that contain arrays of native pointers).
     */
    public DataPointer getData(int offset) {
        ensureInitialized();
        return new DataPointer(dataBuffer.getLong(offset << alignment));
    }

    public long getLong(int offset) {
        ensureInitialized();
        return dataBuffer.getLong(offset << alignment);
    }

    public int getInt(int offset) {
        ensureInitialized();
        return dataBuffer.getInt(offset << alignment);
    }

    public short getShort(int offset) {
        ensureInitialized();
        return dataBuffer.getShort(offset << alignment);
    }

    public byte getByte(int offset) {
        ensureInitialized();
        return dataBuffer.get(offset << alignment);
    }

    public double getDouble(int offset) {
        ensureInitialized();
        return dataBuffer.getDouble(offset << alignment);
    }

    public float getFloat(int offset) {
        ensureInitialized();
        return dataBuffer.getFloat(offset << alignment);
    }

    private void ensureInitialized() {
        if (!initialized) {
            throw new IllegalStateException("Call forCapacity() before reading data");
        }
    }

    // Static resolve of LWJGL MemoryUtil.memByteBuffer
    private static final MethodHandle MEM_BYTE_BUFFER;
    static {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            Class<?> clz = Class.forName(
                    "org.lwjgl.system.MemoryUtil", false, lookup.lookupClass().getClassLoader());
            MEM_BYTE_BUFFER = lookup.findStatic(
                    clz, "memByteBuffer",
                    MethodType.methodType(ByteBuffer.class, long.class, int.class));
        } catch (Throwable err) {
            System.err.println("Could not find LWJGL MemoryUtil.memByteBuffer (is LWJGL 3 on the classpath?)");
            throw new RuntimeException(err);
        }
    }
}
