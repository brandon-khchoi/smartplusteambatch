/** When your routing table is too long, you can split it into small modules **/

import Setting from '@/components/serviceManage/Setting'
import SettingList from '@/components/serviceManage/SettingList'
import DeliveryDetail from '@/components/serviceManage/delivery/DeliveryDetail'
import ReloadDetail from '@/components/serviceManage/reload/ReloadDetail'

import store from '@/vuex/store'

const userMenuAuth = {}

const requireAuth = () => (from, to, next) => {
  let userSubMenuList = store.getters.getUserMenuSub
  let menuRoute = serviceSettingRouter.name

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

const serviceSettingRouter = {
  path: '/setting',
  component: Setting,
  redirect: '/setting/list',
  beforeEnter: requireAuth(),
  props: {'userMenuAuth': userMenuAuth},
  name: 'setting',
  children: [{
    path: 'list',
    component: SettingList,
    name: 'settinglist'
  },
  {
    path: 'delivery/detail',
    component: DeliveryDetail,
    name: 'deliverydetail',
    props: true
  },
  {
    path: 'reload/detail',
    component: ReloadDetail,
    name: 'reloaddetail',
    props: true
  }
  ]
}

export default serviceSettingRouter
