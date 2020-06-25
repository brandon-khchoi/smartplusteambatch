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
          <p>삭제 하시겠습니까 ?</p>
         </div>
        </div>
       </article>
      </div>
     </div>
    </div>
   </div>
  <div class="dialog-button-wrapper">
   <button type="button" class="dialog-button" @click="$emit('close')">아니요</button>
   <button type="button" class="dialog-button" style="color: #37a8fe;" @click="dataDelete()">예</button>
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
    dataDelete () {
      http
        .post('/account/authgroupdel', {
          params: {
            luna_auth_no: this.data.luna_auth_no,
            login_luna_no: this.$store.getters.getUser.luna_no
          }
        })
        .then(result => {
          if (result.data === '0000') {
            this.$emit('close')
            eventBus.$emit('menuAuthModalCall')
          }
        })
        .catch(e => {
          console.log('ModalDeleteConfirm.vue : ' + e)
        })
    }
  },
  props: [
    'data',
    'title'
  ]
}
</script>
