import axios from 'axios'

let getBaseServerUrl = function () {
  let adminServerUrl = ''
  if (location.host === 'ladmin.lunasoft.co.kr') { // 리얼환경
    adminServerUrl = 'https://ladminapi.lunasoft.co.kr'
  } else if (location.host === 'teladmin.lunasoft.co.kr') { // TEST
    // adminServerUrl = 'https://teladminapi.lunasoft.co.kr'
    adminServerUrl = 'https://teladminapi.lunasoft.co.kr'
    // adminServerUrl = 'http://192.168.0.163:8085'
  } else { //  로컬 도메인
    // adminServerUrl = 'https://teladminapi.lunasoft.co.kr'
    adminServerUrl = 'http://localhost:8080'
  }
  return adminServerUrl
}

let asiosInstance = axios.create({
  baseURL: getBaseServerUrl(),
  headers: {
    'Content-type': 'application/json'
  },
  timeout: 60000
})

asiosInstance.interceptors.request.use(function (config) {
  // Do something before request is sent
  config.headers['user_code'] = 1
  // eslint-disable-next-line no-undef
  let userNo = 0
  let menuGroupNo = 0
  let menuMainNo = 0
  let menuSubNo = 0
  let reqKey = ''
  if (localStorage.getItem('USER') !== null) {
    userNo = JSON.parse(localStorage.getItem('USER')).luna_no
    menuMainNo = localStorage.getItem('selectedMainIndex')
    menuSubNo = localStorage.getItem('selectedSubIndex')
    let mMenu = JSON.parse(localStorage.getItem('USER_MENU_MAIN'))
    // eslint-disable-next-line eqeqeq
    let menuGroup = mMenu.filter(item => { return item.menu_main_no == menuMainNo })
    menuGroupNo = menuGroup.length === 0 ? null : menuGroup[0].menu_group_no

    let reqDate = new Date()
    reqKey = reqDate.toLocaleDateString().replace(/\s|\./g, '') + '' +
             reqDate.getHours() + '' +
             reqDate.getMinutes() + '' +
             reqDate.getSeconds() + '' +
             reqDate.getMilliseconds()
  }
  config.headers['user_no'] = userNo
  config.headers['menu_group_no'] = menuGroupNo
  config.headers['menu_main_no'] = menuMainNo
  config.headers['menu_sub_no'] = menuSubNo
  config.headers['request_id'] = reqKey
  return config
}, function (error) {
  // Do something with request error
  return error
})

asiosInstance.interceptors.response.use(function (response) {
  // Any status code that lie within the range of 2xx cause this function to trigger
  // Do something with response data
  return response
}, function (error) {
  // Any status codes that falls outside the range of 2xx cause this function to trigger
  // Do something with response error
  if (error.response && error.response.status === 502) {
    alert('서버 재시작 중입니다.\n10초 뒤에 리프레쉬 이후 사용해주세요.')
  }
  return Promise.reject(error)
})

export default asiosInstance
// timeout 설정을 해야 timeout시에 catch 문에 e.code에 'ECONNABORTED' 로 값 생성됨!!
