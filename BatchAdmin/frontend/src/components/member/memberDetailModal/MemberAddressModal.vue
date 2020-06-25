/* eslint-disable camelcase */
<template>
<div class="vue-modal-box" :width="900">
  <div class="windowPopupWrap">
    <section class="userDetailPopup">
      <div class="titleWrap">
        <h1>영업장 주소 정보 수정</h1>
      </div>
      <article>
        <div class="formContainer">
            <article>
              <div class="articleWrap">
                <div class="tableWrap tableList">
                  <table summary="영업장 주소 정보 수정">
                    <caption class="blind">영업장 주소 정보 수정</caption>
                    <colgroup>
                      <col style="width:60px">
                      <col style="width:150px">
                      <col>
                      <col style="width:350px">
                      <col style="width:100px">
                    </colgroup>
                    <thead>
                      <tr>
                        <th>구분</th>
                        <th>영업장</th>
                        <th>주소</th>
                        <th>전화번호</th>
                        <th>추가/삭제</th>
                      </tr>
                    </thead>
                    <tbody id="member_address_list">
                      <template v-for="(item, key) in address_list">
                        <tr :id="'member_address_'+ item.member_addr_no" :key="key">
                          <td class="textCenter">
                            <span class="inputWrap">
                              <template v-if="item.default_yn == '1'">
                                <input type="checkbox" name v-model="item.default_yn" true-value="1" false-value="0" :id="'address_star_'+ item.member_addr_no + key" checked >
                                <label :for="'address_star_'+ item.member_addr_no + key"></label>
                              </template>
                              <template v-else>
                                <input type="checkbox" name v-model="item.default_yn" true-value="1" false-value="0" :id="'address_star_'+ item.member_addr_no + key">
                                <label :for="'address_star_'+ item.member_addr_no + key"></label>
                              </template>
                            </span>
                          </td>
                          <td>
                            <input type="text" v-model="item.member_addr_name">
                          </td>
                          <td>
                            <p class="addressWrap">
                              <input type="text" name id v-model="item.member_addr" readonly>
                              <input class="btn_address" type="button" value="주소찾기" @click="() => openPostcode(key)">
                              <input type="text" name id v-model="item.member_detail_addr" readonly>
                              <postcode
                                :opened="isOpenedPostcode"
                                @close="() => isOpenedPostcode = false"
                                @complete="result => setAddress(result)"
                            />
                            </p>
                          </td>
                          <td>
                            <p class="phoneNumWrap">
                              <select v-model="item.member_addr_tel_no1">
                                <template v-for="(val,idx) in phoneList" >
                                  <option :value="idx" :key="val.id">{{val}}</option>
                                </template>
                              </select>
                              <span></span>
                              <input type="text" v-model="item.member_addr_tel_no2" maxlength="4" @input="callValidation($event,'number', key, 'member_addr_tel_no2')">
                              <span></span>
                              <input type="text" v-model="item.member_addr_tel_no3" maxlength="4" @input="callValidation($event,'number', key, 'member_addr_tel_no3')">
                            </p>
                          </td>
                          <td class="textCenter">
                            <div class="btnWrap">
                              <p class="btn_plus" @click="addMemberAddressRow(this)">
                                <i class="fas fa-plus"></i>
                              </p>
                              <p class="btn_minus" @click="removeRow(item.member_addr_no, item.default_yn, key)">
                                <i class="fas fa-minus"></i>
                              </p>
                            </div>
                          </td>
                        </tr>
                      </template>
                    </tbody>
                  </table>
                </div>
              </div>
            </article>
            <div class="btnWrap">
              <ul>
                <li>
                  <input class="btn_cancel" type="button" value="취소" @click="$emit('close')">
                </li>
                <li>
                  <input class="btn_save" type="button" value="적용" @click="modifyMemeberAddressInfo()">
                </li>
              </ul>
            </div>
        </div>
      </article>
    </section>
  </div>
  <Spinner v-if="isLoading" />
</div>
</template>

<script>
import http from '@/http-commons'
import { eventBus } from '../../../main'
import Postcode from '@/components/common/Postcode'
import ModalAlert from '@/components/common/modal/ModalAlert'

export default {
  name: 'MemberAddressModal',
  data () {
    return {
      isLoading: false,
      address_list: [],
      form_list: [],
      memberNo: 0,
      isOpenedPostcode: false,
      currentPostOpendKey: 0,
      phoneList: ''
    }
  },
  components: {
    Postcode,
    ModalAlert
  },
  mounted () {
    if (this.address_list.length === 0) {
      this.addMemberAddressRow()
    }
  },
  created () {
    this.memberNo = this.$route.params.member_no
    var propsDataParse = JSON.parse(JSON.stringify(this.member_address_list))

    for (var i in propsDataParse) {
      var item = propsDataParse[i]
      this.address_list.push(item)
    }

    // eslint-disable-next-line no-undef
    this.phoneList = getPhoneList()
  },
  watch: {
    address_list: {
      deep: true,
      handler: function (newVal, oldVal) {
        // eslint-disable-next-line no-unused-vars
        var setChangedAddressObject = newVal.filter(function (p, idx) {
          Object.keys(p).some(function (prop) {
            if (prop === 'member_addr_tel_no1' || prop === 'member_addr_tel_no2' || prop === 'member_addr_tel_no3') {
              newVal[idx].member_addr_tel_no = newVal[idx].member_addr_tel_no1 + '-' + newVal[idx].member_addr_tel_no2 + '-' + newVal[idx].member_addr_tel_no3
            }
          })
        }) // end of filter
      }
    }
  },
  methods: {
    addMemberAddressRow () {
      this.address_list.push({
        default_yn: 0,
        del_yn: 0,
        member_addr: '',
        member_addr_name: '',
        member_addr_no: 0,
        member_addr_tel_no: '',
        member_addr_tel_no1: '',
        member_addr_tel_no2: '',
        member_addr_tel_no3: '',
        member_detail_addr: '',
        member_no: this.memberNo,
        member_zipcode_no: ''
      })
    },
    removeRow (thisTagId, defaultYn, key) {
      let rowId = 'member_address_' + thisTagId

      if (defaultYn === 1) {
        alert('즐겨찾기 주소지는 삭제할 수없습니다.')
      } else {
        this.address_list[key].del_yn = 1
        var thisNode = document.getElementById(rowId)
        thisNode.parentNode.removeChild(thisNode)
      }
    },
    callValidation (event, type, key, tagName) {
      // eslint-disable-next-line no-undef
      switch (type) {
        case 'number' :
          // eslint-disable-next-line no-undef
          if (!numberValidation(event.target.value)) {
            switch (tagName) {
              case 'member_addr_tel_no2' :
                this.address_list[key].member_addr_tel_no2 = ''
                break
              case 'member_addr_tel_no3' :
                this.address_list[key].member_addr_tel_no3 = ''
                break
            }
          }
          break
      }
    },
    validationCheck (requestFormData) {
      let alertText = ''
      for (var pair of requestFormData.entries()) {
        for (const [key, value] of Object.entries(pair[1])) {
          if (value === '' && (key !== 'member_detail_addr' && key !== 'member_addr_tel_no')) {
            switch (key) {
              case 'member_addr_name':
                alertText = '영업장 주소 별칭을 입력하세요'
                break
              case 'member_addr':
                alertText = '영업장 주소지를 선택하세요'
                break
              case 'member_addr_tel_no1':
                alertText = '주소지 연락처 앞자리를 선택하세요'
                break
              case 'member_addr_tel_no2':
                alertText = '주소지 연락처를 입력하세요'
                break
              case 'member_addr_tel_no3':
                alertText = '주소지 연락처 끝 자리를 입력하세요'
                break
            }
            this.$modal.show(ModalAlert,
              {title: '영업장 정보 수정 : ' + key, text: alertText},
              { width: 450, height: 'auto' }
            )
            return false
          }

          if (key === 'member_addr_tel_no') {
            if (value.length < 11) {
              alertText = '올바르지 않은 연락처 입니다.'
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
    modifyMemeberAddressInfo () {
      if (!this.validationCheck(this.address_list)) {
        return
      }

      this.isLoading = true

      http
        .post('/member/address/' + this.memberNo, JSON.stringify(this.address_list))
        .then(response => {
          this.isLoading = false
          var responseCode = response.data.responseCode
          if (responseCode === 0) {
            this.$modal.show(ModalAlert,
              {title: '영업장 정보 수정', text: '영업장 정보가 수정되었습니다.'},
              { width: 450, height: 'auto' }
            )
            this.resultEditMemberAddressListToParent(this.address_list)
            this.$emit('close')
          } else {
            alert('영업장 정보 수정 중 오류가 발생하였습니다. \n 다시 시도해 주세요. \n errormsg : ' + response.data.responseMsg)
          }
        })
    },
    openPostcode (key) {
      this.isOpenedPostcode = true
      this.currentPostOpendKey = key
    },
    setAddress (result) { /** 주소 팝업완료시 주소데이터 set */
      this.address_list[this.currentPostOpendKey].member_addr = result.roadAddress
      this.address_list[this.currentPostOpendKey].member_detail_addr = result.buildingName
      this.address_list[this.currentPostOpendKey].member_zipcode_no = result.zonecode
    },
    resultEditMemberAddressListToParent (result) {
      eventBus.$emit('refreshMembmerDetailInfo', result, 'address')
    }
  },
  props: ['member_address_list']
}
</script>
