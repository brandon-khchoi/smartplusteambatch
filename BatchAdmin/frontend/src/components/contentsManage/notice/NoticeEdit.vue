<template>
  <div class="content partner">
    <h1>
      공지사항 <span style="vertical-align: baseline;" v-if="this.$route.params.noticeInfo === undefined">등록</span><span style="vertical-align: baseline;" v-else>수정</span>
    </h1>
    <div class="formContainer">
      <article>
        <div class="articleWrap">
          <div class="partnerWriteWrap">
            <table summary="공지사항 등록">
              <caption class="blind">공지사항 등록</caption>
              <colgroup>
                <col style="width:100px" />
                <col style="width:calc(25% - 100px)" />
                <col style="width:100px" />
                <col style="width:calc(25% - 100px)" />
                <col style="width:100px" />
                <col style="width:calc(25% - 100px)" />
                <col style="width:100px" />
                <col />
              </colgroup>
              <tbody>
                <tr>
                  <th>
                    분류
                    <span class="necessary">*</span>
                  </th>
                  <td colspan="7">
                    <select v-model="params.noticeTypeCode" style="width:15%">
                      <option value="">선택하세요</option>
                      <template v-for="(val, code) in codeData.noticeType">
                        <option :key="code" :value="code">{{ val }}</option>
                      </template>
                    </select>
                  </td>
                </tr>
                <tr>
                  <th>
                    제목
                    <span class="necessary">*</span>
                  </th>
                  <td colspan="7">
                    <input type="text" v-model="params.noticeTitle" maxlength="45" minlength="2" />
                    <span class="inputWrap" style="padding-left: 20px;">
                      <input type="checkbox" id="partner02" v-model="params.importantYn" />
                      <label for="partner02">중요</label>
                    </span>
                  </td>
                </tr>
                <tr>
                  <th>
                    내용
                    <span class="necessary">*</span>
                  </th>
                  <td colspan="7">
                    <smart-editor ref="smartEditor"
                      :editorContents="editorContents"
                      :rowSize="25"
                      ></smart-editor>
                  </td>
                </tr>
                <tr>
                  <th>첨부파일</th>
                  <td colspan="7">
                    <div class="fileWrap">
                      <span class="textWrap">
                        <i class="far fa-file"></i>
                        <input type="text" readonly="readonly" value="등록된 파일이 없습니다." />
                        <a href="#">
                          <i class="fas fa-times"></i>
                        </a>
                      </span>
                      <input id="fileSearch" type="file" ref="selectedfile" @change="setfiles($event)" multiple="multiple"/>
                      <label for="fileSearch">파일첨부</label>
                    </div>
                    <div style="font-size: 13px; padding-top: 5px; padding-bottom: 0px;"> * 첨부파일 전체 최대 용량 100MB 이내</div>
                    <ul>
                      <template v-for="(item, idx) in noticeFileInfo" >
                        <li :key="idx" style="padding-top: 5px; padding-bottom: 5px;">
                           <i class="fas fa-paperclip"></i> {{ item.noticeFileName }}
                          <i class="fas fa-times" @click="uploadFileDelete(idx)" style="cursor: pointer;"></i>
                        </li>
                      </template>
                    </ul>
                  </td>
                </tr>
                <tr>
                  <th>서비스 구분</th>
                  <td colspan="7">
                    <template v-for="(item, idx) in displayType">
                      <span class="inputWrap" :key="idx">
                        <input type="checkbox" :id="'partner01-0'+idx" v-model="item.displayVal" v-if=" idx == '0'" @change="allselect">
                        <input type="checkbox" :id="'partner01-0'+idx" v-model="item.displayVal" v-if=" idx != '0'" @change="CheckDisplayVal">
                        <label :for="'partner01-0'+idx" >{{ item.name }}</label>
                      </span>
                    </template>
                  </td>
                </tr>
                <tr>
                  <th>노출여부<span class="necessary">*</span></th>
                  <td colspan="7">
                    <span class="inputWrap">
                      <input type="radio" name="partner03" value='1' id="partner03-01" v-model="params.displayYn" />
                      <label for="partner03-01">노출</label>
                    </span>
                    <span class="inputWrap">
                      <input type="radio" name="partner03" value='0' id="partner03-02" v-model="params.displayYn"/>
                      <label for="partner03-02">노출안함</label>
                    </span>
                  </td>
                </tr>
                <tr>
                  <th>노출기간<span class="necessary">*</span></th>
                  <td colspan="7">
                    <div class="datepicker">
                      <DatepickerDate
                      v-bind:propFromDate="fromDate"
                      v-bind:propToDate="toDate"
                      :propsSetBtn="4"
                      :propIsButtonHide= true
                      v-on:update:fromDate="setFromDate"
                      v-on:update:toDate="setToDate"
                      >
                      <!-- 자식이 부모한테 보낼때 v-on 및 메소드명을 뒤에 사용해서 올려줌 -->
                      </DatepickerDate>
                    </div>
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
            <input class="btn_cancel" type="button" @click="noticeWriteCancelConfirm()" value="취소" />
          </li>
          <li>
            <input class="btn_save" type="button" v-if="this.$route.params.noticeInfo === undefined" @click="noticeWriteConfirm()" value="등록" />
            <input class="btn_save" type="button" v-else @click="noticeWriteConfirm()" value="수정" />
          </li>
        </ul>
      </div>
    </div>
    <Spinner v-if="isloading"/>
    <Spinner v-bind:message="'업로드 중...'" v-if="isUploadLoading"/>
  </div>
</template>

<script>
import http from '../../../http-commons'
import DatepickerDate from '@/components/common/DatepickerDate'
import * as lang from 'vuejs-datepicker/dist/locale'
import ModalAlert from '@/components/common/modal/ModalAlert'
import ModalConfirm from '@/components/common/modal/ModalConfirm'
import { eventBus } from '@/main'
import SmartEditor from '@/components/common/SmartEditor'

export default {
  components: {
    DatepickerDate,
    'smart-editor': SmartEditor
  },
  data () {
    return {
      noticeInfo: {},
      noticeFileInfo: [],
      languages: lang,
      uploadFileSize: 0,
      displayType: [],
      allSelected: false,
      editorContents: '',
      fromDate: new Date(),
      toDate: null,
      params: {
        noticeNo: '',
        noticeTypeCode: '',
        noticeTitle: '',
        noticeText: '',
        displayVal: '',
        displayYn: 0,
        importantYn: false
      },
      isloading: false,
      isUploadLoading: false
    }
  },
  created () {
    this.displayTypeSetting()
    if (this.$route.params.noticeInfo !== undefined) {
      this.noticeInfo = this.$route.params.noticeInfo
      this.noticeFileInfo = this.$route.params.noticeFileInfo

      this.noticeInfoSetting()
      this.getUploadNoticeFileSize()
    }
  },
  mounted () {
    eventBus.$on('noticeWrite', () => {
      this.noticeWrite()
    })
    eventBus.$on('noticeWriteCancel', () => {
      this.goNoticeList()
    })
  },
  beforeDestroy () {
    eventBus.$off('noticeWrite')
    eventBus.$off('noticeWriteCancel')
  },
  methods: {
    uploadFileDelete (idx) {
      const fileNo = this.noticeFileInfo[idx].noticeFileNo
      if (fileNo === undefined) {
        this.noticeFileInfo.splice(idx, 1)
        this.$modal.show(ModalAlert,
          { title: '', text: '첨부파일 삭제 성공 하였습니다.' }, { width: 450, height: 'auto' }
        )
        return
      }

      http.get('/notice/deleteNoticeFile', {
        params: {
          noticeFileNo: fileNo
        }
      }).then(response => {
        if (response.data.responseCode === 0) {
          this.noticeFileInfo.splice(idx, 1)
          this.$modal.show(ModalAlert,
            { title: '', text: '첨부파일 삭제 성공하였습니다.' }, { width: 450, height: 'auto' }
          )
        } else {
          this.$modal.show(ModalAlert,
            { title: '', text: response.data.responseMsg }, { width: 450, height: 'auto' }
          )
        }
      }).catch(e => {
        console.log(e)
        this.$modal.show(ModalAlert,
          { title: '', text: '첨부파일 삭제 실패하였습니다.' }, { width: 450, height: 'auto' }
        )
      })
    },
    getNoticeInfo (noticeNo) {
      http.get('/notice/getNoticeInfo', {
        params: {
          noticeNo: noticeNo
        }
      }).then(response => {
        this.params.noticeNo = this.$route.query.editNoticeNo
        this.params = response.data.data.noticeInfo

        this.editorContents = response.data.data.noticeInfo.noticeText

        this.fromDate = new Date(response.data.data.noticeInfo.displayStartDate)
        this.toDate = new Date(response.data.data.noticeInfo.displayEndDate)

        this.noticeFileInfo = response.data.data.noticeFileInfo

        const displayBinaryArr = this.params.noticeDisplayVal.toString(2).split('').reverse()
        if (this.params.noticeDisplayVal === 1) {
          for (const idx in this.displayType) {
            this.displayType[idx].displayVal = true
          }
        } else {
          for (const idx in displayBinaryArr) {
            if (displayBinaryArr[idx] === '1') {
              this.displayType[idx].displayVal = true
            }
          }
        }
      }).catch(e => {
        console.log(e)
      })
    },
    getUploadNoticeFileSize () {
      // eslint-disable-next-line no-undef
      const url = imageApiUrl + '/notice/getNoticeFileSize'
      http
        .get(url, {
          params: {
            noticeNo: this.noticeInfo.noticeNo
          }
        }).then(response => {
          this.uploadFileSize = response.data
        }).catch(e => {
          console.log(e)
        })
    },
    displayTypeSetting () {
      for (const val in this.codeData.noticeDisplay) {
        let item = {}
        item.name = this.codeData.noticeDisplay[val]
        item.displayVal = false
        this.displayType.push(item)
      }
    },
    noticeInfoSetting () {
      this.params = this.noticeInfo
      this.editorContents = this.noticeInfo.noticeText

      this.fromDate = new Date(this.noticeInfo.displayStartDate)
      this.toDate = new Date(this.noticeInfo.displayEndDate)

      const displayBinaryArr = this.params.noticeDisplayVal.toString(2).split('').reverse()
      if (this.params.noticeDisplayVal === 1) {
        this.allselect()
      } else {
        for (const idx in displayBinaryArr) {
          if (displayBinaryArr[idx] === '1') {
            this.displayType[idx].displayVal = true
          }
        }
      }
    },
    allselect () {
      this.allSelected = !this.allSelected
      for (const idx in this.displayType) {
        this.displayType[idx].displayVal = this.allSelected
      }
    },
    CheckDisplayVal () {
      this.params.noticeDisplayVal = ''
      let tempAllSelected = true

      for (const idx in this.displayType) {
        if (idx !== '0' && !this.displayType[idx].displayVal) {
          tempAllSelected = this.displayType[idx].displayVal
        }
      }
      this.allSelected = tempAllSelected
      this.displayType[0].displayVal = tempAllSelected
    },
    openStartDatePicker () {
      this.$refs.openStartDate.showCalendar()
    },
    openEndDatePicker () {
      this.$refs.openEndDate.showCalendar()
    },
    setfiles (e) {
      var input = e.target
      let tempUploadFileSize = 0
      const formData = new FormData()
      if (input.files) {
        for (let idx = 0; idx < input.files.length; idx++) {
          tempUploadFileSize = this.uploadFileSize + input.files[idx].size
          if (tempUploadFileSize > 100 * 1024 * 1024) {
            this.$modal.show(ModalAlert,
              { title: '', text: '전체 파일 업로드 용량은 100mb를 초과 할 수 없습니다.' }, { width: 450, height: 'auto' }
            )
            return
          } else {
            this.uploadFileSize = tempUploadFileSize
          }

          formData.append(input.files[idx].name, input.files[idx])
        }
        this.fileUpload(formData)
      }
    },
    setDisplayVal () {
      let tempVal = []
      if (this.displayType[0].displayVal) {
        this.params.noticeDisplayVal = 1
      } else {
        for (const idx in this.displayType) {
          if (this.displayType[idx].displayVal) {
            tempVal.push('1')
          } else {
            tempVal.push('0')
          }
        }
        this.params.noticeDisplayVal = parseInt(tempVal.reverse().join(''), 2)
      }
    },
    validation () {
      let flag = false
      let message = ''
      if (this.params.noticeTypeCode === '') {
        message = '분류 선택은 필수 입니다.'
        flag = true
      } else if (this.params.noticeTitle.length < 2) {
        message = '제목은 2자 이상 입력해주세요'
        flag = true
      } else if (this.params.noticeText.replace(/(<(?!img)([^>]+)>)|&nbsp;|\s/ig, '') === '') {
        message = '내용은 필수 입니다.'
        flag = true
      } else if (this.params.noticeText.replace(/(<([^>]+)>)/ig, '').length > 5000) {
        message = '내용은 5000자 이내로 작성해야합니다.'
        flag = true
      }
      if (flag) {
        this.$modal.show(ModalAlert,
          { title: '', text: message }, { width: 450, height: 'auto' }
        )
      }

      return flag
    },
    noticeWrite () {
      this.isloading = true
      this.params.uploadFileList = []
      for (const idx in this.noticeFileInfo) {
        if (this.noticeFileInfo[idx].noticeFileNo === undefined) {
          this.params.uploadFileList.push(this.noticeFileInfo[idx])
        }
      }

      http
        .post('/notice/write', JSON.stringify(this.params))
        .then(response => {
          if (response.data.responseCode === 0) {
            this.$modal.show(ModalAlert,
              { title: '', text: response.data.responseMsg }, { width: 450, height: 'auto' }
            )
            this.$router.push({
              name: 'noticeList'
            })
          } else {
            this.isloading = false
            this.$modal.show(ModalAlert,
              { title: '', text: response.data.responseMsg }, { width: 450, height: 'auto' }
            )
          }
        })
        .catch(e => {
          this.isloading = false
          this.$modal.show(ModalAlert,
            { title: '', text: '공지 사항 등록에 실패하였습니다.' }, { width: 450, height: 'auto' }
          )
          console.log(e)
        })
    },
    fileUpload (formData) {
      this.isUploadLoading = true
      // eslint-disable-next-line no-undef
      const url = imageApiUrl + '/notice/fileUpload'

      http
        .post(url, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        })
        .then(response => {
          this.isUploadLoading = false
          if (response.data.responseCode === 0) {
            for (const idx in response.data.fileInfoList) {
              if (response.data.fileInfoList[idx].fileType.match('.(bmp|gif|jpg|jpeg|png)$')) {
                this.$refs.smartEditor.pasteHtml('<img src="' + response.data.fileInfoList[idx].noticeFileAddr + '" />')
              }
              this.noticeFileInfo.push(response.data.fileInfoList[idx])
            }
          } else {
            this.$modal.show(ModalAlert,
              { title: '', text: '파일 업로드 실패' }, { width: 450, height: 'auto' }
            )
          }
        }).catch(e => {
          this.isUploadLoading = false
          console.log(e)
          this.$modal.show(ModalAlert,
            { title: '', text: '파일 업로드 실패' }, { width: 450, height: 'auto' }
          )
        })
    },
    deleteNotice (noticeNo) {
      http
        .get('/notice/delete', {
          params: {
            noticeNo: noticeNo
          }
        })
        .then(response => {
        }).catch(e => {
          console.log(e)
        })
    },
    goNoticeList () {
      this.$router.push({
        name: 'noticeList'
      })
    },
    noticeWriteConfirm () {
      this.$refs.smartEditor.submitContents()
      this.params.noticeText = this.editorContents

      // eslint-disable-next-line no-undef
      this.params.displayStartDate = dateFormatToString(this.fromDate)

      if (this.toDate === null) {
      // eslint-disable-next-line no-undef
        this.params.displayEndDate = '9999-12-31'
      } else {
      // eslint-disable-next-line no-undef
        this.params.displayEndDate = dateFormatToString(this.toDate)
      }

      this.setDisplayVal()

      if (this.validation()) {
        return
      }
      this.$modal.show(
        ModalConfirm,
        {
          title: '공지사항 등록',
          data: {
            url: '',
            refresh: 'noticeWrite'
          }
        },
        {
          width: 450,
          height: 'auto'
        }
      )
    },
    noticeWriteCancelConfirm () {
      this.$modal.show(
        ModalConfirm,
        {
          title: '공지사항 등록 취소',
          data: {
            url: '',
            refresh: 'noticeWriteCancel'
          }
        },
        {
          width: 450,
          height: 'auto'
        }
      )
    },
    setFromDate (value) {
      this.fromDate = value
    },
    setToDate (value) {
      this.toDate = value
    }
  },
  props: {
    'userMenuAuth': Object,
    'codeData': Object
  }
}

</script>
<style>
  .width-max {
    width: 100% !important
  }
  .datepicker input[type="text"] {
    width: 100% !important
  }
</style>
