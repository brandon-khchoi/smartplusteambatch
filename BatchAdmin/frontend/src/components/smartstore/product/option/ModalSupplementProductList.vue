<template>
  <div class="dialog-c-text">

    <template v-if="!readOnly">
      <template v-if="!editYn">
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
          <th>옵션이름</th>
          <th>옵션내용</th>
          <th>추가금액</th>
          <th>재고</th>
          <th>판매상태</th>
        </thead>
        <tbody>
          <!-- 옵션 수정 화면 -->
          <template v-if="editYn">
            <template v-for="(item, key) in editOptionList">
              <tr :key = "key" :style="[item.changed ? { 'background': '#fff0f0'} : {}]">
                <td class="text-center">
                  {{item.product_option_name | removeSparator}}
                </td>
                <td class="inputTd">
                  <input type="text" v-model="item.product_option_text" style="width: auto;" >
                </td>
                <td class="inputTd">
                  <input type="number" v-model.number="item.product_option_add_price" style="width: -webkit-fill-available;display:none;">
                  <input type="text" :value="item.product_option_add_price | currency"
                    @blur="callValidation(item.product_option_add_price,'number', key, 'product_option_add_price');"
                    @input="$parent.addComma(item, 'product_option_add_price');"
                   style="width: 100%;" />
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
                    <template v-if="item.product_option_stock_count == 0">
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
                  <td class="text-center">{{item.product_option_name | removeSparator}}</td>
                  <td class="text-center">{{item.product_option_text | removeSparator}}</td>
                  <td class="text-center">{{item.product_option_add_price | currency}}</td>
                  <td class="text-center">{{item.product_option_stock_count | currency}}</td>
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

    <template v-if="editYn">
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
  name: 'ModalSupplementProductList',
  data () {
    return {
      isLoading: false,
      productInfo: this.product_info,
      editOptionList: [],
      editYn: false,
      readOnly: this.read_only
    }
  },
  props: {
    'product_info': Object,
    'list': Array,
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
          // let oldValInner = oldVal
          newVal.filter(function (p, idx) {
            return Object.keys(p).some(function (prop) {
              if (prop === 'changed') {
                delete dataList[idx]['changed']
              } else {
                if (p[prop] !== initDataList[idx][prop]) { /** 여기가 데이터 변경되었을 경우의 분기 */
                  /** 상품이름 변경시 체크 끝 */
                  dataList[idx]['changed'] = true
                  return true
                }
              }
            })
          })
          // Log it
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
    callValidation (item, type, key, tagName, requireYn) {
      let editRequestValue = item
      let optionText = this.editOptionList[key].product_option_text

      switch (type) {
        case 'number' :
          if (tagName === 'product_option_add_price') {
            // eslint-disable-next-line no-undef
            if (!numberValidation(editRequestValue)) {
              switch (tagName) {
                case 'product_option_add_price' :
                  this.editOptionList[key].product_option_add_price = ''
                  break
              }
            } else {
              if (editRequestValue > 999999990) {
                this.editOptionList[key].product_option_add_price = ''
                this.$modal.show(ModalAlert,
                  {
                    title: '상품 옵션 수정 : ' + '추가상품 추가금액',
                    html: (key + 1) + '번째 추가상품 <strong>[' + optionText + ']</strong>의 추가금액은 999,999,990원 이하로만 입력 가능합니다.'
                  },
                  { width: 700, height: 'auto' }
                )
              }
            }
          } else if (tagName === 'product_option_stock_count') {
            let beforeOptionStockCount = this.list[key].product_option_stock_count
            // eslint-disable-next-line no-undef
            if (!numberValidation(editRequestValue)) {
              switch (tagName) {
                case 'product_option_stock_count' :
                  this.editOptionList[key].product_option_stock_count = beforeOptionStockCount
                  break
              }
            } else {
              if (parseInt(editRequestValue) === 0) {
                this.$modal.show(ModalAlert,
                  {
                    title: '상품 옵션 수정 : ' + '옵션 재고',
                    html: (key + 1) + '번째 옵션 <strong>[' + optionText + ']</strong>의 재고를 0으로 입력할 경우, 해당 옵션은 품절 처리됩니다.'
                  },
                  { width: 700, height: 'auto' }
                )
                return false
              }
            }
          }
          break
      }
    },
    validationCheck (requestFormData) {
      let alertText = ''
      let content = ''

      for (var pair of requestFormData.entries()) {
        let index = pair[0]
        let currnetOptionInfo = pair[1]
        let optionText = currnetOptionInfo.product_option_text
        for (const [key, value] of Object.entries(currnetOptionInfo)) {
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

        // 입력한 옵션 가격이 10원 단위이하인 경우
        let currentOptionAddPrice = currnetOptionInfo.product_option_add_price
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
      return true
    },
    callEdit () {
      this.smartStoreOptionEdit()
    },
    smartStoreOptionEdit () {
      let mallId = this.$store.state.searchParam.mall_id
      let smartstoreProductNo = this.productInfo.smartstore_product_no

      // eslint-disable-next-line no-undef
      let url = smartStroreApiUrl + '/smartstore/update/product/optiondata/' + mallId + '/' + smartstoreProductNo
      console.log('smartStoreOptionEdit')

      let productSimpleInfo = {
        product_no: this.productInfo.product_no,
        product_link_yn: this.productInfo.product_link_yn,
        product_luna_link_yn: this.productInfo.product_luna_link_yn,
        product_option_link_yn: this.productInfo.product_option_link_yn,
        product_price: this.productInfo.product_price,
        product_pc_discount_price: this.productInfo.product_pc_discount_price
      }

      let params = {
        user_code: 1,
        user_no: this.$store.getters.getUser.luna_no,
        mall_id: mallId,
        mall_no: this.$store.state.searchParam.mall_no,
        productSimpleInfo: productSimpleInfo,
        optionType: this.optionType,
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
        this.isLoading = true
        http
          .post(url, JSON.stringify(params))
          .then(response => {
            this.isLoading = false
            this.get_option_list('additionalOption')
            this.closeEdit()
            if (response.data.responseCode === 0) {
            // eslint-disable-next-line no-undef
              eventBus.$emit('modalOptionEditList')
              this.$modal.show(ModalAlert,
                {title: '상품옵션 정보 수정', text: '상품 옵션정보가 수정되었습니다.'},
                { width: 450, height: 'auto' }
              )
            } else {
              this.$modal.show(ModalAlert,
              // { title: '상품옵셩 정보 수정 실패', text: response.data.responseDetail }, { width: 450, height: 'auto' }
                { title: '상품옵션 정보 수정 실패', html: '상품옵션 수정 중 오류가 발생하였습니다. <br>잠시 후 다시 시도해주세요</br>' }, { width: 450, height: 'auto' }
              )
            }
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
.inputTd {
  padding: 3px 10px;
}
</style>
