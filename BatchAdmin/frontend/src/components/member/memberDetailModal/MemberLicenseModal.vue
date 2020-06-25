<!-- 기본 alert -->
<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">{{ title }}</div>
        <div class="dialog-c-text">
          <img :src="imgUrl">
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
  data () {
    return {
    }
  },
  props: ['imgUrl', 'title']
}
</script>
