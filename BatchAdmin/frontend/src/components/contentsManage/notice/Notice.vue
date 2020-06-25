<template>
  <router-view
      :userMenuAuth="userMenuAuth" :codeData="codeData"
      :key="$route.fullPath" >
  </router-view>
</template>

<script>
import http from '@/http-commons'

export default {
  name: 'Notice',
  data () {
    return {
      codeData: {
        noticeDisplay: '',
        noticeType: ''
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
            menu: 'notice'
          }
        })
        .then(result => {
          this.codeData.noticeDisplay = JSON.parse(result.data.notice_display)
          this.codeData.noticeType = JSON.parse(result.data.notice_type)
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
