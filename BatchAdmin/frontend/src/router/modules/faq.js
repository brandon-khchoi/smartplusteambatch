/** When your routing table is too long, you can split it into small modules **/

import Faq from '@/components/contentsManage/faq/Faq'
import FaqList from '@/components/contentsManage/faq/FaqList'
import FaqCreate from '@/components/contentsManage/faq/FaqCreate'
import FaqDetail from '@/components/contentsManage/faq/FaqDetail'
import FaqEdit from '@/components/contentsManage/faq/FaqEdit'

import store from '@/vuex/store'

const userMenuAuth = {}

const requireAuth = () => (from, to, next) => {
  let userSubMenuList = store.getters.getUserMenuSub
  let menuRoute = faqRouter.name

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

const faqRouter = {
  path: '/faq',
  component: Faq,
  redirect: '/faq/list',
  beforeEnter: requireAuth(),
  props: {'userMenuAuth': userMenuAuth},
  name: 'faqinfo',
  children: [{
    path: 'list',
    component: FaqList,
    name: 'faqlist'
    // props: makeProps
  },
  {
    path: 'create',
    component: FaqCreate,
    name: 'faqcreate'
    // props: makeProps
  },
  {
    path: 'detail',
    component: FaqDetail,
    name: 'faqdetail'
    // props: makeProps
  },
  {
    path: 'edit',
    component: FaqEdit,
    name: 'faqedit'
    // props: makeProps
  }
  ]
}
export default faqRouter
