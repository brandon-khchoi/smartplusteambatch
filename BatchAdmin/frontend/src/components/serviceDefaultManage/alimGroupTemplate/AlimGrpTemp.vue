<template>
  <div class="AlimGrpTemp">
    <router-view
        :userMenuAuth="userMenuAuth"
        :http="http"
    ></router-view>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'AlimGrpTemp',
  data () {
    return {
    }
  },
  created () {
    // eslint-disable-next-line no-undef
    let url = apiServerUrl
    this.http = axios.create({baseURL: url})
  },
  methods: {
  },
  props: [
    'userMenuAuth'
  ]
}
</script>
