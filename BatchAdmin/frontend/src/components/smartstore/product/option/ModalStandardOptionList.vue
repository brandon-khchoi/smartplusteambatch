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
      :style="[ standard_option.standardOptionItemList.standardOptionItem.length < 8 ? { 'overflow-y':'auto', 'height': 'auto' } : { 'overflow-y':'scroll', 'height': '600px' } ]">
      <table>
        <colgroup>
          <template v-for="optionGroup in standard_option.standardOptionGroupList.standardOptionGroup">
            <col style="width:100%" :key="optionGroup.id"/>
          </template>
          <col style="width:100%"/>
          <col style="width:100%"/>
        </colgroup>
        <thead>
          <template v-for="optionGroup in standard_option.standardOptionGroupList.standardOptionGroup">
            <th :key=optionGroup.id>{{optionGroup.groupName}}</th>
          </template>
          <th>재고수량</th>
          <th>사용여부</th>
        </thead>
        <tbody>
          <!-- 옵션 수정 화면 -->
          <template v-if="editYn">
            <tr v-if="standard_option.useStockManagement === 'N'">
              <td :colspan="standard_option.standardOptionGroupList.standardOptionGroup.length + 2">
                <template v-if="standard_option.useStockManagement === 'N'">
                  해당 상품은 재고 수량을 관리 하지 않는 상품입니다.
                </template>
              </td>
            </tr>
            <template v-for="item in editOptionList">
              <tr :key = item.id :style="[item.changed ? { 'background': '#fff0f0'} : {}]">
                <td class="text-center">
                  {{item.optionValue1}}
                </td>
                <td class="text-center">
                  {{item.optionValue2}}
                </td>
                <td class="text-center inputTd">
                  <input type="number" v-model.number="item.quantity" style="width: auto;display:none;" >
                  <input type="text" :value="item.quantity | currency"
                    @input="$parent.addComma(item, 'quantity')"
                    :disabled="standard_option.useStockManagement === 'N' ? true : false"
                   style="width: 100%;" />
                </td>
                <td class="text-center">
                  <template v-if="item.usable === 'Y'">
                    사용
                  </template>
                  <template v-else>
                    미사용
                  </template>
                </td>
              </tr>
            </template>
          </template>
          <!-- 옵션 출력 화면 -->
          <template v-else>
            <!-- <tr>
              <td :colspan="standard_option.standardOptionGroupList.standardOptionGroup.length + 2">
                이 상품은 간편옵션이 포함된 상품입니다.
              </td>
            </tr> -->
            <template v-for="optionItem in standard_option.standardOptionItemList.standardOptionItem">
            <tr :key=optionItem.id>
              <td>
                {{optionItem.optionValue1}}
              </td>
              <td>
                {{optionItem.optionValue2}}
              </td>
              <td>
                {{optionItem.quantity | currency}}
              </td>
              <td>
                <template v-if="optionItem.usable === 'Y'">
                  사용
                </template>
                <template v-else>
                  미사용
                </template>
              </td>
            </tr>
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
  name: 'ModalStandardOptionList',
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
    'standard_option': Object,
    'option_type': String,
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
          let initDataList = this.standard_option.standardOptionItemList.standardOptionItem
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
      console.log('openEdit')
      this.editYn = true
      this.editOptionList = JSON.parse(JSON.stringify(this.standard_option.standardOptionItemList.standardOptionItem))
    },
    closeEdit () {
      this.editYn = false
    },
    callEdit () {
      this.smartStoreOptionEdit()
    },
    validationCheck (requestFormData) {
      return true
    },
    smartStoreOptionEdit () {
      console.log('smartStoreOptionEdit')
      let mallId = this.$store.state.searchParam.mall_id
      // let smartstoreProductNo = this.productInfo.smartstore_product_no

      if (JSON.stringify(this.editOptionList) === JSON.stringify(this.standard_option.standardOptionItemList.standardOptionItem)) {
        this.$modal.show(ModalAlert,
          {title: '상품옵션 정보 수정', text: '변경 사항이 없습니다.'},
          { width: 450, height: 'auto' }
        )
        return false
      }

      this.standard_option.standardOptionItemList.standardOptionItem = this.editOptionList
      let params = {
        standardOptionType: this.standard_option
      }

      if (this.validationCheck(this.editOptionList)) {
        this.isLoading = true
        // eslint-disable-next-line no-undef
        let url = smartStroreApiUrl + '/smartstore/update/product/standard/' + mallId
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
            } else {
              this.$modal.show(ModalAlert,
              // { title: '상품옵셩 정보 수정 실패', text: response.data.responseDetail }, { width: 450, height: 'auto' }
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
