import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import actions from './actions'
import mutations from './mutations'

Vue.use(Vuex)

const state = {
  user: localStorage.getItem('USER') == null ? '' : JSON.parse(localStorage.getItem('USER')),
  userMenuMain: localStorage.getItem('USER_MENU_MAIN') == null ? '' : JSON.parse(localStorage.getItem('USER_MENU_MAIN')),
  userMenuSub: localStorage.getItem('USER_MENU_SUB') == null ? '' : JSON.parse(localStorage.getItem('USER_MENU_SUB')),
  errorState: localStorage.getItem('ERROR_STATE') == null ? '' : localStorage.getItem('ERROR_STATE'),
  isAuth: localStorage.getItem('IS_AUTH') == null ? false : localStorage.getItem('IS_AUTH') === 'true',
  routeProps: localStorage.getItem('ROUTE_PROPS') == null ? '' : JSON.parse(localStorage.getItem('ROUTE_PROPS'))
}

export default new Vuex.Store({
  state,
  mutations,
  getters,
  actions
})
