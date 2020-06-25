<template>
  <div class="content partner">
    <h1>FAQ 등록</h1>
    <div class="formContainer">
      <form action="#" method="post" >
        <article>
          <div class="articleWrap">
            <div class="partnerWriteWrap">
              <table summary="FAQ 등록">
                <caption class="blind">FAQ 등록</caption>
                <colgroup>
                  <col style="width:100px" />
                  <col style="width:calc(25% - 100px)" />
                  <col style="width:100px" />
                  <col style="width:calc(25% - 100px)" />
                  <col style="width:100px" />
                  <col style="width:calc(25% - 100px)" />
                  <col style="width:100px" />
                  <col  />
                </colgroup>
                <tbody>
                  <tr>
                    <th>노출위치<span class="necessary">*</span></th>
                    <td colspan="7">
                      <template v-for="(value, code) in codeData.faqDisplay">
                        <span :key="code" style="padding-left:10px;"><p class="inputWrap">
                          <input type="checkbox"
                                :id="'display_'+code"
                                :true-value="code" :false-value="0"
                                :checked="displayCheck(code)"
                                @click="displayValueUpdate($event.target.checked, code)">
                          <label :for="'display_'+code">{{value}}</label>
                        </p></span>
                      </template>
                    </td>
                  </tr>
                  <tr>
                    <th>분류<span class="necessary">*</span></th>
                    <td colspan="7">
                      <span style="width:20%">
                        <select v-model="resultData.faq_type_code">
                          <option value="" disabled>선택하세요</option>
                          <template v-for="(val, code) in codeData.faqType">
                            <option :key="code" :value="code">{{val}}</option>
                          </template>
                        </select>
                      </span>
                    </td>
                  </tr>
                  <tr>
                    <th>제목<span class="necessary">*</span></th>
                    <td colspan="7">
                      <input type="text" placeholder="" v-model="resultData.faq_title" maxlength="200"/>
                    </td>
                  </tr>
                  <tr>
                    <th>내용<span class="necessary">*</span></th>
                    <td colspan="7">
                      <div>
                        <smart-editor ref="smartEditor" :editorContents="editorContents"></smart-editor>
                      </div>
                    </td>
                  </tr>
                  <tr>
                    <th>노출여부<span class="necessary">*</span></th>
                    <td colspan="7">
                      <span class="inputWrap">
                        <input type="radio" name="partner03" v-model="resultData.display_yn" value="1" id="partner03-01" />
                        <label for="partner03-01">노출</label>
                      </span>
                      <span class="inputWrap">
                        <input type="radio" name="partner03" v-model="resultData.display_yn" value="0" id="partner03-02" />
                        <label for="partner03-02">노출안함</label>
                      </span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </article>
        <div class="btnWrap">
          <ul>
            <li><router-link :to="{ name: 'faqlist'}"><input class="btn_cancel" type="button" value="취소"></router-link></li>
            <li><input class="btn_save" type="button" value="저장" @click="set()"></li>
          </ul>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import http from '@/http-commons'
import ModalAlert from '@/components/common/modal/ModalAlert.vue'
import SmartEditor from '@/components/common/SmartEditor.vue'
import SanitizeHtml from 'sanitize-html'

export default {
  name: 'FaqCreate',
  components: {
    'smart-editor': SmartEditor
  },
  data () {
    return {
      editorContents: '',
      resultData: {
        display_yn: '0',
        faq_display_code: '0',
        faq_text: '',
        faq_title: '',
        faq_type_code: ''
      }
    }
  },
  methods: {
    set () {
      // smart-editor 내용 가져오기
      this.$refs.smartEditor.submitContents() // 자식 컴포넌트의 함수를 호출

      if (!this.userMenuAuth.create_data) return false
      if (!this.validate()) return false

      this.isLoading = true
      http
        .post('/faq/faqcreate', {
          params: { // query string
            faq_display_code: this.resultData.faq_display_code,
            faq_type_code: this.resultData.faq_type_code,
            faq_title: this.resultData.faq_title,
            faq_text: this.editorContents,
            display_yn: this.resultData.display_yn,

            login_luna_no: this.$store.getters.getUser.luna_no
          }
        })
        .then(response => {
          let msg = '처리중 문제가 발생했습니다.'
          if (response.data === '0000') {
            msg = '등록 되었습니다.'
            this.$router.push({name: 'faqlist'})
          }
          this.$modal.show(
            ModalAlert,
            {
              title: 'FAQ 등록',
              text: msg
            },
            {
              width: 450,
              height: 167
            }
          )
        })
        .catch(e => {
          console.log(e)
        })
        .finally(() => {
          this.isLoading = false
        })
    },
    displayValueUpdate (chk, code) {
      let codeVal = Number(code)
      let dpCode = Number(this.resultData.faq_display_code)
      if (chk) {
        if (codeVal === 1) {
          this.resultData.faq_display_code = 1
        } else {
          if ((dpCode & 1) === 1) {
            dpCode = codeVal
          } else {
            dpCode += codeVal
          }
          this.resultData.faq_display_code = dpCode
        }
      } else {
        if ((dpCode & 1) === 1) {
          this.resultData.faq_display_code = 0
        } else {
          dpCode -= codeVal
          this.resultData.faq_display_code = dpCode
        }
      }
    },
    displayCheck (code) {
      let dpCode = Number(this.resultData.faq_display_code)
      let val = Number(code)
      if ((dpCode & 1) === 1) { // 전체선택인경우 모두체크
        return true
      }
      if ((dpCode & val) === val) {
        return true
      }
      return false
    },
    validate () {
      let dpCode = Number(this.resultData.faq_display_code)
      let textContents = SanitizeHtml(this.editorContents, {allowedTags: [], allowedAttributes: []})
      if (dpCode === 0 ||
          this.resultData.faq_title.trim() === '' ||
          textContents.trim() === '' ||
          this.resultData.faq_type_code === '') {
        this.$modal.show(
          ModalAlert,
          {
            title: 'FAQ 등록',
            text: '필수 입력값을 확인하세요.'
          },
          {
            width: 450,
            height: 167
          }
        )
        return false
      }
      return true
    }
  },
  props: {
    'userMenuAuth': Object,
    'codeData': Object
  }
}
</script>
