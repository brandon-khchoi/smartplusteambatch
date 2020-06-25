<template>
<table summary="상품관리">
  <caption class="blind">상품관리</caption>
  <colgroup>
  <col style="width:200px"/>
  <col/>
  <col/>
  <col style="width:200px"/>
  <col/>
  <col/>
</colgroup>
<tbody>
  <tr>
    <th style="border-top: 0px;">기간</th>
    <td style="border-top: 0px;">
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
          ref="dateMallUnMatchedSearch"
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
    <th>자사몰 상품명(상품번호)</th>
    <td>
      <input type="text" value="" v-model="searchParam.product_name"  @keyup.enter="$parent.getChild(1)"
        placeholder="상품명" />
    </td>
    <td>
      <input type="text" value="" v-model="searchParam.product_no"  @keyup.enter="$parent.getChild(1)"
        placeholder="상품번호" />
    </td>
    <th>자사몰 판매여부</th>
    <td colspan="2">
      <div class="inputContainer">
        <span class="inputWrap">
          <input type="radio" name="serviceA01" id="serviceA01-01"
            :value="''"
            v-model="searchParam.display_yn">
          <label for="serviceA01-01">전체</label>
        </span>
        <span class="inputWrap">
          <input type="radio" name="serviceA01" id="serviceA01-02"
            :value=1
            v-model="searchParam.display_yn">
          <label for="serviceA01-02">판매중</label>
        </span>
        <span class="inputWrap">
          <input type="radio" name="serviceA01" id="serviceA01-03"
            :value=0
            v-model="searchParam.display_yn">
          <label for="serviceA01-03">미판매</label>
        </span>
      </div>
    </td>
  </tr>
  <tr>
    <th>자사몰 판매(할인)가</th>
    <td >
      <div style="display: inline-block;">
        <span class="inputWrap">
          <input type="checkbox" id="serviceA04-01" v-model="searchParam.product_price_check">
          <label for="serviceA04-01">판매가</label>
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
            <input type="checkbox" id="serviceA04-02" v-model="searchParam.product_pc_discount_price_check">
            <label for="serviceA04-02">할인가</label>
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
    <td colspan="3"></td>
  </tr>
  <tr>
    <th>미연동상품 리스트 노출 상태
    </th>
    <td colspan="2">
      <div class="inputContainer">
        <span class="inputWrap">
          <input type="radio" name="serviceA02" id="serviceA02-01"
            :value="''"
            v-model="searchParam.hide_yn">
          <label for="serviceA02-01">전체</label>
        </span>
        <span class="inputWrap">
          <input type="radio" name="serviceA02" id="serviceA02-02"
            :value=0
            v-model="searchParam.hide_yn">
          <label for="serviceA02-02">노출</label>
        </span>
        <span class="inputWrap">
          <input type="radio" name="serviceA02" id="serviceA02-03"
            :value=1
            v-model="searchParam.hide_yn">
          <label for="serviceA02-03">숨김</label>
        </span>
      </div>
    </td>
    <th>사이즈 이미지
    </th>
    <td colspan="2">
      <div class="inputContainer">
        <span class="inputWrap">
          <input type="radio" name="serviceA03" id="serviceA03-01"
            :value="''"
            v-model="searchParam.size_image_yn">
          <label for="serviceA03-01">전체</label>
        </span>
        <span class="inputWrap">
          <input type="radio" name="serviceA03" id="serviceA03-02"
            :value=1
            v-model="searchParam.size_image_yn">
          <label for="serviceA03-02">있음</label>
        </span>
        <span class="inputWrap">
          <input type="radio" name="serviceA03" id="serviceA03-03"
            :value=0
            v-model="searchParam.size_image_yn">
          <label for="serviceA03-03">없음</label>
        </span>
      </div>
    </td>
  </tr>
  <tr>
    <th>카테고리</th>
    <td colspan="5">
      <div class="vue-select inline-display">
        <v-select label="category_name"
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
      <div class="vue-select inline-display">
        <!-- label="category_name" -->
        <v-select
          :options="secondDepth"
          ref='secondSelect'
          :get-option-label="getLabel"
          @input="selectCategory"
          placeholder="중분류 선택"
        >
          <span slot="no-options" >
            검색된 내용이 없습니다.
          </span>
        </v-select>
      </div>
      <div class="vue-select inline-display">
        <v-select
          :options="thirdDepth"
          :get-option-label="getLabel"
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
  </tr>
</tbody>
</table>

</template>
<script>
import SmartDatepickerDate from '../../SmartDatepickerDate'
import http from '@/http-commons'
import { eventBus } from '@/main'
import 'vue-select/dist/vue-select.css'

// 스마트스토어 미연동상품 탭 검색창
export default {
  name: 'MallUnMatchedSearch',
  components: {
    SmartDatepickerDate
  },
  data () {
    return {
      list: [],
      firthDepth: [],
      secondDepth: [],
      thirdDepth: [],
      fourthDepth: []
    }
  },
  created () {
    // 필터 열기 버튼 통해서 v-if 로 렌데링될때 호출
    // this.getCategoryList()
  },
  mounted () {
    console.log('mounted')
    this.getCategoryList()
    eventBus.$on('mallUnMatchedCategoryReset', () => {
      // 자사몰 명 변경 및 탭 변경시 호출
      this.getCategoryList()
    })
    eventBus.$on('dateMallUnMatchedSearchReset', () => {
      this.$refs.dateMallUnMatchedSearch.$refs.openFromDate.clearDate()
    })
  },
  beforeDestroy () {
    eventBus.$off('mallUnMatchedCategoryReset')
    eventBus.$off('dateMallUnMatchedSearchReset')
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
    setActiveBtn (value) {
      this.searchParam.activeBtn = value
    },
    setFromDate (value) {
      this.searchParam.fromDate = value
    },
    setToDate (value) { /** */
      console.log('setToDate', value)
      this.searchParam.toDate = value
    },
    getCategoryList () {
      console.log('MallUnMatchedSearch getCategoryList')
      let url = '/smartstore/productmatch/list/category'

      http
        .get(url, {
          params: { // query string
            mall_id: this.$store.state.searchParam.mall_id
          }
        })
        .then(response => {
          this.$refs.firstSelect.clearSelection()
          this.$refs.secondSelect.clearSelection()
          this.$refs.thirdSelect.clearSelection()
          this.$refs.fourthSelect.clearSelection()
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
    }

  },
  props: {
    'searchParam': Object,
    'setCategory': Function
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
