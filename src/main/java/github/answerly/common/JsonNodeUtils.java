package github.answerly.common;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json node utils
 *
 * @author liuyan
 * @date 2018/8/2
 * @since 1.0.0
 */
public class JsonNodeUtils {

    /**
     * jackson ObjectMapper
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static <T> T getObjectByTree(Map<String, Object> objectTree, Class<T> type) {
        return OBJECT_MAPPER.convertValue(objectTree, type);
    }
}
