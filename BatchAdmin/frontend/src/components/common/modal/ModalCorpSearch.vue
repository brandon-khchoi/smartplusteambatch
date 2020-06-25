<template>
  <div name="modal-corp-search" class="ModalCorpSearch">
    <div class="vue-modal-box">
      <div class="dialog-content">
          <div class="titleWrap">
            <h1>법인/상호명 찾기</h1>
          </div>
          <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
          <div class="dialog-c-text">
              <div class="popupContentWrap">
                  <div class="popupContent">
                      <div class="searchBtnWrap" style="text-align:left; margin-left: 10px;">
                        <input type="text" v-model="searchTxt" @keyup.enter="get()">
                        <p class="btn_search" @click="get()">찾기</p>
                      </div>
                      <article style="overflow-y:scroll;height:500px">
                        <div class="articleWrap">
                          <div class="tableWrap tableList scrollWrap">
                            <table summary="법인/상호명 찾기">
                              <caption class="blind">법인/상호명 찾기</caption>
                              <colgroup>
                                <col width="100px"/>
                                <col width="150px"/>
                                <col width="100px"/>
                                <col width="70px" v-if="propIsDeliveryService === true"/>
                                <col width="70px"/>
                              </colgroup>
                              <thead>
                                <th>루나ID</th>
                                <th>법인명</th>
                                <th>상호명</th>
                                <th v-if="propIsDeliveryService === true">택배사</th>
                                <th>선택</th>
                              </thead>
                              <tbody>
                                <template v-if="list.length != 0">
                                  <tr v-for="item in list" :key="item.id" >
                                    <td class="text-center">{{item.member_id}}</td>
                                    <td class="text-center">{{item.biz_name}}</td>
                                    <td class="text-center">{{item.mall_name}}</td>
                                    <td class="text-center" v-if="propIsDeliveryService === true">
                                      <template v-if="item.delivery_infos.length > 0" >
                                        <select class="fullwidth" @change="selectChange(item)" >
                                            <template v-for="delivery in item.delivery_infos" >
                                              <option :value="delivery.delivery_company_no" :key="delivery.id">{{delivery.delivery_company_name}}</option>
                                            </template>
                                        </select>
                                      </template>
                                      <template v-else>
                                        <input class="btn_min" type="button" value="설정" @click="addMallDelivery(item)"/>
                                      </template>
                                    </td>
                                    <td class="text-center">
                                      <template v-if="propIsDeliveryService === true" >
                                        <input class="btn_min" type="button" value="선택" @click="setItem(item)"/>
                                        <!-- <template v-if="item.delivery_infos.length > 0" >
                                          <input class="btn_min" type="button" value="선택" @click="setItem(item)"/>
                                        </template>
                                        <template v-else >
                                          <input class="btn_min_disable" type="button" value="선택" disabled />
                                        </template> -->
                                      </template>
                                      <template v-else>
                                        <template v-if="item.mall_no > 0" >
                                          <input class="btn_min" type="button" value="선택" @click="setItem(item)"/>
                                        </template>
                                        <template v-else >
                                          <input class="btn_min_disable" type="button" value="선택" disabled />
                                        </template>
                                      </template>
                                    </td>
                                  </tr>
                                </template>
                                <tr v-else>
                                  <td colspan="5" class="emptyTable" v-if="propIsDeliveryService === true">
                                    해당하는 법인/상호명이 없습니다.
                                  </td>
                                  <td colspan="4" class="emptyTable" v-else>
                                    해당하는 법인/상호명이 없습니다.
                                  </td>
                                </tr>
                              </tbody>
                            </table>
                          </div>
                        </div>
                      </article>
                  </div>
              </div>
          </div>
      </div>
      <div class="dialog-button-wrapper">
        <button type="button" class="dialog-button" @click="$emit('close')">닫기</button>
      </div>
    </div>
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>
import ModalAlert from '@/components/common/modal/ModalAlert'
import http from '@/http-commons'
import axios from 'axios'
import { eventBus } from '@/main'

export default {
  data () {
    return {
      // eslint-disable-next-line no-undef
      searchTxt: nullStr(this.propSearchTxt, ''),
      userNo: this.$store.getters.getUser.luna_no,
      searchParam: {},
      list: [],
      isLoading: false,
      reloadHttp: axios.create({
        // eslint-disable-next-line no-undef
        baseURL: reloadApiUrl,
        headers: {
          'Content-type': 'application/json',
          Authorization: 'Api-Key N1ItieUV.a20YAZe3ekIOBKowWZdPsK5izg929ZQ0'
        }})
    }
  },
  created () {
    if (this.searchTxt !== '') {
      this.get()
    }
  },
  methods: {
    get () {
      if (!this.chkTxtLen()) return

      let form = new FormData()
      form.append('search_text', this.searchTxt)
      this.isLoading = true
      http
        // eslint-disable-next-line no-undef
        .post(apiServerUrl + '/serviceSetting/deliveryService/searchMall', form)
        .then(response => {
          this.isLoading = false
          // console.log(response)
          if (response.data.resultCode === 0) {
            this.list = response.data.search_malls
          } else {
            this.$modal.show(ModalAlert,
              { title: '', text: '시스템 오류.' }, { width: 450, height: 'auto' }
            )
          }
        })
        .catch(e => {
          // console.log(e)
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' },
            { width: 450, height: 'auto' }
          )
        })
        .finally(() => {
          this.isLoading = false
        })
    },
    chkTxtLen () {
      let len = this.searchTxt.length
      if (len > 1) {
        return true
      } else {
        this.$modal.show(ModalAlert,
          { title: '', text: '검색어를 2글자 이상 입력하세요.' },
          { width: 450, height: 'auto' }
        )
        return false
      }
    },
    setItem (item) {
      // console.log(item)
      if (this.propIsDeliveryService) {
        this.chooseDeliveryItem(item) // 서비스세팅용 팝업
      } else {
        this.chooseNormalItem(item) // 일반 회사검색 팝업
      }
    },
    chooseNormalItem (item) {
      this.searchParam = item
      eventBus.$emit('setCorpSearchInfo', this.searchParam)
      this.$emit('close')
    },
    chooseDeliveryItem (item) {
      if (item.selectedDelivertInfo !== 'undefined') { // onchange 안한 경우 첫번째 데이터가 object 로 세팅
        item.selectedDelivertInfo = item.delivery_infos[0]
      }
      this.searchParam = item // 검색 param 계속 넘겨야 함

      this.getCounselTalkStatus(item)
      this.getReloadStatus(item)
      if (item.selectedDelivertInfo === undefined) { // 택배사가 없는 경우
        const resultData = {
          openSetting: {
            service_stat_code: null
          }
        }
        eventBus.$emit('setCorpResultInfo', resultData) // 택배사 데이터가 없으므로 빈값을 전달
        eventBus.$emit('setCorpSearchInfo', this.searchParam)
        this.$emit('close')
        return
      }

      this.getDeliverySetting(item)
    },
    /**
     * 상담톡 상태값 확인 API
     */
    getCounselTalkStatus (item) {
      console.log(item)
      let url = '/servicesetting/counseltalk/status'
      http
        .get(url, {
          params: { // query string
            member_id: item.member_id,
            member_mall_no: item.member_mall_no
          }
        })
        .then(response => {
          console.log(response)
          eventBus.$emit('setCorpResultInfoCounselTalk', response.data.data)
          // 1
        })
        .catch(e => {
          console.log(e)
        })
    },
    /**
     * 리로드 상태값 확인 API
     */
    getReloadStatus (item) {
      let memId = item.member_id
      let shopId = item.member_mall_no
      // eslint-disable-next-line no-undef
      this.reloadHttp
        .get('/api/v1/shop/' + memId + '/' + shopId + '/status')
        .then(response => {
          console.log(response)
          eventBus.$emit('setCorpResultInfoReload', response.data.data)
        })
        .catch(e => {
          console.log(e)
        })
    },
    getDeliverySetting (item) {
      let form = new FormData()
      form.append('mall_id', item.mall_id)
      form.append('mall_no', item.mall_no)
      form.append('delivery_company_no', item.selectedDelivertInfo.delivery_company_no)
      form.append('user_code', 1)
      form.append('user_no', this.userNo)
      form.append('member_id', item.member_id)

      this.isLoading = true
      http
        // eslint-disable-next-line no-undef
        .post(apiServerUrl + '/serviceSetting/deliveryService/getDeliveryReturnSetting', form)
        .then(response => {
          this.isLoading = false
          // console.log(response)
          if (response.data.resultCode === 0) {
            eventBus.$emit('setCorpResultInfo', response.data)
            eventBus.$emit('setCorpSearchInfo', this.searchParam)
            this.$emit('close')
          } else if (response.data.resultCode === 4) {
            eventBus.$emit('setCorpResultInfo', response.data) // 택배사 초기데이터가 세팅되기 전이므로 빈값만 일단 넘기고, 링크 클릭시 생성
            eventBus.$emit('setCorpSearchInfo', this.searchParam)
            this.$emit('close')
          } else {
            this.$modal.show(ModalAlert,
              { title: '', text: '서버 시스템 오류' },
              { width: 450, height: 'auto' }
            )
          }
        })
        .catch(e => {
          // console.log(e)
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' },
            { width: 450, height: 'auto' }
          )
        })
        .finally(() => {
          this.isLoading = false
        })
    },
    selectChange (item) {
      // select box 개수만큼 반복
      for (let i = 0; i < item.delivery_infos.length; i++) {
        if (parseInt(event.target.value) === item.delivery_infos[i].delivery_company_no) {
          item.selectedDelivertInfo = item.delivery_infos[i] // 선택된 select box 에 해당되는 데이터 object를 item 에 set
          return
        }
      }
    },
    /**
     * 택배사 초기 설정 data insert
     */
    addMallDelivery (item) {
      this.isLoading = true
      let params = {}
      params.mall_id = item.mall_id
      params.delivery_company_no = 2
      params.user_code = 1
      params.user_no = this.userNo
      params.member_id = item.member_id
      http
        // eslint-disable-next-line no-undef
        .post(apiServerUrl + '/serviceSetting/deliveryService/addMallDelivery', JSON.stringify(params))
        .then(response => {
          this.isLoading = false
          // console.log(response)
          if (response.data.resultCode === 0) {
            this.get()
          } else {
            this.$modal.show(ModalAlert,
              { title: '', text: '서버 시스템 오류' },
              { width: 450, height: 'auto' }
            )
          }
        })
        .catch(e => {
          // console.log(e)
          this.$modal.show(ModalAlert,
            { title: '', text: '서버오류' },
            { width: 450, height: 'auto' }
          )
        })
        .finally(() => {
          this.isLoading = false
        })
    }
  },
  props: {
    'propSearchTxt': {
      type: String
    },
    'propIsDeliveryService': {
      type: Boolean,
      default: true
    }
  }
}
</script>
