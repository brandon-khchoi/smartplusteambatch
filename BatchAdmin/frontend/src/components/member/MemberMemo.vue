<template>
  <li class="tab userDetail">
    <article>
      <UserMemo
        :userMenuAuth=userMenuAuth
        :member_biz_name=member_biz_name
        :member_no=member_no
        ></UserMemo>
    </article>
      </li>
</template>

<script>

import UserMemo from '@/components/member/UserMemo'

export default {
  components: {
    UserMemo
  },
  props: [
    'userMenuAuth',
    'member_no',
    'member_biz_name'
  ]
}
</script>
