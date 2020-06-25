<template>
  <router-view
      :codeData="codeData"
      :userMenuAuth="userMenuAuth"
      :key="$route.fullPath" >
  </router-view>
</template>

<script>
// import http from '@/http-commons'

export default {
  name: 'Monitor',
  data () {
    return {
      codeData: {}
    }
  },
  mounted () {
  },
  methods: {
  },
  props: {
    'userMenuAuth': Object
  }
}
</script>
