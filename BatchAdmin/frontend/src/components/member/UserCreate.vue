<template>
  <div class="UserCreate">
    <div class="content member">
      <h1>회원 관리 > 회원 등록</h1>
      <div class="formContainer">
        <article>
          <div class="articleWrap">
            <div class="tableWrap tableRegister">
              <table summary="회원 계정 정보">
                  <caption class="blind">회원 계정 정보</caption>
                  <colgroup>
                      <col style="width:200px"/>
                      <col style="width:340px"/>
                      <col style="width:150px"/>
                      <col/>
                  </colgroup>
                  <tbody>
                  <tr>
                      <td colspan="4">
                          <h2 class="spanWrap">
                              회원 계정 정보
                          </h2>
                      </td>
                  </tr>
                  <tr>
                      <th>아이디<span class="necessary">*</span></th>
                      <td colspan="3">
                          <p class="duplicateCheckWrap">
                              <input type="text" v-model="member.memberId"
                                      minlength="4"
                                      maxlength="20"
                                      @input="typing($event, 'no_en', 'memberId')"
                              />
                              <input class="btn_check" type="button" value="중복확인"
                                      @click="userIdCheck()"/>
                          </p>
                          <span class="info">4~20자의 영문 소문자, 숫자 조합</span>
                      </td>
                  </tr>
                  <tr>
                      <th>비밀번호<span class="necessary">*</span></th>
                      <td colspan="3">
                          <input type="password" minlength="8" maxlength="20"
                                  v-model="member.memberPw"   @blur="passwordRegexp"/>
                          <span class="info">8~20자의 영문 대소문자, 숫자, 특수문자 조합</span>
                          <span :class="{'info red' : passwordRegexpTxt!==''}" >{{passwordRegexpTxt}}</span>
                      </td>
                  </tr>
                  <tr>
                      <th>비밀번호확인<span class="necessary">*</span></th>
                      <td colspan="3">
                          <input type="password" minlength="8" maxlength="20" @blur="passwordCheck"
                                  v-model="member.memberPwChk"/>
                          <span :class="{'info red' : passwordChkTxt!==''}" >{{passwordChkTxt}}</span>
                      </td>
                  </tr>
                  </tbody>
              </table>
              <br>
              <table summary="사업자 정보">
                  <caption class="blind">사업자 정보</caption>
                  <colgroup>
                      <col style="width:200px"/>
                      <col style="width:340px"/>
                      <col style="width:150px"/>
                      <col/>
                  </colgroup>
                  <tbody>
                  <tr>
                      <td colspan="4">
                          <h2 class="spanWrap">
                              사업자 정보
                          </h2>
                      </td>
                  </tr>
                  <tr>
                      <th>사업자등록번호<span class="necessary">*</span></th>
                      <td colspan="3">
                          <p class="businessNumWrap" >
                              <input type="text"
                                      v-model="member.memberBizNo1"
                                      maxlength="3"
                                      @input="typing($event, 'no', 'memberBizNo1')"
                              />
                              <span></span>
                              <input type="text"
                                      v-model="member.memberBizNo2"
                                      maxlength="2"
                                      @input="typing($event, 'no', 'memberBizNo2')"
                              />
                              <span></span>
                              <input type="text"
                                      v-model="member.memberBizNo3"
                                      maxlength="5"
                                      @input="typing($event, 'no', 'memberBizNo3')"
                              />

                              <input class="btn_check" type="button" value="중복확인"
                                      @click="bizNoDuplicateCheck()"/>
                                      <!-- @click="bizIdcheck()"/> -->
                          </p>

                      </td>
                  </tr>
                  <tr>
                      <th>법인명<span class="necessary">*</span></th>
                      <td colspan="3">
                          <input type="text" v-model="member.memberBizName"  maxlength="50"/>
                      </td>
                  </tr>
                  <tr>
                      <th>대표자명<span class="necessary">*</span></th>
                      <td colspan="3">
                          <input type="text" v-model="member.memberCeoName" @input="typing($event, 'char', 'memberCeoName')" maxlength="12"/>
                      </td>
                  </tr>
                  <tr>
                      <th>주소</th>
                      <td colspan="3">
                          <p class="addressWrap">
                              <input type="text" class="noinputBox" v-model="member.memberAddr" readonly/>
                              <input type="hidden" v-model="member.memberZipcodeNo" readonly/>
                              <input class="btn_address" type="button" value="주소찾기"
                                      color="blue"
                                      size="s"
                                      @click="() => openPostcode()"
                              />
                              <input type="text"
                                      v-model="member.memberDetailAddr"
                                      maxlength="100"
                              />
                              <postcode
                                  :opened="isOpenedPostcode"
                                  @close="() => isOpenedPostcode = false"
                                  @complete="result => setAddress(result)"
                              />
                          </p>
                      </td>
                  </tr>
                  <tr>
                      <th>전화번호</th>
                      <td colspan="3">
                          <p class="phoneNumWrap">
                              <PhoneForm v-model="member.memberTel1"
                                  type = "phone"
                                  @selectedNum="number => member.memberTel1 = number"
                                  :number = "member.memberTel1">

                              </PhoneForm>
                              <span></span>
                              <input type="text"
                                      maxlength="4"
                                      v-model="member.memberTel2"
                                      @input="typing($event, 'no', 'memberTel2')"
                              />
                              <span></span>
                              <input type="text"
                                      maxlength="4"
                                      v-model="member.memberTel3"
                                      @input="typing($event, 'no', 'memberTel3')"
                              />
                          </p>
                      </td>
                  </tr>
                  </tbody>
              </table>
              <br>
              <table summary="담당자 정보">
                <caption class="blind">담당자 정보</caption>
                <colgroup>
                  <col style="width:200px"/>
                  <col style="width:340px"/>
                  <col style="width:150px"/>
                  <col/>
                </colgroup>
                <tbody>
                <tr>
                  <td colspan="4">
                    <h2 class="spanWrap">
                      담당자 정보
                    </h2>
                  </td>
                </tr>
                <tr>
                  <th>담당자명<span class="necessary">*</span></th>
                  <td colspan="3">
                    <input type="text"
                      v-model="member.memberManagerName"
                      maxlength="30"
                      @input="typing($event, 'char', 'memberManagerName')"
                    />
                  </td>
                </tr>
                <tr>
                  <th>휴대폰번호<span class="necessary">*</span></th>
                  <td colspan="3">
                    <p class="phoneNumWrap">
                      <PhoneForm v-model="member.managePhone1"
                        type = "phone"
                        @selectedNum="number => member.managePhone1 = number"
                        :number = "member.managePhone1">
                      </PhoneForm>
                      <span></span>
                      <input type="text"
                        maxlength="4"
                        v-model="member.managePhone2"
                        @input="typing($event, 'no', 'managePhone2')"
                      />
                      <span></span>
                      <input type="text"
                        maxlength="4"
                        v-model="member.managePhone3"
                        @input="typing($event, 'no', 'managePhone3')"
                      />
                    </p>
                  </td>
                </tr>
                <tr>
                  <th>담당자 이메일<span class="necessary">*</span></th>
                  <td colspan="3">
                    <p class="emailWriteWrap">
                      <input type="text"
                        v-model="member.manageEmail"
                        maxlength="20"
                        @blur="typing($event, 'emailfront', 'email')"
                      />
                      <span>@</span>
                      <input type="text"
                        v-model="member.mailTxt"
                        maxlength="20"
                        @blur="typing($event, 'emailback', 'mailTxt')"
                      />
                      <select v-model="selectEmail">
                        <option value="">직접입력</option>
                        <option value="naver.com">naver.com</option>
                        <option value="nate.com">nate.com</option>
                        <option value="gmail.com">gmail.com</option>
                      </select>
                      <span :class="{'info red' : emailChkTxt!==''}">{{emailChkTxt}}</span>
                    </p>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </article>
        <div class="btnWrap btnEnd" style="text-align:center;">
          <ul>
            <li>
              <input class="btn_cancel btn-lg" type="button" value="취소" @click="backBtn"/>
            </li>
            <li>
              <input class="btn_save btn-lg" type="button" value="등록" @click="create()"/>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import ModalAlert from '@/components/common/modal/ModalAlert'
import Postcode from '@/components/common/Postcode'
import PhoneForm from '@/components/common/PhoneForm'
import http from '@/http-commons'

export default {
  data () {
    return {
      isLoading: false,
      addMsg: '법인/상호명 버튼을 선택하여 법인/상호명을 선택해 주세요.',
      isOpenedPostcode: false,
      postcodeIndex: 0,
      idUserIdChecked: false, // ID 중복확인 처리여부
      isPasswordChecked: false,
      passwordRegexpTxt: '',
      passwordChkTxt: '',
      checkedBizNo: '',
      createFlag: false, // 용석 : 사업자 중복 확인 및 중복 허용여부 체크
      userNo: this.$store.getters.getUser.luna_no,
      selectEmail: '',
      emailChkTxt: '',
      member: { /** 회원생성시 필요한 data */
        memberId: '', // 회원ID
        memberPw: '',
        memberPwChk: '',
        memberBizName: '', // 법인명
        memberCeoName: '', // 대표자명
        memberBizNo1: '', // 사업자등록번호1
        memberBizNo2: '', // 사업자등록번호2
        memberBizNo3: '', // 사업자등록번호3
        memberAddr: '', // address1
        memberDetailAddr: null, // address2
        memberZipcodeNo: '', // address3
        memberTel1: '02', // 사업자 전화번호1
        memberTel2: '', // 사업자 전화번호2
        memberTel3: '', // 사업자 전화번호3
        memberManagerName: '', // 담당자명
        managePhone1: '010', // 담당자 휴대폰1
        managePhone2: '', // 담당자 휴대폰2
        managePhone3: '', // 담당자 휴대폰3
        manageEmail: '', // 메일 ID
        mailTxt: '' // 메일뒤 주소
      },
      valiAlert: {
        memberId: '아이디를 입력해주세요.',
        memberPw: '비밀번호를 입력해주세요.',
        memberPwChk: '비밀번호확인을 해주세요',
        memberBizName: '법인명을 입력해주세요.',
        memberCeoName: '대표자성명을 입력해주세요.',
        memberBizNo1: '사업자번호를 입력해주세요.',
        memberBizNo2: '사업자번호를 입력해주세요.',
        memberBizNo3: '사업자번호를 입력해주세요.',
        memberManagerName: '담당자명을 입력해주세요.',
        managePhone1: '담당자연락처를 입력해주세요.',
        managePhone2: '담당자연락처를 입력해주세요.',
        managePhone3: '담당자연락처를 입력해주세요.',
        manageEmail: '이메일주소를 입력해주세요.',
        mailTxt: '이메일주소를 입력해주세요.'
      }
    }
  },
  watch: {
    selectEmail () {
      this.member.mailTxt = this.selectEmail
      this.emailChkTxt = ''
    },
    member: {
      handler: function (newValue) {
      },
      deep: true
    }
  },
  methods: {
    typing: function (evt, type, name) {
      let message = evt.target.value.trim()
      let patternNo = /^[0-9]+$/

      let patternEmailFront = /^[0-9a-zA-Z]([-_.]|[0-9a-zA-Z])*$/i // 이메일 앞주소 정규식
      let patternEmailBack = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i // 이메일 뒷주소 정규식
      let patternNoEn = /^[a-z0-9]+$/ // 소문자영어 및 숫자
      let patternChar = /^[ㄱ-ㅎ|가-힣a-zA-Z]+$/ // 문자 전부
      if (type === 'no') {
        if (patternNo.test(message) === false) {
          if (name === 'memberBizNo1') this.member.memberBizNo1 = evt.target._value
          else if (name === 'memberBizNo2') this.member.memberBizNo2 = evt.target._value
          else if (name === 'memberBizNo3') this.member.memberBizNo3 = evt.target._value
          else if (name === 'memberTel2') this.member.memberTel2 = evt.target._value
          else if (name === 'memberTel3') this.member.memberTel3 = evt.target._value
          else if (name === 'managePhone2') this.member.managePhone2 = evt.target._value
          else if (name === 'managePhone3') this.member.managePhone3 = evt.target._value
        } else {
          if (name === 'memberBizNo1') this.member.memberBizNo1 = message
          else if (name === 'memberBizNo2') this.member.memberBizNo2 = message
          else if (name === 'memberBizNo3') this.member.memberBizNo3 = message
          else if (name === 'memberTel2') this.member.memberTel2 = message
          else if (name === 'memberTel3') this.member.memberTel3 = message
          else if (name === 'managePhone2') this.member.managePhone2 = message
          else if (name === 'managePhone3') this.member.managePhone3 = message
        }
      } else if (type === 'char') { // 문자만
        if (patternChar.test(message) === false && message !== '') {
          if (name === 'memberCeoName') this.member.memberCeoName = evt.target._value
          else if (name === 'memberManagerName') this.member.memberManagerName = evt.target._value
        } else {
          if (name === 'memberCeoName') this.member.memberCeoName = message
          else if (name === 'memberManagerName') this.member.memberManagerName = message
        }
      } else if (type === 'no_en') { // 영어 숫자
        if (patternNoEn.test(message) === false && message !== '') {
          if (name === 'memberId') {
            this.member.memberId = evt.target._value
          } else if (name === 'email') {
            this.member.manageEmail = evt.target._value
          }
        } else {
          if (name === 'memberId') this.member.memberId = message
          else if (name === 'email') this.member.manageEmail = message
        }
      } else if (type === 'emailback') {
        if (patternEmailBack.test(message) === false) {
          if (name === 'mailTxt') {
            this.member.mailTxt = ''
            this.emailChkTxt = '이메일 형식이 아닙니다.'
          }
        } else {
          if (name === 'mailTxt') {
            this.member.mailTxt = message
            this.emailChkTxt = ''
          }
        }
      } else if (type === 'emailfront') {
        if (patternEmailFront.test(message) === false) {
          if (name === 'email') {
            this.member.manageEmail = ''
            this.emailChkTxt = '이메일 형식이 아닙니다.'
          }
        } else {
          if (name === 'email') {
            this.member.manageEmail = message
            this.emailChkTxt = ''
          }
        }
      }
    },
    backBtn () {
      this.$router.push({name: 'userlist'})
    },
    userIdCheck () { /** 아이디 중복체크 */
      // let regType1 = /^[a-z0-9]{4,20}$/; //소문자나 숫자 4자이상 20자
      let regType = /^(?=.*[a-z])(?=.*[0-9]).{4,20}$/ // 소문자와 숫자 4자부터 20자 조합
      if (!regType.test(this.member.memberId)) {
        this.$modal.show(ModalAlert,
          {title: '회원등록 생성', text: '4자이상 20자의 영문 소문자 및 숫자조합으로만 가능합니다.'},
          { width: 450, height: 'auto' }
        )
        return
      }
      this.isLoading = true
      console.log('test')
      http
        .get('/member/useridcheck', {
          params: { // query string
            member_id: this.member.memberId
          }
        })
        .then(response => {
          this.isLoading = false
          if (response.data.data > 0) {
            this.$modal.show(ModalAlert,
              {title: '회원등록 생성', text: '이미 사용중이거나 탈퇴한 아이디입니다.'},
              { width: 450, height: 'auto' }
            )
          } else {
            this.$modal.show(ModalAlert,
              {title: '회원등록 생성', text: '사용 가능한 아이디입니다.'},
              { width: 450, height: 'auto' }
            )
            this.idUserIdChecked = true
          }
        })
        .catch(e => {
          console.log(e)
          this.errorPopup()
        })
    },
    create () {
      if (!this.checkPw()) return false
      if (!this.validation()) return false
      if (!this.checkDup()) return false
      if (!this.checkBizNoCheckFlag()) return false /** 중복여부 체크 확인 */

      let form = new FormData()
      form.append('member_id', this.member.memberId)
      form.append('member_pw', this.member.memberPw)
      form.append('member_biz_name', this.member.memberBizName)
      form.append('member_ceo_name', this.member.memberCeoName)
      form.append('member_biz_no', this.member.memberBizNo1 + this.member.memberBizNo2 + this.member.memberBizNo3)
      form.append('member_addr', this.member.memberAddr) // 대표자명
      form.append('member_detail_addr', this.member.memberDetailAddr)
      form.append('member_zipcode_no', this.member.memberZipcodeNo)
      form.append('member_addr_tel_no', this.member.memberTel1 + '-' + this.member.memberTel2 + '-' + this.member.memberTel3) // 사업자 전화번호
      form.append('member_manager_name', this.member.memberManagerName)
      form.append('member_manager_tel_no', this.member.managePhone1 + '-' + this.member.managePhone2 + '-' + this.member.managePhone3) // 담당자 휴대폰
      form.append('member_manager_email_addr', this.member.manageEmail + '@' + this.member.mailTxt)
      form.append('user_no', this.userNo)
      form.append('user_code', 1)
      this.isLoading = true
      http
        .post('/member/create', form)
        .then(response => {
          this.isLoading = false
          this.$modal.show(ModalAlert,
            {title: '회원등록 생성', text: '정상적으로 회원이 생성되었습니다.'},
            { width: 450, height: 'auto' }
          )
          this.$router.push({name: 'userlist'})
        })
        .catch(e => {
          console.log(e)
          this.errorPopup()
        })
    },
    validation () {
      for (let i in this.valiAlert) {
        if (!this.member[i]) {
          this.$modal.show(ModalAlert,
            { title: '회원 등록', text: this.valiAlert[i] },
            { width: 450, height: 'auto' }
          )
          return false
        }
      }
      return true
    },
    passwordRegexp () {
      var check = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{6,16}$/ // 영문 + 특수문자 + 숫자 정규식
      if (check.test(this.member.memberPw)) {
        this.passwordRegexpTxt = ''
      } else {
        this.passwordRegexpTxt = '영문 대소문자, 숫자, 특수문자 조합 불일치'
        this.isPasswordChecked = false
      }
    },
    passwordCheck () {
      if (this.member.memberPw === this.member.memberPwChk) {
        this.passwordChkTxt = ''
        this.isPasswordChecked = true
      } else {
        this.passwordChkTxt = '비밀번호가 일치하지 않습니다.'
        this.isPasswordChecked = false
      }
    },
    checkDup () {
      if (!this.idUserIdChecked) {
        this.$modal.show(ModalAlert,
          { title: '회원 등록', text: '회원아이디 중복확인을 해주세요.' },
          { width: 450, height: 'auto' }
        )
        return false
      }
      return true
    },
    checkPw () {
      if (!this.isPasswordChecked) {
        this.$modal.show(ModalAlert,
          { title: '회원 등록', text: '비밀번호 조합 확인 및 비밀번호 확인 해주세요.' },
          { width: 450, height: 'auto' }
        )
        return false
      }

      let res = true
      if (this.member.memberPw !== this.member.memberPwChk) {
        this.$modal.show(ModalAlert,
          { title: '회원 등록', text: '비밀번호가 일치하지 않습니다.' },
          { width: 450, height: 'auto' }
        )
        res = false
      }
      return res
    },
    checkBizNoCheckFlag () {
      if (!this.createFlag) {
        this.$modal.show(ModalAlert,
          { title: '회원 등록', text: '사업자 등록번호 중복여부를 확인하세요' },
          { width: 450, height: 'auto' }
        )
        return false
      }

      let tempBizNo = (this.member.memberBizNo1 + '-' + this.member.memberBizNo2 + '-' + this.member.memberBizNo3)
      if (this.checkedBizNo !== tempBizNo) {
        this.$modal.show(ModalAlert,
          { title: '회원 등록', text: '입력한 사업자 등록번호[' + tempBizNo + '] 의 중복여부를 확인하세요' },
          { width: 450, height: 'auto' }
        )
        return false
      }
      return true
    },
    openPostcode () {
      this.isOpenedPostcode = true
    },
    setAddress (result) { /** 주소 팝업완료시 주소데이터 set */
      this.member.memberAddr = result.roadAddress
      this.member.memberDetailAddr = result.buildingName
      this.member.memberAipcodeNo = result.zonecode
    },
    errorPopup () {
      this.$modal.show(ModalAlert,
        {title: '회원등록 생성', text: '서버오류'},
        { width: 450, height: 'auto' }
      )
    },
    bizNoDuplicateCheck () {
      let memberBizNo = this.member.memberBizNo1 + '-' + this.member.memberBizNo2 + '-' + this.member.memberBizNo3

      if (memberBizNo.length !== 12) {
        console.log('lenght Check2 : ' + memberBizNo.length)
        this.$modal.show(ModalAlert,
          { title: '회원 등록', text: '올바른 사업자 등록번호가 아닙니다.' },
          { width: 450, height: 'auto' }
        )
        return
      }
      this.isLoading = true
      http
        .get('/member/bizNoDuplicateCheck', {
          params: { // query string
            member_biz_no: memberBizNo
          }
        })
        .then(response => {
          this.isLoading = false
          if (response.data.responseCode === 0) {
            let alertMsg = ''
            let count = response.data.data.count
            let duplicateYn = response.data.data.duplicate_yn
            console.log('count : ' + count + ' || duplicate_yn : ' + duplicateYn)

            /** 중복허용 */
            if (duplicateYn === 1) {
              alertMsg = '사업자 등록번호가 확인되었습니다.'
              this.checkedBizNo = memberBizNo
              this.createFlag = true
            } else {
              alertMsg = '이미 사용중인 사업자 번호입니다. 중복허용 설정 변경 후 등록 가능합니다. 고객센터로 문의하시기 바랍니다.'
            }

            this.$modal.show(ModalAlert,
              { title: '회원 등록', text: alertMsg },
              { width: 450, height: 'auto' }
            )
          } else {
            this.errorPopup(response.data.responseMsg)
          }
        })
        .catch(e => {
          console.log(e)
          this.errorPopup()
        })
    }
  },
  components: {
    Postcode,
    PhoneForm
  }
}
</script>
