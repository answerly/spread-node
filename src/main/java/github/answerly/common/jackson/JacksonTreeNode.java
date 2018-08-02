package github.answerly.common.jackson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import github.answerly.common.protocol.TreeNode;

/**
 * spread node mapper for jackson impl
 *
 * @author liuyan
 * @since 1.0.0
 */
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
                String key = TreeNode.ARRAY_ELEMENT_KEY;
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
