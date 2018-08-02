package github.answerly.common.protocol;

import java.util.Iterator;

/**
 * tree node
 *
 * @author liuyan
 * @since 1.0.0
 */
public interface TreeNode {

    /**
     * node key if node is child of array
     */
    static final String ARRAY_ELEMENT_KEY = "";

    /**
     * base struct, eg int string
     *
     * @return
     */
    boolean isValue();

    /**
     * is struct object
     *
     * @return
     */
    boolean isObject();

    /**
     * is array or collection
     *
     * @return
     */
    boolean isArray();

    /**
     * node key
     *
     * @return
     */
    String getKey();

    /**
     * node value
     *
     * @return
     */
    String getValue();

    /**
     * list of sub node or list of filed node
     *
     * @return
     */
    Iterator<TreeNode> elements();
}
