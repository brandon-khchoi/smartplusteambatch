<template>
  <li class="tab userDetail">
      <article>
          <div class="articleWrap">
              <div class="tableTopWrap">
                  <p class="title">담당자 정보</p>
                  <div class="btnWrap">
                      <ul>
                          <li>
                              <p class="btn_min" @click="showEditModal('manager')"><i
                                  class="fas fa-pen"></i>수정</p>
                          </li>
                      </ul>
                  </div>
              </div>
              <div class="tableWrap simple">
                  <table class="textCenter" summary="담당자 정보">
                      <caption class="blind">담당자 정보</caption>
                      <thead>
                          <tr>
                              <th>담당자명</th>
                              <th>연락처</th>
                              <th>이메일</th>
                              <th>비고</th>
                          </tr>
                      </thead>
                      <tbody>
                          <template v-for="(item,key) in member_manager_list">
                              <tr :key="key" v-if="item.del_yn == 0">
                                  <td>{{item.member_manager_name}}</td>
                                  <td>{{item.member_manager_tel_no}}</td>
                                  <td>{{item.member_manager_email_addr}}</td>
                                  <td>{{item.etc_text}}</td>
                              </tr>
                          </template>
                      </tbody>
                  </table>
              </div>
          </div>
          <div class="articleWrap">
              <div class="tableTopWrap">
                  <p class="title">영업장 정보</p>
                  <div class="btnWrap">
                      <ul>
                          <li>
                              <p class="btn_min" @click="showEditModal('address')"><i
                                  class="fas fa-pen"></i>수정</p>
                          </li>
                      </ul>
                  </div>
              </div>
              <div class="tableWrap simple">
                  <table class="textCenter" summary="영업장 정보">
                      <caption class="blind">영업장 정보</caption>
                      <colgroup>
                          <col width="*"/>
                          <col width="*"/>
                          <col width="50%"/>
                      </colgroup>
                      <thead>
                      <tr style="border-bottom: 1px solid #e3e5e8;">
                          <th>영업장</th>
                          <th>대표전화</th>
                          <th>주소</th>
                      </tr>
                      </thead>
                      <tbody>
                          <template v-for="(item,key) in member_address_list">
                              <!-- <tr :key="key"> -->
                                <tr :key="key" v-if="item.del_yn == 0">
                                  <td>{{item.member_addr_name}}</td>
                                  <td>{{item.member_addr_tel_no}}</td>
                                  <td>[{{item.member_zipcode_no}}] {{item.member_addr}} {{item.member_detail_addr}}</td>
                              </tr>
                          </template>
                      </tbody>
                  </table>
              </div>
          </div>
          <div class="articleWrap">
              <div class="tableTopWrap">
                  <p class="title">영업/운영 담당자 정보</p>
                  <div class="btnWrap">
                      <ul>
                          <li>
                              <p class="btn_min" @click="showEditModal('incharge')"><i
                                  class="fas fa-pen"></i>수정</p>
                          </li>
                      </ul>
                  </div>
              </div>
              <div class="tableWrap simple">
                  <table class="textCenter" summary="영업/운영 담당자 정보">
                      <caption class="blind">영업/운영 담당자 정보</caption>
                      <colgroup>
                          <col width="20%"/>
                          <col width="*"/>
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
                          <template v-text="member_incharge_info">
                              <tr>
                                  <td>{{member_incharge_info.manager_sales_luna_name}}</td>
                                  <td>{{member_incharge_info.manager_maintain_luna_name}}</td>
                                  <td>{{member_incharge_info.member_join_path_name}}</td>
                                  <td>-</td>
                              </tr>
                          </template>
                      </tbody>
                  </table>
              </div>
          </div>
            <div class="articleWrap">
              <div class="tableTopWrap">
                  <p class="title">세금계산서 수신정보</p>
                  <div class="btnWrap">
                      <ul>
                          <li>
                              <p class="btn_min" @click="showEditModal('bill')"><i
                                  class="fas fa-pen"></i>수정</p>
                          </li>
                      </ul>
                  </div>
              </div>
              <div class="tableWrap simple">
                  <table class="textCenter" summary="세금계산서 수신정보">
                      <caption class="blind">세금계산서 수신정보</caption>
                      <colgroup>
                          <col width="50%"/>
                          <col width="*"/>
                      </colgroup>
                      <thead>
                      <tr>
                          <th>수신자</th>
                          <th>이메일</th>
                      </tr>
                      </thead>
                      <tbody>
                          <template v-for="(item, key) in member_bill_list">
                              <tr :key="key" v-if="item.del_yn == 0">
                                  <td>{{item.member_bill_name}}</td>
                                  <td>{{item.member_bill_email_addr}}</td>
                              </tr>
                          </template>
                      </tbody>
                  </table>
              </div>
          </div>
      </article>
    </li>
</template>

<script>
import MemberManagerModal from '@/components/member/memberDetailModal/MemberManagerModal'
import MemberAddressModal from '@/components/member/memberDetailModal/MemberAddressModal'
import MemberInchargeModal from '@/components/member/memberDetailModal/MemberInChargeModal'
import MemberBillModal from '@/components/member/memberDetailModal/MemberBillModal'
import { eventBus } from '../../main'

export default {
  name: 'MemberDetail',
  componests: {
    MemberManagerModal,
    MemberAddressModal,
    MemberInchargeModal,
    MemberBillModal
  },
  mounted () {
    eventBus.$on('refreshMembmerDetailInfo', (result, type) =>
      this.setMemberDeailInfo(result, type)
    )
  },
  methods: {
    showEditModal (requestName) {
      if (requestName === 'manager') {
        this.$modal.show(MemberManagerModal, {
          member_manager_list: this.member_manager_list
        },
        {
          width: 900,
          height: 'auto',
          scrollable: true,
          clickToClose: false
        })
      } else if (requestName === 'address') {
        this.$modal.show(MemberAddressModal, {
          member_address_list: Object.assign({}, this.member_address_list),
          userMenuAuth: this.userMenuAuth
        },
        {
          width: 1100,
          height: 'auto',
          scrollable: true,
          clickToClose: false
        })
      } else if (requestName === 'incharge') {
        this.$modal.show(MemberInchargeModal, {
          member_incharge_info: this.member_incharge_info,
          codeData: this.codeData
        },
        {
          width: 1000,
          height: 'auto',
          scrollable: true,
          clickToClose: false
        })
      } else if (requestName === 'bill') {
        this.$modal.show(MemberBillModal, {
          member_bill_list: this.member_bill_list
        },
        {
          width: 1000,
          height: 'auto',
          scrollable: true,
          clickToClose: false
        })
      }
    },
    setAddress (result) {
      alert('너뭐임 : ' + JSON.stringify(result))
    },
    setMemberDeailInfo (editedResult, type) {
      switch (type) {
        case 'manager':
          this.member_manager_list = editedResult
          break
        case 'address':
          this.member_address_list = editedResult
          break
        case 'incharge':
          console.log(JSON.stringify(editedResult))
          this.member_incharge_info = editedResult
          break
        case 'bill':
          this.member_bill_list = editedResult
          break
      }
    }
  },
  props: [
    'member_manager_list',
    'member_address_list',
    'member_incharge_info',
    'member_bill_list',
    'userMenuAuth',
    'codeData'
  ]
}
</script>
l
