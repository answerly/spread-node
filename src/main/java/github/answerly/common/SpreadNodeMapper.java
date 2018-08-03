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
            spreadNode(treeNode, context, Constants.ROOT_ID);
        }
        return context.getSpreadNodes();
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

        TreeNode treeNode = aggregateNode(objectSpreadNode, parentIdNodes);
        return convert(treeNode, type);
    }

    private void spreadNode(TreeNode treeNode, MapperContext context, int parentId) {
        SpreadNode newParentNode = context.appendSpreadNode(treeNode, parentId);
        int newParentId = newParentNode.getId();
        Iterator<TreeNode> iterator = treeNode.elements();
        while (iterator.hasNext()) {
            TreeNode childNode = iterator.next();
            spreadNode(childNode, context, newParentId);
        }
    }

    private TreeNode aggregateNode(SpreadNode parentSpreadNode,
                                   Map<Integer, List<SpreadNode>> parentIdNodes) {
        TreeNode parentTreeNode = createTreeNode(parentSpreadNode);
        List<SpreadNode> childSpreadNodes = parentIdNodes.get(parentSpreadNode.getId());
        if (childSpreadNodes != null && !childSpreadNodes.isEmpty()) {
            for (SpreadNode childSpreadNode : childSpreadNodes) {
                TreeNode value = aggregateNode(childSpreadNode, parentIdNodes);
                parentTreeNode.append(value);
            }
        }
        return parentTreeNode;
    }

    /**
     * object 2 TreeNode
     *
     * @param instance
     * @return
     */
    protected abstract TreeNode getRootTreeNode(Object instance);

    /**
     * create tree node from spread node
     *
     * @param spreadNode
     * @return
     */
    protected abstract TreeNode createTreeNode(SpreadNode spreadNode);

    /**
     * TreeNode to instance
     *
     * @param treeNode
     * @param type
     * @return
     */
    protected abstract <T> T convert(TreeNode treeNode, Class<T> type);
}
