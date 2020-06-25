<template>
  <div>
    <div class="formContainer">
        <form action="#" method="post">
            <article>
                <div class="articleWrap">
                    <div class="paperDetailWrap">
                        <div class="titleWrap">
                            <p class="title">CTI 사용료 차감</p>
                        </div>

                        <div class="searchWrap btnWrap">
                          <table>
                            <colgroup>
                              <col style="width:10%;"/>
                              <col width="width:35%;"/>
                              <col style="width:8%;"/>
                              <col style="width:37%;"/>
                            </colgroup>
                            <tbody>
                              <tr>
                                <th>기간검색</th>
                                <td>
                                  <div>
                                      <DatepickerDate
                                      v-bind:propFromDate="fromDate"
                                      v-bind:propToDate="toDate"
                                      v-on:update:fromDate="setFromDate"
                                      v-on:update:toDate="setToDate"
                                      v-bind:propsSetBtn="4"
                                      >
                                      <!-- 자식이 부모한테 보낼때 v-on 및 메소드명을 뒤에 사용해서 올려줌 -->
                                      </DatepickerDate>
                                  </div>
                                </td>
                                <td>
                                  <div class="btnWrap">
                                    <p class="btn_search"
                                      @click="get()">
                                      <i class="fas fa-search"></i>검색
                                    </p>
                                  </div>
                                </td>
                              </tr>
                            </tbody>
                          </table>
                        </div>

                        <div class="detailContent">
                          <div class="tableWrap">
                            <div class="btnWrap searchWrap">
                              <table summary="CTI 사용료 차감">
                                  <caption class="blind">CTI 사용료 차감</caption>
                                  <colgroup>
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:5%;">
                                    <col style="width:10%;">
                                    <col style="width:5%;">
                                    <col style="width:10%;">
                                    <col style="width:5%;">
                                    <col style="width:10%;">
                                    <col style="width:5%;">
                                    <col style="width:30%;">
                                  </colgroup>
                                  <tbody>
                                    <tr>
                                      <th>현재 포인트</th>
                                      <td>
                                        <strong>{{resultPointData | amountFilter}}</strong>
                                      </td>
                                      <th>과금일</th>
                                      <td>
                                        <datepicker
                                        class="datepicker"
                                        :language="languages['ko']"
                                        format="yyyy-MM-dd"
                                        v-model="addUsageDate"
                                        />
                                      </td>
                                      <th>부운영자 수</th>
                                      <td>
                                        <input type="text"
                                              maxlength="11"
                                              style="width: 100%; text-align: right;"
                                              v-model="addCount"
                                              @input="numberFilter($event,'count')"
                                              />
                                      </td>
                                      <th>과금포인트</th>
                                      <td>
                                        <input type="text"
                                              maxlength="11"
                                              style="width: 100%; text-align: right;"
                                              v-model="addPoint"
                                              @input="numberFilter($event,'point')"
                                              />
                                      </td>
                                      <th>과금사유</th>
                                      <td>
                                        <input type="text"
                                               @blur="tagRemove()"
                                               v-model="addComment"
                                               maxlength="50"
                                               style="width: 70%;"/>
                                        <input v-show="userMenuAuth.create_data || userMenuAuth.modify_data"
                                          class="btn_save"
                                          type="button"
                                          value="저장"
                                          @click="set()"/>
                                      </td>
                                    </tr>
                                  </tbody>
                              </table>
                            </div>
                              <table summary="CTI 사용료 차감">
                                  <caption class="blind">CTI 사용료 차감</caption>
                                  <colgroup>
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:50%;">
                                  </colgroup>
                                  <thead>
                                    <tr>
                                      <th>과금일</th>
                                      <th>수수료(과금) 타입</th>
                                      <th>부운영자 수</th>
                                      <th>수수료(과금포인트)</th>
                                      <th>잔여포인트</th>
                                      <th>코멘트</th>
                                    </tr>
                                  </thead>
                                  <tbody>
                                    <template v-for="(item) in resultData">
                                      <tr :key="item.id">
                                        <td style="text-align: center;">
                                          {{item.usageDate}}
                                        </td>
                                        <td style="text-align: center;">
                                          {{item.feeType}}
                                        </td>
                                        <td style="text-align: right;">
                                          {{item.count | amountFilter}} 개
                                        </td>
                                        <td style="text-align: right;">
                                          {{item.amount | amountFilter}} 원
                                        </td>
                                        <td style="text-align: right;">
                                          {{item.remainPoints | amountFilter}} 원
                                        </td>
                                        <td>
                                          {{item.comment}}
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
        </form>
    </div>
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import ModalAlert from '@/components/common/modal/ModalAlert.vue'
import DatepickerDate from '@/components/common/DatepickerDate'
import Datepicker from 'vuejs-datepicker/dist/vuejs-datepicker.esm.js'
import * as lang from 'vuejs-datepicker/src/locale'
import SanitizeHtml from 'sanitize-html'

export default {
  name: 'CtiFeeDeduct',
  created () {
    let toDateTemp = new Date()
    this.addUsageDate = new Date(toDateTemp)
    this.toDate = new Date(toDateTemp)
    this.fromDate = new Date(toDateTemp.setMonth(toDateTemp.getMonth() - 3))
  },
  mounted () {
    this.get()
  },
  data () {
    return {
      isLoading: false,
      languages: lang,
      fromDate: '',
      toDate: '',
      resultPointData: 0,
      resultData: {},
      addUsageDate: '',
      addPoint: '0',
      addCount: '0',
      addComment: ''
    }
  },
  filters: {
    // 금액 콤마(,) 처리
    amountFilter: function (val) {
      // eslint-disable-next-line no-new-wrappers
      let num = new Number(val)
      return num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, '$1,')
    }
  },
  methods: {
    /** 자식이 부모한테 보내준 데이터 세팅 */
    setFromDate (value) {
      this.fromDate = value
    },
    setToDate (value) { /** */
      this.toDate = value
    },
    numberFilter: function (evt, flag) {
      let orgNum = evt.target.value.replace(/,/g, '')
      let patternNo = /[^0-9]/g
      let num = 0
      if (patternNo.test(orgNum)) {
        orgNum = evt.target._value
      }
      // eslint-disable-next-line no-new-wrappers
      num = new Number(orgNum.replace(/,/g, ''))

      if (flag === 'point') {
        this.addPoint = num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, '$1,')
      } else if (flag === 'count') {
        this.addCount = num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, '$1,')
      }
    },
    tagRemove () {
      this.addComment = SanitizeHtml(this.addComment, {
        allowedTags: [],
        allowedAttributes: []
      })
    },
    get () {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        // eslint-disable-next-line no-undef
        let startDate = dateTimeFormatToString(this.fromDate)
        // eslint-disable-next-line no-undef
        let endDate = dateTimeFormatToString(this.toDate)
        this.http
          .get('/DataCorrection/CTI/points/' + this.corpId + '/' + startDate + '/' + endDate)
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            if (result.data.resultCode === 0) {
              this.resultPointData = result.data.points
              this.resultData = result.data.items
            } else {
              this.resultData = {}
            }
          })
          .catch(e => {
            console.log('CtiFeeDeduct.vue : ' + e)
          })
      }
    },
    set () {
      if (this.userMenuAuth.create_data || this.userMenuAuth.modify_data) {
        if (this.validate()) {
          this.isLoading = true
          this.http
            .post('/DataCorrection/CTI/points',
              {
                // eslint-disable-next-line no-undef
                'usageDate': dateTimeFormatToString(this.addUsageDate),
                'count': this.addCount.replace(/,/g, ''),
                'points': this.addPoint.replace(/,/g, ''),
                'comment': this.addComment,

                'domain': this.corpId,
                'userId': this.$store.getters.getUser.luna_id,
                'userCode': 1,
                'userNo': this.$store.getters.getUser.luna_no
              })
            .then(result => {
              // console.log(result.data)
              this.isLoading = false
              let msg = 'CTI 사용료 차감처리 되었습니다.'
              if (result.data.resultCode !== 0) {
                msg = 'CTI 사용료 차감처리 실패했습니다.'
              }
              this.$modal.show(ModalAlert,
                {
                  title: 'CTI 사용료 차감',
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
              console.log('CtiFeeDeduct.vue : ' + e)
            })
        } else {
          this.$modal.show(ModalAlert,
            {
              title: 'CTI 사용료 차감',
              text: '부운영자 수/수수료(과금포인트) 및 과금사유을 입력해주세요'
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
      let point = this.addPoint === 0 ? 0 : this.addPoint.replace(/,/g, '')
      let count = this.addCount === 0 ? 0 : this.addCount.replace(/,/g, '')
      if ((point > 0 || count > 0) && this.addComment !== '') rtn = true
      return rtn
    }
  },
  components: {
    'DatepickerDate': DatepickerDate,
    'Datepicker': Datepicker
  },
  props: [
    'userMenuAuth',
    'corpId',
    'http'
  ]
}
</script>
