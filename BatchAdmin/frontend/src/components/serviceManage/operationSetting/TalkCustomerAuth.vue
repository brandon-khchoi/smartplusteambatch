<template>
    <div class="TalkCustomerAuth">
            <div class="formContainer">
                <form action="#" method="post">
                    <article>
                        <div class="articleWrap">
                            <div class="talkCustomerAuthDetailWrap">
                                <div class="titleWrap">
                                    <p class="title">상담톡 인증 초기화 요청</p>
                                </div>
                                <div class="detailContent">
                                    <div class="tableWrap">
                                        <table summary="상담톡 인증 초기화 요청">
                                            <caption class="blind">상담톡 인증 초기화 요청</caption>
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
                                              <tr>
                                                <th>쇼핑몰 검색</th>
                                                <td colspan="2">
                                                  <div class="searchBtnWrap single">
                                                    <input type="text"
                                                            v-model="mallName"
                                                            @keyup.enter="mallSearch(mallName)"
                                                            style="width:70%;">
                                                    <p class="btn_search" @click="mallSearch(mallName)">찾기</p>
                                                    <input type="text" hidden>
                                                  </div>
                                                </td>
                                              </tr>
                                              <tr>
                                                <th>쇼핑몰 선택</th>
                                                <td colspan="2">
                                                  <select :disabled="resultMallData===null"
                                                          style="width: 100%;"
                                                          v-model="mallId">
                                                    <option value="9999">선택하세요</option>
                                                    <template v-for="item in resultMallData">
                                                      <option
                                                        :key="item.id"
                                                        :value="item.id">
                                                        {{item.name}}</option>
                                                    </template>
                                                  </select>
                                                </td>
                                              </tr>
                                              <tr>
                                                <th>고객전화번호<span class="necessary"></span></th>
                                                <td colspan="2">
                                                  <input type="text"
                                                         v-model="telNo"
                                                         @input="typing($event, 'no', 'telephoneNumber')"
                                                         style="width:70%;"
                                                         maxlength=11
                                                         :disabled="resultMallData===null">
                                                  <p class="btn_search" @click="get()">인증조회</p>
                                                </td>
                                              </tr>
                                            </tbody>
                                        </table>
                                    </div>

                                    <div v-if="customerInfo != null" class="tableWrap">
                                      <table summary="고객 조회결과">
                                          <caption class="blind">고객 조회결과</caption>
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
                                              <!-- <tr>
                                                <th>고객 ID</th>
                                                <td colspan="2">
                                                  {{customerInfo.id}}
                                                </td>
                                              </tr> -->
                                              <tr>
                                                <th>카카오i <p>사용여부</p></th>
                                                <td colspan="2">
                                                  <p class="toggleWrap">
                                                    <label class="switch">
                                                      <input type="checkbox" v-model="authCondition" disabled>
                                                      <span class="slider"></span>
                                                    </label>
                                                  </p>
                                                </td>
                                              </tr>
                                              <tr>
                                                <th>고객 이름</th>
                                                <td colspan="2">
                                                  {{customerInfo.name}}
                                                </td>
                                              </tr>
                                              <tr>
                                                <th>고객 전화번호</th>
                                                <td colspan="2">
                                                  {{customerInfo.telephoneNumber}}
                                                </td>
                                              </tr>
                                              <tr>
                                                <th>고객 이메일</th>
                                                <td colspan="2">
                                                  {{customerInfo.email}}
                                                </td>
                                              </tr>
                                              <tr>
                                                <th>인증여부</th>
                                                <td colspan="2">
                                                  <p class="toggleWrap">
                                                    <label class="switch">
                                                      <input type="checkbox" v-model="customerInfo.authenticationStatus" disabled>
                                                      <span class="slider"></span>
                                                    </label>
                                                  </p>
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
                        <ul v-show="corpId !== '' && !authCondition && (customerInfo!=null&&customerInfo.authenticationStatus===true)">
                            <li>
                              <input v-show="userMenuAuth.create_data || userMenuAuth.modify_data"
                                class="btn_save btn-lg"
                                type="button"
                                value="인증 초기화"
                                @click="set()"/>
                            </li>
                        </ul>
                        <p v-if="customerInfo != null && authCondition">
                          * 카카오i 사용시, 인증 초기화 불가 - 고객이 직업 해제 해야합니다.
                        </p>
                    </div>
                </form>
            </div>
        <Spinner v-if="isLoading" />
    </div>
</template>

<script>
import ModalAlert from '@/components/common/modal/ModalAlert.vue'

export default {
  name: 'TalkCustomerAuth',
  created () {
  },
  mounted () {
    // this.get()
  },
  data () {
    return {
      isLoading: false,
      mallName: '',
      mallId: '9999', // 임의 쓰레기값
      telNo: null,
      resultMallData: null,
      authCondition: true,
      customerInfo: null
    }
  },
  methods: {
    mallSearch (txt) {
      if (this.userMenuAuth.retrieve_list) {
        this.mallId = '9999'
        this.dataReset()
        this.http
          .get('/DataCorrection/BizMessage/shop/' + this.corpId + '/' + this.mallName)
          .then(result => {
            if (result.data.resultCode === 0) {
              this.resultMallData = result.data.items.length === 0 ? null : result.data.items
            }
          })
          .catch(e => {
            console.log('TalkCustomerAuth.vue : ' + e)
          })
      }
    },
    get () {
      if (this.userMenuAuth.retrieve_list && this.mallId !== '9999' && this.telNo !== null) {
        this.isLoading = true
        this.http
          .get('/DataCorrection/BizMessage/userAuthenticationStatus/' + this.corpId + '/' + this.mallId + '/' + this.telNo)
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            if (result.data.resultCode === 0) {
              this.authCondition = result.data.useKakaoi
              this.customerInfo = result.data.items.length === 0 ? null : result.data.items[0]

              if (this.customerInfo === null) {
                this.$modal.show(ModalAlert,
                  {
                    title: '상담톡 인증조회',
                    text: '미등록 정보입니다.'
                  }, {
                    width: 450,
                    height: 'auto'
                  }
                )
              }
            }
          })
          .catch(e => {
            this.isLoading = false
            console.log('TalkCustomerAuth.vue : ' + e)
          })
      }
    },
    set () {
      if (this.userMenuAuth.create_data || this.userMenuAuth.modify_data) {
        this.isLoading = true

        this.http
          .post('/DataCorrection/BizMessage/userAuthenticationStatus',
            {
              'shopId': this.mallId,
              'katalkUserId': this.customerInfo.id,
              'telephoneNumber': this.customerInfo.telephoneNumber,
              'domain': this.corpId,
              'userId': this.$store.getters.getUser.luna_id,
              'userCode': 1,
              'userNo': this.$store.getters.getUser.luna_no
            })
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            let msg = '상담톡 인증 초기화 요청되었습니다.'
            if (result.data.resultCode !== 0) {
              msg = result.data.message
            }
            this.$modal.show(ModalAlert,
              {
                title: '상담톡 인증 초기화 요청',
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
            console.log('TalkCustomerAuth.vue : ' + e)
          })
      }
    },
    typing: function (evt, type, name) {
      let val = evt.target.value
      // eslint-disable-next-line camelcase
      let pattern_no = /[^0-9]/

      if (type === 'no') {
        if (pattern_no.test(val)) {
          if (name === 'telephoneNumber') this.telNo = evt.target._value
        } else {
          if (name === 'telephoneNumber') this.telNo = evt.target.value.trim()
        }
      }
    },
    dataReset () {
      this.telNo = null
      this.resultMallData = null
      this.authCondition = true
      this.customerInfo = null
    }
  },
  props: [
    'userMenuAuth',
    'corpId',
    'http'
  ]
}
</script>
