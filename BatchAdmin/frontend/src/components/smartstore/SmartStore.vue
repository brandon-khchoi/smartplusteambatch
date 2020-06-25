<template>
  <router-view
      :codeData="codeData"
      :key="$route.fullPath" >
  </router-view>
</template>

<script>
// import http from '@/http-commons'

export default {
  name: 'SmartStore',
  data () {
    return {
      codeData: {}
    }
  },
  mounted () {

  },
  methods: {
  }
  // ,
  // props: {
  //   'userMenuAuth': Object
  // }
}
</script>
