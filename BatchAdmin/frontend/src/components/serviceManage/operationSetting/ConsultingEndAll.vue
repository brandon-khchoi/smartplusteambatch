<template>
    <div class="ConsultingEndAll">
            <div class="formContainer">
                <form action="#" method="post">
                    <article>
                        <div class="articleWrap">
                            <div class="consultingEndAllDetailWrap">
                                <div class="titleWrap">
                                    <p class="title">상담건 종료처리</p>
                                </div>
                                <div class="detailContent">
                                    <div class="tableWrap">
                                        <table summary="상담건 종료처리">
                                            <caption class="blind">상담건 종료처리</caption>
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
                                                <th>상담원 ID<span class="necessary"></span></th>
                                                <td colspan="2">
                                                  <input type="text"
                                                         v-model="conserId"
                                                         style="width:70%;"
                                                         maxlength=11>
                                                  <p class="btn_search" @click="get()">상담건 조회</p>
                                                  <input type="text" hidden>
                                                </td>
                                              </tr>
                                            </tbody>
                                        </table>
                                    </div>

                                    <div v-if="resultData != null" class="tableWrap">
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
                                              <tr>
                                                <th>상담원 이름</th>
                                                <td colspan="2">
                                                  {{resultData.managerName}}
                                                </td>
                                              </tr>
                                              <tr>
                                                <th>상담원 상담건</th>
                                                <td colspan="2">
                                                  {{resultData.chattingInProgressCount}}
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
                        <ul v-show="corpId !== '' && consCnt > 0">
                            <li>
                              <input v-show="userMenuAuth.create_data || userMenuAuth.modify_data"
                                class="btn_save btn-lg"
                                type="button"
                                value="상담건 종료"
                                @click="set()"/>
                            </li>
                        </ul>
                        <p v-if="consCnt === 0">
                          * 진행중인 상담건이 없습니다.
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
  name: 'ConsultingEndAll',
  created () {
  },
  mounted () {
    // this.get()
  },
  data () {
    return {
      isLoading: false,
      conserId: null,
      resultData: null,
      consCnt: ''
    }
  },
  methods: {
    get () {
      if (this.userMenuAuth.retrieve_list && this.conserId != null) {
        this.isLoading = true
        this.http
          .get('/DataCorrection/BizMessage/chattingStatus/' + this.corpId + '/' + this.conserId)
          .then(result => {
            this.isLoading = false
            this.dataReset()
            if (result.data.resultCode === 0) {
              this.resultData = result.data
              this.consCnt = result.data.chattingInProgressCount
            } else {
              this.$modal.show(ModalAlert,
                {
                  title: '상담건 조회',
                  text: '미등록 상담원입니다.'
                }, {
                  width: 450,
                  height: 'auto'
                }
              )
            }
          })
          .catch(e => {
            this.isLoading = false
            console.log('ConsultingEndAll.vue : ' + e)
          })
      }
    },
    set () {
      if (this.userMenuAuth.create_data || this.userMenuAuth.modify_data) {
        this.isLoading = true

        this.http
          .post('/DataCorrection/BizMessage/chattingStatus',
            {
              'loginId': this.conserId,
              'domain': this.corpId,
              'userId': this.$store.getters.getUser.luna_id,
              'userCode': 1,
              'userNo': this.$store.getters.getUser.luna_no
            })
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            let msg = '상담건 종료처리되었습니다.'
            if (result.data.resultCode !== 0) {
              msg = result.data.message
            }
            this.$modal.show(ModalAlert,
              {
                title: '상담건 종료처리',
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
            console.log('ConsultingEndAll.vue : ' + e)
          })
      }
    },
    dataReset () {
      this.resultData = null
      this.consCnt = ''
    }
  },
  props: [
    'userMenuAuth',
    'corpId',
    'http'
  ]
}
</script>
