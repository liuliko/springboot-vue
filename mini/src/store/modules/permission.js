import {asyncRoutes, constantRoutes} from '@/router'
import Layout from '@/layout'

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, promiseTasks) {

  const accessedRouters = asyncRouterMap.filter(route => {
    if (route.component) {
      if (route.component === 'Layout') { // Layout组件特殊处理
        route.component = Layout
      } else {
        try {
          //require 同步
          //import 异步
          promiseTasks.push(import(`@/views/${route.component}.vue`)
            .then(component => {
                try {
                  component.default.name = route.name;
                } catch (e) {
                }
                route.component = component.default
              }
            ).catch((e) => {
              console.log(e);
              route.component = undefined
            }))
          //使用 require 方法 ,可以同步完成. 不再需要异步处理,但是import 异步处理效率会快一些
          // route.component =require(`@/views/${route.component}.vue`).default
        } catch (e) {
          route.component = undefined
        }
      }
    }
    if (route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, promiseTasks)
    }
    return true
  })

  return accessedRouters
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = {...route}
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({commit}, roles) {
    return new Promise(resolve => {
      let accessedRoutes
      var promiseTasks = []
      accessedRoutes = filterAsyncRouter(roles, promiseTasks)
      Promise.all(promiseTasks).then(() => {
        //页面全部import完成后 ,执行 SET_ROUTES
        commit('SET_ROUTES', accessedRoutes)
        resolve(accessedRoutes)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
