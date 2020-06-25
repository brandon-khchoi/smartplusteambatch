<template>
  <div class="content partner">
    <h1>FAQ</h1>
    <div class="formContainer">
      <form action="#" method="post" >
        <div class="searchWrap">
          <table summary="FAQ 조회">
            <caption class="blind">FAQ 조회</caption>
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
                <th>검색기간</th>
                <td colspan="7">
                  <div >
                      <date-picker-date
                      v-bind:propFromDate="fromDate"
                      v-bind:propToDate="toDate"
                      v-on:update:fromDate="setFromDate"
                      v-on:update:toDate="setToDate"
                      >
                      <!-- 자식이 부모한테 보낼때 v-on 및 메소드명을 뒤에 사용해서 올려줌 -->
                      </date-picker-date>
                  </div>
                </td>
              </tr>
              <tr>
                <th>검색구분</th>
                <td colspan="7">
                    <select style="width:10%" v-model="searchParam.searchFlag">
                      <option value="0">전체</option>
                      <option value="1">제목</option>
                      <option value="2">내용</option>
                    </select>
                    <input type="text" style="width: 36%;" minlength="2" maxlength="100" v-model="searchParam.searchTxt" @keydown.enter="get()">
                </td>
              </tr>
              <template v-if="isFilterFormShow === true">
                <tr>
                  <th>분류</th>
                  <td>
                    <select v-model="searchParam.faqType">
                      <option value="">선택하세요</option>
                      <template v-for="(val, code) in codeData.faqType">
                        <option :key="code" :value="code">{{val}}</option>
                      </template>
                    </select>
                  </td>
                  <th>노출위치</th>
                  <td>
                    <select v-model="searchParam.faqDisplay">
                      <option value="">선택하세요</option>
                      <template v-for="(val, code) in codeData.faqDisplay">
                        <option :key="code" :value="code">{{val}}</option>
                      </template>
                    </select>
                  </td>
                  <th>노출여부</th>
                  <td colspan="3">
                    <select v-model="searchParam.faqDisplayYn" class="short3">
                      <option value="">선택하세요</option>
                      <option value="1">노출</option>
                      <option value="0">노출안함</option>
                    </select>
                  </td>
                </tr>
              </template>
            </tbody>
          </table>
          <div class="btnWrap">
            <ul>
              <li>
                <div class="btn_searchToggle" :class="isFilterFormShow===true?'searchOpen':''" @click="funcFilterToggle()"><i class="fas fa-chevron-down arrow"></i><span></span></div>
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
              <p class="total">검색결과 : 총 <span>{{total_cnt}}</span> 건</p>
              <div class="btnWrap">
                <ul>
                  <li v-if="userMenuAuth.create_data">
                    <router-link :to="{ name: 'faqcreate'}">
                      <p class="btn_add"><i class="fas fa-plus"></i>
                      등록</p>
                    </router-link>
                  </li>
                </ul>
              </div>
            </div>
            <div class="tableWrap tableList tableHover">
              <table class="textCenter" summary="FAQ 조회">
                <caption class="blind">FAQ 조회</caption>
                <colgroup>
                  <col style="width:3%" />
                  <col style="width:10%" />
                  <col style="width:30%" />
                  <col style="width:10%" />
                  <col style="width:10%" />
                  <col style="width:12%" />
                  <col style="width:12%" />
                </colgroup>
                <thead>
                  <tr>
                    <th>No</th>
                    <th>분류</th>
                    <th>제목</th>
                    <th>노출위치</th>
                    <th>노출여부</th>
                    <th>등록자</th>
                    <th>등록일</th>
                  </tr>
                </thead>
                <tbody v-if="total_cnt > 0">
                  <template v-for="(item, idx) in list.items">
                    <tr :key="idx">
                      <td class="num">{{item.faq_no}}</td>
                      <td>{{codeData.faqType[item.faq_type_code]}}</td>
                      <td class="textLeft"><router-link :to="{ name: 'faqdetail', params:{faqNo: item.faq_no}}"><strong>{{item.faq_title}}</strong></router-link></td>
                      <td >{{item.faq_display_name}}</td>
                      <td>{{item.display_yn===1?'노출':'노출안함'}}</td>
                      <td v-if="item.create_user_no">{{codeData.lunaAccount[item.create_user_no]}}</td>
                      <td v-else> - </td>
                      <td>{{item.create_date}}</td>
                    </tr>
                  </template>
                </tbody>
                <tbody v-else>
                  <tr>
                    <td colspan="7">검색결과가 없습니다.</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="paginate">
              <pagination
                :paginate="list.paginate"
                :action="get"
                :isPropPageCntShow=true
              ></pagination>
            </div>
          </div>
        </article>
      </form>
    </div>
  </div>
</template>

<script>
import http from '@/http-commons'
import DatepickerDate from '@/components/common/DatepickerDate'
import Pagination from '@/components/common/Pagination'

export default {
  name: 'FaqList',
  components: {
    'date-picker-date': DatepickerDate,
    'pagination': Pagination
  },
  data () {
    return {
      isLoading: false,
      isFilterFormShow: false, // 검색필터 열기 닫기
      fromDate: '',
      toDate: '',
      searchParam: {
        searchFlag: '0',
        searchTxt: '',
        faqType: '',
        faqDisplay: '', // 논리곱 1 2 4
        faqDisplayYn: '',
        per_page: 20
      },
      list: '',
      total_cnt: 0
    }
  },
  created () {
    let toDateTemp = new Date()
    this.toDate = new Date(toDateTemp)
    this.fromDate = new Date(toDateTemp.setMonth(toDateTemp.getMonth() - 3))
    this.get()
  },
  methods: {
    funcFilterToggle () {
      this.isFilterFormShow = !this.isFilterFormShow
    },
    /** 자식이 부모한테 보내준 데이터 세팅 */
    setFromDate (value) {
      this.fromDate = value
    },
    setToDate (value) { /** */
      this.toDate = value
    },
    get (current_page = 1, per_page = 20) {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        http
          .get('/faq/faqlist', {
            params: { // query string
              // eslint-disable-next-line no-undef
              from_date: dateFormatToString(this.fromDate),
              // eslint-disable-next-line no-undef
              to_date: dateFormatToString(this.toDate),
              search_flag: this.searchParam.searchFlag,
              search_txt: this.searchParam.searchTxt,
              faq_type: this.searchParam.faqType,
              faq_display: this.searchParam.faqDisplay,
              faq_display_yn: this.searchParam.faqDisplayYn,
              current_page: current_page,
              per_page: per_page
            }
          })
          .then(response => {
            this.isLoading = false

            this.list = response.data
            this.total_cnt = response.data.pagination.total_cnt
            // eslint-disable-next-line no-undef
            this.list.paginate = makePaginate(response.data.pagination)
          })
          .catch(e => {
            console.log(e)
          })
          .finally(() => {
            this.isLoading = false
          })
      }
    }
  },
  props: {
    'userMenuAuth': Object,
    'codeData': Object
  }
}
</script>
