<template>
  <div class="content main" style="margin-top: 10px; margin-bottom: 10px;">
    <div class="summaryWrap"  >
    <!-- <div class="summaryWrap" > -->
      <ul>
        <li>
          <p class="title">
            자사몰 상품현황
          </p>
          <ul>
            <li>
              <span>전체</span>
              <p>
                <animated-number
                  :value="responseData.mall_data.tot_cnt"
                  :duration="300"
                  :formatValue="formatToPrice"
                />
              </p>
            </li>
            <li>
              <span>판매중</span>
              <p>
                <animated-number
                  :value="responseData.mall_data.display_yn_1"
                  :duration="300"
                  :formatValue="formatToPrice"
                />
              </p>
            </li>
            <li>
              <span>판매중지</span>
              <p>
                <animated-number
                  :value="responseData.mall_data.display_yn_0"
                  :duration="300"
                  :formatValue="formatToPrice"
                />
              </p>
            </li>
            <li>
              <span>신규상품</span>
              <p>
                <animated-number
                  :value="responseData.mall_data.today_register_product"
                  :duration="300"
                  :formatValue="formatToPrice"
                />
              </p>
            </li>
            <li>
              <span>수정상품</span>
              <p>
                <animated-number
                  :value="responseData.mall_data.today_modify_product"
                  :duration="300"
                  :formatValue="formatToPrice"
                />
              </p>
            </li>
          </ul>
        </li>
        <li>
          <p class="title">
            스마트스토어 상품현황
          </p>
          <ul>
            <li style="width:12%;">
              <span>전체</span>
              <p>
               <animated-number
                  :value="responseData.smartstore_data.tot_cnt"
                  :duration="300"
                  :formatValue="formatToPrice"
                />
              </p>
            </li>
            <li style="width:10.5%;">
              <span>판매중</span>
              <p>
                <animated-number
                  :value="responseData.smartstore_data.sell_yn_1"
                  :duration="300"
                  :formatValue="formatToPrice"
                />
              </p>
            </li>
            <li style="width:10.5%;">
              <span>판매중지</span>
              <p>
                <animated-number
                  :value="responseData.smartstore_data.sell_yn_0"
                  :duration="300"
                  :formatValue="formatToPrice"
                />
              </p>
            </li>
            <li style="width:10.5%;">
              <span>전시중</span>
              <p>
                <animated-number
                  :value="responseData.smartstore_data.display_yn_1"
                  :duration="300"
                  :formatValue="formatToPrice"
                />
              </p>
            </li>
            <li style="width:10.5%;">
              <span>전시중지</span>
              <p>
                <animated-number
                  :value="responseData.smartstore_data.display_yn_0"
                  :duration="300"
                  :formatValue="formatToPrice"
                />
              </p>
            </li>
            <li>
              <span>연동상품</span>
              <p>
                <animated-number
                  :value="responseData.smartstore_data.product_link_yn_1"
                  :duration="300"
                  :formatValue="formatToPrice"
                />
              </p>
            </li>
            <li style="width:13%;">
              <span>수기등록 상품</span>
              <p>
                <animated-number
                  :value="responseData.smartstore_data.product_link_yn_0"
                  :duration="300"
                  :formatValue="formatToPrice"
                />
              </p>
            </li>
            <li>
              <span>신규상품</span>
              <p>
                <animated-number
                  :value="responseData.smartstore_data.today_register_product"
                  :duration="300"
                  :formatValue="formatToPrice"
                />
              </p>
            </li>
            <li>
              <span>수정상품</span>
              <p>
                <animated-number
                  :value="responseData.smartstore_data.today_modify_product"
                  :duration="300"
                  :formatValue="formatToPrice"
                />
              </p>
            </li>
          </ul>
        </li>
        <!-- <li>
           <p class="title">
            금일 상품 현황
           </p>
          <ul>
            <li>
              <span>상품등록</span>
              <p>{{responseData.mall_data.today_register_product}}</p>
            </li>
            <li>
              <span>상품수정</span>
              <p>{{responseData.mall_data.today_modify_product}}</p>
            </li>

          </ul>
        </li> -->
      </ul>
      <!-- -->
      <div class="timeDashboard" >
        <div style="margin-right:10px"><br><br><br><br><br>
        <p>※ 자사몰 상품 등록 기준일  :
          <template v-if="responseData.mall_data.product_standard_register_date">
            {{responseData.mall_data.product_standard_register_date}}
          </template>
          <template v-else>
            없음
          </template>
        </p>
        <p>※ 신규상품 최종변경일  :
          <template v-if="responseData.smartstore_data.last_product_register_date">
            {{responseData.smartstore_data.last_product_register_date}}
          </template>
          <template v-else>
            이력 없음
          </template>
        </p>
        <p>※ 수정상품 최종변경일  :
          <template v-if="responseData.smartstore_data.last_product_modify_date">
            {{responseData.smartstore_data.last_product_modify_date}}
          </template>
          <template v-else>
            이력 없음
          </template>
        </p>
        </div>
      </div>
    </div>
    <ProductProgress :key=0 :propData="progressData" v-if="progressData.isProgress" :message="'연동상품 연동...'" style="margin-top: 2px;"/>
    <ProductProgress :key=1 :propData="progressDataUnMatched" v-if="progressDataUnMatched.isProgress" :message="'미연동상품 연동중...'" style="margin-top: 2px;"/>

  </div>
</template>

<script>

import http from '@/http-commons'
import ModalAlert from '@/components/common/modal/ModalAlert'
import { eventBus } from '@/main'
import AnimatedNumber from 'animated-number-vue'

import ProductProgress from './ProductProgress'
import ModalErrorMsg from './ModalErrorMsg'

export default {
  name: 'ProductDashboard',
  components: {
    ProductProgress,
    AnimatedNumber
  },
  data () {
    return {
      userNo: this.$store.getters.getUser.luna_no,
      responseData: {
        smartstore_data: {
          product_link_yn_1: 0,
          display_yn_0: 0,
          display_yn_1: 0,
          sell_yn_0: 0,
          sell_yn_1: 0,
          product_link_yn_0: 0,
          today_register_product: 0,
          today_modify_product: 0,
          tot_cnt: 0
        },
        mall_data: {
          display_yn_0: 0,
          display_yn_1: 0,
          today_register_product: 0,
          today_modify_product: 0,
          tot_cnt: 0
        }
      },
      progressData: {
        resultCnt: -1, // 초기값은 -1, 로딩바가 도는 값은 0, 다시 끝나면 -1
        errCnt: 0,
        errItems: [],
        searchCnt: 0,
        isProgress: false
      },
      progressDataUnMatched: {
        resultCnt: -1,
        errCnt: 0,
        errItems: [],
        searchCnt: 0,
        isProgress: false
      }
    }
  },
  created () {
    eventBus.$on('dashboard', (searchParam) => {
      // 검색버튼으로만 호출되는 메소드
      this.get(searchParam)
    })

    /** [스마트스토어 연동상품] 프로그레스 */
    eventBus.$on('productProgress', (mallId, count, item, isFinish = false, isStart = false, isError = false) => {
      console.log('productProgress eventbus on')
      if (isFinish) { // api 호출이 모두 완료시 데이터 초기화
        this.progressData.isProgress = false
        this.progressData.searchCnt = 0
        this.progressData.resultCnt = -1
        this.progressData.errCnt = 0
        if (this.progressData.errItems.length === 0) {
          this.$modal.show(ModalAlert,
            { title: '', html: '상품연동을 완료하였습니다.' },
            { width: 450, height: 'auto' }
          )
        } else {
          this.$modal.show(ModalErrorMsg,
            { list: this.progressData.errItems },
            { width: '30%', height: 'auto', clickToClose: false, scrollable: true }
          )
        }

        // this.$parent.isLoading = false
        this.progressData.errItems = []
        eventBus.$emit('mallProductListProgress')
        console.log('real finish')
        return
      }
      if (isStart) { // 최초 시작시
        this.progressData.searchCnt = count
        this.progressData.resultCnt = 0
        return
      }

      if (isError) {
        this.progressData.errCnt += 1
        this.progressData.errItems.push(item)
        return
      }

      this.progressData.searchCnt = count
      this.progressData.isProgress = true
      this.progressData.resultCnt += 1
    })

    /** [스마트스토어 미연동상품] 프로그레스 */
    eventBus.$on('productProgressUnMatched', (mallId, count, item, isFinish = false, isStart = false, isError = false) => {
      console.log('productProgressUnMatched eventbus on')
      if (isFinish) { // api 호출이 모두 완료시 데이터 초기화
        this.progressDataUnMatched.isProgress = false
        this.progressDataUnMatched.searchCnt = 0
        this.progressDataUnMatched.resultCnt = -1
        this.progressDataUnMatched.errCnt = 0
        if (this.progressDataUnMatched.errItems.length === 0) {
          this.$modal.show(ModalAlert,
            { title: '', html: '상품연동을 완료하였습니다.' },
            { width: 450, height: 'auto' }
          )
        } else {
          this.$modal.show(ModalErrorMsg,
            { list: this.progressDataUnMatched.errItems },
            { width: '30%', height: 'auto', clickToClose: false, scrollable: true }
          )
        }

        this.progressDataUnMatched.errItems = []
        eventBus.$emit('mallUnMatchedProductListProgress')
        console.log('real finish')
        return
      }
      if (isStart) { // 최초 시작시
        this.progressDataUnMatched.searchCnt = count
        this.progressDataUnMatched.resultCnt = 0
        return
      }

      if (isError) {
        this.progressDataUnMatched.errCnt += 1
        this.progressDataUnMatched.errItems.push(item)
        return
      }

      this.progressDataUnMatched.searchCnt = count
      this.progressDataUnMatched.isProgress = true
      this.progressDataUnMatched.resultCnt += 1
    })
  },
  beforeDestroy () {
    eventBus.$off('dashboard')
    eventBus.$off('productProgress')
    eventBus.$off('productProgressUnMatched')
  },
  watch: {
    'progressData.resultCnt': {
      deep: true,
      handler (newVal, oldVal) {
        if (oldVal !== undefined) { // 페이지 로딩시 무조건 값 세팅되는것 방지
          console.log('progressData.resultCnt 바뀜')
          console.log(newVal + '   ' + oldVal)
          if (newVal === 0) {
            this.$parent.isLoading = true
          } else {
            this.$parent.isLoading = false
          }
        }
      }
    },
    'progressDataUnMatched.resultCnt': {
      deep: true,
      handler (newVal, oldVal) {
        if (oldVal !== undefined) { // 페이지 로딩시 무조건 값 세팅되는것 방지
          console.log('progressDataUnMatched.resultCnt 바뀜')
          if (newVal === 0) {
            this.$parent.isLoading = true
          } else {
            this.$parent.isLoading = false
          }
        }
      }
    }
  },
  methods: {
    get (searchParam) {
      let url = '/smartstore/productmatch/dashboard'

      http
        .get(url, {
          params: { // query string
            mall_id: searchParam.mall_id,
            mall_no: searchParam.mall_no
          }
        })
        .then(response => {
          this.responseData = response.data.data
        })
        .catch(e => {

        })
        .finally(() => {
        })
    },
    formatToPrice (value) {
      return Number(value).toFixed(0)
    }
  },
  props: {
    'userMenuAuth': Object
  }
}

</script>
<style scoped>
/** 첫번째 대쉬보드 */
.content.main .summaryWrap > ul > li:first-child {
    width: 30% !important;
}
.content.main .summaryWrap > ul > li:first-child li {
    width: 20% !important;
}
/* .content.main .summaryWrap > ul > li:not(:first-child) {
    width: 35%;
    margin-left: 2%
} */

/** 두번째 대쉬보드 */
.content.main .summaryWrap > ul > li:last-child {
    width: 50%;
}
.content.main .summaryWrap > ul > li:last-child li {
    width: 11.00%;
}
.content.main .summaryWrap > ul > li li p {
    padding: 5px 0 0px;
    font-size: 25px;
}
.content.main .summaryWrap > ul > li li {
    height: 80px;
    padding: 5px;
    width: 20%;
}

.content.main .summaryWrap .title {
  text-align: center;
  background: #f8f8f8;
}

.timeDashboard {
    float: right;
}

</style>
