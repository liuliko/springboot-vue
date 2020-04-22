package net.hnst.template.api.vue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VueTree<T> {

    private String nodeKey;

    private String name;

    private boolean leaf; //是否叶子节点

    private T bindData;

    private String iconClass;

    private List<VueTree> children;
}
