<template>
  <div class="BusinessSale">
    <router-view
      :userMenuAuth="userMenuAuth"
      :memberStatCodeData="memberStatCodeData"
      :salesCodeData="salesCodeData"
      :salesStatCodeData="salesStatCodeData"
      :salesTypeCodeData="salesTypeCodeData"
      :memberInflowPathCodeData="memberInflowPathCodeData"
      :mallServiceGradeData="mallServiceGradeData"
      :managerSalesLunaNoData="managerSalesLunaNoData"
      :lunaAccount="lunaAccount"
    ></router-view>
  </div>
</template>

<script>
import http from '../../http-commons'

export default {
  name: 'Sales',
  data () {
    return {
      memberStatCodeData: [],
      salesCodeData: [],
      salesStatCodeData: [],
      salesTypeCodeData: [],
      memberInflowPathCodeData: [],
      mallServiceGradeData: [],
      managerSalesLunaNoData: [],
      lunaAccount: []
    }
  },
  mounted () {
    console.log('this is sales.vue mounted')
    this.retreiveCommonData()
  },
  methods: {
    retreiveCommonData () {
      http
        .get('/common/codelist', {
          params: {
            menu: 'sales'
          }
        })
        .then(result => {
          this.memberStatCodeData = JSON.parse(result.data.member_stat_code)
          this.salesCodeData = JSON.parse(result.data.sales_code)
          this.salesStatCodeData = JSON.parse(result.data.sales_stat_code)
          this.salesTypeCodeData = JSON.parse(result.data.sales_type_code)
          this.memberInflowPathCodeData = JSON.parse(result.data.member_inflow_path_code)
          // this.mallServiceGradeData = JSON.parse(result.data.mall_service_grade)
          this.managerSalesLunaNoData = JSON.parse(result.data.manager_sales_luna_no)
          this.lunaAccount = result.data.luna_account
        })
        .catch(e => {
          console.log('Sales.vue : ' + e)
        })
    }
  },
  props: [
    'userMenuAuth'
  ]
}
</script>
