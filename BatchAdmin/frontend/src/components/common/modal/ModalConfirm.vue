<template>
<div class="DeleteConfirm content ">
 <div class="vue-modal-box">
  <div class="dialog-content">
   <div class="dialog-c-title">{{ title }}</div>
    <div class="dialog-c-text">
     <div class="userDetailPopup2">
      <div class="formContainer">
       <article>
        <div class="articleWrap">
         <div class="tableWrap tableRegister">
          <p>진행 하시겠습니까 ?</p>
         </div>
        </div>
       </article>
      </div>
     </div>
    </div>
   </div>
  <div class="dialog-button-wrapper">
   <button type="button" class="dialog-button" @click="$emit('close')">아니요</button>
   <button type="button" class="dialog-button" style="color: #37a8fe;" @click="confirm()">예</button>
  </div>
 </div>
</div>
</template>

<script>
// 삭제컨펌
import http from '../../../http-commons'
import { eventBus } from '../../../main'

export default {
  mounted () {},
  data () {
    return {}
  },
  methods: {
    confirm () {
      if (this.data.url !== '') {
        http
          .post(this.data.url, {
            params: {
              item: this.data.item
            }
          })
          .then(result => {
            if (result.data === '0000') {
              this.$emit('close')
              eventBus.$emit(this.data.refresh)
            }
          })
          .catch(e => {
            console.log('ModalConfirm.vue : ' + e)
          })
      } else {
        this.$emit('close')
        eventBus.$emit(this.data.refresh)
      }
    }
  },
  props: [
    'data',
    'title'
  ]
}
</script>
