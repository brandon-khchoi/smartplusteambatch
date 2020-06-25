// 선언된 이름으로 메소드 사용됨
// ex) this.$store.getters.getUser
export default {
  getUser: state => state.user,
  getUserMenuMain: state => state.userMenuMain,
  getUserMenuSub: state => state.userMenuSub,
  getErrorState: state => state.errorState,
  getIsAuth: state => state.isAuth,
  getRouteProps: state => state.routeProps
}
