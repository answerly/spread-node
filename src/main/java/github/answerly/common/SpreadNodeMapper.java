package github.answerly.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import github.answerly.common.protocol.TreeNode;

/**
 * base spread node mapper
 *
 * @author liuyan
 */
public abstract class SpreadNodeMapper {

    public List<SpreadNode> spread(Object instance) {
        TreeNode rootTreeNode = getRootTreeNode(instance);
        if (!rootTreeNode.isObject()) {
            throw new IllegalArgumentException("object must is struct");
        }
        MapperContext context = new MapperContext();
        Iterator<TreeNode> iterator = rootTreeNode.elements();
        while (iterator.hasNext()) {
            TreeNode treeNode = iterator.next();
            appendSpreadNode(treeNode, context, Constants.ROOT_ID);
        }
        return context.getSpreadNodes();
    }

    private void appendSpreadNode(TreeNode treeNode, MapperContext context, int parentId) {
        SpreadNode newParentNode = context.appendSpreadNode(treeNode, parentId);
        int newParentId = newParentNode.getId();
        Iterator<TreeNode> iterator = treeNode.elements();
        while (iterator.hasNext()) {
            TreeNode childNode = iterator.next();
            if (childNode.isValue()) {
                appendSpreadNode(childNode, context, newParentId);
            }
            if (childNode.isArray()) {
                appendSpreadNode(childNode, context, newParentId);
            }
            if (childNode.isObject()) {
                appendSpreadNode(childNode, context, newParentId);
            }
        }
    }

    public <T> T aggregate(List<SpreadNode> spreadNodes, Class<T> type) {
        Map<Integer, List<SpreadNode>> parentIdNodes = new HashMap<>(8);
        for (SpreadNode spreadNode : spreadNodes) {
            Integer parentId = spreadNode.getParentId();
            List<SpreadNode> parentSpreadNodes = parentIdNodes.get(parentId);
            if (parentSpreadNodes == null) {
                parentIdNodes.put(parentId, (parentSpreadNodes = new ArrayList<>()));
            }
            parentSpreadNodes.add(spreadNode);
        }
        SpreadNode objectSpreadNode = new SpreadNode();
        objectSpreadNode.setId(Constants.ROOT_ID);
        objectSpreadNode.setValueNode(false);
        objectSpreadNode.setArrayNode(false);
        objectSpreadNode.setObjectNode(true);

        Map<String, Object> objectTree = aggregateObject(objectSpreadNode, parentIdNodes);
        return JsonNodeUtils.getObjectByTree(objectTree, type);
    }

    private Map<String, Object> aggregateObject(SpreadNode nodeIsObject,
                                                Map<Integer, List<SpreadNode>> parentIdNodes) {
        Map<String, Object> spreadNodeValue = new HashMap<>(8);
        List<SpreadNode> childSpreadNodes = parentIdNodes.get(nodeIsObject.getId());
        for (SpreadNode childSpreadNode : childSpreadNodes) {
            String key = childSpreadNode.getKey();
            if (childSpreadNode.getValueNode()) {
                Object value = childSpreadNode.getValue();
                spreadNodeValue.put(key, value);
            }
            if (childSpreadNode.getArrayNode()) {
                List<Object> value = aggregateArray(childSpreadNode, parentIdNodes);
                spreadNodeValue.put(key, value);
            }
            if (childSpreadNode.getObjectNode()) {
                Map<String, Object> value = aggregateObject(childSpreadNode, parentIdNodes);
                spreadNodeValue.put(key, value);
            }
        }
        return spreadNodeValue;
    }

    private List<Object> aggregateArray(SpreadNode nodeIsArray, Map<Integer, List<SpreadNode>> parentIdNodes) {
        List<Object> nodeValue = new ArrayList<>();
        List<SpreadNode> arrayNodeList = parentIdNodes.get(nodeIsArray.getId());
        if (arrayNodeList != null && !arrayNodeList.isEmpty()) {
            for (SpreadNode childNode : arrayNodeList) {
                if (childNode.getValueNode()) {
                    nodeValue.add(childNode.getValue());
                }
                if (childNode.getArrayNode()) {
                    nodeValue.add(aggregateArray(childNode, parentIdNodes));
                }
                if (childNode.getObjectNode()) {
                    nodeValue.add(aggregateObject(childNode, parentIdNodes));
                }
            }
        }
        return nodeValue;
    }

    /**
     * object 2 TreeNode
     *
     * @param instance
     * @return
     */
    protected abstract TreeNode getRootTreeNode(Object instance);

    /**
     * TreeNode to instance
     *
     * @param treeNode
     * @return
     */
    protected abstract <T> T convert(TreeNode treeNode);
}
