<template>
    <p class="btn_down" v-on:click="onexport">
        <i class="fas fa-download"></i>엑셀 다운로드
    </p>
</template>

<script>
import XLSX from 'xlsx'
import ModalAlert from '../common/modal/ModalAlert.vue'

export default {
  name: 'ExcelDown',
  props: [
    'propJsonData',
    'propTitle',
    'propFileName'
  ],
  data () {
    return {

    }
  },
  mounted () {
  },
  methods: {
    onexport () { // On Click Excel download button
      if (this.propJsonData.length > 0) {
        let header = ''
        let title = this.propTitle

        let ws2 = XLSX.utils.json_to_sheet([
          title
        ], {header: header, skipHeader: true})
        XLSX.utils.sheet_add_json(ws2, this.propJsonData, {skipHeader: true, origin: 'A2'})

        // let animalWS = XLSX.utils.json_to_sheet(this.animals)
        // let pokemonWS = XLSX.utils.json_to_sheet(this.pokemons)

        // A workbook is the name given to an Excel file
        let wb = XLSX.utils.book_new() // make Workbook of Excel

        // add Worksheet to Workbook
        // Workbook contains one or more worksheets
        XLSX.utils.book_append_sheet(wb, ws2, 'main')
        // XLSX.utils.book_append_sheet(wb, animalWS, 'animals') // sheetAName is name of Worksheet
        // XLSX.utils.book_append_sheet(wb, pokemonWS, 'pokemons')
        // export Excel file
        XLSX.writeFile(wb, this.propFileName) // name of the file is 'book.xlsx'
      } else {
        this.$modal.show(ModalAlert,
          {
            title: '엑셀 다운로드',
            text: '선택된 데이터가 없습니다.'
          }, {
            width: 450,
            height: 'auto'
          }
        )
      }
    }
  }
}
</script>
