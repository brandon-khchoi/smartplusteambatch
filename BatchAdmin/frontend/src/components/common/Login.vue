<template>
  <div class="wrap">
    <header class="header">
      <h1 class="logo">
        <img
          src="//static.lunasoft.co.kr/lunaadmin/images/logo.png"
          width="220"
          height="60"
          alt="루나소프트"
        >
      </h1>
    </header>
    <div class="container">
      <section class="content login">
        <div class="formContainer">
          <div class="loginWrap">
            <img
              src="//static.lunasoft.co.kr/lunaadmin/images/logo_login.png"
              width="239"
              height="47"
              alt="루나소프트"
            >
            <input type="text" placeholder="아이디를 입력해주세요." v-model="id" @input="enFilter($event)">
            <input type="password" placeholder="비밀번호를 입력해주세요." v-model="pw" @keyup.enter="loginSubmit()" >
            <input class="btn_login" type="button" value="로그인" @click="loginSubmit()">
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
import ModalAlert from './modal/ModalAlert.vue'

export default {
  name: 'Login',
  data () {
    return {
      id: '',
      pw: '',
      alert: '',
      loading: false
    }
  },
  methods: {
    ...mapActions(['login']),
    async loginSubmit () {
      try {
        let loginResult = await this.login({
          uid: this.id,
          password: this.pw
        })
        if (loginResult) {
          this.goToPages()
        } else {
          this.$modal.show(ModalAlert,
            {
              title: '로그인',
              text: this.errorState
            }, {
              width: 450,
              height: 'auto'
            }
          )
        }
      } catch (err) {
        console.error('Login.vue err : ' + err)
      }
    },
    goToPages () {
      this.$router.push({
        name: 'dashboard'
      })
    },
    enFilter: function (evt) {
      let patternKr = /[ㄱ-ㅎㅏ-ㅣ가-힣]/
      let patternSp = /[~!@#$%^&*()<>=+’"'{};]/
      let txt = evt.target.value

      if (patternKr.test(txt) || patternSp.test(txt)) {
        this.id = evt.target._value
      } else {
        this.id = txt.trim()
      }
    }
  },
  computed: {
    ...mapGetters({
      errorState: 'getErrorState'
    })
  }
}
</script>
