import loginAPI from './loginAPI'

export default {
  async login (uid, password) {
    try {
      const loginResponse = await loginAPI.login(uid, password)
      return loginResponse
    } catch (err) {
      console.error(err)
    }
  },
  async logout (uid) {
    try {
      const logoutResponse = await loginAPI.logout(uid)
      return logoutResponse
    } catch (err) {
      console.error(err)
    }
  }
}
