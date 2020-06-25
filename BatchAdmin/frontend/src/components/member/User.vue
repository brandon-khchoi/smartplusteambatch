<template>
    <div class="User">
         <router-view
            :codeData="codeData" :userMenuAuth="userMenuAuth" >
        </router-view>
    </div>
</template>

<script>

import http from '@/http-commons'

export default {
  name: 'User',
  data () {
    return {
      codeData: { /** select box data */
        memberTypeCodeData: '', // 회원구분
        memberStatCodeData: '', // 회원상태
        hostingNoData: '', // 호스팅
        inventoryCompanyCodeData: '', // 재고관리
        managerMaintainLunaNoData: '', // 영업담당
        managerSaleslunaNoData: '' // 운영담당
      }

    }
  },
  computed: {
    propsData () {
      console.log('propsData')
      console.log(this.$route.name)
      if (this.$route.name === 'UserList') {
        return {
          //  codeData: this.codeData,
          //  userMenuAuth :this.userMenuAuth,
          //  lunaAccount :  this.lunaAccount,
        }
      }
    }
    // myProps() {
    //     if (this.$route.name === 'a') { return { foo: this.foo }}
    //     if (this.$route.name === 'b') { return { bar: this.bar }}
    // }
  },
  created () {
    console.log('this is user.vue created')
    console.log(this.$route)
    this.retreiveCommonData()
  },
  methods: {
    retreiveCommonData () {
      http
        .get('/common/codelist', {
          params: {
            menu: 'user'
          }
        })
        .then(result => {
          console.log(result)
          console.log('start')
          this.codeData.memberTypeCodeData = JSON.parse(result.data.member_type_code)
          this.codeData.memberStatCodeData = JSON.parse(result.data.member_stat_code)
          this.codeData.hostingNoData = JSON.parse(result.data.hosting_no)
          this.codeData.inventoryCompanyCodeData = JSON.parse(result.data.inventory_company_code)
          this.codeData.managerMaintainLunaNoData = JSON.parse(result.data.manager_maintain)
          this.codeData.managerSaleslunaNoData = JSON.parse(result.data.manager_sales)
          this.codeData.serviceNoData = JSON.parse(result.data.service_no)
        })
        .catch(e => {
          console.log('codelist : ' + e)
        })
    }
  },
  props: {
    'userMenuAuth': Object
  }

}
</script>
