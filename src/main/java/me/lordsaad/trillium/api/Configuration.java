package me.lordsaad.trillium.api;

public class Configuration {
    public static class Player {
        private static String prefix = "player.";
        public static final String NICKNAME = prefix + "nickname";
        public static final String LOCATION = prefix + "location";
        public static final String MUTED = prefix + "muted";
        public static final String GOD = prefix + "god";
        public static final String VANISH = prefix + "vanish";
        public static final String BAN_REASON = prefix + "banreason";
    };
}
