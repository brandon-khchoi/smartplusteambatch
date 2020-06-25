<template>
  <div class="content">
    <div class="tabContainer">
      <ul class="tabBtnWrap">
        <li class="tabBtn on" id="tab1" @click="changeTab('mallLogList')">적재오류 조회</li>
        <li class="tabBtn" id="tabb" @click="changeTab('smartStoreLogList')">스마트스토어 로그 조회</li>
      </ul>
    </div>
    <ul class="tabContents">
      <li class="tab tab1">
        <MallLogList
            v-if="showTabName === 'mallLogList'"
            :userMenuAuth=userMenuAuth
          >
        </MallLogList>
      </li>
      <li class="tab tabb">
          <SmartStoreLogList
            v-if="showTabName === 'smartStoreLogList'"
            :userMenuAuth=userMenuAuth
          >
        </SmartStoreLogList>
      </li>
    </ul>
  </div>
</template>

<script>
// import http from '@/http-commons'
import MallLogList from '@/components/monitor/MallLogList'
import SmartStoreLogList from '@/components/monitor/SmartStoreLogList'

export default {
  name: 'LogList',
  components: {
    MallLogList,
    SmartStoreLogList
  },
  data () {
    return {
      isLoading: false,
      userCode: 1,
      userNo: this.$store.getters.getUser.luna_no,
      showTabName: 'mallLogList'
    }
  },
  created () {
  },
  methods: {
    changeTab (tabName) {
      this.showTabName = tabName
    }
  },
  props: {
    userMenuAuth: Object
  }
}
</script>
