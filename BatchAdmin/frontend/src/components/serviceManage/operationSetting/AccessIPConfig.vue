<template>
    <div class="Paper">
            <div class="formContainer">
                <form action="#" method="post">
                    <article v-show="isModify === false">
                        <div class="articleWrap">
                            <div class="paperDetailWrap">
                                <div class="titleWrap">
                                  <p class="title">접근 IP주소 설정</p>
                                </div>
                                <div class="detailContent">
                                    <div class="tableWrap">
                                        <table summary="접근 IP주소 설정">
                                            <caption class="blind">접근 IP주소 설정</caption>
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
                                              <template>
                                                <tr>
                                                  <th>접근허용 <p>IP주소</p></th>
                                                  <td colspan="7">
                                                    <template v-for="item in resultData">
                                                      <ul :key="item.id" :value="item.id">
                                                        <li>
                                                          <span style="width:100px;">
                                                            <strong>{{item.ip}}</strong>
                                                          </span>
                                                          <span>
                                                            {{'(등록일시 : '+item.creatDate+')'}}
                                                          </span>
                                                        </li>
                                                      </ul>
                                                    </template>
                                                    <p v-if="resultData.length == 0">등록된 정보가 없습니다.</p>
                                                  </td>
                                                </tr>
                                              </template>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </article>
                    <div class="btnWrap" v-show="isModify === false">
                        <ul>
                            <li>
                              <input v-show="userMenuAuth.create_data || userMenuAuth.modify_data"
                                class="btn_save btn-lg"
                                type="button"
                                value="수정"
                                @click="funcModifyStat()"/>
                            </li>
                        </ul>
                    </div>
<!-- -->
<!-- 접근 IP주소 설정 -->
<!-- -->
                  <div v-show="isModify === true">
                    <article>
                      <div class="articleWrap">
                          <div class="paperDetailWrap">
                              <div class="titleWrap">
                                <p class="title">접근 IP주소 설정</p>
                              </div>
                              <div class="detailContent">
                                  <div class="tableWrap">
                                      <table summary="접근 IP주소 설정">
                                          <caption class="blind">접근 IP주소 설정</caption>
                                          <colgroup>
                                              <col style="width:100px"/>
                                              <col style="width:calc(25% - 100px)"/>
                                              <col/>
                                          </colgroup>
                                          <tbody>
                                            <template>
                                              <tr>
                                                <th>접근허용 <p>IP주소</p></th>
                                                <td colspan="7">
                                                  <p>
                                                    <input type="text" placeholder="IP주소를 입력해주세요."
                                                          v-model="addIp"
                                                          @keyup.enter="funcIpAdd()"
                                                          maxlength="15"/>
                                                    <input hidden/>
                                                  </p>
                                                  <template v-for="(item, idx) in resultData">
                                                    <span :key="item.ip+idx" style="margin: 3px 3px; width:160px;">
                                                      <strong>{{item.ip}}</strong>
                                                      <i class="far fa-times-circle"
                                                        style="margin-right: 30px;"
                                                        @click="funcIpDelete('org', item)"
                                                        ></i>
                                                    </span>
                                                    <!-- <p v-if="(idx+1) > 3 && ((idx+1) % 4) == 0" :key="item.ip"/> -->
                                                  </template>

                                                  <template v-for="(item, idx) in ipAddList">
                                                    <span :key="item+idx" style="margin: 3px 3px; width:160px;">
                                                      <strong>{{item}}</strong>
                                                      <i class="far fa-times-circle"
                                                        style="margin-right: 30px;"
                                                        @click="funcIpDelete('new', item)"
                                                        ></i>
                                                    </span>
                                                    <!-- <p v-if="(idx+1) > 3 && ((idx+1) % 4) == 0" :key="item"/> -->
                                                  </template>
                                                </td>
                                              </tr>
                                            </template>
                                          </tbody>
                                      </table>
                                  </div>
                              </div>
                          </div>
                      </div>
                    </article>
                    <div class="btnWrap">
                      <ul>
                        <li>
                          <input v-show="userMenuAuth.create_data || userMenuAuth.modify_data"
                            class="btn_cancel btn-lg"
                            type="button"
                            value="취소"
                            @click="funcModifyCancel()"/>
                        </li>
                        <li>
                          <input v-show="userMenuAuth.create_data || userMenuAuth.modify_data"
                            class="btn_save btn-lg"
                            type="button"
                            value="저장"
                            @click="set()"/>
                        </li>
                      </ul>
                    </div>
                  </div>
<!-- -->
<!-- 최근 접근한 IP주소 -->
<!-- -->
                    <article class="searchResult">
                    <div class="articleWrap">
                        <div class="titleWrap">
                          <p class="title">최근 접근한 IP주소</p>
                        </div>
                        <div>
                          <table>
                            <tr>
                              <th>검색기간</th>
                              <td colspan="5">
                                <div>
                                  <span>
                                    <DatepickerDate class="datepickerDate"
                                    v-bind:propFromDate="fromDate"
                                    v-bind:propToDate="toDate"
                                    v-on:update:fromDate="setFromDate"
                                    v-on:update:toDate="setToDate"
                                    >
                                    <!-- 자식이 부모한테 보낼때 v-on 및 메소드명을 뒤에 사용해서 올려줌 -->
                                    </DatepickerDate>
                                  </span>
                                  <p class="btn_search" style="float: right;" @click="get()">
                                    검색
                                  </p>
                                </div>
                              </td>
                            </tr>
                          </table>
                        </div>
                        <div class="detailContent">
                          <div class="tableWrap">
                            <div class="btnWrap">
                              <table summary="최근 접근한 IP주소">
                                <caption class="blind">최근 접근한 IP주소</caption>
                                <colgroup>
                                  <col style="width:30%;">
                                  <col style="width:30%;">
                                  <col style="width:40%;">
                                </colgroup>
                                <thead>
                                  <tr>
                                    <th>접근 일시</th>
                                    <th>접근 IP주소</th>
                                    <th>계정 정보</th>
                                  </tr>
                                </thead>
                                <tbody>
                                  <template v-for="(item) in resultAccData">
                                    <tr :key="item.id">
                                      <td style="text-align: center;">
                                        {{item.logDate}}
                                      </td>
                                      <td style="text-align: center;">
                                        {{item.ip}}
                                      </td>
                                      <td style="text-align: center;">
                                        {{item.loginId}}
                                      </td>
                                    </tr>
                                  </template>
                                  <tr v-if="resultAccData.length == 0">
                                    <td colspan="3">
                                      <p>접근 이력이 없습니다.</p>
                                    </td>
                                  </tr>
                                </tbody>
                              </table>
                            </div>
                            <Pagination
                                :paginate="resultAccData.paginate"
                                :action="get"
                                :isPropPageCntShow=false>
                            </Pagination>
                          </div>
                        </div>
                      </div>
                    </article>
                </form>
            </div>
        <Spinner v-if="isLoading" />
    </div>
</template>

<script>
import ModalAlert from '@/components/common/modal/ModalAlert.vue'
import Pagination from '@/components/common/Pagination'
import DatepickerDate from '@/components/common/DatepickerDate'

export default {
  name: 'AccessIPConfig',
  created () {
    let toDateTemp = new Date()
    this.toDate = new Date(toDateTemp)
    this.fromDate = new Date(toDateTemp.setMonth(toDateTemp.getMonth() - 1))
  },
  mounted () {
    this.getIPList()
  },
  data () {
    return {
      isLoading: false,
      isModify: false,
      resultData: '',
      resultDataOrg: '',
      resultAccData: '',
      ipDelList: [],
      ipAddList: [],
      addIp: '',
      fromDate: '',
      toDate: ''
    }
  },
  methods: {
    getIPList () {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        this.isModify = false
        this.ipDelList = []
        this.ipAddList = []
        this.http
          .get('/DataCorrection/Etc/whiteIP/' + this.corpNo)
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            if (result.data.resultCode === 0) {
              this.resultData = result.data.items
              this.resultDataOrg = result.data.items
            } else {
              this.resultData = ''
              this.resultDataOrg = ''
              this.isModify = false
            }
            this.get()
          })
          .catch(e => {
            console.log('AccessIPConfig.vue : ' + e)
          })
      }
    },
    get (current_page = 1, per_page = 20) {
      if (this.userMenuAuth.retrieve_list && this.checkDateTerm()) {
        this.isLoading = true
        this.addIp = ''
        this.http
          // eslint-disable-next-line camelcase
          .get('/DataCorrection/Etc/loginLog/' + this.corpId + '/' +
              // eslint-disable-next-line camelcase
              current_page + '/' + per_page + '/10' +
              // eslint-disable-next-line no-undef
              '/' + dateFormatToString(this.fromDate) + '/' + dateFormatToString(this.toDate))
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            if (result.data.resultCode === 0) {
              this.resultAccData = result.data.items

              let page = {
                'per_page': result.data.paging.rowCount,
                'total_cnt': result.data.paging.totalCount,
                'last_page': result.data.paging.lastPage,
                'current_page': result.data.paging.currentPage
              }
              // eslint-disable-next-line no-undef
              this.resultAccData.paginate = makePaginate(page)
              // eslint-disable-next-line no-undef
              this.resultAccData.max = pageMaxNumber(page)
            } else {
              this.resultAccData = ''
            }
          })
          .catch(e => {
            console.log('AccessIPConfig.vue : ' + e)
          })
      }
    },
    set () {
      if (this.userMenuAuth.create_data || this.userMenuAuth.modify_data) {
        this.isLoading = true

        this.funcIpSetProc()
          .then(result => {
            setTimeout(() => {
              this.$modal.show(ModalAlert,
                {
                  title: '접근 IP주소 설정',
                  text: '저장되었습니다.'
                }, {
                  width: 450,
                  height: 'auto'
                }
              )
              this.getIPList()
            }, 1800)
          })
          .catch(e => {
            console.log('AccessIPConfig.vue set : ' + e)
          })
      }
    },
    funcIpSetProc () {
      // 기존ip 삭제
      if (this.ipDelList.length > 0) {
        this.ipDelList.forEach(item => {
          this.http
            .post('/DataCorrection/Etc/whiteIP',
              {
                'memberNo': this.corpNo,
                'memberId': this.corpId,
                'ip': item.ip,
                'delYn': true,
                'userId': this.$store.getters.getUser.luna_id,
                'userCode': 1,
                'userNo': this.$store.getters.getUser.luna_no
              })
            .then(result => {})
            .catch(e => {
              console.log('AccessIPConfig.vue funcIpSetProc : ' + e)
            })
        })
      }

      // 신규추가
      if (this.ipAddList.length > 0) {
        this.ipAddList.forEach(item => {
          this.http
            .post('/DataCorrection/Etc/whiteIP',
              {
                'memberNo': this.corpNo,
                'memberId': this.corpId,
                'ip': item,
                'delYn': false,
                'userId': this.$store.getters.getUser.luna_id,
                'userCode': 1,
                'userNo': this.$store.getters.getUser.luna_no
              })
            .then(result => {})
            .catch(e => {
              console.log('AccessIPConfig.vue funcIpSetProc : ' + e)
            })
        })
      }

      return new Promise(function (resolve) {
        resolve()
      })
    },
    /**
     * 수정버튼클릭상태 값 반전처리
     */
    funcModifyStat () {
      this.isModify = !this.isModify
    },
    /**
     * 수정취소
     */
    funcModifyCancel () {
      this.resultData = this.resultDataOrg
      this.ipDelList = []
      this.ipAddList = []
      this.addIp = ''
      this.funcModifyStat()
    },
    /**
     * 스팸키워드 초기화
     */
    funcResetDefault () {
      this.resultData = this.defaultKeyword
    },
    /**
     * ip삭제 클릭
     */
    funcIpDelete (type, item) {
      if (type === 'org') { // 이미등록된 ip삭제
        this.ipDelList.push(item)
        this.resultData = this.resultData.filter(listItem => {
          return listItem.ip !== item.ip
        })
      } else if (type === 'new') { // 새로추가한 ip삭제
        this.ipAddList = this.ipAddList.filter(listItem => {
          return listItem !== item
        })
      }
    },
    /**
     * ip추가
     */
    funcIpAdd () {
      let reg = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/
      let val = this.addIp
      let dup = true
      if (reg.test(val)) {
        if (this.ipAddList.length > 0) {
          this.ipAddList.forEach(orgItem => {
            if (orgItem === val.trim()) {
              dup = false
            }
          })
        }
        if (this.resultData.length > 0) {
          this.resultData.forEach(orgItem => {
            if (orgItem.ip === val.trim()) {
              dup = false
            }
          })
        }
        // 중복이 아닌경우만 추가
        if (dup) {
          this.ipAddList.push(val)
        } else {
          this.$modal.show(ModalAlert,
            {
              title: '접근 IP주소 설정',
              text: '이미 등록된 IP주소입니다. 다시 입력해주세요.'
            }, {
              width: 450,
              height: 'auto'
            }
          )
        }
      }
    },
    /** 자식이 부모한테 보내준 데이터 세팅 */
    setFromDate (value) {
      this.fromDate = value
    },
    setToDate (value) { /** */
      this.toDate = value
    },
    checkDateTerm () {
      let fromDate = new Date(this.fromDate)
      let termDate = new Date(fromDate.setMonth(fromDate.getMonth() + 3))

      if (Number(termDate) <
          Number(this.toDate)) {
        this.$modal.show(ModalAlert,
          {
            title: '최근 접근한 IP주소',
            text: '최대 검색기간은 3개월 입니다.'
          }, {
            width: 450,
            height: 'auto'
          }
        )
        return false
      }
      return true
    }
  },
  components: {
    Pagination,
    DatepickerDate
  },
  props: [
    'userMenuAuth',
    'corpId',
    'corpNo',
    'http'
  ]
}
</script>

<style scoped>
.datepickerDate /deep/ input[type="text"] {
   width: 150px;
 }
</style>
