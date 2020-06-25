<template>
  <router-view
      :codeData="codeData" :userMenuAuth="userMenuAuth" >
  </router-view>
</template>

<script>
import http from '@/http-commons'

export default {
  name: 'Setting',
  data () {
    return {
      codeData: {}
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
            menu: 'setting'
          }
        })
        .then(result => {
          console.log('codelist : ' + result)
          this.codeData.serviceStatCodeData = JSON.parse(result.data.service_stat_code)
          this.codeData.serviceStatCodeOrderData = JSON.parse(result.data.service_stat_code_order)
        })
        .catch(e => {
          // console.log('codelist : ' + e)
        })
    }
  },
  props: {
    'userMenuAuth': Object
  }
}
</script>
