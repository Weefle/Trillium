package net.gettrillium.trillium.api;

public class Configuration {
    public static class Player {
        private static String prefix = "player.";
        public static final String NICKNAME = prefix + "nickname";
        public static final String LOCATION = prefix + "location";
        public static final String MUTED = prefix + "muted";
        public static final String GOD = prefix + "god";
        public static final String VANISH = prefix + "vanish";
        public static final String BAN_REASON = prefix + "banreason";
        public static final String GROUP = prefix + "group";
        public static final String PVP = prefix + "pvp";
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

    public static class Broadcast {
        public static String FORMAT = PRE_NORMAL + "broadcast-format";
        public static String CENTRALIZE = PRE_NORMAL + "centralize-broadcasts";
        public static String COLOR_TO_USE = PRE_NORMAL + "color-to-use";
        private static String PREFIX = "broadcast.";
        public static String PRE_NORMAL = PREFIX + "regular-broadcasts.";
        private static String AUTO_BROADCAST = PREFIX + "auto-broadcast.";
        public static String AUTO_ENABLED = AUTO_BROADCAST + "enabled";
        public static String FREQUENCY = AUTO_BROADCAST + "frequency";
        public static String AUTO_BROADCASTS = AUTO_BROADCAST + "broadcasts";
        private static String IMP_BROADCAST = PREFIX + "important-broadcast.";
        public static String IMP_ENABLED = IMP_BROADCAST + "enabled";
        public static String IMP_BROADCAST2 = IMP_BROADCAST + "broadcast";

    }
    
    public static class Chat {
        private static String PREFIX = "chat.";
        public static final String INGAME_MOTD = PREFIX + "motd";
        public static final String SERVER_LIST_MOTD = PREFIX + "server-list-motd";
        public static final String ENABLE_DEATH_MESSAGES = PREFIX + "enable-death-messages";
        private static String CHATCHANNEL = PREFIX + "chat-channels.";
        public static final String CCENABLED = CHATCHANNEL + "enabled";
        public static final String CCFORMAT = CHATCHANNEL + "format";
        public static final String CCCOLOR = CHATCHANNEL + "allow-color-codes";
    }

    public static class Server {
        private static String PREFIX = "server.";
        private static final String PVP = PREFIX + "pvp.";
        public static final String PVPENABLE = PVP + "enabled";
        public static final String TOGGLEPVP = PVP + "enable-toggle-pvp";

    }

    public static class PlayerSettings {
        private static String PREFIX = "player-settings.";
        public static final String JOINMESSAGE = PREFIX + "join.message";
        public static final String LEAVEMESSAGE = PREFIX + "leave.message";
        private static String NICKNAME = PREFIX + "nicknames.";
        public static final String CHARLIMIT = NICKNAME + "character-limit";
        public static final String PREF = NICKNAME + "prefix";
        public static final String OPCOLOR = NICKNAME + "ops-color-code";
    }

    public static class GM {
        private static String PREFIX = "group-manager.";
        public static final String ENABLED = PREFIX + "enabled";
        public static final String RELOAD = PREFIX + "auto-reload-frequency";
    }

    public static class Kit {
        private static final String PREFIX = "kits.";
        public static String ENABLED = PREFIX + "enabled";
        public static String KIT_MAKER = PREFIX + "kit-maker.";

    }
}
