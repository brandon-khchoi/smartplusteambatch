<!-- date 검색시 달력 포함 한달전 일주일 금일 날짜 세팅 -->

<template>
    <div class="calendarWrapper">
        <ul>
            <li>
                <input class="btn_calendar" type="button" value="오늘" :style="[isSmallDevice === true ? {width:'40px'}: {}]"
                        @click="funcDateSet(1)" :class="{active: propActiveBtn===1}"/>
            </li>
            <li>
                <input class="btn_calendar" type="button" value="7일" :style="[isSmallDevice === true ? {width:'40px'}: {}]"
                        @click="funcDateSet(2)" :class="{active: propActiveBtn===2}"/>
            </li>
            <li>
                <input class="btn_calendar" type="button" value="1개월" :style="[isSmallDevice === true ? {width:'40px'}: {}]"
                        @click="funcDateSet(3)" :class="{active: propActiveBtn===3}"/>
            </li>
            <li>
                <input class="btn_calendar" type="button" value="3개월" :style="[isSmallDevice === true ? {width:'40px'}: {}]"
                        @click="funcDateSet(4)" :class="{active: propActiveBtn===4}"/>
            </li>
            <li>
                <input class="btn_calendar" type="button" value="6개월" :style="[isSmallDevice === true ? {width:'40px'}: {}]"
                        @click="funcDateSet(5)" :class="{active: propActiveBtn===5}"/>
            </li>
            <li>
                <input class="btn_calendar" type="button" value="1년" :style="[isSmallDevice === true ? {width:'40px'}: {}]"
                        @click="funcDateSet(6)" :class="{active: propActiveBtn===6}"/>
            </li>
            <li>
                <input class="btn_calendar" type="button" value="전체" :style="[isSmallDevice === true ? {width:'40px'}: {}]"
                        @click="funcDateSet(7)" :class="{active: propActiveBtn===7}"/>
            </li>
        </ul>
        <!-- <template v-if="nextLine">
          <br>
        </template> -->
        <datepicker
            placeholder="Select Date"
            :language="languages['ko']"
            format="yyyy-MM-dd"
            v-model="fromDate"
            ref="openFromDate"
            calendar-class="my-datepicker_calendar"
            @focusout.native="closeFromDatePicker"
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
            @focusout.native="closeToDatePicker"
        >
        </datepicker>
        <img class="ui-datepicker-trigger" src="//static.lunasoft.co.kr/lunaadmin/images/calendar.png" style="margin-left:-3px;cursor: pointer" @click="openToDatePicker" >
    </div>
</template>

<script>
import Datepicker from 'vuejs-datepicker/dist/vuejs-datepicker.esm.js'
import * as lang from 'vuejs-datepicker/dist/locale'
import ModalAlert from '@/components/common/modal/ModalAlert.vue'
import { eventBus } from '@/main'

export default {
  props: {
    propFromDate: [String, Date],
    propActiveBtn: Number
    // propToDate: Date,
  },
  data () {
    return {
      languages: lang,
      // activeBtn: this.propActiveBtn,
      fromDate: this.propFromDate,
      toDate: new Date(),
      isSmallDevice: false // 노트북 깨지는 css
    }
  },
  mounted () {
    if (window.screen.width < 1510) {
      this.isSmallDevice = true
    }
    eventBus.$on('SmartDatepickerDateReset', () => {
      this.fromDate = ''
      this.toDate = new Date()
      this.activeBtn = 7
      this.$refs.openFromDate.clearDate()
      // 검색버튼 및 탭클릭으로 호출 됨
    })
  },
  beforeDestroy () {
    eventBus.$off('SmartDatepickerDateReset')
  },
  components: {
    Datepicker
  },
  watch: {
    fromDate (v, orgVal) {
      console.log('fromDate')
      if (v === '') { // 전체 선택한 경우
        this.$emit('updateFromDate', v)
      } else if (v === null) { // 초기화
        // this.fromDate = ''
        // this.$emit('updateFromDate', '')
        // eslint-disable-next-line no-undef
      } else if (dateFormatToString(v) > dateFormatToString(this.toDate)) {
        this.fromDate = new Date(orgVal)
        this.$emit('updateFromDate', orgVal)
        this.$modal.show(ModalAlert,
          {title: '기간 선택', text: '선택 기간이 올바르지 않습니다.'},
          {width: 450, height: 'auto'})
        // this.propFromDate = orgVal
      } else {
        this.$emit('updateFromDate', v)
      }
    },
    toDate (v, orgVal) {
      console.log('toDAte')
      if (this.fromDate === '' || this.fromDate === null) { // 초기화를 눌러서 fromDate 가 '' 가 된 경우
        this.$emit('updateToDate', v)
        // eslint-disable-next-line no-undef
      } else if (dateFormatToString(this.fromDate) > dateFormatToString(v)) {
        this.toDate = new Date(orgVal)
        this.$emit('updateToDate', orgVal)
        this.$modal.show(ModalAlert,
          {title: '기간 선택', text: '선택 기간이 올바르지 않습니다.'},
          {width: 450, height: 'auto'})
        // this.propToDate = orgVal
      } else {
        this.$emit('updateToDate', v)
      }
    }
  },
  methods: {
    funcDateSet (dateBtn) { /** 날짜 설정 버튼 */
      // this.activeBtn = dateBtn
      this.$emit('updateActiveBtn', dateBtn)
      if (dateBtn === 1) {
        let today = new Date()
        this.fromDate = today
        this.toDate = today
      } else if (dateBtn === 2) {
        let toDateTemp = new Date(this.toDate)
        this.fromDate = new Date(toDateTemp.setDate(toDateTemp.getDate() - 7))
      } else if (dateBtn === 3) {
        let toDateTemp = new Date(this.toDate)
        this.fromDate = new Date(toDateTemp.setMonth(toDateTemp.getMonth() - 1))
      } else if (dateBtn === 4) {
        let toDateTemp = new Date(this.toDate)
        this.fromDate = new Date(toDateTemp.setMonth(toDateTemp.getMonth() - 3))
      } else if (dateBtn === 5) {
        let toDateTemp = new Date(this.toDate)
        this.fromDate = new Date(toDateTemp.setMonth(toDateTemp.getMonth() - 6))
      } else if (dateBtn === 6) {
        let toDateTemp = new Date(this.toDate)
        this.fromDate = new Date(toDateTemp.setMonth(toDateTemp.getMonth() - 12))
      } else if (dateBtn === 7) {
        // let toDateTemp = new Date(this.toDate)
        this.fromDate = ''
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
