package net.gettillium.trillium.api;

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
        public static final String GOD = VANISH + "god-mode";
        public static final String PICK_UP_ITEM = VANISH + "pick-up-items";
        public static final String DROP_ITEM = VANISH + "drop-items";
        public static final String SPECTATOR = VANISH + "spectator-mode";
    }

    public static class Afk {
        private static String AFK = "afk.";
        private static String AUTO_AFK = AFK + "auto-afk.";
        public static final String AUTO_AFK_ENABLED = AUTO_AFK + "enabled";
        public static final String AUTO_AFK_TIME = AUTO_AFK + "time-until-idle";
        public static final String AUTO_AFK_KICK = AUTO_AFK + "kick-on-afk";
    }

    public static class Server {
        private static String PREFIX = "server.";
        public static final String SERVER_LIST_MOTD = PREFIX + "server-list-motd";
        public static final String INGAME_MOTD = PREFIX + "motd";
        private static String CHATCHANNEL = PREFIX + "chat-channels.";
        public static final String CCENABLED = CHATCHANNEL + "enabled";
        public static final String CCFORMAT = CHATCHANNEL + "format";
        public static final String CCCOLOR = CHATCHANNEL + "allow-color-codes";
    }

    public static class PlayerSettings {
        private static String PREFIX = "player-settings.";
        private static String NICKNAME = PREFIX + "nicknames.";
        public static final String CHARLIMIT = NICKNAME + "character-limit";
        public static final String PREF = NICKNAME + "prefix";
        public static final String OPCOLOR = NICKNAME + "ops-color-code";
    }
}
