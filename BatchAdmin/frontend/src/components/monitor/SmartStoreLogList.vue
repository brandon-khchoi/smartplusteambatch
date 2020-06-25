<template>
  <div class="content">
    <div class="formContainer">
      <div class="searchWrap">
        <!-- <CommonServiceList :propMallNoQuery= $route.query.mall_no></CommonServiceList> -->
        <table summary="스마트스토어 로그 조회">
          <caption class="blind">스마트스토어 로그 조회</caption>
          <colgroup>
                <col style="width:100px"/>
                <col width="width:calc(25% - 100px)"/>
                <col style="width:100px"/>
                <col style="width:calc(25% - 100px)"/>
                <col style="width:100px"/>
                <col style="width:calc(25% - 100px)"/>
                <col style="width:100px"/>
                <col/>
            </colgroup>
          <tbody>
             <tr>
                <th>로그일자</th>
                <td colspan="3">
                    <div >
                        <DatepickerDate
                        v-bind:propFromDate="fromDate"
                        v-bind:propToDate="toDate"
                        :propsSetBtn="1"
                        v-on:update:fromDate="setFromDate"
                        v-on:update:toDate="setToDate"
                        >
                        <!-- 자식이 부모한테 보낼때 v-on 및 메소드명을 뒤에 사용해서 올려줌 -->
                        </DatepickerDate>
                    </div>
                </td>
                <th>
                  업체명
                </th>
                <td colspan="3">
                  <div class="vue-select">
                    <v-select
                      label="mall_name"
                      multiple
                      taggable
                      :options="productLinkServiceInfo"
                      @input="selectMallInfoVal"
                      placeholder="업체명 검색"
                    >
                      <span slot="no-options" >
                        검색된 내용이 없습니다.
                      </span>

                      <template v-slot:option="option">

                        <div style="display:inline;">
                          {{ option.mall_name }}
                        </div>
                        <div style="float:right;">
                        <img v-if="option.hosting_no === 1" src="https://lunaplus.lunasoft.co.kr/images/lunachat/icon/cafe24/logo-button.png" alt="카페24"/>
                        <img v-if="option.hosting_no === 2" src="https://lunaplus.lunasoft.co.kr/images/lunachat/icon/makeshop/logo-button.png" alt="메이크샵"/>
                        </div>
                      </template>
                    </v-select>
                  </div>
              </td>
            </tr>
            <tr>

              <th>성공여부</th>
              <td>
                <select v-model="searchParam.success_yn"  @change="get()">
                <option value=''>전체</option>
                <option :value=0>실패</option>
                <option :value=1>성공</option>
                </select>
              </td>
              <th>log_user_code</th>
              <td>
                <div class="vue-select">
                  <v-select
                    multiple
                    taggable
                    :options="logUserCodeList"
                    v-model="searchParam.log_user_codes"
                    placeholder="user_code"
                  >
                    <span slot="no-options" >
                      검색된 내용이 없습니다.
                    </span>

                  </v-select>
                </div>
              </td>
              <th>log_request_id</th>
              <td>
                <input type="text" value="" v-model="searchParam.log_request_id"
                  @keyup.enter="get()"
                  placeholder="EX) 27CAF0FF9F9269ED2D90FEE186110ABE" />
              </td>
              <th>method_name</th>
              <td>
                <div class="vue-select">
                  <v-select
                    label="name"
                    taggable
                    :options="methodNameList"
                    v-model="searchParam.method_name"
                    @input="get()"
                    placeholder="EX) insertSmartStoreProduct"
                  >
                    <span slot="no-options" >
                      검색된 내용이 없습니다.
                    </span>
                    <template v-slot:option="option">
                      <div style="display:inline;">
                        {{ option.name }}
                      </div>
                      <div style="float:right;">
                         {{ option.desc }}
                      </div>
                    </template>
                  </v-select>
                </div>
              </td>
              </tr>
            <tr>
              <th>자사몰 상품번호</th>
              <td>
                <input type="text" value="" v-model="searchParam.data.product_no"
                  @keyup.enter="get()"
                  placeholder="EX) 121212" />
              </td>
              <th>스마트스토어<br>상품번호</th>
              <td>
                <input type="text" value="" v-model="searchParam.data.smartstore_product_no"
                  @keyup.enter="get()"
                  placeholder="EX) 1212121212" />
              </td>
              <th>log_text</th>
              <td colspan="3">
                <input type="text" value="" v-model="searchParam.log_text"
                  @keyup.enter="get()"
                  placeholder="LIKE SEARCH EX) SmartStoreProductServiceImpl" />
              </td>
            </tr>
            <tr>
              <th>data.</th>
              <td>
                <div class="vue-select">
                  <v-select
                    label="colname"
                    taggable
                    v-model="searchParam.data.anyDataName"
                    :options="dataColList"
                    placeholder="data컬럼"
                  >
                    <span slot="no-options" >
                      검색된 내용이 없습니다.
                    </span>

                    <template v-slot:option="option">

                      <div style="display:inline;">
                        {{ option.colname }}
                      </div>
                      <div style="float:right;">
                         {{ option.desc }}
                      </div>
                    </template>
                  </v-select>
                </div>
              </td>
              <td >
                <select v-model="searchParam.data.anyDataEquality" >
                <option :value=1>=</option>
                <option :value=2>></option>
                <option :value=3>>=</option>
                <option :value=4>&lt;</option>
                <option :value=5>&lt;=</option>
                <option :value=6>!=</option>
                <option :value=7>like(문자)</option>
                </select>
              </td>
              <td >
                <input type="text" value="" v-model="searchParam.data.anyData"
                  @keyup.enter="get()"
                  placeholder="EX) 값" />
              </td>

              <th>And data.</th>
              <td>
                <div class="vue-select">
                  <v-select
                    label="colname"
                    taggable
                    v-model="searchParam.data.anyData2Name"
                    :options="dataColList"
                    placeholder="data컬럼"
                  >
                    <span slot="no-options" >
                      검색된 내용이 없습니다.
                    </span>

                    <template v-slot:option="option">

                      <div style="display:inline;">
                        {{ option.colname }}
                      </div>
                      <div style="float:right;">
                         {{ option.desc }}
                      </div>
                    </template>
                  </v-select>
                </div>
              </td>
              <td >
                <select v-model="searchParam.data.anyData2Equality" >
                <option :value=1>=</option>
                <option :value=2>></option>
                <option :value=3>>=</option>
                <option :value=4>&lt;</option>
                <option :value=5>&lt;=</option>
                <option :value=6>!=</option>
                <option :value=7>like(문자)</option>
                </select>
              </td>
              <td >
                <input type="text" value="" v-model="searchParam.data.anyData2"
                  @keyup.enter="get()"
                  placeholder="EX) 값" />
              </td>
            </tr>
          </tbody>
        </table>
        <div class="btnWrap">
          <ul>
            <li>
              <p class="btn_search" @click="get()"><i class="fas fa-search"></i>검색</p>
            </li>
          </ul>
        </div>
      </div>
      <article class="searchResult" >
        <div class="articleWrap">
          <div class="tableTopWrap">
            <p class="total">결과 : 총 <span> {{totalCnt}} </span> 건 &nbsp; &nbsp; &nbsp; Filer는 현재페이지에서만 검색됩니다.</p>
          </div>
          <div class="bootstrapWrapper">

            <b-row class="font_size">
            <b-col lg="3" class="my-1">
            <b-form-group
              label="Filter"
              label-cols-sm="3"
              label-align-sm="right"
              label-size="sm"
              label-for="filterInput"
              class="mb-0 font_size"
            >
              <b-input-group size="sm" class="font_size">
                <b-form-input
                  v-model="filter"
                  type="search"
                  id="filterInput"
                  placeholder="Type to Search"
                ></b-form-input>
                <b-input-group-append>
                  <b-button :disabled="!filter" @click="filter = ''">Clear</b-button>
                </b-input-group-append>
              </b-input-group>
            </b-form-group>
            </b-col>
            <b-col lg="6" class="my-1">
              <div>
                <span style="margin-right:5px;">
                  Filter On
                </span>
                <b-form-checkbox-group v-model="filterOn" class="mt-1" style="display:inline;">
                  <b-form-checkbox value="log_date">log_date</b-form-checkbox>
                  <b-form-checkbox value="log_user_code">log_user_code</b-form-checkbox>
                  <b-form-checkbox value="mall_id">mall_id</b-form-checkbox>
                  <b-form-checkbox value="method_name">method_name</b-form-checkbox>
                  <b-form-checkbox value="success_yn">success_yn</b-form-checkbox>
                  <b-form-checkbox value="log_text">log_text</b-form-checkbox>
                  <b-form-checkbox value="log_request_id">log_request_id</b-form-checkbox>
                  <b-form-checkbox value="data">data</b-form-checkbox>
                </b-form-checkbox-group>
              </div>
            </b-col>
            </b-row>
            <b-row v-if="totalCnt > 0">
            <b-col md="6" class="my-1" align-v="end">
            <b-pagination
              v-model="currentPage"
              :total-rows="totalCnt"
              :per-page="perPage"
              :limit="7"
              aria-controls="my-table"
              prev-text="Prev"
              next-text="Next"
              last-text="Last"
              align="right"
            ></b-pagination>
            </b-col>
             <b-col md="4" class="my-1" align-v="end">
              <b-form-group
                label="Per page"
                label-cols-sm="2"
                label-align="right"
                label-for="perPageSelect"
                class="mb-0"
              >
                <b-form-select
                  v-model="perPage"
                  id="perPageSelect"
                  class="w-25"
                  :options="pageOptions"
                ></b-form-select>
              </b-form-group>
            </b-col>
            </b-row>
            <b-table
              striped
              id="my-table"
              :fixed="'fixed'"
              :items="list"
              :fields="fields"
              :per-page="perPage"
              show-empty
              :empty-text="'검색된 내용이 없습니다.'"
              :empty-filtered-text="'필터된 내용이 없습니다.'"
              :sort-by.sync="sortBy"
              :sort-desc.sync="sortDesc"
              :sort-direction="sortDirection"
              :filter="filter"
              :filterIncludedFields="filterOn"
              @filtered="onFiltered"
            >
              <!-- <template slot="top-row" slot-scope="{ fields }">
              <td v-for="field in fields" :key="field.key">
                <input v-model="filters[field.key]" :placeholder="field.label">
              </td>
            </template> -->
              <template v-slot:table-colgroup="scope">
                <col
                  v-for="field in scope.fields"
                  :key="field.key"
                  :style="{ width: field.key === 'log_date' ? '110px'
                            : field.key === 'log_user_code' ? '80px'
                            : field.key === 'mall_id' ? '100px'
                            : field.key === 'log_request_id' ? '150px'
                            : field.key === 'log_text' ? '500px'
                            : field.key === 'success_yn' ? '80px'
                            : field.key === 'actions' ? '80px'
                            :field.key === 'data' ? '300px':'150px' }"
                >
              </template>
              <!-- A virtual column -->
            <!-- <template v-slot:cell(index)="data">
              <span style="width:20px;">
              {{ data.index + 1 }}
              </span>
            </template> -->
              <template v-slot:cell(log_date)="data">
                 {{ data.value }}
              </template>

              <template v-slot:cell(log_text)="data">
                 <b-button size="sm" @click="logTextOpen(data)">
                  <template v-if="data.item.log_text_open">숨기기</template>
                  <template v-else>펼쳐보기</template>
                </b-button>
                 <template v-if="data.item.log_text_open">
                     {{ data.value }}
                 </template>
              </template>

              <!-- <template v-slot:row-details2="row">
                {{row.item.data}}
              </template> -->

              <template v-slot:cell(data)="data">
                  <vue-json-pretty
                      :path="'res'"
                      :deep="3"
                      :showLength= true
                      :highlightMouseoverNode= true
                      :showDoubleQuotes= false
                      :collapsedOnClickBrackets= true
                      :data="data.value">
                  </vue-json-pretty>
              </template>

              <template v-slot:cell(actions)="row">
                <b-button size="sm" @click="row.toggleDetails">
                  {{ row.detailsShowing ? 'Hide' : 'Show' }} Details
                </b-button>
              </template>
              <template v-slot:row-details="row">
                <b-card>
                  <b-row class="mb-2">
                    <b-col sm="3" class="text-sm-right"><b>data</b></b-col>
                    <b-col>
                      <vue-json-pretty
                      :path="'res'"
                      :showLength= true
                      :highlightMouseoverNode= true
                      :showDoubleQuotes= false
                      :collapsedOnClickBrackets= true
                      :data="row.item.data">
                    </vue-json-pretty>
                    </b-col>
                  </b-row>
                  <b-button size="sm" @click="row.toggleDetails">Hide Details</b-button>
                </b-card>
              </template>
              <template v-slot:cell(method_name)="data">
                <!-- <span v-html="data.value"></span> -->

                <p v-if="data.value.length > 25">
                  <br>{{data.value}}
                </p>
                <p v-else>
                  {{data.value}}
                </p>
                <br>[{{getMethodName(data.value)}}]
              </template>
              <!-- 로딩 -->
              <template v-slot:table-busy>
                <div class="text-center text-danger my-2">
                  <b-spinner class="align-middle"></b-spinner>
                  <strong>Loading...</strong>
                </div>
              </template>

            </b-table>

            <b-row v-if="totalCnt > 0">
              <b-col md="6" class="my-1" align-v="end">
              <b-pagination
                v-model="currentPage"
                :total-rows="totalCnt"
                :per-page="perPage"
                :limit="7"
                aria-controls="my-table"
                prev-text="Prev"
                next-text="Next"
                last-text="Last"
                align="right"
              ></b-pagination>
              </b-col>
              <b-col md="4" class="my-1" align-v="end">
                <b-form-group
                  label="Per page"
                  label-cols-sm="2"
                  label-align="right"
                  label-for="perPageSelect"
                  class="mb-0"
                >
                  <b-form-select
                    v-model="perPage"
                    id="perPageSelect"
                    class="w-25"
                    :options="pageOptions"
                  ></b-form-select>
                </b-form-group>
              </b-col>
            </b-row>
          </div>

        </div>
      </article>
    </div>
    <v-dialog />
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import Pagination from '@/components/common/Pagination'
import ModalAlert from '@/components/common/modal/ModalAlert'
import DatepickerDate from '@/components/common/DatepickerDate'
import CommonServiceList from '@/components/smartstore/CommonServiceList'

import http from '@/http-commons'

export default {
  name: 'SmartStoreLogList',
  components: {
    Pagination,
    DatepickerDate,
    CommonServiceList
  },
  data () {
    return {
      isLoading: false,
      userCode: 1,
      userNo: this.$store.getters.getUser.luna_no,
      productLinkServiceInfo: [],
      logUserCodeList: [1, 2, 3, 4, 5],
      dataColList: [
        {'colname': 'optionResponseList.mallOptionTotalCount', 'desc': '옵션 전체개수'},
        {'colname': 'optionResponseList.optionCountEqualYn', 'desc': '옵션 자사몰 = 연동 수'},
        {'colname': 'optionResponseList.createdOptionCount', 'desc': '옵션 연동 개수'},
        {'colname': 'optionResponseList.optionCreatedYn', 'desc': '옵션 생성 여부'},
        {'colname': 'optionResponseList.optionType', 'desc': '옵션 연동 타입'},
        {'colname': 'optionResponseList.errorType.code', 'desc': '옵션 연동 에러코드'},
        {'colname': 'optionResponseList.errorType.detail', 'desc': '옵션 연동 에러detail'},
        {'colname': 'optionResponseList.errorType.message', 'desc': '옵션 연동 에러message'},
        {'colname': 'createdProductInfo.createdThumbnailImageUrl', 'desc': '상품 연동시 썸네일'},
        {'colname': 'createdProductInfo.smartStoreCategoryNo', 'desc': '상품 연동시 카테고리'},
        {'colname': 'createdProductInfo.thumbnailImageType', 'desc': '상품 연동시 썸네일 타입'},
        {'colname': 'createdProductInfo.productDiscountPrice', 'desc': '상품 연동시 할인가'},
        {'colname': 'createdProductInfo.productPrice', 'desc': '상품 연동시 상품가'},
        {'colname': 'createdProductInfo.productName', 'desc': '상품 연동시 상품명'},
        {'colname': 'editProductInfo.productSellLinkYn', 'desc': '상품수정 판매 플래그'},
        {'colname': 'editProductInfo.productPriceLinkYn', 'desc': '상품수정 가격 플래그'},
        {'colname': 'editProductInfo.productOptionLinkYn', 'desc': '상품수정 옵션 플래그'},
        {'colname': 'editProductInfo.productNameLinkYn', 'desc': '상품수정 이름 플래그'},
        {'colname': 'editProductInfo.productDetailLinkYn', 'desc': '상품수정 상세 플래그'},
        {'colname': 'editProductInfo.sellYn', 'desc': '상품수정 판매여부'},
        {'colname': 'editProductInfo.productName', 'desc': '상품수정 이름'},
        {'colname': 'editProductInfo.productPrice', 'desc': '상품수정 가격'},
        {'colname': 'editProductInfo.productDiscountPrice', 'desc': '상품수정 할인가격'},
        {'colname': 'editProductInfo.preProductSellLinkYn', 'desc': '상품수정 이전 판매 플래그'},
        {'colname': 'editProductInfo.preProductPriceLinkYn', 'desc': '상품수정 이전 가격 플래그'},
        {'colname': 'editProductInfo.preProductOptionLinkYn', 'desc': '상품수정 이전 옵션 플래그'},
        {'colname': 'editProductInfo.preProductNameLinkYn', 'desc': '상품수정 이전 이름 플래그'},
        {'colname': 'editProductInfo.preProductDetailLinkYn', 'desc': '상품수정 이전 상세 플래그'},
        {'colname': 'editProductInfo.preSellYn', 'desc': '상품수정 이전 판매여부'},
        {'colname': 'editProductInfo.preProductName', 'desc': '상품수정 이전 이름'},
        {'colname': 'editProductInfo.preProductPrice', 'desc': '상품수정 이전 가격'},
        {'colname': 'editProductInfo.preProductDiscountPrice', 'desc': '상품수정 이전 할인가격'},
        {'colname': 'productCreatedYn', 'desc': '상품 생성여부'},
        {'colname': 'responseDetail', 'desc': '응답메시지'},
        {'colname': 'errorType.code', 'desc': '연동 에러코드'},
        {'colname': 'errorType.detail', 'desc': '연동 에러detail'},
        {'colname': 'errorType.message', 'desc': '연동 에러message'},
        {'colname': 'syncedList', 'desc': '동기화리스트'},
        {'colname': 'syncedCount', 'desc': '동기화개수'},
        {'colname': 'changedList', 'desc': '변경리스트'},
        {'colname': 'changedCount', 'desc': '변경개수'},
        {'colname': 'notChangedCount', 'desc': '미변경개수'},
        {'colname': 'notChangedList', 'desc': '미변경리스트'},
        {'colname': 'notSelectedCount', 'desc': '미조회개수'},
        {'colname': 'notSelectedList', 'desc': '미조회리스트'},
        {'colname': 'failList', 'desc': '실패리스트'},
        {'colname': 'failCount', 'desc': '실패개수'},
        {'colname': 'totalCount', 'desc': '전체개수'}],
      methodNameList: [
        {'name': 'insertSmartStoreProduct', 'desc': '상품연동'},
        {'name': 'editSmartStoreProduct', 'desc': '상품수정'},
        {'name': 'updateProductCategory', 'desc': '상품카테고리 수정'},
        {'name': 'manageSmartStoreOption', 'desc': '상품옵션수정'},
        {'name': 'registerSeller', 'desc': '판매자등록'},
        {'name': 'insertCategory', 'desc': '카테고리등록'},
        {'name': 'upsertSmartStoreProductList', 'desc': '수기등록'},
        {'name': 'setOptionListTot', 'desc': '배치 자사몰 -> 스마트스토어(옵션) item'},
        {'name': 'syncOptionByMall', 'desc': '배치 자사몰 -> 스마트스토어(옵션) list'},
        {'name': 'setSmartStoreProductSyncServiceTot', 'desc': '배치 루나 <- 스마트스토어 list'},
        {'name': 'setChangedProductInfoTot', 'desc': '배치 루나 <- 스마트스토어 item'},
        {'name': 'updateUnMatchedProductList', 'desc': '배치 수기등록상품 최신화'},
        {'name': 'syncByMallProductInfoToSmartStoreService', 'desc': '배치 자사몰 -> 스마트스토어(상품) list'},
        {'name': 'setLatestSmartStoreProductInfo', 'desc': '배치 자사몰 -> 스마트스토어(상품) item'},
        {'name': 'makeSimplOptionRequest', 'desc': '옵션 request'}
      ],
      fromDate: '',
      toDate: '',
      searchParam: {
        log_start_date: '',
        log_end_date: '',
        mall_ids: [],
        log_user_codes: [],
        method_name: '',
        log_request_id: '',
        log_text: '',
        data: {
          product_no: '',
          smartstore_product_no: '',
          anyDataEquality: 1,
          anyData2Equality: 1
        },
        success_yn: ''
      },
      list: [],
      fields: [
        { key: 'log_date', label: 'log_date', sortable: true, class: 'text-center' },
        { key: 'log_user_code', label: 'log_user_code', sortable: true, class: 'text-center' },
        { key: 'mall_id', label: 'mall_id', sortable: true, class: 'text-center' },
        { key: 'method_name', label: 'method_name', sortable: true, class: 'text-center' },
        { key: 'success_yn', label: 'success_yn', sortable: true, class: 'text-center' },
        { key: 'log_text', label: 'log_text', sortable: true },
        { key: 'data', label: 'data', sortable: true },
        { key: 'log_request_id', label: 'log_request_id', sortable: true },
        { key: 'actions', label: 'Actions' }
      ],
      totalCnt: 0,
      totalRows: 1,
      currentPage: 1,
      perPage: 50,
      pageOptions: [10, 20, 50, 100],
      sortBy: '',
      sortDesc: false,
      sortDirection: 'asc',
      filter: null,
      filterOn: [],
      status: '',
      searchStr: '',
      detailOn: false,
      options: [
        { value: 0, text: '0' },
        { value: 1, text: '1' },
        { value: 2, text: '2' },
        { value: 3, text: '3' },
        { value: 4, text: '4' }
      ]
    }
  },
  watch: {
    currentPage (newPage) {
      this.get()
    },
    perPage (perPage) {
      this.get()
    }

  },

  created () {
    this.getServiceList()
    this.setDateParam()
  },
  methods: {
    setFromDate (value) {
      this.fromDate = value
    },
    setToDate (value) { /** */
      this.toDate = value
    },
    getMethodName (item) {
      let result = this.methodNameList.find(element => {
        return element.name === item
      })
      if (result === undefined) {
        result = {}
        result.desc = '미등록'
      }

      return result.desc
    },
    setDateParam () {
      let toDateTemp = new Date()
      this.toDate = new Date(toDateTemp)
      this.fromDate = new Date(toDateTemp.setDate(toDateTemp.getDate()))
      this.get()
    },
    // pageBtnClick (pageNum) {
    //   alert('asdfasd' + pageNum)
    //   this.get()
    // },
    // getLabel: function (val) {
    //   if (typeof val === 'object') {
    //     return val.name + ' [' + val.desc + ']'
    //   } else {
    //     return this.options.filter(function (option) {
    //       return option.name === val
    //     })[0].name
    //   }
    // },
    onFiltered (filteredItems) {
      // Trigger pagination to update the number of buttons/pages due to filtering
      this.totalRows = filteredItems.length
      this.currentPage = 1
    },
    filterFunction (row, { status, search, fields }) {
      return (
        (row.status === status || !status) &&
        (!fields.length ||
          fields.some(n => this.toStr(row[n]).includes(search)))
      )
    },
    get () {
      if (this.userMenuAuth.retrieve_list) {
        this.searchParam.log_start_date = this.fromDate
        this.searchParam.log_end_date = this.toDate

        let param = {

        }
        param.perPage = this.perPage
        param.currentPage = this.currentPage
        param.log_start_date = this.searchParam.log_start_date
        param.log_end_date = this.searchParam.log_end_date

        if (this.searchParam.method_name) {
          param.method_name = this.searchParam.method_name.name
        }
        if (this.searchParam.mall_ids.length > 0) {
          param.mall_ids = JSON.parse(JSON.stringify(this.searchParam.mall_ids.toLocaleString()))
        } else {
          param.mall_ids = ''
        }
        if (this.searchParam.log_user_codes.length > 0) {
          param.log_user_codes = JSON.parse(JSON.stringify(this.searchParam.log_user_codes))
        } else {
          param.log_user_codes = []
        }
        param.log_request_id = this.searchParam.log_request_id
        param.success_yn = this.searchParam.success_yn
        param.log_text = encodeURIComponent(this.searchParam.log_text.trim())

        if (this.searchParam.data) {
          param.data = {}
          if (this.searchParam.data.product_no) {
            param.data.product_no = this.searchParam.data.product_no
          }
          if (this.searchParam.data.smartstore_product_no) {
            param.data.smartstore_product_no = this.searchParam.data.smartstore_product_no
          }
          if (this.searchParam.data.anyData && this.searchParam.data.anyDataName && this.searchParam.data.anyDataName.colname && this.searchParam.data.anyDataEquality) {
            param.data.anyData = isNaN(Number.parseInt(this.searchParam.data.anyData)) ? this.searchParam.data.anyData : Number.parseInt(this.searchParam.data.anyData)
            param.data.anyDataName = this.searchParam.data.anyDataName.colname
            param.data.anyDataEquality = this.searchParam.data.anyDataEquality
          }
          if (this.searchParam.data.anyData2 && this.searchParam.data.anyData2Name && this.searchParam.data.anyData2Name.colname && this.searchParam.data.anyData2Equality) {
            param.data.anyData2 = isNaN(Number.parseInt(this.searchParam.data.anyData2)) ? this.searchParam.data.anyData2 : Number.parseInt(this.searchParam.data.anyData2)
            param.data.anyData2Name = this.searchParam.data.anyData2Name.colname
            param.data.anyData2Equality = this.searchParam.data.anyData2Equality
          }
        }

        this.isLoading = true
        let url = '/monitor/smartstorelog/list'
        http
          .post(url, JSON.stringify(param))
          .then(response => {
            this.isLoading = false
            if (response.data.responseCode === 0) {
              this.list = response.data.data.list
              this.totalCnt = response.data.data.totalCnt
              console.log(response)
              for (const idx in this.list) {
                this.list[idx].log_date = this.dateTimeFormatToString(this.list[idx].log_date)
              }
            }
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
    getServiceList () {
      let url = '/smartstore/servicelist'
      let paramData = null
      http
        .get(url, {params: paramData})
        .then(response => {
          this.productLinkServiceInfo = response.data
        })
        .catch(e => {
        })
    },
    /**
     * value 는 array
     */
    selectMallInfoVal (valueArr) {
      this.searchParam.mall_ids = []
      for (const value of valueArr) {
        this.searchParam.mall_ids.push(value.mall_id)
      }
      this.get()
    },
    typing: function (event, type, item, name) {
      let patternNo = /^[0-9]+$/

      let message = event.target.value.trim()
      console.log(message)
      if (type === 'no') {
        // 숫자가 아니면 지움
        if (!patternNo.test(message)) {
          item[name] = message.substr(0, message.length - 1)
        }
      }
    },
    dateTimeFormatToString (date) {
      let newDate = new Date(date)
      if (date.length === 10) return date

      function pad (num) {
        num = num + ''
        return num.length < 2 ? '0' + num : num
      }

      return newDate.getFullYear() + '-' + pad(newDate.getMonth() + 1) + '-' + pad(newDate.getDate()) + ' ' + pad(newDate.getHours()) + ':' + pad(newDate.getMinutes()) + ':' + pad(newDate.getSeconds())
    },
    logTextOpen (data) {
      if (data.item.log_text_open) {
        data.item.log_text_open = false
      } else {
        data.item.log_text_open = true
      }
      this.$forceUpdate()
    }
  },
  props: {
    userMenuAuth: Object
  }
}
</script>

<style src="vue-select/dist/vue-select.css"></style>

<style scoped lang="scss">

    .vue-select >>> .vs__dropdown-toggle {
    border-radius: 0px;
    height: 36px;
  }
  .searchWrap table tr:nth-child(n+3) {
    display: table-row !important;
  }

</style>
