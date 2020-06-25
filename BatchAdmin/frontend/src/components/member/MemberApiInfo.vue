<template>
  <li class="tab userDetail">
    <article>
      <div class="articleWrap">
        <div class="tableWrap">
          <table summary="API 정보">
            <caption class="blind">API 정보</caption>
            <colgroup>
                <col style="width:200px;"/>
                <col/>
            </colgroup>
            <tbody>
              <tr>
                <th style="border-left:0px;border-top:0px;border-bottom:0px;">상호명</th>
                <td style="border:0px;">
                  <select v-model="mall_id" style="width:300px" @change="changeMallInfo($event)" >
                    <template v-for="item in mallInfoList" >
                      <option :value="item.mall_id" :key="item.id" :data-mallno="item.mall_no" :data-hostingno="item.hosting_no" :data-mallname="item.mall_name"
                            :data-hostingmallid="item.hosting_mall_id" :data-hostingmallsubyn="item.hosting_mall_sub_yn" :data-hostingmallno="item.hosting_mall_no"
                            :data-membermallno="item.member_mall_no">
                          {{item.mall_name}}
                      </option>
                    </template>
                  </select>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    <div class="articleWrap">

      <div class="tableTopWrap">
          <p class="title">API 정보</p>
          <div class="btnWrap">
              <ul>
                  <li>
                      <template v-if="hosting_no === 1">
                        <p class="btn_min" @click="cafe24DataSyncBtn()" style="width:150px;"><i
                          class="fas fa-pen"></i>카페24 가입 동기화</p>
                      </template>
                      <template v-else>
                      <template v-if="mallApiInfoList.length > 0">
                      <p class="btn_min" @click="showEditModal()"><i
                          class="fas fa-pen"></i>수정</p>
                      </template>
                      <template v-else>
                        <p class="btn_min" @click="showEditModal()"><i
                          class="fas fa-pen"></i>등록</p>
                      </template>
                      </template>
                  </li>
              </ul>
          </div>
      </div>
      <div class="tableWrap simple">
        <table summary="API 정보">
            <caption class="blind">API 정보</caption>
            <colgroup>
                <col style="width:200px;"/>
                <col/>
            </colgroup>
            <tbody v-if="mallApiInfoList.length > 0">
                <tr>
                  <th>호스팅</th>
                  <td>
                    {{codeData.hostingNoData[mallApiInfoList[0].hosting_no]}}
                  </td>
                </tr>
                <tr>
                  <th>서비스</th>
                  <td>
                    <template v-for="(item) in mallApiInfoList">
                      {{codeData.serviceNoData[item.service_no]}}
                    </template>
                  </td>
                </tr>
                <tr>
                  <th>member_id</th>
                  <td>
                    {{mallApiInfoList[0].member_id}}
                  </td>
                </tr>
                <tr>
                  <th>mall_id</th>
                  <td>
                    {{mallApiInfoList[0].mall_id}}
                  </td>
                </tr>
                <tr>
                  <th>hosting_no</th>
                  <td>{{mallApiInfoList[0].hosting_no}}</td>
                </tr>
                <tr>
                  <th>hosting_mall_no</th>
                  <td>{{mallApiInfoList[0].hosting_mall_no}}</td>
                </tr>
                <tr>
                  <th>hosting_mall_id</th>
                  <td>{{mallApiInfoList[0].hosting_mall_id}}</td>
                </tr>
                <template v-if="hosting_no === 1">
                  <tr>
                    <th>access_token</th>
                    <td>
                      <template v-for="(item) in mallApiInfoList">
                        <template v-if="item.access_token">
                         {{item.access_token}}
                        </template>
                      </template>
                    </td>
                  </tr>
                </template>
                <template v-else>
                  <tr>
                    <th>hosting_url_addr</th>
                    <td>{{mallApiInfoList[0].hosting_url_addr}}</td>
                  </tr>
                  <tr>
                    <th rowspan="2">리로드 API KEY</th>
                    <td>
                      <template v-for="(item) in mallApiInfoList">
                        <template v-if="item.service_no === 9">
                          License KEY : {{item.license_key}}
                        </template>
                      </template>
                    </td>
                  </tr>
                  <tr>
                    <td>
                      <template v-for="(item) in mallApiInfoList">
                        <template v-if="item.service_no === 9">
                          API KEY : {{item.api_key}}
                        </template>
                      </template>
                    </td>
                  </tr>
                  <tr>
                    <th rowspan="2">카카오아이 API KEY</th>
                    <td>
                      <template v-for="(item) in mallApiInfoList">
                        <template v-if="item.service_no === 5">
                          License KEY : {{item.license_key}}
                        </template>
                      </template>

                    </td>
                  </tr>
                  <tr>
                    <td>
                      <template v-for="(item) in mallApiInfoList">
                        <template v-if="item.service_no === 5">
                          API KEY : {{item.api_key}}
                        </template>
                      </template>
                    </td>
                  </tr>
                </template>
            </tbody>
            <tbody v-else>
                <tr>
                  <td colspan="2">
                    조회된 API 정보가 없습니다.
                  </td>
                </tr>
            </tbody>
        </table>
      </div>
    </div>

    <div style="margin: 10px;">

      <div class="tableRegister">
        <div style="float:left;margin-top:10px;">
          카제고리 가져오기 유무 :
          <template v-if="category_yn === 1">
            Y
          </template>
          <template v-else>
            N
          </template>
        </div>
        <button type="button" style="width:120px;" class="btn_min" @click="setCategoryConfirm">카테고리 가져오기
        </button>
        <br>
      </div>
    </div>

    </article>
    <v-dialog :clickToClose=false />
    <Spinner v-if="isLoading" />
  </li>
</template>

<script>
import ModalAlert from '@/components/common/modal/ModalAlert'
import http from '@/http-commons'
import ModalApiInfoEdit from './memberDetailModal/ModalApiInfoEdit'
import { eventBus } from '../../main'

export default {
  name: 'MemberApiInfo',
  componests: {
  },
  data () {
    return {
      isLoading: false,
      mallApiInfoList: [], // mall_id 로 조회된 auth 테이블 정보
      mall_id: '',
      mall_no: 0,
      hosting_mall_id: '',
      hosting_no: 0,
      hosting_mall_no: 0,
      hosting_mall_sub_yn: 0,
      category_yn: 0 // 적재된 카테고리 개수
    }
  },
  created () {
    console.log('created')
    this.setFirstMallData() // 회원 변수 세팅
  },
  mounted () {
    console.log('mounted', this.mall_id)
    this.get(this.mall_id) // auth api 테이블 조회
    this.getHasCategoryInfo(this.mall_id) // 카테고리 조회
    eventBus.$on('mallAuthInfoDataUpdated', () => { // auth 정보 샘성 및 수정 후 재조회(카페24 X)
      this.get(this.mall_id)
    })
  },
  beforeDestroy () {
    eventBus.$off('mallAuthInfoDataUpdated')
  },
  methods: {
    get (mallId) {
      console.log('get', mallId)
      if (this.userMenuAuth.retrieve_list) {
        /** 카페24 */
        if (this.hosting_no === 1) {
          this.getCafe24()
        /** 일반 */
        } else {
          this.getCommon(mallId)
        }
      }
    },
    getCommon (mallId) {
      this.isLoading = true

      http
        .get('/member/apiinfo/list', {
          params: { // query string
            member_id: this.member_id,
            mall_id: mallId
          }
        })
        .then(response => {
          this.isLoading = false
          this.mallApiInfoList = response.data.data
        })
        .catch(e => {
          console.log(e)
        })
        .finally(() => {
          this.isLoading = false
        })
    },
    getCafe24 () {
      console.log('getCafe24')
      this.isLoading = true

      http
        .get('/member/apiinfo/cafe24list', {
          params: { // query string
            hosting_mall_no: this.hosting_mall_no,
            hosting_mall_id: this.hosting_mall_id
          }
        })
        .then(response => {
          this.isLoading = false
          this.mallApiInfoList = response.data.data
        })
        .catch(e => {
          console.log(e)
        })
        .finally(() => {
          this.isLoading = false
        })
    },
    /**
     * select box 용 초기 데이터 세팅
     */
    setFirstMallData () {
      console.log('setFirstMallData', this.mallInfoList)
      this.mall_id = this.mallInfoList[0].mall_id
      this.mall_no = this.mallInfoList[0].mall_no
      this.hosting_mall_id = this.mallInfoList[0].hosting_mall_id
      this.hosting_no = this.mallInfoList[0].hosting_no
      this.hosting_mall_no = this.mallInfoList[0].hosting_mall_no
      this.hosting_mall_sub_yn = this.mallInfoList[0].hosting_mall_sub_yn
      this.member_mall_no = this.mallInfoList[0].member_mall_no
    },
    changeMallInfo (event) {
      let mallId = event.target.value
      let mallNo = parseInt(event.target[event.target.selectedIndex].dataset.mallno)
      let hostingmallid = event.target[event.target.selectedIndex].dataset.hostingmallid
      let hostingno = parseInt(event.target[event.target.selectedIndex].dataset.hostingno)
      let hostingmallno = parseInt(event.target[event.target.selectedIndex].dataset.hostingmallno)
      let hostingmallsubyn = parseInt(event.target[event.target.selectedIndex].dataset.hostingmallsubyn)
      let membermallno = parseInt(event.target[event.target.selectedIndex].dataset.membermallno)
      this.mall_id = mallId
      this.mall_no = mallNo
      this.hosting_mall_id = hostingmallid
      this.hosting_no = hostingno
      this.hosting_mall_no = hostingmallno
      this.hosting_mall_sub_yn = hostingmallsubyn
      this.member_mall_no = membermallno

      this.get(mallId)
      this.getHasCategoryInfo(mallId)
    },
    showEditModal () {
      this.$modal.show(ModalApiInfoEdit,
        {
          mallApiInfoList: this.mallApiInfoList,
          userMenuAuth: this.userMenuAuth,
          mall_no: this.mall_no,
          codeData: this.codeData
        },
        { width: '50%', height: 'auto', clickToClose: false, scrollable: true }
      )
    },
    /**
     * 카테고리 개수 확인
     */
    getHasCategoryInfo (mallId) {
      if (this.userMenuAuth.retrieve_list) {
        http
          .get('/member/apiinfo/category/hasyn', {
            params: { // query string
              mall_id: mallId
            }
          })
          .then(response => {
            this.category_yn = response.data.data > 0 ? 1 : 0
          })
          .catch(e => {
            console.log(e)
          })
          .finally(() => {
          })
      }
    },
    setCategoryConfirm () {
      this.$modal.show('dialog', {
        text: '카테고리 가져오기 하시겠습니까?',
        buttons: [{
          title: '취소',
          handler: () => {
            this.$modal.hide('dialog')
          }
        },
        {
          title: '확인', // Button title
          default: true, // ENTER KEY
          handler: () => {
            this.setCategoryData()
            this.$modal.hide('dialog')
          }
        }]
      })
    },
    /**
     * GATEWAY API 에 카테고리 적재 카프카 쏘기
     */
    setCategoryData () {
      if (this.mallApiInfoList.length === 0) {
        this.$modal.show(ModalAlert,
          { title: '카테고리 가져오기', text: 'API 정보를 입력 후 설정바랍니다.' }
          , { width: 450, height: 'auto' }
        )
        return
      }
      let serviceNo = 0
      if (this.mallApiInfoList[0] === undefined) {
        /** 멀티몰 일 경우 */
        for (const item of this.serviceNoListByHostingMallId) {
          serviceNo = item
          break
        }
      } else {
        serviceNo = this.mallApiInfoList[0].service_no
      }

      let paramData = {
        'mallInfo': {
          'hosting_no': this.hosting_no,
          'hosting_mall_id': this.hosting_mall_id,
          'hosting_mall_no': this.hosting_mall_no,
          'member_mall_no': this.member_mall_no,
          'member_id': this.member_id
        },
        'serviceInfo': {
          'session_id': 'senddata_lunaadmin',
          'service_no': serviceNo
        }
      }
      this.isLoading = true
      // eslint-disable-next-line no-undef
      let url = gatewayApiUrl + '/gate/category/topic?topicName=category-topic'
      // JSON.stringify(params)
      http
        .post(url, {
          requiredParam: paramData
        })
        .then(response => {
          console.log(response)
          this.$modal.show(ModalAlert,
            { title: '카테고리 가져오기', text: '카테고리 정보를 가져오고 있습니다.\n잠시후에 조회 바랍니다.' }
            , { width: 450, height: 'auto' }
          )
        })
        .catch(e => {
          this.$modal.show(ModalAlert,
            { title: '카테고리 가져오기', text: 'GATEWAY 서버 오류' }
            , { width: 450, height: 'auto' }
          )
        })
        .finally(() => {
          this.isLoading = false
        })
    },
    cafe24DataSyncBtn () {
      this.$modal.show('dialog', {
        text: '카페24 가입 동기화를 하시겠습니까?',
        buttons: [{
          title: '취소',
          handler: () => {
            this.$modal.hide('dialog')
          }
        },
        {
          title: '확인', // Button title
          default: true, // ENTER KEY
          handler: () => {
            this.cafe24DataSync()
            this.$modal.hide('dialog')
          }
        }]
      })
    },
    cafe24DataSync () {
      if (this.mallApiInfoList.length === 0) {
        this.$modal.show(ModalAlert,
          { title: '카페24 가입 동기화', text: '등록된 API 정보가 없습니다.' }
          , { width: 450, height: 'auto' }
        )
        return
      }

      let formData = new FormData()
      formData.append('member_id', this.member_id)
      formData.append('mall_id', this.mall_id)
      formData.append('hosting_no', this.hosting_no)
      formData.append('hosting_mall_no', this.hosting_mall_no)
      formData.append('hosting_mall_id', this.hosting_mall_id)
      formData.append('hosting_mall_sub_yn', this.hosting_mall_sub_yn)
      this.isLoading = true
      http
        .post('/datasync/ladmin/cafe24join',
          formData,
          {
            headers: {
              apiKey: '617e3c05ff5debf757676b76a12e5caacb118ca27c2c8ecec15ab3e4600866a4'
            }
          }
        )
        .then(response => {
          console.log(response)
          if (response.data.rtnCode === '0000') {
            this.get(this.mall_id)
          }
          this.$modal.show(ModalAlert,
            { title: '카페24 가입 동기화', text: response.data.rtnMsg }
            , { width: 450, height: 'auto' }
          )
        })
        .catch(e => {
          console.log(e)
        })
        .finally(() => {
          this.isLoading = false
        })
    }

  },
  props: {
    'userMenuAuth': Object,
    'member_id': String,
    'mallInfoList': Array,
    'codeData': Object
  }
}
</script>
l
