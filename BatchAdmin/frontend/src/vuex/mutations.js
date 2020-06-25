import * as types from './mutation_types'

export default {
  [types.USER] (state, user) {
    state.user = user
  },
  [types.USER_MENU_MAIN] (state, userMenuMain) {
    state.userMenuMain = userMenuMain
  },
  [types.USER_MENU_SUB] (state, userMenuSub) {
    state.userMenuSub = userMenuSub
  },
  [types.ERROR_STATE] (state, errorState) {
    state.errorState = errorState
  },
  [types.IS_AUTH] (state, isAuth) {
    state.isAuth = isAuth
  },
  [types.ROUTE_PROPS] (state, routeProps) {
    state.routeProps = routeProps
  }
}
