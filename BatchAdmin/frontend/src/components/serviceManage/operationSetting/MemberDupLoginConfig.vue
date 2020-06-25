<template>
  <div>
    <div class="formContainer">
        <form action="#" method="post">
            <article>
                <div class="articleWrap">
                    <div class="paperDetailWrap">
                        <div class="titleWrap">
                            <p class="title">중복로그인 설정 </p>
                            <h6 style="margin: 10px 100px">*도메인 기준으로 멀티몰의 경우 설정에 따라 모든 몰에 적용이 됩니다.</h6>
                        </div>
                        <div class="detailContent">
                          <div class="tableWrap">
                            <div class="btnWrap searchWrap">
                              <table summary="중복로그인 설정">
                                  <caption class="blind">중복로그인 설정</caption>
                                  <colgroup>
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:80%;">
                                  </colgroup>
                                  <tbody>
                                    <tr>
                                      <th>
                                        사용 여부
                                      </th>
                                      <td style="text-align: left;">
                                        <p class="toggleWrap">
                                          <label class="switch" >
                                            <input type="checkbox" v-model="dupLoginUse">
                                            <span class="slider"></span>
                                          </label>
                                        </p>
                                      </td>
                                    </tr>
                                    <tr>
                                      <th rowspan="2">
                                        중복로그인 설정 기준일
                                      </th>
                                      <td>
                                        <p class="inputWrap" style="padding: 5px; text-align:left">
                                          <input type="checkbox" id="startDate" v-model="startDateUse">
                                          <label for="startDate">시작일자 설정</label>
                                        </p>
                                        <datepicker
                                        :disabled="!startDateUse"
                                        class="datepicker"
                                        :language="languages['ko']"
                                        format="yyyy-MM-dd"
                                        v-model="startDate"
                                        />
                                      </td>
                                    </tr>
                                    <tr>
                                      <td>
                                        <p class="inputWrap" style="padding: 5px; text-align:left">
                                          <input type="checkbox" id="endDate" v-model="endDateUse">
                                          <label for="endDate">종료일자 설정</label>
                                        </p>
                                        <datepicker
                                        :disabled="!endDateUse"
                                        class="datepicker"
                                        :language="languages['ko']"
                                        format="yyyy-MM-dd"
                                        v-model="endDate"
                                        />
                                      </td>
                                    </tr>
                                  </tbody>
                              </table>
                                <article>
                                </article>
                                <div class="btnWrap">
                                  <ul>
                                      <li>
                                        <input v-show="userMenuAuth.create_data || userMenuAuth.modify_data"
                                          class="btn_save"
                                          type="button"
                                          value="저장"
                                          @click="set()"/>
                                      </li>
                                  </ul>
                                </div>

                            </div>
                              <div class="titleWrap">
                                  <p class="title">로그 이력</p>
                              </div>
                              <table summary="중복로그인 설정">
                                  <caption class="blind">중복로그인 설정</caption>
                                  <colgroup>
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:30%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                  </colgroup>
                                  <thead>
                                    <tr>
                                      <th>등록일시</th>
                                      <th>법인명</th>
                                      <th>설정 기간</th>
                                      <th>영업담당자</th>
                                      <th>운영담당자</th>
                                    </tr>
                                  </thead>
                                  <tbody>
                                    <template v-for="(item) in resultLogData">
                                      <tr :key="item.id">
                                        <td style="text-align: center;">
                                          {{item.logDate}}
                                        </td>
                                        <td style="text-align: center;">
                                          {{item.memberName}}
                                        </td>
                                        <td style="text-align: center;">
                                          {{item.whitelistStartDate}}  ~  {{item.whitelistEndDate}}
                                        </td>
                                        <td style="text-align: center;">
                                          {{item.saleMangerLundId}}({{item.saleMangerKorName}})
                                        </td>
                                        <td style="text-align: center;">
                                          {{item.maintainMangerLundId}}({{item.maintainMangerKorName}})
                                        </td>
                                      </tr>
                                    </template>
                                  </tbody>
                              </table>
                          </div>
                          <Pagination
                              :paginate="resultLogData.paginate"
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
import Datepicker from 'vuejs-datepicker/dist/vuejs-datepicker.esm.js'
import * as lang from 'vuejs-datepicker/src/locale'
import Pagination from '@/components/common/Pagination'

export default {
  name: 'MemberDupLoginConfig',
  watch: {
    dupLoginUse (val) {
      if (val) {
        this.startDateUse = true
      } else if (!val) {
        this.startDateUse = false
        this.startDate = null
        this.endDateUse = false
        this.endDate = null
      }
    }
  },
  created () {
    this.startDate = new Date()
    this.endDate = new Date()
  },
  mounted () {
    this.get()
  },
  data () {
    return {
      isLoading: false,
      languages: lang,
      resultLogData: {},
      dupLoginUse: false,
      startDate: '',
      startDateUse: false,
      endDate: '',
      endDateUse: false
    }
  },
  methods: {
    get (current_page = 1, per_page = 10) {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        this.http
          // eslint-disable-next-line camelcase
          .get('/DataCorrection/Etc/duplicateLoginLog/' + this.corpNo + '/' + current_page + '/' + per_page + '/5')
          .then(result => {
            this.isLoading = false
            this.resultLogData = result.data.items

            let page = {
              'per_page': result.data.paging.rowCount,
              'total_cnt': result.data.paging.totalCount,
              'last_page': result.data.paging.lastPage,
              'current_page': result.data.paging.currentPage
            }
            // eslint-disable-next-line no-undef
            this.resultLogData.paginate = makePaginate(page)
            // eslint-disable-next-line no-undef
            this.resultLogData.max = pageMaxNumber(page)

            this.getConfig()
          })
          .catch(e => {
            this.isLoading = false
            console.log('MemberDupLoginConfig.vue : ' + e)
          })
      }
    },
    getConfig () {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        this.http
          .get('/DataCorrection/Etc/duplicateLogin/' + this.corpNo)
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            if (result.data.resultCode === 0) {
              this.dupLoginUse = result.data.useYn
              this.startDate = result.data.whitelistStartDate
              this.endDate = result.data.whitelistEndDate
              if (result.data.whitelistStartDate !== '') this.startDateUse = true
              if (result.data.whitelistEndDate !== '') this.endDateUse = true
            }
          })
          .catch(e => {
            this.isLoading = false
            console.log('MemberDupLoginConfig.vue : ' + e)
          })
      }
    },
    set () {
      if (this.userMenuAuth.create_data || this.userMenuAuth.modify_data) {
        if (this.validate()) {
          this.isLoading = true
          this.http
            .post('/DataCorrection/Etc/duplicateLogin',
              {
                // 'lunaOldId': this.$store.getters.getUser.luna_old_id,
                'memberNo': this.corpNo,
                // eslint-disable-next-line no-undef
                'whitelistStartDate': this.startDateUse ? dateFormatToString(this.startDate) : null,
                // eslint-disable-next-line no-undef
                'whitelistEndDate': this.endDateUse ? dateFormatToString(this.endDate) : null,
                'useYn': this.dupLoginUse,

                'userId': this.$store.getters.getUser.luna_id,
                'userCode': 1,
                'userNo': this.$store.getters.getUser.luna_no
              })
            .then(result => {
              // console.log(result.data)
              this.isLoading = false
              let msg = '중복로그인 설정처리 되었습니다.'
              if (result.data.resultCode !== 0) {
                msg = '중복로그인 설정처리 실패했습니다.'
              }
              this.$modal.show(ModalAlert,
                {
                  title: '중복로그인 설정',
                  text: msg
                }, {
                  width: 450,
                  height: 'auto'
                }
              )
              this.get()
            })
            .catch(e => {
              this.isLoading = false
              console.log('MemberDupLoginConfig.vue : ' + e)
            })
        } else {
          this.$modal.show(ModalAlert,
            {
              title: '중복로그인 설정',
              text: '설정정보를 올바르게 입력해주세요'
            }, {
              width: 450,
              height: 'auto'
            }
          )
        }// if
      }
    },
    validate () {
      let rtn = false

      if (this.dupLoginUse) {
        if ((this.startDateUse && this.startDate !== '' && !this.endDateUse) ||
            (this.endDateUse && this.startDateUse && this.startDate !== '' && this.endDate !== '')) {
          rtn = true
        }
      } else if (!this.dupLoginUse) {
        rtn = true
      }

      if ((this.startDateUse && this.endDateUse) && (this.startDate !== '' && this.endDate !== '')) {
        // eslint-disable-next-line no-undef
        let sDate = dateFormatToString(this.startDate)
        // eslint-disable-next-line no-undef
        let eDate = dateFormatToString(this.endDate)
        let sNo = Number(sDate.replace(/-/gi, ''))
        let eNo = Number(eDate.replace(/-/gi, ''))

        if (sNo > eNo) {
          rtn = false
        }
      }

      return rtn
    }
  },
  components: {
    'Datepicker': Datepicker,
    'Pagination': Pagination
  },
  props: [
    'userMenuAuth',
    'corpNo',
    'http'
  ]
}
</script>
