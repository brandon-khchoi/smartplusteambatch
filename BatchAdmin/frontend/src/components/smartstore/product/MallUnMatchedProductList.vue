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
                <select v-model="orderby" style="width:150px" @change="get(1, searchParam.per_page)">
                  <option :value=0>자사몰 등록일 역순</option>
                  <option :value=1>자사몰 등록일</option>
                  <option :value=2>자사몰 수정일 역순</option>
                  <option :value=3>자사몰 수정일</option>
                </select>
              </li>
              <li v-if="isReqBtnActive">
                <p class="btn_add" @click="reqConfirm()">
                  상품 연동하기
                </p>
              </li>
            </ul>
          </div>
        </div>
        <div class="tableWrap tableList tableGroup">
          <table summary="스마트스토어 상품관리">
            <caption class="blind">스마트스토어 상품관리</caption>
            <colgroup>
                <col style="width:3%"/>
                <col style="width:7%"/>
                <col style="width:3%"/>
                <col style="width:10%"/>
                <col style="width:5%"/>
                <col style="width:5%"/>
                <col style="width:5%"/>
                <col style="width:5%"/>
                <col style="width:5%"/>
                <col style="width:5%"/>
                <col style="width:10%"/>
                <col style="width:5%"/>
                <col style="width:5%"/>
                <col style="width:5%"/>
                <!-- <col style="width:5%"/> -->
                <!-- <col style="width:4%"/> -->
            </colgroup>
            <thead>
            <tr>
              <th rowspan="2">
                <span class="inputWrap checkboxCenter">
                  <input type="checkbox" id="service01-01-01" @change="checkAll" v-model="allSelected" >
                  <label for="service01-01-01"></label>
                </span>
              </th>
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
              <th rowspan="2">등록일(수정일)</th>
              <th rowspan="2">사이즈 이미지</th>
              <th rowspan="2">노출여부</th>
              <th rowspan="2">상세</th>
              <!-- <th rowspan="2">상세</th> -->
            </tr>
            </thead>
            <tbody>
              <template v-if="responseData.total_cnt">
              <template v-for="(item, idx) in responseData.data.list">
              <tr
                :key="idx" :style="[item.changed ? { 'background': '#fff0f0'} : {}]" >
                <!-- 체크박스 -->

                <td class="textCenter">
                  <div :key="idx + 'way'" v-if="idx == 5" v-waypoint="{ active: true, callback: onWaypoint}" ></div>
                  <span class="inputWrap checkboxCenter">
                    <input type="checkbox" :id="'service01-01' + idx " :value="item.product_no"
                      v-model="item.selected" @click="select(idx)">
                    <label :for="'service01-01' + idx "></label>
                  </span>
                </td>
                <!-- 이미지 -->
                <td class="textCenter">
                    <template v-if="setProductThumbnailpriority[idx] === '-'">
                      {{setProductThumbnailpriority[idx]}}
                    </template>
                    <a v-else @click="$parent.showImg(setProductThumbnailpriority[idx])">
                      <img v-if="setProductThumbnailpriority[idx]" v-lazy="setProductThumbnailpriority[idx]"
                        style="width: 50px;"/>
                    </a>
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
                <!-- 카테고리 -->
                <td class="textCenter">
                  <button type="button" class="btn_min" :id="idx + '_btn_category'" @click="$parent.showCategory(item, 'mall')">보기</button>
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
                <!-- 등록일(수정일) -->
                <td class="textCenter">
                    {{item.product_register_date}} <br>
                    ({{item.product_modify_date}})
                </td>
                <!-- 사이즈 이미지 -->
                <td class="textCenter">
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
                <td>
                  <div class="textCenter">
                  <template v-if="parseInt(item.hide_yn) === 0">
                    <!-- <i class="tooltip-toggle fas fa-eye" @mouseover="$bvToast.show('hideToast_'+idx)" style="padding:8px;">
                      <b-toast :id="'hideToast_'+idx" static no-auto-hide>
                        <b-link><a @click="getProductHideEvent(item)">목록에서 숨기기</a></b-link>
                      </b-toast>
                    </i> -->
                    <i class="tooltip-toggle fas fa-eye" @click="showConfirmModal(item)" style="padding:8px;"></i>
                  </template>
                  <template v-else>
                    <!-- <i class="tooltip-toggle fas fa-eye-slash" @mouseover="$bvToast.show('unHideToast_'+idx)" style="padding:8px;">
                      <b-toast :id="'unHideToast_'+idx" static no-auto-hide>
                        <b-link><a @click="getProductHideEvent(item)">목록에서 보이기</a></b-link>
                      </b-toast>
                    </i> -->
                    <i class="tooltip-toggle fas fa-eye-slash" @click="showConfirmModal(item)" style="padding:8px;"> </i>
                  </template>
                  </div>
                </td>
                <!-- <td>
                  <template>
                    <div class="textCenter">
                        <i class="tooltip-toggle fas fa-ellipsis-h" @mouseover="$bvToast.show('detailToast_'+idx)" style="padding:8px;">
                            <b-toast :id="'detailToast_'+idx" static no-auto-hide>
                              <b-link><a>작업이력</a></b-link>
                            </b-toast>
                        </i>
                    </div>
                  </template>
                </td> -->
                <td class="textCenter">
                    <i class="bootstrapWrapper tooltip-toggle fas fa-ellipsis-h" @mouseover="$bvToast.show('hideToast')" style="padding:8px;">
                        <b-toast id="hideToast" static no-auto-hide>
                          <div>
                            <b-link><a @click="selectCreateProductFailReason(item)">연동실패사유</a></b-link>
                          </div>
                        </b-toast>
                    </i>
                </td>
              </tr>
              </template>
              </template>
              <template v-else>
                <tr >
                  <td colspan="14" class="textCenter" >
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
      >상품 연동하기</button>
    </div>

  </div>

</template>

<script>

import Pagination from '@/components/common/Pagination'
import ModalAlert from '@/components/common/modal/ModalAlert'
import ModalHtmlConfirm from '@/components/common/modal/ModalHtmlConfirm'
import ModalProductFailReasonLog from './ModalProductFailReasonLog'
import http from '@/http-commons'
import { eventBus } from '@/main'

// 자사몰 미연동리스트
export default {
  name: 'MallUnMatchedProductList',
  components: {
    Pagination
  },
  data () {
    return {
      isLoading: false,
      userNo: this.$store.getters.getUser.luna_no,
      allSelected: false,
      isFilterFormShow: false, // 검색필터 열기 닫기
      isReqBtnActive: true, // 상품 연동하기 활성화 버튼
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
      orderby: 0,
      isScrollBtnShow: false // 스크롤에 대한 버튼
    }
  },
  mounted () {
    // 이벤트 받는 부분
    eventBus.$on('mallUnMatchedProductList', (currentPage) => {
      // 검색버튼 및 탭클릭으로 호출 됨
      console.log('event on mallUnMatchedProductList' + currentPage)
      this.get(currentPage, this.searchParam.per_page)
    })
    eventBus.$on('mallUnMatchedProductListProgress', () => {
      // 프로그레스 완료시 호출
      this.isReqBtnActive = true
      this.get(this.searchParam.current_page, this.searchParam.per_page)
    })
    eventBus.$on('success', (currentPage) => {
      this.get(this.searchParam.current_page, this.searchParam.per_page)
    })
    // eventBus.$on('modalConfirmSuccess', () => {
    //   // 프로그레스 완료시 호출
    //   eventBus.$emit('dashboard', this.$store.state.searchParam)
    //   this.get(this.searchParam.current_page, this.searchParam.per_page)
    // })
  },
  beforeDestroy () {
    eventBus.$off('mallUnMatchedProductList')
    eventBus.$off('mallUnMatchedProductListProgress')
    eventBus.$off('success')
  },
  watch: {
    'responseData.data.list': {
      deep: true,
      handler (newVal, oldVal) {
        // console.log(newVal === oldVal)

        if (oldVal !== undefined && oldVal.length > 0) { // 페이지 로딩시 무조건 값 세팅되는것 방지
          // isChangedData 를 false 로 변경시 isResetData를 true 로 변경해야 handler 사용 가능
          let initDataList = this.initResponseData.data.list
          let dataList = this.responseData.data.list
          // let selected = this.selected
          newVal.filter(function (p, idx) {
            return Object.keys(p).some(function (prop) {
              if (prop === 'selected') return true

              if (prop === 'changed') { // 변경되었던 데이터만 changed 라는 객체로 잇어서 changed 가 있는 데이터들만 삭제
                delete dataList[idx]['changed']
                delete dataList[idx]['selected']
                // selected.splice(idx, 1)
              } else {
                if (p[prop] !== initDataList[idx][prop]) {
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
  computed: {
    setProductThumbnailpriority () {
      // item.product_image_type_code

      let imageTypeCode = this.responseData.data.list[0].product_image_type_code

      let imageArr = []
      // if(detailData.indexOf('gif') === -1)
      switch (imageTypeCode) {
        case 1:
          imageArr.push('file_image_detail_text')
          imageArr.push('file_image_list_text')
          imageArr.push('file_image_tiny_text')
          break
        case 2:
          imageArr.push('file_image_list_text')
          imageArr.push('file_image_detail_text')
          imageArr.push('file_image_tiny_text')
          break
        case 3:
          imageArr.push('file_image_tiny_text')
          imageArr.push('file_image_detail_text')
          imageArr.push('file_image_list_text')
          break
      }
      let data = []
      for (const idx in this.responseData.data.list) {
        const item = this.responseData.data.list[idx]
        /** gif 제외하고 노출 */
        for (let i = 0; i < imageArr.length; i++) {
          if (item[imageArr[i]] !== undefined && item[imageArr[i]].indexOf('gif') === -1) {
            data.push(item[imageArr[i]])
            break
          }
        }
        if (data[idx] === undefined) {
          for (let i = 0; i < imageArr.length; i++) {
            /** 전부다 gif 면 있는 gif이미지라도 노출 */
            if (item[imageArr[i]] !== undefined) {
              data.push(item[imageArr[i]])
              break
            }
          }
        }
        /** 아무것도 없을 경우 빈값이라도 넣어야 list 데이터 개수 일치 */
        if (data[idx] === undefined) {
          data.push('-')
        }
      }

      return data
    }
  },
  methods: {
    onWaypoint ({ going, direction }) {
      // going: in, out
      // direction: top, right, bottom, left
      console.log(direction)
      if (direction === this.$waypointMap.DIRECTION_BOTTOM && going === this.$waypointMap.GOING_OUT) { // 위쪽으로 스크롤 및 위로 포인트 지점 지날때
        this.isScrollBtnShow = false
      }
      if (direction === this.$waypointMap.DIRECTION_TOP) { // 아래쪽으로 스크롤
        this.isScrollBtnShow = true
      }
    },
    get (currentPage, perPage) {
      if (this.userMenuAuth.retrieve_list) {
        if (this.$store.state.searchParam === undefined || this.$store.state.searchParam.mall_no === 0) {
          this.$modal.show(ModalAlert,
            {title: '', text: '쇼핑몰을 선택해주세요.'},
            { width: 450, height: 'auto' }
          )
          return
        }
        /** 가격 검색 */
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
        console.log('search')
        this.isLoading = true
        let url = '/smartstore/productmatch/unmatchedlist'
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
              product_name: encodeURIComponent(this.searchParam.product_name.trim()),
              display_yn: this.searchParam.display_yn,
              hide_yn: this.searchParam.hide_yn,
              size_image_yn: this.searchParam.size_image_yn,
              product_price_start: productPriceStart,
              product_price_end: productPriceEnd,
              product_pc_discount_price_start: productPcDiscountPriceStart,
              product_pc_discount_price_end: productPcDiscountPriceEnd,
              product_category_no: this.searchParam.product_category_no
            }
          })
          .then(response => {
            this.isLoading = false
            this.responseData = response.data
            // eslint-disable-next-line no-undef
            this.responseData.paginate = makePaginate(response.data); this.responseData.max = pageMaxNumber(response.data)
            this.initResponseData = JSON.parse(JSON.stringify(this.responseData)) // 값만 복사

            this.searchParam.current_page = this.responseData.current_page
            this.searchParam.per_page = this.responseData.per_page

            this.allSelected = false // 연동완료 거나 페이지 변경 및 다시 조회시 전체 체크박스 해제
            this.checkAll()

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
    /**
     * 왼쪽 제일 상단 전체 체크박스 선택
     */
    checkAll () { // 전체선택 체크박스
      if (this.allSelected) {
        for (const item in this.responseData.data.list) {
          this.responseData.data.list[item]['selected'] = this.responseData.data.list[item].product_no.toString()
        }
      } else {
        for (const item in this.responseData.data.list) {
          delete this.responseData.data.list[item]['selected']
        }
      }
    },
    /**
     * 한개 체크 박스 선택시
     */
    select (idx) {
      this.allSelected = false
      // this.selectdJsonData
      if (event.target.checked) { // 상품 하나 체크 on
        if (this.responseData.data.list[idx].product_no === parseInt(event.target.value)) {
          console.log(event.target.value)
          this.responseData.data.list[idx]['selected'] = event.target.value
          // this.selected.push(event.target.value)
          // this.selectedJsonData
        }
      } else { // 상품 하나 체크 off
        if (parseInt(this.responseData.data.list[idx]['selected']) === parseInt(event.target.value)) {
          // this.selected.splice(idx, 1)
          delete this.responseData.data.list[idx]['selected']
        }
      }
    },
    /**
     * 체크된 데이터에 대한 [상품 연동하기] 클릭시 컨펌창
     */
    reqConfirm () {
      if (!this.userMenuAuth.create_data) {
        return
      }
      this.$modal.show('dialog', {
        title: '상품 연동하기',
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
     * 체크된 데이터에 대한 [상품 연동하기] 클릭 메소드
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

        for (const idx in this.responseData.data.list) {
          if (this.responseData.data.list[idx]['selected']) {
            let item = {}
            item.product_no = this.responseData.data.list[idx].product_no
            item.product_link_yn = 0
            item.product_name = this.responseData.data.list[idx].product_name // error message 때문에 추가
            item.product_register_date = this.responseData.data.list[idx].product_register_date
            item.file_product_size_image_url_addr = this.responseData.data.list[idx].file_product_size_image_url_addr // 사이즈 이미즈 없는 건 client 에서 막으려고 추가
            item.file_product_size_image_fail_reason = this.responseData.data.list[idx].file_product_size_image_fail_reason // 사이즈 이미즈 없는 건 client 에서 막으려고 추가
            list.push(item)
          }
        }
        console.log('list', list)
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
          api_update_date: apiUpdateDate
          // list: list
        }
        eventBus.$emit('productProgressUnMatched', params.mall_id, list.length, null, false, true) // 시작 event
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
      eventBus.$emit('productProgressUnMatched', params.mall_id, list.length, null, true)
      console.log('Done!')
    },
    async getAwait (tempArr, params, totCnt) {
      const promises = []
      for (const item of tempArr) {
        promises.push(
          this.getParallelList(params, item).then(function (resolvedData) {
            eventBus.$emit('productProgressUnMatched', params.mall_id, totCnt, item, false)
          }).catch(function (err) {
            console.log('err', err)
            item.errorMsg = err.message
            eventBus.$emit('productProgressUnMatched', params.mall_id, totCnt, item, false, false, true)
          })
        )
      }
      await Promise.all(promises)
      console.log('part Done!')
      return promises
    },
    getParallelList (params, item) {
      // eslint-disable-next-line no-undef
      let url = smartStroreApiUrl + '/smartstore/insertproduct/' + this.$store.state.searchParam.mall_id
      params.list = []
      // params.list.push(item)
      params.itemInfo = item
      return new Promise((resolve, reject) =>
        http
          .post(url, JSON.stringify(params))
          .then(response => {
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
    showConfirmModal (item) {
      let smartstoreProductName = item.product_name
      let url = '/smartstore/productmatch/hide'

      let btnText = {
        cancleText: '취소',
        confirmText: '확인'
      }

      let param = { // query string
        mall_no: this.$store.state.searchParam.mall_no,
        mall_id: this.$store.state.searchParam.mall_id,
        product_no: item.product_no,
        user_code: 1,
        user_no: this.userNo,
        hide_status: item.hide_yn
      }

      let data = {
        url: url,
        requestParam: param,
        btnText: btnText,
        eventBusParam: this.searchParam.current_page
      }

      let link = item.product_mall_url_addr + item.product_no
      let html = '<a href="' + link + '" target="_blank"><strong>' + smartstoreProductName + '</strong></a><br>상품을 숨김 상태를 변경 하시겠습니까?'
      let title = '숨김 상태 변경'
      if (parseInt(item.hide_yn) === 0) {
        title = '목록에서 숨기기'
        html = '<a href="' + link + '" target="_blank"><strong>' + smartstoreProductName + '</strong></a><br>상품을 숨김 처리 하시겠습니까?'
      } else {
        title = '목록에서 보이기'
        html = '<a href="' + link + '" target="_blank"><strong>' + smartstoreProductName + '</strong></a><br>상품을 노출 처리 하시겠습니까?'
      }
      this.$modal.show(ModalHtmlConfirm,
        {
          title: title,
          html: html,
          data: data
        },
        {
          width: 600, height: 'auto', clickToClose: false
        }
      )
    },
    selectCreateProductFailReason (item) {
      this.$modal.show(ModalProductFailReasonLog,
        {
          item: item
        },
        {
          width: '80%',
          height: 'auto',
          scrollable: true }
      )
    }
  },
  props: {
    'userMenuAuth': Object,
    'searchParam': Object
  }
}

</script>
<style scoped lang="scss">

.bootstrapBtnFixed{
  position: fixed;
    right: 40px;
    top: 90%;
}
/** 툴팁 부모 영역 위치 지정 처리 */
.tooltip-toggle {
    position: relative;
    /** hover시 눈깔 투명도 처리 */
    &:hover {
      cursor: pointer;
      opacity: .7;
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
  min-width: 110px;
  left: 50%;
  transform: translateX(-50%);
}
/** hover 이벤트 시 툴팁 노출 처리 */
.tooltip-toggle:hover >>> .toast {
  display: block;
  opacity: 1;
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

.tdLine {
  border-bottom: 1px solid #888;
}
.tooltip-toggle >>> .toast-header { display: none !important; }
</style>
