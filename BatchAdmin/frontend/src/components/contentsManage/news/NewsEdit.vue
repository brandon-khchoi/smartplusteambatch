<template>
  <div class="content">
    <h1>NEWS <span style="vertical-align: baseline;" v-if="this.$route.params.newsInfo === undefined">등록</span><span style="vertical-align: baseline;" v-else>수정</span></h1>
    <div class="formContainer">
      <article>
        <div class="articleWrap">
          <div class="partnerWriteWrap">
            <table summary="공지사항 등록">
              <caption class="blind">공지사항 등록</caption>
              <colgroup>
                <col style="width:10%" />
                <col />
              </colgroup>
              <tbody>
                <tr>
                  <th>분류<span class="necessary">*</span></th>
                  <td>
                    <select v-model="params.newsTypeCode">
                      <option value="">선택하세요</option>
                      <option v-for="(val, code) in codeData.newsType" :key="code" :value="code" > {{ val }} </option>
                    </select>
                  </td>
                </tr>
                <tr>
                  <th>제목<span class="necessary">*</span></th>
                  <td>
                    <input type="text" v-model="params.newsTitle" style="width:320px" maxlength="45" minlength="2" />
                  </td>
                </tr>
                <tr>
                  <th>이미지<span class="necessary">*</span></th>
                  <td>
                    <div :class="'fileWrap '+ select">
                      <span class="textWrap">
                        <i class="far fa-file"></i>
                        <input type="text" readonly="readonly" style="border: 1px solid #e8e8e8;width: 522px" :value="fileName" placeholder=" 파일을 업로드해 주세요"/>
                        <a @click="fileDelete()">
                          <i class="fas fa-times"></i>
                        </a>
                      </span>
                      <input id="fileSearch" type="file" ref="selectedFile" accept="image/*" @change="setfile($event)" />
                      <label for="fileSearch">파일첨부</label>
                    </div>
                    <div style="color: #757575;margin-top: 10px;">
                      * 권장비율: 3:2 <br/>
                      * 지원파일: gif, jpg, png (최대 20MB)
                    </div>
                  </td>
                </tr>
                <tr>
                  <th>연결URL<span class="necessary">*</span></th>
                  <td>
                    <div class="tableTopWrap" style="padding-top: 0px;">
                    <input type="text" v-model="params.newsUrlAddr" :style="[isUrlOk ? {float: 'left',width: '522px'}: {border:'1px solid red',float: 'left',width: '522px'}]" />
                      <div class="btnWrap" style="margin-top: 3px;float: left;" >
                        <ul>
                          <li>
                            <p class="btn_add" v-if="isUrlOk"><a :href="params.newsUrlAddr" target="_blank" >연결확인</a></p>
                            <p class="btn_add" style="border: 1px solid gray;"  v-else><a href="#" style="color: #e8e8e8;background-color: gray; cursor: default;">연결확인</a></p>
                          </li>
                        </ul>
                      </div>
                    </div>
                    <div style="color: red;margin-top: 10px;margin-left: 5px;" v-if="!isUrlOk">
                      필수 입력 사항입니다.
                    </div>
                  </td>
                </tr>
                <tr>
                  <th>노출여부<span class="necessary">*</span></th>
                  <td>
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
                  <td>
                    <div class="datepickerWrap">
                      <DatepickerDate
                      v-bind:propFromDate="fromDate"
                      v-bind:propToDate="toDate"
                      :propIsButtonHide= "true"
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
            <input class="btn_preview  btn_grayLight" type="button" @click="newsPreview" value="미리보기">
          </li>
          <li>
            <input class="btn_cancel" type="button" @click="editCancel" value="취소" />
          </li>
          <li>
            <input class="btn_save" type="button" @click="saveNewsInfoConfirm" value="저장" />
          </li>
        </ul>
      </div>
    </div>
    <Spinner v-if="isloading"/>
  </div>
</template>

<script>

import ModalAlert from '@/components/common/modal/ModalAlert'
import DatepickerDate from '@/components/common/DatepickerDate'
import ModalConfirm from '@/components/common/modal/ModalConfirm'
import NewsPreviewModal from '@/components/contentsManage/news/NewsPreviewModal'
import * as lang from 'vuejs-datepicker/dist/locale'
import http from '../../../http-commons'
import { eventBus } from '@/main'

export default {
  components: {
    DatepickerDate
  },
  data () {
    return {
      languages: lang,
      displayDate: new Date(),
      fromDate: new Date(),
      toDate: null,
      uploadImageFile: null,
      params: {
        newsTypeCode: '',
        newsTitle: '',
        newsUrlAddr: 'https://',
        newsFileAddr: '',
        displayYn: 0
      },
      select: '',
      fileName: '',
      isloading: false,
      isUrlOk: false
    }
  },
  created () {
    if (this.$route.params.newsInfo !== undefined) {
      this.newInfoSetting()
    }
  },
  mounted () {
    eventBus.$on('saveNewsInfo', () => {
      this.saveNewsInfo()
    })
    eventBus.$on('editCancel', () => {
      this.$router.push({ name: 'newsList' })
    })
  },
  beforeDestroy () {
    eventBus.$off('saveNewsInfo')
    eventBus.$off('editCancel')
  },
  watch: {
    'params.newsUrlAddr' (v) {
      if ('https://'.indexOf(v) >= 0 || 'http://'.indexOf(v) >= 0 || v.replace(/\s/gi, '') === '') {
        this.isUrlOk = false
      } else {
        this.isUrlOk = true
      }
    }
  },
  methods: {
    fileDelete () {
      this.$refs.selectedFile.value = ''
      this.uploadImageFile = null
      this.select = ''
      this.fileName = ''
      this.params.newsFileAddr = ''
    },
    editCancel () {
      let modalTitle = 'news 등록 취소'
      if (this.$route.query.newsNo !== '') {
        modalTitle = 'news 수정 취소'
      }

      this.$modal.show(

        ModalConfirm,
        {
          title: modalTitle,
          data: {
            url: '',
            refresh: 'editCancel'
          }
        },
        {
          width: 450,
          height: 'auto'
        }
      )
    },
    openDatePicker () {
      this.$refs.openDate.showCalendar()
    },
    newInfoSetting () {
      this.params = this.$route.params.newsInfo

      this.fromDate = new Date(this.params.displayStartDate)
      this.toDate = new Date(this.params.displayEndDate)

      this.fileName = this.params.newsFileAddr.substring(this.params.newsFileAddr.indexOf('_') + 1, this.params.newsFileAddr.length)
      this.select = 'selected'
    },
    validation () {
      let message = ''
      let flag = false
      if (this.params.newsTypeCode === '') {
        flag = true
        message = '분류 선택은 필수입니다.'
      } else if (this.params.newsTitle.replace(/\s/gi, '') === '') {
        flag = true
        message = '뉴스 제목은 필수입니다.'
      } else if (this.params.newsTitle.length < 2 && this.params.newsTitle.length > 45) {
        message = '뉴스 제목은 2자 이상 45자 이하여야 합니다.'
        flag = true
      } else if (this.fileName === '') {
        message = '이미지 파일 업로드는 필수입니다.'
        flag = true
      } else if (!this.isUrlOk) {
        message = '올바르지 않은 URL 입니다.'
        flag = true
      }

      if (flag) {
        this.$modal.show(ModalAlert,
          { title: '', text: message }, { width: 450, height: 'auto' }
        )
      }
      return flag
    },
    saveNewsInfoConfirm () {
      if (this.validation()) {
        return
      }

      this.$modal.show(
        ModalConfirm,
        {
          title: 'news 저장',
          data: {
            url: '',
            refresh: 'saveNewsInfo'
          }
        },
        {
          width: 450,
          height: 'auto'
        }
      )
    },
    sendNewInfo () {
      // eslint-disable-next-line no-undef
      this.params.displayStartDate = dateFormatToString(this.fromDate)
      if (this.toDate === null) {
      // eslint-disable-next-line no-undef
        this.params.displayEndDate = '9999-12-31'
      } else {
      // eslint-disable-next-line no-undef
        this.params.displayEndDate = dateFormatToString(this.toDate)
      }

      http
        .post('/newsInfo/saveNewsInfo', JSON.stringify(this.params))
        .then(response => {
          this.$modal.show(ModalAlert,
            { title: '', text: '뉴스 저장 성공하였습니다.' }, { width: 450, height: 'auto' }
          )
          this.$router.push({ name: 'newsList' })
        }).catch(e => {
          this.isloading = false
          this.$modal.show(ModalAlert,
            { title: '', text: '뉴스 저장에 실패하였습니다.' }, { width: 450, height: 'auto' }
          )
          console.log(e)
        })
    },
    saveNewsInfo () {
      this.isloading = true
      if (this.$refs.selectedFile.files[0] === undefined) {
        this.sendNewInfo()
      } else {
        let formData = new FormData()

        let file = this.$refs.selectedFile.files[0]
        formData.append('uploadfile', file)
        formData.append('fileKey', 'lunaadmin/newsInfo/' + file.lastModified + '_' + file.name)

        // eslint-disable-next-line no-undef
        const url = imageApiUrl + '/s3Upload/fileUpload'

        http
          .post(url, formData)
          .then(response => {
            this.params.newsFileAddr = response.data
            this.sendNewInfo()
          }).catch(e => {
            this.isloading = false
            this.$modal.show(ModalAlert,
              { title: '', text: '뉴스 저장에 실패하였습니다.' }, { width: 450, height: 'auto' }
            )
            console.log(e)
          })
      }
    },
    setfile (e) {
      var input = e.target
      if (input.files && input.files[0]) {
        let file = input.files[0]
        if (file.size > 20971520) {
          input.value = ''
          this.$modal.show(ModalAlert,
            { title: '', text: '파일 용량은 20MB를 넘을 수 없습니다.' }, { width: 450, height: 'auto' }
          )
        } else {
          var reader = new FileReader()
          reader.onload = (e) => {
            this.uploadImageFile = e.target.result
          }
          reader.readAsDataURL(input.files[0])
          this.select = 'selected'
          this.fileName = file.name
        }
      } else {
        this.uploadImageFile = null
        this.select = ''
        this.fileName = ''
      }
    },
    setFromDate (value) {
      this.fromDate = value
    },
    setToDate (value) {
      this.toDate = value
    },
    newsPreview () {
      let propUploadImageFile = ''
      if (this.uploadImageFile === null) {
        propUploadImageFile = this.params.newsFileAddr
      } else {
        propUploadImageFile = this.uploadImageFile
      }
      this.$modal.show(NewsPreviewModal,
        { newsUrlAddr: this.newsUrlAddr,
          uploadImageFile: propUploadImageFile,
          newsTypeCodeDesc: this.codeData.newsType[this.params.newsTypeCode],
          newsTitle: this.params.newsTitle },
        { width: 400, height: 'auto' }
      )
    }
  },
  props: {
    'codeData': Object
  }
}
</script>

<style>
  .datepickerWrap input[type="text"] {
    width: 100% !important
  }
</style>
