<template>
  <div>
    <div class="formContainer">
        <form action="#" method="post">
            <article>
                <div class="articleWrap">
                    <div class="paperDetailWrap">
                        <div class="titleWrap">
                            <p class="title">알림톡 서비스 차감</p>
                        </div>
                        <div class="detailContent">
                          <div class="tableWrap">
                            <div class="btnWrap searchWrap">
                              <table summary="알림톡 서비스 차감">
                                  <caption class="blind">알림톡 서비스 차감</caption>
                                  <colgroup>
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:20%;">
                                    <col style="width:10%;">
                                  </colgroup>
                                  <tbody>
                                    <tr>
                                      <th>
                                        현재 알림톡 잔여수
                                      </th>
                                      <td>
                                        <strong>{{resultCountData | amountFilter}}</strong>
                                      </td>
                                      <th>
                                        차감 건 수
                                      </th>
                                      <td>
                                        <input type="text"
                                              maxlength="11"
                                              style="width: 100%; text-align: right;"
                                              v-model="addCount"
                                              @input="numberFilter($event,'count')"
                                              />
                                      </td>
                                      <th>
                                        총비용
                                      </th>
                                      <td>
                                        <input type="text"
                                              maxlength="11"
                                              style="width: 100%; text-align: right;"
                                              v-model="addAmount"
                                              @input="numberFilter($event,'point')"
                                              />
                                      </td>
                                      <th>
                                        비고
                                      </th>
                                      <td>
                                        <input type="text"
                                               v-model="addMemo"
                                               @blur="tagRemove()"
                                               maxlength="1000"
                                               style="width: 100%;"/>
                                      </td>
                                    </tr>
                                    <tr>
                                      <th>
                                        컨펌 담당자
                                      </th>
                                      <td>
                                        <select v-model="addConfirmUser" style="width: 100%">
                                          <option disabled value="">선택하세요</option>
                                          <option v-for="(item, idx) in confirmUserInfo"
                                            :key="idx" :value="item.id">{{item.name +'.'+item.nickName}}</option>
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
                                    <tr>
                                      <th>
                                        결제타입
                                      </th>
                                      <td :colspan="{7: addType==='0'}">
                                        <select v-model="addType" style="width: 100%">
                                          <option disabled value="0">선택하세요.</option>
                                          <option value="1">무통장</option>
                                          <option value="2">서비스</option>
                                        </select>
                                      </td>
                                      <!-- 결제타입 무통장 경우 시작-->
                                      <template v-if="addType === '1'">
                                        <th>
                                          은행명
                                        </th>
                                        <td>
                                          <select v-model="addBankName" style="width: 100%">
                                            <option disabled value="">선택하세요</option>
                                            <option value="국민">국민은행</option>
                                            <option value="우리">우리은행</option>
                                          </select>
                                        </td>
                                      </template>
                                      <!-- 결제타입 무통장 경우 끝-->
                                      <!-- 결제타입 서비스 경우 시작-->
                                      <template v-if="addType === '2'">
                                        <th>
                                          상점
                                        </th>
                                        <td>
                                          <select v-model="addShopId" style="width: 100%">
                                            <option disabled value="0">선택하세요</option>
                                            <option v-for="(item, idx) in shopList"
                                              :key="idx" :value="item.id">{{item.name}}</option>
                                          </select>
                                        </td>
                                        <th>
                                          개월 수
                                        </th>
                                        <td>
                                          <select v-model="addInstMonth" style="width: 100%">
                                            <option value="0">미선택</option>
                                            <option value="1">1</option>
                                            <option value="2">2</option>
                                            <option value="3">3</option>
                                            <option value="4">4</option>
                                            <option value="5">5</option>
                                            <option value="6">6</option>
                                            <option value="7">7</option>
                                            <option value="8">8</option>
                                            <option value="9">9</option>
                                            <option value="10">10</option>
                                            <option value="11">11</option>
                                            <option value="12">12</option>
                                          </select>
                                        </td>
                                      </template>
                                      <!-- 결제타입 서비스 경우 끝-->
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
                              <table summary="알림톡 서비스 차감">
                                  <caption class="blind">알림톡 서비스 차감</caption>
                                  <colgroup>
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:10%;">
                                    <col style="width:40%;">
                                  </colgroup>
                                  <thead>
                                    <tr>
                                      <th>결제일자</th>
                                      <th>결제수단</th>
                                      <th>충전건수</th>
                                      <th>총비용</th>
                                      <th>컨펌 담당자</th>
                                      <th>은행명</th>
                                      <th>비고</th>
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
                                          {{item.count | amountFilter}} 건
                                        </td>
                                        <td style="text-align: right;">
                                          {{item.amount | amountFilter}} 원
                                        </td>
                                        <td style="text-align: center;">
                                          {{item.confirmUser}}
                                        </td>
                                        <td style="text-align: center;">
                                          {{item.bankName}}
                                        </td>
                                        <td>
                                          {{item.memo}}
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
  name: 'TalkServiceChargeDeduction',
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
      resultCountData: 0, // 상담톡/알림톡 포인트
      resultData: {}, // 상담톡/알림톡 히스토리 목록
      confirmUserInfo: {}, // 구어드민 루나직원 id
      addConfirmUser: '',
      addConfirmDate: '',
      shopList: {}, // 샵리스트
      addShopId: '0',
      addType: '0',
      addBankName: '',
      addInstMonth: '0',
      addAmount: '0',
      addCount: '0',
      addMemo: ''
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
    mallSearch () {
      // let emptyVal = '%20'
      this.http
        .get('/DataCorrection/AlimTalk/shop/' + this.corpId)
        .then(result => {
          if (result.data.resultCode === 0) {
            this.shopList = result.data.items.length === 0 ? null : result.data.items
          } else {
            this.shopList = null
          }
        })
        .catch(e => {
          console.log('shopList : ' + e)
        })
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
        this.addAmount = num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, '$1,')
      } else if (flag === 'count') {
        this.addCount = num.toFixed(0).replace(/(\d)(?=(\d{3})+(?:\.\d+)?$)/g, '$1,')
      }
    },
    tagRemove () {
      this.addMemo = SanitizeHtml(this.addMemo, {
        allowedTags: [],
        allowedAttributes: []
      })
    },
    get () {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        this.http
          .get('/DataCorrection/AlimTalk/count/' + this.corpId)
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            if (result.data.resultCode === 0) {
              this.resultCountData = result.data.count
              this.resultData = result.data.items
            } else {
              this.resultData = {}
            }
            this.getConfirmUserInfo()
            this.mallSearch()
          })
          .catch(e => {
            console.log('TalkServiceChargeDeduction.vue : ' + e)
          })
      }
    },
    getConfirmUserInfo () {
      this.http
        .get('/DataCorrection/Etc/lunaUsers')
        .then(result => { this.confirmUserInfo = result.data.items })
    },
    set () {
      if (this.userMenuAuth.create_data || this.userMenuAuth.modify_data) {
        if (this.validate()) {
          this.isLoading = true
          this.http
            .post('/DataCorrection/AlimTalk/decreaseCount',
              {
                // 'lunaOldId': this.$store.getters.getUser.luna_old_id,
                'type': Number(this.addType),
                'count': this.addCount.replace(/,/g, ''),
                'amount': this.addAmount.replace(/,/g, ''),
                'memo': this.addMemo,
                // eslint-disable-next-line no-undef
                'confirmDate': dateFormatToString(this.addConfirmDate),
                'confirmUser': this.addConfirmUser,
                'shopId': this.addType === '2' ? Number(this.addShopId) : 0,
                'installmentMonth': this.addType === '2' ? Number(this.addInstMonth) : 0,
                'bankName': this.addType === '1' ? this.addBankName : '',

                'domain': this.corpId,
                'userId': this.$store.getters.getUser.luna_id,
                'userCode': 1,
                'userNo': this.$store.getters.getUser.luna_no
              })
            .then(result => {
              // console.log(result.data)
              this.isLoading = false
              let msg = '알림톡 서비스 차감처리 되었습니다.'
              if (result.data.resultCode !== 0) {
                msg = '알림톡 서비스 차감처리 실패했습니다.'
              }
              this.$modal.show(ModalAlert,
                {
                  title: '알림톡 서비스 차감',
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
              console.log('TalkServiceChargeDeduction.vue : ' + e)
            })
        } else {
          this.$modal.show(ModalAlert,
            {
              title: '알림톡 서비스 차감',
              text: '차감 정보를 입력해주세요'
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
      // 금액,건수 모두 0입력가능
      // let amount = this.addAmount === 0 ? 0 : this.addAmount.replace(/,/g, '')
      // let count = this.addCount === 0 ? 0 : this.addCount.replace(/,/g, '')
      if (this.addMemo !== '' && this.addType !== '0' && this.addConfirmUser !== '') {
        rtn = true
      }
      if (this.addType === '1' && this.addBankName === '') rtn = false

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
