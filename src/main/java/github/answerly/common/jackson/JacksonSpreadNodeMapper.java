package github.answerly.common.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.TextNode;
import github.answerly.common.Constants;
import github.answerly.common.SpreadNode;
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
        JsonNode inner;
        try {
            String jsonStr = OBJECT_MAPPER.writeValueAsString(instance);
            inner = OBJECT_MAPPER.readTree(jsonStr);
        } catch (JsonProcessingException ignore) {
            // maybe if circle ref
            throw new IllegalArgumentException(ignore);
        } catch (IOException ignore) {
            // maybe if circle ref
            throw new IllegalArgumentException(ignore);
        }
        return new JacksonTreeNode(key, inner);
    }

    @Override
    protected TreeNode createTreeNode(SpreadNode spreadNode) {
        String key = spreadNode.getKey();
        if (spreadNode.getValueNode() && null == spreadNode.getValue()) {
            return new JacksonTreeNode(key, NullNode.getInstance());
        }
        if (spreadNode.getValueNode() && null != spreadNode.getValue()) {
            return new JacksonTreeNode(key, TextNode.valueOf(spreadNode.getValue()));
        }
        if (spreadNode.getObjectNode()) {
            return new JacksonTreeNode(key, OBJECT_MAPPER.createObjectNode());
        }
        if (spreadNode.getArrayNode()) {
            return new JacksonTreeNode(key, OBJECT_MAPPER.createArrayNode());
        }
        return null;
    }

    @Override
    protected <T> T convert(TreeNode treeNode, Class<T> type) {
        JacksonTreeNode jacksonTreeNode = (JacksonTreeNode)treeNode;
        return OBJECT_MAPPER.convertValue(jacksonTreeNode.getInner(), type);
    }

}
