package client;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class OneStepResponse<T> {
    private int status;
    private Map<String, List<String>> headers;
    private T body;
}
