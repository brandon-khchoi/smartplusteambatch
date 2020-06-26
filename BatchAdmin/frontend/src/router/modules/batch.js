
import BatchManage from '@/components/batch/BatchManage'
import BatchManageList from '@/components/batch/BatchManageList'
import store from '@/vuex/store'

const userMenuAuth = {}
const requireAuth = () => (from, to, next) => {
  let userSubMenuList = store.getters.getUserMenuSub
  let menuRoute = batchRouter.name

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

const batchRouter = {
  path: '/batch/BatchManage',
  component: BatchManage,
  redirect: '/batch/BatchManage/list',
  beforeEnter: requireAuth(),
  props: {'userMenuAuth': userMenuAuth},
  name: 'batch',
  children: [{
    path: 'list',
    component: BatchManageList,
    name: 'batchManage'
    // props: makeProps
  }]
}

export default batchRouter
