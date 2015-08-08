package net.gettrillium.trillium.api;

public class Configuration {

    public static class Player {
        public static final String NICKNAME = "nickname";
        public static final String LOCATION = "location";
        public static final String MUTED = "muted";
        public static final String GOD = "god";
        public static final String VANISH = "vanish";
        public static final String BAN_REASON = "ban-reason";
        public static final String GROUP = "group";
        public static final String PVP = "pvp";
        public static final String HOMES = "homes";
    }

    public static class Ability {
        private static final String PREFIX = "ability.";
        public static final String AUTO_RESPAWN = PREFIX + "auto-respawn";
        private static final String VANISH = PREFIX + "vanish.";
        public static final String GOD = VANISH + "god-mode";
        public static final String PICK_UP_ITEM = VANISH + "pick-up-items";
        public static final String DROP_ITEM = VANISH + "drop-items";
        public static final String SPECTATOR = VANISH + "spectator-mode";
    }

    public static class Afk {
        private static final String AFK = "afk.";
        private static final String AUTO_AFK = AFK + "auto-afk.";
        public static final String AUTO_AFK_ENABLED = AUTO_AFK + "enabled";
        public static final String AUTO_AFK_TIME = AUTO_AFK + "time-until-idle";
        public static final String AUTO_AFK_KICK = AUTO_AFK + "kick-on-afk";
    }

    public static class Chat {
        private static final String PREFIX = "chat.";
        public static final String INGAME_MOTD = PREFIX + "motd";
        public static final String SERVER_LIST_MOTD = PREFIX + "server-list-motd";
        public static final String ENABLE_DEATH_MESSAGES = PREFIX + "enable-death-messages";

        private static final String CHATCHANNEL = PREFIX + "chat-channels.";
        public static final String CCENABLED = CHATCHANNEL + "enabled";
        public static final String CCFORMAT = CHATCHANNEL + "format";
        public static final String CCCOLOR = CHATCHANNEL + "allow-color-codes";

        private static final String CHAT_FORMAT = PREFIX + "chat-format.";
        public static final String CHAT_FORMAT_ENABLED = CHAT_FORMAT + "enabled";
        public static final String GLOBAL_FORMAT = CHAT_FORMAT + "global-format";
        public static final String PERM_SPECIFIC_FORMATS = CHAT_FORMAT + "permission-specific-formats";
        public static final String GROUP_SPECIFIC_FORMATS = CHAT_FORMAT + "group-specific-formats";
    }

    public static class Server {
        private static final String PREFIX = "server.";
        private static final String PVP = PREFIX + "pvp.";
        public static final String PVPENABLE = PVP + "enabled";
        public static final String TOGGLEPVP = PVP + "enable-toggle-pvp";
        public static final String METRICS = PREFIX + "allow-metrics";
        public static final String INSTRUMENT = PREFIX + "tune-instrument";
        public static final String NOTE = PREFIX + "tune-note";
        public static final String TONE = PREFIX + "tune-tone";
        public static final String PERM_BASED_HELP_MENU = PREFIX + "permission-based-help-menu";
        private static final String SQL = PREFIX + "sql.";
        public static final String SQL_ENABLED = SQL + "enabled";
        public static final String SQL_HOST = SQL + "host-name";
        public static final String SQL_PORT = SQL + "port";
        public static final String SQL_DATABASE = SQL + "database";
        public static final String SQL_USER = SQL + "user";
        public static final String SQL_PASS = SQL + "pass";
    }

    public static class PlayerSettings {
        private static final String PREFIX = "player-settings.";
        public static final String JOINMESSAGE = PREFIX + "join.message";
        public static final String LEAVEMESSAGE = PREFIX + "leave.message";
        private static final String NICKNAME = PREFIX + "nicknames.";
        public static final String CHARLIMIT = NICKNAME + "character-limit";
        public static final String PREF = NICKNAME + "prefix";
        public static final String OPCOLOR = NICKNAME + "ops-color-code";
        private static final String NEW_PLAYER_PREFIX = PREFIX + "join.new-players.";
        public static final String NEWJOINMESSAGE = NEW_PLAYER_PREFIX + "new-player-message";
        public static final String COMMANDS_TO_RUN = NEW_PLAYER_PREFIX + "commands-to-run";
        public static final String TEMP_GOD_MODE_ENABLED = NEW_PLAYER_PREFIX + "temp-god-mode.enabled";
        public static final String TEMP_GOD_MODE_TIME = NEW_PLAYER_PREFIX + "temp-god-mode.time-until-temp-is-over";
        public static final String HOMES_ENABLED = PREFIX + "enable-homes";
        public static final String HOMES_MAX = PREFIX + "max-number-of-homes";
    }

    public static class Broadcast {
        private static final String PREFIX = "broadcast.";
        public static final String PRE_NORMAL = PREFIX + "regular-broadcasts.";
        public static final String FORMAT = PRE_NORMAL + "broadcast-format";
        public static final String CENTRALIZE = PRE_NORMAL + "centralize-broadcasts";
        public static final String COLOR_TO_USE = PRE_NORMAL + "color-to-use";
        private static final String AUTO_BROADCAST = PREFIX + "auto-broadcast.";
        public static final String AUTO_ENABLED = AUTO_BROADCAST + "enabled";
        public static final String FREQUENCY = AUTO_BROADCAST + "frequency";
        public static final String AUTO_BROADCASTS = AUTO_BROADCAST + "broadcasts";
        private static final String IMP_BROADCAST = PREFIX + "important-broadcast.";
        public static final String IMP_ENABLED = IMP_BROADCAST + "enabled";
        public static final String IMP_BROADCAST2 = IMP_BROADCAST + "broadcast";
    }

    public static class GM {
        private static final String PREFIX = "group-manager.";
        public static final String ENABLED = PREFIX + "enabled";
        public static final String RELOAD = PREFIX + "auto-reload-frequency";
    }

    public static class Kit {
        private static final String PREFIX = "kits.";
        public static final String ENABLED = PREFIX + "enabled";
        public static final String KIT_MAKER = PREFIX + "kit-maker.";
    }

    public static class PluginMessages {
        private static final String PREFIX = "plugin-messages.";
        private static final String ERRORS = PREFIX + "errors.";
        private static final String COLORS = PREFIX + "colors.";
        public static final String FORMAT = PREFIX + "format";
        public static final String NEUTRAL_COLOR = COLORS + "neutral-message";
        public static final String BAD_COLOR = COLORS + "bad-message";
        public static final String GOOD_COLOR = COLORS + "good-message";
        public static final String INVALID_PLAYER = ERRORS + "invalid-player";
        public static final String NO_PERMISSION = ERRORS + "no-permission";
        public static final String CONSOLE_NOT_ALLOWED = ERRORS + "console-not-allowed";
        public static final String TOO_FEW_ARGUMENTS = ERRORS + "too-few-arguments";
        public static final String WRONG_ARGUMENTS = ERRORS + "wrong-arguments";
        public static final String FROM_TO_TO_MESSAGE = PREFIX + "from-to-to-message";
        public static final String TO_FROM_FROM_MESSAGE = PREFIX + "to-from-from-message";
        private static final String PALLETE = PREFIX + "color-pallete.";
        public static final String PALLETE_MINOR = PALLETE + "minor-color";
        public static final String PALLETE_MAJOR = PALLETE + "major-color";
        public static final String PALLETE_HIGHLIGHT = PALLETE + "highlight-color";
    }

    public static class Economy {
        private static final String PREFIX = "economy.";
        public static final String ENABLED = PREFIX + "enabled";
        public static final String CURRENCY_SYMBOL = PREFIX + "currency-symbol";
        public static final String STARTER_BALANCE = PREFIX + "starter-balance";
    }
}
