<!-- 기본 alert -->
<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">
          {{ title }}
        </div>
        <div class="dialog-c-text">
          <p v-if="text">{{ text }}</p>
          <img v-if="imgSrc" :src="imgSrc"/>
          <div v-if="html" v-html="html">
          </div>
        </div>
      </div>
      <div class="dialog-button-wrapper">
        <button type="button" class="dialog-button" @click="$emit('close')">닫기</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ModalAlert',
  data () {
    return {
    }
  },
  created () {
  },
  mounted () {
    window.addEventListener('keyup', this.doStuff) // enter 클릭시 alert 창 닫기
  },
  beforeDestroy () {
    window.removeEventListener('keyup', this.doStuff)
  },
  methods: {
    doStuff (event) {
      if (event.keyCode === 13) {
        this.callEvent()
      }
    },
    callEvent () {
      this.$emit('close')
    }

  },
  props: {
    'text': String,
    'html': String,
    'title': String,
    'imgSrc': String
  }
}
</script>
