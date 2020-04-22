package net.hnst.template.api.vue;

import lombok.*;
import lombok.extern.slf4j.Slf4j;


import java.io.Serializable;


@Setter
@Getter
@ToString
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 路径
     */
    private String path;
    /**
     * 代码
     */
    private String code;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 图标
     */
    private String icon;
    /**
     * 父id
     */
    private String parentId;
    /**
     * 父name
     */
    private String parentName;
    /**
     * 类型  1 节点 2 菜单
     *
     */
    private Integer type;
    /**
     * 級別
     */
    private Integer level;
}
