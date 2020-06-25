<template>
    <select
      v-if="type == 'phone'"
      v-model="selectedNum"
      @input="$event => $emit('selectedNum', $event.target.value)"
      >
      <option
        v-for="option in phoneList"
        :value="option" :key="option.id">
        {{option}}
      </option>
    </select>

    <select
      v-else
      v-model="selectedFax"
      @input="$event => $emit('selectedFax', $event.target.value)"
      >
      <option
        v-for="option in faxList"
        :value="option" :key="option.id">
        {{option}}
      </option>
    </select>
</template>

<script>
export default {
  mounted () {
    if (this.number) {
      this.selectedNum = this.number
      this.selectedFax = this.number
    } else {
      this.selectedNum = '010'
      this.selectedFax = '02'
    }
  },
  watch: {
    number () {
      if (this.number) {
        this.selectedNum = this.number
        this.selectedFax = this.number
      } else {
        this.selectedNum = '010'
        this.selectedFax = '02'
      }
    }
  },
  data () {
    return {
      selectedNum: '010',
      selectedFax: '02',
      phoneList: {
        '010': '010',
        '011': '011',
        '02': '02',
        '031': '031',
        '032': '032',
        '033': '033',
        '041': '041',
        '042': '042',
        '043': '043',
        '044': '044',
        '051': '051',
        '052': '052',
        '053': '053',
        '054': '054',
        '055': '055',
        '061': '061',
        '062': '062',
        '063': '063',
        '064': '064',
        '070': '070',
        '016': '016',
        '017': '017',
        '018': '018',
        '019': '019'
      },
      faxList: {
        '02': '02',
        '031': '031',
        '032': '032',
        '033': '033',
        '041': '041',
        '042': '042',
        '043': '043',
        '044': '044',
        '051': '051',
        '052': '052',
        '053': '053',
        '054': '054',
        '055': '055',
        '061': '061',
        '062': '062',
        '063': '063',
        '064': '064',
        '070': '070'
      }
    }
  },
  props: {
    'type': {type: String, required: true},
    'number': {type: String, required: false}
  }
}
</script>
