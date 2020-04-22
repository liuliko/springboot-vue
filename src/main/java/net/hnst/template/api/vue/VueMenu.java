package net.hnst.template.api.vue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VueMenu {

    private String redirect;

    private String path;

    private String component;

    private List<VueMenu> children;

    private Meta meta;

    private String name;

    private boolean hidden;

    private boolean alwaysShow;

    public static class Meta {

        public Meta(String icon, String title,String fullName) {
            this.icon = icon;
            this.title = title;
            this.noCache=false;
            this.affix=false;
            this.fullName=fullName;
        }

        public boolean getNoCache() {
            return noCache;
        }

        public void setNoCache(boolean noCache) {
            this.noCache = noCache;
        }

        public boolean getAffix() {
            return affix;
        }

        public void setAffix(boolean affix) {
            this.affix = affix;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        private String icon;

        private String title;

        private Boolean noCache;

        private Boolean affix;

        private String fullName;
    }
}
