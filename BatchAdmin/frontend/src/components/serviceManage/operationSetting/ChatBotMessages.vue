<template>
    <div class="Paper">
            <div class="formContainer">
                <form action="#" method="post">
                    <article>
                        <div class="articleWrap">
                            <div class="paperDetailWrap">
                                <div class="titleWrap">
                                    <p class="title">상담톡 메시지 변경</p>
                                </div>
                                <div class="detailContent">
                                    <div class="tableWrap">
                                        <table summary="상담톡 메시지 변경">
                                            <caption class="blind">상담톡 메시지 변경</caption>
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
                                              <template v-for="(item, idx) in chatMsgData">
                                                <tr :key="item.id">
                                                  <th>종류</th>
                                                  <td>
                                                    <p>{{msgNames[item.botName]}}</p>
                                                     ( {{item.botName}} )
                                                  </td>
                                                  <th>번호</th>
                                                  <td>
                                                    {{item.id}}
                                                  </td>
                                                </tr>
                                                <tr :key="idx">
                                                  <th>내용</th>
                                                  <td colspan="3">
                                                    <textarea v-model="item.messageContent"
                                                              rows="4"
                                                              maxlength="1000"
                                                    >
                                                    </textarea>
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
  name: 'ChatBotMessages',
  created () {
  },
  mounted () {
    this.get()
  },
  data () {
    return {
      isLoading: false,
      msgNames: {
        'chatting': '연결 메시지',
        'end': '연결 메시지',
        'hello': '인사 메시지',
        'chatting_end': '종료 메시지'
      },
      resultData: {},
      chatMsgData: {}// chatting 과 end 메세지 통합에 의한 가공 처리값
    }
  },
  methods: {
    get () {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        this.http
          .get('/DataCorrection/BizMessage/chatBotMessages/' + this.corpId)
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            if (result.data.resultCode === 0) {
              this.resultData = result.data.items

              this.chatMsgData = this.resultData.filter(item => {
                return item.botName !== 'end'
              })
            } else {
              this.resultData = {}
              this.chatMsgData = {}
            }
          })
          .catch(e => {
            console.log('ChatBotMessages.vue : ' + e)
          })
      }
    },
    set () {
      if (this.userMenuAuth.create_data || this.userMenuAuth.modify_data) {
        let chatting = this.chatMsgData.filter(item => {
          return item.botName === 'chatting'
        })
        let end = this.resultData.filter(item => {
          return item.botName === 'end'
        })
        this.chatMsgData.push({
          'id': end[0].id,
          'createdDate': end[0].createdDate,
          'updatedDate': end[0].updatedDate,
          'botName': 'end',
          'messageContent': chatting[0].messageContent
        })

        this.isLoading = true
        this.http
          .post('/DataCorrection/BizMessage/chatBotMessages',
            {
              'items': this.chatMsgData,
              'domain': this.corpId,
              'userId': this.$store.getters.getUser.luna_id,
              'userCode': 1,
              'userNo': this.$store.getters.getUser.luna_no
            })
          .then(result => {
            // console.log(result.data)
            this.isLoading = false
            let msg = '상담톡 메시지 변경처리 되었습니다.'
            if (result.data.resultCode !== 0) {
              msg = '상담톡 메시지 변경처리 실패했습니다.'
            }
            this.$modal.show(ModalAlert,
              {
                title: '상담톡 메시지 변경',
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
            console.log('ChatBotMessages.vue : ' + e)
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
