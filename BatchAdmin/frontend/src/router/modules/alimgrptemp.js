/** When your routing table is too long, you can split it into small modules **/

import AlimGrpTemp from '@/components/serviceDefaultManage/alimGroupTemplate/AlimGrpTemp'
import AlimGrpTempList from '@/components/serviceDefaultManage/alimGroupTemplate/AlimGrpTempList'
import AlimGrpTempCreate from '@/components/serviceDefaultManage/alimGroupTemplate/AlimGrpTempCreate'

import store from '@/vuex/store'

const userMenuAuth = {}

const requireAuth = () => (from, to, next) => {
  let userSubMenuList = store.getters.getUserMenuSub
  let menuRoute = alimgrptempRouter.name

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

const alimgrptempRouter = {
  path: '/service/alimgrptemp',
  component: AlimGrpTemp,
  redirect: '/service/alimgrptemp/list',
  beforeEnter: requireAuth(),
  props: {'userMenuAuth': userMenuAuth},
  name: 'alimgrptemp',
  children: [{
    path: 'list',
    component: AlimGrpTempList,
    name: 'alimgrptemplist'
    // props: makeProps
  },
  {
    path: 'create',
    component: AlimGrpTempCreate,
    name: 'alimgrptempcreate'
    // props: makeProps
  }
  ]
}
export default alimgrptempRouter
