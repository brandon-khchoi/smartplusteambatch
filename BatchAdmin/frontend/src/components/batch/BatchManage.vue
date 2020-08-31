<template>
  <router-view
    :userMenuAuth="userMenuAuth" :codeData="codeData"
    :key="$route.fullPath" >
  </router-view>
</template>

<script>
import http from '@/http-commons'

export default {
  name: 'BatchManage',
  data () {
    return {
      codeData: {
        noticeDisplay: '',
        noticeType: '',
        lunaTeamInfo: ''
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
            menu: 'batch'
          }
        })
        .then(result => {
          this.codeData.batchCycleTypeCode = JSON.parse(result.data.batch_cycle_type_code)
          this.codeData.batchTimeTypeCode = JSON.parse(result.data.batch_time_type_code)
          this.codeData.lunaTeamInfo = JSON.parse(result.data.team_no)
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
