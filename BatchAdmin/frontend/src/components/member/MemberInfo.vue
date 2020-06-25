<template>
  <div class="content member detail">
    <h1 class="textLeft">회원 관리 &gt; <a href="#" @click="goToList">회원 조회</a> &gt; 회원 상세 정보</h1>
      <div class="formContainer">
          <div class="userDetailWrap">
              <!-- member info area -->
              <div class="userInfo">
                <div class="basicWrap">
                  <div class="leftWrap">
                    <ul>
                      <li class="userId">{{member.member_id}}</li>
                      <li class="userCompany">{{member.member_biz_name}}</li>
                    </ul>
                  </div>
                  <div class="rightWrap">
                      <p>{{member.member_stat_name}}</p>
                  </div>
                  <div class="textBoth"></div>
                </div>

                  <!-- member business info area -->
                  <article>
                      <div class="articleWrap">
                          <div class="tableTopWrap">
                              <p class="title">사업자정보</p>
                              <div class="btnWrap">
                                  <ul>
                                      <li>
                                          <p class="btn_min" @click="openMemberInfoEditModal()">
                                              <i class="fas fa-pen"></i>수정
                                          </p>
                                      </li>
                                  </ul>
                              </div>
                          </div>
                          <div class="tableWrap simple">
                              <table summary="사업자정보">
                                  <caption class="blind">사업자정보</caption>
                                  <colgroup>
                                      <col>
                                      <col>
                                  </colgroup>
                                  <tbody>
                                      <tr>
                                          <th>사업자 등록 번호</th>
                                            <td @click="openCorpDocument(member.member_biz_no)">
                                              <a>
                                                <strong>
                                                  {{member.member_biz_no}}
                                                  <!-- {{member.member_biz_no1}}-{{member.member_biz_no2}}-{{member.member_biz_no3}} -->
                                                </strong>
                                              </a>
                                            </td>

                                      </tr>
                                      <tr>
                                          <th>사업자 등록증</th>
                                          <td>
                                              <div class="float-right" v-if="member.file_image_biz_card_name != ''">
                                                  <p class="btn_min" @click="openBizLicenseImg(member.member_id)">보기</p>
                                              </div>
                                          </td>
                                      </tr>
                                      <tr>
                                          <th>법인명</th>
                                          <td>{{member.member_biz_name}}</td>
                                      </tr>
                                      <tr>
                                          <th>상호명</th>
                                          <td>{{member.shop_name}}</td>
                                      </tr>
                                      <tr>
                                          <th>대표자명</th>
                                          <td>{{member.member_ceo_name}}</td>
                                      </tr>
                                  </tbody>
                              </table>
                          </div>
                      </div>
                      <!-- member business info area -->

                      <!-- member account info area -->
                      <div class="articleWrap">
                          <div class="tableTopWrap">
                              <p class="title">계정정보</p>
                          </div>
                          <div class="tableWrap simple">
                              <table summary="계정정보">
                                  <caption class="blind">계정정보</caption>
                                  <colgroup>
                                      <col>
                                      <col>
                                  </colgroup>
                                  <tbody>
                                      <tr>
                                          <th>가입일</th>
                                          <td>{{member.join_date}}</td>
                                      </tr>
                                      <tr>
                                          <th>회원구분</th>
                                          <td>{{member.member_type_name}}</td>
                                      </tr>
                                      <tr>
                                          <th>회원상태</th>
                                          <td>{{member.member_stat_name}}</td>
                                      </tr>
                                      <tr>
                                          <th>최근 로그인</th>
                                          <td>-</td>
                                      </tr>
                                  </tbody>
                              </table>
                          </div>
                      </div>

                  </article>
                  <!-- member account info area -->
                  <div class="btnWrap">
                    <ul>
                      <li><input class="btn_cancel" type="button" value="목록으로" @click="goToList"></li>
                    </ul>
                  </div>

              </div>

              <div class="tabContainer">
                  <ul class="tabBtnWrap">
                      <li class="tabBtn on" id="userDetail" @click="changeTab('detail')">상세정보<li>
                      <li class="tabBtn" id="userDetail" @click="changeTab('apiinfo')">API 정보</li>
                      <li class="tabBtn" id="userDetail" @click="changeTab('memo')">메모</li>
                  </ul>

                  <ul class="tabContents">
                    <MemberDetail v-if="showTabName === 'detail'"
                        :member_manager_list=member_manager_list
                        :member_address_list=member_address_list
                        :member_incharge_info=member_incharge_info
                        :member_bill_list=member_bill_list
                        :userMenuAuth=userMenuAuth
                        :codeData=codeData
                        >
                    </MemberDetail>

                    <!-- <MemberApiInfo v-if="showTabName === 'apiinfo'"
                      :userMenuAuth=userMenuAuth
                      :member_id=member.member_id
                      :first_mall_id=mallInfoList[0].mall_id
                      :first_mall_no=mallInfoList[0].mall_no
                      :mallInfoList=mallInfoList
                      :codeData=codeData
                    >
                    </MemberApiInfo> -->

                    <MemberApiInfo v-if="showTabName === 'apiinfo'"
                      :userMenuAuth=userMenuAuth
                      :member_id=member.member_id
                      :mallInfoList=mallInfoList
                      :codeData=codeData
                    >
                    </MemberApiInfo>

                    <MemberMemo v-if="showTabName === 'memo'"
                      :userMenuAuth=userMenuAuth
                      :member_biz_name=memberBizName
                      :member_no=memberNo
                    >
                    </MemberMemo>

                  </ul>
              </div>
          </div>
          <!-- member info area end -->

      </div>

  </div>
    <!-- </div> -->
</template>

<script>
import http from '@/http-commons'

import MemberDetail from '@/components/member/MemberDetail'
import MemberMemo from '@/components/member/MemberMemo'
import MemberInfoModal from '@/components/member/memberDetailModal/MemberInfoModal'
import MemberApiInfo from '@/components/member/MemberApiInfo'
import ModalAlert from '@/components/common/modal/ModalAlert'

import { eventBus } from '../../main'

export default {
  name: 'MemberInfo',
  components: {
    MemberDetail,
    MemberMemo,
    MemberInfoModal,
    MemberApiInfo
  },
  data () {
    return {
      member: {},
      mallInfoList: [],
      member_manager_list: '',
      member_address_list: '',
      member_incharge_info: '',
      member_bill_list: '',
      email_list: [],
      showTabName: 'detail',
      memberNo: 0,
      memberBizName: ''
    }
  },
  created () {
    this.memberNo = this.$route.params.member_no
    this.getMemberDetail(this.memberNo)
  },
  mounted () {
    // document.getElementById('content_main').removeAttribute('class')
    eventBus.$on('resetMemberInfo', (result) =>
      this.setMemberInfoData(result)
    )
  },
  methods: {
    getMemberDetail (memberNo) {
      http
        .get('/member/' + memberNo)
        .then(response => {
          this.member = response.data.memberInfo
          this.mallInfoList = response.data.mallInfoList
          this.memberBizName = response.data.memberInfo.member_biz_name
          this.member_manager_list = response.data.memberManagerList
          this.member_address_list = response.data.memberAddressList
          this.member_incharge_info = response.data.memberInfo.memberIncharge
          this.member_bill_list = response.data.memberBillList
        })
    },
    openMemberInfoEditModal () {
      this.$modal.show(MemberInfoModal,
        {
          codeData: this.codeData,
          member: this.member
        },
        {
          width: 850,
          height: 'auto',
          scrollable: true,
          clickToClose: false
        },
        { width: 500, height: 'auto' }
      )
    },
    openCorpDocument (bizNo) {
      let no = bizNo.split('-').join('')
      window.open('http://www.ftc.go.kr/bizCommPop.do?wrkr_no=' + no, '_blank', 'toolbar=no,scrollbars=no,resizable=yes,status=no,location=no,top=50,left=2250,width=1200,height=900')
    },
    openBizLicenseImg (memberId) {
      let image = new Image()
      image.src = 'http://v1.lunasoft.co.kr/etc/license_file/' + memberId + '.gif'
      let self = this
      image.onload = function () {
        let width = this.width > 500 ? '600px' : '500px'
        self.$modal.show(ModalAlert,
          {title: '사업자 등록증', imgSrc: image.src},
          { width: width, height: 'auto', scrollable: true }
        )
      }
      image.onerror = function () {
        self.$modal.show(ModalAlert,
          {title: '사업자 등록증', text: '이미지가 없습니다.'},
          { width: '500px', height: 'auto', scrollable: true }
        )
      }
    },
    setMemberInfoData (editedMembmerInfo) {
      this.member = editedMembmerInfo
    },
    changeTab (tabName) {
      this.showTabName = tabName
    },
    goToList () {
      console.log(this.$route.searchFlag)
      this.$router.push(
        {
          name: 'userlist',
          query: {
            searchFlag: this.$route.query.searchFlag,
            current_page: this.$route.query.current_page,
            hostingNo: this.$route.query.hostingNo,
            inventoryCompanyCode: this.$route.query.inventoryCompanyCode,
            managerMaintainLunaNo: this.$route.query.managerMaintainLunaNo,
            managerSalesLunaNo: this.$route.query.managerSalesLunaNo,
            memberStatCode: this.$route.query.memberStatCode,
            memberTypeCode: this.$route.query.memberTypeCode,
            per_page: this.$route.query.per_page,
            searchTxt: this.$route.query.searchTxt,
            searhFormDate: this.$route.query.searhFormDate,
            searhToDate: this.$route.query.searhToDate,
            isFilterFormShow: this.$route.query.isFilterFormShow
          }
        }
      )
    }
  },
  props: [
    'userMenuAuth',
    'codeData'
  ]
}
</script>
