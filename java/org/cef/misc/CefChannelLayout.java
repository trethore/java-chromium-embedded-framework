package org.cef.misc;

/**
 * Mirror of cef_channel_layout_t. Values must stay in sync with CEF headers.
 */
public enum CefChannelLayout {
    CEF_CHANNEL_LAYOUT_NONE(0),
    CEF_CHANNEL_LAYOUT_UNSUPPORTED(1),
    CEF_CHANNEL_LAYOUT_MONO(2),
    CEF_CHANNEL_LAYOUT_STEREO(3),
    CEF_CHANNEL_LAYOUT_2_1(4),
    CEF_CHANNEL_LAYOUT_SURROUND(5),
    CEF_CHANNEL_LAYOUT_4_0(6),
    CEF_CHANNEL_LAYOUT_2_2(7),
    CEF_CHANNEL_LAYOUT_QUAD(8),
    CEF_CHANNEL_LAYOUT_5_0(9),
    CEF_CHANNEL_LAYOUT_5_1(10),
    CEF_CHANNEL_LAYOUT_5_0_BACK(11),
    CEF_CHANNEL_LAYOUT_5_1_BACK(12),
    CEF_CHANNEL_LAYOUT_7_0(13),
    CEF_CHANNEL_LAYOUT_7_1(14),
    CEF_CHANNEL_LAYOUT_7_1_WIDE(15),
    CEF_CHANNEL_LAYOUT_STEREO_DOWNMIX(16),
    CEF_CHANNEL_LAYOUT_2POINT1(17),
    CEF_CHANNEL_LAYOUT_3_1(18),
    CEF_CHANNEL_LAYOUT_4_1(19),
    CEF_CHANNEL_LAYOUT_6_0(20),
    CEF_CHANNEL_LAYOUT_6_0_FRONT(21),
    CEF_CHANNEL_LAYOUT_HEXAGONAL(22),
    CEF_CHANNEL_LAYOUT_6_1(23),
    CEF_CHANNEL_LAYOUT_6_1_BACK(24),
    CEF_CHANNEL_LAYOUT_6_1_FRONT(25),
    CEF_CHANNEL_LAYOUT_7_0_FRONT(26),
    CEF_CHANNEL_LAYOUT_7_1_WIDE_BACK(27),
    CEF_CHANNEL_LAYOUT_OCTAGONAL(28),
    CEF_CHANNEL_LAYOUT_DISCRETE(29),
    CEF_CHANNEL_LAYOUT_STEREO_AND_KEYBOARD_MIC(30),
    CEF_CHANNEL_LAYOUT_4_1_QUAD_SIDE(31),
    CEF_CHANNEL_LAYOUT_BITSTREAM(32),
    CEF_CHANNEL_LAYOUT_5_1_4_DOWNMIX(33),
    CEF_CHANNEL_LAYOUT_MAX(33);

    private final int id;

    CefChannelLayout(int id) {
        this.id = id;
    }

    // Map layout id (from CEF enum values) to enum instance. Some ids are not
    // contiguous and CEF_CHANNEL_LAYOUT_MAX shares id 33, so indexing by ordinal
    // would return the wrong value for duplicate ids.
    private static final java.util.Map<Integer, CefChannelLayout> ID_MAP =
            new java.util.HashMap<>();
    static {
        for (CefChannelLayout layout : CefChannelLayout.values()) {
            // Keep first occurrence for duplicate ids.
            ID_MAP.putIfAbsent(layout.id, layout);
        }
    }

    public static CefChannelLayout forId(int id) {
        CefChannelLayout layout = ID_MAP.get(id);
        return layout != null ? layout : CEF_CHANNEL_LAYOUT_UNSUPPORTED;
    }

    public int getId() {
        return id;
    }
}
