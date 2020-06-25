<template>
  <div class="content">
    <h1>스마트스토어 상품관리</h1>
    <div class="formContainer">
      <div class="searchWrap">
        <CommonServiceList :propMallNoQuery= $route.query.mall_no></CommonServiceList>
        <template v-if="isFilterFormShow ===  true">

            <MallUnMatchedSearch v-show="showTabName === 'mallUnMatchedProductList'"
              :searchParam=MallUnMatchedSearchParam
              >
            </MallUnMatchedSearch>
            <MallSearch v-show="showTabName === 'mallProductList'"
              :searchParam=MallSearchParam
              >
            </MallSearch>
            <UnMatchedSearch v-show="showTabName === 'unMatchedProductList'"
              :searchParam=UnMatchedSearchParam
              >
            </UnMatchedSearch>
        </template>
        <div class="btnWrap" style="text-align: center;">
          <ul>
            <li>
              <p class="btn_search" @click="getChild(1)"><i class="fas fa-search"></i>검색</p>
            </li>
            <li>
              <p class="btn_search" @click="setSearchReset()" style="background: #fff;border: 1px solid #e3e5e8;color:#222222;">초기화</p>
            </li>
            <li style="float:right;">
              <div
                  class="btn_searchOpenToggle"
                  :class= "{searchOpen: isFilterFormShow}"
                  @click="showSearchFilter"
              >
               <i class="fas fa-chevron-down arrow"></i>
               <span></span>
              </div>
            </li>
          </ul>
        </div>
      </div>
      <ProductDashboard
        :key="$route.fullPath"
         />
      <div class="tabContainer">
        <ul class="tabBtnWrap">
            <li class="tabBtn on" id="tab1" @click="changeTab('mallUnMatchedProductList')">스마트스토어 미연동상품</li>
            <li class="tabBtn" id="tab2" @click="changeTab('mallProductList')">스마트스토어 연동상품</li>
            <li class="tabBtn" id="tab3" @click="changeTab('unMatchedProductList')">스마트스토어 수기등록상품</li>
            <template v-if="showTabName === 'mallProductList'">
            <div style="text-align: center;">
              <svg width="530" height="50" style="margin-top:20px;">
                <rect x="0" y="0" width="30" height="10" style="fill:#e4f0ff;stroke-width:1;stroke:#222222;stroke-opacity:1;fill-opacity:1"  />
                <text x="35" y="10" fill="black">자사몰에서 연동된상품</text>
                <rect x="165" y="0" width="30" height="10" style="fill:#ffffff;stroke-width:1;stroke:#222222;stroke-opacity:1;fill-opacity:1"  />
                <text x="200" y="10" fill="black">수기등록상품에서 매칭</text>
                <rect x="325" y="0" width="30" height="10" style="fill:#fff0f0;stroke-width:1;stroke:#222222;stroke-opacity:1;fill-opacity:1"  />
                <text x="360" y="10" fill="black">변경할상품</text>
                <rect x="425" y="0" width="30" height="10" style="fill:#f1f1f1;stroke-width:1;stroke:#222222;stroke-opacity:1;fill-opacity:1"  />
                <text x="460" y="10" fill="black">삭제된상품</text>
              </svg>
            </div>
            </template>
        </ul>

        <ul class="tabContents">
          <li class="tab tab1">
            <MallUnMatchedProductList
              v-show="showTabName === 'mallUnMatchedProductList'"
              :userMenuAuth=userMenuAuth
              :searchParam=MallUnMatchedSearchParam
            >
            </MallUnMatchedProductList>
          </li>
          <li class="tab tab2">
            <MallProductList
              v-show="showTabName === 'mallProductList'"
              :userMenuAuth=userMenuAuth
              :searchParam=MallSearchParam
            >
            </MallProductList>
          </li>

          <li class="tab tab3">
            <UnMatchedProductList
              v-show="showTabName === 'unMatchedProductList'"
              :userMenuAuth=userMenuAuth
              :searchParam=UnMatchedSearchParam
            >
            </UnMatchedProductList>
          </li>
        </ul>
      </div>
    </div>
    <v-dialog :clickToClose=false />
    <Spinner v-if="isLoading" />
  </div>

</template>

<script>

// 탭변수 정의
/**
 * 1. mallUnMatchedProductList : [스마트스토어 미연동상품] 탭
 * 2. mallProductList : [스마트스토어 연동상품] 탭
 * 3. unMatchedProductList : [스마트스토어 수기관리상품] 탭
 */
import CommonServiceList from '../CommonServiceList'
import MallUnMatchedProductList from './MallUnMatchedProductList' // [스마트스토어 미연동상품]
import UnMatchedProductList from './UnMatchedProductList' // [스마트스토어 수기등록상품]
import MallProductList from './MallProductList' // [스마트스토어 연동상품]
import MallUnMatchedSearch from './search/MallUnMatchedSearch' // [스마트스토어 미연동상품]
import MallSearch from './search/MallSearch' // [스마트스토어 미연동상품]
import UnMatchedSearch from './search/UnMatchedSearch'
import ProductDashboard from './ProductDashboard'
import ProductProgress from './ProductProgress'
import ModalAlert from '@/components/common/modal/ModalAlert'
import ModalOptionList from './optionRead/ModalOptionList'
import ModalCategory from './ModalCategory'
import ModalStoreCategory from './ModalStoreCategory'
import { eventBus } from '@/main'

const initMallUnMatchedSearchParam = {
  searhDateFlag: 0,
  fromDate: '',
  toDate: '',
  activeBtn: 7,
  per_page: 100,
  current_page: 1,
  orderby: 0,
  product_name: '',
  product_no: '',
  display_yn: 1, // mall_db 에서 판매상태
  hide_yn: 0,
  size_image_yn: '',
  product_price_check: false,
  product_price_start: '',
  product_price_end: '',
  product_pc_discount_price_check: false,
  product_pc_discount_price_start: '',
  product_pc_discount_price_end: '',
  product_category_no: ''
}

const initMallSearchParam = {
  searhDateFlag: 0,
  fromDate: '',
  toDate: '',
  activeBtn: 7,
  per_page: 100,
  current_page: 1,
  orderby: 0,
  product_name: '',
  smartstore_product_name: '',
  product_no: '',
  smartstore_product_no: '',
  display_yn: '', // mall_db 에서 판매상태
  smartstore_display_sellArr: [],
  smartstore_display_yn: '', // 스마트스토어에서는 전시상태
  smartstore_sell_yn: '', // 스마트스토어에서는 판매상태
  product_link_yn: '',
  product_price_check: false,
  product_price_start: '',
  product_price_end: '',
  product_pc_discount_price_check: false,
  product_pc_discount_price_start: '',
  product_pc_discount_price_end: '',
  smart_product_price_check: false,
  smart_product_price_start: '',
  smart_product_price_end: '',
  smart_product_pc_discount_price_check: false,
  smart_product_pc_discount_price_start: '',
  smart_product_pc_discount_price_end: ''
}

const initUnMatchedSearchParam = {
  searhDateFlag: 0,
  fromDate: '',
  toDate: '',
  activeBtn: 7,
  per_page: 100,
  current_page: 1,
  orderby: 0,
  smartstore_product_name: '',
  smartstore_product_no: '',
  smartstore_display_yn: '', // 스마트스토어에서는 전시상태
  smartstore_sell_yn: '', // 스마트스토어에서는 판매상태
  smartstore_display_sellArr: [],
  product_link_yn: '',
  smart_product_price_check: false,
  smart_product_price_start: '',
  smart_product_price_end: '',
  smart_product_pc_discount_price_check: false,
  smart_product_pc_discount_price_start: '',
  smart_product_pc_discount_price_end: ''
}

export default {
  name: 'ProductList',
  components: {
    CommonServiceList,
    UnMatchedProductList,
    MallUnMatchedProductList,
    MallProductList,
    MallUnMatchedSearch,
    MallSearch,
    UnMatchedSearch,
    ProductDashboard,
    ProductProgress
  },
  data () {
    return {
      isLoading: false,
      userNo: this.$store.getters.getUser.luna_no,
      allSelected: false,
      isFilterFormShow: false, // 검색필터 열기 닫기
      showTabName: 'mallUnMatchedProductList',
      store_info: {
        product_option_link_yn: 0,
        product_name_link_yn: 0,
        product_price_link_yn: 0,
        product_stock_link_yn: 0,
        product_sell_link_yn: 0
      },
      MallUnMatchedSearchParam: JSON.parse(JSON.stringify(initMallUnMatchedSearchParam)),
      MallSearchParam: JSON.parse(JSON.stringify(initMallSearchParam)),
      UnMatchedSearchParam: JSON.parse(JSON.stringify(initUnMatchedSearchParam))
    }
  },
  created () {

  },
  mounted () {
    console.log('mounted')
    this.setSearchReset()
    /** 자사몰명 변경시마다 호출 */
    eventBus.$on('commonServiceListChange', () => {
      this.defaultCategoryReset() // 자사몰 변경될때만 전체 카테고리 세팅
      if (this.showTabName === 'mallUnMatchedProductList') {
        this.getChild(1)
      } else if (this.showTabName === 'mallProductList') {
        this.getChild(1)
      } else if (this.showTabName === 'unMatchedProductList') {
        this.getChild(1)
      }
    })
  },
  beforeDestroy () {
    eventBus.$off('commonServiceListChange')
  },
  watch: {
    showTabName: {
      handler (newVal, oldVal) {
        console.log('showTabName')
        /** 탭 변경시 마다 호출 */
        if (newVal === 'mallUnMatchedProductList') {
          // this.searchCategoryReset()
          eventBus.$emit('mallUnMatchedProductList', 1)
        } else if (newVal === 'mallProductList') {
          // this.searchCategoryReset()
          eventBus.$emit('mallProductList', 1)
        } else if (newVal === 'unMatchedProductList') {
          // this.searchCategoryReset()
          eventBus.$emit('unMatchedProductList', 1)
        }
      }
    }
  },
  /**
   * ProductList 에 있는 메소드명은 함부로 바꾸면 안됨. 자식 컴포넌트에서 $parent.메소드 로 접근
   */
  methods: {
    getChild (currentPage) {
      if (this.showTabName === 'mallUnMatchedProductList') {
        // 이벤트 발생
        eventBus.$emit('mallUnMatchedProductList', currentPage)
      } else if (this.showTabName === 'mallProductList') {
        eventBus.$emit('mallProductList', currentPage)
      } else if (this.showTabName === 'unMatchedProductList') {
        eventBus.$emit('unMatchedProductList', currentPage)
      }
    },
    /**
     * 초기화 버튼 클릭시 변수 전체 리셋
     */
    setSearchReset () {
      console.log('setSearchReset')

      if (this.showTabName === 'mallUnMatchedProductList') {
        eventBus.$emit('dateMallUnMatchedSearchReset') // 데이트 picker reset
        this.MallUnMatchedSearchParam = JSON.parse(JSON.stringify(initMallUnMatchedSearchParam))
        this.MallUnMatchedSearchParam.toDate = new Date()
        this.searchCategoryReset()
      } else if (this.showTabName === 'mallProductList') {
        eventBus.$emit('dateMallSearchReset') // 데이트 picker reset
        this.MallSearchParam = JSON.parse(JSON.stringify(initMallSearchParam))
        this.MallSearchParam.toDate = new Date()
        this.searchCategoryReset()
      } else if (this.showTabName === 'unMatchedProductList') {
        eventBus.$emit('dateUnMatchedSearchReset') // 데이트 picker reset
        this.UnMatchedSearchParam = JSON.parse(JSON.stringify(initUnMatchedSearchParam))
        this.UnMatchedSearchParam.toDate = new Date()
        this.searchCategoryReset()
      }
    },
    addComma (item, part) {
      let strNumber = event.target.value.replace(/,/g, '') // 컴마뺀 string 숫자
      let patternNo = /^[0-9]+$/
      // let patternNo = /[^0-9]/g
      if (patternNo.test(strNumber) === false) { // 숫자 아닐경우 input value 삭제
        event.target.value = ''
      } else {
        item[part] = parseInt(strNumber)
      }
    },
    showSearchFilter (e) { /** 검색필터 열기 닫기 */
      this.isFilterFormShow = !this.isFilterFormShow
    },
    /**
     * 초기화 버튼 후 호출되는 카테고리 reset
     */
    searchCategoryReset () {
      console.log('searchCategoryReset')
      if (this.showTabName === 'mallUnMatchedProductList') {
        eventBus.$emit('mallUnMatchedCategoryReset')
      } else if (this.showTabName === 'mallProductList') {
        eventBus.$emit('mallCategoryReset')
      } else if (this.showTabName === 'unMatchedProductList') {
        eventBus.$emit('unMatchedCategoryReset')
      }
    },
    /**
     * 자사몰 변경시 전체 카테고리 새로 조회하기
     */
    defaultCategoryReset () {
      console.log('defaultCategoryReset')
      eventBus.$emit('mallUnMatchedCategoryReset')
      eventBus.$emit('mallCategoryReset')
      eventBus.$emit('unMatchedCategoryReset')
    },
    typing: function (evt, type, part, name) {
      let message = evt.target.value.trim()
      message = message.toString()
      let patternNo = /^[0-9]+$/
      // let patternNo = /[^0-9]/g
      if (type === 'no') {
        if (patternNo.test(message) === false) {
          // console.log('숫자만')
          if (message.length !== 0) {
          }
        }
      }
    },
    showImg (imgSrc) {
      let image = new Image()
      image.src = imgSrc
      let self = this
      image.onload = function () {
        let width = this.width > 500 ? '800px' : '500px'
        self.$modal.show(ModalAlert,
          {title: '', imgSrc: imgSrc},
          { width: width, height: 'auto', scrollable: true }
        )
      }
      image.onerror = function () {
        self.$modal.show(ModalAlert,
          {title: '', text: '이미지가 없습니다.'},
          { width: '500px', height: 'auto', scrollable: true }
        )
      }
    },
    showOption (item) {
      this.$modal.show(ModalOptionList,
        { item: item },
        { width: '55%', height: 'auto', scrollable: true }
      )
    },
    showHtml (html) {
      this.$modal.show(ModalAlert,
        {title: '', html: html},
        { width: '50%', height: 'auto', scrollable: true }
      )
    },
    /**
     * part : mall -> 스마트스토어 미연동상품 탭 자사몰 카테고리
     *        mallmatched -> 스마트스토어 연동상품 탭 자사몰카테고리
     *        store -> 스마트스토어 카테고리
     */
    showCategory (item, part, readonly) {
      if (part === 'mall') {
        this.$modal.show(ModalCategory,
          { item: item, userMenuAuth: this.userMenuAuth },
          { width: '65%', height: 'auto', scrollable: true }
        )
      } else if (part === 'mallmatched') {
        // MallProductList 컴포넌트에서 자사몰 category open
        this.$modal.show(ModalCategory,
          { item: item, userMenuAuth: this.userMenuAuth, whereOpen: 'MallProductList' },
          { width: '50%', height: 'auto', scrollable: true }
        )
      } else { // 'store'
        this.$modal.show(ModalStoreCategory,
          { item: item, readonly: readonly, userMenuAuth: this.userMenuAuth },
          { width: '50%', height: 'auto', scrollable: true }
        )
      }
    },
    changeTab (tabName) {
      this.showTabName = tabName
    }
  },
  props: {
    'userMenuAuth': Object
  }
}

</script>
<style scoped>

input[type="number"] {
    border: none;
    padding: 0 10px;
    min-height: 36px;
    text-align: right;
    border: 1px solid #e8e8e8;
    line-height: 34px;
    width: 103px;
}

.toggleWrap .slider:before {
    position: absolute;
    content: "";
    height: 18px;
    width: 18px;
    left: 2px;
    bottom: 2px;
    background-color: #fff;
    -webkit-transition: .3s;
    transition: .3s;
    border-radius: 50%;
}

.toggleWrap .switch {
    position: relative;
    display: inline-block;
    width: 42px;
    height: 22px;
}

</style>
