<template>
    <div class="Paper">
            <div class="formContainer">
                <form action="#" method="post">
                    <article>
                        <div class="articleWrap">
                            <div class="paperDetailWrap">
                                <div class="titleWrap">
                                    <p class="title">상담톡 통합상담 버튼 노출</p>
                                </div>
                                <div class="detailContent">
                                    <div class="tableWrap">
                                        <table summary="상담톡 통합상담 버튼 노출">
                                            <caption class="blind">상담톡 통합상담 버튼 노출</caption>
                                            <colgroup>
                                                <col style="width:100px"/>
                                                <col/>
                                            </colgroup>
                                            <tbody>
                                              <tr>
                                                <th>노출 여부<span class="necessary"></span></th>
                                                <td>
                                                  <p class="toggleWrap">
                                                    <label class="switch">
                                                      <input type="checkbox" v-model="resultData" :disabled="resultData === null">
                                                      <span class="slider"></span>
                                                    </label>
                                                  </p>
                                                </td>
                                              </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </article>
                    <div class="btnWrap">
                        <ul v-show="corpId !== '' && resultData !== null">
                            <li>
                              <input v-show="userMenuAuth.create_data || userMenuAuth.modify_data"
                                class="btn_save btn-lg"
                                type="button"
                                value="저장"
                                @click="set()"/>
                            </li>
                        </ul>
                        <p v-if="resultData === null">
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
  name: 'ChattingAllState',
  created () {
  },
  mounted () {
    this.get()
  },
  data () {
    return {
      isLoading: false,
      resultData: null
    }
  },
  methods: {
    get () {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        this.http
          .get('/DataCorrection/BizMessage/chattingAllStatus/' + this.corpId)
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            if (result.data.resultCode === 0) {
              this.resultData = result.data.visible
            } else {
              this.resultData = null
            }
          })
          .catch(e => {
            console.log('ChattingAllState.vue : ' + e)
          })
      }
    },
    set () {
      if (this.userMenuAuth.create_data || this.userMenuAuth.modify_data) {
        this.isLoading = true

        this.http
          .post('/DataCorrection/BizMessage/chattingAllStatus',
            {
              'visible': this.resultData,
              'domain': this.corpId,
              'userId': this.$store.getters.getUser.luna_id,
              'userCode': 1,
              'userNo': this.$store.getters.getUser.luna_no
            })
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            let msg = '상담톡 통합상담 버튼노출 처리되었습니다.'
            if (result.data.resultCode !== 0) {
              msg = '상담톡 통합상담 버튼노출 처리실패했습니다.'
            }
            this.$modal.show(ModalAlert,
              {
                title: '상담톡 통합상담 버튼노출',
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
            console.log('ChattingAllState.vue : ' + e)
          })
      }
    }
  },
  props: [
    'userMenuAuth',
    'corpId',
    'http'
  ]
}
</script>
