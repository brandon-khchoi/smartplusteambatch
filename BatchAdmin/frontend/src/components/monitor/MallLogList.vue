<template>
  <div class="content">
    <div class="formContainer">
      <div class="searchWrap">
        <table summary="상품관리">
          <caption class="blind">상품관리</caption>
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
                <th>토픽명</th>
                <td colspan="3">
                      <div class="vue-select">
                        <v-select
                          label="label"
                          multiple
                          taggable
                          v-model="searchParam.topic_names"
                          :options="topic_name_list"
                          placeholder="토픽명 검색"
                        >
                          <template v-slot:option="option">
                              {{ option.label }}
                          </template>
                        </v-select>
                      </div>
                </td>
            </tr>
            <tr>
              <!-- <th>파라미터
              </th>
              <td>
                <input type="text" value="" v-model="searchParam.param_list"
                  @keyup.enter="get()"
                  placeholder="like검색" />
              </td> -->

              <th>재적재여부</th>
              <td>
                <select v-model="searchParam.kafka_reproduce_yn"  @change="get()">
                <option value=''>전체</option>
                <option :value=0>미적재</option>
                <option :value=1>재적재</option>
                </select>
              </td>
              <th>오류내용</th>
              <td colspan="3">
                <input type="text" value="" v-model="searchParam.exception_text" @keyup.enter="get()"
                  placeholder="like검색" />
              </td>
            </tr>
            <tr>
              <th>hosting_no</th>
              <td>
                <input type="text" value="" v-model="searchParam.param_list.requiredParam.mallInfo.hosting_no"
                  @input="typing($event, 'no', searchParam.param_list.requiredParam.mallInfo, 'hosting_no')"
                  @keyup.enter="get()"
                  placeholder="EX) 1" />
              </td>
              <th>hosting_mall_id</th>
              <td>
                <input type="text" value="" v-model="searchParam.param_list.requiredParam.mallInfo.hosting_mall_id"
                  @keyup.enter="get()"
                  placeholder="EX) sseoqkr7" />
              </td>
              <th>hosting_mall_no</th>
              <td>
                <input type="text" value="" v-model="searchParam.param_list.requiredParam.mallInfo.hosting_mall_no"
                  @input="typing($event, 'no', searchParam.param_list.requiredParam.mallInfo, 'hosting_mall_no')"
                  @keyup.enter="get()"
                  placeholder="EX) 1" />
              </td>
              <th>member_mall_no</th>
              <td>
                <input type="text" value="" v-model="searchParam.param_list.requiredParam.mallInfo.member_mall_no"
                  @input="typing($event, 'no', searchParam.param_list.requiredParam.mallInfo, 'member_mall_no')"
                  @keyup.enter="get()"
                  placeholder="EX) 1" />
              </td>
            </tr>
            <tr>
              <th>member_id</th>
              <td>
                <input type="text" value="" v-model="searchParam.param_list.requiredParam.mallInfo.member_id"
                  @keyup.enter="get()"
                  placeholder="EX) hotping" />
              </td>
              <th>session_id</th>
              <td>
                <input type="text" value="" v-model="searchParam.param_list.requiredParam.serviceInfo.session_id"
                  @keyup.enter="get()"
                  placeholder="EX) 1862CE67923F9F4C557C330DD373DD96" />
              </td>
              <th>service_no</th>
              <td>
                <input type="text" value="" v-model="searchParam.param_list.requiredParam.serviceInfo.service_no"
                  @input="typing($event, 'no', searchParam.param_list.requiredParam.serviceInfo, 'service_no')"
                  @keyup.enter="get()"
                  placeholder="EX) 5" />
              </td>
            </tr>
            <tr>
              <th>category_no</th>
              <td>
                <input type="text" value="" v-model="searchParam.param_list.productRequestParam.category_no"
                  @keyup.enter="get()"
                  placeholder="EX) 001" />
              </td>
              <th>product_no</th>
              <td>
                <input type="text" value="" v-model="searchParam.param_list.productRequestParam.product_no"
                  @input="typing($event, 'no', searchParam.param_list.productRequestParam, 'product_no')"
                  @keyup.enter="get()"
                  placeholder="EX) 19215" />
              </td>
              <th>created_start_date</th>
              <td>
                <input type="text" value="" v-model="searchParam.param_list.productRequestParam.created_start_date"
                  @keyup.enter="get()"
                  placeholder="EX) 2020-01-14" />
              </td>
              <th>created_end_date</th>
              <td>
                <input type="text" value="" v-model="searchParam.param_list.productRequestParam.created_end_date"
                  @keyup.enter="get()"
                  placeholder="EX) 2020-01-14" />
              </td>
            </tr>
            <tr>
              <th>updated_start_date</th>
              <td>
                <input type="text" value="" v-model="searchParam.param_list.productRequestParam.updated_start_date"
                  @keyup.enter="get()"
                  placeholder="EX) 2020-01-14" />
              </td>
              <th>updated_end_date</th>
              <td>
                <input type="text" value="" v-model="searchParam.param_list.productRequestParam.updated_end_date"
                  @keyup.enter="get()"
                  placeholder="EX) 2020-01-14" />
              </td>
              <th>sell_start_date</th>
              <td>
                <input type="text" value="" v-model="searchParam.param_list.productRequestParam.sell_start_date"
                  @keyup.enter="get()"
                  placeholder="EX) 2020-01-14" />
              </td>
              <th>sell_end_date</th>
              <td>
                <input type="text" value="" v-model="searchParam.param_list.productRequestParam.sell_end_date"
                  @keyup.enter="get()"
                  placeholder="EX) 2020-01-14" />
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
            <p class="total">결과 : 총 <span> {{list.length}} </span> 건</p>
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
                  <b-form-checkbox value="topic_name">topic_name</b-form-checkbox>
                  <b-form-checkbox value="param_list">param_list</b-form-checkbox>
                  <b-form-checkbox value="exception_text">exception_text</b-form-checkbox>
                  <b-form-checkbox value="kafka_reproduce_yn">kafka_reproduce_yn</b-form-checkbox>
                </b-form-checkbox-group>
              </div>
            </b-col>
            </b-row>
            <b-row class="font_size">
              <b-col md="6" class="my-1">
                <span style="margin-left:10px;">
                  param_list json 보기
                </span>
                <select v-model="paramListDepth" style="width:100px;">
                  <option :value=0>0</option>
                  <option :value=1>1</option>
                  <option :value=2>2</option>
                  <option :value=3>3</option>
                  </select>
              </b-col>
              <b-col md="6" class="my-1">
              </b-col>
            </b-row>
            <b-row v-if="list.length > 0">
            <b-col md="6" class="my-1" align-v="end">
            <b-pagination
              v-model="currentPage"
              :total-rows="list.length"
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
              :current-page="currentPage"
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
                            : field.key === 'hosting_no' ? '80px'
                            : field.key === 'member_id' ? '100px'
                            : field.key === 'exception_text' ? '500px'
                            : field.key === 'kafka_reproduce_yn' ? '80px'
                            : field.key === 'actions' ? '80px'
                            :field.key === 'param_list' ? '300px':'150px' }"
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
              <template v-slot:cell(hosting_no)="data">
                 {{ data.item.param_list.requiredParam.mallInfo.hosting_no }}
              </template>
              <template v-slot:cell(member_id)="data">
                 {{ data.item.param_list.requiredParam.mallInfo.member_id }}
              </template>

              <template v-slot:cell(param_list)="data">
                  <vue-json-pretty
                      :path="'res'"
                      :deep="paramListDepth"
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
                    <b-col sm="3" class="text-sm-right"><b>param_list</b></b-col>
                    <b-col>
                      <vue-json-pretty
                      :path="'res'"
                      :showLength= true
                      :highlightMouseoverNode= true
                      :showDoubleQuotes= false
                      :collapsedOnClickBrackets= true
                      :data="row.item.param_list">
                    </vue-json-pretty>
                    </b-col>
                  </b-row>
                  <b-button size="sm" @click="row.toggleDetails">Hide Details</b-button>
                </b-card>
              </template>
              <template v-slot:cell(exception_text)="data">
                <!-- <span v-html="data.value"></span> -->
                {{data.value}}
              </template>
              <!-- 로딩 -->
              <template v-slot:table-busy>
                <div class="text-center text-danger my-2">
                  <b-spinner class="align-middle"></b-spinner>
                  <strong>Loading...</strong>
                </div>
              </template>

            </b-table>

            <b-row v-if="list.length > 0">
              <b-col md="6" class="my-1" align-v="end">
              <b-pagination
                v-model="currentPage"
                :total-rows="list.length"
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
import http from '@/http-commons'

export default {
  name: 'MallLogList',
  components: {
    Pagination,
    DatepickerDate
  },
  data () {
    return {
      isLoading: false,
      userCode: 1,
      userNo: this.$store.getters.getUser.luna_no,
      fromDate: '',
      toDate: '',
      topic_name_list: ['category-topic', 'smartplus-product-topic', 'recommendation-product-topic'],
      searchParam: {
        log_start_date: '',
        log_end_date: '',
        topic_names: [],
        param_list: {
          requiredParam: {
            mallInfo: {
              hosting_no: '',
              hosting_mall_id: '',
              hosting_mall_no: '',
              member_mall_no: '',
              member_id: ''
            },
            serviceInfo: {
              session_id: '',
              service_no: ''
            }
          },
          productRequestParam: {
            category_no: '',
            product_no: '',
            created_start_date: '',
            created_end_date: '',
            updated_start_date: '',
            updated_end_date: '',
            sell_start_date: '',
            sell_end_date: ''
          }
        },
        exception_text: '',
        kafka_reproduce_yn: 0
      },
      list: [],
      fields: [
        { key: 'log_date', label: 'log_date', sortable: true, class: 'text-center' },
        { key: 'topic_name', label: 'topic_name', sortable: true, class: 'text-center' },
        { key: 'hosting_no', label: 'hosting_no', class: 'text-center' },
        { key: 'member_id', label: 'member_id', class: 'text-center' },
        { key: 'param_list', label: 'param_list', sortable: true },
        { key: 'exception_text', label: 'exception_text', sortable: true },
        { key: 'kafka_reproduce_yn', label: '재적재여부', sortable: true, class: 'text-center' },
        { key: 'actions', label: 'Actions' }
      ],
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
      paramListDepth: 0,
      options: [
        { value: 0, text: '0' },
        { value: 1, text: '1' },
        { value: 2, text: '2' },
        { value: 3, text: '3' },
        { value: 4, text: '4' }
      ]
    }
  },
  computed: {
    // filter () {
    //   return {
    //     status: this.status,
    //     search: this.searchStr.toLowerCase(),
    //     fields: this.filterOn
    //   }
    // }
  },
  created () {
    // this.get()
    this.setDateParam()
  },
  methods: {
    setFromDate (value) {
      this.fromDate = value
    },
    setToDate (value) { /** */
      this.toDate = value
    },
    setDateParam () {
      let toDateTemp = new Date()
      this.toDate = new Date(toDateTemp)
      this.fromDate = new Date(toDateTemp.setDate(toDateTemp.getDate()))
      this.get()
    },
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
        param.log_start_date = this.searchParam.log_start_date
        param.log_end_date = this.searchParam.log_end_date
        if (this.searchParam.topic_names.length > 0) {
          param.topic_names = JSON.parse(JSON.stringify(this.searchParam.topic_names.toLocaleString()))
        } else {
          param.topic_names = ''
        }

        param.kafka_reproduce_yn = this.searchParam.kafka_reproduce_yn
        // param.param_list = encodeURIComponent(this.searchParam.param_list.trim())
        param.param_list = this.searchParam.param_list
        param.exception_text = encodeURIComponent(this.searchParam.exception_text.trim())

        this.isLoading = true
        let url = '/monitor/log/list'
        http
          .post(url, JSON.stringify(param))
          .then(response => {
            this.isLoading = false
            if (response.data.responseCode === 0) {
              this.list = JSON.parse(response.data.data)
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
    }
  },
  props: {
    userMenuAuth: Object
  }
}
</script>

<style src="vue-select/dist/vue-select.css"></style>

<style scoped lang="scss">

   .font_size {
      font-size: 1rem
    }
  .vue-select >>> .vs__dropdown-toggle {
    border-radius: 0px;
    height: 36px;
  }
  .searchWrap table tr:nth-child(n+3) {
    display: table-row !important;
  }
  input[type="number"] {
    padding: 0 10px;
    text-align: left;
    min-height: 36px;
    background: #fff;
    border: 1px solid #e8e8e8;
    vertical-align: middle;
    line-height: 34px;
  }
  .searchWrap table td input[type="number"] {
    width: 100%;
  }
</style>
