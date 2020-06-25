<template>
  <div class="AccountCreate">
    <div class="content employ">
      <h1>계정 생성</h1>
      <div class="formContainer">
        <form action="#" method="post">
          <article>
          <div class="articleWrap">
            <div class="titleWrap">
              <p class="title">기본정보</p>
            </div>
            <div class="tableWrap tableRegister">
              <table summary="기본정보">
                <caption class="blind">기본정보</caption>
                <colgroup>
                  <col style="width:200px">
                  <col>
                </colgroup>
                <tbody>
                  <tr>
                    <th>
                      사번
                      <span>(통합어드민 계정)</span>
                      <span class="necessary">*</span>
                    </th>
                    <td>
                      <input
                        type="text"
                        placeholder="예) L180802"
                        v-model="users.luna_id"
                        maxlength="7"
                        @input="typing($event, 'id')"
                      >
                    </td>
                  </tr>
                  <tr>
                    <th>
                      한글이름
                      <span class="necessary">*</span>
                    </th>
                    <td>
                      <input
                        type="text"
                        placeholder="예) 홍길동"
                        v-model="users.luna_kor_name"
                        maxlength="7"
                        @input="typing($event, 'kr', 'luna_kor_name')"
                      >
                    </td>
                  </tr>
                  <tr>
                    <th>
                      영문이름
                      <span>(한글표기)</span>
                      <span class="necessary">*</span>
                    </th>
                    <td>
                      <input
                        type="text"
                        placeholder="예) 티파니"
                        v-model="users.luna_eng_kor_nick_name"
                        maxlength="20"
                        @input="typing($event, 'kr', 'luna_eng_kor_nick_name')"
                      >
                    </td>
                  </tr>
                  <tr>
                    <th>
                      영문이름
                      <span>(영문표기)</span>
                      <span class="necessary">*</span>
                    </th>
                    <td>
                      <input
                        type="text"
                        placeholder="예) Tiffany"
                        v-model="users.luna_eng_nick_name"
                        maxlength="20"
                        @input="typing($event, 'en', 'luna_eng_nick_name')"
                      >
                    </td>
                  </tr>
                  <tr>
                    <th>
                      모바일
                      <span class="necessary">*</span>
                    </th>
                    <td>
                      <p class="phoneNumWrap">
                        <PhoneForm
                          type="phone"
                          :number="getTel.tel1"
                          @selectedNum="number => getTel.tel1 = number"
                        />
                        <span></span>
                        <input
                          type="text"
                          maxlength="4"
                          v-model="getTel.tel2"
                          @input="typing($event, 'no', 'tel2')"
                        >
                        <span></span>
                        <input
                          type="text"
                          maxlength="4"
                          v-model="getTel.tel3"
                          @input="typing($event, 'no', 'tel3')"
                        >
                      </p>
                    </td>
                  </tr>
                  <tr>
                    <th>
                      이메일
                      <span class="necessary">*</span>
                    </th>
                    <td>
                      <p class="emailWrap">
                        <input
                          type="text"
                          placeholder="이메일주소를 입력해주세요"
                          v-model="users.luna_email_addr"
                          maxlength="50"
                          @input="typing($event, 'en', 'luna_email_addr')"
                        >
                        @lunasoft.co.kr
                      </p>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="articleWrap">
            <div class="titleWrap">
              <p class="title">소속 정보</p>
            </div>
            <div class="tableWrap tableRegister">
                <table summary="소속 정보">
                  <caption class="blind">소속 정보</caption>
                  <colgroup>
                    <col style="width:200px" />
                    <col  />
                  </colgroup>
                  <tbody>
                    <tr>
                      <th>
                        소속
                        <span class="necessary">*</span>
                      </th>
                      <td>
                        <select v-model="deptNo">
                          <option disabled value=''>선택하세요</option>
                          <template v-for="(val, index) in lunaDeptNoData">
                            <option :key="index" :value="index">{{val}}</option>
                          </template>
                        </select>
                      </td>
                    </tr>
                    <tr>
                      <th>팀</th>
                      <td>
                        <select v-model="teamNo">
                          <option disabled value=''>선택하세요</option>
                          <template v-for="(val, index) in selectTeamData">
                            <option :key="index" :value="index">{{val}}</option>
                          </template>
                        </select>
                      </td>
                    </tr>
                    <tr>
                      <th>파트</th>
                      <td>
                        <select v-model="partNo">
                          <option disabled value=''>선택하세요</option>
                          <template v-for="(val, index) in selectPartData">
                            <option :key="index" :value="index">{{val}}</option>
                          </template>
                        </select>
                      </td>
                    </tr>
                    <tr>
                      <th>
                        직책
                        <span class="necessary">*</span>
                      </th>
                      <td>
                        <select v-model="users.luna_duty_no">
                          <option disabled value>선택하세요</option>
                          <template v-for="(val, index) in lunaDutyNoData">
                            <option :key="index" :value="index">{{val}}</option>
                          </template>
                        </select>
                      </td>
                    </tr>
                    <tr>
                      <th>
                        입사일자
                        <span class="necessary">*</span>
                      </th>
                      <td>
                        <datepicker
                          class="datepicker"
                          placeholder="예) 1986-07-27"
                          :language="languages['ko']"
                          format="yyyy-MM-dd"
                          v-model="users.luna_join_date"
                        />
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div class="articleWrap">
              <div class="titleWrap">
                <p class="title">계정 정보</p>
              </div>
              <div class="tableWrap tableRegister">
                <table summary="계정 정보">
                  <caption class="blind">계정 정보</caption>
                  <colgroup>
                    <col style="width:200px" />
                    <col  />
                  </colgroup>
                  <tbody>
                    <tr>
                      <th>
                        권한그룹
                        <span class="necessary">*</span>
                      </th>
                      <td>
                        <select v-model="users.luna_auth_no">
                          <option disabled value>선택하세요</option>
                          <template v-for="(val, index) in lunaAuthGroupNoData">
                            <option :key="index" :value="index">{{val}}</option>
                          </template>
                        </select>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </article>
          <div class="btnWrap">
            <ul>
              <li>
                <input class="btn_cancel" type="button" value="취소" @click="goList()">
              </li>
              <li v-show="userMenuAuth.create_data">
                <input class="btn_save" type="button" value="등록" @click="create()">
              </li>
            </ul>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import Datepicker from 'vuejs-datepicker/dist/vuejs-datepicker.esm.js'
// eslint-disable-next-line no-unused-vars
import * as lang from 'vuejs-datepicker/dist/locale'
import http from '../../http-commons'

import ModalAlert from '../common/modal/ModalAlert.vue'
import PhoneForm from '../common/PhoneForm.vue'

export default {
  mounted () {
    this.users = {
      luna_id: '',
      luna_kor_name: '',
      luna_eng_kor_nick_name: '',
      luna_eng_nick_name: '',
      luna_mobile_no: '',
      luna_email_addr: '',
      luna_dept_no: '',
      luna_team_no: '',
      luna_part_no: '',
      luna_duty_no: '',
      luna_join_date: '',
      luna_auth_no: ''
    }
  },
  components: {
    Datepicker,
    ModalAlert,
    PhoneForm
  },
  data () {
    return {
      languages: lang,
      deptNo: '',
      teamNo: '',
      partNo: '',
      selectTeamData: null,
      selectPartData: null,
      users: [],
      getTel: {
        tel1: '010',
        tel2: '',
        tel3: ''
      },
      valiAlert: {
        luna_id: '사번(통합어드민 계정)을 입력해주세요.',
        luna_kor_name: '한글성명을 입력해주세요.',
        luna_eng_kor_nick_name: '영문성명(한글표기)을 입력해주세요.',
        luna_eng_nick_name: '영문성명(영문표기)을 입력해주세요.',
        luna_email_addr: '이메일을 입력해주세요.',
        user_tel: '핸드폰번호를 입력해주세요.',
        luna_dept_no: '소속을 선택해주세요.',
        luna_duty_no: '직책을 선택해주세요.',
        luna_auth_no: '권한을 선택해주세요.',
        luna_join_date: '입사일자을 선택해주세요.'
      }
    }
  },
  watch: {
    deptNo () {
      let teamDpCode = ''
      this.users.luna_dept_no = this.deptNo
      this.selectTeamData = {}
      this.teamNo = ''
      for (let idx in this.lunaTeamNoData) {
        teamDpCode = this.lunaTeamNoData[idx].luna_dept_no
        if (teamDpCode === this.deptNo) {
          this.selectTeamData[this.lunaTeamNoData[idx].luna_team_no] = this.lunaTeamNoData[idx].luna_team_name
        }
      }
    },
    teamNo () {
      let partTmCode = ''
      this.users.luna_team_no = this.teamNo
      this.selectPartData = {}
      this.partNo = ''
      for (let idx in this.lunaPartNoData) {
        partTmCode = this.lunaPartNoData[idx].luna_team_no
        if (partTmCode === this.teamNo) {
          this.selectPartData[this.lunaPartNoData[idx].luna_part_no] = this.lunaPartNoData[idx].luna_part_name
        }
      }
    }
  },
  methods: {
    typing: function (evt, type, name) {
      let message = evt.target.value
      // eslint-disable-next-line camelcase
      let pattern_kr = /[^ㄱ-ㅎㅏ-ㅣ가-힣]/
      // eslint-disable-next-line camelcase
      let pattern_en = /[^A-z]/
      // eslint-disable-next-line camelcase
      let pattern_no = /[^0-9]/
      // eslint-disable-next-line camelcase
      let pattern_id = /[^A-Z0-9]/g

      if (type === 'kr') {
        if (pattern_kr.test(message)) {
          if (name === 'luna_kor_name') {
            this.users.luna_kor_name = ''
          } else if (name === 'luna_eng_kor_nick_name') {
            this.users.luna_eng_kor_nick_name = ''
          }
        } else {
          if (name === 'luna_kor_name') {
            this.users.luna_kor_name = evt.target.value.trim()
          } else if (name === 'luna_eng_kor_nick_name') {
            this.users.luna_eng_kor_nick_name = evt.target.value.trim()
          }
        }
      } else if (type === 'en') {
        if (pattern_en.test(message)) {
          if (name === 'luna_eng_nick_name') this.users.luna_eng_nick_name = ''
          else if (name === 'luna_email_addr') this.users.luna_email_addr = ''
        } else {
          if (name === 'luna_eng_nick_name') {
            this.users.luna_eng_nick_name = evt.target.value.trim()
          } else if (name === 'luna_email_addr') {
            this.users.luna_email_addr = evt.target.value.trim()
          }
        }
      } else if (type === 'no') {
        if (pattern_no.test(message)) {
          if (name === 'tel2') this.getTel.tel2 = ''
          else if (name === 'tel3') this.getTel.tel3 = ''
        } else {
          if (name === 'tel2') this.getTel.tel2 = evt.target.value.trim()
          else if (name === 'tel3') this.getTel.tel3 = evt.target.value.trim()
        }
      } else if (type === 'id') {
        if (pattern_id.test(message)) {
          this.users.luna_id = evt.target._value
        } else {
          this.users.luna_id = evt.target.value.trim()
        }
      }
    },
    validation () {
      for (let i in this.valiAlert) {
        if (!this.users[i]) {
          this.$modal.show(
            ModalAlert,
            {
              title: '직원 계정 생성',
              text: this.valiAlert[i]
            },
            {
              width: 450,
              height: 167
            }
          )
          return false
        }
      }
      return true
    },
    goList () {
      this.$router.go(-1)
    },
    create () {
      this.users.user_tel =
        this.getTel.tel1 + '-' + this.getTel.tel2 + '-' + this.getTel.tel3
      if (!this.validation()) return false

      http
        .post('/account/accountcreate', {
          params: {
            luna_id: this.users.luna_id,
            luna_kor_name: this.users.luna_kor_name,
            luna_eng_kor_nick_name: this.users.luna_eng_kor_nick_name,
            luna_eng_nick_name: this.users.luna_eng_nick_name,
            luna_mobile_no: this.users.user_tel,
            luna_email_addr: this.users.luna_email_addr,
            luna_dept_no: this.deptNo,
            luna_team_no: this.teamNo,
            luna_part_no: this.partNo,
            luna_duty_no: this.users.luna_duty_no,
            // eslint-disable-next-line no-undef
            luna_join_date: dateFormatToString(this.users.luna_join_date),
            luna_auth_no: this.users.luna_auth_no,
            login_luna_no: this.$store.getters.getUser.luna_no
          }
        })
        .then(result => {
          var msg = '정상적으로 새로운 직원 계정이 생성되었습니다.'
          if (result.data !== '0000') {
            msg = '진행중에 문제가 발생했습니다.'
          }
          this.$modal.show(
            ModalAlert,
            {
              title: '계정 생성',
              text: msg
            },
            {
              width: 450,
              height: 167
            }
          )

          if (result.data === '0000') {
            this.$router.push({
              name: 'accountlist'
            })
          }
        })
        .catch(e => {
          console.log('AccountCreate.vue : ' + e)
        })
    }
  },
  props: [
    'userMenuAuth',
    'lunaDeptNoData',
    'lunaTeamNoData',
    'lunaPartNoData',
    'lunaDutyNoData',
    'lunaAuthGroupNoData'
  ]
}
</script>
