/** When your routing table is too long, you can split it into small modules **/

import Sales from '@/components/sales/Sales'
import SalesList from '@/components/sales/SalesList'
import SalesCreate from '@/components/sales/SalesCreate'
import SalesEdit from '@/components/sales/SalesEdit'

import store from '@/vuex/store'

const userMenuAuth = {}

const requireAuth = () => (from, to, next) => {
  let userSubMenuList = store.getters.getUserMenuSub
  let menuRoute = salesRouter.name

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

const salesRouter = {
  path: '/sales/sales',
  component: Sales,
  redirect: '/sales/sales/list',
  beforeEnter: requireAuth(),
  props: {'userMenuAuth': userMenuAuth},
  name: 'sales',
  children: [{
    path: 'list',
    component: SalesList,
    name: 'saleslist'
    // props: makeProps
  },
  {
    path: 'create',
    component: SalesCreate,
    name: 'salescreate'
    // props: makeProps
  },
  {
    path: 'edit',
    component: SalesEdit,
    name: 'salesedit'
    // props: makeProps
  }
  ]
}
export default salesRouter
