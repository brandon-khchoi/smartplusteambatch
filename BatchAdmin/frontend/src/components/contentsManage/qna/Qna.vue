<template>
  <router-view
      :userMenuAuth="userMenuAuth" :codeData="codeData"
      :key="$route.fullPath" >
  </router-view>
</template>

<script>
import http from '@/http-commons'

export default {
  name: 'Qna',
  data () {
    return {
      codeData: {
        lunaAccount: ''
      }
    }
  },
  mounted () {
    this.retreiveCommonData()
  },
  methods: {
    retreiveCommonData () {
      http
        .get('/common/codelist', {
          params: {
            menu: 'qna'
          }
        })
        .then(result => {
          this.codeData.lunaAccount = result.data.luna_account
        })
        .catch(e => {
          console.log('Faq.vue : ' + e)
        })
    }

  },
  props: {
    'userMenuAuth': Object
  }
}
</script>
