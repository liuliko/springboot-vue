package net.hnst.template.controller;


import net.hnst.template.api.vue.SysMenu;
import net.hnst.template.api.vue.VueMenu;
import net.hnst.template.service.BaseServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping
public class LoginController extends BaseController {


    public static final String menuSplit = "  -  ";


    @RequestMapping(value = "user/login")
    @ResponseBody
    public Object login(String username, String password, HttpSession session) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return getMessage(-1);
        }

        // return getMessage(BaseServiceImpl.getFailMapWithMessage("当前账号已经被冻结,请联系管理员"));

        return getMessage(BaseServiceImpl.getOkMapWithData(""));
    }


    @RequestMapping("user/menu")
    @ResponseBody
    public Object getSysMenu(HttpSession session) {


        /**
         * 首页
         */
        VueMenu dashboard = VueMenu.builder().component("Layout").path("/").redirect("/dashboard").name("index").children(new ArrayList<>()).meta(new VueMenu.Meta("el-icon-menu", "首页", "首页")).build();

        dashboard.setChildren(new ArrayList<>());

        VueMenu.Meta dashboardMeta = new VueMenu.Meta("el-icon-menu", "首页", "首页");
        dashboardMeta.setAffix(true);
        dashboardMeta.setNoCache(true);


        dashboard.getChildren().add(VueMenu.builder().name("dashboard-index").path("/dashboard").component("dashboard/index").children(new ArrayList<>()).meta(dashboardMeta).build());


        ArrayList list = new ArrayList();
        list.add(dashboard);


        //用户权限
        List<SysMenu> sysMenus = new ArrayList<>();



        sysMenus.add(SysMenu.builder().id("dcd7f8c1-b0c1-499a-989e-d75f06661e49").code("test/setSystemPrice/index").icon("el-icon-menu").parentId("d35fb472-e4e5-4f5e-81c1-fb4cf556fc81").name("test").path("test/setSystemPrice/index").type(2).parentName("系统管理").sort(1).build());
        sysMenus.add(SysMenu.builder().id("d35fb472-e4e5-4f5e-81c1-fb4cf556fc81").code("sys").icon("el-icon-menu").parentId("0").name("系统管理").path("/sys").type(1).parentName("0").sort(1).build());


        sysMenus.sort(Comparator.comparing(SysMenu::getSort));
        for (SysMenu sysMenu : sysMenus) {
            if (sysMenu.getParentId().equals("0")) {
                VueMenu level1 = VueMenu.builder().component("Layout").redirect("/").path(sysMenu.getPath()).children(new ArrayList<>()).meta(new VueMenu.Meta(sysMenu.getIcon(), sysMenu.getName(), sysMenu.getName())).build();
                setChildren(sysMenu, level1, sysMenus);
                list.add(level1);
            }
        }


        VueMenu vue404 = VueMenu.builder().path("*").redirect("/404").component("Layout").hidden(true).meta(new VueMenu.Meta("", "404", "404")).build();

        list.add(vue404);

        List<String> roles = new ArrayList<>();
        roles.add("admin");

        Map info = new HashMap();
        info.put("roles", roles);
        info.put("name", "someone");
        info.put("menus", list);

        return getMessage(info);
    }

    private void setChildren(SysMenu sysMenu, VueMenu vueMenu, List<SysMenu> vmenus) {
        List<VueMenu> vueMenus = new ArrayList<>();
        vueMenu.setChildren(vueMenus);
        for (SysMenu temp : vmenus) {
            if (temp.getParentId().equals(sysMenu.getId()) && temp.getType() != 3) {
                if (temp.getType() == 1) {
                    VueMenu layout = VueMenu.builder().component("Layout").redirect("/").path(temp.getPath()).children(new ArrayList<>()).meta(new VueMenu.Meta(temp.getIcon(), temp.getName(), vueMenu.getMeta().getFullName() + menuSplit + temp.getName())).build();
                    vueMenus.add(layout);
                    setChildren(temp, layout, vmenus);
                } else {
                    VueMenu m = VueMenu.builder().name(temp.getPath().replaceAll("/", "-")).component(temp.getPath()).path(temp.getPath()).children(new ArrayList<>()).meta(new VueMenu.Meta(temp.getIcon(), temp.getName(), vueMenu.getMeta().getFullName() + menuSplit + temp.getName())).build();
                    vueMenus.add(m);
                }
            }
        }
    }


    @RequestMapping(value = "user/logout")
    @ResponseBody
    public Object logout(HttpSession session) {
        session.invalidate();
        return getMessage("");
    }

}
