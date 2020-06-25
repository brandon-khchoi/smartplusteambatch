<template>
    <div class="windowPopupWrap">
    <main>
      <section class="userDetailPopup">
        <div class="titleWrap">
          <h1>회원 세금계산서 정보 수정</h1>
        </div>
        <article>
          <div class="formContainer">
            <form action="#" method="post">
              <article>
                <div class="articleWrap">
                  <div class="tableWrap tableList">
                    <table summary="영업장 주소 정보 수정">
                      <caption class="blind">회원 세금계산서 정보 수정</caption>
                      <colgroup>
                        <col>
                        <col>
                        <col>
                        <col>
                      </colgroup>
                      <thead>
                        <tr>
                          <th>수신자</th>
                          <th colspan="2">이메일</th>
                          <th>추가/삭제</th>
                        </tr>
                      </thead>
                      <tbody>
                        <template v-for="(item, key) in bill_list">
                            <tr :id="'member_bill_' + item.member_bill_no" :key="key">
                                <td>
                                    <input type="text" placeholder="" name="" id="" v-model="item.member_bill_name">
                                </td>
                                <td colspan="2">
                                    <p class="emailWriteWrap">
                                        <input type="text" placeholder="" name="" id="" v-model="item.member_bill_email_addr_id">
                                        <span>@</span>
                                        <input type="text" placeholder="" name="" id="" v-model="item.member_bill_email_addr_host">
                                       <select v-model="item.member_bill_email_addr_host">
                                        <template v-for="(val,idx) in emailList" >
                                          <option :value="idx" :key="val.id">{{val}}</option>
                                        </template>
                                      </select>
                                    </p>
                                </td>
                                <td class="textCenter">
                                    <div class="btnWrap">
                                        <p class="btn_plus" @click="addBillRow(this)">
                                          <i class="fas fa-plus"></i>
                                        </p>
                                        <p class="btn_minus" @click="removeRow(item.member_bill_no, key)">
                                          <i class="fas fa-minus" ></i>
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
                  <li><input class="btn_cancel" type="button" value="취소" @click="$emit('close')"></li>
                  <li><input class="btn_save" type="button" value="적용" @click="modifyMemberBillList"></li>
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
import http from '@/http-commons'
import { eventBus } from '../../../main'
import ModalAlert from '@/components/common/modal/ModalAlert'

export default {
  name: 'MemberBillModal',
  data () {
    return {
      isLoading: false,
      emailList: '',
      bill_list: [],
      memberNo: 0
    }
  },
  mounted () {
    if (this.bill_list.length === 0) {
      this.addBillRow()
    }
  },
  created () {
    this.memberNo = this.$route.params.member_no
    var propsDataParse = JSON.parse(JSON.stringify(this.member_bill_list))

    for (var i in propsDataParse) {
      var item = propsDataParse[i]
      this.bill_list.push(item)
    }
    // eslint-disable-next-line no-undef
    this.emailList = getEmailList()
  },
  watch: {
    bill_list: {
      deep: true,
      handler: function (newVal, oldVal) {
        // eslint-disable-next-line no-unused-vars
        var setChangedBillObject = newVal.filter(function (object, idx) {
          Object.keys(object).some(function (prop) {
            if (prop === 'member_bill_email_addr_id' || prop === 'member_bill_email_addr_host') {
              newVal[idx].member_bill_email_addr = newVal[idx].member_bill_email_addr_id + '@' + newVal[idx].member_bill_email_addr_host
            }
          })
        }) // end of filter
      }
    }
  },
  methods: {
    close () {
      this.$emit('close')
    },
    removeRow (thisTagId, key) {
      let rowId = 'member_bill_' + thisTagId
      this.bill_list[key].del_yn = 1
      var thisNode = document.getElementById(rowId)
      thisNode.parentNode.removeChild(thisNode)
    },
    addBillRow () {
      this.bill_list.push({
        del_yn: 0,
        member_bill_email_addr: '',
        member_bill_name: '',
        member_bill_no: 0,
        member_no: this.memberNo,
        modify_user_code: 0,
        modify_user_no: 0
      })
    },
    validationCheck (requestFormData) {
      let alertText = ''

      for (var pair of requestFormData.entries()) {
        for (const [key, value] of Object.entries(pair[1])) {
          if (value === '') {
            switch (key) {
              case 'member_bill_name':
                alertText = '세금계산서 수신자 정보를 입력하세요'
                break
              case 'member_bill_email_addr_id':
                alertText = '세금 계산서 이메일 앞 자리를 입력하세요'
                break
              case 'member_bill_email_addr_host':
                alertText = '세금 계산서 이메일 뒷 자리를 입력하세요'
                break
            }
            this.$modal.show(ModalAlert,
              {title: '세금계산서 정보 수정', text: alertText},
              { width: 450, height: 'auto' }
            )
            return false
          }

          if (key === 'member_bill_email_addr_host') {
            // eslint-disable-next-line no-undef
            if (!backEmailValid(value)) {
              alertText = '올바르지 않은 이메일 형식입니다.'
              this.bill_list[pair[0]].member_bill_email_addr_host = ''
              this.$modal.show(ModalAlert,
                {title: '세금계산서 정보 수정', text: alertText},
                { width: 450, height: 'auto' }
              )
              return false
            }
          }
        }
      }
      return true
    },
    modifyMemberBillList () {
      if (!this.validationCheck(this.bill_list)) {
        return
      }

      if (JSON.stringify(this.bill_list) === JSON.stringify(this.member_bill_list)) {
        this.$modal.show(ModalAlert,
          {title: '세금 계산서 정보 수정', text: '변경 사항이 없습니다.'},
          { width: 450, height: 'auto' }
        )
        this.$emit('close')
        return
      }

      this.isLoading = true
      http
        .post('/member/bill/' + this.memberNo, JSON.stringify(this.bill_list))
        .then(response => {
          this.isLoading = false
          var responseCode = response.data.responseCode
          if (responseCode === 0) {
            this.$modal.show(ModalAlert,
              {title: '세금 계산서 정보 수정', text: '세금 계산서 정보가 수정되었습니다.'},
              { width: 450, height: 'auto' }
            )
            this.resultEditMemberBillListToParent(this.bill_list)
            this.$emit('close')
          } else {
            alert('세금계산서 수정 중 오류가 발생하였습니다. \n 다시 시도해 주세요. \n errormsg : ' + response.data.responseMsg)
          }
        })
    },
    resultEditMemberBillListToParent (result) {
      eventBus.$emit('refreshMembmerDetailInfo', result, 'bill')
    }
  },
  props: [
    'member_bill_list'
  ]

}
</script>
