<!-- 기본 alert -->
<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">

          <template v-if="item.smartstore_product_no === item.product_no">
            <!-- 수기상품 옵션 -->
            스마트스토어 상품번호 : <a :href="item.product_store_url_addr +item.smartstore_product_no" target="_blank">{{item.smartstore_product_no}}</a><br>
          </template>
          <template v-else>
            <!-- 자사몰 옵션 -->
            자사몰 상품번호 : <a :href="item.product_mall_url_addr +item.product_no" target="_blank">{{item.product_no}}</a><br>
          </template>

          상품명 : {{ item.product_name }}
        </div>

        <!-- 옵션 타입에 따른 탭분리 시작-->
        <div class="modalTabContainer">
          <ul class="modalTabBtnWrap">
            <template v-if="optionType === 'combination'">
              <li class="modalTabBtn on" id='combinationOptionList' @click="changeTab('combination')">조합 옵션</li>
            </template>
            <template v-else>
              <li class="modalTabBtn on" id='simpleOptionList' @click="changeTab('simple')">기본 옵션</li>
            </template>
            <template v-if="additionOptionYN">
              <li class="modalTabBtn" id='additionalOptionList' @click="changeTab('additionalOption')">추가 상품</li>
            </template>
          </ul>

          <template v-if="showTabName === 'combination'">
            <ReadCombinationOptionList
            :list=requireOptionList
            :multiple_combinationYN=multipleCombinationYN
            :combination_option_name_list=combinationOptionNameList
            :get_option_list="get"
            >
            </ReadCombinationOptionList>
          </template>

          <template v-else-if="showTabName === 'simple'">
            <ReadSimpleOptionList
            :list=requireOptionList
            :get_option_list="get"
            >
            </ReadSimpleOptionList>
          </template>

          <template v-if="showTabName === 'additionalOption' ">
            <ReadSupplementProductList
            :list=additionalOptionList
            :get_option_list="get"
            >
            </ReadSupplementProductList>
          </template>
      </div>
      <!-- 옵션 타입에 따른 탭분리 끝 -->

        <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
      </div>
      <div class="dialog-button-wrapper">
        <button type="button" class="dialog-button" @click="$emit('close')">닫기</button>
      </div>
    </div>
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import http from '@/http-commons'
import ModalAlert from '@/components/common/modal/ModalAlert'
import ReadCombinationOptionList from './ReadCombinationOptionList'
import ReadSimpleOptionList from './ReadSimpleOptionList'
import ReadSupplementProductList from './ReadSupplementProductList'

// 첫번째 탭, 두번째 탭, 세번째 탭에서 다 사용. MALL_DB 뿐 아니라 세번째 탭에서는 SMARTSTORE DB 사용

export default {
  name: 'ModalOptionList',
  components: {
    ReadCombinationOptionList,
    ReadSimpleOptionList,
    ReadSupplementProductList
  },
  data () {
    return {
      isLoading: false,
      requireOptionList: [], // 기본옵션 및 조합옵션 리스트
      additionalOptionList: [], // 추가상품
      multipleCombinationYN: false, // 조합상품시 조합이 2depth 이상
      combinationOptionNameList: [], // 조합옵션 옵션이름
      optionType: '', // 첫번째 탭
      additionOptionYN: false, // 추가상품 여부
      showTabName: '' // 노출하고 있는 탭
    }
  },
  props: {
    'item': Object
  },
  mounted () {
    this.get()
  },
  methods: {

    get () {
      let productNo = this.item.product_no
      this.isLoading = true
      let url = '/smartstore/productmatch/optionlist'
      // 수기등록 상품에서 옵션 [보기] 버튼 클릭하는 경우
      if (this.item.smartstore_product_no === this.item.product_no) {
        // productNo = this.item.smartstore_product_no
        url = '/smartstore/productmatch/store/unmatched/optionlist'
      }

      http
        .get(url, {
          params: { // query string
            mall_id: this.$store.state.searchParam.mall_id,
            product_no: productNo
          }
        })
        .then(response => {
          this.isLoading = false
          // this.list = response.data.data
          this.requireOptionList = response.data.data.requireOptionList
          this.additionalOptionList = response.data.data.additionalOptionList
          this.multipleCombinationYN = response.data.data.multipleCombinationYN
          this.combinationOptionNameList = response.data.data.combinationOptionNameList
          this.optionType = response.data.data.optionType
          this.additionOptionYN = response.data.data.additionOptionYN
          this.showTabName = this.optionType
        })
        .catch(e => {
          // console.log(e)
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          )
        })
        .finally(() => {
          this.isLoading = false
        })
    },
    changeTab (tabName) {
      this.showTabName = tabName
    }
  }
}
</script>
