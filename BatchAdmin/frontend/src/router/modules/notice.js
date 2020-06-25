/** When your routing table is too long, you can split it into small modules **/

import Notice from '@/components/contentsManage/notice/Notice.vue'
import NoticeList from '@/components/contentsManage/notice/NoticeList.vue'
import NoticeEdit from '@/components/contentsManage/notice/NoticeEdit.vue'
import NoticeDetail from '@/components/contentsManage/notice/NoticeDetail.vue'

import store from '@/vuex/store'

const userMenuAuth = {}

const requireAuth = () => (from, to, next) => {
  let userSubMenuList = store.getters.getUserMenuSub
  let menuRoute = noticeRouter.name

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

const noticeRouter = {
  path: '/contents/notice/',
  component: Notice,
  redirect: '/contents/notice/list',
  beforeEnter: requireAuth(),
  props: {'userMenuAuth': userMenuAuth},
  name: 'noticeinfo',
  children: [{
    path: 'list',
    component: NoticeList,
    name: 'noticeList'
    // props: makeProps
  },
  {
    path: 'edit',
    component: NoticeEdit,
    name: 'noticeEdit'
    // props: makeProps
  },
  {
    path: 'detail',
    component: NoticeDetail,
    name: 'noticeDetail'
    // props: makeProps
  }
  ]
}
export default noticeRouter
