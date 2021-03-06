package net.gettrillium.trillium.api;

public class Permission {
    public static class Punish {
        private static final String PREFIX = "tr.punish.";
        public static final String MUTE = PREFIX + "mute";
        public static final String KICK = PREFIX + "kick";
        public static final String BAN = PREFIX + "ban";
        public static final String UNBAN = PREFIX + "unban";
        public static final String BANIP = PREFIX + "banip";
        public static final String UNBANIP = PREFIX + "unbanip";
        public static final String SHADOW_BAN = PREFIX + "shadow-ban";
        public static final String SHADOW_MUTE = PREFIX + "shadow-mute";
        public static final String FREEZE = PREFIX + "freeze";
    }

    public static class Ability {
        private static final String PREFIX = "tr.ability.";
        public static final String FLY = PREFIX + "fly";
        public static final String FLY_OTHER = PREFIX + "fly.other";
        public static final String GOD = PREFIX + "god";
        public static final String GOD_OTHER = PREFIX + "god.other";
        public static final String SPEED = PREFIX + "speed";
        public static final String GAMEMODE = PREFIX + "gamemode";
        public static final String GAMEMODE_OTHER = PREFIX + "gamemode.other";
        public static final String VANISH = PREFIX + "vanish";
        public static final String VANISH_OTHER = PREFIX + "vanish.other";
        public static final String PVP = PREFIX + "pvp";
        public static final String FEED = PREFIX + "feed";
        public static final String FEED_OTHER = PREFIX + "feed.other";
        public static final String HEAL = PREFIX + "heal";
        public static final String HEAL_OTHER = PREFIX + "heal.other";
    }

    public static class Afk {
        private static final String PREFIX = "tr.afk.";
        public static final String USE = PREFIX + "use";
    }

    public static class Admin {
        private static final String PREFIX = "tr.admin.";
        public static final String CHESTFINDER = PREFIX + "chestfinder";
        public static final String SETSPAWN = PREFIX + "setspawn";
        public static final String LAG = PREFIX + "lag";
        public static final String TRILLIUM = PREFIX + "trillium";
        public static final String KILLALL = PREFIX + "killall";
        public static final String INV_CRAFTING = PREFIX + "inventory.crafting";
        public static final String INV_PLAYER = PREFIX + "inventory.player";
        public static final String INV_ENDERCHEST = PREFIX + "inventory.enderchest";
        public static final String REPORT = PREFIX + "report";
        public static final String REPORT_RECEIVER = PREFIX + "reportreceiver";
        public static final String CMDBINDER = PREFIX + "commandbinder";
        public static final String TGM = PREFIX + "trilliumgroupmanager";
        public static final String CLEARINV = PREFIX + "clearinventory";
    }

    public static class Teleport {
        private static final String PREFIX = "tr.teleport.";
        public static final String BACK = PREFIX + "back";
        public static final String SPAWN = PREFIX + "spawn";
        public static final String TP = PREFIX + "tp";
        public static final String TP_OTHER = PREFIX + "tp.other";
        public static final String TP_COORDS = PREFIX + "tp.coords";
        public static final String TPHERE = PREFIX + "tphere";
        public static final String TPREQEST = PREFIX + "tprequest";
        public static final String TPREQESTHERE = PREFIX + "tprequesthere";
        public static final String TPRRESPOND = PREFIX + "tprequest.respond";
        private static final String WARP = PREFIX + "warp.";
        public static final String WARP_CREATE = WARP + "create";
        public static final String WARP_LIST = WARP + "list";
        public static final String WARP_TP = WARP + "teleport";
        private static final String HOME = PREFIX + "home.";
        public static final String HOME_CREATE = HOME + "create";
        public static final String HOME_TP = HOME + "tp";
        public static final String HOME_LIST = HOME + "view";
        public static final String COOLDOWN_EXEMPT = PREFIX + "cooldown-exempt";
    }

    public static class Chat {
        private static final String PREFIX = "tr.chat.";
        public static final String BROADCAST = PREFIX + "broadcast";
        public static final String GLOBAL = PREFIX + "global";
        public static final String MOTD = PREFIX + "motd";
        public static final String INFO = PREFIX + "information";
        public static final String ME = PREFIX + "me";
        public static final String MESSAGE = PREFIX + "message";
        public static final String NICK = PREFIX + "nickname";
        public static final String NICK_OTHER = PREFIX + "nickname.other";
        public static final String NICK_COLOR = PREFIX + "nickname.color";
        public static final String NICK_OTHER_COLOR = PREFIX + "nickname.other.color";
        public static final String CHATCHANNEL = PREFIX + "chatchannel.";
        public static final String COLOR = PREFIX + "color";
        public static final String CLEARCHAT = PREFIX + "clearchat";
        public static final String COOLDOWN_EXEMPT = PREFIX + "cooldown-exempt";
        public static final String PERM_FORMAT = PREFIX + "perm-format.";
    }

    public static class Fun {
        private static final String PREFIX = "tr.fun.";
        public static final String SUDO = PREFIX + "sudo";
        public static final String SMITE = PREFIX + "smite";
    }

    public static class Kit {
        private static final String PREFIX = "tr.kit.";
        public static final String USE = PREFIX + "use.";
        public static final String GIVE = PREFIX + "give";
        public static final String COOLDOWN_EXEMPT = PREFIX + "cooldown-exempt";
    }

    public static class Economy {
        private static final String PREFIX = "tr.economy.";
        public static final String PAY = PREFIX + "pay";
        public static final String BALANCE = PREFIX + "balance";
        public static final String ADMIN = PREFIX + "admin";
    }
}
