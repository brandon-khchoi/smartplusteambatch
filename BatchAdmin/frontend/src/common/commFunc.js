/* eslint-disable no-undef */
// pagination
pageMaxNumber = function (data) {
  return data.total_cnt - ((data.current_page - 1) * data.per_page)
}

makePaginate = function (data, showNo1) {
  const res = []

  // 페이지 블록 default -> 10
  if (undefined === data.block) data.block = 10

  if (data.total_cnt > data.per_page || showNo1) {
    // 페이지 계산
    const currentBlock = Math.ceil(data.current_page / data.block) // 현재 블록
    const startPage = (Math.max(currentBlock, 1) - 1) * data.block + 1 // 시작 페이지
    const lastPage = startPage + data.block - 1 // 마지막 페이지
    const lastRoop = Math.min(lastPage, data.last_page)

    res.push({ i: 'prev', href: data.current_page === 1 ? false : Math.max(startPage - 1, 1), active: false, per_page: data.per_page })
    for (let i = Math.max(startPage, 1); i <= lastRoop; i++) {
      res.push({ i: i, href: data.current_page === i ? false : i, active: data.current_page === i, per_page: data.per_page })
    }
    res.push({ i: 'next', href: data.current_page === data.last_page ? false : Math.min(lastPage + 1, data.last_page), active: false, per_page: data.per_page })
  } else {
    res.push({ i: 'prev', href: false, active: false, per_page: data.per_page })
    res.push({ i: 1, href: 1, active: true, per_page: data.per_page })
    res.push({ i: 'next', href: false, active: false, per_page: data.per_page })
  }

  return res
}

dateFormatToString = function (date) {
  let newDate = new Date(date)
  if (date.length === 10) return date

  function pad (num) {
    num = num + ''
    return num.length < 2 ? '0' + num : num
  }
  return newDate.getFullYear() + '-' + pad(newDate.getMonth() + 1) + '-' + pad(newDate.getDate())
}
/**
 * 2019-08-21 15:01:01
 */
dateTimeFormatToString = function (date) {
  let newDate = new Date(date)
  if (date.length === 10) return date

  function pad (num) {
    num = num + ''
    return num.length < 2 ? '0' + num : num
  }
  return newDate.getFullYear() + '-' + pad(newDate.getMonth() + 1) + '-' + pad(newDate.getDate()) + ' ' + pad(newDate.getHours()) + ':' + pad(newDate.getMinutes()) + ':' + pad(newDate.getSeconds())
}

/**
 * 문자열이 빈 문자열인지 체크하여 기본 문자열로 리턴한다.
 * @param str           : 체크할 문자열
 * @param defaultStr    : 문자열이 비어있을경우 리턴할 기본 문자열
 */
nullStr = function (str, defaultStr) {
  if (typeof str === 'undefined' || str == null || str === '') { str = defaultStr }

  return str
}
/**
 * ms 을 시간, 분, 초, ms 단위로 변경
 * @param s           : 1000ms
 * @return time {ms: '', secs: '', mins: '', hrs: '' }   : 객체에서 변수 꺼내 사용
 * EX) 1000ms -> {ms: 0, secs: 1, mins: 0, hrs: 0}
 */
msToTime = function (s) {
  let time = {}
  let ms = s % 1000
  s = (s - ms) / 1000
  let secs = s % 60
  s = (s - secs) / 60
  let mins = s % 60
  let hrs = (s - mins) / 60
  time.ms = ms; time.secs = secs; time.mins = mins; time.hrs = hrs
  return time
}

/**
 * date 타입의 두 param 의 날짜 차이 및 추가로 ms 로 변환
 * @param (Date startDate , Date endDate )
 * @return calDate {diffDays: '날짜차이', onlyTime: '날짜와 시간까지 필요한 경우 사용', diffTime: '시간차이' }   : 객체에서 변수 꺼내 사용
 * EX) DeliveryDetail.vue 사용 확인
 */
calDiffDate = function (startDate, endDate) {
  let diffTime = Math.abs(endDate.getTime() - startDate.getTime())
  let diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24))
  let onlyTime = diffTime - (diffDays * (1000 * 60 * 60 * 24))
  let calDate = {}
  calDate.diffDays = diffDays
  calDate.onlyTime = onlyTime // ms을 위 msToTime 메소드를 사용하여 시간으로 변환 가능
  calDate.diffTime = diffTime
  return calDate
}

if (location.host === 'ladmin.lunasoft.co.kr') { // 리얼환경
  apiServerUrl = 'https://admin-parcelapi.lunasoft.co.kr/api' // 닷넷 core api url
  apiLogServerUrl = 'https://luna-log-api.lunasoft.co.kr/api'
  smartStroreApiUrl = 'https://smartplusapi.lunasoft.co.kr'
  gatewayApiUrl = 'https://shopapi.lunasoft.co.kr'
  reloadApiUrl = 'https://prd-reload-service-api.lunasoft.co.kr'
  imageApiUrl = 'https://iagent.lunasoft.co.kr'
  lunaplusApi = 'https://lunaplus6.lunasoft.co.kr/lunaplus'
  v1Api = 'https://v1.lunasoft.co.kr/api'
} else if (location.host === 'staging-ladmin.lunasoft.co.kr') { // 스테이징
  apiServerUrl = 'https://staging-admin-parcelapi.lunasoft.co.kr/api'
  apiLogServerUrl = ' https://test-luna-log-api.lunasoft.co.kr/api'
  smartStroreApiUrl = 'https://test-smartplusapi.lunasoft.co.kr'
  gatewayApiUrl = 'https://devshopapi.lunasoft.co.kr'
  reloadApiUrl = 'https://dev-reload-service-api.lunasoft.co.kr'
  imageApiUrl = 'https://teiagent.lunasoft.co.kr'
  lunaplusApi = 'https://test-lunaplus6.lunasoft.co.kr/lunaplus'
  v1Api = 'https://test-v1.lunasoft.co.kr/api'
} else { // test 및 로컬 도메인
  apiServerUrl = 'https://test-admin-parcelapi.lunasoft.co.kr/api' // 닷넷 core test 환경
  apiLogServerUrl = ' https://test-luna-log-api.lunasoft.co.kr/api'
  smartStroreApiUrl = 'https://test-smartplusapi.lunasoft.co.kr'
  gatewayApiUrl = 'https://devshopapi.lunasoft.co.kr'
  // smartStroreApiUrl = 'http://192.168.0.51'
  reloadApiUrl = 'https://dev-reload-service-api.lunasoft.co.kr'
  imageApiUrl = 'https://teiagent.lunasoft.co.kr'
  lunaplusApi = 'https://test-lunaplus6.lunasoft.co.kr/lunaplus'
  v1Api = 'https://test-v1.lunasoft.co.kr/api'
}
