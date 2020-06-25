/* eslint-disable no-undef */
/* eslint-disable camelcase */
<template>
  <div class="windowPopupWrap">
    <main>
      <section class="userDetailPopup2">
        <div class="titleWrap">
          <h1>사업자 정보</h1>
        </div>
        <article>
          <div class="formContainer">
            <form action="#" method="post" id="member_biz_info_form">
              <article>
                <div class="articleWrap">
                  <div class="tableWrap tableRegister">
                    <table summary="기본 정보">
                      <caption class="blind">기본 정보</caption>
                      <colgroup>
                        <col style="width:150px">
                        <col>
                      </colgroup>
                      <template v-text="memberInfo">
                        <tbody>
                          <tr>
                            <th>사업자등록번호<span class="necessary">*</span></th>
                            <td>
                              <p class="businessNumWrap">
                                <input type="text" v-model.trim="memberInfo.member_biz_no1" maxlength="3" @input="callValidation($event,'number',3, 'member_biz_no1')">
                                <span></span>
                                <input type="text" v-model.trim="memberInfo.member_biz_no2" maxlength="2" @input="callValidation($event,'number',2, 'member_biz_no2')">
                                <span></span>
                                <input type="text" v-model.trim="memberInfo.member_biz_no3" maxlength="5" @input="callValidation($event,'number',5, 'member_biz_no3')">
                              </p>
                              <p v-if="multiBizNoFlag">{{multiBizNoMsg}}</p>
                              <p class="duplicateCheckWrap">
                                <input type="button" value="변경하기" class="btn_check" @click="bizNoDuplicateCheck()">
                              </p>
                              <span class="inputWrap">
                                <input type="checkbox" v-model.trim="memberInfo.member_join_biz_no_duplicate_yn" true-value="1" false-value="0" id="member_join_biz_no_duplicate_yn">
                                <label for="member_join_biz_no_duplicate_yn">중복 허용</label>
                              </span>
                            </td>
                          </tr>

                          <tr>
                            <th>사업자 등록증<span class="necessary">*</span></th>
                            <td>
                              <div class="fileWrap">
                                <span class="textWrap">
                                  <i class="far fa-file"></i>
                                  <input type="text" readonly="readonly" v-model.trim="memberLicenseImgFileName">
                                  <a href="#"><i class="fas fa-times"></i></a>
                                </span>
                                <input id="member_biz_license_img" type="file" @change="setMemberLicenseImgFileName">
                                <label for="member_biz_license_img">파일선택</label>
                              </div>
                            </td>
                          </tr>

                          <tr>
                            <th>법인명<span class="necessary">*</span></th>
                            <td>
                              <input type="text" placeholder="법인명을 입력하세요" v-model.trim="memberInfo.member_biz_name" minlength="2" maxlength="50">
                            </td>
                          </tr>

                          <tr>
                            <th>상호명</th>
                            <td v-if="memberInfo.shop_name == ''">
                              <input type="text" disabled="disabled" v-model.trim="memberInfo.shop_name">
                            </td>
                            <td v-else>
                              등록된 상호가 없습니다.
                            </td>
                          </tr>

                          <tr>
                            <th>대표자명<span class="necessary">*</span></th>
                            <td>
                              <input type="text" placeholder="대표자명을 입력하세요" v-model.trim="memberInfo.member_ceo_name">
                            </td>
                          </tr>

                        </tbody>
                      </template>
                    </table>
                  </div>
                </div>
              </article>
              <div class="btnWrap">
                <ul>
                  <li><input class="btn_cancel" type="button" value="취소" @click="$emit('close')"></li>
                  <li><input class="btn_save" type="button" value="적용" @click="modifyMemberBizInfo()"></li>
                </ul>
              </div>
            </form>
          </div>
        </article>
      </section>
    </main>
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import axios from 'axios'
import http from '@/http-commons'
import ModalAlert from '@/components/common/modal/ModalAlert'
import { eventBus } from '../../../main'
export default {

  data () {
    return {
      isLoading: false,
      lunaMaintainList: this.codeData.managerMaintainLunaNoData,
      lunaSalesList: this.codeData.managerSaleslunaNoData,
      inflowPath: this.codeData.inventoryCompanyCodeData,
      memberInfo: '',
      memberNo: 0,
      memberLicenseImgFile: '',
      memberLicenseImgFileName: '등록된 파일이 없습니다.',
      member_biz_no: 0,
      editableFlag: false,
      multiBizNoFlag: false,
      checkRequestBizNo: '',
      multiBizNoMsg: '동일한 사업자 등록번호로 생성된 회원 계정이 있습니다.\n'
    }
  },
  created () {
    this.memberNo = this.$route.params.member_no
    this.memberInfo = JSON.parse(JSON.stringify(this.member))
  },
  watch: {
    memberInfo: {
      deep: true,
      handler: function (newVal) {
        if (newVal.member_biz_no1) {
          this.memberInfo.member_biz_no = newVal.member_biz_no1.trim() + '-' + newVal.member_biz_no2 + '-' + newVal.member_biz_no3
        }
        if (newVal.member_biz_no2) {
          this.memberInfo.member_biz_no = newVal.member_biz_no1 + '-' + newVal.member_biz_no2.trim() + '-' + newVal.member_biz_no3
        }
        if (newVal.member_biz_no3) {
          this.memberInfo.member_biz_no = newVal.member_biz_no1 + '-' + newVal.member_biz_no2 + '-' + newVal.member_biz_no3.trim()
        }
      }
    }
  },
  methods: {
    bizNoDuplicateCheck () {
      if (this.memberInfo.member_biz_no === this.member.member_biz_no) {
        this.checkRequestBizNo = this.memberInfo.member_biz_no
        this.$modal.show(ModalAlert,
          {title: '사업자 정보 수정', text: '사업자 등록 번호가 확인되었습니다.'},
          { width: 450, height: 'auto' }
        )
        this.editableFlag = true
        return
      }

      this.checkRequestBizNo = this.memberInfo.member_biz_no
      if (this.checkRequestBizNo.length !== 12) {
        this.$modal.show(ModalAlert,
          {title: '사업자 정보 수정', text: '올바른 사업자 등록 번호가 아닙니다.'},
          { width: 450, height: 'auto' }
        )
        return
      }
      this.isLoading = true
      http
        .get('/member/bizNoDuplicateCheck', {
          params: { // query string
            member_biz_no: this.checkRequestBizNo
          }
        })
        .then(response => {
          this.isLoading = false
          // console.log('bizNoDuplicateCheck result : ' + JSON.stringify(response))
          if (response.data.responseCode === 0) {
            let count = response.data.data.count
            let duplicateYn = response.data.data.duplicate_yn
            // console.log('count : ' + count + ' || duplicate_yn : ' + duplicateYn)

            if (count > 0) {
              this.multiBizNoFlag = true
            }

            if (duplicateYn !== 0) {
              this.editableFlag = true
              // this.memberInfo.member_join_biz_no_duplicate_yn = 1
              this.$modal.show(ModalAlert,
                {title: '사업자 정보 수정', text: '사업자 등록 번호가 확인되었습니다.'},
                { width: 450, height: 'auto' }
              )
            } else {
              this.memberInfo.member_join_biz_no_duplicate_yn = 0
              this.$modal.show(ModalAlert,
                {title: '사업자 정보 수정', text: '이미 사용중인 사업자 번호입니다. 중복허용 설정 변경 후 등록 가능합니다. 고객센터로 문의하시기 바랍니다.'},
                { width: 450, height: 'auto' }
              )
            }
          } else {
            this.errorPopup(response.data.responseMsg)
          }
        })
        .catch(e => {
          console.log(e)
          this.errorPopup()
        })
    },
    setMemberLicenseImgFileName () {
      this.memberLicenseImgFile = document.getElementById('member_biz_license_img').files[0]
      this.memberLicenseImgFileName = document.getElementById('member_biz_license_img').value
    },
    callValidation (event, type, length, tagName) {
      // eslint-disable-next-line no-undef
      console.log('current function1 : ' + event.target.value)
      let val = event.target.value.trim()
      switch (type) {
        case 'number' :
          // eslint-disable-next-line no-undef
          if (!numberValidation(val) && val !== '') {
            console.log('not valid value')
            switch (tagName) {
              case 'member_biz_no1' :
                this.memberInfo.member_biz_no1 = event.target._value
                break
              case 'member_biz_no2' :
                this.memberInfo.member_biz_no2 = event.target._value
                break
              case 'member_biz_no3' :
                this.memberInfo.member_biz_no3 = event.target._value
                break
            }
          }
          break
      }
    },
    modifyMemberBizInfo () {
      let memberPointRequest = new FormData()
      memberPointRequest.append('member_no', this.memberNo)
      memberPointRequest.append('license_img', this.memberLicenseImgFile)
      memberPointRequest.append('file_image_biz_card_name', this.memberLicenseImgFileName)
      memberPointRequest.append('member_join_biz_no_duplicate_yn', this.memberInfo.member_join_biz_no_duplicate_yn)
      memberPointRequest.append('member_biz_no', this.memberInfo.member_biz_no)
      memberPointRequest.append('member_biz_name', this.memberInfo.member_biz_name)
      memberPointRequest.append('member_ceo_name', this.memberInfo.member_ceo_name)

      if (!this.validationCheck(memberPointRequest)) {
        return
      }

      axios
        .post('/member/point/' + this.memberNo, memberPointRequest,
          {
            // baseURL: 'http://localhost:8080/admin',
            // baseURL: 'http://localhost:8080',
            headers: {'Content-Type': 'multipart/form-data'}
          })
        .then(response => {
          console.log(JSON.stringify(response))
          if (response.data.responseCode === 0) {
            this.$modal.show(ModalAlert,
              {title: '사업자 정보 수정', text: '사업자 정보가 수정되었습니다.'},
              { width: 450, height: 'auto' }
            )

            console.log('after modify object : ' + JSON.stringify(this.memberInfo))

            this.resultDataToParent(this.memberInfo)
            this.$emit('close')
          } else {
            alert('회원 대표 정보 수정 중 오류가 발생하였습니다. \n 다시 시도해 주세요. \n errormsg : ' + response.data.responseMsg)
          }
        })
    },
    validationCheck (requestFormData) {
      let alertText = ''

      if (this.member_biz_no.length < 12) {
        this.editableFlag = false
        alertText = '사업자 등록번호 13자리를 입력하세요'
        this.$modal.show(ModalAlert,
          {title: '사업자 정보 수정', text: alertText},
          { width: 450, height: 'auto' }
        )
        return
      }
      console.log(this.memberInfo.member_biz_no + ' || ' + this.checkRequestBizNo)
      if (this.memberInfo.member_biz_no !== this.checkRequestBizNo) {
        this.editableFlag = false
        alertText = '입력한 사업자 등록번호[' + this.memberInfo.member_biz_no + '] 의 중복여부를 확인하세요'
        this.$modal.show(ModalAlert,
          {title: '사업자 정보 수정', text: alertText},
          { width: 450, height: 'auto' }
        )
        return
      }

      if (this.memberLicenseImgFile === '') {
        alertText = '사업자 등록증을 선택하세요'
        this.$modal.show(ModalAlert,
          {title: '사업자 정보 수정', text: alertText},
          { width: 450, height: 'auto' }
        )
        return
      }

      // eslint-disable-next-line no-undef
      if (!checkImageType(this.memberLicenseImgFileName)) {
        alertText = 'jpg, gif, png, jpeg/i 확장자 이미지만 등록 가능합니다.'
        this.$modal.show(ModalAlert,
          {title: '사업자 정보 수정', text: alertText},
          { width: 450, height: 'auto' }
        )
        return
      }

      console.log('===== validation check requestFormData ===== ')
      for (var pair of requestFormData.entries()) {
        if (pair[1] === '' || pair[1] === 'undefined') {
          switch (pair[0]) {
            case 'file_image_biz_card_name':
              alertText = '사업자 등록증을 선택하세요'
              break
            case 'member_biz_no':
              alertText = '사업자 등록 번호를 입력하세요'
              break
            case 'member_biz_name':
              alertText = '법인명을 입력하세요'
              break
            case 'member_ceo_name':
              alertText = '대표자명을 입력하세요'
              break
          }

          this.$modal.show(ModalAlert,
            {title: '사업자 정보 수정', text: alertText},
            { width: 450, height: 'auto' }
          )
          return false
        }
      }
      return true
    },
    resultDataToParent (result) {
      eventBus.$emit('resetMemberInfoInfo', result)
    },
    errorPopup (errorMsg) {
      this.$modal.show(ModalAlert,
        {title: '사업자 정보 수정', text: errorMsg},
        { width: 450, height: 'auto' }
      )
    }
  },
  props: [
    'codeData',
    'member'
  ]
}
</script>
