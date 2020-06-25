<template>
<div class="v--modal-box v--modal">
  <div class="windowPopupWrap">
    <main>
      <section class="userDetailPopup">
        <div class="titleWrap">
          <h1>담당자 정보 수정</h1>
        </div>
        <article>
          <div class="formContainer">
            <form id="member_manager_edit_form" action="#" method="post">
              <article>
                <div class="articleWrap">
                  <div class="tableWrap tableList">
                    <table summary="담당자 정보 수정 ">
                      <caption class="blind">담당자 정보 수정 </caption>
                      <colgroup>
                        <col style="width:60px">
                        <col style="width:150px">
                        <col>
                        <col style="width:180px">
                        <col style="width:100px">
                      </colgroup>
                      <thead>
                        <tr>
                          <th>구분</th>
                          <th>담당자명</th>
                          <th>연락처/이메일주소</th>
                          <th>서비스</th>
                          <th>추가/삭제</th>
                        </tr>
                      </thead>
                      <tbody id="member_manager_list_area">
                        <!-- data print area -->
                        <template v-for="(item, key) in manager_list">
                          <tr :id="'member_manager_'+ item.member_manager_no" :key="key">
                            <td class="textCenter">
                              <span class="inputWrap">
                                <template v-if="item.default_yn == '1'">
                                  <input type="checkbox" v-model="item.default_yn" true-value="1" false-value="0" :id="'manager_star_'+ item.member_manager_no" checked>
                                  <label :for="'manager_star_'+ item.member_manager_no"></label>
                                </template>
                                <template v-else="">
                                  <input type="checkbox" v-model="item.default_yn" true-value="1" false-value="0" :id="'manager_star_'+ item.member_manager_no">
                                  <label :for="'manager_star_'+ item.member_manager_no"></label>
                                </template>
                              </span>
                            </td>
                            <td>
                              <input type="text" name="member_manager_name" maxlength="50" v-model="item.member_manager_name">
                            </td>
                            <td>
                              <p class="phoneNumWrap">
                                <select v-model="item.member_manager_tel_no1">
                                  <template v-for="(val,idx) in phoneList" >
                                    <option :value="idx" :key="val.id">{{val}}</option>
                                  </template>
                                </select>
                                <span></span>
                                <input type="text" v-model="item.member_manager_tel_no2" maxlength="4" @input="callValidation($event,'number', key, 'member_manager_tel_no2')">
                                <span></span>
                                <input type="text" v-model="item.member_manager_tel_no3" maxlength="4" @input="callValidation($event,'number', key, 'member_manager_tel_no3')">
                              </p>
                              <p class="emailWriteWrap">
                                <input type="text" name="member_manager_email_addr_id" v-model="item.member_manager_email_addr_id" @input="callValidation($event,'emailId', key, 'member_manager_email_addr_id')">
                                <span>@</span>
                                <input type="text" name="member_manager_email_addr_id" v-model="item.member_manager_email_addr_host" @input="callValidation($event,'emailHost', key, 'member_manager_email_addr_host')">
                                <select v-model="item.member_manager_email_addr_host">
                                  <template v-for="(val,idx) in emailList" >
                                    <option :value="idx" :key="val.id">{{val}}</option>
                                  </template>
                                </select>
                              </p>
                            </td>
                            <td>
                              <input type="text" placeholder="" name="etc_text" id="" v-model="item.etc_text">
                            </td>
                            <td class="textCenter">
                              <div class="btnWrap">
                                <p class="btn_plus" @click="addManagerRow(this)">
                                  <i class="fas fa-plus"></i>
                                </p>
                                <p class="btn_minus" @click="removeRow(item.member_manager_no, item.default_yn, key)">
                                  <i class="fas fa-minus"></i>
                                </p>
                              </div>
                            </td>
                          </tr>
                        </template>
                        <!-- data print area end -->
                      </tbody>
                    </table>
                  </div>
                </div>
              </article>
              <div class="btnWrap">
                <ul>
                  <li><input class="btn_cancel" type="button" value="취소" @click="$emit('close')"></li>
                  <li><input class="btn_save" type="button" value="적용" @click="editManagerInfoRequest()"></li>
                </ul>
              </div>
            </form>
          </div>
        </article>
      </section>
    </main>
  </div>
<Spinner v-if="isLoading" />
</div>
</template>

<script>
import http from '@/http-commons'
import { eventBus } from '../../../main'
import ModalAlert from '@/components/common/modal/ModalAlert'

export default {
  name: 'MemberManagerEditModal',
  data () {
    return {
      isLoading: false,
      manager_list: [],
      memberNo: 0,
      selectedMail: '',
      emailList: '',
      phoneList: ''
    }
  },
  mounted () {
    if (this.manager_list.length === 0) {
      this.addManagerRow()
    }
  },
  created () {
    this.memberNo = this.$route.params.member_no
    var propsDataParse = JSON.parse(JSON.stringify(this.member_manager_list))

    for (var i in propsDataParse) {
      var item = propsDataParse[i]
      this.manager_list.push(item)
    }

    // eslint-disable-next-line no-undef
    this.emailList = getEmailList()
    // eslint-disable-next-line no-undef
    this.phoneList = getPhoneList()
  },
  watch: {
    manager_list: {
      deep: true,
      handler: function (newVal, oldVal) {
        // eslint-disable-next-line no-unused-vars
        var setChangedMangerObject = newVal.filter(function (p, idx) {
          Object.keys(p).some(function (prop) {
            if (prop === 'member_manager_tel_no1' || prop === 'member_manager_tel_no2' || prop === 'member_manager_tel_no3') {
              newVal[idx].member_manager_tel_no = newVal[idx].member_manager_tel_no1 + '-' + newVal[idx].member_manager_tel_no2 + '-' + newVal[idx].member_manager_tel_no3
            }

            if (prop === 'member_manager_email_addr_id' || prop === 'member_manager_email_addr_host') {
              newVal[idx].member_manager_email_addr = newVal[idx].member_manager_email_addr_id + '@' + newVal[idx].member_manager_email_addr_host
            }
          })
        }) // end of filter
      }
    }
  },
  methods: {
    addManagerRow () {
      this.manager_list.push({
        default_yn: 0,
        del_yn: 0,
        etc_text: '',
        member_manager_email_addr: '',
        member_manager_email_addr_host: '',
        member_manager_email_addr_id: '',
        member_manager_name: '',
        member_manager_no: 0,
        member_manager_tel_no: '',
        member_manager_tel_no1: '',
        member_manager_tel_no2: '',
        member_manager_tel_no3: '',
        member_no: this.memberNo
      })
    },
    removeRow (thisTagId, defaultYn, key) {
      let rowId = 'member_manager_' + thisTagId

      if (defaultYn === 1) {
        alert('즐겨찾기 주소지는 삭제할 수없습니다.')
      } else {
        this.manager_list[key].del_yn = 1
        var thisNode = document.getElementById(rowId)
        thisNode.parentNode.removeChild(thisNode)
      }
    },
    callValidation (event, type, key, tagName) {
      switch (type) {
        case 'number' :
          // eslint-disable-next-line no-undef
          if (!numberValidation(event.target.value)) {
            switch (tagName) {
              case 'member_manager_tel_no2' :
                this.manager_list[key].member_manager_tel_no2 = ''
                break
              case 'member_manager_tel_no3' :
                this.manager_list[key].member_manager_tel_no3 = ''
                break
            }
          }
          break
      }
    },
    editManagerInfoRequest () {
      if (!this.validationCheck(this.manager_list)) {
        return
      }

      console.log('request memberNo : ' + JSON.stringify(this.manager_list))
      this.isLoading = true
      http
        .post('/member/manager/' + this.memberNo, JSON.stringify(this.manager_list))
        .then(response => {
          this.isLoading = false
          if (response.data.responseCode === 0) {
            this.$modal.show(ModalAlert,
              {title: '담당자 정보 수정', text: '담장자 정보가 수정되었습니다.'},
              { width: 450, height: 'auto' }
            )
            this.resultEditMemberManagerListToParent(this.manager_list)
            this.$emit('close')
          } else {
            alert('담당자 정보 수정 중 오류가 발생하였습니다. \n 다시 시도해 주세요. \n errormsg : ' + response.data.responseMsg)
          }
        })
    },
    validationCheck (requestFormData) {
      let alertText = ''

      for (var pair of requestFormData.entries()) {
        for (const [key, value] of Object.entries(pair[1])) {
          if (value === '' && key !== 'member_manager_tel_no') {
            switch (key) {
              case 'member_manager_name':
                alertText = '담당자 이름을 입력하세요'
                break
              case 'member_manager_tel_no1':
                alertText = '담당자 연락처를 앞자리를 선택하세요'
                break
              case 'member_manager_tel_no2':
                alertText = '담당자 연락처를 입력하세요'
                break
              case 'member_manager_tel_no3':
                alertText = '담당자 연락처 끝 자리를 입력하세요'
                break
              case 'member_manager_email_addr_id':
                alertText = '담당자 이메일 앞자리를 입력하세요'
                break
              case 'member_manager_email_addr_host':
                alertText = '담당자 이메일 주소를 선택하세요'
                break
              case 'etc_text':
                alertText = '담당 서비스를 입력하세요'
                break
            }
            this.$modal.show(ModalAlert,
              {title: '담당자 정보 수정' + key, text: alertText},
              { width: 450, height: 'auto' }
            )
            return false
          }

          if (key === 'member_manager_tel_no') {
            if (value.length < 11) {
              alertText = '올바르지 않은 연락처 입니다.'
              this.$modal.show(ModalAlert,
                {title: '담당자 정보 수정', text: alertText},
                { width: 450, height: 'auto' }
              )
              return false
            }
          }

          if (key === 'member_manager_email_addr_id') {
            // eslint-disable-next-line no-undef
            if (!frontEmailValid(value)) {
              alertText = '올바르지 않은 이메일 형식입니다.'
              this.manager_list[pair[0]].member_manager_email_addr_id = ''
              this.$modal.show(ModalAlert,
                {title: '담당자 정보 수정', text: alertText},
                { width: 450, height: 'auto' }
              )
              return false
            }
          }

          if (key === 'member_manager_email_addr_host') {
            // eslint-disable-next-line no-undef
            if (!backEmailValid(value)) {
              alertText = '올바르지 않은 이메일 형식입니다.'
              this.manager_list[pair[0]].member_manager_email_addr_host = ''
              this.$modal.show(ModalAlert,
                {title: '담당자 정보 수정', text: alertText},
                { width: 450, height: 'auto' }
              )
              return false
            }
          }
        }
      }
      return true
    },
    resultEditMemberManagerListToParent (result) {
      eventBus.$emit('refreshMembmerDetailInfo', result, 'manager')
    }
  },
  props: [
    'member_manager_list'
  ]

}
</script>
