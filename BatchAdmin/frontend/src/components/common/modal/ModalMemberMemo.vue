<template>
    <div name="modal-member-memo" class="ModalMemberMemo">
        <div class="vue-modal-box">
            <div class="dialog-content">
                <div class="titleWrap">
                    <h1>{{title}}</h1>
                </div>
                <UserMemo :member_no="memberNo" :userMenuAuth="userMenuAuth" :member_biz_name="title" ></UserMemo>
            </div>
            <div class="dialog-button-wrapper">
                <button type="button" class="dialog-button" @click="$emit('close')">닫기</button>
            </div>
        </div>
    </div>
</template>

<script>
import UserMemo from '@/components/member/UserMemo'

export default {
  name: 'ModalMemberMemo',
  components: {
    UserMemo
  },
  data () {
    return {
      memberNo: this.member_no
    }
  },
  props: [
    'title',
    'userMenuAuth',
    'member_no'
  ]
}
</script>
<style scoped>

</style>
