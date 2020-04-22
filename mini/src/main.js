import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN' // lang i18n

import '@/styles/index.scss' // global css
import '@/styles/public.scss'

import App from './App'
import store from './store'
import router from './router'

import '@/icons' // icon
import '@/permission' // permission control
import  amountInput from   '@/components/AmountInput/install'
import  selfDatepick from  '@/components/datePickRange/install'

/**
 * If you don't want to use mock-server
 * you want to use mockjs for request interception
 * you can execute:
 *
 * import { mockXHR } from '../mock'
 * mockXHR()
 */

/**
 * 将mathjs 添加到全局服务
 */
import mathjs from '@/components/Math/index.js'
import print from 'vue-print-nb'
import elDragDialog from '@/directive/el-drag-dialog/index'

// set ElementUI lang to EN
Vue.use(amountInput)
Vue.use(selfDatepick)
Vue.use(mathjs)
Vue.use(print)
Vue.use(elDragDialog)
Vue.use(ElementUI, { locale })

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
