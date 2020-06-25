<template>
    <div class="windowPopupWrap">
    <main>
      <section class="userDetailPopup">
        <div class="titleWrap">
          <h1>루나소프트 담당자 정보</h1>
        </div>
        <article>
          <div class="formContainer">
            <form action="#" method="post">
              <article>
                <div class="articleWrap">
                  <div class="tableWrap tableList">
                    <table summary="영업장 주소 정보 수정">
                      <caption class="blind">루나소프트 담당자 정보 수정</caption>
                      <colgroup>
                        <col>
                        <col>
                        <col>
                        <col>
                      </colgroup>
                      <thead>
                        <tr>
                          <th>영업담당</th>
                          <th>운영담당</th>
                          <th>유입경로</th>
                          <th>리셀러</th>
                        </tr>
                      </thead>
                      <tbody>
                        <template v-text="inchargeInfo">
                            <tr>
                                <td>
                                    <!-- <select v-model="inchargeInfo.manager_sales_luna_no"> -->
                                    <select v-model="inchargeInfo.manager_sales_luna_no">
                                        <template v-for="(val,idx) in lunaSalesList" >
                                            <option :value="idx" :key="val.id">{{val}}</option>
                                        </template>
                                    </select>
                                </td>
                                <td>
                                    <select v-model="inchargeInfo.manager_maintain_luna_no">
                                        <template v-for="(val,idx) in this.lunaMaintainList" >
                                            <option :value="idx" :key="val.id">{{val}}</option>
                                        </template>
                                    </select>
                                </td>
                                <td class="textCenter">
                                    <select>
                                        <option selected>이지어드민</option>
                                        <template v-for="(val,idx) in this.inflowPath" >
                                            <option :value="idx" :key="val.id">{{val}}</option>
                                        </template>
                                    </select>
                                </td>
                                <td class="textCenter">
                                    <input type="text" name="" id="" value="-" disabled="disabled" style="text-align: center;">
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
                  <li><input class="btn_save" type="button" value="적용" @click="modifyMemberInchargeInfo"></li>
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
  name: 'MemberInchargeModal',
  data () {
    return {
      isLoading: false,
      maintain_luna_seleted: '',
      maintain_luna_name: this.member_incharge_info.manager_maintain_luna_name,
      sales_luna_seleted: '',
      sales_luna_name: this.member_incharge_info.manager_sales_luna_name,
      inchargeInfo: '',
      lunaMaintainList: this.codeData.managerMaintainLunaNoData,
      lunaSalesList: this.codeData.managerSaleslunaNoData,
      inflowPath: this.codeData.inventoryCompanyCodeData,
      memberNo: 0
    }
  },
  created () {
    this.memberNo = this.$route.params.member_no
    this.inchargeInfo = JSON.parse(JSON.stringify(this.member_incharge_info))
  },
  mounted () {
    // document.getElementsByClassName('v--modal-box v--modal')[0].setAttribute('style', 'top: 100px; left: 300px; width: 1300px; height: auto;')
    if (this.inchargeInfo === null) {
      this.addMemberAddressRow()
    }
  },
  methods: {
    close () {
      this.$emit('close')
    },
    modifyMemberInchargeInfo () {
      console.log('reqData = ' + JSON.stringify(this.inchargeInfo))
      this.isLoading = true
      http
        .post('/member/incharge/' + this.memberNo, JSON.stringify(this.inchargeInfo))
        .then(response => {
          this.isLoading = false
          console.log(JSON.stringify(response))
          if (response.data.responseCode === 0) {
            this.$modal.show(ModalAlert,
              {title: '영업/운영 담장자 정보 수정', text: '영업/운영 담당자 정보가 수정되었습니다.'},
              { width: 450, height: 'auto' }
            )
            this.inchargeInfo.manager_maintain_luna_name = this.lunaMaintainList[this.inchargeInfo.manager_maintain_luna_no]
            this.inchargeInfo.manager_sales_luna_name = this.lunaSalesList[this.inchargeInfo.manager_sales_luna_no]
            this.resultEditMemberInchargeToParent(this.inchargeInfo)
            this.$emit('close')
          } else {
            alert('영업/운영 정보 수정 중 오류가 발생하였습니다. \n 다시 시도해 주세요. \n errormsg : ' + response.data.responseMsg)
          }
        })
    },
    resultEditMemberInchargeToParent (result) {
      eventBus.$emit('refreshMembmerDetailInfo', result, 'incharge')
    }
  },
  props: [
    'member_incharge_info',
    'codeData'
  ]

}
</script>
