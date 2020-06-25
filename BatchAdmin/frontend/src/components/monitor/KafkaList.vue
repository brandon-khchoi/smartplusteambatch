<template>
  <div class="content">
    <div class="formContainer">
      <div class="bootstrapWrapper">
        <div style="backgorud:#ffffff">
            <b-embed
            type="iframe"
            aspect="16by9"
            :src="src"
          ></b-embed>
        </div>
      </div>
    </div>
    <v-dialog />
  </div>
</template>

<script>

export default {
  name: 'KafkaList',
  components: {
  },
  data () {
    return {
      userCode: 1,
      userNo: this.$store.getters.getUser.luna_no,
      src: 'http://172.31.3.130:8080'
    }
  },
  created () {
    // this.get()
  },
  methods: {
  },
  props: {
    userMenuAuth: Object
  }
}
</script>
