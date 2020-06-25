<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">
          작업이력
        </div>
        <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
        <div class="dialog-c-text">
          <div class="tableWrap tableList tableHover">
              <table>
                  <colgroup>
                    <col style="width:15%"/>
                    <col style="width:20%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                    <col style="width:10%"/>
                  </colgroup>
                  <thead>
                    <tr>
                      <th rowspan="2">작업일시</th>
                      <th rowspan="2">상품명</th>
                      <th rowspan="2">판매가</th>
                      <th rowspan="2">할인가</th>
                      <th rowspan="2"><p>판매상태</p>(재고상태)</th>
                      <th colspan="5">스마트스토어 동기화</th>
                      <th rowspan="2">담당자</th>
                      <th rowspan="2">옵션에러확인</th>
                    </tr>
                    <tr>
                      <th>가격</th>
                      <th>판매상태</th>
                      <th>옵션</th>
                      <th>상품이름</th>
                      <th>상품상세</th>
                    </tr>
                  </thead>
                  <tbody>
                    <template v-for="(item, idx) in resultData">
                      <tr :key="idx">
                          <td class="text-center"> {{item.log_date | dateFilter}}</td>
                          <td style="text-align: left;"> {{item.smartstore_product_name}}</td>
                          <td class="text-center"> {{item.smart_product_price | priceFilter}}</td>
                          <td class="text-center"> {{item.smart_product_pc_discount_price | priceFilter}}</td>
                          <td class="text-center"> {{item.smartstore_sell_yn===1?'판매중':item.smartstore_sell_yn===0?'미판매':''}}</td>
                          <td class="text-center">
                            <p class="toggleWrap">
                              <span class="inputWrap checkboxCenter">
                                <input type="checkbox" id="product_price_link_yn" disabled v-model="item.product_price_link_yn" :true-value=1 :false-value=0>
                                <label for="product_price_link_yn"></label>
                              </span>
                            </p>
                          </td>
                          <td class="text-center">
                            <p class="toggleWrap">
                              <span class="inputWrap checkboxCenter">
                                <input type="checkbox" id="product_sell_link_yn" disabled v-model="item.product_sell_link_yn" :true-value=1 :false-value=0>
                                <label for="product_sell_link_yn"></label>
                              </span>
                            </p>
                          </td>
                          <td class="text-center">
                            <p class="toggleWrap">
                              <span class="inputWrap checkboxCenter">
                                <input type="checkbox" id="product_option_link_yn" disabled v-model="item.product_option_link_yn" :true-value=1 :false-value=0>
                                <label for="product_option_link_yn"></label>
                              </span>
                            </p>
                          </td>
                          <td class="text-center">
                            <p class="toggleWrap">
                              <span class="inputWrap checkboxCenter">
                                <input type="checkbox" id="product_name_link_yn" disabled v-model="item.product_name_link_yn" :true-value=1 :false-value=0>
                                <label for="product_name_link_yn"></label>
                              </span>
                            </p>
                          </td>
                          <td class="text-center">
                            <p class="toggleWrap">
                              <span class="inputWrap checkboxCenter">
                                <input type="checkbox" id="product_detail_link_yn" disabled v-model="item.product_detail_link_yn" :true-value=1 :false-value=0>
                                <label for="product_detail_link_yn"></label>
                              </span>
                            </p>
                          </td>
                          <td class="text-center"> {{item.log_user_id}}</td>
                          <td class="text-center">
                            <p class="toggleWrap" v-show="item.opt_err">
                              <label class="switch">
                                <input type="checkbox" @click="funcOptErrShow('optErrMsg'+idx)">
                                <span class="slider"></span>
                              </label>
                            </p>
                          </td>
                      </tr>
                      <tr :key="'optErr'+idx" :id="'optErrMsg'+idx" hidden>
                        <td colspan="12" style="text-align : left" >{{item.opt_err | optErrMsg}}</td>
                      </tr>
                    </template>
                    <tr v-if="resultData === ''">
                      <td colspan="12" class="emptyTable">
                        작업이력이 없습니다.
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
    priceFilter: function (val) {
      if (val === 0 || val === undefined) {
        return ''
      } else {
        return val.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, '$1,')
      }
    },
    dateFilter: function (val) {
      return val.replace('T', ' ')
    },
    optErrMsg: function (val) {
      let msg = ''
      if (val !== undefined) {
        if (val.combination) {
          msg += '* ' + val.combination.substring(val.combination.indexOf('detail=') + 7, val.combination.indexOf(', message='))
        }
        if (val.supplement) {
          msg += '* ' + val.supplement.substring(val.supplement.indexOf('detail=') + 7, val.supplement.indexOf(', message='))
        }
        if (val.simple) {
          msg += '* ' + val.simple.substring(val.simple.indexOf('detail=') + 7, val.simple.indexOf(', message='))
        }
      }
      return msg
    }
  },
  methods: {
    get () {
      this.http
        .get('/smartstore/productlog/' + this.item.mall_id + '/' + this.item.product_no)
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
    },
    funcOptErrShow (optErrKey) {
      document.getElementById(optErrKey).hidden = !document.getElementById(optErrKey).hidden
    }

  },
  props: {
    'item': Object
  }
}
</script>
