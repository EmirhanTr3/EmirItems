package xyz.emirdev.emiritems.parameters;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import revxrsal.commands.autocomplete.SuggestionProvider;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.node.ExecutionContext;
import revxrsal.commands.parameter.ParameterType;
import revxrsal.commands.stream.MutableStringStream;
import xyz.emirdev.emiritems.EICommandException;
import xyz.emirdev.emiritems.EmirItems;
import xyz.emirdev.emiritems.item.CustomItem;

public class CustomItemParameterType implements ParameterType<CommandActor, CustomItem> {

    @Override
    public CustomItem parse(@NotNull MutableStringStream input, @NotNull ExecutionContext<@NotNull CommandActor> context) {
        String itemId = input.readString();

        CustomItem item = EmirItems.get().getItemManager().getItem(itemId);

        if (item == null) throw new EICommandException(
                "<red>Invalid item:</red> <yellow>{0}</yellow>",
                itemId
        );

        return item;
    }

    @Override
    public @NotNull SuggestionProvider<@NotNull CommandActor> defaultSuggestions() {
        return (context) -> EmirItems.get().getItemManager().getItems().keySet();
    }

    @Override
    public boolean isGreedy() {
        return false;
    }
}