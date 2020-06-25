<template>
  <div class="popupContainer writeContainer qnaPopup2" style="display: block;">
    <div class="popupWrap" style="left: calc(50% - 417px); top: calc(50% - 262px);">
      <div class="popupTitleWrap">
        <p class="title">1:1 문의 내용</p>
        <p class="close" @click="$emit('close')">
          <i class="fas fa-times"></i>
        </p>
      </div>
      <div class="popupContentWrap" style="width: 100%;">
        <div class="popupContent">
          <div class="formContainer">
            <form action="#" method="post">
              <article>
                <div class="articleWrap">
                  <div class="tableWrap">
                    <table summary="1:1 문의 내용">
                      <caption class="blind">1:1 문의 내용</caption>
                      <colgroup>
                        <col style="width:100px" />
                        <col />
                        <col style="width:100px" />
                        <col />
                      </colgroup>
                      <tbody>
                        <tr>
                          <th>회원 ID</th>
                          <td> {{ qnaInfo.memberId }} </td>
                          <th>문의일</th>
                          <td> {{ qnaInfo.createDate }} </td>
                        </tr>
                        <tr>
                          <th>법인명</th>
                          <td>{{ qnaInfo.bizName }}</td>
                          <th>상호명</th>
                          <td> {{ qnaInfo.mallNames }} </td>
                        </tr>
                        <tr>
                          <th>휴대폰번호</th>
                          <td>{{ qnaInfo.memberTelNo }}</td>
                          <th>이메일</th>
                          <td>{{ qnaInfo.memberEmailAddr }}</td>
                        </tr>
                        <tr>
                          <th>제목</th>
                          <td colspan="3">
                            {{ qnaInfo.questionTitle}}
                          </td>
                        </tr>
                        <tr>
                          <th>문의내용</th>
                          <td colspan="3">
                            <textarea rows="5"  disabled="disabled" :value="qnaInfo.questionText"> </textarea>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                    <table summary="1:1 문의 작성">
                      <caption class="blind">1:1 문의 작성</caption>
                      <colgroup>
                        <col style="width:100px" />
                        <col />
                        <col style="width:100px" />
                        <col />
                      </colgroup>
                      <tbody>
                        <tr v-if="qnaInfo.answerYn == 1" >
                          <th>답변자</th>
                          <td> {{ codeData.lunaAccount[qnaInfo.answerLunaNo] }} </td>
                          <th>답변일</th>
                          <td v-text="qnaInfo.answerDate.replace('(','').replace(')','')"> {{ qnaInfo.answerDate }} </td>
                        </tr>
                        <tr>
                          <th>답변내용<span v-if="qnaInfo.answerYn == 0">*</span></th>
                          <td colspan="3">
                            <textarea rows="5" v-if="qnaInfo.answerYn == 1" disabled="disabled" :value="qnaInfo.answerText"> </textarea>
                            <textarea rows="5" v-else v-model="qnaInfo.answerText"> </textarea>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
              </article>
              <div class="btnWrap">
                <ul>
                  <li>
                    <input class="btn_cancel" type="button" @click="$emit('close')" value="닫기" />
                  </li>
                  <li v-if="qnaInfo.answerYn == 0">
                    <input class="btn_save" type="button" @click="saveAnswerConfirm()" value="답변" />
                  </li>
                </ul>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import http from '../../../http-commons'
import store from '../../../vuex/store'
import ModalAlert from '@/components/common/modal/ModalAlert'
import ModalConfirm from '@/components/common/modal/ModalConfirm'
import { eventBus } from '@/main'

export default {
  data () {
    return {
      qnaInfo: {}
    }
  },
  mounted () {
    this.getQnaInfo()
    eventBus.$on('qnaAnswerConfirm', () => {
      this.saveAnswer()
    })
  },
  beforeDestroy () {
    eventBus.$off('qnaAnswerConfirm')
  },
  methods: {
    saveAnswerConfirm () {
      if (this.qnaInfo.answerText === undefined || this.qnaInfo.answerText.replace(/\s/gi, '') === '') {
        this.$modal.show(ModalAlert,
          { title: '', text: '답변 내용은 필수 입니다.' }, { width: 450, height: 'auto' }
        )
        return
      }

      this.$modal.show(
        ModalConfirm,
        {
          title: '답변 등록 확인',
          data: {
            url: '',
            refresh: 'qnaAnswerConfirm'
          }
        },
        {
          width: 450,
          height: 'auto'
        }
      )
    },
    getQnaInfo () {
      http
        .get('/qna/getQnaInfo', {
          params: {
            qnaNo: this.qnaNo
          }
        }).then(response => {
          this.qnaInfo = response.data.data
        }).catch(e => {
          console.log(e)
        })
    },
    saveAnswer () {
      let params = {
        qnaNo: this.qnaNo,
        answerText: this.qnaInfo.answerText,
        answerLunaNo: store.getters.getUser.luna_no,
        emailAddr: this.qnaInfo.memberEmailAddr,
        questionTitle: this.qnaInfo.questionTitle,
        questionText: this.qnaInfo.questionText
      }
      http
        .post('/qna/saveAnswer', JSON.stringify(params))
        .then(response => {
          console.log(response)
          eventBus.$emit('qnaAnswer')
          this.$emit('close')
        }).catch(e => {
          console.log(e)
        })
    }
  },
  props: {
    qnaNo: Number,
    'codeData': Object
  }

}
</script>

<style>
</style>
