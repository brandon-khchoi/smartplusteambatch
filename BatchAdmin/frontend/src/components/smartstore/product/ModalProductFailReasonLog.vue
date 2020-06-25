<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">
          연동실패사유
        </div>
        <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
        <div class="dialog-c-text">
          <div class="tableWrap tableList tableHover">
              <table>
                  <colgroup>
                    <col style="width:15%"/>
                    <col style="width:20%"/>
                    <col style="width:55%"/>
                    <col style="width:10%"/>
                  </colgroup>
                  <thead>
                    <tr>
                      <th>작업일시</th>
                      <th>에러코드</th>
                      <th>에러내용</th>
                      <th>담당자</th>
                    </tr>
                  </thead>
                  <tbody>
                    <template v-for="(item, idx) in resultData">
                      <tr :key="idx">
                          <td class="text-center"> {{item.logDate | dateFilter}}</td>
                          <td class="text-center"> {{item.createFailCode}} </td>
                          <td class="text-center"> {{item.responseDetail}} </td>
                          <td class="text-center"> {{item.logUserId}}</td>
                      </tr>
                    </template>
                    <tr v-if="resultData === ''">
                      <td colspan="4" class="emptyTable">
                        연동이력이 없습니다.
                      </td>
                    </tr>
                  </tbody>
              </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ModalProductLog',
  data () {
    return {
      resultData: ''
    }
  },
  created () {
  },
  mounted () {
    // eslint-disable-next-line no-undef
    let url = smartStroreApiUrl
    this.http = axios.create({baseURL: url})
    this.get()
    window.addEventListener('keyup', this.doStuff) // enter 클릭시 alert 창 닫기
  },
  beforeDestroy () {
    window.removeEventListener('keyup', this.doStuff)
  },
  filters: {
    dateFilter: function (val) {
      return val.replace('T', ' ')
    }
  },
  methods: {
    get () {
      this.http
        .get('/smartstore/productfaillog/' + this.item.mall_id + '/' + this.item.product_no)
        .then(result => {
          // console.log(result.data)
          this.isLoading = false
          if (result.data.size > 0) {
            this.resultData = result.data.data
          } else {
            this.resultData = ''
          }
        })
        .catch(e => {
          console.log('ModalProductLog.vue : ' + e)
        })
    },
    doStuff (event) {
      if (event.keyCode === 13) {
        this.callEvent()
      }
    },
    callEvent () {
      this.$emit('close')
    }
  },
  props: {
    'item': Object
  }
}
</script>
