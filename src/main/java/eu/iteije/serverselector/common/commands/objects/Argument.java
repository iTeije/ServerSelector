package eu.iteije.serverselector.common.commands.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Argument {

    /**
     * Syntax is replaced by given data (e.g. speed by 3)
     * Description is the text displayed in the help page
     */
    private String syntax;
    private String description;

}
