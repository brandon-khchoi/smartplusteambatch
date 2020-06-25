<!-- date 검색시 달력 포함 한달전 일주일 금일 날짜 세팅 -->

<template>
  <div >
    <datepicker
      class="datepicker"
      placeholder="예) 2019-07-27"
      :language="languages['ko']"
      format="yyyy-MM-dd"
      ref="openDate"
      :disabled-dates="disabledDates"
      v-model="date"
    />
    <img class="ui-datepicker-trigger" src="//static.lunasoft.co.kr/lunaadmin/images/calendar.png" style="margin-left:-5px" @click="openDatePicker" >
  </div>
</template>

<script>
import Datepicker from 'vuejs-datepicker/dist/vuejs-datepicker.esm.js'
import * as lang from 'vuejs-datepicker/dist/locale'

export default {
  props: {
    propDate: String
  },
  data () {
    return {
      languages: lang,
      date: this.propDate,
      disabledDates: {
        ranges: [{ // Disable dates in given ranges (exclusive).
          from: new Date(2016, 0, 1),
          to: new Date(new Date().setDate(new Date().getDate() - 1)) // 어제
        }]
      }
    }
  },
  created () {

  },
  components: {
    Datepicker
  },
  watch: {
    date (v) {
      console.log(v)
      // eslint-disable-next-line no-undef
      let date = dateFormatToString(v)
      this.$emit('update:startDate', date)
    }
  },
  methods: {

    openDatePicker () {
      this.$refs.openDate.showCalendar()
    }
  }
}
</script>
