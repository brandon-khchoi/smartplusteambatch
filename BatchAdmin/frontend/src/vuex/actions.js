import { USER, USER_MENU_MAIN, USER_MENU_SUB, IS_AUTH, ERROR_STATE, ROUTE_PROPS } from './mutation_types'
import api from '../service'

let setUser = ({ commit }, data) => {
  commit(USER, data)
  localStorage.setItem('USER', JSON.stringify(data))
}

let setUserMenuMain = ({ commit }, data) => {
  commit(USER_MENU_MAIN, data)
  localStorage.setItem('USER_MENU_MAIN', JSON.stringify(data))
}

let setUserMenuSub = ({ commit }, data) => {
  commit(USER_MENU_SUB, data)
  localStorage.setItem('USER_MENU_SUB', JSON.stringify(data))
}

let setErrorState = ({ commit }, data) => {
  commit(ERROR_STATE, data)
  localStorage.setItem('ERROR_STATE', JSON.stringify(data))
}

let setIsAuth = ({ commit }, data) => {
  commit(IS_AUTH, data)
  localStorage.setItem('IS_AUTH', data)
}

let processInResponse = (store, loginResponse) => {
  if (loginResponse.loginFailMsg) {
    setErrorState(store, loginResponse.loginFailMsg)
  } else {
    let user = JSON.parse(JSON.stringify(loginResponse.user))
    let userMenuMain = JSON.parse(JSON.stringify(loginResponse.userMenuMain))
    let userMenuSub = JSON.parse(JSON.stringify(loginResponse.userMenuSub))

    let expiredKey = new Date().toLocaleDateString().replace(/\s|\./g, '')
    localStorage.setItem('key', expiredKey)

    setUser(store, user)
    setUserMenuMain(store, userMenuMain)
    setUserMenuSub(store, userMenuSub)
    setErrorState(store, '')
    setIsAuth(store, true)
  }
}

let processOutResponse = ({ commit }, logoutResponse) => {
  localStorage.removeItem('USER')
  commit(USER, '')
  localStorage.removeItem('USER_MENU_MAIN')
  commit(USER_MENU_MAIN, '')
  localStorage.removeItem('USER_MENU_SUB')
  commit(USER_MENU_SUB, '')
  localStorage.removeItem('ERROR_STATE')
  commit(ERROR_STATE, '')
  localStorage.removeItem('IS_AUTH')
  commit(IS_AUTH, false)
  localStorage.removeItem('key')
}

export default {
  async login (store, { uid, password }) {
    let loginResponse = await api.login(uid, password)
    processInResponse(store, loginResponse)
    return store.getters.getIsAuth
  },
  async logout (store, { uid }) {
    let logoutResponse = await api.logout(uid)
    processOutResponse(store, logoutResponse)
    return logoutResponse
  },
  routeProps (store, props) {
    console.log('routeProps')
    store.commit(ROUTE_PROPS, props)
    localStorage.setItem('ROUTE_PROPS', JSON.stringify(props))
    console.log('routeProps end')
    // setRouteProps(store, props)
  },
  loginExpired (store) { // index.js 에서 store.dispatch('loginExpired') 와같은 방식으로 호출함
    let key = localStorage.getItem('key')
    let compareKey = new Date().toLocaleDateString().replace(/\s|\./g, '')

    if (key !== compareKey) {
      processOutResponse(store, null)

      // 선택메뉴 상태 유지값 제거
      localStorage.removeItem('selectedMainIndex')
      localStorage.removeItem('selectedSubIndex')
      localStorage.removeItem('selectedUrl')

      return false
    }
    return true
  }
}
