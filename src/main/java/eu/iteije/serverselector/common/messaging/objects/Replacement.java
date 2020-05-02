package eu.iteije.serverselector.common.messaging.objects;

import eu.iteije.serverselector.common.messaging.enums.ReplacementType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Replacement {

    private String key;
    private String replacement;
    private ReplacementType replacementType;

}
