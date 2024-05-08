package models.lombok;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@SuppressWarnings("SpellCheckingInspection")
@Data
public class ColorInformation {

    private Integer id;

    private String name;

    private Integer year;

    private String color;

    @JsonProperty("pantone_value")
    private String pantonValue;
}
