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
        public static final String GAMEMODE = "gamemode";
    }

    public static class Server {
        private static final String PREFIX = "server.";
        public static final String ALLOW_METRICS = PREFIX + "allow-metrics";
        public static final String AUTOMATIC_BACKUPS = PREFIX + "automatic-backups";
        public static final String AUTOMATIC_BACKUP_FREQUENCY = PREFIX + "automatic-backup-frequency";
        private static final String SQL = PREFIX + "sql.";
        public static final String SQL_ENABLED = SQL + "enabled";
        public static final String SQL_DATABASE_TYPE = SQL + "database-type";
        public static final String SQL_HOST_NAME = SQL + "host-name";
        public static final String SQL_PORT = SQL + "port";
        public static final String SQL_DATABASE = SQL + "database";
        public static final String SQL_USER = SQL + "user";
        public static final String SQL_PASS = SQL + "pass";
        private static final String SPAWN = PREFIX + "spawn.";
        public static final String SPAWN_SPAWN_PER_GROUP = SPAWN + "spawn-per-group";
        public static final String SPAWN_PERM_FOR_EACH_SPAWN = SPAWN + "perm-for-each-spawn";
        private static final String PVP = PREFIX + "pvp.";
        public static final String PVP_ENABLED = PVP + "enabled";
        public static final String PVP_ENABLE_TOGGLE_PVP = PVP + "enable-toggle-pvp";
        public static final String PERMISSION_BASED_HELP_MENU = PREFIX + "permission-based-help-menu";
        public static final String TUNE_INSTRUMENT = PREFIX + "tune-instrument";
        public static final String TUNE_NOTE = PREFIX + "tune-note";
        public static final String TUNE_TONE = PREFIX + "tune-tone";
        private static final String COOLDOWN = PREFIX + "cooldown.";
        public static final String COOLDOWN_TELEPORTATION = COOLDOWN + "teleportation";
        public static final String COOLDOWN_CHAT = COOLDOWN + "chat";
        public static final String COOLDOWN_KIT = COOLDOWN + "kit";
    }

    public static class Chat {
        private static final String PREFIX = "chat.";
        public static final String SERVER_LIST_MOTD = PREFIX + "server-list-motd";
        public static final String MOTD = PREFIX + "motd";
        private static final String CHAT_CHANNELS = PREFIX + "chat-channels.";
        public static final String CHAT_CHANNELS_ENABLED = CHAT_CHANNELS + "enabled";
        public static final String CHAT_CHANNELS_FORMAT = CHAT_CHANNELS + "format";
        public static final String CHAT_CHANNELS_ALLOW_COLOR_CODES = CHAT_CHANNELS + "allow-color-codes";
        public static final String ENABLE_DEATH_MESSAGES = PREFIX + "enable-death-messages";
        private static final String CHAT_FORMAT = PREFIX + "chat-format.";
        public static final String CHAT_FORMAT_ENABLED = CHAT_FORMAT + "enabled";
        public static final String CHAT_FORMAT_GLOBAL_FORMAT = CHAT_FORMAT + "global-format";

        // set to public
        public static final String CHAT_FORMAT_PERMISSION_SPECIFIC_FORMATS = CHAT_FORMAT + "permission-specific-formats.";

        public static final String PERMISSION_SPECIFIC_FORMATS_ADMIN = CHAT_FORMAT_PERMISSION_SPECIFIC_FORMATS + "admin";
        public static final String PERMISSION_SPECIFIC_FORMATS_MODERATOR = CHAT_FORMAT_PERMISSION_SPECIFIC_FORMATS + "moderator";

        // set to public
        public static final String CHAT_FORMAT_GROUP_SPECIFIC_FORMATS = CHAT_FORMAT + "group-specific-formats.";

        public static final String GROUP_SPECIFIC_FORMATS_EXAMPLE_ADMIN = CHAT_FORMAT_GROUP_SPECIFIC_FORMATS + "example-admin";
        public static final String GROUP_SPECIFIC_FORMATS_EXAMPLE_MOD = CHAT_FORMAT_GROUP_SPECIFIC_FORMATS + "example-mod";
    }

    public static class PluginMessages {
        private static final String PREFIX = "plugin-messages.";
        private static final String COLOR_PALLETE = PREFIX + "color-pallete.";
        public static final String COLOR_PALLETE_MINOR_COLOR = COLOR_PALLETE + "minor-color";
        public static final String COLOR_PALLETE_MAJOR_COLOR = COLOR_PALLETE + "major-color";
        public static final String COLOR_PALLETE_HIGHLIGHT_COLOR = COLOR_PALLETE + "highlight-color";
        public static final String FORMAT = PREFIX + "format";
        public static final String FROM_TO_TO_MESSAGE = PREFIX + "from-to-to-message";
        public static final String TO_FROM_FROM_MESSAGE = PREFIX + "to-from-from-message";
        private static final String COLORS = PREFIX + "colors.";
        public static final String COLORS_NEUTRAL_MESSAGE = COLORS + "neutral-message";
        public static final String COLORS_BAD_MESSAGE = COLORS + "bad-message";
        public static final String COLORS_GOOD_MESSAGE = COLORS + "good-message";
        private static final String ERRORS = PREFIX + "errors.";
        public static final String ERRORS_INVALID_PLAYER = ERRORS + "invalid-player";
        public static final String ERRORS_NO_PERMISSION = ERRORS + "no-permission";
        public static final String ERRORS_CONSOLE_NOT_ALLOWED = ERRORS + "console-not-allowed";
        public static final String ERRORS_TOO_FEW_ARGUMENTS = ERRORS + "too-few-arguments";
        public static final String ERRORS_WRONG_ARGUMENTS = ERRORS + "wrong-arguments";
    }

    public static class Broadcast {
        private static final String PREFIX = "broadcast.";
        private static final String REGULAR_BROADCASTS = PREFIX + "regular-broadcasts.";
        public static final String REGULAR_BROADCASTS_BROADCAST_FORMAT = REGULAR_BROADCASTS + "broadcast-format";
        public static final String REGULAR_BROADCASTS_CENTRALIZE_BROADCASTS = REGULAR_BROADCASTS + "centralize-broadcasts";
        public static final String REGULAR_BROADCASTS_COLOR_TO_USE = REGULAR_BROADCASTS + "color-to-use";
        private static final String AUTO_BROADCAST = PREFIX + "auto-broadcast.";
        public static final String AUTO_BROADCAST_ENABLED = AUTO_BROADCAST + "enabled";
        public static final String AUTO_BROADCAST_FREQUENCY = AUTO_BROADCAST + "frequency";

        // set to public
        public static final String AUTO_BROADCAST_BROADCASTS = AUTO_BROADCAST + "broadcasts.";

        public static final String BROADCASTS_1 = AUTO_BROADCAST_BROADCASTS + "1";
        public static final String BROADCASTS_2 = AUTO_BROADCAST_BROADCASTS + "2";
        private static final String IMPORTANT_BROADCAST = PREFIX + "important-broadcast.";
        public static final String IMPORTANT_BROADCAST_ENABLED = IMPORTANT_BROADCAST + "enabled";
        public static final String IMPORTANT_BROADCAST_BROADCAST = IMPORTANT_BROADCAST + "broadcast";
    }

    public static class PlayerSettings {
        private static final String PREFIX = "player-settings.";
        private static final String BED_SPAWN = PREFIX + "bed-spawn.";
        public static final String BED_SPAWN_ENABLED = BED_SPAWN + "enabled";
        public static final String BED_SPAWN_FORCE_SAVE_IF_DAYTIME = BED_SPAWN + "force-save-if-daytime";
        public static final String BED_SPAWN_ON_DEATH_RESPAWN_TO_BED = BED_SPAWN + "on-death-respawn-to-bed";
        private static final String NICKNAMES = PREFIX + "nicknames.";
        public static final String NICKNAMES_OPS_COLOR_CODE = NICKNAMES + "ops-color-code";
        public static final String NICKNAMES_PREFIX = NICKNAMES + "prefix";
        public static final String NICKNAMES_CHARACTER_LIMIT = NICKNAMES + "character-limit";
        private static final String JOIN = PREFIX + "join.";
        public static final String JOIN_MESSAGE = JOIN + "message";
        private static final String JOIN_NEW_PLAYERS = JOIN + "new-players.";
        public static final String NEW_PLAYERS_NEW_PLAYER_MESSAGE = JOIN_NEW_PLAYERS + "new-player-message";
        public static final String NEW_PLAYERS_COMMANDS_TO_RUN = JOIN_NEW_PLAYERS + "commands-to-run";
        private static final String JOIN_TEMP_GOD_MODE = JOIN + "temp-god-mode.";
        public static final String TEMP_GOD_MODE_ENABLED = JOIN_TEMP_GOD_MODE + "enabled";
        public static final String TEMP_GOD_MODE_TIME_UNTIL_TEMP_IS_OVER = JOIN_TEMP_GOD_MODE + "time-until-temp-is-over";
        private static final String LEAVE = PREFIX + "leave.";
        public static final String LEAVE_MESSAGE = LEAVE + "message";
        public static final String ENABLE_HOMES = PREFIX + "enable-homes";
        public static final String MAX_NUMBER_OF_HOMES = PREFIX + "max-number-of-homes";
    }

    public static class Kits {
        private static final String PREFIX = "kits.";
        public static final String ENABLED = PREFIX + "enabled";

        // set to public
        public static final String KIT_MAKER = PREFIX + "kit-maker.";

        private static final String KIT_MAKER_EXAMPLE_KIT = KIT_MAKER + "example-kit.";
        public static final String EXAMPLE_KIT_COOL_DOWN = KIT_MAKER_EXAMPLE_KIT + "cool-down";
        public static final String EXAMPLE_KIT_ITEMS = KIT_MAKER_EXAMPLE_KIT + "items";
    }

    public static class Ability {
        private static final String PREFIX = "ability.";
        public static final String AUTO_RESPAWN = PREFIX + "auto-respawn";
        private static final String VANISH = PREFIX + "vanish.";
        public static final String VANISH_SPECTATOR_MODE = VANISH + "spectator-mode";
        public static final String VANISH_GOD_MODE = VANISH + "god-mode";
        public static final String VANISH_PICK_UP_ITEMS = VANISH + "pick-up-items";
        public static final String VANISH_DROP_ITEMS = VANISH + "drop-items";
    }

    public static class Afk {
        private static final String PREFIX = "afk.";
        private static final String AUTO_AFK = PREFIX + "auto-afk.";
        public static final String AUTO_AFK_ENABLED = AUTO_AFK + "enabled";
        public static final String AUTO_AFK_TIME_UNTIL_IDLE = AUTO_AFK + "time-until-idle";
        public static final String AUTO_AFK_KICK_ON_AFK = AUTO_AFK + "kick-on-afk";
    }

    public static class Economy {
        private static final String PREFIX = "economy.";
        public static final String ENABLED = PREFIX + "enabled";
        public static final String CURRENCY_SYMBOL = PREFIX + "currency-symbol";
        public static final String STARTER_BALANCE = PREFIX + "starter-balance";
    }
}