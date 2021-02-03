<template>
  <div class="BatchList">
    <div class="content employ">
      <h1> 배치 관리
        <template v-if="realLunaTeamNo === 19">
          <select v-model="lunaTeamNo" style="width:10%" @change="teamChange()">
            <template v-for="(val, code) in codeData.lunaTeamInfo">
              <option :key="code" :value="Number(val.luna_team_no)" v-if="val.luna_dept_no === '2' || val.luna_dept_no === '12'">{{ val.luna_team_name }}</option>
            </template>
            <option value="999">리로드</option>
            <option value="9999">스마트플러스 테스트</option>
          </select>
        </template>
      </h1>
      <div class="formContainer">
        <div class="tapContiner">
          <div class="searchWrap">
            <table summary="배치 조회">
              <caption class="blind">배치 조회</caption>
              <colgroup>
                <col style="width: 10%;" />
                <col style="width: 15%;" />
                <col style="width: 10%;" />
                <col style="width: 15%" />
                <col style="width: 50%;" />
              </colgroup>
              <tbody>
              <tr>
                <td colspan="5">
                  <p class="btn_search" @click="createGroupModalOpen" >그룹 관리</p>
                </td>
              </tr>
              <tr>
                <th>검색구분</th>
                <td>
                  <select v-model="searchType" style="width:80%">
                    <option :value="'1'">이름</option>
                    <option :value="'2'">호출 URL</option>
                  </select>
                </td>
                <th>동작여부</th>
                <td>
                  <select v-model="searchUseYn" style="width:80%">
                    <option value="">전체</option>
                    <option :value="'1'">Y</option>
                    <option :value="'0'">N</option>
                  </select>
                </td>
                <td style="border-left: hidden;"></td>
              </tr>
              <tr>
                <th>검색그룹</th>
                <td colspan="3">
                  <p class="searchWordWrap short3">
                    <select v-model="searchGroup" style="width:25%">
                      <option value="">전체</option>
                      <option v-for="(item, idx) in batchGroupList" :key="idx" :value="item.batch_group_no" > {{item.batch_group_name}}</option>
                    </select>
                    <input type="text" v-model="searchText" @keydown.enter="searchBatch" style="width:65%" />
                  </p>
                </td>
                <td style="text-align: right; border-left: hidden;">
                  <p class="btn_search" @click="searchBatch" >
                    <i class="fas fa-search"></i>검색
                  </p>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
          <div class="tabContainer">
            <ul class="tabBtnWrap">
              <li class="tabBtn" :class="{'on': isGroupSelected('')}" @click="getbatchList()">전체</li>
              <li class="tabBtn" :class="{'on': isGroupSelected(item.batch_group_no)}" v-for="(item, idx) in batchGroupList" :key="idx" @click="getbatchList(item.batch_group_no)" > {{item.batch_group_name}}</li>
            </ul>
            <ul class="tabBtnWrap">
            </ul>
          </div>
          <article class="searchResult">
            <div class="articleWrap">
              <div class="tableTopWrap" style="padding-bottom:10px">
                <div class="btnWrap" style="float:left">
                  <ul>
                    <li>
                      <p class="btn_add" @click="createNewBatch()">추가</p>
                      <p class="btn_add" @click="deleteBatch()">삭제 </p>
                    </li>
                  </ul>
                </div>
                <template v-if="paginationData.total_cnt > 0">
                  <div style="width: 70%;display: inline-block;">
                    <Pagination
                      :paginate=paginationData.paginate
                      :action="get"
                      :isPropPageCntShow="true"
                      :propPageCnt=selectedPerPage
                      :propStyle="'clear: none;padding: 0px;padding-left:150px;'"
                    ></Pagination>
                  </div>
                </template>
                <div class="btnWrap">
                  <ul>
                    <li>
                      <p class="btn_add" @click="modify()">체크 항목 저장</p>
                    </li>
                  </ul>
                </div>
              </div>
              <div class="tableWrap" style="padding-bottom:70px;">
                <table class="textCenter" summary="배치리스트">
                  <caption class="blind">배치리스트</caption>
                  <colgroup>
                    <col style="width:3%" />
                    <col style="width:18%" />
                    <col style="width:7%" />
                    <col style="width:7%" />
                    <col style="width:10%" />
                    <col style="width:11%" />
                    <col style="width:8%" />
                    <col style="width:29%" />
                    <col style="width:5%" />
                  </colgroup>
                  <thead>
                  <tr>
                    <th>
                        <span class="inputWrap checkboxCenter">
                          <input type="checkbox" id="checkAll" @change="checkAll" v-model="allSelected" >
                          <label for="checkAll"></label>
                        </span>
                    </th>
                    <th>이름</th>
                    <th>실행주기(sec)</th>
                    <th>동작여부</th>
                    <th>
                      시작타입 <i id="tooltip-target-1" class="far fa-question-circle"></i>
                    </th>
                    <th>시간포맷</th>
                    <th>시작시간</th>
                    <th>호출 URL</th>
                    <th>실행로그</th>
                  </tr>
                  </thead>
                  <tbody>
                  <template v-for="(item, idx) in batchInfoList" >
                    <tr :key="idx" :style="[initBatchInfoList[idx].use_yn === '1' ? {'background':'#e4f0ff'}:{}]">
                      <td class="textCenter tdLine">
                        <div :key="idx + 'way'" v-if="idx == 8" v-waypoint="{ active: true, callback: onWaypoint}" ></div>
                        <span class="inputWrap checkboxCenter">
                          <input type="checkbox" :id="'batchInfo-' + idx"
                                 v-model="item.selected" @change="check()">
                          <label :for="'batchInfo-' + idx"></label>
                        </span>
                      </td>
                      <td>
                        <input type="text" v-if="item.batch_no !==''" v-model="item.batch_name" style="width: 95%;" />
                        <input type="text" v-else-if="item.batch_no ===''" v-model="item.batch_name" style="width: 95%;" v-focus/>
                      </td>
                      <td>
                        <input type="text" v-model="item.batch_cycle_sec" style="width: 95%;" />
                      </td>
                      <td>
                        <select v-model="item.use_yn" style="width: 95%;">
                          <option :value="1" >Y</option>
                          <option :value="0" >N</option>
                        </select>
                      </td>
                      <td>
                        <select v-model="item.batch_cycle_type_code" style="width: 95%;">
                          <template v-for="(val, code) in codeData.batchCycleTypeCode">
                            <option :key="code" :value="code">{{ val }}</option>
                          </template>
                        </select>
                      </td>
                      <td>
                        <select v-model="item.batch_time_type_code" style="width: 95%;">
                          <option></option>
                          <template v-for="(val, code) in codeData.batchTimeTypeCode">
                            <option :key="code" :value="code">{{ val }}</option>
                          </template>
                        </select>
                      </td>
                      <td>
                        <input type="text" v-model="item.batch_start_time" style="width: 95%;" />
                      </td>
                      <td>
                        <input type="text" v-model="item.batch_call_url_addr" style="width: 95%;"/>
                      </td>
                      <td>
                        <div class="tableTopWrap" >
                          <div class="btnWrap" style="width:90%">
                            <ul>
                              <li style="width:100%">
                                <p class="btn_add" style="width:100%" @click="batchLogModal(item.batch_no)">로그</p>
                              </li>
                            </ul>
                          </div>
                        </div>
                      </td>
                    </tr>
                  </template>
                  </tbody>
                </table>
              </div>
            </div>
          </article>
        </div>
      </div>
    </div>
    <b-tooltip target="tooltip-target-1" triggers="hover">
      <div style="text-align: left; font-size: 15px; background-color:white; padding:10px; border: 1px solid black; border-radius: 10px;">
        배치 시작 타입
        <br/>
        instant : 즉시 실행 후 주기에 따라 반복 실행
        <br/>
        delay : 지정된 시작시간에 맞춰 시작 후 주기에 따라 반복 실행
        <br/>
        instant only once : 즉시 실행 1회 후 종료
        <br/>
        delay only once : 지정된 시작시간에 맞춰 1회 실행 후 종료
      </div>
    </b-tooltip>
    <div class="bootstrapWrapper" v-show="isScrollBtnShow" style="top:93%">
      <button type="button" class="btn btn-primary btn-lg" @click="createNewBatch()"
              style="background-color: #37a8fe;border-color: #37a8fe;"
      >추가</button>
      <button type="button" class="btn btn-primary btn-lg" @click="modify()"
              style="background-color: #37a8fe;border-color: #37a8fe;"
      >체크 항목 저장</button>
    </div>
  </div>
</template>

<script>

import { eventBus } from '@/main'
import http from '../../http-commons'
import store from '../../vuex/store'
import ModalAlert from '@/components/common/modal/ModalAlert'
import CreateBatchGroupModal from '@/components/batch/CreateBatchGroupModal'
import BatchLogModal from '@/components/batch/BatchLogModal'
import ModalBatchErrorTable from '@/components/common/modal/ModalBatchErrorTable'
import Pagination from '@/components/common/Pagination'

export default {
  components: {
    Pagination
  },
  mounted () {
    this.getbatchList()
    this.getbatchGroupList()
    eventBus.$on('saveBatchGroup', (result) => {
      setTimeout(() => {
        this.getbatchGroupList()
        this.getbatchList()
      }, 100)
    })
  },
  beforeDestroy () {
    eventBus.$off('saveBatchGroup')
  },
  data () {
    return {
      lunaNo: store.getters.getUser.luna_no,
      lunaTeamNo: store.getters.getUser.luna_team_no,
      realLunaTeamNo: store.getters.getUser.luna_team_no,
      batchInfoList: [],
      initBatchInfoList: [],
      batchGroupList: [],
      allSelected: false,
      selectedGroupNo: '',
      searchType: '1',
      searchUseYn: '',
      searchText: '',
      searchGroup: '',
      paginationData: {
        total_cnt: 0
      },
      selectedPerPage: 0,
      selectedPage: 0,
      isSearch: false,
      isScrollBtnShow: false
    }
  },
  watch: {
    'batchInfoList': {
      deep: true,
      handler (newVal, oldVal) {
        // console.log(newVal === oldVal)

        if (oldVal !== undefined && oldVal.length > 0) { // 페이지 로딩시 무조건 값 세팅되는것 방지
          // isChangedData 를 false 로 변경시 isResetData를 true 로 변경해야 handler 사용 가능
          let initDataList = this.initBatchInfoList
          let dataList = this.batchInfoList
          // let oldValInner = oldVal
          newVal.filter(function (p, idx) {
            return Object.keys(p).some(function (prop) {
              if (prop === 'selected') return true

              if (prop === 'changed') { // 변경되었던 데이터만 changed 라는 객체로 잇어서 changed 가 있는 데이터들만 삭제
                delete dataList[idx]['changed']
                delete dataList[idx]['selected']
                // selected.splice(idx, 1)
              } else {
                if (p[prop] !== initDataList[idx][prop]) { /** 여기가 데이터 변경되었을 경우의 분기 */
                  /** 상품이름 변경시 체크 끝 */
                  dataList[idx]['changed'] = true
                  let batchNo = dataList[idx]['batch_no'].toString()
                  dataList[idx]['selected'] = batchNo
                  return true
                }
              }
            })
          })
          // Log it
        }
      }
    }
  },
  directives: {
    focus: {
      // 디렉티브 정의
      inserted: function (el) {
        el.focus()
      }
    }
  },
  methods: {
    teamChange () {
      this.getbatchList()
      this.getbatchGroupList()
    },
    onWaypoint ({ going, direction }) {
      // going: in, out
      // direction: top, right, bottom, left
      if (direction === this.$waypointMap.DIRECTION_BOTTOM && going === this.$waypointMap.GOING_OUT) { // 위쪽으로 스크롤 및 위로 포인트 지점 지날때
        this.isScrollBtnShow = false
      }
      if (direction === this.$waypointMap.DIRECTION_TOP) { // 아래쪽으로 스크롤
        this.isScrollBtnShow = true
      }
    },
    batchLogModal (batchNo) {
      this.$modal.show(BatchLogModal,
        { batchNo: batchNo }, { width: 1000, height: 'auto' }
      )
    },
    async get (page, perPage) {
      let batchInfo = {}
      batchInfo.luna_team_no = this.lunaTeamNo
      batchInfo.batch_group_no = this.selectedGroupNo
      batchInfo.page = page
      batchInfo.perPage = perPage
      this.selectedPerPage = Number(perPage)
      this.selectedPage = page

      if (this.isSearch) {
        batchInfo.use_yn = this.searchUseYn
        if (this.searchType === '1') {
          batchInfo.batch_name = '%' + this.searchText + '%'
        } else if (this.searchType === '2') {
          batchInfo.batch_call_url_addr = '%' + this.searchText + '%'
        }
        this.selectedGroupNo = this.searchGroup
      }
      return this.getBatchListSend(batchInfo)
    },
    getBatchListSend (batchInfo) {
      return new Promise(resolve => {
        http
          .post('/batchManage/batchList', JSON.stringify(batchInfo))
          .then(result => {
            resolve(result)
            // eslint-disable-next-line no-undef
            this.paginationData.paginate = makePaginate(result.data); this.paginationData.max = pageMaxNumber(result.data)
            this.paginationData.total_cnt = result.data.total_cnt
            this.batchInfoList = result.data.data
            this.initBatchInfoList = JSON.parse(JSON.stringify(result.data.data))
          })
          .catch(e => {
            console.log(e)
          })
      })
    },
    getbatchList (groupNo = '') {
      this.allSelected = false
      this.selectedGroupNo = groupNo

      let batchInfo = {}
      batchInfo.luna_team_no = this.lunaTeamNo
      batchInfo.batch_group_no = groupNo
      batchInfo.page = 1
      this.selectedPage = 1
      if (this.selectedPerPage === 0) {
        batchInfo.perPage = 20
        this.selectedPerPage = 20
      } else {
        batchInfo.perPage = this.selectedPerPage
      }

      this.isSearch = false

      this.getBatchListSend(batchInfo)
    },
    getbatchGroupList () {
      let form = new FormData()
      form.append('lunaTeamNo', this.lunaTeamNo)

      http
        .post('/batchManage/batchGroupList', form)
        .then(result => {
          this.batchGroupList = result.data
        })
        .catch(e => {
          console.log(e)
        })
    },
    isGroupSelected (groupNo) {
      if (groupNo === this.selectedGroupNo) {
        return true
      } else {
        return false
      }
    },
    searchBatch () {
      let batchInfo = {}
      batchInfo.luna_team_no = this.lunaTeamNo
      batchInfo.batch_group_no = this.searchGroup

      batchInfo.use_yn = this.searchUseYn
      if (this.searchType === '1') {
        batchInfo.batch_name = '%' + this.searchText + '%'
      } else if (this.searchType === '2') {
        batchInfo.batch_call_url_addr = '%' + this.searchText + '%'
      }

      batchInfo.page = 1
      if (this.selectedPerPage === 0) {
        batchInfo.perPage = 20
        this.selectedPerPage = 20
      } else {
        batchInfo.perPage = this.selectedPerPage
      }

      this.selectedGroupNo = this.searchGroup
      this.isSearch = true
      http
        .post('/batchManage/batchList', JSON.stringify(batchInfo))
        .then(response => {
          // eslint-disable-next-line no-undef
          this.paginationData.paginate = makePaginate(response.data); this.paginationData.max = pageMaxNumber(response.data)
          this.paginationData.total_cnt = response.data.total_cnt
          this.batchInfoList = response.data.data
          this.initBatchInfoList = JSON.parse(JSON.stringify(response.data.data))
        }).catch(e => {
          console.log(e)
        })
    },
    check () {
      this.allSelected = false
    },
    checkAll () { // 전체선택 체크박스
      if (this.allSelected) {
        for (const item in this.batchInfoList) {
          this.batchInfoList[item]['selected'] = true
        }
      } else {
        for (const item in this.batchInfoList) {
          delete this.batchInfoList[item]['selected']
        }
      }
    },
    getCheckItemList () {
      let itemList = []

      for (const idx in this.batchInfoList) {
        if (this.batchInfoList[idx]['selected']) {
          let item = {}
          item.batch_no = this.batchInfoList[idx].batch_no
          item.batch_name = this.batchInfoList[idx].batch_name
          if (this.batchInfoList[idx].batch_group_no === '') {
            item.batch_group_no = this.selectedGroupNo
          } else {
            item.batch_group_no = this.batchInfoList[idx].batch_group_no
          }
          item.batch_cycle_sec = this.batchInfoList[idx].batch_cycle_sec
          item.use_yn = this.batchInfoList[idx].use_yn
          item.work_yn = this.batchInfoList[idx].work_yn
          item.batch_cycle_type_code = this.batchInfoList[idx].batch_cycle_type_code
          item.batch_time_type_code = this.batchInfoList[idx].batch_time_type_code
          item.batch_start_time = this.batchInfoList[idx].batch_start_time
          item.batch_call_url_addr = this.batchInfoList[idx].batch_call_url_addr

          if (this.batchInfoList[idx].batch_no === '') {
            item.create_user_code = this.batchInfoList[idx].create_user_code
            item.create_user_no = this.batchInfoList[idx].create_user_no
          } else {
            item.update_user_code = '1'
            item.update_user_no = this.lunaNo
          }

          itemList.push(item)
        }
      }
      return itemList
    },
    getDeleteItemList () {
      let itemList = []

      for (const idx in this.batchInfoList) {
        if (this.batchInfoList[idx]['selected']) {
          let item = {}
          item.idx = idx
          item.batch_no = this.batchInfoList[idx].batch_no
          item.batch_name = this.batchInfoList[idx].batch_name
          if (this.batchInfoList[idx].batch_group_no === '') {
            item.batch_group_no = this.selectedGroupNo
          } else {
            item.batch_group_no = this.batchInfoList[idx].batch_group_no
          }
          item.batch_cycle_sec = this.batchInfoList[idx].batch_cycle_sec
          item.use_yn = this.batchInfoList[idx].use_yn
          item.work_yn = this.batchInfoList[idx].work_yn
          item.batch_cycle_type_code = this.batchInfoList[idx].batch_cycle_type_code
          item.batch_time_type_code = this.batchInfoList[idx].batch_time_type_code
          item.batch_start_time = this.batchInfoList[idx].batch_start_time
          item.batch_call_url_addr = this.batchInfoList[idx].batch_call_url_addr
          itemList.push(item)
        }
      }
      return itemList
    },
    async modify () {
      let modList = this.getCheckItemList()
      if (modList.length === 0) {
        this.$modal.show(ModalAlert,
          { title: '', text: '업데이트 할 배치를 선택하세요' }, { width: 450, height: 'auto' }
        )
        return
      }

      let alertStr = ''
      let alertFlag = false
      for (const idx in modList) {
        if (modList[idx].batch_name === '') {
          alertFlag = true
          alertStr = '이름은 필수 값입니다.'
          break
        } else if (modList[idx].batch_cycle_sec === '') {
          alertFlag = true
          alertStr = '실행주기는 필수 값입니다.'
          break
        } else if (modList[idx].use_yn === '') {
          alertFlag = true
          alertStr = '동작여부는 필수 값입니다.'
          break
        } else if (modList[idx].batch_cycle_type_code === '') {
          alertFlag = true
          alertStr = '시작타입은 필수 값입니다.'
          break
        } else if (modList[idx].batch_cycle_type_code.toString() === '2' || modList[idx].batch_cycle_type_code.toString() === '4') {
          if (modList[idx].batch_time_type_code === '') {
            alertFlag = true
            alertStr = '지연시작(delay)시 시간포맷은 필수 값입니다.'
            break
          } else if (modList[idx].batch_start_time === '') {
            alertFlag = true
            alertStr = '지연시작(delay)시 시작시간은 필수 값입니다.'
            break
          } else if (modList[idx].batch_time_type_code.toString() === '4' && modList[idx].batch_start_time !== '') {

          }
        } else if (modList[idx].batch_call_url_addr === '') {
          alertFlag = true
          alertStr = '호출 URL은 필수 값입니다.'
          break
        }
      }
      if (alertFlag) {
        this.$modal.show(ModalAlert,
          { title: '', html: alertStr }, { width: 450, height: 'auto' }
        )
        return
      }

      let context = this

      await this.modifySend(modList).then(function (result) {
        if (result.data.resultStr === 'success') {
          context.allSelected = false
          context.$modal.show(ModalAlert,
            { title: '', text: '체크 항목 저장에 성공하였습니다.' }, { width: 450, height: 'auto' }
          )
          setTimeout(() => {
            if (context.paginationData.paginate.length > 0) {
              context.get(context.selectedPage, context.selectedPerPage)
            } else {
              context.getbatchList(context.selectedGroupNo)
            }
          }, 100)
        } else {
          context.$modal.show(ModalBatchErrorTable,
            { title: '', list: result.data.responseList }, { width: 800, height: 'auto' }
          )
        }
      })
    },
    async modifySend (modList) {
      return new Promise(resolve =>
        http
          .post('/batchManage/updateBatch', JSON.stringify(modList))
          .then(response => {
            resolve(response)
          })
          .catch(e => {
            this.$modal.show(ModalAlert,
              { title: '', text: '알 수 없는 오류 발생했습니다.' }, { width: 450, height: 'auto' }
            )
          }))
    },
    createGroupModalOpen () {
      this.$modal.show(CreateBatchGroupModal,
        {lunaTeamNo: this.lunaTeamNo}, { width: 700, height: 'auto' }
      )
    },
    createNewBatch () {
      if (this.selectedGroupNo === '') {
        this.$modal.show(ModalAlert,
          { title: '', text: '배치 그룹을 지정해주세요' }, { width: 450, height: 'auto' }
        )
        return
      }

      let context = this
      if (this.paginationData.paginate.length > 0 && this.paginationData.paginate[this.paginationData.paginate.length - 1].href) {
        this.get(this.paginationData.paginate[this.paginationData.paginate.length - 1].href, this.selectedPerPage)
          .then(function () {
            var item = {}
            item.batch_no = ''
            item.batch_name = ''
            item.batch_group_no = context.selectedGroupNo
            item.batch_cycle_sec = ''
            item.use_yn = '0'
            item.work_yn = '1'
            item.batch_cycle_type_code = '1'
            item.batch_start_time = ''
            item.batch_call_url_addr = ''
            item.create_user_code = '1'
            item.create_user_no = context.lunaNo
            item.selected = true
            context.initBatchInfoList.push(item)
            context.batchInfoList.push(item)
          })
      } else {
        var item = {}
        item.batch_no = ''
        item.batch_name = ''
        item.batch_group_no = context.selectedGroupNo
        item.batch_cycle_sec = ''
        item.use_yn = '0'
        item.work_yn = '1'
        item.batch_cycle_type_code = '1'
        item.batch_start_time = ''
        item.batch_call_url_addr = ''
        item.create_user_code = '1'
        item.create_user_no = context.lunaNo
        item.selected = true
        context.initBatchInfoList.push(item)
        context.batchInfoList.push(item)
      }
    },
    async deleteBatch (batchNo) {
      let delList = this.getDeleteItemList()

      if (delList.length === 0) {
        this.$modal.show(ModalAlert,
          { title: '', text: '삭제 할 배치를 선택하세요' }, { width: 450, height: 'auto' }
        )
        return
      }

      let context = this

      await this.deleteBatchSend(delList).then(function (result) {
        if (result.data === 'success') {
          for (const idx in delList.reverse()) {
            context.batchInfoList.splice(delList[idx].idx, 1)
          }
          context.$modal.show(ModalAlert,
            { title: '', text: '체크 항목 삭제에 성공하였습니다.' }, { width: 450, height: 'auto' }
          )
        } else {
          context.$modal.show(ModalAlert,
            { title: '', html: result.data }, { width: 450, height: 'auto' }
          )
        }
        setTimeout(() => {
          context.getbatchList(context.selectedGroupNo)
        }, 100)
      })
    },
    async deleteBatchSend (delList) {
      return new Promise(resolve => {
        http
          .post('/batchManage/deleteBatch', JSON.stringify(delList))
          .then(response => {
            resolve(response)
          })
          .catch(e => {
            console.log(e)
          })
      })
    }
  },
  props: {
    'userMenuAuth': Object,
    'codeData': Object
  }
}
</script>

<style scoped lang="scss">
  .balloon {
    display: inline-block;
    position: relative;
    background: #ccc;
    height: 50px;
    width: 120px;
    margin: 0 auto 10px;
  }
  .balloon:after {
    content: '';
    position: absolute;
    border-top: 10px solid #ccc;
    border-right: 5px solid transparent;
    border-left: 5px solid transparent;
    bottom: -9px;
    left: 5px;
  }

  .bootstrapWrapper {
    position: fixed;
    right: 40px;
    top: 90%;
  }
</style>
