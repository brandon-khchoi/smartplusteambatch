<template>
  <div class="content partner">
    <h1>FAQ 상세</h1>
    <div class="formContainer">
      <form action="#" method="post" >
        <div class="writeContainer">
          <article>
            <div class="articleWrap">
              <div class="titleWrap">
                <p class="title">{{resultData.faq_title}}</p>
                <ul class="infoWrap">
                  <li class="writerWrap"><i :class="resultData.display_yn===1?'fas fa-eye':'far fa-eye-slash'"/></li>
                  <li class="timeWrap">{{resultData.create_date}}</li>
                </ul>
              </div>
              <div class="tableWrap">
                <table summary="FAQ 미리보기">
                  <caption class="blind">FAQ 미리보기</caption>
                  <colgroup>
                    <col style="width:100px" />
                    <col  />
                  </colgroup>
                  <tbody>
                    <tr>
                      <td colspan="2" class="scrollContainer" v-html="resultData.faq_text">
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </article>
          <div class="btnWrap">
            <ul>
              <li><router-link :to="{ name: 'faqlist'}"><a href="#" class="btn_cancel">목록으로</a></router-link></li>
              <li v-show="userMenuAuth.delete_data"><a href="#" class="btn_delete" @click="funcDelete()">삭제</a></li>
              <li v-show="userMenuAuth.modify_data"><router-link :to="{ name: 'faqedit', params:{faqData: resultData}}"><a href="#" class="btn_save">수정</a></router-link></li>
            </ul>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import http from '@/http-commons'
import ModalAlert from '@/components/common/modal/ModalAlert.vue'

export default {
  name: 'FaqDetail',
  components: {
  },
  data () {
    return {
      resultData: ''
    }
  },
  created () {
    this.get()
  },
  methods: {
    get () {
      if (this.userMenuAuth.retrieve_list) {
        this.isLoading = true
        http
          .get('/faq/faqdetail', {
            params: { // query string
              faq_no: this.$route.params.faqNo
            }
          })
          .then(response => {
            this.resultData = response.data
          })
          .catch(e => {
            console.log(e)
          })
          .finally(() => {
            this.isLoading = false
          })
      }
    },
    funcDelete () {
      if (!this.userMenuAuth.delete_data) return false

      this.isLoading = true
      http
        .post('/faq/faqmod', {
          params: { // query string
            faq_no: this.$route.params.faqNo,
            del_yn: 1,

            login_luna_no: this.$store.getters.getUser.luna_no
          }
        })
        .then(response => {
          let msg = '처리중 문제가 발생했습니다.'
          if (response.data === '0000') {
            msg = '삭제 되었습니다.'
            this.$router.push({name: 'faqlist'})
          }
          this.$modal.show(
            ModalAlert,
            {
              title: 'FAQ 삭제',
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
    }
  },
  props: {
    'userMenuAuth': Object,
    'codeData': Object
  }
}
</script>
