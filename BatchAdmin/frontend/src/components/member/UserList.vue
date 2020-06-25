<template>
  <div class="UserList">
    <h1>회원 관리 > 회원 조회</h1>
    <div class="formContainer">
      <div class="searchWrap">
        <table summary="회원 조회">
            <caption class="blind">회원 조회</caption>
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
                    <th>기간검색</th>
                    <td colspan="3">
                        <div >
                            <DatepickerDate
                            v-bind:propFromDate="fromDate"
                            v-bind:propToDate="toDate"
                            v-on:update:fromDate="setFromDate"
                            v-on:update:toDate="setToDate"
                            >
                            <!-- 자식이 부모한테 보낼때 v-on 및 메소드명을 뒤에 사용해서 올려줌 -->
                            </DatepickerDate>
                        </div>
                    </td>
                      <th>검색부분</th>
                    <td colspan="3">
                        <p class="searchWordWrap">
                            <select v-model="searchParam.searchFlag">
                                <option value="1">상호명</option>
                                <option value="2">회원ID</option>
                                <option value="3">법인명</option>
                                <option value="4">대표번호</option>
                                <option value="5">이메일</option>
                                <option value="6">사업자번호</option>
                                <option value="7">담당자명</option>
                                <option value="8">호스팅 몰 아이디</option>
                            </select>
                            <input type="text" value="" v-model="searchParam.searchTxt" minlength="2" @keyup.enter="get()" />
                        </p>
                    </td>
                </tr>
            <template v-if="isFilterFormShow ===  true">
                <tr>
                    <th>회원구분</th>
                    <td>
                        <select v-model="searchParam.memberTypeCode">
                            <option value="">전체</option>
                            <template v-for="(val,idx) in this.codeData.memberTypeCodeData" >
                                <option :value="idx" :key="val.id">{{val}}</option>
                            </template>
                        </select>
                    </td>
                    <th>회원상태</th>
                    <td>
                        <select v-model="searchParam.memberStatCode">
                            <option value="">전체</option>
                            <template v-for="(val,idx) in this.codeData.memberStatCodeData">
                                <option :value="idx" :key="val.id">{{val}}</option>
                            </template>
                        </select>
                    </td>
                    <th>호스팅</th>
                    <td>
                        <select v-model="searchParam.hostingNo">
                            <option value="">전체</option>
                            <template v-for="(val,idx) in this.codeData.hostingNoData">
                                <option :value="idx" :key="val.id">{{val}}</option>
                            </template>
                        </select>
                    </td>
                    <th>재고관리</th>
                    <td>
                        <select v-model="searchParam.inventoryCompanyCode">
                            <option value="">전체</option>
                            <template v-for="(val,idx) in this.codeData.inventoryCompanyCodeData">
                                <option :value="idx" :key="val.id">{{val}}</option>
                            </template>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th>영업담당</th>
                    <td>
                        <select v-model="searchParam.managerSalesLunaNo">
                            <option value="">전체</option>
                            <template v-for="(val,idx) in this.codeData.managerSaleslunaNoData">
                                <option :value="idx" :key="val.id">{{val}}</option>
                            </template>
                        </select>
                    </td>
                    <th>운영담당</th>
                    <td>
                        <select v-model="searchParam.managerMaintainLunaNo">
                            <option value="">전체</option>
                            <template v-for="(val,idx) in this.codeData.managerMaintainLunaNoData">
                                <option :value="idx" :key="val.id">{{val}}</option>
                            </template>
                        </select>
                    </td>
                    <td colspan="4">
                    </td>
                </tr>
            </template>
            </tbody>
        </table>
        <div class="btnWrap">
          <ul>
            <li>
              <div
                  class="btn_searchOpenToggle"
                  :class= "{searchOpen: isFilterFormShow}"
                  @click="showSearchFilter"
              >
              <i class="fas fa-chevron-down arrow"></i><span></span></div>
            </li>
            <li>
              <p class="btn_search" @click="get()"><i class="fas fa-search"></i>검색</p>
            </li>
          </ul>
        </div>
      </div>
      <div class="loading">
        <article class="searchResult">
          <div class="articleWrap">
            <div class="tableTopWrap">
              <p class="total">결과 : 총 <span>{{list.total_cnt}}</span> 건</p>
              <div class="btnWrap">
                <ul>
                  <li v-if="userMenuAuth.excl_down">
                    <ExcelDown :propTitle="excelTitle" propFileName="memberList.xlsx" :propJsonData="selectedJsonData" ></ExcelDown>
                  </li>
                  <li>
                    <p class="btn_add" v-if="userMenuAuth.create_data">
                      <router-link :to="{ name: 'usercreate'}"><i class="fas fa-plus" ></i>회원 생성</router-link>
                    </p>
                    <p class="btn_add" v-else>
                      <i class="fas fa-plus" >
                      </i>회원 생성
                    </p>
                  </li>
                </ul>
              </div>
            </div>
            <div class="tableWrap tableList tableGroup">
              <table summary="회원리스트">
                <caption class="blind">회원리스트</caption>
                <colgroup>
                    <col style="width:3%"/>
                    <col style="width:6%"/>
                    <col style="width:10%"/>
                    <col/>
                    <col/>
                    <col style="width:12%"/>
                    <col style="width:12%"/>
                    <col style="width:12%"/>
                    <col style="width:12%"/>
                    <col style="width:12%"/>
                    <col style="width:7%"/>
                </colgroup>
                <thead>
                <tr>
                  <th>선택
                    <span class="inputWrap checkboxCenter">
                      <input type="checkbox" id="service01-01" @change="checkAll" v-model="allSelected" >
                      <label for="service01-01"></label>
                    </span>
                  </th>
                  <th>구분</th>
                  <th>회원ID</th>
                  <th>법인명</th>
                  <th>상호명</th>
                  <th>호스팅</th>
                  <th>호스팅 몰 아이디</th>
                  <th>영업담당</th>
                  <th>운영담당</th>
                  <th>가입일자</th>
                  <th>메모</th>
                </tr>
                </thead>
                <tbody>
                <template v-if="list.total_cnt">
                <template v-for="item in list.data">
                  <tr :key="item.id">
                    <template v-if="item.first_yn=='Y' ">
                      <td :rowspan="item.row_span" class="textCenter">
                        <span class="inputWrap checkboxCenter">
                        <input type="checkbox" :id="item.member_id + item.member_no" :value="item.member_id" v-model="selected"  @click="select">
                        <label :for="item.member_id + item.member_no"></label>
                        </span>
                      </td>
                      <td :rowspan="item.row_span" class="textCenter">{{item.member_type_code_kor}}</td>
                      <td :rowspan="item.row_span">{{item.member_id}}</td>
                      <td :rowspan="item.row_span" >
                        <strong v-if="userMenuAuth.retrieve_detail">
                          <router-link :to="{ name: 'memberinfo',
                            params: { member_no: item.member_no },
                            query: { searchFlag: searchParam.searchFlag, current_page : searchParam.current_page, hostingNo: searchParam.hostingNo,
                                    inventoryCompanyCode: searchParam.inventoryCompanyCode, managerMaintainLunaNo: searchParam.managerMaintainLunaNo,
                                    managerSalesLunaNo: searchParam.managerSalesLunaNo, memberStatCode: searchParam.memberStatCode,
                                    memberTypeCode: searchParam.memberTypeCode, per_page: searchParam.per_page,
                                    searchTxt: searchParam.searchTxt, searhFormDate: searchParam.searhFormDate, searhToDate: searchParam.searhToDate,
                                    isFilterFormShow: isFilterFormShow  } }">
                            {{item.member_biz_name}}
                            </router-link>
                        </strong>
                        <strong v-else>
                          {{item.member_biz_name}}
                        </strong>
                      </td>
                    </template>

                    <td class="textCenter"><span v-text="item.mall_name"/></td>
                    <td rowspan="" class="textCenter">{{item.hosting_name}}</td>
                    <td rowspan="" class="textCenter">{{item.hosting_mall_id}}</td>
                    <td rowspan="" class="textCenter">{{item.manager_sales_luna_name}}</td>
                    <td rowspan="" class="textCenter">{{item.manager_maintain_luna_name}}</td>
                    <td rowspan="" class="textCenter">{{item.create_date}}</td>
                    <template v-if="item.first_yn==='Y'">
                      <td :rowspan="item.row_span" class="textCenter">
                        <button type="button" class="btn_min" @click="showMemo(item)">메모</button>
                      </td>
                    </template>
                  </tr>
                </template>
                </template>
                <template v-else>
                  <tr >
                    <td colspan="11" class="textCenter" >
                      조회된 내용이 없습니다.
                    </td>
                  </tr>
                </template>
                </tbody>
              </table>
            </div>

            <Pagination
              :paginate="list.paginate"
              :action="get"
              :isPropPageCntShow="true"
              :propPageCnt="parseInt(this.searchParam.per_page)"
            ></Pagination>

          </div>
        </article>
      </div>
    </div>
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>

import http from '@/http-commons'
import Pagination from '@/components/common/Pagination'
import DatepickerDate from '@/components/common/DatepickerDate'

import ModalAlert from '@/components/common/modal/ModalAlert'
import ModalMemberMemo from '@/components/common/modal/ModalMemberMemo'

import ExcelDown from '@/components/common/ExcelDown'

export default {
  name: 'UserList',
  components: {
    Pagination,
    DatepickerDate,
    ExcelDown
  },
  data () {
    return {
      isLoading: false,
      list: {
        total_cnt: 0,
        data: []
      }, // 회원 리스트
      isFilterFormShow: false, // 검색필터 열기 닫기
      fromDate: '',
      toDate: '',
      searchParam: {
        searhFormDate: '',
        searhToDate: '',
        searchFlag: '1', // 검색어 select box
        searchTxt: '', // 검색부분
        memberTypeCode: '', // 회원구분
        memberStatCode: '', // 회원상태
        hostingNo: '', // 호스팅
        inventoryCompanyCode: '', // 재고관리
        managerSalesLunaNo: '', // 영업담당
        managerMaintainLunaNo: '', // 운영담당
        per_page: 20
      },
      selected: [], // checkbox selected
      selectedJsonData: [],
      allSelected: false, // 전체 checkbox
      excelTitle: {member_type_code_kor: '구분', member_id: '회원ID', member_biz_name: '법인명', mall_name: '상호명', hosting_name: '호스팅', manager_sales_luna_name: '영업담당', manager_maintain_luna_name: '운영담당', create_date: '가입일자'}
    }
  },
  mounted () {
    console.log('mounted')
  },
  created () {
    console.log(this.$route.query)

    if (Object.keys(this.$route.query).length !== 0) { // 쿼리 데이터가 있을 경우
      console.log('데이터 있음')
      this.searchParam = this.$route.query
      this.fromDate = new Date(this.searchParam.searhFormDate)
      this.toDate = new Date(this.searchParam.searhToDate)
      this.isFilterFormShow = this.searchParam.isFilterFormShow
      this.get(this.searchParam.current_page, this.searchParam.per_page)
    } else { // 쿼리가 아무것도 없을 경우 기본 메인 쿼리
      let toDateTemp = new Date()
      this.toDate = new Date(toDateTemp)
      this.fromDate = new Date(toDateTemp.setMonth(toDateTemp.getMonth() - 12))
      this.get()
    }
    // this.get()
  },
  methods: {
    /** 자식이 부모한테 보내준 데이터 세팅 */
    setFromDate (value) {
      this.fromDate = value
    },
    setToDate (value) { /** */
      this.toDate = value
    },
    showSearchFilter () { /** 검색필터 열기 닫기 */
      console.log('showSearchFilter start')
      this.isFilterFormShow = !this.isFilterFormShow
      console.log('showSearchFilter end')
    },
    /** 자식이 부모한테 보내준 데이터 세팅 끝 */
    get (current_page = 1, per_page = 20) {
      if (this.userMenuAuth.retrieve_list) {
        if (this.searchParam.searchTxt === '') {
          this.searchParam.searchTxt = null
        }
        // eslint-disable-next-line no-undef
        this.searchParam.searhFormDate = dateFormatToString(this.fromDate); this.searchParam.searhToDate = dateFormatToString(this.toDate)
        this.isLoading = true
        http
          .get('/member/userlist', {
            params: { // query string
              from_date: this.searchParam.searhFormDate,
              to_date: this.searchParam.searhToDate,
              search_flag: this.searchParam.searchFlag,
              search_txt: this.searchParam.searchTxt,
              member_type_code: this.searchParam.memberTypeCode,
              member_stat_code: this.searchParam.memberStatCode,
              hosting_no: this.searchParam.hostingNo,
              manager_sales_luna_no: this.searchParam.managerSalesLunaNo,
              manager_maintain_luna_no: this.searchParam.managerMaintainLunaNo,
              inventory_company_code: this.searchParam.inventoryCompanyCode,
              current_page: current_page,
              per_page: per_page
            }
          })
          .then(response => {
            this.isLoading = false

            this.list = response.data
            // eslint-disable-next-line no-undef
            this.list.paginate = makePaginate(response.data); this.list.max = pageMaxNumber(response.data)

            this.searchParam.current_page = this.list.current_page
            this.searchParam.per_page = this.list.per_page
            if (this.allSelected) {
              this.checkAll()
            }
          })
          .catch(e => {
            console.log(e)
            if (e.response.data.data !== 'undefined' && e.response.data.responseCode === -3) {
              this.$modal.show(ModalAlert,
                { title: '회원 조회', text: e.response.data.data },
                { width: 450, height: 'auto' }
              )
            } else {
              this.$modal.show(ModalAlert,
                { title: '회원 조회', text: '서버오류' },
                { width: 450, height: 'auto' }
              )
            }
          })
          .finally(() => {
            this.isLoading = false
          })
      }
    },
    showMemo (member) {
      this.$modal.show(ModalMemberMemo,
        {
          title: member.member_biz_name,
          member_no: member.member_no,
          userMenuAuth: this.userMenuAuth
        }, {
          width: 900,
          height: 'auto',
          clickToClose: false,
          // draggable: true,
          scrollable: true
        }
      )
    },
    select () {
      this.allSelected = false
      // this.selectdJsonData
      if (event.target.checked) {
        for (const item in this.list.data) {
          if (this.list.data[item].member_id.toString() === event.target.value) {
            let data = JSON.parse(JSON.stringify(this.list.data[item]))

            delete data.row_span; delete data.member_type_code; delete data.member_stat_code; delete data.member_no; delete data.first_yn; delete data.member_stat_code_kor; delete data.member_mall_no
            this.selectedJsonData.push(data)
            // this.selectedJsonData
          }
        }
      } else {
        for (const item in this.selectedJsonData) {
          if (this.selectedJsonData[item].member_id.toString() === event.target.value) {
            this.selectedJsonData.splice(item, 1) // item 인덱스 1개 삭제
          }
        }
      }
    },
    checkAll () {
      this.selected = []
      this.selectedJsonData = []
      if (this.allSelected) {
        for (const item in this.list.data) {
          this.selected.push(this.list.data[item].member_id.toString())
          let data = JSON.parse(JSON.stringify(this.list.data[item]))
          delete data.row_span; delete data.member_type_code; delete data.member_stat_code; delete data.member_no; delete data.first_yn; delete data.member_stat_code_kor; delete data.member_mall_no
          this.selectedJsonData.push(data)
        }
      } else {
        this.selected = []
        this.selectedJsonData = []
      }
    }
  },
  props: {
    'userMenuAuth': Object,
    'codeData': Object
  }
}
</script>
