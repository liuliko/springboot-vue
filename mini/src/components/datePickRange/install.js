import dateRangePick from "./index"
import simple from "./simple";

export default {
  install: function (Vue) {
    Vue.component("el-date-pick-range", dateRangePick)
    Vue.component("el-date-simple-range", simple)
  }
}
