<template>
  <div class="content service setting">
    <h1>서비스 관리</h1>
    <div class="formContainer">
      <div class="searchWrap">
        <table summary="서비스 세팅">
          <caption class="blind">서비스 세팅</caption>
          <colgroup>
            <col style="width:100px">
            <col style="width:300px">
            <col style="width:100px">
            <col style="width:10%">
            <col style="width:100px">
            <col style="width:10%">
            <col style="width:100px">
            <col>
          </colgroup>
          <tbody>
            <tr>
              <th>법인/상호명</th>
              <td>
                <div class="searchBtnWrap single">
                  <div @click ="showCorpSearch" style="display: inline-block; cursor: pointer">
                    <input
                      type="text"
                      placeholder="법인/상호명을 검색해주세요."
                      v-model="searchTxt"
                      disabled
                      style="pointer-events: none;width:100%"
                    >
                  </div>
                  <p class="btn_search" @click="showCorpSearch">찾기</p>
                </div>
              </td>
              <th colspan="6"></th>
            </tr>
          </tbody>
        </table>
      </div>
      <article class="searchResult">
        <div class="articleWrap">
          <div class="tableTopWrap"></div>
          <div class="tableWrap tableList tableGroup">
            <table summary="서비스세팅">
              <caption class="blind">서비스세팅</caption>
              <colgroup>
                <col style="width:20%">
                <col style="width:40%">
                <col style="width:40%">
              </colgroup>
              <thead>
                <tr>
                  <th>서비스</th>
                  <th>서비스 사용 상태</th>
                  <th>바로가기</th>
                </tr>
              </thead>
              <tbody>
                <template v-if="dataList.statusTxt">
                  <tr>
                    <td rowspan class="textCenter">{{dataList.title}}</td>
                    <td rowspan class="textCenter">{{dataList.statusTxt}}</td>
                    <td rowspan class="textCenter">
                      <a @click="clickLink(1)">택배회수 서비스 세팅</a>
                      <!-- <router-link :to="{ name: 'DeliveryDetail'}" >택배회수 서비스 세팅</router-link> -->
                      <!-- <router-link :to="{ name: 'DeliveryDetail', params: { propServerData: corpData }}" >택배회수 서비스 세팅</router-link> -->
                    </td>
                  </tr>
                  <tr>
                    <td rowspan class="textCenter">{{counselTalkData.title}}</td>
                    <td rowspan class="textCenter">{{counselTalkData.statusTxt}}</td>
                    <td rowspan class="textCenter">
                      <a @click="clickLink(2)">상담톡 서비스 세팅</a>
                      <!-- <router-link :to="{ name: 'DeliveryDetail'}" >택배회수 서비스 세팅</router-link> -->
                      <!-- <router-link :to="{ name: 'DeliveryDetail', params: { propServerData: corpData }}" >택배회수 서비스 세팅</router-link> -->
                    </td>
                  </tr>
                  <tr>
                    <td rowspan class="textCenter">{{reloadData.title}}</td>
                    <td rowspan class="textCenter">{{reloadData.statusTxt}}</td>
                    <td rowspan class="textCenter">
                      <a @click="clickLink(3)">리로드 서비스 세팅</a>
                    </td>
                  </tr>
                </template>
                <template v-else>
                  <tr>
                    <td class="textCenter" colspan="4">
                      <!-- <router-link :to="{ name: 'DeliveryDetail'}">택배회수 서비스 세팅</router-link> -->
                      검색 결과가 없습니다.
                    </td>
                  </tr>
                </template>
              </tbody>
            </table>
          </div>
        </div>
        <form name="frmPopup">
          <input type="hidden" name="domain" value=""/>
          <input type="hidden" name="admin_user" value=""/>
        </form>
      </article>
    </div>
  </div>
</template>

<script>
import { eventBus } from '@/main'
import ModalCorpSearch from '@/components/common/modal/ModalCorpSearch'
import ModalAlert from '@/components/common/modal/ModalAlert'
import { mapActions } from 'vuex'
import http from '@/http-commons'

export default {
  name: 'SettingList',
  data () {
    return {
      searchTxt: '',
      lunaId: this.$store.getters.getUser.luna_id,
      userNo: this.$store.getters.getUser.luna_no,
      dataList: {
        title: '택배회수',
        status: '',
        statusTxt: ''
      }, // [택배회수]
      counselTalkData: {
        title: '상담톡',
        status: '',
        statusTxt: ''
      },
      reloadData: {
        title: '리로드',
        status: '',
        statusTxt: ''
      },
      mallSearchInfo: {}, // 검색한 내용
      corpData: {} // 검색결과 [택배회수]
    }
  },
  mounted () {
    eventBus.$on('setCorpResultInfo', item => this.setCorpResultInfo(item)) // 검색결과
    eventBus.$on('setCorpResultInfoCounselTalk', item => this.setCorpResultInfoCounselTalk(item)) // 상담톡 상태 검색결과
    eventBus.$on('setCorpResultInfoReload', item => this.setCorpResultInfoReload(item)) // 리로드 상태 검색결과
    eventBus.$on('setCorpSearchInfo', item => this.setCorpSearchInfo(item)) // 검색내용
  },
  beforeDestroy () {
    eventBus.$off('setCorpResultInfo')
    eventBus.$off('setCorpResultInfoCounselTalk')
    eventBus.$off('setCorpResultInfoReload')
    eventBus.$off('setCorpSearchInfo')
  },
  methods: {
    ...mapActions(['routeProps']),
    showCounselPop () {
      console.log('showCounselPop')
      var frmPop = document.frmPopup
      var url = 'https://lunaplus.lunasoft.co.kr/lunachat/admin/open/page'
      window.open('', 'popupView', 'width=850,height=800,left=20%')

      frmPop.action = url
      frmPop.method = 'post'
      frmPop.target = 'popupView' // window,open()의 두번째 인수와 같아야 하며 필수다.
      frmPop.domain.value = this.mallSearchInfo.member_id
      frmPop.admin_user.value = this.lunaId
      frmPop.submit()
    },
    showCorpSearch () {
      // console.log('showCorpSearch')
      if (!this.userMenuAuth.retrieve_list) {
        return
      }

      this.$modal.show(
        ModalCorpSearch,
        {
          propSearchTxt: this.mallSearchInfo.biz_name
        },
        {
          width: 1200,
          height: 'auto',
          clickToClose: false,
          // draggable: true,
          scrollable: true
        }
      )
    },
    setCorpResultInfo (item) {
      console.log('setCorpResultInfo')
      console.log(item)
      this.corpData = item // props 로 넘길 데이터
      /** 응답데이터 정리 */
      if (!this.corpData.returnPlaceInfo.return_place_tel_sub_no) {
        this.corpData.returnPlaceInfo.return_place_tel_sub_no = ['', '', '']
      }
      if (!this.corpData.returnPlaceInfo.return_place_tel_no) {
        this.corpData.returnPlaceInfo.return_place_tel_no = ['', '', '']
      }
      /** 응답데이터 정리 끝 */
      this.dataList.status = item.openSetting.service_stat_code

      if (this.dataList.status === null) {
        // code 값 null 은 안넘어와서 추가
        this.dataList.statusTxt = '미사용'
        return
      }

      if (this.codeData.serviceStatCodeData[item.openSetting.service_stat_code] === undefined) {
        this.dataList.statusTxt = '미사용'
      } else {
        this.dataList.statusTxt = this.codeData.serviceStatCodeData[item.openSetting.service_stat_code]
      }
    },
    setCorpSearchInfo (item) {
      // console.log('setCorpSearchInfo')
      // console.log(item)
      this.mallSearchInfo = item
      this.searchTxt = this.mallSearchInfo.biz_name + '/' + this.mallSearchInfo.mall_name
    },
    setCorpResultInfoCounselTalk (item) {
      this.counselTalkData.status = item
      // 0 : 비활성, 1 : 사용중, -1 : 미사용
      if (this.counselTalkData.status === 1) {
        this.counselTalkData.statusTxt = '사용중'
      } else if (this.counselTalkData.status === 0) {
        this.counselTalkData.statusTxt = '비활성'
      } else {
        this.counselTalkData.statusTxt = '미사용'
      }
    },
    setCorpResultInfoReload (item) {
      // 1: 오픈예정, 2: 사용중, 3: 일시정지, 4:정지
      if (item.service_stat_code !== undefined) {
        this.reloadData.status = item.service_stat_code
        this.reloadData.statusTxt = item.service_stat_text
      } else {
        this.reloadData.statusTxt = '미사용'
      }
    },
    async clickLink (flag) {
      if (!this.userMenuAuth.retrieve_detail) {
        return
      }

      // console.log(this.mallSearchInfo)
      if (flag === 1) {
        if (this.corpData.resultCode === 4) { // 데이터를 정상적으로 받아오지 못한 경우에만 다시 데이터 가져옴!!!
          let context = this
          await this.setNewDeliveryItem(this.mallSearchInfo).then(async function (resolvedData) {
            await context.getDeliverySetting(context.mallSearchInfo)
          }).catch(function (err) {
            console.log('err', err)
          })
          // await Promise.all(promise)
        } else if (this.corpData.resultCode === undefined) {
          // 데이터 없는 경우 링크 이동 안함
          return
        }
        this.$router.push({ name: 'deliverydetail', params: {propSettingData: this.corpData, propSearchMallData: this.mallSearchInfo} })
      } else if (flag === 2) {
        this.showCounselPop()
      } else if (flag === 3) {
        this.$router.push({ name: 'reloaddetail', params: {propSettingData: this.corpData, propSearchMallData: this.mallSearchInfo} })
      }
    },
    async setNewDeliveryItem (item) {
      console.log('setNewDeliveryItem start')
      // const promises = []
      let form = new FormData()
      form.append('mall_id', item.mall_id)
      form.append('mall_no', item.mall_no)
      form.append('delivery_company_no', item.selectedDelivertInfo.delivery_company_no)
      form.append('user_code', 1)
      form.append('user_no', this.userNo)
      form.append('member_id', item.member_id)
      this.isLoading = true
      return new Promise((resolve, reject) =>
        http
        // eslint-disable-next-line no-undef
          .post(apiServerUrl + '/serviceSetting/deliveryService/setDeliveryReturnSetting', form)
          .then(response => {
            this.isLoading = false
            // console.log(response)
            if (response.data.resultCode === 0) {
              resolve()
            } else {
              this.$modal.show(ModalAlert,
                { title: '', text: '서버 시스템 오류' },
                { width: 450, height: 'auto' }
              )
              reject(response)
            }
          })
          .catch(e => {
          // console.log(e)
            this.$modal.show(ModalAlert,
              { title: '', text: '서버오류' },
              { width: 450, height: 'auto' }
            )
            reject(e)
          })
          .finally(() => {
            this.isLoading = false
          })
      )
      // console.log('setNewDeliveryItem end')
    },
    /**
     * 택배회수 데이터 가져오기
     */
    async getDeliverySetting (item) {
      console.log('getDeliverySetting start')
      let form = new FormData()
      form.append('mall_id', item.mall_id)
      form.append('mall_no', item.mall_no)
      form.append('delivery_company_no', item.selectedDelivertInfo.delivery_company_no)
      form.append('user_code', 1)
      form.append('user_no', this.userNo)
      form.append('member_id', item.member_id)
      return new Promise((resolve, reject) =>
        http
        // eslint-disable-next-line no-undef
          .post(apiServerUrl + '/serviceSetting/deliveryService/getDeliveryReturnSetting', form)
          .then(response => {
          // console.log(response)
            if (response.data.resultCode === 0) {
              console.log('getDeliverySetting response')
              this.corpData = response.data
              if (!this.corpData.returnPlaceInfo.return_place_tel_sub_no) {
                this.corpData.returnPlaceInfo.return_place_tel_sub_no = ['', '', '']
              }
              if (!this.corpData.returnPlaceInfo.return_place_tel_no) {
                this.corpData.returnPlaceInfo.return_place_tel_no = ['', '', '']
              }
              resolve()
            } else {
              this.$modal.show(ModalAlert,
                { title: '', text: '서버 시스템 오류' },
                { width: 450, height: 'auto' }
              )
              reject(response)
            }
          })
          .catch(e => {
          // console.log(e)
            this.$modal.show(ModalAlert,
              { title: '', text: '서버오류' },
              { width: 450, height: 'auto' }
            )
            reject(e)
          })
          .finally(() => {
          })
      )
    }
  },
  props: {
    codeData: Object,
    userMenuAuth: Object
  }
}
</script>
