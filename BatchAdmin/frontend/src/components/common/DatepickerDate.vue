<!-- date 검색시 달력 포함 한달전 일주일 금일 날짜 세팅 -->

<template>
    <div class="calendarWrapper">
        <ul v-if="!isButtonHide">
            <li>
                <input class="btn_calendar" type="button" value="오늘"
                        @click="funcDateSet(1)" :class="{active: dateSetBtn==1}"/>
            </li>
            <li>
                <input class="btn_calendar" type="button" value="7일"
                        @click="funcDateSet(2)" :class="{active: dateSetBtn==2}"/>
            </li>
            <li>
                <input class="btn_calendar" type="button" value="1개월"
                        @click="funcDateSet(3)" :class="{active: dateSetBtn==3}"/>
            </li>
            <li>
                <input class="btn_calendar" type="button" value="3개월"
                        @click="funcDateSet(4)" :class="{active: dateSetBtn==4}"/>
            </li>
        </ul>
        <datepicker
            placeholder="Select Date"
            :language="languages['ko']"
            format="yyyy-MM-dd"
            v-model="fromDate"
            ref="openFromDate"
            calendar-class="my-datepicker_calendar"
        >
        </datepicker>
        <img class="ui-datepicker-trigger" src="//static.lunasoft.co.kr/lunaadmin/images/calendar.png" style="margin-left:-3px;cursor: pointer" @click="openFromDatePicker">
        <span class="dash"/>
        <datepicker
            placeholder="Select Date"
            format="yyyy-MM-dd"
            :language="languages['ko']"
            v-model="toDate"
            ref="openToDate"
            calendar-class="my-datepicker_calendar"
        >
        </datepicker>
        <img class="ui-datepicker-trigger" src="//static.lunasoft.co.kr/lunaadmin/images/calendar.png" style="margin-left:-3px;cursor: pointer" @click="openToDatePicker" >
    </div>
</template>

<script>
import Datepicker from 'vuejs-datepicker/dist/vuejs-datepicker.esm.js'
import * as lang from 'vuejs-datepicker/dist/locale'
import ModalAlert from '@/components/common/modal/ModalAlert.vue'

export default {
  props: {
    propFromDate: Date,
    propToDate: Date,
    propsSetBtn: Number,
    propIsButtonHide: Boolean
  },
  data () {
    return {
      dateSetBtn: 0,
      languages: lang,
      fromDate: this.propFromDate,
      toDate: this.propToDate,
      isButtonHide: this.propIsButtonHide
    }
  },
  created () {
    if (this.propsSetBtn !== 'undefined') {
      this.dateSetBtn = this.propsSetBtn
    }
  },
  components: {
    Datepicker
  },
  watch: {
    fromDate (v, orgVal) {
      if (v > this.toDate) {
        this.fromDate = new Date(orgVal)
        this.$emit('update:fromDate', orgVal)
        this.$modal.show(ModalAlert,
          {title: '기간 선택', text: '선택 기간이 올바르지 않습니다.'},
          {width: 450, height: 'auto'})
        // this.propFromDate = orgVal
      } else {
        this.$emit('update:fromDate', v)
      }
    },
    toDate (v, orgVal) {
      if (this.fromDate > v) {
        this.toDate = new Date(orgVal)
        this.$emit('update:toDate', orgVal)
        this.$modal.show(ModalAlert,
          {title: '기간 선택', text: '선택 기간이 올바르지 않습니다.'},
          {width: 450, height: 'auto'})
        // this.propToDate = orgVal
      } else {
        this.$emit('update:toDate', v)
      }
    }
  },
  methods: {
    funcDateSet (dateSetBtn) { /** 날짜 설정 버튼 */
      this.dateSetBtn = dateSetBtn
      if (dateSetBtn === 1) {
        let today = new Date()
        this.fromDate = today
        this.toDate = today
      } else if (dateSetBtn === 2) {
        let toDateTemp = new Date(this.toDate)
        this.fromDate = new Date(toDateTemp.setDate(toDateTemp.getDate() - 7))
      } else if (dateSetBtn === 3) {
        let toDateTemp = new Date(this.toDate)
        this.fromDate = new Date(toDateTemp.setMonth(toDateTemp.getMonth() - 1))
      } else if (dateSetBtn === 4) {
        let toDateTemp = new Date(this.toDate)
        this.fromDate = new Date(toDateTemp.setMonth(toDateTemp.getMonth() - 3))
      }
    },
    openFromDatePicker () {
      this.$refs.openFromDate.$el.querySelector('input').focus()
      this.$refs.openFromDate.showCalendar()
    },
    openToDatePicker () {
      this.$refs.openToDate.$el.querySelector('input').focus()
      this.$refs.openToDate.showCalendar()
    },
    closeFromDatePicker () {
      if (this.$refs.openFromDate.isOpen) {
        this.$refs.openFromDate.close(true)
      }
    },
    closeToDatePicker () {
      if (this.$refs.openToDate.isOpen) {
        this.$refs.openToDate.close(true)
      }
    }
  }
}
</script>
