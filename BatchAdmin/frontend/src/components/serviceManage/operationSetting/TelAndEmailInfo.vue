<template>
    <div class="Paper">
            <div class="formContainer">
                <form action="#" method="post">
                    <article>
                        <div class="articleWrap">
                            <div class="paperDetailWrap">
                                <div class="titleWrap">
                                  <p class="title">담당자 전화번호 정보 변경</p>
                                </div>
                                <div class="detailContent">
                                    <div class="tableWrap">
                                        <table summary="담당자 전화번호 정보 변경">
                                            <caption class="blind">담당자 전화번호 정보 변경</caption>
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
                                                  <th>담당자 선택</th>
                                                  <td colspan="2">
                                                    <select :disabled="resultData===null"
                                                            @change="setSelectData($event.target.value)"
                                                            style="width: 100%"
                                                      >
                                                      <option value="">선택하세요</option>
                                                      <template v-for="(item ,idx) in resultData">
                                                        <option
                                                          :key="idx"
                                                          :value="idx">
                                                          {{item.representName}} //
                                                          {{item.telephoneNumber}} //
                                                          {{item.email}}
                                                        </option>
                                                      </template>
                                                    </select>
                                                  </td>
                                                </tr>
                                                <tr>
                                                  <th>업체명</th>
                                                  <td colspan="2">
                                                    {{resultSelectData.companyName}}
                                                  </td>
                                                </tr>
                                                <tr>
                                                  <th>담당자명</th>
                                                  <td colspan="2">
                                                    {{resultSelectData.representName}}
                                                  </td>
                                                </tr>
                                                <tr>
                                                  <th>연락처</th>
                                                  <td colspan="2">
                                                    <input type="text"
                                                           @input="typing($event, 'no', 'telephoneNumber')"
                                                           v-model="resultSelectData.telephoneNumber"
                                                           maxlength="11"
                                                           style="width: 100%; text-align: left;"/>
                                                  </td>
                                                </tr>
                                                <tr>
                                                  <th>이메일</th>
                                                  <td colspan="2">
                                                    <input type="text"
                                                           @input="typing($event, 'ml', 'email')"
                                                           v-model="resultSelectData.email"
                                                           maxlength="100"
                                                           style="width: 100%;"/>
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
                        <ul v-show="corpId !== '' && resultData[0]">
                            <li>
                              <input v-show="userMenuAuth.create_data || userMenuAuth.modify_data"
                                class="btn_save btn-lg"
                                type="button"
                                value="저장"
                                @click="set()"/>
                            </li>
                        </ul>
                        <p v-if="!resultData[0]">
                          등록 정보가 없어 수정이 불가합니다.
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
  name: 'TelAndEmailInfo',
  created () {
  },
  mounted () {
    this.get()
  },
  data () {
    return {
      isLoading: false,
      resultSelectData: '',
      resultData: {}
    }
  },
  methods: {
    setSelectData (val) {
      this.resultSelectData = Object.assign({}, this.resultData[val])
    },
    typing: function (evt, type, name) {
      let val = evt.target.value
      // eslint-disable-next-line camelcase
      let pattern_no = /[^0-9]/
      // eslint-disable-next-line camelcase
      let pattern_mail = /[ㄱ-ㅎㅏ-ㅣ가-힣]/
      // eslint-disable-next-line camelcase
      let pattern_sp = /[~!#$%<>^&*()=+’"'{}]/

      if (type === 'no') {
        if (pattern_no.test(val)) {
          if (name === 'telephoneNumber') this.resultSelectData.telephoneNumber = evt.target._value
        } else {
          if (name === 'telephoneNumber') this.resultSelectData.telephoneNumber = evt.target.value.trim()
        }
      } else if (type === 'ml') {
        if (pattern_mail.test(val) || pattern_sp.test(val)) {
          if (name === 'email') this.resultSelectData.email = evt.target._value
        } else {
          if (name === 'email') this.resultSelectData.email = evt.target.value.trim()
        }
      }
    },
    get () {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        this.http
          .get('/DataCorrection/Etc/user/' + this.corpId)
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            if (result.data.resultCode === 0) {
              this.resultData = result.data.items
            } else {
              this.resultData = {}
            }
          })
          .catch(e => {
            console.log('TelAndEmailInfo.vue : ' + e)
          })
      }
    },
    set () {
      if (this.validate()) return false
      if (this.userMenuAuth.create_data || this.userMenuAuth.modify_data) {
        this.isLoading = true

        this.http
          .post('/DataCorrection/Etc/user',
            {
              'seq': this.resultSelectData.seq,
              'id': this.corpId,
              'telephoneNumber': this.resultSelectData.telephoneNumber,
              'email': this.resultSelectData.email,
              'userId': this.$store.getters.getUser.luna_id,
              'userCode': 1,
              'userNo': this.$store.getters.getUser.luna_no
            })
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            let msg = '담당자 전화번호 정보 변경처리 되었습니다.'
            if (result.data.resultCode !== 0) {
              msg = '담당자 전화번호 정보 변경처리 실패했습니다.'
            }
            this.$modal.show(ModalAlert,
              {
                title: '담당자 전화번호 정보 변경',
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
            console.log('TelAndEmailInfo.vue : ' + e)
          })
      }
    },
    validate () {
      let mailChk = /^[0-9a-zA-Z]([-_.]|[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/g
      if (!mailChk.test(this.resultSelectData.email)) {
        this.$modal.show(ModalAlert,
          {
            title: '담당자 전화번호 정보 변경',
            text: 'e-mail 형식이 올바르지 않습니다'
          }, {
            width: 450,
            height: 'auto'
          }
        )
        return true
      }
      return false
    }
  },
  props: [
    'userMenuAuth',
    'corpId',
    'http'
  ]
}
</script>
