<template>
  <router-view
      :userMenuAuth="userMenuAuth" :codeData="codeData"
      :key="$route.fullPath" >
  </router-view>
</template>

<script>
import http from '@/http-commons'

export default {
  name: 'News',
  data () {
    return {
      codeData: {
        newsType: ''
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
            menu: 'news'
          }
        })
        .then(result => {
          this.codeData.newsType = JSON.parse(result.data.news_type)
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
