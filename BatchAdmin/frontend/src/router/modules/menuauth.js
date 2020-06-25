/** When your routing table is too long, you can split it into small modules **/

import MenuAuth from '@/components/employ/MenuAuth'

import store from '@/vuex/store'

const userMenuAuth = {}

const requireAuth = () => (from, to, next) => {
  let userSubMenuList = store.getters.getUserMenuSub
  let menuRoute = menuauthRouter.name

  let auth = false
  userSubMenuList.forEach(element => {
    if (element.menu_url_addr === menuRoute) {
      auth = true
      let menuAuth = element.auth_val
      userMenuAuth['menu_display'] = (menuAuth & 1) === 1
      userMenuAuth['retrieve_list'] = (menuAuth & 2) === 2
      userMenuAuth['retrieve_detail'] = (menuAuth & 4) === 4
      userMenuAuth['create_data'] = (menuAuth & 8) === 8
      userMenuAuth['modify_data'] = (menuAuth & 16) === 16
      userMenuAuth['delete_data'] = (menuAuth & 32) === 32
      userMenuAuth['excl_down'] = (menuAuth & 64) === 64
    }
  })
  if (auth) {
    return next()
  }
  history.back()
}

// import http from '../../http-commons'

/** 동적으로 데이터를 props 로 넘기고 싶은 상황에 사용, 이렇게 안하면 param으로만 props 를 넘길수 있음  */
// function makeProps() {
//     return {
//         data: [],
//     }
// }
// 계정정보를  user에 해당되는 component 는 다 들고 있어야 함
// store 로 각 component 에서 데이터 가져올지 여기서 데이터를 가져와서 props로 넘길지 고민해보자
const menuauthRouter = {
  path: '/account/menuauth/list',
  component: MenuAuth,
  // redirect: '/account/menuauth/list',
  beforeEnter: requireAuth(),
  props: {'userMenuAuth': userMenuAuth},
  name: 'menuauth'
}
export default menuauthRouter
