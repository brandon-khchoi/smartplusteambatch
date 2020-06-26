import axios from '../http-commons'

const getUserInfo = (uid, password) => {
  return axios.post('/login/loginsubmit', {
    params: {
      'uid': uid,
      'password': password
    }
  })
}
const delUserInfo = (uid) => {
  return axios.get('/login/logout', {
    params: {
      'uid': uid
    }
  })
}

export default {
  async login (uid, password) {
    try {
      const user = await getUserInfo(uid, password)
      const userInfoResponse = user.data
      return userInfoResponse
    } catch (err) {
      console.error(err)
    }
  },
  async logout (uid) {
    try {
      const logoutResponse = await delUserInfo(uid)
      return logoutResponse
    } catch (err) {
      console.error(err)
    }
  }

}
