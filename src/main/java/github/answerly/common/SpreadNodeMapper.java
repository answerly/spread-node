package github.answerly.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * spread node mapper
 *
 * @author liuyan
 */
public abstract class SpreadNodeMapper {

    public List<SpreadNode> object2Node(Object instance) {
        JsonNode objectTree = JsonNodeUtils.getObjectTree(instance);
        if (objectTree.isNull() || !objectTree.isObject()) {
            throw new IllegalArgumentException("object must is struct");
        }
        MapperContext context = new MapperContext();
        Iterator<String> keys = objectTree.fieldNames();
        while (keys.hasNext()) {
            String key = keys.next();
            JsonNode valueNode = objectTree.path(key);
            if (valueNode.isValueNode()) {
                context.appendValueNode(key, getNodeValue(valueNode), Constants.ROOT_ID);
            }
            if (valueNode.isArray()) {
                appendArrayNode(key, valueNode, context, Constants.ROOT_ID);
            }
            if (valueNode.isObject()) {
                appendObjectNode(key, valueNode, context, Constants.ROOT_ID);
            }
        }
        return context.getNodeList();
    }

    public <T> T node2Object(List<SpreadNode> nodeList, Class<T> type) {
        Map<Integer, List<SpreadNode>> parentIdNodes = new HashMap<>();
        for (SpreadNode node : nodeList) {
            Integer parentId = node.getParentId();
            List<SpreadNode> currentNodeList = parentIdNodes.get(parentId);
            if (currentNodeList == null) {
                parentIdNodes.put(parentId, (currentNodeList = new ArrayList<>()));
            }
            currentNodeList.add(node);
        }
        SpreadNode rootNode = new SpreadNode();
        rootNode.setId(Constants.ROOT_ID);
        rootNode.setValueNode(false);
        rootNode.setArrayNode(false);
        rootNode.setObjectNode(true);

        Map<String, Object> objectTree = node2Object(rootNode, parentIdNodes);
        return JsonNodeUtils.getObjectByTree(objectTree, type);
    }

    private void appendArrayNode(String key, JsonNode valueNode, MapperContext context, int parentId) {
        SpreadNode arrayNode = context.appendArrayNode(key, parentId);
        Iterator<JsonNode> iterator = valueNode.elements();
        while (iterator.hasNext()) {
            JsonNode childNode = iterator.next();
            if (childNode.isValueNode()) {
                context.appendValueNode(key, getNodeValue(childNode), arrayNode.getId());
            }
            if (childNode.isArray()) {
                appendArrayNode(Constants.ARRAY_ELEMENT_KEY, childNode, context, arrayNode.getId());
            }
            if (childNode.isObject()) {
                appendObjectNode(Constants.ARRAY_ELEMENT_KEY, childNode, context, arrayNode.getId());
            }
        }
    }

    private void appendObjectNode(String key, JsonNode valueNode, MapperContext context, int parentId) {
        SpreadNode objectNode = context.appendObjectNode(key, parentId);
        Iterator<String> iterator = valueNode.fieldNames();
        while (iterator.hasNext()) {
            String childKey = iterator.next();
            JsonNode childNode = valueNode.findPath(childKey);
            if (childNode.isValueNode()) {
                context.appendValueNode(childKey, getNodeValue(childNode), objectNode.getId());
            }
            if (childNode.isArray()) {
                appendArrayNode(childKey, childNode, context, objectNode.getId());
            }
            if (childNode.isObject()) {
                appendObjectNode(childKey, childNode, context, objectNode.getId());
            }
        }
    }

    private List<Object> node2List(SpreadNode nodeIsList, Map<Integer, List<SpreadNode>> parentIdNodes) {
        List<Object> nodeValue = new ArrayList<>();
        List<SpreadNode> arrayNodeList = parentIdNodes.get(nodeIsList.getId());
        if (arrayNodeList != null && !arrayNodeList.isEmpty()) {
            for (SpreadNode childNode : arrayNodeList) {
                if (childNode.getValueNode()) {
                    nodeValue.add(childNode.getValue());
                }
                if (childNode.getArrayNode()) {
                    nodeValue.add(node2List(childNode, parentIdNodes));
                }
                if (childNode.getObjectNode()) {
                    nodeValue.add(node2Object(childNode, parentIdNodes));
                }
            }
        }
        return nodeValue;
    }

    private Map<String, Object> node2Object(SpreadNode nodeIsObject,
                                            Map<Integer, List<SpreadNode>> parentIdNodes) {
        Map<String, Object> nodeValue = new HashMap<>();
        List<SpreadNode> fieldNodeList = parentIdNodes.get(nodeIsObject.getId());
        for (SpreadNode node : fieldNodeList) {
            if (node.getValueNode()) {
                nodeValue.put(node.getKey(), node.getValue());
            }
            if (node.getArrayNode()) {
                nodeValue.put(node.getKey(), node2List(node, parentIdNodes));
            }
            if (node.getObjectNode()) {
                nodeValue.put(node.getKey(), node2Object(node, parentIdNodes));
            }
        }
        return nodeValue;
    }

    private String getNodeValue(JsonNode valueNode) {
        if (valueNode.isNull()) {
            return null;
        }
        return valueNode.asText();
    }

}
