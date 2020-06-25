<template>
  <div>
    <article class="searchResult" >
      <div class="articleWrap">
        <div class="tableTopWrap">
          <p class="total">결과 : 총 <span> {{responseData.total_cnt}} </span> 건</p>
          <template v-if="responseData.total_cnt > 0">
            <div style="width: 70%;display: inline-block;">
              <Pagination
                :paginate="responseData.paginate"
                :action="get"
                :isPropPageCntShow="true"
                :propPageCnt="searchParam.per_page"
                :propStyle="'clear: none;padding: 0px;padding-left:150px;'"
              ></Pagination>
            </div>
          </template>
          <div class="btnWrap">
            <ul>
              <li>
                <select v-model="orderby" style="width:170px" @change="get(1, searchParam.per_page)">
                  <option :value=0>스마트스토어 등록일 역순</option>
                  <option :value=1>스마트스토어 등록일</option>
                  <option :value=2>스마트스토어 수정일 역순</option>
                  <option :value=3>스마트스토어 수정일</option>
                </select>
              </li>
              <li >
                <p class="btn_add" @click="reqConfirm()" style="width:150px;">
                  수기상품 동기화하기
                </p>
              </li>
            </ul>
          </div>
        </div>
        <div class="tableWrap tableList tableGroup">
          <table summary="스마트스토어 상품관리">
            <caption class="blind">스마트스토어 상품관리</caption>
            <colgroup>
                <col style="width:8%"/>
                <col style="width:3%"/>
                <col style="width:10%"/>
                <col style="width:5%"/>
                <col style="width:5%"/>
                <col style="width:5%"/>
                <col style="width:6%"/>
                <col style="width:5%"/>
                <col style="width:5%"/>
                <col style="width:5%"/>
                <col style="width:10%"/>
                <col style="width:5%"/>
            </colgroup>
            <thead>
            <tr>
              <th>이미지</th>
              <th>쇼핑몰</th>
              <th>상품명</th>
              <th>카테고리</th>
              <th>판매가</th>
              <th>할인가</th>
              <th>상품번호</th>
              <th>옵션</th>
              <th>판매상태<br>(재고상태)</th>
              <th>전시상태</th>
              <th>등록일(수정일)</th>
              <th>매칭</th>
            </tr>
            </thead>
            <tbody>
              <template v-if="responseData.total_cnt">
              <template v-for="(item, idx) in responseData.data.list">
                <!-- @mouseover="hover = true" @mouseleave="hover = false"  -->
              <tr
                :key="idx" :style="[item.product_link_yn === 1 ? { 'background': '#e4f0ff'} : {}
                                    , item.changed ? { 'background': '#fff0f0'} : {}
                                    , item.del_yn === '1' ? { 'background': '#f1f1f1'} : {}]">
                <!-- 이미지 -->
                <td :rowspan="[item.product_link_yn === 1 ? '2' : '1']" class="textCenter">
                  <a @click="$parent.showImg(item.file_image_detail_text)"
                    v-if="item.file_image_detail_text">
                    <img v-if="item.file_image_detail_text" v-lazy="item.file_image_detail_text"
                      style="width: 50px;"/>
                  </a>
                  <p v-else>
                    -
                  </p>
                </td>
                <!-- 쇼핑몰명 -->
                <td class="textCenter tdLine">
                  <img src="http://static.lunasoft.co.kr/lunaadmin/images/naver_logo.png" alt="스마트스토어">
                </td>
                <!-- 상품명 -->
                <td class="textCenter">
                  {{item.product_name }}
                </td>
                <!-- 카테고리 -->
                <td class="textCenter">
                  <button type="button" class="btn_min" :id="idx + '_btn_store_category'" style="width: 90%;"
                  :style="[item.smartstore_del_yn === '1'? { 'pointer-events': 'none'} : {}]"
                  @click="$parent.showCategory(item, 'store', true)">{{item.store_category_name}}</button>
                </td>
                <!-- 판매가 -->
                <td class="textCenter">
                    {{item.product_price | currency}}
                </td>
                <!-- 할인가 -->
                <td class="textCenter">
                  {{item.product_pc_discount_price | currency}}
                </td>
                <!-- 상품번호 -->
                <td class="textCenter">
                  <a :href="item.product_store_url_addr +item.smartstore_product_no" target="_blank"><strong>{{item.smartstore_product_no}}</strong></a>
                  <br>
                  {{item.smartstore_product_custom_code}}
                </td>
                <!-- 옵션 -->
                <td class="textCenter">
                  <button type="button" class="btn_min" :id="idx + '_btn_option'" @click="$parent.showOption(item)">보기</button>
                </td>
                <!-- 판매상태 -->
                <td class="textCenter">
                  <template v-if="item.sell_yn === '1'">
                    판매중
                  </template>
                  <template v-else>
                    미판매
                  </template>
                  <template v-if="item.soldout_yn === '1'">
                    <br>(재고없음)
                  </template>
                </td>
                <!-- 전시상태 -->
                <td class="textCenter">
                  <template v-if="item.display_yn === '1'">
                    전시중
                  </template>
                  <template v-else>
                    미전시
                  </template>
                </td>
                <!-- 등록일(수정일) -->
                <td class="textCenter">
                    {{item.product_register_date}} <br>
                    ({{item.product_modify_date}})
                </td>
                <td class="textCenter">
                  <button type="button" class="btn_min" :id="idx + '_btn_product'" @click="showProductModal(item)">확인</button>
                </td>
              </tr>

              </template>
              </template>
              <template v-else>
                <tr >
                  <td colspan="12" class="textCenter" >
                    조회된 내용이 없습니다.
                  </td>
                </tr>
              </template>

            </tbody>
          </table>
        </div>
        <Pagination
          :paginate="responseData.paginate"
          :action="get"
          :propPageCnt="searchParam.per_page"
          :isPropPageCntShow="true"
        ></Pagination>
      </div>
    </article>
    <v-dialog :clickToClose=false />
    <Spinner v-if="isLoading" />
  </div>

</template>

<script>

import Pagination from '@/components/common/Pagination'
import ModalAlert from '@/components/common/modal/ModalAlert'
import { eventBus } from '@/main'
import ModalProductInfo from './ModalProductInfo'
import http from '@/http-commons'

export default {
  name: 'UnMatchedProductList',
  components: {
    Pagination
  },
  data () {
    return {
      isLoading: false,
      userNo: this.$store.getters.getUser.luna_no,
      responseData: {
        total_cnt: 0,
        data: {
          list: []
        }
      },
      orderby: 0
    }
  },
  mounted () {
    // 이벤트 받기
    eventBus.$on('unMatchedProductList', (currentPage) => {
      // 검색버튼 및 탭클릭으로 호출 됨
      this.get(currentPage, this.searchParam.per_page)
    })

    eventBus.$on('modalProductInfoUpdate', () => {
      // ModalProductInfo 에서 상품 업데이트시 팝업 닫히면서 호출
      this.get(this.searchParam.current_page, this.searchParam.per_page)
    })
  },
  beforeDestroy () {
    eventBus.$off('unMatchedProductList')
    eventBus.$off('modalProductInfoUpdate')
  },
  methods: {
    get (currentPage, perPage) {
      if (this.userMenuAuth.retrieve_list) {
        if (this.$store.state.searchParam === undefined || this.$store.state.searchParam.mall_no === 0) {
          this.$modal.show(ModalAlert,
            {title: '', text: '쇼핑몰을 선택해주세요.'},
            { width: 450, height: 'auto' }
          )
        }

        let smartProductPriceStart = this.searchParam.smart_product_price_check ? this.searchParam.smart_product_price_start : ''
        let smartProductPriceEnd = this.searchParam.smart_product_price_check ? this.searchParam.smart_product_price_end : ''
        if (this.searchParam.smart_product_price_check) {
          if (smartProductPriceStart === '' || smartProductPriceEnd === '') {
            this.$modal.show(ModalAlert,
              {title: '', text: '스마트스토어 판매가를 입력해주세요.'},
              { width: 450, height: 'auto' }
            )
            return
          }
        }
        let smartProductPcDiscountPriceStart = this.searchParam.smart_product_pc_discount_price_check ? this.searchParam.smart_product_pc_discount_price_start : ''
        let smartProductPcDiscountPriceEnd = this.searchParam.smart_product_pc_discount_price_check ? this.searchParam.smart_product_pc_discount_price_end : ''
        if (this.searchParam.smart_product_pc_discount_price_check) {
          if (smartProductPcDiscountPriceStart === '' || smartProductPcDiscountPriceEnd === '') {
            this.$modal.show(ModalAlert,
              {title: '', text: '스마트스토어 할인가를 입력해주세요.'},
              { width: 450, height: 'auto' }
            )
            return
          }
        }

        let smartProductCategoryNo = ''
        let smartCategoryDepthNo = 0
        /** 스마트스토어 카테고리 선택시 생김 */
        if (this.searchParam.categoryItem) {
          // if (!this.validaionCheck(this.searchParam.categoryItem)) return
          let item = this.searchParam.categoryItem
          if (parseInt(item.depth4_category_no) !== 0) {
            smartProductCategoryNo = item.depth4_category_no
            smartCategoryDepthNo = 4
          } else if (parseInt(item.depth3_category_no) !== 0) {
            smartProductCategoryNo = item.depth3_category_no
            smartCategoryDepthNo = 3
          } else if (parseInt(item.depth2_category_no) !== 0) {
            smartProductCategoryNo = item.depth2_category_no
            smartCategoryDepthNo = 2
          } else if (parseInt(item.depth1_category_no) !== 0) {
            smartProductCategoryNo = item.depth1_category_no
            smartCategoryDepthNo = 1
          }
        }

        this.isLoading = true
        let url = '/smartstore/productmatch/unmatched/list'
        http
          .get(url, {
            params: { // query string
              mall_id: this.$store.state.searchParam.mall_id,
              mall_no: this.$store.state.searchParam.mall_no,
              cal_code: this.$store.state.searchParam.cal_code,
              per_page: perPage,
              current_page: currentPage,
              orderby: this.orderby,
              searhDateFlag: this.searchParam.searhDateFlag,
              // eslint-disable-next-line no-undef
              fromDate: this.searchParam.fromDate === '' ? null : dateFormatToString(this.searchParam.fromDate),
              // eslint-disable-next-line no-undef
              toDate: this.searchParam.toDate === '' ? dateFormatToString(new Date()) : dateFormatToString(this.searchParam.toDate),
              smartstore_product_no: this.searchParam.smartstore_product_no,
              smartstore_product_name: encodeURIComponent(this.searchParam.smartstore_product_name.trim()),
              smartstore_display_yn: this.searchParam.smartstore_display_sellArr.includes(3) && this.searchParam.smartstore_display_sellArr.includes(4) === true ? null
                : this.searchParam.smartstore_display_sellArr.includes(3) === true ? 1
                  : this.searchParam.smartstore_display_sellArr.includes(4) === true ? 0 : null,
              smartstore_sell_yn: this.searchParam.smartstore_display_sellArr.includes(1) && this.searchParam.smartstore_display_sellArr.includes(2) === true ? null
                : this.searchParam.smartstore_display_sellArr.includes(1) === true ? 1
                  : this.searchParam.smartstore_display_sellArr.includes(2) === true ? 0 : null,
              smart_product_price_start: smartProductPriceStart,
              smart_product_price_end: smartProductPriceEnd,
              smart_product_pc_discount_price_start: smartProductPcDiscountPriceStart,
              smart_product_pc_discount_price_end: smartProductPcDiscountPriceEnd,
              store_product_category_no: smartProductCategoryNo,
              store_category_depth_no: smartCategoryDepthNo
            }
          })
          .then(response => {
            this.isLoading = false
            this.responseData = response.data
            // eslint-disable-next-line no-undef
            this.responseData.paginate = makePaginate(response.data); this.responseData.max = pageMaxNumber(response.data)

            this.searchParam.current_page = this.responseData.current_page
            this.searchParam.per_page = this.responseData.per_page

            eventBus.$emit('dashboard', this.$store.state.searchParam)
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
      }
    },
    showProductModal (item) {
      console.log('makeproduct')
      this.isLoading = true
      let url = '/smartstore/productmatch/unmatched/productinfo'
      http
        .get(url, {
          params: { // query string
            mall_no: this.$store.state.searchParam.mall_no,
            mall_id: this.$store.state.searchParam.mall_id,
            product_name: encodeURIComponent(item.product_name.trim()),
            product_price: item.product_price,
            product_pc_discount_price: item.product_pc_discount_price
          }
        })
        .then(response => {
          this.isLoading = false
          let list = response.data.data
          this.$modal.show(ModalProductInfo,
            { item: item, list: list, userMenuAuth: this.userMenuAuth },
            { width: '50%', height: 'auto', scrollable: true }
          )
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
    /**
     * [수기상품 동기화하기] 클릭시 컨펌창
     */
    reqConfirm () {
      if (!this.userMenuAuth.create_data) {
        return
      }
      this.$modal.show('dialog', {
        title: '수기상품 동기화하기',
        text: '스마트스토어 상품을 동기화 하시겠습니까?',
        buttons: [{
          title: '취소',
          handler: () => {
            this.$modal.hide('dialog')
          }
        },
        {
          title: '확인', // Button title
          default: true, // ENTER KEY
          handler: () => {
            this.productListReq()
            this.$modal.hide('dialog')
          }
        }]
      })
    },
    /**
     * [수기상품 동기화하기] 클릭시 컨펌 수락시
     * 스마트 스토어의 데이터를 일괄 업데이트
     */
    productListReq () {
      if (this.userMenuAuth.retrieve_list) {
        if (this.$store.state.searchParam === undefined || this.$store.state.searchParam.mall_no === 0) {
          this.$modal.show(ModalAlert,
            {title: '', text: '쇼핑몰을 선택해주세요.'},
            { width: 450, height: 'auto' }
          )
          return
        }

        // eslint-disable-next-line no-undef
        let apiUpdateDate = dateTimeFormatToString(new Date())
        let params = {
          user_code: 1,
          user_no: this.userNo,
          api_update_date: apiUpdateDate
        }
        // eslint-disable-next-line no-undef
        let url = smartStroreApiUrl + '/smartstore/update/unmached/product/' + this.$store.state.searchParam.mall_id
        http
          .post(url, JSON.stringify(params))
          .then(response => {
            console.log(response)
          })
          .catch(e => {
            /** 오류 발생시 ProductDashboard 에서 돌고 있는 interval 클리어 해야함 */
            console.log(e)
            if (e.code === 'ECONNABORTED') {
              // timeout
            } else {
              this.$modal.show(ModalAlert,
                { title: '', text: '스마트스토어 동기화하기 오류' }, { width: 450, height: 'auto' }
              )
            }
          })
          .finally(() => {
          })
        this.$modal.show(ModalAlert,
          { title: '', html: '스마트스토어 수기등록상품을 동기화하고 있습니다.<br>잠시뒤에 조회 바랍니다.' }, { width: 450, height: 'auto' }
        )
      }
    }
  },
  props: {
    'userMenuAuth': Object,
    'searchParam': Object
  }
}

</script>
<style scoped>

</style>
