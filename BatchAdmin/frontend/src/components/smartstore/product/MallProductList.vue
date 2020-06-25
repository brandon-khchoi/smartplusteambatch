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
                <a><i @click="showFilterModal()" class="bootstrapWrapper tooltip-toggle fas fa-filter"></i></a>
              </li>
              <li>
                <select v-model="orderby" style="width:150px" @change="get(1, searchParam.per_page)">
                  <option :value=0>자사몰 등록일 역순</option>
                  <option :value=1>자사몰 등록일</option>
                  <option :value=2>자사몰 수정일 역순</option>
                  <option :value=3>자사몰 수정일</option>
                  <option :value=4>스마트스토어 등록일</option>
                  <option :value=5>스마트스토어 등록일 역순</option>
                  <option :value=6>스마트스토어 수정일</option>
                  <option :value=7>스마트스토어 수정일 역순</option>
                </select>
              </li>
              <li v-if="isReqBtnActive">
                <p class="btn_add" @click="reqConfirm">
                  상품정보 수정하기
                </p>
              </li>
            </ul>
          </div>
        </div>
        <div class="tableWrap tableList tableGroup">
          <table summary="스마트스토어 상품관리">
            <caption class="blind">스마트스토어 상품관리</caption>
            <colgroup>
                <!-- <col style="width:3%"/> -->
                <col style="width:5%"/>
                <col style="width:3%"/>
                <col style="width:10%"/>
                <col style="width:7%"/>
                <col style="width:5%"/>
                <col style="width:5%"/>
                <col style="width:7%"/>
                <col style="width:5%"/>
                <col style="width:5%"/>
                <col style="width:5%"/>
                <col style="width:8%"/>
                <!-- <col style="width:4%"/> -->
                <col style="width:5%"/>
                <col style="width:3%"/>
                <col style="width:3%"/>
                <col style="width:3%"/>
                <col style="width:3%"/>
                <col style="width:3%"/>
                <col style="width:3%"/>
                <!-- <col style="width:4%"/> -->
            </colgroup>
            <thead>
            <tr>
              <!-- <th rowspan="2">
                <span class="inputWrap checkboxCenter">
                  <input type="checkbox" id="service01-02" @change="checkAll" v-model="allSelected" >
                  <label for="service01-02"></label>
                </span>
              </th> -->
              <th rowspan="2">이미지</th>
              <th rowspan="2">쇼핑몰</th>
              <th rowspan="2">상품명</th>
              <th rowspan="2">카테고리</th>
              <th rowspan="2">판매가</th>
              <th rowspan="2">할인가</th>
              <th rowspan="2">상품번호
                <!-- <br>(스마트스토어 상품번호)<br>(스마트스토어  상품코드) -->
              </th>
              <th rowspan="2">옵션</th>
              <th rowspan="2">판매상태<br>(재고상태)</th>
              <th rowspan="2">전시상태</th>
              <th rowspan="2">등록일(수정일)</th>
              <!-- <th rowspan="2">해시태그</th> -->
              <th rowspan="2">사이즈 이미지</th>
              <th rowspan="2">상세</th>
              <th colspan="5">스마트스토어 동기화</th>
            </tr>
            <tr>

              <th >
                가격<br>
                <p class="toggleWrap">
                  <span class="inputWrap checkboxCenter">
                    <input type="checkbox" id="mall_product_price_link_yn"
                      @click="productRequestConfirm('가격', 'product_price_link_yn')"
                      :true-value=1 :false-value=0
                      v-model="store_info.product_price_link_yn"
                      >
                    <label for="mall_product_price_link_yn"></label>
                  </span>
                </p>
              </th>
              <th >
                판매상태<br>
                <p class="toggleWrap">
                  <span class="inputWrap checkboxCenter">
                    <input type="checkbox" id="mall_product_sell_link_yn"
                      @click="productRequestConfirm('판매상태', 'product_sell_link_yn')"
                      :true-value=1 :false-value=0
                      v-model="store_info.product_sell_link_yn"
                      >
                    <label for="mall_product_sell_link_yn"></label>
                  </span>
                </p>
              </th>
              <th >
                옵션<br>
                <p class="toggleWrap">
                  <span class="inputWrap checkboxCenter">
                    <input type="checkbox" id="mall_product_option_link_yn"
                      @click="productRequestConfirm('옵션', 'product_option_link_yn')"
                      :true-value=1 :false-value=0
                      v-model="store_info.product_option_link_yn"
                      >
                    <label for="mall_product_option_link_yn"></label>
                  </span>
                </p>
              </th>
              <th >
                상품이름<br>
                <p class="toggleWrap">
                  <span class="inputWrap checkboxCenter">
                    <input type="checkbox" id="mall_product_name_link_yn"
                      @click="productRequestConfirm('상품이름', 'product_name_link_yn')"
                      :true-value=1 :false-value=0
                      v-model="store_info.product_name_link_yn"
                      >
                    <label for="mall_product_name_link_yn"></label>
                  </span>
                </p>
              </th>
              <th >
                상품상세<br>
                <p class="toggleWrap">
                  <span class="inputWrap checkboxCenter">
                    <input type="checkbox" id="mall_product_detail_link_yn"
                      @click="productRequestConfirm('상품상세', 'product_detail_link_yn')"
                      :true-value=1 :false-value=0
                      v-model="store_info.product_detail_link_yn"
                      >
                    <label for="mall_product_detail_link_yn"></label>
                  </span>
                </p>
              </th>
            </tr>
            </thead>
            <tbody>
              <template v-if="responseData.total_cnt">
              <template v-for="(item, idx) in responseData.data.list">
                <!-- @mouseover="hover = true" @mouseleave="hover = false"  -->
                <!-- {{item.filtered}} -->
              <tr v-if="item.filtered === 1"
                :key="idx" :style="[item.product_luna_link_yn === 1 ? { 'background': '#e4f0ff'} : {},
                                    item.smartstore_del_yn === '1' ? { 'background': '#f1f1f1'} : {},
                                    item.changed ? { 'background': '#fff0f0'} : {}
                                    ]">
                <!-- 체크박스 -->
                <!-- <td rowspan="2" class="textCenter tdLine">
                  <span class="inputWrap checkboxCenter">
                    <input type="checkbox" :id="'service01-02-' + idx " :value="item.product_no"
                      v-model="item.selected" @click="select(idx)">
                    <label :for="'service01-02-' + idx "></label>
                  </span>
                </td> -->
                <!-- 이미지 -->
                <!-- <div :key="idx + 'way'" v-if="idx == 5" v-waypoint="{ active: true, callback: onWaypoint}" ></div> -->
                <td rowspan="2" class="textCenter tdLine">
                  <div :key="idx + 'way'" v-if="idx == 5" v-waypoint="{ active: true, callback: onWaypoint}" ></div>
                    <a @click="$parent.showImg(item.file_image_detail_text)"
                      v-if="item.file_image_detail_text">
                      <img v-if="item.file_image_detail_text" v-lazy="item.file_image_detail_text"
                        style="width: 50px;"/>
                    </a>
                    <p v-else>
                      -
                    </p>
                </td>
                <!-- 쇼핑몰 로고 -->
                <td class="textCenter">
                  <template v-if="$store.state.searchParam.hosting_no === 1">
                    <img src="https://lunaplus.lunasoft.co.kr/images/lunachat/icon/cafe24/logo-button.png" alt="카페24"/>
                  </template>
                  <template v-if="$store.state.searchParam.hosting_no === 2">
                    <img src="https://lunaplus.lunasoft.co.kr/images/lunachat/icon/makeshop/logo-button.png" alt="메이크샵"/>
                  </template>
                </td>
                <!-- 상품명 -->
                <td class="textCenter" v-html="item.product_name">
                </td>
                <td class="textCenter">
                 <button type="button" class="btn_min" :id="idx + '_btn_category'" style="width: 90%;"
                  @click="$parent.showCategory(item, 'mallmatched')">카테고리</button>
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
                  <a :href="item.product_mall_url_addr +item.product_no" target="_blank"><strong>{{item.product_no}}</strong></a>
                  <br>
                </td>
                <!-- 옵션 -->
                <td class="textCenter">
                  <button type="button" class="btn_min" :id="idx + '_btn_option'" @click="$parent.showOption(item)">보기</button>
                </td>
                <!-- 판매상태 -->
                <td class="textCenter">
                  <template v-if="item.display_yn === '1' && item.sell_yn === '1' && item.soldout_yn === '0'">
                    판매중
                  </template>
                  <template v-else>
                    판매중지
                  </template>
                  <template v-if="item.soldout_yn === '1'">
                    <br>(재고없음)
                  </template>
                  <template v-else>
                  </template>
                </td>
                <!-- 전시상태 -->
                <td class="textCenter">
                </td>
                <!-- 등록일(수정일) -->
                <td class="textCenter">
                    {{item.product_register_date}} <br>
                    <template v-if="item.product_modify_date">
                      ({{item.product_modify_date}})
                    </template>
                    <template v-else>
                      (수정이력 없음)
                    </template>

                </td>
                <!-- 태그 -->
                <!-- <td rowspan="2" class="textCenter tdLine">
                    <template v-if="item.product_tag_text">
                      <button type="button" class="btn_min" :id="idx + '_btn'" @click="showHashTag(item)">보기</button>
                    </template>
                    <template v-else>
                      없음
                    </template>
                </td>-->
                <!-- 사이즈 이미지 -->
                <td rowspan="2" class="textCenter tdLine">
                    <template v-if="item.file_product_size_image_url_addr">
                      <button type="button" class="btn_min" :id="idx + '_btn'" @click="$parent.showImg(item.file_product_size_image_url_addr)">보기</button>
                      <!-- <button type="button" class="btn_min" :id="idx + '_btn'" @click="showHtml(item.product_pc_detail_explain_text)">보기</button> -->
                    </template>
                    <template v-else>
                      <template v-if="item.file_product_size_image_fail_reason">
                        <button type="button" class="btn_min" :id="idx + '_btn'"
                          style="border: 1px solid #e72b4b;color: #e72b4b;"
                          @click="$parent.showHtml(item.file_product_size_image_fail_reason)">실패사유</button>
                      </template>
                      <template v-else>
                        없음
                      </template>

                    </template>
                </td>
                <td rowspan="2" class="textCenter tdLine">
                    <i class="bootstrapWrapper tooltip-toggle fas fa-ellipsis-h" @mouseover="$bvToast.show('hideToast_'+idx)" style="padding:8px;">
                        <b-toast :id="'hideToast_'+idx" static no-auto-hide>
                          <div>
                            <b-link><a @click="selectProductLog(item)">작업이력</a></b-link>
                          </div>
                          <template v-if="item.product_tag_text">
                            <div>
                              <b-link><a @click="showHashTag(item)">해시태그</a></b-link>
                            </div>
                          </template>
                          <template v-if="parseInt(item.smartstore_del_yn) === 1">
                            <div>
                              <b-link><a @click="updateDeletedProductStatus(item)"> 연동 상태 변경하기</a></b-link>
                            </div>
                          </template>
                        </b-toast>
                    </i>
                </td>
                <!-- 스마트스토어 자동연동 -->
                <!-- 스마트스토어 자동연동 가격 -->
                <td rowspan="2" class="textCenter tdLine">
                  <span class="inputWrap checkboxCenter">
                    <input type="checkbox" :id="'mall-price-' + idx "
                      :true-value=1 :false-value=0
                      :disabled="item.smartstore_del_yn === '1'"
                      :value="item.product_price_link_yn" v-model="item.product_price_link_yn" >
                    <label :for="'mall-price-' + idx "></label>
                  </span>
                </td>
                <td rowspan="2" class="textCenter tdLine">
                  <span class="inputWrap checkboxCenter">
                    <input type="checkbox" :id="'mall-sell-' + idx "
                      :true-value=1 :false-value=0
                      :disabled="item.smartstore_del_yn === '1'"
                      :value="item.product_sell_link_yn" v-model="item.product_sell_link_yn" >
                    <label :for="'mall-sell-' + idx "></label>
                  </span>
                </td>
                 <!-- 스마트스토어 자동연동 옵션 -->
                <td rowspan="2" class="textCenter tdLine">
                  <span class="inputWrap checkboxCenter">
                    <input type="checkbox" :id="'mall-option-' + idx "
                      :true-value=1 :false-value=0
                      :disabled="item.smartstore_del_yn === '1'"
                      :value="item.product_option_link_yn" v-model="item.product_option_link_yn" >
                    <label :for="'mall-option-' + idx "></label>
                  </span>
                </td>
                 <!-- 스마트스토어 자동연동 상품이름 -->
                <td rowspan="2" class="textCenter tdLine">
                  <span class="inputWrap checkboxCenter">
                    <input type="checkbox" :id="'mall-name-' + idx "
                      :true-value=1 :false-value=0
                      :disabled="item.smartstore_del_yn === '1'"
                      :value="item.product_name_link_yn" v-model="item.product_name_link_yn" >
                    <label :for="'mall-name-' + idx "></label>
                  </span>
                </td>
                 <!-- 스마트스토어 자동연동 상품상세 -->
                <td rowspan="2" class="textCenter tdLine">
                  <span class="inputWrap checkboxCenter">
                    <input type="checkbox" :id="'mall-detail-' + idx "
                      :true-value=1 :false-value=0
                      :disabled="item.smartstore_del_yn === '1'"
                      :value="item.product_detail_link_yn" v-model="item.product_detail_link_yn" >
                    <label :for="'mall-detail-' + idx "></label>
                  </span>
                </td>
              </tr>
              <!-- 스마트스토어 둘째줄 -->
              <tr v-if="item.filtered === 1"
                :key="idx + '_2'"
                :style="[item.product_luna_link_yn === 1 ? { 'background': '#e4f0ff'} : {},
                          item.smartstore_del_yn === '1'? { 'background': '#f1f1f1'} : {},
                          item.changed ? { 'background': '#fff0f0'} : {}
                          ]">
                <td class="textCenter tdLine">
                  <img src="http://static.lunasoft.co.kr/lunaadmin/images/naver_logo.png" alt="스마트스토어">
                </td>
                <td class="textCenter tdLine" >
                  <input type="text" v-model="item.smartstore_product_name" style="width: 100%;font-size: 11px;"
                    :disabled="item.smartstore_del_yn === '1'"
                    @input="typingProductName(item, idx)" />
                </td>
                <td class="textCenter tdLine">
                  <button type="button" class="btn_min" :id="idx + '_btn_store_category'" style="width: 90%;"
                  :style="[item.smartstore_del_yn === '1'? { 'pointer-events': 'none'} : {}]"
                  @click="$parent.showCategory(item, 'store')">{{item.store_category_name}}</button>
                </td>
                <td class="textCenter tdLine">
                  <input type="number" v-model="item.smart_product_price"
                   :disabled="item.smartstore_del_yn === '1'"
                    style="width: 100%; display:none;" />
                    <input type="text" :value="item.smart_product_price | currency"
                     :disabled="item.smartstore_del_yn === '1'"
                     @input="$parent.addComma(item, 'smart_product_price')"
                     @blur="typingPrice(item, idx, 'smart_product_price', 'product_price')"
                    style="width: 100%;"  />
                </td>
                <td class="textCenter tdLine">
                  <input type="number" v-model="item.smart_product_pc_discount_price"
                  :disabled="item.smartstore_del_yn === '1'"
                  style="width: 100%; display:none;" />
                  <input type="text" :value="item.smart_product_pc_discount_price | currency"
                    :disabled="item.smartstore_del_yn === '1'"
                    @input="$parent.addComma(item, 'smart_product_pc_discount_price')"
                    @blur="typingPrice(item, idx,  'smart_product_pc_discount_price', 'product_pc_discount_price')"
                  style="width: 100%;" />
                </td>
                <td class="textCenter tdLine">
                  <template v-if="item.smartstore_product_no">
                    <a :href="item.product_store_url_addr +item.smartstore_product_no" target="_blank"><strong>{{item.smartstore_product_no}}</strong></a>
                    <br>
                  </template>
                  <template v-if="item.smartstore_product_custom_code">
                      ({{item.smartstore_product_custom_code}})<br>
                  </template>
                </td>

                <!-- 스마트스토어 둘째줄 옵션 -->
                <td class="textCenter tdLine">
                  <!-- <button type="button" class="btn_min" :id="idx + '_btn_option'" @click="showOption(item)">보기</button> -->
                  <button type="button" class="btn_min" :id="idx + '_btn_option'"
                    :style="[item.smartstore_del_yn === '1'? { 'pointer-events': 'none'} : {}]"
                    @click="showEditOption(item)">보기</button>
                </td>

                <td class="textCenter tdLine">
                    <select v-model="item.smartstore_sell_yn" style="width:100%" @change="changeSelect(item, idx)"
                      :disabled="item.smartstore_del_yn === '1'" >
                      <option :value="'1'">판매중</option>
                      <option :value="'0'">미판매</option>
                    </select>
                </td>
                <td class="textCenter tdLine">
                  <template v-if="item.smartstore_display_yn === '1'">
                    전시중
                  </template>
                  <template v-else>
                    미전시
                  </template>
                </td>
                <td class="textCenter tdLine">
                  {{item.smartstore_product_register_date}} <br>
                    <template v-if="item.smartstore_product_modify_date">
                      ({{item.smartstore_product_modify_date}})
                    </template>
                    <template v-else>
                      (수정이력 없음)
                    </template>
                </td>
              </tr>
              </template>
              </template>
              <template v-else>
                <tr >
                  <td colspan="18" class="textCenter" >
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
    <div class="bootstrapWrapper bootstrapBtnFixed" v-show="isReqBtnActive">
      <button type="button" class="btn btn-primary btn-lg" @click="reqConfirm()"
        style="background-color: #37a8fe;border-color: #37a8fe;"
        >상품정보 수정하기</button>
    </div>
  </div>

</template>

<script>
/** [스마트스토어 연동상품] 리스트 두번째탭 */
import Pagination from '@/components/common/Pagination'
import ModalAlert from '@/components/common/modal/ModalAlert'

/* 용석 결과내 재검색 관련 modal 추가 */
import MallProductListFilter from './MallProductListFilter'
/* 용석 결과내 재검색 관련 modal 추가 끝 */

import ModalProductLog from './ModalProductLog'
import ModalHtmlConfirm from '@/components/common/modal/ModalHtmlConfirm'
import ModalOptionEditList from './option/ModalOptionEditList'
import ModalHashTag from './ModalHashTag'
import http from '@/http-commons'
import { eventBus } from '@/main'

export default {
  name: 'MallProductList',
  components: {
    Pagination
  },
  data () {
    return {
      isLoading: false,
      userNo: this.$store.getters.getUser.luna_no,
      // allSelected: false,
      isFilterFormShow: false, // 검색필터 열기 닫기
      isReqBtnActive: true, // 체크 상품연동 활성화여부
      isScrollBtnShow: false,
      store_info: {
        product_option_link_yn: 0,
        product_name_link_yn: 0,
        product_price_link_yn: 0,
        product_sell_link_yn: 0,
        product_detail_link_yn: 0
      },
      orderby: 0,
      responseData: {
        total_cnt: 0,
        data: {
          list: []
        }
      },
      initResponseData: {
        total_cnt: 0,
        data: {
          list: []
        }
      },
      flagObject: {
        allCheckYn: true, // 전체 선택
        filterInitYn: true, // 필터링 초기화 여부
        connectFlag: {
          product_luna_link_yn: true, // 자사몰 연동 상품
          product_unmatched_link_yn: true,
          // product_luna_link_yn: false, // 수기등록 매칭 상품
          smartstore_del_yn: true // 삭제된 연동 상품
        },
        syncFlag: {
          product_price_link_yn: true, // 가격 동기화 상품
          product_sell_link_yn: true, // 판매상태 동기화 상품
          product_option_link_yn: true, // 옵션(재고) 동기화 상품
          product_name_link_yn: true, // 상품명 동기화 상품
          product_detail_link_yn: true // 상품상세 동기화 상품
        }
      }
    }
  },
  mounted () {
    // 이벤트 받는 부분
    eventBus.$on('mallProductList', (currentPage) => {
      // 검색버튼 및 탭클릭으로 호출 됨
      this.get(currentPage, this.searchParam.per_page)
    })
    /** 스마트 스토어 상품 옵션 수정 시 재 조회 메소드 호출 이벤트 버스 */
    eventBus.$on('modalOptionEditList', () => {
      this.get(this.searchParam.current_page, this.searchParam.per_page)
    })

    /** 스마트 스토어 카테고리 변경시 재조회 */
    eventBus.$on('modalCategory', () => {
      this.get(this.searchParam.current_page, this.searchParam.per_page)
    })

    /** 두번째 탭의 프로그레스바 완료시 호출 */
    eventBus.$on('mallProductListProgress', () => {
      this.isReqBtnActive = true
      this.get(this.searchParam.current_page, this.searchParam.per_page)
    })
    /** ModalProductListFilter에서 호출 하는 evenetBus */
    eventBus.$on('productFiltering', (flagObject) => {
      this.getFilteredProductList(flagObject)
    })

    eventBus.$on('success', (currentPage) => {
      // 검색버튼 및 탭클릭으로 호출 됨
      this.get(currentPage, this.searchParam.per_page)
    })
  },
  beforeDestroy () {
    eventBus.$off('mallProductList')
    eventBus.$off('modalOptionEditList')
    eventBus.$off('modalCategory')
    eventBus.$off('mallProductListProgress')
    eventBus.$off('success')
    eventBus.$off('productFiltering')
  },
  watch: {
    'responseData.data.list': {
      deep: true,
      handler (newVal, oldVal) {
        console.log(newVal === oldVal)

        if (oldVal !== undefined && oldVal.length > 0) { // 페이지 로딩시 무조건 값 세팅되는것 방지
          // isChangedData 를 false 로 변경시 isResetData를 true 로 변경해야 handler 사용 가능
          let initDataList = this.initResponseData.data.list
          let dataList = this.responseData.data.list
          // let oldValInner = oldVal
          newVal.filter(function (p, idx) {
            return Object.keys(p).some(function (prop) {
              // if (prop === 'filtered') return true // filtered 변경체크에서 제외
              if (prop === 'selected') return true

              if (prop === 'changed') { // 변경되었던 데이터만 changed 라는 객체로 잇어서 changed 가 있는 데이터들만 삭제
                delete dataList[idx]['changed']
                delete dataList[idx]['selected']
                // selected.splice(idx, 1)
              } else {
                if (p[prop] !== initDataList[idx][prop]) { /** 여기가 데이터 변경되었을 경우의 분기 */
                  /** 상품이름 변경시 체크 끝 */
                  if (prop === 'filtered') return true
                  dataList[idx]['changed'] = true
                  let productNo = dataList[idx]['product_no'].toString()
                  dataList[idx]['selected'] = productNo
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
    onWaypoint ({ going, direction }) {
      // going: in, out
      // direction: top, right, bottom, left
      if (direction === this.$waypointMap.DIRECTION_BOTTOM && going === this.$waypointMap.GOING_OUT) { // 위쪽으로 스크롤 및 위로 포인트 지점 지날때
        this.isScrollBtnShow = false
      }
      if (direction === this.$waypointMap.DIRECTION_TOP) { // 아래쪽으로 스크롤
        this.isScrollBtnShow = true
      }
    },
    get (currentPage, perPage) {
      // this.flagObject = this.resetFlagObject()
      if (this.userMenuAuth.retrieve_list) {
        if (this.$store.state.searchParam === undefined || this.$store.state.searchParam.mall_no === 0) {
          this.$modal.show(ModalAlert,
            {title: '', text: '쇼핑몰을 선택해주세요.'},
            { width: 450, height: 'auto' }
          )
          return
        }

        let productPriceStart = this.searchParam.product_price_check ? this.searchParam.product_price_start : ''
        let productPriceEnd = this.searchParam.product_price_check ? this.searchParam.product_price_end : ''
        if (this.searchParam.product_price_check) {
          if (productPriceStart === '' || productPriceEnd === '') {
            this.$modal.show(ModalAlert,
              {title: '', text: '자사몰 판매가를 입력해주세요.'},
              { width: 450, height: 'auto' }
            )
            return
          }
        }
        /** 가격 검색 */
        let productPcDiscountPriceStart = this.searchParam.product_pc_discount_price_check ? this.searchParam.product_pc_discount_price_start : ''
        let productPcDiscountPriceEnd = this.searchParam.product_pc_discount_price_check ? this.searchParam.product_pc_discount_price_end : ''
        if (this.searchParam.product_pc_discount_price_check) {
          if (productPcDiscountPriceStart === '' || productPcDiscountPriceEnd === '') {
            this.$modal.show(ModalAlert,
              {title: '', text: '자사몰 할인가를 입력해주세요.'},
              { width: 450, height: 'auto' }
            )
            return
          }
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
        console.log('asdfasdfasd')
        this.isLoading = true
        let url = '/smartstore/productmatch/list'
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
              product_no: this.searchParam.product_no,
              smartstore_product_no: this.searchParam.smartstore_product_no,
              product_name: encodeURIComponent(this.searchParam.product_name.trim()),
              smartstore_product_name: encodeURIComponent(this.searchParam.smartstore_product_name.trim()),
              display_yn: this.searchParam.display_yn,
              smartstore_display_yn: this.searchParam.smartstore_display_sellArr.includes(3) && this.searchParam.smartstore_display_sellArr.includes(4) === true ? null
                : this.searchParam.smartstore_display_sellArr.includes(3) === true ? 1
                  : this.searchParam.smartstore_display_sellArr.includes(4) === true ? 0 : null,
              smartstore_sell_yn: this.searchParam.smartstore_display_sellArr.includes(1) && this.searchParam.smartstore_display_sellArr.includes(2) === true ? null
                : this.searchParam.smartstore_display_sellArr.includes(1) === true ? 1
                  : this.searchParam.smartstore_display_sellArr.includes(2) === true ? 0 : null,
              product_price_start: productPriceStart,
              product_price_end: productPriceEnd,
              product_pc_discount_price_start: productPcDiscountPriceStart,
              product_pc_discount_price_end: productPcDiscountPriceEnd,
              smart_product_price_start: smartProductPriceStart,
              smart_product_price_end: smartProductPriceEnd,
              smart_product_pc_discount_price_start: smartProductPcDiscountPriceStart,
              smart_product_pc_discount_price_end: smartProductPcDiscountPriceEnd,
              product_category_no: this.searchParam.product_category_no,
              store_product_category_no: smartProductCategoryNo,
              store_category_depth_no: smartCategoryDepthNo
            }
          })
          .then(response => {
            this.isLoading = false
            this.responseData = response.data
            // eslint-disable-next-line no-undef
            this.responseData.paginate = makePaginate(response.data); this.responseData.max = pageMaxNumber(response.data)
            this.initResponseData = JSON.parse(JSON.stringify(this.responseData)) // 값만 복사

            this.store_info = {
              product_option_link_yn: 0,
              product_name_link_yn: 0,
              product_price_link_yn: 0,
              product_sell_link_yn: 0,
              product_detail_link_yn: 0
            }

            this.searchParam.current_page = this.responseData.current_page
            this.searchParam.per_page = this.responseData.per_page

            // this.allSelected = false // 연동완료 거나 페이지 변경 및 다시 조회시 전체 체크박스 해제
            // this.checkAll()

            eventBus.$emit('dashboard', this.$store.state.searchParam)
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
    },
    /**
     * 자사몰 자동연동 토글시 전체 체크박스 선택 기능
     */
    apiCheckAll (checkItem) {
      // store_info 에 있는 데이터가 1이면 전체 데이터를 1로 화면 노출
      for (const storeItem in this.store_info) { // object 객체 루프
        if (checkItem !== undefined) { // 제일 상단의 해당 item 하나만 토글 체크박스 체크한 경우
          if (checkItem !== storeItem) {
            continue
          }
          if (this.store_info[storeItem] === 1) { // 토글 체크박스 on
            for (const item in this.responseData.data.list) {
              if (this.responseData.data.list[item]['smartstore_del_yn'] !== '1') {
                this.responseData.data.list[item][storeItem] = 1
              }
            }
          } else { // 토글 체크박스 off
            for (const item in this.responseData.data.list) {
              if (this.responseData.data.list[item]['smartstore_del_yn'] !== '1') {
                this.responseData.data.list[item][storeItem] = 0
              }
            }
          }
        }
      }
    },
    /**
     * 연동 토글 버튼 클릭 메소드
     */
    productRequestConfirm (message, data) {
      if (this.$store.state.searchParam === undefined || this.$store.state.searchParam.mall_no === 0) {
        this.$modal.show(ModalAlert,
          {title: '', text: '쇼핑몰을 선택해주세요.'},
          { width: 450, height: 'auto' }
        )

        return
      }
      let textMsg
      if (this.store_info[data] === 0) { // 토글데이터 변경이 바로 되지 않아 거꾸로 조건 사용
        textMsg = '' + message + ' 항목을 전체 선택하시겠습니까?'
      } else {
        textMsg = '' + message + ' 항목을 전체 취소하시겠습니까?'
      }
      this.$modal.show('dialog', {
        title: '전체 선택',
        text: textMsg,
        buttons: [{
          title: '취소',
          handler: () => {
            this.store_info[data] = Number(!this.store_info[data])
            console.log(data)
            this.$modal.hide('dialog')
          }
        },
        {
          title: '확인', // Button title
          default: true, // ENTER KEY
          handler: () => {
            this.apiCheckAll(data)
            this.$modal.hide('dialog')
          }
        }]
      })
    },
    /**
     * 체크된 데이터에 대한 [상품정보 수정하기] 클릭시 컨펌창
     */
    reqConfirm () {
      if (!this.userMenuAuth.create_data) {
        return
      }
      this.$modal.show('dialog', {
        title: '상품정보 수정하기',
        text: '연동하시겠습니까?',
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
            this.productCheckListReq()
            this.$modal.hide('dialog')
          }
        }]
      })
    },
    /**
     * 체크된 데이터에 대한 [상품정보 수정하기] 클릭 메소드
     */
    productCheckListReq () {
      if (this.userMenuAuth.retrieve_list) {
        if (this.$store.state.searchParam === undefined || this.$store.state.searchParam.mall_no === 0) {
          this.$modal.show(ModalAlert,
            {title: '', text: '쇼핑몰을 선택해주세요.'},
            { width: 450, height: 'auto' }
          )
          return
        }

        let list = []
        let isOptionChanged = 0
        for (const idx in this.responseData.data.list) {
          if (this.responseData.data.list[idx]['selected']) {
            let item = {}
            item.product_no = this.responseData.data.list[idx].product_no
            item.product_link_yn = this.responseData.data.list[idx].product_link_yn
            item.product_name_link_yn = this.responseData.data.list[idx].product_name_link_yn
            item.product_detail_link_yn = this.responseData.data.list[idx].product_detail_link_yn
            item.product_price_link_yn = this.responseData.data.list[idx].product_price_link_yn
            item.product_sell_link_yn = this.responseData.data.list[idx].product_sell_link_yn

            if (this.responseData.data.list[idx].smartstore_product_name !== this.initResponseData.data.list[idx].smartstore_product_name) {
              item.smartstore_product_name = this.responseData.data.list[idx].smartstore_product_name
            }
            if (this.responseData.data.list[idx].smart_product_price !== this.initResponseData.data.list[idx].smart_product_price ||
                this.responseData.data.list[idx].smart_product_pc_discount_price !== this.initResponseData.data.list[idx].smart_product_pc_discount_price
            ) {
              item.smart_product_price = this.responseData.data.list[idx].smart_product_price
              item.smart_product_pc_discount_price = this.responseData.data.list[idx].smart_product_pc_discount_price
            }
            if (this.responseData.data.list[idx].smartstore_sell_yn !== this.initResponseData.data.list[idx].smartstore_sell_yn) {
              item.smartstore_sell_yn = this.responseData.data.list[idx].smartstore_sell_yn
            }
            item.product_option_link_yn = this.responseData.data.list[idx].product_option_link_yn
            if (this.responseData.data.list[idx].product_option_link_yn !== this.initResponseData.data.list[idx].product_option_link_yn) {
              isOptionChanged = 1
            }

            item.smartstore_product_no = this.responseData.data.list[idx].smartstore_product_no
            item.product_name = this.responseData.data.list[idx].product_name // 에러 메시지 떄문에 사용
            item.product_register_date = this.responseData.data.list[idx].product_register_date // 정렬때문에 사용
            item.file_product_size_image_url_addr = this.responseData.data.list[idx].file_product_size_image_url_addr // 사이즈 이미즈 없는 건 client 에서 막으려고 추가
            item.file_product_size_image_fail_reason = this.responseData.data.list[idx].file_product_size_image_fail_reason // 사이즈 이미즈 없는 건 client 에서 막으려고 추가
            list.push(item)
          }
        }

        if (list.length === 0) {
          this.$modal.show(ModalAlert,
            { title: '', text: '연동할 상품을 선택해주세요.' }, { width: 450, height: 'auto' }
          )
          return
        }
        // 등록일 순으로 오름차순
        list.sort(function (a, b) {
          return a.product_register_date < b.product_register_date ? -1 : a.product_register_date > b.product_register_date ? 1 : 0
        })

        // eslint-disable-next-line no-undef
        let apiUpdateDate = dateTimeFormatToString(new Date())
        let params = {
          user_code: 1,
          user_no: this.userNo,
          hosting_no: this.$store.state.searchParam.hosting_no,
          mall_id: this.$store.state.searchParam.mall_id,
          mall_no: this.$store.state.searchParam.mall_no,
          product_option_link_yn_ischanged: isOptionChanged,
          api_update_date: apiUpdateDate
          // list: list
        }

        eventBus.$emit('productProgress', params.mall_id, list.length, null, false, true) // 시작 event
        this.isReqBtnActive = false
        this.requestSmartstore(params, list)
      }
    },
    chunk (array, chunkSize) {
      var that = array
      return Array(Math.ceil(that.length / chunkSize)).fill().map(function (_, i) {
        return that.slice(i * chunkSize, i * chunkSize + chunkSize)
      })
    },
    async requestSmartstore (params, list) {
      let tempList = list
      tempList = this.chunk(tempList, 3)
      const promises = []
      for (const tempArr of tempList) {
        // api 연동 3개씩 끊어서 호출되고, 3개가 끝날때까지 기다리고 끝나면 다시 3개를 이어서 보냄
        promises.push(await this.getAwait(tempArr, params, list.length))
      }
      await Promise.all(promises)
      // api 연동 완료 시점
      eventBus.$emit('productProgress', params.mall_id, list.length, null, true)
      console.log('Done!')
    },
    async getAwait (tempArr, params, totCnt) {
      const promises = []
      for (const item of tempArr) {
        promises.push(
          this.getParallelList(params, item).then(function (resolvedData) {
            eventBus.$emit('productProgress', params.mall_id, totCnt, item, false)
          }).catch(function (err) {
            console.log('err', err)
            item.errorMsg = err.message
            eventBus.$emit('productProgress', params.mall_id, totCnt, item, false, false, true)
          })
        )
        console.log(promises)
      }
      await Promise.all(promises)
      console.log('part Done!')
      return promises
    },
    getParallelList (params, item) {
      console.log('getParallelList')
      // eslint-disable-next-line no-undef
      let url = smartStroreApiUrl + '/smartstore/update/product/' + this.$store.state.searchParam.mall_id
      params.itemInfo = item
      return new Promise((resolve, reject) =>
        http
          .post(url, JSON.stringify(params))
          .then(response => {
            console.log(response)
            if (response.data.responseCode !== undefined) {
              if (response.data.responseCode === 0) {
                resolve(item.product_no)
              } else if (response.data.responseCode === -1) {
                reject(new Error(response.data.responseDetail))
              }
            } else { // 응답 vo로 바꾸기 이전
              if (response.data.indexOf('Fail') >= 0) {
                if (response.data) {
                  reject(new Error(response.data))
                } else {
                  reject(new Error('상품데이터 확인필요'))
                }
              } else {
                resolve(item.product_no)
              }
            }
          })
          .catch(e => {
            console.log(e)
            if (e.code === 'ECONNABORTED') {
              // timeout
              this.isLoading = false
              reject(new Error('timeout'))
            } else {
              reject(new Error('서버오류'))
            }
          })
          .finally(() => {
          })
      )
    },
    /**
     * targetPart : 스마트스토어 변수명
     * part : 몰 변수명
     */
    typingPrice (item, idx, targetPart, part) {
      let originalPrice = this.initResponseData.data.list[idx][targetPart]
      let productMaxPrice = originalPrice + (originalPrice * 0.5) // 기본 옵션 -> 수정 가능한 최대값
      let productMinPrice = originalPrice + (originalPrice * -0.5) // 기본 옵션 -> 수정 가능한 최소값
      let typePrice = parseInt(event.target.value.replace(/,/gi, ''))
      // 기본 옵션의 추가가격 수정일 경우 상품가의 -50% ~ 50% 범위 내에서만 수정 가능
      if (typePrice > productMaxPrice || typePrice < productMinPrice) {
        this.$modal.show(ModalAlert,
          {
            html: '상품 판매가격(' + parseInt(item.product_price).toLocaleString() + '원)의 50% 범위 이내에서 수정 가능합니다.<br><font color="gray">수정가능 금액 : ' + parseInt(productMinPrice).toLocaleString() + '원 ~ ' + parseInt(productMaxPrice).toLocaleString() + '원</font>'
          },
          { width: 700, height: 'auto' }
        )
        this.responseData.data.list[idx][targetPart] = this.initResponseData.data.list[idx][targetPart]
        /** 할인가 수정시 할인가는 상품금액보다 크면 안됨 */
      } else if (targetPart === 'smart_product_pc_discount_price' && typePrice > this.responseData.data.list[idx]['smart_product_price']) {
        this.$modal.show(ModalAlert,
          {
            html: '할인가는 상품 판매가격(' + parseInt(item.smart_product_price).toLocaleString() + ')원 이하로 설정가능합니다.'
          },
          { width: 700, height: 'auto' }
        )
        this.responseData.data.list[idx][targetPart] = this.initResponseData.data.list[idx][targetPart]
      } else if (targetPart === 'smart_product_price' && typePrice < this.responseData.data.list[idx]['smart_product_pc_discount_price']) {
        this.$modal.show(ModalAlert,
          {
            html: '판매가는 상품 할인가격(' + parseInt(item.smart_product_pc_discount_price).toLocaleString() + ')원 이상으로 설정가능합니다.'
          },
          { width: 700, height: 'auto' }
        )
        this.responseData.data.list[idx][targetPart] = this.initResponseData.data.list[idx][targetPart]
      } else { // 변경가능한 금액 범위
        /** 자사몰 상품가격과 스마트스토어 상품가격이 같으면 상품가격연동 */

        if (this.responseData.data.list[idx]['product_pc_discount_price'] === this.responseData.data.list[idx]['smart_product_pc_discount_price'] &&
            this.responseData.data.list[idx]['product_price'] === this.responseData.data.list[idx]['smart_product_price']) {
          this.responseData.data.list[idx]['product_price_link_yn'] = 1
        } else {
          this.responseData.data.list[idx]['product_price_link_yn'] = 0
        }
      }
    },
    typingProductName (item, idx) {
      /** 자사몰 상품명과 스마트스토어 상품명이 같으면 상품이름연동 */
      if (this.responseData.data.list[idx]['product_name'] === this.responseData.data.list[idx]['smartstore_product_name']) {
        this.responseData.data.list[idx]['product_name_link_yn'] = 1
      } else {
        this.responseData.data.list[idx]['product_name_link_yn'] = 0
      }
    },
    changeSelect (item, idx) {
      if (this.responseData.data.list[idx]['display_yn'] === this.responseData.data.list[idx]['smartstore_sell_yn']) {
        this.responseData.data.list[idx]['product_sell_link_yn'] = 1
      } else {
        this.responseData.data.list[idx]['product_sell_link_yn'] = 0
      }
    },
    showEditOption (item) {
      this.$modal.show(ModalOptionEditList,
        { item: item, userMenuAuth: this.userMenuAuth },
        { width: '50%', height: 'auto', clickToClose: false, scrollable: true }
      )
    },

    showHashTag (item) {
      this.$modal.show(ModalHashTag,
        { item: item },
        { width: '50%', height: 'auto', scrollable: true }
      )
    },
    updateDeletedProductStatus (item) {
      let smartstoreProductName = item.product_name
      let url = '/smartstore/productmatch/deleted/product/status'

      let btnText = {
        cancleText: '취소',
        confirmText: '확인'
      }

      let param = {
        user_code: 1,
        user_no: this.userNo,
        mall_no: this.$store.state.searchParam.mall_no,
        mall_id: this.$store.state.searchParam.mall_id,
        product_no: item.product_no,
        smartstore_product_no: item.smartstore_product_no,
        del_yn: item.smartstore_del_yn
      }

      let data = {
        url: url,
        requestParam: param,
        btnText: btnText,
        eventBusParam: this.searchParam.current_page
      }

      let link = item.product_mall_url_addr + item.product_no
      let html = '스마트스토어에서 삭제한 <br><a href="' + link + '" target="_blank"><strong>' + smartstoreProductName + '</strong></a><br>상품을 연동 전 상태로 변경하시겠습니까?'
      this.$modal.show(ModalHtmlConfirm,
        {
          title: '연동 상태 변경',
          html: html,
          data: data
        },
        {
          width: 600, height: 'auto'
        }
      )
    },
    selectProductLog (item) {
      this.$modal.show(ModalProductLog,
        {
          item: item
        },
        {
          width: '80%',
          height: 'auto',
          scrollable: true }
      )
    },
    showFilterModal () {
      if (this.$store.state.searchParam === undefined || this.$store.state.searchParam.mall_no === 0) {
        this.$modal.show(ModalAlert,
          {title: '', text: '쇼핑몰을 선택해주세요.'},
          { width: 450, height: 'auto' }
        )
        return
      }

      this.$modal.show(MallProductListFilter,
        {
          savedFlagObject: this.flagObject
        },
        {
          width: 430,
          height: 'auto',
          clickToClose: false,
          draggable: true
        }
        // { width: 430, height: 'auto', draggable: true, clickToClose: false }
      )
    },
    getFilteredProductList (flagObject) {
      this.responseData.data.list = JSON.parse(JSON.stringify(this.initResponseData.data.list))
      this.flagObject = flagObject
      let filterInitYn = flagObject.filterInitYn
      // console.log('flagObject : ' + JSON.stringify(flagObject))

      let offSyncFlag = 0
      for (var syncIdx in flagObject.syncFlag) {
        if (!flagObject.syncFlag[syncIdx]) {
          offSyncFlag = offSyncFlag + 1
        }
      }

      let offConnectFlag = 0
      for (var connectIdx in flagObject.connectFlag) {
        if (!flagObject.connectFlag[connectIdx]) {
          offConnectFlag = offConnectFlag + 1
        }
      }

      if (!filterInitYn) {
        for (var idx in this.responseData.data.list) {
          this.responseData.data.list[idx]['filtered'] = 0
          if (offConnectFlag !== 3) {
            for (var key in flagObject.connectFlag) {
              if (flagObject.connectFlag[key]) {
                if (
                  (key === 'product_unmatched_link_yn' && parseInt(this.responseData.data.list[idx]['product_luna_link_yn']) === 0) ||
                      (key === 'product_luna_link_yn' && parseInt(this.responseData.data.list[idx]['product_luna_link_yn']) === 1 && parseInt(this.responseData.data.list[idx]['smartstore_del_yn']) === 0) ||
                      (key === 'smartstore_del_yn' && parseInt(this.responseData.data.list[idx]['smartstore_del_yn']) === 1)
                ) {
                  if (offSyncFlag === 5) {
                    if (parseInt(this.responseData.data.list[idx]['product_price_link_yn']) === 0 &&
                        parseInt(this.responseData.data.list[idx]['product_sell_link_yn']) === 0 &&
                        parseInt(this.responseData.data.list[idx]['product_option_link_yn']) === 0 &&
                        parseInt(this.responseData.data.list[idx]['product_name_link_yn']) === 0 &&
                        parseInt(this.responseData.data.list[idx]['product_detail_link_yn']) === 0) {
                      this.responseData.data.list[idx]['filtered'] = 1
                    }/* 동기화 플래그 전체 꺼져있는 상품 검색 끝 */
                  } else {
                    for (var innerKey in flagObject.syncFlag) {
                      /* 동기화 플래그 켜져있는 상품 검색 */
                      if (flagObject.syncFlag[innerKey]) {
                        if (parseInt(this.responseData.data.list[idx][innerKey]) === 1) {
                          this.responseData.data.list[idx]['filtered'] = 1
                        }
                      }/* 동기화 플래그 켜져있는 상품 검색 끝 */
                    }
                  }
                }
              }
            }
          } else {
            if (offSyncFlag === 5) {
              if (parseInt(this.responseData.data.list[idx]['product_price_link_yn']) === 0 &&
                        parseInt(this.responseData.data.list[idx]['product_sell_link_yn']) === 0 &&
                        parseInt(this.responseData.data.list[idx]['product_option_link_yn']) === 0 &&
                        parseInt(this.responseData.data.list[idx]['product_name_link_yn']) === 0 &&
                        parseInt(this.responseData.data.list[idx]['product_detail_link_yn']) === 0) {
                this.responseData.data.list[idx]['filtered'] = 1
              }/* 동기화 플래그 전체 꺼져있는 상품 검색 끝 */
            } else {
              for (innerKey in flagObject.syncFlag) {
                /* 동기화 플래그 켜져있는 상품 검색 */
                if (flagObject.syncFlag[innerKey]) {
                  if (parseInt(this.responseData.data.list[idx][innerKey]) === 1) {
                    this.responseData.data.list[idx]['filtered'] = 1
                  }
                }/* 동기화 플래그 켜져있는 상품 검색 끝 */
              }
            }
          }
        }
        this.$forceUpdate()
      } else { /* 검색조건 초기화 or 전체 선택인 경우 */
        this.flagObject = this.resetFlagObject()
        this.responseData.data.list = JSON.parse(JSON.stringify(this.initResponseData.data.list))
        this.$forceUpdate()
      }
    },
    resetFlagObject () {
      return {
        allCheckYn: false, // 전체 선택
        filterInitYn: false, // 필터링 초기화 여부
        connectFlag: {
          product_luna_link_yn: false, // 자사몰 연동 상품
          product_unmatched_link_yn: false,
          // product_luna_link_yn: false, // 수기등록 매칭 상품
          smartstore_del_yn: false // 삭제된 연동 상품
        },
        syncFlag: {
          product_price_link_yn: false, // 가격 동기화 상품
          product_sell_link_yn: false, // 판매상태 동기화 상품
          product_option_link_yn: false, // 옵션(재고) 동기화 상품
          product_name_link_yn: false, // 상품명 동기화 상품
          product_detail_link_yn: false // 상품상세 동기화 상품
        }
      }
    }
  },
  props: {
    'userMenuAuth': Object,
    'searchParam': Object
  }
}

</script>
<style scoped lang="scss">

.bootstrapBtnFixed {
  position: fixed;
  right: 40px;
  top: 90%;
  // background-color: #37a8fe;
  // border-color: #37a8fe;
}

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
    height: 12px;
    width: 12px;
    // left: 2px;
    bottom: 2px;
    background-color: #fff;
    -webkit-transition: .3s;
    transition: .3s;
    border-radius: 50%;
}

.toggleWrap .switch {
    position: relative;
    display: inline-block;
    width: 35px;
    height: 16px;
}

.tdLine {
  border-bottom: 1px solid #888;
}
// 용석 연동상태 변경 아이콘 관련 CSS 추가
.tooltip-toggle {
    position: relative;
    /** hover시 눈깔 투명도 처리 */
    &:hover {
      cursor: pointer;
      // opacity: .8;
      opacity: 1;
      //눈깔모양 opacity(투명도) 푸는거 이건 어려워서 패스
      // >>> .toast {
      //   opacity: 1;
      // }
    }
}
/** 툴팁 중간 영역 위치 지정 처리 */
.tooltip-toggle .b-toast.b-toast-prepend {
  position: absolute;
}
/** 툴팁 상세 스타일 지정 글자 크기에 맞춰서 정렬 처리 */
.tooltip-toggle >>> .toast {
  display: none;
  position: absolute;
  max-width: 300px !important;
  top: 0px;
  margin-left: 5px;
  font-size: 1rem;
  width: auto;
  min-width: 125px;
  left: 50%;
  transform: translateX(-50%);
}
/** hover 이벤트 시 툴팁 노출 처리 */
.tooltip-toggle:hover >>> .toast {
  display: block;
  opacity: 1;
}
.tooltip-toggle >>> .toast-header { display: none !important; }
</style>
