package github.answerly.common;

/**
 * spread node
 *
 * @author liuyan
 */
public class SpreadNode {

    private Integer id;

    private Integer parentId;

    private String key;

    private String value;

    private Boolean valueNode;

    private Boolean arrayNode;

    private Boolean objectNode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getValueNode() {
        return valueNode;
    }

    public void setValueNode(Boolean valueNode) {
        this.valueNode = valueNode;
    }

    public Boolean getArrayNode() {
        return arrayNode;
    }

    public void setArrayNode(Boolean arrayNode) {
        this.arrayNode = arrayNode;
    }

    public Boolean getObjectNode() {
        return objectNode;
    }

    public void setObjectNode(Boolean objectNode) {
        this.objectNode = objectNode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("id=").append(id);
        sb.append(", parentId=").append(parentId);
        sb.append(", key=").append(key);
        sb.append(", value=").append(value);
        sb.append(", valueNode=").append(valueNode);
        sb.append(", objectNode=").append(objectNode);
        sb.append(", arrayNode=").append(arrayNode);
        sb.append("]");
        return sb.toString();
    }
}
