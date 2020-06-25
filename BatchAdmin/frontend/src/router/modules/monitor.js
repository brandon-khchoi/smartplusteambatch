
import Monitor from '@/components/monitor/Monitor'
import LogList from '@/components/monitor/LogList'
import HistrixList from '@/components/monitor/HistrixList'
import KafkaList from '@/components/monitor/KafkaList'
import store from '@/vuex/store'
const userMenuAuth = {}
const requireAuth = () => (from, to, next) => {
  let userSubMenuList = store.getters.getUserMenuSub
  let menuRoute = monitorRouter.name
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

const monitorRouter = {
  path: '/monitor',
  component: Monitor,
  redirect: '/monitor/mall/loglist',
  beforeEnter: requireAuth(),
  props: {'userMenuAuth': userMenuAuth},
  name: 'monitor',
  children: [{
    path: 'mall/loglist',
    component: LogList,
    name: 'mallloglist'
  },
  {
    path: 'histirx',
    component: HistrixList,
    name: 'histirxlist'
  },
  {
    path: 'kafka',
    component: KafkaList,
    name: 'kafkalist'
  }
  ]
}

export default monitorRouter
