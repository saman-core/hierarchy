package io.samancore.hierarchy.json_diagram;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.samancore.hierarchy.model.Cardinality;
import io.samancore.hierarchy.model.StateType;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ERDiagram {
    private List<Cell> cells;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cell {
        private String type;
        private String id;
        private String name;
        private String comment;
        private StateType stateType;
        private Source source;
        private Target target;
        @JsonProperty("cardinality_source")
        private Cardinality cardinalitySource;

        @JsonProperty("cardinality_target")
        private Cardinality cardinalityTarget;
    }

    @Data
    public static class Source {
        private String id;
    }

    @Data
    public static class Target {
        private String id;
    }
}
