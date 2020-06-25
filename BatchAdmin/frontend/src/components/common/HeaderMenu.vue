<template>
  <header class="header">
    <h1 class="logo" @click="goMain()"><img src="//static.lunasoft.co.kr/lunaadmin/images/logo.png" width="220" height="60" alt="루나소프트 admin" /></h1>
    <ul>
      <li><p class="btn_mypage" @click="mypage()">마이페이지</p></li>
      <li><p class="btn_logout" @click="logoutProc()">로그아웃</p></li>
    </ul>
  </header>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'

export default {
  data () {
    return {
      loading: false
    }
  },
  methods: {
    ...mapActions(['logout']),
    async logoutProc () {
      try {
        let loginResult = await this.logout({
          uid: this.$store.getters.getUser.luna_id
        })
        if (loginResult) {
          this.clearFinalMenu_LS()
          this.goToPages()
        } else {
          console.log(this.errorState)
        }
      } catch (err) {
        console.error('HeaderMenu.vue logout err : ' + err)
      }
    },
    goToPages () {
      this.$router.push({
        name: 'login'
      })
    },
    goMain () {
      this.$router.push({
        name: 'dashboard'
      })
    },
    mypage () {
      this.clearFinalMenu_LS()
      this.$router.push({
        name: 'mypage'
      })
    },
    /* 새로고침 메뉴유지를 위한 local storage 제거 */
    clearFinalMenu_LS () {
      localStorage.removeItem('selectedMainIndex')
      localStorage.removeItem('selectedSubIndex')
      localStorage.removeItem('selectedUrl')
    }
  },
  computed: {
    ...mapGetters({
      errorState: 'getErrorState'
    })
  }
}
</script>
