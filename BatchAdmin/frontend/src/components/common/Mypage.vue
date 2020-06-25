<template>
  <div class="Mypage">
    <div class="content mypage">
      <h1>마이페이지</h1>
      <div class="formContainer">
        <div class="mypageWrap">
          <div class="titleWrap">
            <p class="title">비밀번호 변경</p>
          </div>
          <div class="detailContent">
            <div class="tableRegister">
              <table>
                <caption class="blind">비밀번호 변경</caption>
                <colgroup>
                  <col style="width:160px" />
                  <col  />
                </colgroup>
                <tbody>
                  <tr>
                    <th>현재 비밀번호</th>
                    <td>
                      <input type="password"
                      placeholder="현재 비밀번호를 입력해주세요."
                          v-model="user[0]"/>
                    </td>
                  </tr>
                  <tr>
                    <th>변경할 비밀번호</th>
                    <td>
                      <input type="password"
                        maxlength="12"
                      placeholder="변경할 비밀번호를 입력해주세요."
                          v-model="user[1]"/>
                      <span class="info">6자 이상 영소문자, 숫자, 특수문자 입력</span>
                    </td>
                  </tr>
                  <tr>
                    <th>변경할 비밀번호 확인</th>
                    <td>
                      <input type="password"
                        maxlength="12"
                      placeholder="변경할 비밀번호를 한번 더 입력해주세요."
                          v-model="user[2]"/>
                      <span class="warning" v-if="user[2] && (user[1] != user[2])">
                        비밀번호가 일치하지 않습니다.
                      </span>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="2"
                        class="textRight"
                      >
                      <input v-show="user_btn" class="btn_save" type="button" value="적용" @click="changePassword(user)"/>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ModalAlert from './modal/ModalAlert'
import http from '../../http-commons'

export default {
  data () {
    return {
      user: [],
      user_btn: false
    }
  },
  watch: {
    user () {
      this.user_btn = false
      if (this.user[0] && this.user[1] && this.user[2]) {
        if (this.user[1] === this.user[2]) {
          this.user_btn = true
        }
      }
    }
  },
  methods: {
    changePassword (data) {
      http
        .post('/mypage/passwordmod', {
          params: {
            login_luna_no: this.$store.getters.getUser.luna_no,
            cur_password: data[0],
            new_password: data[1],
            new_password_cnfrm: data[2]
          }
        })
        .then(result => {
          if (result.data === '0000') {
            this.$modal.show(ModalAlert,
              {
                title: '비밀번호 변경 성공',
                text: '정상적으로 변경되었습니다.'
              }, {
                width: 450,
                height: 167
              }
            )

            // 해당 섹션 초기화
            this.user = []
          } else {
            this.$modal.show(ModalAlert,
              {
                title: '비밀번호 변경 에러',
                text: result.data === '0002' ? '기존비밀번호가 일치하지 않습니다.' : '처리중 에러가 발생했습니다.'
              }, {
                width: 450,
                height: 167
              })
            return false
          }
        })
        .catch(e => {
          console.log('Mypage.vue : ' + e)
        })
    }
  }
}
</script>
