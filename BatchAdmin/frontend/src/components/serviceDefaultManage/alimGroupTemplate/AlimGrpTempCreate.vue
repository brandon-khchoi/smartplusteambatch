<template>
  <div class="AlimGrpTempCreate">
    <div class="content employ">
      <h1>알림톡그룹템플릿 등록</h1>
      <div class="formContainer">
        <form action="#" method="post">
          <article>
          <div class="articleWrap">
            <div class="tableWrap tableRegister">
              <table>
                <colgroup>
                  <col style="width:200px">
                  <col>
                </colgroup>
                <tbody>
                  <tr>
                      <th>
                        허브파트너
                        <span class="necessary">*</span>
                      </th>
                      <td>
                        <select v-model="partnerCode">
                          <option v-show="partnerCode===''" disabled value=''>선택하세요.</option>
                          <option value="0">비즈톡</option>
                          <option value="1">쥬피터</option>
                        </select>
                      </td>
                    </tr>
                  <tr>
                    <th>
                      그룹템플릿 등록
                      <span class="necessary">*</span>
                    </th>
                    <td>
                      <div class="fileWrap">
                        <span class="textWrap">
                          <i class="far fa-file"></i>
                          <input type="text" readonly="readonly"
                            :value="fileName==''?'등록된 파일이 없습니다.':fileName">
                          <!-- <a href="#"><i class="fas fa-times"></i></a> -->
                        </span>
                        <input id="member_biz_license_img"
                               type="file"
                               accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
                               @change="handleFileUpload($event)">
                        <label for="member_biz_license_img">파일선택</label>
                      </div>
                      <span v-show="fileSizeOverMsg!==''"
                            style="color: #FF0000; padding-left:10px;">* {{fileSizeOverMsg}}</span>
                      <br />
                      <br />
                      <p>*파일형식 : xls, xlsx, csv</p>
                      <p>*최대 20MB</p>
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
                <input class="btn_cancel" type="button" value="취소" @click="goList()">
              </li>
              <li v-show="userMenuAuth.create_data">
                <input class="btn_save" type="button" value="등록" @click="create()" :disabled="partnerCode==='' || fileName===''">
              </li>
            </ul>
          </div>
        </form>
      </div>
    </div>
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import ModalAlert from '../../common/modal/ModalAlert.vue'
import PhoneForm from '../../common/PhoneForm.vue'

export default {
  mounted () {
  },
  components: {
    ModalAlert,
    PhoneForm
  },
  data () {
    return {
      partnerCode: '',
      fileData: '',
      fileName: '',
      fileSizeOverMsg: '',
      isLoading: false
    }
  },
  methods: {
    handleFileUpload (event) {
      if (event.target.files.length > 0) {
        let fileSize = event.target.files[0].size
        let maxSize = 20 * 1024 * 1024// 20MB

        if (fileSize > maxSize) {
          this.fileSizeOverMsg = '첨부파일 사이즈는 20MB 이내로 등록 가능합니다. '
          this.fileName = ''
          this.fileData = ''
        } else {
          this.fileSizeOverMsg = ''
          this.fileName = event.target.files[0].name
          this.fileData = event.target.files[0]
        }
      } else {
        this.fileSizeOverMsg = ''
        this.fileName = ''
        this.fileData = ''
      }
    },
    goList () {
      this.$router.go(-1)
    },
    create () {
      let formData = new FormData()

      formData.append('HubPartner', this.partnerCode)
      formData.append('FormFile', this.fileData)
      formData.append('LunaId', this.$store.getters.getUser.luna_id)

      this.isLoading = true
      this.http
        .post('/AlimTalk/GroupTemplate/upload', formData)
        .then(result => {
          this.isLoading = false
          let msg = ''
          if (result.data.resultCode === 0 || result.data.resultCode === 4) { // 0:성공 4:중복등록
            msg = result.data.message
          } else {
            msg = '파일 업로드를 실패하였습니다. 파일 양식 확인 후 다시 시도해 주세요.'
          }
          this.$modal.show(
            ModalAlert,
            {
              title: '알림톡그룹템플릿 등록',
              text: msg
            },
            {
              width: 450,
              height: 167
            }
          )

          this.$router.push({
            name: 'alimgrptemplist'
          })
        })
        .catch(e => {
          this.isLoading = false
          console.log('AlimGrpTempCreate.vue : ' + e)
        })
    }
  },
  props: [
    'userMenuAuth',
    'http'
  ]
}
</script>
