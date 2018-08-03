package github.answerly.common.jackson;

import java.util.List;

import github.answerly.common.SpreadNode;
import github.answerly.common.SpreadNodeMapper;
import junit.framework.TestCase;

/**
 * jackson impl test
 *
 * @author liuyan
 * @since 1.0.0
 */
public class JacksonSpreadNodeMapperTest extends TestCase {

    SpreadNodeMapper spreadNodeMapper = new JacksonSpreadNodeMapper();

    public void testSpreadAndAggregate() {
        Object instance = null;
        List<SpreadNode> spreadNodeList = spreadNodeMapper.spread(null);
        System.out.println(spreadNodeList);
    }

    static class Person {

        private String name;

        private Integer age;

        private Person wife;

        private List<Person> children;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Person getWife() {
            return wife;
        }

        public void setWife(Person wife) {
            this.wife = wife;
        }

        public List<Person> getChildren() {
            return children;
        }

        public void setChildren(List<Person> children) {
            this.children = children;
        }
    }
}
