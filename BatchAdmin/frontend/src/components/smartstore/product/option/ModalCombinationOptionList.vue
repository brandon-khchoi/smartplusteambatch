<template>
  <div class="dialog-c-text">

    <template v-if="!this.readOnly">
      <template v-if="!this.editYn">
        <div class="tableTopWrap tab-action">
          <div class="btnWrap">
            <ul>
              <li>
                <p class="btn_min" @click="openEdit()">
                    <i class="fas fa-pen"></i>수정
                </p>
              </li>
            </ul>
          </div>
        </div>
      </template>
    </template>

    <div class="tableWrap tableList tableHover"
      :style="[ list.length < 8 ? { 'overflow-y':'auto', 'height': 'auto' } : { 'overflow-y':'scroll', 'height': '600px' } ]">
      <table>
        <colgroup>
          <col style="width:15%"/>
          <col style="width:25%"/>
          <col style="width:15%"/>
          <col style="width:15%"/>
          <col style="width:10%"/>
        </colgroup>
        <thead>
          <template v-if="multipleCombinationYN">
            <template v-for="optionName in combination_option_name_list">
              <th :key=optionName.id>{{optionName}}</th>
            </template>
            <th>추가금액</th>
            <th>재고</th>
            <th>판매상태</th>
          </template>
          <template v-else>
            <th>옵션이름</th>
            <th>옵션내용</th>
            <th>추가금액</th>
            <th>재고</th>
            <th>판매상태</th>
          </template>
        </thead>
        <tbody>
          <!-- 옵션 수정 화면 -->
          <template v-if="this.editYn">
            <template v-for="(item, key) in editOptionList">
              <tr :key = "key" :style="[item.changed ? { 'background': '#fff0f0'} : {}]">
                <template v-if="multipleCombinationYN">
                  <template v-for="(textItem, key) in item.combinationOptionTextList">
                    <td :key="key" class="text-center">
                      <template v-if="(key+1) === item.combinationOptionTextList.length">
                        <!-- <input type="text" :key="key" v-model="item.combinationOptionTextList[key]" style="width: auto;" > -->
                        <input type="text" :key="key" v-model="item.combinationOptionTextList[key]" style="width: -webkit-fill-available;" >
                      </template>
                      <template v-else>
                        {{textItem}}
                      </template>
                    </td>
                  </template>
                </template>
                <template v-else>
                  <td class="text-center">{{item.product_option_name | removeSparator}}</td>
                  <td class="inputTd"><input type="text" v-model="item.product_option_text" style="width: auto;" ></td>
                </template>

                <td class="inputTd">
                  <input type="number" v-model.number="item.product_option_add_price" style="width: -webkit-fill-available;display:none;">
                  <input type="text" :value="item.product_option_add_price | currency"
                    @blur="callValidation(item.product_option_add_price,'number', key, 'product_option_add_price');"
                    @input="$parent.addComma(item, 'product_option_add_price');"
                   style="width: 100%;" />
                   <!--  -->
                  <!-- <input :id="'product_option_add_price_'+key" type="text" v-model="item.product_option_add_price" > -->
                </td>

                <td class="text-center inputTd">
                  <input type="number" v-model.number="item.product_option_stock_count" style="width: -webkit-fill-available;display:none;">
                  <input type="text" :value="item.product_option_stock_count | currency"
                    @blur="callValidation(item.product_option_stock_count,'number', key, 'product_option_stock_count');"
                    @input="$parent.addComma(item, 'product_option_stock_count');"
                   style="width: 100%;" />
                </td>

                <td class="text-center">
                  <template v-if="item.use_yn === '1'">
                    <template v-if="item.product_option_stock_count === 0">
                      품절
                    </template>
                    <template v-else>
                      SALE
                    </template>
                  </template>
                  <template v-else>
                    미 판매
                  </template>
                </td>
              </tr>
            </template>
          </template>

          <!-- 옵션 출력 화면 -->
          <template v-else>
            <template v-if="list.length > 0" >
              <template v-for="item in list">
                <tr :key = "item.id">
                  <template v-if="multipleCombinationYN">
                    <template v-for="textItem in item.combinationOptionTextList">
                      <td :key=textItem.id class="text-center">
                      {{textItem}}
                      </td>
                    </template>
                  </template>
                  <template v-else>
                    <td class="text-center">{{item.product_option_name | removeSparator}}</td>
                    <td class="text-center">{{item.product_option_text | removeSparator}}</td>
                  </template>

                  <td class="text-center">
                    {{item.product_option_add_price | currency}}
                  </td>

                  <td class="text-center">
                    {{item.product_option_stock_count | currency}}
                  </td>

                  <td class="text-center">
                    <template v-if="item.use_yn === '1'">
                      <template v-if="item.product_option_stock_count === 0">
                        품절
                      </template>
                      <template v-else>
                        판매중
                      </template>
                    </template>
                    <template v-else>
                      미 판매
                    </template>
                  </td>
                </tr>
              </template>
            </template>
          </template>
        </tbody>
      </table>
    </div>

    <template v-if="this.editYn">
      <div class="windowPopupWrap">
        <div class="formContainer">
            <article>
            </article>
            <div class="btnWrap">
              <ul>
                <li><input class="btn_cancel" type="button" value="취소" @click="closeEdit()"></li>
                <li><input class="btn_save" type="button" value="적용" @click="callEdit()"></li>
              </ul>
            </div>
        </div>
      </div>
    </template>

    <Spinner v-if="isLoading" />

  </div>
</template>

<script>
import http from '@/http-commons'
import ModalAlert from '@/components/common/modal/ModalAlert'
import { eventBus } from '@/main'

export default {
  name: 'ModalCombinationOptionList',
  data () {
    return {
      isLoading: false,
      productInfo: this.product_info,
      multipleCombinationYN: this.multiple_combinationYN,
      editOptionList: [],
      editYn: false,
      readOnly: this.read_only
    }
  },
  props: {
    'product_info': Object,
    'list': Array,
    'option_type': String,
    'multiple_combinationYN': Boolean,
    'combination_option_name_list': Array,
    'read_only': Boolean,
    get_option_list: {
      type: Function
    }
  },
  watch: {
    'editOptionList': {
      deep: true,
      handler (newVal, oldVal) {
        if (oldVal !== undefined && oldVal.length > 0) { // 페이지 로딩시 무조건 값 세팅되는것 방지
          // isChangedData 를 false 로 변경시 isResetData를 true 로 변경해야 handler 사용 가능
          let initDataList = this.list
          let dataList = this.editOptionList
          newVal.filter(function (p, idx) {
            return Object.keys(p).some(function (prop) {
              if (prop === 'changed') {
                delete dataList[idx]['changed']
              } else {
                if (p[prop] !== initDataList[idx][prop]) { /** 여기가 데이터 변경되었을 경우의 분기 */
                  /** 상품이름 변경시 체크 끝 */
                  if (prop === 'combinationOptionTextList') {
                    if (p[prop][initDataList[idx][prop].length - 1] !== initDataList[idx][prop][initDataList[idx][prop].length - 1]) {
                      dataList[idx]['changed'] = true
                    }
                  } else {
                    dataList[idx]['changed'] = true
                  }
                  return true
                }
              }
            })
          })
        }
      }
    }
  },
  methods: {
    openEdit () {
      this.editYn = true
      this.editOptionList = JSON.parse(JSON.stringify(this.list))
    },
    closeEdit () {
      this.editYn = false
    },
    callValidation (item, type, key, tagName) {
      let editRequestValue = item
      let optionText = this.editOptionList[key].product_option_text

      if (tagName === 'product_option_add_price') {
        // eslint-disable-next-line no-undef
        let productPrice = this.productInfo.smart_product_price
        let optionMaxPrice = (productPrice * 0.5) // 기본 옵션 -> 수정 가능한 최대값
        let optionMinPrice = (productPrice * -0.5) // 기본 옵션 -> 수정 가능한 최소값

        // 추가 상품일 경우 0~99,999,000원 내에서만 수정 가능
        if (editRequestValue > optionMaxPrice || editRequestValue < optionMinPrice) {
          this.editOptionList[key].product_option_add_price = 0
          this.$modal.show(ModalAlert,
            {
              title: '상품 옵션 수정 : ' + '기본옵션 추가금액',
              html: (key + 1) + '번째 기본옵션 <strong>[' + optionText + ']</strong>의 추가금액은 상품 판매가격(' + parseInt(productPrice).toLocaleString() + '원)의 50% 범위 이내에서 수정 가능합니다. <br><font color="gray">수정가능 금액 : ' + parseInt(optionMinPrice).toLocaleString() + '원 ~ ' + parseInt(optionMaxPrice).toLocaleString() + '원</font>'
            },
            { width: 700, height: 'auto' }
          )
          return false
        }
      } else if (tagName === 'product_option_stock_count') {
        let beforeOptionStockCount = this.list[key].product_option_stock_count

        let afterTotalStockCount = 0
        for (let index = 0; index < this.editOptionList.length; index++) {
          afterTotalStockCount += parseInt(this.editOptionList[index].product_option_stock_count)
        }

        let beforeTotalStockCount = 0
        for (let index = 0; index < this.list.length; index++) {
          beforeTotalStockCount += parseInt(this.list[index].product_option_stock_count)
        }

        if (parseInt(editRequestValue) === 0) {
          this.$modal.show(ModalAlert,
            {
              title: '상품 옵션 수정 : ' + '옵션 재고',
              html: (key + 1) + '번째 기본옵션 <strong>[' + optionText + ']</strong>의 재고를 0으로 입력할 경우, 해당 옵션은 품절 처리됩니다.'
            },
            { width: 700, height: 'auto' }
          )
          return false
        } else if (parseInt(afterTotalStockCount) > 99999999) {
          let editStockCount = 99999999 - (beforeTotalStockCount - beforeOptionStockCount)
          if (beforeTotalStockCount === 99999999) {
            editStockCount = 0
          }
          this.$modal.show(ModalAlert,
            {
              title: '상품 옵션 수정 : ' + '옵션 재고',
              html: '기본옵션의 재고 합은 99,999,999개 이하로 입력 하세요 <br>[입력 가능한 최대 재고량 : ' + editStockCount.toLocaleString() + '개]'
            },
            { width: 700, height: 'auto' }
          )
          this.editOptionList[key].product_option_stock_count = beforeOptionStockCount
          return false
        }
      }
    },
    validationCheck (requestFormData) {
      console.log('validationCheck')
      let overPriceYn = true
      let alertText = ''
      let content = ''

      for (var pair of requestFormData.entries()) {
        let index = pair[0]
        let currentOptionInfo = pair[1]
        let optionText = currentOptionInfo.product_option_text
        for (const [key, value] of Object.entries(currentOptionInfo)) {
          if (value === '') {
            switch (key) {
              case 'product_option_text':
                alertText = (index + 1) + '번째 옵션의 내용을 입력하세요'
                content = '옵션내용'
                break
              case 'product_option_add_price':
                alertText = (index + 1) + '번째 옵션의 추가 금액을 입력하세요'
                content = '추가금액'
                break
            }
            this.$modal.show(ModalAlert,
              {title: '상품 옵션 수정 : ' + content, html: alertText},
              { width: 450, height: 'auto' }
            )
            return false
          }
        }
        // 0원인 기본옵션이 하나도 없는경우 -> 최소 한개 이상의 기본 옵션은 판매가와 동일해야함
        if (currentOptionInfo.require_yn === 1 && parseInt(currentOptionInfo.product_option_add_price) === 0) {
          overPriceYn = false
        }

        // 입력한 옵션 가격이 10원 단위이하인 경우
        let currentOptionAddPrice = currentOptionInfo.product_option_add_price
        if ((parseInt(currentOptionAddPrice) % 10) !== 0) {
          this.$modal.show(ModalAlert,
            {
              title: '상품 옵션 수정 : ' + '추가금액',
              html: (index + 1) + '번째 기본옵션 <strong>[' + optionText + ']</strong>의 추가금액은 최소 10원 단위부터 수정 가능합니다.'
            },
            { width: 600, height: 'auto' }
          )
          return false
        }
      }
      if (overPriceYn) {
        this.$modal.show(ModalAlert,
          {title: '상품 옵션 수정 : ' + '추가금액', text: '기본옵션의 추가금액 변경 시, 최소 1개 이상의 옵션은 상품 가격과 같아야 합니다.'},
          { width: 500, height: 'auto' }
        )
        return false
      }
      return true
    },
    callEdit () {
      this.smartStoreOptionEdit()
    },
    smartStoreOptionEdit () {
      let mallId = this.$store.state.searchParam.mall_id
      let smartstoreProductNo = this.productInfo.smartstore_product_no

      if (this.multipleCombinationYN) { // 옵션이 2개 이상인 조합옵션의 validation 체크 및 옵션text 값 $$추가
        var nameList = this.combination_option_name_list // [색상, 사이즈]
        let index = 0
        for (var pair of this.editOptionList.entries()) {
          let currentOptionInfo = pair[1]
          let combinationOptionTextList = currentOptionInfo.combinationOptionTextList // EX) ["Gray", "M"]

          let optionText = ''
          var lastIdx = combinationOptionTextList.length - 1
          for (var i = 0; i < combinationOptionTextList.length; i++) {
            if (combinationOptionTextList[lastIdx] === '') {
              this.$modal.show(ModalAlert,
                {
                  title: '상품 옵션 수정',
                  html: (index + 1) + '번째 <strong>[' + nameList[lastIdx] + ']</strong> 의 옵션값을 입력하세요'
                },
                { width: 700, height: 'auto' }
              )
              return
            }

            if (i === 0) {
              optionText = combinationOptionTextList[i]
            } else {
              optionText += '$$' + combinationOptionTextList[i]
            }
          }
          currentOptionInfo.product_option_text = optionText
          index += 1
        }
      }

      let productSimpleInfo = {
        product_no: this.productInfo.product_no,
        smartstore_product_no: this.productInfo.smartstore_product_no,
        product_price: this.productInfo.product_price,
        product_pc_discount_price: this.productInfo.product_pc_discount_price
      }

      let params = {
        user_code: 1,
        user_no: this.$store.getters.getUser.luna_no,
        mall_id: mallId,
        mall_no: this.$store.state.searchParam.mall_no,
        productSimpleInfo: productSimpleInfo,
        optionType: this.option_type,
        optionNameList: this.combination_option_name_list,
        list: this.editOptionList
      }

      // console.log('params : ' + JSON.stringify(params))
      if (JSON.stringify(this.editOptionList) === JSON.stringify(this.list)) {
        this.$modal.show(ModalAlert,
          {title: '상품옵션 정보 수정', text: '변경 사항이 없습니다.'},
          { width: 450, height: 'auto' }
        )
        return false
      }

      if (this.validationCheck(this.editOptionList)) {
        // eslint-disable-next-line no-undef
        let url = smartStroreApiUrl + '/smartstore/update/product/optiondata/' + mallId + '/' + smartstoreProductNo
        this.isLoading = true
        http
          .post(url, JSON.stringify(params))
          .then(response => {
            this.isLoading = false
            this.get_option_list()
            this.closeEdit()
            if (response.data.responseCode === 0) {
              // eslint-disable-next-line no-undef
              eventBus.$emit('modalOptionEditList')
              this.$modal.show(ModalAlert,
                {title: '상품옵션 정보 수정', text: '상품 옵션정보가 수정되었습니다.'},
                { width: 450, height: 'auto' }
              )
            } else if (response.data.responseCode === -1 && response.data.data) {
              this.$modal.show(ModalAlert,
                { title: '상품옵션 정보 수정 실패', html: response.data.data.message + '<br>' + response.data.data.detail }, { width: 450, height: 'auto' }
              )
            } else if (response.data.responseCode === -1 && response.data.responseDetail) {
              this.$modal.show(ModalAlert,
                { title: '상품옵션 정보 수정 실패', html: response.data.responseDetail }, { width: 450, height: 'auto' }
              )
            } else {
              this.$modal.show(ModalAlert,
                { title: '상품옵션 정보 수정 실패', html: '상품옵션 수정 중 오류가 발생하였습니다. <br>잠시 후 다시 시도해주세요</br>' }, { width: 450, height: 'auto' }
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
          })
      }
    }
  }
}
</script>
<style scoped>
  .tab-action {
    position: absolute;
    right: 20px;
    top: 74px;
  }
input[type="text"] {
    min-height: 30px;
    height: 30px;
    font-size: 14px;
}
.inputTd {
  padding: 3px 10px;
}
</style>
