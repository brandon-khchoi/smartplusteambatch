<template>
  <div class="content">
      <h1>
        공지사항
      </h1>
      <div class="formContainer">
        <form action="#" method="post">
          <div class="searchWrap">
            <table summary="공지사항 조회">
              <caption class="blind">공지사항 조회</caption>
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
                  <td colspan="7">
                    <div >
                        <DatepickerDate
                        v-bind:propFromDate="fromDate"
                        v-bind:propToDate="toDate"
                        :propsSetBtn="4"
                        v-on:update:fromDate="setFromDate"
                        v-on:update:toDate="setToDate"
                        >
                        <!-- 자식이 부모한테 보낼때 v-on 및 메소드명을 뒤에 사용해서 올려줌 -->
                        </DatepickerDate>
                    </div>
                  </td>
                </tr>
                <tr>
                  <th>검색구분</th>
                  <td colspan="7">
                    <select v-model="searchType" style="width:10%">
                      <option value="">전체</option>
                      <option value="title">제목</option>
                      <option value="text">내용</option>
                    </select>
                    <input style="width:36%" v-model="searchText" @keyup.enter="isSearch = true;get(1, selectedPerPage)" type="text" />
                  </td>
                </tr>
                <tr v-if=isFilterFormShow>
                  <th>분류</th>
                  <td>
                    <select v-model="noticeType" style="width:100%">
                      <option value="">전체</option>
                      <template v-for="(val, code) in codeData.noticeType">
                        <option :key="code" :value="code">{{ val }}</option>
                      </template>
                    </select>
                  </td>
                  <th>서비스 구분</th>
                  <td >
                    <select v-model="noticeDisplayVal" style="width:100%">
                      <template v-for="(val, code) in codeData.noticeDisplay">
                        <option :key="code" :value="code">{{ val }}</option>
                      </template>
                    </select>
                  </td>
                  <th>노출여부</th>
                  <td colspan="3">
                    <select v-model="displayYn" class="short3">
                      <option value="">전체</option>
                      <option value="1">노출</option>
                      <option value="0">노출안함</option>
                    </select>
                  </td>
                </tr>
              </tbody>
            </table>
            <div class="btnWrap">
              <ul>
                <li>
                  <div class="btn_searchToggle" :class= "{searchOpen: isFilterFormShow}" @click="showSearchFilter">
                    <i class="fas fa-chevron-down arrow"></i>
                    <span></span>
                  </div>
                </li>
                <li>
                  <p class="btn_search" @click="isSearch = true; get(1, selectedPerPage)">
                    <i class="fas fa-search"></i>검색
                  </p>
                </li>
              </ul>
            </div>
          </div>
          <article class="searchResult">
            <div class="articleWrap">
              <div class="tableTopWrap">
                <p class="total">
                  검색결과 : 총
                  <span>{{ paginationData.total_cnt }}</span> 건
                </p>
                <div class="btnWrap">
                  <ul>
                    <li>
                      <p class="btn_add">
                        <a @click="createNotice()">
                          <i class="fas fa-plus"></i>등록
                        </a>
                      </p>
                    </li>
                  </ul>
                </div>
              </div>
              <div class="tableWrap tableList tableHover">
                <table class="textCenter" summary="공지사항 조회">
                  <caption class="blind">공지사항 조회</caption>
                  <colgroup>
                    <col style="width:5%" />
                    <col style="width:10%" />
                    <col />
                    <col style="width:10%" />
                    <col style="width:7%" />
                    <col style="width:10%" />
                    <col style="width:10%" />
                    <col style="width:10%" />
                  </colgroup>
                  <thead>
                    <tr>
                      <th>No</th>
                      <th>분류</th>
                      <th>제목</th>
                      <th>서비스 구분</th>
                      <th>노출여부</th>
                      <th>등록자</th>
                      <th>등록일</th>
                      <th>노출기간</th>
                    </tr>
                  </thead>
                  <tbody v-if="noticeList.length > 0">
                    <template v-for="(item, idx) in noticeList" >
                      <tr :key="idx">
                        <td>
                          {{ item.notice_no }}
                        </td>
                        <td> {{ codeData.noticeType[item.notice_type_code] }}</td>
                        <td style="text-align: left;font-weight: bold;">
                          <a @click="noticeDetail(item.notice_no)" > <span style="vertical-align: baseline;" v-if="item.important_yn == '1'">[중요]</span> {{ item.notice_title }} </a>
                        </td>
                        <td v-text="displayValChecker(item.notice_display_val)"></td>
                        <td>{{ item.display_yn_desc }}</td>
                        <td v-text="item.create_user == null ? '-' : item.create_user"></td>
                        <td>{{ item.create_date }}</td>
                        <td>{{ item.display_start_date }} ~ <span style="vertical-align: baseline;" v-if="item.display_end_date !='9999-12-31'">{{ item.display_end_date }}</span></td>
                      </tr>
                    </template>
                  </tbody>
                  <tbody v-else>
                    <tr>
                      <td colspan="8" v-if="isSearch"> 검색결과가 없습니다. </td>
                      <td colspan="8" v-else> 공지사항이 없습니다. </td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <div class="paginate">
              <template v-if="paginationData.total_cnt > 0">
                  <div style="width: 70%;display: inline-block;">
                    <Pagination
                      :paginate=paginationData.paginate
                      :action="get"
                      :isPropPageCntShow="true"
                      :propPageCnt=selectedPerPage
                      :propStyle="'clear: none;padding: 0px;'"
                    ></Pagination>
                  </div>
              </template>
              </div>
            </div>
          </article>
        </form>
      </div>
  </div>
</template>

<script>

import http from '../../../http-commons'
import Pagination from '@/components/common/Pagination'
import DatepickerDate from '@/components/common/DatepickerDate'
import ModalAlert from '@/components/common/modal/ModalAlert'

export default {
  components: {
    Pagination,
    DatepickerDate
  },
  data () {
    return {
      isSearch: false,
      noticeList: [],
      paginationData: {
        total_cnt: 0
      },
      selectedPerPage: 20,
      isFilterFormShow: false,
      fromDate: '',
      toDate: '',
      noticeType: '',
      searchType: '',
      noticeDisplayVal: '1',
      displayYn: '',
      searchText: ''
    }
  },
  created () {
    this.setDateParam()
  },
  mounted () {
  },
  methods: {
    get (page = 1, perPage = 20) {
      let params = {}
      params.page = page
      this.selectedPerPage = Number(perPage)
      params.perPage = Number(perPage)
      if (this.isSearch) {
        // eslint-disable-next-line no-undef
        params.fromDate = dateFormatToString(this.fromDate); params.toDate = dateFormatToString(this.toDate)
        params.searchType = this.searchType
        if (this.searchText === '' || this.searchText.length >= 2) {
          if (this.searchText !== '') {
            params.searchText = '%' + this.searchText + '%'
          }
        } else {
          this.$modal.show(ModalAlert,
            { title: '', text: '검색어는 최소 2자 이상입니다.' }, { width: 450, height: 'auto' }
          )
          return
        }

        if (this.isFilterFormShow) {
          params.noticeType = this.noticeType
          params.noticeDisplayVal = this.noticeDisplayVal
          params.displayYn = this.displayYn
        }
      }

      http
        .post('/notice/list', params)
        .then(response => {
          this.noticeList = response.data.data
          // eslint-disable-next-line no-undef
          this.paginationData.paginate = makePaginate(response.data); this.paginationData.max = pageMaxNumber(response.data)
          this.paginationData.total_cnt = response.data.total_cnt
        }).catch(e => {
          console.log(e)
        })
    },
    setDateParam () {
      let toDateTemp = new Date()
      this.toDate = new Date(toDateTemp)
      this.fromDate = new Date(toDateTemp.setMonth(toDateTemp.getMonth() - 3))
      this.get()
    },
    setFromDate (value) {
      this.fromDate = value
    },
    setToDate (value) {
      this.toDate = value
    },
    showSearchFilter (e) { /** 검색필터 열기 닫기 */
      this.isFilterFormShow = !this.isFilterFormShow
    },
    displayValChecker (displayVal) {
      let result = ''
      if (displayVal === 0) {
        result = '-'
      } else {
        for (const val in this.codeData.noticeDisplay) {
          if ((displayVal & val) === Number(val)) {
            result += this.codeData.noticeDisplay[val] + ', '
          }
        }
        result = result.substring(0, result.lastIndexOf(', '))
      }
      return result
    },
    createNotice () {
      this.$router.push({
        name: 'noticeEdit'
      })
    },
    noticeDetail (noticeNo) {
      this.$router.push({ name: 'noticeDetail', query: {'noticeNo': noticeNo} })
    }
  },
  props: {
    'userMenuAuth': Object,
    'codeData': Object
  }
}
</script>
