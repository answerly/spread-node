package github.answerly.common.jackson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.answerly.common.Constants;
import github.answerly.common.SpreadNodeMapper;
import github.answerly.common.protocol.TreeNode;

/**
 * jackson SpreadNodeMapper impl
 *
 * @author liuyan
 * @date 2018/8/2
 * @since 1.0.0
 */
public class JacksonSpreadNodeMapper extends SpreadNodeMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    protected TreeNode getRootTreeNode(Object instance) {
        String key = Constants.NULL_KEY;
        JsonNode inner = null;
        try {
            String jsonStr = OBJECT_MAPPER.writeValueAsString(instance);
            inner = OBJECT_MAPPER.readTree(jsonStr);
        } catch (JsonProcessingException ignore) {
        } catch (IOException ignore) {
        }
        return new JacksonTreeNode(key, inner);
    }

    @Override
    protected <T> T convert(TreeNode treeNode) {
        OBJECT_MAPPER.createObjectNode();
        return null;
    }

    class JacksonTreeNode implements TreeNode {

        private final String key;

        private final JsonNode inner;

        JacksonTreeNode(String key, JsonNode inner) {
            this.key = key;
            this.inner = inner;
        }

        @Override
        public boolean isValue() {
            return inner.isValueNode();
        }

        @Override
        public boolean isObject() {
            return inner.isObject();
        }

        @Override
        public boolean isArray() {
            return inner.isArray();
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            if (!inner.isNull() && inner.isValueNode()) {
                return inner.asText();
            }
            return null;
        }

        @Override
        public Iterator<TreeNode> elements() {
            if (inner.isArray()) {
                List<TreeNode> treeNodes = new ArrayList<>();
                Iterator<JsonNode> iterator = inner.elements();
                while (iterator.hasNext()) {
                    String key = Constants.NULL_KEY;
                    JsonNode nextInner = iterator.next();
                    treeNodes.add(new JacksonTreeNode(key, nextInner));
                }
                return treeNodes.iterator();
            }
            if (inner.isObject()) {
                List<TreeNode> treeNodes = new ArrayList<>();
                Iterator<String> iterator = inner.fieldNames();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    JsonNode nextInner = inner.path(key);
                    treeNodes.add(new JacksonTreeNode(key, nextInner));
                }
                return treeNodes.iterator();
            }
            return Collections.emptyIterator();
        }
    }
}
