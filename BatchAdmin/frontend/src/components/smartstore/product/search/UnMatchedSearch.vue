<template>
<table summary="상품관리">
  <caption class="blind">상품관리</caption>
  <colgroup>
  <col style="width:200px"/>
  <col/>
  <col />
  <col style="width:200px"/>
  <col/>
  <col />
</colgroup>
<tbody>
  <tr>
    <th style="border-top: 0px;">기간</th>
    <td style="border-top: 0px;" colspan="1">
      <select v-model="searchParam.searhDateFlag">
          <option :value=0>상품등록일</option>
          <option :value=1>상품수정일</option>
      </select>
    </td>
    <td style="border-top: 0px;" colspan="4">
      <div >
          <SmartDatepickerDate
          v-bind:propFromDate="searchParam.fromDate"
          :propActiveBtn="searchParam.activeBtn"
          ref="dateUnMatchedSearch"
          @updateActiveBtn="setActiveBtn"
          @updateFromDate="setFromDate"
          @updateToDate="setToDate"
          >
          <!-- 자식이 부모한테 보낼때 v-on 및 메소드명을 뒤에 사용해서 올려줌 -->
          </SmartDatepickerDate>
      </div>
    </td>
  </tr>
  <tr>
    <th style="border-top: 0px;">스마트스토어 상품명(상품번호)</th>
    <td style="border-top: 0px;" colspan="1">
      <input type="text" value="" v-model="searchParam.smartstore_product_name"  @keyup.enter="$parent.getChild(1, searchParam.per_page)"
        placeholder="상품명"/>
    </td>
    <td style="border-top: 0px;" colspan="1">
      <input type="text" value="" v-model="searchParam.smartstore_product_no"  @keyup.enter="$parent.getChild(1, searchParam.per_page)"
        placeholder="상품번호" />
    </td>
    <th>스마트스토어 판매(전시) 여부</th>
    <td colspan="2">
      <span class="inputWrap">
        <input type="checkbox" id="serviceC05-00" :value=0 v-model="searchParam.smartstore_display_sellArr" @input="statsClickAll($event)" >
        <label for="serviceC05-00">전체</label>
      </span>
      <span class="inputWrap">
        <input type="checkbox" id="serviceC05-01" :value=1 v-model="searchParam.smartstore_display_sellArr" >
        <label for="serviceC05-01">판매중</label>
      </span>
      <span class="inputWrap">
        <input type="checkbox" id="serviceC05-02" :value=2 v-model="searchParam.smartstore_display_sellArr" >
        <label for="serviceC05-02">미판매</label>
      </span>
      <span class="inputWrap">
        <input type="checkbox" id="serviceC05-03" :value=3 v-model="searchParam.smartstore_display_sellArr" >
        <label for="serviceC05-03">전시중</label>
      </span>
      <span class="inputWrap">
        <input type="checkbox" id="serviceC05-04" :value=4 v-model="searchParam.smartstore_display_sellArr" >
        <label for="serviceC05-04">미전시</label>
      </span>
    </td>
  </tr>
  <tr>
    <th>스마트스토어 판매(할인)가</th>
    <td>
      <span class="inputWrap">
        <input type="checkbox" id="serviceC04-03" v-model="searchParam.smart_product_price_check">
        <label for="serviceC04-03">판매가</label>
      </span>
      <br>
      <template v-if="searchParam.smart_product_price_check">
        <input type="text" :value="searchParam.smart_product_price_start | currency" @input="$parent.addComma(searchParam, 'smart_product_price_start')"
                style="width: 40%;"  />
        <input type="text" v-model="searchParam.smart_product_price_start"
          style="display:none;" />
        ~
        <input type="text" :value="searchParam.smart_product_price_end | currency" @input="$parent.addComma(searchParam, 'smart_product_price_end')"
                style="width: 40%;"  />
        <input type="text" v-model="searchParam.smart_product_price_end"
          style="display:none;" />
        </template>
        <template v-else>
          <input type="text" v-model="searchParam.smart_product_price_start" disabled
            style="width:40%" />
          ~
          <input type="text" v-model="searchParam.smart_product_price_end" disabled
            style="width:40%" />
      </template>
    </td>
    <td>
      <span class="inputWrap">
        <input type="checkbox" id="serviceC04-04" v-model="searchParam.smart_product_pc_discount_price_check" >
        <label for="serviceC04-04">할인가</label>
      </span>
      <br>
      <template v-if="searchParam.smart_product_pc_discount_price_check">
        <input type="text" :value="searchParam.smart_product_pc_discount_price_start | currency" @input="$parent.addComma(searchParam, 'smart_product_pc_discount_price_start')"
                style="width: 40%;"  />
        <input type="text" v-model="searchParam.smart_product_pc_discount_price_start"
          style="display:none;"  />
        ~
        <input type="text" :value="searchParam.smart_product_pc_discount_price_end | currency" @input="$parent.addComma(searchParam, 'smart_product_pc_discount_price_end')"
                style="width: 40%;"  />
        <input type="text" v-model="searchParam.smart_product_pc_discount_price_end"
          style="display:none;" />
      </template>
      <template v-else>
        <input type="text" v-model="searchParam.smart_product_pc_discount_price_start" disabled
          style="width:40%"  />
        ~
        <input type="text" v-model="searchParam.smart_product_pc_discount_price_end" disabled
          style="width:40%" />
      </template>
    </td>
    <td colspan="3">
    </td>
  </tr>
  <tr>
    <th>카테고리</th>
    <td colspan="5">
      <div class="vue-select inline-display">
        <v-select
          label="category_name"
          :options="depthData"
          ref='firstSmartSelect'
          @input="getDepthDataSecond"
          placeholder="대분류 선택"
        >
          <span slot="no-options" >
            검색된 내용이 없습니다.
          </span>

        </v-select>
      </div>
      <div class="vue-select inline-display" >
        <v-select
          label="category_name"
          :options="item.depth2Data"
          :disabled="item.depth2Data.length === 0 "
          ref='secondSmartSelect'
          @input="getDepthDataThird"
          placeholder="중분류 선택"
        >
          <span slot="no-options" >
            검색된 내용이 없습니다.
          </span>
        </v-select>
      </div>
      <div class="vue-select inline-display" >
        <v-select
          label="category_name"
          :options="item.depth3Data"
          :disabled="item.depth3Data.length === 0 "
          ref='thirdSmartSelect'
          @input="getDepthDataFourth"
          placeholder="소분류 선택"
        >
          <span slot="no-options" >
            검색된 내용이 없습니다.
          </span>
        </v-select>
      </div>
      <div class="vue-select inline-display">
        <v-select
          label="category_name"
          :options="item.depth4Data"
          :disabled="item.depth4Data.length === 0 "
          ref='fourthSmartSelect'
          @input="getDepthDataFinal"
          placeholder="세분류 선택"
        >
          <span slot="no-options" >
            검색된 내용이 없습니다.
          </span>

        </v-select>
      </div>
    </td>
  </tr>

</tbody>
</table>
</template>
<script>
import SmartDatepickerDate from '../../SmartDatepickerDate'
import ModalAlert from '@/components/common/modal/ModalAlert'
import { eventBus } from '@/main'
import http from '@/http-commons'
import 'vue-select/dist/vue-select.css'

export default {
  name: 'UnMatchedSearch',
  components: {
    SmartDatepickerDate
  },
  data () {
    return {
      depthData: [], // 스마트스토어 카테고리 1depth 데이터
      item: { // 스마트스토어 카테고리 select박스 선택된 데이터
        depth1_category_no: '0',
        depth2_category_no: '0',
        depth3_category_no: '0',
        depth4_category_no: '0',
        depth2Data: [],
        depth3Data: [],
        depth4Data: []
      }
    }
  },
  mounted () {
    this.getCategoryDepth()
    eventBus.$on('unMatchedCategoryReset', () => {
      // 자사몰 명 변경 및 탭 변경시 호출
      console.log('mallCategoryReset event')
      this.$refs.firstSmartSelect.clearSelection()
      this.$refs.secondSmartSelect.clearSelection()
      this.$refs.thirdSmartSelect.clearSelection()
      this.$refs.fourthSmartSelect.clearSelection()
    })
    eventBus.$on('dateUnMatchedSearchReset', () => {
      this.$refs.dateUnMatchedSearch.$refs.openFromDate.clearDate()
    })
  },
  beforeDestroy () {
    eventBus.$off('unMatchedCategoryReset')
    eventBus.$off('dateUnMatchedSearchReset')
  },
  methods: {
    setActiveBtn (value) {
      this.searchParam.activeBtn = value
    },
    setFromDate (value) {
      this.searchParam.fromDate = value
    },
    setToDate (value) {
      this.searchParam.toDate = value
    },
    /**
     * 스마트스토어 카테고리 1depth 조회
     */
    getCategoryDepth () {
      let url = '/smartstore/categorydepth'

      http
        .get(url)
        .then(response => {
          this.depthData.push({category_name: '대분류'})
          this.depthData = response.data
        })
        .catch(e => {
          // console.log(e)
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          )
        })
    },
    /**
     * 스마트스토어 첫번째 select박스 호출
     */
    getDepthDataSecond (value) {
      if (value === null) {
        this.item.depth1_category_no = '0'
        this.item.depth2_category_no = '0'
        this.item.depth3_category_no = '0'
        this.item.depth4_category_no = '0'
        // this.item.depth1_smart_category_no = smartCategoryNo
        this.item.depth2Data = []
        this.item.depth3Data = []
        this.item.depth4Data = []
      }
      if (value) {
        this.getDepthDataAPI(value)
      }
    },
    /**
     * 스마트스토어 두번째 select박스 호출
     */
    getDepthDataThird (value) {
      if (value === null) {
        this.item.depth2_category_no = '0'
        this.item.depth3_category_no = '0'
        this.item.depth4_category_no = '0'
        this.item.depth3Data = []
        this.item.depth4Data = []
      }
      if (value) {
        this.getDepthDataAPI(value)
      }
    },
    /**
     * 스마트스토어 세번째 select박스 호출
     */
    getDepthDataFourth (value) {
      if (value === null) {
        this.item.depth3_category_no = '0'
        this.item.depth4_category_no = '0'
        this.item.depth4Data = []
      }
      if (value) {
        this.getDepthDataAPI(value)
      }
    },
    /**
     * 스마트스토어 마지막 select박스 호출
     */
    getDepthDataFinal (value) {
      if (value === null) {
        this.item.depth4_category_no = '0'
      }
      if (value) {
        this.getDepthDataAPI(value)
      }
    },
    getDepthDataAPI (value) {
      let categoryNo = '' // 스마트 스토어에서 가져온 키 값(외부업체 키 값)
      let smartCategoryNo = '' // 스마트 스토어 db 의 pk(우리 키 값)
      categoryNo = value.category_no
      smartCategoryNo = value.store_category_no
      if (value.category_depth_no === 1) {
        this.item.depth1_category_no = categoryNo
        this.item.depth1_smart_category_no = smartCategoryNo
      } else if (value.category_depth_no === 2) {
        this.item.depth2_category_no = categoryNo
        this.item.depth2_smart_category_no = smartCategoryNo
      } else if (value.category_depth_no === 3) {
        this.item.depth3_category_no = categoryNo
        this.item.depth3_smart_category_no = smartCategoryNo
      } else if (value.category_depth_no === 4) {
        this.item.depth4_category_no = categoryNo
        this.item.depth4_smart_category_no = smartCategoryNo
      }

      let url = '/smartstore/categorydepthbyno'
      http
        .get(url, {
          params: { // query string
            category_no: categoryNo
          }
        })
        .then(response => {
          if (value.category_depth_no === 1) {
            this.item.depth2Data = response.data
            this.item.depth3Data = []
            this.item.depth4Data = []
            this.$refs.secondSmartSelect.clearSelection()
            this.$refs.thirdSmartSelect.clearSelection()
            this.$refs.fourthSmartSelect.clearSelection()
            this.item.depth2_category_no = '0' // 뒤에 있는 select box 초기화
            this.item.depth3_category_no = '0'
            this.item.depth4_category_no = '0'
          } else if (value.category_depth_no === 2) {
            this.item.depth3Data = response.data
            this.item.depth4Data = []
            this.$refs.thirdSmartSelect.clearSelection()
            this.$refs.fourthSmartSelect.clearSelection()
            this.item.depth3_category_no = '0'
            this.item.depth4_category_no = '0'
          } else if (value.category_depth_no === 3) {
            this.$refs.fourthSmartSelect.clearSelection()
            this.item.depth4Data = response.data
            this.item.depth4_category_no = '0'
          }

          this.$forceUpdate() // 없으면 하위 depth 데이터가 화면에 바로 반영 안됨
          this.searchParam.categoryItem = this.item
        })
        .catch(e => {
          // console.log(e)
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' }, { width: 450, height: 'auto' }
          )
        })
    },
    statsClickAll (event) {
      // 전체 선택
      if (event.target.checked === true) {
        this.searchParam.smartstore_display_sellArr = []
        this.searchParam.smartstore_display_sellArr.push(1, 2, 3, 4)
      } else {
        this.searchParam.smartstore_display_sellArr = []
      }
    }
  },
  props: {
    'searchParam': Object
  }
}
</script>
<style scoped>
.searchWrap table tr:nth-child(n+3) {
    display: table-row !important;
  }
.vue-select >>> .vs__dropdown-toggle {
    border-radius: 0px;
    height: 36px;
}
.inline-display {
  display: inline-block;
  width: 20%;
  margin-right: 5px;
}
</style>
