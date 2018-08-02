package github.answerly.common;

import java.util.ArrayList;
import java.util.List;

/**
 * map context
 *
 * @author liuyan
 * @since 1.0.0
 */
class MapperContext {

    private int idGenerator;

    private List<SpreadNode> nodeList;

    MapperContext() {
        this.idGenerator = 0;
        this.nodeList = new ArrayList<>();
    }

    SpreadNode appendValueNode(String key, String value, int parentId) {
        SpreadNode valueNode = new SpreadNode();
        valueNode.setId(nextId());
        valueNode.setParentId(parentId);
        valueNode.setKey(key);
        valueNode.setValue(value);
        valueNode.setValueNode(true);
        valueNode.setArrayNode(false);
        valueNode.setObjectNode(false);
        this.nodeList.add(valueNode);
        return valueNode;
    }

    SpreadNode appendArrayNode(String key, int parentId) {
        SpreadNode arrayNode = new SpreadNode();
        arrayNode.setId(nextId());
        arrayNode.setParentId(parentId);
        arrayNode.setKey(key);
        arrayNode.setValue("");
        arrayNode.setValueNode(false);
        arrayNode.setArrayNode(true);
        arrayNode.setObjectNode(false);
        this.nodeList.add(arrayNode);
        return arrayNode;
    }

    SpreadNode appendObjectNode(String key, int parentId) {
        SpreadNode objectNode = new SpreadNode();
        objectNode.setId(nextId());
        objectNode.setParentId(parentId);
        objectNode.setKey(key);
        objectNode.setValue("");
        objectNode.setValueNode(false);
        objectNode.setArrayNode(false);
        objectNode.setObjectNode(true);
        this.nodeList.add(objectNode);
        return objectNode;
    }

    int nextId() {
        return idGenerator++;
    }

    public List<SpreadNode> getNodeList() {
        return nodeList;
    }
}
