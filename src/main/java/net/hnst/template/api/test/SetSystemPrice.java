package net.hnst.template.api.test;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import net.hnst.template.api.IdEnable;
import net.hnst.template.api.pagination.AdvancedPage;

import java.io.Serializable;


@Setter
@Getter
@ToString
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class SetSystemPrice extends AdvancedPage implements Serializable,IdEnable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;
    /**
     * 系统id
     */
    private String systemId;
    /**
     * 商品id
     */
    private String commondityId;
    /**
     * 价格
     */
    private Integer price;
}
