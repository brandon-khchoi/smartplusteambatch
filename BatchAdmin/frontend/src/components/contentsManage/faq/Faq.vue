<template>
  <div class="Faq">
    <router-view
        :userMenuAuth="userMenuAuth" :codeData="codeData"
    ></router-view>
  </div>
</template>

<script>
import http from '@/http-commons'

export default {
  name: 'Faq',
  data () {
    return {
      codeData: {
        faqDisplay: '',
        faqType: '',
        lunaAccount: ''
      }
    }
  },
  mounted () {
    console.log('this is faq.vue mounted')
    this.retreiveCommonData()
  },
  methods: {
    retreiveCommonData () {
      http
        .get('/common/codelist', {
          params: {
            menu: 'faq'
          }
        })
        .then(result => {
          this.codeData.faqDisplay = JSON.parse(result.data.faq_display)
          this.codeData.faqType = JSON.parse(result.data.faq_type)
          this.codeData.lunaAccount = result.data.luna_account
        })
        .catch(e => {
          console.log('Faq.vue : ' + e)
        })
    }
  },
  props: [
    'userMenuAuth'
  ]
}
</script>
