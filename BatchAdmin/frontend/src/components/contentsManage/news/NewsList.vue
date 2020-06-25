<template>
  <div class="content">
      <h1>NEWS 목록</h1>
      <div class="formContainer">
        <!-- <div class="searchWrap">
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
                      </DatepickerDate>
                  </div>
                </td>
              </tr>
              <tr>
                <th>검색구분</th>
                <td colspan="7">
                  <select v-model="newsTypeCode" style="width:10%">
                    <option value="">전체</option>
                    <option value="1">언론보도</option>
                  </select>
                  <input style="width:30%" v-model="searchText" @keydown.enter="get()" type="text"/>
                </td>
              </tr>
            </tbody>
          </table>
          <div class="btnWrap">
            <ul>
              <li>
                <p class="btn_search" @click="isSearch = true; get()">
                  <i class="fas fa-search"></i>검색
                </p>
              </li>
            </ul>
          </div>
        </div> -->
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
                      <a @click="editNews()">
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
                <thead>
                </thead>
                <tbody v-if="newsInfoList.length > 0">
                  <template  v-for="(item, idx) in newsInfoList" >
                    <tr :key="idx">
                      <template v-for="(subItem, subIdx) in item">
                        <td v-if="subItem.newsNo != undefined" :key="subIdx">
                          <table >
                            <tr>
                              <td style="text-align: right; border: none;">
                                <div class="writeContainer">
                                  <div class="titleWrap" style="border: none;padding-bottom: 5px;padding-top: 0px;">
                                    <ul class="infoWrap">
                                      <li class="writerWrap" style="font-size: 15px;">
                                        <i v-if="subItem.displayYn == '0'" class="far fa-eye-slash"></i>
                                        <i v-else class="far fa-eye"></i>
                                      </li>
                                      <li class="timeWrap" style="font-size: 13px;">
                                        {{ subItem.displayStartDate }} ~ <span style="vertical-align: baseline;" v-if="subItem.displayEndDate !='9999-12-31'">{{ subItem.displayEndDate }}</span>
                                      </li>
                                    </ul>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr>
                              <td style="padding:0px;border:none;text-align: -webkit-center;">
                                <div class="NewsIndex" >
                                  <div class="section newsblock" style="padding: 10px 0px 0px 10px">
                                    <div class="sub-news">
                                      <div class="inner" style="margin-right: 0px;">
                                        <div class="item-wrap">
                                          <div class="item">
                                            <a style="pointer-events: none; cursor: default;">
                                              <img :src="subItem.newsFileAddr" />
                                              <div class="txt-box" style="text-align:left;font-family: NanumBarunGothic,'Noto Sans KR',sans-serif;">
                                                <span class="cate">[{{ codeData.newsType[subItem.newsTypeCode] }}]</span>
                                                <p class="tit">{{ subItem.newsTitle }}</p>
                                              </div>
                                            </a>
                                          </div>
                                        </div>
                                      </div>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                            <tr>
                              <td style="border:none;">
                                <div class="articleWrap">
                                  <div class="tableTopWrap">
                                    <div class="btnWrap" style="float: unset;">
                                      <ul>
                                        <li>
                                          <p class="btn_add">
                                            <a @click="deleteNewsInfoConfirm(subItem.newsNo)">
                                              삭제
                                            </a>
                                          </p>
                                        </li>
                                        <li>
                                          <p class="btn_add">
                                            <a @click="editNews(idx, subIdx)">
                                              수정
                                            </a>
                                          </p>
                                        </li>
                                      </ul>
                                    </div>
                                  </div>
                                </div>
                              </td>
                            </tr>
                          </table>
                        </td>
                        <td v-else :key="subIdx">
                        </td>
                      </template>
                    </tr>
                  </template>
                </tbody>
                <tbody v-else>
                  <tr>
                    <td colspan="9">
                      뉴스가 없습니다.
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="paginate">
              <template v-if="paginationData.total_cnt > 0">
                <div style="width: 70%;display: inline-block;">
                  <div class="text-center" v-if="paginationData.paginate">
                   <div class="paginate">
                      <ul>
                        <li class="page-item" v-for="page in paginationData.paginate" :class="{on: page.active}" :key="page.id">
                          <a
                            :class="{ disabled: page.active === false && page.href === false }"
                            @click="get(page.href, page.per_page)"
                           >
                            <i class="fas fa-angle-left" v-if="page.i == 'prev'"></i>
                            <i class="fas fa-angle-right" v-else-if="page.i == 'next'"></i>
                            <span v-else>{{ page.i }}</span>
                          </a>
                        </li>
                      </ul>
                    </div>
                  </div>
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
import ModalConfirm from '@/components/common/modal/ModalConfirm'
import { eventBus } from '@/main'
import('@/styles/newinfo.css')

export default {
  components: {
    Pagination,
    DatepickerDate
  },
  data () {
    return {
      isSearch: false,
      newsInfoList: [],
      paginationData: {
        total_cnt: 0
      },
      selectedPerPage: 15,
      isFilterFormShow: false,
      fromDate: '',
      toDate: '',
      newsTypeCode: '',
      searchText: ''
    }
  },
  created () {
    this.setDateParam()
  },
  mounted () {
    eventBus.$on('deleteNewsInfo', () => {
      this.get()
    })
  },
  beforeDestroy () {
    eventBus.$off('deleteNewsInfo')
  },
  methods: {
    get (page = 1, perPage = 15) {
      let params = {}
      params.page = page
      params.perPage = this.selectedPerPage
      if (this.isSearch) {
        // eslint-disable-next-line no-undef
        params.fromDate = dateFormatToString(this.fromDate); params.toDate = dateFormatToString(this.toDate)

        params.newsTypeCode = this.newsTypeCode
        if (params.searchText !== '') {
          params.searchText = '%' + this.searchText + '%'
        }
      }

      http
        .post('/newsInfo/getNewsInfoList', params)
        .then(result => {
          // this.newsInfoList = result.data.data
          this.newsInfoList = []
          const emptyTableSize = Math.ceil(result.data.data.length / 3) * 3 - result.data.data.length

          // console.log(emptyTableSize)
          let subList = []

          for (const idx in result.data.data) {
            subList.push(result.data.data[idx])
            if ((Number(idx) + 1) % 3 === 0) {
              this.newsInfoList.push(subList)
              subList = []
            } else if ((Number(idx) + 1) === result.data.data.length) {
              for (let i = 0; i < emptyTableSize; i++) {
                subList.push({})
              }
              this.newsInfoList.push(subList)
              subList = []
            }
          }

          // eslint-disable-next-line no-undef
          this.paginationData.paginate = makePaginate(result.data); this.paginationData.max = pageMaxNumber(result.data)
          this.paginationData.total_cnt = result.data.total_cnt
        }).catch(e => {
          console.log(e)
        })
    },
    deleteNewsInfoConfirm (newsNo) {
      this.$modal.show(
        ModalConfirm,
        {
          title: '뉴스 삭제',
          data: {
            url: '/newsInfo/deleteNewsInfo',
            item: {
              newsNo: newsNo
            },
            refresh: 'deleteNewsInfo'
          }
        },
        {
          width: 450,
          height: 'auto'
        }
      )
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
    editNews (idx, subIdx) {
      let newsInfo
      if (idx !== undefined && subIdx !== undefined) {
        newsInfo = this.newsInfoList[idx][subIdx]
      }

      this.$router.push({
        name: 'newsEdit',
        params: {'newsInfo': newsInfo}
      })
    }
  },
  props: {
    'codeData': Object
  }
}
</script>
