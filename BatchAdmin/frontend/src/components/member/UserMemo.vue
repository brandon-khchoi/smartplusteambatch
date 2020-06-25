<template>
  <div class="windowPopupWrap">
    <section class="userMemo">
      <article>
        <div class="formContainer">
          <form action="#" method="post">
            <div class="searchWrap">
              <table summary="회원메모 조회">
                <caption class="blind">회원메모 조회</caption>
                <colgroup>
                  <col style="width:100px">
                  <col>
                </colgroup>
                <tbody>
                  <tr>
                    <th>기간검색</th>
                    <td>
                      <DatepickerDate v-bind:propFromDate="fromDate" v-bind:propToDate="toDate"
                        v-on:update:fromDate="setFromDate" v-on:update:toDate="setToDate">
                        <!-- 자식이 부모한테 보낼때 v-on 및 메소드명을 뒤에 사용해서 올려줌 -->
                      </DatepickerDate>
                    </td>
                  </tr>
                  <tr>
                    <th>메모내용</th>
                    <td>
                      <p class="searchWordWrap">
                        <input type="text" @keyup.enter="get()" v-model="searchParam.searchTxt" minlength="2"
                          style="width:calc(50% + 40px)" />
                      </p>
                    </td>
                  </tr>
                  <tr>
                    <th>법인/상호</th>
                    <td>
                      <p class="searchWordWrap">
                        <select @change="onChangeSearch">
                            <option value="">전체</option>
                        <template v-for="item in selectData" >
                            <option v-if="item.member_no != undefined" :value="item.member_no" :key="item.id">{{item.member_biz_name}}</option>
                        </template>
                        <template v-for="item in selectData" >
                            <option v-if="item.mall_no != undefined" :value="item.mall_no" :key="item.id">{{item.mall_name}}</option>
                        </template>
                      </select>
                      </p>
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

            <article class="userComment">
              <div class="articleWrap" >
                <div class="tableTopWrap">
                  <p class="title">메모 이력</p>
                </div>
                <ul v-if="memoCnt ">
                  <template v-for="item in list.data">
                  <li :key="item.id">
                    <div class="listTopWrap">
                      <div class="titleWrap">
                        <template v-if="item.edit">
                          <template v-if="item.mall_no == 0">
                            <!-- 법인에 대한 메모일 경우 -->
                            <select v-model="updateMemo.memberNo" @change="onChangeUpt">
                              <option :value="item.member_no">{{memberBizName}}</option>
                              <template v-for="select in selectData">
                                <option v-if="select.mall_no != undefined" :value="select.mall_no" :key="select.id">
                                  {{select.mall_name}}</option>
                              </template>
                            </select>
                          </template>
                          <template v-else>
                            <!-- 몰에 대한 메모일 경우 -->
                            <select v-model="updateMemo.mallNo" @change="onChangeUpt">
                              <option :value="item.mall_no">{{item.mall_name}}</option>
                              <template v-for="select in selectData">
                                <option v-if="select.member_no != undefined" :value="select.member_no"
                                  :key="select.id">{{select.member_biz_name}}</option>
                              </template>
                              <template v-for="select in selectData">
                                <option v-if="select.mall_no != undefined && select.mall_no != item.mall_no"
                                  :value="select.mall_no" :key="select.id">{{select.mall_name}}</option>
                              </template>
                            </select>
                          </template>
                        </template>
                        <template v-else>
                          <p v-if="item.mall_no == 0">
                            {{memberBizName}}
                          </p>
                          <p v-else>
                            {{item.mall_name}}
                          </p>
                        </template>
                        <p>
                          {{item.luna_kor_name}}
                        </p>
                        <span class="date">
                          {{item.create_date}}
                        </span>
                      </div>
                      <div class="btnWrap">
                        <ul>
                          <li>
                            <p class="btn_min" v-if="userMenuAuth.delete_data" @click="editDel(item)">
                              <i class="fas fa-pen"></i>삭제
                            </p>
                            <p class="btn_min" v-else>
                              <i class="fas fa-pen"></i>삭제
                            </p>
                          </li>
                          <li>
                            <template v-if="userMenuAuth.modify_data">
                            <p class="btn_min" @click="editOpen(item)" v-if="!item.edit">
                              <i class="fas fa-pen"></i>수정
                            </p>
                            </template>
                            <template v-else>
                            <p class="btn_min">
                              <i class="fas fa-pen"></i>수정
                            </p>
                            </template>
                          </li>
                        </ul>
                      </div>
                    </div>
                    <div class="memoWrap">
                      <div class="text-left" rows="4" disabled v-if="!item.edit" v-text="item.memo_text" />
                      <textarea rows="4" v-else v-text="item.memo_text" v-model="item.memo_text" />
                      <div class="btnWrap" v-if="item.edit">
                          <ul>
                              <li>
                                  <p class="btn_min" @click="editCancel(item)">
                                      <i class="fas fa-times"></i>취소
                                  </p>
                              </li>
                              <li>
                                  <p class="btn_min" @click="editSet(item)">
                                      <i class="fas fa-check"></i>적용
                                  </p>
                              </li>
                          </ul>
                      </div>
                      </div>
                  </li>
                  </template>
                </ul>
                <ul v-else>
                  <li>
                    <div class="listTopWrap">
                      <div class="titleWrap">
                        조회된 메모가 없습니다.
                      </div>
                    </div>
                  </li>
                </ul>
                <Pagination v-if="memoCnt"
                    :paginate="list.paginate"
                    :action="get"
                    :isPropPageCntShow="false"
                ></Pagination>
              </div>
              <div class="articleWrap">
                    <div class="tableTopWrap">
                        <p class="title">메모 입력</p>
                    </div>
                    <ul>
                        <li>
                <div class="listTopWrap">
                    <div class="commentWrap">
                      <span>법인/상호명 &nbsp;&nbsp;</span>
                      <select v-model="createMemo.memberNo" @change="onChange" >
                        <template v-for="item in selectData" >
                            <option v-if="item.member_no != undefined" :value="item.member_no" :key="item.id">{{item.member_biz_name}}</option>
                        </template>
                        <template v-for="item in selectData" >
                            <option v-if="item.mall_no != undefined" :value="item.mall_no" :key="item.id">{{item.mall_name}}</option>
                        </template>
                      </select>
                    </div>
                    <div class="commentWrap">
                        <textarea rows="4" placeholder="내용을 입력해 주세요.(300자)" v-model="createMemo.memoText" ></textarea>
                        <input class="btn_save" type="button" value="등록" v-if="userMenuAuth.create_data" @click="create">
                        <input class="btn_save" type="button" value="등록" v-else>
                    </div>
                </div>
              </li>
              </ul>
        </div>
      </article>
      </form>
  </div>
  </article>
  <v-dialog />
  </section>
  <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import http from '@/http-commons'
import Pagination from '@/components/common/Pagination'
import DatepickerDate from '@/components/common/DatepickerDate'

import ModalAlert from '@/components/common/modal/ModalAlert'

export default {
  name: 'UserMemo',
  components: {
    Pagination,
    DatepickerDate
  },
  data () {
    return {
      isLoading: false,
      idDelConfirmed: false,
      fromDate: '',
      toDate: '',
      memberNo: this.member_no,
      memberBizName: this.member_biz_name,
      userNo: this.$store.getters.getUser.luna_no,
      memoCnt: 0,
      list: {
        total_cnt: 0,
        data: []
      },
      selectData: '',
      searchParam: {
        searchFlag: '1', // 기본 전체 select box
        searchTxt: '', // 검색부분
        mallNo: '',
        isMall: false
      },
      createMemo: { // 메모생성시 필요한 데이터
        memberNo: this.member_no,
        mallNo: '',
        memoText: '',
        isMall: false
      },
      updateMemo: {
        memberNo: this.member_no,
        mallNo: '',
        isMall: false
      }
    }
  },
  props: {
    'userMenuAuth': Object,
    'member_no': String,
    'member_biz_name': String
  },
  mounted () {
    console.log('this is UserMemo.vue mounted')
    console.log(this.memberBizName)
  },
  created () {
    let toDateTemp = new Date()
    this.toDate = new Date(toDateTemp)
    this.fromDate = new Date(toDateTemp.setMonth(toDateTemp.getMonth() - 12))
    this.get()
    this.fetchData()
  },
  methods: {
    /** 자식이 부모한테 보내준 데이터 세팅 */
    setFromDate (value) {
      this.fromDate = value
    },
    setToDate (value) {
      this.toDate = value
    },
    get (current_page = 1, per_page = 2) {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true

        if (this.searchParam.searchTxt === '') {
          this.searchParam.searchTxt = null
        }

        // eslint-disable-next-line no-undef
        const searhFormDate = dateFormatToString(this.fromDate)
        // eslint-disable-next-line no-undef
        const searhToDate = dateFormatToString(this.toDate)
        let mallNo = ''
        if (this.searchParam.searchFlag === 2) { // 2인 경우는 mall_no가 0 인경우
          if (this.searchParam.isMall) {
            mallNo = this.searchParam.mallNo
          } else {
            mallNo = 0
          }
        }

        http
          .get('/member/memolist', {
            params: { // query string
              from_date: searhFormDate,
              to_date: searhToDate,
              member_no: this.memberNo,
              search_flag: this.searchParam.searchFlag,
              search_txt: this.searchParam.searchTxt,
              mall_no: mallNo,
              current_page: current_page,
              per_page: per_page
            }
          })
          .then(response => {
            this.isLoading = false
            this.list = response.data
            this.memoCnt = response.data.data.length
            // eslint-disable-next-line no-undef
            this.list.paginate = makePaginate(response.data)
          })
          .catch(e => {
            console.log(e)
            if (e.response.data.data !== 'undefined') {
              this.$modal.show(ModalAlert,
                { title: '메모 조회', text: '서버 오류' }, { width: 450, height: 'auto' }
              )
            }
          })
          .finally(() => {
            this.isLoading = false
          })
      }
    },
    create () {
      let form = new FormData()

      if (this.createMemo.isMall) {
        form.append('mall_no', this.createMemo.mallNo)
      }
      form.append('member_no', this.memberNo)

      form.append('memo_text', this.createMemo.memoText)
      form.append('user_no', this.userNo)
      form.append('user_code', 1)

      http
        .post('/member/memo/create', form)
        .then(response => {
          this.$modal.show(ModalAlert, {
            title: '메모등록',
            text: '메모를 등록하였습니다.'
          }, {
            width: 450,
            height: 'auto'
          })
          this.get()
          this.createMemo.memoText = '' // 초기화
        })
        .catch(e => {
          console.log(e)
          if (e.response.data.data !== 'undefined') {
            this.$modal.show(ModalAlert,
              { title: '메모등록', text: '서버 오류' },
              { width: 450, height: 'auto' })
          } else {
            this.$modal.show(ModalAlert,
              { title: '메모 수정', text: '서버 오류' },
              { width: 450, height: 'auto' }
            )
          }
        })
    },
    fetchData () {
      console.log(this.memberNo)
      http
        .get('/member/memo/selectbox', {
          params: { // query string
            member_no: this.memberNo
          }
        })
        .then(response => {
          this.selectData = response.data.data
        })
        .catch(e => {
          console.log(e)
        })
    },
    editOpen (item) {
      item.edit = true
      this.updateMemo.mallNo = item.mall_no
      item.origin = item.memo_text
      this.$forceUpdate()
    },
    editCancel (item) {
      item.edit = false
      item.memo_text = item.origin
      this.$forceUpdate()
    },
    editSet (item) {
      let form = new FormData()
      if (this.updateMemo.isMall) {
        form.append('mall_no', this.updateMemo.mallNo)
      }
      form.append('member_memo_no', item.member_memo_no)
      form.append('member_no', this.memberNo)
      form.append('memo_text', item.memo_text)
      form.append('user_no', this.userNo)
      form.append('user_code', 1)
      form.append('del_yn', 0)
      http
        .post('/member/memo/update', form)
        .then(response => {
          console.log(response)
          this.$modal.show(ModalAlert,
            { title: '메모수정', text: '메모를 수정하였습니다.' },
            { width: 450, height: 'auto' }
          )
          this.get()
        })
        .catch(e => {
          console.log(e)
          if (e.response.data.data !== 'undefined') {
            this.$modal.show(ModalAlert,
              { title: '메모 수정', text: '서버 오류' },
              { width: 450, height: 'auto' })
          } else {
            this.$modal.show(ModalAlert,
              { title: '메모 수정', text: '서버 오류' },
              { width: 450, height: 'auto' })
          }
        })
    },
    editDel (item) {
      this.$modal.show('dialog', {
        title: '메모 삭제',
        text: '메모를 삭제하겠습니까?',
        buttons: [{
          title: '취소'
        },
        {
          title: '확인', // Button title
          default: true, // Will be triggered by default if 'Enter' pressed.
          handler: () => {
            this.idDelConfirmed = true
            this.delSummit(item)
            this.$modal.hide('dialog')
          } // Button click handler
        }
        ]
      })
    },
    delSummit (item) {
      this.idDelConfirmed = false
      let form = new FormData()

      form.append('member_memo_no', item.member_memo_no)
      form.append('member_no', this.memberNo)
      form.append('mall_no', item.mall_no)
      form.append('memo_text', item.memo_text)
      form.append('user_no', this.userNo)
      form.append('user_code', 1)
      form.append('del_yn', 1)

      http
        .post('/member/memo/update', form)
        .then(response => {
          console.log(response)
          this.$modal.show(ModalAlert,
            { title: '메모삭제', text: '메모를 삭제하였습니다.' },
            { width: 450, height: 'auto' }
          )
          this.get()
        })
        .catch(e => {
          console.log(e)
          if (e.response.data.data !== 'undefined') {
            this.$modal.show(ModalAlert,
              { title: '메모 삭제', text: '서버 오류' },
              { width: 450, height: 'auto' }
            )
          } else {
            this.$modal.show(ModalAlert,
              { title: '메모 삭제', text: '서버 오류' },
              { width: 450, height: 'auto' }
            )
          }
        })
    },
    onChangeSearch () {
      console.log(event.target.value)
      if (event.target.value === '') { // 전체를 선택한 경우
        this.searchParam.searchFlag = 1
        this.searchParam.isMall = false
      } else { // 전체가 아닌 select 를 선택한 경우
        if (this.memberNo === event.target.value) {
          this.searchParam.isMall = false
          this.searchParam.searchFlag = 2
        } else {
          this.searchParam.mallNo = event.target.value
          this.searchParam.isMall = true
          this.searchParam.searchFlag = 2
        }
      }
    },
    onChange () {
      console.log(event.target.value)
      if (event.target.options[0].value === event.target.value) {
        this.createMemo.isMall = false
      } else {
        this.createMemo.mallNo = event.target.value
        this.createMemo.isMall = true
      }
    },
    onChangeUpt () {
      console.log(event.target.value)
      // eslint-disable-next-line eqeqeq
      if (this.memberNo == event.target.value) {
        this.updateMemo.isMall = false
      } else {
        this.updateMemo.mallNo = event.target.value
        this.updateMemo.isMall = true
      }
    }
  }
}

</script>
