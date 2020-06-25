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
          <option :value=0>상품등록일(자사몰)</option>
          <option :value=1>상품수정일(자사몰)</option>
          <option :value=2>상품등록일(스마트스토어)</option>
          <option :value=1>상품수정일(스마트스토어)</option>
      </select>
    </td>
    <td style="border-top: 0px;" colspan="4">
      <div >
          <SmartDatepickerDate
          v-bind:propFromDate="searchParam.fromDate"
          :propActiveBtn="searchParam.activeBtn"
          ref="dateMallSearch"
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
    <th style="border-top: 0px;">자사몰 상품명(상품번호)</th>
    <td style="border-top: 0px;" colspan="1">
      <input type="text" value="" v-model="searchParam.product_name"  @keyup.enter="$parent.getChild(1)"
          placeholder="상품명"/>
    </td>
    <td style="border-top: 0px;" colspan="1">
      <input type="text" value="" v-model="searchParam.product_no"  @keyup.enter="$parent.getChild(1)"
          placeholder="상품번호"/>
    </td>
    <th style="border-top: 0px;">스마트스토어 상품명</th>
    <td style="border-top: 0px;" colspan="1">
      <input type="text" value="" v-model="searchParam.smartstore_product_name"  @keyup.enter="$parent.getChild(1)"
        placeholder="상품명"/>
    </td>
    <td style="border-top: 0px;" colspan="1">
      <input type="text" value="" v-model="searchParam.smartstore_product_no"  @keyup.enter="$parent.getChild(1)"
        placeholder="상품번호"/>
    </td>
  </tr>
  <tr>
    <th>자사몰 판매여부</th>
    <td colspan="2">
      <div class="inputContainer">
        <span class="inputWrap">
          <input type="radio" name="serviceB01" id="serviceB01-01"
            :value="''"
            v-model="searchParam.display_yn">
          <label for="serviceB01-01">전체</label>
        </span>
        <span class="inputWrap">
          <input type="radio" name="serviceB01" id="serviceB01-02"
            :value=1
            v-model="searchParam.display_yn">
          <label for="serviceB01-02">판매중</label>
        </span>
        <span class="inputWrap">
          <input type="radio" name="serviceB01" id="serviceB01-03"
            :value=0
            v-model="searchParam.display_yn">
          <label for="serviceB01-03">미판매</label>
        </span>
      </div>
    </td>
    <th>스마트스토어 판매(전시) 여부</th>
    <td colspan="2">
      <span class="inputWrap">
        <input type="checkbox" id="serviceB05-00" :value=0 v-model="searchParam.smartstore_display_sellArr" @input="statsClickAll($event)" >
        <label for="serviceB05-00">전체</label>
      </span>
      <span class="inputWrap">
        <input type="checkbox" id="serviceB05-01" :value=1 v-model="searchParam.smartstore_display_sellArr" >
        <label for="serviceB05-01">판매중</label>
      </span>
      <span class="inputWrap">
        <input type="checkbox" id="serviceB05-02" :value=2 v-model="searchParam.smartstore_display_sellArr" >
        <label for="serviceB05-02">미판매</label>
      </span>
      <span class="inputWrap">
        <input type="checkbox" id="serviceB05-03" :value=3 v-model="searchParam.smartstore_display_sellArr" >
        <label for="serviceB05-03">전시중</label>
      </span>
      <span class="inputWrap">
        <input type="checkbox" id="serviceB05-04" :value=4 v-model="searchParam.smartstore_display_sellArr" >
        <label for="serviceB05-04">미전시</label>
      </span>
    </td>
  </tr>
  <tr>
    <th>자사몰 판매(할인)가</th>
    <td >
      <div style="display: inline-block;">
        <span class="inputWrap">
          <input type="checkbox" id="serviceB04-01" v-model="searchParam.product_price_check">
          <label for="serviceB04-01">판매가</label>
        </span>
        <br>
        <template v-if="searchParam.product_price_check">
          <input type="text" :value="searchParam.product_price_start | currency" @input="$parent.addComma(searchParam, 'product_price_start')"
                  style="width: 40%;"  />
          <input type="text" v-model="searchParam.product_price_start"
            style="display: none;"  />
          ~
          <input type="text" :value="searchParam.product_price_end | currency" @input="$parent.addComma(searchParam, 'product_price_end')"
                style="width: 40%;"  />
          <input type="text" v-model="searchParam.product_price_end"
            style="display: none;"/>
        </template>
        <template v-else>
          <input type="text" :value="searchParam.product_price_start | currency" disabled
            style="width:40%"  />
          ~
          <input type="text" :value="searchParam.product_price_end | currency" disabled
            style="width:40%"/>
        </template>
      </div>
    </td>
    <td>
        <div style="display: inline-block;">
          <span class="inputWrap">
            <input type="checkbox" id="serviceB04-02" v-model="searchParam.product_pc_discount_price_check">
            <label for="serviceB04-02">할인가</label>
          </span>
          <br>
          <template v-if="searchParam.product_pc_discount_price_check">
            <input type="text" :value="searchParam.product_pc_discount_price_start | currency" @input="$parent.addComma(searchParam, 'product_pc_discount_price_start')"
                  style="width: 40%;"  />
            <input type="text" v-model="searchParam.product_pc_discount_price_start"
              style="display:none;" />
            ~
            <input type="text" :value="searchParam.product_pc_discount_price_end | currency" @input="$parent.addComma(searchParam, 'product_pc_discount_price_end')"
                style="width: 40%;"  />
            <input type="text" v-model="searchParam.product_pc_discount_price_end"
              style="display:none;" />
          </template>
          <template v-else>
            <input type="text" v-model="searchParam.product_pc_discount_price_start" disabled
              style="width:40%" />
              ~
            <input type="text" v-model="searchParam.product_pc_discount_price_end" disabled
              style="width:40%" />
          </template>

      </div>
    </td>
    <th>스마트스토어 판매(할인)가</th>
    <td>
      <span class="inputWrap">
        <input type="checkbox" id="serviceB04-03" v-model="searchParam.smart_product_price_check">
        <label for="serviceB04-03">판매가</label>
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
        <input type="checkbox" id="serviceB04-04" v-model="searchParam.smart_product_pc_discount_price_check" >
        <label for="serviceB04-04">할인가</label>
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
  </tr>
  <tr>
    <th>카테고리</th>
    <td>
      <select v-model="categorySearchType" @change="changeCategorySearchType($event)">
          <option :value=0>자사몰 카테고리</option>
          <option :value=1>스마트스토어 카테고리</option>
      </select>
    </td>
    <template v-if="categorySearchType === 0">
      <td colspan="4">
        <div class="vue-select inline-display">
          <v-select
            id="firstSelect"
            :get-option-label="getLabel"
            :options="firthDepth"
            ref='firstSelect'
            @input="selectCategory"
            placeholder="대분류 선택"
          >
            <span slot="no-options" >
              검색된 내용이 없습니다.
            </span>

          </v-select>
        </div>
        <div class="vue-select inline-display" >
          <v-select
            :get-option-label="getLabel"
            :options="secondDepth"
            ref='secondSelect'
            @input="selectCategory"
            placeholder="중분류 선택"
          >
            <span slot="no-options" >
              검색된 내용이 없습니다.
            </span>
          </v-select>
        </div>
        <div class="vue-select inline-display" >
          <v-select
            :get-option-label="getLabel"
            :options="thirdDepth"
            ref='thirdSelect'
            @input="selectCategory"
            placeholder="소분류 선택"
          >
            <span slot="no-options" >
              검색된 내용이 없습니다.
            </span>
          </v-select>
        </div>
        <div class="vue-select inline-display">
          <v-select
            :get-option-label="getLabel"
            :options="fourthDepth"
            ref='fourthSelect'
            @input="selectCategory"
            placeholder="세분류 선택"
          >
            <span slot="no-options" >
              검색된 내용이 없습니다.
            </span>

          </v-select>
        </div>
      </td>
    </template>
    <template v-else-if="categorySearchType === 1">
      <td colspan="4">
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
    </template>
  </tr>
</tbody>
</table>
</template>
<script>
import SmartDatepickerDate from '../../SmartDatepickerDate'
import ModalAlert from '@/components/common/modal/ModalAlert'
import http from '@/http-commons'
import { eventBus } from '@/main'
import 'vue-select/dist/vue-select.css'

export default {
  name: 'MallSearch',
  components: {
    SmartDatepickerDate
  },
  data () {
    return {
      categorySearchType: 0,
      list: [],
      firthDepth: [],
      secondDepth: [],
      thirdDepth: [],
      fourthDepth: [],
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
  created () {
    // 필터 열기 버튼 통해서 v-if 로 렌데링될때 호출
    // this.getCategoryList()
    // this.getCategoryDepth()
  },
  mounted () {
    console.log('mounted')
    this.getCategoryList()
    this.getCategoryDepth()
    eventBus.$on('mallCategoryReset', () => {
      // 자사몰 명 변경 및 탭 변경시 호출
      this.getCategoryList()
      this.resetVueSelect(1)
    })
    eventBus.$on('dateMallSearchReset', () => {
      this.$refs.dateMallSearch.$refs.openFromDate.clearDate()
    })
  },
  beforeDestroy () {
    eventBus.$off('mallCategoryReset')
    eventBus.$off('dateMallSearchReset')
  },
  methods: {
    getLabel: function (val) {
      if (typeof val === 'object') {
        return val.category_name + ' [' + val.category_no + ']'
      } else {
        return this.options.filter(function (option) {
          return option.category_name === val
        })[0].category_name
      }
    },
    changeCategorySearchType () {
      console.log('changeCategorySearchType', this.categorySearchType)
      this.resetVueSelect(2)
    },
    /**
     * turnFlag 값 정의
     * reset(초기화버튼) : 1
     * change(셀렉트박스 변경) : 2
     */
    resetVueSelect (turnFlag) {
      if (turnFlag === 1) { // reset(초기화버튼)
        if (this.categorySearchType === 0) {
          this.$refs.firstSelect.clearSelection()
          this.$refs.secondSelect.clearSelection()
          this.$refs.thirdSelect.clearSelection()
          this.$refs.fourthSelect.clearSelection()
        } else if (this.categorySearchType === 1) {
          this.$refs.firstSmartSelect.clearSelection()
          this.$refs.secondSmartSelect.clearSelection()
          this.$refs.thirdSmartSelect.clearSelection()
          this.$refs.fourthSmartSelect.clearSelection()
        }
      } else if (turnFlag === 2) { // change(셀렉트박스 변경)
        if (this.categorySearchType === 1) {
          this.$refs.firstSelect.clearSelection()
          this.$refs.secondSelect.clearSelection()
          this.$refs.thirdSelect.clearSelection()
          this.$refs.fourthSelect.clearSelection()
        } else if (this.categorySearchType === 0) {
          this.$refs.firstSmartSelect.clearSelection()
          this.$refs.secondSmartSelect.clearSelection()
          this.$refs.thirdSmartSelect.clearSelection()
          this.$refs.fourthSmartSelect.clearSelection()
        }
      }
    },
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
     * 자사몰 카테고리 전체 리스트
     */
    getCategoryList () {
      console.log('MallSearch getCategoryList')
      let url = '/smartstore/productmatch/list/category'

      http
        .get(url, {
          params: { // query string
            mall_id: this.$store.state.searchParam.mall_id
          }
        })
        .then(response => {
          // this.resetVueSelect()
          this.list = response.data.data
          this.firthDepth = []
          this.secondDepth = []
          this.thirdDepth = []
          this.fourthDepth = []
          for (const item of this.list) {
            if (item.category_depth_no === 1) {
              this.firthDepth.push(item)
            } else if (item.category_depth_no === 2) {
              this.secondDepth.push(item)
            } else if (item.category_depth_no === 3) {
              this.thirdDepth.push(item)
            } else if (item.category_depth_no === 4) {
              this.fourthDepth.push(item)
            }
          }
        })
        .catch(e => {

        })
        .finally(() => {
        })
    },
    /**
     * 자사몰 카테고리 선택
     */
    selectCategory (value) {
      console.log('selectCategory', value)
      if (value === null) {
        this.searchParam.product_category_no = ''
      } else {
        if (value.category_depth_no === 1) {
          this.$refs.secondSelect.clearSelection()
          this.$refs.thirdSelect.clearSelection()
          this.$refs.fourthSelect.clearSelection()
        } else if (value.category_depth_no === 2) {
          this.$refs.firstSelect.clearSelection()
          this.$refs.thirdSelect.clearSelection()
          this.$refs.fourthSelect.clearSelection()
        } else if (value.category_depth_no === 3) {
          this.$refs.firstSelect.clearSelection()
          this.$refs.secondSelect.clearSelection()
          this.$refs.fourthSelect.clearSelection()
        } else if (value.category_depth_no === 4) {
          this.$refs.firstSelect.clearSelection()
          this.$refs.secondSelect.clearSelection()
          this.$refs.thirdSelect.clearSelection()
        }
        this.searchParam.product_category_no = value.category_no
        this.$parent.getChild(1)
      }
    },
    /**
     * 스마트스토어 카테고리 1depth 조회
     */
    getCategoryDepth () {
      let url = '/smartstore/categorydepth'

      http
        .get(url)
        .then(response => {
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
  width: 23%;
  margin-right: 5px;
}
</style>
