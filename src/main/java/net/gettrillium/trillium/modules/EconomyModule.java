package net.gettrillium.trillium.modules;

import net.gettrillium.trillium.Trillium;
import net.gettrillium.trillium.api.Configuration;
import net.gettrillium.trillium.api.Permission;
import net.gettrillium.trillium.api.TrilliumModule;
import net.gettrillium.trillium.api.command.Command;
import net.gettrillium.trillium.api.messageutils.Error;
import net.gettrillium.trillium.api.messageutils.Message;
import net.gettrillium.trillium.api.messageutils.Mood;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomyModule extends TrilliumModule {

    @Command(command = "balance",
            description = "Check how much money you have in your account.",
            usage = "/balance",
            aliases = {"money", "wallet"},
            permissions = {Permission.Economy.BALANCE, Permission.Economy.ADMIN})
    public void balance(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (Trillium.economy != null) {
                if (p.hasPermission(Permission.Economy.BALANCE) || p.hasPermission(Permission.Economy.ADMIN)) {

                    new Message(Mood.NEUTRAL, "Balance", getConfig().getString(Configuration.Economy.CURRENCY_SYMBOL) + Trillium.economy.getBalance(p)).to(p);
                }
            } else {
                new Message(Mood.BAD, "Balance", "This feature is disabled.").to(p);
            }
        } else {
            new Message("Balance", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }

    @Command(command = "pay",
            description = "Send money to a certain player.",
            usage = "/pay <player> <amount>",
            permissions = {Permission.Economy.PAY, Permission.Economy.ADMIN})
    public void pay(CommandSender cs, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (Trillium.economy != null) {
                if (p.hasPermission(Permission.Economy.PAY)) {
                    if (Trillium.economy.hasAccount(p)) {
                        if (args.length >= 2) {
                            Player target = Bukkit.getPlayer(args[0]);
                            if (target != null) {
                                if (StringUtils.isNumeric(args[1])) {
                                    if (Trillium.economy.has(p, Double.parseDouble(args[0]))) {

                                        Trillium.economy.withdrawPlayer(p, Double.parseDouble(args[0]));
                                        Trillium.economy.depositPlayer(p, Double.parseDouble(args[0]));

                                        new Message(Mood.NEUTRAL, "Balance", "You transferred " + getConfig().getString(Configuration.Economy.CURRENCY_SYMBOL) + " to " + target.getName() + "'s account").to(p);
                                        new Message(Mood.NEUTRAL, "Balance", p.getName() + " transferred " + getConfig().getString(Configuration.Economy.CURRENCY_SYMBOL) + args[0] + " to your account").to(p);

                                    } else {
                                        new Message(Mood.BAD, "Balance", "You don't have enough money.").to(p);
                                    }
                                } else {
                                    new Message(Mood.BAD, "Balance", args[1] + " is not a number.").to(p);
                                }
                            } else {
                                new Message("Balance", Error.INVALID_PLAYER, args[0]).to(p);
                            }
                        } else {
                            new Message("Balance", Error.TOO_FEW_ARGUMENTS, "/pay <player> <amount>").to(p);
                        }
                    } else {
                        new Message(Mood.BAD, "Balance", "You don't have an economy account.").to(p);
                    }
                } else {
                    new Message("Balance", Error.NO_PERMISSION).to(p);
                }
            } else {
                new Message(Mood.BAD, "Balance", "This feature is disabled.").to(p);
            }
        } else {
            new Message("Balance", Error.CONSOLE_NOT_ALLOWED).to(cs);
        }
    }
}
