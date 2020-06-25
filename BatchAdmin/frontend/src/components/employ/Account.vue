<template>
  <div class="Account">
    <router-view
        :userMenuAuth="userMenuAuth"
        :lunaDeptNoData="lunaDeptNoData"
        :lunaTeamNoData="lunaTeamNoData"
        :lunaPartNoData="lunaPartNoData"
        :lunaDutyNoData="lunaDutyNoData"
        :lunaAuthGroupNoData="lunaAuthGroupNoData"
    ></router-view>
  </div>
</template>

<script>
import http from '../../http-commons'

export default {
  name: 'Account',
  data () {
    return {
      lunaDeptNoData: [],
      lunaTeamNoData: [],
      lunaPartNoData: [],
      lunaDutyNoData: [],
      lunaAuthGroupNoData: []
    }
  },
  mounted () {
    console.log('this is account.vue mounted')
    this.retreiveCommonData()
  },
  methods: {
    retreiveCommonData () {
      http
        .get('/common/codelist', {
          params: {
            menu: 'account'
          }
        })
        .then(result => {
          this.lunaDeptNoData = JSON.parse(result.data.dept_no)
          this.lunaTeamNoData = JSON.parse(result.data.team_no)
          this.lunaPartNoData = JSON.parse(result.data.part_no)
          this.lunaDutyNoData = JSON.parse(result.data.duty_no)
          this.lunaAuthGroupNoData = JSON.parse(result.data.luna_auth_group_no)
        })
        .catch(e => {
          console.log('Account.vue : ' + e)
        })
    }
  },
  props: [
    'userMenuAuth'
  ]
}
</script>
