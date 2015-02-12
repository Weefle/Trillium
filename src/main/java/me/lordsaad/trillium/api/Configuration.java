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
    }

    public static class Ability {
        private static String PREFIX = "ability.";
        public static final String AUTO_RESPAWN = PREFIX + "auto-respawn";
        private static String VANISH = PREFIX + "vanish.";
        public static final String GOD = PREFIX + VANISH + "god-mode";
        public static final String PICK_UP_ITEM = VANISH + "pick-up-item";
        public static final String DROP_ITEM = VANISH + "drop-item";
        public static final String SPECTATOR = VANISH + "spectator-mode";
        private static String AFK = PREFIX + "afk.";
        private static String AUTO_AFK = AFK + "auto-afk.";
        public static final String AUTO_AFK_ENABLED = AUTO_AFK + "enabled";
        public static final String AUTO_AFK_TIME = AUTO_AFK + "time-until-idle";
        public static final String AUTO_AFK_KICK = AUTO_AFK + "kick-on-afk";
        public static final String AUTO_UNAFK = AUTO_AFK + "auto-unafk";
    }

    public static class Server {
        private static String PREFIX = "server.";
        public static final String SERVER_LIST_MOTD = PREFIX + "server-list-motd";
        public static final String INGAME_MOTD = PREFIX + "motd";
    }
}
