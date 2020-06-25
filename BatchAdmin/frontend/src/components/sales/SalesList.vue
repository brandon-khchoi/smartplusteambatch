<template>
    <div class="SaleList">
        <div class="content business sale">
            <h1>영업 조회</h1>
            <div class="formContainer">
                <div class="searchWrap">
                    <table summary="영업 조회">
                      <caption class="blind">영업 조회</caption>
                        <colgroup>
                            <col style="width:100px"/>
                            <col style="width:calc(25% - 100px)"/>
                            <col style="width:100px"/>
                            <col style="width:calc(25% - 100px)"/>
                            <col style="width:100px"/>
                            <col style="width:calc(25% - 100px)"/>
                            <col style="width:100px"/>
                            <col/>
                        </colgroup>
                        <tbody>
                          <tr>
                            <th>검색기간</th>
                            <td colspan="3">
                                <div >
                                  <DatepickerDate
                                    v-bind:propFromDate="fromDate"
                                    v-bind:propToDate="toDate"
                                    v-on:update:fromDate="setFromDate"
                                    v-on:update:toDate="setToDate"
                                  ></DatepickerDate>
                                </div>
                            </td>
                            <th>검색구분</th>
                            <td colspan="3">
                                <p class="searchWordWrap">
                                    <select v-model="retrieveData.searchFlag">
                                        <option value="1">상호명</option>
                                        <option value="2">법인명</option>
                                    </select>
                                    <input type="text" value="" v-model="retrieveData.searchTxt"/>
                                </p>
                            </td>
                          </tr>
                          <tr>
                            <th>회원구분</th>
                            <td colspan="3">
                              <span class="inputWrap">
                                  <input type="radio" id="memstat" value="" v-model="memberStatPicked">
                                  <label for="memstat">전체</label>
                              </span>
                                <template v-for="(val, idx) in memberStatCodeData">
                                  <span class="inputWrap" :key="idx">
                                      <input type="radio" :id="val" :value="idx"
                                              v-model="memberStatPicked">
                                      <label :for="val">{{val}}</label>
                                  </span>
                                </template>
                            </td>
                            <th>영업항목</th>
                            <td>
                                <select v-model="retrieveData.salesCode">
                                    <option value="">전체</option>
                                    <template v-for="(val, idx) in salesCodeData">
                                        <option :value="idx" :key="idx">{{val}}</option>
                                    </template>
                                </select>
                            </td>
                            <th>영업상태</th>
                            <td>
                                <select v-model="retrieveData.salesStatCode">
                                    <option value="">전체</option>
                                    <template v-for="(val, idx) in salesStatCodeData">
                                        <option :value="idx" :key="idx">{{val}}</option>
                                    </template>
                                </select>
                            </td>
                          </tr>
                        </tbody>
                    </table>
                    <!-- :XXX 조회조건 common.css 에 3번째 라인부터 display:none 처리
                          pub 서버 파일을 수정해야 해서 임시로 table을 하나 더 생성함 -->
                  <template v-if="isFilterFormShow ===  true">
                    <table summary="영업 조회">
                      <caption class="blind">영업 조회</caption>
                        <colgroup>
                            <col style="width:100px"/>
                            <col style="width:calc(25% - 100px)"/>
                            <col style="width:100px"/>
                            <col style="width:calc(25% - 100px)"/>
                            <col style="width:100px"/>
                            <col style="width:calc(25% - 100px)"/>
                            <col style="width:100px"/>
                            <col/>
                        </colgroup>
                        <tbody>
                          <tr>
                            <th>등급</th>
                            <td>
                                <select v-model="retrieveData.mallServiceGrade">
                                    <option value="">전체</option>
                                    <template v-for="(val, idx) in mallServiceGradeData">
                                        <option :value="idx" :key="idx">{{val}}</option>
                                    </template>
                                </select>
                            </td>
                            <th>유입경로</th>
                            <td>
                                <select v-model="retrieveData.memberInflowPathCode">
                                    <option value="">전체</option>
                                    <template v-for="(val, idx) in memberInflowPathCodeData">
                                        <option :value="idx" :key="idx">{{val}}</option>
                                    </template>
                                </select>
                            </td>
                            <th>영업담당</th>
                            <td>
                                <select v-model="retrieveData.managerSalesLunaNo">
                                    <option value="">전체</option>
                                    <template v-for="(val, idx) in managerSalesLunaNoData">
                                        <option :value="idx" :key="idx">{{val}}</option>
                                    </template>
                                </select>
                            </td>
                            <th>영업형태</th>
                            <td>
                                <select v-model="retrieveData.salesTypeCode">
                                    <option value="">전체</option>
                                    <template v-for="(val, idx) in salesTypeCodeData">
                                        <option :value="idx" :key="idx">{{val}}</option>
                                    </template>
                                </select>
                            </td>
                          </tr>
                          <tr>
                            <th>등록자</th>
                            <td>
                                <select v-model="retrieveData.lunaAccountNo">
                                    <option value="">전체</option>
                                    <template v-for="(val, idx) in lunaAccount">
                                        <option :value="idx" :key="idx">{{val}}</option>
                                    </template>
                                </select>
                            </td>
                          </tr>
                        </tbody>
                    </table>
                  </template>
                    <div class="btnWrap">
                        <ul>
                            <li>
                            <div class="btn_searchOpenToggle"
                                :class= "{searchOpen: isFilterFormShow}"
                                @click="showSearchFilter">
                                <i class="fas fa-chevron-down arrow"></i><span></span></div>
                            </li>
                            <li>
                                <p class="btn_search" @click="get()"><i class="fas fa-search"></i>검색</p>
                            </li>
                        </ul>
                    </div>
                </div>
                <article class="searchResult">
                    <div class="articleWrap">
                        <div class="tableTopWrap">
                            <p class="total">결과 : 총 <span>{{salesList.total_cnt}}</span> 건</p>
                            <div class="btnWrap">
                                <ul>
                                    <li v-show="userMenuAuth.excl_down">
                                      <ExcelDown :propTitle="excelTitle" propFileName="salesList.xlsx" :propJsonData="exlSelectedJsonData" ></ExcelDown>
                                    </li>
                                    <li>
                                        <p class="btn_add" @click="createSales"
                                           v-if="userMenuAuth.create_data">
                                            <i class="fas fa-plus"></i>영업활동 등록
                                        </p>
                                        <p class="btn_add" v-else>
                                            <i class="fas fa-plus"></i>영업활동 등록
                                        </p>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div class="tableWrap tableList tableHover">
                            <table class="textCenter" summary="영업활동 리스트">
                                <caption class="blind">영업활동 리스트</caption>
                                <colgroup>
                                    <col style="width:3%"/>
                                    <col style="width:10%"/>
                                    <col/>
                                    <col style="width:6%"/>
                                    <col style="width:8%"/>
                                    <col style="width:9%"/>
                                    <col style="width:9%"/>
                                    <col style="width:7%"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>선택</th>
                                    <th>법인명</th>
                                    <th>상호명</th>
                                    <th>등급</th>
                                    <th>구분</th>
                                    <th>영업항목</th>
                                    <th>유입경로</th>
                                    <th>상태</th>
                                    <th>영업담당</th>
                                    <th>영업일</th>
                                    <th>등록일</th>
                                    <th>삭제</th>
                                </tr>
                                </thead>
                                <tbody>
                                <template v-for="(item, idx) in salesList.items">
                                  <tr :key="idx">
                                    <td><input type="checkbox" :id="idx" :value="item.sales_no" v-model="exlSelected"  @click="exlSelect"/></td>
                                    <td>{{item.member_biz_name}}</td>
                                    <td>
                                        <a  v-if="userMenuAuth.retrieve_detail"
                                            class="spanHover"
                                            @click="editSales(item)">
                                          <strong>{{item.mall_name}}</strong>
                                        </a>
                                        <span v-else>{{item.mall_name}}</span>
                                    </td>
                                    <td>
                                        {{mallServiceGradeData[item.mall_service_grade]}}
                                    </td>
                                    <td>
                                        {{memberStatCodeData[item.member_stat_code]}}
                                    </td>
                                    <td>
                                        {{salesCodeData[item.sales_code]}}
                                    </td>
                                    <td>
                                        {{memberInflowPathCodeData[item.member_inflow_path_code]}}
                                    </td>
                                    <td>
                                        {{salesStatCodeData[item.sales_stat_code]}}
                                    </td>
                                    <td>
                                        {{managerSalesLunaNoData[item.manager_sales_luna_no]}}
                                    </td>
                                    <td class="textCenter">{{item.sales_date}}</td>
                                    <td class="textCenter">{{item.create_date}}</td>
                                    <td class="textCenter">
                                        <input class="btn_min" type="button" value="삭제" @click="salesDel(item)">
                                    </td>
                                  </tr>
                                </template>
                                </tbody>
                            </table>
                        </div>
                        <Pagination
                            :paginate="salesList.paginate"
                            :action="get"
                            :isPropPageCntShow=true
                        ></Pagination>
                    </div>
                </article>
            </div>
        </div>
        <Spinner v-if="isLoading" />
    </div>
</template>

<script>
// 영업조회
import ModalConfirm from '../common/modal/ModalConfirm.vue'
import DatepickerDate from '@/components/common/DatepickerDate'
import Pagination from '@/components/common/Pagination'
import http from '../../http-commons'
import { eventBus } from '../../main'
import ExcelDown from '@/components/common/ExcelDown'

export default {
  created () {
    let toDateTemp = new Date()
    this.toDate = new Date(toDateTemp)
    this.fromDate = new Date(toDateTemp.setMonth(toDateTemp.getMonth() - 12))
  },
  mounted () {
    eventBus.$on('refreshSalesList', () => this.get())
    this.get(1)
  },
  data () {
    return {
      isLoading: false,
      isFilterFormShow: false, // 검색필터 열기 닫기
      fromDate: '',
      toDate: '',
      memberStatPicked: '',
      salesList: [],
      retrieveData: {
        searchFlag: '1',
        searchTxt: '',
        mallServiceGrade: '',
        salesCode: '',
        salesStatCode: '',
        memberInflowPathCode: '',
        managerSalesLunaNo: '',
        salesTypeCode: '',
        lunaAccountNo: ''
      },

      exlSelected: [], // checkbox selected
      exlSelectedJsonData: [],
      excelTitle: {
        sales_no: '등록번호',
        member_biz_name: '법인명',
        mall_name: '상호명',
        mall_service_grade: '등급',
        member_stat_code: '구분',
        sales_code: '영업항목',
        member_inflow_path_code: '유입경로',
        sales_stat_code: '상태',
        manager_sales_luna_no: '영업담당',
        sales_date: '영업일',
        create_date: '등록일'
      }
      // excelHeader: [
      //   'sales_no',
      //   'member_biz_name',
      //   'mall_name',
      //   'mall_service_grade',
      //   'member_stat_code',
      //   'sales_code',
      //   'member_inflow_path_code',
      //   'sales_stat_code',
      //   'manager_sales_luna_no',
      //   'sales_date',
      //   'create_date'
      // ]
    }
  },
  methods: {
    setFromDate (value) {
      this.fromDate = value
    },
    setToDate (value) {
      this.toDate = value
    },
    showSearchFilter () { /** 검색필터 열기 닫기 */
      console.log('showSearchFilter start')
      this.isFilterFormShow = !this.isFilterFormShow
      console.log('showSearchFilter end')
    },
    get (current_page = 1, per_page = 20) {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        http
          .get('/sales/saleslist', {
            params: {
              current_page: current_page,
              per_page: per_page,

              // eslint-disable-next-line no-undef
              from_date: dateFormatToString(this.fromDate),
              // eslint-disable-next-line no-undef
              to_date: dateFormatToString(this.toDate),
              search_flag: this.retrieveData.searchFlag,
              search_txt: this.retrieveData.searchTxt,
              member_stat_code: this.memberStatPicked,
              sales_code: this.retrieveData.salesCode,
              sales_stat_code: this.retrieveData.salesStatCode,
              mall_service_grade: this.retrieveData.mallServiceGrade,
              member_inflow_path_code: this.retrieveData.memberInflowPathCode,
              manager_sales_luna_no: this.retrieveData.managerSalesLunaNo,
              sales_type_code: this.retrieveData.salesTypeCode,
              luna_account_no: this.retrieveData.lunaAccountNo
            }
          })
          .then(result => {
            this.isLoading = false
            this.salesList = result.data
            this.total_cnt = result.data.pagination.total_cnt
            // eslint-disable-next-line no-undef
            this.salesList.paginate = makePaginate(result.data.pagination)
            // eslint-disable-next-line no-undef
            this.salesList.max = pageMaxNumber(result.data.pagination)
          })
          .catch(e => {
            console.log('SalesList.vue : ' + e)
          })
      }
    },
    salesDel (item) {
      this.$modal.show(
        ModalConfirm,
        {
          title: '영업내역 삭제',
          data: {
            url: '/sales/salesdel',
            item: {
              del_type: 'all', // all:전체삭제, one:개별삭제
              mall_no: item.mall_no,
              sales_code: item.sales_code,
              login_luna_no: this.$store.getters.getUser.luna_no
            },
            refresh: 'refreshSalesList'
          }
        },
        {
          width: 450,
          height: 'auto'
        }
      )
    },
    // eslint-disable-next-line camelcase
    editSales (item) {
      this.$router.push({
        name: 'salesedit',
        params: { salesDtailInfo: item }
      })
    },
    createSales (item) {
      this.$router.push({
        name: 'salescreate'
      })
    },
    exlSelect () {
      // this.exlSelectdJsonData
      if (event.target.checked) {
        for (let item in this.salesList.items) {
          if (this.salesList.items[item].sales_no.toString() === event.target.value) {
            let data = JSON.parse(JSON.stringify({
              'sales_no': this.salesList.items[item].sales_no,
              'member_biz_name': this.salesList.items[item].member_biz_name,
              'mall_name': this.salesList.items[item].mall_name,
              'mall_service_grade': this.mallServiceGradeData[this.salesList.items[item].mall_service_grade] || '-',
              'member_stat_code': this.memberStatCodeData[this.salesList.items[item].member_stat_code] || '-',
              'sales_code': this.salesCodeData[this.salesList.items[item].sales_code] || '-',
              'member_inflow_path_code': this.memberInflowPathCodeData[this.salesList.items[item].member_inflow_path_code] || '-',
              'sales_stat_code': this.salesStatCodeData[this.salesList.items[item].sales_stat_code] || '-',
              'manager_sales_luna_no': this.managerSalesLunaNoData[this.salesList.items[item].manager_sales_luna_no] || '-',
              'sales_date': this.salesList.items[item].sales_date,
              'create_date': this.salesList.items[item].create_date
            }))

            delete data.mall_no
            delete data.member_no
            this.exlSelectedJsonData.push(data)
            // this.exlSelectedJsonData
          }
        }
      } else {
        for (let item in this.exlSelectedJsonData) {
          if (this.exlSelectedJsonData[item].sales_no.toString() === event.target.value) {
            this.exlSelectedJsonData.splice(item, 1) // item 인덱스 1개 삭제
          }
        }
      }
    }
  },
  components: {
    Pagination,
    DatepickerDate,
    ExcelDown
  },
  props: [
    'userMenuAuth',
    'memberStatCodeData',
    'salesCodeData',
    'salesStatCodeData',
    'salesTypeCodeData',
    'memberInflowPathCodeData',
    'mallServiceGradeData',
    'managerSalesLunaNoData',
    'lunaAccount'
  ]
}
</script>
