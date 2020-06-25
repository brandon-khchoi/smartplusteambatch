<template>
  <div>
    <div class="formContainer">
        <form action="#" method="post">
            <article>
                <div class="articleWrap">
                    <div class="paperDetailWrap">
                        <div class="titleWrap">
                            <p class="title">상담톡 포인트 차감</p>
                        </div>
                        <div class="detailContent">
                          <div class="tableWrap">
                            <div class="btnWrap searchWrap">
                              <table summary="상담톡 포인트 차감">
                                  <caption class="blind">상담톡 포인트 차감</caption>
                                  <colgroup>
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:20%;">
                                    <col style="width:30%;">
                                  </colgroup>
                                  <tbody>
                                    <tr>
                                      <th>
                                        현재 포인트
                                      </th>
                                      <td>
                                        <strong>{{resultPointData | amountFilter}} (P)</strong>
                                      </td>
                                      <th>
                                        차감등록 포인트
                                      </th>
                                      <td>
                                        <input type="text"
                                              maxlength="11"
                                              style="width: 100%; text-align: right;"
                                              v-model="addPoint"
                                              @input="addAmountFilter($event)"
                                              />
                                      </td>
                                      <th>
                                        상세내역
                                      </th>
                                      <td>
                                        <input type="text"
                                               @blur="tagRemove()"
                                               v-model="addComment"
                                               maxlength="1000"
                                               style="width: 100%;"/>
                                      </td>
                                    </tr>
                                    <tr>
                                      <th>
                                        결제타입
                                      </th>
                                      <td>
                                        <select v-model="addType" style="width: 100%">
                                          <option disabled value="0">선택하세요.</option>
                                          <option value="1">무통장</option>
                                          <option value="2">서비스</option>
                                        </select>
                                      </td>
                                      <th>컨펌 날짜</th>
                                      <td>
                                        <datepicker
                                        class="datepicker"
                                        :language="languages['ko']"
                                        format="yyyy-MM-dd"
                                        v-model="addConfirmDate"
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
                              <table summary="상담톡 포인트 차감">
                                  <caption class="blind">상담톡 포인트 차감</caption>
                                  <colgroup>
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:60%;">
                                  </colgroup>
                                  <thead>
                                    <tr>
                                      <th>결제일</th>
                                      <th>결제수단</th>
                                      <th>충전금액</th>
                                      <th>처리상태</th>
                                      <th>상세내역</th>
                                    </tr>
                                  </thead>
                                  <tbody>
                                    <template v-for="(item) in resultData">
                                      <tr :key="item.id">
                                        <td style="text-align: center;">
                                          {{item.createdDate}}
                                        </td>
                                        <td style="text-align: center;">
                                          {{item.payType}}
                                        </td>
                                        <td style="text-align: right;">
                                          {{item.amount | amountFilter}}
                                        </td>
                                        <td style="text-align: center;">
                                          {{item.confirm}}
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
import SanitizeHtml from 'sanitize-html'
import Datepicker from 'vuejs-datepicker/dist/vuejs-datepicker.esm.js'
import * as lang from 'vuejs-datepicker/src/locale'

export default {
  name: 'ConsultingTalkPointDeduction',
  created () {
    this.addConfirmDate = new Date()
  },
  mounted () {
    this.get()
  },
  data () {
    return {
      isLoading: false,
      languages: lang,
      resultPointData: 0, // 상담톡 포인트
      resultData: {}, // 상담톡 히스토리 목록
      addType: '0',
      addConfirmDate: '',
      addPoint: '0',
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
    addAmountFilter: function (evt) {
      let orgNum = evt.target.value.replace(/,/g, '')
      let patternNo = /[^0-9]/g
      let num = 0
      if (patternNo.test(orgNum)) {
        orgNum = evt.target._value
      }
      // eslint-disable-next-line no-new-wrappers
      num = new Number(orgNum.replace(/,/g, ''))

      this.addPoint = num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, '$1,')
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
        this.http
          .get('/DataCorrection/BizMessage/points/' + this.corpId)
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
            console.log('ConsultingTalkPointDeduction.vue : ' + e)
          })
      }
    },
    set () {
      if (this.userMenuAuth.create_data || this.userMenuAuth.modify_data) {
        if (this.validate()) {
          this.isLoading = true
          this.http
            .post('/DataCorrection/BizMessage/decreasePoints',
              {
                'type': Number(this.addType),
                'lunaOldId': this.$store.getters.getUser.luna_old_id,
                'points': this.addPoint.replace(/,/g, ''),
                'comment': this.addComment,
                // eslint-disable-next-line no-undef
                'confirmDate': dateFormatToString(this.addConfirmDate),

                'domain': this.corpId,
                'userId': this.$store.getters.getUser.luna_id,
                'userCode': 1,
                'userNo': this.$store.getters.getUser.luna_no
              })
            .then(result => {
              // console.log(result.data)
              this.isLoading = false
              let msg = '상담톡 포인트 차감처리 되었습니다.'
              if (result.data.resultCode !== 0) {
                msg = '상담톡 포인트 차감처리 실패했습니다.'
              }
              this.$modal.show(ModalAlert,
                {
                  title: '상담톡 포인트 차감',
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
              console.log('ConsultingTalkPointDeduction.vue : ' + e)
            })
        } else {
          this.$modal.show(ModalAlert,
            {
              title: '상담톡 포인트 차감',
              text: '포인트 차감 정보를 입력해주세요'
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
      // eslint-disable-next-line no-unused-vars
      let point = this.addPoint === 0 ? 0 : this.addPoint.replace(/,/g, '')
      if (this.addComment !== '' && this.addType !== '0') rtn = true
      return rtn
    }
  },
  components: {
    'Datepicker': Datepicker
  },
  props: [
    'userMenuAuth',
    'corpId',
    'http'
  ]
}
</script>
