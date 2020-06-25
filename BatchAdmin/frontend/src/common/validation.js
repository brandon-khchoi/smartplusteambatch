/* eslint-disable no-undef */
/**
 *공백 제거
 */
removeSpace = function (inputValue) {
  console.log('removeSpace : ' + inputValue.replace(/\s*/g, ''))
  // return inputValue.replace(/\s/gi, '')
  return inputValue.replace(/\s*/g, '')
  // return inputValue.replace(/[^0-9]$/gi, '')
  // return inputValue.replace(/ /gi, '').trim()
}

/**
 * 이메일 전체 정규식
 */
emailValid = function (inputValue) {
  let emailRegex = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/
  return emailRegex.test(inputValue)
}

/**
 * 이메일 앞주소 정규식
 */
frontEmailValid = function (inputValue) {
  let patternEmailFront = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*$/i
  return patternEmailFront.test(inputValue)
}

/**
 * 이메일 뒷주소 정규식
 */
backEmailValid = function (inputValue) {
  // let patternEmailBack = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i
  let patternEmailBack = /((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/
  return patternEmailBack.test(inputValue)
}

passwordValidation = function (inputValue) {
  let passwordRegex = /^.*(?=.{4,8})(?=.*[0-9])(?=.*[a-zA-Z]).*$/
  return passwordRegex.test(inputValue)
}

/**
 * 숫자 정규식 (양수)
 */
numberValidation = function (inputValue) {
  let numberRegex = /^[0-9]+$/
  return numberRegex.test(inputValue)
}

/**
 * 숫자 정규식 (음수포함)
 */
numberValidationWithMinus = function (inputValue) {
  let numberRegex = /^[-]?\d*(\d*)$/
  return numberRegex.test(inputValue)
}

/**
 * 문자 정규식
 */
wordValidation = function (inputValue) {
  let wordRegex = /^[ㄱ-ㅎ|가-힣a-zA-Z]+$/
  return wordRegex.test(inputValue)
}

/**
 * 이미지 확장자 검사
 */
checkImageType = function (fileName) {
  var pattern = /jpg|gif|png|jpeg/i
  return fileName.match(pattern)
}

lengthCheck = function (inputValue) {
  return ''
}

getPhoneList = function () {
  let phoneList = {
    '010': '010',
    '011': '011',
    '02': '02',
    '031': '031',
    '032': '032',
    '033': '033',
    '041': '041',
    '042': '042',
    '043': '043',
    '044': '044',
    '051': '051',
    '052': '052',
    '053': '053',
    '054': '054',
    '055': '055',
    '061': '061',
    '062': '062',
    '063': '063',
    '064': '064',
    '070': '070',
    '016': '016',
    '017': '017',
    '018': '018',
    '019': '019'
  }
  return phoneList
}

getEmailList = function () {
  let emailList = {
    'naver.com': 'naver.com',
    'hanmail.net': 'hanmail.net',
    'nate.com': 'nate.com',
    'gmail.com': 'gmail.com',
    'daum.net': 'daum.net',
    'hotmail.com': 'hotmail.com',
    'korea.com': 'korea.com',
    'lycos.co.kr': 'lycos.co.kr',
    'empas.com': 'empas.com',
    'dreamwiz.com': 'dreamwiz.com',
    'yahoo.com': 'yahoo.com',
    'ymail.com': 'ymail.com',
    'live.com': 'live.com',
    'aol.com': 'aol.com',
    'msn.com': 'msn.com',
    'me.com': 'me.com',
    'icloud.com': 'icloud.com',
    'qq.com': 'qq.com',
    'link.com': 'link.com',
    '': '직접입력'
  }
  return emailList
}

catchEvent = function (event) {
  console.log('common function : ' + event.target.value)
}

onlyNumber = function (event) {
  var keyValue = event.keyCode
  if (((keyValue >= 48) && (keyValue <= 57))) {
    console.log('number')
    return true
  } else {
    console.log('not number')
    return false
  }
}
// showKeyCode = function (event) {
//   event = event || window.event
//   console.log('value : ' + this.event.target.value)
//   console.log('keyCode : ' + this.event.keyCode)
//   // var keyID = (event.which) ? event.which : event.keyCode
//   // if ((keyID >= 48 && keyID <= 57) || (keyID >= 96 && keyID <= 105)) {
//   //   console.log('keycod : ' + keyID)
//   // } else {
//   //   return false
//   // }
// }
