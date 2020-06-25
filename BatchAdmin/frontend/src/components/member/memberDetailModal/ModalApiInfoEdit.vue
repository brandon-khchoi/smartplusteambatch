<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">
          <template v-if="mallApiInfoList.length > 0">
          API 정보 수정
          </template>
          <template v-else>
            API 정보 등록
          </template>
        </div>
        <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
      </div>
      <div class="dialog-c-text">
        <article>
          <div class="tableWrap tableList">
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
                      <th>서비스<span class="necessary">*</span></th>
                      <td>
                        <span class="inputWrap">
                          <input type="checkbox" id="service00-01" :value=5 v-model="useServiceArr" >
                          <label for="service00-01">
                            {{codeData.serviceNoData[5]}}
                          </label>
                        </span>
                        <span class="inputWrap">
                          <input type="checkbox" id="service00-02" :value=9 v-model="useServiceArr" >
                          <label for="service00-02">
                            {{codeData.serviceNoData[9]}}
                          </label>
                        </span>
                      </td>
                    </tr>
                    <tr>
                      <th>member_id</th>
                      <td>
                        {{mallApiInfoList[0].member_id}}
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
                    <template v-if="createMallData.hosting_no !== 1">
                    <tr>
                      <th>hosting_url_addr</th>
                      <td>{{mallApiInfoList[0].hosting_url_addr}}</td>
                    </tr>
                    <tr>
                      <th rowspan="2">API KEY<span class="necessary">*</span></th>
                      <td>
                        License KEY : {{license_key}}

                      </td>
                    </tr>
                    <tr>
                      <td>
                        API KEY :
                        <input type="text" v-model="api_key" style="width: 80%;" @input="typing($event)"/>
                        <p v-if="isValidation" style="color:red;">
                          *숫자, 영문, 특문만 입력 할 수 있습니다.
                        </p>
                      </td>
                    </tr>
                    </template>
                </tbody>
                <!-- API 정보가 없어서 신규 등록 해야할 때 -->
                <tbody v-else>
                    <tr>
                      <th>호스팅</th>
                      <td>
                        {{codeData.hostingNoData[createMallData.hosting_no]}}
                      </td>
                    </tr>
                    <tr>
                      <th>서비스<span class="necessary">*</span></th>
                      <td>
                        <span class="inputWrap">
                          <input type="checkbox" id="service00-01" :value=5 v-model="useServiceArr" >
                          <label for="service00-01">
                            {{codeData.serviceNoData[5]}}
                          </label>
                        </span>
                        <span class="inputWrap">
                          <input type="checkbox" id="service00-02" :value=9 v-model="useServiceArr" >
                          <label for="service00-02">
                            {{codeData.serviceNoData[9]}}
                          </label>
                        </span>
                      </td>
                    </tr>
                    <tr>
                      <th>member_id</th>
                      <td>
                        {{createMallData.member_id}}
                      </td>
                    </tr>
                    <tr>
                      <th>hosting_no</th>
                      <td>{{createMallData.hosting_no}}</td>
                    </tr>
                    <tr>
                      <th>hosting_mall_no</th>
                      <td>{{createMallData.hosting_mall_no}}</td>
                    </tr>
                    <tr>
                      <th>hosting_mall_id</th>
                      <td>{{createMallData.hosting_mall_id}}</td>
                    </tr>
                    <template v-if="createMallData.hosting_no !== 1">
                    <tr>
                      <th>hosting_url_addr</th>
                      <td>
                        <input type="text" v-model="createMallData.hosting_url_addr" style="width: 100%;"/>
                      </td>
                    </tr>
                    <tr>
                      <th rowspan="2">API KEY<span class="necessary">*</span></th>
                      <td>
                        LICENSE KEY : {{license_key}}
                      </td>
                    </tr>
                    <tr>
                      <td>
                        API KEY :
                        <input type="text" v-model="api_key" style="width: 80%;" @input="typing($event)"/>
                        <p v-if="isValidation" style="color:red;">
                          * 숫자, 영문, 특문만 입력 할 수 있습니다.
                        </p>
                      </td>
                    </tr>
                    </template>
                </tbody>
            </table>
          </div>
          <div class="formContainer">
            <article>
            </article>

            <div class="btnWrap">
              <template v-if="createMallData.hosting_no !== 1">
              <ul>
                <li><input class="btn_cancel" type="button" value="취소" @click="$emit('close')"></li>
                <template v-if="mallApiInfoList.length > 0">
                  <!-- 최초 등록 -->
                  <li><input class="btn_save" type="button" value="적용" @click="save()"></li>
                </template>
                <template v-else>
                  <!-- 수정 -->
                  <li><input class="btn_save" type="button" value="적용" @click="firstSave()"></li>
                </template>
              </ul>
              </template>
            </div>

          </div>
        </article>
      </div>
    </div>
  </div>
</template>

<script>
import http from '@/http-commons'
import ModalAlert from '@/components/common/modal/ModalAlert'
import { eventBus } from '../../../main'

export default {
  name: 'ModalApiInfoEdit',
  components: {
  },
  data () {
    return {
      useServiceArr: [], // 사용중인 서비스 리스트
      initUseServiceArr: [],
      api_key: '', // 메이크샵
      license_key: '', // 메이크샵
      access_tocken: '',
      createMallData: {
        hosting_mall_id: '',
        hosting_mall_sub_yn: '',
        hosting_mall_no: 0,
        hosting_no: 0,
        mall_id: '',
        mall_name: '',
        mall_no: 0,
        hosting_url_addr: '',
        member_id: '',
        member_mall_no: 0
      }, // API 신규 추가시 사용
      isValidation: false
    }
  },
  props: {
    'mallApiInfoList': Array,
    'userMenuAuth': Object,
    'mall_no': Number,
    'codeData': Object
  },
  mounted () {
    this.setInitData()
    this.getCreateMallData()
  },
  methods: {
    closeModal () {
      this.$emit('close')
    },
    setInitData () {
      this.useServiceArr = []
      for (const item of this.mallApiInfoList) {
        if (item.service_no) {
          if (item.service_no === 5) {
            this.useServiceArr.push(5)
          } else if (item.service_no === 9) {
            this.useServiceArr.push(9)
          }
        }
        if (item.api_key) {
          this.api_key = item.api_key
          this.license_key = item.license_key
        }
      }

      this.initUseServiceArr = JSON.parse(JSON.stringify(this.useServiceArr))
    },
    /**
     * 신규 등록을 위한 데이터 가져오기
     */
    getCreateMallData () {
      console.log('getCreateMallData', this.mall_no)
      if (this.userMenuAuth.retrieve_list) {
        http
          .get('/member/mall/list', {
            params: { // query string
              mall_no: this.mall_no
            }
          })
          .then(response => {
            console.log(response)
            this.createMallData = response.data.data
            if (this.mallApiInfoList.length === 0) {
              if (this.createMallData.hosting_no === 2) {
                this.license_key = 'OTRhNDFkOTE2NWNhY2UyYzA2YWNiZmU0YmUzNmExYzQ='
              } else if (this.createMallData.hosting_no === 3) {
                this.license_key = 'lunasoft'
              } else if (this.createMallData.hosting_no === 12) {
                this.license_key = 'JThGJTVCTSVGMU8lMDc2JUY5'
              }
            }
            // hosting_mall_id 가 없으면 mall_id 로 대체
            if (!this.createMallData.hosting_mall_id) {
              this.createMallData.hosting_mall_id = this.createMallData.mall_id
            }
            if (!this.createMallData.hosting_url_addr.includes('http')) {
              this.createMallData.hosting_url_addr = 'http://' + this.createMallData.hosting_url_addr
            }
          })
          .catch(e => {
            console.log(e)
          })
          .finally(() => {
            this.isLoading = false
          })
      }
    },
    save () {
      // 수정일 경우
      if (this.isValidationCheck()) {
        return
      }
      this.updateAll()
    },
    /**
     * 최초등록일 경우
     */
    firstSave () {
      if (this.useServiceArr.indexOf(5) > -1 && this.initUseServiceArr.indexOf(5) === -1 && this.useServiceArr.indexOf(9) > -1 && this.initUseServiceArr.indexOf(9) === -1) {
        this.create([5, 9])
      } else if (this.useServiceArr.indexOf(5) > -1 && this.initUseServiceArr.indexOf(5) === -1) {
        this.create([5])
      } else if (this.useServiceArr.indexOf(9) > -1 && this.initUseServiceArr.indexOf(9) === -1) {
        this.create([9])
      } else {
        this.$modal.show(ModalAlert,
          { title: 'API 정보 등록', text: '사용할 서비스를 체크해주세요.' }
          , { width: 450, height: 'auto' }
        )
      }
    },
    /**
     * 신규로 등록할때만 사용
     */
    create (serviceNoArr) {
      console.log('create', serviceNoArr)
      if (this.isValidationCheck()) {
        return
      }
      let form = new FormData()

      form.append('member_id', this.createMallData.member_id)
      form.append('mall_id', this.createMallData.mall_id)
      form.append('hosting_no', this.createMallData.hosting_no)
      form.append('hosting_mall_no', this.createMallData.hosting_mall_no)
      form.append('hosting_mall_id', this.createMallData.hosting_mall_id)
      if (serviceNoArr) {
        form.append('service_no_arr', serviceNoArr)
      } else {
        this.$modal.show(ModalAlert,
          { title: 'API 정보 등록', text: '사용할 서비스를 체크해주세요.' }
          , { width: 450, height: 'auto' }
        )
        return
      }

      if (this.createMallData.hosting_url_addr) {
        form.append('hosting_url_addr', this.createMallData.hosting_url_addr)
      } else {
        this.$modal.show(ModalAlert,
          { title: 'API 정보 등록', text: 'hosting_url_addr 를 입력해주세요.' }
          , { width: 450, height: 'auto' }
        )
        return
      }

      if (this.license_key) {
        form.append('license_key', this.license_key)
      } else {
        this.$modal.show(ModalAlert,
          { title: 'API 정보 등록', text: 'LICENSE KEY 를 입력해주세요.' }
          , { width: 450, height: 'auto' }
        )
        return
      }
      if (this.api_key) {
        form.append('api_key', this.api_key)
      } else {
        this.$modal.show(ModalAlert,
          { title: 'API 정보 등록', text: 'API KEY 를 입력해주세요.' }
          , { width: 450, height: 'auto' }
        )
        return
      }

      http
        .post('/member/apiinfo/create', form)
        .then(response => {
          console.log(response)
          this.$modal.show(ModalAlert, {
            title: 'API 정보 등록',
            text: 'API 정보를 등록하였습니다.'
          }, {
            width: 450,
            height: 'auto'
          })
          eventBus.$emit('mallAuthInfoDataUpdated')
          this.closeModal()
        })
        .catch(e => {
          console.log(e)
        })
    },
    updateAll () {
      let url = '/member/apiinfo/update'
      let params = {}
      let createMallInfoList = []

      if (this.api_key) {

      } else {
        this.$modal.show(ModalAlert,
          { title: 'API 정보 등록', text: 'API KEY 를 입력해주세요.' }
          , { width: 450, height: 'auto' }
        )
        return
      }

      /** 삭제 */
      for (const mallItem of this.mallApiInfoList) {
        mallItem.api_key = this.api_key
        mallItem.license_key = this.license_key
        // if (serviceNo === mallItem.service_no) {
        // 켜져있다가 현재 꺼진 경우
        if (mallItem.service_no === 5) {
          if (this.useServiceArr.indexOf(5) === -1 && this.initUseServiceArr.indexOf(5) > -1) {
            // 삭제
            mallItem.del_yn = 1
          }
        } else if (mallItem.service_no === 9) {
          // 켜져있다가 현재 꺼진 경우
          if (this.useServiceArr.indexOf(9) === -1 && this.initUseServiceArr.indexOf(9) > -1) {
            // 삭제
            mallItem.del_yn = 1
          }
        }
      }

      /** 등록 */
      if (this.initUseServiceArr.indexOf(5) === -1 && this.useServiceArr.indexOf(5) > -1) {
        this.createMallData.service_no = 5
        if (this.api_key) {
          this.createMallData.api_key = this.api_key
        }
        if (this.license_key) this.createMallData.license_key = this.license_key
        createMallInfoList.push(this.createMallData)
      }
      if (this.initUseServiceArr.indexOf(9) === -1 && this.useServiceArr.indexOf(9) > -1) {
        this.createMallData.service_no = 9
        if (this.api_key) {
          this.createMallData.api_key = this.api_key
        }
        if (this.license_key) this.createMallData.license_key = this.license_key
        createMallInfoList.push(this.createMallData)
      }

      for (const createItem of createMallInfoList) {
        this.mallApiInfoList.push(createItem)
      }
      params.item = this.mallApiInfoList

      http
        .post(url, JSON.stringify(params))
        .then(response => {
          console.log(response)
          this.$modal.show(ModalAlert, {
            title: 'API 정보 수정',
            text: 'API 정보를 수정하였습니다.'
          }, {
            width: 450,
            height: 'auto'
          })
          eventBus.$emit('mallAuthInfoDataUpdated')
          this.closeModal()
        })
        .catch(e => {
          console.log(e)
          eventBus.$emit('mallAuthInfoDataUpdated')
          this.closeModal()
        })
        .finally(() => {
        })
    },
    typing (evt) {
      let message = evt.target.value.trim()
      let patternKor = /[ㄱ-ㅎ가-힣]/g // 한글이 char 하나라도 있으면
      console.log(message)
      if (patternKor.test(message) === true) {
        this.isValidation = true
      } else {
        this.isValidation = false
      }
    },
    isValidationCheck () {
      if (this.isValidation) {
        this.$modal.show(ModalAlert,
          { title: 'API 정보 등록', text: '* 숫자, 영문, 특문만 입력 할 수 있습니다.' }
          , { width: 450, height: 'auto' }
        )
        return true
      } else {
        return false
      }
    }
  }
}
</script>
