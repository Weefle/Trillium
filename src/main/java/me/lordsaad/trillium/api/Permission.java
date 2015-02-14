package me.lordsaad.trillium.api;

public class Permission {
    public static class Punish {
        private static final String PREFIX = "tr.punish.";
        public static final String MUTE = PREFIX + "mute";
        public static final String KICK = PREFIX + "kick";
        public static final String BAN = PREFIX + "ban";
        public static final String UNBAN = PREFIX + "unban";
    }

    public static class Ability {
        private static final String PREFIX = "tr.ability.";
        public static final String BACK = PREFIX + "back";
        public static final String FLY = PREFIX + "fly";
        public static final String FLY_OTHER = PREFIX + "fly.other";
        public static final String GOD = PREFIX + "god";
        public static final String GOD_OTHER = PREFIX + "god.other";
        public static final String SPEED = PREFIX + "speed";
        public static final String GAMEMODE = PREFIX + "gamemode";
        public static final String GAMEMODE_OTHER = PREFIX + "gamemode.other";
        public static final String VANISH = PREFIX + "vanish";
        public static final String VANISH_OTHER = PREFIX + "vanish.other";
    }

    public static class Afk {
        private static final String PREFIX = "tr.afk.";
        public static final String USE = PREFIX + "use";
    }

    public static class Admin {
        private static final String PREFIX = "tr.admin.";
        public static final String BROADCAST = PREFIX + "broadcast";
        public static final String CHESTFINDER = PREFIX + "chestfinder";
        public static final String SETSPAWN = PREFIX + "setspawn";
        public static final String LAG = PREFIX + "lag";
    }

    public static class Teleport {
        private static final String PREFIX = "tr.teleport.";
        public static final String SPAWN = PREFIX + "spawn";
        public static final String TP = PREFIX + "tp";
        public static final String TP_OTHER = PREFIX + "tp.other";
        public static final String TP_COORDS = PREFIX + "tp.coords";
        public static final String TPHERE = PREFIX + "tphere";
        public static final String TPREQEST = PREFIX + "tprequest";
        public static final String TPREQESTHERE = PREFIX + "tprequesthere";
        public static final String TPRRESPOND = PREFIX + "tprequest.respond";
    }

    public static class Chat {
        private static final String PREFIX = "tr.chat.";
        public static final String BROADCAST = PREFIX + "broadcast";
        public static final String MOTD = PREFIX + "motd";
        public static final String INFO = PREFIX + "information";
    }
}
