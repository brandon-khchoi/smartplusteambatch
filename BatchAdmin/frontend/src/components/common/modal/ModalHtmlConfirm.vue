<template>
<div class="DeleteConfirm content ">
 <div class="vue-modal-box">
  <div class="dialog-content">
   <div class="dialog-c-title"><h2>{{ title }}</h2></div>
    <div class="dialog-c-text">
     <div class="userDetailPopup2">
      <div class="formContainer">
       <article>
        <div class="articleWrap">
         <div class="tableWrap tableRegister">
            <div v-if="html" v-html="html"></div>
         </div>
        </div>
       </article>
      </div>
     </div>
    </div>
   </div>
  <div class="dialog-button-wrapper">
  </div>

  <div class="formContainer">
    <form action="#" method="post" id="member_biz_info_form">
      <article>
      </article>
      <div class="btnWrap">
        <ul>
          <template v-if="data.btnText">
            <li><input type="button" :value=data.btnText.cancleText class="btn_cancel" @click="$emit('close')"></li>
            <li><input type="button" :value=data.btnText.confirmText class="btn_save" @click="confirm()"></li>
          </template>
          <template v-else>
            <li><input type="button" value="취소" class="btn_cancel" @click="$emit('close')"></li>
            <li><input type="button" value="적용" class="btn_save" @click="confirm()"></li>
          </template>
        </ul>
      </div>
      </form>
    </div>

 </div>
</div>
</template>

<script>
// 삭제컨펌
import http from '../../../http-commons'
import ModalAlert from '@/components/common/modal/ModalAlert'
import { eventBus } from '../../../main'

export default {
  mounted () {},
  data () {
    return {}
  },
  methods: {
    confirm () {
      console.log(JSON.stringify(this.data.item))
      if (this.data.url !== '') {
        http
          .post(this.data.url, this.data.requestParam
          )
          .then(response => {
            this.$emit('close')
            if (response.data.responseCode === 0) {
              eventBus.$emit('success', this.data.eventBusParam)
            } else {
              this.$modal.show(ModalAlert,
                { title: '', text: response.data.responseMsg }, { width: 450, height: 'auto' }
              )
            }
          })
          .catch(e => {
            console.log('ModalConfirm.vue : ' + e)
          })
      } else {
        this.$emit('close')
        console.log('else???')
        // eventBus.$emit('mallProductList', this.searchParam.current_page)
      }
    }
  },
  props: [
    'data',
    'btnText',
    'title',
    'html'
  ]
}
</script>

<style scoped>
.dialog-button-wrapper{
  display: none;
}
</style>
