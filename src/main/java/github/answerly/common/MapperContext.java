package github.answerly.common;

import java.util.ArrayList;
import java.util.List;

import github.answerly.common.protocol.TreeNode;

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

    SpreadNode appendSpreadNode(TreeNode treeNode, int parentId) {
        SpreadNode spreadNode = new SpreadNode();
        spreadNode.setId(nextId());
        spreadNode.setParentId(parentId);
        spreadNode.setKey(treeNode.getKey());
        spreadNode.setValue(treeNode.getValue());
        spreadNode.setValueNode(treeNode.isValue());
        spreadNode.setArrayNode(treeNode.isArray());
        spreadNode.setObjectNode(treeNode.isObject());
        this.nodeList.add(spreadNode);
        return spreadNode;
    }

    int nextId() {
        return idGenerator++;
    }

    public List<SpreadNode> getSpreadNodes() {
        return nodeList;
    }
}
