/** When your routing table is too long, you can split it into small modules **/

import Account from '@/components/employ/Account'
import AccountList from '@/components/employ/AccountList'
import AccountCreate from '@/components/employ/AccountCreate'
import AccountEdit from '@/components/employ/AccountEdit'

import store from '@/vuex/store'

const userMenuAuth = {}

const requireAuth = () => (from, to, next) => {
  let userSubMenuList = store.getters.getUserMenuSub
  let menuRoute = acconuntRouter.name

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

const acconuntRouter = {
  path: '/account/account',
  component: Account,
  redirect: '/account/account/list',
  beforeEnter: requireAuth(),
  props: {'userMenuAuth': userMenuAuth},
  name: 'account',
  children: [{
    path: 'list',
    component: AccountList,
    name: 'accountlist'
    // props: makeProps
  },
  {
    path: 'create',
    component: AccountCreate,
    name: 'accountcreate'
    // props: makeProps
  },
  {
    path: 'edit',
    component: AccountEdit,
    name: 'accountedit'
    // props: makeProps
  }
  ]
}
export default acconuntRouter
