/** When your routing table is too long, you can split it into small modules **/

import User from '@/components/member/User'
import UserList from '@/components/member/UserList'
import UserCreate from '@/components/member/UserCreate'
import MemberInfo from '@/components/member/MemberInfo'

import store from '@/vuex/store'

const userMenuAuth = {}
const requireAuth = () => (from, to, next) => {
  let userSubMenuList = store.getters.getUserMenuSub
  let menuRoute = memberRouter.name
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

const memberRouter = {
  path: '/member/user',
  component: User,
  redirect: '/member/user/list',
  beforeEnter: requireAuth(),
  props: {'userMenuAuth': userMenuAuth},
  name: 'user',
  children: [{
    path: 'list',
    component: UserList,
    name: 'userlist'
  },
  {
    path: 'create',
    component: UserCreate,
    name: 'usercreate'
  },
  {
    path: 'detail/:member_no',
    component: MemberInfo,
    name: 'memberinfo'
  }

  ]
}

export default memberRouter
