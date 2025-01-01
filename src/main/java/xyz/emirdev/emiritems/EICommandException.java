package xyz.emirdev.emiritems;

import org.jetbrains.annotations.NotNull;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.exception.SendableException;

public class EICommandException extends SendableException {
    public Object[] args;

    public EICommandException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public void sendTo(@NotNull CommandActor actor) {
        if (!this.getMessage().isEmpty()) {
            actor.error(Utils.convertComponentToLegacyString(
                    Utils.format(getMessage(), args)
            ));
        }
    }
}