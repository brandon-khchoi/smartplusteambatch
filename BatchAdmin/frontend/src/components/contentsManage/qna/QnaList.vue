<template>
  <div class="content">
      <h1>1:1문의</h1>
      <div class="formContainer">
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
                    <option value="memberId">회원ID</option>
                    <option value="bizName">법인명</option>
                    <option value="mallName">상호명</option>
                    <option value="title">제목</option>
                    <option value="text">내용</option>
                    <option value="tel">휴대폰번호</option>
                    <option value="email">이메일</option>
                  </select>
                  <input style="width:36%" v-model="searchText" @keyup.enter="isSearch = true; get(1, selectedPerPage)" type="text"/>
                </td>
              </tr>
              <tr v-if=isFilterFormShow>
                <th>답변자</th>
                <td>
                  <select v-model="answerLunaNo" style="width:100%">
                    <option value="">전체</option>
                    <option v-for="(val, code) in codeData.lunaAccount" :key="code" :value="code" > {{ val }} </option>
                  </select>
                </td>
                <th>답변여부</th>
                <td colspan="5">
                  <select v-model="answerYn" class="short5">
                    <option value="">전체</option>
                    <option value="1">답변완료</option>
                    <option value="0">미처리</option>
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
            </div>
            <div class="tableWrap tableList tableHover">
              <table class="textCenter" summary="공지사항 조회">
                <caption class="blind">공지사항 조회</caption>
                <colgroup>
                  <col style="width:5%" />
                  <col style="width:10%" />
                  <col style="width:10%" />
                  <col style="width:10%" />
                  <col />
                  <col style="width:8%" />
                  <col style="width:8%" />
                  <col style="width:8%" />
                  <col style="width:7%" />
                </colgroup>
                <thead>
                  <tr>
                    <th>No</th>
                    <th>회원ID</th>
                    <th>법인명</th>
                    <th>상호명</th>
                    <th>제목</th>
                    <th>문의일</th>
                    <th>답변일</th>
                    <th>답변자</th>
                    <th>답변여부</th>
                  </tr>
                </thead>
                <tbody v-if="qnaInfoList.length > 0">
                  <template v-for="(item, idx) in qnaInfoList" >
                    <tr :key="idx">
                      <td>
                        {{ item.qna_no }}
                      </td>
                      <td> {{ item.member_id }} </td>
                      <td> {{ item.biz_name }} </td>
                      <td v-if="item.mall_count > 0"> {{ item.mall_name }} <span v-if="item.mall_count > 1" style="vertical-align: baseline;">외 {{ item.mall_count-1 }}개 </span> </td>
                      <td v-else>-</td>
                      <td style="text-align: left;"> <a @click="qnaEditModal(item.qna_no)" style="font-weight: bold;"> {{ item.question_title }} </a></td>
                      <td v-html="item.create_date.replace(' ', '<br/>')">  </td>
                      <td v-html="item.answer_date != undefined ? item.answer_date.replace(' ', '<br/>') : '-'">  </td>
                      <td v-text="codeData.lunaAccount[item.answer_luna_no] == null ? '-' : codeData.lunaAccount[item.answer_luna_no]"></td>
                      <td v-if="item.answer_yn == 1"> 답변완료 </td>
                      <td v-else style="color:red;font-weight: bold;"> 미처리 </td>
                    </tr>
                  </template>
                </tbody>
                <tbody v-else>
                  <tr>
                    <td colspan="9" v-if="isSearch">검색결과가 없습니다.</td>
                    <td colspan="9" v-else>1:1 문의가 없습니다.</td>
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
      </div>
  </div>
</template>

<script>

import http from '../../../http-commons'
import Pagination from '@/components/common/Pagination'
import DatepickerDate from '@/components/common/DatepickerDate'
import qnaEditModal from '@/components/contentsManage/qna/QnaEditModal'
import ModalAlert from '@/components/common/modal/ModalAlert'
import { eventBus } from '@/main'

export default {
  components: {
    Pagination,
    DatepickerDate
  },
  data () {
    return {
      isSearch: false,
      qnaInfoList: [],
      answerLunaList: [],
      paginationData: {
        total_cnt: 0
      },
      selectedPerPage: 20,
      isFilterFormShow: false,
      fromDate: '',
      toDate: '',
      searchType: '',
      searchText: '',
      answerLunaNo: '',
      answerYn: ''
    }
  },
  created () {
    this.setDateParam()
  },
  mounted () {
    eventBus.$on('qnaAnswer', () => {
      this.get()
    })
  },
  beforeDestroy () {
    eventBus.$off('qnaAnswer')
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
          if (this.searchText !== '' && 'mallName,title,text'.indexOf(this.searchType) > 0) {
            params.searchText = '%' + this.searchText + '%'
          } else {
            params.searchText = this.searchText
          }
        } else {
          this.$modal.show(ModalAlert,
            { title: '', text: '검색어는 최소 2자 이상입니다.' }, { width: 450, height: 'auto' }
          )
          return
        }

        if (this.isFilterFormShow) {
          params.answerLunaNo = this.answerLunaNo
          params.answerYn = this.answerYn
        }
      }

      http
        .post('/qna/list', params)
        .then(result => {
          this.qnaInfoList = result.data.data
          // eslint-disable-next-line no-undef
          this.paginationData.paginate = makePaginate(result.data); this.paginationData.max = pageMaxNumber(result.data)
          this.paginationData.total_cnt = result.data.total_cnt
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
    qnaEditModal (qnaNo) {
      this.$modal.show(qnaEditModal,
        { qnaNo: qnaNo, 'codeData': this.codeData }, { width: 832, height: 522 }
      )
    }
  },
  props: {
    'codeData': Object
  }
}
</script>
