package github.answerly.common.protocol;

import java.util.Iterator;

/**
 * @author liuyan
 * @date 2018/8/2
 * @since 1.0.0
 */
public class TreeNodeSupport implements TreeNode {

    @Override
    public boolean isValue() {
        return false;
    }

    @Override
    public boolean isObject() {
        return false;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getValue() {
        return null;
    }

    @Override
    public Iterator<TreeNode> elements() {
        return null;
    }
}
