<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">
          스마트스토어 상품번호 : <a :href="item.product_store_url_addr +item.smartstore_product_no" target="_blank">{{item.smartstore_product_no}}</a><br>
          상품명 : {{ item.product_name }}
        </div>

        <!-- 옵션 타입에 따른 탭분리 시작-->
        <div class="modalTabContainer">
          <div class="tabWrapper">
            <ul class="modalTabBtnWrap">
              <template v-if="optionType === 'combination'">
                <li class="modalTabBtn on" id='combinationOptionList' @click="changeTab('combination')">조합 옵션</li>
              </template>
              <template v-if="optionType === 'simple'">
                <li class="modalTabBtn on" id='simpleOptionList' @click="changeTab('simple')">기본 옵션</li>
              </template>
              <template v-if="optionType === 'standard'">
                <li class="modalTabBtn on" id='standardOptionList' @click="changeTab('standard')">간편 옵션</li>
              </template>
              <template v-if="additionOptionYN">
                <li class="modalTabBtn" id='additionalOptionList' @click="changeTab('additionalOption')">추가 상품</li>
              </template>
            </ul>
          </div>

          <template v-if="showTabName === 'combination'">
            <ModalCombinationOptionList
            :product_info=item
            :list=requireOptionList
            :option_type=optionType
            :multiple_combinationYN=multipleCombinationYN
            :combination_option_name_list=combinationOptionNameList
            :read_only=false
            :get_option_list="get"
            >
            </ModalCombinationOptionList>
          </template>

          <template v-if="showTabName === 'simple'">
            <ModalSimpleOptionList
            :product_info=item
            :list=requireOptionList
            :option_type=optionType
            :read_only=false
            :get_option_list="get"
            >
            </ModalSimpleOptionList>
          </template>
          <template v-if="showTabName === 'standard' ">
            <ModalStandardOptionList
            :product_info=item
            :standard_option=standardOptionType
            :option_type=optionType
            :read_only=true
            :get_option_list="get"
            >
            </ModalStandardOptionList>
          </template>
          <template v-if="showTabName === 'additionalOption' ">
            <ModalSupplementProductList
            :product_info=item
            :list=additionalOptionList
            :option_type=optionType
            :read_only=false
            :get_option_list="get"
            >
            </ModalSupplementProductList>
          </template>

        </div>
        <template v-if="dataLen === 0 && isLoading === false">
          <div class="tab-message">
            <p>연동된 옵션이 없습니다.</p>
          </div>
        </template>
      <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
      </div>
      <!-- 옵션 타입에 따른 탭분리 끝 -->

      <div class="dialog-button-wrapper">
        <button type="button" class="dialog-button" @click="closeModal()">닫기</button>
      </div>
    </div>
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import http from '@/http-commons'
import ModalAlert from '@/components/common/modal/ModalAlert'
import ModalCombinationOptionList from '@/components/smartstore/product/option/ModalCombinationOptionList'
import ModalSimpleOptionList from '@/components/smartstore/product/option/ModalSimpleOptionList'
import ModalSupplementProductList from '@/components/smartstore/product/option/ModalSupplementProductList'
import ModalStandardOptionList from '@/components/smartstore/product/option/ModalStandardOptionList'

export default {
  name: 'ModalOptionEditList',
  components: {
    ModalCombinationOptionList,
    ModalSimpleOptionList,
    ModalSupplementProductList,
    ModalStandardOptionList
  },
  data () {
    return {
      isLoading: false,
      requireOptionList: [], // 옵션리스트(조합, 기본)
      additionalOptionList: [], // 추가옵션(상품) 옵션 리스트
      standardOptionType: {}, // 간편옵션 스마트스토어데이터
      editYn: false, // 스마트 스토어 미연동 리스트 탭 구분플래그
      multipleCombinationYN: false, // 조합옵션일 경우 조합옵션 리스트 개수가 2개 이상인 경우 true
      combinationOptionNameList: [], // 조합옵션 이름 리스트
      optionType: '', // simple, additionalOption, combination 값 3개 끝
      optionNameList: [], // 기본옵션 이름 리스트
      additionOptionYN: false, // 추가옵션 리스트 존재 여부
      showTabName: ''
    }
  },
  props: {
    'item': Object,
    'userMenuAuth': Object
  },
  mounted () {
    this.get()
  },
  computed: {
    dataLen () {
      let dataLen = 0
      if (this.requireOptionList != null) {
        dataLen += this.requireOptionList.length
      }
      if (this.additionalOptionList != null) {
        dataLen += this.additionalOptionList.length
      }
      if (this.standardOptionType != null && this.standardOptionType.standardOptionItemList != null && this.standardOptionType.standardOptionItemList.standardOptionItem != null) {
        dataLen += this.standardOptionType.standardOptionItemList.standardOptionItem.length
      }
      return dataLen
    }
  },
  methods: {
    async get (tabName) {
      let productNo = this.item.product_no
      let smartstoreProductNo = this.item.smartstore_product_no
      // eslint-disable-next-line no-undef
      this.isLoading = true

      const requirePromise = this.getOptionRequireList(smartstoreProductNo, productNo)
        .then(function (resolveData) {
          console.log('requirePromise success')
        }).catch(function (err) {
          console.error(err) // Error 출력
        })

      const standardPromise = this.getOptionStandardList(smartstoreProductNo, productNo)
        .then(function (resolveData) {
          console.log('standardPromise success')
        }).catch(function (err) {
          console.error(err) // Error 출력
        })

      const supplementPromise = this.getSupplementOption(smartstoreProductNo, productNo)
        .then(function (resolveData) {
          console.log('supplementPromise success')
        }).catch(function (err) {
          console.error(err) // Error 출력
        })
      const context = this
      Promise.all([requirePromise, standardPromise, supplementPromise]).then(function (values) {
        console.log('1,2,3 모두완료', values)
        if (tabName) {
          context.changeTab(tabName)
        } else {
          context.showTabName = context.optionType
        }
        context.isLoading = false
      })
    },
    getOptionRequireList (smartstoreProductNo, productNo) {
      console.log('getOptionRequireList')
      // eslint-disable-next-line no-undef
      let url = smartStroreApiUrl + '/smartstore/option/require/list/' + this.$store.state.searchParam.mall_id
      return new Promise((resolve, reject) =>
        http
          .get(url, {
            params: { // query string
              smartstore_product_no: smartstoreProductNo,
              product_no: productNo
            }
          })
          .then(response => {
            if (response.data.responseCode === -1 && response.data.data) {
              reject(new Error('응답오류'))
            } else {
              this.requireOptionList = response.data.data.requireOptionList ? response.data.data.requireOptionList : []
              this.multipleCombinationYN = response.data.data.multipleCombinationYN
              this.combinationOptionNameList = response.data.data.combinationOptionNameList ? response.data.data.combinationOptionNameList : []
              this.optionType = response.data.data.optionType ? response.data.data.optionType : this.optionType
              resolve()
            }
          })
          .catch(e => {
            console.log(e)
            if (e.code === 'ECONNABORTED') {
              // timeout
              reject(new Error('timeout'))
            } else {
              reject(new Error('서버오류'))
            }
          })
          .finally(() => {
          })
      )
    },
    getOptionStandardList (smartstoreProductNo, productNo) {
      console.log('getOptionStandardList')
      // eslint-disable-next-line no-undef
      let url = smartStroreApiUrl + '/smartstore/option/standard/list/' + this.$store.state.searchParam.mall_id
      return new Promise((resolve, reject) =>

        http
          .get(url, {
            params: { // query string
              smartstore_product_no: smartstoreProductNo,
              product_no: productNo
            }
          })
          .then(response => {
            if (response.data.responseCode === -1 && response.data.data) {
              reject(new Error('응답오류'))
            } else {
              // this.list = response.data.data.data
              this.standardOptionType = response.data.data.standardOptionType ? response.data.data.standardOptionType : {} // 간편옵션 api 응답
              this.optionType = response.data.data.optionType ? response.data.data.optionType : this.optionType
              resolve()
            }
          })
          .catch(e => {
            console.log(e)
            if (e.code === 'ECONNABORTED') {
              // timeout
              reject(new Error('timeout'))
            } else {
              reject(new Error('서버오류'))
            }
          })
          .finally(() => {
          })
      )
    },
    getSupplementOption (smartstoreProductNo, productNo) {
      console.log('getSupplementOption')
      // eslint-disable-next-line no-undef
      let url = smartStroreApiUrl + '/smartstore/option/supplement/list/' + this.$store.state.searchParam.mall_id
      return new Promise((resolve, reject) =>
        http
          .get(url, {
            params: { // query string
              smartstore_product_no: smartstoreProductNo,
              product_no: productNo
            }
          })
          .then(response => {
            if (response.data.responseCode === -1 && response.data.data) {
              reject(new Error('응답오류'))
            } else {
              // this.list = response.data.data.data
              this.additionalOptionList = response.data.data.additionalOptionList ? response.data.data.additionalOptionList : []
              this.additionOptionYN = response.data.data.additionOptionYN
              resolve()
            }
          })
          .catch(e => {
            console.log(e)
            if (e.code === 'ECONNABORTED') {
              // timeout
              reject(new Error('timeout'))
            } else {
              reject(new Error('서버오류'))
            }
          })
          .finally(() => {
          })
      )
    },
    validationCheck (requestFormData) {
      let overPriceYn = true
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
        // 0원인 기본옵션이 하나도 없는경우 -> 최소 한개 이상의 기본 옵션은 판매가와 동일해야함
        if (currnetOptionInfo.require_yn === '1' && parseInt(currnetOptionInfo.product_option_add_price) === 0) {
          overPriceYn = false
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
      if (overPriceYn) {
        this.$modal.show(ModalAlert,
          {title: '상품 옵션 수정 : ' + '추가금액', text: '기본옵션의 추가금액 변경 시, 최소 1개 이상의 옵션은 상품 가격과 같아야 합니다.'},
          { width: 500, height: 'auto' }
        )
        return false
      }
      return true
    },
    addComma (item, part) {
      let strNumber = event.target.value.replace(/,/g, '') // 컴마뺀 string 숫자
      let patternNo = /^[0-9]+$/
      // let patternNo = /[^0-9]/g
      if (patternNo.test(strNumber) === false) { // 숫자 아닐경우 input value 삭제
        event.target.value = 0
        item[part] = 0
      } else {
        item[part] = parseInt(strNumber)
      }
    },
    closeModal () {
      this.$emit('close')
    },
    changeTab (tabName) {
      console.log('request tabName : ' + tabName)
      this.showTabName = tabName
      console.log('after tabName : ' + this.showTabName)
    }
  }
}
</script>
<style scoped>
.tab-message {
    position: absolute;
    left: 50px;
    top: 90px;
  }
</style>
