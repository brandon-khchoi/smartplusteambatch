<template>
  <div class="AccountEdit">
    <div class="loading" v-if="loading"></div>
    <div class="content employ">
      <h1>계정 정보</h1>
      <div class="formContainer">
        <form action="#" method="post">
        <article>
        <div class="articleWrap">
          <div class="titleWrap">
            <p class="title">기본정보</p>
          </div>
          <div class="tableWrap tableRegister">
            <table summary="기본 정보">
              <caption class="blind">기본 정보</caption>
              <colgroup>
                <col style="width:200px">
                <col>
              </colgroup>
              <tbody>
                <tr>
                  <th>사번</th>
                  <td>
                    <input type="text" :value="user.luna_id" disabled="disabled">
                    <input
                      type="checkbox"
                      v-model="user.resign_yn"
                      :true-value="1"
                      :false-value="0"
                    >
                    <label>퇴사</label>
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
                      v-model="user.luna_kor_name"
                      class="auth_update"
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
                      v-model="user.luna_eng_kor_nick_name"
                      class="auth_update"
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
                      v-model="user.luna_eng_nick_name"
                      class="auth_update"
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
                        :number="user.tel1"
                        @selectedNum="number => user.tel1 = number"
                      ></PhoneForm>
                      <span></span>
                      <input
                        type="text"
                        v-model="user.tel2"
                        maxlength="4"
                        class="auth_update"
                        @input="typing($event, 'no', 'tel2')"
                      >
                      <span></span>
                      <input
                        type="text"
                        v-model="user.tel3"
                        maxlength="4"
                        class="auth_update"
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
                    <input type="text" v-model="user.email_id">@lunasoft.co.kr
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
                    <select v-model="deptNo" class="auth_update">
                      <option disabled value=''>선택하세요</option>
                      <template v-for="(dept, index) in lunaDeptNoData">
                        <option :key="index" :value="index">{{dept}}</option>
                      </template>
                    </select>
                  </td>
                </tr>
                <tr>
                  <th>팀</th>
                  <td>
                    <select v-model="teamNo" class="auth_update">
                      <option disabled value=''>선택하세요</option>
                      <template v-for="(team, index) in selectTeamData">
                        <option :key="index" :value="index">{{team}}</option>
                      </template>
                    </select>
                  </td>
                </tr>
                <tr>
                  <th>파트</th>
                  <td>
                    <select v-model="partNo" class="auth_update">
                      <option disabled value=''>선택하세요</option>
                      <template v-for="(part, index) in selectPartData">
                        <option :key="index" :value="index">{{part}}</option>
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
                    <select v-model="user.luna_duty_no" class="auth_update">
                      <option disabled value>선택하세요</option>
                      <template v-for="(duty, index) in lunaDutyNoData">
                        <option :key="index" :value="index">{{duty}}</option>
                      </template>
                    </select>
                  </td>
                </tr>
                <tr>
                  <th>입사일자</th>
                  <td>
                    <input type="text" v-model="user.luna_join_date" disabled="disabled">
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
                  <th>계정 생성일자</th>
                  <td>
                    <input type="text" v-model="user.create_date" disabled="disabled">
                  </td>
                </tr>
                <tr>
                  <th>
                    권한그룹
                    <span class="necessary">*</span>
                  </th>
                  <td>
                    <select v-model="user.luna_auth_no" class="auth_update">
                      <option disabled value>선택하세요</option>
                      <template v-for="(auth, index) in lunaAuthGroupNoData">
                        <option :key="index" :value="index">{{auth}}</option>
                      </template>
                    </select>
                  </td>
                </tr>
                <tr>
                  <th>최근 로그인</th>
                  <td>
                    <div class="moreWrap">
                      <input type="text" v-model="user.login_last_date" disabled="disabled">
                    </div>
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
              <input class="btn_cancel" type="button" value="목록으로" @click="goList()">
            </li>
            <li v-show="userMenuAuth.modify_data">
              <input
                class="btn_save"
                type="button"
                value="적용"
                @click="set()"
              >
            </li>
          </ul>
        </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import ModalAlert from '../common/modal/ModalAlert.vue'
import PhoneForm from '../common/PhoneForm.vue'
import http from '../../http-commons'

export default {
  mounted () {
    this.get(this.$route.params.luna_no)
  },
  data () {
    return {
      user: {
        tel1: '010',
        tel2: '',
        tel3: ''
      },
      deptNo: '',
      teamNo: '',
      partNo: '',
      selectTeamData: null,
      selectPartData: null,
      loading: true
    }
  },
  watch: {
    deptNo () {
      // get()에서 호출된 경우 luna_team_no 정보가 있으면 teamNo 초기화 안함
      if (this.user.luna_dept_no !== Number(this.deptNo)) {
        this.teamNo = ''
      }

      let teamDpCode = 0
      this.user.luna_dept_no = this.deptNo
      this.user.luna_team_no = this.teamNo
      this.selectTeamData = {}
      for (let idx in this.lunaTeamNoData) {
        teamDpCode = Number(this.lunaTeamNoData[idx].luna_dept_no)
        if (teamDpCode === Number(this.deptNo)) {
          this.selectTeamData[Number(this.lunaTeamNoData[idx].luna_team_no)] = this.lunaTeamNoData[idx].luna_team_name
        }
      }
    },
    teamNo () {
      if (this.user.luna_team_no !== Number(this.teamNo)) {
        this.partNo = ''
      }
      let partTmCode = 0
      this.user.luna_team_no = this.teamNo
      this.user.luna_part_no = this.partNo
      this.selectPartData = {}
      for (let idx in this.lunaPartNoData) {
        partTmCode = Number(this.lunaPartNoData[idx].luna_team_no)
        if (partTmCode === Number(this.teamNo)) {
          this.selectPartData[Number(this.lunaPartNoData[idx].luna_part_no)] = this.lunaPartNoData[idx].luna_part_name
        }
      }
    }
  },
  methods: {
    goList () {
      this.$router.go(-1)
    },
    typing: function (evt, type, name) {
      let message = evt.target.value
      // eslint-disable-next-line camelcase
      let pattern_kr = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/
      // eslint-disable-next-line camelcase
      let pattern_en = /[A-z]/
      // eslint-disable-next-line camelcase
      let pattern_no = /[0-9]/
      if (type === 'kr') {
        if (pattern_kr.test(message) === false) {
          if (name === 'luna_kor_name') this.user.luna_kor_name = ''
          else if (name === 'luna_eng_kor_nick_name') { this.user.luna_eng_kor_nick_name = '' }
        } else {
          if (name === 'luna_kor_name') { this.user.luna_kor_name = evt.target.value.trim() } else if (name === 'luna_eng_kor_nick_name') { this.user.luna_eng_kor_nick_name = evt.target.value.trim() }
        }
      } else if (type === 'en') {
        if (pattern_en.test(message) === false) {
          if (name === 'luna_eng_nick_name') this.user.luna_eng_nick_name = ''
        } else {
          if (name === 'luna_eng_nick_name') { this.user.luna_eng_nick_name = evt.target.value.trim() }
        }
      } else if (type === 'no') {
        if (pattern_no.test(message) === false) {
          if (name === 'tel2') this.user.tel2 = ''
          else if (name === 'tel3') this.user.tel3 = ''
        } else {
          if (name === 'tel2') this.user.tel2 = evt.target.value.trim()
          else if (name === 'tel3') this.user.tel3 = evt.target.value.trim()
        }
      }
    },
    // eslint-disable-next-line camelcase
    get (luna_no) {
      http
        .get('/account/accountedit', {
          params: {
            luna_no: luna_no
          }
        })
        .then(result => {
          this.user = result.data
          this.deptNo = result.data.luna_dept_no
          this.teamNo = result.data.luna_team_no || ''
          this.partNo = result.data.luna_part_no || ''
          this.watch.deptNo()
        })
        .catch(e => {
          console.log('AccountEdit.vue : ' + e)
        })
    },
    set () {
      http
        .post('/account/accountmod', {
          params: {
            luna_id: this.user.luna_id,
            resign_yn: this.user.resign_yn,
            luna_kor_name: this.user.luna_kor_name,
            luna_eng_kor_nick_name: this.user.luna_eng_kor_nick_name,
            luna_eng_nick_name: this.user.luna_eng_nick_name,
            luna_mobile_no:
              this.user.tel1 + '-' + this.user.tel2 + '-' + this.user.tel3,
            email_id: this.user.email_id + '@lunasoft.co.kr',
            luna_dept_no: this.deptNo,
            luna_team_no: this.teamNo,
            luna_part_no: this.partNo,
            luna_duty_no: this.user.luna_duty_no,
            luna_auth_no: this.user.luna_auth_no,
            login_luna_no: this.$store.getters.getUser.luna_no
          }
        })
        .then(result => {
          var msg = '정상적으로 계정이 수정되었습니다.'
          if (result.data !== '0000') {
            msg = '진행중에 문제가 발생했습니다.'
          }
          this.$modal.show(
            ModalAlert,
            {
              title: '계정정보 수정',
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
          console.log('AccountEdit.vue : ' + e)
        })
    }
  },
  components: {
    PhoneForm
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
