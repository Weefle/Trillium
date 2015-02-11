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
    
    public static class Ability {
        private static String PREFIX = "ability.";
        private static String VANISH = "vanish.";
        public static final String VANISH_ENABLED = PREFIX + VANISH + "enabled";
        public static final String PICK_UP_ITEM = PREFIX + VANISH + "pick-up-item";
        public static final String DROP_ITEM = PREFIX + VANISH + "drop-item";
    };
}
