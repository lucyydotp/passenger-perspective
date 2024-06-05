package me.lucyydotp.papers;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

public class Translations {
    public static final String FOCUS = "papers.focus";

    public static final String FOCUS_ERROR = "papers.focus.error";

    public static final String CLEAR_FOCUS = "papers.clearfocus";

    private static final Component PREFIX = Component.literal("[PaPers] ").withStyle(Style.EMPTY.withColor(0x00b6c7));

    public static MutableComponent translate(String key) {
        return PREFIX.copy().append(
                Component.translatable(key)
        );
    }
}
