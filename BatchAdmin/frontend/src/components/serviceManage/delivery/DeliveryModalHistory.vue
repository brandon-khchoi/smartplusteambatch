<!-- 기본 alert -->
<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">
          {{ title }}
        </div>
        <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
        <div class="dialog-c-text">
          <div id="emptyTable" class="tableWrap tableList scrollWrap" style="overflow-y:scroll;height:350px">
              <table>
                  <colgroup>
                        <col width="10%"/>
                        <col width="20%"/>
                        <col width="20%"/>
                        <col width="20%"/>
                  </colgroup>
                  <thead>
                    <tr>
                    <th rowspan="2">NO</th>
                    <th colspan="3">사용기간</th>
                    </tr>
                    <tr>
                      <th>시작일</th>
                      <th>종료일</th>
                      <th>사용기간일</th>
                    </tr>
                  </thead>
                  <tbody>
                    <template v-if="list.length > 0">
                      <template v-for="item in list">
                        <tr :key = "item.id">
                            <td class="text-center">{{item.firstCol}}</td>
                            <td class="text-center">{{item.secondCol}}</td>
                            <td class="text-center">{{item.thirdCol}}</td>
                            <td class="text-center">{{item.fourthCol}}</td>
                        </tr>
                      </template>
                    </template>
                    <tr v-else>
                      <td colspan="4" class="emptyTable" >
                        이력이 없습니다.
                      </td>
                    </tr>
                  </tbody>
              </table>
          </div>
        </div>
      </div>
      <div class="dialog-button-wrapper">
        <button type="button" class="dialog-button" @click="$emit('close')">닫기</button>
      </div>
    </div>
  </div>
</template>

<script>
import http from '@/http-commons'
import ModalAlert from '@/components/common/modal/ModalAlert'
export default {
  name: 'DeliveryModalHistory',
  data () {
    return {
      list: []
    }
  },
  props: {
    'title': String,
    'mall_no': Number
  },
  created () {
    this.get()
  },
  mounted () {

  },
  methods: {
    get () {
      // console.log('showHistory')
      // eslint-disable-next-line no-undef
      let url = apiServerUrl + '/serviceSetting/deliveryService/getDeliveryServiceHistory'
      let form = new FormData()
      form.append('mall_no', this.mall_no)
      http
        .post(url, form)
        .then(response => {
          this.isLoading = false
          // console.log(response)
          if (response.data.resultCode === 0) {
            let usePeriod = response.data.usePeriod

            let size = usePeriod.length
            // 처음 오픈 예정도 나옴
            if (size > 1) {
              let temp = {}
              for (let i = 1; i < size + 1; i++) {
                if (i === 1) {
                  continue
                }
                let historyDate = usePeriod[i - 1].history_date
                let statCode = usePeriod[i - 1].service_stat_code
                let nextStatCode = 0 // 0은 db 상태코드에 없어서 사용
                let nextHistoryDate = ''
                // let prevStatCode = this.settingData.openSetting.usePeriods[i - 2].service_stat_code
                // let prevHistoryDate = this.settingData.openSetting.usePeriods[i - 2].history_date
                if (i < size) {
                  nextStatCode = usePeriod[i].service_stat_code
                  nextHistoryDate = usePeriod[i].history_date
                }
                // index 2 부터
                if (statCode === 2 && nextStatCode === 0) {
                  temp.startDate = historyDate; temp.endDate = nextHistoryDate
                  temp.secondCol = temp.startDate.split('T')[0] + ' ' + temp.startDate.split('T')[1]
                  temp.thirdCol = '~'
                  temp.fourthCol = '-'
                  this.list.push(temp); temp = {}
                } else if (statCode === 2 && nextStatCode !== 0) {
                  temp.startDate = historyDate
                  temp.endDate = nextHistoryDate
                  // eslint-disable-next-line no-undef
                  let calDate = calDiffDate(new Date(temp.startDate), new Date(temp.endDate)); let diffDays = calDate.diffDays; let onlyTime = calDate.onlyTime
                  // eslint-disable-next-line no-undef
                  temp.fourthCol = diffDays + '일 ' + msToTime(onlyTime).hrs + '시간 ' + msToTime(onlyTime).mins + '분'

                  temp.secondCol = temp.startDate.split('T')[0] + ' ' + temp.startDate.split('T')[1]
                  temp.thirdCol = temp.endDate.split('T')[0] + ' ' + temp.endDate.split('T')[1]
                  this.list.push(temp); temp = {}
                }
              }

              for (let j = 1; j < this.list.length + 1; j++) {
                this.list[j - 1].firstCol = j
              }

              this.list.sort(function (a, b) { // 첫번째 행으로 내림차순
                return a.firstCol < b.firstCol ? 1 : a.firstCol > b.firstCol ? -1 : 0
              })
            }
          } else {
            this.$modal.show(ModalAlert,
              { title: '', text: '시스템 오류.' }, { width: 450, height: 'auto' }
            )
          }
        })
        .catch(e => {
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          )
        })
        .finally(() => {
          this.isLoading = false
          if (this.list.length < 1) {
            let div = document.getElementById('emptyTable')
            div.style.cssText = 'overflow-y: auto; height: auto;'
          }
        })
    }
  }
}
</script>
