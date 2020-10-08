<template>
  <div class="test-message">
    {{testMsgShow}}
  </div>
</template>

<script>

export default {
  name: 'Footer',
  data () {
    return {
    }
  },
  computed: {
    testMsgShow () {
      let msg = ''
      if (location.host === 'batchadmin.lunasoft.co.kr') {
        msg = '' // 리얼
      } else {
        msg = 'TEST'
      }
      return msg
    }
  },
  methods: {

  }
}
</script>
<style scoped>
.test-message {
  position: fixed;
  top: 40%;
  left: 45%;
  font-size: 100px;
  opacity: 0.1;
  pointer-events: none;
  z-index: 1000;
}
</style>
